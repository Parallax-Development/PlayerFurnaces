package dev.darkblade.playerfurnaces.importer.impl;

import dev.darkblade.playerfurnaces.importer.ImportResult;
import dev.darkblade.playerfurnaces.importer.RecipeImporter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

public class CraftorithmRecipeImporter implements RecipeImporter {

    @Override
    public String getPluginName() {
        return "craftorithm";
    }

    @Override
    public boolean isAvailable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Craftorithm");
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public ImportResult importRecipes(File baseRecipesDir, boolean overwrite) {
        if (!isAvailable()) {
            return new ImportResult("Craftorithm is not installed or enabled on this server.");
        }

        File targetDir = new File(baseRecipesDir, "craftorithm");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        int importedCount = 0;
        int skippedCount = 0;

        List<RecipeInfo> extractedRecipes = collectRecipes();

        for (RecipeInfo info : extractedRecipes) {
            String fileName = sanitizeFileName(info.id) + ".yml";
            File recipeFile = new File(targetDir, fileName);

            if (recipeFile.exists() && !overwrite) {
                skippedCount++;
                continue;
            }

            YamlConfiguration config = new YamlConfiguration();
            String rootKey = "craftorithm_" + sanitizeKey(info.id);

            // Input section
            config.set(rootKey + ".input.material", info.inputMaterial);
            if (info.inputId != null && !info.inputId.isEmpty()) {
                config.set(rootKey + ".input.id", "craftorithm:" + info.inputId);
            }
            if (info.inputName != null && !info.inputName.isEmpty()) {
                config.set(rootKey + ".input.name", info.inputName);
            }
            if (info.inputCustomModelData != null) {
                config.set(rootKey + ".input.custom-model-data", info.inputCustomModelData);
            }

            // Result section
            if (info.resultId != null && !info.resultId.isEmpty()) {
                config.set(rootKey + ".result.id", "craftorithm:" + info.resultId);
            } else {
                config.set(rootKey + ".result.material", info.resultMaterial);
            }
            config.set(rootKey + ".result.amount", info.resultAmount);
            if (info.resultName != null && !info.resultName.isEmpty()) {
                config.set(rootKey + ".result.name", info.resultName);
            }
            if (info.resultCustomModelData != null) {
                config.set(rootKey + ".result.custom-model-data", info.resultCustomModelData);
            }

            // Cooking settings
            config.set(rootKey + ".cook-time-ticks", info.cookTimeTicks);
            config.set(rootKey + ".experience", info.experience);

            try {
                config.save(recipeFile);
                importedCount++;
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[PlayerFurnaces] Failed to save imported recipe file: " + recipeFile.getName(), e);
            }
        }

        return new ImportResult(importedCount, skippedCount);
    }

    private List<RecipeInfo> collectRecipes() {
        List<RecipeInfo> list = new ArrayList<>();
        Set<String> processedKeys = new HashSet<>();

        // 1. Try Craftorithm API Reflection first
        try {
            Class<?> apiClass = Class.forName("cc.sy.craftorithm.api.CraftorithmAPI");
            Method getRecipesMethod = null;
            for (Method m : apiClass.getMethods()) {
                if (m.getName().toLowerCase(Locale.ROOT).contains("recipe")) {
                    getRecipesMethod = m;
                    break;
                }
            }
            if (getRecipesMethod != null) {
                Object result = getRecipesMethod.invoke(null);
                if (result instanceof Collection<?> coll) {
                    for (Object obj : coll) {
                        RecipeInfo info = parseObjectToRecipeInfo(obj);
                        if (info != null && processedKeys.add(info.id)) {
                            list.add(info);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // 2. Fallback / Complement via Bukkit recipe iterator for CookingRecipe
        try {
            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                if (!(recipe instanceof CookingRecipe<?> cookingRecipe)) {
                    continue;
                }

                NamespacedKey key = null;
                if (cookingRecipe instanceof org.bukkit.Keyed keyed) {
                    key = keyed.getKey();
                }

                boolean isCraftorithmRelated = key != null && (key.getNamespace().equalsIgnoreCase("craftorithm") || key.getNamespace().equalsIgnoreCase("crafthorim"));
                
                ItemStack resultItem = cookingRecipe.getResult();
                RecipeChoice inputChoice = cookingRecipe.getInputChoice();
                ItemStack inputStack = null;
                if (inputChoice instanceof RecipeChoice.ExactChoice exactChoice && !exactChoice.getChoices().isEmpty()) {
                    inputStack = exactChoice.getChoices().get(0);
                }

                String inputCraftId = extractCraftorithmId(inputStack);
                String resultCraftId = extractCraftorithmId(resultItem);

                if (!isCraftorithmRelated && inputCraftId == null && resultCraftId == null) {
                    continue;
                }

                String recipeId = (key != null) ? key.getKey() : "smelt_" + (resultItem != null ? resultItem.getType().name().toLowerCase(Locale.ROOT) : "item");
                if (!processedKeys.add(recipeId)) {
                    continue;
                }

                RecipeInfo info = new RecipeInfo();
                info.id = recipeId;
                info.cookTimeTicks = cookingRecipe.getCookingTime();
                info.experience = cookingRecipe.getExperience();

                if (inputStack != null) {
                    info.inputMaterial = inputStack.getType().name();
                    info.inputId = inputCraftId;
                    if (inputStack.hasItemMeta()) {
                        var meta = inputStack.getItemMeta();
                        if (meta.hasDisplayName()) info.inputName = meta.getDisplayName();
                        if (meta.hasCustomModelData()) info.inputCustomModelData = meta.getCustomModelData();
                    }
                } else if (cookingRecipe.getInput() != null) {
                    info.inputMaterial = cookingRecipe.getInput().getType().name();
                }

                if (resultItem != null) {
                    info.resultMaterial = resultItem.getType().name();
                    info.resultAmount = resultItem.getAmount();
                    info.resultId = resultCraftId;
                    if (resultItem.hasItemMeta()) {
                        var meta = resultItem.getItemMeta();
                        if (meta.hasDisplayName()) info.resultName = meta.getDisplayName();
                        if (meta.hasCustomModelData()) info.resultCustomModelData = meta.getCustomModelData();
                    }
                }

                if (info.inputMaterial != null && info.resultMaterial != null) {
                    list.add(info);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "[PlayerFurnaces] Error parsing Bukkit cooking recipes for Craftorithm: " + e.getMessage());
        }

        return list;
    }

    private RecipeInfo parseObjectToRecipeInfo(Object obj) {
        if (obj == null) return null;
        try {
            Method getIdMethod = obj.getClass().getMethod("getId");
            String id = (String) getIdMethod.invoke(obj);
            RecipeInfo info = new RecipeInfo();
            info.id = id;
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractCraftorithmId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            if (key.getNamespace().equalsIgnoreCase("craftorithm") || key.getNamespace().equalsIgnoreCase("crafthorim")) {
                return pdc.get(key, PersistentDataType.STRING);
            }
        }
        return null;
    }

    private String sanitizeFileName(String str) {
        return str.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_\\-]", "_");
    }

    private String sanitizeKey(String str) {
        return str.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
    }

    private static class RecipeInfo {
        String id;
        String inputMaterial;
        String inputId;
        String inputName;
        Integer inputCustomModelData;

        String resultMaterial;
        String resultId;
        String resultName;
        Integer resultCustomModelData;
        int resultAmount = 1;

        int cookTimeTicks = 200;
        double experience = 0.0;
    }
}

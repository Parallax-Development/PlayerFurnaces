package dev.darkblade.playerfurnaces.importer.impl;

import dev.darkblade.playerfurnaces.importer.ImportResult;
import dev.darkblade.playerfurnaces.importer.RecipeImporter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            if (info.inputId != null && !info.inputId.isEmpty()) {
                config.set(rootKey + ".input.id", "craftorithm:" + info.inputId);
            }
            if (info.inputMaterial != null) {
                config.set(rootKey + ".input.material", info.inputMaterial);
            }
            if (info.inputName != null && !info.inputName.isEmpty()) {
                config.set(rootKey + ".input.name", info.inputName);
            }
            if (info.inputLore != null && !info.inputLore.isEmpty()) {
                config.set(rootKey + ".input.lore", info.inputLore);
            }
            if (info.inputCustomModelData != null) {
                config.set(rootKey + ".input.custom-model-data", info.inputCustomModelData);
            }
            if (info.inputPdc != null && !info.inputPdc.isEmpty()) {
                for (Map.Entry<String, String> entry : info.inputPdc.entrySet()) {
                    config.set(rootKey + ".input.pdc." + entry.getKey(), entry.getValue());
                }
            }

            // Result section
            if (info.resultId != null && !info.resultId.isEmpty()) {
                config.set(rootKey + ".result.id", "craftorithm:" + info.resultId);
            } else if (info.resultMaterial != null) {
                config.set(rootKey + ".result.material", info.resultMaterial);
            }
            config.set(rootKey + ".result.amount", info.resultAmount);
            if (info.resultName != null && !info.resultName.isEmpty()) {
                config.set(rootKey + ".result.name", info.resultName);
            }
            if (info.resultLore != null && !info.resultLore.isEmpty()) {
                config.set(rootKey + ".result.lore", info.resultLore);
            }
            if (info.resultCustomModelData != null) {
                config.set(rootKey + ".result.custom-model-data", info.resultCustomModelData);
            }
            if (info.resultPdc != null && !info.resultPdc.isEmpty()) {
                for (Map.Entry<String, String> entry : info.resultPdc.entrySet()) {
                    config.set(rootKey + ".result.pdc." + entry.getKey(), entry.getValue());
                }
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

                ItemStack inputStack = null;
                RecipeChoice inputChoice = cookingRecipe.getInputChoice();
                if (inputChoice instanceof RecipeChoice.ExactChoice exactChoice && !exactChoice.getChoices().isEmpty()) {
                    inputStack = exactChoice.getChoices().get(0);
                } else if (inputChoice instanceof RecipeChoice.MaterialChoice materialChoice && !materialChoice.getChoices().isEmpty()) {
                    Material mat = materialChoice.getChoices().get(0);
                    inputStack = new ItemStack(mat);
                } else if (cookingRecipe.getInput() != null) {
                    inputStack = cookingRecipe.getInput();
                }

                ItemStack resultItem = cookingRecipe.getResult();

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
                    extractMetadata(inputStack, info, true);
                }

                if (resultItem != null) {
                    info.resultMaterial = resultItem.getType().name();
                    info.resultAmount = resultItem.getAmount();
                    info.resultId = resultCraftId;
                    extractMetadata(resultItem, info, false);
                }

                if (info.inputMaterial != null && (info.resultMaterial != null || info.resultId != null)) {
                    list.add(info);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "[PlayerFurnaces] Error parsing Bukkit cooking recipes for Craftorithm: " + e.getMessage());
        }

        return list;
    }

    private void extractMetadata(ItemStack stack, RecipeInfo info, boolean isInput) {
        if (stack == null || !stack.hasItemMeta()) return;
        var meta = stack.getItemMeta();

        if (meta.hasDisplayName()) {
            if (isInput) info.inputName = meta.getDisplayName();
            else info.resultName = meta.getDisplayName();
        }

        if (meta.hasLore() && meta.getLore() != null) {
            if (isInput) info.inputLore = new ArrayList<>(meta.getLore());
            else info.resultLore = new ArrayList<>(meta.getLore());
        }

        if (meta.hasCustomModelData()) {
            if (isInput) info.inputCustomModelData = meta.getCustomModelData();
            else info.resultCustomModelData = meta.getCustomModelData();
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Map<String, String> pdcMap = new HashMap<>();
        for (NamespacedKey key : pdc.getKeys()) {
            if (key.getNamespace().equalsIgnoreCase("craftorithm") || key.getNamespace().equalsIgnoreCase("crafthorim")) {
                continue;
            }
            try {
                String val = pdc.get(key, PersistentDataType.STRING);
                if (val != null) {
                    pdcMap.put(key.toString(), val);
                }
            } catch (Exception ignored) {}
        }
        if (!pdcMap.isEmpty()) {
            if (isInput) info.inputPdc = pdcMap;
            else info.resultPdc = pdcMap;
        }
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
        List<String> inputLore;
        Integer inputCustomModelData;
        Map<String, String> inputPdc;

        String resultMaterial;
        String resultId;
        String resultName;
        List<String> resultLore;
        Integer resultCustomModelData;
        Map<String, String> resultPdc;
        int resultAmount = 1;

        int cookTimeTicks = 200;
        double experience = 0.0;
    }
}

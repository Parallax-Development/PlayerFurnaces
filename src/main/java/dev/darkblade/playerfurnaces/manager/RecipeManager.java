package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.model.CustomRecipe;
import dev.darkblade.playerfurnaces.model.RecipeItemDefinition;
import dev.darkblade.playerfurnaces.provider.ItemResolverRegistry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class RecipeManager {

    private final PlayerFurnacesPlugin plugin;
    private final ItemResolverRegistry itemResolverRegistry;
    private final List<CustomRecipe> recipes = new ArrayList<>();
    private boolean vanillaSmeltingEnabled = true;
    private final Set<Material> disabledVanillaMaterials = new HashSet<>();

    public RecipeManager(PlayerFurnacesPlugin plugin, ItemResolverRegistry itemResolverRegistry) {
        this.plugin = plugin;
        this.itemResolverRegistry = itemResolverRegistry;
    }

    public void loadRecipes() {
        recipes.clear();
        disabledVanillaMaterials.clear();

        FileConfiguration pluginConfig = plugin.getConfig();
        if (pluginConfig != null) {
            this.vanillaSmeltingEnabled = pluginConfig.getBoolean("recipes.vanilla-smelting.enabled", true);
            List<String> disabledList = pluginConfig.getStringList("recipes.vanilla-smelting.disabled-materials");
            if (disabledList != null) {
                for (String matName : disabledList) {
                    Material mat = Material.matchMaterial(matName);
                    if (mat != null) {
                        disabledVanillaMaterials.add(mat);
                    } else {
                        plugin.getLogger().warning("Invalid material in recipes.vanilla-smelting.disabled-materials: " + matName);
                    }
                }
            }
        }

        File recipesDir = new File(plugin.getDataFolder(), "recipes");
        if (!recipesDir.exists()) {
            recipesDir.mkdirs();
        }

        List<File> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(recipesDir.toPath())) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
                        return name.endsWith(".yml") || name.endsWith(".yaml");
                    })
                    .map(Path::toFile)
                    .forEach(files::add);
        } catch (Exception e) {
            plugin.getLogger().warning("Error reading recipes directory: " + e.getMessage());
            return;
        }

        int skippedCount = 0;
        for (File file : files) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                for (String rootKey : config.getKeys(false)) {
                    if (rootKey.equalsIgnoreCase("fuel")) {
                        continue;
                    }
                    ConfigurationSection section = config.getConfigurationSection(rootKey);
                    if (section != null && section.contains("input")) {
                        CustomRecipe recipe = parseRecipe(rootKey, section, config, file.getName());
                        if (recipe != null) {
                            recipes.add(recipe);
                        } else {
                            skippedCount++;
                        }
                    }
                }
            } catch (Exception e) {
                skippedCount++;
                plugin.getLogger().warning("Error loading recipe file: " + file.getName() + " -> " + e.getMessage());
            }
        }
        if (skippedCount > 0) {
            plugin.getLogger().info("Loaded " + recipes.size() + " custom furnace recipe overrides (" + skippedCount + " skipped due to configuration errors).");
        } else {
            plugin.getLogger().info("Loaded " + recipes.size() + " custom furnace recipe overrides.");
        }
    }

    private CustomRecipe parseRecipe(String recipeId, ConfigurationSection section, YamlConfiguration fileConfig, String fileName) {
        boolean disabled = section.getBoolean("disabled", false);
        RecipeItemDefinition input = parseItemDef(section.getConfigurationSection("input"), "input", recipeId, fileName);
        RecipeItemDefinition result = parseItemDef(section.getConfigurationSection("result"), "result", recipeId, fileName);

        if (input == null || (!disabled && result == null)) {
            return null;
        }

        int cookTime = section.getInt("cook-time-ticks", 200);
        double exp = section.getDouble("experience", 0.0);

        String fuelType = null;
        Integer fuelBurnTicks = null;

        ConfigurationSection fuelSection = section.getConfigurationSection("fuel");
        if (fuelSection == null && fileConfig.isConfigurationSection("fuel")) {
            fuelSection = fileConfig.getConfigurationSection("fuel");
        }

        if (fuelSection != null) {
            fuelType = fuelSection.getString("type");
            if (fuelType != null && !validateTypeIdentifier(fuelType)) {
                plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): fuel type '" + fuelType + "' is not a valid material or registered item provider.");
                return null;
            }
            if (fuelSection.contains("burn-time-ticks")) {
                fuelBurnTicks = fuelSection.getInt("burn-time-ticks");
            }
        }

        return new CustomRecipe(recipeId, input, result, cookTime, exp, fuelType, fuelBurnTicks, disabled);
    }

    private RecipeItemDefinition parseItemDef(ConfigurationSection sec, String sectionName, String recipeId, String fileName) {
        if (sec == null) {
            plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): missing '" + sectionName + "' section.");
            return null;
        }
        String id = sec.getString("id");
        String matStr = sec.getString("material");

        if ((id == null || id.trim().isEmpty()) && (matStr == null || matStr.trim().isEmpty())) {
            plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): section '" + sectionName + "' must specify either 'material' or 'id'.");
            return null;
        }

        Material mat = null;
        if (matStr != null && !matStr.trim().isEmpty()) {
            mat = Material.matchMaterial(matStr.trim());
            if (mat == null) {
                plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): section '" + sectionName + ".material' specified invalid material '" + matStr + "'.");
                return null;
            }
        }

        if (id != null && !id.trim().isEmpty()) {
            String trimmedId = id.trim();
            if (trimmedId.contains(":")) {
                String namespace = trimmedId.split(":", 2)[0].trim();
                if (itemResolverRegistry != null && itemResolverRegistry.getProvider(namespace) == null) {
                    plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): section '" + sectionName + ".id' uses unknown or unregistered provider namespace '" + namespace + "' in id '" + trimmedId + "'.");
                    return null;
                }
            } else if (mat == null) {
                mat = Material.matchMaterial(trimmedId);
                if (mat == null) {
                    plugin.getLogger().warning("In '" + fileName + "' (recipe '" + recipeId + "'): section '" + sectionName + ".id' specified invalid material '" + trimmedId + "'.");
                    return null;
                }
            }
        }

        String name = sec.getString("name");
        List<String> lore = sec.getStringList("lore");
        Integer cmd = sec.contains("custom-model-data") ? sec.getInt("custom-model-data") : null;
        int amount = sec.getInt("amount", 1);

        Map<String, String> pdcMap = new HashMap<>();
        ConfigurationSection pdcSec = sec.getConfigurationSection("pdc");
        if (pdcSec != null) {
            for (String k : pdcSec.getKeys(false)) {
                pdcMap.put(k, pdcSec.getString(k));
            }
        }

        return new RecipeItemDefinition(id, mat, name, lore, cmd, pdcMap, amount);
    }

    private boolean validateTypeIdentifier(String typeStr) {
        if (typeStr == null || typeStr.trim().isEmpty()) {
            return false;
        }
        String trimmed = typeStr.trim();
        if (trimmed.contains(":")) {
            String namespace = trimmed.split(":", 2)[0].trim();
            return itemResolverRegistry != null && itemResolverRegistry.getProvider(namespace) != null;
        }
        return Material.matchMaterial(trimmed) != null;
    }

    public CustomRecipe findMatchingRecipe(ItemStack input) {
        if (input == null || input.getType().isAir()) {
            return null;
        }
        for (CustomRecipe recipe : recipes) {
            if (ItemMatcher.matches(input, recipe.getInput(), itemResolverRegistry)) {
                return recipe;
            }
        }
        return null;
    }

    public List<CustomRecipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    public boolean isVanillaSmeltingEnabled() {
        return vanillaSmeltingEnabled;
    }

    public boolean isVanillaMaterialDisabled(Material material) {
        return material != null && disabledVanillaMaterials.contains(material);
    }
}

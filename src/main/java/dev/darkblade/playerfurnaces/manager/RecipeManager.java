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
import java.util.*;

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

        File[] files = recipesDir.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (files == null) return;

        for (File file : files) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                for (String rootKey : config.getKeys(false)) {
                    if (rootKey.equalsIgnoreCase("fuel")) {
                        continue;
                    }
                    ConfigurationSection section = config.getConfigurationSection(rootKey);
                    if (section != null && section.contains("input")) {
                        CustomRecipe recipe = parseRecipe(rootKey, section, config);
                        if (recipe != null) {
                            recipes.add(recipe);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Error loading recipe file: " + file.getName() + " -> " + e.getMessage());
            }
        }
        plugin.getLogger().info("Loaded " + recipes.size() + " custom furnace recipe overrides.");
    }

    private CustomRecipe parseRecipe(String recipeId, ConfigurationSection section, YamlConfiguration fileConfig) {
        boolean disabled = section.getBoolean("disabled", false);
        RecipeItemDefinition input = parseItemDef(section.getConfigurationSection("input"));
        RecipeItemDefinition result = parseItemDef(section.getConfigurationSection("result"));

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
            if (fuelSection.contains("burn-time-ticks")) {
                fuelBurnTicks = fuelSection.getInt("burn-time-ticks");
            }
        }

        return new CustomRecipe(recipeId, input, result, cookTime, exp, fuelType, fuelBurnTicks, disabled);
    }

    private RecipeItemDefinition parseItemDef(ConfigurationSection sec) {
        if (sec == null) return null;
        String id = sec.getString("id");
        String matStr = sec.getString("material");
        Material mat = matStr != null ? Material.matchMaterial(matStr) : null;
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

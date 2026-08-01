package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.SmeltingManager;
import dev.darkblade.playerfurnaces.model.CustomFuel;
import dev.darkblade.playerfurnaces.provider.ItemResolverRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class FuelManager {

    private final PlayerFurnacesPlugin plugin;
    private final ItemResolverRegistry itemResolverRegistry;
    private final Map<String, CustomFuel> fuels = new HashMap<>();

    public FuelManager(PlayerFurnacesPlugin plugin, ItemResolverRegistry itemResolverRegistry) {
        this.plugin = plugin;
        this.itemResolverRegistry = itemResolverRegistry;
    }

    public void loadFuels() {
        fuels.clear();
        File fuelsDir = new File(plugin.getDataFolder(), "fuels");
        if (!fuelsDir.exists()) {
            fuelsDir.mkdirs();
        }

        File[] files = fuelsDir.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (files == null) return;

        int skippedCount = 0;
        for (File file : files) {
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                for (String rootKey : config.getKeys(false)) {
                    ConfigurationSection section = config.getConfigurationSection(rootKey);
                    if (section != null && section.contains("type") && section.contains("burn-time-ticks")) {
                        String type = section.getString("type");
                        if (!isValidFuelType(type, rootKey, file.getName())) {
                            skippedCount++;
                            continue;
                        }
                        int burnTicks = section.getInt("burn-time-ticks", 1600);
                        fuels.put(rootKey, new CustomFuel(rootKey, type, burnTicks));
                    }
                }
            } catch (Exception e) {
                skippedCount++;
                plugin.getLogger().warning("Error loading fuel file: " + file.getName() + " -> " + e.getMessage());
            }
        }
        if (skippedCount > 0) {
            plugin.getLogger().info("Loaded " + fuels.size() + " custom global furnace fuels (" + skippedCount + " skipped due to configuration errors).");
        } else {
            plugin.getLogger().info("Loaded " + fuels.size() + " custom global furnace fuels.");
        }
    }

    private boolean isValidFuelType(String typeStr, String fuelKey, String fileName) {
        if (typeStr == null || typeStr.trim().isEmpty()) {
            plugin.getLogger().warning("In '" + fileName + "' (fuel '" + fuelKey + "'): missing or empty fuel type.");
            return false;
        }
        String trimmed = typeStr.trim();
        if (trimmed.contains(":")) {
            String namespace = trimmed.split(":", 2)[0].trim();
            if (itemResolverRegistry != null && itemResolverRegistry.getProvider(namespace) == null) {
                plugin.getLogger().warning("In '" + fileName + "' (fuel '" + fuelKey + "'): uses unknown or unregistered provider namespace '" + namespace + "' in type '" + trimmed + "'.");
                return false;
            }
            return true;
        }
        if (org.bukkit.Material.matchMaterial(trimmed) == null) {
            plugin.getLogger().warning("In '" + fileName + "' (fuel '" + fuelKey + "'): specified invalid material '" + trimmed + "'.");
            return false;
        }
        return true;
    }

    public CustomFuel findMatchingFuel(ItemStack fuelStack) {
        if (fuelStack == null || fuelStack.getType().isAir()) {
            return null;
        }
        for (CustomFuel fuel : fuels.values()) {
            if (itemResolverRegistry.matches(fuelStack, fuel.getType())) {
                return fuel;
            }
        }
        return null;
    }

    public int getBurnTime(ItemStack fuelStack) {
        CustomFuel customFuel = findMatchingFuel(fuelStack);
        if (customFuel != null) {
            return customFuel.getBurnTimeTicks();
        }
        return SmeltingManager.getFuelBurnTime(fuelStack);
    }
}

package dev.darkblade.playerfurnaces.engine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SmeltingManager {

    private static final Map<Material, Integer> FUEL_BURN_TIMES = new HashMap<>();

    static {
        FUEL_BURN_TIMES.put(Material.LAVA_BUCKET, 20000);
        FUEL_BURN_TIMES.put(Material.COAL_BLOCK, 16000);
        FUEL_BURN_TIMES.put(Material.BLAZE_ROD, 2400);
        FUEL_BURN_TIMES.put(Material.COAL, 1600);
        FUEL_BURN_TIMES.put(Material.CHARCOAL, 1600);
        FUEL_BURN_TIMES.put(Material.OAK_LOG, 300);
        FUEL_BURN_TIMES.put(Material.SPRUCE_LOG, 300);
        FUEL_BURN_TIMES.put(Material.BIRCH_LOG, 300);
        FUEL_BURN_TIMES.put(Material.JUNGLE_LOG, 300);
        FUEL_BURN_TIMES.put(Material.ACACIA_LOG, 300);
        FUEL_BURN_TIMES.put(Material.DARK_OAK_LOG, 300);
        FUEL_BURN_TIMES.put(Material.MANGROVE_LOG, 300);
        FUEL_BURN_TIMES.put(Material.CHERRY_LOG, 300);
        FUEL_BURN_TIMES.put(Material.OAK_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.SPRUCE_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.BIRCH_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.JUNGLE_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.ACACIA_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.DARK_OAK_PLANKS, 300);
        FUEL_BURN_TIMES.put(Material.STICK, 100);
    }

    public static CookingRecipe<?> getSmeltingRecipe(ItemStack input) {
        if (input == null || input.getType().isAir()) {
            return null;
        }

        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice().test(input)) {
                    return furnaceRecipe;
                }
            } else if (recipe instanceof CookingRecipe<?> cookingRecipe) {
                if (cookingRecipe.getInputChoice().test(input)) {
                    return cookingRecipe;
                }
            }
        }
        return null;
    }

    public static int getFuelBurnTime(ItemStack fuel) {
        if (fuel == null || fuel.getType().isAir()) {
            return 0;
        }
        return FUEL_BURN_TIMES.getOrDefault(fuel.getType(), 0);
    }
}

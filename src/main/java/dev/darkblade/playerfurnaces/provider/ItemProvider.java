package dev.darkblade.playerfurnaces.provider;

import org.bukkit.inventory.ItemStack;

public interface ItemProvider {

    /**
     * Returns the unique namespace for this provider (e.g. "crafthorim", "oraxen", "minecraft").
     */
    String getNamespace();

    /**
     * Constructs an ItemStack from this provider with the specified ID and amount.
     * @param id Item ID without namespace prefix
     * @param amount Quantity requested
     * @return Generated ItemStack or null if invalid/unsupported
     */
    ItemStack getItem(String id, int amount);

    /**
     * Checks if a given ItemStack matches the provider's item ID.
     * @param itemStack ItemStack in furnace slot
     * @param id Target item ID
     * @return true if item matches
     */
    boolean isSimilar(ItemStack itemStack, String id);
}

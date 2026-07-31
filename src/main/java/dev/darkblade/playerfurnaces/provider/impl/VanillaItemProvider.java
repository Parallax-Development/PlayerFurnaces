package dev.darkblade.playerfurnaces.provider.impl;

import dev.darkblade.playerfurnaces.provider.ItemProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VanillaItemProvider implements ItemProvider {

    @Override
    public String getNamespace() {
        return "minecraft";
    }

    @Override
    public ItemStack getItem(String id, int amount) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        Material mat = Material.matchMaterial(id.trim());
        if (mat != null && !mat.isAir()) {
            return new ItemStack(mat, amount);
        }
        return null;
    }

    @Override
    public boolean isSimilar(ItemStack itemStack, String id) {
        if (itemStack == null || id == null || id.trim().isEmpty()) {
            return false;
        }
        Material mat = Material.matchMaterial(id.trim());
        return mat != null && itemStack.getType() == mat;
    }
}

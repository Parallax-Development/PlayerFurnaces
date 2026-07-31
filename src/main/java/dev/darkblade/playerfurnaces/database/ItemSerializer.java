package dev.darkblade.playerfurnaces.database;

import org.bukkit.inventory.ItemStack;

public class ItemSerializer {

    public static byte[] serialize(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0) {
            return null;
        }
        return itemStack.serializeAsBytes();
    }

    public static ItemStack deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return ItemStack.deserializeBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

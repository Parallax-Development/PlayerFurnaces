package dev.darkblade.playerfurnaces.util;

import dev.darkblade.playerfurnaces.model.RecipeItemDefinition;
import dev.darkblade.playerfurnaces.provider.ItemResolverRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class RecipeItemBuilder {

    public static ItemStack build(RecipeItemDefinition def, ItemResolverRegistry registry) {
        if (def == null) {
            return null;
        }

        ItemStack item = null;
        if (def.getId() != null && def.getId().contains(":") && registry != null) {
            item = registry.resolveItem(def.getId(), def.getAmount());
        }

        if (item == null && def.getMaterial() != null) {
            item = new ItemStack(def.getMaterial(), def.getAmount());
        }

        if (item == null) {
            return null;
        }

        return applyMetadata(item, def);
    }

    public static ItemStack applyMetadata(ItemStack item, RecipeItemDefinition def) {
        if (item == null || def == null || item.getType().isAir()) {
            return item;
        }

        ItemMeta meta = null;
        try {
            meta = item.getItemMeta();
        } catch (Exception ignored) {
            return item;
        }

        if (meta == null) {
            return item;
        }

        if (def.getName() != null && !def.getName().trim().isEmpty()) {
            meta.setDisplayName(ColorUtils.colorize(def.getName().trim()));
        }

        if (def.getLore() != null && !def.getLore().isEmpty()) {
            meta.setLore(ColorUtils.colorize(def.getLore()));
        }

        if (def.getCustomModelData() != null) {
            meta.setCustomModelData(def.getCustomModelData());
        }

        if (def.getPdc() != null && !def.getPdc().isEmpty()) {
            var container = meta.getPersistentDataContainer();
            for (Map.Entry<String, String> entry : def.getPdc().entrySet()) {
                String keyStr = entry.getKey();
                String valueStr = entry.getValue();
                if (keyStr == null || valueStr == null) continue;

                NamespacedKey key;
                if (keyStr.contains(":")) {
                    String[] parts = keyStr.split(":", 2);
                    key = new NamespacedKey(parts[0].trim(), parts[1].trim());
                } else {
                    key = NamespacedKey.minecraft(keyStr.trim());
                }
                container.set(key, PersistentDataType.STRING, valueStr);
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}

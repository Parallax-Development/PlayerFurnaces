package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.model.RecipeItemDefinition;
import dev.darkblade.playerfurnaces.provider.ItemResolverRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class ItemMatcher {

    public static boolean matches(ItemStack input, RecipeItemDefinition def, ItemResolverRegistry registry) {
        if (input == null || input.getType().isAir() || def == null) {
            return false;
        }

        String id = def.getId();
        if (id != null && id.contains(":")) {
            return registry.matches(input, id);
        }

        if (def.getMaterial() != null && input.getType() != def.getMaterial()) {
            return false;
        }

        if (def.getCustomModelData() != null) {
            if (!input.hasItemMeta() || !input.getItemMeta().hasCustomModelData() || input.getItemMeta().getCustomModelData() != def.getCustomModelData()) {
                return false;
            }
        }

        if (def.getPdc() != null && !def.getPdc().isEmpty()) {
            if (!input.hasItemMeta()) {
                return false;
            }
            ItemMeta meta = input.getItemMeta();
            var container = meta.getPersistentDataContainer();
            for (Map.Entry<String, String> entry : def.getPdc().entrySet()) {
                String keyStr = entry.getKey();
                String valueStr = entry.getValue();
                NamespacedKey key;
                if (keyStr.contains(":")) {
                    String[] parts = keyStr.split(":", 2);
                    key = new NamespacedKey(parts[0], parts[1]);
                } else {
                    key = NamespacedKey.minecraft(keyStr);
                }
                if (!container.has(key, PersistentDataType.STRING)) {
                    return false;
                }
                String actualValue = container.get(key, PersistentDataType.STRING);
                if (!valueStr.equals(actualValue)) {
                    return false;
                }
            }
        }

        return true;
    }
}

package dev.darkblade.playerfurnaces.provider;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemResolverRegistry {

    private final Map<String, ItemProvider> providers = new HashMap<>();

    public void registerProvider(ItemProvider provider) {
        providers.put(provider.getNamespace().toLowerCase(), provider);
    }

    public void registerAlias(String alias, ItemProvider provider) {
        if (alias != null && provider != null) {
            providers.put(alias.toLowerCase(), provider);
        }
    }

    public ItemProvider getProvider(String namespace) {
        return providers.get(namespace.toLowerCase());
    }

    public ItemStack resolveItem(String fullId, int amount) {
        if (fullId == null || fullId.trim().isEmpty()) {
            return null;
        }

        String namespace = "minecraft";
        String itemId = fullId.trim();

        if (fullId.contains(":")) {
            String[] parts = fullId.split(":", 2);
            namespace = parts[0].trim();
            itemId = parts[1].trim();
        }

        ItemProvider provider = getProvider(namespace);
        if (provider != null) {
            return provider.getItem(itemId, amount);
        }
        return null;
    }

    public boolean matches(ItemStack itemStack, String fullId) {
        if (itemStack == null || fullId == null || fullId.trim().isEmpty()) {
            return false;
        }

        String namespace = "minecraft";
        String itemId = fullId.trim();

        if (fullId.contains(":")) {
            String[] parts = fullId.split(":", 2);
            namespace = parts[0].trim();
            itemId = parts[1].trim();
        }

        ItemProvider provider = getProvider(namespace);
        if (provider != null) {
            return provider.isSimilar(itemStack, itemId);
        }
        return false;
    }
}

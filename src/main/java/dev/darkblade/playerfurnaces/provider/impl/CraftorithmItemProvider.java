package dev.darkblade.playerfurnaces.provider.impl;

import dev.darkblade.playerfurnaces.provider.ItemProvider;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.logging.Level;

public class CraftorithmItemProvider implements ItemProvider {

    private boolean available = false;
    private Method getItemMethod;

    public CraftorithmItemProvider() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Craftorithm");
        if (plugin != null && plugin.isEnabled()) {
            try {
                Class<?> apiClass = Class.forName("cc.sy.craftorithm.api.CraftorithmAPI");
                getItemMethod = apiClass.getMethod("getItem", String.class);
                available = true;
            } catch (ClassNotFoundException e) {
                // If API class differs, fallback matching will be used via PDC
                available = false;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "[PlayerFurnaces] Error hooking into Craftorithm: " + e.getMessage());
            }
        }
    }

    @Override
    public String getNamespace() {
        return "crafthorim";
    }

    @Override
    public ItemStack getItem(String id, int amount) {
        if (available && getItemMethod != null) {
            try {
                Object result = getItemMethod.invoke(null, id);
                if (result instanceof ItemStack itemStack) {
                    ItemStack copy = itemStack.clone();
                    copy.setAmount(amount);
                    return copy;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public boolean isSimilar(ItemStack itemStack, String id) {
        if (itemStack == null || id == null || id.isEmpty()) {
            return false;
        }

        ItemStack craftItem = getItem(id, 1);
        if (craftItem != null) {
            return itemStack.isSimilar(craftItem);
        }

        if (itemStack.hasItemMeta()) {
            var pdc = itemStack.getItemMeta().getPersistentDataContainer();
            for (var key : pdc.getKeys()) {
                if (key.getNamespace().equalsIgnoreCase("craftorithm") || key.getNamespace().equalsIgnoreCase("crafthorim")) {
                    String value = pdc.get(key, org.bukkit.persistence.PersistentDataType.STRING);
                    if (id.equalsIgnoreCase(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

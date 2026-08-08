package dev.darkblade.playerfurnaces.provider.impl;

import dev.darkblade.playerfurnaces.provider.ItemProvider;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;

public class ExecutableItemsItemProvider implements ItemProvider {

    private boolean available = false;
    private Method getExecutableItemMethod;

    public ExecutableItemsItemProvider() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ExecutableItems");
        if (plugin != null && plugin.isEnabled()) {
            try {
                Class<?> apiClass = Class.forName("com.ssomar.score.api.executableitems.ExecutableItemsAPI");
                getExecutableItemMethod = apiClass.getMethod("getExecutableItem", String.class);
                available = true;
            } catch (ClassNotFoundException e) {
                available = false;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "[PlayerFurnaces] Error hooking into ExecutableItems: " + e.getMessage());
            }
        }
    }

    @Override
    public String getNamespace() {
        return "executableitems";
    }

    @Override
    public ItemStack getItem(String id, int amount) {
        if (!available || id == null || id.trim().isEmpty()) {
            return null;
        }

        try {
            Object result = getExecutableItemMethod.invoke(null, id.trim());
            if (result instanceof Optional<?> opt && opt.isPresent()) {
                Object execItem = opt.get();
                Method buildMethod = null;
                for (Method m : execItem.getClass().getMethods()) {
                    if (m.getName().equals("buildItem")) {
                        buildMethod = m;
                        break;
                    }
                }
                if (buildMethod != null) {
                    Object itemStackObj = null;
                    if (buildMethod.getParameterCount() == 2) {
                        itemStackObj = buildMethod.invoke(execItem, amount, Optional.empty());
                    } else if (buildMethod.getParameterCount() == 1) {
                        itemStackObj = buildMethod.invoke(execItem, amount);
                    }
                    if (itemStackObj instanceof ItemStack itemStack) {
                        ItemStack copy = itemStack.clone();
                        copy.setAmount(amount);
                        return copy;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public boolean isSimilar(ItemStack itemStack, String id) {
        if (itemStack == null || id == null || id.trim().isEmpty()) {
            return false;
        }

        ItemStack execItem = getItem(id, 1);
        if (execItem != null) {
            return itemStack.isSimilar(execItem);
        }

        if (itemStack.hasItemMeta()) {
            var pdc = itemStack.getItemMeta().getPersistentDataContainer();
            for (var key : pdc.getKeys()) {
                String ns = key.getNamespace().toLowerCase();
                if (ns.contains("executableitem") || ns.equalsIgnoreCase("ei")) {
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

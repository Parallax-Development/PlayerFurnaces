package dev.darkblade.playerfurnaces.provider.impl;

import dev.darkblade.playerfurnaces.provider.ItemProvider;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Optional;

public class ExecutableItemsItemProvider implements ItemProvider {

    private boolean available = false;
    private Object targetInstance = null;
    private Method getExecutableItemMethod = null;

    public ExecutableItemsItemProvider() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ExecutableItems");
        if (plugin == null || !plugin.isEnabled()) {
            return;
        }

        // Strategy 1: ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id)
        try {
            Class<?> apiClass = Class.forName("com.ssomar.score.api.executableitems.ExecutableItemsAPI");
            Method getManagerMethod = apiClass.getMethod("getExecutableItemsManager");
            Object manager = getManagerMethod.invoke(null);
            if (manager != null) {
                Method getItemMethod = manager.getClass().getMethod("getExecutableItem", String.class);
                this.targetInstance = manager;
                this.getExecutableItemMethod = getItemMethod;
                this.available = true;
                return;
            }
        } catch (Exception ignored) {
        }

        // Strategy 2: ExecutableItemsAPI.getExecutableItem(id) static method
        try {
            Class<?> apiClass = Class.forName("com.ssomar.score.api.executableitems.ExecutableItemsAPI");
            Method getItemMethod = apiClass.getMethod("getExecutableItem", String.class);
            this.targetInstance = null;
            this.getExecutableItemMethod = getItemMethod;
            this.available = true;
            return;
        } catch (Exception ignored) {
        }

        // Strategy 3: ExecutableItemsManager.getInstance().getExecutableItem(id)
        try {
            Class<?> managerClass = Class.forName("com.ssomar.score.executableitems.ExecutableItemsManager");
            Method getInstanceMethod = managerClass.getMethod("getInstance");
            Object manager = getInstanceMethod.invoke(null);
            if (manager != null) {
                Method getItemMethod = manager.getClass().getMethod("getExecutableItem", String.class);
                this.targetInstance = manager;
                this.getExecutableItemMethod = getItemMethod;
                this.available = true;
                return;
            }
        } catch (Exception ignored) {
        }

        // Strategy 4: Fallback PDC matching if API methods are non-existent
        this.available = false;
    }

    @Override
    public String getNamespace() {
        return "executableitems";
    }

    @Override
    public ItemStack getItem(String id, int amount) {
        if (!available || getExecutableItemMethod == null || id == null || id.trim().isEmpty()) {
            return null;
        }

        try {
            Object result = getExecutableItemMethod.invoke(targetInstance, id.trim());
            if (result instanceof Optional<?> opt) {
                result = opt.orElse(null);
            }
            if (result != null) {
                Method buildMethod = null;
                for (Method m : result.getClass().getMethods()) {
                    if (m.getName().equals("buildItem")) {
                        buildMethod = m;
                        break;
                    }
                }
                if (buildMethod != null) {
                    Object itemStackObj = null;
                    if (buildMethod.getParameterCount() == 2) {
                        itemStackObj = buildMethod.invoke(result, amount, Optional.empty());
                    } else if (buildMethod.getParameterCount() == 1) {
                        itemStackObj = buildMethod.invoke(result, amount);
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
                if (ns.contains("executableitem") || ns.equalsIgnoreCase("ei") || ns.contains("ssomar")) {
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

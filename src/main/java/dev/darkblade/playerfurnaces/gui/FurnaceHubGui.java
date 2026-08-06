package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.model.FurnaceStatus;
import dev.darkblade.playerfurnaces.model.MenuLayout;
import dev.darkblade.playerfurnaces.model.MenuSlotData;
import dev.darkblade.playerfurnaces.model.MenuStateData;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FurnaceHubGui implements InventoryHolder {

    private final PlayerFurnacesPlugin plugin;
    private final Player viewer;
    private final Player targetOwner;
    private final Inventory inventory;
    private final MenuLayout layout;

    public FurnaceHubGui(PlayerFurnacesPlugin plugin, Player viewer, Player targetOwner) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.targetOwner = targetOwner;
        this.layout = plugin.getMenuManager().getLayout("furnace_hub");
        
        String title = layout != null ? layout.getTitle().replace("{player}", targetOwner.getName()) : "Furnaces";
        int size = layout != null ? layout.getSize() : 54;
        
        this.inventory = Bukkit.createInventory(this, size, title);
        refresh();
    }

    public void refresh() {
        inventory.clear();
        if (layout == null) return;

        // Render fillers and static items
        for (Map.Entry<Integer, MenuSlotData> entry : layout.getSlots().entrySet()) {
            int slot = entry.getKey();
            MenuSlotData data = entry.getValue();
            
            if ("FILLER".equals(data.getType())) {
                inventory.setItem(slot, createItem(data.getDefaultState(), null));
            }
        }

        int maxFurnaces = plugin.getConfig().getInt("settings.default-furnace-count", 14);

        for (int i = 1; i <= maxFurnaces; i++) {
            // Find slot from dynamic layout mapping
            Integer slot = layout.getDynamicSlotMap().get(i);
            if (slot == null) break; // Layout does not have space for more furnaces

            MenuSlotData slotData = layout.getSlots().get(slot);
            if (slotData == null || !"FURNACE_SLOT".equals(slotData.getType())) continue;

            boolean hasPerm = plugin.getFurnaceManager().hasPermissionForFurnace(targetOwner, i);
            VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(targetOwner.getUniqueId(), i);
            FurnaceEngine.updateFurnaceState(furnace);

            MenuStateData stateData;
            if (!hasPerm) {
                stateData = slotData.getState("locked");
            } else {
                switch (furnace.getStatus()) {
                    case SMELTING -> stateData = slotData.getState("smelting");
                    case NO_FUEL -> stateData = slotData.getState("no_fuel");
                    default -> stateData = slotData.getState("idle");
                }
            }
            
            inventory.setItem(slot, createItem(stateData, furnace));
        }
    }

    private ItemStack createItem(MenuStateData stateData, VirtualFurnace furnace) {
        if (stateData == null || stateData.getMaterial() == null) return new ItemStack(Material.AIR);
        
        ItemStack item = new ItemStack(stateData.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (stateData.getCustomModelData() != null) {
                meta.setCustomModelData(stateData.getCustomModelData());
            }

            if (meta instanceof SkullMeta skullMeta) {
                if (stateData.getSkullOwner() != null && !stateData.getSkullOwner().isEmpty()) {
                    String owner = stateData.getSkullOwner().replace("{player}", targetOwner != null ? targetOwner.getName() : viewer.getName());
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
                } else if (stateData.getSkullTexture() != null && !stateData.getSkullTexture().isEmpty()) {
                    dev.darkblade.playerfurnaces.util.ColorUtils.applySkullTexture(skullMeta, stateData.getSkullTexture());
                }
            }

            String name = stateData.getName();
            if (name != null && !name.isEmpty() && furnace != null) {
                meta.setDisplayName(dev.darkblade.playerfurnaces.util.ColorUtils.colorize(name.replace("{id}", String.valueOf(furnace.getFurnaceId()))));
            } else if (name != null && !name.isEmpty()) {
                meta.setDisplayName(dev.darkblade.playerfurnaces.util.ColorUtils.colorize(name));
            }

            if (stateData.getLore() != null && !stateData.getLore().isEmpty()) {
                List<String> lore = new ArrayList<>();
                for (String line : stateData.getLore()) {
                    if (furnace != null) {
                        String itemType = furnace.getInputItem() != null ? furnace.getInputItem().getType().name() : "Air";
                        String itemAmount = furnace.getInputItem() != null ? String.valueOf(furnace.getInputItem().getAmount()) : "0";
                        String remainingTime = String.valueOf(furnace.getBurnTime() / 20);
                        
                        line = line.replace("{id}", String.valueOf(furnace.getFurnaceId()))
                                   .replace("{item}", itemType)
                                   .replace("{amount}", itemAmount)
                                   .replace("{time}", remainingTime);
                    }
                    lore.add(dev.darkblade.playerfurnaces.util.ColorUtils.colorize(line));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public Player getTargetOwner() {
        return targetOwner;
    }

    public MenuLayout getLayout() {
        return layout;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

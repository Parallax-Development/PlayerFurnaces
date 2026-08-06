package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
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

public class FurnaceViewGui implements InventoryHolder {

    private final PlayerFurnacesPlugin plugin;
    private final Player viewer;
    private final VirtualFurnace furnace;
    private final Inventory inventory;
    private final MenuLayout layout;

    // Dynamically resolved slots
    private int inputSlot = -1;
    private int fuelSlot = -1;
    private int outputSlot = -1;
    private int collectSlot = -1;
    private int backSlot = -1;

    public FurnaceViewGui(PlayerFurnacesPlugin plugin, Player viewer, VirtualFurnace furnace) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.furnace = furnace;
        this.layout = plugin.getMenuManager().getLayout("furnace_view");

        String title = layout != null ? layout.getTitle().replace("{id}", String.valueOf(furnace.getFurnaceId())).replace("{status}", furnace.getStatus().name()) : "Furnace View";
        int size = layout != null ? layout.getSize() : 45;

        this.inventory = Bukkit.createInventory(this, size, title);
        resolveSlots();
        refresh();
    }

    private void resolveSlots() {
        if (layout == null) return;
        for (Map.Entry<Integer, MenuSlotData> entry : layout.getSlots().entrySet()) {
            int slot = entry.getKey();
            String type = entry.getValue().getType();
            if ("INPUT".equals(type)) inputSlot = slot;
            else if ("FUEL_SLOT".equals(type)) fuelSlot = slot;
            else if ("OUTPUT".equals(type)) outputSlot = slot;
            else if ("COLLECT".equals(type)) collectSlot = slot;
            else if ("BACK".equals(type)) backSlot = slot;
        }
    }

    public void refresh() {
        FurnaceEngine.updateFurnaceState(furnace);
        inventory.clear();
        
        if (layout == null) return;

        for (Map.Entry<Integer, MenuSlotData> entry : layout.getSlots().entrySet()) {
            int slot = entry.getKey();
            MenuSlotData data = entry.getValue();
            String type = data.getType();

            switch (type) {
                case "FILLER":
                case "COLLECT":
                case "BACK":
                    inventory.setItem(slot, createItem(data.getDefaultState(), furnace));
                    break;
                case "INPUT":
                    inventory.setItem(slot, furnace.getInputItem());
                    break;
                case "FUEL_SLOT":
                    inventory.setItem(slot, furnace.getFuelItem());
                    break;
                case "OUTPUT":
                    inventory.setItem(slot, furnace.getOutputItem());
                    break;
                case "PROGRESS":
                    if (furnace.getCookTime() > 0 && furnace.getTotalCookTime() > 0) {
                        inventory.setItem(slot, createItem(data.getState("active"), furnace));
                    } else {
                        inventory.setItem(slot, createItem(data.getState("waiting"), furnace));
                    }
                    break;
                case "FUEL_INDICATOR":
                    if (furnace.getBurnTime() > 0) {
                        inventory.setItem(slot, createItem(data.getState("active"), furnace));
                    } else {
                        inventory.setItem(slot, createItem(data.getState("inactive"), furnace));
                    }
                    break;
            }
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
                    String owner = stateData.getSkullOwner().replace("{player}", viewer != null ? viewer.getName() : "");
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
                } else if (stateData.getSkullTexture() != null && !stateData.getSkullTexture().isEmpty()) {
                    dev.darkblade.playerfurnaces.util.ColorUtils.applySkullTexture(skullMeta, stateData.getSkullTexture());
                }
            }

            String name = stateData.getName();
            if (name != null && !name.isEmpty() && furnace != null) {
                int pct = furnace.getTotalCookTime() > 0 ? (furnace.getCookTime() * 100) / furnace.getTotalCookTime() : 0;
                String replaced = name.replace("{id}", String.valueOf(furnace.getFurnaceId()))
                                      .replace("{pct}", String.valueOf(pct))
                                      .replace("{time}", String.valueOf(furnace.getBurnTime() / 20));
                meta.setDisplayName(dev.darkblade.playerfurnaces.util.ColorUtils.colorize(replaced));
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

    public VirtualFurnace getFurnace() {
        return furnace;
    }

    public MenuLayout getLayout() {
        return layout;
    }

    public int getInputSlot() { return inputSlot; }
    public int getFuelSlot() { return fuelSlot; }
    public int getOutputSlot() { return outputSlot; }
    public int getCollectSlot() { return collectSlot; }
    public int getBackSlot() { return backSlot; }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.model.FurnaceStatus;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FurnaceHubGui implements InventoryHolder {

    private final PlayerFurnacesPlugin plugin;
    private final Player viewer;
    private final Player targetOwner;
    private final Inventory inventory;

    public FurnaceHubGui(PlayerFurnacesPlugin plugin, Player viewer, Player targetOwner) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.targetOwner = targetOwner;
        String title = plugin.getMessageManager().getMessage("gui.hub.title", false, "{player}", targetOwner.getName());
        this.inventory = Bukkit.createInventory(this, 54, title);
        refresh();
    }

    public void refresh() {
        inventory.clear();
        int maxFurnaces = plugin.getConfig().getInt("settings.default-furnace-count", 14);

        for (int i = 1; i <= maxFurnaces; i++) {
            boolean hasPerm = plugin.getFurnaceManager().hasPermissionForFurnace(targetOwner, i);
            VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(targetOwner.getUniqueId(), i);
            FurnaceEngine.updateFurnaceState(furnace);

            ItemStack icon;
            ItemMeta meta;
            List<String> lore;

            if (!hasPerm) {
                icon = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                meta = icon.getItemMeta();
                meta.setDisplayName(plugin.getMessageManager().getMessage("gui.hub.locked.name", false, "{id}", String.valueOf(i)));
                lore = plugin.getMessageManager().getMessageList("gui.hub.locked.lore", "{id}", String.valueOf(i));
            } else {
                FurnaceStatus status = furnace.getStatus();
                String itemType = furnace.getInputItem() != null ? furnace.getInputItem().getType().name() : "Air";
                String itemAmount = furnace.getInputItem() != null ? String.valueOf(furnace.getInputItem().getAmount()) : "0";
                String remainingTime = String.valueOf(furnace.getBurnTime() / 20);

                switch (status) {
                    case SMELTING -> {
                        icon = new ItemStack(Material.BLAST_FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(plugin.getMessageManager().getMessage("gui.hub.smelting.name", false, "{id}", String.valueOf(i)));
                        lore = plugin.getMessageManager().getMessageList("gui.hub.smelting.lore",
                                "{id}", String.valueOf(i),
                                "{item}", itemType,
                                "{amount}", itemAmount,
                                "{time}", remainingTime);
                    }
                    case NO_FUEL -> {
                        icon = new ItemStack(Material.FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(plugin.getMessageManager().getMessage("gui.hub.no-fuel.name", false, "{id}", String.valueOf(i)));
                        lore = plugin.getMessageManager().getMessageList("gui.hub.no-fuel.lore", "{id}", String.valueOf(i));
                    }
                    default -> {
                        icon = new ItemStack(Material.FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(plugin.getMessageManager().getMessage("gui.hub.idle.name", false, "{id}", String.valueOf(i)));
                        lore = plugin.getMessageManager().getMessageList("gui.hub.idle.lore", "{id}", String.valueOf(i));
                    }
                }
            }

            if (meta != null) {
                meta.setLore(lore);
                icon.setItemMeta(meta);
            }
            inventory.setItem(i - 1, icon);
        }
    }

    public Player getTargetOwner() {
        return targetOwner;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

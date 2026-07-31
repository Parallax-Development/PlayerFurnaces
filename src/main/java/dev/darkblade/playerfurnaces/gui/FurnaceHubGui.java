package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.model.FurnaceStatus;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
        String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.hub-title", "&8Hornos de {player}")
                .replace("{player}", targetOwner.getName()));
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
            List<String> lore = new ArrayList<>();

            if (!hasPerm) {
                icon = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                meta = icon.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "Horno #" + i + " (Bloqueado)");
                lore.add(ChatColor.GRAY + "Requiere permiso: " + ChatColor.YELLOW + "playerfurnaces.furnace." + i);
            } else {
                FurnaceStatus status = furnace.getStatus();
                switch (status) {
                    case SMELTING -> {
                        icon = new ItemStack(Material.BLAST_FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + "Horno #" + i + " (Cocinando)");
                        lore.add(ChatColor.GRAY + "Estado: " + ChatColor.GREEN + "🟢 Fundiendo...");
                        if (furnace.getInputItem() != null) {
                            lore.add(ChatColor.GRAY + "Procesando: " + ChatColor.WHITE + furnace.getInputItem().getType().name() + " x" + furnace.getInputItem().getAmount());
                        }
                        lore.add(ChatColor.GRAY + "Combustible: " + ChatColor.YELLOW + (furnace.getBurnTime() / 20) + "s restantes");
                    }
                    case NO_FUEL -> {
                        icon = new ItemStack(Material.FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + "Horno #" + i + " (Sin Combustible)");
                        lore.add(ChatColor.GRAY + "Estado: " + ChatColor.RED + "🔴 Requiere combustible");
                    }
                    default -> {
                        icon = new ItemStack(Material.FURNACE);
                        meta = icon.getItemMeta();
                        meta.setDisplayName(ChatColor.YELLOW + "Horno #" + i + " (Inactivo)");
                        lore.add(ChatColor.GRAY + "Estado: " + ChatColor.GRAY + "⏸️ Listo para usar");
                    }
                }
                lore.add("");
                lore.add(ChatColor.YELLOW + "▶ Haz clic para abrir este horno");
            }

            meta.setLore(lore);
            icon.setItemMeta(meta);
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

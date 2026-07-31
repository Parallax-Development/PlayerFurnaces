package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
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

public class FurnaceViewGui implements InventoryHolder {

    public static final int INPUT_SLOT = 11;
    public static final int FUEL_SLOT = 29;
    public static final int OUTPUT_SLOT = 15;
    public static final int COLLECT_SLOT = 24;
    public static final int BACK_SLOT = 40;

    private final PlayerFurnacesPlugin plugin;
    private final Player viewer;
    private final VirtualFurnace furnace;
    private final Inventory inventory;

    public FurnaceViewGui(PlayerFurnacesPlugin plugin, Player viewer, VirtualFurnace furnace) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.furnace = furnace;

        String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.furnace-title", "&8Horno #{id}")
                .replace("{id}", String.valueOf(furnace.getFurnaceId()))
                .replace("{status}", furnace.getStatus().name()));

        this.inventory = Bukkit.createInventory(this, 45, title);
        refresh();
    }

    public void refresh() {
        FurnaceEngine.updateFurnaceState(furnace);

        ItemStack filler = createItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 45; i++) {
            if (i != INPUT_SLOT && i != FUEL_SLOT && i != OUTPUT_SLOT && i != COLLECT_SLOT && i != BACK_SLOT && i != 13 && i != 20) {
                inventory.setItem(i, filler);
            }
        }

        inventory.setItem(INPUT_SLOT, furnace.getInputItem());
        inventory.setItem(FUEL_SLOT, furnace.getFuelItem());
        inventory.setItem(OUTPUT_SLOT, furnace.getOutputItem());

        ItemStack progressItem;
        if (furnace.getCookTime() > 0 && furnace.getTotalCookTime() > 0) {
            int pct = (furnace.getCookTime() * 100) / furnace.getTotalCookTime();
            progressItem = createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Progreso: " + pct + "%");
        } else {
            progressItem = createItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "Esperando...");
        }
        inventory.setItem(13, progressItem);

        ItemStack fuelIndicator;
        if (furnace.getBurnTime() > 0) {
            fuelIndicator = createItem(Material.FIRE_CHARGE, ChatColor.GOLD + "Combustible Activo",
                    ChatColor.YELLOW + "Tiempo restante: " + (furnace.getBurnTime() / 20) + "s");
        } else {
            fuelIndicator = createItem(Material.COAL, ChatColor.RED + "Sin Combustible Activo");
        }
        inventory.setItem(20, fuelIndicator);

        ItemStack collectBtn = createItem(Material.HOPPER, ChatColor.GREEN + "Recoger Todo",
                ChatColor.GRAY + "Haz clic para recoger los productos procesados.");
        inventory.setItem(COLLECT_SLOT, collectBtn);

        ItemStack backBtn = createItem(Material.ARROW, ChatColor.YELLOW + "Volver al Menú Principal");
        inventory.setItem(BACK_SLOT, backBtn);
    }

    public VirtualFurnace getFurnace() {
        return furnace;
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                List<String> list = new ArrayList<>();
                for (String l : lore) {
                    list.add(l);
                }
                meta.setLore(list);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

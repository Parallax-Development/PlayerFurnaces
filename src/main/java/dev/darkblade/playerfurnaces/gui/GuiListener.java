package dev.darkblade.playerfurnaces.gui;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GuiListener implements Listener {

    private final PlayerFurnacesPlugin plugin;

    public GuiListener(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inv = event.getInventory();

        if (inv.getHolder() instanceof FurnaceHubGui hubGui) {
            event.setCancelled(true);
            int rawSlot = event.getRawSlot();

            if (rawSlot >= 0 && rawSlot < 54) {
                int furnaceId = rawSlot + 1;
                Player target = hubGui.getTargetOwner();

                if (!plugin.getFurnaceManager().hasPermissionForFurnace(target, furnaceId)) {
                    plugin.getMessageManager().sendMessage(player, "no-furnace-permission", "{id}", String.valueOf(furnaceId));
                    return;
                }

                VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(target.getUniqueId(), furnaceId);
                FurnaceEngine.updateFurnaceState(furnace);
                FurnaceViewGui viewGui = new FurnaceViewGui(plugin, player, furnace);
                player.openInventory(viewGui.getInventory());
            }
            return;
        }

        if (inv.getHolder() instanceof FurnaceViewGui viewGui) {
            int rawSlot = event.getRawSlot();
            VirtualFurnace furnace = viewGui.getFurnace();

            if (rawSlot >= 0 && rawSlot < 45) {
                if (rawSlot == FurnaceViewGui.COLLECT_SLOT) {
                    event.setCancelled(true);
                    collectOutput(player, furnace);
                    viewGui.refresh();
                    return;
                }

                if (rawSlot == FurnaceViewGui.BACK_SLOT) {
                    event.setCancelled(true);
                    syncFurnaceFromInventory(inv, furnace);
                    FurnaceHubGui hubGui = new FurnaceHubGui(plugin, player, player);
                    player.openInventory(hubGui.getInventory());
                    return;
                }

                if (rawSlot != FurnaceViewGui.INPUT_SLOT && rawSlot != FurnaceViewGui.FUEL_SLOT && rawSlot != FurnaceViewGui.OUTPUT_SLOT) {
                    event.setCancelled(true);
                    return;
                }

                if (rawSlot == FurnaceViewGui.OUTPUT_SLOT) {
                    ItemStack cursor = event.getCursor();
                    if (cursor != null && !cursor.getType().isAir()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                syncFurnaceFromInventory(inv, furnace);
                viewGui.refresh();
            });
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof FurnaceHubGui) {
            event.setCancelled(true);
            return;
        }

        if (event.getInventory().getHolder() instanceof FurnaceViewGui viewGui) {
            for (int slot : event.getRawSlots()) {
                if (slot < 45 && slot != FurnaceViewGui.INPUT_SLOT && slot != FurnaceViewGui.FUEL_SLOT) {
                    event.setCancelled(true);
                    return;
                }
            }
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                syncFurnaceFromInventory(event.getInventory(), viewGui.getFurnace());
                viewGui.refresh();
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof FurnaceViewGui viewGui) {
            syncFurnaceFromInventory(event.getInventory(), viewGui.getFurnace());
            plugin.getDatabaseManager().saveFurnace(viewGui.getFurnace());
        }
    }

    private void syncFurnaceFromInventory(Inventory inv, VirtualFurnace furnace) {
        furnace.setInputItem(inv.getItem(FurnaceViewGui.INPUT_SLOT));
        furnace.setFuelItem(inv.getItem(FurnaceViewGui.FUEL_SLOT));
        furnace.setOutputItem(inv.getItem(FurnaceViewGui.OUTPUT_SLOT));
        FurnaceEngine.updateFurnaceState(furnace);
    }

    private void collectOutput(Player player, VirtualFurnace furnace) {
        ItemStack output = furnace.getOutputItem();
        if (output != null && output.getAmount() > 0) {
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(output);
            if (leftover.isEmpty()) {
                furnace.setOutputItem(null);
                plugin.getMessageManager().sendMessage(player, "collection.success");
            } else {
                furnace.setOutputItem(leftover.get(0));
                plugin.getMessageManager().sendMessage(player, "collection.partial");
            }
        }
    }
}

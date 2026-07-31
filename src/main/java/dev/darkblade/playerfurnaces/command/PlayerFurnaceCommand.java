package dev.darkblade.playerfurnaces.command;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.gui.FurnaceHubGui;
import dev.darkblade.playerfurnaces.gui.FurnaceViewGui;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerFurnaceCommand implements CommandExecutor {

    private final PlayerFurnacesPlugin plugin;

    public PlayerFurnaceCommand(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser ejecutado por jugadores.");
            return true;
        }

        if (!player.hasPermission("playerfurnaces.command.use")) {
            player.sendMessage(ChatColor.RED + "No tienes permiso para utilizar este comando.");
            return true;
        }

        if (args.length == 0) {
            FurnaceHubGui hub = new FurnaceHubGui(plugin, player, player);
            player.openInventory(hub.getInventory());
            return true;
        }

        try {
            int furnaceId = Integer.parseInt(args[0]);
            int max = plugin.getConfig().getInt("settings.default-furnace-count", 14);
            if (furnaceId < 1 || furnaceId > max) {
                player.sendMessage(ChatColor.RED + "El ID del horno debe estar entre 1 y " + max);
                return true;
            }

            if (!plugin.getFurnaceManager().hasPermissionForFurnace(player, furnaceId)) {
                player.sendMessage(ChatColor.RED + "No tienes permiso para acceder al Horno #" + furnaceId);
                return true;
            }

            VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(player.getUniqueId(), furnaceId);
            FurnaceEngine.updateFurnaceState(furnace);
            FurnaceViewGui viewGui = new FurnaceViewGui(plugin, player, furnace);
            player.openInventory(viewGui.getInventory());
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Uso: /furnace [id]");
        }

        return true;
    }
}

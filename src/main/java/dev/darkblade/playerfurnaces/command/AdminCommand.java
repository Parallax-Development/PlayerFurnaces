package dev.darkblade.playerfurnaces.command;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.gui.FurnaceHubGui;
import dev.darkblade.playerfurnaces.gui.FurnaceViewGui;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    private final PlayerFurnacesPlugin plugin;

    public AdminCommand(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("playerfurnaces.admin")) {
            plugin.getMessageManager().sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getRecipeManager().loadRecipes();
            plugin.getFuelManager().loadFuels();
            plugin.getMessageManager().reloadMessages();
            plugin.getMessageManager().sendMessage(sender, "reload-success");
            return true;
        }

        if (args[0].equalsIgnoreCase("view")) {
            if (!(sender instanceof Player admin)) {
                plugin.getMessageManager().sendMessage(sender, "only-players");
                return true;
            }

            if (args.length < 2) {
                plugin.getMessageManager().sendMessage(admin, "usage-admin-view");
                return true;
            }

            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

            if (args.length == 2) {
                if (target.isOnline()) {
                    FurnaceHubGui hub = new FurnaceHubGui(plugin, admin, target.getPlayer());
                    admin.openInventory(hub.getInventory());
                } else {
                    plugin.getMessageManager().sendMessage(admin, "player-offline", "{player}", targetName);
                }
                return true;
            }

            try {
                int furnaceId = Integer.parseInt(args[2]);
                VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(target.getUniqueId(), furnaceId);
                FurnaceEngine.updateFurnaceState(furnace);
                FurnaceViewGui viewGui = new FurnaceViewGui(plugin, admin, furnace);
                admin.openInventory(viewGui.getInventory());
            } catch (NumberFormatException e) {
                plugin.getMessageManager().sendMessage(admin, "admin-furnace-id-number");
            }
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        plugin.getMessageManager().sendListMessage(sender, "admin-help");
    }
}

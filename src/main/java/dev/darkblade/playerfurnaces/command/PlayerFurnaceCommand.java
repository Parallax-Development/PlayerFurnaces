package dev.darkblade.playerfurnaces.command;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.gui.FurnaceHubGui;
import dev.darkblade.playerfurnaces.gui.FurnaceViewGui;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerFurnaceCommand implements TabExecutor {

    private final PlayerFurnacesPlugin plugin;

    public PlayerFurnaceCommand(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getMessageManager().sendMessage(sender, "only-players");
            return true;
        }

        if (!player.hasPermission("playerfurnaces.command.use")) {
            plugin.getMessageManager().sendMessage(player, "no-permission");
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
                plugin.getMessageManager().sendMessage(player, "furnace-id-invalid", "{max}", String.valueOf(max));
                return true;
            }

            if (!plugin.getFurnaceManager().hasPermissionForFurnace(player, furnaceId)) {
                plugin.getMessageManager().sendMessage(player, "no-furnace-permission", "{id}", String.valueOf(furnaceId));
                return true;
            }

            VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(player.getUniqueId(), furnaceId);
            FurnaceEngine.updateFurnaceState(furnace);
            FurnaceViewGui viewGui = new FurnaceViewGui(plugin, player, furnace);
            player.openInventory(viewGui.getInventory());
        } catch (NumberFormatException e) {
            plugin.getMessageManager().sendMessage(player, "usage-furnace");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }

        if (!player.hasPermission("playerfurnaces.command.use")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            int max = plugin.getConfig().getInt("settings.default-furnace-count", 14);
            List<String> validIds = new ArrayList<>();
            for (int i = 1; i <= max; i++) {
                if (plugin.getFurnaceManager().hasPermissionForFurnace(player, i)) {
                    validIds.add(String.valueOf(i));
                }
            }
            return StringUtil.copyPartialMatches(args[0], validIds, new ArrayList<>());
        }

        return Collections.emptyList();
    }
}

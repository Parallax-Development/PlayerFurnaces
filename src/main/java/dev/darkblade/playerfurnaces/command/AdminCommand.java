package dev.darkblade.playerfurnaces.command;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.gui.FurnaceHubGui;
import dev.darkblade.playerfurnaces.gui.FurnaceViewGui;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

public class AdminCommand implements TabExecutor {

    private final PlayerFurnacesPlugin plugin;
    private static final List<String> SUBCOMMANDS = List.of("reload", "view", "force-open");

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
            plugin.getMenuManager().loadMenus();
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

        if (args[0].equalsIgnoreCase("force-open")) {
            if (args.length < 3) {
                sender.sendMessage("§cUsage: /pfa force-open <player> <index> [--bypass-perms]");
                return true;
            }

            String targetName = args[1];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null || !target.isOnline()) {
                plugin.getMessageManager().sendMessage(sender, "player-offline", "{player}", targetName);
                return true;
            }

            if (target.isDead()) {
                sender.sendMessage("§cCannot force open furnace for a dead player.");
                return true;
            }

            int furnaceId;
            try {
                furnaceId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                plugin.getMessageManager().sendMessage(sender, "admin-furnace-id-number");
                return true;
            }

            int max = plugin.getConfig().getInt("settings.default-furnace-count", 14);
            if (furnaceId < 1 || furnaceId > max) {
                sender.sendMessage("§cFurnace index must be between 1 and " + max + ".");
                return true;
            }

            boolean bypassPerms = false;
            if (args.length >= 4 && (args[3].equalsIgnoreCase("--bypass-perms") || args[3].equalsIgnoreCase("-b"))) {
                bypassPerms = true;
            }

            boolean hasPerm = plugin.getFurnaceManager().hasPermissionForFurnace(target, furnaceId);
            if (!bypassPerms && !hasPerm) {
                sender.sendMessage("§cPlayer does not have permission for furnace " + furnaceId + ". Use --bypass-perms to force.");
                return true;
            }

            target.closeInventory();
            VirtualFurnace furnace = plugin.getFurnaceManager().getOrCreateFurnace(target.getUniqueId(), furnaceId);
            FurnaceEngine.updateFurnaceState(furnace);
            FurnaceViewGui viewGui = new FurnaceViewGui(plugin, target, furnace);
            target.openInventory(viewGui.getInventory());

            sender.sendMessage("§aSuccessfully forced " + target.getName() + " to open furnace " + furnaceId + ".");
            return true;
        }

        sendHelp(sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("playerfurnaces.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, new ArrayList<>());
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("force-open"))) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return StringUtil.copyPartialMatches(args[1], playerNames, new ArrayList<>());
        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("force-open"))) {
            int max = plugin.getConfig().getInt("settings.default-furnace-count", 14);
            List<String> validIds = new ArrayList<>();
            for (int i = 1; i <= max; i++) {
                validIds.add(String.valueOf(i));
            }
            return StringUtil.copyPartialMatches(args[2], validIds, new ArrayList<>());
        }
        
        if (args.length == 4 && args[0].equalsIgnoreCase("force-open")) {
            return StringUtil.copyPartialMatches(args[3], List.of("--bypass-perms"), new ArrayList<>());
        }

        return Collections.emptyList();
    }

    private void sendHelp(CommandSender sender) {
        plugin.getMessageManager().sendListMessage(sender, "admin-help");
    }
}

package dev.darkblade.playerfurnaces.command;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.gui.FurnaceHubGui;
import dev.darkblade.playerfurnaces.gui.FurnaceViewGui;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            sender.sendMessage(ChatColor.RED + "No tienes permiso para ejecutar comandos administrativos.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "¡Configuración de PlayerFurnaces recargada correctamente!");
            return true;
        }

        if (args[0].equalsIgnoreCase("view")) {
            if (!(sender instanceof Player admin)) {
                sender.sendMessage(ChatColor.RED + "Este comando solo puede ser ejecutado por un jugador.");
                return true;
            }

            if (args.length < 2) {
                admin.sendMessage(ChatColor.RED + "Uso: /pfadmin view <jugador> [id]");
                return true;
            }

            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

            if (args.length == 2) {
                if (target.isOnline()) {
                    FurnaceHubGui hub = new FurnaceHubGui(plugin, admin, target.getPlayer());
                    admin.openInventory(hub.getInventory());
                } else {
                    admin.sendMessage(ChatColor.RED + "El jugador " + targetName + " no está en línea.");
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
                admin.sendMessage(ChatColor.RED + "El ID del horno debe ser un número entero.");
            }
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "=== PlayerFurnaces Admin Commands ===");
        sender.sendMessage(ChatColor.WHITE + "/pfadmin view <jugador> [id] " + ChatColor.GRAY + "- Inspecciona los hornos de un jugador");
        sender.sendMessage(ChatColor.WHITE + "/pfadmin reload " + ChatColor.GRAY + "- Recarga la configuración del plugin");
    }
}

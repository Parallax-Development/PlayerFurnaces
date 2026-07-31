package dev.darkblade.playerfurnaces.listener;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerFurnacesPlugin plugin;

    public PlayerListener(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getFurnaceManager().loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getFurnaceManager().unloadPlayer(event.getPlayer().getUniqueId());
    }
}

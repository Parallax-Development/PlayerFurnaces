package dev.darkblade.playerfurnaces.manager;

import dev.darkblade.playerfurnaces.PlayerFurnacesPlugin;
import dev.darkblade.playerfurnaces.engine.FurnaceEngine;
import dev.darkblade.playerfurnaces.model.VirtualFurnace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class FurnaceManager {

    private final PlayerFurnacesPlugin plugin;
    private final Map<UUID, Map<Integer, VirtualFurnace>> cache = new ConcurrentHashMap<>();

    public FurnaceManager(PlayerFurnacesPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayer(UUID uuid) {
        plugin.getDatabaseManager().loadFurnaces(uuid).thenAccept(list -> {
            Map<Integer, VirtualFurnace> map = new ConcurrentHashMap<>();
            for (VirtualFurnace f : list) {
                map.put(f.getFurnaceId(), f);
            }
            cache.put(uuid, map);
        });
    }

    public void unloadPlayer(UUID uuid) {
        Map<Integer, VirtualFurnace> map = cache.remove(uuid);
        if (map != null) {
            for (VirtualFurnace f : map.values()) {
                FurnaceEngine.updateFurnaceState(f);
                plugin.getDatabaseManager().saveFurnace(f);
            }
        }
    }

    public VirtualFurnace getOrCreateFurnace(UUID uuid, int id) {
        Map<Integer, VirtualFurnace> map = cache.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        return map.computeIfAbsent(id, k -> new VirtualFurnace(uuid, id));
    }

    public boolean hasPermissionForFurnace(Player player, int furnaceId) {
        if (player.hasPermission("playerfurnaces.admin") || player.hasPermission("playerfurnaces.furnace.*")) {
            return true;
        }
        return player.hasPermission("playerfurnaces.furnace." + furnaceId);
    }

    public void saveAll() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Map<Integer, VirtualFurnace> map : cache.values()) {
            for (VirtualFurnace f : map.values()) {
                FurnaceEngine.updateFurnaceState(f);
                futures.add(plugin.getDatabaseManager().saveFurnace(f));
            }
        }
        if (!futures.isEmpty()) {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    public void startTickTask() {
        long refreshInterval = plugin.getConfig().getLong("settings.gui-refresh-ticks", 10L);
        if (refreshInterval < 1L) refreshInterval = 10L;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map<Integer, VirtualFurnace> map : cache.values()) {
                for (VirtualFurnace f : map.values()) {
                    FurnaceEngine.updateFurnaceState(f);
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTopInventory().getHolder() instanceof dev.darkblade.playerfurnaces.gui.FurnaceHubGui hubGui) {
                    hubGui.refresh();
                } else if (player.getOpenInventory().getTopInventory().getHolder() instanceof dev.darkblade.playerfurnaces.gui.FurnaceViewGui viewGui) {
                    viewGui.refresh();
                }
            }
        }, refreshInterval, refreshInterval);
    }

}

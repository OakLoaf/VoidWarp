package me.dave.voidwarp.modes.warp;

import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.data.VoidModes;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

abstract class AbstractWarpMode implements VoidModes {

    /**
     * Get the closest warp to the player
     *
     * @param player Player to get the closest warp to
     * @param warps List of warp names that will be checked
     */
    public abstract CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps);

    /**
     * Get the warp data for the closest warp to the player
     *
     * @param player Player to teleport
     * @param worldData Relevant world data
     */
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        if (worldData.warps().size() == 0) {
            completableFuture.complete(null);
            return completableFuture;
        }

        getClosestWarp(player, worldData.warps()).thenAccept(warp -> {
            if (warp == null) completableFuture.complete(new WarpData("Spawn", player.getWorld().getSpawnLocation()));
            else completableFuture.complete(warp);
        });

        return completableFuture;
    }
}

package me.dave.voidwarp.modes.wap;

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
     * Teleport a player to the closest warp to them
     *
     * @param player Player to teleport
     * @param warp Warp to teleport to
     */
    public CompletableFuture<Boolean> teleport(Player player, WarpData warp) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        if (warp == null) completableFuture.complete(false);
        else completableFuture.complete(player.teleport(warp.getLocation()));

        return completableFuture;
    }

    /**
     * Teleport a player to the closest warp to them
     *
     * @param player Player to teleport
     * @param worldData Relevant world data
     */
    public CompletableFuture<String> run(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        if (worldData.warps().size() == 0) {
            completableFuture.complete(null);
            return completableFuture;
        }

        getClosestWarp(player, worldData.warps()).thenAccept(warp -> {
            if (warp == null) warp = new WarpData("Spawn", player.getWorld().getSpawnLocation());
            final WarpData finalWarp = warp;

            teleport(player, warp).thenAccept(teleported -> {
                if (teleported) completableFuture.complete(finalWarp.getName());
                else completableFuture.complete(null);
            });
        });

        return completableFuture;
    }
}

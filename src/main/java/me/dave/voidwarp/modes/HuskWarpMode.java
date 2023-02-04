package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.HuskHomesHook;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class HuskWarpMode implements VoidModes {

    public CompletableFuture<String> run(Player player, World world, ConfigManager.WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        WarpMode.WarpData closestWarp = huskHomeGetClosestWarp(player, world, worldData.warps());
        if (closestWarp == null) closestWarp = new WarpMode.WarpData("Spawn", world.getSpawnLocation());

        if (player.teleport(closestWarp.getLocation())) completableFuture.complete(closestWarp.getName());
        else completableFuture.complete(null);

        return completableFuture;
    }

    private WarpMode.WarpData huskHomeGetClosestWarp(Player player, World world, Collection<String> warps) {
        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) return null;

        return huskHomesAPI.getClosestWarp(player, world, warps);
    }
}

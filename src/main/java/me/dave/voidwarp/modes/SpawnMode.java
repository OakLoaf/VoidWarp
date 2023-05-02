package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsSpawnHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.data.VoidModes;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class SpawnMode implements VoidModes {

    public CompletableFuture<String> run(Player player, WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        World world = player.getWorld();

        tpHuskHomeSpawn(player).thenAccept(success -> {
            if (!success) {
                if (!tpEssentialsSpawn(player)) {
                    if (player.teleport(world.getSpawnLocation())) {
                        completableFuture.complete("Spawn");
                    }
                    else completableFuture.complete(null);
                }
                else completableFuture.complete("Spawn");
            }
            else completableFuture.complete("Spawn");
        });

        return completableFuture;
    }

    private boolean tpEssentialsSpawn(Player player) {
        EssentialsSpawnHook essentialsSpawn = VoidWarp.essentialsSpawnAPI();
        if (essentialsSpawn == null) return false;

        return player.teleport(essentialsSpawn.getSpawn("default"));
    }

    private CompletableFuture<Boolean> tpHuskHomeSpawn(Player player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) completableFuture.complete(false);
        else huskHomesAPI.getSpawn().thenAccept(spawn -> huskHomesAPI.teleportPlayer(player, spawn).thenAccept(completableFuture::complete));

        return completableFuture;
    }
}

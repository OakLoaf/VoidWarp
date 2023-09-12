package me.dave.voidwarp.modes;

import me.dave.voidwarp.config.ConfigManager.WorldData;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsSpawnHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.data.VoidModes;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class SpawnMode implements VoidModes {

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        World world = player.getWorld();

        getHuskHomeSpawn(player).thenAccept(huskSpawn -> {
            if (huskSpawn == null) {
                Location essentialsSpawn = getEssentialsSpawn();
                if (essentialsSpawn == null) completableFuture.complete(new WarpData("Spawn", world.getSpawnLocation()));
                else completableFuture.complete(new WarpData("Spawn", essentialsSpawn));
            }
            else {
                completableFuture.complete(new WarpData("Spawn", huskSpawn));
            }
        });

        return completableFuture;
    }

    private Location getEssentialsSpawn() {
        EssentialsSpawnHook essentialsSpawn = VoidWarp.essentialsSpawnAPI();
        if (essentialsSpawn == null) return null;

        return essentialsSpawn.getSpawn("default");
    }

    private CompletableFuture<Runnable> getHuskHomeSpawn(Player player) {
        CompletableFuture<Runnable> completableFuture = new CompletableFuture<>();

        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) completableFuture.complete(null);
        else huskHomesAPI.getSpawn().thenAccept(spawn -> completableFuture.complete(() -> huskHomesAPI.teleportPlayer(player, spawn)));

        return completableFuture;
    }
}

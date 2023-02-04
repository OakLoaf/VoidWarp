package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class WarpMode implements VoidModes {

    public CompletableFuture<String> run(Player player, World world, WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        WarpData closestWarp = huskHomeGetClosestWarp(player, world, worldData.warps());
        if (closestWarp == null) {
            closestWarp = essentialsGetClosestWarp(player, world, worldData.warps());
        }
        if (closestWarp == null) {
            closestWarp = new WarpData("Spawn", world.getSpawnLocation());
        }

        if (player.teleport(closestWarp.location)) completableFuture.complete(closestWarp.name);
        else completableFuture.complete(null);

        return completableFuture;
    }

    private WarpData essentialsGetClosestWarp(Player player, World world, Collection<String> warps) {
        EssentialsHook essentials = VoidWarp.essentialsAPI();
        if (essentials == null) return null;

        return essentials.getClosestWarp(player, world, warps);
    }

    private WarpData huskHomeGetClosestWarp(Player player, World world, Collection<String> warps) {
        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) return null;

        return huskHomesAPI.getClosestWarp(player, world, warps);
    }

    public record WarpData(String name, Location location) {
        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }
    }
}

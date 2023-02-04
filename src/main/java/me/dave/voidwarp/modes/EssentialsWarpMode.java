package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsHook;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class EssentialsWarpMode implements VoidModes {

    public CompletableFuture<String> run(Player player, World world, ConfigManager.WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        WarpMode.WarpData closestWarp = essentialsGetClosestWarp(player, world, worldData.warps());
        if (closestWarp == null) closestWarp = new WarpMode.WarpData("Spawn", world.getSpawnLocation());

        if (player.teleport(closestWarp.getLocation())) completableFuture.complete(closestWarp.getName());
        else completableFuture.complete(null);

        return completableFuture;
    }

    private WarpMode.WarpData essentialsGetClosestWarp(Player player, World world, Collection<String> warps) {
        EssentialsHook essentials = VoidWarp.essentialsAPI();
        if (essentials == null) return null;

        return essentials.getClosestWarp(player, world, warps);
    }
}

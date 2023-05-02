package me.dave.voidwarp.modes;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class WarpMode extends AbstractWarpMode {

    @Override
    public CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        huskHomeGetClosestWarp(player, warps).thenAccept(closestWarp -> {
            if (closestWarp == null) {
                closestWarp = warpSystemGetClosestWarp(player, warps);

                if (closestWarp == null) {
                    closestWarp = essentialsGetClosestWarp(player, warps);

                    if (closestWarp == null) {
                        closestWarp = new WarpData("Spawn", player.getWorld().getSpawnLocation());
                    }
                }
            }
            completableFuture.complete(closestWarp);
        });

        return completableFuture;
    }

    private WarpData essentialsGetClosestWarp(Player player, Collection<String> warps) {
        EssentialsHook essentials = VoidWarp.essentialsAPI();
        if (essentials == null) return null;
        return essentials.getClosestWarp(player, warps);
    }

    private CompletableFuture<WarpData> huskHomeGetClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) completableFuture.complete(null);
        else huskHomesAPI.getClosestWarp(player, warps).thenAccept(completableFuture::complete);

        return completableFuture;
    }

    private WarpData warpSystemGetClosestWarp(Player player, Collection<String> warps) {
        if (VoidWarp.warpSystemAPI() == null) return null;
        return VoidWarp.warpSystemAPI().getClosestWarp(player, warps);
    }
}

package me.dave.voidwarp.modes;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class HuskWarpMode extends AbstractWarpMode {

    @Override
    public CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
        if (huskHomesAPI == null) completableFuture.complete(null);
        else huskHomesAPI.getClosestWarp(player, warps).thenAccept(completableFuture::complete);

        return completableFuture;
    }
}

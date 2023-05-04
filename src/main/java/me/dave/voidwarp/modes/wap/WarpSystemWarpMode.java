package me.dave.voidwarp.modes.wap;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.WarpSystemHook;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class WarpSystemWarpMode extends AbstractWarpMode {

    @Override
    public CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        WarpSystemHook warpSystemAPI = VoidWarp.warpSystemAPI();
        if (warpSystemAPI == null) return null;
        else completableFuture.complete(warpSystemAPI.getClosestWarp(player, warps));

        return completableFuture;
    }
}

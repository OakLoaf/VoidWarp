package me.dave.voidwarp.modes.wap;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class EssentialsWarpMode extends AbstractWarpMode {

    @Override
    public CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        EssentialsHook essentials = VoidWarp.essentialsAPI();
        if (essentials == null) completableFuture.complete(null);
        else completableFuture.complete(essentials.getClosestWarp(player, warps));

        return completableFuture;
    }
}

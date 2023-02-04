package me.dave.voidwarp.apis;

import me.dave.voidwarp.modes.WarpMode;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.position.Warp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HuskHomesHook {
    private final HuskHomesAPI huskHomes = HuskHomesAPI.getInstance();

    public CompletableFuture<Collection<String>> getWarps() {
        CompletableFuture<Collection<String>> completableFuture = new CompletableFuture<>();

        huskHomes.getWarps().thenAccept((warps) -> {
            List<String> outputWarps = new ArrayList<>();
            for (Warp warp : warps) {
                outputWarps.add(warp.meta.name);
            }
            completableFuture.complete(outputWarps);
        });

        return completableFuture;
    }

    public CompletableFuture<Warp> getWarp(String warpName) {
        CompletableFuture<Warp> completableFuture = new CompletableFuture<>();

        huskHomes.getWarp(warpName).thenAccept((warp) -> {
            if (warp.isEmpty()) {
                completableFuture.complete(null);
                return;
            }
            completableFuture.complete(warp.get());
        });

        return completableFuture;
    }

    public WarpMode.WarpData getClosestWarp(Player player, World world, Collection<String> warps) {
        List<CompletableFuture<Warp>> futureList = new ArrayList<>();

        for (String warpName : warps) {
            CompletableFuture<Warp> warpFuture = getWarp(warpName);
            futureList.add(warpFuture);
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        try {
            combinedFuture.get();
        } catch (ExecutionException | InterruptedException err) {
            throw new RuntimeException(err);
        }

        Location playerLoc = player.getLocation();
        double minDistance = Double.MAX_VALUE;
        WarpMode.WarpData closestWarp = null;

        for (CompletableFuture<Warp> future : futureList) {
            assert(future.isDone());

            try {
                Warp warp = future.get();
                if (warp == null) continue;
                Location warpLoc = huskHomes.getLocation(warp);
                if (warpLoc == null || warpLoc.getWorld() != world) continue;
                double warpDistance = warpLoc.distanceSquared(playerLoc);
                if (warpDistance < minDistance) {
                    minDistance = (warpDistance);
                    closestWarp = new WarpMode.WarpData(warp.meta.name, warpLoc);
                }
            } catch (InterruptedException | ExecutionException err) {
                throw new RuntimeException(err);
            }
        }

        return closestWarp;
    }

    public CompletableFuture<Position> getSpawn() {
        CompletableFuture<Position> completableFuture = new CompletableFuture<>();

        huskHomes.getSpawn().thenAccept(spawn -> {
            if (spawn.isEmpty()) {
                completableFuture.complete(null);
                return;
            }
            completableFuture.complete(spawn.get());
        });

        return completableFuture;
    }

    public CompletableFuture<Boolean> teleportPlayer(Player player, Position position) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        huskHomes.teleportBuilder(huskHomes.adaptUser(player))
            .setTarget(position)
            .toTeleport()
            .thenAccept(teleport -> teleport.execute()
                .thenAccept(tpResult -> {
                    completableFuture.complete(tpResult.successful());
                })
            );

        return completableFuture;
    }
}

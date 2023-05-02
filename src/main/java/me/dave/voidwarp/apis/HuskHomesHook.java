package me.dave.voidwarp.apis;

import me.dave.voidwarp.data.WarpData;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.position.Warp;
import net.william278.huskhomes.teleport.TeleportationException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HuskHomesHook {
    private final HuskHomesAPI huskHomes = HuskHomesAPI.getInstance();

    public CompletableFuture<Collection<String>> getWarps() {
        CompletableFuture<Collection<String>> completableFuture = new CompletableFuture<>();

        huskHomes.getWarps().thenAccept((warps) -> {
            List<String> outputWarps = new ArrayList<>();
            for (Warp warp : warps) {
                outputWarps.add(warp.getName());
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

    public CompletableFuture<WarpData> getClosestWarp(Player player, Collection<String> warps) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        Location playerLoc = player.getLocation();

        huskHomes.getLocalWarps().thenAccept(huskWarps -> {
            huskWarps = huskWarps.stream().filter(warp -> warps.contains(warp.getName())).toList();

            double minDistance = Double.MAX_VALUE;
            WarpData closestWarp = null;
            for (Warp warp : huskWarps) {
                if (warp == null) continue;
                Location warpLoc = huskHomes.getLocation(warp);
                if (warpLoc == null || warpLoc.getWorld() != playerLoc.getWorld()) continue;
                double warpDistance = warpLoc.distanceSquared(playerLoc);
                if (warpDistance < minDistance) {
                    minDistance = (warpDistance);
                    closestWarp = new WarpData(warp.getName(), warpLoc);
                }
            }

            completableFuture.complete(closestWarp);
        });

        return completableFuture;
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

        boolean success = false;
        try {
            huskHomes.teleportBuilder(huskHomes.adaptUser(player))
                .target(position)
                .toTeleport()
                .execute();

            success = true;
        } catch(TeleportationException exc) {
            exc.printStackTrace();
        }

        completableFuture.complete(success);
        return completableFuture;
    }
}

package me.dave.voidwarp.mode;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public class BounceMode extends VoidMode<BounceMode.BounceModeData> {

    public BounceMode(BounceModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        WarpData warpData = new WarpData("boing", () -> {
            Vector velocity = data.getCenter().toVector().subtract(player.getLocation().toVector())
                .multiply(data.getSpeed())
                .setY(0.5);

            player.setVelocity(velocity);
        });

        completableFuture.complete(warpData);
        return completableFuture;
    }

    public static class BounceModeData extends VoidModeData {
        private final Location center;
        private final double speed;

        public BounceModeData(String name, Location center, double speed) {
            super(name);
            this.center = center;
            this.speed = speed;
        }

        public Location getCenter() {
            return center;
        }

        public double getSpeed() {
            return speed;
        }
    }
}

package me.dave.voidwarp.mode;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class BounceMode extends VoidMode<BounceMode.BounceModeData> {

    public BounceMode(BounceModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        return null;
    }

    public static class BounceModeData extends VoidModeData {
        private final Location center;

        public BounceModeData(String name, Location center) {
            super(name);
            this.center = center;
        }

        public Location getCenter() {
            return center;
        }
    }
}

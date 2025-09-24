package org.lushplugins.voidwarp.mode;

import org.lushplugins.voidwarp.config.ConfigManager;
import org.lushplugins.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class LocationMode extends VoidMode<LocationMode.LocationModeData> {

    public LocationMode(LocationModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        return CompletableFuture.completedFuture(new WarpData(data.getName(), data.getLocation()));
    }

    public static class LocationModeData extends VoidModeData {
        private final Location location;

        public LocationModeData(String name, @Nullable Location location) {
            super(name);

            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }
}

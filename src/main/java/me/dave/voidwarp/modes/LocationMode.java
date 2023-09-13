package me.dave.voidwarp.modes;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.VoidMode;
import me.dave.voidwarp.data.VoidModeData;
import me.dave.voidwarp.data.WarpData;
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

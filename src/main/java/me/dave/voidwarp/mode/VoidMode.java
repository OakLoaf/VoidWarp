package me.dave.voidwarp.mode;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public abstract class VoidMode<T extends VoidModeData> {
    protected final T data;

    public VoidMode(T data) {
        this.data = data;
    }

    /**
     * Get the warp data to be used
     *
     * @param player Player to teleport
     * @param worldData Relevant world data
     */
    public abstract CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData);

    public T getData() {
        return data;
    }
}

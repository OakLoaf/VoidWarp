package me.dave.voidwarp.events;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.VoidMode;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.modes.CommandMode;
import me.dave.voidwarp.modes.LocationMode;
import me.dave.voidwarp.modes.SpawnMode;
import me.dave.voidwarp.modes.WarpMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerEvents implements Listener {
    private final HashSet<UUID> processingList = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Checks if player bypasses VoidWarp
        if (player.hasPermission("voidwarp.admin.bypass")) {
            return;
        }

        UUID playerUniqueId = player.getUniqueId();
        // Checks if player teleportation is currently being processed
        if (processingList.contains(player.getUniqueId())) {
            return;
        }

        // Gets and checks this world's WorldData
        World world = player.getWorld();
        ConfigManager.WorldData worldData = VoidWarp.configManager.getWorldData(world.getName());
        if (worldData == null) {
            return;
        }

        // Checks if the player is within the set height range
        double currYHeight = player.getLocation().getY();
        if (currYHeight <= worldData.yMin() || currYHeight >= worldData.yMax()) {
            return;
        }

        // Gets the world's VoidMode
        VoidMode<?> voidMode;
        switch (worldData.modeType()) {
            case COMMAND -> voidMode = new CommandMode((CommandMode.CommandModeData) worldData.data());
            case LOCATION -> voidMode = new LocationMode((LocationMode.LocationModeData) worldData.data());
            case SPAWN -> voidMode = new SpawnMode((SpawnMode.SpawnModeData) worldData.data());
            case WARP -> voidMode = new WarpMode((WarpMode.WarpModeData) worldData.data());
            default -> throw new IllegalArgumentException("Invalid mode specified");
        }

        // Attempts to teleport the player using the specified VoidMode
        processingList.add(playerUniqueId);
        voidMode.getWarpData(player, worldData)
            .completeOnTimeout(null, 5000, TimeUnit.MILLISECONDS)
            .thenAccept(warpData -> {
                if (warpData == null) {
                    // TODO: Make message configurable
                    ChatColorHandler.sendActionBarMessage(player, "&cFailed to warp");
                    processingList.remove(playerUniqueId);
                    return;
                }

                // Teleports the player if a location is provided
                Location location = warpData.getLocation();
                if (location != null) {
                    player.setFallDistance(0);
                    player.teleport(location);
                }

                // Executes runnable actions
                Runnable runnable = warpData.getRunnable();
                if (runnable != null) {
                    runnable.run();
                }

                // Creates and sends Action Bar message
                String message = null;
                String destinationName = warpData.getName();
                if (destinationName != null) {
                    message = worldData.message().replaceAll("%location%", destinationName);
                }
                if (message != null) {
                    ChatColorHandler.sendActionBarMessage(player, message);
                }

                // Remove the player from the processing list
                processingList.remove(playerUniqueId);
            });

        // TODO: Create some form of backup that removes the player if for whatever reason they aren't removed
    }
}

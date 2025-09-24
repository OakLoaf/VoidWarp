package org.lushplugins.voidwarp.listener;

import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.voidwarp.config.ConfigManager;
import org.lushplugins.voidwarp.hook.WorldGuardHook;
import org.lushplugins.voidwarp.mode.*;
import org.lushplugins.voidwarp.VoidWarp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
    private final HashMap<UUID, Long> processingMap = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Checks if player bypasses VoidWarp
        if (player.hasPermission("voidwarp.admin.bypass")) {
            return;
        }

        UUID playerUniqueId = player.getUniqueId();
        Long timeout = processingMap.get(playerUniqueId);
        // Checks if player teleportation is currently being processed
        if (timeout != null && timeout >= Instant.now().getEpochSecond()) {
            return;
        }
        processingMap.remove(playerUniqueId);

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

        if (VoidWarp.isPluginAvailable(WorldGuardHook.PLUGIN_NAME) && VoidWarp.getOrLoadHook(WorldGuardHook.PLUGIN_NAME) instanceof WorldGuardHook hook && !hook.isRegionEnabled(player)) {
            return;
        }

        // Gets the world's VoidMode
        VoidMode<?> voidMode;
        switch (worldData.modeType()) {
            case BOUNCE -> voidMode = new BounceMode((BounceMode.BounceModeData) worldData.data());
            case COMMAND -> voidMode = new CommandMode((CommandMode.CommandModeData) worldData.data());
            case LOCATION -> voidMode = new LocationMode((LocationMode.LocationModeData) worldData.data());
            case SPAWN -> voidMode = new SpawnMode((SpawnMode.SpawnModeData) worldData.data());
            case WARP -> voidMode = new WarpMode((WarpMode.WarpModeData) worldData.data());
            default -> throw new IllegalArgumentException("Invalid mode specified");
        }

        // Attempts to teleport the player using the specified VoidMode
        processingMap.put(playerUniqueId, Instant.now().plusSeconds(6).getEpochSecond());
        voidMode.getWarpData(player, worldData)
            .completeOnTimeout(null, 5000, TimeUnit.MILLISECONDS)
            .thenAccept(warpData -> {
                if (warpData == null) {
                    // TODO: Make message configurable
                    ChatColorHandler.sendActionBarMessage(player, "&cFailed to warp");
                    processingMap.remove(playerUniqueId);
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
                String message = worldData.message();
                String destinationName = warpData.getName();
                if (message != null) {
                    if (destinationName != null) {
                        message = message.replaceAll("%location%", destinationName);
                    }

                    ChatColorHandler.sendActionBarMessage(player, message);
                }

                // Remove the player from the processing list
                processingMap.remove(playerUniqueId);
            });

        // TODO: Create some form of backup that removes the player if for whatever reason they aren't removed
    }
}

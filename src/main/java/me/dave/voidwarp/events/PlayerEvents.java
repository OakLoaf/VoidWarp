package me.dave.voidwarp.events;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.data.VoidMode;
import me.dave.voidwarp.data.VoidModes;
import me.dave.voidwarp.modes.*;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.modes.warp.EssentialsWarpMode;
import me.dave.voidwarp.modes.warp.HuskWarpMode;
import me.dave.voidwarp.modes.warp.WarpMode;
import me.dave.voidwarp.modes.warp.WarpSystemWarpMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerEvents implements Listener {
    private final EnumMap<VoidMode, VoidModes> voidModesMap = new EnumMap<>(VoidMode.class);
    private final HashSet<UUID> processingList = new HashSet<>();

    public PlayerEvents() {
        voidModesMap.put(VoidMode.COMMAND, new CommandMode());
        voidModesMap.put(VoidMode.SPAWN, new SpawnMode());
        if (VoidWarp.essentialsAPI() != null || VoidWarp.huskHomesAPI() != null) voidModesMap.put(VoidMode.WARP, new WarpMode());
        if (VoidWarp.essentialsAPI() != null) voidModesMap.put(VoidMode.ESSENTIALS_WARP, new EssentialsWarpMode());
        if (VoidWarp.huskHomesAPI() != null) voidModesMap.put(VoidMode.HUSKHOMES_WARP, new HuskWarpMode());
        if (VoidWarp.warpSystemAPI() != null) voidModesMap.put(VoidMode.WARPSYSTEM_WARP, new WarpSystemWarpMode());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Checks if player bypasses VoidWarp
        if (player.hasPermission("voidwarp.admin.bypass")) return;
        // Checks if player teleportation is currently being processed
        if (processingList.contains(player.getUniqueId())) return;

        // Gets and checks this world's WorldData
        World world = player.getWorld();
        ConfigManager.WorldData worldData = VoidWarp.configManager.getWorldData(world.getName());
        if (worldData == null) return;

        // Checks if the player is within the set height range
        double currYHeight = player.getLocation().getY();
        if (currYHeight <= worldData.yMin()) return;
        if (currYHeight >= worldData.yMax()) return;

        // Gets the world's VoidMode, if it fails then it will default to Spawn
        VoidModes voidMode = voidModesMap.get(worldData.mode());
        if (voidMode == null) voidMode = voidModesMap.get(VoidMode.SPAWN);

        // Attempts to teleport the player using the specified VoidMode
        UUID playerUUID = player.getUniqueId();
        processingList.add(playerUUID);
        voidMode.getWarpData(player, worldData).thenAccept(warpData -> {
            // Teleports the player if a location is provided
            Location location = warpData.getLocation();
            if (location != null) player.teleport(location);

            // Executes runnable actions
            Runnable runnable = warpData.getRunnable();
            if (runnable != null) runnable.run();

            // Creates and sends Action Bar message
            String message = null;
            String destinationName = warpData.getName();
            if (destinationName != null) message = worldData.message().replaceAll("%location%", destinationName);
            if (message != null) ChatColorHandler.sendActionBarMessage(player, message);

            // Remove the player from the processing list
            processingList.remove(playerUUID);
        });

        // TODO: Create some form of backup that removes the player if for whatever reason they aren't removed
    }
}

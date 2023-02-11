package me.dave.voidwarp.events;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.modes.VoidMode;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.modes.CommandMode;
import me.dave.voidwarp.modes.SpawnMode;
import me.dave.voidwarp.modes.VoidModes;
import me.dave.voidwarp.modes.WarpMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

public class PlayerEvents implements Listener {
    private final EnumMap<VoidMode, VoidModes> voidModesMap = new EnumMap<>(VoidMode.class);

    public PlayerEvents() {
        voidModesMap.put(VoidMode.COMMAND, new CommandMode());
        voidModesMap.put(VoidMode.SPAWN, new SpawnMode());
        if (VoidWarp.essentialsAPI() != null || VoidWarp.huskHomesAPI() != null) voidModesMap.put(VoidMode.WARP, new WarpMode());
        if (VoidWarp.essentialsAPI() != null) voidModesMap.put(VoidMode.ESSENTIALS_WARP, new WarpMode());
        if (VoidWarp.huskHomesAPI() != null) voidModesMap.put(VoidMode.HUSKHOME_WARP, new WarpMode());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("voidwarp.admin.bypass")) return;
        World world = player.getWorld();
        ConfigManager.WorldData worldData = VoidWarp.configManager.getWorldData(world.getName());
        if (worldData == null) return;
        double currYHeight = player.getLocation().getY();
        if (currYHeight <= worldData.yMin()) return;
        if (currYHeight >= worldData.yMax()) return;

        VoidModes voidMode = voidModesMap.get(worldData.mode());
        if (voidMode == null) voidMode = voidModesMap.get(VoidMode.SPAWN);

        String teleportMessage = worldData.message();
        CompletableFuture<String> teleportLocation = voidMode.run(player, world, worldData);

        teleportLocation.thenAccept(location -> {
            String message = null;
            if (location != null) message = teleportMessage.replaceAll("%location%", location);
            if (message != null) ChatColorHandler.sendActionBarMessage(player, message);
        });
    }
}

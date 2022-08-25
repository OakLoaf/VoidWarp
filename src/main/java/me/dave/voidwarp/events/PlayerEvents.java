package me.dave.voidwarp.events;

import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.VoidMode;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.modes.CommandMode;
import me.dave.voidwarp.modes.SpawnMode;
import me.dave.voidwarp.modes.VoidModes;
import me.dave.voidwarp.modes.WarpMode;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EnumMap;

public class PlayerEvents implements Listener {
    private final EnumMap<VoidMode, VoidModes> voidModesMap = new EnumMap<>(VoidMode.class);

    public PlayerEvents() {
        voidModesMap.put(VoidMode.COMMAND, new CommandMode());
        voidModesMap.put(VoidMode.SPAWN, new SpawnMode());
        if (VoidWarp.hasEssentials()) voidModesMap.put(VoidMode.WARP, new WarpMode());
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
        String teleportMessage = worldData.message();
        String teleportLocation = null;
        VoidModes voidMode = voidModesMap.get(worldData.mode());
        if (voidMode == null) voidMode = voidModesMap.get(VoidMode.SPAWN);
        teleportLocation = voidMode.run(player, world, worldData);

        Audience audience = VoidWarp.getBukkitAudiences().sender(player);
        if (teleportLocation != null) teleportMessage = teleportMessage.replaceAll("%location%", teleportLocation);
        if (teleportMessage != null) audience.sendActionBar(MiniMessage.miniMessage().deserialize(teleportMessage));
    }
}

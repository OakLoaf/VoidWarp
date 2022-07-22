package me.dave.voidwarp.events;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.VoidWarp;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ConfigManager.WorldData worldData = VoidWarp.configManager.getWorldData(world.getName());
        if (worldData == null) return;
        double currYHeight = player.getLocation().getY();
        if (currYHeight <= worldData.yMin()) return;
        if (currYHeight >= worldData.yMax()) return;

        switch (worldData.mode()) {
            case SPAWN -> player.teleport(world.getSpawnLocation());
            case COMMAND -> {
                for (String command : worldData.commands()) {
                    player.performCommand(command);
                }
            }
            case WARP -> {
                Location playerLoc = player.getLocation();
                double minDistance = Double.MAX_VALUE;
                String closestWarp = null;
                for (String thisWarp : worldData.warps()) {
                    Location warpLoc = null;
                    try {
                        warpLoc = VoidWarp.essentialsAPI().getWarps().getWarp(thisWarp);
                    } catch (InvalidWorldException | WarpNotFoundException err) {
                        err.printStackTrace();
                    }
                    if (warpLoc == null) continue;
                    double warpDistance = warpLoc.distanceSquared(playerLoc);
                    if (warpDistance < minDistance) {
                        minDistance = (warpDistance);
                        closestWarp = thisWarp;
                    }
                }
                Location closestWarpLoc = null;
                try {
                     closestWarpLoc = VoidWarp.essentialsAPI().getWarps().getWarp(closestWarp);
                } catch (InvalidWorldException | WarpNotFoundException err) {
                    err.printStackTrace();
                }
                if (closestWarpLoc == null) player.teleport(world.getSpawnLocation());
                else player.teleport(closestWarpLoc);
            }
        }
    }
}

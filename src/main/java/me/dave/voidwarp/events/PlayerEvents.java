package me.dave.voidwarp.events;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.dave.voidwarp.ConfigManager;
import me.dave.voidwarp.VoidWarp;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
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
            case SPAWN -> {
                if (VoidWarp.hasEssentials()) player.teleport((VoidWarp.essentialsSpawnAPI().getSpawn("default")));
                else player.teleport(world.getSpawnLocation());
            }
            case COMMAND -> {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                for (String command : worldData.commands()) {
                    Bukkit.dispatchCommand(console, command.toLowerCase().replaceAll("%player%", player.getName()));
                }
            }
            case WARP -> {
                Location playerLoc = player.getLocation();
                double minDistance = Double.MAX_VALUE;
                String closestWarp = null;
                for (String thisWarp : worldData.warps()) {
                    Location warpLoc;
                    try {
                        warpLoc = VoidWarp.essentialsAPI().getWarps().getWarp(thisWarp);
                    } catch (InvalidWorldException | WarpNotFoundException err) {
                        continue;
                    }
                    if (warpLoc == null || warpLoc.getWorld() != world) continue;
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
                if (closestWarpLoc == null) {
                    if (VoidWarp.hasEssentials()) player.teleport((VoidWarp.essentialsSpawnAPI().getSpawn("default")));
                    else player.teleport(world.getSpawnLocation());
                } else player.teleport(closestWarpLoc);
            }
        }
    }
}

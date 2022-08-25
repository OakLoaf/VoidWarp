package me.dave.voidwarp.modes;

import com.earth2me.essentials.commands.WarpNotFoundException;
import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.VoidWarp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpMode implements VoidModes {

    public String run(Player player, World world, WorldData worldData) {
        Location playerLoc = player.getLocation();
        double minDistance = Double.MAX_VALUE;
        String closestWarp = null;
        for (String thisWarp : worldData.warps()) {
            Location warpLoc;
            try {
                warpLoc = VoidWarp.essentialsAPI().getWarps().getWarp(thisWarp);
            } catch (net.ess3.api.InvalidWorldException | WarpNotFoundException err) {
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
        } catch (net.ess3.api.InvalidWorldException | WarpNotFoundException err) {
            err.printStackTrace();
        }
        if (closestWarpLoc == null) {
            if (VoidWarp.hasEssentials()) player.teleport((VoidWarp.essentialsSpawnAPI().getSpawn("default")));
            else player.teleport(world.getSpawnLocation());
            return "Spawn";
        } else {
            player.teleport(closestWarpLoc);
            return closestWarp;
        }
    }
}

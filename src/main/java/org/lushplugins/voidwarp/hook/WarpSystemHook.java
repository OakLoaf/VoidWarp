package org.lushplugins.voidwarp.hook;

import de.codingair.warpsystem.api.ITeleportManager;
import de.codingair.warpsystem.api.TeleportService;
import org.lushplugins.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class WarpSystemHook implements Hook {
    public static final String PLUGIN_NAME = "WarpSystem";
    private final ITeleportManager warpSystem;

    public WarpSystemHook() {
        warpSystem = TeleportService.get();
    }

    public Collection<String> getWarps() {
        return new ArrayList<>(warpSystem.simpleWarps());
    }

    public Location getWarp(String warpName) {
        return warpSystem.simpleWarp(warpName);
    }

    public WarpData getClosestWarp(Player player, Collection<String> warps) {
        Location playerLoc = player.getLocation();
        World world = playerLoc.getWorld();
        double minDistance = Double.MAX_VALUE;
        String closestWarp = null;

        for (String thisWarp : warps) {
            Location warpLoc = getWarp(thisWarp);
            if (warpLoc == null || warpLoc.getWorld() != world) continue;
            double warpDistance = warpLoc.distanceSquared(playerLoc);
            if (warpDistance < minDistance) {
                minDistance = (warpDistance);
                closestWarp = thisWarp;
            }
        }

        return new WarpData(closestWarp, getWarp(closestWarp));
    }
}

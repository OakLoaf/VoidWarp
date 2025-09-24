package org.lushplugins.voidwarp.hook;

import org.lushplugins.voidwarp.data.WarpData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import su.nightexpress.sunlight.SunLightAPI;
import su.nightexpress.sunlight.SunLightPlugin;
import su.nightexpress.sunlight.module.spawns.SpawnsModule;
import su.nightexpress.sunlight.module.spawns.impl.Spawn;
import su.nightexpress.sunlight.module.warps.WarpsModule;
import su.nightexpress.sunlight.module.warps.impl.Warp;

import java.util.Collection;
import java.util.Optional;

public class SunlightHook implements Hook {
    public static final String PLUGIN_NAME = "SunLight";
    private final SunLightPlugin sunLight = SunLightAPI.getPlugin();

    public Collection<String> getWarps() {
        if (sunLight.getModuleManager().getModule("warps") instanceof WarpsModule module) {
            return module.getWarps().stream().map(Warp::getName).toList();
        } else {
            return null;
        }
    }

    public Location getWarp(String warpName) {
        if (sunLight.getModuleManager().getModule("warps") instanceof WarpsModule module) {
            Optional<Warp> warp = module.getWarps().stream().filter(aWarp -> aWarp.getName().equalsIgnoreCase(warpName)).findFirst();
            if (warp.isPresent()) {
                return warp.get().getLocation();
            }
        }

        return null;
    }

    public WarpData getClosestWarp(Player player, Collection<String> warps) {
        Location playerLoc = player.getLocation();
        double minDistance = Double.MAX_VALUE;
        String closestWarp = null;

        for (String thisWarp : warps) {
            Location warpLoc = getWarp(thisWarp);
            if (warpLoc == null || warpLoc.getWorld() != playerLoc.getWorld()) continue;
            double warpDistance = warpLoc.distanceSquared(playerLoc);
            if (warpDistance < minDistance) {
                minDistance = (warpDistance);
                closestWarp = thisWarp;
            }
        }

        return new WarpData(closestWarp, getWarp(closestWarp));
    }

    public Location getSpawn() {
        if (sunLight.getModuleManager().getModule("spawns") instanceof SpawnsModule module) {
            Spawn defaultSpawn = module.getDefaultSpawn();
            if (defaultSpawn != null) {
                return defaultSpawn.getLocation();
            }
        }

        return null;
    }
}

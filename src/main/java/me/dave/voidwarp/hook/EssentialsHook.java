package me.dave.voidwarp.hook;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.dave.voidwarp.data.WarpData;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EssentialsHook implements Hook {
    public static final String PLUGIN_NAME = "Essentials";
    private final Essentials essentials;

    public EssentialsHook() {
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
    }

    public Collection<String> getWarps() {
        return essentials.getWarps().getList();
    }

    public Location getWarp(String warpName) {
        try {
            if (essentials.getWarps() == null) return null;
            return essentials.getWarps().getWarp(warpName);
        } catch (InvalidWorldException | WarpNotFoundException err) {
            return null;
        }
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
}

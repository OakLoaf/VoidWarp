package me.dave.voidwarp.apis;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.modes.WarpMode;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EssentialsHook {
    private final Essentials essentials;

    public EssentialsHook() {
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    public Collection<String> getWarps() {
        return essentials.getWarps().getList();
    }

    public Location getWarp(String warpName) {
        try {
            return essentials.getWarps().getWarp(warpName);
        } catch (InvalidWorldException | WarpNotFoundException err) {
            return null;
        }
    }

    public WarpMode.WarpData getClosestWarp(Player player, World world, Collection<String> warps) {
        Location playerLoc = player.getLocation();
        double minDistance = Double.MAX_VALUE;
        String closestWarp = null;

        for (String thisWarp : warps) {
            Location warpLoc = VoidWarp.essentialsAPI().getWarp(thisWarp);
            if (warpLoc == null || warpLoc.getWorld() != world) continue;
            double warpDistance = warpLoc.distanceSquared(playerLoc);
            if (warpDistance < minDistance) {
                minDistance = (warpDistance);
                closestWarp = thisWarp;
            }
        }

        return new WarpMode.WarpData(closestWarp, getWarp(closestWarp));
    }
}

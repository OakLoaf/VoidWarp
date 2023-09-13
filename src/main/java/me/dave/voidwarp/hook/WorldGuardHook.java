package me.dave.voidwarp.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class WorldGuardHook implements Hook, Listener {
    public static final String PLUGIN_NAME = "WorldGuard";
    private static StateFlag ALLOW_VOID_WARP_FLAG;

    public WorldGuardHook() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("void-warp", true);
            registry.register(flag);
            ALLOW_VOID_WARP_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("void-warp");
            if (existing instanceof StateFlag) {
                ALLOW_VOID_WARP_FLAG = (StateFlag) existing;
            }
        }
    }

    public boolean isRegionEnabled(@NotNull Player player) {
        return isRegionEnabled(player.getWorld(), player.getLocation());
    }

    public boolean isRegionEnabled(@NotNull World world, @NotNull Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            return true;
        }

        ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
        List<ProtectedRegion> regions = set.getRegions().stream().sorted(Comparator.comparing(ProtectedRegion::getPriority)).toList();
        if (regions.size() == 0) {
            return true;
        }

        ProtectedRegion region = regions.get(0);
        StateFlag.State flag = region.getFlag(ALLOW_VOID_WARP_FLAG);
        if (flag == null) {
            return true;
        }

        return flag.equals(StateFlag.State.ALLOW);
    }
}

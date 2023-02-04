package me.dave.voidwarp.apis;

import com.earth2me.essentials.spawn.EssentialsSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EssentialsSpawnHook {
    private final EssentialsSpawn essentialsSpawn;

    public EssentialsSpawnHook() {
        essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
    }

    public Location getSpawn(String group) {
        return essentialsSpawn.getSpawn(group);
    }
}

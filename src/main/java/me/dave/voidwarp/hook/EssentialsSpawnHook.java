package me.dave.voidwarp.hook;

import com.earth2me.essentials.spawn.EssentialsSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EssentialsSpawnHook implements Hook {
    public static final String PLUGIN_NAME = "EssentialsSpaw";
    private final EssentialsSpawn essentialsSpawn;

    public EssentialsSpawnHook() {
        essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
    }

    public Location getSpawn(String group) {
        return essentialsSpawn.getSpawn(group);
    }
}

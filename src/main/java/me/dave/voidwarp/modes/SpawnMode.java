package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.VoidWarp;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnMode implements VoidModes {

    public String run(Player player, World world, WorldData worldData) {
        if (VoidWarp.hasEssentialsSpawn()) player.teleport((VoidWarp.essentialsSpawnAPI().getSpawn("default")));
        else player.teleport(world.getSpawnLocation());
        return "Spawn";
    }
}

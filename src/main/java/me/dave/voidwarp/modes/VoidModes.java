package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface VoidModes {
    String run(Player player, World world, ConfigManager.WorldData worldData);
}

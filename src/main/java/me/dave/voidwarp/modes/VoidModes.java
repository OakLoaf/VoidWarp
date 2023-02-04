package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface VoidModes {
    CompletableFuture<String> run(Player player, World world, ConfigManager.WorldData worldData);
}

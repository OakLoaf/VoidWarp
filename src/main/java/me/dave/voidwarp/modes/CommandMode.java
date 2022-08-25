package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandMode implements VoidModes {

    public String run(Player player, World world, WorldData worldData) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : worldData.commands()) {
            Bukkit.dispatchCommand(console, command.toLowerCase().replaceAll("%player%", player.getName()));
        }
        return null;
    }
}

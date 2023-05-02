package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.data.VoidModes;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CommandMode implements VoidModes {

    public CompletableFuture<String> run(Player player, WorldData worldData) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : worldData.commands()) {
            Bukkit.dispatchCommand(console, command.toLowerCase().replaceAll("%player%", player.getName()));
        }

        completableFuture.complete(null);
        return completableFuture;
    }
}

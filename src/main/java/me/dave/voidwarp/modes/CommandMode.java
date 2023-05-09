package me.dave.voidwarp.modes;

import me.dave.voidwarp.ConfigManager.WorldData;
import me.dave.voidwarp.data.VoidModes;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CommandMode implements VoidModes {

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        WarpData warpData = new WarpData(null, () -> {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            for (String command : worldData.commands()) {
                Bukkit.dispatchCommand(console, command.toLowerCase().replaceAll("%player%", player.getName()));
            }
        });

        completableFuture.complete(warpData);
        return completableFuture;
    }
}

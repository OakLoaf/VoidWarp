package org.lushplugins.voidwarp.mode;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lushplugins.voidwarp.config.ConfigManager;
import org.lushplugins.voidwarp.data.WarpData;

import java.util.concurrent.CompletableFuture;

public class PlayerCommandMode extends VoidMode<CommandMode.CommandModeData> {

    public PlayerCommandMode(CommandMode.CommandModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        WarpData warpData = new WarpData(data.getName(), () -> {
            data.getCommands().forEach(command -> Bukkit.dispatchCommand(player, command.replaceAll("%player%", player.getName())));
        });

        completableFuture.complete(warpData);
        return completableFuture;
    }
}

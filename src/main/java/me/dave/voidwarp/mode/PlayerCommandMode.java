package me.dave.voidwarp.mode;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerCommandMode extends VoidMode<CommandMode.CommandModeData> {

    public PlayerCommandMode(CommandMode.CommandModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

      WarpData warpData = new WarpData(
          data.getName(),
          () -> data.getCommands().forEach(
              command -> Bukkit.dispatchCommand(
                  player,
                  command.replaceAll("%player%", player.getName())
              )
          )
      );

        completableFuture.complete(warpData);
        return completableFuture;
    }
}

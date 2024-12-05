package me.dave.voidwarp.mode;

import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.data.WarpData;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandMode extends VoidMode<CommandMode.CommandModeData> {

    public CommandMode(CommandModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, ConfigManager.WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();

        WarpData warpData = new WarpData(data.getName(), () -> {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            data.getCommands().forEach(command -> Bukkit.dispatchCommand(console, command.replaceAll("%player%", player.getName())));
        });

        completableFuture.complete(warpData);
        return completableFuture;
    }

    public static class CommandModeData extends VoidModeData {
        private final List<String> commands;

        public CommandModeData(String name, @Nullable List<String> commands) {
            super(name);

            this.commands = commands;
        }

        public List<String> getCommands() {
            return commands;
        }
    }
}

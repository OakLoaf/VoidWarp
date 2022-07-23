package me.dave.voidwarp.commands;

import me.dave.voidwarp.VoidWarp;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Audience audience = VoidWarp.getBukkitAudiences().sender(sender);
        if (!sender.hasPermission("voidwarp.admin.reload")) {
            audience.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have insufficient permissions "));
            return true;
        }
        VoidWarp.configManager.reloadConfig();
        audience.sendMessage(MiniMessage.miniMessage().deserialize("<green>Reloaded VoidWarp"));
        return true;
    }
}

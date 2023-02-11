package me.dave.voidwarp.commands;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.voidwarp.VoidWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("voidwarp.admin.reload")) {
            sender.sendMessage(ChatColorHandler.translateAlternateColorCodes("&cYou have insufficient permissions."));
            return true;
        }
        VoidWarp.configManager.reloadConfig();
        sender.sendMessage(ChatColorHandler.translateAlternateColorCodes("&aReloaded VoidWarp"));
        return true;
    }
}

package org.lushplugins.voidwarp.command;

import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.voidwarp.VoidWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("voidwarp.admin.reload")) {
            sender.sendMessage(ChatColorHandler.translate("&cYou have insufficient permissions."));
            return true;
        }
        VoidWarp.configManager.reloadConfig();
        sender.sendMessage(ChatColorHandler.translate("&#b7faa2VoidWarp has been reloaded &#66b04f🔃"));
        return true;
    }
}

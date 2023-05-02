package me.dave.voidwarp;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.apis.EssentialsSpawnHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.apis.WarpSystemHook;
import me.dave.voidwarp.commands.ReloadCmd;
import me.dave.voidwarp.events.PlayerEvents;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWarp extends JavaPlugin {
    private static VoidWarp plugin;
    public static ConfigManager configManager;
    private static EssentialsHook essentials = null;
    private static EssentialsSpawnHook essentialsSpawn = null;
    private static HuskHomesHook huskHomes = null;
    private static WarpSystemHook warpSystem = null;

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin("Essentials") != null) essentials = new EssentialsHook();
        else getLogger().info("Essentials plugin not found. Continuing without Essentials.");

        if (pluginManager.getPlugin("EssentialsSpawn") != null) essentialsSpawn = new EssentialsSpawnHook();
        else getLogger().info("EssentialsSpawn plugin not found. Continuing without EssentialsSpawn.");

        if (pluginManager.getPlugin("HuskHomes") != null) huskHomes = new HuskHomesHook();
        else getLogger().info("HuskHomes plugin not found. Continuing without HuskHomes.");

        if (pluginManager.getPlugin("WarpSystem") != null) warpSystem = new WarpSystemHook();
        else getLogger().info("WarpSystem plugin not found. Continuing without WarpSystem.");

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        configManager = new ConfigManager();

        getCommand("vwreload").setExecutor(new ReloadCmd());

        ChatColorHandler.enableMiniMessage(true);
    }

    public static VoidWarp getInstance() {
        return plugin;
    }

    public static EssentialsHook essentialsAPI() {
        return essentials;
    }

    public static EssentialsSpawnHook essentialsSpawnAPI() {
        return essentialsSpawn;
    }

    public static HuskHomesHook huskHomesAPI() {
        return huskHomes;
    }

    public static WarpSystemHook warpSystemAPI() {
        return warpSystem;
    }
}

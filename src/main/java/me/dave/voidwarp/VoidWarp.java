package me.dave.voidwarp;

import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.apis.EssentialsSpawnHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.commands.ReloadCmd;
import me.dave.voidwarp.events.PlayerEvents;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWarp extends JavaPlugin {
    private static VoidWarp plugin;
    private static BukkitAudiences bukkitAudiences;
    public static ConfigManager configManager;
    private static EssentialsHook essentials = null;
    private static EssentialsSpawnHook essentialsSpawn = null;
    private static HuskHomesHook huskHomes = null;

    @Override
    public void onEnable() {
        plugin = this;
        bukkitAudiences = BukkitAudiences.create(this);

        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin("Essentials") != null) essentials = new EssentialsHook();
        else getLogger().info("Essentials plugin not found. Continuing without Essentials.");

        if (pluginManager.getPlugin("EssentialsSpawn") != null) essentialsSpawn = new EssentialsSpawnHook();
        else getLogger().info("EssentialsSpawn plugin not found. Continuing without EssentialsSpawn.");

        if (pluginManager.getPlugin("HuskHomes") != null) huskHomes = new HuskHomesHook();
        else getLogger().info("HuskHomes plugin not found. Continuing without HuskHomes.");

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        configManager = new ConfigManager();

        getCommand("vwreload").setExecutor(new ReloadCmd());
    }

    public static VoidWarp getInstance() {
        return plugin;
    }

    public static BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
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
}

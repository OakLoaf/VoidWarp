package org.lushplugins.voidwarp;

import org.lushplugins.voidwarp.hook.*;
import org.lushplugins.voidwarp.commands.ReloadCmd;
import org.lushplugins.voidwarp.config.ConfigManager;
import org.lushplugins.voidwarp.events.PlayerEvents;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class VoidWarp extends JavaPlugin {
    private static VoidWarp plugin;
    private static HashMap<String, Hook> availablePlugins;
    public static ConfigManager configManager;

    @Override
    public void onLoad() {
        plugin = this;
        availablePlugins = new HashMap<>();

        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin(WorldGuardHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"WorldGuard\". WorldGuard support enabled.");
            availablePlugins.put(WorldGuardHook.PLUGIN_NAME, new WorldGuardHook());
        }
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin(EssentialsHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"Essentials\". Essentials support enabled.");
            availablePlugins.put(EssentialsHook.PLUGIN_NAME, null);
        }

        if (pluginManager.getPlugin(EssentialsSpawnHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"EssentialsSpawn\". EssentialsSpawn support enabled.");
            availablePlugins.put(EssentialsSpawnHook.PLUGIN_NAME, null);
        }

        if (pluginManager.getPlugin(HuskHomesHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"HuskHomes\". HuskHomes support enabled.");
            availablePlugins.put(HuskHomesHook.PLUGIN_NAME, null);
        }

        if (pluginManager.getPlugin(SunlightHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"SunLight\". SunLight support enabled.");
            availablePlugins.put(SunlightHook.PLUGIN_NAME, null);
        }

        if (pluginManager.getPlugin(WarpSystemHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"WarpSystem\". WarpSystem support enabled.");
            availablePlugins.put(WarpSystemHook.PLUGIN_NAME, null);
        }

        configManager = new ConfigManager();

        getCommand("vwreload").setExecutor(new ReloadCmd());

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    @Override
    public void onDisable() {
        availablePlugins.clear();
        availablePlugins = null;
    }

    public static VoidWarp getInstance() {
        return plugin;
    }

    public static boolean isPluginAvailable(String plugin) {
        return availablePlugins.keySet().contains(plugin);
    }

    public static Hook getOrLoadHook(String plugin) {
        if (!availablePlugins.containsKey(plugin)) {
            throw new IllegalArgumentException(plugin + " is not an available plugin");
        }

        Hook hook = availablePlugins.get(plugin);

        if (hook == null) {
            switch(plugin) {
                case EssentialsHook.PLUGIN_NAME -> hook = new EssentialsHook();
                case EssentialsSpawnHook.PLUGIN_NAME -> hook = new EssentialsSpawnHook();
                case SunlightHook.PLUGIN_NAME -> hook = new SunlightHook();
                case HuskHomesHook.PLUGIN_NAME -> hook = new HuskHomesHook();
                case WarpSystemHook.PLUGIN_NAME -> hook = new WarpSystemHook();
            }

            if (hook != null) {
                availablePlugins.put(plugin, hook);
            }
        }

        return hook;
    }
}

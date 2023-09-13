package me.dave.voidwarp;

import me.dave.voidwarp.hook.*;
import me.dave.voidwarp.commands.ReloadCmd;
import me.dave.voidwarp.config.ConfigManager;
import me.dave.voidwarp.events.PlayerEvents;
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
            availablePlugins.put("WorldGuard", new WorldGuardHook());
        }
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin(EssentialsHook.PLUGIN_NAME) != null) {
            getLogger().info("Found plugin \"Essentials\". Essentials support enabled.");
            availablePlugins.put("Essentials", null);
        }

        if (pluginManager.getPlugin("EssentialsSpawn") != null) {
            getLogger().info("Found plugin \"EssentialsSpawn\". EssentialsSpawn support enabled.");
            availablePlugins.put("EssentialsSpawn", null);
        }

        if (pluginManager.getPlugin("HuskHomes") != null) {
            getLogger().info("Found plugin \"HuskHomes\". HuskHomes support enabled.");
            availablePlugins.put("HuskHomes", null);
        }

        if (pluginManager.getPlugin("WarpSystem") != null) {
            getLogger().info("Found plugin \"WarpSystem\". WarpSystem support enabled.");
            availablePlugins.put("WarpSystem", null);
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
                case "Essentials" -> hook = new EssentialsHook();
                case "EssentialsSpawn" -> hook = new EssentialsSpawnHook();
                case "HuskHomes" -> hook = new HuskHomesHook();
                case "WarpSystem" -> hook = new WarpSystemHook();
            }

            if (hook != null) {
                availablePlugins.put(plugin, hook);
            }
        }

        return hook;
    }
}

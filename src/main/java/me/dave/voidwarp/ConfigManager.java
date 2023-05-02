package me.dave.voidwarp;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.apis.EssentialsHook;
import me.dave.voidwarp.apis.HuskHomesHook;
import me.dave.voidwarp.apis.WarpSystemHook;
import me.dave.voidwarp.data.VoidMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigManager {
    private final VoidWarp plugin = VoidWarp.getInstance();
    private final HashMap<String, WorldData> worldDataMap = new HashMap<>();

    public ConfigManager() {
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        worldDataMap.clear();

        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection configurationSection) {
                String worldName = entry.getKey();
                VoidMode mode;
                try {
                    mode = VoidMode.valueOf(configurationSection.getString("mode", "spawn").toUpperCase());
                } catch (IllegalArgumentException err) {
                    err.printStackTrace();
                    continue;
                }
                double yMin = configurationSection.getDouble("yMin", -Double.MAX_VALUE);
                double yMax = configurationSection.getDouble("yMax", Double.MAX_VALUE);
                String message = configurationSection.getString("message");

                Collection<String> commands = null;
                Collection<String> warps = null;

                switch (mode) {
                    case WARP -> {
                        HuskHomesHook huskHomesAPI = VoidWarp.huskHomesAPI();
                        WarpSystemHook warpSystemAPI = VoidWarp.warpSystemAPI();
                        EssentialsHook essentialsAPI = VoidWarp.essentialsAPI();

                        if (huskHomesAPI == null && warpSystemAPI == null && essentialsAPI == null) {
                            plugin.getLogger().severe("Mode \"WARP\" needs either HuskHomes, WarpSystem or Essentials to function");
                            plugin.getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            plugin.getLogger().severe(configurationSection.getCurrentPath() + ".mode");
                            break;
                        }

                        Collection<String> warpConfigList = configurationSection.getStringList("warps");
                        boolean isWhitelist = configurationSection.getBoolean("whitelist");

                        if (huskHomesAPI != null) {
                            huskHomesAPI.getWarps().thenAccept((huskWarps) -> {
                                Collection<String> warpCollection = huskWarps;

                                if (warpCollection.size() == 0) warpCollection = VoidWarp.essentialsAPI().getWarps();

                                if (isWhitelist) warpCollection = warpConfigList;
                                else warpCollection.removeAll(warpConfigList);
                                worldDataMap.put(worldName, new WorldData(mode, yMin, yMax, message, null, warpCollection));
                            });
                            return;
                        }

                        if (warpSystemAPI != null) warps = VoidWarp.warpSystemAPI().getWarps();
                        else warps = VoidWarp.essentialsAPI().getWarps(); // if not HuskHomes or WarpSystem then uses Essentials

                        if (isWhitelist) warps = warpConfigList;
                        else warps.removeAll(warpConfigList);
                    }

                    case ESSENTIALS_WARP -> {
                        if (VoidWarp.essentialsAPI() == null) {
                            plugin.getLogger().severe("Mode \"ESSENTIALS_WARP\" needs Essentials to function");
                            plugin.getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            plugin.getLogger().severe(configurationSection.getCurrentPath() + ".mode");
                            break;
                        }

                        Collection<String> warpConfigList = configurationSection.getStringList("warps");
                        boolean isWhitelist = configurationSection.getBoolean("whitelist");

                        warps = VoidWarp.essentialsAPI().getWarps();
                        if (isWhitelist) warps = warpConfigList;
                        else warps.removeAll(warpConfigList);
                    }

                    case HUSKHOMES_WARP -> {
                        if (VoidWarp.huskHomesAPI() == null) {
                            plugin.getLogger().severe("Mode \"HUSKHOMES_WARP\" needs HuskHomes to function");
                            plugin.getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            plugin.getLogger().severe(configurationSection.getCurrentPath() + ".mode");
                            break;
                        }

                        Collection<String> warpConfigList = configurationSection.getStringList("warps");
                        boolean isWhitelist = configurationSection.getBoolean("whitelist");

                        VoidWarp.huskHomesAPI().getWarps().thenAccept((huskWarps) -> {
                            Collection<String> warpCollection = huskWarps;

                            if (warpCollection.size() == 0) warpCollection = VoidWarp.essentialsAPI().getWarps();

                            if (isWhitelist) warpCollection = warpConfigList;
                            else warpCollection.removeAll(warpConfigList);
                            worldDataMap.put(worldName, new WorldData(mode, yMin, yMax, message, null, warpCollection));
                        });
                        return;
                    }

                    case WARPSYSTEM_WARP -> {
                        if (VoidWarp.warpSystemAPI() == null) {
                            plugin.getLogger().severe("Mode \"WARPSYSTEM_WARP\" needs WarpSystem to function");
                            plugin.getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            plugin.getLogger().severe(configurationSection.getCurrentPath() + ".mode");
                            break;
                        }

                        Collection<String> warpConfigList = configurationSection.getStringList("warps");
                        boolean isWhitelist = configurationSection.getBoolean("whitelist");

                        warps = VoidWarp.warpSystemAPI().getWarps();
                        if (isWhitelist) warps = warpConfigList;
                        else warps.removeAll(warpConfigList);
                    }

                    case COMMAND -> commands = configurationSection.getStringList("commands");
                }
                worldDataMap.put(worldName, new WorldData(mode, yMin, yMax, message, commands, warps));
            }
        }
    }

    public record WorldData(VoidMode mode, double yMin, double yMax, String message, Collection<String> commands, Collection<String> warps) {}

    public HashMap<String, WorldData> getWorldDataMap() { return worldDataMap; }

    public WorldData getWorldData(String worldName) { return worldDataMap.get(worldName); }
}

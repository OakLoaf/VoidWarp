package me.dave.voidwarp;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
                double yMin = configurationSection.getDouble("yMin", Double.MIN_VALUE);
                double yMax = configurationSection.getDouble("yMax", Double.MAX_VALUE);
                String message = configurationSection.getString("displayMessage");

                Collection<String> commands = null;
                Collection<String> warps = null;

                switch (mode) {
                    case WARP -> {
                        if (!VoidWarp.hasEssentials()) {
                            plugin.getLogger().severe("Mode \"WARP\" cannot be used without Essentials");
                            plugin.getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            plugin.getLogger().severe(configurationSection.getCurrentPath() + ".mode");
                            break;
                        }
                        boolean isWhitelist = configurationSection.getBoolean("whitelist");
                        warps = VoidWarp.essentialsAPI().getWarps().getList();
                        Collection<String> warpConfigList = configurationSection.getStringList("warps");
                        if (isWhitelist) warps = warpConfigList;
                        else warps.removeAll(warpConfigList);
                    }
                    case COMMAND -> commands = configurationSection.getStringList("commands");
                }
                worldDataMap.put(worldName, new WorldData(mode, yMin, yMax, message, commands, warps));
            }
        }
    }

    public static record WorldData(VoidMode mode, double yMin, double yMax, String message, Collection<String> commands, Collection<String> warps) {}

    public HashMap<String, WorldData> getWorldDataMap() { return worldDataMap; }

    public WorldData getWorldData(String worldName) { return worldDataMap.get(worldName); }
}

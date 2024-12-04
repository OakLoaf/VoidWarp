package me.dave.voidwarp.config;

import me.dave.voidwarp.VoidWarp;
import me.dave.voidwarp.mode.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigManager {
    private final HashMap<String, WorldData> worldDataMap = new HashMap<>();

    public ConfigManager() {
        VoidWarp.getInstance().saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        VoidWarp.getInstance().reloadConfig();
        FileConfiguration config = VoidWarp.getInstance().getConfig();
        worldDataMap.clear();

        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection configurationSection) {
                String worldName = entry.getKey();
                double yMin = configurationSection.getDouble("yMin", -Double.MAX_VALUE);
                double yMax = configurationSection.getDouble("yMax", Double.MAX_VALUE);
                String message = configurationSection.getString("message", null);

                VoidModeType mode;
                try {
                    mode = VoidModeType.valueOf(configurationSection.getString("mode", "spawn").toUpperCase());
                } catch (IllegalArgumentException err) {
                    err.printStackTrace();
                    continue;
                }

                VoidModeData data;
                switch (mode) {
                    case BOUNCE -> {
                        World world = Bukkit.getWorld(worldName);
                        double x = configurationSection.getDouble("x");
                        double y = configurationSection.getDouble("y");
                        double z = configurationSection.getDouble("z");

                        String locationName = configurationSection.getString("name");
                        Location location = new Location(world, x, y, z);
                        double speed = configurationSection.getDouble("speed", 1) / 100D;

                        data = new BounceMode.BounceModeData(locationName, location, speed);
                    }

                    case COMMAND, CONSOLE -> {
                        String locationName = configurationSection.getString("name");
                        List<String> commands = configurationSection.getStringList("commands");

                        data = new CommandMode.CommandModeData(locationName, commands);
                    }


                    case SPAWN -> {
                        String locationName = configurationSection.getString("name", "Spawn");
                        String plugin = configurationSection.getString("plugin");

                        data = new SpawnMode.SpawnModeData(locationName, plugin);
                    }

                    case LOCATION -> {
                        World world = Bukkit.getWorld(worldName);
                        double x = configurationSection.getDouble("x");
                        double y = configurationSection.getDouble("y");
                        double z = configurationSection.getDouble("z");
                        float yaw = (float) configurationSection.getDouble("yaw", 0.0);
                        float pitch = (float)  configurationSection.getDouble("pitch", 0.0);

                        String locationName = configurationSection.getString("name");
                        Location location = new Location(world, x, y, z, yaw, pitch);

                        data = new LocationMode.LocationModeData(locationName, location);
                    }

                    case WARP -> {
                        String locationName = configurationSection.getString("name");
                        String plugin = configurationSection.getString("plugin");
                        List<String> warps = configurationSection.getStringList("warps");
                        boolean whitelist = configurationSection.getBoolean("whitelist");

                        try {
                            data = new WarpMode.WarpModeData(locationName, plugin, warps, whitelist);
                        } catch (IllegalArgumentException e) {
                            VoidWarp.getInstance().getLogger().severe(e.getMessage());
                            VoidWarp.getInstance().getLogger().severe("Mode \"WARP\" needs either Essentials, HuskHomes or WarpSystem to function");
                            VoidWarp.getInstance().getLogger().severe("VoidWarp Defaulting to SPAWN in World: " + worldName);
                            VoidWarp.getInstance().getLogger().severe(configurationSection.getCurrentPath() + ".modeType");

                            mode = VoidModeType.SPAWN;
                            data = new SpawnMode.SpawnModeData(locationName, "Vanilla");
                        }
                    }

                    default -> throw new IllegalArgumentException(mode + " is not a valid VoidMode");
                }
                worldDataMap.put(worldName, new WorldData(mode, yMin, yMax, message, data));
            }
        }
    }

    public WorldData getWorldData(String worldName) {
        return worldDataMap.get(worldName);
    }

    public record WorldData(VoidModeType modeType, double yMin, double yMax, String message, VoidModeData data) {}
}

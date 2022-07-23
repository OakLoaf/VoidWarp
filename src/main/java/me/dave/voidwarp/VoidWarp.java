package me.dave.voidwarp;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import me.dave.voidwarp.commands.ReloadCmd;
import me.dave.voidwarp.events.PlayerEvents;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWarp extends JavaPlugin {
    private static VoidWarp plugin;
    private static BukkitAudiences bukkitAudiences;
    public static ConfigManager configManager;
    private static Essentials essentials;
    private static EssentialsSpawn essentialsSpawn;
    private static boolean hasEssentialsSpawn;
    private static boolean hasEssentials;

    @Override
    public void onEnable() {
        plugin = this;
        bukkitAudiences = BukkitAudiences.create(this);

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin("Essentials") != null) {
            hasEssentials = true;
            essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        } else {
            hasEssentials = false;
            getLogger().info("Essentials plugin not found. Continuing without Essentials.");
        }

        if (pluginManager.getPlugin("EssentialsSpawn") != null) {
            hasEssentialsSpawn = true;
            essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
        } else {
            hasEssentialsSpawn = false;
            getLogger().info("EssentialsSpawn plugin not found. Continuing without EssentialsSpawn.");
        }


        configManager = new ConfigManager();

        getCommand("vwreload").setExecutor(new ReloadCmd());
    }

    public static VoidWarp getInstance() { return plugin; }

    public static BukkitAudiences getBukkitAudiences() { return bukkitAudiences; }

    public static boolean hasEssentials() { return hasEssentials; }

    public static boolean hasEssentialsSpawn() { return hasEssentialsSpawn; }

    public static Essentials essentialsAPI() { return essentials; }

    public static EssentialsSpawn essentialsSpawnAPI() { return essentialsSpawn; }
}

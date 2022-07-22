package me.dave.voidwarp;

import com.earth2me.essentials.Essentials;
import me.dave.voidwarp.events.PlayerEvents;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWarp extends JavaPlugin {
    private static VoidWarp plugin;
    private static BukkitAudiences bukkitAudiences;
    private static Essentials essentials;
    public static ConfigManager configManager;
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

        configManager = new ConfigManager();
    }

    public static VoidWarp getInstance() { return plugin; }

    public static BukkitAudiences getBukkitAudiences() { return bukkitAudiences; }

    public static boolean hasEssentials() { return hasEssentials; }

    public static Essentials essentialsAPI() { return essentials; }
}

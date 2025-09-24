package org.lushplugins.voidwarp.mode;

import org.lushplugins.voidwarp.config.ConfigManager.WorldData;
import org.lushplugins.voidwarp.VoidWarp;
import org.lushplugins.voidwarp.hook.EssentialsSpawnHook;
import org.lushplugins.voidwarp.hook.HuskHomesHook;
import org.lushplugins.voidwarp.data.WarpData;
import org.lushplugins.voidwarp.hook.SunlightHook;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SpawnMode extends VoidMode<SpawnMode.SpawnModeData> {

    public SpawnMode(SpawnModeData data) {
        super(data);
    }

    @Override
    public CompletableFuture<WarpData> getWarpData(Player player, WorldData worldData) {
        CompletableFuture<WarpData> completableFuture = new CompletableFuture<>();
        World world = player.getWorld();

        switch (data.getPlugin()) {
            case EssentialsSpawnHook.PLUGIN_NAME -> {
                if (VoidWarp.getOrLoadHook(EssentialsSpawnHook.PLUGIN_NAME) instanceof EssentialsSpawnHook hook) {
                    completableFuture.complete(new WarpData(data.getName(), hook.getSpawn("default")));
                } else {
                    completableFuture.complete(null);
                }
            }
            case HuskHomesHook.PLUGIN_NAME -> {
                if (VoidWarp.getOrLoadHook(HuskHomesHook.PLUGIN_NAME) instanceof HuskHomesHook hook) {
                    hook.getSpawn().thenAccept(spawn -> completableFuture.complete(new WarpData(data.getName(), () -> hook.teleportPlayer(player, spawn))));
                } else {
                    completableFuture.complete(null);
                }
            }
            case SunlightHook.PLUGIN_NAME -> {
                if (VoidWarp.getOrLoadHook(SunlightHook.PLUGIN_NAME) instanceof SunlightHook hook) {
                    completableFuture.complete(new WarpData(data.getName(), hook.getSpawn()));
                } else {
                    completableFuture.complete(null);
                }
            }
            default -> {
                completableFuture.complete(new WarpData(data.getName(), world.getSpawnLocation()));
            }
        }

        return completableFuture;
    }

    public static class SpawnModeData extends VoidModeData {
        private final String plugin;

        public SpawnModeData(String name, @Nullable String plugin) {
            super(name);

            if (plugin == null) {
                if (VoidWarp.isPluginAvailable(EssentialsSpawnHook.PLUGIN_NAME)) {
                    plugin = EssentialsSpawnHook.PLUGIN_NAME;
                }
                else if (VoidWarp.isPluginAvailable(HuskHomesHook.PLUGIN_NAME)) {
                    plugin = HuskHomesHook.PLUGIN_NAME;
                }
                else if (VoidWarp.isPluginAvailable(SunlightHook.PLUGIN_NAME)) {
                    plugin = SunlightHook.PLUGIN_NAME;
                }
                else {
                    plugin = "Vanilla";
                }
            }

            this.plugin = plugin;
        }

        public String getPlugin() {
            return plugin;
        }
    }
}

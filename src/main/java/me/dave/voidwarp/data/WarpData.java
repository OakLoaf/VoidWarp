package me.dave.voidwarp.data;

import org.bukkit.Location;

public class WarpData {
    private final String name;
    private final Location location;
    private final Runnable runnable;

    public WarpData(String name, Location location) {
        this.name = name;
        this.location = location;
        this.runnable = null;
    }

    public WarpData(String name, Runnable runnable) {
        this.name = name;
        this.location = null;
        this.runnable = runnable;
    }

    public WarpData(String name, Location location, Runnable runnable) {
        this.name = name;
        this.location = location;
        this.runnable = runnable;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}

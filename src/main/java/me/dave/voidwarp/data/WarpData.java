package me.dave.voidwarp.data;

import org.bukkit.Location;

public class WarpData {
    private final String name;
    private final Location location;

    public WarpData(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}

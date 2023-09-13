package me.dave.voidwarp.mode;

public enum VoidModeType {
    COMMAND(CommandMode.class),
    LOCATION(LocationMode.class),
    SPAWN(SpawnMode.class),
    WARP(WarpMode.class);


    private final Class<? extends VoidMode<?>> clazz;

    VoidModeType(Class<? extends VoidMode<?>> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends VoidMode<?>> getVoidMode() {
        return clazz;
    }
}

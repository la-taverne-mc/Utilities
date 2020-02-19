package fr.neolithic.utilities.utilities;

import java.util.UUID;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class SerializedHome {
    private final String uuid;
    private final String home;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SerializedHome(@NotNull UUID uuid, @NotNull String home, @NotNull Location location) {
        this.uuid = uuid.toString();
        this.home = home;
        this.world = location.getWorld().getUID().toString();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public SerializedHome(@NotNull String uuid, @NotNull String home, @NotNull String world, @NotNull double x, @NotNull double y, @NotNull double z, @NotNull float yaw, @NotNull float pitch) {
        this.uuid = uuid;
        this.home = home;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String uuid() { return uuid; }
    public String home() { return home; }
    public String world() { return world; }
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }
    public float yaw() { return yaw; }
    public float pitch() {return pitch; }

    public String toString() {
        String str = uuid;
        str += ", " + home;
        str += ", " + world;
        str += ", " + String.valueOf(x);
        str += ", " + String.valueOf(y);
        str += ", " + String.valueOf(z);
        str += ", " + String.valueOf(yaw);
        str += ", " + String.valueOf(pitch);

        return str;
    }
}

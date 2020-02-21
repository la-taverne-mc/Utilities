package fr.neolithic.utilities.utilities.back;

import java.util.UUID;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class SerializedPlayerLastLocation {
    private String playerUuid;
    private String worldUuid;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SerializedPlayerLastLocation(@NotNull UUID playerUuid, @NotNull Location location) {
        this.playerUuid = playerUuid.toString();
        this.worldUuid = location.getWorld().getUID().toString();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public SerializedPlayerLastLocation(@NotNull String playerUuid, @NotNull String worldUuid, @NotNull double x, @NotNull double y, @NotNull double z, @NotNull float yaw, @NotNull float pitch) {
        this.playerUuid = playerUuid;
        this.worldUuid = worldUuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String playerUuid() { return playerUuid; }
    public String worldUuid() { return worldUuid; }
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }
    public float yaw() { return yaw; }
    public float pitch() { return pitch; }

    public String toString() {
        String str = playerUuid;
        str += ", " + worldUuid;
        str += ", " + String.valueOf(x);
        str += ", " + String.valueOf(y);
        str += ", " + String.valueOf(z);
        str += ", " + String.valueOf(yaw);
        str += ", " + String.valueOf(pitch);

        return str;
    }
}
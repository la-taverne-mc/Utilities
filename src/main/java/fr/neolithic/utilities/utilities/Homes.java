package fr.neolithic.utilities.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Homes {
    private HashMap<UUID, PlayerHomes> homes = new HashMap<UUID, PlayerHomes>();

    public @Nullable List<String> getPlayerHomes(@NotNull UUID playerUuid) {
        if (!homes.containsKey(playerUuid)) return null;

        List<String> playerHomes = homes.get(playerUuid).getHomes();
        Collections.sort(playerHomes);

        return playerHomes;
    }

    public boolean addHome(@NotNull UUID playerUuid, @NotNull String home, @NotNull Location location) {
        if (!homes.containsKey(playerUuid)) homes.put(playerUuid, new PlayerHomes());

        return homes.get(playerUuid).addHome(home, getApproximativeLocation(location));
    }

    public @Nullable Location getHomeLocation(@NotNull UUID playerUuid, @NotNull String home) {
        if (!homes.containsKey(playerUuid)) return null;

        return homes.get(playerUuid).getHomeLocation(home);
    }

    public boolean deleteHome(@NotNull UUID playerUuid, @NotNull String home) {
        if (!homes.containsKey(playerUuid)) return false;
        
        return homes.get(playerUuid).deleteHome(home);
    }

    private Location getApproximativeLocation(@NotNull Location location) {
        double x = closest(new double[] {location.getBlockX() - 0.5f, location.getBlockX() + 0.5f}, location.getX());
        double z = closest(new double[] {location.getBlockZ() - 0.5f, location.getBlockZ() + 0.5f}, location.getZ());
        float yaw = closest(new float[] {-180.f, -90.f, 0.f, 90.f, 180.f}, location.getYaw());
        float pitch = closest(new float[] {-90.f, -45.f, 0.f, 45.f, 90.f}, location.getPitch());
        
        return new Location(location.getWorld(), x, location.getBlockY(), z, yaw, pitch);
    }

    private float closest(@NotNull float[] numbers, @NotNull float number) {
        float distance = Math.abs(numbers[0] - number);
        int idx = 0;

        for (int i = 0; i < numbers.length; i++) {
            float idistance = Math.abs(numbers[i] - number);
            if (idistance < distance) {
                idx = i;
                distance = idistance;
            }
        }

        return numbers[idx];
    }

    private double closest(@NotNull double[] numbers, @NotNull double number) {
        double distance = Math.abs(numbers[0] - number);
        int idx = 0;

        for (int i = 0; i < numbers.length; i++) {
            double idistance = Math.abs(numbers[i] - number);
            if (idistance < distance) {
                idx = i;
                distance = idistance;
            }
        }

        return numbers[idx];
    }
}
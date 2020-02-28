package fr.neolithic.utilities.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationUtils {
    public static Location getApproximativeLocation(@NotNull Location location) {
        double x = closest(new double[] {location.getBlockX() - 0.5f, location.getBlockX() + 0.5f}, location.getX());
        double z = closest(new double[] {location.getBlockZ() - 0.5f, location.getBlockZ() + 0.5f}, location.getZ());
        float yaw = closest(new float[] {-180.f, -90.f, 0.f, 90.f, 180.f}, location.getYaw());
        float pitch = closest(new float[] {-90.f, -45.f, 0.f, 45.f, 90.f}, location.getPitch());
        
        return new Location(location.getWorld(), x, location.getBlockY(), z, yaw, pitch);
    }

    public static float closest(@NotNull float[] numbers, @NotNull float number) {
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

    public static double closest(@NotNull double[] numbers, @NotNull double number) {
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
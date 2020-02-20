package fr.neolithic.utilities.utilities.homes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.Database;

public class Homes {
    private HashMap<UUID, PlayerHomes> homes = new HashMap<UUID, PlayerHomes>();

    private Database db;

    public Homes(Database db) {
        this.db = db;
    }

    public @Nullable List<String> getPlayerHomes(@NotNull UUID playerUuid) {
        if (!homes.containsKey(playerUuid)) return null;

        List<String> playerHomes = homes.get(playerUuid).getHomes();
        Collections.sort(playerHomes);

        return playerHomes;
    }

    public boolean addHome(@NotNull UUID playerUuid, @NotNull String home, @NotNull Location location) {
        if (!homes.containsKey(playerUuid)) homes.put(playerUuid, new PlayerHomes(playerUuid));

        if (homes.get(playerUuid).addHome(home, getApproximativeLocation(location))) {
            db.addHome(new SerializedHome(playerUuid, home, getApproximativeLocation(location)));
            return true;
        }

        return false;
    }

    public @Nullable Location getHomeLocation(@NotNull UUID playerUuid, @NotNull String home) {
        if (!homes.containsKey(playerUuid)) return null;

        return homes.get(playerUuid).getHomeLocation(home);
    }

    public boolean deleteHome(@NotNull UUID playerUuid, @NotNull String home) {
        if (!homes.containsKey(playerUuid)) return false;

        if (homes.get(playerUuid).deleteHome(home)) {
            db.deleteHome(playerUuid.toString(), home);
            return true;
        }
        
        return false;
    }

    public void loadHomes(List<SerializedHome> serializedHomes) {
        for (SerializedHome serializedHome : serializedHomes) {
            deserialize(serializedHome);
        }
    }

    public void deserialize(SerializedHome serializedHome) {
        UUID uuid = UUID.fromString(serializedHome.uuid());

        if (!homes.containsKey(uuid)) homes.put(uuid, new PlayerHomes(uuid));

        homes.get(uuid).addHome(serializedHome.home(), new Location(Bukkit.getWorld(UUID.fromString(serializedHome.worldUuid())),
            serializedHome.x(), serializedHome.y(), serializedHome.z(), serializedHome.yaw(), serializedHome.pitch()));
    }

    public @Nullable List<SerializedHome> serialize() {
        if (homes.isEmpty()) return null;
        
        List<SerializedHome> serializedHomes = Lists.newArrayList();

        for (PlayerHomes home : homes.values()) {
            List<SerializedHome> playerHomes = home.serialize();

            if (playerHomes == null) continue;

            for (SerializedHome serializedHome : playerHomes) {
                serializedHomes.add(serializedHome);
            }
        }

        return serializedHomes;
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

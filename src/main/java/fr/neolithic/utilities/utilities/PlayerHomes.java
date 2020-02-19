package fr.neolithic.utilities.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerHomes {
    private UUID ownerUuid;
    private HashMap<String, Location> homes = new HashMap<String, Location>();

    public PlayerHomes(@NotNull UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }
    
    public @Nullable List<String> getHomes() {
        if (homes.keySet() != null) {
            return new ArrayList<String>(homes.keySet());
        }

        return null;
    }

    public boolean addHome(@NotNull String home, @NotNull Location location) {
        if (!homes.containsKey(home)) {
            homes.put(home, location);
            return true;
        }

        return false;
    }

    public @Nullable Location getHomeLocation(@NotNull String home) {
        return homes.get(home);
    }

    public boolean deleteHome(@NotNull String home) {
        if (homes.containsKey(home)) {
            homes.remove(home);
            return true;
        }

        return false;
    }

    public @Nullable List<SerializedHome> serialize() {
        if (homes.isEmpty()) return null;

        List<SerializedHome> serializedHomes = Lists.newArrayList();

        for (Entry<String, Location> home : homes.entrySet()) {
            serializedHomes.add(new SerializedHome(ownerUuid, home.getKey(), home.getValue()));
        }

        return serializedHomes;
    }
}

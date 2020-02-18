package fr.neolithic.utilities.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerHomes {
    private HashMap<String, Location> homes = new HashMap<String, Location>();
    
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
}
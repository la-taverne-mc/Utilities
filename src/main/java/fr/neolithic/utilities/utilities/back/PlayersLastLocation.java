package fr.neolithic.utilities.utilities.back;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.Database;

public class PlayersLastLocation {
    private HashMap<UUID, Location> playersLastLocation = new HashMap<UUID, Location>();

    public PlayersLastLocation(Database db) {
        try {
            ResultSet resultSet = db.getPlayersLastLocations();
            List<SerializedPlayerLastLocation> serializedPlayerLastLocations = Lists.newArrayList();

            while (resultSet.next()) {
                SerializedPlayerLastLocation serializedPlayerLastLocation = new SerializedPlayerLastLocation(resultSet.getString("playerUuid"), resultSet.getString("worldUuid"),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
                
                serializedPlayerLastLocations.add(serializedPlayerLastLocation);
            }

            if (!serializedPlayerLastLocations.isEmpty()) deserialize(serializedPlayerLastLocations);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerLastLocation(@NotNull UUID playerUuid, @NotNull Location location) {
        if (playersLastLocation.containsKey(playerUuid)) {
            playersLastLocation.replace(playerUuid, location);
            return;
        }

        playersLastLocation.put(playerUuid, location);
    }

    public @Nullable Location getPlayerLastLocation(@NotNull UUID playerUuid) {
        return playersLastLocation.get(playerUuid);
    }

    public @Nullable List<SerializedPlayerLastLocation> serialize() {
        if (playersLastLocation.isEmpty()) return null;

        List<SerializedPlayerLastLocation> list = Lists.newArrayList();

        for (Entry<UUID, Location> playerLastLocation : playersLastLocation.entrySet()) {
            list.add(new SerializedPlayerLastLocation(playerLastLocation.getKey(), playerLastLocation.getValue()));
        }

        return list;
    }

    public void deserialize(@NotNull List<SerializedPlayerLastLocation> playerLastLocations) {
        for (SerializedPlayerLastLocation serializedPlayerLastLocation : playerLastLocations) {
            UUID playerUuid = UUID.fromString(serializedPlayerLastLocation.playerUuid());
            Location location = new Location(Bukkit.getWorld(UUID.fromString(serializedPlayerLastLocation.worldUuid())),
                serializedPlayerLastLocation.x(), serializedPlayerLastLocation.y(), serializedPlayerLastLocation.z(),
                serializedPlayerLastLocation.yaw(), serializedPlayerLastLocation.pitch());

            setPlayerLastLocation(playerUuid, location);
        }
    }
}
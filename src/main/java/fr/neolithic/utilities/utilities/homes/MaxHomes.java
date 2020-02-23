package fr.neolithic.utilities.utilities.homes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import fr.neolithic.utilities.utilities.Database;

public class MaxHomes {
    private Database db;
    private HashMap<UUID, Integer> relativeMaxHomes = new HashMap<UUID, Integer>();
    private HashMap<UUID, Integer> staticMaxHomes = new HashMap<UUID, Integer>();

    public MaxHomes(Database db) {
        this.db = db;

        try {
            ResultSet resultSet = db.getMaxHomes();

            while (resultSet.next()) {
                if (resultSet.getBoolean("isRelative")) {
                    relativeMaxHomes.put(UUID.fromString(resultSet.getString("playerUuid")), resultSet.getInt("maxHomes"));
                }
                else {
                    staticMaxHomes.put(UUID.fromString(resultSet.getString("playerUuid")), resultSet.getInt("maxHomes"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean canPlayerCreateHome(@NotNull Player player, @NotNull int currentTotalHomes) {
        if (staticMaxHomes.containsKey(player.getUniqueId()) && currentTotalHomes <= staticMaxHomes.get(player.getUniqueId())) return false;

        if (player.hasPermission("utilities.maxhomes.unlimited")) return true;

        if (relativeMaxHomes.containsKey(player.getUniqueId())) {
            for (int i = 0; i <= currentTotalHomes; i++) {
                if (player.hasPermission("utilities.maxhomes." + String.valueOf(i - relativeMaxHomes.get(player.getUniqueId())))) return false;
            }

            return true;
        }

        for (int i = 0; i <= currentTotalHomes; i++) {
            if (player.hasPermission("utilities.maxhomes." + String.valueOf(i))) return false;
        }

        return true;
    }

    public void addMaxHome(@NotNull UUID playerUuid, @NotNull int amount) {
        if (staticMaxHomes.containsKey(playerUuid)) staticMaxHomes.remove(playerUuid);

        if (relativeMaxHomes.containsKey(playerUuid)) {
            relativeMaxHomes.put(playerUuid, relativeMaxHomes.get(playerUuid) + amount);
        }
        else {
            relativeMaxHomes.put(playerUuid, amount);
        }

        db.addMaxHome(playerUuid.toString(), amount);
    }

    public void subtractMaxHome(@NotNull UUID playerUuid, @NotNull int amount) {
        if (staticMaxHomes.containsKey(playerUuid)) staticMaxHomes.remove(playerUuid);

        if (relativeMaxHomes.containsKey(playerUuid)) {
            relativeMaxHomes.put(playerUuid, relativeMaxHomes.get(playerUuid) - amount);
        }
        else {
            relativeMaxHomes.put(playerUuid, -amount);
        }

        db.subtractMaxHome(playerUuid.toString(), amount);
    }

    public void setMaxHome(@NotNull UUID playerUuid, @NotNull int amount) {
        if (relativeMaxHomes.containsKey(playerUuid)) relativeMaxHomes.remove(playerUuid);

        staticMaxHomes.put(playerUuid, amount);

        db.setMaxHome(playerUuid.toString(), amount);
    }

    public void resetMaxHome(@NotNull UUID playerUuid) {
        if (relativeMaxHomes.containsKey(playerUuid)) relativeMaxHomes.remove(playerUuid);
        if (staticMaxHomes.containsKey(playerUuid)) staticMaxHomes.remove(playerUuid);

        db.resetMaxHome(playerUuid.toString());
    }
}

package fr.neolithic.utilities.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utils.Database;
import fr.neolithic.utilities.utils.LocationUtils;
import fr.neolithic.utilities.utils.back.PlayersLastLocation;

public class SpawnExecutor implements TabExecutor {
    private Location spawn = null;
    private PlayersLastLocation playersLastLocation;
    private Database db;

    public SpawnExecutor(@NotNull Database db, @NotNull PlayersLastLocation playersLastLocation) {
        this.playersLastLocation = playersLastLocation;
        this.db = db;

        try {
            ResultSet resultSet = db.getSpawn();

            if (resultSet.next() && resultSet.getString("home").equalsIgnoreCase("spawn")) {
                spawn = new Location (Bukkit.getWorld(UUID.fromString(resultSet.getString("worldUuid"))),
                    resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"),
                    resultSet.getFloat("yaw"), resultSet.getFloat("pitch"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "setspawn":
                    spawn = LocationUtils.getApproximativeLocation(player.getLocation());
                    db.setSpawn(spawn.getWorld().getUID().toString(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
                    player.sendMessage("§eSpawn défini");
                    
                    return true;
                
                case "delspawn":
                    spawn = null;
                    db.deleteSpawn();
                    player.sendMessage("§eSpawn supprimé");

                    return true;
                
                case "spawn":
                    if (spawn == null) {
                        player.sendMessage("§cLe spawn n'a pas été défini");
                        return false;
                    }

                    player.sendMessage("§eTéléportation en cours...");
                    playersLastLocation.setPlayerLastLocation(player.getUniqueId(), player.getLocation());
                    player.teleport(spawn);

                    return true;

                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

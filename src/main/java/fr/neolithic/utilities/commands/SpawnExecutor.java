package fr.neolithic.utilities.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.Database;
import fr.neolithic.utilities.utilities.back.PlayersLastLocation;

public class SpawnExecutor implements TabExecutor {
    private Location spawn;
    private PlayersLastLocation playersLastLocation;
    private Database db;

    public SpawnExecutor(Location spawn, PlayersLastLocation playersLastLocation, Database db) {
        this.spawn = spawn;
        this.playersLastLocation = playersLastLocation;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "setspawn":
                    spawn = getApproximativeLocation(player.getLocation());
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

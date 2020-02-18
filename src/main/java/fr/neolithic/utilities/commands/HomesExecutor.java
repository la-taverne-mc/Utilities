package fr.neolithic.utilities.commands;

import java.util.List;

import com.google.common.collect.Lists;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.Homes;

public class HomesExecutor implements TabExecutor {
    private Homes homes;

    public HomesExecutor(Homes homes) {
        this.homes = homes;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String home = "";

            switch (command.getName().toLowerCase()) {
                case "sethome":
                    if (args.length != 1) {
                        player.sendMessage("§cUsage : /sethome <home>");
                        return false;
                    }

                    home = args[0].toLowerCase();

                    if (home.equalsIgnoreCase("bed")) {
                        player.sendMessage("§cLe home '" + home + "' existe déjà");
                        return false;
                    }

                    if (homes.addHome(player.getUniqueId(), home, player.getLocation())) {
                        player.sendMessage("§eLe home '" + home + "' a été défini");
                        return true;
                    }
                    else {
                        player.sendMessage("§cLe home '" + home + "' existe déjà");
                        return false;
                    }

                case "delhome":
                    if (args.length != 1) {
                        player.sendMessage("§cUsage : /delhome <home>");
                        return false;
                    }

                    home = args[0].toLowerCase();

                    if (home.equalsIgnoreCase("bed")) {
                        player.sendMessage("§cLe home '" + home + "' ne peut pas être supprimé");
                        return false;
                    }

                    if (homes.deleteHome(player.getUniqueId(), home)) {
                        player.sendMessage("§eLe home '" + home + "' a été supprimé");
                        return true;
                    }
                    else {
                        player.sendMessage("§cLe home '" + home + "' n'existe pas");
                        return false;
                    }
                
                case "homes":
                    player.sendMessage("§eVoici tes homes :");

                    String playerHomes = player.getBedSpawnLocation() == null ? "  §e§mbed" : "  §ebed";
                    List<String> _homes = homes.getPlayerHomes(player.getUniqueId());

                    if (_homes != null) {
                        for (String _home : _homes) {
                            playerHomes += "§r§e, " + _home;
                        }
                    }

                    player.sendMessage(playerHomes);

                    return true;

                case "home":
                    if (args.length != 1) {
                        player.sendMessage("§cUsage : /home <home>");
                        return false;
                    }

                    home = args[0].toLowerCase();

                    if (home.equalsIgnoreCase("bed")) {
                        if (player.getBedSpawnLocation() == null) {
                            player.sendMessage("§cTon lit est soit non défini, soit manquant, soit bloqué");
                            return false;
                        }
    
                        player.sendMessage("§eTéléportation en cours...");
                        player.teleport(player.getBedSpawnLocation());

                        return true;
                    }

                    Location loc = homes.getHomeLocation(player.getUniqueId(), home);

                    if (loc == null) {
                        player.sendMessage("§cLe home '" + home + "' n'existe pas");
                        return false;
                    }

                    player.sendMessage("§eTéléportation en cours...");
                    player.teleport(loc);

                    return true;
                    
                case "bed":
                    if (player.getBedSpawnLocation() == null) {
                        player.sendMessage("§cTon lit est soit non défini, soit manquant, soit bloqué");
                        return false;
                    }

                    player.sendMessage("§eTéléportation en cours...");
                    player.teleport(player.getBedSpawnLocation());

                    return true;
                
                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;

        if (command.getName().equalsIgnoreCase("delhome") || command.getName().equalsIgnoreCase("home")) {
            if (args.length == 1) {
                List<String> list = Lists.newArrayList();
                Player player = (Player) sender;
                List<String> playerHomes = homes.getPlayerHomes(player.getUniqueId());

                if (playerHomes == null) return null;

                for (String home : playerHomes) {
                    if (home.toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(home);
                    }
                }

                return list;
            }
        }
        
        return null;
    }
}

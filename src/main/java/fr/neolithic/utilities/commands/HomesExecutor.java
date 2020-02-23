package fr.neolithic.utilities.commands;

import java.util.List;
import java.util.Scanner;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.Database;
import fr.neolithic.utilities.utilities.back.PlayersLastLocation;
import fr.neolithic.utilities.utilities.homes.Homes;
import fr.neolithic.utilities.utilities.homes.MaxHomes;

public class HomesExecutor implements TabExecutor {
    private Homes homes;
    private MaxHomes maxHomes;
    private PlayersLastLocation playersLastLocation;

    public HomesExecutor(@NotNull Database db, @NotNull PlayersLastLocation playersLastLocation) {
        this.homes = new Homes(db);
        this.maxHomes = new MaxHomes(db);
        this.playersLastLocation = playersLastLocation;
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

                    if (homes.getPlayerHomes(player.getUniqueId()) == null || maxHomes.canPlayerCreateHome(player, homes.getPlayerHomes(player.getUniqueId()).size())) {
                        home = args[0].toLowerCase();

                        if (home.equalsIgnoreCase("bed")) {
                            player.sendMessage("§cLe home '" + home + "' existe déjà");
                            return false;
                        }

                        if (!StringUtils.isAlphanumeric(home)) {
                            player.sendMessage("§cLe nom d'un home ne peut que comporter des chiffres et des lettres");
                            return false;
                        }

                        if (home.length() > 16) {
                            player.sendMessage("§cLe nom d'un home ne peut pas faire plus de 16 caractères");
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
                    }
                    
                    player.sendMessage("§cTu possèdes déjà trop de homes supprime en un pour en définir un nouveau");
                    return false;

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
                        playersLastLocation.setPlayerLastLocation(player.getUniqueId(), player.getLocation());
                        player.teleport(player.getBedSpawnLocation());

                        return true;
                    }

                    Location loc = homes.getHomeLocation(player.getUniqueId(), home);

                    if (loc == null) {
                        player.sendMessage("§cLe home '" + home + "' n'existe pas");
                        return false;
                    }

                    player.sendMessage("§eTéléportation en cours...");
                    playersLastLocation.setPlayerLastLocation(player.getUniqueId(), player.getLocation());
                    player.teleport(loc);

                    return true;
                    
                case "bed":
                    if (player.getBedSpawnLocation() == null) {
                        player.sendMessage("§cTon lit est soit non défini, soit manquant, soit bloqué");
                        return false;
                    }

                    player.sendMessage("§eTéléportation en cours...");
                    playersLastLocation.setPlayerLastLocation(player.getUniqueId(), player.getLocation());
                    player.teleport(player.getBedSpawnLocation());

                    return true;
                
                default:
                    return false;
            }
        }
        else {
            Player target = null;

            switch (command.getName().toLowerCase()) {
                case "addmaxhomes":
                    if (args.length < 1 && args.length > 2) {
                        sender.sendMessage("§cUsage : /addmaxhomes <player> [amount]");
                        return false;
                    }
                    
                    target = Bukkit.getPlayer(args[0]);

                    if (target != null && target.isOnline()) {
                        int amount = 1;
                        
                        if (args.length == 2) {
                            if (isInteger(args[1])) {
                                amount = Integer.parseInt(args[1]);
                            }
                            else {
                                sender.sendMessage("§cLe montant de homes à ajouter doit être un nombre entier");
                            }
                        }

                        if (amount == 1) {
                            maxHomes.addMaxHome(target.getUniqueId(), 1);
                            sender.sendMessage("§e1 home a été ajouté à '" + target.getName() + "'");
                        }
                        else if (amount > 1) {
                            maxHomes.addMaxHome(target.getUniqueId(), amount);
                            sender.sendMessage("§e" + amount + "homes ont été ajouté à '" + target.getName() + "'");
                        }
                        else {
                            sender.sendMessage("§cLe montant de homes à ajouter doit être supérieur à 0");
                            return false;
                        }

                        return true;
                    }

                    sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                    return false;
                    
                case "subtractmaxhomes":
                    if (args.length < 1 && args.length > 2) {
                        sender.sendMessage("§cUsage : /subtractmaxhomes <player> [amount]");
                        return false;
                    }
                    
                    target = Bukkit.getPlayer(args[0]);

                    if (target != null && target.isOnline()) {
                        int amount = 1;
                        
                        if (args.length == 2) {
                            if (isInteger(args[1])) {
                                amount = Integer.parseInt(args[1]);
                            }
                            else {
                                sender.sendMessage("§cLe montant de homes à soustraire doit être un nombre entier");
                            }
                        }

                        if (amount == 1) {
                            maxHomes.subtractMaxHome(target.getUniqueId(), 1);
                            sender.sendMessage("§e1 home a été soustrait à '" + target.getName() + "'");
                        }
                        else if (amount > 1) {
                            maxHomes.subtractMaxHome(target.getUniqueId(), amount);
                            sender.sendMessage("§e" + amount + "homes ont été soustraits à '" + target.getName() + "'");
                        }
                        else {
                            sender.sendMessage("§cLe montant de homes à soustraire doit être supérieur à 0");
                            return false;
                        }
                        
                        return true;
                    }

                    sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                    return false;
                
                case "setmaxhomes":
                    if (args.length != 2) {
                        sender.sendMessage("§cUsage : /setmaxhomes <player> <amount>");
                        return false;
                    }
                    
                    target = Bukkit.getPlayer(args[0]);

                    if (target != null && target.isOnline()) {
                        if (isInteger(args[1])) {
                            int amount = Integer.parseInt(args[1]);

                            if (amount == 1) {
                                maxHomes.subtractMaxHome(target.getUniqueId(), 1);
                                sender.sendMessage("§e'" + target.getName() + "' a maintenant 1 home");
                            }
                            else if (amount > 1) {
                                maxHomes.subtractMaxHome(target.getUniqueId(), amount);
                                sender.sendMessage("§e'" + target.getName() + "' a maintenant " + amount + " homes");
                            }
                            else {
                                sender.sendMessage("§cLe montant de homes à soustraire doit être supérieur à 0");
                                return false;
                            }
                        }
                        else {
                            sender.sendMessage("§cLe montant de homes à définir doit être un nombre entier");
                        }
                    }

                    sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                    return false;
                    
                case "resetmaxhomes":
                    if (args.length != 1) {
                        sender.sendMessage("§cUsage : /resetmaxhomes <player>");
                        return false;
                    }
                    
                    target = Bukkit.getPlayer(args[0]);

                    if (target != null && target.isOnline()) {
                        maxHomes.resetMaxHome(target.getUniqueId());
                        sender.sendMessage("§eLe nombre de homes maximum à été remis par défaut pour '" + target.getName() + "'");
                        return true;
                    }

                    sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                    return false;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;

        if ((command.getName().equalsIgnoreCase("delhome") || command.getName().equalsIgnoreCase("home")) && args.length == 1) {
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
        
        return null;
    }

    public boolean isInteger(String str) {
        Scanner scanner = new Scanner(str.trim());
        if (!scanner.hasNextInt()) return false;
        scanner.nextInt();
        return !scanner.hasNext();
    }
}

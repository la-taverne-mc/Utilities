package fr.neolithic.utilities.commands;

import java.util.List;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utilities.back.PlayersLastLocation;

public class MiscellaneousExecutor implements TabExecutor {
    private PlayersLastLocation playersLastLocation;

    public MiscellaneousExecutor(PlayersLastLocation playersLastLocation) {
        this.playersLastLocation = playersLastLocation;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "back":
                    Location lastLocation = playersLastLocation.getPlayerLastLocation(player.getUniqueId());

                    if (lastLocation != null) {
                        player.sendMessage("§eTéléportation en cours...");
                        playersLastLocation.setPlayerLastLocation(player.getUniqueId(), player.getLocation());
                        player.teleport(lastLocation);
                        return true;
                    }
                    
                    player.sendMessage("§cTu n'as pas d'ancien emplacement au quel te téléporter");
                    return false;

                case "fly":
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null && target.isOnline()) {
                            if (target.getAllowFlight()) {
                                if (!player.getName().equals(target.getName())) {
                                    player.sendMessage("§eFly mode §cdésactivé §epour '" + target.getName() + "'");
                                }
                                target.sendMessage("§eFly mode §cdésactivé");
                                target.setAllowFlight(false);
                            }
                            else {
                                if (!player.getName().equals(target.getName())) {
                                    player.sendMessage("§eFly mode §aactivé §epour '" + target.getName() + "'");
                                }
                                target.sendMessage("§eFly mode §aactivé");
                                target.setAllowFlight(true);
                            }

                            return true;
                        }

                        player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }
                    else if (args.length == 0) {
                        if (player.getAllowFlight()) {
                            player.sendMessage("§eFly mode §cdésactivé");
                            player.setAllowFlight(false);
                        }
                        else {
                            player.sendMessage("§eFly mode §aactivé");
                            player.setAllowFlight(true);
                        }

                        return true;
                    }

                    player.sendMessage("§cUsage : /fly [username]");
                    return false;
                
                case "god":
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null && target.isOnline()) {
                            if (target.isInvulnerable()) {
                                if (!player.getName().equals(target.getName())) {
                                    player.sendMessage("§eGod mode §cdésactivé §epour '" + target.getName() + "'");
                                }
                                target.sendMessage("§eGod mode §cdésactivé");
                                target.setInvulnerable(false);
                            }
                            else {
                                if (!player.getName().equals(target.getName())) {
                                    player.sendMessage("§eGod mode §aactivé §epour '" + target.getName() + "'");
                                }
                                target.sendMessage("§eGod mode §aactivé");
                                target.setInvulnerable(true);
                            }

                            return true;
                        }

                        player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }
                    else if (args.length == 0) {
                        if (player.isInvulnerable()) {
                            player.sendMessage("§eGod mode §cdésactivé");
                            player.setInvulnerable(false);
                        }
                        else {
                            player.sendMessage("§eGod mode §aactivé");
                            player.setInvulnerable(true);
                        }

                        return true;
                    }

                    player.sendMessage("§cUsage : /god [username]");
                    return false;
                    
                case "heal":
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null && target.isOnline()) {
                            if (!player.getName().equals(target.getName())) {
                                player.sendMessage("§eLe joueur '" + target.getName() + "' a été heal");
                            }
                            target.sendMessage("§eTu as été heal");
                            player.setHealth(20);
                            player.setFoodLevel(20);
                            player.setSaturation(20.f);

                            return true;
                        }

                        player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }
                    else if (args.length == 0) {
                        player.sendMessage("§eTu as été heal");
                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.setSaturation(20.f);

                        return true;
                    }

                    player.sendMessage("§cUsage : /heal [username]");
                    return false;

                case "feed":
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null && target.isOnline()) {
                            if (!player.getName().equals(target.getName())) {
                                player.sendMessage("§eLe joueur '" + target.getName() + "' a été feed");
                            }
                            target.sendMessage("§eTu as été feed");
                            player.setFoodLevel(20);
                            player.setSaturation(20.f);

                            return true;
                        }

                        player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }
                    else if (args.length == 0) {
                        player.sendMessage("§eTu as été feed");
                        player.setFoodLevel(20);
                        player.setSaturation(20.f);

                        return true;
                    }

                    player.sendMessage("§cUsage : /feed [username]");
                    return false;

                default:
                    return false;
            }
        }
        
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;

        if ((command.getName().equalsIgnoreCase("fly") || command.getName().equalsIgnoreCase("god")) && args.length == 1) {
            List<String> list = Lists.newArrayList();

            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            
            return list;
        }
        
        return null;
    }
}
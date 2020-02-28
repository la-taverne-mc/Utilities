package fr.neolithic.utilities.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.neolithic.utilities.utils.IntegerUtils;

public class GamemodeExecutor implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = player;

            switch (command.getName().toLowerCase()) {
                case "gm":
                    if (args.length < 1 || args.length > 2) {
                        player.sendMessage("§cUsage : /gm <gamemode> [target player]");
                        return false;
                    }

                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            target = Bukkit.getPlayer(args[1]);
                        }
                        else {
                            player.sendMessage("§cLe joueur '" + args[1] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    if (IntegerUtils.isInteger(args[0])) {
                        int gamemode = Integer.parseInt(args[0]);
                        switch (gamemode) {
                            case 0:
                                if (target != player) {
                                    player.sendMessage("§e'" + target.getName() + "' est passé en mode survie");
                                }
                                target.sendMessage("§eTu es passé en mode survie");
                                target.setGameMode(GameMode.SURVIVAL);
                                return true;
                            case 1:
                                if (target != player) {
                                    player.sendMessage("§e'" + target.getName() + "' est passé en mode créatif");
                                }
                                target.sendMessage("§eTu es passé en mode créatif");
                                target.setGameMode(GameMode.CREATIVE);
                                return true;
                            case 2:
                                if (target != player) {
                                    player.sendMessage("§e'" + target.getName() + "' est passé en mode aventure");
                                }
                                target.sendMessage("§eTu es passé en mode aventure");
                                target.setGameMode(GameMode.ADVENTURE);
                                return true;
                            case 3:
                                if (target != player) {
                                    player.sendMessage("§e'" + target.getName() + "' est passé en mode spectateur");
                                }
                                target.sendMessage("§eTu es passé en mode spectateur");
                                target.setGameMode(GameMode.SPECTATOR);
                                return true;
                            default:
                                player.sendMessage("§cIl n'y a pas de gamemode associé à '" + String.valueOf(gamemode) + "'");
                                return false;
                        }
                    }

                    return true;
    
                case "gms":
                    if (args.length > 1) {
                        player.sendMessage("§cUsage : /gms [target player]");
                        return false;
                    }

                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            target = Bukkit.getPlayer(args[0]);
                        }
                        else {
                            player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    if (target != player) {
                        player.sendMessage("§e'" + target.getName() + "' est passé en mode survie");
                    }
                    target.sendMessage("§eTu es passé en mode survie");
                    target.setGameMode(GameMode.SURVIVAL);
                    return true;
                    
                case "gmc":
                    if (args.length > 1) {
                        player.sendMessage("§cUsage : /gmc [target player]");
                        return false;
                    }

                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            target = Bukkit.getPlayer(args[0]);
                        }
                        else {
                            player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    if (target != player) {
                        player.sendMessage("§e'" + target.getName() + "' est passé en mode créatif");
                    }
                    target.sendMessage("§eTu es passé en mode créatif");
                    target.setGameMode(GameMode.CREATIVE);
                    return true;
    
                case "gma":
                    if (args.length > 1) {
                        player.sendMessage("§cUsage : /gma [target player]");
                        return false;
                    }

                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            target = Bukkit.getPlayer(args[0]);
                        }
                        else {
                            player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    if (target != player) {
                        player.sendMessage("§e'" + target.getName() + "' est passé en mode aventure");
                    }
                    target.sendMessage("§eTu es passé en mode aventure");
                    target.setGameMode(GameMode.ADVENTURE);
                    return true;
    
                case "gmsp":
                    if (args.length > 1) {
                        player.sendMessage("§cUsage : /gmsp [target player]");
                        return false;
                    }

                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            target = Bukkit.getPlayer(args[0]);
                        }
                        else {
                            player.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    if (target != player) {
                        player.sendMessage("§e'" + target.getName() + "' est passé en mode spectateur");
                    }
                    target.sendMessage("§eTu es passé en mode spectateur");
                    target.setGameMode(GameMode.SPECTATOR);
                    return true;
    
                default:
                    return false;
            }
        }
        else {
            Player target = null;

            switch (command.getName().toLowerCase()) {
                case "gm":
                    if (args.length != 2) {
                        sender.sendMessage("§cUsage : /gm <gamemode> <target player>");
                        return false;
                    }

                    if (Bukkit.getPlayer(args[1]) != null) {
                        target = Bukkit.getPlayer(args[1]);
                    }
                    else {
                        sender.sendMessage("§cLe joueur '" + args[1] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }

                    if (IntegerUtils.isInteger(args[0])) {
                        int gamemode = Integer.parseInt(args[0]);
                        switch (gamemode) {
                            case 0:
                                sender.sendMessage("§e'" + target.getName() + "' est passé en mode survie");
                                target.sendMessage("§eTu es passé en mode survie");
                                target.setGameMode(GameMode.SURVIVAL);
                                return true;
                            case 1:
                                sender.sendMessage("§e'" + target.getName() + "' est passé en mode créatif");
                                target.sendMessage("§eTu es passé en mode créatif");
                                target.setGameMode(GameMode.CREATIVE);
                                return true;
                            case 2:
                                sender.sendMessage("§e'" + target.getName() + "' est passé en mode aventure");
                                target.sendMessage("§eTu es passé en mode aventure");
                                target.setGameMode(GameMode.ADVENTURE);
                                return true;
                            case 3:
                                sender.sendMessage("§e'" + target.getName() + "' est passé en mode spectateur");
                                target.sendMessage("§eTu es passé en mode spectateur");
                                target.setGameMode(GameMode.SPECTATOR);
                                return true;
                            default:
                                sender.sendMessage("§cIl n'y a pas de gamemode associé à '" + String.valueOf(gamemode) + "'");
                                return false;
                        }
                    }

                    return true;
    
                case "gms":
                    if (args.length != 1) {
                        sender.sendMessage("§cUsage : /gms <target player>");
                        return false;
                    }

                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            target = Bukkit.getPlayer(args[0]);
                        }
                        else {
                            sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                            return false;
                        }
                    }

                    sender.sendMessage("§e'" + target.getName() + "' est passé en mode survie");
                    target.sendMessage("§eTu es passé en mode survie");
                    target.setGameMode(GameMode.SURVIVAL);
                    return true;
                    
                case "gmc":
                    if (args.length != 1) {
                        sender.sendMessage("§cUsage : /gmc <target player>");
                        return false;
                    }

                    if (Bukkit.getPlayer(args[0]) != null) {
                        target = Bukkit.getPlayer(args[0]);
                    }
                    else {
                        sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }

                    sender.sendMessage("§e'" + target.getName() + "' est passé en mode créatif");
                    target.sendMessage("§eTu es passé en mode créatif");
                    target.setGameMode(GameMode.CREATIVE);
                    return true;
    
                case "gma":
                    if (args.length != 1) {
                        sender.sendMessage("§cUsage : /gma <target player>");
                        return false;
                    }

                    if (Bukkit.getPlayer(args[0]) != null) {
                        target = Bukkit.getPlayer(args[0]);
                    }
                    else {
                        sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }

                    sender.sendMessage("§e'" + target.getName() + "' est passé en mode aventure");
                    target.sendMessage("§eTu es passé en mode aventure");
                    target.setGameMode(GameMode.ADVENTURE);
                    return true;
    
                case "gmsp":
                    if (args.length != 1) {
                        sender.sendMessage("§cUsage : /gmsp <target player>");
                        return false;
                    }

                    if (Bukkit.getPlayer(args[0]) != null) {
                        target = Bukkit.getPlayer(args[0]);
                    }
                    else {
                        sender.sendMessage("§cLe joueur '" + args[0] + "' n'existe pas ou n'est pas en ligne");
                        return false;
                    }

                    sender.sendMessage("§e'" + target.getName() + "' est passé en mode spectateur");
                    target.sendMessage("§eTu es passé en mode spectateur");
                    target.setGameMode(GameMode.SPECTATOR);
                    return true;
    
                default:
                    return false;
            }
        }
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

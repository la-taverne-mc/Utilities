package fr.neolithic.utilities.commands;

import java.util.List;

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
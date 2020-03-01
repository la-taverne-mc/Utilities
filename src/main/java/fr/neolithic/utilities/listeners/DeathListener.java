package fr.neolithic.utilities.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import fr.neolithic.utilities.utils.back.PlayersLastLocation;

public class DeathListener implements Listener {
    private PlayersLastLocation playersLastLocation;

    public DeathListener(@NotNull PlayersLastLocation playersLastLocation) {
        this.playersLastLocation = playersLastLocation;
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            event.getEntity().sendMessage("§eFais §c/back §epour revenir à l'emplacement de ta mort");
            playersLastLocation.setPlayerLastLocation(event.getEntity().getUniqueId(), event.getEntity().getLocation());
        }
    }
}
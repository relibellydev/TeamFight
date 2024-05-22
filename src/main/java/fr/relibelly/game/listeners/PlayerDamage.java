package fr.relibelly.game.listeners;

import fr.relibelly.TeamFight;
import fr.relibelly.game.GameTypes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
                return;
            }

            if (TeamFight.getInstance().getGame().getGameTypes() != GameTypes.PLAYING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {}
}

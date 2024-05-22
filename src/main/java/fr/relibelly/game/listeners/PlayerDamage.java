package fr.relibelly.game.listeners;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.tasks.RoundTask;
import fr.relibelly.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDamage implements Listener {

    private final Game game = TeamFight.getInstance().getGame();

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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            GamePlayer victimPlayer = game.getGamePlayerByPlayer(victim);
            GamePlayer damagerPlayer = game.getGamePlayerByPlayer(damager);

            if (game.getGameTypes() != GameTypes.PLAYING) {
                event.setCancelled(true);
                return;
            }

            if (victimPlayer.getTeam().getName().equals(damagerPlayer.getTeam().getName())) {
                event.setCancelled(true);
                return;
            }

            damagerPlayer.addHit();
            damagerPlayer.addTotalHits();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        victim.setHealth(20.0D);
        event.getDrops().clear();
        event.setDeathMessage(null);
        victim.getInventory().clear();
        GamePlayer victimPlayer = game.getGamePlayerByPlayer(event.getEntity());
        victimPlayer.addDeath();
        victimPlayer.getTeam().addDeath();
        if (victimPlayer.getTeam().getDeaths() >= victimPlayer.getTeam().getSize()) {
            new RoundTask(victimPlayer.getTeam().equals(game.getRedTeam()) ? game.getBlueTeam() : game.getRedTeam()).runTaskTimer(TeamFight.getInstance(), 0L, 20L);
        }
        PlayerUtils.cleanPlayer(victim, GameMode.SPECTATOR);
        victim.teleport(game.getLocations().getSpawn());
        victim.setAllowFlight(true);
        victim.setFlying(true);
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            GamePlayer killerPlayer = game.getGamePlayerByPlayer(killer);
            killerPlayer.addKill();
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                gamePlayer.getPlayer().sendMessage(TeamFight.PREFIX + victimPlayer.getTeam().getFormattedTeamName() + victim.getName() + " §7a été tué par " + killerPlayer.getTeam().getFormattedTeamName() + killer.getName());
            }
        } else {
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                gamePlayer.getPlayer().sendMessage(TeamFight.PREFIX + victimPlayer.getTeam().getFormattedTeamName() + victim.getName() + " §7a été tué par le vide.");
            }
        }
    }
}

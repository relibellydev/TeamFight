package fr.relibelly.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class PlayerUtils {
    public static void cleanPlayer(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setTotalExperience(0);
        player.setExp(0);
        player.setHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.getInventory().clear();
        player.getInventory().setBoots(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setHelmet(null);
    }

    public static void setNameTag(Player player, String prefix, String suffix, int sortPriority) {
        Scoreboard manager = player.getScoreboard();
        String priority = ((sortPriority < 10) ? "0" : "") + sortPriority;
        Team team = null;
        for(Team t : manager.getTeams()) {
            if (t.getPrefix().equals(prefix) && t.getSuffix().equals(suffix) && t.getName().startsWith(priority)) {
                team = t;
                break;
            }
        }
        while(team == null) {
            String tn = priority + UUID.randomUUID().toString().substring(30);
            if (manager.getTeam(tn) == null) {
                team = manager.registerNewTeam(tn);
                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        }
        team.addEntry(player.getName());
    }
}

package fr.relibelly.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
}

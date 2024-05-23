package fr.relibelly.game.moving;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MoveRunnable extends BukkitRunnable {

    private final Game game = TeamFight.getInstance().getGame();
    private final double SPECTATOR_RADIUS = 100;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = game.getGamePlayerByPlayer(player);
            if (game.getGameTypes() == GameTypes.WAITING || game.getGameTypes() == GameTypes.FINISHING) {
                if (player.getLocation().getY() < TeamFight.getInstance().getVoidLimit()) {
                    player.teleport(game.getLocations().getSpawn());
                }
            } else if (game.getGameTypes() == GameTypes.PLAYING) {
                if (player.getLocation().getY() < TeamFight.getInstance().getVoidLimit()) {
                    if (!gamePlayer.isSpectator()) {
                        player.setHealth(0.0);

                        PlayerUtils.cleanPlayer(player, GameMode.SPECTATOR);
                        player.teleport(game.getLocations().getSpawn());
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }
                }
            }


            if (gamePlayer.isSpectator() || player.getGameMode() == GameMode.SPECTATOR) {
                if (gamePlayer.getPlayer().getLocation().distance(game.getLocations().getCenter()) > SPECTATOR_RADIUS) {
                    player.teleport(game.getLocations().getSpawn());
                }
            }
        }
    }
}

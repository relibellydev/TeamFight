package fr.relibelly.game.tasks;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartingTask extends BukkitRunnable {

    private int timer = 11;
    private Game game;

    public GameStartingTask() {
        this.game = TeamFight.getInstance().getGame();
    }

    @Override
    public void run() {
        this.timer--;
        if (game.getGameTypes() != GameTypes.WAITING) {
            cancel();
            return;
        }

        if (game.getPlayers().size() < TeamFight.getInstance().getMaxPerTeam() * 2) {
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                gamePlayer.getPlayer().sendMessage(TeamFight.PREFIX + "§cDébut annulé, un joueur à quitté.");
            }
            cancel();
            return;
        }

        if (timer == 10 || (timer <= 5 && timer >= 1)) {
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                TeamFight.getInstance().getTitle().sendTitle(gamePlayer.getPlayer(), 1, 20, 1, "", "Lancement dans §a" + timer + (timer == 1 ? " seconde" : "secondes"));
                gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.NOTE_STICKS, 1F, 1F);
            }
        }

        if (timer == 0) {
            game.startGame();
            cancel();
            return;
        }
    }
}

package fr.relibelly.game.tasks;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {


    private Game game;
    private int timer = 0;

    public GameTimerTask() {
       this.game = TeamFight.getInstance().getGame();
    }

    @Override
    public void run() {
        this.timer++;
        if (this.game.getGameTypes() != GameTypes.PLAYING) {
            cancel();
            return;
        }

        this.game.setTimer(this.timer);
    }
}

package fr.relibelly.game.tasks;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundTask extends BukkitRunnable {

    private final Game game;
    private int timer;
    private Team goalerTeam;

    public RoundTask(Team goalerTeam) {
        this.game = TeamFight.getInstance().getGame();
        this.goalerTeam = goalerTeam;
        if (goalerTeam == null) {
            timer = 4;
        } else {
            goalerTeam.addPoint();
            game.cleanMap();
            timer = 5;
        }
    }

    @Override
    public void run() {
        this.timer--;
        if (goalerTeam != null) {
            if (timer == 4) {
                for (GamePlayer gamePlayer : game.getPlayers().values()) {
                    TeamFight.getInstance().getTitle().sendTitle(gamePlayer.getPlayer(), 1, 18, 1, goalerTeam.getColorCode() + "§L+1 " + goalerTeam.getName());
                    if (!gamePlayer.isSpectator()) {
                        gamePlayer.getPlayer().sendMessage("§7§m--------------------------------------");
                        gamePlayer.getPlayer().sendMessage("§fLes " + goalerTeam.getFormattedTeamName() + "§font gagné la manche ! §7(" + goalerTeam.getPoints() + "/" + TeamFight.getInstance().getObjective() + ")");
                        gamePlayer.getPlayer().sendMessage("§fTu as infligé §b" + gamePlayer.getHits() + " hits §fdurant cette manche.");
                        gamePlayer.getPlayer().sendMessage("§7§m--------------------------------------");
                        gamePlayer.setHits(0);
                    }
                }

                if (goalerTeam.getPoints() >= TeamFight.getInstance().getObjective()) {
                    Bukkit.getScheduler().runTaskLater(TeamFight.getInstance(), () -> game.endGame(goalerTeam), 20L);
                    cancel();
                    return;
                }
            }
        }

        if (timer <= 3 && timer >= 1) {
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                if (!gamePlayer.isSpectator()) {
                    TeamFight.getInstance().getTitle().sendTitle(gamePlayer.getPlayer(), 5, 20, 5, "", goalerTeam != null ? "§fProchaine manche dans §b" + timer + "s" : "§fDebut de la partie dans §b" + timer + "s");
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.NOTE_BASS, 1F, 1F);
                    gamePlayer.getPlayer().teleport(gamePlayer.getTeam().getSpawn());
                    if (goalerTeam != null) {
                        gamePlayer.givePlayingInventory();
                    }
                }
            }
        }

        if (timer == 0) {
            for (GamePlayer gamePlayer : game.getPlayers().values()) {
                if (!gamePlayer.isSpectator()) {
                    TeamFight.getInstance().getTitle().sendTitle(gamePlayer.getPlayer(), 10, 40, 10, "§a§lAU COMBAT ! ", "§6Bonne change !");
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.NOTE_PLING, 1F, 1F);
                    gamePlayer.getPlayer().teleport(gamePlayer.getTeam().getSpawn());
                    gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }


}

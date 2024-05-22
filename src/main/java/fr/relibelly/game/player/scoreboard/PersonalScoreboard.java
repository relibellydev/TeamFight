package fr.relibelly.game.player.scoreboard;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.utils.TimerUtils;
import fr.relibelly.utils.scoreboard.ObjectiveSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PersonalScoreboard {
    private Player player;

    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    private GamePlayer gamePlayer;
    private Game game;

    private TeamFight main;
    private String timer;

    public PersonalScoreboard(Player player){
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "Scoreboard");

        this.main = TeamFight.getInstance();
        reloadData();
        objectiveSign.addReceiver(player);
    }

    public void reloadData(){
        this.main = TeamFight.getInstance();
        this.game = this.main.getGame();
        this.gamePlayer = game.getGamePlayerByPlayer(player);
        this.timer = TimerUtils.formatTimer(game.getTimer());
    }

    public void setLines(String ip){
        objectiveSign.setDisplayName("§6§lTeamFight");

        if (game.getGameTypes() == GameTypes.WAITING) {
            objectiveSign.setLine(1, "§f");
            objectiveSign.setLine(2, "§8• §fMode: §b" + main.getMaxPerTeam() + "v" + main.getMaxPerTeam());
            objectiveSign.setLine(3, "§8• §fJoueurs: §a" + game.getPlayers().size() + "/" + main.getMaxPerTeam() * 2);
            objectiveSign.setLine(4, "§7");
            objectiveSign.setLine(5, ip);
        } else if (game.getGameTypes() == GameTypes.PLAYING) {
            objectiveSign.setLine(0, "§c");
            objectiveSign.setLine(1, "§8• §fChrono: §a" + this.timer);
            objectiveSign.setLine(2, "§e");
            objectiveSign.setLine(3, "§8• §cRouge: §f" + this.game.getRedTeam().getPoints() + "/" + main.getObjective());
            objectiveSign.setLine(4, "§8• §9Bleu: §f" + this.game.getBlueTeam().getPoints() + "/" + main.getObjective());
            objectiveSign.setLine(5, "§d");
            objectiveSign.setLine(6, "§8• §fTués: §a" + this.gamePlayer.getKills());
            objectiveSign.setLine(7, "§8• §fMorts: §c" + this.gamePlayer.getDeaths());
            objectiveSign.setLine(8, "§8• §fHits: §d" + this.gamePlayer.getHits());
            objectiveSign.setLine(9, "§a");
            objectiveSign.setLine(10, ip);
        } else if (game.getGameTypes() == GameTypes.FINISHING) {
            objectiveSign.setLine(1, "§f ");
            objectiveSign.setLine(2, "§cPartie terminée");
            objectiveSign.setLine(3, "§bMerci d'avoir joué !");
            objectiveSign.setLine(4, "§3 ");

            objectiveSign.setLine(10, ip);
        }

        objectiveSign.updateLines();
    }

    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}

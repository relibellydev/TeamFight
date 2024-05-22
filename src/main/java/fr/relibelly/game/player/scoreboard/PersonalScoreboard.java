package fr.relibelly.game.player.scoreboard;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
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
    }

    public void setLines(String ip){
        objectiveSign.setDisplayName("§6§lTeamFight");

        if (game.getGameTypes() == GameTypes.WAITING) {
            objectiveSign.setLine(1, "§1");
            objectiveSign.setLine(2, "§8• §fMode: §b" + main.getMaxPerTeam() + "v" + main.getMaxPerTeam());
            objectiveSign.setLine(3, "§8• §fJoueurs: §a" + game.getPlayers().size() + "/" + main.getMaxPerTeam() * 2);
            objectiveSign.setLine(4, "§2");
            objectiveSign.setLine(5, ip);
        } else if (game.getGameTypes() == GameTypes.PLAYING) {
            objectiveSign.setLine(1, "§1");
            objectiveSign.setLine(2, "§8• §cChrono: §a");
            objectiveSign.setLine(3, "§2");
            objectiveSign.setLine(4, "§8• §cRouge: §f" + this.game.getRedTeam().getPoints() + "/" + main.getObjective());
            objectiveSign.setLine(5, "§8• §9Bleu: §f" + this.game.getBlueTeam().getPoints() + "/" + main.getObjective());
            objectiveSign.setLine(6, "§3");
            objectiveSign.setLine(7, "§8• §7Tués: §a" + this.gamePlayer.getKills());
            objectiveSign.setLine(8, "§8• §7Morts: §c" + this.gamePlayer.getDeaths());
            objectiveSign.setLine(9, "§8• §7Hits: §d" + this.gamePlayer.getHits());
            objectiveSign.setLine(10, "§4");
            objectiveSign.setLine(11, ip);
        } else if (game.getGameTypes() == GameTypes.FINISHING) {
            objectiveSign.setLine(1, "§1");
            objectiveSign.setLine(2, "§cPartie terminée");
            objectiveSign.setLine(3, "§bMerci d'avoir joué !");
            objectiveSign.setLine(4, "§2");

            objectiveSign.setLine(5, ip);
        }

        objectiveSign.updateLines();
    }

    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}

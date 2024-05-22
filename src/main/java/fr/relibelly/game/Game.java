package fr.relibelly.game;

import fr.relibelly.TeamFight;
import fr.relibelly.game.locations.Locations;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.tasks.RoundTask;
import fr.relibelly.game.tasks.GameTimerTask;
import fr.relibelly.game.teams.Team;
import fr.relibelly.utils.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class Game {

    private int timer;
    private GameTypes gameTypes;
    private Map<UUID, GamePlayer> players;
    private List<Team> teams;
    private Team redTeam;
    private Team blueTeam;
    private Locations locations;
    private ArrayList<Location> blocks;

    public Game() {
        this.gameTypes = GameTypes.WAITING;
        this.players = new HashMap<>();
        this.teams = new ArrayList<>();
        this.redTeam = new Team("Rouge", "§c", ChatColor.RED, Color.RED);
        this.blueTeam = new Team("Bleu", "§9", ChatColor.BLUE, Color.BLUE);

        this.redTeam.setGame(this);
        this.blueTeam.setGame(this);

        this.teams.add(this.redTeam);
        this.teams.add(this.blueTeam);
        this.locations = new Locations();
        this.blocks = new ArrayList<>();

    }


    public Team getSmallestTeam() {
        Optional<Team> team = teams.stream()
                .filter(Team::isNotFull)
                .min(Comparator.comparingInt(Team::getSize));

        return team.orElse(null);
    }

    public GamePlayer getGamePlayerByPlayer(Player player) {
        return this.players.values().stream().filter(gp -> gp.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public Team getTeamByName(String name) {
        return this.teams.stream().filter(team -> team.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void cleanMap() {
        this.blocks.forEach(location -> location.getBlock().setType(Material.AIR));
        this.blocks.clear();
    }

    public void checkPlayers() {
        for (Team team : getTeams()) {
            if (team.getPlayers().isEmpty()) {
                for (Team t : getTeams()) {
                    if (!t.getPlayers().isEmpty()) {
                        endGame(t);
                    }
                }
            }
        }
    }



    public void startGame() {
        this.gameTypes = GameTypes.PLAYING;

        for (GamePlayer gamePlayer : this.players.values()) {
            if (!gamePlayer.isSpectator()) {
                gamePlayer.joinAvaiblableTeam();
                gamePlayer.setTeamNametag();
                gamePlayer.getPlayer().teleport(gamePlayer.getTeam().getSpawn());
                gamePlayer.givePlayingInventory();
            }
        }

        new RoundTask(null).runTaskTimer(TeamFight.getInstance(), 0L, 20L);
        new GameTimerTask().runTaskTimer(TeamFight.getInstance(), 0L, 20L);
    }

    public void endGame(Team winnerTeam) {
        this.gameTypes = GameTypes.FINISHING;

        for (GamePlayer gamePlayer : this.players.values()) {
            if (!gamePlayer.isSpectator()) {
                PlayerUtils.cleanPlayer(gamePlayer.getPlayer(), GameMode.ADVENTURE);
                gamePlayer.getPlayer().setAllowFlight(true);
                gamePlayer.getPlayer().setFlying(true);
                TeamFight.getInstance().getTitle().sendTitle(gamePlayer.getPlayer(), 20, 60, 20,
                        gamePlayer.getTeam().getName().equals(winnerTeam.getName()) ? "§6§lVictoire" : "§cDéfaite", gamePlayer.getTeam().getName().equals(winnerTeam.getName()) ? "§eBien joué !" : "§fVictoire de l'équipe " + winnerTeam.getFormattedTeamName());
                gamePlayer.getPlayer().sendMessage("§7§m--------------------------------------");
                gamePlayer.getPlayer().sendMessage("§fL'équipe' " + winnerTeam.getFormattedTeamName() + "§fa gagné la partie !");
                gamePlayer.getPlayer().sendMessage("§fTu as infligé au total §b" + gamePlayer.getTotalHits() + " hits §fdurant la partie.");
                gamePlayer.getPlayer().sendMessage("");
                List<GamePlayer> bestHitters = new ArrayList<>(this.players.values());
                bestHitters.sort(Comparator.comparingInt(GamePlayer::getTotalHits).reversed());
                int i = 1;
                for (GamePlayer gp : bestHitters) {
                    gamePlayer.getPlayer().sendMessage("§6#" + i + " §f- §a" + gp.getPlayer().getName() + " §f- §b" + gp.getTotalHits() + " hits");
                    i++;
                }
                gamePlayer.getPlayer().sendMessage("");
                gamePlayer.getPlayer().sendMessage("§cFermeture du serveur dans 15 secondes.");
                gamePlayer.getPlayer().sendMessage("§7§m--------------------------------------");
            }
        }

        //Shutdown
        Bukkit.getScheduler().runTaskLater(TeamFight.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(TeamFight.PREFIX + "§cFin de la partie. Fermeture du serveur...");
            }

            shutdown();
        }, 20 * 15);

    }

    private void shutdown() {
        Bukkit.getScheduler().runTaskLater(TeamFight.getInstance(), Bukkit::shutdown, 20L);
    }
}

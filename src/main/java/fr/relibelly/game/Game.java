package fr.relibelly.game;

import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.teams.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;

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

    public Game() {
        this.gameTypes = GameTypes.WAITING;
        this.players = new HashMap<>();
        this.teams = new ArrayList<>();
        this.redTeam = new Team("Rouge", "§c", Color.RED);
        this.blueTeam = new Team("Bleu", "§9", Color.BLUE);

        this.redTeam.setGame(this);
        this.blueTeam.setGame(this);

        this.teams.add(this.redTeam);
        this.teams.add(this.blueTeam);

    }


    public Team getSmallestTeam() {
        Optional<Team> team = teams.stream()
                .filter(Team::isNotFull)
                .min(Comparator.comparingInt(Team::getSize));

        return team.orElse(null);
    }
}

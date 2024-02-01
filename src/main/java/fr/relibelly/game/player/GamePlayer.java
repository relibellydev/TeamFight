package fr.relibelly.game.player;

import fr.relibelly.game.teams.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class GamePlayer {

    private Player player;
    private int kills;
    private int deaths;
    private Team team;

    public GamePlayer(Player player) {
        this.player = player;
    }
}

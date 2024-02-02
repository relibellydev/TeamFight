package fr.relibelly.game.teams;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class Team {

    private int points;
    private String name;
    private String colorCode;
    private Color color;
    private Game game;

    public Team(String name, String colorCode, Color color) {
        this.name = name;
        this.colorCode = colorCode;
        this.color = color;
        this.points = 0;
    }

    public HashMap<UUID, GamePlayer> getPlayers() {
        HashMap<UUID, GamePlayer> players = new HashMap<>();
        for (GamePlayer gamePlayer : game.getPlayers().values()) {
            if (gamePlayer.getTeam() != null && gamePlayer.getTeam() == this) {
                players.put(gamePlayer.getPlayer().getUniqueId(), gamePlayer);
            }
        }
        return players;
    }

    public boolean isFull() {
        return getPlayers().size() >= TeamFight.getInstance().getMaxPerTeam();
    }

    public boolean isNotFull() {
        return getPlayers().size() < TeamFight.getInstance().getMaxPerTeam();
    }

    public int getSize() {
        return getPlayers().size();
    }
}

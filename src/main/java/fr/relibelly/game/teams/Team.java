package fr.relibelly.game.teams;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Team {

    private int points;
    private int deaths;
    private String name;
    private String colorCode;
    private ChatColor chatColor;
    private Color color;
    private Game game;

    public Team(String name, String colorCode, ChatColor chatColor, Color color) {
        this.name = name;
        this.colorCode = colorCode;
        this.chatColor = chatColor;
        this.color = color;
        this.points = 0;
        this.deaths = 0;
    }

    public HashMap<UUID, GamePlayer> getPlayers() {
        return this.game.getPlayers().values().stream()
                .filter(gp -> gp.getTeam() != null && gp.getTeam().equals(this))
                .collect(Collectors.toMap(gp -> gp.getPlayer().getUniqueId(), gp -> gp, (a, b) -> a, HashMap::new));
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

    public String getFormattedTeamName() {
        return this.chatColor + this.name + " " + this.chatColor;
    }

    public void addPoint() {
        this.points++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public Location getSpawn() {
        switch (name) {
            case "Rouge":
                return TeamFight.getInstance().getGame().getLocations().getRedSpawn();
            case "Bleu":
                return TeamFight.getInstance().getGame().getLocations().getBlueSpawn();
        }
        return null;
    }
}

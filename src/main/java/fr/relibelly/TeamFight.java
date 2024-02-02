package fr.relibelly;

import fr.relibelly.game.Game;
import fr.relibelly.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class TeamFight extends JavaPlugin {

    private static TeamFight instance;

    private String worldName;
    private Location spawn;
    private Location redSpawn;
    private Location blueSpawn;
    private Location center;
    private int maxPerTeam;
    private int objective;

    private Game game;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.maxPerTeam = getConfig().getInt("TeamFight.Settings.maxPerTeam");
        this.objective = getConfig().getInt("TeamFight.Settings.objective");
        this.worldName = getConfig().getString("TeamFight.Locations.worldName");
        this.spawn = LocationUtils.str2loc(getConfig().getString("TeamFight.Locations.spawn"), worldName);
        this.center = LocationUtils.str2loc(getConfig().getString("TeamFight.Locations.center"), worldName);
        this.redSpawn = LocationUtils.str2loc(getConfig().getString("TeamFight.Locations.redSpawn"), worldName);
        this.blueSpawn = LocationUtils.str2loc(getConfig().getString("TeamFight.Locations.blueSpawn"), worldName);

        this.game = new Game();

    }

    @Override
    public void onDisable() {
    }


    public static TeamFight getInstance() {
        return instance;
    }
}

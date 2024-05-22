package fr.relibelly.game.locations;

import fr.relibelly.TeamFight;
import fr.relibelly.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public class Locations {

    private final TeamFight instance = TeamFight.getInstance();

    private final String worldName;
    private final Location spawn;
    private final Location redSpawn;
    private final Location blueSpawn;
    private final Location center;

    public Locations() {
        this.worldName = this.instance.getConfig().getString("TeamFight.Locations.worldName");

        this.spawn = LocationUtils.str2loc(this.instance.getConfig().getString("TeamFight.Locations.spawn"), worldName);
        this.center = LocationUtils.str2loc(this.instance.getConfig().getString("TeamFight.Locations.center"), worldName);
        this.redSpawn = LocationUtils.str2loc(this.instance.getConfig().getString("TeamFight.Locations.redSpawn"), worldName);
        this.blueSpawn = LocationUtils.str2loc(this.instance.getConfig().getString("TeamFight.Locations.blueSpawn"), worldName);
    }

    public boolean isEmpty() {
        return this.spawn.equals(new Location(Bukkit.getWorld(worldName), 0.0, 0.0, 0.0, 0.0F, 0.0F))
                && this.center.equals(new Location(Bukkit.getWorld(worldName), 0.0, 0.0, 0.0, 0.0F, 0.0F))
                && this.redSpawn.equals(new Location(Bukkit.getWorld(worldName), 0.0, 0.0, 0.0, 0.0F, 0.0F))
                && this.blueSpawn.equals(new Location(Bukkit.getWorld(worldName), 0.0, 0.0, 0.0, 0.0F, 0.0F));
    }
}

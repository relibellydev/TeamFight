package fr.relibelly;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class TeamFight extends JavaPlugin {

    private static TeamFight instance;

    private Location spawn;
    private Location redSpawn;
    private Location blueSpawn;
    private Location center;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static TeamFight getInstance() {
        return instance;
    }
}

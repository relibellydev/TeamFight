package fr.relibelly.game.listeners.manager;

import fr.relibelly.TeamFight;
import fr.relibelly.game.listeners.PlayerConnection;
import fr.relibelly.game.listeners.PlayerDamage;
import fr.relibelly.game.listeners.PlayerListener;
import fr.relibelly.game.listeners.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Register {

    private TeamFight instance;
    private PluginManager pm;

    public Register() {
        this.instance = TeamFight.getInstance();
        this.pm = Bukkit.getServer().getPluginManager();
    }

    public void registerListener() {
        this.pm.registerEvents(new PlayerConnection(), instance);
        this.pm.registerEvents(new PlayerListener(), instance);
        this.pm.registerEvents(new ServerListener(), instance);
        this.pm.registerEvents(new PlayerDamage(), instance);
    }
}

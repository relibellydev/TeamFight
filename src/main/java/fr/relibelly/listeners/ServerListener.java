package fr.relibelly.listeners;

import fr.relibelly.TeamFight;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(TeamFight.getInstance().getGame().getGameTypes().getDisplayName());
    }
}

package fr.relibelly.game.listeners;

import fr.relibelly.game.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        GamePlayer.join(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        GamePlayer.quit(player);
    }
}

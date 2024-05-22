package fr.relibelly.game.listeners;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.teams.Team;
import fr.relibelly.game.teams.ui.TeamSelector;
import fr.relibelly.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private Game game = TeamFight.getInstance().getGame();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getGamePlayerByPlayer(player);
        if (game.getGameTypes() == GameTypes.PLAYING) {
            if (!gamePlayer.isSpectator()) {
                if (event.getBlock().getType() == Material.SANDSTONE) {
                    if (game.getBlocks().contains(event.getBlock().getLocation())) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        player.getInventory().addItem(new ItemBuilder(Material.SANDSTONE).build(true));
                        game.getBlocks().remove(event.getBlock().getLocation());
                        return;
                    }
                    TeamFight.getInstance().getTitle().sendActionBar(player, "§cVous ne pouvez pas casser ce bloc !");
                    event.setCancelled(true);
                    return;
                }
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getGamePlayerByPlayer(player);
        if (game.getGameTypes() == GameTypes.PLAYING) {
            if (!gamePlayer.isSpectator()) {
                if (event.getBlock().getType() == Material.SANDSTONE) {
                    if (event.getBlock().getLocation().getY() < TeamFight.getInstance().getHeightLimit()) {
                        event.setCancelled(false);
                        game.getBlocks().add(event.getBlock().getLocation());
                        return;
                    }
                    TeamFight.getInstance().getTitle().sendActionBar(player, "§cVous avez atteint la limite de hauteur !");
                    event.setCancelled(true);
                }
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (game.getGameTypes() == GameTypes.WAITING) {
            event.setCancelled(true);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§f" + player.getName() + " §8» §f" + event.getMessage()));
        } else {
            event.setCancelled(true);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(game.getGamePlayerByPlayer(player).getTeam().getFormattedTeamName() + player.getName() + " §8» §f" + event.getMessage()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = game.getGamePlayerByPlayer(player);
        if (event.getAction() == null && event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if (event.getClickedInventory().getName().equals("Choisir une équipe")) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            String displayName = currentItem.getItemMeta().getDisplayName();
            if (displayName.equals("§7Aléatoire")) {
                gamePlayer.setTeam(null);
                player.closeInventory();
                player.getInventory().setItem(4, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§aChoisir une équipe §f(Clic droit)").build(false));
                return;
            }

            Team team = game.getTeamByName(displayName.substring(2));
            if (team != null) {
                if (gamePlayer.getTeam() == null || !gamePlayer.getTeam().equals(team)) {
                    gamePlayer.setTeam(team);
                    new TeamSelector(player).open();
                    player.getInventory().setItem(4, new ItemBuilder(Material.WOOL, 1, (short) (team.getName().equals("Rouge") ? 14 : 11)).setDisplayName("§aChoisir une équipe §f(Clic droit)").build(false));
                }
            } else {
                TeamFight.getInstance().getLogger().info("ERROR: Team null in TEAMSELECTOR (PlayerListener.java) inventory click");
            }
            return;
        }

        if (event.getSlot() >= 36 && event.getSlot() <= 39) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameTypes() != GameTypes.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == null && event.getItem() == null) return;
        Player player = event.getPlayer();
        if (game.getGameTypes() == GameTypes.WAITING) {
            switch (event.getItem().getType()) {
                case WOOL:
                case STAINED_CLAY:
                    new TeamSelector(player).open();
                    break;
            }
        }
    }


}

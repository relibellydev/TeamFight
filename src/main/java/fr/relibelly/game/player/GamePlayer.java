package fr.relibelly.game.player;

import fr.relibelly.TeamFight;
import fr.relibelly.game.Game;
import fr.relibelly.game.GameTypes;
import fr.relibelly.game.tasks.GameStartingTask;
import fr.relibelly.game.teams.Team;
import fr.relibelly.utils.ItemBuilder;
import fr.relibelly.utils.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class GamePlayer {

    private Player player;
    private int kills;
    private int deaths;
    private int hits;
    private int totalHits;
    private Team team;
    private Game game;
    private boolean spectator;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void addKill() {
        this.kills++;
    }
    public void addHit() {
        this.hits++;
    }
    public void addDeath() {
        this.deaths++;
    }
    public void addTotalHits() {
        this.totalHits++;
    }

    public void givePlayingInventory() {
        PlayerUtils.cleanPlayer(player, GameMode.ADVENTURE);
        this.player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setDisplayName(this.team.getColor() + this.team.getName()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setLeatherArmorColor(this.team.getColor()).build(false));
        this.player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName(this.team.getColor() + this.team.getName()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setLeatherArmorColor(this.team.getColor()).build(false));
        this.player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName(this.team.getColor() + this.team.getName()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setLeatherArmorColor(this.team.getColor()).build(false));
        this.player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(this.team.getColor() + this.team.getName()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setLeatherArmorColor(this.team.getColor()).build(false));

        this.player.getInventory().setItem(0, new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).setInfinityDurability().build(false));
        this.player.getInventory().setItem(1, new ItemBuilder(Material.GOLDEN_APPLE, 10).setInfinityDurability().build(false));
        this.player.getInventory().setItem(8, new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 2).setInfinityDurability().build(false));
        for (int i = 2; i < 8; i++)
            this.player.getInventory().setItem(i, (new ItemBuilder(Material.SANDSTONE, 64)).build(true));
    }

    public void joinAvaiblableTeam() {
        if (this.team == null) {
            setTeam(this.game.getSmallestTeam());
        }
    }

    public void setTeamNametag() {
        if (this.team.getName().equals("Rouge")) {
            PlayerUtils.setNameTag(player, "§cRouge ", "", 1);
        } else {
            PlayerUtils.setNameTag(player, "§9Bleu ", "", 2);
        }
    }

    public static void join(Player player) {
        Game g = TeamFight.getInstance().getGame();
        GamePlayer gamePlayer = new GamePlayer(player);
        gamePlayer.setGame(g); 
        g.getPlayers().put(player.getUniqueId(), gamePlayer);
        if (g.getGameTypes() == GameTypes.WAITING && g.getPlayers().size() <= 2 * TeamFight.getInstance().getMaxPerTeam()) {
            player.teleport(g.getLocations().getSpawn());
            gamePlayer.setTeam(null);
            gamePlayer.setSpectator(false);
            PlayerUtils.cleanPlayer(player, GameMode.ADVENTURE);
            PlayerUtils.setNameTag(player, "§f", "", 1);
            player.getInventory().setItem(4, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§aChoisir une équipe §f(Clic droit)").build(false));
            for (GamePlayer  gp : g.getPlayers().values()) {
                gp.getPlayer().sendMessage(TeamFight.PREFIX + "§b" + player.getName() + "§f a rejoint la partie. §a(" + g.getPlayers().size() + "/" + 2 * TeamFight.getInstance().getMaxPerTeam() + ")");
            }

            //Start game
            if (g.getPlayers().size() == TeamFight.getInstance().getMaxPerTeam() * 2) {
                new GameStartingTask().runTaskTimer(TeamFight.getInstance(), 0L, 20L);
            }
        } else {
            gamePlayer.setSpectator(true);
            player.teleport(g.getLocations().getSpawn());
            PlayerUtils.cleanPlayer(player, GameMode.SPECTATOR);
            player.setAllowFlight(true);
            player.setFlying(true);
            PlayerUtils.setNameTag(player, "§7Spectateur §7", "", 100);
        }

        TeamFight.getInstance().getScoreboardManager().onLogin(player);
    }

    public static void quit(Player player) {
        Game g = TeamFight.getInstance().getGame();
        GamePlayer gamePlayer = g.getGamePlayerByPlayer(player);
        g.getPlayers().remove(player.getUniqueId());
        if (g.getGameTypes() == GameTypes.WAITING) {
            for (GamePlayer  gp : g.getPlayers().values()) {
                gp.getPlayer().sendMessage(TeamFight.PREFIX + "§b" + player.getName() + "§f a quitté la partie. §c(" + g.getPlayers().size() + "/" + 2 * TeamFight.getInstance().getMaxPerTeam() + ")");
            }
        } else {
            if (!gamePlayer.isSpectator()) {
                g.checkPlayers();
            }
        }

        TeamFight.getInstance().getScoreboardManager().onLogout(player);
    }
}

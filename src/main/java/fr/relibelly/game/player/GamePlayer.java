package fr.relibelly.game.player;

import fr.relibelly.game.Game;
import fr.relibelly.game.teams.Team;
import fr.relibelly.utils.ItemBuilder;
import fr.relibelly.utils.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

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

        this.player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).setInfinityDurability().build(false));
        this.player.getInventory().setItem(1, new ItemBuilder(Material.GOLDEN_APPLE, 10).setInfinityDurability().build(false));
        this.player.getInventory().setItem(8, new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 2).setInfinityDurability().build(false));
        for (int i = 2; i < 8; i++)
            this.player.getInventory().setItem(i, (new ItemBuilder(Material.SANDSTONE, 64)).build(true));
    }

    public void joinAvaiblableTeam() {
        if (this.team != null) {
            setTeam(this.game.getSmallestTeam());
        }
    }

    public static void join(Player player) {
        //TODO
    }

    public static void quit(Player player) {
        //TODO
    }
}

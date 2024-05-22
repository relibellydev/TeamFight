package fr.relibelly.game.teams.ui;

import fr.relibelly.TeamFight;
import fr.relibelly.game.player.GamePlayer;
import fr.relibelly.game.teams.Team;
import fr.relibelly.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamSelector {

    private final Player player;

    public TeamSelector(Player player) {
        this.player = player;
    }

    public void open() {
        Inventory ui = Bukkit.createInventory(null, 9, "Choisir une équipe");
        for (Team team : TeamFight.getInstance().getGame().getTeams()) {
            List<String> lore = new ArrayList<>();
            for (GamePlayer gamePlayer : team.getPlayers().values()) {
                lore.add("§f- §7" + gamePlayer.getPlayer().getName());
            }
            lore.add("");
            lore.add("§b» §6Clique pour rejoindre");
            ui.addItem(new ItemBuilder(Material.WOOL, 1, (short) (team.getName().equals("Rouge") ? 14 : 11)).setDisplayName(team.getColorCode() + team.getName()).setLoreWithList(lore).build(false));
        }

        ui.setItem(8, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§7Aléatoire").setLoreWithList(Arrays.asList("", "§b» §6Clique pour rejoindre")).build(false));
        this.player.openInventory(ui);
    }
}

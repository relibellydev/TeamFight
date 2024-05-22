package fr.relibelly;

import fr.relibelly.game.Game;
import fr.relibelly.game.listeners.manager.Register;
import fr.relibelly.game.moving.MoveRunnable;
import fr.relibelly.utils.Title;
import fr.relibelly.utils.scoreboard.ScoreboardManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public final class TeamFight extends JavaPlugin {

    public static String PREFIX = "§6[TeamFight] §7";

    @Getter
    private static TeamFight instance;

    private int maxPerTeam;
    private int objective;
    private int heightLimit;
    private int voidLimit;

    private Game game;
    private final Title title = new Title();

    private ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.maxPerTeam = getConfig().getInt("TeamFight.Settings.maxPerTeam");
        this.objective = getConfig().getInt("TeamFight.Settings.objective");
        this.heightLimit = getConfig().getInt("TeamFight.Settings.heightLimit");
        this.voidLimit = getConfig().getInt("TeamFight.Settings.voidLimit");

        this.game = new Game();

        if (!game.getLocations().isEmpty()) {
            new Register().registerListener();
            new MoveRunnable().runTaskTimer(this, 0L, 5L);

            this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
            this.executorMonoThread = Executors.newScheduledThreadPool(1);
            this.scoreboardManager = new ScoreboardManager();
        } else {
            getLogger().info("No locations were registered in the config.yml.");
        }

    }

    @Override
    public void onDisable() {
    }


}

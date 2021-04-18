package me.danvb10.sustenance;

import me.danvb10.sustenance.commands.CommandManager;
import me.danvb10.sustenance.gui.scoreboard.ScoreboardManager;
import me.danvb10.sustenance.listeners.Listeners;
import me.danvb10.sustenance.utilities.config.ConfigManager;
import me.danvb10.sustenance.utilities.config.SimpleConfigGet;
import me.danvb10.sustenance.utilities.logging.LoggingManager;
import me.danvb10.sustenance.utilities.messaging.ActionBarManager;
import me.danvb10.sustenance.utilities.messaging.MessagingManager;
import me.danvb10.sustenance.utilities.playerdata.NutritionManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    // Be careful not to use these objects within each other before they've been declared!
    // Declare necessary plugin-wide support objects
    public static SimpleConfigGet simpleConfigGet;
    public static LoggingManager logger;
    public static MessagingManager messagingManager;
    public static ConfigManager configManager;
    public static NutritionManager nutritionManager;
    public static ActionBarManager actionBarManager;
    public static ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        // Instantiate/initialize necessary plugin-wide support objects
        simpleConfigGet = new SimpleConfigGet(this);
        logger = new LoggingManager(this);
        configManager = new ConfigManager(this);
        messagingManager = new MessagingManager(this);
        nutritionManager = new NutritionManager(this);
        actionBarManager = new ActionBarManager(this);
        scoreboardManager = new ScoreboardManager(this);
        // Enable commands
        CommandManager commandManager = new CommandManager(this);
        // Register listeners
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
    }

    @Override
    public void onDisable() {
        nutritionManager.removeSaveAll();
        configManager.disable();
        scoreboardManager.destroyAllBoards();
    }
}

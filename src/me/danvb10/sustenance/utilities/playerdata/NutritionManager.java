package me.danvb10.sustenance.utilities.playerdata;

import me.danvb10.sustenance.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NutritionManager {

    Main plugin;
    private HashMap<UUID, PlayerNutrition> nutritionHashMap = new HashMap<UUID, PlayerNutrition>();

    String datapath;

    public NutritionManager(Main plugin) {
        this.plugin = plugin;
        datapath = plugin.getDataFolder().getAbsolutePath() + "/userdata/";

        // create PlayerNutrition objects for each online player that won't engage onJoin()
        for(Player p : Bukkit.getOnlinePlayers()) {
            plugin.logger.verbose("Player is already online, redirect: " + p.getName());
            addPlayer(p);
        }
    }

    public void addPlayer(Player p) {
        PlayerNutrition pn;
        if(PlayerNutritionFileManager.isReadable(datapath, p.getUniqueId().toString())) {
            plugin.logger.verbose("found datafile for " + p.getName());
            pn = PlayerNutritionFileManager.reader(datapath, p.getUniqueId().toString());
            if(pn == null) {
                plugin.logger.verbose("the datafile for " + p.getName() + " was null! creating new");
                pn = new PlayerNutrition(p);
            } else {
                plugin.logger.verbose("datafile for " + p.getName() + " successfully loaded");
            }
        } else {
            plugin.logger.verbose("no datafile for " + p.getName() + ", creating new");
            pn = new PlayerNutrition(p);
        }

        nutritionHashMap.put(p.getUniqueId(), pn.getThis());
        plugin.logger.verbose("created PlayerNutrition object for player: " + p.getName());
    }

    public void removeSavePlayer(Player p) {
        PlayerNutritionFileManager.writer(datapath, nutritionHashMap.get(p.getUniqueId()));
        nutritionHashMap.remove(p.getUniqueId());
        plugin.logger.verbose("removed PlayerNutrition object for player: " + p.getName());
    }

    public void removeSaveAll() {
        plugin.logger.verbose("removing all PlayerNutrition objects");
        for(Player p : Bukkit.getOnlinePlayers()) {
            removeSavePlayer(p);
        }
        plugin.logger.verbose("removed");
    }

    public PlayerNutrition getPlayerNutrition(Player p) {
        return nutritionHashMap.get(p.getUniqueId());
    }

    public boolean isConditionallyExempt(Player p) {
        if(getPlayerNutrition(p).isExempt() || p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
            return true;
        }
        return false;
    }

}

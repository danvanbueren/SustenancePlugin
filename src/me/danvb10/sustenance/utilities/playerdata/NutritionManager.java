package me.danvb10.sustenance.utilities.playerdata;

import me.danvb10.sustenance.Sustenance;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class NutritionManager {

    Sustenance plugin;
    private final HashMap<UUID, PlayerNutrition> nutritionHashMap = new HashMap<UUID, PlayerNutrition>();

    String datapath;

    public NutritionManager(Sustenance plugin) {
        this.plugin = plugin;
        datapath = plugin.getDataFolder().getAbsolutePath() + "/userdata/";

        // create PlayerNutrition objects for each online player that won't engage onJoin()
        for(Player p : Bukkit.getOnlinePlayers()) {
            Sustenance.logger.verbose("NutritionManager:NutritionManager(): " + "Player is already online, redirect: " + p.getName());
            addPlayer(p);
        }
    }

    public void addPlayer(Player p) {
        PlayerNutrition pn;
        if(PlayerNutritionFileManager.isReadable(datapath, p.getUniqueId().toString())) {
            Sustenance.logger.verbose("NutritionManager:addPlayer(): " + "found datafile for " + p.getName());
            pn = PlayerNutritionFileManager.reader(datapath, p.getUniqueId().toString());
            if(pn == null) {
                Sustenance.logger.verbose("NutritionManager:addPlayer(): " + "the datafile for " + p.getName() + " was null! creating new");
                pn = new PlayerNutrition(p);
            } else {
                Sustenance.logger.verbose("NutritionManager:addPlayer(): " + "datafile for " + p.getName() + " successfully loaded");
            }
        } else {
            Sustenance.logger.verbose("NutritionManager:addPlayer(): " + "no datafile for " + p.getName() + ", creating new");
            pn = new PlayerNutrition(p, 75, 75, 75, 75);
        }

        nutritionHashMap.put(p.getUniqueId(), pn.getThis());
        Sustenance.logger.verbose("NutritionManager:addPlayer(): " + "created PlayerNutrition object for player: " + p.getName());
    }

    public void removeSavePlayer(Player p) {
        getPlayerNutrition(p).clearActiveEffects();
        PlayerNutritionFileManager.writer(datapath, nutritionHashMap.get(p.getUniqueId()));
        nutritionHashMap.remove(p.getUniqueId());
        Sustenance.logger.verbose("NutritionManager:removeSavePlayer(): " + "removed PlayerNutrition object for player: " + p.getName());
    }

    public void removeSaveAll() {
        Sustenance.logger.verbose("NutritionManager:removeSaveAll(): " + "removing all PlayerNutrition objects");
        for(Player p : Bukkit.getOnlinePlayers()) {
            removeSavePlayer(p);
        }
        Sustenance.logger.verbose("NutritionManager:removeSaveAll(): " + "removed");
    }

    public PlayerNutrition getPlayerNutrition(Player p) {
        return nutritionHashMap.get(p.getUniqueId());
    }

    public boolean isConditionallyExempt(Player p) {
        return getPlayerNutrition(p).isExempt() || p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR);
    }

}

package me.danvb10.sustenance.gui.scoreboard;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    Main plugin;

    HashMap<UUID, BukkitTask> boardOnIndefinitely = new HashMap<UUID, BukkitTask>();
    HashMap<UUID, BukkitTask> boardOnDefinitelyAllTasks = new HashMap<UUID, BukkitTask>();

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void showBoardDefiniteTime(Player p, int seconds) {

        BukkitTask showBoardTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    setBoardToCurrentValues(p);
                } catch (Exception e) {
                    plugin.logger.error("Runnable for showTimedBoard() encountered an error updating!\n" + e.getStackTrace());
                }
            }
        }, 0L, 5L);

        BukkitTask cancelTaskAfterTime = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
        @Override
        public void run() {
            try {
                showBoardTask.cancel();
                if(!boardOnIndefinitely.containsKey(p.getUniqueId())) {
                    setBoardToClear(p);
                }
            } catch (Exception e) {
                plugin.logger.error("Runnable for showTimedBoard() encountered an error cancelling!\n" + e.getStackTrace());
            }
        }
    }, seconds * 20L);

        boardOnDefinitelyAllTasks.put(p.getUniqueId(), showBoardTask);

    }

    public BukkitTask showBoardIndefiniteTime(Player p) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    setBoardToCurrentValues(p);
                } catch (Exception e) {
                    plugin.logger.error("Runnable for showTimedBoard() encountered an error updating!\n" + e.getStackTrace());
                }
            }
        }, 0L, 5L);

        return bukkitTask;
    }

    private void setBoardToCurrentValues(Player p) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective obj = board.registerNewObjective("SustBrd846285923", "dummy");
        obj.setDisplayName("Nutrition Status");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        PlayerNutrition pn = Main.nutritionManager.getPlayerNutrition(p);

        Score line1 = obj.getScore("- Fruit: " + pn.getValue(Category.FRUIT) + "%");
        line1.setScore(4);

        Score line2 = obj.getScore("- Grain: " + pn.getValue(Category.GRAIN) + "%");
        line2.setScore(3);

        Score line3 = obj.getScore("- Protein: " + pn.getValue(Category.PROTEIN) + "%");
        line3.setScore(2);

        Score line4 = obj.getScore("- Vegetables: " + pn.getValue(Category.FRUIT) + "%");
        line4.setScore(1);

        p.setScoreboard(board);
    }

    private void setBoardToClear(Player p) {
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void toggleBoard(Player p) {
        UUID id = p.getUniqueId();

        if(boardOnIndefinitely.containsKey(id)) {
            Main.messagingManager.info("Hiding nutrition status", p);
            // cancel task
            boardOnIndefinitely.get(id).cancel();
            // remove from tracker list
            boardOnIndefinitely.remove(id);
            // clear any definite tasks
            boardOnDefinitelyAllTasks.forEach((thisId, thisTask) -> {
                thisTask.cancel();
            });
            // clear board from gui
            setBoardToClear(p);
        } else {
            Main.messagingManager.info("Showing nutrition status", p);
            boardOnIndefinitely.put(id, showBoardIndefiniteTime(p));
        }
    }

}

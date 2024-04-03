package me.danvb10.sustenance.gui.scoreboard;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ScoreboardManager {
    Sustenance plugin;
    HashMap<UUID, BukkitTask> boardOnIndefinitely = new HashMap<>();
    HashMap<UUID, BukkitTask> boardOnDefinitelyAllTasks = new HashMap<>();

    public ScoreboardManager(Sustenance plugin) {
        this.plugin = plugin;
    }

    public void showBoardDefiniteTime(Player p, int seconds) {

        BukkitTask showBoardTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            try {
                setBoardToCurrentValues(p);
            } catch (Exception e) {
                Sustenance.logger.verbose("ScoreboardManager:showBoardDefiniteTime(): " + "Runnable for showTimedBoard() encountered an error updating!\n" + Arrays.toString(e.getStackTrace()));
            }
        }, 0L, 5L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                showBoardTask.cancel();
                if(!boardOnIndefinitely.containsKey(p.getUniqueId())) {
                    setBoardToClear(p);
                }
            } catch (Exception e) {
                Sustenance.logger.verbose("ScoreboardManager:showBoardDefiniteTime(): " + "Runnable for showTimedBoard() encountered an error cancelling!\n" + Arrays.toString(e.getStackTrace()));
            }
        }, seconds * 20L);

        boardOnDefinitelyAllTasks.put(p.getUniqueId(), showBoardTask);

    }

    public BukkitTask showBoardIndefiniteTime(Player p) {

        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            try {
                setBoardToCurrentValues(p);
            } catch (Exception e) {
                Sustenance.logger.verbose("ScoreboardManager:showBoardIndefiniteTime(): " + "Runnable for showTimedBoard() encountered an error updating!\n" + Arrays.toString(e.getStackTrace()));
            }
        }, 0L, 5L);
    }

    private void setBoardToCurrentValues(Player p) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();

        Objective obj = board.registerNewObjective("SustBrd846285923", "dummy", "");
        obj.setDisplayName("Nutrition Status");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition(p);

        if(Sustenance.nutritionManager.isConditionallyExempt(p)) {
            Score line0 = obj.getScore(ChatColor.RED + "" + ChatColor.BOLD + "YOU ARE EXEMPT!");
            line0.setScore(5);
        }

        Score line1 = obj.getScore("- Fruit: " + pn.getValue(Category.FRUIT) + "%");
        line1.setScore(4);

        Score line2 = obj.getScore("- Grain: " + pn.getValue(Category.GRAIN) + "%");
        line2.setScore(3);

        Score line3 = obj.getScore("- Protein: " + pn.getValue(Category.PROTEIN) + "%");
        line3.setScore(2);

        Score line4 = obj.getScore("- Vegetables: " + pn.getValue(Category.VEGETABLE) + "%");
        line4.setScore(1);

        p.setScoreboard(board);
    }

    private void setBoardToClear(Player p) {
        p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
    }

    public void toggleBoard(Player p) {
        UUID id = p.getUniqueId();
        if(boardOnIndefinitely.containsKey(id)) {
            Sustenance.messagingManager.info("Hiding nutrition status", p);
            removeBoardAndReferences(p);
        } else {
            Sustenance.messagingManager.info("Showing nutrition status", p);
            boardOnIndefinitely.put(id, showBoardIndefiniteTime(p));
        }
    }

    public void removeBoardAndReferences(Player p) {
        try {
            UUID id = p.getUniqueId();
            // cancel task
            boardOnIndefinitely.get(id).cancel();
            // remove from tracker list
            boardOnIndefinitely.remove(id);
            // clear any definite tasks
            boardOnDefinitelyAllTasks.forEach((thisId, thisTask) -> thisTask.cancel());
            // clear board from gui
            setBoardToClear(p);
        } catch (Exception e) {
            Sustenance.logger.verbose("ScoreboardManager:removeBoardAndReferences(): " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void destroyAllBoards() {
        try {
            boardOnDefinitelyAllTasks.forEach((id, task) -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getUniqueId().equals(id)) {
                        removeBoardAndReferences(p);
                    }
                }
            });
            boardOnIndefinitely.forEach((id, task) -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getUniqueId().equals(id)) {
                        removeBoardAndReferences(p);
                    }
                }
            });
        } catch (Exception e) {
            Sustenance.logger.verbose("ScoreboardManager:destroyAllBoards(): " + Arrays.toString(e.getStackTrace()));
        }
    }
}
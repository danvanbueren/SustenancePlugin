package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusStatuslist {
    public static void goCode(CommandSender sender, Command command, String label, String[] args) {

        Boolean playersAdded = false;

        String message = "Nutrition values for current online players...\n";
        for(Player p : Bukkit.getOnlinePlayers()) {
            playersAdded = true;
            message += "'" + p.getName() + "': " + Main.nutritionManager.getPlayerNutrition(p).getFormattedNutrition() + "\n";
        }

        if(!playersAdded) {
            message += "There are no players currently online!";
        }

        Main.messagingManager.info(message, sender);

    }
}

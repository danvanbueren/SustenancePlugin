package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusStatuslist {
    public static void goCode(CommandSender sender, Command command, String label, String[] args) {

        boolean playersAdded = false;

        StringBuilder message = new StringBuilder("Nutrition values for current online players...\n");
        for(Player p : Bukkit.getOnlinePlayers()) {
            playersAdded = true;
            message.append("'").append(p.getName()).append("': ").append(Main.nutritionManager.getPlayerNutrition(p).getFormattedNutrition()).append("\n");
        }

        if(!playersAdded) {
            message.append("There are no players currently online!");
        }

        Main.messagingManager.info(message.toString(), sender);

    }
}

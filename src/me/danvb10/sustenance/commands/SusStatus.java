package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusStatus {

    public static void goCode(CommandSender sender, String[] args) {

        if(args.length > 1) {
            boolean foundPlayer = false;
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getName().equalsIgnoreCase(args[1])) {
                    foundPlayer = true;

                    if(sender instanceof Player) {
                        Player pCheck = (Player) sender;
                        if(p.getUniqueId().equals(pCheck.getUniqueId())) {
                            Main.scoreboardManager.toggleBoard(pCheck);
                        } else {
                            Main.messagingManager.info("Nutrition status for '" + p.getName() + "'...\n" + Main.nutritionManager.getPlayerNutrition(p).getFormattedNutrition(), sender);
                        }
                    } else {
                        Main.messagingManager.info("Nutrition status for '" + p.getName() + "'...\n" + Main.nutritionManager.getPlayerNutrition(p).getFormattedNutrition(), sender);
                    }
                }
            }
            if(!foundPlayer) {
                Main.messagingManager.info("Could not find an online player named '" + args[1] + "'", sender);
            }
        } else {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                Main.scoreboardManager.toggleBoard(p);
            } else {
                Main.messagingManager.info("Self lookup not available in console!\nUsage: sus status {PlayerName} <- SHOWS STATUS OF PLAYER\n(alt): sus statuslist <- SHOWS STATUS OF ALL ONLINE PLAYERS", sender);
            }
        }

    }
}

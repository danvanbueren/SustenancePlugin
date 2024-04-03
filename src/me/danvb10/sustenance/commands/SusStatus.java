package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Sustenance;
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
                            Sustenance.scoreboardManager.toggleBoard(pCheck);
                        } else {
                            Sustenance.messagingManager.info("Nutrition status for '" + p.getName() + "'...\n" + Sustenance.nutritionManager.getPlayerNutrition(p).getFormattedNutrition(), sender);
                        }
                    } else {
                        Sustenance.messagingManager.info("Nutrition status for '" + p.getName() + "'...\n" + Sustenance.nutritionManager.getPlayerNutrition(p).getFormattedNutrition(), sender);
                    }
                }
            }
            if(!foundPlayer) {
                Sustenance.messagingManager.info("Could not find an online player named '" + args[1] + "'", sender);
            }
        } else {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                Sustenance.scoreboardManager.toggleBoard(p);
            } else {
                Sustenance.messagingManager.info("Self lookup not available in console!\nUsage: sus status {PlayerName} <- SHOWS STATUS OF PLAYER\n(alt): sus statuslist <- SHOWS STATUS OF ALL ONLINE PLAYERS", sender);
            }
        }

    }
}

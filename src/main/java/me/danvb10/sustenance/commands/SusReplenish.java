package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Sustenance;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusReplenish {
    public static void goCode(CommandSender sender, Command command, String label, String[] args) {
        boolean foundPlayer = false;

        if(args.length > 1) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equalsIgnoreCase(args[1])) {
                    boolean sentMessage = false;
                    Sustenance.nutritionManager.getPlayerNutrition(p).setFullStats();
                    if(sender instanceof Player) {
                        if(p.equals((Player) sender)) {
                            sentMessage = true;
                            Sustenance.messagingManager.info("Replenished your nutrition!", sender);
                        }
                    }
                    if(!sentMessage) {
                        Sustenance.messagingManager.info("Replenishing all categories for player '" + p.getName() +"'", sender);
                        Sustenance.messagingManager.info("Your nutrition has been replenished by a server admin!", p);
                    }
                    foundPlayer = true;
                }
            }
        } else if(sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            Sustenance.nutritionManager.getPlayerNutrition(p).setFullStats();
            Sustenance.messagingManager.info("Replenished your nutrition!", sender);
            foundPlayer = true;
        } else {
            Sustenance.messagingManager.info("Self replenish not available in console!\nUsage: sus replenish {PlayerName}", sender);
            foundPlayer = true;
        }

        if (!foundPlayer) {
            Sustenance.messagingManager.info("Could not find an online player named '" + args[1] + "'", sender);
        }
    }
}
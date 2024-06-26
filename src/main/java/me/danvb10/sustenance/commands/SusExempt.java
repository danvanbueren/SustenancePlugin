package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusExempt {

    static final String consoleUsage = "Console usage: /sus exempt {player}";
    static final String inGameUsage = "Usage: /sus exempt [player]";
    static boolean isPlayer;

    public static boolean goCode(CommandSender sender, Command command, String label, String[] args) {

        isPlayer = sender instanceof Player;

        if (args.length < 2 && !isPlayer) {
            Sustenance.messagingManager.info("Missing required arguments!\n" + getUsage(), sender);
            return false;
        }

        Player p;
        if(sender instanceof Player) {
            if (args.length > 1) {
                p = CommandManager.getPlayer(args[1]);
                if (p == null) {
                    Sustenance.messagingManager.info("Couldn't find online player '" + args[1] + "'", sender);
                    return false;
                }
                PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition(p);
                if (pn.isExempt()) {
                    pn.setExempt(false);

                    if (p.equals((Player) sender)) {
                        Sustenance.messagingManager.info("You are no longer exempt from nutrition!", sender);
                    } else {
                        Sustenance.messagingManager.info("'" + p.getName() + "' is no longer exempt from nutrition!", sender);
                    }

                } else {
                    pn.setExempt(true);
                    if (p.equals((Player) sender)) {
                        Sustenance.messagingManager.info("You are now exempt from nutrition!", sender);
                    } else {
                        Sustenance.messagingManager.info("'" + p.getName() + "' is now exempt from nutrition!", sender);
                    }
                }
            } else {
                PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition((Player) sender);
                if (pn.isExempt()) {
                    pn.setExempt(false);
                    Sustenance.messagingManager.info("You are no longer exempt from nutrition!", sender);
                } else {
                    pn.setExempt(true);
                    Sustenance.messagingManager.info("You are now exempt from nutrition!", sender);
                }
            }
        } else {
            // Sender is console
            p = CommandManager.getPlayer(args[1]);
            if (p == null) {
                Sustenance.messagingManager.info("Couldn't find online player '" + args[1] + "'", sender);
                return false;
            }
            PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition(p);
            if (toggleExempt(pn)) {
                Sustenance.messagingManager.info(p.getName() + " is now exempt from nutrition!", sender);
                Sustenance.messagingManager.info("An admin set you to be exempt from nutrition!", p);
            } else {
                Sustenance.messagingManager.info(p.getName() + " is no longer exempt from nutrition!", sender);
                Sustenance.messagingManager.info("An admin set you to no longer be exempt from nutrition!", p);
            }
        }

    return true;
    }

    private static String getUsage() {
        if(isPlayer) {
            return inGameUsage;
        } else {
            return consoleUsage;
        }
    }

    private static boolean toggleExempt(PlayerNutrition pn) {
        if (pn.isExempt()) {
            pn.setExempt(false);
            return false;
            // false to represent no longer exempt
        } else {
            pn.setExempt(true);
            return true;
            // true to represent now exempt
        }
    }
}

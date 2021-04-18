package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.enums.CategoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;

public class SusSetvalue {

    static final String consoleUsage = "Console usage: /sus setvalue {category} {value} {player}";
    static final String inGameUsage = "Usage: /sus setvalue {category} {value} [player]";
    static boolean isPlayer;

    public static boolean goCode(CommandSender sender, Command command, String label, String[] args) {

        isPlayer = sender instanceof Player;

        // Check to make sure all required arguments are present
        if((args.length < 3 && isPlayer) || (args.length < 4 && !isPlayer)) {
            Main.messagingManager.info("Missing required arguments!\n" + getUsage(), sender);
            return false;
        }

        // Required arguments are in place, let's make them
        Category category = CategoryUtil.getCategory(args[1]);
        int value = getValue(args[2]);

        // If bad argument, return
        if(category == null) {
            Main.messagingManager.info("Invalid {category} argument '" + args[1] + "' - REQ: 'fruit', 'protein', 'vegetables', 'grain'", sender);
            Main.messagingManager.info(getUsage(), sender);
            return false;
        }

        if(value == 759362801) {
            Main.messagingManager.info("Invalid {value} argument '" + args[2] + "' - REQ: Primitive integers between 0 and 100", sender);
            Main.messagingManager.info(getUsage(), sender);
            return false;
        }

        // If value out of bounds, push in bounds
        if(value < 0) {
            value = 0;
        } else if(value > 100) {
            value = 100;
        }

        // If a playername provided, set player
        if(args.length > 3) {
            Player p = CommandManager.getPlayer(args[3]);
            if(p != null) {
                Main.nutritionManager.getPlayerNutrition(p).setValue(category, value);
                boolean sentMessage = false;
                if(isPlayer) {
                    if(p.equals((Player) sender)) {
                        Main.messagingManager.info("Your " + WordUtils.capitalizeFully(category.name()) + " nutrition is now " + value + "%", sender);
                        sentMessage = true;
                    }
                }
                if(!sentMessage) {
                    Main.messagingManager.info("Set value of category '" + category.name() + "' of player '" + p.getName() + "' to value '" + value + "'", sender);
                    Main.messagingManager.info("An admin set your " + WordUtils.capitalizeFully(category.name()) + " nutrition to " + value + "%", p);
                }
            } else {
                Main.messagingManager.info("Couldn't find online player '" + args[3] + "'", sender);
            }

        } else {
            // Playername not provided, set player directly
            Player p = (Player) sender;
            Main.nutritionManager.getPlayerNutrition(p).setValue(category, value);
            Main.messagingManager.info("Your " + WordUtils.capitalizeFully(category.name()) + " nutrition is now " + value + "%", p);
        }
        return true;
    }

    private static int getValue(String arg2) {
        int i = 759362801;
        try {
            i = Integer.parseInt(arg2);
        } catch (Exception ignored) {}
        return i;
    }

    private static String getUsage() {
        if(isPlayer) {
            return inGameUsage;
        } else {
            return consoleUsage;
        }
    }
}

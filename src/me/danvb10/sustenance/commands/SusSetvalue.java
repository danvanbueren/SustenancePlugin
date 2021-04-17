package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SusSetvalue {
    public static void goCode(CommandSender sender, Command command, String label, String[] args) {

        Boolean userInputError = false;

        if(sender instanceof Player) {
            if(args.length < 3) {
                // usage (permissions player)
                Main.messagingManager.info("Usage: /sus setvalue {category} {value} [player]", sender);
            } else if(args.length == 3) {
                // setvalue to self (permissions)
                Player p = (Player) sender;
                Category category;
                int value;

                switch(args[1].toLowerCase()) {
                    case "fruit":
                        category = Category.FRUIT;
                        break;
                    case "protein":
                        category = Category.PROTEIN;
                        break;
                    case "vegetables":
                        category = Category.VEGETABLE;
                        break;
                    case "grain":
                        category = Category.GRAIN;
                        break;
                    default:
                        userInputError = true;
                        Main.messagingManager.info("Invalid {category} argument '" + args[1] + "': \n(ALLOWED: 'fruit', 'protein', 'vegetables', 'grain')", sender);
                        Main.messagingManager.info("Usage: /sus setvalue {category} {value} [player]", sender);
                        break;
                }

                //if(Integer.parseInt(args[2]) >= 0 && Integer.parseInt(args[2]) <= 100) {
                 //   sender.sendMessage("well done, numbers.");
                //}


                // Main.nutritionManager.getPlayerNutrition(p).setValue(category, value);
            } else if(args.length > 3) {
                // setvalue to others (with permissions)
            }
        } else {
            if(args.length < 4) {
                // usage (console)
                Main.messagingManager.info("Console usage: /sus setvalue {category} {value} {player}", sender);
            } else if(args.length >= 4) {
                // setvalue to others
            }
        }
    }
}

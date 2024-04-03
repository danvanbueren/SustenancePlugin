package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.messaging.MessageTemplates;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sus implements CommandExecutor {

    // Handle variables outside of the logic
    private final String menuPre = " \n"
            + ChatColor.AQUA  + MessageTemplates.chatBar("Sustenance") + "\n"
            + ChatColor.WHITE + "Status: " + ChatColor.GRAY;
    private final String menuPost = "\n"
            + ChatColor.WHITE + "Commands: " + ChatColor.GRAY + "/sus help\n"
            + ChatColor.AQUA  + MessageTemplates.chatBar("Sustenance") + "\n ";

    // Handle running any command registered to the plugin
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Status section
        String menuStatus;
        if(sender instanceof Player) {
            Player p = (Player) sender;
            menuStatus = Sustenance.nutritionManager.getPlayerNutrition(p).getFormattedNutrition();
            if(Sustenance.nutritionManager.isConditionallyExempt(p)) {
                menuStatus += ChatColor.RED + "" + ChatColor.BOLD + "\nYOU ARE EXEMPT!";
            }
        } else { menuStatus = "Not available in console!"; }

        String menu = menuPre + menuStatus + menuPost;

        // Check to see if there are arguments
        if (args.length > 0) {

            // If there are arguments, send the process to the applicable class
            switch (args[0].toLowerCase()) {
                case "exempt":
                    SusExempt.goCode(sender, command, label, args);
                    break;
                case "help": case "?":
                    SusHelp.goCode(sender, command, label, args);
                    break;
                case "replenish":
                    SusReplenish.goCode(sender, command, label, args);
                    break;
                case "setvalue":
                    SusSetvalue.goCode(sender, command, label, args);
                    break;
                case "status":
                    SusStatus.goCode(sender, args);
                    break;
                case "statuslist":
                    SusStatuslist.goCode(sender, command, label, args);
                    break;
                case "config":
                    SusConfig.goCode(sender, command, label, args);
                    break;
                default:
                    // Argument exists but doesn't match cases - send menu
                    Sustenance.messagingManager.info("'" + args[0] + "' is not a command!", sender);
                    sender.sendMessage(menu);
                    break;
            }
        } else {
            // There are no arguments - send menu
            sender.sendMessage(menu);
        }

        return true;
    }
}

package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.utilities.messaging.MessageTemplates;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SusHelp {

    private static final String message = " \n"
            + ChatColor.AQUA  + MessageTemplates.chatBar("Sustenance Help") + "\n"
            + ChatColor.WHITE + "Commands:\n"
            + ChatColor.WHITE + "/sustenance, /sus " + ChatColor.GRAY + "<- ROOT COMMAND\n"
            + ChatColor.WHITE + "Arguments:\n"
            + ChatColor.WHITE + "  help, ? " + ChatColor.GRAY + "<- INSTRUCTIONS & USAGE\n"
            + ChatColor.WHITE + "  status [player] " + ChatColor.GRAY + "<- SHOW STATUS BOARD/GET PLAYER STATUS\n"
            + ChatColor.WHITE + "  statuslist " + ChatColor.GRAY + "<- ALL ONLINE PLAYER STATUS\n"
            + ChatColor.WHITE + "  setvalue {category} {value} [player] " + ChatColor.GRAY + "<- SET CATEGORY\n"
            + ChatColor.WHITE + "  replenish [player] " + ChatColor.GRAY + "<- FILL NUTRITION\n"
            + ChatColor.WHITE + "  exempt [player] " + ChatColor.GRAY + "<- EXEMPT FROM NUTRITION\n"
            + ChatColor.WHITE + "  config [~] " + ChatColor.GRAY + "<- ~\n"
            + ChatColor.AQUA  + MessageTemplates.chatBar("Sustenance Help") + "\n ";

    public static void goCode(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(message);
    }
}

package me.danvb10.sustenance.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SusHelp {

    private static String message = " \n"
            + ChatColor.AQUA  + "===========SUSTENANCE===========\n"
            + ChatColor.WHITE + "Commands:\n"
            + ChatColor.WHITE + "/sustenance, /sus " + ChatColor.GRAY + "<- PARENT\n"
            + ChatColor.WHITE + "Arguments:\n"
            + ChatColor.WHITE + "  help, ? " + ChatColor.GRAY + "<- INSTRUCTIONS & USAGE\n"
            + ChatColor.WHITE + "  status [player]" + ChatColor.GRAY + "<- PLAYER'S OWN STATUS, ARG PLAYER STATUS\n"
            + ChatColor.WHITE + "  statuslist" + ChatColor.GRAY + "<- ALL PLAYERS' STATUS\n"
            + ChatColor.WHITE + "  setvalue" + ChatColor.GRAY + "<- coming soon...\n"
            + ChatColor.WHITE + "  replenish" + ChatColor.GRAY + "<- coming soon...\n"
            + ChatColor.WHITE + "  exempt" + ChatColor.GRAY + "<- coming soon...\n"
            + ChatColor.AQUA  + "================================\n ";

    public static void goCode(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(message);
    }
}

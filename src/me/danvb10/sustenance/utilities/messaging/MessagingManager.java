package me.danvb10.sustenance.utilities.messaging;

import me.danvb10.sustenance.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessagingManager {

    Main plugin;

    static String prefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "Sustenance" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] ";

    public static String infoPrefix = prefix + ChatColor.RESET;
    public static String warnPrefix = prefix + ChatColor.YELLOW + "" + ChatColor.BOLD + "WARNING: " + ChatColor.RESET;
    public static String errorPrefix = prefix + ChatColor.RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RESET;

    public MessagingManager(Main plugin) {
        this.plugin = plugin;
    }

    public void info(String message, CommandSender sender) {
        sender.sendMessage(infoPrefix + ChatColor.GRAY + message);
    }
    public void warn(String message, CommandSender sender) {
        sender.sendMessage(warnPrefix + ChatColor.YELLOW + message);
    }
    public void error(String message, CommandSender sender) {
        sender.sendMessage(errorPrefix + ChatColor.RED + message);
    }

}

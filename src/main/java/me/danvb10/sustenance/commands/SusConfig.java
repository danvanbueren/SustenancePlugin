package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Sustenance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SusConfig {
    public static void goCode(CommandSender sender, Command command, String label, String[] args) {

        Sustenance.messagingManager.info("The config command is coming soon!", sender);

    }
}

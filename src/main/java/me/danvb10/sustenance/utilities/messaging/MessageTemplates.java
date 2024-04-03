package me.danvb10.sustenance.utilities.messaging;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageTemplates {

    public static String chatBar(String title) {
        title = title.toUpperCase();
        int extraSpace = 53 - title.length();
        if(extraSpace < 0) {
            title = title.substring(0, 52);
        }
        StringBuilder bar = new StringBuilder();
        for(int i = 0; i < ((extraSpace / 2) - 1); i++) {
            bar.append("=");
        }
        StringBuilder s = new StringBuilder(bar + " " + title + " " + bar);
        if(s.length() > 53) {
            s = new StringBuilder(s.substring(0, 52));
        }
        if(s.length() < 53) {
            for(int i = 0; i < 53 - s.length(); i++) {
                s.append("=");
            }
        }
        return ChatColor.GREEN + s.toString() + ChatColor.RESET;
    }

    public static String welcomeMessage(Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append(chatBar("Sustenance") + "\n");
        sb.append("Welcome back, " + p.getName() + "!\n");
        sb.append("Your current nutrition is temporarily displayed.\n");
        sb.append("To pin your nutrition status, use the command:\n");
        sb.append(ChatColor.ITALIC + "/sus status\n");
        sb.append(chatBar("Sustenance"));
        return sb.toString();
    }
}

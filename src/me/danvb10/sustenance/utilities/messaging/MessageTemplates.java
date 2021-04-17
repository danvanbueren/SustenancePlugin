package me.danvb10.sustenance.utilities.messaging;

import me.danvb10.sustenance.Main;
import org.bukkit.entity.Player;

public class MessageTemplates {
    public static String nutritionStatus(Player p) {
        return MessagingManager.infoPrefix + "Your current nutrition is...\n" + Main.nutritionManager.getPlayerNutrition(p).getFormattedNutrition();
    }
}

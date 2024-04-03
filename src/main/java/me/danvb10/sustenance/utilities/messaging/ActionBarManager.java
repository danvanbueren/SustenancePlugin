package me.danvb10.sustenance.utilities.messaging;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.enums.Category;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ActionBarManager {

    Sustenance plugin;

    public ActionBarManager(Sustenance plugin) {
        this.plugin = plugin;
    }

    // Used for multiple listeners
    public void actionBarMessageFoodNutrition(Player player, Material material) {
        if (!material.isEdible())
            return;

        int count = 0;

        StringBuilder message = new StringBuilder(ChatColor.BOLD + "Nutrition: " + ChatColor.RESET);
        for(Category c : Sustenance.simpleConfigGet.getCategoriesFromMaterial(material)) {
            count++;

            int value = Sustenance.simpleConfigGet.getValueFromCategoryAndMaterial(c, material);
            String name = c.name().toUpperCase();

            String sign = "";
            if (value > 0) {
                sign = "+";
            }

            message.append(sign).append(value).append(" ").append(name).append(", ");
        }

        if(count < 1){
            message.append("No nutritional value");
        } else {
            message = new StringBuilder(message.substring(0, message.length() - 2));
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message.toString()));
    }


    public void clear(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }
}

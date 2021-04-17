package me.danvb10.sustenance.utilities.messaging;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;

public class ActionBarManager {

    Main plugin;

    public ActionBarManager(Main plugin) {
        this.plugin = plugin;
    }

    // Used for multiple listeners
    public void actionBarMessageFoodNutrition(Player player, Material material) {
        if(material.isEdible()) {
            String message = ChatColor.BOLD + "Nutrition: " + ChatColor.RESET;
            for(Category c : plugin.simpleConfigGet.getCategoriesFromMaterial(material)) {
                message += "+" + plugin.simpleConfigGet.getValueFromCategoryAndMaterial(c, material) + " " + WordUtils.capitalizeFully(c.name()) + ", ";
            }

            if(message.equals(ChatColor.BOLD + "Nutrition: " + ChatColor.RESET)){
                message += "No nutritional value";
            } else {
                message = message.substring(0, message.length() - 2);
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    public void clear(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }
}

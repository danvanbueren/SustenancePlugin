package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandManager {

    // Load all commands
    // The only time this can be called is during onEnable();
    public CommandManager(Main plugin) {

        // Register top level commands
        // We're gonna handle most switch/case logic inside the primary command class
        Objects.requireNonNull(plugin.getCommand("sustenance")).setExecutor(new Sus());
        Objects.requireNonNull(plugin.getCommand("sus")).setExecutor(new Sus());

    }

    public static Player getPlayer(String name) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }
}

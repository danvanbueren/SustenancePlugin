package me.danvb10.sustenance.commands;

import me.danvb10.sustenance.Main;

public class CommandManager {

    // Load all commands
    // The only time this can be called is during onEnable();
    public CommandManager(Main plugin) {

        // Register top level commands
        // We're gonna handle most switch/case logic inside the primary command class
        plugin.getCommand("sustenance").setExecutor(new Sus());
        plugin.getCommand("sus").setExecutor(new Sus());

    }
}

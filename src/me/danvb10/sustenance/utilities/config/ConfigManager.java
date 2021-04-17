package me.danvb10.sustenance.utilities.config;

import me.danvb10.sustenance.Main;
import org.bukkit.Material;

import java.io.File;

public class ConfigManager {

    Main plugin;

    // Runs with onEnable()
    public ConfigManager(Main plugin) {
        this.plugin = plugin;

        // Get config from disk
        readWrite();

        // Had to check the config before applying this setting
        plugin.logger.refreshVerbose();

        // Tell user if they messed up the config
        verifyCategories();

    }

    // Runs with onDisable()
    public void disable() {
        readWrite();
        plugin.logger.verbose("onDisable() -> ConfigManager.disable() -> readWrite()");
    }

    // Logic to read and write config
    public void readWrite() {

        // Read from file
        plugin.reloadConfig();

        // Check if file exists
        if(new File("plugins/Sustenance/config.yml").isFile()) {

            // Announce found config
            plugin.logger.verbose("Found config file");

        } else {
            // Announce file not found
            plugin.logger.verbose("Couldn't find config file, creating a template");
            // Copy default config before saving
            plugin.getConfig().options().copyDefaults(true);
        }

        // Make sure the necessary keys are in config
        checkRequiredConfigKeys();

        // Save config to file
        plugin.saveConfig();
    }

    public void checkRequiredConfigKeys() {
        if(!plugin.getConfig().isSet("settings")) {
            plugin.logger.verbose("settings key is not set in config, adding");
            plugin.getConfig().set("settings", "");
        }
        if(!plugin.getConfig().isSet("settings.decay-multiplier")) {
            plugin.logger.verbose("settings.decay-multiplier key is not set in config, adding");
            plugin.getConfig().set("settings.decay-multiplier", 5);
        }
        if(!plugin.getConfig().isSet("settings.verbose-logging")) {
            plugin.logger.verbose("settings.verbose-logging key is not set in config, adding");
            plugin.getConfig().set("settings.verbose-logging", false);
        }
        if(!plugin.getConfig().isSet("categories")) {
            plugin.logger.verbose("categories key is not set in config, adding");
            plugin.getConfig().set("categories", "");
        }
        if(!plugin.getConfig().isSet("effects")) {
            plugin.logger.verbose("effects key is not set in config, adding");
            plugin.getConfig().set("effects", "");
        }
    }

    public void verifyCategories() {
        // Loop through 'categories'
        for (String category : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
            plugin.logger.verbose("LOOKUP FOR CATEGORY :: " + category);

            // Loop through Strings in current category
            for (String unverifiedMaterial : plugin.getConfig().getConfigurationSection("categories." + category).getKeys(false)) {

                // Safeguard against an invalid value
                int verifiedValue = -1;

                // Get the value from the config
                Object unverifiedValue = plugin.getConfig().get("categories." + category + "." + unverifiedMaterial);

                // Check if value from config is an Integer
                if (unverifiedValue instanceof Integer) {
                    // Set to verified value
                    verifiedValue = (int) unverifiedValue;
                } else {
                    // Warn the user that the value is not an Integer
                    plugin.logger.warn("CONFIG ERROR! The value 'categories." + category + "." + unverifiedMaterial + "." + unverifiedValue + "' cannot be cast to an Integer - IGNORING!");
                }

                // Check to see if the value is actually an Integer
                if(verifiedValue != -1) {
                    plugin.logger.verbose("Attempting to match '" + unverifiedMaterial + "' to a Material...");

                    // Attempt to match the object to an actual Material
                    Material verifiedMaterial = Material.matchMaterial(unverifiedMaterial);

                    // Check if object was matched to Material or not
                    if (verifiedMaterial == null) {
                        // Warn user object couldn't be matched to Material
                        plugin.logger.warn("CONFIG ERROR! The key 'categories." + category + "." + unverifiedMaterial + "' cannot be matched to a Material - IGNORING!");
                    } else {
                        // Successfully matched object to material
                        plugin.logger.verbose("matched item '" + unverifiedMaterial + "' to " + verifiedMaterial + "!");

                        // Check if material is edible
                        if(verifiedMaterial.isEdible()){

                            // Present value
                            plugin.logger.verbose("Material is edible! Value: " + verifiedValue);
                        } else {

                            // Warn user that material is not edible
                            plugin.logger.warn("CONFIG ERROR! " + verifiedMaterial + " is not edible - IGNORING!");
                        }
                    }
                }
            }
        }
    }
}
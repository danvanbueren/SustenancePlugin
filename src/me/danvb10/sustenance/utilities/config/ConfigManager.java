package me.danvb10.sustenance.utilities.config;

import me.danvb10.sustenance.Sustenance;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;

public class ConfigManager {

    Sustenance plugin;

    // Runs with onEnable()
    public ConfigManager(Sustenance plugin) {

        this.plugin = plugin;

        // Create default config if none exists
        plugin.saveDefaultConfig();

        // Make sure the necessary keys are in config
        checkRequiredConfigKeys();

        // Had to check the config before applying this setting
        Sustenance.logger.refreshVerbose();

        // Tell user if they messed up the config
        verifyCategories();

    }

    public void checkRequiredConfigKeys() {
        if(!plugin.getConfig().isSet("settings")) {
            Sustenance.logger.verbose("ConfigManager:checkRequiredConfigKeys(): " + "settings key is not set in config, adding");
            plugin.getConfig().set("settings", "");
        }
        if(!plugin.getConfig().isSet("settings.decay-multiplier")) {
            Sustenance.logger.verbose("ConfigManager:checkRequiredConfigKeys(): " + "settings.decay-multiplier key is not set in config, adding");
            plugin.getConfig().set("settings.decay-multiplier", 5);
        }
        if(!plugin.getConfig().isSet("settings.verbose-logging")) {
            Sustenance.logger.verbose("ConfigManager:checkRequiredConfigKeys(): " + "settings.verbose-logging key is not set in config, adding");
            plugin.getConfig().set("settings.verbose-logging", false);
        }
        if(!plugin.getConfig().isSet("categories")) {
            Sustenance.logger.verbose("ConfigManager:checkRequiredConfigKeys(): " + "categories key is not set in config, adding");
            plugin.getConfig().set("categories", "");
        }
        if(!plugin.getConfig().isSet("effects")) {
            Sustenance.logger.verbose("ConfigManager:checkRequiredConfigKeys(): " + "effects key is not set in config, adding");
            plugin.getConfig().set("effects", "");
        }
    }

    public void overwriteWithDefaultConfig() {

        File configFile = new File(plugin.getDataFolder() + "\\config.yml");

        if (configFile.delete()) {
            System.out.println("Deleted old config: " + configFile.getName());
        } else {
            System.out.println("Failed to delete old config.");
        }

        plugin.saveDefaultConfig();
        System.out.println("Saved default config.");
    }

    public int getDecayMultiplier() {
        Object unverifiedValue = plugin.getConfig().get("settings.decay-multiplier");

        if (unverifiedValue instanceof Integer) {
            return (int) unverifiedValue;
        }

        Sustenance.logger.verbose("ConfigManager:getDecayMultiplier(): " + "Failed to fetch settings.decay-multiplier from config.yml - ConfigManager:getDecayMultiplier() - Fallback on default value: 3");
        return 3;
    }

    public void verifyCategories() {
        // Check if config section is null
        if (Objects.isNull(plugin.getConfig().getConfigurationSection("categories"))) {
            Sustenance.logger.error("ConfigManager:verifyCategories(): " + "Malformed config.yml - Replaced with default");
            overwriteWithDefaultConfig();
        }

        // Loop through 'categories'
        for (String category : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
            Sustenance.logger.verbose("ConfigManager:verifyCategories(): " + "LOOKUP FOR CATEGORY: " + category);

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
                    Sustenance.logger.warn("CONFIG ERROR! The value 'categories." + category + "." + unverifiedMaterial + "." + unverifiedValue + "' cannot be cast to an Integer - IGNORING!");
                }

                // Check to see if the value is actually an Integer
                if(verifiedValue != -1) {
                    Sustenance.logger.verbose("ConfigManager:verifyCategories(): " + "Attempting to match '" + unverifiedMaterial + "' to a Material...");

                    // Attempt to match the object to an actual Material
                    Material verifiedMaterial = Material.matchMaterial(unverifiedMaterial);

                    // Check if object was matched to Material or not
                    if (verifiedMaterial == null) {
                        // Warn user object couldn't be matched to Material
                        Sustenance.logger.warn("CONFIG ERROR! The key 'categories." + category + "." + unverifiedMaterial + "' cannot be matched to a Material - IGNORING!");
                    } else {
                        // Successfully matched object to material
                        Sustenance.logger.verbose("ConfigManager:verifyCategories(): " + "matched item '" + unverifiedMaterial + "' to " + verifiedMaterial + "!");

                        // Check if material is edible
                        if(verifiedMaterial.isEdible()){

                            // Present value
                            Sustenance.logger.verbose("ConfigManager:verifyCategories(): " + "Material is edible! Value: " + verifiedValue);
                        } else {

                            // Warn user that material is not edible
                            Sustenance.logger.warn("CONFIG ERROR! " + verifiedMaterial + " is not edible - IGNORING!");
                        }
                    }
                }
            }
        }
    }
}
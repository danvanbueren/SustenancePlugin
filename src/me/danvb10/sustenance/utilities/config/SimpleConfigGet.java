package me.danvb10.sustenance.utilities.config;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import org.bukkit.Material;

import java.util.ArrayList;

public class SimpleConfigGet {

    Main plugin;

    public SimpleConfigGet(Main plugin) {
        this.plugin = plugin;
    }

    public ArrayList<Category> getCategoriesFromMaterial(Material m) {

        ArrayList<Category> returnCats = new ArrayList<Category>();

        for (String category : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
            for (String unverifiedMaterial : plugin.getConfig().getConfigurationSection("categories." + category).getKeys(false)) {
                Object unverifiedValue = plugin.getConfig().get("categories." + category + "." + unverifiedMaterial);
                if(unverifiedValue instanceof Integer) {
                    Material material = Material.matchMaterial(unverifiedMaterial);
                    if (material == m && material.isEdible()) {
                        int value = (int) unverifiedValue;
                        switch(category.toLowerCase()) {
                            case "grain":
                                returnCats.add(Category.GRAIN);
                                break;
                            case "protein":
                                returnCats.add(Category.PROTEIN);
                                break;
                            case "vegetables":
                                returnCats.add(Category.VEGETABLE);
                                break;
                            case "fruit":
                                returnCats.add(Category.FRUIT);
                                break;
                        }
                    }
                }
            }
        }
        return returnCats;
    }

    public int getValueFromCategoryAndMaterial(Category passedActualCategory, Material passedActualMaterial) {
        // Use switch{case} to set String name of enum
        String categoryString = null;
        switch(passedActualCategory) {
            case GRAIN:
                categoryString = "grain";
                break;
            case PROTEIN:
                categoryString = "protein";
                break;
            case VEGETABLE:
                categoryString = "vegetables";
                break;
            case FRUIT:
                categoryString = "fruit";
                break;
        }

        // Check if switch{case} was successful
        if(categoryString != null) {
            // Loop through every category in the config
            for (String categoryInLoop : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
                // If the current category equals the category we are looking for, continue
                if(categoryInLoop.equalsIgnoreCase(categoryString)) {
                    // Loop through every material in the current category
                    for (String materialStringInLoop : plugin.getConfig().getConfigurationSection("categories." + categoryInLoop).getKeys(false)) {
                        // If the current material equals the material we are looking for, continue
                        if(Material.matchMaterial(materialStringInLoop).equals(passedActualMaterial)) {
                            // Due to error handling, we will get the key value as an object and store it here for ease of use
                            Object keyValueObj = plugin.getConfig().get("categories." + categoryInLoop + "." + materialStringInLoop);
                            // Check if the object is actually an integer
                            if (keyValueObj instanceof Integer) {
                                // Match the original string to a material for ease of use
                                Material material = Material.matchMaterial(materialStringInLoop);
                                // Check that the material found is what was passed originally, then check if the material is a food item
                                if (material == passedActualMaterial && material.isEdible()) {
                                    // Return the value as an integer, since we confirmed that it is an integer
                                    return (int) keyValueObj;
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
}
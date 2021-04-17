package me.danvb10.sustenance.utilities.playerdata;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class PlayerNutrition implements Serializable {

    private static final long serialVersionUID = 1L;
    transient Player player;
    UUID playerUuid;
    int vegetables = 100, protein = 100, grain = 100, fruit = 100;

    @Override
    public String toString() {
        return "serialVersionUID: " + serialVersionUID + "; Player: " + player.getName() + "; UUID: " + playerUuid + "; vegetables: " + vegetables + "; protein: " + protein + "; grain: " + grain + "; fruit: " + fruit;
    }

    // CONSTRUCTOR - REALLY SHOULDN'T BE USING THIS EVER OUTSIDE OF DEV PURPOSES
    public PlayerNutrition(Player player) {
        this.player = player;
        playerUuid = player.getUniqueId();
        limitCategoryValues();
    }

    // CONSTRUCTOR - USE THIS ONE TO CREATE NEW PNs
    public PlayerNutrition(Player player, int vegetables, int protein, int grain, int fruit) {
        this.player = player;
        playerUuid = player.getUniqueId();
        this.vegetables = vegetables;
        this.protein = protein;
        this.grain = grain;
        this.fruit = fruit;
        limitCategoryValues();
    }

    // Getters
    public PlayerNutrition getThis() {
        return this;
    }

    public int getValue(Category category) {
        switch(category) {
            case GRAIN:
                return grain;
            case VEGETABLE:
                return vegetables;
            case PROTEIN:
                return protein;
            case FRUIT:
                return fruit;
            default:
                return 0;
        }
    }

    // Setters & Saving
    public void setValue(Category category, int value) {
        switch(category) {
            case GRAIN:
                grain = value;
                break;
            case VEGETABLE:
                vegetables = value;
                break;
            case PROTEIN:
                protein = value;
                break;
            case FRUIT:
                fruit = value;
                break;
            default:
                break;
        }
        limitCategoryValues();
        // save, implement ASAP!
    }

    // Pairs with onNutritionDepletion() - Natural nutrition decay effect
    public void naturalReduceStats() {
        Double randomValue = new Random().nextDouble();

        int localizedHardness = (20 - player.getFoodLevel()) / 2;
        grain -= localizedHardness;
        fruit -= localizedHardness;
        vegetables -= localizedHardness;
        protein -= localizedHardness;

        // Some crappy logic to guide depletion rate
        if(player.getFoodLevel() <= 19) {
            if (randomValue > 0.7 || player.getFoodLevel() < 5) {
                grain -= 1; // * hardness_multiplier;
                fruit -= localizedHardness;
            }
            if ((randomValue < 0.8 && randomValue > 0.4) || player.getFoodLevel() < 5) {
                fruit -= 1; // * hardness_multiplier;
                protein -= localizedHardness;
            }
            if ((randomValue < 0.5 && randomValue > 0.3) || player.getFoodLevel() < 5) {
                protein -= 1; // * hardness_multiplier;
                vegetables -= localizedHardness;
            }
            if ((randomValue < 0.4) || player.getFoodLevel() < 5) {
                vegetables -= 1; // * hardness_multiplier;
                grain -= localizedHardness;
            }
        }
        checkIfNeedWarned();
        limitCategoryValues();
    }

    private void checkIfNeedWarned() {
        if(grain < 25 || fruit < 25 || vegetables < 25 || protein < 25)
            Main.messagingManager.warn("You're nutrition is falling dangerously low!", (CommandSender) player);
    }

    // Pairs with onDeath() - Resets stats
    public void setFullStats() {
        vegetables = 100;
        protein = 100;
        fruit = 100;
        grain = 100;
        limitCategoryValues();
    }

    private void limitCategoryValues() {
        if(vegetables > 100)
            vegetables = 100;
        if(protein > 100)
            protein = 100;
        if(fruit > 100)
            fruit = 100;
        if(grain > 100)
            grain = 100;
    }

    // Returns prettified nutrition info
    public String getFormattedNutrition() {
        return "Vegetables: " + vegetables + "%, Protein: " + protein + "%, Grain: " + grain + "%, Fruit: " + fruit + "%";
    }
}

package me.danvb10.sustenance.utilities.playerdata;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.enums.Category;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class PlayerNutrition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    transient Player player;

    UUID playerUuid;

    int vegetables = 75, protein = 75, grain = 75, fruit = 75;

    private boolean isExempt = false;

    transient ArrayList<PotionEffectType> activeEffects = new ArrayList<>();

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
        return switch (category) {
            case GRAIN -> grain;
            case VEGETABLE -> vegetables;
            case PROTEIN -> protein;
            case FRUIT -> fruit;
        };
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
        updatePlayerEffects();
        limitCategoryValues();
    }

    // Pairs with onNutritionDepletion() - Natural nutrition decay effect
    public void naturalReduceStats() {
        double randomValue = new Random().nextDouble();

        int localizedHardness = (((20 - player.getFoodLevel()) / 2) * Sustenance.configManager.getDecayMultiplier()) / 3;

        if(grain > 70) {
            grain -= (int) (localizedHardness * 1.2);
        } else if (grain < 30) {
            grain -= (int) (localizedHardness * 0.8);
        } else {
            grain -= localizedHardness;
        }

        if(fruit > 70) {
            fruit -= (int) (localizedHardness * 1.2);
        } else if (fruit < 30) {
            fruit -= (int) (localizedHardness * 0.8);
        } else {
            fruit -= localizedHardness;
        }

        if(vegetables > 70) {
            vegetables -= (int) (localizedHardness * 1.2);
        } else if (vegetables < 30) {
            vegetables -= (int) (localizedHardness * 0.8);
        } else {
            vegetables -= localizedHardness;
        }

        if(protein > 70) {
            protein -= (int) (localizedHardness * 1.2);
        } else if (protein < 30) {
            protein -= (int) (localizedHardness * 0.8);
        } else {
            protein -= localizedHardness;
        }

        // extreme punishment
        if(player.getFoodLevel() <= 5) {
            if (randomValue > 0.7 || player.getFoodLevel() < 5) {
                grain -= 2; // * hardness_multiplier;
                fruit -= localizedHardness;
            }
            if ((randomValue < 0.8 && randomValue > 0.4) || player.getFoodLevel() < 5) {
                fruit -= 2; // * hardness_multiplier;
                protein -= localizedHardness;
            }
            if ((randomValue < 0.5 && randomValue > 0.3) || player.getFoodLevel() < 5) {
                protein -= 2; // * hardness_multiplier;
                vegetables -= localizedHardness;
            }
            if ((randomValue < 0.4) || player.getFoodLevel() < 5) {
                vegetables -= 2; // * hardness_multiplier;
                grain -= localizedHardness;
            }
        }
        updatePlayerEffects();
        limitCategoryValues();
    }

    public void clearActiveEffects() {
        if (this.activeEffects == null)
            this.activeEffects = new ArrayList<>();

        if (this.activeEffects.isEmpty())
            return;

        for(PotionEffectType effectType : this.activeEffects){
            Sustenance.logger.verbose("PlayerNutrition:clearActiveEffects(): " + "Cleared effect " + effectType + " from " + player.getName());
            this.player.removePotionEffect(effectType);
            this.player.addPotionEffect(new PotionEffect(effectType, 0, 0));
        }

        this.activeEffects = new ArrayList<>();
    }

    public void updatePlayerEffects() {

        clearActiveEffects();

        int averageLevel = (grain + protein + fruit + vegetables) / 4;

        if (averageLevel > 90 && grain > 75 && protein > 75 && fruit > 75 && vegetables > 75) {
            // apply major buffs
            applyNewEffects("major-buffs");
            Sustenance.messagingManager.warn("You are feeling very strong due to great nutrition!", player);
            return;
        }

        if (averageLevel > 70 && grain > 50 && protein > 50 && fruit > 50 && vegetables > 50) {
            // apply minor buffs
            applyNewEffects("minor-buffs");
            Sustenance.messagingManager.warn("You are starting to feel strong due to good nutrition!", player);
            return;
        }

        if (averageLevel < 10 || grain == 0 || protein == 0 || fruit == 0 || vegetables == 0) {
            // apply major debuffs
            applyNewEffects("major-debuffs");
            Sustenance.messagingManager.warn("You are feeling very ill due to a lack of nutrition!", player);
            return;
        }

        if (averageLevel < 30 || grain < 10 || protein < 10 || fruit < 10 || vegetables < 10) {
            // apply minor debuffs
            applyNewEffects("minor-debuffs");
            Sustenance.messagingManager.warn("You are starting to feel ill due to a lack of nutrition!", player);
        }
    }

    private void applyNewEffects(String effectCategory) {
        ArrayList<PotionEffect> effects = Sustenance.simpleConfigGet.getEffects(effectCategory);

        if (isExempt)
            return;

        if (effects == null || effects.isEmpty())
            return;

        for (PotionEffect effect : effects) {
            this.player.addPotionEffect(effect);
            this.activeEffects.add(effect.getType());
        }
    }

    // Pairs with onDeath() - Resets stats
    public void setFullStats() {
        vegetables = 100;
        protein = 100;
        fruit = 100;
        grain = 100;

        updatePlayerEffects();
        limitCategoryValues();
    }

    public void setRespawnStats() {
        vegetables = 75;
        protein = 75;
        fruit = 75;
        grain = 75;

        updatePlayerEffects();
        limitCategoryValues();
    }

    private void limitCategoryValues() {
        if(vegetables > 100)
            vegetables = 100;
        if(vegetables < 0)
            vegetables = 0;
        if(protein > 100)
            protein = 100;
        if(protein < 0)
            protein = 0;
        if(fruit > 100)
            fruit = 100;
        if(fruit < 0)
            fruit = 0;
        if(grain > 100)
            grain = 100;
        if(grain < 0)
            grain = 0;
    }

    // Returns prettified nutrition info
    public String getFormattedNutrition() {
        String result = "";
        if(isExempt) {
            result += ChatColor.RED + "" + ChatColor.BOLD + "HARD EXEMPT! " + ChatColor.GRAY;
        }
        result += "Vegetables: " + vegetables + "%, Protein: " + protein + "%, Grain: " + grain + "%, Fruit: " + fruit + "%";
        return result;
    }

    // GETTERS/SETTERS
    public boolean isExempt() {
        return isExempt;
    }

    public void setExempt(boolean exempt) {
        Sustenance.logger.verbose("PlayerNutrition:setExempt(): " + player.getName() + " - PlayerNutrition: setExempt set to " + exempt);
        isExempt = exempt;
    }
}

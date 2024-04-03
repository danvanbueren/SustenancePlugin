package me.danvb10.sustenance.utilities.enums;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectTypeUtil {

    /*
     * Reference https://minecraft.fandom.com/wiki/Effect#Effect_list for effect list containing names
     */
    public static PotionEffectType getTypeFromNormalName(String name) {
        switch(name.toLowerCase()) {
            case "absorption":
                return PotionEffectType.ABSORPTION;
            case "bad_omen":
                return PotionEffectType.BAD_OMEN;
            case "blindness":
                return PotionEffectType.BLINDNESS;
            case "conduit_power":
                return PotionEffectType.CONDUIT_POWER;
            case "darkness":
                return PotionEffectType.DARKNESS;
            case "dolphins_grace":
                return PotionEffectType.DOLPHINS_GRACE;
            case "fire_resistance":
                return PotionEffectType.FIRE_RESISTANCE;
            case "glowing":
                return PotionEffectType.GLOWING;
            case "haste":
                return PotionEffectType.FAST_DIGGING;
            case "health_boost":
                return PotionEffectType.HEALTH_BOOST;
            case "hero_of_the_village":
                return PotionEffectType.HERO_OF_THE_VILLAGE;
            case "hunger":
                return PotionEffectType.HUNGER;
            case "invisibility":
                return PotionEffectType.INVISIBILITY;
            case "jump_boost":
                return PotionEffectType.JUMP;
            case "levitation":
                return PotionEffectType.LEVITATION;
            case "luck":
                return PotionEffectType.LUCK;
            case "mining_fatigue":
                return PotionEffectType.SLOW_DIGGING;
            case "nausea":
                return PotionEffectType.CONFUSION;
            case "night_vision":
                return PotionEffectType.NIGHT_VISION;
            case "poison":
                return PotionEffectType.POISON;
            case "regeneration":
                return PotionEffectType.REGENERATION;
            case "resistance":
                return PotionEffectType.DAMAGE_RESISTANCE;
            case "saturation":
                return PotionEffectType.SATURATION;
            case "slow_falling":
                return PotionEffectType.SLOW_FALLING;
            case "slowness":
                return PotionEffectType.SLOW;
            case "speed":
                return PotionEffectType.SPEED;
            case "strength":
                return PotionEffectType.INCREASE_DAMAGE;
            case "unluck":
                return PotionEffectType.UNLUCK;
            case "water_breathing":
                return PotionEffectType.WATER_BREATHING;
            case "weakness":
                return PotionEffectType.WEAKNESS;
            case "wither":
                return PotionEffectType.WITHER;
            default:
                return null;
        }
    }
}

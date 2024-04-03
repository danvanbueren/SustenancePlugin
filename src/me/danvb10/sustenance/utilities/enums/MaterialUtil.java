package me.danvb10.sustenance.utilities.enums;

import org.bukkit.Material;

public class MaterialUtil {

    public MaterialUtil() {

    }

    public static boolean isConsumable(Material material) {

        switch(material) {
            case MILK_BUCKET:
            case WATER:
            case POTION:
            case HONEY_BOTTLE:
                return true;
        }

        return false;
    }
}

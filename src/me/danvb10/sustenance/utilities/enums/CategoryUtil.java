package me.danvb10.sustenance.utilities.enums;

public class CategoryUtil {

    public static Category getCategory(String arg1) {
        switch(arg1.toLowerCase()) {
            case "fruit":
                return Category.FRUIT;
            case "protein":
                return Category.PROTEIN;
            case "vegetables":
                return Category.VEGETABLE;
            case "grain":
                return Category.GRAIN;
        }
        return null;
    }
}

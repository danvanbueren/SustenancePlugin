package me.danvb10.sustenance.utilities.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;

public class PlayerNutritionFileManager {

    public static void writer(String directory, PlayerNutrition playerNutrition) {

        File dir = new File(directory);
        if (!dir.exists()){ dir.mkdirs(); }
        File datafile = new File(directory + playerNutrition.playerUuid.toString());

        try {
            if(datafile.isFile()) {
                datafile.delete();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(datafile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(playerNutrition);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Couldn't write userdata: " + e.toString());
            e.printStackTrace();
        }
    }

    public static boolean isReadable(String directory, String playerUuid) {
        File dir = new File(directory);
        if (!dir.exists()){ dir.mkdirs(); }
        File datafile = new File(directory + playerUuid);

        if(datafile.isFile()) {
            return true;
        }
        return false;
    }

    public static PlayerNutrition reader(String directory, String playerUuid) {

        File dir = new File(directory);
        if (!dir.exists()){ dir.mkdirs(); }
        File datafile = new File(directory + playerUuid);

        try {
            FileInputStream fi = new FileInputStream(datafile);
            ObjectInputStream oi = new ObjectInputStream(fi);

            PlayerNutrition pn = (PlayerNutrition) oi.readObject();

            // Attempting to latch player
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getUniqueId().toString().equals(playerUuid)) {
                    // latched player
                    pn.player = p;
                }
            }
            System.out.println(pn);

            oi.close();
            fi.close();

            return pn;

        } catch (Exception e) {
            Bukkit.getLogger().warning("Couldn't read userdata");
            e.printStackTrace();
        }
        return null;
    }

}

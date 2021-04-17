package me.danvb10.sustenance.listeners;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.messaging.MessageTemplates;
import me.danvb10.sustenance.utilities.messaging.MessagingManager;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    Main plugin;
    public Listeners(Main plugin) {
        this.plugin = plugin;
    }

    /*
     * WHEN PLAYER JOINS, ADD PLAYER TO MANAGER OBJECT, MESSAGE CURRENT STATUS - CHANGE TO BOARD
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Main.nutritionManager.addPlayer(p);
        plugin.logger.verbose("Changed(addPlayer) nutritionManager: " + Main.nutritionManager.toString());
        plugin.scoreboardManager.showBoardDefiniteTime(p, 5);
    }
    /*
     * WHEN PLAYER QUITS, REMOVE PLAYER FROM MANAGER OBJECT
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.nutritionManager.removeSavePlayer(p);
        plugin.logger.verbose("Changed(removePlayer) nutritionManager: " + Main.nutritionManager.toString());
    }
    /*
     * WHEN PLAYER RESPAWNS, RESET STATS AND SEND MESSAGE - CHANGE TO BOARD
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Main.nutritionManager.getPlayerNutrition(p).setFullStats();
        plugin.scoreboardManager.showBoardDefiniteTime(p, 5);
    }
    /*
     * WHEN ANY INVENTORY IS CLOSED, CLEAR ACTION BAR MESSAGE FOR TIDINESS
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        plugin.actionBarManager.clear(p);
    }
    /*
     * WHEN PLAYER NUTRITION FALLS, NATURAL REDUCE STATS
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if(entity instanceof Player) {
            Player p = (Player) entity;
            Main.nutritionManager.getPlayerNutrition(p).naturalReduceStats();
        }
    }
    /*
     * WHEN PLAYER EQUIPS NEW ITEM, SEND ACTION BAR MESSAGE OF NUTRITIONAL VALUE
     */
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack is = p.getInventory().getItem(e.getNewSlot());
        if(is != null) {
            if(is.getType().isEdible()) {
                plugin.actionBarManager.actionBarMessageFoodNutrition(p, is.getType());
            } else {
                plugin.actionBarManager.clear(p);
            }
        } else {
            plugin.actionBarManager.clear(p);
        }
    }
    /*
     *
     */
    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack mainHandItemStack = p.getInventory().getItemInMainHand();
        Material mainHandMaterial = mainHandItemStack.getType();
        ItemStack offHandItemStack = p.getInventory().getItemInOffHand();
        Material offHandMaterial = offHandItemStack.getType();
        Boolean usedMainHand = false, usedOffHand = false;

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(mainHandItemStack != null) {
                if(mainHandMaterial.isEdible()){
                    plugin.actionBarManager.actionBarMessageFoodNutrition(p, mainHandMaterial);
                    usedMainHand = true;
                }
            }
            if(!usedMainHand && offHandItemStack != null) {
                if(mainHandMaterial.isEdible()) {
                    plugin.actionBarManager.actionBarMessageFoodNutrition(p, offHandMaterial);
                    usedOffHand = true;
                }
            }
            if(!usedMainHand && !usedOffHand) {
                plugin.actionBarManager.clear(p);
            }
        }
    }
    /*
     * HERE
     */
    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        Material mainHand = e.getMainHandItem().getType();
        Material offHand = e.getOffHandItem().getType();
        Boolean usedMainHand = false, usedOffHand = false;
        if(e.getMainHandItem() != null) {
            if(e.getMainHandItem().getType().isEdible()){
                Material m = e.getMainHandItem().getType();
                plugin.actionBarManager.actionBarMessageFoodNutrition(p, m);
                usedMainHand = true;
            }
        }
        if(!usedMainHand && e.getOffHandItem() != null) {
            Material m = e.getOffHandItem().getType();
            if(m.isEdible()) {
                plugin.actionBarManager.actionBarMessageFoodNutrition(p, m);
                usedOffHand = true;
            }
        }
        if(!usedMainHand && !usedOffHand) {
            plugin.actionBarManager.clear(p);
        }
    }
    /*
     * HERE
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        int[] slotsToCheck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 40};
        int slotClicked = e.getSlot();
        if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            InventoryHolder ih = e.getClickedInventory().getHolder();
            if (ih instanceof Player) {
                Player p = (Player) ih;
                for (int i : slotsToCheck) {
                    if(slotClicked == i) {
                        if(p.getInventory().getItem(i) != null) {
                            Material m = p.getInventory().getItem(i).getType();
                            plugin.actionBarManager.actionBarMessageFoodNutrition(p, m);
                        } else {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (slotClicked == i && p.getInventory().getItem(i) != null) {
                                            Material m = p.getInventory().getItem(i).getType();
                                            plugin.actionBarManager.actionBarMessageFoodNutrition(p, m);
                                        }
                                    } catch(Exception e) {
                                        plugin.logger.error("Runnable for onInvClick() event encountered an error!\n" + e.getStackTrace());
                                    }
                                }
                            }, 1L);
                        }
                    }
                }
            }
        }
    }
    /*
     * HERE
     */
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        // if a player consumes any food, show the board AND send a message saying what changed!
        Player p = e.getPlayer();
        PlayerNutrition pn = plugin.nutritionManager.getPlayerNutrition(p);
        ItemStack item = e.getItem();
        Material material = item.getType();

        if(item.getType().isEdible()) {
            // Message player what they ate and what the current values are
            for(Category category : plugin.simpleConfigGet.getCategoriesFromMaterial(material)) {
                int nutritionValue = plugin.simpleConfigGet.getValueFromCategoryAndMaterial(category, material);
                int currentValue = pn.getValue(category);
                pn.setValue(category, currentValue + nutritionValue);
                int newValue = pn.getValue(category);
                int delta = newValue - currentValue;

                p.sendMessage(MessagingManager.infoPrefix + WordUtils.capitalizeFully(category.name()) + " +" + delta + "% = " + newValue + "%");
                plugin.scoreboardManager.showBoardDefiniteTime(p, 5);
            }
        }
    }
}

package me.danvb10.sustenance.listeners;

import me.danvb10.sustenance.Main;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.messaging.MessageTemplates;
import me.danvb10.sustenance.utilities.messaging.MessagingManager;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

import java.util.Arrays;
import java.util.Objects;

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
        Main.logger.verbose("Changed(addPlayer) nutritionManager: " + Main.nutritionManager.toString());
        Main.scoreboardManager.showBoardDefiniteTime(p, 10);
        p.sendMessage(MessageTemplates.welcomeMessage(p));
    }
    /*
     * WHEN PLAYER QUITS, REMOVE PLAYER FROM MANAGER OBJECT
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.nutritionManager.removeSavePlayer(p);
        Main.logger.verbose("Changed(removePlayer) nutritionManager: " + Main.nutritionManager.toString());
        Main.scoreboardManager.removeBoardAndReferences(p);
    }
    /*
     * WHEN PLAYER RESPAWNS, RESET STATS AND SEND MESSAGE - CHANGE TO BOARD
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Main.nutritionManager.getPlayerNutrition(p).setFullStats();
        Main.scoreboardManager.showBoardDefiniteTime(p, 5);
    }
    /*
     * WHEN ANY INVENTORY IS CLOSED, CLEAR ACTION BAR MESSAGE FOR TIDINESS
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        if(!Main.nutritionManager.isConditionallyExempt(p)) {
            Main.actionBarManager.clear(p);
        }
    }
    /*
     * WHEN PLAYER NUTRITION FALLS, NATURAL REDUCE STATS
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if(entity instanceof Player) {
            Player p = (Player) entity;
            if(!Main.nutritionManager.isConditionallyExempt(p)) {
                Main.nutritionManager.getPlayerNutrition(p).naturalReduceStats();
            }
        }
    }
    /*
     * WHEN PLAYER EQUIPS NEW ITEM, SEND ACTION BAR MESSAGE OF NUTRITIONAL VALUE
     */
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if(!Main.nutritionManager.isConditionallyExempt(p)) {
            ItemStack is = p.getInventory().getItem(e.getNewSlot());
            if (is != null) {
                if (is.getType().isEdible()) {
                    Main.actionBarManager.actionBarMessageFoodNutrition(p, is.getType());
                } else {
                    Main.actionBarManager.clear(p);
                }
            } else {
                Main.actionBarManager.clear(p);
            }
        }
    }
    /*
     *
     */
    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(!Main.nutritionManager.isConditionallyExempt(p)) {
            ItemStack mainHandItemStack = p.getInventory().getItemInMainHand();
            Material mainHandMaterial = mainHandItemStack.getType();
            ItemStack offHandItemStack = p.getInventory().getItemInOffHand();
            Material offHandMaterial = offHandItemStack.getType();
            boolean usedMainHand = false, usedOffHand = false;

            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (mainHandItemStack != null) {
                    if (mainHandMaterial.isEdible()) {
                        Main.actionBarManager.actionBarMessageFoodNutrition(p, mainHandMaterial);
                        usedMainHand = true;
                    }
                }
                if (!usedMainHand && offHandItemStack != null) {
                    if (mainHandMaterial.isEdible()) {
                        Main.actionBarManager.actionBarMessageFoodNutrition(p, offHandMaterial);
                        usedOffHand = true;
                    }
                }
                if (!usedMainHand && !usedOffHand) {
                    Main.actionBarManager.clear(p);
                }
            }
        }
    }
    /*
     * HERE
     */
    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if(!Main.nutritionManager.isConditionallyExempt(p)) {
            Material mainHand = Objects.requireNonNull(e.getMainHandItem()).getType();
            Material offHand = Objects.requireNonNull(e.getOffHandItem()).getType();
            boolean usedMainHand = false, usedOffHand = false;
            if (e.getMainHandItem() != null) {
                if (e.getMainHandItem().getType().isEdible()) {
                    Material m = e.getMainHandItem().getType();
                    Main.actionBarManager.actionBarMessageFoodNutrition(p, m);
                    usedMainHand = true;
                }
            }
            if (!usedMainHand && e.getOffHandItem() != null) {
                Material m = e.getOffHandItem().getType();
                if (m.isEdible()) {
                    Main.actionBarManager.actionBarMessageFoodNutrition(p, m);
                    usedOffHand = true;
                }
            }
            if (!usedMainHand && !usedOffHand) {
                Main.actionBarManager.clear(p);
            }
        }
    }
    /*
     * HERE
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        int[] slotsToCheck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 40};
        int slotClicked = e.getSlot();
        if(Objects.requireNonNull(e.getClickedInventory()).getType().equals(InventoryType.PLAYER)) {
            InventoryHolder ih = e.getClickedInventory().getHolder();
            if (ih instanceof Player) {
                Player p = (Player) ih;
                if(!Main.nutritionManager.isConditionallyExempt(p)) {
                    for (int i : slotsToCheck) {
                        if (slotClicked == i) {
                            if (p.getInventory().getItem(i) != null) {
                                Material m = Objects.requireNonNull(p.getInventory().getItem(i)).getType();
                                Main.actionBarManager.actionBarMessageFoodNutrition(p, m);
                            } else {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (slotClicked == i && p.getInventory().getItem(i) != null) {
                                                Material m = Objects.requireNonNull(p.getInventory().getItem(i)).getType();
                                                Main.actionBarManager.actionBarMessageFoodNutrition(p, m);
                                            }
                                        } catch (Exception e) {
                                            Main.logger.error("Runnable for onInvClick() event encountered an error!\n" + Arrays.toString(e.getStackTrace()));
                                        }
                                    }
                                }, 1L);
                            }
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
        if (!Main.nutritionManager.isConditionallyExempt(p)) {
            PlayerNutrition pn = Main.nutritionManager.getPlayerNutrition(p);
            ItemStack item = e.getItem();
            Material material = item.getType();

            if (item.getType().isEdible()) {
                // Message player what they ate and what the current values are
                for (Category category : Main.simpleConfigGet.getCategoriesFromMaterial(material)) {
                    int nutritionValue = Main.simpleConfigGet.getValueFromCategoryAndMaterial(category, material);
                    int currentValue = pn.getValue(category);
                    pn.setValue(category, currentValue + nutritionValue);
                    int newValue = pn.getValue(category);
                    int delta = newValue - currentValue;

                    p.sendMessage(MessagingManager.infoPrefix + WordUtils.capitalizeFully(category.name()) + " +" + delta + "% = " + newValue + "%");
                    Main.scoreboardManager.showBoardDefiniteTime(p, 5);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
        Player p = e.getPlayer();
        PlayerNutrition pn = Main.nutritionManager.getPlayerNutrition(p);
        GameMode gm = e.getNewGameMode();
        String gmPretty = WordUtils.capitalizeFully(gm.name());

        switch(gm) {
            case SURVIVAL:
            case ADVENTURE:
                if(!pn.isExempt()) {
                    Main.messagingManager.info("Detected " + gmPretty + " gamemode. You are now subject to nutrition effects.", p);
                } else {
                    Main.messagingManager.info("Detected " + gmPretty + " gamemode. Due to a hard exemption placed on your userdata, you will remain exempt from nutrition effects.", p);
                }
            case CREATIVE:
            case SPECTATOR:
                if(!pn.isExempt()) {
                    Main.messagingManager.info("Detected " + gmPretty + " gamemode. You are now conditionally exempt from nutrition effects.", p);
                } else {
                    Main.messagingManager.info("Detected " + gmPretty + " gamemode. Due to a hard exemption placed on your userdata, you were already exempt from nutrition effects.", p);
                }
        }
    }
}

package me.danvb10.sustenance.listeners;

import me.danvb10.sustenance.Sustenance;
import me.danvb10.sustenance.utilities.enums.Category;
import me.danvb10.sustenance.utilities.enums.MaterialUtil;
import me.danvb10.sustenance.utilities.messaging.MessageTemplates;
import me.danvb10.sustenance.utilities.messaging.MessagingManager;
import me.danvb10.sustenance.utilities.playerdata.PlayerNutrition;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

    Sustenance plugin;

    public Listeners(Sustenance plugin) {
        this.plugin = plugin;
    }

    /*
     * WHEN PLAYER JOINS, ADD PLAYER TO MANAGER OBJECT, MESSAGE CURRENT STATUS - CHANGE TO BOARD
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        try {
            Player p = e.getPlayer();
            Sustenance.nutritionManager.addPlayer(p);
            Sustenance.logger.verbose("Listeners:onPlayerJoinEvent(): nutritionManager - add player: " + p.getName());
            Sustenance.scoreboardManager.showBoardDefiniteTime(p, 10);
            p.sendMessage(MessageTemplates.welcomeMessage(p));
            Sustenance.nutritionManager.getPlayerNutrition(p).updatePlayerEffects();
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerJoinEvent(): " + exception);
        }
    }

    /*
     * WHEN PLAYER QUITS, REMOVE PLAYER FROM MANAGER OBJECT
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        try {
            Player p = e.getPlayer();
            Sustenance.logger.verbose("Listeners:onPlayerQuitEvent(): nutritionManager - remove player: " + p.getName());
            Sustenance.scoreboardManager.removeBoardAndReferences(p);
            Sustenance.nutritionManager.removeSavePlayer(p);
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerQuitEvent(): " + exception);
        }
    }

    /*
     * WHEN PLAYER RESPAWNS, RESET STATS AND SEND MESSAGE - CHANGE TO BOARD
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        try {
            Player p = e.getPlayer();
            Sustenance.nutritionManager.getPlayerNutrition(p).setRespawnStats();
            Sustenance.scoreboardManager.showBoardDefiniteTime(p, 5);
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerRespawnEvent(): " + exception);
        }
    }

    /*
     * WHEN ANY INVENTORY IS CLOSED, CLEAR ACTION BAR MESSAGE FOR TIDINESS
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        try {
            Player p = (Player) e.getPlayer();

            if (!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                Sustenance.actionBarManager.clear(p);
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onInventoryCloseEvent(): " + exception);
        }
    }

    /*
     * WHEN PLAYER NUTRITION FALLS, NATURAL REDUCE STATS
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        try {
            Entity entity = e.getEntity();
            if(entity instanceof Player) {
                Player p = (Player) entity;
                if(!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                    Sustenance.nutritionManager.getPlayerNutrition(p).naturalReduceStats();
                }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onFoodLevelChangeEvent(): " + exception);
        }
    }

    /*
     * WHEN PLAYER EQUIPS NEW ITEM, SEND ACTION BAR MESSAGE OF NUTRITIONAL VALUE
     */
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
        try {
            Player p = e.getPlayer();
            if(!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                ItemStack is = p.getInventory().getItem(e.getNewSlot());
                if (is != null) {
                    if (is.getType().isEdible()) {
                        Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, is.getType());
                    } else {
                        Sustenance.actionBarManager.clear(p);
                    }
                } else {
                    Sustenance.actionBarManager.clear(p);
                }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerItemHeldEvent(): " + exception);
        }
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if(!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                ItemStack mainHandItemStack = p.getInventory().getItemInMainHand();
                Material mainHandMaterial = mainHandItemStack.getType();
                ItemStack offHandItemStack = p.getInventory().getItemInOffHand();
                Material offHandMaterial = offHandItemStack.getType();
                boolean usedMainHand = false, usedOffHand = false;

                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (mainHandItemStack != null) {
                        if (mainHandMaterial.isEdible()) {
                            Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, mainHandMaterial);
                            usedMainHand = true;
                        }
                    }
                    if (!usedMainHand && offHandItemStack != null) {
                        if (mainHandMaterial.isEdible()) {
                            Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, offHandMaterial);
                            usedOffHand = true;
                        }
                    }
                    if (!usedMainHand && !usedOffHand) {
                        Sustenance.actionBarManager.clear(p);
                    }
                }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onClickEvent(): " + exception);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        try {
        Player p = e.getPlayer();
            if(!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                Material mainHand = Objects.requireNonNull(e.getMainHandItem()).getType();
                Material offHand = Objects.requireNonNull(e.getOffHandItem()).getType();
                boolean usedMainHand = false, usedOffHand = false;
                if (e.getMainHandItem() != null) {
                    if (e.getMainHandItem().getType().isEdible()) {
                        Material m = e.getMainHandItem().getType();
                        Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, m);
                        usedMainHand = true;
                    }
                }
                if (!usedMainHand && e.getOffHandItem() != null) {
                    Material m = e.getOffHandItem().getType();
                    if (m.isEdible()) {
                        Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, m);
                        usedOffHand = true;
                    }
                }
                if (!usedMainHand && !usedOffHand) {
                    Sustenance.actionBarManager.clear(p);
                }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerSwapHandItemsEvent(): " + exception);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        try {
            int[] slotsToCheck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 40};
            int slotClicked = e.getSlot();
            if (Objects.requireNonNull(e.getClickedInventory()).getType().equals(InventoryType.PLAYER)) {
                InventoryHolder ih = e.getClickedInventory().getHolder();
                if (ih instanceof Player) {
                    Player p = (Player) ih;
                    if (!Sustenance.nutritionManager.isConditionallyExempt(p)) {
                        for (int i : slotsToCheck) {
                            if (slotClicked == i) {
                                if (p.getInventory().getItem(i) != null) {
                                    Material m = Objects.requireNonNull(p.getInventory().getItem(i)).getType();
                                    Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, m);
                                } else {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (slotClicked == i && p.getInventory().getItem(i) != null) {
                                                    Material m = Objects.requireNonNull(p.getInventory().getItem(i)).getType();
                                                    Sustenance.actionBarManager.actionBarMessageFoodNutrition(p, m);
                                                }
                                            } catch (Exception e) {
                                                Sustenance.logger.error("Runnable for onInvClick() event encountered an error!\n" + Arrays.toString(e.getStackTrace()));
                                            }
                                        }
                                    }, 1L);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onInventoryClickEvent(): " + exception);
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        try {
            // if a player consumes any food, show the board AND send a message saying what changed!
            Player p = e.getPlayer();

            if (Sustenance.nutritionManager.isConditionallyExempt(p))
                return;

            PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition(p);
            ItemStack item = e.getItem();
            Material material = item.getType();

            if (material.isEdible() || MaterialUtil.isConsumable(material)) {
                // Message player what they ate and what the current values are
                for (Category category : Sustenance.simpleConfigGet.getCategoriesFromMaterial(material)) {
                    int nutritionValue = Sustenance.simpleConfigGet.getValueFromCategoryAndMaterial(category, material);
                    int currentValue = pn.getValue(category);
                    pn.setValue(category, currentValue + nutritionValue);
                    int newValue = pn.getValue(category);
                    int delta = newValue - currentValue;

                    p.sendMessage(MessagingManager.infoPrefix + (category.name()).toUpperCase() + " +" + delta + "% = " + newValue + "%");
                    Sustenance.scoreboardManager.showBoardDefiniteTime(p, 5);
                }
            }

        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerItemConsumeEvent(): " + exception);
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent e) {
        try {
            Player p = e.getPlayer();
            PlayerNutrition pn = Sustenance.nutritionManager.getPlayerNutrition(p);
            GameMode gm = e.getNewGameMode();
            String gmPretty = (gm.name()).toUpperCase();

            switch(gm) {
                case SURVIVAL:
                case ADVENTURE:
                    if(!pn.isExempt()) {
                        Sustenance.messagingManager.info("Detected " + gmPretty + " gamemode. You are now subject to nutrition effects.", p);
                    } else {
                        pn.clearActiveEffects();
                        Sustenance.messagingManager.info("Detected " + gmPretty + " gamemode. Due to a hard exemption placed on your userdata, you will remain exempt from nutrition effects.", p);
                    }
                case CREATIVE:
                case SPECTATOR:
                    pn.clearActiveEffects();
                    if(!pn.isExempt()) {
                        Sustenance.messagingManager.info("Detected " + gmPretty + " gamemode. You are now conditionally exempt from nutrition effects.", p);
                    } else {
                        Sustenance.messagingManager.info("Detected " + gmPretty + " gamemode. Due to a hard exemption placed on your userdata, you were already exempt from nutrition effects.", p);
                    }
            }
        } catch (Exception exception) {
            Sustenance.logger.verbose("Listeners:onPlayerGameModeChangeEvent(): " + exception);
        }
    }
}

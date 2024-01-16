package top.speedcubing.knockbackffa.cosmetics.killeffect.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;

public class GuiKillEffectSelector implements Listener {

    private KnockBackFFA plugin;
    private KillEffectManager killEffectManager;

    public GuiKillEffectSelector(KnockBackFFA plugin, KillEffectManager killEffectManager) {
        this.plugin = plugin;
        this.killEffectManager = killEffectManager;
    }

    public void open(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Kill Effects");
        ItemStack NONE = new CustomItem(Material.BARRIER, 1, "§aNONE",
                "§7Selecting this option",
                "§7disables your Kill Effect.",
                "",
                (killEffectManager.hasSelectedOne(p) ? "§eClick to select!" : "§aSELECTED!"));
        if (p.hasPermission("killeffect.blood_explosion")) {
            ItemStack BLOOD_EXPLOSION = new CustomItem(killEffectManager.getKillEffectById("BLOOD_EXPLOSION").getIcon(), 1,
                    "§aBlood Explosion Kill Effect",
                    "§7Select the Blood Explosion",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "BLOOD_EXPLOSION") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(12, BLOOD_EXPLOSION);
        } else {
            ItemStack LOCKED_BLOOD_EXPLOSION = new CustomItem(killEffectManager.getKillEffectById("BLOOD_EXPLOSION").getIcon(), 1,
                    "§aBlood Explosion Kill Effect",
                    "§7Select the Blood Explosion",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "BLOOD_EXPLOSION") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(12, LOCKED_BLOOD_EXPLOSION);
        }
        if (p.hasPermission("killeffect.heart_explosion")) {
            ItemStack HEART_EXPLOSION = new CustomItem(killEffectManager.getKillEffectById("HEART_EXPLOSION").getIcon(), 1,
                    "§aHeart Explosion Kill Effect",
                    "§7Select the Heart Explosion",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "HEART_EXPLOSION") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(13, HEART_EXPLOSION);
        } else {
            ItemStack LOCKED_HEART_EXPLOSION = new CustomItem(killEffectManager.getKillEffectById("HEART_EXPLOSION").getIcon(), 1,
                    "§aHeart Explosion Kill Effect",
                    "§7Select the Heart Explosion",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "HEART_EXPLOSION") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(13, LOCKED_HEART_EXPLOSION);
        }
        if (p.hasPermission("killeffect.head_rocket")) {
            ItemStack HEAD_ROCKET = new CustomItem(killEffectManager.getKillEffectById("HEAD_ROCKET").getIcon(), 1,
                    "§aHead Rocket Kill Effect",
                    "§7Select the Head Rocket",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "HEAD_ROCKET") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(14, HEAD_ROCKET);
        } else {
            ItemStack LOCKED_HEAD_ROCKET = new CustomItem(killEffectManager.getKillEffectById("HEAD_ROCKET").getIcon(), 1,
                    "§aHead Rocket Kill Effect",
                    "§7Select the Head Rocket",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "HEAD_ROCKET") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(14, LOCKED_HEAD_ROCKET);
        }
        if (p.hasPermission("killeffect.final_smash")) {
            ItemStack FINAL_SMASH = new CustomItem(killEffectManager.getKillEffectById("FINAL_SMASH").getIcon(), 1,
                    "§aFinal Smash Kill Effect",
                    "§7Select the Final Smash",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "FINAL_SMASH") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(15, FINAL_SMASH);
        } else {
            ItemStack LOCKED_FINAL_SMASH = new CustomItem(killEffectManager.getKillEffectById("FINAL_SMASH").getIcon(), 1,
                    "§aFinal Smash Kill Effect",
                    "§7Select the Final Smash",
                    "§7Kill Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!killEffectManager.hasSelected(p, "FINAL_SMASH") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(15, LOCKED_FINAL_SMASH);
        }
        if (p.hasPermission("killeffect.lightning")) {
            ItemStack LIGHTNING = new CustomItem(killEffectManager.getKillEffectById("LIGHTNING").getIcon(), 1,
                    "§aLightning Kill Effect",
                    "§7Select the Lightning",
                    "§7Kill Effect.",
                    "§7This change is cosmetic",
                    "",
                    (!killEffectManager.hasSelected(p, "LIGHTNING") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(20, LIGHTNING);
        } else {
            ItemStack LOCKED_LIGHTNING = new CustomItem(killEffectManager.getKillEffectById("LIGHTNING").getIcon(), 1,
                    "§aLightning Kill Effect",
                    "§7Select the Lightning",
                    "§7Kill Effect.",
                    "§7This change is cosmetic",
                    "",
                    (!killEffectManager.hasSelected(p, "LIGHTNING") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(20, LOCKED_LIGHTNING);
        }
        if (p.hasPermission("killeffect.tnt")) {
            ItemStack TNT = new CustomItem(killEffectManager.getKillEffectById("TNT").getIcon(), 1,
                    "§aLightning Kill Effect",
                    "§7Select the TNT",
                    "§7Kill Effect.",
                    "§7This change is cosmetic",
                    "",
                    (!killEffectManager.hasSelected(p, "TNT") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(21, TNT);
        } else {
            ItemStack LOCKED_TNT = new CustomItem(killEffectManager.getKillEffectById("TNT").getIcon(), 1,
                    "§aLightning Kill Effect",
                    "§7Select the TNT",
                    "§7Kill Effect.",
                    "§7This change is cosmetic",
                    "",
                    (!killEffectManager.hasSelected(p, "TNT") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(21, LOCKED_TNT);
        }

        ItemStack close = new CustomItem(Material.BARRIER, 1, "§cClose");
        ItemStack back = new CustomItem(Material.ARROW, 1, "§aGo Back",
                "§7Back to Cosmetics Menu");
        inventory.setItem(45, back);
        inventory.setItem(11, NONE);
        inventory.setItem(49, close);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory() != null) {
            if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("Kill Effects")) {
                e.setCancelled(true);
                if (e.getSlotType().equals(SlotType.OUTSIDE) || e.getCurrentItem().getType().equals(Material.AIR)) {
                    return;
                }
                if (e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                    e.setCancelled(true);
                    if (e.getSlot() == 49) {
                        p.closeInventory();
                    }
                    if (e.getSlot() == 45) {
                        plugin.getGuiCosmeticsSelector().open(p);
                    }
                    if (e.getSlot() == 11) {
                        if (killEffectManager.hasSelectedOne(p)) {
                            killEffectManager.setKillEffect(p, "NONE");
                            open(p);
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                            p.sendMessage("§6You selected §aNONE§6!");
                        } else {
                            p.sendMessage("§cYou have already selected this item!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 12) {
                        if (p.hasPermission("killeffect.blood_explosion")) {
                            if (!killEffectManager.hasSelected(p, "BLOOD_EXPLOSION")) {
                                killEffectManager.setKillEffect(p, "BLOOD_EXPLOSION");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aBlood Explosion Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 13) {
                        if (p.hasPermission("killeffect.heart_explosion")) {
                            if (!killEffectManager.hasSelected(p, "HEART_EXPLOSION")) {
                                killEffectManager.setKillEffect(p, "HEART_EXPLOSION");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aHeart Explosion Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 14) {
                        if (p.hasPermission("killeffect.head_rocket")) {
                            if (!killEffectManager.hasSelected(p, "HEAD_ROCKET")) {
                                killEffectManager.setKillEffect(p, "HEAD_ROCKET");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aHead Rocket Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 15) {
                        if (p.hasPermission("killeffect.final_smash")) {
                            if (!killEffectManager.hasSelected(p, "FINAL_SMASH")) {
                                killEffectManager.setKillEffect(p, "FINAL_SMASH");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aFinal Smash Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 20) {
                        if (p.hasPermission("killeffect.lightning")) {
                            if (!killEffectManager.hasSelected(p, "LIGHTNING")) {
                                killEffectManager.setKillEffect(p, "LIGHTNING");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aLightning Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 21) {
                        if (p.hasPermission("killeffect.tnt")) {
                            if (!killEffectManager.hasSelected(p, "TNT")) {
                                killEffectManager.setKillEffect(p, "TNT");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aTNT Kill Effect§6!");
                            } else {
                                p.sendMessage("§cYou have already selected this item!");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                            }
                        } else {
                            p.sendMessage("§cYou haven't unlocked this yet!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                }
            }
        }
    }
}

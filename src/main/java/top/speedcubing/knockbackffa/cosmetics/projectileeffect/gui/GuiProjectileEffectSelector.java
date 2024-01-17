package top.speedcubing.knockbackffa.cosmetics.projectileeffect.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.server.player.User;

public class GuiProjectileEffectSelector implements Listener {
    private KnockBackFFA plugin;
    private ProjectileEffectManager projectileEffectManager;

    public GuiProjectileEffectSelector(KnockBackFFA plugin, ProjectileEffectManager projectileEffectManager) {
        this.plugin = plugin;
        this.projectileEffectManager = projectileEffectManager;
    }

    public void open(Player p) {
        User user = User.getUser(p);
        Inventory inventory = Bukkit.createInventory(null, 54, "Projectile Effects");
        ItemStack NONE = new CustomItem(Material.BARRIER, 1, "§aNONE", "§7Selecting this option",
                "§7disables your Kill Effect.",
                "",
                (projectileEffectManager.hasSelectedOne(p) ? "§eClick to select!" : "§aSELECTED!"));
        inventory.setItem(11, NONE);
        if (p.hasPermission("projectileeffect.heart") || user.hasPermission("projectileeffect.heart")) {
            ItemStack HEART = new CustomItem(projectileEffectManager.getProjectileEffectById("HEART_PARTICLE").getIcon(), 1,
                    "§aHeart Projectile Effect",
                    "§7Select the Heart Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "HEART_PARTICLE") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(12, HEART);
        } else {
            ItemStack LOCKED_HEART = new CustomItem(projectileEffectManager.getProjectileEffectById("HEART_PARTICLE").getIcon(), 1,
                    "§aHeart Projectile Effect",
                    "§7Select the Heart Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "HEART_PARTICLE") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(12, LOCKED_HEART);
        }
        if (p.hasPermission("projectiileeffect.note") || user.hasPermission("projectileeffect.note")) {
            ItemStack NOTE = new CustomItem(projectileEffectManager.getProjectileEffectById("NOTE_PARTICLE").getIcon(),1,
                    "§aNote Projectile Effect",
                    "§7Select the Note Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p,"NOTE_PARTICLE") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(13, NOTE);
        } else {
            ItemStack LOCKED_NOTE = new CustomItem(projectileEffectManager.getProjectileEffectById("NOTE_PARTICLE").getIcon(),1,
                    "§aNote Projectile Effect",
                    "§7Select the Note Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p,"NOTE_PARTICLE") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(13, LOCKED_NOTE);
        }
        if (p.hasPermission("projectileeffect.egg") || user.hasPermission("projectileeffect.egg")) {
            ItemStack EGG = new CustomItem(projectileEffectManager.getProjectileEffectById("EGG_PARTICLE").getIcon(), 1,
                    "§aEgg Projectile Effect",
                    "§7Select the Egg Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "EGG_PARTICLE") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(14, EGG);
        } else {
            ItemStack LOCKED_EGG = new CustomItem(projectileEffectManager.getProjectileEffectById("EGG_PARTICLE").getIcon(), 1,
                    "§aEgg Projectile Effect",
                    "§7Select the Egg Particle",
                    "§7Projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "EGG_PARTICLE") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(14, LOCKED_EGG);
        }
        if (p.hasPermission("projectileeffect.champ") || user.hasPermission("projectileeffect.champ")) {
            ItemStack CHAMP = new CustomItem(projectileEffectManager.getProjectileEffectById("CHAMP").getIcon(),1,
                    "§aChamp Projectile Effect",
                    "§7SSelect the Champ Particle",
                    "§7projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "CHAMP") ? "§eClick to select!" : "§aSELECTED!"));
            inventory.setItem(15,CHAMP);
        } else {
            ItemStack LOCKED_CHAMP = new CustomItem(projectileEffectManager.getProjectileEffectById("CHAMP").getIcon(),1,
                    "§aChamp Projectile Effect",
                    "§7SSelect the Champ Particle",
                    "§7projectile Effect. This change is",
                    "§7cosmetic.",
                    "",
                    (!projectileEffectManager.hasSelected(p, "CHAMP") ? "§cLocked!" : "§aSELECTED!"));
            inventory.setItem(15,LOCKED_CHAMP);
        }
        ItemStack close = new CustomItem(Material.BARRIER, 1, "§cClose");
        ItemStack back = new CustomItem(Material.ARROW, 1, "§aGo Back",
                "§7Back to Cosmetics Menu");
        inventory.setItem(45,back);
        inventory.setItem(49,close);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = User.getUser(p);
        if (p.getOpenInventory().getTopInventory() != null) {
            if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("Projectile Effects")) {
                e.setCancelled(true);
                if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || e.getCurrentItem().getType().equals(Material.AIR)) {
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
                        if (projectileEffectManager.hasSelectedOne(p)) {
                            projectileEffectManager.setProjectileEffect(p, "NONE");
                            open(p);
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                            p.sendMessage("§6You selected §aNONE§6!");
                        } else {
                            p.sendMessage("§cYou have already selected this item!");
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                        }
                    }
                    if (e.getSlot() == 12) {
                        if (p.hasPermission("projectileeffect.heart") || user.hasPermission("projectileeffect.heart")) {
                            if (!projectileEffectManager.hasSelected(p, "HEART_PARTICLE")) {
                                projectileEffectManager.setProjectileEffect(p, "HEART_PARTICLE");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aHeart Projectile Effect§6!");
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
                        if (p.hasPermission("projectileeffect.note") || user.hasPermission("projectileeffect.note")) {
                            if (!projectileEffectManager.hasSelected(p, "NOTE_PARTICLE")) {
                                projectileEffectManager.setProjectileEffect(p, "NOTE_PARTICLE");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aNote Projectile Effect§6!");
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
                        if (p.hasPermission("projectileeffect.egg") || user.hasPermission("projectileeffect.egg")) {
                            if (!projectileEffectManager.hasSelected(p, "EGG_PARTICLE")) {
                                projectileEffectManager.setProjectileEffect(p, "EGG_PARTICLE");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aEgg Projectile Effect§6!");
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
                        if (p.hasPermission("projectileeffect.champ") || user.hasPermission("projectileeffect.champ")) {
                            if (!projectileEffectManager.hasSelected(p, "CHAMP")) {
                                projectileEffectManager.setProjectileEffect(p, "CHAMP");
                                open(p);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                                p.sendMessage("§6You selected §aChamp Projectile Effect§6!");
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

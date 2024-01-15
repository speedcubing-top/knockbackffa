package top.speedcubing.knockbackffa.projectileeffect.gui;

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
import top.speedcubing.knockbackffa.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.projectileeffect.manager.ProjectileEffectManager;

public class GuiProjectileEffectSelector implements Listener {
    private ProjectileEffectManager projectileEffectManager;

    public GuiProjectileEffectSelector(ProjectileEffectManager projectileEffectManager) {
        this.projectileEffectManager = projectileEffectManager;
    }

    public void open(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Projectile Effects");
        ItemStack NONE = new CustomItem(Material.BARRIER, 1, "§aNONE", "§7Selecting this option",
                "§7disables your Kill Effect.",
                "",
                (projectileEffectManager.hasSelectedOne(p) ? "§eClick to select!" : "§aSELECTED!"));
        inventory.setItem(11, NONE);
        if (p.hasPermission("projectileeffect.heart")) {
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
        ItemStack close = new CustomItem(Material.BARRIER, 1, "§cClose");
        inventory.setItem(49,close);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
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
                        if (p.hasPermission("projectileeffect.heart_particle")) {
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
                }
            }
        }
    }
}

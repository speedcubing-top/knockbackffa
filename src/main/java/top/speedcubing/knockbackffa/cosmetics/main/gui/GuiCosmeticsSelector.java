package top.speedcubing.knockbackffa.cosmetics.main.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;

public class GuiCosmeticsSelector implements Listener {
    private KnockBackFFA plugin;
    private KillEffectManager killEffectManager;
    private ProjectileEffectManager projectileEffectManager;
    private String killEffectName;
    private String projectileEffectName;
    public GuiCosmeticsSelector(KnockBackFFA plugin, KillEffectManager killEffectManager, ProjectileEffectManager projectileEffectManager) {
        this.plugin = plugin;
        this.killEffectManager = killEffectManager;
        this.projectileEffectManager = projectileEffectManager;
    }
    public void open(Player p) {
        Inventory inventory = Bukkit.createInventory(null,54,"Cosmetics");
        KillEffect killEffect = killEffectManager.getKillEffect(p);
        if (killEffect != null) {
            killEffectName = killEffect.getName();
        } else {
            killEffectName = "NONE";
        }
        ItemStack KILL_SELECTOR = new CustomItem(Material.IRON_SWORD,1,"§aKill Effects",
                "§7Click this option",
                "§7change to Kill Effects Selector.",
                "",
                "§7Current Selected:",
                "§a" + killEffectName,
                "",
                "§eClick to view!");
        ItemMeta meta = KILL_SELECTOR.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        KILL_SELECTOR.setItemMeta(meta);
        inventory.setItem(21,KILL_SELECTOR);

        ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(p);
        if (projectileEffect != null) {
            projectileEffectName = projectileEffect.getName();
        } else {
            projectileEffectName = "NONE";
        }
        ItemStack PROJECTILE_SELECTOR = new CustomItem(Material.ARROW,1,"§aProjectile Effects",
                "§7Click this option",
                "§7change to Projectile Effects",
                "§7Selector.",
                "",
                "§7Current Selected:",
                "§a" + projectileEffectName,
                "",
                "§eClick to view!");
        inventory.setItem(23,PROJECTILE_SELECTOR);

        ItemStack close = new CustomItem(Material.BARRIER, 1, "§cClose");
        inventory.setItem(49,close);
        p.openInventory(inventory);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory() != null) {
            if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("Cosmetics")) {
                e.setCancelled(true);
                if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || e.getCurrentItem().getType().equals(Material.AIR)) {
                    return;
                }
                if (e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                    e.setCancelled(true);
                    if (e.getSlot() == 49) {
                        p.closeInventory();
                    }
                    if (e.getSlot() == 21) {
                        plugin.getGuiKillEffectSelector().open(p);
                    }
                    if (e.getSlot() == 23) {
                        plugin.getGuiProjectileEffectSelector().open(p);
                    }
                }
            }
        }
    }
}

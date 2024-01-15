package top.speedcubing.knockbackffa.projectile.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.knockbackffa.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.projectile.manager.ProjectileEffectManager;

public class GuiProjectileEffectSelector implements Listener {
    private ProjectileEffectManager projectileEffectManager;
    public GuiProjectileEffectSelector(ProjectileEffectManager projectileEffectManager) {
        this.projectileEffectManager = projectileEffectManager;
    }
    public void open(Player p) {
        Inventory inventory = Bukkit.createInventory(null,54,"Projectile Effects");
        ItemStack None = new CustomItem(Material.BARRIER,1,"§NONE");
    }
}

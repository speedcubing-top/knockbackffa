package top.speedcubing.knockbackffa.Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.speedcubing.knockbackffa.utils.ItemDamageUtil;

public class TrollUtilsItemList {
    public static ItemStack oceanBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName(cl("&0OceanBow"));
        meta.spigot().setUnbreakable(true);
        bow.setItemMeta(meta);
        return bow;
    }
    public static ItemStack verticalStick() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(cl("&cVerticalStick"));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack fastPickaxe() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DIG_SPEED,100,true);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack verticalStickProMax() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6VerticalStickProMax");
        item.setItemMeta(meta);
        return item;
    }
    private static String cl(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}

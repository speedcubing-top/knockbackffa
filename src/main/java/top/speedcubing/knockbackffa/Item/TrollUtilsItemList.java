package top.speedcubing.knockbackffa.Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TrollUtilsItemList {
    public static ItemStack oceanBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName(cl("&0OceanBow"));
        meta.spigot().setUnbreakable(true);
        bow.setItemMeta(meta);
        return bow;
    }
    private static String cl(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}

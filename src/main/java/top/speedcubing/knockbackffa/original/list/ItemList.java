package top.speedcubing.knockbackffa.original.list;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.server.lang.LangItem;

public class ItemList {
    public static final ItemStack[] itemStacks = new ItemStack[14];
    public static LangItem invalid = new LangItem(Material.STAINED_GLASS_PANE).name("%lang_inv_cant_set_item%").durability(8);
    public static LangItem inventory = new LangItem(Material.CHEST).name("%lang_inv_inventory%");
    public static LangItem cosmetics = new LangItem(Material.EMERALD).name("§aCosmetics");

    public static void init() {
        itemStacks[0] = new ItemBuilder(Material.LEATHER_HELMET).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
        itemStacks[1] = new ItemBuilder(Material.LEATHER_CHESTPLATE).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
        itemStacks[2] = new ItemBuilder(Material.LEATHER_LEGGINGS).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
        itemStacks[3] = new ItemBuilder(Material.LEATHER_BOOTS).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
        itemStacks[4] = new ItemBuilder(Material.GOLD_SWORD).unBreak().ench(Enchantment.DAMAGE_ALL, 1).build();
        itemStacks[5] = new ItemBuilder(Material.STICK).ench(Enchantment.KNOCKBACK, 1).build();
        itemStacks[6] = new ItemBuilder(Material.ENDER_PEARL).build();
        itemStacks[7] = new ItemBuilder(Material.WEB).build();
        itemStacks[8] = new ItemBuilder(Material.SANDSTONE).amount(64).build();
        itemStacks[9] = new ItemBuilder(Material.ARROW).amount(16).build();
        itemStacks[10] = new ItemBuilder(Material.BOW).unBreak().ench(Enchantment.ARROW_KNOCKBACK, 1).build();
        itemStacks[13] = new ItemBuilder(Material.STONE_PICKAXE).unBreak().ench(Enchantment.DIG_SPEED, 1).build();
    }

}

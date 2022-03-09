package speedcubing;

import cubing.bukkit.Event.ServerEventManager;
import cubing.bukkit.InventoryUtils;
import cubing.bukkit.ItemUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KnockBackFFA extends JavaPlugin {
    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final ItemStack[][] itemStacks = new ItemStack[4][12];
    public static final Inventory[] KitEditor = new Inventory[4];
    public static final InventoryHolder KitEditorHolder = () -> null;
    public static int id = 0;
    public static final Map<BlockPosition, Integer[]> Diff = new HashMap<>();
    public static String[] Kill = {
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7."
    };

    public static int centerX = 0;
    public static int centerZ = 0;
    public static int offsetY = 0;

    public static void switchMap(int newX, int newZ, int newY) {
        centerX = newX;
        centerZ = newZ;
        offsetY = newY;
    }

    public void onEnable() {
        Bukkit.getPluginCommand("map").setExecutor(new map());
        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);
        World world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setTime(6000);
        ServerEventManager.registerListeners(new ServerEvent());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Set<PacketPlayOutBlockBreakAnimation> a = new HashSet<>();
            Set<BlockPosition> ba = new HashSet<>();
            for (Map.Entry<BlockPosition, Integer[]> x : KnockBackFFA.Diff.entrySet()) {
                int age = x.getValue()[1];
                BlockPosition b = x.getKey();
                if (age % 10 == 0)
                    a.add(new PacketPlayOutBlockBreakAnimation(x.getValue()[0], b, age / 10 - 1));
                if (age == 110) {
                    world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
                    ba.add(b);
                }
            }
            for (Player
                    p : Bukkit.getOnlinePlayers()) {
                PlayerConnection c = ((CraftPlayer) p).getHandle().playerConnection;
                for (PacketPlayOutBlockBreakAnimation packet : a) {
                    c.sendPacket(packet);
                }
            }
            for (BlockPosition b : ba) {
                KnockBackFFA.Diff.remove(b);
            }
            for (Integer[] x : KnockBackFFA.Diff.values()) {
                x[1] += 1;
            }
        }, 0, 1);

        String[] inv = {"§bInventar", "§bInventory", "§b物品欄", "§b物品栏"};
        String[] title = {"§cKnockBack FFA Inventar", "§cKnockBack FFA Inventory", "§cKnockBack FFA 物品欄", "§cKnockBack FFA 物品栏"};
        String[] save = {"§aÄnderungen speichern", "§aSave Changes", "§a儲存設定", "§a储存设置"};
        String[] unsave = {"§cnicht gespeicherte Änderungen", "§cUnSave Changes", "§c不儲存設定", "§c不储存设置"};
        String[] display = {"§4Sie können die Elemente hier nicht einstellen!", "§4You can't set the items here!", "§4你不能將物品設定於此處!", "§4你不能将物品设定于此处!"};
        for (int i = 0; i < 4; i++) {
            itemStacks[i][0] = ItemUtils.item(Material.LEATHER_HELMET, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}, null);
            itemStacks[i][1] = ItemUtils.item(Material.LEATHER_CHESTPLATE, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}, null);
            itemStacks[i][2] = ItemUtils.item(Material.LEATHER_LEGGINGS, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}, null);
            itemStacks[i][3] = ItemUtils.item(Material.LEATHER_BOOTS, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}, null);
            itemStacks[i][4] = ItemUtils.item(Material.WOOD_SWORD, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.DAMAGE_ALL}, new int[]{1}, null);
            itemStacks[i][5] = ItemUtils.item(Material.STICK, null, 1, -1, false, false, null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null);
            itemStacks[i][6] = ItemUtils.item(Material.ENDER_PEARL, null, 1, -1, false, false, null, null, null, null);
            itemStacks[i][7] = ItemUtils.item(Material.WEB, null, 1, -1, false, false, null, null, null, null);
            itemStacks[i][8] = ItemUtils.item(Material.SANDSTONE, null, 64, -1, false, false, null, null, null, null);
            itemStacks[i][9] = ItemUtils.item(Material.ARROW, null, 16, -1, false, false, null, null, null, null);
            itemStacks[i][10] = ItemUtils.item(Material.BOW, null, 1, -1, false, true, null, new Enchantment[]{Enchantment.ARROW_KNOCKBACK}, new int[]{1}, null);
            itemStacks[i][11] = ItemUtils.item(Material.CHEST, inv[i], 1, -1, false, false, null, null, null, null);
            KitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
            InventoryUtils.fillItem(KitEditor[i], 0, 9, ItemUtils.item(Material.STAINED_GLASS_PANE, "§a", 1, 7, false, false, null, null, null, null));
            KitEditor[i].setItem(3, ItemUtils.item(Material.INK_SACK, save[i], 1, 10, false, false, null, null, null, null));
            KitEditor[i].setItem(5, ItemUtils.item(Material.INK_SACK, unsave[i], 1, 1, false, false, null, null, null, null));
            Invalid[i] = ItemUtils.item(Material.STAINED_GLASS_PANE, display[i], 1, 8, false, false, null, null, null, null);
        }
    }

    public void onDisable() {
        World world = Bukkit.getWorld("world");
        for (BlockPosition b : KnockBackFFA.Diff.keySet()) {
            world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
        }
    }
}

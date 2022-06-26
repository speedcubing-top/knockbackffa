package speedcubing;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import speedcubing.lib.bukkit.inventory.InventoryUtils;
import speedcubing.lib.bukkit.inventory.ItemBuilder;
import speedcubing.lib.eventbus.LibEventManager;
import speedcubing.server.config;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class KnockBackFFA extends JavaPlugin {
    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final ItemStack[][] itemStacks = new ItemStack[4][12];
    public static final Inventory[] KitEditor = new Inventory[4];
    public static final InventoryHolder KitEditorHolder = () -> null;
    public static int id = 0;
    public static final Map<BlockPosition, Integer[]> Diff = new HashMap<>();
    public static final Map<UUID, Integer> score = new HashMap<>();
    public static final List<UUID> session = new ArrayList<>();
    public static String[] Kill = {
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7.",
            "%format0%%name0%§7 was killed by %format1%%name1%§7."
    };

    public static String[][] sidebar = {
            {"§3KnockBackFFA", "§6" + config.SERVERIP},
            {"§3KnockBackFFA", "§6" + config.SERVERIP},
            {"§3KnockBackFFA", "§6" + config.SERVERIP},
            {"§3KnockBackFFA", "§6" + config.SERVERIP}
    };

    public static double centerX = 0;
    public static double centerZ = 0;
    public static double offsetY = 100;
    public static float yaw = 90;
    public static final int spawnprotectY = 146;

    public static void switchMap(double newX, double newZ, double newY, float newYaw) {
        centerX = newX;
        centerZ = newZ;
        offsetY = newY;
        yaw = newYaw;
    }

    public void onEnable() {
        Bukkit.getPluginCommand("map").setExecutor((commandSender, command, s, strings) -> {
            KnockBackFFA.switchMap(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Float.parseFloat(strings[3]));
            commandSender.sendMessage(strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3]);
            return true;
        });
        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);
        World world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.EASY);
        world.setTime(6000);
        LibEventManager.registerListeners(new ServerEvent());
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
            Bukkit.getOnlinePlayers().forEach(b -> a.forEach(((CraftPlayer) b).getHandle().playerConnection::sendPacket));
            ba.forEach(KnockBackFFA.Diff::remove);
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
            itemStacks[i][0] = new ItemBuilder(Material.LEATHER_HELMET).unBreak().ench(new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}).build();
            itemStacks[i][1] = new ItemBuilder(Material.LEATHER_CHESTPLATE).unBreak().ench(new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}).build();
            itemStacks[i][2] = new ItemBuilder(Material.LEATHER_LEGGINGS).unBreak().ench(new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}).build();
            itemStacks[i][3] = new ItemBuilder(Material.LEATHER_BOOTS).unBreak().ench(new Enchantment[]{Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE}, new int[]{2, 4}).build();
            itemStacks[i][4] = new ItemBuilder(Material.WOOD_SWORD).unBreak().ench(new Enchantment[]{Enchantment.DAMAGE_ALL}, new int[]{1}).build();
            itemStacks[i][5] = new ItemBuilder(Material.STICK).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][6] = new ItemBuilder(Material.ENDER_PEARL).build();
            itemStacks[i][7] = new ItemBuilder(Material.WEB).build();
            itemStacks[i][8] = new ItemBuilder(Material.SANDSTONE).amount(64).build();
            itemStacks[i][9] = new ItemBuilder(Material.ARROW).amount(16).build();
            itemStacks[i][10] = new ItemBuilder(Material.BOW).unBreak().ench(new Enchantment[]{Enchantment.ARROW_KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][11] = new ItemBuilder(Material.CHEST).name(inv[i]).build();
            KitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
            InventoryUtils.fillItem(KitEditor[i], 0, 9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("§a").build());
            KitEditor[i].setItem(3, new ItemBuilder(Material.INK_SACK).durability(10).name(save[i]).build());
            KitEditor[i].setItem(5, new ItemBuilder(Material.INK_SACK).durability(1).name(unsave[i]).build());
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(display[i]).build();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                speedcubingServer.tcp.sendClean(25576 - Bukkit.getPort() % 2, "online|4|" + Bukkit.getOnlinePlayers().size());
            }
        }, 0, 1000);
    }

    public void onDisable() {
        World world = Bukkit.getWorld("world");
        KnockBackFFA.Diff.keySet().forEach(a -> world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
    }

    public static List<String> getSideBar() {
        List<String> result = new ArrayList<>();
        int size = session.size();
        for (int j = 0; j < 10; j++) {
            if (size <= j)
                result.add("§7-,---");
            else {
                UUID ses = session.get(j);
                int sc = score.get(ses);
                if (sc == 0)
                    result.add("§7-,---");
                else
                    result.add(speedcubingServer.getFormat(Member.getMember(ses).user.rank)[1] + Bukkit.getPlayer(ses).getName() + "§8: §e" + sc);
            }
        }
        return result;
    }
}

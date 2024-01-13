package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.lib.bukkit.entity.*;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.lib.bukkit.inventory.*;
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.server.database.*;
import top.speedcubing.server.utils.config;


import java.util.*;

public class KnockBackFFA extends JavaPlugin {
    private static KnockBackFFA instance;


    public static String bold(String original) {
        StringBuilder builder = new StringBuilder();
        boolean bold = false;
        for (char c : original.toCharArray()) {
            builder.append(c);
            if (bold) {
                builder.append("§l");
                bold = false;
            }
            if (c == '§' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(original.charAt(original.indexOf(c) + 1)) > -1) {
                bold = true;
            }
        }
        return builder.toString();
    }


    public static double centerX = 0;
    public static double centerZ = 0;
    public static double offsetY = 100;
    public static float yaw = 0;
    public static double holoX = 0;
    public static double holoZ = 0;
    public static int spawnprotectY = 146;

    public static int spawnY = 150;

    static final ClickEvent clickEvent = (p, e) -> Member.getMember(p).setDisplayType(false);
    public static final String[] board = new String[10];
    public static final String[] dailyBoard = new String[10];
    public static final String[] boardName = new String[10];
    public static final String[] dailyBoardName = new String[10];
    public static final Hologram[] personal = new Hologram[2];

    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final ItemStack[][] itemStacks = new ItemStack[4][13];
    public static final Inventory[] KitEditor = new Inventory[4];
    public static final InventoryHolder KitEditorHolder = () -> null;
    public static int id = 0;
    public static final Map<Block, Integer[]> Diff = new HashMap<>();

    public static void switchMap(double centerX, double centerZ, double offsetY, float yaw, double holoX, double holoZ) {
        KnockBackFFA.centerX = centerX;
        KnockBackFFA.centerZ = centerZ;
        KnockBackFFA.yaw = yaw;
        KnockBackFFA.offsetY = offsetY;
        KnockBackFFA.holoX = holoX;
        KnockBackFFA.holoZ = holoZ;
        personal[0].setLocation(KnockBackFFA.holoX, personal[0].armorStand.locY, KnockBackFFA.holoZ, 0, 0);
        personal[1].setLocation(KnockBackFFA.holoX, personal[1].armorStand.locY, KnockBackFFA.holoZ, 0, 0);
    }

    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "knockbackffa");
        Bukkit.getPluginCommand("map").setExecutor((commandSender, command, s, strings) -> {
            KnockBackFFA.switchMap(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Float.parseFloat(strings[3]), Double.parseDouble(strings[4]), Double.parseDouble(strings[5]));
            commandSender.sendMessage(strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3]);
            return true;
        });
        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);
        World world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.EASY);
        world.setTime(6000);
        world.setAutoSave(false);
        CubingEventManager.registerListeners(new ServerEvent());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Set<PacketPlayOutBlockBreakAnimation> a = new HashSet<>();
            Set<Block> ba = new HashSet<>();
            for (Map.Entry<Block, Integer[]> x : Diff.entrySet()) {
                int age = x.getValue()[1];
                Block block = x.getKey();
                a.add(new PacketPlayOutBlockBreakAnimation(x.getValue()[0], new BlockPosition(block.getX(), block.getY(), block.getZ()), age));
                if (age == 10) {
                    block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).setType(Material.AIR);
                    ba.add(block);
                }
            }
            Bukkit.getOnlinePlayers().forEach(b -> a.forEach(((CraftPlayer) b).getHandle().playerConnection::sendPacket));
            ba.forEach(Diff::remove);
            for (Integer[] x : Diff.values()) {
                x[1] += 1;
            }
        }, 0, 10);
        config.event.call();

        String[] inv = {"§bInventar", "§bInventory", "§b物品欄", "§b物品栏"};
        String[] title = {"§cKnockBack FFA Inventar", "§cKnockBack FFA Inventory", "§cKnockBack FFA 物品欄", "§cKnockBack FFA 物品栏"};
        String[] save = {"§aÄnderungen speichern", "§aSave Changes", "§a儲存設定", "§a储存设置"};
        String[] unsave = {"§cnicht gespeicherte Änderungen", "§cUnSave Changes", "§c不儲存設定", "§c不储存设置"};
        String[] display = {"§4Sie können die Elemente hier nicht einstellen!", "§4You can't set the items here!", "§4你不能將物品設定於此處!", "§4你不能将物品设定于此处!"};
        for (
                int i = 0;
                i < 4; i++) {
            itemStacks[i][0] = new ItemBuilder(Material.LEATHER_HELMET).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
            itemStacks[i][1] = new ItemBuilder(Material.LEATHER_CHESTPLATE).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
            itemStacks[i][2] = new ItemBuilder(Material.LEATHER_LEGGINGS).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
            itemStacks[i][3] = new ItemBuilder(Material.LEATHER_BOOTS).unBreak().ench(Enchantment.PROTECTION_ENVIRONMENTAL, 2).ench(Enchantment.PROTECTION_PROJECTILE, 4).build();
            itemStacks[i][4] = new ItemBuilder(Material.WOOD_SWORD).unBreak().ench(Enchantment.DAMAGE_ALL, 1).build();
            itemStacks[i][5] = new ItemBuilder(Material.STICK).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][6] = new ItemBuilder(Material.ENDER_PEARL).build();
            itemStacks[i][7] = new ItemBuilder(Material.WEB).build();
            itemStacks[i][8] = new ItemBuilder(Material.SANDSTONE).amount(64).build();
            itemStacks[i][9] = new ItemBuilder(Material.ARROW).amount(16).build();
            itemStacks[i][10] = new ItemBuilder(Material.BOW).unBreak().ench(new Enchantment[]{Enchantment.ARROW_KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][11] = new ItemBuilder(Material.CHEST).name(inv[i]).build();
            itemStacks[i][12] = new ItemBuilder(Material.EMERALD).name("§aCosmetics").build();
            KitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
            InventoryUtils.fillItem(KitEditor[i], 0, 9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("§a").build());
            KitEditor[i].setItem(3, new ItemBuilder(Material.INK_SACK).durability(10).name(save[i]).build());
            KitEditor[i].setItem(5, new ItemBuilder(Material.INK_SACK).durability(1).name(unsave[i]).build());
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(display[i]).build();
        }

        personal[0] = new

                Hologram("", false, false).

                world("world").

                setLocation(KnockBackFFA.holoX, 152.4, KnockBackFFA.holoZ, 0, 0).

                setClickEvent(clickEvent);

        personal[1] = new

                Hologram("", false, false).

                world("world").

                setLocation(KnockBackFFA.holoX, 148.2, KnockBackFFA.holoZ, 0, 0).

                setClickEvent(clickEvent);
        new

                Hologram("§6§lClick to toggle!", true, true).

                setLocation(holoX, 148.5, holoZ, 0, 0).

                world("world").

                setClickEvent(clickEvent);
        new

                Timer("Cubing-Leaderboard-Thread").

                schedule(new TimerTask() {
                    int lastKill;
                    int index;
                    int a;
                    String[] arr;
                    String[] arr2;

                    @Override
                    public void run() {
                        try {
                            arr = Database.connection.select("priority,name,kbffa_kill,id").from("playersdata").orderBy("kbffa_kill DESC, name ASC").limit(0, 10).getStringArray();
                            arr2 = Database.connection.select("priority,name,kbffa_dailykill,id").from("playersdata").orderBy("kbffa_dailykill DESC, name ASC").limit(0, 10).getStringArray();
                            Database.connection.update("champ", "id=" + arr[3], "subject='kbffa_kill'");
                            Database.connection.update("champ", "id=" + arr2[3], "subject='kbffa_dailykill'");
                            lastKill = 0;
                            index = 0;
                            for (int i = 0; i < 10; i++) {
                                a = Integer.parseInt(arr[i * 4 + 2]);
                                if (lastKill != a) {
                                    index = i + 1;
                                    lastKill = a;
                                }
                                boardName[i] = arr[i * 4 + 3];
                                board[i] = StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", Rank.getFormat(arr[i * 4], Integer.parseInt(arr[i * 4 + 3]))[0] + arr[i * 4 + 1]).replace("%k%", arr[i * 4 + 2]);
                            }
                            lastKill = 0;
                            index = 0;
                            for (int i = 0; i < 10; i++) {
                                a = Integer.parseInt(arr2[i * 4 + 2]);
                                if (lastKill != a) {
                                    index = i + 1;
                                    lastKill = a;
                                }
                                dailyBoardName[i] = arr2[i * 4 + 3];
                                dailyBoard[i] = arr2[i * 4 + 2].equals("0") ? "§e#. §7### - §e#" : StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", Rank.getFormat(arr2[i * 4], Integer.parseInt(arr2[i * 4 + 3]))[0] + arr2[i * 4 + 1]).replace("%k%", arr2[i * 4 + 2]);
                            }
                            for (Member m : Member.users.values()) {
                                m.a();
                                if (m.killedPlayer) {
                                    m.killedPlayer = false;
                                    m.user.dbUpdate("kbffa_kill=" + m.kill + ",kbffa_dailykill=" + m.dailyKill);
                                }
                                m.updatePlayerPos(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 1000);
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, 1);
        new

                Timer("Cubing-Daily-Thread").

                schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Member.users.values().forEach(a -> a.dailyKill = 0);
                        Database.connection.update("playersdata", "kbffa_dailykill=0", "1");
                    }
                }, new

                        GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.

                        get(Calendar.MONTH), tomorrow.

                        get(Calendar.DATE), 0, 0).

                        getTime().

                        getTime() - System.currentTimeMillis(), 86400000);

    }


    public void onDisable() {
        KnockBackFFA.Diff.keySet().forEach(a -> Bukkit.getWorld("world").getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
    }
}
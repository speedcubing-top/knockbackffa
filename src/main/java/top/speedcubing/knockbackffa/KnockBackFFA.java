package top.speedcubing.knockbackffa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.knockbackffa.commands.ChangeMapCommand;
import top.speedcubing.knockbackffa.commands.ComboCommand;
import top.speedcubing.knockbackffa.commands.DebugFFACommand;
import top.speedcubing.knockbackffa.commands.InvSeeCommand;
import top.speedcubing.knockbackffa.commands.KillMyselfCommand;
import top.speedcubing.knockbackffa.commands.MapViewCommand;
import top.speedcubing.knockbackffa.commands.ProfileCommand;
import top.speedcubing.knockbackffa.commands.SendMessageCommand;
import top.speedcubing.knockbackffa.commands.SetArmorColor;
import top.speedcubing.knockbackffa.commands.TrollUtilsCommand;
import top.speedcubing.knockbackffa.commands.VoteCommand;
import top.speedcubing.knockbackffa.cosmetics.killeffect.commands.CommandSWKillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.gui.GuiKillEffectSelector;
import top.speedcubing.knockbackffa.cosmetics.killeffect.listeners.KillListener;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.ConfigUtil;
import top.speedcubing.knockbackffa.cosmetics.main.commands.CosmeticCommand;
import top.speedcubing.knockbackffa.cosmetics.main.gui.GuiCosmeticsSelector;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.commands.ProjectileCommand;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.gui.GuiProjectileEffectSelector;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.listeners.ProjectileListener;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.knockbackffa.games.tag.commands.TagGameCommand;
import top.speedcubing.knockbackffa.games.tag.commands.TagGameListCommand;
import top.speedcubing.knockbackffa.games.tag.listeners.TagGameListener;
import top.speedcubing.knockbackffa.trollutillisteners.Darts;
import top.speedcubing.knockbackffa.trollutillisteners.Fireball;
import top.speedcubing.knockbackffa.trollutillisteners.OceanBow;
import top.speedcubing.knockbackffa.trollutillisteners.VerticalStick;
import top.speedcubing.knockbackffa.trollutillisteners.VerticalStickProMax;
import top.speedcubing.knockbackffa.utils.GsonSerializer;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.lib.bukkit.events.entity.ClickEvent;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.server.speedcubingServer;
import top.speedcubing.server.utils.MapManager;

public class KnockBackFFA extends JavaPlugin {
    private KillEffectManager killEffectManager;
    private FileConfiguration dataYml;
    private GuiKillEffectSelector guiKillEffectSelector;
    private GuiProjectileEffectSelector guiProjectileEffectSelector;
    private GuiCosmeticsSelector guiCosmeticsSelector;
    private String nmsVersion;
    private ProjectileEffectManager projectileEffectManager;
    private FileConfiguration projectileEffectDataYml;

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
    public static double centerY = 150;
    public static double centerZ = 0;
    public static double offsetY = 100;
    public static float yaw = 0;
    public static double holoX = 0;
    public static double holoZ = 0;
    public static int spawnprotectY = KnockBackFFA.spawnY - 4;

    public static int spawnY = 150;
    public static String currentMapName = "HOT_AIR_BALLOON";

    static final Consumer<ClickEvent> clickEvent = (r) -> Member.getMember(r.getPlayer()).setDisplayType(false);
    public static final String[] board = new String[10];
    public static final String[] dailyBoard = new String[10];
    public static final String[] boardName = new String[10];
    public static final String[] dailyBoardName = new String[10];
    public static final Hologram[] personal = new Hologram[2];

    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final ItemStack[][] itemStacks = new ItemStack[4][14];
    public static final Inventory[] KitEditor = new Inventory[4];
    public static final InventoryHolder KitEditorHolder = () -> null;
    public static int id = 0;
    public static final Map<BlockPosition, Integer[]> BlockPlaced = new HashMap<>();


    public static void switchMap(double centerX, double centerZ, double offsetY, float yaw, double holoX, double holoZ, double centerY, String mapName) {
        KnockBackFFA.centerX = centerX;
        KnockBackFFA.centerY = centerY == 0 ? 150 : centerY;
        KnockBackFFA.centerZ = centerZ;
        KnockBackFFA.yaw = yaw;
        KnockBackFFA.offsetY = offsetY;
        KnockBackFFA.holoX = holoX;
        KnockBackFFA.holoZ = holoZ;
        personal[0].setLocation(KnockBackFFA.holoX, personal[0].armorStand.locY, KnockBackFFA.holoZ, 0, 0);
        personal[1].setLocation(KnockBackFFA.holoX, personal[1].armorStand.locY, KnockBackFFA.holoZ, 0, 0);
        KnockBackFFA.currentMapName = mapName;
    }

    @Override
    public void onEnable() {

        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "knockbackffa");
        CubingEventManager.registerListeners(new ServerEvent());
        new ConfigReloadEvent().call();
        regitserCommands();
        registerListener();

        loadVersion();
        saveDefaultConfig();
        ConfigUtil.init(this);
        loadDataYml();
        loadProjectileEffectDataYml();

        killEffectManager = new KillEffectManager(this);
        projectileEffectManager = new ProjectileEffectManager(this);
        speedcubingServer.registerListeners(
                new EventListeners(killEffectManager, projectileEffectManager),
                guiKillEffectSelector = new GuiKillEffectSelector(this, killEffectManager),
                guiProjectileEffectSelector = new GuiProjectileEffectSelector(this, projectileEffectManager),
                guiCosmeticsSelector = new GuiCosmeticsSelector(this, killEffectManager, projectileEffectManager)
        );

        try {
            MapManager.install("map_kbffa", "kbffa", Difficulty.EASY, 6000, false);
        } catch (Exception e) {
            Bukkit.getServer().shutdown();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Set<PacketPlayOutBlockBreakAnimation> a = new HashSet<>();
            Set<BlockPosition> ba = new HashSet<>();
            for (Map.Entry<BlockPosition, Integer[]> x : BlockPlaced.entrySet()) {
                int age = x.getValue()[1];
                BlockPosition block = x.getKey();
                a.add(new PacketPlayOutBlockBreakAnimation(x.getValue()[0], block, age));
                if (age == 10) {
                    Bukkit.getWorlds().get(0).getBlockAt(block.getX(), block.getY(), block.getZ()).setType(Material.AIR);
                    ba.add(block);
                }
            }


            Bukkit.getOnlinePlayers().forEach(b -> a.forEach(((CraftPlayer) b).getHandle().playerConnection::sendPacket));
            ba.forEach(BlockPlaced::remove);
            for (Integer[] x : BlockPlaced.values()) {
                x[1] += 1;
            }
        }, 0, 10);

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
            itemStacks[i][4] = new ItemBuilder(Material.GOLD_SWORD).unBreak().ench(Enchantment.DAMAGE_ALL, 1).build();
            itemStacks[i][5] = new ItemBuilder(Material.STICK).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][6] = new ItemBuilder(Material.ENDER_PEARL).build();
            itemStacks[i][7] = new ItemBuilder(Material.WEB).build();
            itemStacks[i][8] = new ItemBuilder(Material.SANDSTONE).amount(64).build();
            itemStacks[i][9] = new ItemBuilder(Material.ARROW).amount(16).build();
            itemStacks[i][10] = new ItemBuilder(Material.BOW).unBreak().ench(new Enchantment[]{Enchantment.ARROW_KNOCKBACK}, new int[]{1}).build();
            itemStacks[i][11] = new ItemBuilder(Material.CHEST).name(inv[i]).build();
            itemStacks[i][12] = new ItemBuilder(Material.EMERALD).name("§aCosmetics").build();
            itemStacks[i][13] = new ItemBuilder(Material.STONE_PICKAXE).unBreak().ench(Enchantment.DIG_SPEED, 1).build();
            KitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
            InventoryUtils.fillItem(KitEditor[i], 0, 9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("§a").build());
            KitEditor[i].setItem(3, new ItemBuilder(Material.INK_SACK).durability(10).name(save[i]).build());
            KitEditor[i].setItem(5, new ItemBuilder(Material.INK_SACK).durability(1).name(unsave[i]).build());
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(display[i]).build();
        }

        personal[0] = new Hologram("", false, false).
                world("kbffa").
                setLocation(KnockBackFFA.holoX, KnockBackFFA.centerY + 2.4, KnockBackFFA.holoZ, 0, 0).
                setClickEvent(clickEvent);

        personal[1] = new Hologram("", false, false).
                world("kbffa").
                setLocation(KnockBackFFA.holoX, KnockBackFFA.centerY - 1.8, KnockBackFFA.holoZ, 0, 0).
                setClickEvent(clickEvent);

        new Hologram("§6§lClick to toggle!", true, true).
                setLocation(holoX, 148.5, holoZ, 0, 0).
                world("kbffa").
                setClickEvent(clickEvent);

        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, 1);
        new Timer("Cubing-Daily-Thread").schedule(new TimerTask() {
            @Override
            public void run() {
                Member.users.values().forEach(a -> a.dailyKill = 0);
                Database.connection.update("playersdata", "kbffa_dailykill=0", "1");
            }
        }, new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
                tomorrow.get(Calendar.DATE), 0, 0).getTime().getTime() - System.currentTimeMillis(), 86400000);

        //Remove block placed
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            String bps = Database.connection.select("value").from("settings").where("name='kbffa_blockplaced'").getString();
            if (bps == null || bps.isEmpty()) {
                System.out.println("BlockPlaced is empty");
                return;
            }
            Map<BlockPosition, Integer[]> bp = GsonSerializer.deserializeObject(bps, new TypeToken<HashMap<BlockPosition, Integer[]>>() {
            }.getType());
            bp.keySet().forEach(a -> Bukkit.getWorld("kbffa").getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        }, 60);
    }

    @Override
    public void onDisable() {
        String blockPlacedSerialize = GsonSerializer.serializeObject(BlockPlaced);
        if (Database.connection.exist("settings", "name='kbffa_blockplaced'")) {
            Database.connection.update("settings", "value='" + blockPlacedSerialize + "'", "name='kbffa_blockplaced'");
            System.out.println("BlockPlaced saved");
        } else {
            Database.connection.insert("settings", "name,value", "'kbffa_blockplaced','" + blockPlacedSerialize + "'");
            System.out.println("BlockPlaced inserted");
        }
        //KnockBackFFA.BlockPlaced.keySet().forEach(a -> Bukkit.getWorld("kbffa").getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        killEffectManager.saveKillEffects();
        projectileEffectManager.saveProjectileEffects();
    }

    private void regitserCommands() {
        getCommand("trollutils").setExecutor(new TrollUtilsCommand());
        getCommand("killeffect").setExecutor(new CommandSWKillEffect(this));
        getCommand("projectileeffect").setExecutor(new ProjectileCommand(this));
        getCommand("cosmetic").setExecutor(new CosmeticCommand(this));
        getCommand("debugffa").setExecutor(new DebugFFACommand());
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("sendmessage").setExecutor(new SendMessageCommand());
        getCommand("killmyself").setExecutor(new KillMyselfCommand());
        getCommand("taggame").setExecutor(new TagGameCommand());
        getCommand("taggamelist").setExecutor(new TagGameListCommand());
        getCommand("invsee").setExecutor(new InvSeeCommand());
        getCommand("mapview").setExecutor(new MapViewCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("changemap").setExecutor(new ChangeMapCommand());
        getCommand("combo").setExecutor(new ComboCommand());
        getCommand("setarmorcolor").setExecutor(new SetArmorColor());

        Bukkit.getPluginCommand("map").setExecutor((commandSender, command, s, strings) -> {
            if (strings[6] == null) strings[6] = "150";
            KnockBackFFA.switchMap(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Float.parseFloat(strings[3]), Double.parseDouble(strings[4]), Double.parseDouble(strings[5]), Double.parseDouble(strings[6]), strings[7]);
            commandSender.sendMessage(strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3]);
            return true;
        });
    }

    private void registerListener() {
        speedcubingServer.registerListeners(
                new OceanBow(),
                new KillListener(this),
                new ProjectileListener(this),
                new Fireball(),
                new VerticalStick(),
                new Darts(),
                new TagGameListener(),
                new VerticalStickProMax(),
                new ComboCommand(),
                new ChangeMapCommand());
    }

    public void loadVersion() {
        nmsVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public void loadDataYml() {
        saveResource("data.yml", false);
        File dataYmlFile = new File(getDataFolder() + "/data.yml");
        if (!dataYmlFile.exists()) {
            try {
                dataYmlFile.createNewFile();
            } catch (IOException e) {
                log("There was an error loading the §cdata.yml");
                e.printStackTrace();
                return;
            }
        }
        dataYml = YamlConfiguration.loadConfiguration(dataYmlFile);
        log("§adata.yml Loaded");
    }

    public void saveDataYml() {
        File dataYmlFile = new File(getDataFolder() + "/data.yml");
        if (dataYmlFile.exists()) {
            try {
                dataYml.save(dataYmlFile);
            } catch (IOException e) {
                log("An error occurred while saving §cdata.yml");
                e.printStackTrace();
            }
        }
        log(" Saved §adata.yml");
    }

    public void loadProjectileEffectDataYml() {
        saveResource("projectileeffectdata.yml", false);
        File dataYmlFile = new File(getDataFolder() + "/projectileeffectdata.yml");
        if (!dataYmlFile.exists()) {
            try {
                dataYmlFile.createNewFile();
            } catch (IOException e) {
                log("There was an error loading the projectileeffectdata.yml");
                e.printStackTrace();
                return;
            }
        }
        projectileEffectDataYml = YamlConfiguration.loadConfiguration(dataYmlFile);
        log("§aprojectileeffectdata.yml Loaded");
    }

    public void saveProjectileEffectDataYml() {
        File dataYmlFile = new File(getDataFolder() + "/projectileeffectdata.yml");
        if (dataYmlFile.exists()) {
            try {
                projectileEffectDataYml.save(dataYmlFile);
            } catch (IOException e) {
                log("An error occurred while saving projectileeffectdata.yml");
                e.printStackTrace();
            }
        }
        log(" Saved §aprojetcileeffectdata.yml");
    }

    /**
     * Here is kill effect code
     */
    public FileConfiguration getDataYml() {
        return dataYml;
    }

    public static void log(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
    }

    public KillEffectManager getKillEffectManager() {
        return killEffectManager;
    }

    public GuiKillEffectSelector getGuiKillEffectSelector() {
        return guiKillEffectSelector;
    }

    public String getNmsVersion() {
        return nmsVersion;
    }

    public FileConfiguration getProjectileEffectDataYml() {
        return projectileEffectDataYml;
    }

    public ProjectileEffectManager getProjectileEffectManager() {
        return projectileEffectManager;
    }

    public GuiProjectileEffectSelector getGuiProjectileEffectSelector() {
        return guiProjectileEffectSelector;
    }

    public GuiCosmeticsSelector getGuiCosmeticsSelector() {
        return guiCosmeticsSelector;
    }
}
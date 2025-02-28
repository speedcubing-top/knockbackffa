package top.speedcubing.knockbackffa.original;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.speedcubing.common.database.Database;
import top.speedcubing.knockbackffa.api.events.FFAPlayerDeathEvent;
import top.speedcubing.knockbackffa.original.configuration.Configuration;
import top.speedcubing.knockbackffa.original.list.EntityList;
import top.speedcubing.knockbackffa.original.list.ItemList;
import top.speedcubing.knockbackffa.original.list.StringList;
import top.speedcubing.lib.bukkit.PlayerUtils;
import top.speedcubing.lib.bukkit.SideBar;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.bukkit.packetwrapper.OutScoreboardScore;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLRow;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.player.User;

public class Member {
    private static final Logger log = LoggerFactory.getLogger(Member.class);
    public static ConcurrentHashMap<Integer, Member> users = new ConcurrentHashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember(User.getUser(sender).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    final User user;
    public Member Damage;
    public long KillTime;
    boolean iseditingkit;
    Set<EnderPearl> pearls = new HashSet<>();
    final PacketPlayOutScoreboardScore maxHealPacket;
    public boolean isFighting;
    public String inventory;
    int kill;
    boolean killedPlayer;
    int dailyKill;
    int death;
    public int killstreak;
    long lastClick;
    boolean displayType;
    final Hologram personal;
    int placedBlock;
    int breakBlock;
    public String armorColor;
    public boolean isUsingTunnel = false;
    //weapon selector
    public int weaponId;

    public final Hologram[] killBoard = new Hologram[10];

    public Member(Player player) {
        this.user = User.getUser(player);
        maxHealPacket = new OutScoreboardScore().a(player.getName()).b(StringList.obj).c(20).d(PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE).packet;
        SQLRow r = user.dbSelect("kbffa_inventory,kbffa_kill,kbffa_death,kbffa_killstreak,kbffa_dailykill,kbffa_armorcolor");
        inventory = r.getString(0);
        kill = r.getInt(1);
        death = r.getInt(2);
        killstreak = r.getInt(3);
        dailyKill = r.getInt(4);
        armorColor = r.getString(5) == null ? "null" : r.getString(5);
        weaponId = user.dbSelect("kbffa_weapon").getInt(0);
        player.teleport(new Location(player.getWorld(), Configuration.centerX + 0.5, Configuration.centerY, Configuration.centerZ + 0.5, Configuration.yaw, 0));
        player.setLevel(0);
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHeldItemSlot(0);
        inventory.setHelmet(new ItemStack(Material.AIR));
        inventory.setChestplate(new ItemStack(Material.AIR));
        inventory.setLeggings(new ItemStack(Material.AIR));
        inventory.setBoots(new ItemStack(Material.AIR));
        inventory.clear();
        setHotbarDefault();
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        String[] sidebar = StringList.sidebar[user.lang];
        SideBar sideBar = new SideBar(player, sidebar[0])
                .staticLine(sidebar[1], 12)
                .staticLine(sidebar[2], 11)
                .staticLine(sidebar[3], 10)
                .changeableLine(sidebar[4] + 0, 9)
                .staticLine(sidebar[5], 8)
                .staticLine(sidebar[6], 7)
                .changeableLine(sidebar[7] + kill, 6)
                .staticLine(sidebar[8], 5)
                .staticLine(sidebar[9], 4)
                .changeableLine(sidebar[10] + death, 3)
                .staticLine(sidebar[11], 2)
                .staticLine(sidebar[12], 1)
                .changeableLine(sidebar[13] + killstreak, 0);
        sideBar.scoreboard.registerNewObjective(StringList.obj, "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        sideBar.create();
        Member.users.values().forEach(a -> {
            if (a.user.bGetHealth() > 19)
                connection.sendPacket(a.maxHealPacket);
        });
        for (int i = 0; i < killBoard.length; i++) {
            killBoard[i] = new Hologram("", false, false).setLocation(Configuration.holoX, 152 - i * 0.3, Configuration.holoZ, 0, 0).world("kbffa").setClickEvent(KnockBackFFA.clickEvent).setListener(player).spawn();
        }
        personal = new Hologram("", false, false).world("kbffa").setLocation(Configuration.holoX, 148.9, Configuration.holoZ, 0, 0).setClickEvent(KnockBackFFA.clickEvent).setListener(player);
        setDisplayType(true);
        users.put(user.id, this);
    }

    public void setHotbarDefault() {
        user.setLangItem(4, ItemList.inventory);
        user.setLangItem(6, ItemList.cosmetics);
    }

    public void setDisplayType(boolean spawn) {
        long l = System.currentTimeMillis();
        if (l - lastClick > 1000) {
            displayType = !displayType;
            a();
            String dailyTitle = Lang.of("%lang_kbffa_daily_title%").getString(user.lang);
            String lifetimetitle = Lang.of("%lang_kbffa_lifetime_title%").getString(user.lang);
            String type = Lang.of("%lang_kbffa_holo_switch%").getString(user.lang);
            String type2 = Lang.of("%lang_kbffa_holo_switch2%").getString(user.lang);
            if (spawn) {
                EntityList.personal[0].setListener(user.player).setName(displayType ? lifetimetitle : dailyTitle).spawn().setListener();
                EntityList.personal[1].setListener(user.player).setName(displayType ? type2 : type).spawn().setListener();
            } else {
                EntityList.personal[0].setListener(user.player).setName(displayType ? lifetimetitle : dailyTitle).setListener();
                EntityList.personal[1].setListener(user.player).setName(displayType ? type2 : type).setListener();
            }
            updatePlayerPos(spawn);
            lastClick = l;
        }
    }

    public void a() {
        String[] s = displayType ? KnockBackFFA.board : KnockBackFFA.dailyBoard;
        String[] s2 = displayType ? KnockBackFFA.boardName : KnockBackFFA.dailyBoardName;
        for (int i = 0; i < 10; i++) {
            killBoard[i].setName(user.id == Integer.parseInt(s2[i]) ? KnockBackFFA.bold(s[i]) : s[i]);
        }
    }

    public void kill() {
        killedPlayer = true;
        kill += 1;
        dailyKill += 1;
    }

    public void death(boolean quit) {
        isFighting = false;
        boolean killed = System.currentTimeMillis() - this.KillTime < 100000 && Damage != null;
        if (user.player.hasPotionEffect(PotionEffectType.ABSORPTION))
            user.player.removePotionEffect(PotionEffectType.ABSORPTION);
        if (user.player.hasPotionEffect(PotionEffectType.REGENERATION))
            user.player.removePotionEffect(PotionEffectType.REGENERATION);
        user.player.setItemOnCursor(new ItemStack(Material.AIR));
        if (user.player.hasPotionEffect(PotionEffectType.JUMP)) user.player.removePotionEffect(PotionEffectType.JUMP);
        if (killed) {
            Player damager = Damage.user.player;

            FFAPlayerDeathEvent ffaPlayerDeathEvent = new FFAPlayerDeathEvent(user.player, damager, user.player.getLocation());
            Bukkit.getServer().getPluginManager().callEvent(ffaPlayerDeathEvent);

            double heal = damager.getHealth() / 2;
            String color = heal > 8.5 ? "§a" : (heal > 6.5 ? "§e" : (heal > 4.5 ? "§6" : "§c"));
            user.sendMessage("%lang_kbffa_kill%", user.getColorName(false), Damage.user.getColorName(false), color + (int) Math.round(heal));
            Damage.user.sendMessage("%lang_kbffa_kill%", user.getColorName(false), Damage.user.getColorName(false), color + (int) Math.round(heal));
            SideBar sideBar = SideBar.getSidebar(damager);
            Damage.user.sound(Sound.SUCCESSFUL_HIT);
            damager.setHealth(20);
            Damage.kill();
            int currentKillStreak = damager.getLevel() + 1;
            sideBar.setLineByScore(StringList.sidebar[Damage.user.lang][4] + currentKillStreak, 9).setLineByScore(StringList.sidebar[Damage.user.lang][7] + Damage.kill, 6);
            damager.setLevel(currentKillStreak);
            Damage.item(true);
            if (currentKillStreak > Damage.killstreak) {
                Damage.killstreak += 1;
                sideBar.setLineByScore(StringList.sidebar[Damage.user.lang][13] + Damage.killstreak, 0);
            }
            death += 1;
        } else if (!quit)
            user.sendMessage("%lang_kbffa_die%");
        for (Member m : users.values()) {
            if (m.Damage == this)
                m.Damage = null;
        }
        if (!quit) {
            SideBar sideBar = SideBar.getSidebar(user.player);
            PlayerInventory inventory = user.bGetInventory();
            Damage = null;
            inventory.clear();
            inventory.setHelmet(new ItemStack(Material.AIR));
            inventory.setChestplate(new ItemStack(Material.AIR));
            inventory.setLeggings(new ItemStack(Material.AIR));
            inventory.setBoots(new ItemStack(Material.AIR));
            setHotbarDefault();
            if (killed)
                sideBar.setLineByScore(StringList.sidebar[user.lang][10] + death, 3);
            sideBar.setLineByScore(StringList.sidebar[user.lang][4] + 0, 9);
            user.bSetLevel(0);
            user.bSetHealth(20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(KnockBackFFA.getPlugin(KnockBackFFA.class), () -> {
                user.bSetHealth(20);
                user.bSetFireTicks(0);
            }, 1);
            PlayerUtils.removeArrows(user.player);
            user.bTeleport(new Location(user.player.getWorld(), Configuration.centerX + 0.5, Configuration.centerY, Configuration.centerZ + 0.5, Configuration.yaw, 0));
            if (pearls != null)
                pearls.forEach(EnderPearl::remove);
        }
    }

    public void updatePlayerPos(boolean spawn) {
        try (SQLConnection connection = Database.getCubing()) {
            personal.setName(
                    (displayType ? kill : dailyKill) == 0 ?
                            Lang.of("%lang_leaderboard_nodata%").getString(user.lang) :
                            KnockBackFFA.bold(StringList.leaderboard.replace("%i%", Integer.toString(
                                            connection.select("COUNT(*)").from("playersdata")
                                                    .where((displayType ? "kbffa_kill > (SELECT kbffa_kill" : "kbffa_dailykill > (SELECT kbffa_dailykill") + " FROM playersdata where id = " + user.id + ")")
                                                    .executeResult().getInt() + 1))
                                    .replace("%j%", user.getPrefixName(false))
                                    .replace("%k%", Integer.toString(displayType ? kill : dailyKill))));
        }
        if (spawn)
            personal.spawn();
    }

    public void giveHotbarItems(boolean editKit) {
        char[] cfg = inventory.toCharArray();
        ItemStack[] stack = ItemList.itemStacks;
        Inventory inventory = user.bGetInventory();
        if (editKit) {
            InventoryUtils.fillItem(inventory, 0, 8, new ItemStack(Material.AIR));
        } else {
            inventory.clear();
        }
        inventory.setItem(cfg[0] - 48, new ItemBuilder(Material.WOOD_SWORD).unBreak().ench(Enchantment.DAMAGE_ALL, 1).build());
        inventory.setItem(cfg[1] - 48, stack[5]);
        inventory.setItem(cfg[2] - 48, stack[6]);
        inventory.setItem(cfg[3] - 48, stack[7]);
        inventory.setItem(cfg[4] - 48, stack[8]);
        inventory.setItem(cfg[5] - 48, stack[9]);
        inventory.setItem(cfg[6] - 48, stack[10]);
        inventory.setItem(cfg[7] - 48, stack[13]);
    }

    public void item(boolean win) {
        user.bCloseInventory();
        char[] cfg = inventory.toCharArray();
        PlayerInventory inventory = user.bGetInventory();
        ItemStack[] stack = ItemList.itemStacks;
        pearls.clear();
        //stack[8] is blocked
        if (win) {
            if (isFighting) {
                inventory.setItem(cfg[3] - 48, stack[7]);
                inventory.setItem(cfg[4] - 48, stack[8]);
                inventory.setItem(cfg[5] - 48, stack[9]);
                if (inventory.contains(Material.ENDER_PEARL))
                    inventory.addItem(stack[6]);
                else
                    inventory.setItem(cfg[2] - 48, stack[6]);
            }
        } else {
            giveHotbarItems(false);
            ItemStack helmet = stack[0].clone();
            ItemStack chestplate = stack[1].clone();
            ItemStack leggings = stack[2].clone();
            ItemStack boots = stack[3].clone();
            LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
            if (armorColor.equals("null") || armorColor.isEmpty())
                armorColor = "160,101,64|160,101,64|160,101,64|160,101,64";
            String[] rgb = armorColor.split("\\|");
            meta.setColor(Color.fromRGB(Integer.parseInt(rgb[0].split(",")[0]), Integer.parseInt(rgb[0].split(",")[1]), Integer.parseInt(rgb[0].split(",")[2])));
            helmet.setItemMeta(meta);
            meta = (LeatherArmorMeta) chestplate.getItemMeta();
            meta.setColor(Color.fromRGB(Integer.parseInt(rgb[1].split(",")[0]), Integer.parseInt(rgb[1].split(",")[1]), Integer.parseInt(rgb[1].split(",")[2])));
            chestplate.setItemMeta(meta);
            meta = (LeatherArmorMeta) leggings.getItemMeta();
            meta.setColor(Color.fromRGB(Integer.parseInt(rgb[2].split(",")[0]), Integer.parseInt(rgb[2].split(",")[1]), Integer.parseInt(rgb[2].split(",")[2])));
            leggings.setItemMeta(meta);
            meta = (LeatherArmorMeta) boots.getItemMeta();
            meta.setColor(Color.fromRGB(Integer.parseInt(rgb[3].split(",")[0]), Integer.parseInt(rgb[3].split(",")[1]), Integer.parseInt(rgb[3].split(",")[2])));
            boots.setItemMeta(meta);
            inventory.setHelmet(helmet);
            inventory.setChestplate(chestplate);
            inventory.setLeggings(leggings);
            inventory.setBoots(boots);
        }
        user.bUpdateInventory();
    }
}

package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import top.speedcubing.knockbackffa.api.events.FFAPlayerDeathEvent;
import top.speedcubing.lib.bukkit.*;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.lib.bukkit.packetwrapper.OutScoreboardScore;
import top.speedcubing.server.database.Database;
import top.speedcubing.server.player.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Member {
    public static ConcurrentHashMap<Integer, Member> users = new ConcurrentHashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember(User.getUser(sender).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    final User user;
    Member Damage;
    long KillTime;
    boolean iseditingkit;
    Set<EnderPearl> pearls = new HashSet<>();
    final PacketPlayOutScoreboardScore maxHealPacket;
    public boolean isFighting;
    public String inventory;
    int kill;
    boolean killedPlayer;
    int dailyKill;
    int death;
    int killstreak;
    long lastClick;
    boolean displayType;
    final Hologram personal;
    int placedBlock;
    int breakBlock;

    public final Hologram[] killBoard = new Hologram[10];

    public Member(Player player) {
        this.user = User.getUser(player);
        maxHealPacket = new OutScoreboardScore().a(player.getName()).b(StringList.obj).c(20).d(PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE).packet;
        String[] s = user.dbSelect("kbffa_inventory,kbffa_kill,kbffa_death,kbffa_killstreak,kbffa_dailykill").getStringArray();
        inventory = s[0];
        kill = Integer.parseInt(s[1]);
        death = Integer.parseInt(s[2]);
        killstreak = Integer.parseInt(s[3]);
        dailyKill = Integer.parseInt(s[4]);
        player.teleport(new Location(player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
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
        inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
        inventory.setItem(6, KnockBackFFA.itemStacks[user.lang][12]);
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
            killBoard[i] = new Hologram("", false, false).setLocation(KnockBackFFA.holoX, 152 - i * 0.3, KnockBackFFA.holoZ, 0, 0).world("world").setClickEvent(KnockBackFFA.clickEvent).setListener(player).spawn();
        }
        personal = new Hologram("", false, false).world("world").setLocation(KnockBackFFA.holoX, 148.9, KnockBackFFA.holoZ, 0, 0).setClickEvent(KnockBackFFA.clickEvent).setListener(player);
        setDisplayType(true);
        users.put(user.id, this);
    }


    public void setDisplayType(boolean spawn) {
        long l = System.currentTimeMillis();
        if (l - lastClick > 1000) {
            displayType = !displayType;
            a();
            if (spawn) {
                KnockBackFFA.personal[0].setListener(user.player).setName(displayType ? StringList.title2.get(user.lang) : StringList.title.get(user.lang)).spawn().setListener();
                KnockBackFFA.personal[1].setListener(user.player).setName(displayType ? StringList.type2.get(user.lang) : StringList.type.get(user.lang)).spawn().setListener();
            } else {
                KnockBackFFA.personal[0].setListener(user.player).setName(displayType ? StringList.title2.get(user.lang) : StringList.title.get(user.lang)).setListener();
                KnockBackFFA.personal[1].setListener(user.player).setName(displayType ? StringList.type2.get(user.lang) : StringList.type.get(user.lang)).setListener();
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
        if (killed) {
            Player damager = Damage.user.player;
            //call death event for kill effect
            FFAPlayerDeathEvent ffaPlayerDeathEvent = new FFAPlayerDeathEvent(user.player,damager ,user.player.getLocation());
            Bukkit.getServer().getPluginManager().callEvent(ffaPlayerDeathEvent);

            double heal = damager.getHealth() / 2;
            String color = heal > 8.5 ? "§a" : (heal > 6.5 ? "§e" : (heal > 4.5 ? "§6" : "§c"));
            user.sendLangMessage(StringList.died, user.getFormatName(false), Damage.user.getFormatName(false), color + (int) Math.round(heal));
            Damage.user.sendLangMessage(StringList.died, user.getFormatName(false), Damage.user.getFormatName(false), color + (int) Math.round(heal));
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
            user.sendLangMessage(StringList.diedYourself);
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
            inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
            inventory.setItem(6, KnockBackFFA.itemStacks[user.lang][12]);
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
            user.bTeleport(new Location(user.player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
            if (pearls != null)
                pearls.forEach(EnderPearl::remove);
        }
    }

    public void updatePlayerPos(boolean spawn) {
        personal.setName(
                (displayType ? kill : dailyKill) == 0 ?
                        StringList.noData.get(user.lang) :
                        KnockBackFFA.bold(
                                StringList.leaderboard.replace("%i%", Integer.toString(
                                                Database.connection.select("COUNT(*)").from("playersdata")
                                                        .where((displayType ? "kbffa_kill > (SELECT kbffa_kill" : "kbffa_dailykill > (SELECT kbffa_dailykill") + " FROM playersdata where id = " + user.id + ")")
                                                        .getInt() + 1))
                                        .replace("%j%", user.getPrefixName(false))
                                        .replace("%k%", Integer.toString(displayType ? kill : dailyKill))));
        if (spawn)
            personal.spawn();
    }

    public void item(boolean win) {
        user.bCloseInventory();
        char[] cfg = inventory.toCharArray();
        PlayerInventory inventory = user.bGetInventory();
        ItemStack[] stack = KnockBackFFA.itemStacks[user.lang];
        pearls.clear();
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
            inventory.setItem(4, new ItemStack(Material.AIR));
            inventory.setItem(cfg[0] - 48, stack[4]);
            inventory.setItem(cfg[1] - 48, stack[5]);
            inventory.setItem(cfg[2] - 48, stack[6]);
            inventory.setItem(cfg[3] - 48, stack[7]);
            inventory.setItem(cfg[4] - 48, stack[8]);
            inventory.setItem(cfg[5] - 48, stack[9]);
            inventory.setItem(cfg[6] - 48, stack[10]);
            inventory.setItem(cfg[7] - 48, stack[13]);
            inventory.setHelmet(stack[0]);
            inventory.setChestplate(stack[1]);
            inventory.setLeggings(stack[2]);
            inventory.setBoots(stack[3]);
        }
        user.bUpdateInventory();
    }
}

package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import top.speedcubing.lib.bukkit.PlayerUtils;
import top.speedcubing.lib.bukkit.SideBar;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.lib.bukkit.packetwrapper.OutScoreboardScore;
import top.speedcubing.server.libs.User;
import top.speedcubing.server.speedcubingServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Member {
    private static final String obj = "§c❤";
    public static Map<Integer, Member> users = new HashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember(User.getUser(sender).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    public final User user;
    public Member Damage;
    public long KillTime;
    public boolean iseditingkit;
    public Set<EnderPearl> pearls = new HashSet<>();
    private final PacketPlayOutScoreboardScore maxHealPacket;
    public boolean isFighting;
    public String inventory;
    public int kill;
    public int death;
    public int killstreak;

    public Hologram hologram1;
    public Hologram hologram2;


    public Member(Player player) {
        this.user = User.getUser(player);
        maxHealPacket = new OutScoreboardScore().a(player.getName()).b(obj).c(20).d(PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE).packet;
        String[] s = speedcubingServer.connection.select("kbffa_inventory,kbffa_kill,kbffa_death,kbffa_killstreak").from("playersdata").where("id=" + user.id).getStringArray();
        inventory = s[0];
        kill = Integer.parseInt(s[1]);
        death = Integer.parseInt(s[2]);
        killstreak = Integer.parseInt(s[3]);
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
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        String[] sidebar = KnockBackFFA.sidebar[user.lang];
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
        String format = speedcubingServer.getFormat(user.rank)[0] + player.getName();
        hologram1 = new Hologram("", false, false).addListener(player).world("world").setLocation(KnockBackFFA.holoX, 150.8, KnockBackFFA.holoZ, 0, 0);
        hologram2 = new Hologram("", false, false).addListener(player).world("world").setLocation(KnockBackFFA.holo2X, 150.8, KnockBackFFA.holo2Z, 0, 0);
        updatePlayerPos();
        hologram1.spawn();
        hologram2.spawn();
        sideBar.scoreboard.registerNewObjective(obj, "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        sideBar.create();
        for (Member m : Member.users.values()) {
            if (m.user.player.getHealth() > 19)
                connection.sendPacket(m.maxHealPacket);
        }
        users.put(user.id, this);
    }

    public void death(boolean quit) {
        isFighting = false;
        boolean killed = false;
        if (System.currentTimeMillis() - this.KillTime < 100000) {
            if (Damage != null) {
                killed = true;
                Player damager = Damage.user.player;
                SideBar sideBar = SideBar.getSidebar(damager);
                damager.playSound(damager.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);
                damager.setHealth(20);
                Damage.kill += 1;
                int currentKillStreak = damager.getLevel() + 1;
                sideBar.setLineByScore(KnockBackFFA.sidebar[Damage.user.lang][4] + currentKillStreak, 9).setLineByScore(KnockBackFFA.sidebar[Damage.user.lang][7] + Damage.kill, 6);
                damager.setLevel(currentKillStreak);
                Damage.item(true);
                if (currentKillStreak > Damage.killstreak) {
                    Damage.killstreak += 1;
                    sideBar.setLineByScore(KnockBackFFA.sidebar[Damage.user.lang][13] + Damage.killstreak, 0);
                }
                String ft = speedcubingServer.getFormat(Damage.user.rank)[1];
                String ft2 = speedcubingServer.getFormat(user.rank)[1];
                users.values().forEach(u -> u.user.player.sendMessage(KnockBackFFA.Kill[u.user.lang].replace("%format0%", ft2).replace("%name0%", user.player.getName()).replace("%format1%", ft).replace("%name1%", Damage.user.player.getName())));
                death += 1;
            }
        }
        if (!quit) {
            SideBar sideBar = SideBar.getSidebar(user.player);
            PlayerInventory inventory = user.player.getInventory();
            Damage = null;
            for (Member m : users.values()) {
                if (m.Damage == this)
                    m.Damage = null;
            }
            inventory.clear();
            inventory.setHelmet(new ItemStack(Material.AIR));
            inventory.setChestplate(new ItemStack(Material.AIR));
            inventory.setLeggings(new ItemStack(Material.AIR));
            inventory.setBoots(new ItemStack(Material.AIR));
            inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
            if (killed)
                sideBar.setLineByScore(KnockBackFFA.sidebar[user.lang][10] + death, 3);
            sideBar.setLineByScore(KnockBackFFA.sidebar[user.lang][4] + 0, 9);
            user.player.setLevel(0);
            user.player.setHealth(20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(KnockBackFFA.getPlugin(KnockBackFFA.class), () -> {
                user.player.setHealth(20);
                user.player.setFireTicks(0);
            }, 1);
            PlayerUtils.removeArrows(user.player);
            user.player.teleport(new Location(user.player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
            user.player.setFireTicks(0);
            if (pearls != null)
                pearls.forEach(EnderPearl::remove);
        }
    }

    public void updatePlayerPos() {
        String format = speedcubingServer.getFormat(user.rank)[0] + user.player.getName();
        int rank1 = speedcubingServer.connection.select("COUNT(*)").from("playersdata").where("kbffa_kill > (SELECT kbffa_kill FROM playersdata where id=" + user.id + ")").getInt() + 1;
        int rank2 = speedcubingServer.connection.select("COUNT(*)").from("playersdata").where("kbffa_killstreak > (SELECT kbffa_killstreak FROM playersdata where id=" + user.id + ")").getInt() + 1;
        hologram1.setName(KnockBackFFA.bold(KnockBackFFA.leaderboard.replace("%i%", Integer.toString(rank1)).replace("%j%", format).replace("%k%", Integer.toString(kill))));
        hologram2.setName(KnockBackFFA.bold(KnockBackFFA.leaderboard.replace("%i%", Integer.toString(rank2)).replace("%j%", format).replace("%k%", Integer.toString(killstreak))));
    }

    public void item(boolean win) {
        user.player.closeInventory();
        char[] cfg = inventory.toCharArray();
        PlayerInventory inventory = user.player.getInventory();
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
            inventory.setHelmet(stack[0]);
            inventory.setChestplate(stack[1]);
            inventory.setLeggings(stack[2]);
            inventory.setBoots(stack[3]);
        }
        user.player.updateInventory();
    }
}

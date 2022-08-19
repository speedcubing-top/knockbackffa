package speedcubing;

import com.google.gson.Gson;
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
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBar;
import speedcubing.lib.bukkit.packetwrapper.OutScoreboardScore;
import speedcubing.lib.utils.SQL.SQLUtils;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Member extends User {
    private static final String obj = "§c❤";
    public static Map<Integer, Member> users = new HashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember((Player) sender);
    }

    public static Member getMember(Player player) {
        return getMember(User.getUser(player).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    public Member Damage;
    public long KillTime;
    public boolean iseditingkit;
    public Set<EnderPearl> pearls = new HashSet<>();
    private final PacketPlayOutScoreboardScore maxHealPacket;
    public boolean isFighting;
    public String inventory;
    public int kill;
    public int death;

    public Member(Player player) {
        super(player.getUniqueId());
        maxHealPacket = new OutScoreboardScore().a(player.getName()).b(obj).c(20).d(PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE).packet;
        String[] s = SQLUtils.getStringArray(speedcubingServer.connection.select("playersdata", "kbffa_inventory,kbffa_kill,kbffa_death", "id=" + id));
        inventory = s[0];
        kill = Integer.parseInt(s[1]);
        death = Integer.parseInt(s[2]);
        player.teleport(new Location(player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
        KnockBackFFA.session.add(this);
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
        inventory.setItem(4, KnockBackFFA.itemStacks[lang][11]);
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        int size = KnockBackFFA.session.size();
        String[] sidebar = KnockBackFFA.sidebar[lang];
        SideBar sideBar = new SideBar(player, sidebar[0])
                .staticLine(sidebar[1], 10);
        for (int j = 0; j < 10; j++) {
            String result;
            if (size <= j) {
                result = "§7-,---";
            } else {
                Member ses = KnockBackFFA.session.get(j);
                int sc = ses.player.getLevel();
                if (sc == 0)
                    result = "§7-,---";
                else {
                    result = speedcubingServer.getFormat(ses.rank)[1] + ses.player.getName() + "§8: §e" + sc;
                }
            }
            sideBar.changeableLine(result, 9 - j);
        }
        sideBar.scoreboard.registerNewObjective(obj, "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        sideBar.create();
        for (Member m : Member.users.values()) {
            if (m.player.getHealth() > 19)
                connection.sendPacket(m.maxHealPacket);
        }
        users.put(id, this);
    }

    public void death(boolean quit) {
        isFighting = false;
        int i = 0;
        if (System.currentTimeMillis() - this.KillTime < 100000) {
            if (Damage != null) {
                Damage.player.playSound(Damage.player.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);
                Damage.player.setHealth(20);
                int a = Damage.player.getLevel();
                for (; i < KnockBackFFA.session.size(); i++) {
                    Member p = KnockBackFFA.session.get(i);
                    if (p.player.getLevel() < a + 1 || p.player.getLevel() == 0) {
                        KnockBackFFA.session.remove(Damage);
                        KnockBackFFA.session.add(i, Damage);
                        break;
                    }
                }
                Damage.kill += 1;
                Damage.item(true);
                Damage.player.setLevel(Damage.player.getLevel() + 1);
                String ft = speedcubingServer.getFormat(Damage.rank)[1];
                String ft2 = speedcubingServer.getFormat(rank)[1];
                users.values().forEach(u -> u.player.sendMessage(KnockBackFFA.Kill[u.lang].replace("%format0%", ft2).replace("%name0%", player.getName()).replace("%format1%", ft).replace("%name1%", Damage.player.getName())));
            }
        }
        if (!quit || Damage != null)
            death += 1;
        if (!quit) {
            Damage = null;
            for (Member m : users.values()) {
                if (m.Damage == this)
                    m.Damage = null;
            }
            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            inventory.setHelmet(new ItemStack(Material.AIR));
            inventory.setChestplate(new ItemStack(Material.AIR));
            inventory.setLeggings(new ItemStack(Material.AIR));
            inventory.setBoots(new ItemStack(Material.AIR));
            inventory.setItem(4, KnockBackFFA.itemStacks[lang][11]);
            player.setLevel(0);
            player.setHealth(20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(KnockBackFFA.getPlugin(KnockBackFFA.class), () -> {
                player.setHealth(20);
                player.setFireTicks(0);
            }, 1);
            PlayerUtils.removeArrows(player);
            player.teleport(new Location(player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
            player.setFireTicks(0);
            if (pearls != null)
                pearls.forEach(EnderPearl::remove);
        }
        KnockBackFFA.session.remove(this);
        KnockBackFFA.session.add(this);
        if (i < 10) {
            List<String> s = KnockBackFFA.getSideBar();
            for (SideBar sideBar : SideBar.getSidebars()) {
                for (int j = 0; j < 10; j++) {
                    sideBar.setLine(s.get(j), j + 2);
                }
            }
        }
    }

    public void item(boolean win) {
        player.closeInventory();
        char[] cfg = inventory.toCharArray();
        PlayerInventory inventory = player.getInventory();
        ItemStack[] stack = KnockBackFFA.itemStacks[lang];
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
        player.updateInventory();
    }
}

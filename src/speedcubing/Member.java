package speedcubing;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBarAPI;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Member {
    public static Map<UUID, Member> users = new HashMap<>();

    public static Member getMember(UUID uuid) {
        return users.get(uuid);
    }

    public User user;
    public Player player;
    public UUID Damage;
    public Long KillTime;
    public boolean iseditingkit;
    public Set<EnderPearl> pearls = new HashSet<>();
    public boolean isFighting;
    public String SQLCache;

    public Member(Player player) {
        UUID uuid = player.getUniqueId();
        user = User.getUser(uuid);
        this.player = player;
        users.put(uuid, this);
        PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0);
        new Thread(() -> SQLCache = speedcubingServer.connection.selectString("playersdata", "kbffa_inventory", "uuid='" + uuid + "'")).start();
        KnockBackFFA.score.put(uuid, 0);
        KnockBackFFA.session.add(uuid);
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
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        int size = KnockBackFFA.session.size();
        String[] sidebar = KnockBackFFA.sidebar[user.lang];
        objective.setDisplayName(sidebar[11]);
        SideBarAPI.sidebarTeams(scoreboard, new String[][]{
                {sidebar[10]},
                {"", sidebar[9], ""},
                {"", sidebar[8], ""},
                {"", sidebar[7], ""},
                {"", sidebar[6], ""},
                {"", sidebar[5], ""},
                {"", sidebar[4], ""},
                {"", sidebar[3], ""},
                {"", sidebar[2], ""},
                {"", sidebar[1], ""},
                {"", sidebar[0], ""},
        }, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0});
        for (int j = 0; j < 10; j++) {
            String prefix, suffix;
            if (size <= j) {
                prefix = "§7-,---";
                suffix = "";
            } else {
                UUID ses = KnockBackFFA.session.get(j);
                int sc = KnockBackFFA.score.get(ses);
                if (sc == 0) {
                    prefix = "§7-,---";
                    suffix = "";
                } else {
                    String[] xx = SideBarAPI.prefixSuffixSplit(speedcubingServer.getFormat(Member.getMember(ses).user.rank)[1] + Bukkit.getPlayer(ses).getName() + "§8: §e" + sc);
                    prefix = xx[0];
                    suffix = xx[1];
                }
            }
            Team t = scoreboard.getTeam("§" + j + "§f");
            t.setPrefix(prefix);
            t.setSuffix(suffix);
        }
        scoreboard.registerNewObjective("§c❤", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
    }

    public void death(Player damager) {
        isFighting = false;
        int i = 0;
        if (KillTime != null) {
            if (System.currentTimeMillis() - this.KillTime < 2500) {
                if (damager == null)
                    damager = Bukkit.getPlayer(Damage);
                if (damager != null) {
                    damager.playSound(damager.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);
                    damager.setHealth(20);
                    UUID uuid = damager.getUniqueId();
                    Member Damager = getMember(uuid);
                    int a = KnockBackFFA.score.get(uuid);
                    for (; i < KnockBackFFA.session.size(); i++) {
                        if (KnockBackFFA.score.get(KnockBackFFA.session.get(i)) < a + 1 || KnockBackFFA.score.get(KnockBackFFA.session.get(i)) == 0) {
                            KnockBackFFA.session.remove(uuid);
                            KnockBackFFA.session.add(i, uuid);
                            break;
                        }
                    }
                    KnockBackFFA.score.put(uuid, a + 1);
                    Damager.item(true);
                    damager.setLevel(a + 1);
                    String ft = speedcubingServer.getFormat(Damager.user.rank)[1];
                    String ft2 = speedcubingServer.getFormat(user.rank)[1];
                    Player finalDamager = damager;
                    users.values().forEach(u -> u.player.sendMessage(KnockBackFFA.Kill[u.user.lang].replace("%format0%", ft2).replace("%name0%", player.getName()).replace("%format1%", ft).replace("%name1%", finalDamager.getName())));
                }
            }
        }
        KnockBackFFA.session.remove(player.getUniqueId());
        KnockBackFFA.session.add(player.getUniqueId());
        KnockBackFFA.score.put(player.getUniqueId(), 0);
        if (i < 10) {
            List<String>[] s = KnockBackFFA.getSideBar();
            for (int j = 0; j < 10; j++) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Team t = p.getScoreboard().getTeam("§" + j + "§f");
                    t.setPrefix(s[0].get(j));
                    t.setSuffix(s[1].get(j));
                }
            }
        }
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(new ItemStack(Material.AIR));
        inventory.setChestplate(new ItemStack(Material.AIR));
        inventory.setLeggings(new ItemStack(Material.AIR));
        inventory.setBoots(new ItemStack(Material.AIR));
        inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
        player.setLevel(0);
        player.setHealth(20);
        PlayerUtils.removeArrows(((CraftPlayer) player).getHandle().getDataWatcher());
        PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(KnockBackFFA.getPlugin(KnockBackFFA.class), () -> player.setFireTicks(0), 1);
        player.setFireTicks(0);
        if (pearls != null)
            pearls.forEach(EnderPearl::remove);
    }

    public void item(boolean win) {
        player.closeInventory();
        char[] cfg = SQLCache.toCharArray();
        PlayerInventory inventory = player.getInventory();
        // blocks
        ItemStack[] stack = KnockBackFFA.itemStacks[user.lang];
        pearls.clear();
        if (win) {
            if (inventory.contains(Material.ENDER_PEARL)) {
                inventory.addItem(stack[6]);
                inventory.setItem(cfg[3] - 48, stack[7]);
                inventory.setItem(cfg[4] - 48, stack[8]);
                inventory.setItem(cfg[5] - 48, stack[9]);
            } else
                inventory.setItem(cfg[2] - 48, stack[6]);
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

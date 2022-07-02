package speedcubing;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBar;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Member extends User {
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

    public int Damage;
    public long KillTime;
    public boolean iseditingkit;
    public Set<EnderPearl> pearls = new HashSet<>();
    public boolean isFighting;
    public String SQLCache;

    public Member(Player player) {
        super(player.getUniqueId());
        users.put(id, this);
        player.teleport(new Location(player.getWorld(), KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, KnockBackFFA.yaw, 0));
        new Thread(() -> SQLCache = speedcubingServer.connection.selectString("playersdata", "kbffa_inventory", "id='" + id + "'")).start();
        KnockBackFFA.score.put(id, 0);
        KnockBackFFA.session.add(id);
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
        int size = KnockBackFFA.session.size();
        String[] sidebar = KnockBackFFA.sidebar[lang];
        SideBar sideBar = new SideBar(player, sidebar[0])
                .staticLine(sidebar[1], 10);
        for (int j = 0; j < 10; j++) {
            String result;
            if (size <= j) {
                result = "§7-,---";
            } else {
                int ses = KnockBackFFA.session.get(j);
                int sc = KnockBackFFA.score.get(ses);
                if (sc == 0)
                    result = "§7-,---";
                else {
                    Member member = Member.getMember(ses);
                    result = speedcubingServer.getFormat(member.rank)[1] + member.player.getName() + "§8: §e" + sc;
                }
            }
            sideBar.changeableLine(result, 9 - j);
        }
        sideBar.scoreboard.registerNewObjective("§c❤", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        sideBar.create();
    }

    public void death(Player damager, boolean quit) {
        isFighting = false;
        int i = 0;
        if (System.currentTimeMillis() - this.KillTime < 100000) {
            Member Damager = Member.getMember(Damage);
            if (damager == null)
                if (Damager != null)
                    damager = Damager.player;
            if (damager != null) {
                damager.playSound(damager.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);
                damager.setHealth(20);
                int a = KnockBackFFA.score.get(Damager.id);
                for (; i < KnockBackFFA.session.size(); i++) {
                    if (KnockBackFFA.score.get(KnockBackFFA.session.get(i)) < a + 1 || KnockBackFFA.score.get(KnockBackFFA.session.get(i)) == 0) {
                        KnockBackFFA.session.remove(Integer.valueOf(Damage));
                        KnockBackFFA.session.add(i, Damage);
                        break;
                    }
                }
                KnockBackFFA.score.put(Damager.id, a + 1);
                Damager.item(true);
                damager.setLevel(a + 1);
                String ft = speedcubingServer.getFormat(Damager.rank)[1];
                String ft2 = speedcubingServer.getFormat(rank)[1];
                Player finalDamager = damager;
                users.values().forEach(u -> u.player.sendMessage(KnockBackFFA.Kill[u.lang].replace("%format0%", ft2).replace("%name0%", player.getName()).replace("%format1%", ft).replace("%name1%", finalDamager.getName())));
            }
        }
        KnockBackFFA.session.remove(Integer.valueOf(id));
        KnockBackFFA.session.add(id);
        KnockBackFFA.score.put(id, 0);
        if (i < 10) {
            List<String> s = KnockBackFFA.getSideBar();
            for (SideBar sideBar : SideBar.getSidebars()) {
                for (int j = 0; j < 10; j++) {
                    sideBar.setLine(s.get(j), j + 2);
                }
            }
        }
        if (!quit) {
            Damage = 0;
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
    }

    public void item(boolean win) {
        player.closeInventory();
        char[] cfg = SQLCache.toCharArray();
        PlayerInventory inventory = player.getInventory();
        // blocks
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

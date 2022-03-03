package speedcubing;

import cubing.bukkit.PlayerUtils;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class User {
    public static Map<UUID, User> users = new HashMap<>();

    public Player player;
    public int Kills;
    public UUID Damage;
    public Long KillTime;
    public boolean iseditingkit;
    public Set<EnderPearl> pearls;
    public boolean isFighting;
    public String SQLCache;
    public int lang;

    public User(Player player) {
        UUID uuid = player.getUniqueId();
        this.player = player;
        lang = PlayerData.getLang(uuid);
        pearls = new HashSet<>();
        Kills = 0;
        users.put(uuid, this);
        PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, 90, 0);
        new Thread(() -> SQLCache = speedcubingServer.connection.selectString("playersdata", "kbffa_inventory", "uuid='" + uuid + "'")).start();
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
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective("§c❤", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
    }

    public void death(Player damager) {
        isFighting = false;
        if (KillTime != null) {
            if (System.currentTimeMillis() - this.KillTime < 2500) {
                if (damager == null)
                    damager = Bukkit.getPlayer(Damage);
                if (damager != null) {
                    damager.playSound(damager.getLocation(), Sound.SUCCESSFUL_HIT, 1F, 1F);
                    damager.setHealth(20);
                    User Damager = User.users.get(damager.getUniqueId());
                    Damager.Kills += 1;
                    Damager.item(true);
                    damager.setLevel(Damager.Kills);
                    String ft = Rank.format(damager.getUniqueId())[0];
                    String ft2 = Rank.format(player.getUniqueId())[0];
                    for (User u : users.values()) {
                        u.player.sendMessage(KnockBackFFA.Kill[u.lang].replace("%format0%", ft2).replace("%name0%", player.getName()).replace("%format1%", ft).replace("%name1%", damager.getName()));
                    }
                }
            }
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
        PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, KnockBackFFA.centerX + 0.5, 150, KnockBackFFA.centerZ + 0.5, 90, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(KnockBackFFA.getPlugin(KnockBackFFA.class), () -> player.setFireTicks(0), 1);
        player.setFireTicks(0);
        if (pearls != null)
            for (EnderPearl pearl : pearls) {
                pearl.remove();
            }
        Kills = 0;
    }

    public void item(boolean win) {
        player.closeInventory();
        char[] cfg = SQLCache.toCharArray();
        PlayerInventory inventory = player.getInventory();
        // blocks
        ItemStack[] stack = KnockBackFFA.itemStacks[lang];
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

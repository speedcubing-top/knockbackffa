package speedcubing;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import speedcubing.lib.bukkit.InventoryUtils;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.listeners.PlayerJoin;
import speedcubing.server.speedcubingServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EventListeners implements Listener {

    @EventHandler
    public void BlocKBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL)
            e.setCancelled(true);
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Block block = e.getBlock();
            Material old = e.getBlockReplacedState().getType();
            if (block.getY() > 144 || Math.abs(block.getZ() - KnockBackFFA.centerZ) > 500 || Math.abs(block.getX() - KnockBackFFA.centerX) > 500 || old.equals(Material.STATIONARY_LAVA) || old.equals(Material.LAVA) || old.equals(Material.STATIONARY_WATER) || old.equals(Material.WATER))
                e.setCancelled(true);
            else {
                KnockBackFFA.Diff.put(new BlockPosition(block.getX(), block.getY(), block.getZ()), new Integer[]{KnockBackFFA.id, 0});
                KnockBackFFA.id++;
            }
        }
    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            Member user = Member.getMember(player.getUniqueId());
            Location to = e.getTo();
            if (to.getY() < 148 && !user.isFighting) {
                user.isFighting = true;
                user.item(false);
            } else if (to.getY() <= KnockBackFFA.offsetY || (Math.abs(to.getX() - KnockBackFFA.centerX - 0.5) > 499.5 || Math.abs(to.getZ() - KnockBackFFA.centerZ - 0.5) > 499.5))
                user.death(null);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (!Member.getMember(entity.getUniqueId()).isFighting || cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            else if ((cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.DROWNING) && ((Player) entity).getHealth() - e.getFinalDamage() < 0)
                Member.getMember(entity.getUniqueId()).death(null);
        }
    }

    @EventHandler
    public void EntityShootBowEvent(EntityShootBowEvent e) {
        if (e.getEntity().getLocation().getY() > 148)
            e.setCancelled(true);
    }

    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Arrow)
            entity.remove();
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Entity damage = e.getDamager();
            Player damager = null;
            if (damage instanceof Arrow)
                damager = (Player) ((Arrow) e.getDamager()).getShooter();
            else if (damage instanceof Player)
                damager = (Player) e.getDamager();
            if (damager != null) {
                UUID victimuuid = victim.getUniqueId();
                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    Member.getMember(victimuuid).death(damager);
                    e.setCancelled(true);
                    victim.damage(0);
                }
                Member user = Member.getMember(victimuuid);
                user.Damage = damage.getUniqueId();
                user.KillTime = System.currentTimeMillis();
            }
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            if (inventory.getName().equals("container.crafting")) {
                if (e.getWhoClicked().getGameMode() == GameMode.SURVIVAL)
                    e.setCancelled(true);
            } else if (inventory.getHolder() == KnockBackFFA.KitEditorHolder) {
                e.setCancelled(true);
                if (e.getAction() != InventoryAction.PICKUP_HALF) {
                    Player player = (Player) e.getWhoClicked();
                    player.updateInventory();
                    switch (RawSlot) {
                        case 3:
                            ItemStack cursor = e.getCursor();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Inventory inv = player.getInventory();
                            if (cursor.getType() == Material.AIR) {
                                UUID uuid = player.getUniqueId();
                                Member user = Member.getMember(uuid);
                                player.sendMessage(GlobalString.SavedSettings[user.user.lang]);
                                List<Material> t = Arrays.asList(Material.WOOD_SWORD, Material.STICK, Material.ENDER_PEARL, Material.WEB, Material.SANDSTONE, Material.ARROW, Material.BOW);
                                String s = "       ";
                                for (int i = 0; i < 9; i++) {
                                    if (inv.getItem(i) != null && t.contains(inv.getItem(i).getType())) {
                                        int index = t.indexOf(inv.getItem(i).getType());
                                        s = s.substring(0, index) + i + s.substring(index + 1);
                                    }
                                }
                                user.SQLCache = s;
                                new Thread(() -> speedcubingServer.connection.update("playersdata", "kbffa_inventory='" + user.SQLCache + "'", "uuid='" + uuid + "'")).start();
                                inv.clear();
                                inv.setItem(4, KnockBackFFA.itemStacks[user.user.lang][11]);
                                user.iseditingkit = false;
                                player.closeInventory();
                            } else {
                                for (int i = 0; i < 9; i++) {
                                    if (inv.getItem(i) == null) {
                                        inv.setItem(i, cursor);
                                        break;
                                    }
                                }
                                player.setItemOnCursor(new ItemStack(Material.AIR));
                            }
                            break;
                        case 5:
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            player.closeInventory();
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == KnockBackFFA.KitEditorHolder) {
            Player player = (Player) e.getPlayer();
            player.setItemOnCursor(new ItemStack(Material.AIR));
            Member user = Member.getMember(player.getUniqueId());
            if (user.iseditingkit) {
                user.iseditingkit = false;
                player.sendMessage(GlobalString.UnSavedSettings[user.user.lang]);
                Inventory inventory = player.getInventory();
                inventory.clear();
                inventory.setItem(4, KnockBackFFA.itemStacks[user.user.lang][11]);
            }
        }
    }


    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().equals(Material.CHEST)) {
            Member user = Member.getMember(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
            Inventory inventory = player.getInventory();
            inventory.setItem(4, new ItemStack(Material.AIR));
            ItemStack[] itemStacks = KnockBackFFA.itemStacks[user.user.lang];
            char[] cfg = user.SQLCache.toCharArray();
            inventory.setItem(cfg[0] - 48, itemStacks[4]);
            inventory.setItem(cfg[1] - 48, itemStacks[5]);
            inventory.setItem(cfg[2] - 48, itemStacks[6]);
            inventory.setItem(cfg[3] - 48, itemStacks[7]);
            inventory.setItem(cfg[4] - 48, itemStacks[8]);
            inventory.setItem(cfg[5] - 48, itemStacks[9]);
            inventory.setItem(cfg[6] - 48, itemStacks[10]);
            user.iseditingkit = true;
            player.openInventory(KnockBackFFA.KitEditor[user.user.lang]);
            InventoryUtils.fillItem(inventory, 9, 36, KnockBackFFA.Invalid[user.user.lang]);
        }
        if (player.getLocation().getY() > 148) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid)) {
            PlayerUtils.removeArrows(((CraftPlayer) player).getHandle().getDataWatcher());
            Member.users.remove(uuid);
            int i = KnockBackFFA.session.indexOf(uuid);
            KnockBackFFA.session.remove(uuid);
            KnockBackFFA.score.remove(uuid);
            if (i != -1 && i < 10) {
                List<String>[] s = KnockBackFFA.getSideBar();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = p.getScoreboard();
                    for (int j = 0; j < 10; j++) {
                        Team t = scoreboard.getTeam("§" + j + "§f");
                        t.setPrefix(s[0].get(j));
                        t.setSuffix(s[1].get(j));
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOnline())
            new Member(player);
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getTo().getY() >= 148)
            e.setCancelled(true);
    }

    @EventHandler
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            Member.getMember(((Player) pearl.getShooter()).getUniqueId()).pearls.add(pearl);
        }
    }
}


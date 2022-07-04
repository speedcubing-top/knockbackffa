package speedcubing;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBar;
import speedcubing.lib.bukkit.inventory.InventoryUtils;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;

import java.util.Arrays;
import java.util.List;

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
            if (block.getY() > KnockBackFFA.spawnprotectY || Math.abs(block.getZ() - KnockBackFFA.centerZ) > 500 || Math.abs(block.getX() - KnockBackFFA.centerX) > 500 || old.equals(Material.STATIONARY_LAVA) || old.equals(Material.LAVA) || old.equals(Material.STATIONARY_WATER) || old.equals(Material.WATER))
                e.setCancelled(true);
            else {
                KnockBackFFA.Diff.put(block, new Integer[]{KnockBackFFA.id, 0});
                KnockBackFFA.id++;
            }
        }
    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            Member user = Member.getMember(player);
            Location to = e.getTo();
            if (to.getY() < KnockBackFFA.spawnprotectY && !user.isFighting) {
                user.isFighting = true;
                user.item(false);
            } else if (to.getY() <= KnockBackFFA.offsetY || (Math.abs(to.getX() - KnockBackFFA.centerX - 0.5) > 499.5 || Math.abs(to.getZ() - KnockBackFFA.centerZ - 0.5) > 499.5))
                user.death(null, false);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            else if ((cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.DROWNING) && ((Player) entity).getHealth() - e.getFinalDamage() < 0)
                Member.getMember(entity).death(null, false);
        }
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
            Entity damage = e.getDamager();
            Player damager = null;
            if (damage instanceof Arrow)
                damager = (Player) ((Arrow) e.getDamager()).getShooter();
            else if (damage instanceof Player)
                damager = (Player) e.getDamager();
            if (damager != null) {
                Player victim = (Player) e.getEntity();
                Member victimMember = Member.getMember(victim);
                Member Damager = Member.getMember(damager);
                if (victimMember.isFighting && Damager.isFighting) {
                    if (victim.getHealth() - e.getFinalDamage() <= 0) {
                        victimMember.death(damager, false);
                        e.setCancelled(true);
                        victim.damage(0);
                    } else if (victim != damager)
                        victimMember.Damage = Damager;
                    victimMember.KillTime = System.currentTimeMillis();
                } else e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent e) {
        if (e.getCursor() != null && e.getCursor().getAmount() != 1)
            e.setCancelled(true);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
        if (e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PLACE_ONE)
            e.setCancelled(true);
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            if (inventory.getName().equals("container.crafting")) {
                if (e.getWhoClicked().getGameMode() == GameMode.SURVIVAL)
                    e.setCancelled(true);
            } else if (inventory.getHolder() == KnockBackFFA.KitEditorHolder) {
                if (RawSlot < 36)
                    e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                switch (RawSlot) {
                    case 3:
                        ItemStack cursor = e.getCursor();
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                        Inventory inv = player.getInventory();
                        if (cursor.getType() == Material.AIR) {
                            Member user = Member.getMember(player);
                            player.sendMessage(GlobalString.SavedSettings[user.lang]);
                            List<Material> t = Arrays.asList(Material.WOOD_SWORD, Material.STICK, Material.ENDER_PEARL, Material.WEB, Material.SANDSTONE, Material.ARROW, Material.BOW);
                            String s = "       ";
                            for (int i = 0; i < 9; i++) {
                                if (inv.getItem(i) != null && t.contains(inv.getItem(i).getType())) {
                                    int index = t.indexOf(inv.getItem(i).getType());
                                    s = s.substring(0, index) + i + s.substring(index + 1);
                                }
                            }
                            user.SQLCache = s;
                            new Thread(() -> speedcubingServer.connection.update("playersdata", "kbffa_inventory='" + user.SQLCache + "'", "id=" + user.id)).start();
                            inv.clear();
                            inv.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
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

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getInventory().getHolder().equals(KnockBackFFA.KitEditorHolder)) {
            Player player = (Player) e.getPlayer();
            player.setItemOnCursor(new ItemStack(Material.AIR));
            Member user = Member.getMember(player);
            if (user.iseditingkit) {
                user.iseditingkit = false;
                player.sendMessage(GlobalString.UnSavedSettings[user.lang]);
                Inventory inventory = player.getInventory();
                inventory.clear();
                inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
            }
        }
    }


    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().equals(Material.CHEST)) {
            Member member = Member.getMember(player);
            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
            Inventory inventory = player.getInventory();
            inventory.setItem(4, new ItemStack(Material.AIR));
            ItemStack[] itemStacks = KnockBackFFA.itemStacks[member.lang];
            char[] cfg = member.SQLCache.toCharArray();
            inventory.setItem(cfg[0] - 48, itemStacks[4]);
            inventory.setItem(cfg[1] - 48, itemStacks[5]);
            inventory.setItem(cfg[2] - 48, itemStacks[6]);
            inventory.setItem(cfg[3] - 48, itemStacks[7]);
            inventory.setItem(cfg[4] - 48, itemStacks[8]);
            inventory.setItem(cfg[5] - 48, itemStacks[9]);
            inventory.setItem(cfg[6] - 48, itemStacks[10]);
            member.iseditingkit = true;
            player.openInventory(KnockBackFFA.KitEditor[member.lang]);
            InventoryUtils.fillItem(inventory, 9, 36, KnockBackFFA.Invalid[member.lang]);
        }
        if (player.getLocation().getY() > KnockBackFFA.spawnprotectY) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtils.removeArrows(player);
        Member member = Member.getMember(player);
        member.death(null, true);
        Member.users.remove(member.id);
        int i = KnockBackFFA.session.indexOf(member);
        KnockBackFFA.session.remove(member);
        KnockBackFFA.score.remove(member);
        if (i != -1 && i < 10) {
            List<String> s = KnockBackFFA.getSideBar();
            for (SideBar sideBar : SideBar.getSidebars()) {
                for (int j = 0; j < 10; j++) {
                    sideBar.setLine(s.get(j), j + 2);
                }
            }
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        new Member(e.getPlayer());
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
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getTo().getY() >= KnockBackFFA.spawnprotectY)
            e.setCancelled(true);
    }

    @EventHandler
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            Member.getMember((Player) pearl.getShooter()).pearls.add(pearl);
        }
    }
}


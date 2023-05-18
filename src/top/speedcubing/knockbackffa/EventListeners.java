package top.speedcubing.knockbackffa;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import top.speedcubing.lib.bukkit.PlayerUtils;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.server.libs.GlobalString;

import java.util.*;

public class EventListeners implements Listener {

    //BOW BOOST PLUGIN
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity livingEntity = e.getEntity();
        Vector dir = livingEntity.getLocation().getDirection();
        Arrow a = (Arrow) e.getProjectile();
        double speed = a.getVelocity().length();
        Vector vel = dir.multiply(speed);
        a.setVelocity(vel);
    }

    @EventHandler
    public void onVelo(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        EntityDamageEvent event = p.getLastDamageCause();
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Arrow) {
                if (((Arrow) damager).getShooter().equals(p)) {
                    Vector velo = e.getVelocity();
                    double speed = Math.sqrt(velo.getX() * velo.getX() + velo.getZ() * velo.getZ());
                    Vector dir = damager.getLocation().getDirection().normalize();
                    Vector newvelo = new Vector(dir.getX() * speed * -1.0D, velo.getY(), dir.getZ() * speed);
                    e.setVelocity(newvelo);
                }
            }
        }
    }

    @EventHandler
    public void BlocKBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL)
            e.setCancelled(true);
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Member member = Member.getMember(e.getPlayer());
            member.placedblock++;
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
                user.death(false);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                if (!Member.getMember(entity).isFighting)
                    e.setCancelled(true);
            } else if ((cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA) && ((Player) entity).getHealth() - e.getFinalDamage() < 0)
                Member.getMember(entity).death(false);
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
        if (e.getEntity() instanceof Player && e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            Entity damage = e.getDamager();
            Player damager = null;
            if (damage instanceof EnderPearl)
                damager = (Player) ((EnderPearl) e.getDamager()).getShooter();
            else if (damage instanceof Arrow)
                damager = (Player) ((Arrow) e.getDamager()).getShooter();
            else if (damage instanceof Player)
                damager = (Player) damage;
            if (damager != null) {
                Player victim = (Player) e.getEntity();
                Member victimMember = Member.getMember(victim);
                Member Damager = Member.getMember(damager);
                if (victimMember.isFighting && Damager.isFighting) {
                    if (victim != damager)
                        victimMember.Damage = Damager;
                    if (victim.getHealth() - e.getFinalDamage() <= 0) {
                        victimMember.death(false);
                        e.setCancelled(true);
                        victim.damage(0);
                    }
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
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        Inventory inv = player.getInventory();
                        if (cursor.getType() == Material.AIR) {
                            player.sendMessage(GlobalString.SavedSettings[member.user.lang]);
                            List<Material> t = Arrays.asList(Material.WOOD_SWORD, Material.STICK, Material.ENDER_PEARL, Material.WEB, Material.SANDSTONE, Material.ARROW, Material.BOW);
                            for (int i = 0; i < 9; i++) {
                                if (inv.getItem(i) != null && t.contains(inv.getItem(i).getType())) {
                                    int index = t.indexOf(inv.getItem(i).getType());
                                    member.inventory = member.inventory.substring(0, index) + i + member.inventory.substring(index + 1);
                                }
                            }
                            inv.clear();
                            inv.setItem(4, KnockBackFFA.itemStacks[member.user.lang][11]);
                            member.iseditingkit = false;
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
            Member member = Member.getMember(player);
            if (member.iseditingkit) {
                member.iseditingkit = false;
                player.sendMessage(GlobalString.UnSavedSettings[member.user.lang]);
                Inventory inventory = player.getInventory();
                inventory.clear();
                inventory.setItem(4, KnockBackFFA.itemStacks[member.user.lang][11]);
            }
        }
    }


    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().equals(Material.CHEST)) {
            Member member = Member.getMember(player);
            member.user.sound(Sound.CLICK);
            Inventory inventory = player.getInventory();
            inventory.setItem(4, new ItemStack(Material.AIR));
            ItemStack[] itemStacks = KnockBackFFA.itemStacks[member.user.lang];
            char[] cfg = member.inventory.toCharArray();
            inventory.setItem(cfg[0] - 48, itemStacks[4]);
            inventory.setItem(cfg[1] - 48, itemStacks[5]);
            inventory.setItem(cfg[2] - 48, itemStacks[6]);
            inventory.setItem(cfg[3] - 48, itemStacks[7]);
            inventory.setItem(cfg[4] - 48, itemStacks[8]);
            inventory.setItem(cfg[5] - 48, itemStacks[9]);
            inventory.setItem(cfg[6] - 48, itemStacks[10]);
            member.iseditingkit = true;
            player.openInventory(KnockBackFFA.KitEditor[member.user.lang]);
            InventoryUtils.fillItem(inventory, 9, 36, KnockBackFFA.Invalid[member.user.lang]);
        }
        if (e.getClickedBlock().getType() == Material.CHEST && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (player.getLocation().getY() > KnockBackFFA.spawnprotectY && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtils.removeArrows(player);
        Member member = Member.getMember(player);
        member.death(true);
        member.user.dbUpdate("kbffa_inventory='" + member.inventory + "',kbffa_kill=" + member.kill + ",kbffa_dailykill=" + member.dailyKill + ",kbffa_death=" + member.death + ",kbffa_killstreak=" + member.killstreak + ",kbffa_blockplace=kbffa_blockplace+" + member.placedblock);
        Member.users.remove(member.user.id);
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


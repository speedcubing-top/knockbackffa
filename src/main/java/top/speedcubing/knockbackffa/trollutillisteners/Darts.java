package top.speedcubing.knockbackffa.trollutillisteners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.Member;
import top.speedcubing.server.speedcubingServer;

import java.util.HashMap;
import java.util.UUID;

public class Darts implements Listener {
    private HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (cooldowns.containsKey(playerId)) {
            long lastUse = cooldowns.get(playerId);
            if (System.currentTimeMillis() - lastUse < 500) {
                return;
            }
        }

        if (event.getItem() != null && player.getItemInHand().getType() == Material.NETHER_STAR && event.getAction() == Action.RIGHT_CLICK_AIR) {
            shootNetherStar(player);
            player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
            if (player.getItemInHand().getAmount() == 1) {
                player.setItemInHand(new ItemStack(Material.AIR));
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
            cooldowns.put(playerId, System.currentTimeMillis());
        }
    }

    private void shootNetherStar(Player player) {
        Location spawnLocation = player.getEyeLocation().subtract(0, 1, 0);
        ArmorStand armorStand = player.getWorld().spawn(spawnLocation, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setArms(true);
        armorStand.setRightArmPose(armorStand.getRightArmPose().setX(0).setY(0).setZ(330));
        armorStand.setItemInHand(new ItemStack(Material.NETHER_STAR));

        Vector direction = player.getEyeLocation().getDirection().normalize().multiply(1.2);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand.isDead()) {
                    armorStand.remove();
                    cancel();
                    return;
                }
                Location loc = armorStand.getLocation();
                armorStand.teleport(loc.add(direction));

                if (armorStand.getLocation().distanceSquared(player.getLocation()) > 256) {
                    armorStand.remove();
                    cancel();
                    return;
                }

                if (armorStand.getLocation().getBlock().getType().isSolid()) {
                    armorStand.remove();
                    cancel();
                    return;
                }
                armorStand.getNearbyEntities(0.85, 1, 0.85).forEach(a -> {
                    if (a instanceof Player) {
                        Player target = (Player) a;
                        if (target != player && target.getGameMode() == GameMode.SURVIVAL) {
                            Member victim = Member.getMember(target);
                            Member damager = Member.getMember(player);
                            if (victim.isFighting && damager.isFighting) {
                                victim.Damage = damager;
                                if (player.getHealth() - 1 <= 0) {
                                    victim.death(false);
                                    player.damage(0);
                                } else {
                                    target.damage(1);
                                    target.setVelocity(direction.multiply(0.14).setY(0.475));
                                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
                                }
                                victim.KillTime = System.currentTimeMillis();
                            }
                            armorStand.remove();
                            cancel();
                        }
                    }

                });
            }
        }.runTaskTimer(speedcubingServer.getInstance(), 1L, 1L);
    }
}

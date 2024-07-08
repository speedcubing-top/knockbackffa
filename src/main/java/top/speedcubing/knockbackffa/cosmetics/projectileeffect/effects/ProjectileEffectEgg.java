package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.server.speedcubingServer;

public class ProjectileEffectEgg extends ProjectileEffect {
    public ProjectileEffectEgg() {
        super("Egg Particle", "EGG_PARTICLE", Material.EGG);
    }
    private BukkitRunnable particleTask;

    @Override
    public void play(Projectile projectile) {
        particleTask = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;

                if (ticks >= 50) {
                    cancel();
                    return;
                }

                Location loc = projectile.getLocation();
                World world = projectile.getWorld();
                playEggParticles(world, loc);
                if (projectile.isDead() || projectile.isOnGround()) {
                    cancel();
                }
            }
        };

        particleTask.runTaskTimer(speedcubingServer.getInstance(), 0L, 1L);
    }

    private void playEggParticles(World world, Location location) {
        ItemStack EGG = new ItemStack(Material.EGG);
        Item drop = world.dropItem(location, EGG);
        drop.setPickupDelay(32768);
        new BukkitRunnable() {
            @Override
            public void run() {
                drop.remove();
            }
        }.runTaskLater(speedcubingServer.getInstance(), 10);
    }
}

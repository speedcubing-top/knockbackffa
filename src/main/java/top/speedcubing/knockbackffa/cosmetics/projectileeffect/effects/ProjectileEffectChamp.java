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

public class ProjectileEffectChamp extends ProjectileEffect {
    public ProjectileEffectChamp() {
        super("Champ", "CHAMP", Material.GOLD_HELMET);
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
                playChampParticles(world, loc);
                if (projectile.isDead() || projectile.isOnGround()) {
                    cancel();
                }
            }
        };

        particleTask.runTaskTimer(speedcubingServer.getInstance(), 0L, 2L);
    }

    private void playChampParticles(World world, Location location) {
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
        Item drop = world.dropItem(location, helmet);
        Item drop2 = world.dropItem(location, boots);
        drop.setPickupDelay(32768);
        drop2.setPickupDelay(32768);
        new BukkitRunnable() {
            @Override
            public void run() {
                drop.remove();
                drop2.remove();
            }
        }.runTaskLater(speedcubingServer.getInstance(), 14);
    }

}

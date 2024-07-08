package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.server.speedcubingServer;

public class ProjectileEffectHeart extends ProjectileEffect {

    public ProjectileEffectHeart() {
        super("Heart Particle", "HEART_PARTICLE", Material.GOLDEN_APPLE);
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
                playHeartParticles(loc);
                if (projectile.isDead() || projectile.isOnGround()) {
                    cancel();
                }
            }
        };

        particleTask.runTaskTimer(speedcubingServer.getInstance(), 0L, 2L);
    }

    private void playHeartParticles(Location location) {
        Bukkit.getWorld(location.getWorld().getName()).playEffect(location, Effect.HEART,2);
    }

}

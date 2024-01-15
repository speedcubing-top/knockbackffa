package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;

public class ProjectileEffectHeart extends ProjectileEffect implements Listener {
    private KnockBackFFA plugin;

    public ProjectileEffectHeart(KnockBackFFA plugin) {
        super("Heart Particle", "HEART_PARTICLE", Material.GOLDEN_APPLE);
        this.plugin = plugin;
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
                if (projectile.isDead()) {
                    cancel();
                }
            }
        };

        particleTask.runTaskTimer(KnockBackFFA.getInstance(), 0L, 2L);
    }

    private void playHeartParticles(Location location) {
        Bukkit.getWorld(location.getWorld().getName()).playEffect(location, Effect.HEART,2);
    }

}

package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.server.speedcubingServer;

public class ProjectileEffectNote extends ProjectileEffect {
    private final Random random = new Random();
    private BukkitRunnable particleTask;

    public ProjectileEffectNote() {
        super("Note Particle", "NOTE_PARTICLE", Material.NOTE_BLOCK);
    }

    @Override
    public void play(Projectile projectile) {
        particleTask = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;

                if (ticks >= 50 || projectile.isDead()) {
                    cancel();
                    return;
                }

                Location loc = projectile.getLocation();
                playNoteParticles(loc);
                if (projectile.isDead() || projectile.isOnGround()) {
                    cancel();
                }
            }
        };

        particleTask.runTaskTimer(speedcubingServer.getInstance(), 0L, 2L);
    }

    private void playNoteParticles(Location location) {
        Bukkit.getWorld(location.getWorld().getName()).playEffect(location, Effect.NOTE,2);
    }
}

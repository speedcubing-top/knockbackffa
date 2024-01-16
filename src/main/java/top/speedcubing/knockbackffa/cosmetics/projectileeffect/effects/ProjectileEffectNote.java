package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;

import java.util.Random;

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
            }
        };

        particleTask.runTaskTimer(KnockBackFFA.getInstance(), 0L, 2L);
    }

    private void playNoteParticles(Location location) {
        Bukkit.getWorld(location.getWorld().getName()).playEffect(location, Effect.NOTE,2);
    }
}

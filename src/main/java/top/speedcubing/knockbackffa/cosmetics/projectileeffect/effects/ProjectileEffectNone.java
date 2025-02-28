package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Material;
import org.bukkit.entity.Projectile;

public class ProjectileEffectNone extends ProjectileEffect {

    public ProjectileEffectNone() {
        super("NONE", "NONE", Material.BARRIER);
    }

    @Override
    public void play(Projectile projectile) {

    }
}

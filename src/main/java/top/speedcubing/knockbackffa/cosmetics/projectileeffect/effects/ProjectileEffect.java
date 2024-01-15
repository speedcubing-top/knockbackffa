package top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects;

import org.bukkit.Material;
import org.bukkit.entity.Projectile;

public abstract class ProjectileEffect {
    protected String name;
    protected String id;
    protected Material icon;
    public ProjectileEffect(String name, String id, Material icon) {
        this.name = name;
        this.id = id;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Material getIcon() {
        return icon;
    }
    public abstract void play(Projectile projectile);
}

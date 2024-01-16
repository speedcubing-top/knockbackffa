package top.speedcubing.knockbackffa.cosmetics.projectileeffect.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;

public class ProjectileListener implements Listener {
    private KnockBackFFA plugin;

    public ProjectileListener(KnockBackFFA plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Player p = (Player) e.getEntity().getShooter();
        Projectile projectile = e.getEntity();
        ProjectileEffectManager projectileEffectManager = plugin.getProjectileEffectManager();
        ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(p);
        if (projectile instanceof EnderPearl || projectile instanceof Fireball || projectile instanceof Arrow || projectile instanceof Egg || projectile instanceof Snowball || projectile instanceof FishHook) {
            if (projectileEffect != null) {
                projectileEffect.play(projectile);
            }
        }
    }
}

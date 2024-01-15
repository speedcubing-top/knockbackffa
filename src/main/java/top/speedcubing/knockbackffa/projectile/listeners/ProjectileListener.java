package top.speedcubing.knockbackffa.projectile.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileListener implements Listener {
    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Player p = (Player) e.getEntity();
        Projectile projectile = e.getEntity();
        if (projectile instanceof EnderPearl || projectile instanceof Fireball || projectile instanceof Arrow) {
            //play effect
        }
    }
}

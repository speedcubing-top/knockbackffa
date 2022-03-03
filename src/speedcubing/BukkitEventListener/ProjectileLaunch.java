package speedcubing.BukkitEventListener;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import speedcubing.KnockBackFFA;
import speedcubing.User;

public class ProjectileLaunch implements Listener {

    @EventHandler
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            User.users.get(((Player) pearl.getShooter()).getUniqueId()).pearls.add(pearl);
        }
    }
}

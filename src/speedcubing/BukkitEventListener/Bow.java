package speedcubing.BukkitEventListener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Bow implements Listener {
    @EventHandler
    public void EntityShootBowEvent(EntityShootBowEvent e) {
        if (e.getEntity().getLocation().getY() > 148)
            e.setCancelled(true);
    }

    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Arrow)
            entity.remove();
    }
}

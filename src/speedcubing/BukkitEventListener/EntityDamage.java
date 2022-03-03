package speedcubing.BukkitEventListener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import speedcubing.User;

public class EntityDamage implements Listener {
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (!User.users.get(entity.getUniqueId()).isFighting || cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            else if ((cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.DROWNING) && ((Player) entity).getHealth() - e.getFinalDamage() < 0)
                User.users.get(entity.getUniqueId()).death(null);
        }
    }
}

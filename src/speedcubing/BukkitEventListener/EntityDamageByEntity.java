package speedcubing.BukkitEventListener;


import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.util.UUID;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Entity damage = e.getDamager();
            Player damager = null;
            if (damage instanceof Arrow)
                damager = (Player) ((Arrow) e.getDamager()).getShooter();
            else if (damage instanceof Player)
                damager = (Player) e.getDamager();
            if (damager != null) {
                UUID victimuuid = victim.getUniqueId();
                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    User.users.get(victimuuid).death(damager);
                    e.setCancelled(true);
                    victim.damage(0);
                }
                User user = User.users.get(victimuuid);
                user.Damage = damage.getUniqueId();
                user.KillTime = System.currentTimeMillis();
            }
        }
    }
}
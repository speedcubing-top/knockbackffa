package top.speedcubing.knockbackffa.trollutilListeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class VerticalStick implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player player = (Player) e.getEntity();
            if (damager.getItemInHand() != null) {
                if (damager.getItemInHand().hasItemMeta()) {
                    if (damager.getItemInHand().getItemMeta().getDisplayName().contains("VerticalStick")) {
                        Location loc = player.getLocation();
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        player.teleport(new Location(player.getWorld(), x, y - 10, z));
                    }
                }
            }
        }
    }
}

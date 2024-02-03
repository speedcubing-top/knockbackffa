package top.speedcubing.knockbackffa.trollutillisteners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import top.speedcubing.knockbackffa.Member;

public class VerticalStick implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Member member = Member.getMember(damager);
            Player player = (Player) e.getEntity();
            if (damager.getItemInHand() == null) {
                return;
            }
            if (damager.getItemInHand().getType() == Material.STICK) {
                if (damager.getItemInHand().hasItemMeta()) {
                    if (damager.getItemInHand().getItemMeta().getDisplayName() != null && damager.getItemInHand().getItemMeta().getDisplayName().equals("§cVerticalStick")) {
                        if (!member.isFighting) {
                            return;
                        }
                        Location loc = player.getLocation();
                        player.teleport(new Location(player.getWorld(),loc.getX(),loc.getY() - 15,loc.getZ()));
                    }
                }
            }
        }
    }
}

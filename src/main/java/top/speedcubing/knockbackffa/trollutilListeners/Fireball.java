package top.speedcubing.knockbackffa.trollutilListeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.KnockBackFFA;

public class Fireball implements Listener {
    @EventHandler
    public void onFireball(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack item = p.getItemInHand();
            if (item != null && item.getType() == Material.FIREBALL) {
                e.setCancelled(true);

                if (!p.hasMetadata("fireballCooldown")) {
                    org.bukkit.entity.Fireball fireball = p.launchProjectile(org.bukkit.entity.Fireball.class);

                    Vector direction = p.getLocation().getDirection().normalize().multiply(0.8);
                    fireball.setVelocity(direction);
                    item.setAmount(item.getAmount() - 1);
                    p.setItemInHand(item);
                    p.setMetadata("fireballCooldown", new FixedMetadataValue(KnockBackFFA.getInstance(), true));
                    Bukkit.getScheduler().runTaskLater(KnockBackFFA.getInstance(), () -> p.removeMetadata("fireballCooldown", KnockBackFFA.getInstance()), 18);
                }
            }
        }
    }
}

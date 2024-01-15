package top.speedcubing.knockbackffa.trollutilListeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.KnockBackFFA;

public class OceanBow implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || event.getAction() == Action.PHYSICAL) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (item.getType().equals(Material.BOW) && item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("OceanBow")) {

                if (player.hasMetadata("oceanBowCooldown")) {
                    return;
                }

                shootArrows(player);


                player.setMetadata("oceanBowCooldown", new FixedMetadataValue(KnockBackFFA.getInstance(), true));
                Bukkit.getScheduler().runTaskLater(KnockBackFFA.getInstance(), () -> player.removeMetadata("oceanBowCooldown", KnockBackFFA.getInstance()), 6);
            }
        }
    }

    private void shootArrows(Player player) {
        Vector playerDirection = player.getLocation().getDirection().normalize();

        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(-i * 5.0);

            double x = playerDirection.getX();
            double z = playerDirection.getZ();

            double newX = x * Math.cos(angle) - z * Math.sin(angle);
            double newZ = x * Math.sin(angle) + z * Math.cos(angle);

            Vector arrowDirection = new Vector(newX, playerDirection.getY(), newZ).normalize();
            Arrow arrow = player.launchProjectile(Arrow.class, arrowDirection);

            arrow.setVelocity(arrowDirection.multiply(2.0));
        }
    }


}

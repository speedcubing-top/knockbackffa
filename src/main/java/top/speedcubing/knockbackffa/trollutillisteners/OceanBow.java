package top.speedcubing.knockbackffa.trollutillisteners;

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
import top.speedcubing.server.speedcubingServer;

public class OceanBow implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();
        if (item == null || event.getAction() == Action.PHYSICAL) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (item.getType() == Material.BOW) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equals("§0OceanBow")) {

                        if (player.hasMetadata("oceanBowCooldown")) {
                            return;
                        }

                        shootArrows(player);


                        player.setMetadata("oceanBowCooldown", new FixedMetadataValue(speedcubingServer.getInstance(), true));
                        Bukkit.getScheduler().runTaskLater(speedcubingServer.getInstance(), () -> player.removeMetadata("oceanBowCooldown", speedcubingServer.getInstance()), 2);
                    }
                }
            }
        }
    }

    private void shootArrows(Player player) {
        Vector playerDirection = player.getLocation().getDirection().normalize();

        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(-i * 7.0);

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

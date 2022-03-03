package speedcubing.BukkitEventListener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }
}
package speedcubing.BukkitEventListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {
    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getTo().getY() >= 150)
            e.setCancelled(true);
    }
}

package speedcubing.BukkitEventListener;

import cubing.bukkit.PlayerUtils;
import cubingserver.things.events.PlayerJoin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.User;

import java.util.UUID;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid)) {
            PlayerUtils.removeArrows(((CraftPlayer) player).getHandle().getDataWatcher());
            User.users.remove(uuid);
        }
    }
}
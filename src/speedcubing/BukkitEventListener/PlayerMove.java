package speedcubing.BukkitEventListener;


import cubing.bukkit.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.util.UUID;

public class PlayerMove implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            User user = User.users.get(player.getUniqueId());
            Location to = e.getTo();
            if (to.getY() < 147 && !user.isFighting) {
                user.isFighting = true;
                user.item(false);
            } else if (to.getY() <= 100 || (Math.abs(to.getX() - KnockBackFFA.centerX - 0.5) > 499.5 || Math.abs(to.getZ() - KnockBackFFA.centerZ - 0.5) > 499.5))
                user.death(null);
        }
    }
}



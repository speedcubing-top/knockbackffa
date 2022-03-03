package speedcubing.BukkitEventListener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void BlocKBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL)
            e.setCancelled(true);
    }
}


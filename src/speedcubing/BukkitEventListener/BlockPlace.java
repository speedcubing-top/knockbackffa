package speedcubing.BukkitEventListener;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import speedcubing.KnockBackFFA;

public class BlockPlace implements Listener {

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Block block = e.getBlock();
            Material old = e.getBlockReplacedState().getType();
            if (block.getY() > 144 || Math.abs(block.getZ() - KnockBackFFA.centerZ) > 500 || Math.abs(block.getX() - KnockBackFFA.centerX) > 500 || old.equals(Material.STATIONARY_LAVA) || old.equals(Material.LAVA) || old.equals(Material.STATIONARY_WATER) || old.equals(Material.WATER))
                e.setCancelled(true);
            else {
                KnockBackFFA.Diff.put(new BlockPosition(block.getX(), block.getY(), block.getZ()), new Integer[]{KnockBackFFA.id, 0});
                KnockBackFFA.id++;
            }
        }
    }
}

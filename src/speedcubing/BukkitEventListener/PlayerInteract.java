package speedcubing.BukkitEventListener;

import cubing.bukkit.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.util.UUID;

public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().equals(Material.CHEST)) {
                User user = User.users.get(player.getUniqueId());
                player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                Inventory inventory = player.getInventory();
                inventory.setItem(4, new ItemStack(Material.AIR));
                ItemStack[] itemStacks = KnockBackFFA.itemStacks[user.lang];
                char[] cfg = user.SQLCache.toCharArray();
                inventory.setItem(cfg[0] - 48, itemStacks[4]);
                inventory.setItem(cfg[1] - 48, itemStacks[5]);
                inventory.setItem(cfg[2] - 48, itemStacks[6]);
                inventory.setItem(cfg[3] - 48, itemStacks[7]);
                inventory.setItem(cfg[4] - 48, itemStacks[8]);
                inventory.setItem(cfg[5] - 48, itemStacks[9]);
                inventory.setItem(cfg[6] - 48, itemStacks[10]);
                user.iseditingkit = true;
                player.openInventory(KnockBackFFA.KitEditor[user.lang]);
                InventoryUtils.fillItem(inventory, 9, 36, KnockBackFFA.Invalid[user.lang]);
        }
        if (player.getLocation().getY() > 148) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }
}
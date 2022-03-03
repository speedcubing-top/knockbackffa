package speedcubing.BukkitEventListener;


import cubingserver.StringList.GlobalString;
import cubingserver.libs.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.util.UUID;

public class InventoryClose implements Listener {
    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == KnockBackFFA.KitEditorHolder) {
            Player player = (Player) e.getPlayer();
            User user = User.users.get(player.getUniqueId());
            if (user.iseditingkit) {
                user.iseditingkit = false;
                player.sendMessage(GlobalString.UnSavedSettings[user.lang]);
                Inventory inventory = player.getInventory();
                inventory.clear();
                inventory.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
            }
        }
    }
}

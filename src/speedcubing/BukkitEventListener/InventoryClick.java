package speedcubing.BukkitEventListener;


import com.google.common.collect.Sets;
import cubingserver.StringList.GlobalString;
import cubingserver.speedcubingServer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InventoryClick implements Listener {

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            if (inventory.getName().equals("container.crafting")) {
                if (e.getWhoClicked().getGameMode()  == GameMode.SURVIVAL)
                    e.setCancelled(true);
            } else if (inventory.getHolder() == KnockBackFFA.KitEditorHolder && RawSlot <= 35) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                player.updateInventory();
                switch (RawSlot) {
                    case 3:
                        ItemStack cursor = e.getCursor();
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                        Inventory inv = player.getInventory();
                        if (cursor.getType() == Material.AIR) {
                            UUID uuid = player.getUniqueId();
                            User user = User.users.get(uuid);
                            player.sendMessage(GlobalString.SavedSettings[user.lang]);
                            List<Material> t = Arrays.asList(Material.WOOD_SWORD, Material.STICK, Material.ENDER_PEARL, Material.WEB, Material.SANDSTONE, Material.ARROW, Material.BOW);
                            String newcfg = "";
                            List<Integer> empty = new ArrayList<>(Sets.newHashSet(0, 1, 2, 3, 4, 5, 6, 7, 8));
                            for (int i = 0; i < 7; i++) {
                                Material m = t.get(i);
                                int rm = -1;
                                for (Integer j : empty) {
                                    if (inv.getItem(j) != null && inv.getItem(j).getType().equals(m)) {
                                        newcfg += j;
                                        rm = j;
                                        break;
                                    }
                                }
                                empty.remove(Integer.valueOf(rm));
                            }
                            user.SQLCache = newcfg;
                            new Thread(() -> speedcubingServer.connection.update("playersdata", "kbffa_inventory='" + user.SQLCache + "'", "uuid='" + uuid + "'")).start();
                            inv.clear();
                            inv.setItem(4, KnockBackFFA.itemStacks[user.lang][11]);
                            user.iseditingkit = false;
                            player.closeInventory();
                        } else {
                            for (int i = 0; i < 9; i++) {
                                if (inv.getItem(i) == null) {
                                    inv.setItem(i, cursor);
                                    break;
                                }
                            }
                            player.setItemOnCursor(new ItemStack(Material.AIR));
                        }
                        break;
                    case 5:
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                        player.closeInventory();
                        break;
                }
            }
        }
    }
}

package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import top.speedcubing.knockbackffa.managers.MapManager;
import top.speedcubing.knockbackffa.utils.LangText;
import top.speedcubing.server.player.User;

import java.util.concurrent.atomic.AtomicInteger;

public class ChangeMapCommand implements CommandExecutor, Listener {

    /** Marks the map selector so clicks are recognised regardless of the (translated) title. */
    public static final class MenuHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        open(((Player) sender).getPlayer());
        return true;
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof MenuHolder)) return;
        if (!User.getUser(p).isStaff) p.closeInventory();
        e.setCancelled(true);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),MapManager.getMapCommandByMapName(e.getCurrentItem().getItemMeta().getDisplayName()) + " " + e.getCurrentItem().getItemMeta().getDisplayName());
        p.closeInventory();
    }
    private void open(Player player) {
        int lang = User.getUser(player).lang;
        Inventory mapInv = Bukkit.createInventory(new MenuHolder(), 36, LangText.str(lang, "%lang_kbffa_changemap_menu_title%"));
        AtomicInteger count = new AtomicInteger(0);
        MapManager.getAllMapNames().forEach(mapName -> {
            MapView mapView = Bukkit.createMap(player.getWorld());
            ItemStack map = new ItemStack(Material.MAP, 1, mapView.getId());
            ItemMeta meta = map.getItemMeta();

            // the display name is the map id the click handler switches to; keep it untranslated
            meta.setDisplayName(mapName);
            meta.setLore(LangText.lines(lang, "%lang_kbffa_changemap_lore%"));
            map.setItemMeta(meta);
            mapInv.setItem(count.getAndIncrement(), map);
        });
        player.openInventory(mapInv);
    }
}

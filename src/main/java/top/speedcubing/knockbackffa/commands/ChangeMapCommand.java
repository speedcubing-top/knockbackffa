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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import top.speedcubing.knockbackffa.managers.MapManager;
import top.speedcubing.server.player.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChangeMapCommand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        open(((Player) sender).getPlayer());
        return true;
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!User.getUser(p).isStaff) p.closeInventory();
        if (e.getClickedInventory() == null || !e.getClickedInventory().getTitle().equals("Map Selector")) return;
        e.setCancelled(true);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),MapManager.getMapCommandByMapName(e.getCurrentItem().getItemMeta().getDisplayName()) + " " + e.getCurrentItem().getItemMeta().getDisplayName());
        p.closeInventory();
    }
    private void open(Player player) {
        Inventory mapInv = Bukkit.createInventory(null, 36, "Map Selector");
        AtomicInteger count = new AtomicInteger(0);
        MapManager.getAllMapNames().forEach(mapName -> {
            MapView mapView = Bukkit.createMap(player.getWorld());
            ItemStack map = new ItemStack(Material.MAP, 1, mapView.getId());
            ItemMeta meta = map.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add("§eLeft Click to change map!");

            meta.setDisplayName(mapName);
            meta.setLore(lore);
            map.setItemMeta(meta);
            mapInv.setItem(count.getAndIncrement(), map);
        });
        player.openInventory(mapInv);
    }
}

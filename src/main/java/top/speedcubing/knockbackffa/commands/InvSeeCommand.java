package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvSeeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("§cUsage: /invsee <player>");
            return true;
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage("§cPlayer not found!");
                return true;
            }
            if (target == player) {
                player.sendMessage("§cYou can't open yourself inventory");
                return true;
            }
            Inventory inv = target.getInventory();
            player.openInventory(inv);
        }
        return true;
    }
}

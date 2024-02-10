package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.Item.TrollUtilsItemList;
import top.speedcubing.server.player.User;

public class TrollUtilsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            User user = User.getUser(sender);
            if (user.hasPermission("cmd.trollutils") || user.hasPermission("cmd.*")) {
                if (args.length == 0) {
                    user.player.sendMessage("Usage: /trollutils <item>");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("oceanbow") || args[0].equalsIgnoreCase("ob")) {
                        user.bGetInventory().addItem(TrollUtilsItemList.oceanBow());
                    } else if (args[0].equalsIgnoreCase("verticalstick") || args[0].equalsIgnoreCase("vs")) {
                        user.bGetInventory().addItem(TrollUtilsItemList.verticalStick());
                    } else if (args[0].equalsIgnoreCase("fastpickaxe") || args[0].equalsIgnoreCase("fp")) {
                        user.bGetInventory().addItem(TrollUtilsItemList.fastPickaxe());
                    } else if (args[0].equalsIgnoreCase("verticalstickpromax") || args[0].equalsIgnoreCase("vspm")) {
                        user.bGetInventory().addItem(TrollUtilsItemList.verticalStickProMax());
                    }
                }
            }
        }
        return true;
    }
}

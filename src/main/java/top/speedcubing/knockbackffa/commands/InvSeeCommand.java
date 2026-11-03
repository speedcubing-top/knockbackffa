package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import top.speedcubing.server.player.User;

public class InvSeeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        User user = User.getUser(player);
        if (args.length != 1) {
            user.sendMessage("%lang_kbffa_usage%", "/invsee <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            user.sendMessage("%lang_kbffa_player_not_found%");
            return true;
        }
        if (target == player) {
            user.sendMessage("%lang_kbffa_invsee_self%");
            return true;
        }
        Inventory targetInv = target.getInventory();
        player.openInventory(targetInv);
        return true;
    }
}

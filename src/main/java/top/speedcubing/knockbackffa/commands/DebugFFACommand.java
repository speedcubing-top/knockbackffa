package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.Member;


public class DebugFFACommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Member member = Member.getMember(player);
            if (args.length == 0) {
                return false;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("invlength")) {
                player.sendMessage("Inventory Length: " + member.inventory.length());
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("invlist")) {
                player.sendMessage("Inventory List: " + member.inventory);
                return true;
            }
        }
        return false;
    }
}

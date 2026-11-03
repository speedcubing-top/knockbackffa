package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.common.text.Msg;

public class MyWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        player.sendMessage(Msg.KNOCKBACKFFA + Msg.INFO + "You are in world: " + Msg.VALUE + player.getWorld().getName());
        return true;
    }
}

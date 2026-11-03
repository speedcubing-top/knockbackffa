package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.speedcubing.common.text.Msg;
import top.speedcubing.knockbackffa.original.EventListeners;

public class CPSLimit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Msg.KNOCKBACKFFA + Msg.INFO + "CPS limit: " + Msg.VALUE + EventListeners.cpsLimit + " cps");
            return true;
        }
        int cps = Integer.parseInt(args[0]);
        EventListeners.cpsLimit = cps;
        sender.sendMessage(Msg.KNOCKBACKFFA + Msg.SUCCESS + "CPS limit set to " + Msg.VALUE + cps + " cps" + Msg.SUCCESS + ".");
        return true;
    }
}

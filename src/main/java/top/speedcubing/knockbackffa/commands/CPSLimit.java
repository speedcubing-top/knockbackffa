package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.speedcubing.knockbackffa.original.EventListeners;
import top.speedcubing.server.lang.Lang;

public class CPSLimit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Lang.of("%lang_kbffa_cps_limit%", Integer.toString(EventListeners.cpsLimit)).get(sender).toLegacyText());
            return true;
        }
        int cps = Integer.parseInt(args[0]);
        EventListeners.cpsLimit = cps;
        sender.sendMessage(Lang.of("%lang_kbffa_cps_limit_set%", Integer.toString(cps)).get(sender).toLegacyText());
        return true;
    }
}

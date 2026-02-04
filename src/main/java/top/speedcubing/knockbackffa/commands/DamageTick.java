package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.original.EventListeners;

public class DamageTick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof  Player player)) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(player.getMaximumNoDamageTicks() + " ticks");
            return true;
        }
        int cps = Integer.parseInt(args[0]);
        EventListeners.cpsLimit = cps;
        sender.sendMessage("§aCPS LIMIT: " + cps);
//        sender.sendMessage("§aTick: " + tick);
//        Bukkit.getOnlinePlayers().forEach(p -> {
//            p.setMaximumNoDamageTicks(tick);
//        });
        return true;
    }
}

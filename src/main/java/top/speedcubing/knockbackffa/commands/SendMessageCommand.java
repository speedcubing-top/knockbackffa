package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                return false;
            }
            if (args.length >= 2) {
                StringBuilder messageBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    messageBuilder.append(args[i]).append(" ");
                }
                String message = messageBuilder.toString().trim();
                if (args[0].equalsIgnoreCase("@a")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(cl(message));
                        player.sendMessage("Send Message!");
                        return true;
                    }
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        target.sendMessage(cl(message));
                        player.sendMessage("Send Message!");
                        return true;
                    } else {
                        player.sendMessage("Player not found!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private String cl(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }
}

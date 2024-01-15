package top.speedcubing.knockbackffa.projectile.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProjectileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("gui")) {
                    //open gui
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    //reload config
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("gui")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        //open gui
                        return true;
                    } else {
                        player.sendMessage("Player not found!");
                        return true;
                    }
                }
            }
            player.sendMessage("/projectileeffect gui <player> - Open Projectile Effect selector.");
            player.sendMessage("/projectileeffect reload - Realod config.yml");
        }
        return true;
    }
}

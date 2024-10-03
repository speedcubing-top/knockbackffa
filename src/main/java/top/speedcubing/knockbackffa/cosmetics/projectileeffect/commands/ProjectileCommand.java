package top.speedcubing.knockbackffa.cosmetics.projectileeffect.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.original.KnockBackFFA;

public class ProjectileCommand implements CommandExecutor {
    private KnockBackFFA plugin;
    public ProjectileCommand(KnockBackFFA plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("gui")) {
                    plugin.getGuiProjectileEffectSelector().open(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.getProjectileEffectManager().saveProjectileEffects();
                    player.sendMessage( "§aconfig.yml reloaded!");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("gui")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        plugin.getGuiProjectileEffectSelector().open(target);
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

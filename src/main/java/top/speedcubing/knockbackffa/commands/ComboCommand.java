package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.speedcubing.knockbackffa.Member;

import java.util.HashSet;
import java.util.Set;

public class ComboCommand implements CommandExecutor, Listener {
    static Set<Player> comboSet = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("§cUsage: /combo <player>");
            return true;
        }
        Player target = p.getServer().getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage("§cPlayer not found.");
            return true;
        }
        if (comboSet.contains(target)) {
            comboSet.remove(target);
            p.sendMessage("§a" + target.getName() + " §7is no longer in combo mode.");
        } else {
            comboSet.add(target);
            p.sendMessage("§a" + target.getName() + " §7is now in combo mode.");
        }
        return true;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        comboSet.remove(e.getPlayer());
    }
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player target = (Player) e.getEntity();
            if (comboSet.contains(damager)) {
                target.setMaximumNoDamageTicks(0);
            } else {
                target.setMaximumNoDamageTicks(20);
                target.setLastDamage(0.25);
            }
        }
    }
}

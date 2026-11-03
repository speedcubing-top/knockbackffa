package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.speedcubing.server.player.User;

import java.util.HashSet;
import java.util.Set;

public class ComboCommand implements CommandExecutor, Listener {
    static Set<Player> comboSet = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        User user = User.getUser(p);
        if (args.length == 0) {
            user.sendMessage("%lang_kbffa_usage%", "/combo <player>");
            return true;
        }
        Player target = p.getServer().getPlayerExact(args[0]);
        if (target == null) {
            user.sendMessage("%lang_kbffa_player_not_found%");
            return true;
        }
        if (comboSet.contains(target)) {
            comboSet.remove(target);
            user.sendMessage("%lang_kbffa_combo_disabled%", target.getName());
        } else {
            comboSet.add(target);
            user.sendMessage("%lang_kbffa_combo_enabled%", target.getName());
        }
        return true;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        comboSet.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player target) {
            if (comboSet.contains(damager)) {
                target.setMaximumNoDamageTicks(0);
            } else {
                target.setMaximumNoDamageTicks(18);
            }
        }
    }
}

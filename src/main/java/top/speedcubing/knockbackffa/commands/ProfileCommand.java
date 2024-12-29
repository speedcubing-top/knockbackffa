package top.speedcubing.knockbackffa.commands;

import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLRow;
import top.speedcubing.server.player.User;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                sendFFADataByName(player, User.getUser(player).realName);
                return true;
            }
            if (args.length == 1) {
                String targetName = args[0];
                Player target = Bukkit.getPlayerExact(targetName);
                if (target == null) {
                    sendFFADataByName(player, targetName);
                } else {
                    User user = User.getUser(target);
                    if (user.nicked()) {
                        player.sendMessage("§cThis player disabled their stats api.");
                    } else {
                        sendFFADataByName(player, target.getName());
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void sendFFADataByName(Player sender, String playerName) {
        SQLRow r = Database.getCubing().select("priority,kbffa_kill,kbffa_dailykill,kbffa_blockplace,kbffa_death,kbffa_killstreak,name,kbffa_blockBreak").from("playersdata").where("name=?").setString(1, playerName).executeResult().get(0);
        String rank = r.getString(0);
        String kills = r.getString(1);
        String dailyKills = r.getString(2);
        String blockPlaces = r.getString(3);
        String deaths = r.getString(4);
        String killStreak = r.getString(5);
        String name = r.getString(6);
        String blockBreaks = r.getString(7);
        if (rank != null && kills != null && dailyKills != null && blockPlaces != null && deaths != null && killStreak != null && blockBreaks != null) {
            int killsInt = Integer.parseInt(kills);
            int deathsInt = Integer.parseInt(deaths);
            double kdr = (double) killsInt / deathsInt;
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedKDR = df.format(kdr);
            String equalsLine = "=".repeat(name.length());
            sender.sendMessage("======== §b" + name + " §r========\n" +
                    "Rank: " + rank +
                    "\nKills: " + kills + "\nDeaths: " + deaths + "\nKDR: " + formattedKDR
                    + "\nDaily Kills: " + dailyKills + "\nBest Kill Streak: " + killStreak
                    + "\nBlock Placed: " + blockPlaces +
                    "\nBlock Breaks: " + blockBreaks +
                    "\n========" + equalsLine + "========");
        } else {
            sender.sendMessage("This player profile cannot be found");
        }
    }
}
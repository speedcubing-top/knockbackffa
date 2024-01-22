package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.server.database.Database;

import java.text.DecimalFormat;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                String rank = Database.connection.select("priority").from("playersdata").where("name='" + player.getName() + "'").getString();
                String kills = Database.connection.select("kbffa_kill").from("playersdata").where("name='" + player.getName() + "'").getString();
                String dailyKills = Database.connection.select("kbffa_dailykill").from("playersdata").where("name='" + player.getName() + "'").getString();
                String blocks = Database.connection.select("kbffa_blockplace").from("playersdata").where("name='" + player.getName() + "'").getString();
                String deaths = Database.connection.select("kbffa_death").from("playersdata").where("name='" + player.getName() + "'").getString();
                String killStreak = Database.connection.select("kbffa_killstreak").from("playersdata").where("name='" + player.getName() + "'").getString();
                String name = Database.connection.select("name").from("playersdata").where("name='" + player.getName() + "'").getString();
                if (kills != null && dailyKills != null && blocks != null && deaths != null && killStreak != null) {
                    int killsInt = Integer.parseInt(kills);
                    int deathsInt = Integer.parseInt(deaths);
                    double kdr = (double) killsInt / deathsInt;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedKDR = df.format(kdr);
                    player.sendMessage("===== " + name + " =====\n" +
                            "Rank: " + rank +
                            "Kills: " + kills + "\nDeaths: " + deaths + "\nKDR: " + formattedKDR
                            + "\nDaily Kills: " + dailyKills + "\nBest Kill Streak: " + killStreak
                            + "\nBlock Placed: " + blocks);
                    return true;
                } else {
                    player.sendMessage("This player profile cannot be found");
                    return true;
                }
            }
            if (args.length == 1) {
                String target = args[0];
                String rank = Database.connection.select("priority").from("playersdata").where("name='" + player.getName() + "'").getString();
                String kills = Database.connection.select("kbffa_kill").from("playersdata").where("name='" + target + "'").getString();
                String dailyKills = Database.connection.select("kbffa_dailykill").from("playersdata").where("name='" + target + "'").getString();
                String blocks = Database.connection.select("kbffa_blockplace").from("playersdata").where("name='" + target + "'").getString();
                String deaths = Database.connection.select("kbffa_death").from("playersdata").where("name='" + target + "'").getString();
                String killStreak = Database.connection.select("kbffa_killstreak").from("playersdata").where("name='" + target + "'").getString();
                String name = Database.connection.select("name").from("playersdata").where("name='" + target + "'").getString();
                if (kills != null && dailyKills != null && blocks != null && deaths != null && killStreak != null) {
                    int killsInt = Integer.parseInt(kills);
                    int deathsInt = Integer.parseInt(deaths);
                    double kdr = (double) killsInt / deathsInt;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedKDR = df.format(kdr);
                    player.sendMessage("===== " + name + " =====\n" +
                            "Rank: " + rank +
                            "Kills: " + kills + "\nDeaths: " + deaths + "\nKDR: " + formattedKDR
                            + "\nDaily Kills: " + dailyKills + "\nBest Kill Streak: " + killStreak
                            + "\nBlock Placed: " + blocks);
                } else {
                    player.sendMessage("This player profile cannot be found");
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}

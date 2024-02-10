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
                String[] data = Database.connection.select("priority,kbffa_kill,kbffa_dailykill,kbffa_blockplace,kbffa_death,kbffa_killstreak,name,kbffa_blockbreak").from("playersdata").where("name='" + player.getName() + "'").getStringArray();
                String rank = data[0];
                String kills = data[1];
                String dailyKills = data[2];
                String blockPlaces = data[3];
                String deaths = data[4];
                String killStreak = data[5];
                String name = data[6];
                String blockBreaks = data[7];
                if (rank != null && kills != null && dailyKills != null && blockPlaces != null && deaths != null && killStreak != null && blockBreaks != null) {
                    int killsInt = Integer.parseInt(kills);
                    int deathsInt = Integer.parseInt(deaths);
                    double kdr = (double) killsInt / deathsInt;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedKDR = df.format(kdr);
                    player.sendMessage("======== §b" + name + " §r========\n" +
                            "Rank: " + rank +
                            "\nKills: " + kills + "\nDeaths: " + deaths + "\nKDR: " + formattedKDR
                            + "\nDaily Kills: " + dailyKills + "\nBest Kill Streak: " + killStreak
                            + "\nBlock Placed: " + blockPlaces +
                            "\nBlock Breaks: " + blockBreaks);
                    return true;
                } else {
                    player.sendMessage("This player profile cannot be found");
                    return true;
                }
            }
            if (args.length == 1) {
                String target = args[0];
                String[] data = Database.connection.select("priority,kbffa_kill,kbffa_dailykill,kbffa_blockplace,kbffa_death,kbffa_killstreak,name,kbffa_blockBreak").from("playersdata").where("name='" + target + "'").getStringArray();
                String rank = data[0];
                String kills = data[1];
                String dailyKills = data[2];
                String blockPlaces = data[3];
                String deaths = data[4];
                String killStreak = data[5];
                String name = data[6];
                String blockBreaks = data[7];
                if (rank != null && kills != null && dailyKills != null && blockPlaces != null && deaths != null && killStreak != null && blockBreaks != null) {
                    int killsInt = Integer.parseInt(kills);
                    int deathsInt = Integer.parseInt(deaths);
                    double kdr = (double) killsInt / deathsInt;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedKDR = df.format(kdr);
                    player.sendMessage("======== §b" + name + " §r========\n" +
                            "Rank: " + rank +
                            "\nKills: " + kills + "\nDeaths: " + deaths + "\nKDR: " + formattedKDR
                            + "\nDaily Kills: " + dailyKills + "\nBest Kill Streak: " + killStreak
                            + "\nBlock Placed: " + blockPlaces +
                            "\nBlock Breaks: " + blockBreaks);
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
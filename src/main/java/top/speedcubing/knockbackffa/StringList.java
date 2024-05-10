package top.speedcubing.knockbackffa;

import top.speedcubing.server.lang.LangMessage;
import top.speedcubing.server.utils.config;

public class StringList {
    public static final String obj = "§c❤";

    public static LangMessage title = new LangMessage(
            "§b§lDaily Kills",
            "§b§lDaily Kills",
            "§b§lDaily Kills",
            "§b§lDaily Kills"
    );

    public static LangMessage title2 = new LangMessage(
            "§b§lLifetime Kills",
            "§b§lLifetime Kills",
            "§b§lLifetime Kills",
            "§b§lLifetime Kills"
    );
    public static LangMessage type = new LangMessage(
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime"
    );

    public static LangMessage type2 = new LangMessage(
            "§7Daily §aLifetime",
            "§7Daily §aLifetime",
            "§7Daily §aLifetime",
            "§7Daily §aLifetime"
    );
    public static String leaderboard = "§e%i%. %j% §7- §e%k%";

    public static LangMessage noData = new LangMessage(
            "§7No data found.",
            "§7No data found.",
            "§7No data found.",
            "§7No data found."
    );
    public static LangMessage diedYourself = new LangMessage(
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!"
    );
    public static LangMessage died = new LangMessage(
            "§8[§bKnockBackFFA§8] %1% §7was killed by %2%§7! §8[%3%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] %1% §7was killed by %2%§7! §8[%3%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] %1% §7was killed by %2%§7! §8[%3%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] %1% §7was killed by %2%§7! §8[%3%" + obj + "§8]"
    );

    public static String[][] sidebar = {
            {"§3KnockBackFFA", "§6speedcubing.top", "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6speedcubing.top", "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6speedcubing.top", "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6speedcubing.top", "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"}
    };
}

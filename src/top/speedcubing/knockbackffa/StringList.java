package top.speedcubing.knockbackffa;

import top.speedcubing.server.config;

public class StringList {
    public static final String obj = "§c❤";

    public static String[] title = {
            "§b§lDaily Kills",
            "§b§lDaily Kills",
            "§b§lDaily Kills",
            "§b§lDaily Kills"
    };

    public static String[] title2 = {
            "§b§lLifetime Kills",
            "§b§lLifetime Kills",
            "§b§lLifetime Kills",
            "§b§lLifetime Kills",
    };
    public static String[] type = {
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime",
            "§aDaily §7Lifetime"
    };

    public static String[] type2 = {
            "§7Daily §aLifetime",
            "§7Daily §aLifetime",
            "§7Daily §aLifetime",
            "§7Daily §aLifetime"
    };
    public static String leaderboard = "§e%i%. %j% §7- §e%k%";

    public static String[] noData = {
            "§7No data found.",
            "§7No data found.",
            "§7No data found.",
            "§7No data found."
    };
    public static String[] Kill = {
            "§8[§bKnockBackFFA§8] §7You killed %t%§!",
            "§8[§bKnockBackFFA§8] §7You killed %t%§!",
            "§8[§bKnockBackFFA§8] §7You killed %t%§!",
            "§8[§bKnockBackFFA§8] §7You killed %t%§!"
    };
    public static String[] diedYourself = {
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!",
            "§8[§bKnockBackFFA§8] §cYou died!"
    };
    public static String[] died = {
            "§8[§bKnockBackFFA§8] §cYou were killed by %t%§c! §8[%h%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] §cYou were killed by %t%§c! §8[%h%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] §cYou were killed by %t%§c! §8[%h%" + obj + "§8]",
            "§8[§bKnockBackFFA§8] §cYou were killed by %t%§c! §8[%h%" + obj + "§8]"
    };

    public static String[][] sidebar = {
            {"§3KnockBackFFA", "§6" + config.SERVERIP, "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6" + config.SERVERIP, "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6" + config.SERVERIP, "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"},
            {"§3KnockBackFFA", "§6" + config.SERVERIP, "§3", "Current Killstreak", "§6", "§0", "Total Kills", "§e", "§1", "Total Deaths", "§c", "§2", "Killstreak Record", "§9"}
    };
}

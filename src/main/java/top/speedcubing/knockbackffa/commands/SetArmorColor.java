package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.original.Member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetArmorColor implements CommandExecutor {
    private static final Pattern RGB_PATTERN = Pattern.compile("^([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})$");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length != 2) {
            player.sendMessage("§cUsage: /setarmorcolor <armor(1~4)> <red>,<green>,<blue>");
            return true;
        }

        String armor = args[0];
        if (!isInt(armor) || Integer.parseInt(armor) < 1 || Integer.parseInt(armor) > 4) {
            player.sendMessage("§cInvalid armor value. Please use a value between 1 and 4.");
            return true;
        }

        String rgb = args[1];
        if (!isValidRGB(rgb)) {
            player.sendMessage("§cInvalid RGB value. Please use the format <red>,<green>,<blue> where each value is between 0 and 255.");
            return true;
        }

        String originalColor = Member.getMember(player).armorColor.equals("null") ? "160,101,64|160,101,64|160,101,64|160,101,64" : Member.getMember(player).armorColor;
        int armorSlot = Integer.parseInt(armor);
        String[] originalColors = originalColor.split("\\|");
        originalColors[armorSlot - 1] = rgb;
        Member.getMember(player).armorColor = String.join("|", originalColors);
        player.sendMessage("§aArmor color updated.");
        return true;
    }

    private static boolean isValidRGB(String rgbString) {
        Matcher matcher = RGB_PATTERN.matcher(rgbString);
        if (matcher.matches()) {
            int red = Integer.parseInt(matcher.group(1));
            int green = Integer.parseInt(matcher.group(2));
            int blue = Integer.parseInt(matcher.group(3));
            return isValidRGBValue(red) && isValidRGBValue(green) && isValidRGBValue(blue);
        }
        return false;
    }

    private static boolean isValidRGBValue(int value) {
        return value >= 0 && value <= 255;
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

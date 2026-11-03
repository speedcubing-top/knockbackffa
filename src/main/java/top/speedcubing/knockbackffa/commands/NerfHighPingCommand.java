package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.speedcubing.cubingpaper.CubingPaperConfig;
import top.speedcubing.server.lang.Lang;

public class NerfHighPingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            send(sender, "%lang_kbffa_nerfhighping_status%", String.valueOf(CubingPaperConfig.nerfHighPingKnockback),
                    String.valueOf(CubingPaperConfig.nerfHighPingKnockbackFriction), String.valueOf(CubingPaperConfig.nerfHighPingKnockbackMinPing));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                CubingPaperConfig.nerfHighPingKnockback = true;
                send(sender, "%lang_kbffa_nerfhighping_enabled%");
                break;
            case "disable":
                CubingPaperConfig.nerfHighPingKnockback = false;
                send(sender, "%lang_kbffa_nerfhighping_disabled%");
                break;
            case "f":
                if (args.length < 2) { send(sender, "%lang_kbffa_usage%", "/nerfhighping f <value>"); return true; }
                CubingPaperConfig.nerfHighPingKnockbackFriction = Double.parseDouble(args[1]);
                send(sender, "%lang_kbffa_nerfhighping_friction_set%", String.valueOf(CubingPaperConfig.nerfHighPingKnockbackFriction));
                break;
            case "p":
                if (args.length < 2) { send(sender, "%lang_kbffa_usage%", "/nerfhighping p <value>"); return true; }
                CubingPaperConfig.nerfHighPingKnockbackMinPing = Integer.parseInt(args[1]);
                send(sender, "%lang_kbffa_nerfhighping_minping_set%", String.valueOf(CubingPaperConfig.nerfHighPingKnockbackMinPing));
                break;
            default:
                send(sender, "%lang_kbffa_usage%", "/nerfhighping [enable|disable|f <val>|p <val>]");
        }
        return true;
    }

    private static void send(CommandSender sender, String key, String... param) {
        sender.sendMessage(Lang.of(key, param).get(sender).toLegacyText());
    }
}

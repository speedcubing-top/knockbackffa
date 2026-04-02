package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.speedcubing.cubingpaper.CubingPaperConfig;

public class NerfHighPingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Enabled: " + CubingPaperConfig.nerfHighPingKnockback
                    + ", Friction: " + CubingPaperConfig.nerfHighPingKnockbackFriction
                    + ", MinPing: " + CubingPaperConfig.nerfHighPingKnockbackMinPing);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                CubingPaperConfig.nerfHighPingKnockback = true;
                sender.sendMessage("High-ping knockback nerf enabled.");
                break;
            case "disable":
                CubingPaperConfig.nerfHighPingKnockback = false;
                sender.sendMessage("High-ping knockback nerf disabled.");
                break;
            case "f":
                if (args.length < 2) { sender.sendMessage("Usage: /nerfhighping f <value>"); return true; }
                CubingPaperConfig.nerfHighPingKnockbackFriction = Double.parseDouble(args[1]);
                sender.sendMessage("Set friction to " + CubingPaperConfig.nerfHighPingKnockbackFriction);
                break;
            case "p":
                if (args.length < 2) { sender.sendMessage("Usage: /nerfhighping p <value>"); return true; }
                CubingPaperConfig.nerfHighPingKnockbackMinPing = Integer.parseInt(args[1]);
                sender.sendMessage("Set min-ping to " + CubingPaperConfig.nerfHighPingKnockbackMinPing);
                break;
            default:
                sender.sendMessage("Usage: /nerfhighping [enable|disable|f <val>|p <val>]");
        }
        return true;
    }
}

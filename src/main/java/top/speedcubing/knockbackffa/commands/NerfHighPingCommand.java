package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.speedcubing.common.text.Msg;
import top.speedcubing.cubingpaper.CubingPaperConfig;

public class NerfHighPingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Msg.KNOCKBACKFFA + Msg.INFO + "Enabled: " + Msg.VALUE + CubingPaperConfig.nerfHighPingKnockback
                    + Msg.INFO + ", Friction: " + Msg.VALUE + CubingPaperConfig.nerfHighPingKnockbackFriction
                    + Msg.INFO + ", MinPing: " + Msg.VALUE + CubingPaperConfig.nerfHighPingKnockbackMinPing);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                CubingPaperConfig.nerfHighPingKnockback = true;
                sender.sendMessage(Msg.KNOCKBACKFFA + Msg.SUCCESS + "High-ping knockback nerf enabled.");
                break;
            case "disable":
                CubingPaperConfig.nerfHighPingKnockback = false;
                sender.sendMessage(Msg.KNOCKBACKFFA + Msg.SUCCESS + "High-ping knockback nerf disabled.");
                break;
            case "f":
                if (args.length < 2) { sender.sendMessage(Msg.KNOCKBACKFFA + Msg.usage("/nerfhighping f <value>")); return true; }
                CubingPaperConfig.nerfHighPingKnockbackFriction = Double.parseDouble(args[1]);
                sender.sendMessage(Msg.KNOCKBACKFFA + Msg.SUCCESS + "Set friction to " + Msg.VALUE + CubingPaperConfig.nerfHighPingKnockbackFriction + Msg.SUCCESS + ".");
                break;
            case "p":
                if (args.length < 2) { sender.sendMessage(Msg.KNOCKBACKFFA + Msg.usage("/nerfhighping p <value>")); return true; }
                CubingPaperConfig.nerfHighPingKnockbackMinPing = Integer.parseInt(args[1]);
                sender.sendMessage(Msg.KNOCKBACKFFA + Msg.SUCCESS + "Set min-ping to " + Msg.VALUE + CubingPaperConfig.nerfHighPingKnockbackMinPing + Msg.SUCCESS + ".");
                break;
            default:
                sender.sendMessage(Msg.KNOCKBACKFFA + Msg.usage("/nerfhighping [enable|disable|f <val>|p <val>]"));
        }
        return true;
    }
}

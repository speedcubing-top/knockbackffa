package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.Member;

public class KillMyselfCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Member member = Member.getMember(commandSender);
            if (member.isFighting) {
                member.death(false);
            } else {
                commandSender.sendMessage("You are not fighting");
            }
        }
        return true;
    }
}

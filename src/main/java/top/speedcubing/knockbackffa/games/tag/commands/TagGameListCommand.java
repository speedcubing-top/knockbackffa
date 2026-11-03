package top.speedcubing.knockbackffa.games.tag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.common.text.Msg;
import top.speedcubing.server.player.User;

public class TagGameListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            User.getUser(player).sendMessage("%lang_kbffa_tag_party_list%");
            int index = 1;
            for (Player players : TagGameCommand.getPartyPlayers(TagGameCommand.partyList)) {
                player.sendMessage(Msg.INFO + index + ". " + Msg.VALUE + players.getName());
                index++;
            }
        }
        return true;
    }
}

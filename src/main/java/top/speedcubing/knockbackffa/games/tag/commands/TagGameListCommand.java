package top.speedcubing.knockbackffa.games.tag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagGameListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage("§6[Tag Game] §bParty List");
            int index = 1;
            for (Player players : TagGameCommand.getPartyPlayers(TagGameCommand.partyList)) {
                player.sendMessage(index + ". " + players.getName());
                index++;
            }
        }
        return true;
    }
}

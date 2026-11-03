package top.speedcubing.knockbackffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.common.text.Msg;
import top.speedcubing.knockbackffa.managers.MapManager;
import top.speedcubing.knockbackffa.managers.VoteManager;
import top.speedcubing.server.player.User;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 0 && VoteManager.getVoteStatus()) {
            MapManager.open(player);
            return true;
        } else if (args.length == 0 && !VoteManager.getVoteStatus()) {
            User.getUser(player).sendMessage("%lang_kbffa_vote_not_in_progress%");
            return true;
        }
        if (args.length == 1) {
            if (!User.getUser(player).hasPermission("vote.start") && !User.getUser(player).isStaff) return true;
            switch (args[0]) {
                case "start":
                    if (VoteManager.getVoteStatus()) {
                        player.sendMessage(Msg.KNOCKBACKFFA + Msg.ERROR + "A vote is already in progress.");
                    } else {
                        VoteManager.startVote();
                    }
                break;
                case "end":
//                    if (!VoteManager.getVoteStatus()) {
//                        User.getUser(player).sendMessage("%lang_kbffa_vote_not_in_progress%");
//                    } else {
//                        VoteManager.endVote();
//                    }
                    User.getUser(player).sendMessage("%lang_kbffa_vote_ends_automatically%");
                    break;
                default:

            }
        }
        return true;
    }
}

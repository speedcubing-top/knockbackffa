package speedcubing;

import cubingserver.libs.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import speedcubing.KnockBackFFA;

public class map implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (PlayerData.getAbilityRank(((Player) commandSender).getUniqueId()) <= 20) {
            KnockBackFFA.switchMap(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            commandSender.sendMessage(strings[0] + " " + strings[1] + " " + strings[2]);
        }
        return true;
    }
}

package speedcubing;

import org.bukkit.entity.Player;
import speedcubing.lib.utils.Console;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;
import speedcubing.spigot.Event.ServerEventHandler;
import speedcubing.spigot.Event.events.PlayInChatEvent;

public class ServerEvent {

    @ServerEventHandler
    public void PlayInChatEvent(PlayInChatEvent e) {
        String message = e.packet.a();
        if (message.charAt(0) != '/') {
            Player player = e.player;
            e.setCancelled(true);
            String[] formats = speedcubingServer.getFormat(Member.getMember(player.getUniqueId()).user.rank);
            String text = formats[0] + player.getName();
            String[] str = GlobalString.MessageFormat[0];
            String[] out = new String[]{
                    str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
            };
            Member.users.values().forEach(a -> a.player.sendMessage(out[a.user.lang]));
            Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
        }
    }
}

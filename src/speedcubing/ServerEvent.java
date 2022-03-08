package speedcubing;

import cubing.bukkit.Event.ServerEventHandler;
import cubing.bukkit.Event.events.PlayInChatEvent;
import cubing.bukkit.PlayerUtils;
import cubing.utils.Console;
import cubingserver.StringList.GlobalString;
import cubingserver.customEvents.UDPEvent;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ServerEvent {
    @ServerEventHandler
    public void UDPEvent(UDPEvent e) {
        String[] rs = e.rs;
        if (rs[0].equals("o") && rs[1].equals("1")) {
            speedcubingServer.AllPlayers = Integer.parseInt(rs[2]);
            for (User u : User.users.values()) {
                PlayerUtils.sendTabHeaderFooter(((CraftPlayer) u.player).getHandle().playerConnection, GlobalString.LobbyTabList[0][u.lang], GlobalString.LobbyTabList[1][u.lang].replace("%int%", rs[2]));
            }
        }
    }

    @ServerEventHandler
    public void PlayInChatEvent(PlayInChatEvent e) {
        Player player = e.player;
        String message = e.packet.a();
        long time = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();
        boolean spam = PlayerJoin.Canspam.contains(uuid);
        if (!spam && time - speedcubingServer.spam.get(uuid) < 500)
            player.sendMessage(GlobalString.spam[PlayerData.getLang(uuid)]);
        else if (message.charAt(0) != '/') {
            if (!spam && speedcubingServer.lastmsg.get(uuid).equals(message))
                player.sendMessage(GlobalString.samemsg[PlayerData.getLang(uuid)]);
            else {
                String[] formats = Rank.format(uuid);
                String text = formats[0] + formats[1] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                for (User u : User.users.values()) {
                    u.player.sendMessage(out[u.lang]);
                }
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
                speedcubingServer.lastmsg.put(uuid, message);
            }
            e.setCancelled(true);
        }
        speedcubingServer.spam.put(uuid, time);
    }
}

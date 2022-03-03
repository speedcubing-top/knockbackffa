package speedcubing.All;

import cubing.utils.Console;
import cubingserver.StringList.GlobalString;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import speedcubing.User;

import java.util.List;
import java.util.UUID;

public class Chat {

    public void PacketInject(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get("PacketPlayInChat") != null)
            return;
        channel.pipeline().addAfter("decoder", "PacketPlayInChat", new MessageToMessageDecoder<PacketPlayInChat>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInChat packet, List<Object> arg) {
                String message = packet.a();
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
                        for(User u : User.users.values()){
                            u.player.sendMessage(out[u.lang]);
                        }
                        Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
                        speedcubingServer.lastmsg.put(uuid, message);
                    }
                } else arg.add(packet);
                speedcubingServer.spam.put(uuid, time);
            }
        });
    }
}

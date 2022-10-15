package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.speedcubing.lib.bukkit.event.PlayInEvent;
import top.speedcubing.lib.eventbus.LibEventHandler;
import top.speedcubing.lib.utils.Console;
import top.speedcubing.server.config;
import top.speedcubing.server.events.ConfigReloadEvent;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.libs.GlobalString;
import top.speedcubing.server.speedcubingServer;

public class ServerEvent {

    @LibEventHandler
    public void ConfigReloadEvent(ConfigReloadEvent e) {
        KnockBackFFA.centerX = config.config.getAsJsonObject("defaultkbffa").get("x").getAsDouble();
        KnockBackFFA.centerZ = config.config.getAsJsonObject("defaultkbffa").get("z").getAsDouble();
        KnockBackFFA.offsetY = config.config.getAsJsonObject("defaultkbffa").get("y").getAsDouble();
        KnockBackFFA.yaw = config.config.getAsJsonObject("defaultkbffa").get("yaw").getAsFloat();
        KnockBackFFA.holoX = config.config.getAsJsonObject("defaultkbffa").get("holox").getAsDouble();
        KnockBackFFA.holoZ = config.config.getAsJsonObject("defaultkbffa").get("holoz").getAsDouble();
    }


    @LibEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.packet instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.packet).a();
            if (message.charAt(0) != '/') {
                Player player = e.player;
                e.isCancelled = true;
                String[] formats = speedcubingServer.getFormat(Member.getMember(player).user.rank);
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                speedcubingServer.globalChat(Bukkit.getOnlinePlayers(), player, out);
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
            }
        }
    }

    @LibEventHandler
    public void ToggleFlyEvent(ToggleFlyEvent e) {
        e.isCancelled = true;
        e.player.sendMessage(GlobalString.OnlyInHub[Member.getMember(e.player).user.lang]);
    }

    @LibEventHandler
    public void NickEvent(NickEvent e) {
        e.isCancelled = true;
        e.player.sendMessage(GlobalString.OnlyInHub[Member.getMember(e.player).user.lang]);
    }

    @LibEventHandler
    public void SkinEvent(SkinEvent e) {
        e.isCancelled = true;
        e.player.sendMessage(GlobalString.OnlyInHub[Member.getMember(e.player).user.lang]);
    }
}

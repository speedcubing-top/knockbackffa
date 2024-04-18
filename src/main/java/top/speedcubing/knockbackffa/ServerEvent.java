package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.lib.events.packet.PlayInEvent;
import top.speedcubing.server.events.ConfigReloadEvent;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.share.Chat;
import top.speedcubing.server.utils.config;

public class ServerEvent {

    @CubingEventHandler
    public void ConfigReloadEvent(ConfigReloadEvent e) {
        KnockBackFFA.centerX = config.config.getAsJsonObject("defaultkbffa").get("x").getAsDouble();
        KnockBackFFA.centerZ = config.config.getAsJsonObject("defaultkbffa").get("z").getAsDouble();
        KnockBackFFA.offsetY = config.config.getAsJsonObject("defaultkbffa").get("y").getAsDouble();
        KnockBackFFA.yaw = config.config.getAsJsonObject("defaultkbffa").get("yaw").getAsFloat();
        KnockBackFFA.holoX = config.config.getAsJsonObject("defaultkbffa").get("holox").getAsDouble();
        KnockBackFFA.holoZ = config.config.getAsJsonObject("defaultkbffa").get("holoz").getAsDouble();
        KnockBackFFA.spawnprotectY = config.config.getAsJsonObject("defaultkbffa").get("spawnproty").getAsInt();
    }


    @CubingEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.packet instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.packet).a();
            if (message.charAt(0) != '/') {
                e.isCancelled = true;
                Chat.globalChat(Bukkit.getOnlinePlayers(), e.player, GlobalString.MessageFormat, message, Member.getMember(e.player).user.getPrefixName(false), Member.getMember(e.player).user.getChatColor(false));
            }
        }
    }

    @CubingEventHandler
    public void ToggleFlyEvent(ToggleFlyEvent e) {
        e.isCancelled = true;
        Member.getMember(e.player).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void NickEvent(NickEvent e) {
        e.isCancelled = true;
        Member.getMember(e.player).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void SkinEvent(SkinEvent e) {
        e.isCancelled = true;
        Member.getMember(e.player).user.sendLangMessage(GlobalString.OnlyInHub);
    }
}

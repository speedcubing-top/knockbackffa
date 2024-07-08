package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.events.CubingTickEvent;
import top.speedcubing.common.rank.Rank;
import top.speedcubing.lib.bukkit.events.packet.PlayInEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.share.Chat;

public class ServerEvent {

    @CubingEventHandler
    public void configReloadEvent(ConfigReloadEvent e) {
        KnockBackFFA.centerX = ServerConfig.config.getAsJsonObject("defaultkbffa").get("x").getAsDouble();
        KnockBackFFA.centerZ = ServerConfig.config.getAsJsonObject("defaultkbffa").get("z").getAsDouble();
        KnockBackFFA.offsetY = ServerConfig.config.getAsJsonObject("defaultkbffa").get("y").getAsDouble();
        KnockBackFFA.yaw = ServerConfig.config.getAsJsonObject("defaultkbffa").get("yaw").getAsFloat();
        KnockBackFFA.holoX = ServerConfig.config.getAsJsonObject("defaultkbffa").get("holox").getAsDouble();
        KnockBackFFA.holoZ = ServerConfig.config.getAsJsonObject("defaultkbffa").get("holoz").getAsDouble();
        KnockBackFFA.spawnprotectY = ServerConfig.config.getAsJsonObject("defaultkbffa").get("spawnproty").getAsInt();
    }

    @CubingEventHandler
    public void cubingTickEvent(CubingTickEvent e) {
        try {
            String[] arr = Database.connection.select("priority,name,kbffa_kill,id").from("playersdata").orderBy("kbffa_kill DESC, name ASC").limit(0, 10).getStringArray();
            String[] arr2 = Database.connection.select("priority,name,kbffa_dailykill,id").from("playersdata").orderBy("kbffa_dailykill DESC, name ASC").limit(0, 10).getStringArray();
            Database.connection.update("champ", "id=" + arr[3], "subject='kbffa_kill'");
            Database.connection.update("champ", "id=" + arr2[3], "subject='kbffa_dailykill'");
            int lastKill = 0;
            int index = 0;
            for (int i = 0; i < 10; i++) {
                int a = Integer.parseInt(arr[i * 4 + 2]);
                if (lastKill != a) {
                    index = i + 1;
                    lastKill = a;
                }
                KnockBackFFA.boardName[i] = arr[i * 4 + 3];
                KnockBackFFA.board[i] = StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", Rank.getFormat(arr[i * 4], Integer.parseInt(arr[i * 4 + 3])).getPrefix() + arr[i * 4 + 1]).replace("%k%", arr[i * 4 + 2]);
            }
            lastKill = 0;
            index = 0;
            for (int i = 0; i < 10; i++) {
                int a = Integer.parseInt(arr2[i * 4 + 2]);
                if (lastKill != a) {
                    index = i + 1;
                    lastKill = a;
                }
                KnockBackFFA.dailyBoardName[i] = arr2[i * 4 + 3];
                KnockBackFFA.dailyBoard[i] = arr2[i * 4 + 2].equals("0") ? "§e#. §7### - §e#" : StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", Rank.getFormat(arr2[i * 4], Integer.parseInt(arr2[i * 4 + 3])).getPrefix() + arr2[i * 4 + 1]).replace("%k%", arr2[i * 4 + 2]);
            }
            for (Member m : Member.users.values()) {
                m.a();
                if (m.killedPlayer) {
                    m.killedPlayer = false;
                    m.user.dbUpdate("kbffa_kill=" + m.kill + ",kbffa_dailykill=" + m.dailyKill);
                }
                m.updatePlayerPos(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @CubingEventHandler
    public void playInEvent(PlayInEvent e) {
        if (e.getPacket() instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.getPacket()).a();
            if (message.charAt(0) != '/') {
                e.setCancelled(true);
                Chat.globalChat(Bukkit.getOnlinePlayers(), e.getPlayer(), GlobalString.MessageFormat, message, Member.getMember(e.getPlayer()).user.getPrefixName(false), Member.getMember(e.getPlayer()).user.getChatColor(false));
            }
        }
    }

    @CubingEventHandler
    public void nickEvent(NickEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void skinEvent(SkinEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void toggleFlyEvent(ToggleFlyEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }
}

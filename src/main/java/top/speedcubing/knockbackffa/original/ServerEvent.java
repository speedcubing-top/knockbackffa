package top.speedcubing.knockbackffa.original;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.events.CubingTickEvent;
import java.util.HashMap;
import java.util.Map;
import top.speedcubing.common.profiler.Profiler;
import top.speedcubing.common.rank.Rank;
import top.speedcubing.common.rank.RankFormat;
import top.speedcubing.knockbackffa.original.configuration.Configuration;
import top.speedcubing.knockbackffa.original.list.EntityList;
import top.speedcubing.knockbackffa.original.list.StringList;
import top.speedcubing.lib.bukkit.SideBar;
import top.speedcubing.lib.bukkit.events.packet.PlayInEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLResult;
import top.speedcubing.lib.utils.SQL.SQLRow;
import top.speedcubing.server.events.player.LanguageChangeEvent;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.share.Chat;

public class ServerEvent {

    private static final int BOARD_REFRESH_TICKS = 5;
    private Integer lastKillChamp, lastDailyKillChamp;

    @CubingEventHandler
    public void cubingTickEvent(CubingTickEvent e) {
        try (SQLConnection connection = Database.getCubing()) {
            if (e.getTick() % BOARD_REFRESH_TICKS == 0)
                refreshBoards(connection);
            for (Member m : Member.users.values()) {
                m.a();
                if (m.killedPlayer) {
                    m.killedPlayer = false;
                    m.user.dbUpdate("kbffa_kill=" + m.kill + ",kbffa_dailykill=" + m.dailyKill);
                }
            }
            // one scan for every online player's position instead of one ranking query per player
            Member.updatePositions(connection, Member.users.values());
            for (Member m : Member.users.values())
                m.updatePlayerPos(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // two top-10 queries + one periodrank query for both boards, every BOARD_REFRESH_TICKS seconds;
    // the champ table is only written when a leader actually changes
    private void refreshBoards(SQLConnection connection) {
        try (Profiler.Timer t = Profiler.timer("kbffa.leaderboard")) {
            refreshBoardsUntimed(connection);
        }
    }

    private void refreshBoardsUntimed(SQLConnection connection) {
        SQLResult arr = connection.select("priority,name,kbffa_kill,id").from("playersdata").orderBy("kbffa_kill DESC, name ASC").limit(0, 10).executeResult();
        SQLResult arr2 = connection.select("priority,name,kbffa_dailykill,id").from("playersdata").orderBy("kbffa_dailykill DESC, name ASC").limit(0, 10).executeResult();
        if (arr.isEmpty() || arr2.isEmpty())
            return;

        Integer killChamp = arr.get(0).getInt(3);
        if (!killChamp.equals(lastKillChamp)) {
            connection.update("champ", "id=" + killChamp, "subject='kbffa_kill'");
            lastKillChamp = killChamp;
        }
        Integer dailyKillChamp = arr2.get(0).getInt(3);
        if (!dailyKillChamp.equals(lastDailyKillChamp)) {
            connection.update("champ", "id=" + dailyKillChamp, "subject='kbffa_dailykill'");
            lastDailyKillChamp = dailyKillChamp;
        }

        Map<Integer, String> dbRanks = new HashMap<>();
        for (SQLRow r : arr)
            dbRanks.put(r.getInt(3), r.getString(0));
        for (SQLRow r : arr2)
            dbRanks.put(r.getInt(3), r.getString(0));
        Map<Integer, String> ranks = Rank.resolveAll(dbRanks);

        int lastKill = 0;
        int index = 0;
        int i = 0;
        for (SQLRow r : arr) {
            int a = r.getInt(2);
            if (lastKill != a) {
                index = i + 1;
                lastKill = a;
            }
            KnockBackFFA.boardName[i] = r.getString(3);
            // 0 kills never moved index off 0 ("0. name - 0"); show it as an empty slot like the daily board
            KnockBackFFA.board[i] = a == 0 ? EMPTY_SLOT : StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", prefix(ranks, r.getInt(3)) + r.getString(1)).replace("%k%", r.getString(2));
            i++;
        }
        // fewer than ten players: an empty hologram name shows the armor stand's own "Armor Stand"
        for (; i < KnockBackFFA.board.length; i++) {
            KnockBackFFA.boardName[i] = null;
            KnockBackFFA.board[i] = EMPTY_SLOT;
        }
        lastKill = 0;
        index = 0;
        i = 0;
        for (SQLRow r : arr2) {
            int a = r.getInt(2);
            if (lastKill != a) {
                index = i + 1;
                lastKill = a;
            }
            KnockBackFFA.dailyBoardName[i] = r.getString(3);
            KnockBackFFA.dailyBoard[i] = r.getInt(2) == 0 ? EMPTY_SLOT : StringList.leaderboard.replace("%i%", Integer.toString(index)).replace("%j%", prefix(ranks, r.getInt(3)) + r.getString(1)).replace("%k%", r.getString(2));
            i++;
        }
        for (; i < KnockBackFFA.dailyBoard.length; i++) {
            KnockBackFFA.dailyBoardName[i] = null;
            KnockBackFFA.dailyBoard[i] = EMPTY_SLOT;
        }
    }

    private static final String EMPTY_SLOT = "§e#. §7### - §e#";

    private static String prefix(Map<Integer, String> ranks, int id) {
        Rank rank = Rank.rankByName.get(ranks.get(id));
        return rank == null ? "" : rank.getFormat().getPrefix();
    }


    @CubingEventHandler
    public void playInEvent(PlayInEvent e) {
        if (e.getPacket() instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.getPacket()).a();
            if (message.charAt(0) != '/') {
                e.setCancelled(true);
                RankFormat format = Member.getMember(e.getPlayer()).user.getFormat(false);
                Chat.globalChat(Bukkit.getOnlinePlayers(), e.getPlayer(), Lang.of("%lang_message_format%"), message, format.getPrefix() + e.getPlayer().getName(), format.getChatColor());
            }
        }
    }

    @CubingEventHandler
    public void nickEvent(NickEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void skinEvent(SkinEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void toggleFlyEvent(ToggleFlyEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void languageChangeEvent(LanguageChangeEvent e) {
        EntityList.switchLanguage(e.getPlayer(), e.getOldLang(), e.getNewLang());
        SideBar bar = SideBar.getSidebar(e.getPlayer());
        if (bar != null) {
            String[] sidebar = StringList.sidebar[e.getNewLang()];
            bar.setLineByScore(sidebar[3], 10).setLineByScore(sidebar[6], 7).setLineByScore(sidebar[9], 4).setLineByScore(sidebar[12], 1);
        }
    }
}

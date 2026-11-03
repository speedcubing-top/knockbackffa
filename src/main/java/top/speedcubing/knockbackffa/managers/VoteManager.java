package top.speedcubing.knockbackffa.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.lib.minecraft.text.ComponentText;
import top.speedcubing.lib.minecraft.text.TextClickEvent;
import top.speedcubing.lib.minecraft.text.TextHoverEvent;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.player.User;
import top.speedcubing.server.speedcubingServer;

public class VoteManager {
    private static boolean voteStatus = false;
    private static final Map<UUID, String> playerVote = new HashMap<>();
    private static final Map<String, Integer> votesCast = new HashMap<>();

    public static boolean getVoteStatus() {
        return voteStatus;
    }

    public static void startHourlyVoteTask() {
        long ticksPerHour = 3600L * 20L;
        Bukkit.getScheduler().runTaskTimer(KnockBackFFA.instance, VoteManager::startVote, 0L, ticksPerHour);
    }

    public static void startVote() {
        setVoteStatus(true);
        Bukkit.getOnlinePlayers().forEach(player -> sendVoteInvite(player, "%lang_kbffa_vote_started%"));
        new BukkitRunnable() {
            @Override
            public void run() {
                int voteCount = getVoteCountByMapName(calculateVoteGetMapName());
                if (voteCount == 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        User.getUser(player).sendMessage("%lang_kbffa_vote_no_votes%");
                    });
                    return;
                }
                Bukkit.getOnlinePlayers().forEach(player -> {
                    User.getUser(player).sendMessage("%lang_kbffa_vote_result%", calculateVoteGetMapName(), Integer.toString(voteCount));
                });
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MapManager.getMapCommandByMapName(calculateVoteGetMapName()) + " " + calculateVoteGetMapName());
                endVote();
            }
        }.runTaskLater(speedcubingServer.getInstance(), 20 * 60);

        MapManager.getAllMapNames().forEach(map -> {
            votesCast.put(map, 0);
        });
    }

    /** header: language key of the first line, followed by a clickable "/vote" line */
    public static void sendVoteInvite(Player player, String header) {
        User.getUser(player).sendMessage(Lang.of(new ComponentText()
                .str(header)
                .both("\n%lang_kbffa_vote_click%", TextClickEvent.runCommand("/vote"), TextHoverEvent.showText("%lang_kbffa_vote_click_hover%"))));
    }

    public static void endVote() {
        setVoteStatus(false);
        playerVote.clear();
        votesCast.clear();
    }

    public static String calculateVoteGetMapName() {
        String maxVotedOption = null;
        int maxVotes = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : votesCast.entrySet()) {
            String option = entry.getKey();
            int votes = entry.getValue();
            if (votes > maxVotes) {
                maxVotes = votes;
                maxVotedOption = option;
            }
        }
        return maxVotedOption;
    }

    public static Integer getVoteCountByMapName(String mapName) {
        return votesCast.getOrDefault(mapName, null);
    }


    public static void vote(Player player, String option) {
        UUID uuid = player.getUniqueId();
        playerVote.put(uuid, option);
        votesCast.put(option, votesCast.get(option) + 1);
    }

    public static boolean isPlayerVoted(Player player) {
        return playerVote.containsKey(player.getUniqueId());
    }

    public static boolean isOptionsExist(String option) {
        return votesCast.containsKey(option);
    }

    private static void setVoteStatus(boolean bool) {
        voteStatus = bool;
    }

}

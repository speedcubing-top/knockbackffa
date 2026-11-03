package top.speedcubing.knockbackffa.games.tag.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import top.speedcubing.knockbackffa.games.tag.events.TagGameEndEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyDisbandEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyJoinEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyKickEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGameStartEvent;
import top.speedcubing.common.text.Msg;
import top.speedcubing.server.player.User;
import top.speedcubing.server.speedcubingServer;

public class TagGameCommand implements CommandExecutor {
    public static List<UUID> partyList = new ArrayList<>();
    public static boolean partyStatus = false;
    public static BukkitTask gameTask;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUuid = player.getUniqueId();
            User user = User.getUser(player);
            if (args.length == 0) {
                usage(player);
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "create":
                        if (!isInParty(playerUuid)) {
                            createParty(playerUuid);
                            user.sendMessage("%lang_kbffa_tag_party_created%");
                        } else {
                            user.sendMessage("%lang_kbffa_tag_already_have_party%");
                        }
                        break;
                    case "disband":
                        if (isInParty(playerUuid)) {
                            disbandParty();
                            user.sendMessage("%lang_kbffa_tag_party_disbanded_self%");
                            if (isGameRunning()) {
                                endGame();
                            }
                        } else {
                            user.sendMessage("%lang_kbffa_tag_not_in_party_create%");
                        }
                        break;
                    case "end":
                        if (isInParty(playerUuid)) {
                            if (isGameRunning()) {
                                endGame();
                            } else {
                                user.sendMessage("%lang_kbffa_tag_game_not_running%");
                            }
                        } else {
                            user.sendMessage("%lang_kbffa_tag_not_in_party%");
                        }
                        break;
                    case "list":
                        user.sendMessage("%lang_kbffa_tag_party_list%");
                        int index = 1;
                        for (Player players : getPartyPlayers(partyList)) {
                            player.sendMessage(Msg.INFO + index + ". " + Msg.VALUE + players.getName());
                            index++;
                        }
                        break;
                    case "forcejoin":
                        if (isInParty(playerUuid)) {
                            user.sendMessage("%lang_kbffa_tag_already_in_party%");
                            return true;
                        }
                        if (!partyList.isEmpty()) {
                            joinPlayerToParty(playerUuid);
                            user.sendMessage("%lang_kbffa_tag_party_joined%");
                        } else {
                            user.sendMessage("%lang_kbffa_tag_no_party%");
                        }
                        break;
                    case "leave":
                        if (!isInParty(playerUuid)) {
                            user.sendMessage("%lang_kbffa_tag_not_in_party%");
                            return true;
                        }
                        kickPlayerFromParty(playerUuid);
                        user.sendMessage("%lang_kbffa_tag_party_left%");
                        break;
                    case "join":
                        if (isInParty(playerUuid)) {
                            user.sendMessage("%lang_kbffa_tag_already_in_party%");
                            return true;
                        }
                        if (!partyStatus) {
                            user.sendMessage("%lang_kbffa_tag_party_not_open%");
                            return true;
                        }
                        if (!partyList.isEmpty()) {
                            joinPlayerToParty(playerUuid);
                            user.sendMessage("%lang_kbffa_tag_party_joined%");
                        } else {
                            user.sendMessage("%lang_kbffa_tag_no_party%");
                        }
                        break;
                    case "open":
                        if (!isInParty(playerUuid)) {
                            user.sendMessage("%lang_kbffa_tag_not_in_party%");
                            return true;
                        }
                        partyStatus = !partyStatus;
                        user.sendMessage(partyStatus ? "%lang_kbffa_tag_party_opened%" : "%lang_kbffa_tag_party_closed%");
                        break;
                    default:
                        usage(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("start")) {
                    String gameTime = args[1];

                    if (!isInt(gameTime)) {
                        user.sendMessage("%lang_kbffa_tag_usage%", "/taggame start <seconds>");
                        return true;
                    }
                    if (!isInParty(playerUuid)) {
                        user.sendMessage("%lang_kbffa_tag_not_in_party%");
                        return true;
                    }

                    if (isGameRunning()) {
                        user.sendMessage("%lang_kbffa_tag_game_already_running%");
                        return true;
                    }

                    if (Integer.parseInt(gameTime) < 60 || Integer.parseInt(gameTime) > 1800) {
                        user.sendMessage("%lang_kbffa_tag_invalid_game_time%", "60", "1800");
                        return true;
                    }

                    startGame(Integer.parseInt(gameTime));
                    user.sendMessage("%lang_kbffa_tag_game_starting%");
                } else if (args[0].equalsIgnoreCase("add")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);

                    if (target == null) {
                        user.sendMessage("%lang_kbffa_tag_player_not_found%");
                        return true;
                    }
                    if (target == player) {
                        user.sendMessage("%lang_kbffa_tag_cannot_add_self%");
                        return true;
                    }

                    UUID targetUuid = target.getUniqueId();
                    if (isInParty(targetUuid)) {
                        user.sendMessage("%lang_kbffa_tag_target_already_in_party%", target.getName());
                        return true;
                    }

                    joinPlayerToParty(targetUuid);
                    user.sendMessage("%lang_kbffa_tag_target_added%", target.getName());
                } else if (args[0].equalsIgnoreCase("kick")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);

                    if (target == null) {
                        user.sendMessage("%lang_kbffa_tag_player_not_found%");
                        return true;
                    }
                    if (target == player) {
                        user.sendMessage("%lang_kbffa_tag_cannot_kick_self%");
                        return true;
                    }

                    UUID targetUuid = target.getUniqueId();
                    if (!isInParty(targetUuid)) {
                        user.sendMessage("%lang_kbffa_tag_target_not_in_party%", target.getName());
                        return true;
                    }
                    kickPlayerFromParty(targetUuid);
                    user.sendMessage("%lang_kbffa_tag_target_kicked%", target.getName());
                }
            }
        }
        return true;
    }
    private void createParty(UUID uuid) {
        joinPlayerToParty(uuid);
    }
    private void disbandParty() {
        partyList.clear();
        TagGamePartyDisbandEvent tagGamePartyDisbandEvent = new TagGamePartyDisbandEvent(partyList);
        Bukkit.getPluginManager().callEvent(tagGamePartyDisbandEvent);
    }
    private void joinPlayerToParty(UUID uuid) {
        partyList.add(uuid);
        TagGamePartyJoinEvent tagGamePartyJoinEvent = new TagGamePartyJoinEvent(uuid);
        Bukkit.getPluginManager().callEvent(tagGamePartyJoinEvent);
    }
    private void kickPlayerFromParty(UUID uuid) {
        partyList.remove(uuid);
        TagGamePartyKickEvent tagGamePartyKickEvent = new TagGamePartyKickEvent(uuid);
        Bukkit.getPluginManager().callEvent(tagGamePartyKickEvent);
    }
    private void startGame(int gameTime) {
        TagGameStartEvent tagGameStartEvent = new TagGameStartEvent(partyList, gameTime);
        Bukkit.getPluginManager().callEvent(tagGameStartEvent);
        gameTask = new BukkitRunnable() {
            int timeLeft = gameTime;
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    if (timeLeft < 20) {
                        List<Player> players = getPartyPlayers(partyList);
                        for (Player player : players) {
                            User.getUser(player).sendMessage("%lang_kbffa_tag_countdown%", Integer.toString(timeLeft));
                        }
                    }
                } else {
                    endGame();
                }
            }
        }.runTaskTimer(speedcubingServer.getInstance(), 0, 20);
    }


    public static void endGame() {
        gameTask.cancel();
        gameTask = null;
        TagGameEndEvent tagGameEndEvent = new TagGameEndEvent(partyList);
        Bukkit.getPluginManager().callEvent(tagGameEndEvent);
    }
    public static boolean isGameRunning() {
        return gameTask != null;
    }


    private boolean isInParty(UUID uuid) {
        if (partyList.contains(uuid)) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static List<Player> getPartyPlayers(List<UUID> party) {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : party) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }
    private void usage(Player player) {
        User.getUser(player).sendMessage("%lang_kbffa_tag_help%", Msg.VALUE + "/taggame create\n" +
                Msg.VALUE + "/taggame disband\n" +
                Msg.VALUE + "/taggame end\n" +
                Msg.VALUE + "/taggame list\n" +
                Msg.VALUE + "/taggame forcejoin\n" +
                Msg.VALUE + "/taggame start <second>\n" +
                Msg.VALUE + "/taggame add <player>\n" +
                Msg.VALUE + "/taggame kick <player>");
    }
}

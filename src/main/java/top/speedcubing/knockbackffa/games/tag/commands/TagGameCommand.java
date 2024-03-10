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
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.games.tag.events.TagGameEndEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyDisbandEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyJoinEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyKickEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGameStartEvent;

public class TagGameCommand implements CommandExecutor {
    public static List<UUID> partyList = new ArrayList<>();
    public static boolean partyStatus = false;
    public static BukkitTask gameTask;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUuid = player.getUniqueId();
            if (args.length == 0) {
                usage(player);
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "create":
                        if (!isInParty(playerUuid)) {
                            createParty(playerUuid);
                            player.sendMessage("§6[Tag Game] §aSuccessfully created tag game party!");
                        } else {
                            player.sendMessage("§6[Tag Game] §cYou already have a party, please use /taggame disable to disable party");
                        }
                        break;
                    case "disband":
                        if (isInParty(playerUuid)) {
                            disbandParty();
                            player.sendMessage("§6[Tag Game] §aSuccessfully disband tag game party");
                            if (isGameRunning()) {
                                endGame();
                            }
                        } else {
                            player.sendMessage("§6[Tag Game] §cYou are not in party, please use /taggame create to create party");
                        }
                        break;
                    case "end":
                        if (isInParty(playerUuid)) {
                            if (isGameRunning()) {
                                endGame();
                            } else {
                                player.sendMessage("§[Tag Game] §cGame is not running");
                            }
                        } else {
                            player.sendMessage("§6[Tag Game] §cYou are not in party");
                        }
                        break;
                    case "list":
                        player.sendMessage("§6[Tag Game] §bParty List");
                        int index = 1;
                        for (Player players : getPartyPlayers(partyList)) {
                            player.sendMessage(index + ". " + players.getName());
                            index++;
                        }
                        break;
                    case "forcejoin":
                        if (isInParty(playerUuid)) {
                            player.sendMessage("§6[Tag Game] §cYou already in party");
                            return true;
                        }
                        if (!partyList.isEmpty()) {
                            joinPlayerToParty(playerUuid);
                            player.sendMessage("§6[Tag Game] §aSuccessfully forced join party");
                        } else {
                            player.sendMessage("§6[Tag Game] §cParty is null, please use /taggame create to create party");
                        }
                        break;
                    case "leave":
                        if (!isInParty(playerUuid)) {
                            player.sendMessage("§6[Tag Game] §cYou are not in the party");
                            return true;
                        }
                        kickPlayerFromParty(playerUuid);
                        player.sendMessage("§6[Tag Game] §aYou successfully left the party!");
                        break;
                    case "join":
                        if (isInParty(playerUuid)) {
                            player.sendMessage("§6[Tag Game] §cYou are already in a party!");
                            return true;
                        }
                        if (!partyStatus) {
                            player.sendMessage("§6[Tag Game] §cParty is not open!");
                            return true;
                        }
                        if (!partyList.isEmpty()) {
                            joinPlayerToParty(playerUuid);
                            player.sendMessage("§6[Tag Game] §aSuccessfully forced join party");
                        } else {
                            player.sendMessage("§6[Tag Game] §cParty is null, please use /taggame create to create party");
                        }
                        break;
                    case "open":
                        if (!isInParty(playerUuid)) {
                            player.sendMessage("§6[Tag Game] §cYou are not in the party");
                            return true;
                        }
                        partyStatus = !partyStatus;
                        player.sendMessage("§6[Tag Game] §bParty is now " + (partyStatus ? "§aOpen" : "§cClose"));
                        break;
                    default:
                        usage(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("start")) {
                    String gameTime = args[1];

                    if (!isInt(gameTime)) {
                        player.sendMessage("§6[Tag Game] §cUsage: /taggame start <GameTime(Integer)>");
                        return true;
                    }
                    if (!isInParty(playerUuid)) {
                        player.sendMessage("§6[Tag Game] §cYou are not in party");
                        return true;
                    }

                    if (isGameRunning()) {
                        player.sendMessage("§6[Tag Game] §cTag Game is running, please use /taggame end to end game");
                        return true;
                    }

                    if (Integer.parseInt(gameTime) < 60 || Integer.parseInt(gameTime) > 1800) {
                        player.sendMessage("§6[Tag Game] §cGame time must be between 60 and 1800");
                        return true;
                    }

                    startGame(Integer.parseInt(gameTime));
                    player.sendMessage("§6[Tag Game] §aStarting Game");
                } else if (args[0].equalsIgnoreCase("add")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);

                    if (target == null) {
                        player.sendMessage("§6[Tag Game] §cPlayer not found!");
                        return true;
                    }
                    if (target == player) {
                        player.sendMessage("§6[Tag Game] §cYou cannot add yourself");
                        return true;
                    }

                    UUID targetUuid = target.getUniqueId();
                    if (isInParty(targetUuid)) {
                        player.sendMessage("§6[Tag Game] §cPlayer is in party");
                        return true;
                    }

                    joinPlayerToParty(targetUuid);
                    player.sendMessage("§6[Tag Game] §aSuccessfully added " + target.getName() + "to the party");
                } else if (args[0].equalsIgnoreCase("kick")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);

                    if (target == null) {
                        player.sendMessage("§6[Tag Game] §cPlayer not found!");
                        return true;
                    }
                    if (target == player) {
                        player.sendMessage("§6[Tag Game] §cYou cannot kick yourself");
                        return true;
                    }

                    UUID targetUuid = target.getUniqueId();
                    if (!isInParty(targetUuid)) {
                        player.sendMessage("§6[Tag Game] §c" + target.getName() + " is not in party");
                        return true;
                    }
                    kickPlayerFromParty(targetUuid);
                    player.sendMessage("§6[Tag Game] §aSuccessfully kicked " + target.getName() + "from party");
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
                            player.sendMessage("§6[Tag Game] §9Countdown: " + timeLeft);
                        }
                    }
                } else {
                    endGame();
                }
            }
        }.runTaskTimer(KnockBackFFA.getInstance(), 0, 20);
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
        player.sendMessage("§6[Tag Game]\n" +
                "/taggame create\n" +
                "/taggame disband\n" +
                "/taggame end\n" +
                "/taggame list\n" +
                "/taggame forcejoin\n" +
                "/taggame start <second>\n" +
                "/taggame add <player>\n" +
                "/taggame kick <player>");
    }
}

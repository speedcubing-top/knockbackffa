package top.speedcubing.knockbackffa.games.tag.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.speedcubing.knockbackffa.games.tag.commands.TagGameCommand;
import top.speedcubing.knockbackffa.games.tag.events.*;
import top.speedcubing.server.player.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TagGameListener implements Listener {
    public static List<UUID> tagPlayer = new ArrayList<>();
    public static List<UUID> lastTag = new ArrayList<>();
    @EventHandler
    public void onPartyJoinParty(TagGamePartyJoinEvent e) {
        UUID uuid = e.getUuid();
        Player player = Bukkit.getPlayer(uuid);
        sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §9Player §6" + player.getName() + " §9joined the party");
    }
    @EventHandler
    public void onPartyKickParty(TagGamePartyKickEvent e) {
        UUID uuid = e.getUuid();
        Player player = Bukkit.getPlayer(uuid);
        sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §9Player §6" + player.getName() + " §9was kicked from party");
        if (tagPlayer.contains(uuid)) {
            tagPlayer.remove(uuid);
            Player newTag = getRandomPlayer(TagGameCommand.partyList);
            tagPlayer.add(newTag.getUniqueId());
            sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §9Tag §6" + player.getName() + " §9was kicked from party, new tag is §6" + newTag.getName());
        }
        if (lastTag.contains(uuid)) {
            lastTag.remove(uuid);
        }
    }
    @EventHandler
    public void onPartyDisband(TagGamePartyDisbandEvent e) {
        sendMessageToParty(e.getPartyList(),"§6[Tag Game] §9Party disbanded");
        lastTag.clear();
        tagPlayer.clear();
        if (TagGameCommand.isGameRunning()) {
            TagGameCommand.endGame();
        }
    }
    @EventHandler
    public void onGameStart(TagGameStartEvent e) {
        int gameLeft = e.getGameLeft();
        sendMessageToParty(e.getPartyList(),"§6[Tag Game] §9Game Start! CountDown: " + gameLeft + "s\n" +
                "Rules: 鬼抓人\n遊戲開始時，鬼會有10秒無法移動");
        Player ghost = getRandomPlayer(e.getPartyList());
        ghost.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,255,true));
        ghost.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,255,true));
        sendMessageToParty(e.getPartyList(),"§6[Tag Game] §9Player " + ghost.getName() + " is tag!");
        tagPlayer.add(ghost.getUniqueId());
    }
    @EventHandler
    public void onGhostChange(EntityDamageByEntityEvent e) {
        if (!TagGameCommand.isGameRunning()) {
            return;
        }
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        Player tag = (Player) e.getDamager();
        if (!TagGameCommand.partyList.contains(player.getUniqueId())) {
            tag.sendMessage("§6[Tag Game] §6" + player.getName() +  " §9is not in game");
            return;
        }
        if (tagPlayer.contains(tag.getUniqueId())) {
            if (!tagPlayer.contains(player.getUniqueId())) {
                if (!lastTag.contains(player.getUniqueId())) {
                    tagPlayer.remove(tag.getUniqueId());
                    tagPlayer.add(player.getUniqueId());
                    if (!lastTag.isEmpty()) {
                        lastTag.clear();
                    }
                    lastTag.add(tag.getUniqueId());
                    sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §6" + player.getName() + " §9is tag now!");
                    player.sendMessage("§6[Tag Game] §9You are tag now, hit other player!");
                } else {
                    tag.sendMessage("§6[Tag Game] " + player.getName() + " §9is last tag, you can't hit him");
                }
            }
        }
    }
    @EventHandler
    public void onGameEnd(TagGameEndEvent e) {
        sendMessageToParty(e.getPartyList(),"§6[Tag Game] §9Game Over");
        UUID uuid = tagPlayer.get(0);
        Player loser = Bukkit.getPlayer(uuid);
        sendMessageToParty(e.getPartyList(),"§6[Tag Game] " + loser.getName() + " §9Lose");
        loser.setHealth(0);
        tagPlayer.clear();
        lastTag.clear();
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!TagGameCommand.partyList.contains(p.getUniqueId())) {
            return;
        }
        TagGameCommand.partyList.remove(p.getUniqueId());
        sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §9Player §6" + p.getName() + " §9quit!");
        if (!tagPlayer.contains(p.getUniqueId())) {
            return;
        }
        tagPlayer.remove(p.getUniqueId());
        if (lastTag.contains(p.getUniqueId())) {
            lastTag.remove(p.getUniqueId());
        }
        Player newTag = getRandomPlayer(TagGameCommand.partyList);
        tagPlayer.add(newTag.getUniqueId());
        sendMessageToParty(TagGameCommand.partyList,"§6[Tag Game] §9Tag §6" + p.getName() + " §9quit, new tag is §6" + newTag.getName());

    }
    public static void sendMessageToParty(List<UUID> party, String message) {
        party.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        });
    }
    public static Player getRandomPlayer(List<UUID> players) {
        if (players.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(players.size());
        UUID uuid = players.get(index);
        return Bukkit.getPlayer(uuid);
    }
}

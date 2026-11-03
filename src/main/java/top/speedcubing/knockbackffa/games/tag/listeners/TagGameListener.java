package top.speedcubing.knockbackffa.games.tag.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.speedcubing.knockbackffa.games.tag.commands.TagGameCommand;
import top.speedcubing.knockbackffa.games.tag.events.TagGameEndEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyDisbandEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyJoinEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGamePartyKickEvent;
import top.speedcubing.knockbackffa.games.tag.events.TagGameStartEvent;
import top.speedcubing.server.player.User;

public class TagGameListener implements Listener {
    public static List<UUID> tagPlayer = new ArrayList<>();
    public static List<UUID> lastTag = new ArrayList<>();
    @EventHandler
    public void onPartyJoinParty(TagGamePartyJoinEvent e) {
        UUID uuid = e.getUuid();
        Player player = Bukkit.getPlayer(uuid);
        sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_member_joined%", player.getName());
    }
    @EventHandler
    public void onPartyKickParty(TagGamePartyKickEvent e) {
        UUID uuid = e.getUuid();
        Player player = Bukkit.getPlayer(uuid);
        sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_member_kicked%", player.getName());
        if (tagPlayer.contains(uuid)) {
            tagPlayer.remove(uuid);
            Player newTag = getRandomPlayer(TagGameCommand.partyList);
            tagPlayer.add(newTag.getUniqueId());
            sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_tagger_kicked%", player.getName(), newTag.getName());
        }
        lastTag.remove(uuid);
    }
    @EventHandler
    public void onPartyDisband(TagGamePartyDisbandEvent e) {
        sendMessageToParty(e.getPartyList(), "%lang_kbffa_tag_party_disbanded%");
        lastTag.clear();
        tagPlayer.clear();
        if (TagGameCommand.isGameRunning()) {
            TagGameCommand.endGame();
        }
    }
    @EventHandler
    public void onGameStart(TagGameStartEvent e) {
        int gameLeft = e.getGameLeft();
        sendMessageToParty(e.getPartyList(), "%lang_kbffa_tag_game_started%", Integer.toString(gameLeft));
        Player ghost = getRandomPlayer(e.getPartyList());
        ghost.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,255,true));
        ghost.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,255,true));
        sendMessageToParty(e.getPartyList(), "%lang_kbffa_tag_first_tagger%", ghost.getName());
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
            User.getUser(tag).sendMessage("%lang_kbffa_tag_target_not_in_game%", player.getName());
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
                    sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_new_tagger%", player.getName());
                    User.getUser(player).sendMessage("%lang_kbffa_tag_you_are_tagger%");
                } else {
                    User.getUser(tag).sendMessage("%lang_kbffa_tag_no_tag_back%", player.getName());
                }
            }
        }
    }
    @EventHandler
    public void onGameEnd(TagGameEndEvent e) {
        sendMessageToParty(e.getPartyList(), "%lang_kbffa_tag_game_over%");
        UUID uuid = tagPlayer.get(0);
        Player loser = Bukkit.getPlayer(uuid);
        sendMessageToParty(e.getPartyList(), "%lang_kbffa_tag_loser%", loser.getName());
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
        sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_member_quit%", p.getName());
        if (!tagPlayer.contains(p.getUniqueId())) {
            return;
        }
        tagPlayer.remove(p.getUniqueId());
        if (lastTag.contains(p.getUniqueId())) {
            lastTag.remove(p.getUniqueId());
        }
        Player newTag = getRandomPlayer(TagGameCommand.partyList);
        tagPlayer.add(newTag.getUniqueId());
        sendMessageToParty(TagGameCommand.partyList, "%lang_kbffa_tag_tagger_quit%", p.getName(), newTag.getName());

    }
    public static void sendMessageToParty(List<UUID> party, String message, String... param) {
        party.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                User.getUser(player).sendMessage(message, param);
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

package top.speedcubing.knockbackffa.cosmetics.main;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import top.speedcubing.server.player.User;

/**
 * Chat + sound feedback shared by the cosmetics selector GUIs.
 */
public final class CosmeticFeedback {

    private CosmeticFeedback() {
    }

    public static void selected(Player p, String item) {
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
        User.getUser(p).sendMessage("%lang_kbffa_cosmetic_selected%", item);
    }

    public static void alreadySelected(Player p) {
        deny(p, "%lang_kbffa_cosmetic_already_selected%");
    }

    public static void locked(Player p) {
        deny(p, "%lang_kbffa_cosmetic_locked%");
    }

    /** rank: coloured rank name, e.g. "§6PREMIUM" */
    public static void needRank(Player p, String rank) {
        deny(p, "%lang_kbffa_cosmetic_need_rank%", rank);
    }

    private static void deny(Player p, String key, String... param) {
        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
        User.getUser(p).sendMessage(key, param);
    }
}

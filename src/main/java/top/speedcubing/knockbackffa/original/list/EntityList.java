package top.speedcubing.knockbackffa.original.list;

import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.original.configuration.Configuration;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.knockbackffa.original.Tunnel;
import top.speedcubing.knockbackffa.utils.LangText;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.server.lang.LanguageSystem;
import top.speedcubing.server.player.User;

public class EntityList {
    // one hologram per language (index = language id); a player only listens to the one of their language
    public static final Hologram[] clickToggle = new Hologram[LanguageSystem.langCount];
    public static final Hologram[] toBlueTunnel = new Hologram[LanguageSystem.langCount];
    public static final Hologram[] toGreenTunnel = new Hologram[LanguageSystem.langCount];
    public static final Hologram[] personal = new Hologram[2];

    public static void init() {
        personal[0] = new Hologram("", false, false).
                world("map_kbffa").
                setLocation(Configuration.holoX, Configuration.centerY + 2.4, Configuration.holoZ, 0, 0).
                setClickEvent(KnockBackFFA.clickEvent);

        personal[1] = new Hologram("", false, false).
                world("map_kbffa").
                setLocation(Configuration.holoX, Configuration.centerY - 1.8, Configuration.holoZ, 0, 0).
                setClickEvent(KnockBackFFA.clickEvent);

        for (int lang = 0; lang < LanguageSystem.langCount; lang++) {
            clickToggle[lang] = new Hologram(LangText.str(lang, "%lang_kbffa_holo_click_toggle%"), false, true).
                    setLocation(Configuration.holoX, 148.5, Configuration.holoZ, 0, 0).
                    world("map_kbffa").
                    setClickEvent(KnockBackFFA.clickEvent);

            //blue
            toGreenTunnel[lang] = new Hologram(LangText.str(lang, "%lang_kbffa_holo_to_green_pipeline%"), false, true).
                    world("map_kbffa").
                    setLocation(30027.5, 98, 20034.5, 0, 0).
                    setClickEvent((r) -> Tunnel.tunnel(r.getPlayer()));

            //green
            toBlueTunnel[lang] = new Hologram(LangText.str(lang, "%lang_kbffa_holo_to_blue_pipeline%"), false, true).
                    world("map_kbffa").
                    setLocation(29973.5, 98, 20010.5, 0, 0).
                    setClickEvent((r) -> Tunnel.tunnel(r.getPlayer()));
        }
    }

    /**
     * Lets the player see the shared holograms in their language. Call before the library's MONITOR
     * join/world-change handler, which then spawns every auto-spawn hologram the player listens to.
     */
    public static void addListener(Player player) {
        int lang = User.getUser(player).lang;
        clickToggle[lang].addListener(player);
        toGreenTunnel[lang].addListener(player);
        toBlueTunnel[lang].addListener(player);
    }

    /** Replaces the player's shared holograms with the ones in their new language. */
    public static void switchLanguage(Player player, int oldLang, int newLang) {
        boolean visible = player.getWorld().getName().equals("map_kbffa");
        for (Hologram[] holograms : new Hologram[][]{clickToggle, toGreenTunnel, toBlueTunnel}) {
            if (visible)
                holograms[oldLang].setListener(player).despawn().undoSetListener();
            holograms[oldLang].removeListener(player);
            holograms[newLang].addListener(player);
            if (visible)
                holograms[newLang].setListener(player).spawn().undoSetListener();
        }
    }

    public static void respawn(Hologram[] holograms) {
        for (Hologram hologram : holograms) {
            hologram.despawn();
            hologram.spawn();
        }
    }
}

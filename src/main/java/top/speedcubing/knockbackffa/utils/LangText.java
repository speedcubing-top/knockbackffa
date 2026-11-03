package top.speedcubing.knockbackffa.utils;

import java.util.Arrays;
import java.util.List;
import top.speedcubing.server.lang.Lang;

/**
 * Resolves language keys to plain strings for item names, lores and inventory titles.
 */
public final class LangText {

    private LangText() {
    }

    public static String str(int lang, String key, String... param) {
        return Lang.of(key, param).getString(lang);
    }

    /** one lore entry per line of the translated value ("\n" separated) */
    public static List<String> lines(int lang, String key, String... param) {
        return Arrays.asList(str(lang, key, param).split("\n", -1));
    }
}

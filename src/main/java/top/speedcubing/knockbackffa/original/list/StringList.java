package top.speedcubing.knockbackffa.original.list;

import top.speedcubing.knockbackffa.utils.LangText;
import top.speedcubing.server.lang.LanguageSystem;

public class StringList {
    public static final String obj = "§c❤";

    public static String leaderboard = "§e%i%. %j% §7- §e%k%";

    /** sidebar lines per language id; the colour-only entries are unique blank lines / value prefixes */
    public static final String[][] sidebar = new String[LanguageSystem.langCount][];

    static {
        for (int lang = 0; lang < sidebar.length; lang++) {
            sidebar[lang] = new String[]{"§3KnockBackFFA", "§6speedcubing.top", "§3",
                    LangText.str(lang, "%lang_kbffa_sidebar_killstreak%"), "§6", "§0",
                    LangText.str(lang, "%lang_kbffa_sidebar_kills%"), "§e", "§1",
                    LangText.str(lang, "%lang_kbffa_sidebar_deaths%"), "§c", "§2",
                    LangText.str(lang, "%lang_kbffa_sidebar_killstreak_record%"), "§9"};
        }
    }
}

package top.speedcubing.knockbackffa;

import org.bukkit.command.*;
import top.speedcubing.server.libs.GlobalString;

public class configuration implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : Member.getMember(commandSender).user.lang]);
        reload();
        return true;
    }
    public void reload() {
        KnockBackFFA.getPlugin(KnockBackFFA.class).reloadConfig();
              centerX = KnockBackFFA.getPlugin(KnockBackFFA.class).getConfig().getDouble("x");
        offsetY = KnockBackFFA.getPlugin(KnockBackFFA.class).getConfig().getDouble("y");
        centerZ = KnockBackFFA.getPlugin(KnockBackFFA.class).getConfig().getDouble("z");
        yaw = KnockBackFFA.getPlugin(KnockBackFFA.class).getConfig().getFloat("yaw");
    }

    public static double centerX = 10000;
    public static double centerZ = -10000;
    public static double offsetY = 100;
    public static float yaw = 0;

}

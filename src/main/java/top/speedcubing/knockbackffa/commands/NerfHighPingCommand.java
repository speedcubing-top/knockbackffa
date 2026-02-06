package top.speedcubing.knockbackffa.commands;

import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;

public class NerfHighPingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ClassLoader loader = EntityLiving.class.getClassLoader();
        try {
            Class<?> coreNerfKnockback = Class.forName("top.speedcubing.knockbackffa.top.nontage.patchs.NerfKnockback", false, loader);
            Field nerfFrictionField = coreNerfKnockback.getDeclaredField("nerfFriction");
            Field nerfPingField = coreNerfKnockback.getDeclaredField("nerfPing");

            nerfFrictionField.setAccessible(true);
            nerfPingField.setAccessible(true);

            double nerfFriction = nerfFrictionField.getDouble(null);
            int nerfPing = nerfPingField.getInt(null);

            if (args.length != 2) {
                sender.sendMessage("Friction: " + nerfFriction + ", Ping: " + nerfPing);
            } else {
                if (args[0].equalsIgnoreCase("f")) {
                    double value = Double.parseDouble(args[1]);
                    nerfFrictionField.setDouble(null, value);
                    sender.sendMessage("Set Friction to " + value);
                } else if (args[0].equalsIgnoreCase("p")) {
                    int value = Integer.parseInt(args[1]);
                    nerfPingField.setInt(null, value);
                    sender.sendMessage("Set Ping to " + value);
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}

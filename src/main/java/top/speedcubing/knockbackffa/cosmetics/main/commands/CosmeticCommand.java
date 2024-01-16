package top.speedcubing.knockbackffa.cosmetics.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.KnockBackFFA;

public class CosmeticCommand implements CommandExecutor {
    private KnockBackFFA plugin;
    public CosmeticCommand(KnockBackFFA plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getGuiCosmeticsSelector().open(player);

        }
        return true;
    }
}

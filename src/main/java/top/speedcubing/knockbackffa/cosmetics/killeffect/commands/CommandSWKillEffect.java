package top.speedcubing.knockbackffa.cosmetics.killeffect.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.ConfigUtil;

public class CommandSWKillEffect implements CommandExecutor {

	private KnockBackFFA plugin;
	public String prefix = "§r";

	public CommandSWKillEffect(KnockBackFFA plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("gui")) {
				if (!(sender instanceof Player)) {
					return true;
				}
				Player p = (Player) sender;
				plugin.getGuiKillEffectSelector().open(p);
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.getKillEffectManager().saveKillEffects();
				plugin.reloadConfig();
				ConfigUtil.init(plugin);
				sender.sendMessage(prefix + "§aconfig.yml reloaded!");
				return true;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("gui")) {
				Player t = Bukkit.getPlayer(args[1]);
				if (t != null) {
					plugin.getGuiKillEffectSelector().open(t);
				}
				return true;
			}
		}
		sender.sendMessage("/ke gui [player] §2--- §bOpen Kill Effect selector");
		sender.sendMessage("/ke reload §2--- §bReloads config.yml");
		return true;
	}

}

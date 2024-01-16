package top.speedcubing.knockbackffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("s")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    gamemodeUpdateMessage(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("sp")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    gamemodeUpdateMessage(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("c")) {
                    player.setGameMode(GameMode.CREATIVE);
                    gamemodeUpdateMessage(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("a")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    gamemodeUpdateMessage(player);
                    return true;
                } else {
                    return false;
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("s")) {
                        target.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage("Updated " + target.getName() + " game mode");
                        gamemodeUpdateMessage(target);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("sp")) {
                        target.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage("Updated " + target.getName() + " game mode");
                        gamemodeUpdateMessage(target);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("c")) {
                        target.setGameMode(GameMode.CREATIVE);
                        player.sendMessage("Updated " + target.getName() + " game mode");
                        gamemodeUpdateMessage(target);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("a")) {
                        target.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage("Updated " + target.getName() + " game mode");
                        gamemodeUpdateMessage(target);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    private void gamemodeUpdateMessage(Player player) {
        player.sendMessage(ChatColor.WHITE + "Your game mode has been updated");
    }
}

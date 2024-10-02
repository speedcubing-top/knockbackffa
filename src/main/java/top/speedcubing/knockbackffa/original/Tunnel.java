package top.speedcubing.knockbackffa.original;

import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tunnel {

    public static void tunnel(Player player) {
        // true = at green
        Member member = Member.getMember(player);
        long l = System.currentTimeMillis();
        if (l - member.lastClick < 30000) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
            player.sendMessage("§cYou must wait another §e" + (30 - (l - member.lastClick) / 1000) + "s §cbefore using this!");
            return;
        }

        boolean b = EventListeners.isInRange(player, new Location(player.getWorld(), 29978.5, 98, 20005.5), new Location(player.getWorld(), 29968.5, 105, 20015.5));
        Location spawnLoc = b ? new Location(player.getWorld(), 29973.5, 108, 20010.5, player.getLocation().getYaw(), player.getLocation().getPitch()) : new Location(player.getWorld(), 30027.5, 108, 20034.5, player.getLocation().getYaw(), player.getLocation().getPitch());
        AtomicInteger stage = new AtomicInteger(0);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stage.get() == 0) {
                    player.setVelocity(new Vector(0, 0, 0));
                    player.teleport(spawnLoc);
                    member.isUsingTunnel = true;
                    stage.set(1);
                    return;
                }
                if (stage.get() != 0 && !member.isUsingTunnel) {
                    cancel();
                    return;
                }
                Location loc = player.getLocation();
                switch (stage.get()) {
                    case 1:
                        if (loc.getY() < 130) {
                            player.setVelocity(new Vector(0, 1.2, 0));
                        } else if (!b) {
                            player.setVelocity(new Vector(-1.8, 0, 0));
                            stage.set(2);
                        } else {
                            player.setVelocity(new Vector(0, 0, 1.8));
                            stage.set(2);
                        }
                        break;

                    case 2:
                        if (!b) {
                            if (loc.getX() > 29974.5) {
                                player.setVelocity(new Vector(-1.8, 0, 0));
                            } else {
                                player.setVelocity(new Vector(0, 0, -1.8));
                                stage.set(3);
                            }
                        } else {
                            if (loc.getZ() < 20033.5) {
                                player.setVelocity(new Vector(0, 0, 1.8));
                            } else {
                                player.setVelocity(new Vector(1.8, 0, 0));
                                stage.set(3);
                            }
                        }
                        break;

                    case 3:
                        if (!b) {
                            if (loc.getZ() > 20011.5) {
                                player.setVelocity(new Vector(0, 0, -1.8));
                            } else {
                                player.setVelocity(new Vector(0, -1, 0));
                                stage.set(4);
                            }
                        } else {
                            if (loc.getX() < 30026.5) {
                                player.setVelocity(new Vector(1.8, 0, 0));
                            } else {
                                player.setVelocity(new Vector(0, -1, 0));
                                stage.set(4);
                            }
                        }
                        break;

                    case 4:
                        if (loc.getY() > 106) {
                            player.setVelocity(new Vector(0, -1.2, 0));
                        } else {
                            member.isUsingTunnel = false;
                            player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 4, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                            cancel();
                        }
                        break;
                }
            }
        }.runTaskTimer(KnockBackFFA.instance, 0, 1);

        member.lastClick = l;
    }
}

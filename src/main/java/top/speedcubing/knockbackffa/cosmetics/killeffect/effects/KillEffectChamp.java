package top.speedcubing.knockbackffa.cosmetics.killeffect.effects;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.server.speedcubingServer;

public class KillEffectChamp extends KillEffect {
    public KillEffectChamp() {
        super("Champ", "CHAMP", Material.GOLD_HELMET);
    }

    @Override
    public void play(Player target) {
        for (int i = 0; i < 20; i++) {
            ItemStack goldHelmet = new ItemStack(Material.GOLD_HELMET);

            Item item = target.getWorld().dropItem(target.getLocation(), goldHelmet);

            double angle = Math.toRadians(i * (360 / 20.0));
            double radius = 1.5;
            double x = target.getLocation().getX() + radius * Math.cos(angle);
            double y = target.getLocation().getY() + 1.5;
            double z = target.getLocation().getZ() + radius * Math.sin(angle);

            item.teleport(new Vector(x, y, z).toLocation(target.getWorld()));

            item.setVelocity(new Vector(0,0.8,0));
            item.setPickupDelay(32767);
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (item.isOnGround()) {
                        item.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(speedcubingServer.instance,0,1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (item != null) {
                        item.remove();
                    }
                }
            }.runTaskLater(speedcubingServer.instance, 80);
        }
    }
}

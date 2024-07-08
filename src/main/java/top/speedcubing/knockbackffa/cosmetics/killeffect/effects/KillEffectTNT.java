package top.speedcubing.knockbackffa.cosmetics.killeffect.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.server.speedcubingServer;

public class KillEffectTNT extends KillEffect {
    public KillEffectTNT() {
        super("TNT","TNT", Material.TNT);
    }
    @Override
    public void play(Player target) {
        Location loc = target.getLocation();
        World world = loc.getWorld();
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(loc, EntityType.PRIMED_TNT);
        tnt.setMetadata("effectTNT", new FixedMetadataValue(speedcubingServer.getInstance(), true));
        tnt.setFuseTicks(60);
        tnt.setVelocity(new Vector(0,1.2,0));
    }
}

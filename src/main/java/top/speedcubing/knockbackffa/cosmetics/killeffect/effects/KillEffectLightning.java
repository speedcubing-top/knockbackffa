package top.speedcubing.knockbackffa.cosmetics.killeffect.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillEffectLightning extends KillEffect {
    public KillEffectLightning() {
        super("Lightning","LIGHTNING", Material.GLASS);
    }
    @Override
    public void play(Player p) {
        Location loc = p.getLocation();
        p.getWorld().strikeLightningEffect(loc);
    }
}

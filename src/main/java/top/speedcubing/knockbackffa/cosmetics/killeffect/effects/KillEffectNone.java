package top.speedcubing.knockbackffa.cosmetics.killeffect.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillEffectNone extends KillEffect {
    public KillEffectNone() {
        super("NONE", "NONE", Material.BARRIER);
    }

    @Override
    public void play(Player target) {

    }
}

package top.speedcubing.knockbackffa.cosmetics.killeffect.listeners;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.api.event.FFAPlayerDeathEvent;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;

public class KillListener implements Listener {

    private KnockBackFFA plugin;
    private String nmsVersion;

    public KillListener(KnockBackFFA plugin) {
        this.plugin = plugin;
        this.nmsVersion = plugin.getNmsVersion();
    }
    @EventHandler
    public void onFFADeath(FFAPlayerDeathEvent e) {
        Player p = e.getPlayer();
        KillEffectManager killEffectManager = plugin.getKillEffectManager();
        if (e.getKiller() != null) {
            Player k = e.getKiller();
            KillEffect killEffect = killEffectManager.getKillEffect(k);
            if (killEffect != null) {
                killEffect.play(p);
            }
            if (killEffectManager.isKillSoundEnabled()) {
                k.playSound(k.getLocation(), killEffectManager.getKillSound(), 1, killEffectManager.getKillSoundPitch());
            }
        }
        if (killEffectManager.isAutoRespawnEnabled()) {
            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> {
                        switch (nmsVersion) {
                            case "v1_8_R3":
                                ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) p).getHandle().playerConnection
                                        .a(new net.minecraft.server.v1_8_R3.PacketPlayInClientCommand(
                                                net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
                                break;
                            default:
                                break;
                        }
                    });
        }
    }

}

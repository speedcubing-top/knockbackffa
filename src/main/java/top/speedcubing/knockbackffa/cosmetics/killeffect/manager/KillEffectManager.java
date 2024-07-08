package top.speedcubing.knockbackffa.cosmetics.killeffect.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.api.events.KillEffectChangeEvent;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectBloodExplosion;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectChamp;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectFinalSmash;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectHeadRocket;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectHeartExplosion;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectLightning;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffectTNT;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.ConfigUtil;

public class KillEffectManager {

    private List<KillEffect> killEffects;
    private HashMap<String, KillEffect> killEffectMap;
    private KnockBackFFA plugin;

    /**
     * If you make particle for "Champ", you need modify GuiCosmeticsSelector
     */
    public KillEffectManager(KnockBackFFA plugin) {
        killEffects = new ArrayList<>();
        registerKillEffect(new KillEffectBloodExplosion());
        registerKillEffect(new KillEffectHeartExplosion());
        registerKillEffect(new KillEffectHeadRocket());
        registerKillEffect(new KillEffectFinalSmash());
        registerKillEffect(new KillEffectLightning());
        registerKillEffect(new KillEffectTNT());
        registerKillEffect(new KillEffectChamp());
        this.plugin = plugin;
        loadKillEffects();
    }

    public void loadKillEffects() {
        killEffectMap = new HashMap<>();
        FileConfiguration dataYml = plugin.getDataYml();
        for (String uuid : dataYml.getKeys(false)) {
            String effectId = dataYml.getString(uuid + ".selected-effect");
            killEffectMap.put(uuid, getKillEffectById(effectId));
        }
    }

    public void saveKillEffects() {
        FileConfiguration dataYml = plugin.getDataYml();
        for (String uuid : killEffectMap.keySet()) {
            dataYml.set(uuid + ".selected-effect", killEffectMap.get(uuid).getId());
        }
        plugin.saveDataYml();
    }

    public KillEffect getKillEffectByName(String name) {
        for (KillEffect killEffect : killEffects) {
            if (killEffect.getName().equalsIgnoreCase(name)) {
                return killEffect;
            }
        }
        return null;
    }

    public void registerKillEffect(KillEffect killEffect) {
        killEffects.add(killEffect);
    }

    public KillEffect getKillEffectById(String id) {
        for (KillEffect killEffect : killEffects) {
            if (killEffect.getId().equalsIgnoreCase(id)) {
                return killEffect;
            }
        }
        return null;
    }

    public KillEffect getKillEffect(Player p) {
        String uuid = p.getUniqueId().toString();
        if (killEffectMap.containsKey(uuid)) {
            return killEffectMap.get(uuid);
        }
        return null;
    }

    public void setKillEffect(Player p, KillEffect effect) {
        String uuid = p.getUniqueId().toString();
        if (killEffectMap.containsKey(uuid)) {
            killEffectMap.remove(uuid);
        }
        if (effect != null) {
            killEffectMap.put(uuid, effect);
        }
    }

    public void setKillEffect(Player p, String effectId) {
        String uuid = p.getUniqueId().toString();
        if (killEffectMap.containsKey(uuid)) {
            killEffectMap.remove(uuid);
        }
        KillEffect effect = getKillEffectById(effectId);
        if (effect != null) {
            killEffectMap.put(uuid, effect);
            KillEffectChangeEvent killEffectChangeEvent = new KillEffectChangeEvent(p, effectId);
            Bukkit.getPluginManager().callEvent(killEffectChangeEvent);
        }
    }

    public boolean hasSelected(Player p, KillEffect effect) {
        if (hasSelectedOne(p) && effect.equals(getKillEffect(p))) {
            return true;
        }
        return false;
    }

    public boolean hasSelected(Player p, String effectId) {
        if (hasSelectedOne(p) && getKillEffect(p).getId().contentEquals(effectId)) {
            return true;
        }
        return false;
    }


    public boolean isKillSoundEnabled() {
        return ConfigUtil.ENABLE_KILLSOUND;
    }

    public Sound getKillSound() {
        return ConfigUtil.KILLSOUND;
    }

    public float getKillSoundPitch() {
        return ConfigUtil.KILLSOUND_PITCH;
    }

    public boolean isAutoRespawnEnabled() {
        return ConfigUtil.ENABLE_AUTORESPAWN;
    }

    public boolean hasSelectedOne(Player p) {
        return getKillEffect(p) != null;
    }

}

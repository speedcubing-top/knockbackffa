package top.speedcubing.knockbackffa.projectile.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.projectile.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.projectile.effects.ProjectileEffectHeart;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectileEffectManager {
    private List<ProjectileEffect> projectileEffects;
    private HashMap<String, ProjectileEffect> projectileEffectMap;
    private KnockBackFFA plugin;

    public ProjectileEffectManager(KnockBackFFA plugin) {
        projectileEffects = new ArrayList<>();
        registerProjectileEffect(new ProjectileEffectHeart());
        this.plugin = plugin;
    }
    public void loadProjectileEffects() {
        FileConfiguration dataYml = plugin.getProjectileEffectDataYml();
        for (String uuid : dataYml.getKeys(false)) {
            String effectId = dataYml.getString(uuid + ".selected-effect");
            projectileEffectMap.put(uuid, getProjectileEffectById(effectId));
        }
    }
    public void saveProjectileEffects() {
        FileConfiguration dataYml = plugin.getProjectileEffectDataYml();
        for (String uuid : projectileEffectMap.keySet()) {
            dataYml.set(uuid + ".selected-effect", projectileEffectMap.get(uuid).getId());
        }
        plugin.saveProjectileEffectDataYml();
    }
    public ProjectileEffect getProjectileEffectById(String id) {
        for (ProjectileEffect projectileEffect : projectileEffects) {
            if (projectileEffect.getId().equalsIgnoreCase(id)) {
                return projectileEffect;
            }
        }
        return null;
    }
    public ProjectileEffect getProjectileEffect(Player p) {
        String uuid = p.getUniqueId().toString();
        if (projectileEffectMap.containsKey(uuid)) {
            return projectileEffectMap.get(uuid);
        }
        return null;
    }
    public void setKillEffect(Player p, String effectId) {
        String uuid = p.getUniqueId().toString();
        if (projectileEffectMap.containsKey(uuid)) {
            projectileEffectMap.remove(uuid);
        }
        ProjectileEffect effect = getProjectileEffectById(effectId);
        if (effect != null) {
            projectileEffectMap.put(uuid, effect);
        }
    }
    public boolean hasSelected(Player p, String effectId) {
        if (hasSelectedOne(p) && getProjectileEffect(p).getId().contentEquals(effectId)) {
            return true;
        }
        return false;
    }
    public boolean hasSelectedOne(Player p) {
        return getProjectileEffect(p) != null;
    }

    public void registerProjectileEffect(ProjectileEffect projectileEffect) {
        projectileEffects.add(projectileEffect);
    }
}

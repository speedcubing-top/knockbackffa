package top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.api.events.ProjectileEffectChangeEvent;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectileEffectManager {
    private List<ProjectileEffect> projectileEffects;
    private HashMap<String, ProjectileEffect> projectileEffectMap;
    private KnockBackFFA plugin;

    /**
     * If you make particle for "Champ", you need modify GuiCosmeticsSelector
     */

    public ProjectileEffectManager(KnockBackFFA plugin) {
        projectileEffects = new ArrayList<>();
        registerProjectileEffect(new ProjectileEffectHeart());
        registerProjectileEffect(new ProjectileEffectNote());
        registerProjectileEffect(new ProjectileEffectEgg());
        registerProjectileEffect(new ProjectileEffectChamp());
        this.plugin = plugin;
        loadProjectileEffects();
    }
    public void loadProjectileEffects() {
        projectileEffectMap = new HashMap<>();
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
    public void setProjectileEffect(Player p, String effectId) {
        String uuid = p.getUniqueId().toString();
        if (projectileEffectMap.containsKey(uuid)) {
            projectileEffectMap.remove(uuid);
        }
        ProjectileEffect effect = getProjectileEffectById(effectId);
        if (effect != null) {
            projectileEffectMap.put(uuid, effect);
            ProjectileEffectChangeEvent projectileEffectChangeEvent = new ProjectileEffectChangeEvent(p,effectId);
            Bukkit.getPluginManager().callEvent(projectileEffectChangeEvent);
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

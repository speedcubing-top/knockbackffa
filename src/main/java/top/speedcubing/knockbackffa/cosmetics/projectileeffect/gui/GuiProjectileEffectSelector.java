package top.speedcubing.knockbackffa.cosmetics.projectileeffect.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.cosmetics.main.CosmeticFeedback;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.knockbackffa.utils.LangText;
import top.speedcubing.server.player.User;

public class GuiProjectileEffectSelector implements Listener {

    /** Marks the projectile effect menu so clicks are recognised regardless of the (translated) title. */
    public static final class MenuHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }

    private record Entry(int slot, String id, String permission) {
    }

    private static final Entry[] EFFECTS = {
            new Entry(12, "HEART_PARTICLE", "projectileeffect.heart"),
            new Entry(13, "NOTE_PARTICLE", "projectileeffect.note"),
            new Entry(14, "EGG_PARTICLE", "projectileeffect.egg"),
            new Entry(15, "CHAMP", "projectileeffect.champ")
    };

    private final KnockBackFFA plugin;
    private final ProjectileEffectManager projectileEffectManager;

    public GuiProjectileEffectSelector(KnockBackFFA plugin, ProjectileEffectManager projectileEffectManager) {
        this.plugin = plugin;
        this.projectileEffectManager = projectileEffectManager;
    }

    /** translated display name of a projectile effect id, e.g. "HEART_PARTICLE" */
    public static String effectName(int lang, String id) {
        return LangText.str(lang, "%lang_kbffa_projectileeffect_" + id.toLowerCase() + "%");
    }

    public void open(Player p) {
        User user = User.getUser(p);
        int lang = user.lang;
        Inventory inventory = Bukkit.createInventory(new MenuHolder(), 54, LangText.str(lang, "%lang_kbffa_projectileeffect_menu_title%"));

        List<String> noneLore = new ArrayList<>(LangText.lines(lang, "%lang_kbffa_projectileeffect_none_lore%"));
        noneLore.add("");
        noneLore.add(LangText.str(lang, !projectileEffectManager.hasSelected(p, "NONE") ? "%lang_server_status_click_select%" : "%lang_kbffa_cosmetic_selected_lore%"));
        inventory.setItem(11, new CustomItem(Material.BARRIER, 1, "§a" + effectName(lang, "NONE"), noneLore));

        for (Entry entry : EFFECTS) {
            boolean unlocked = p.hasPermission(entry.permission()) || user.hasPermission(entry.permission());
            String name = effectName(lang, entry.id());
            List<String> lore = new ArrayList<>(LangText.lines(lang, "%lang_kbffa_projectileeffect_item_lore%", name));
            lore.add("");
            lore.add(LangText.str(lang, projectileEffectManager.hasSelected(p, entry.id()) ? "%lang_kbffa_cosmetic_selected_lore%"
                    : unlocked ? "%lang_server_status_click_select%" : "%lang_server_status_locked%"));
            inventory.setItem(entry.slot(), new CustomItem(projectileEffectManager.getProjectileEffectById(entry.id()).getIcon(), 1,
                    LangText.str(lang, "%lang_kbffa_projectileeffect_item_name%", name), lore));
        }

        inventory.setItem(45, new CustomItem(Material.ARROW, 1, LangText.str(lang, "%lang_kbffa_menu_back%"),
                LangText.str(lang, "%lang_kbffa_menu_back_lore%")));
        inventory.setItem(49, new CustomItem(Material.BARRIER, 1, LangText.str(lang, "%lang_kbffa_menu_close%")));
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = User.getUser(p);
        Inventory top = p.getOpenInventory().getTopInventory();
        if (top == null || !(top.getHolder() instanceof MenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (!e.getClickedInventory().equals(top)) {
            return;
        }
        int slot = e.getSlot();
        if (slot == 49) {
            p.closeInventory();
        }
        if (slot == 45) {
            plugin.getGuiCosmeticsSelector().open(p);
        }
        if (slot == 11) {
            if (projectileEffectManager.hasSelectedOne(p)) {
                projectileEffectManager.setProjectileEffect(p, "NONE");
                open(p);
                CosmeticFeedback.selected(p, effectName(user.lang, "NONE"));
            } else {
                CosmeticFeedback.alreadySelected(p);
            }
        }
        for (Entry entry : EFFECTS) {
            if (slot != entry.slot()) {
                continue;
            }
            if (p.hasPermission(entry.permission()) || user.hasPermission(entry.permission())) {
                if (!projectileEffectManager.hasSelected(p, entry.id())) {
                    projectileEffectManager.setProjectileEffect(p, entry.id());
                    open(p);
                    CosmeticFeedback.selected(p, LangText.str(user.lang, "%lang_kbffa_projectileeffect_item_name%", effectName(user.lang, entry.id())));
                } else {
                    CosmeticFeedback.alreadySelected(p);
                }
            } else {
                CosmeticFeedback.locked(p);
            }
        }
    }
}

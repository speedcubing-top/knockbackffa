package top.speedcubing.knockbackffa.cosmetics.main.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.knockbackffa.original.Member;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.gui.GuiKillEffectSelector;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.cosmetics.main.CosmeticFeedback;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.gui.GuiProjectileEffectSelector;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.knockbackffa.utils.LangText;
import top.speedcubing.lib.bukkit.inventory.InventoryBuilder;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.minecraft.text.ComponentText;
import top.speedcubing.lib.minecraft.text.TextClickEvent;
import top.speedcubing.lib.minecraft.text.TextHoverEvent;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.player.User;

public class GuiCosmeticsSelector implements Listener {

    /** Marks the cosmetics menu so clicks are recognised regardless of the (translated) title. */
    public static final class MenuHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }

    private record Weapon(Material material, String key, int slot) {
    }

    private static final Weapon[] WEAPONS = {
            new Weapon(Material.WOOD_SWORD, "%lang_kbffa_weapon_wood_sword%", 10),
            new Weapon(Material.STONE_SWORD, "%lang_kbffa_weapon_stone_sword%", 11),
            new Weapon(Material.IRON_SWORD, "%lang_kbffa_weapon_iron_sword%", 12),
            new Weapon(Material.GOLD_SWORD, "%lang_kbffa_weapon_gold_sword%", 13),
            new Weapon(Material.DIAMOND_SWORD, "%lang_kbffa_weapon_diamond_sword%", 14),
            new Weapon(Material.APPLE, "%lang_kbffa_weapon_apple%", 15),
            new Weapon(Material.GOLDEN_APPLE, "%lang_kbffa_weapon_golden_apple%", 16),
            new Weapon(Material.WOOD_AXE, "%lang_kbffa_weapon_wood_axe%", 19),
            new Weapon(Material.STONE_AXE, "%lang_kbffa_weapon_stone_axe%", 20),
            new Weapon(Material.IRON_AXE, "%lang_kbffa_weapon_iron_axe%", 21),
            new Weapon(Material.GOLD_AXE, "%lang_kbffa_weapon_gold_axe%", 22),
            new Weapon(Material.DIAMOND_AXE, "%lang_kbffa_weapon_diamond_axe%", 23)
    };

    private static final Material[] ARMOR_PIECES = {Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
    private static final String[] ARMOR_LORE_KEYS = {"%lang_kbffa_armor_lore_helmet%", "%lang_kbffa_armor_lore_chestplate%", "%lang_kbffa_armor_lore_leggings%", "%lang_kbffa_armor_lore_boots%"};

    private final KnockBackFFA plugin;
    private final KillEffectManager killEffectManager;
    private final ProjectileEffectManager projectileEffectManager;

    public GuiCosmeticsSelector(KnockBackFFA plugin, KillEffectManager killEffectManager, ProjectileEffectManager projectileEffectManager) {
        this.plugin = plugin;
        this.killEffectManager = killEffectManager;
        this.projectileEffectManager = projectileEffectManager;
    }

    public void open(Player p) {
        int lang = User.getUser(p).lang;
        Inventory inventory = Bukkit.createInventory(new MenuHolder(), 54, LangText.str(lang, "%lang_kbffa_cosmetic_menu_title%"));
        ItemStack AXE = new CustomItem(Material.DIAMOND_AXE, 1, LangText.str(lang, "%lang_kbffa_cosmetic_weapon_selector%"),
                LangText.lines(lang, "%lang_kbffa_cosmetic_weapon_selector_lore%"));
        ItemMeta meta1 = AXE.getItemMeta();
        meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        AXE.setItemMeta(meta1);
        inventory.setItem(13, AXE);
        KillEffect killEffect = killEffectManager.getKillEffect(p);
        String killEffectId = killEffect != null ? killEffect.getId() : "NONE";
        if (isChampOrStaff(p)) {
            if (killEffect != null) {
                if (killEffect.getId().equals("CHAMP")) {
                    killEffectManager.setKillEffect(p, "NONE");
                    killEffectId = "NONE";
                }
            }
        }

        ItemStack KILL_SELECTOR = new CustomItem(Material.IRON_SWORD, 1, LangText.str(lang, "%lang_kbffa_cosmetic_kill_effects%"),
                LangText.lines(lang, "%lang_kbffa_cosmetic_kill_effects_lore%", GuiKillEffectSelector.effectName(lang, killEffectId)));
        ItemMeta meta = KILL_SELECTOR.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        KILL_SELECTOR.setItemMeta(meta);
        inventory.setItem(21, KILL_SELECTOR);

        ItemStack ARMOR = new CustomItem(Material.LEATHER_CHESTPLATE, 1, LangText.str(lang, "%lang_kbffa_cosmetic_armor_selector%"),
                LangText.lines(lang, "%lang_kbffa_cosmetic_armor_selector_lore%"));
        ItemMeta meta2 = ARMOR.getItemMeta();
        meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ARMOR.setItemMeta(meta2);
        inventory.setItem(22, ARMOR);

        ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(p);
        String projectileEffectId = projectileEffect != null ? projectileEffect.getId() : "NONE";
        if (isChampOrStaff(p)) {
            if (projectileEffect != null) {
                if (projectileEffect.getId().equals("CHAMP")) {
                    projectileEffectManager.setProjectileEffect(p, "NONE");
                    projectileEffectId = "NONE";
                }
            }
        }
        ItemStack PROJECTILE_SELECTOR = new CustomItem(Material.ARROW, 1, LangText.str(lang, "%lang_kbffa_cosmetic_projectile_effects%"),
                LangText.lines(lang, "%lang_kbffa_cosmetic_projectile_effects_lore%", GuiProjectileEffectSelector.effectName(lang, projectileEffectId)));
        inventory.setItem(23, PROJECTILE_SELECTOR);

        ItemStack close = new CustomItem(Material.BARRIER, 1, LangText.str(lang, "%lang_kbffa_menu_close%"));
        inventory.setItem(49, close);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory top = p.getOpenInventory().getTopInventory();
        if (top == null || !(top.getHolder() instanceof MenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (e.getClickedInventory().equals(top)) {
            if (e.getSlot() == 49) {
                p.closeInventory();
            }
            if (e.getSlot() == 13) {
                openWeaponSelector(p);
            }
            if (e.getSlot() == 21) {
                plugin.getGuiKillEffectSelector().open(p);
            }
            if (e.getSlot() == 22) {
                openArmorSelector(p);
            }
            if (e.getSlot() == 23) {
                plugin.getGuiProjectileEffectSelector().open(p);
            }
        }
    }

    private void openWeaponSelector(Player p) {
        int lang = User.getUser(p).lang;
        InventoryBuilder inv = new InventoryBuilder(54, LangText.str(lang, "%lang_kbffa_weapon_menu_title%"));
        ItemStack grayGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(7).build();
        int id = Member.getMember(p).weaponId;
        for (Weapon weapon : WEAPONS) {
            String name = LangText.str(lang, weapon.key());
            inv.setItem(weaponGenerator(lang, id, weapon.material(), name), (ex) -> selectWeapon(p, weapon.material(), name), weapon.slot());
        }

        for (int i = 0; i < 9; i++) {
            inv.setItem(grayGlass, i);
            inv.setItem(grayGlass, i + 45);
        }
        for (int i = 0; i < 6; i++) {
            inv.setItem(grayGlass, i * 9);
            inv.setItem(grayGlass, i * 9 + 8);
        }
        inv.setItem(new ItemBuilder(Material.ARROW).name(LangText.str(lang, "%lang_kbffa_menu_back%")).lore(LangText.str(lang, "%lang_kbffa_menu_back_lore%")).build(), (ex) -> open(p), 45);
        inv.setItem(new ItemBuilder(Material.BARRIER).name(LangText.str(lang, "%lang_kbffa_menu_close%")).build(), (ex) -> p.closeInventory(), 49);
        inv.setClickable(false);
        p.openInventory(inv.getInventory());
    }

    private void openArmorSelector(Player p) {
        int lang = User.getUser(p).lang;
        String armorColor = Member.getMember(p).armorColor.equals("null") ? "default" : Member.getMember(p).armorColor;
        boolean hasColor = !armorColor.equals("default") && !armorColor.isEmpty();
        String[] colors = hasColor ? armorColor.split("\\|") : null;
        InventoryBuilder builder = new InventoryBuilder(9, LangText.str(lang, "%lang_kbffa_armor_menu_title%"));
        for (int i = 0; i < ARMOR_PIECES.length; i++) {
            String shownColor = hasColor ? colors[i] : LangText.str(lang, "%lang_kbffa_armor_color_default%");
            ItemStack piece = new ItemBuilder(ARMOR_PIECES[i]).addLore(LangText.lines(lang, ARMOR_LORE_KEYS[i], shownColor)).build();
            if (hasColor) {
                ItemMeta pieceMeta = piece.getItemMeta();
                String[] rgb = colors[i].split(",");
                ((LeatherArmorMeta) pieceMeta).setColor(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                piece.setItemMeta(pieceMeta);
            }
            int armorSlot = i + 1;
            builder.setItem(piece, (ex) -> {
                if (!User.getUser(p).hasPermission("cmd.setarmorcolor")) {
                    CosmeticFeedback.needRank(p, "§dVIP+");
                    return;
                }
                if (ex.getEvent().isLeftClick()) {
                    sendArmorColorHint(p, armorSlot);
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                    p.closeInventory();
                } else if (ex.getEvent().isRightClick()) {
                    p.performCommand("setarmorcolor " + armorSlot + " 160,101,64");
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                    openArmorSelector(p);
                }
            }, i);
        }

        ItemStack back = new ItemBuilder(Material.ARROW).name(LangText.str(lang, "%lang_kbffa_menu_back%")).lore(LangText.str(lang, "%lang_kbffa_menu_back_lore%")).build();
        ItemStack close = new ItemBuilder(Material.BARRIER).name(LangText.str(lang, "%lang_kbffa_menu_close%")).build();
        builder.setItem(back, (ex) -> plugin.getGuiCosmeticsSelector().open(p), 7);
        builder.setItem(close, (ex) -> p.closeInventory(), 8);
        builder.setClickable(false);
        p.openInventory(builder.getInventory());
    }

    private void selectWeapon(Player p, Material weapon, String name) {
        if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
            CosmeticFeedback.needRank(p, "§6PREMIUM");
            return;
        }
        Member.getMember(p).weaponId = weapon.getId();
        CosmeticFeedback.selected(p, name);
        openWeaponSelector(p);
    }

    private static void sendArmorColorHint(Player p, int armorSlot) {
        User.getUser(p).sendMessage(Lang.of(new ComponentText().both("%lang_kbffa_armor_color_hint%"
                , TextClickEvent.suggestCommand("/setarmorcolor " + armorSlot + " <R>,<G>,<B>")
                , TextHoverEvent.showText("%lang_kbffa_armor_color_hint_hover%"))));
    }

    private static boolean isChampOrStaff(Player p) {
        User user = User.getUser(p);
        return !user.isStaff && !"champ".equals(user.displayRank) && !"champ".equals(user.realRank);
    }

    /** name: translated weapon name without colour */
    private static ItemStack weaponGenerator(int lang, int weaponId, Material weapon, String name) {
        boolean selected = weaponId == weapon.getId();
        List<String> lore = new ArrayList<>(LangText.lines(lang, "%lang_kbffa_weapon_lore%", "§f" + name));
        lore.add("");
        lore.add(LangText.str(lang, selected ? "%lang_kbffa_cosmetic_selected_lore%" : "%lang_server_status_click_select%"));
        ItemBuilder builder = new ItemBuilder(weapon).name("§f" + name).lore(lore);
        if (selected) {
            builder.glow();
        }
        return builder.hideAttr().build();
    }

}

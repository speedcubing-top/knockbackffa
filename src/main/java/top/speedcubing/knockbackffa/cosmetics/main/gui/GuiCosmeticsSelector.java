package top.speedcubing.knockbackffa.cosmetics.main.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.knockbackffa.original.Member;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.CustomItem;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.lib.bukkit.inventory.InventoryBuilder;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.minecraft.text.ComponentText;
import top.speedcubing.lib.minecraft.text.TextClickEvent;
import top.speedcubing.lib.minecraft.text.TextHoverEvent;
import top.speedcubing.server.player.User;

public class GuiCosmeticsSelector implements Listener {
    private final KnockBackFFA plugin;
    private final KillEffectManager killEffectManager;
    private final ProjectileEffectManager projectileEffectManager;

    public GuiCosmeticsSelector(KnockBackFFA plugin, KillEffectManager killEffectManager, ProjectileEffectManager projectileEffectManager) {
        this.plugin = plugin;
        this.killEffectManager = killEffectManager;
        this.projectileEffectManager = projectileEffectManager;
    }

    public void open(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Cosmetics");
        ItemStack AXE = new CustomItem(Material.DIAMOND_AXE, 1, "§aWeapon Selector",
                "§7Click this option",
                "§7change to Weapon Selector.");
        ItemMeta meta1 = AXE.getItemMeta();
        meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        AXE.setItemMeta(meta1);
        inventory.setItem(13, AXE);
        KillEffect killEffect = killEffectManager.getKillEffect(p);
        String killEffectName;
        if (killEffect != null) {
            killEffectName = killEffect.getName();
        } else {
            killEffectName = "NONE";
        }
        if (!isChampOrStaff(p)) {
            if (killEffect != null) {
                if (killEffect.getId().equals("CHAMP")) {
                    killEffectManager.setKillEffect(p, "NONE");
                    killEffectName = "NONE";
                }
            }
        }


        ItemStack KILL_SELECTOR = new CustomItem(Material.IRON_SWORD, 1, "§aKill Effects",
                "§7Click this option",
                "§7change to Kill Effects Selector.",
                "",
                "§7Current Selected:",
                "§a" + killEffectName,
                "",
                "§eClick to view!");
        ItemMeta meta = KILL_SELECTOR.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        KILL_SELECTOR.setItemMeta(meta);
        inventory.setItem(21, KILL_SELECTOR);

        ItemStack ARMOR = new CustomItem(Material.LEATHER_CHESTPLATE, 1, "§aArmor Color Selector",
                "§7Click this option",
                "§7change to Armor Selector.");
        ItemMeta meta2 = ARMOR.getItemMeta();
        meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ARMOR.setItemMeta(meta2);
        inventory.setItem(22, ARMOR);

        ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(p);
        String projectileEffectName;
        if (projectileEffect != null) {
            projectileEffectName = projectileEffect.getName();
        } else {
            projectileEffectName = "NONE";
        }
        if (!isChampOrStaff(p)) {
            if (projectileEffect != null) {
                if (projectileEffect.getId().equals("CHAMP")) {
                    projectileEffectManager.setProjectileEffect(p, "NONE");
                    projectileEffectName = "NONE";
                }
            }
        }
        ItemStack PROJECTILE_SELECTOR = new CustomItem(Material.ARROW, 1, "§aProjectile Effects",
                "§7Click this option",
                "§7change to Projectile Effects",
                "§7Selector.",
                "",
                "§7Current Selected:",
                "§a" + projectileEffectName,
                "",
                "§eClick to view!");
        inventory.setItem(23, PROJECTILE_SELECTOR);

        ItemStack close = new CustomItem(Material.BARRIER, 1, "§cClose");
        inventory.setItem(49, close);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory() != null) {
            if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("Cosmetics")) {
                e.setCancelled(true);
                if (e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || e.getCurrentItem().getType().equals(Material.AIR)) {
                    return;
                }
                if (e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                    e.setCancelled(true);
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
        }
    }

    private void openWeaponSelector(Player p) {
        InventoryBuilder inv = new InventoryBuilder(54, "Weapon Selector");
        ItemStack grayGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(7).build();
        int id = Member.getMember(p).weaponId;
        ItemStack woodSword = weaponGenerator(id, Material.WOOD_SWORD, "§fWood Sword");
        ItemStack stoneSword = weaponGenerator(id, Material.STONE_SWORD, "§fStone Sword");
        ItemStack ironSword = weaponGenerator(id, Material.IRON_SWORD, "§fIron Sword");
        ItemStack goldenSwrod = weaponGenerator(id, Material.GOLD_SWORD, "§fGolden Sword");
        ItemStack diamondSword = weaponGenerator(id, Material.DIAMOND_SWORD, "§fDiamond Sword");
        ItemStack apple = weaponGenerator(id, Material.APPLE, "§fApple Sword");
        ItemStack goldenApple = weaponGenerator(id, Material.GOLDEN_APPLE, "§fGolden Apple Sword");
        ItemStack woodAxe = weaponGenerator(id, Material.WOOD_AXE, "§fWood Axe");
        ItemStack stoneAxe = weaponGenerator(id, Material.STONE_AXE, "§fStone Axe");
        ItemStack ironAxe = weaponGenerator(id, Material.IRON_AXE, "§fIron Axe");
        ItemStack goldenAxel = weaponGenerator(id, Material.GOLD_AXE, "§fGolden Axe");
        ItemStack diamondAxel = weaponGenerator(id, Material.DIAMOND_AXE, "§fDiamond Axe");


        inv.setItem(woodSword, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.WOOD_SWORD.getId();
            p.sendMessage("§aSelected Wood Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 10);

        inv.setItem(stoneSword, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.STONE_SWORD.getId();
            p.sendMessage("§aSelected Stone Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 11);

        inv.setItem(ironSword, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.IRON_SWORD.getId();
            p.sendMessage("§aSelected Iron Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 12);

        inv.setItem(goldenSwrod, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.GOLD_SWORD.getId();
            p.sendMessage("§aSelected Golden Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 13);

        inv.setItem(diamondSword, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.DIAMOND_SWORD.getId();
            p.sendMessage("§aSelected Diamond Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 14);

        inv.setItem(apple, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.APPLE.getId();
            p.sendMessage("§aSelected Apple Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 15);

        inv.setItem(goldenApple, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.GOLDEN_APPLE.getId();
            p.sendMessage("§aSelected Golden Apple Sword!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 16);

        inv.setItem(woodAxe, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.WOOD_AXE.getId();
            p.sendMessage("§aSelected Wood Axe!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 19);

        inv.setItem(stoneAxe, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.STONE_AXE.getId();
            p.sendMessage("§aSelected Stone Axe!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 20);

        inv.setItem(ironAxe, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.IRON_AXE.getId();
            p.sendMessage("§aSelected Iron Axe!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 21);

        inv.setItem(goldenAxel, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.GOLD_AXE.getId();
            p.sendMessage("§aSelected Golden Axe!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 22);

        inv.setItem(diamondAxel, (ex) -> {
            if (!User.getUser(p).hasPermission("knockbackffa.setweapon") && !User.getUser(p).isStaff && !User.getUser(p).hasPermission("perms.*")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §6PREMIUM §cor higher rank to use this");
                return;
            }
            Member.getMember(p).weaponId = Material.DIAMOND_AXE.getId();
            p.sendMessage("§aSelected Diamond Axe!");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
            openWeaponSelector(p);
        }, 23);


        for (int i = 0; i < 9; i++) {
            inv.setItem(grayGlass, i);
            inv.setItem(grayGlass, i + 45);
        }
        for (int i = 0; i < 6; i++) {
            inv.setItem(grayGlass, i * 9);
            inv.setItem(grayGlass, i * 9 + 8);
        }
        inv.setItem(new ItemBuilder(Material.ARROW).name("§aGo Back").lore("§7Back to Cosmetics Menu").build(), (ex) -> open(p), 45);
        inv.setItem(new ItemBuilder(Material.BARRIER).name("§cClose").build(), (ex) -> p.closeInventory(), 49);
        inv.setClickable(false);
        p.openInventory(inv.getInventory());
    }

    private void openArmorSelector(Player p) {
        String armorColor = Member.getMember(p).armorColor.equals("null") ? "default" : Member.getMember(p).armorColor;
        boolean hasColor = !armorColor.equals("default") && !armorColor.isEmpty();
        InventoryBuilder builder = new InventoryBuilder(9, "Armor Color Selector");
        ItemStack helmet;
        ItemStack chestplate;
        ItemStack leggings;
        ItemStack boots;
        ItemStack back = new ItemBuilder(Material.ARROW).name("§aGo Back").lore("§7Back to Cosmetics Menu").build();
        ItemStack close = new ItemBuilder(Material.BARRIER).name("§cClose").build();
        if (hasColor) {
            String[] colors = armorColor.split("\\|");
            helmet = new ItemBuilder(Material.LEATHER_HELMET).addLore("§7Left Click to change your helmet color."
                    , "§7Current Color: §a" + colors[0],
                    "§7Right Click to reset this armor").build();
            ItemMeta helmetMeta = helmet.getItemMeta();
            String[] helmetColor = colors[0].split(",");
            int red1 = Integer.parseInt(helmetColor[0]);
            int green1 = Integer.parseInt(helmetColor[1]);
            int blue1 = Integer.parseInt(helmetColor[2]);
            ((LeatherArmorMeta) helmetMeta).setColor(Color.fromRGB(red1, green1, blue1));
            helmet.setItemMeta(helmetMeta);

            chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).addLore("§7Left Click to change your chestplate color."
                    , "§7Current Color: §a" + colors[1],
                    "§7Right Click to reset this armor").build();
            ItemMeta chestplateMeta = chestplate.getItemMeta();
            String[] chestplateColor = colors[1].split(",");
            int red2 = Integer.parseInt(chestplateColor[0]);
            int green2 = Integer.parseInt(chestplateColor[1]);
            int blue2 = Integer.parseInt(chestplateColor[2]);
            ((LeatherArmorMeta) chestplateMeta).setColor(Color.fromRGB(red2, green2, blue2));
            chestplate.setItemMeta(chestplateMeta);

            leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).addLore("§7Left Click to change your leggings color."
                    , "§7Current Color: §a" + colors[2],
                    "§7Right Click to reset this armor").build();
            ItemMeta leggingsMeta = leggings.getItemMeta();
            String[] leggingsColor = colors[2].split(",");
            int red3 = Integer.parseInt(leggingsColor[0]);
            int green3 = Integer.parseInt(leggingsColor[1]);
            int blue3 = Integer.parseInt(leggingsColor[2]);
            ((LeatherArmorMeta) leggingsMeta).setColor(Color.fromRGB(red3, green3, blue3));
            leggings.setItemMeta(leggingsMeta);

            boots = new ItemBuilder(Material.LEATHER_BOOTS).addLore("§7Left Click to change your boots color."
                    , "§7Current Color: §a" + colors[3],
                    "§7Right Click to reset this armor").build();
            ItemMeta bootsMeta = boots.getItemMeta();
            String[] bootsColor = colors[3].split(",");
            int red4 = Integer.parseInt(bootsColor[0]);
            int green4 = Integer.parseInt(bootsColor[1]);
            int blue4 = Integer.parseInt(bootsColor[2]);
            ((LeatherArmorMeta) bootsMeta).setColor(Color.fromRGB(red4, green4, blue4));
            boots.setItemMeta(bootsMeta);

        } else {
            helmet = new ItemBuilder(Material.LEATHER_HELMET).addLore("§7Left Click to change your helmet color."
                    , "§7Current Color: §a" + armorColor,
                    "§7Right Click to reset this armor").build();
            chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).addLore("§7Left Click to change your chestplate color."
                    , "§7Current Color: §a" + armorColor,
                    "§7Right Click to reset this armor").build();
            leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).addLore("§7Left Click to change your leggings color."
                    , "§7Current Color: §a" + armorColor,
                    "§7Right Click to reset this armor").build();
            boots = new ItemBuilder(Material.LEATHER_BOOTS).addLore("§7Left Click to change your boots color."
                    , "§7Current Color: §a" + armorColor,
                    "§7Right Click to reset this armor").build();
        }
        builder.setItem(helmet, (ex) -> {
            if (!User.getUser(p).hasPermission("cmd.setarmorcolor")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §dVIP+ §cor higher rank to use this");
                return;
            }
            if (ex.getEvent().isLeftClick()) {
                p.sendMessage(new ComponentText().both("§eClick here to set your armor color."
                        , TextClickEvent.suggestCommand("/setarmorcolor 1 <R>,<G>,<B>")
                        , TextHoverEvent.showText("§eClick here to set your armor color.")).toBungee());
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                p.closeInventory();
            } else if (ex.getEvent().isRightClick()) {
                p.performCommand("setarmorcolor 1 160,101,64");
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                openArmorSelector(p);
            }
        }, 0);
        builder.setItem(chestplate, (ex) -> {
            if (!User.getUser(p).hasPermission("cmd.setarmorcolor")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §dVIP+ §cor higher rank to use this");
                return;
            }
            if (ex.getEvent().isLeftClick()) {
                p.sendMessage(new ComponentText().both("§eClick here to set your armor color."
                        , TextClickEvent.suggestCommand("/setarmorcolor 2 <R>,<G>,<B>")
                        , TextHoverEvent.showText("§eClick here to set your armor color.")).toBungee());
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                p.closeInventory();
            } else if (ex.getEvent().isRightClick()) {
                p.performCommand("setarmorcolor 2 160,101,64");
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                openArmorSelector(p);
            }
        }, 1);
        builder.setItem(leggings, (ex) -> {
            if (!User.getUser(p).hasPermission("cmd.setarmorcolor")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §dVIP+ §cor higher rank to use this");
                return;
            }
            if (ex.getEvent().isLeftClick()) {
                p.sendMessage(new ComponentText().both("§eClick here to set your armor color."
                        , TextClickEvent.suggestCommand("/setarmorcolor 3 <R>,<G>,<B>")
                        , TextHoverEvent.showText("§eClick here to set your armor color.")).toBungee());
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                p.closeInventory();
            } else if (ex.getEvent().isRightClick()) {
                p.performCommand("setarmorcolor 3 160,101,64");
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                openArmorSelector(p);
            }
        }, 2);
        builder.setItem(boots, (ex) -> {
            if (!User.getUser(p).hasPermission("cmd.setarmorcolor")) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                p.sendMessage("§cYou need §dVIP+ §cor higher rank to use this");
                return;
            }
            if (ex.getEvent().isLeftClick()) {
                p.sendMessage(new ComponentText().both("§eClick here to set your armor color."
                        , TextClickEvent.suggestCommand("/setarmorcolor 4 <R>,<G>,<B>")
                        , TextHoverEvent.showText("§eClick here to set your armor color.")).toBungee());
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                p.closeInventory();
            } else if (ex.getEvent().isRightClick()) {
                p.performCommand("setarmorcolor 4 160,101,64");
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                openArmorSelector(p);
            }
        }, 3);

        builder.setItem(back, (ex) -> plugin.getGuiCosmeticsSelector().open(p), 7);
        builder.setItem(close, (ex) -> p.closeInventory(), 8);
        builder.setClickable(false);
        p.openInventory(builder.getInventory());
    }

    private static boolean isChampOrStaff(Player p) {
        User user = User.getUser(p);
        return user.isStaff || "champ".equals(user.displayRank) || "champ".equals(user.realRank);
    }

    private static ItemStack weaponGenerator(int weaponId, Material weapon, String name) {
        return weaponId == weapon.getId() ? new ItemBuilder(weapon).name(name).glow().lore(
                "§7Select the " + ChatColor.stripColor(name), "§7Weapon.", "§7This change is cosmetic.", ""
                , "§aSELECTED!").hideAttr().build() : new ItemBuilder(weapon).name(name).lore(
                "§7Select the " + ChatColor.stripColor(name), "§7Weapon.", "§7This change is cosmetic.", "", "§eClick to select!").hideAttr().build();
    }

}

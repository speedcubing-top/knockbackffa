package top.speedcubing.knockbackffa;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.speedcubing.knockbackffa.api.events.FFAPlayerDeathEvent;
import top.speedcubing.knockbackffa.api.events.MapSwitchEvent;
import top.speedcubing.knockbackffa.cosmetics.killeffect.effects.KillEffect;
import top.speedcubing.knockbackffa.cosmetics.killeffect.manager.KillEffectManager;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.effects.ProjectileEffect;
import top.speedcubing.knockbackffa.cosmetics.projectileeffect.manager.ProjectileEffectManager;
import top.speedcubing.knockbackffa.managers.MapManager;
import top.speedcubing.knockbackffa.managers.VoteManager;
import top.speedcubing.lib.bukkit.PlayerUtils;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.minecraft.text.TextBuilder;
import top.speedcubing.lib.minecraft.text.TextClickEvent;
import top.speedcubing.lib.minecraft.text.TextHoverEvent;
import top.speedcubing.server.authenticator.ImageRenderer;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.player.User;
import top.speedcubing.server.speedcubingServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class EventListeners implements Listener {
    private final KillEffectManager killEffectManager;
    private final ProjectileEffectManager projectileEffectManager;
    HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    public EventListeners(KillEffectManager killEffectManager, ProjectileEffectManager projectileEffectManager) {
        this.killEffectManager = killEffectManager;
        this.projectileEffectManager = projectileEffectManager;
    }


    //BOW BOOST PLUGIN
//    @EventHandler
//    public void onShoot(EntityShootBowEvent e) {
//        LivingEntity livingEntity = e.getEntity();
//        Vector dir = livingEntity.getLocation().getDirection();
//        Arrow a = (Arrow) e.getProjectile();
//        double speed = a.getVelocity().length();
//        Vector vel = dir.multiply(speed);
//        a.setVelocity(vel);
//    }

    @EventHandler
    public void onPlayerDeath(FFAPlayerDeathEvent e) {
        Member killer = Member.getMember(e.getKiller());
        if (Member.getMember(e.getPlayer()).isUsingTunnel) {
            Member.getMember(e.getPlayer()).isUsingTunnel = false;
        }
        KnockBackFFA.toGreenTunnel.despawn();
        KnockBackFFA.toGreenTunnel.spawn();
        KnockBackFFA.toBlueTunnel.despawn();
        KnockBackFFA.toBlueTunnel.spawn();
        int newKillstreak = e.getKiller().getLevel() + 1;
        if (isRbwMap(KnockBackFFA.currentMapName)) {
            e.getKiller().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            if (newKillstreak % 10 == 0) {
                ItemBuilder jump = new ItemBuilder(Material.POTION).name("§aJump Boost").addPotion(new PotionEffect(PotionEffectType.JUMP, 200, 4));
                e.getKiller().getInventory().addItem(jump.build());
            }
        }
        killer.user.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8, 1));
        if (newKillstreak < 10 || newKillstreak % 5 != 0) return;
        String formatKillstreakColor = newKillstreak == 10 ? "§7" : newKillstreak == 15 ? "§f" : newKillstreak == 20 ? "§a" : newKillstreak == 25 ? "§2" : newKillstreak == 30 ? "§e" : newKillstreak == 35 ? "§6" : newKillstreak == 40 ? "§c" : newKillstreak == 45 ? "§4" : "§5";
        Bukkit.getOnlinePlayers().forEach(a -> {
            a.sendMessage("§8[§bKnockBackFFA§8] " + killer.user.getPrefixName(false) + " §ehas gotten a " + formatKillstreakColor + newKillstreak + " §ekillstreak");
        });
    }

    @EventHandler
    public void onPotion(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        Bukkit.getScheduler().runTaskLater(speedcubingServer.getInstance(), () -> {
            if (item.getType().equals(Material.POTION)) {
                e.getPlayer().getInventory().remove(item);
            }
        }, 10);
    }

    //MAP VIEW AND VOTE
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !e.getClickedInventory().getTitle().equals("Map Vote")) return;
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        switch (e.getClick()) {
            case LEFT:
                if (clickedItem != null && clickedItem.getType() == Material.MAP) {
                    if (!VoteManager.getVoteStatus()) {
                        p.sendMessage("§cVoting is not in progress.");
                        return;
                    }
                    if (VoteManager.isPlayerVoted(p)) {
                        p.sendMessage("§cYou have already voted");
                        return;
                    }
                    if (!VoteManager.isOptionsExist(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()))) {
                        p.sendMessage("Occurred Error, options not exist");
                        return;
                    }
                    VoteManager.vote(p, ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
                    p.sendMessage("§bYou voted for " + ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
                    p.closeInventory();
                }
                break;
            case RIGHT:
                if (clickedItem != null && clickedItem.getType() == Material.MAP) {
                    if (VoteManager.isPlayerVoted(p)) {
                        p.sendMessage("§cYou have already voted");
                        return;
                    }
                    p.closeInventory();
                    String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                    String url = MapManager.getMapByName(itemName);
                    if (url != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                MapView view = Bukkit.createMap(p.getWorld());
                                view.getRenderers().forEach(view::removeRenderer);
                                try {
                                    ImageRenderer renderer = new ImageRenderer(url);
                                    view.addRenderer(renderer);
                                    ItemStack mapItem = new ItemStack(Material.MAP, 1, view.getId());
                                    ItemMeta mapMeta = mapItem.getItemMeta();
                                    mapMeta.setDisplayName(ChatColor.GOLD + itemName);
                                    mapItem.setItemMeta(mapMeta);
                                    p.getInventory().addItem(mapItem);
                                } catch (IOException ee) {
                                    ee.printStackTrace();
                                    p.sendMessage(ChatColor.RED + "An error occurred! Is the URL correct?");
                                }
                            }
                        }.runTaskAsynchronously(speedcubingServer.instance);
                    }
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onJoinWhenVoting(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!VoteManager.getVoteStatus()) return;
        if (VoteManager.isPlayerVoted(player)) return;
        TextBuilder textBuilder = new TextBuilder()
                .str("§eMap Voting!")
                .both("\n§dClick to vote!", TextClickEvent.runCommand("/vote"), TextHoverEvent.showText("§dCLICK ME!"));
        player.sendMessage(textBuilder.toBungee());
    }


    //BOW BOOST
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        Arrow arrow = (Arrow) e.getProjectile();
        LivingEntity shooter = e.getEntity();
        if (!(shooter instanceof Player)) return;
        Player player = (Player) shooter;

        Vector direction = shooter.getLocation().getDirection();
        double speed = arrow.getVelocity().length();
        Vector velocity = direction.multiply(speed);
        arrow.setVelocity(velocity);

        if (!User.getUser(player).isStaff) return;

        if (((CraftArrow) arrow).getKnockbackStrength() == 1) {
            ((CraftArrow) arrow).setKnockbackStrength(2);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        ((CraftArrow) arrow).setKnockbackStrength(1);
                    }
                }
            }.runTaskLater(speedcubingServer.getInstance(), 8);
        }
    }

    @EventHandler
    public void onVelo(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        EntityDamageEvent event = p.getLastDamageCause();
        if (event != null && !event.isCancelled() && event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Arrow) {
                if (((Arrow) damager).getShooter().equals(p)) {
                    Vector velo = e.getVelocity();
                    double speed = Math.sqrt(velo.getX() * velo.getX() + velo.getZ() * velo.getZ());
                    Vector dir = damager.getLocation().getDirection().normalize();
                    Vector newvelo = new Vector(dir.getX() * speed * -1.0D, velo.getY(), dir.getZ() * speed);
                    if (User.getUser(p).isStaff) {
                        newvelo = new Vector(dir.getX() * speed * -1.0D, velo.getY() + 0.05, dir.getZ() * speed);
                    }
                    e.setVelocity(newvelo);
                }
            }
        }
    }

    @EventHandler
    public void BlocKBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Member member = Member.getMember(p);
        Block block = e.getBlock();
        if (p.getGameMode() == GameMode.CREATIVE)
            KnockBackFFA.BlockPlaced.remove(new BlockPosition(block.getX(), block.getY(), block.getZ()));

        if (p.getGameMode() == GameMode.SURVIVAL) {
            if (KnockBackFFA.BlockPlaced.containsKey(new BlockPosition(block.getX(), block.getY(), block.getZ()))) {
                KnockBackFFA.BlockPlaced.remove(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                block.setType(Material.AIR);
                member.breakBlock++;
            }
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Member member = Member.getMember(e.getPlayer());
        if (member.isUsingTunnel) {
            e.setCancelled(true);
        }
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            member.placedBlock++;

            Block block = e.getBlock();
            Material old = e.getBlockReplacedState().getType();
            if (block.getY() >= 113 && isRbwMap(KnockBackFFA.currentMapName)) e.setCancelled(true);
            if (block.getY() >= 136 && KnockBackFFA.currentMapName.equals("GAMING")) e.setCancelled(true);
            if (block.getY() >= 131 && KnockBackFFA.currentMapName.equals("ROMAN_ARENA")) e.setCancelled(true);
            if (block.getY() > KnockBackFFA.spawnprotectY || Math.abs(block.getZ() - KnockBackFFA.centerZ) > 500 || Math.abs(block.getX() - KnockBackFFA.centerX) > 500 || old.equals(Material.STATIONARY_LAVA) || old.equals(Material.LAVA) || old.equals(Material.STATIONARY_WATER) || old.equals(Material.WATER))
                e.setCancelled(true);
            else {
                KnockBackFFA.BlockPlaced.put(new BlockPosition(block.getX(), block.getY(), block.getZ()), new Integer[]{KnockBackFFA.id, 0});
                KnockBackFFA.id++;
            }
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            Member user = Member.getMember(player);
            Location to = e.getTo();
            Location musicRoomLoc1 = new Location(player.getWorld(), 948, 134, 976);
            Location musicRoomLoc2 = new Location(player.getWorld(), 931, 129, 956);

            Location yellowLoc1 = new Location(player.getWorld(), 29960.5, 99, 20062.5);
            Location yellowLoc2 = new Location(player.getWorld(), 29958.5, 102, 20064.5);
            Location greenLoc1 = new Location(player.getWorld(), 29960.5, 99, 19982.5);
            Location greenLoc2 = new Location(player.getWorld(), 29958.5, 102, 19980.5);
            Location redLoc1 = new Location(player.getWorld(), 30040.5, 99, 19982.5);
            Location redLoc2 = new Location(player.getWorld(), 30042.5, 102, 19980.5);
            Location blueLoc1 = new Location(player.getWorld(), 30040.5, 99, 20062.5);
            Location blueLoc2 = new Location(player.getWorld(), 30042.5, 102, 20064.5);
            if (to.getY() < KnockBackFFA.spawnprotectY && !user.isFighting) {
                user.isFighting = true;
                user.item(false);
                player.getInventory().remove(Material.EMERALD);

                if (!isChampOrStaff(player)) {
                    KillEffect killEffect = killEffectManager.getKillEffect(player);
                    ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(player);
                    if (killEffect != null) {
                        if (killEffect.getId().equals("CHAMP")) {
                            killEffectManager.setKillEffect(player, "NONE");
                        }
                    }
                    if (projectileEffect != null) {
                        if (projectileEffect.getId().equals("CHAMP")) {
                            projectileEffectManager.setProjectileEffect(player, "NONE");
                        }
                    }
                }

            } else if (to.getY() <= KnockBackFFA.offsetY || (Math.abs(to.getX() - KnockBackFFA.centerX - 0.5) > 499.5 || Math.abs(to.getZ() - KnockBackFFA.centerZ - 0.5) > 499.5)) {
                user.death(false);
            }
            if (KnockBackFFA.currentMapName.equals("CRUSHERS")) {
                if (isInRange(player, yellowLoc1, yellowLoc2)) {
                    player.teleport(new Location(player.getWorld(), 29973.5, 101.5, 20034.5, player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                }
                if (isInRange(player, greenLoc1, greenLoc2)) {
                    player.teleport(new Location(player.getWorld(), 29973.5, 101.5, 20010.5, player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                }
                if (isInRange(player, redLoc1, redLoc2)) {
                    player.teleport(new Location(player.getWorld(), 30027.5, 107.5, 20010.5, player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                }
                if (isInRange(player, blueLoc1, blueLoc2)) {
                    player.teleport(new Location(player.getWorld(), 30027.5, 101.5, 20034.5, player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
                }
            }
            //idk kys
            if (!User.getUser(player).isStaff && !isYt(player)) {
                if (isInRange(player, musicRoomLoc1, musicRoomLoc2)) {
                    user.death(false);
                }
            }
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                if (!Member.getMember(entity).isFighting)
                    e.setCancelled(true);
            } else if ((cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA) && ((Player) entity).getHealth() - e.getFinalDamage() < 0)
                Member.getMember(entity).death(false);
        }
    }

    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Arrow)
            entity.remove();
    }

    @EventHandler
    public void fireballHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Fireball)) return;
        Location location = e.getEntity().getLocation();

        ProjectileSource projectileSource = e.getEntity().getShooter();
        if (!(projectileSource instanceof Player)) return;
        double fireballHorizontal = 1.25 * -1;
        double fireballVertical = 0.88;
        double fireballExplosionSize = 3;

        Vector vector = location.toVector();

        World world = location.getWorld();

        assert world != null;
        Collection<Entity> nearbyEntities = world
                .getNearbyEntities(location, fireballExplosionSize, fireballExplosionSize, fireballExplosionSize);
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;


            Vector playerVector = player.getLocation().toVector();
            Vector normalizedVector = vector.subtract(playerVector).normalize();
            Vector horizontalVector = normalizedVector.multiply(fireballHorizontal);
            double y = normalizedVector.getY();
            double x = normalizedVector.getX();
            double z = normalizedVector.getZ();
            if (y < 0) y += 1.5;
            if (y <= 0.5 && x == -0.0 && z == -0.0) { //not moving and not jumping
                y = fireballVertical * 1.7;
            } else if (y <= 0.5 && x != 0.0 && z != 0.0) {
                y = fireballVertical * 1.3;
                x = horizontalVector.getX() * 0.65;
                z = horizontalVector.getZ() * 0.65;
            } else if (y > 0.5 && x == -0.0 && z == -0.0) {
                y = y * fireballVertical * 1.5;
            } else if (y > 0.5 && x != -0.0 && z != -0.0) {
                y = fireballVertical * 1.7;
                x = horizontalVector.getX() * 0.8;
                z = horizontalVector.getZ() * 0.8;
            }
            if (isFireballHigherThanPlayer(location, player.getLocation())) {
                y *= 0;
                x *= 2;
                z *= 2;
                //跟x y z憶起計算看能不能做出像後kb
            }
            player.setVelocity(horizontalVector.setX(x).setY(y).setZ(z));
        }
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            Entity damage = e.getDamager();
            Player damager = null;
            if (damage instanceof EnderPearl) {
                damager = (Player) ((EnderPearl) e.getDamager()).getShooter();
            } else if (damage instanceof Arrow) {
                damager = (Player) ((Arrow) e.getDamager()).getShooter();
            } else if (damage instanceof Player) {
                damager = (Player) damage;
            }
            if (damager != null) {
                Player victim = (Player) e.getEntity();
                Member victimMember = Member.getMember(victim);
                Member Damager = Member.getMember(damager);
                if (victimMember.isFighting && Damager.isFighting) {
                    if (victim != damager) {
                        victimMember.Damage = Damager;
                    }
                    if (victim.getHealth() - e.getFinalDamage() <= 0) {
                        victimMember.death(false);
                        e.setCancelled(true);
                        victim.damage(0);
                    }
                    victimMember.KillTime = System.currentTimeMillis();
                } else {
                    e.setCancelled(true);
                }
            }
        }
        if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (player.getGameMode() == GameMode.SURVIVAL) {
                e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Arrow || e.getDamager() instanceof TNTPrimed || e.getDamager() instanceof Fireball || e.getDamager() instanceof FishHook
                || e.getDamager() instanceof Snowball || e.getDamager() instanceof Egg) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractionEntity(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame) {
            Player player = e.getPlayer();
            if (player.getGameMode() == GameMode.SURVIVAL) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent e) {
        if (e.getCursor() != null && e.getCursor().getAmount() != 1)
            e.setCancelled(true);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();
        Member member = Member.getMember(player);
        if (e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PLACE_ONE) {
            if (member.isFighting) {
                if (isEquipmentSlot(RawSlot) && player.getGameMode() == GameMode.SURVIVAL) {
                    e.setCancelled(true);
                }
                return;
            }
            e.setCancelled(true);
        }
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            if (inventory.getName().equals("container.crafting")) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    if (member.isFighting && !isEquipmentSlot(RawSlot)) {
                        return;
                    }
                    e.setCancelled(true);
                }
            } else if (inventory.getHolder() == KnockBackFFA.KitEditorHolder) {
                if (RawSlot < 36)
                    e.setCancelled(true);
                switch (RawSlot) {
                    case 3:
                        ItemStack cursor = e.getCursor();
                        member.user.sound(Sound.CLICK);
                        Inventory inv = player.getInventory();
                        if (cursor.getType() == Material.AIR) {
                            member.user.sendLangMessage(GlobalString.SavedSettings);
                            List<Material> t = Arrays.asList(Material.GOLD_SWORD, Material.STICK, Material.ENDER_PEARL, Material.WEB, Material.SANDSTONE, Material.ARROW, Material.BOW, Material.STONE_PICKAXE);
                            for (int i = 0; i < 9; i++) {
                                if (inv.getItem(i) != null && t.contains(inv.getItem(i).getType())) {
                                    int index = t.indexOf(inv.getItem(i).getType());
                                    member.inventory = member.inventory.substring(0, index) + i + member.inventory.substring(index + 1);
                                }
                            }
                            inv.clear();
                            inv.setItem(4, KnockBackFFA.itemStacks[member.user.lang][11]);
                            inv.setItem(6, KnockBackFFA.itemStacks[member.user.lang][12]);
                            member.iseditingkit = false;
                            player.closeInventory();
                        } else {
                            for (int i = 0; i < 9; i++) {
                                if (inv.getItem(i) == null) {
                                    inv.setItem(i, cursor);
                                    break;
                                }
                            }
                            player.setItemOnCursor(new ItemStack(Material.AIR));
                        }
                        break;
                    case 5:
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                        player.closeInventory();
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getInventory().getName().contains("KnockBack FFA")) {
            Player player = (Player) e.getPlayer();
            player.setItemOnCursor(new ItemStack(Material.AIR));
            Member member = Member.getMember(player);
            if (member.iseditingkit) {
                member.iseditingkit = false;
                member.user.sendLangMessage(GlobalString.UnSavedSettings);
                Inventory inventory = player.getInventory();
                inventory.clear();
                inventory.setItem(4, KnockBackFFA.itemStacks[member.user.lang][11]);
                inventory.setItem(6, KnockBackFFA.itemStacks[member.user.lang][12]);
            }
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().equals(Material.CHEST)) {
            Member member = Member.getMember(player);
            if (member.isFighting) {
                return;
            }
            member.user.sound(Sound.CLICK);
            Inventory inventory = player.getInventory();
            inventory.setItem(4, new ItemStack(Material.AIR));
            ItemStack[] itemStacks = KnockBackFFA.itemStacks[member.user.lang];
            char[] cfg = member.inventory.toCharArray();
            inventory.setItem(cfg[0] - 48, itemStacks[4]);
            inventory.setItem(cfg[1] - 48, itemStacks[5]);
            inventory.setItem(cfg[2] - 48, itemStacks[6]);
            inventory.setItem(cfg[3] - 48, itemStacks[7]);
            inventory.setItem(cfg[4] - 48, itemStacks[8]);
            inventory.setItem(cfg[5] - 48, itemStacks[9]);
            inventory.setItem(cfg[6] - 48, itemStacks[10]);
            inventory.setItem(cfg[7] - 48, itemStacks[13]);

            member.iseditingkit = true;
            player.openInventory(KnockBackFFA.KitEditor[member.user.lang]);

            player.getInventory().remove(Material.EMERALD);

            InventoryUtils.fillItem(inventory, 9, 36, KnockBackFFA.Invalid[member.user.lang]);
        } else if (player.getItemInHand().getType().equals(Material.EMERALD)) {
            KnockBackFFA.getPlugin(KnockBackFFA.class).getGuiCosmeticsSelector().open(player);
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.PISTON_BASE && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.PISTON_STICKY_BASE && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.PISTON_EXTENSION && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.PISTON_MOVING_PIECE && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.NOTE_BLOCK && player.getGameMode() != GameMode.CREATIVE && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(true);
        }
        if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.EXPLOSIVE_MINECART) {
            e.setCancelled(true);
        }
        if (player.getLocation().getY() > KnockBackFFA.spawnprotectY && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            player.updateInventory();
        }
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENDER_CHEST && player.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtils.removeArrows(player);
        Member member = Member.getMember(player);
        member.death(true);
        member.user.dbUpdate("kbffa_inventory='" + member.inventory + "',kbffa_kill=" + member.kill + ",kbffa_dailykill=" + member.dailyKill + ",kbffa_death=" + member.death + ",kbffa_killstreak=" + member.killstreak + ",kbffa_blockplace=kbffa_blockplace+" + member.placedBlock + ",kbffa_blockbreak=kbffa_blockbreak+" + member.breakBlock + ",kbffa_armorcolor='" + member.armorColor + "'");
        Member.users.remove(member.user.id);
        //remove bypass anticheat
        if (perms.containsKey(e.getPlayer().getUniqueId())) {
            perms.get(player.getUniqueId()).unsetPermission("vulcan.bypass.*");
            perms.get(player.getUniqueId()).unsetPermission("grim.nosetback");
            perms.get(player.getUniqueId()).unsetPermission("grim.nomodifypacket");
            perms.get(player.getUniqueId()).unsetPermission("grim.exempt");
            perms.get(player.getUniqueId()).unsetPermission("karhu.*");
            perms.get(player.getUniqueId()).unsetPermission("karhu.staff");
            perms.get(player.getUniqueId()).unsetPermission("karhu.alerts");
            perms.get(player.getUniqueId()).unsetPermission("karhu.gui");
            perms.get(player.getUniqueId()).unsetPermission("karhu.reload");
            perms.get(player.getUniqueId()).unsetPermission("karhu.autoban");
            perms.get(player.getUniqueId()).unsetPermission("karhu.bypass");
            perms.get(player.getUniqueId()).unsetPermission("karhu.bypass.ban");
            perms.get(player.getUniqueId()).unsetPermission("karhu.logs");
            perms.get(player.getUniqueId()).unsetPermission("karhu.info");
            perms.get(player.getUniqueId()).unsetPermission("karhu.recentbans");
            perms.get(player.getUniqueId()).unsetPermission("karhu.hover-debug");
            perms.get(player.getUniqueId()).unsetPermission("karhu.mitigations");
            perms.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        new Member(e.getPlayer());
        Player p = e.getPlayer();
        User user = User.getUser(p);
        //add bypass anticheat
        if (user.id == 4196 || user.id == 13758 || user.id == 27397 || user.id == 42415) {
            PermissionAttachment attachment = p.addAttachment(speedcubingServer.getInstance());
            perms.put(user.bGetUniqueId(), attachment);

            PermissionAttachment pperms = perms.get(user.bGetUniqueId());
            pperms.setPermission("vulcan.bypass.*", true);
            pperms.setPermission("grim.nosetback", true);
            pperms.setPermission("grim.nomodifypacket", true);
            pperms.setPermission("grim.exempt", true);
            pperms.setPermission("karhu.*", true);
            pperms.setPermission("karhu.staff", true);
            pperms.setPermission("karhu.alerts", true);
            pperms.setPermission("karhu.gui", true);
            pperms.setPermission("karhu.reload", true);
            pperms.setPermission("karhu.autoban", true);
            pperms.setPermission("karhu.bypass", true);
            pperms.setPermission("karhu.bypass.ban", true);
            pperms.setPermission("karhu.logs", true);
            pperms.setPermission("karhu.info", true);
            pperms.setPermission("karhu.recentbans", true);
            pperms.setPermission("karhu.hover-debug", true);
            pperms.setPermission("karhu.mitigations", true);

        }
        if (!isChampOrStaff(p)) {
            KillEffect killEffect = killEffectManager.getKillEffect(p);
            ProjectileEffect projectileEffect = projectileEffectManager.getProjectileEffect(p);
            if (killEffect != null) {
                if (killEffect.getId().equals("CHAMP")) {
                    killEffectManager.setKillEffect(p, "NONE");
                }
            }
            if (projectileEffect != null) {
                if (projectileEffect.getId().equals("CHAMP")) {
                    projectileEffectManager.setProjectileEffect(p, "NONE");
                }
            }
        }
    }

    @EventHandler
    public void MapSwitchEvent(MapSwitchEvent e) {
        KnockBackFFA.clickToggle.despawn();
        KnockBackFFA.clickToggle
                .setLocation(KnockBackFFA.holoX, 148.5, KnockBackFFA.holoZ, 0, 0)
                .world("world")
                .spawn();
    }

    @EventHandler
    public void VehicleExitEvent(VehicleExitEvent e) {
        if (e.getVehicle() instanceof Minecart && e.getExited() instanceof Player player) {
            if (Member.getMember(player).isUsingTunnel) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent e) {
        if (e.getEntity() instanceof Minecart && KnockBackFFA.currentMapName.equals("CRUSHERS")) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
        Member member = Member.getMember(e.getPlayer());
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getTo().getY() >= KnockBackFFA.spawnprotectY)
            e.setCancelled(true);
        if (member == null) return;
        if (member.isUsingTunnel) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        if (Member.getMember(e.getEntity().getPlayer()).isUsingTunnel) {
            Member.getMember(e.getEntity().getPlayer()).isUsingTunnel = false;
        }
    }

    @EventHandler
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) projectile;
            Member.getMember((Player) pearl.getShooter()).pearls.add(pearl);
        }
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.CREEPER || entity.getType() == EntityType.PRIMED_TNT || entity.getType() == EntityType.MINECART_TNT || entity.getType() == EntityType.FIREBALL || entity.getType() == EntityType.WITHER_SKULL || entity.getType() == EntityType.WITHER) {
            entity.getNearbyEntities(5, 5, 5).forEach(a -> {
                if (a instanceof Player) {
                    Player player = (Player) a;
                    player.damage(5);
                }
            });
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void witherBreak(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof Wither) e.setCancelled(true);
        if (e.getEntity() instanceof EnderDragon) e.setCancelled(true);
    }

    //Canceled tnt kill effect damage
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof TNTPrimed) {
            if (event.getDamager().hasMetadata("effectTNT")) {
                event.setCancelled(true);
            }
        }
    }

    private static boolean isChampOrStaff(Player p) {
        User user = User.getUser(p);
        return user.isStaff || "champ".equals(user.displayRank) || "champ".equals(user.realRank);
    }

    private static boolean isYt(Player p) {
        User user = User.getUser(p);
        return "yt".equals(user.displayRank) || "yt".equals(user.realRank) || "ytplus".equals(user.displayRank) || "ytplus".equals(user.realRank);
    }

    private static boolean isEquipmentSlot(int rawSlot) {
        return rawSlot >= 5 && rawSlot <= 8;
    }

    public static boolean isInRange(Player player, Location corner1, Location corner2) {
        Location playerLocation = player.getLocation();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return playerLocation.getX() >= minX && playerLocation.getX() <= maxX &&
                playerLocation.getY() >= minY && playerLocation.getY() <= maxY &&
                playerLocation.getZ() >= minZ && playerLocation.getZ() <= maxZ;
    }

    public static boolean isRbwMap(String s) {
        return s.equalsIgnoreCase("invasion");
    }

    private static boolean isFireballHigherThanPlayer(Location fireballLocation, Location playerLocation) {
        return fireballLocation.getY() - 1 > playerLocation.getY();
    }

}


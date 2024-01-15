package top.speedcubing.knockbackffa.cosmetics.killeffect.effects;



import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import top.speedcubing.knockbackffa.KnockBackFFA;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.ConfigUtil;
import top.speedcubing.knockbackffa.cosmetics.killeffect.utils.ParticleEffect;

public class KillEffectHeartExplosion extends KillEffect {

	public KillEffectHeartExplosion() {
		super("Heart Explosion", "HEART_EXPLOSION", Material.GOLDEN_APPLE);
	}

	@Override
	public void play(Player p) {
		Location loc = p.getLocation();
		float offsetX = ConfigUtil.HEART_EXPLOSION_OFFSET_X;
		float offsetY = ConfigUtil.HEART_EXPLOSION_OFFSET_Y;
		float offsetZ = ConfigUtil.HEART_EXPLOSION_OFFSET_Z;
		float addX = ConfigUtil.HEART_EXPLOSION_ADD_X;
		float addY = ConfigUtil.HEART_EXPLOSION_ADD_Y;
		float addZ = ConfigUtil.HEART_EXPLOSION_ADD_Z;
		float speed = ConfigUtil.HEART_EXPLOSION_SPEED;
		BukkitRunnable runnable = new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				if (i > 0) {
					ParticleEffect.HEART.display(offsetX, offsetY, offsetZ, speed, 10, loc.clone().add(addX, addY, addZ), 256f);
					i--;
					if (i == 0) {
						cancel();
					}
				}
			}

		};
		runnable.runTaskTimer(KnockBackFFA.getInstance(), 5, 5);
	}

	public static class KillEffectFinalSmash extends KillEffect {

		public KillEffectFinalSmash() {
			super("Final Smash", "FINAL_SMASH", Material.ARMOR_STAND);
		}

		@Override
		public void play(Player p) {
			Location loc = p.getLocation();
			World world = ((CraftWorld) loc.getWorld()).getHandle();
			EntityArmorStand entityArmorStand = new EntityArmorStand(world);
			entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(),
					MathHelper.d((entityArmorStand.pitch * 256F) / 360F),
					MathHelper.d((entityArmorStand.yaw * 256F) / 360F));
			entityArmorStand.setInvisible(false);
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skullMeta.setOwner(p.getName());
			skull.setItemMeta(skullMeta);
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			net.minecraft.server.v1_8_R3.ItemStack nmsSkull = CraftItemStack.asNMSCopy(skull);
			net.minecraft.server.v1_8_R3.ItemStack nmsChest = CraftItemStack.asNMSCopy(chest);
			net.minecraft.server.v1_8_R3.ItemStack nmsLeg = CraftItemStack.asNMSCopy(leg);
			net.minecraft.server.v1_8_R3.ItemStack nmsBoots = CraftItemStack.asNMSCopy(boots);
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
			PacketPlayOutEntityEquipment packetEquipSkull = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 4,
					nmsSkull);
			PacketPlayOutEntityEquipment packetEquipChest = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 3,
					nmsChest);
			PacketPlayOutEntityEquipment packetEquipLeg = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 2,
					nmsLeg);
			PacketPlayOutEntityEquipment packetEquipBoots = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 1,
					nmsBoots);
			for (Player all : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipSkull);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipChest);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipLeg);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipBoots);
			}
			BukkitRunnable runnable = new BukkitRunnable() {
				int i = ConfigUtil.FINAL_SMASH_DURATION;
				double addX = 0.5d;
				double addZ = 0.5d;
				Location lastPos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);

				@Override
				public void run() {
					if (i == ConfigUtil.FINAL_SMASH_DURATION || i == ConfigUtil.FINAL_SMASH_DURATION_TURN) {
						double randomDouble1 = Math.random();
						if (randomDouble1 < 0.5d) {
							addX = 0.5d;
						}
						if (randomDouble1 >= 0.5d) {
							addX = -0.5d;
						}
						double randomDouble2 = Math.random();
						if (randomDouble2 < 0.5d) {
							addZ = 0.5d;
						}
						if (randomDouble2 >= 0.5d) {
							addZ = -0.5d;
						}
					}
					if (i > 0) {
						entityArmorStand.locY += ConfigUtil.FINAL_SMASH_ADD_Y;
						entityArmorStand.locX += addX;
						entityArmorStand.locZ += addZ;
						Location pos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
						if (pos.getBlock().getType() == Material.AIR) {
							PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation(
									entityArmorStand,
									(byte) MathHelper.floor(((entityArmorStand.yaw += ConfigUtil.HEAD_ROCKET_ADD_YAW) * 256.0F) / 360.0F));
							PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entityArmorStand);
							for (Player all : Bukkit.getOnlinePlayers()) {
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityHeadRotation);
							}
							ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, pos, 256f);
							lastPos = pos;
							i--;
						} else {
							i = 0;
						}
						if (i == 0) {
							PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
							for (Player all : Bukkit.getOnlinePlayers()) {
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
							}
							Firework fw = (Firework) lastPos.getWorld().spawn(lastPos, Firework.class);
							FireworkMeta fm = fw.getFireworkMeta();
							fm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED)
									.withFade(Color.RED).build());
							fw.setFireworkMeta(fm);
							Bukkit.getScheduler().runTaskLater(KnockBackFFA.getInstance(), () -> {
								fw.detonate();
							}, 1L);
							cancel();
						}
					}
				}

			};
			runnable.runTaskTimer(KnockBackFFA.getInstance(), 1, 1);
		}
	}

	public static class KillEffectHeadRocket extends KillEffect {

		public KillEffectHeadRocket() {
			super("Head Rocket", "HEAD_ROCKET", Material.SKULL_ITEM);
		}

		@Override
		public void play(Player p) {
			Location loc = p.getLocation();
			World world = ((CraftWorld) loc.getWorld()).getHandle();
			EntityArmorStand entityArmorStand = new EntityArmorStand(world);
			entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(),
					MathHelper.d((entityArmorStand.pitch * 256F) / 360F),
					MathHelper.d((entityArmorStand.yaw * 256F) / 360F));
			entityArmorStand.setInvisible(true);
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skullMeta.setOwner(p.getName());
			skull.setItemMeta(skullMeta);
			net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(skull);
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
			PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 4,
					nmsItemStack);
			for (Player all : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityEquipment);
			}
			BukkitRunnable runnable = new BukkitRunnable() {
				int i = ConfigUtil.HEAD_ROCKET_DURATION;
				Location lastPos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (i > 0) {
						entityArmorStand.locY += ConfigUtil.HEAD_ROCKET_ADD_Y;
						Location pos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
						if (pos.getBlock().getType() == Material.AIR) {
							PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation(
									entityArmorStand,
									(byte) MathHelper.floor(((entityArmorStand.yaw += ConfigUtil.HEAD_ROCKET_ADD_YAW) * 256.0F) / 360.0F));
							PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entityArmorStand);
							for (Player all : Bukkit.getOnlinePlayers()) {
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityHeadRotation);
							}
							ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, pos, 256f);
							lastPos = pos;
							i--;
						} else {
							i = 0;
						}
						if (i == 0) {
							PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
							for (Player all : Bukkit.getOnlinePlayers()) {
								((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
								all.playEffect(lastPos, Effect.STEP_SOUND, 152);
							}
							cancel();
						}
					}
				}

			};
			runnable.runTaskTimer(KnockBackFFA.getInstance(), 1, 1);
		}
	}
}

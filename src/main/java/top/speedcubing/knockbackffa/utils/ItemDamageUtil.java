package top.speedcubing.knockbackffa.utils;

import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemDamageUtil {
    public static ItemStack setAttackDamage(ItemStack item, double damage) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        net.minecraft.server.v1_8_R3.NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new net.minecraft.server.v1_8_R3.NBTTagCompound();
        net.minecraft.server.v1_8_R3.NBTTagList modifiers = new net.minecraft.server.v1_8_R3.NBTTagList();

        net.minecraft.server.v1_8_R3.NBTTagCompound damageModifier = new net.minecraft.server.v1_8_R3.NBTTagCompound();
        damageModifier.set("AttributeName", new net.minecraft.server.v1_8_R3.NBTTagString(GenericAttributes.ATTACK_DAMAGE.getName()));
        damageModifier.set("Name", new net.minecraft.server.v1_8_R3.NBTTagString("generic.attackDamage"));
        damageModifier.set("Amount", new net.minecraft.server.v1_8_R3.NBTTagDouble(damage));
        damageModifier.set("Operation", new net.minecraft.server.v1_8_R3.NBTTagInt(0));
        damageModifier.set("UUIDMost", new net.minecraft.server.v1_8_R3.NBTTagLong(UUID.randomUUID().getMostSignificantBits()));
        damageModifier.set("UUIDLeast", new net.minecraft.server.v1_8_R3.NBTTagLong(UUID.randomUUID().getLeastSignificantBits()));

        modifiers.add(damageModifier);
        tag.set("AttributeModifiers", modifiers);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}

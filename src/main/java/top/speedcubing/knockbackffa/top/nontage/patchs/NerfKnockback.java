package top.speedcubing.knockbackffa.top.nontage.patchs;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
// import top.nontage.jniil.annotations.InjectClassInfo; // JNIIL removed

// @InjectClassInfo(anchorClassType = EntityLiving.class)
public class NerfKnockback {
    public static double nerfFriction = 2;
    public static int nerfPing = 100;
    public static boolean shouldNerfKnockback(Entity entity) {
        if (!(entity instanceof EntityPlayer player)) {
            return false;
        }
        int ping = player.ping;
        return ping >= nerfPing;
    }
}

package top.speedcubing.knockbackffa.top.nontage.patchs;

// JNIIL removed — dependency unavailable
//import net.minecraft.server.v1_8_R3.Entity;
//import net.minecraft.server.v1_8_R3.EntityLiving;
//import top.nontage.jniil.annotations.At;
//import top.nontage.jniil.annotations.InjectMethodInfo;
//import top.nontage.jniil.builder.ExprBuilder;
//import top.nontage.jniil.builder.LocalValue;
//import top.nontage.jniil.interfaces.Injectable;
//import top.nontage.relocated.javassist.CtMethod;

public class KnockbackPatch /* implements Injectable */ {

//    @InjectMethodInfo(
//            targetType = EntityLiving.class,
//            targetMethodName = "a",
//            targetMethodParamTypes = {Entity.class, float.class, double.class, double.class}
//    )
//    @At(line = 918)
//    @Override
//    public String getInjectSourceCode(CtMethod ctMethod) {
//        ExprBuilder builder = ExprBuilder.begin();
//        builder.extractExistingLocals(ctMethod);
//        LocalValue<Double> friction = builder.local(9, "friction", Double.class);
//        builder.codeBlock(block -> {
//            block.ifThen("top.speedcubing.knockbackffa.top.nontage.patchs.NerfKnockback.shouldNerfKnockback($0)", () -> {
//                block.addLine(friction.setExpr("top.speedcubing.knockbackffa.top.nontage.patchs.NerfKnockback.nerfFriction"));
//            });
//        });
//        return builder.compile();
//    }
}

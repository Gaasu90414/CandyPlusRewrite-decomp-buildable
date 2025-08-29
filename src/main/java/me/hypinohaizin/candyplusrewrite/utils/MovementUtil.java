//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class MovementUtil {
   public static Vec3d getMoveInputs(EntityPlayerSP player) {
      float moveUpward = 0.0F;
      if (player.movementInput.jump) {
         ++moveUpward;
      }

      if (player.movementInput.sneak) {
         --moveUpward;
      }

      float moveForward = 0.0F;
      float moveStrafe = 0.0F;
      moveForward += player.movementInput.forwardKeyDown ? 1.0F : 0.0F;
      moveStrafe += player.movementInput.leftKeyDown ? 1.0F : 0.0F;
      return new Vec3d((double)(moveForward - (player.movementInput.backKeyDown ? 1.0F : 0.0F)), (double)moveUpward, (double)(moveStrafe - (player.movementInput.rightKeyDown ? 1.0F : 0.0F)));
   }

   public static Vec2f toWorld(Vec2f vec, float yaw) {
      if (vec.x == 0.0F && vec.y == 0.0F) {
         return new Vec2f(0.0F, 0.0F);
      } else {
         float r = MathHelper.sqrt(vec.x * vec.x + vec.y * vec.y);
         float yawCos = MathHelper.cos((float)((double)yaw * 3.141592653589793D / 180.0D));
         float yawSin = MathHelper.sin((float)((double)yaw * 3.141592653589793D / 180.0D));
         float vecCos = vec.x / r;
         float vecSin = vec.y / r;
         float cos = yawCos * vecCos + yawSin * vecSin;
         float sin = yawSin * vecCos - vecSin * yawCos;
         return new Vec2f(-r * sin, r * cos);
      }
   }
}

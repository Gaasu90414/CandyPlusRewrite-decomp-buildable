//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.MovementUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class PhaseWalk extends Module {
   public Setting<Float> multiplier = this.register(new Setting("SpeedMultiplier", 1.0F, 5.0F, 0.1F));
   public Setting<Float> downSpeed = this.register(new Setting("DownSpeed", 0.1F, 2.0F, 0.05F));
   public Setting<Boolean> shiftLimit = this.register(new Setting("ShiftLimit", true));
   public Setting<Boolean> voidSafe = this.register(new Setting("VoidSafe", true));
   public Setting<Integer> voidY = this.register(new Setting("VoidY", 60, 256, 0, (v) -> {
      return (Boolean)this.voidSafe.getValue();
   }));
   private boolean sneakedFlag;
   private boolean phaseSuffix;
   private long startTime = 0L;

   public PhaseWalk() {
      super("PhaseWalk", Module.Categories.MOVEMENT, false, false);
   }

   public void onEnable() {
      this.sneakedFlag = false;
      this.phaseSuffix = false;
      this.startTime = 0L;
   }

   public void onDisable() {
      this.phaseSuffix = false;
   }

   public void onTick() {
      EntityPlayerSP player = mc.player;
      if (player != null && mc.world != null) {
         boolean isInside = PlayerUtil.isInsideBlock();
         if (isInside) {
            if (this.startTime == 0L) {
               this.startTime = System.currentTimeMillis();
            }

            this.phaseSuffix = true;
            player.noClip = true;
            player.fallDistance = 0.0F;
            Vec3d input = MovementUtil.getMoveInputs(player);
            double mul = (double)(Float)this.multiplier.getValue();
            Vec2f moveVec = MovementUtil.toWorld(new Vec2f((float)input.x, (float)input.z), player.rotationYaw);
            player.motionX = (double)moveVec.x * mul;
            player.motionZ = (double)moveVec.y * mul;
            player.motionY = 0.0D;
            if (player.movementInput.sneak) {
               if ((Boolean)this.shiftLimit.getValue()) {
                  if (!this.sneakedFlag) {
                     if ((Boolean)this.voidSafe.getValue() && player.posY - (double)(Float)this.downSpeed.getValue() <= (double)(Integer)this.voidY.getValue()) {
                        player.setPosition(player.posX, (double)(Integer)this.voidY.getValue() + 1.0D, player.posZ);
                     } else {
                        player.setPosition(player.posX, player.posY - (double)(Float)this.downSpeed.getValue(), player.posZ);
                     }
                  }
               } else {
                  player.setPosition(player.posX, player.posY - (double)(Float)this.downSpeed.getValue(), player.posZ);
               }

               this.sneakedFlag = true;
            } else if (this.sneakedFlag) {
               this.sneakedFlag = false;
            }
         } else {
            this.phaseSuffix = false;
            player.noClip = false;
            if (this.startTime != 0L) {
               this.startTime = 0L;
            }
         }

         if ((Boolean)this.voidSafe.getValue() && player.posY <= (double)(Integer)this.voidY.getValue()) {
            player.setPosition(player.posX, (double)(Integer)this.voidY.getValue() + 1.0D, player.posZ);
            this.phaseSuffix = true;
         }

      }
   }
}

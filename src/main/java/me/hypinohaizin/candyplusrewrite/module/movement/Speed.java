//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod.EventHandler;

public class Speed extends Module {
   public Setting<Speed.SpeedMode> mode;
   public Setting<Float> multiplier;
   public Setting<Boolean> forceSprint;
   public Setting<Boolean> enableOnJump;
   public Setting<Boolean> resetMotion;
   public Setting<Boolean> fastJump;
   public Setting<Boolean> jumpSkip;
   private boolean lastActive = false;

   public Speed() {
      super("Speed", Module.Categories.MOVEMENT, false, false);
      this.mode = this.register(new Setting("Mode", Speed.SpeedMode.STRAFE));
      this.multiplier = this.register(new Setting("Multiplier", 2.0F, 5.0F, 0.0F));
      this.forceSprint = this.register(new Setting("Force Sprint", true));
      this.enableOnJump = this.register(new Setting("Enable On Jump", false));
      this.resetMotion = this.register(new Setting("Reset Motion", true));
      this.fastJump = this.register(new Setting("Fast Jump", true));
      this.jumpSkip = this.register(new Setting("Jump Skip", true));
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      this.resetMotion();
   }

   private void resetMotion() {
      if ((Boolean)this.resetMotion.getValue() && this.lastActive && mc.player != null) {
         this.lastActive = false;
         mc.player.motionX = 0.0D;
         mc.player.motionZ = 0.0D;
      }
   }

   @EventHandler
   public void onTick() {
      if (mc.player != null && mc.world != null) {
         EntityPlayer player = mc.player;
         Vec3d input = this.getMoveInputs(player);
         if ((Boolean)this.forceSprint.getValue() && (input.x != 0.0D || input.z != 0.0D)) {
            player.setSprinting(true);
         }

         if ((Boolean)this.enableOnJump.getValue() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            this.resetMotion();
         } else if (!player.isInWater() && !player.isInLava() && !player.isOnLadder()) {
            this.lastActive = true;
            Vec3d moveInputs = this.getMoveInputs(player);
            moveInputs = moveInputs.subtract(0.0D, moveInputs.y, 0.0D).normalize();
            float yaw = player.rotationYaw;
            Vec3d motion = this.toWorld(moveInputs.x, moveInputs.z, yaw);
            AxisAlignedBB box = player.getEntityBoundingBox().offset(motion.x / 10.0D, 0.0D, motion.z / 10.0D);
            if ((Boolean)this.fastJump.getValue() && mc.gameSettings.keyBindJump.isKeyDown() && player.onGround) {
               player.motionY = 0.42D;
            }

            if ((Boolean)this.jumpSkip.getValue() && mc.world.collidesWithAnyBlock(box) && !mc.world.collidesWithAnyBlock(box.offset(0.0D, 0.25D, 0.0D)) && player.motionY > 0.0D) {
               double maxHeight = this.getMaxHeight(box.offset(0.0D, 0.25D, 0.0D));
               player.setPosition(player.posX + motion.x / 10.0D, maxHeight, player.posZ + motion.z / 10.0D);
               player.motionY = 0.0D;
            }

            if (this.mode.getValue() == Speed.SpeedMode.VANILLA) {
               float value = (Float)this.multiplier.getValue();
               player.motionX = motion.x * (double)value;
               player.motionZ = motion.z * (double)value;
            } else if (this.mode.getValue() == Speed.SpeedMode.STRAFE) {
               Vec3d vec = new Vec3d(player.motionX, 0.0D, player.motionZ);
               double mul = Math.max(vec.distanceTo(Vec3d.ZERO), player.isSneaking() ? 0.0D : 0.1D);
               if (!mc.gameSettings.keyBindForward.isKeyDown() && player.onGround) {
                  mul *= 1.18D;
               } else if (!mc.gameSettings.keyBindForward.isKeyDown()) {
                  mul *= 1.025D;
               }

               player.motionX = motion.x * mul;
               player.motionZ = motion.z * mul;
            }

         }
      }
   }

   private Vec3d getMoveInputs(EntityPlayer player) {
      double forward = 0.0D;
      double strafe = 0.0D;
      if (mc.gameSettings.keyBindForward.isKeyDown()) {
         ++forward;
      }

      if (mc.gameSettings.keyBindBack.isKeyDown()) {
         --forward;
      }

      if (mc.gameSettings.keyBindLeft.isKeyDown()) {
         ++strafe;
      }

      if (mc.gameSettings.keyBindRight.isKeyDown()) {
         --strafe;
      }

      return new Vec3d(strafe, 0.0D, forward);
   }

   private Vec3d toWorld(double x, double z, float yaw) {
      double radians = Math.toRadians((double)yaw);
      double cos = Math.cos(radians);
      double sin = Math.sin(radians);
      return new Vec3d(x * cos - z * sin, 0.0D, x * sin + z * cos);
   }

   private double getMaxHeight(AxisAlignedBB box) {
      return box.maxY;
   }

   public static enum SpeedMode {
      VANILLA,
      STRAFE;
   }
}

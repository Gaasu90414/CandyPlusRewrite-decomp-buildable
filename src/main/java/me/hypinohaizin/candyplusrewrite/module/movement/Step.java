//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Step extends Module {
   public Setting<Integer> height = this.register(new Setting("Height", 2, 10, 1));
   public Setting<Step.Mode> mode;
   public Setting<Boolean> pauseBurrow;
   public Setting<Boolean> pauseSneak;
   public Setting<Boolean> pauseWeb;
   public Setting<Boolean> onlyMoving;
   public Setting<Boolean> spoof;
   public Setting<Integer> delay;
   public Setting<Boolean> turnOff;
   private final double[] oneBlockOffset = new double[]{0.42D, 0.753D};
   private final double[] oneFiveOffset = new double[]{0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D};
   private final double[] twoOffset = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D};
   private final double[] twoFiveOffset = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D};

   public Step() {
      super("Step", Module.Categories.MOVEMENT, false, false);
      this.mode = this.register(new Setting("Mode", Step.Mode.Vanilla));
      this.pauseBurrow = this.register(new Setting("PauseBurrow", true));
      this.pauseSneak = this.register(new Setting("PauseSneak", true));
      this.pauseWeb = this.register(new Setting("PauseWeb", true));
      this.onlyMoving = this.register(new Setting("OnlyMoving", true));
      this.spoof = this.register(new Setting("Spoof", true));
      this.delay = this.register(new Setting("Delay", 3, 20, 0));
      this.turnOff = this.register(new Setting("Disable", false));
   }

   public static double[] forward(double speed) {
      float forward = mc.player.movementInput.moveForward;
      float side = mc.player.movementInput.moveStrafe;
      float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (side < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         side = 0.0F;
         forward = forward > 0.0F ? 1.0F : -1.0F;
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         if ((mc.world.getBlockState(playerPos).getBlock() == Blocks.PISTON_HEAD || mc.world.getBlockState(playerPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(playerPos).getBlock() == Blocks.BEDROCK) && (Boolean)this.pauseBurrow.getValue()) {
            mc.player.stepHeight = 0.1F;
         } else if ((mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.PISTON_HEAD || mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.BEDROCK) && (Boolean)this.pauseBurrow.getValue()) {
            mc.player.stepHeight = 0.1F;
         } else if ((Boolean)this.pauseWeb.getValue() && mc.player.isInWeb) {
            mc.player.stepHeight = 0.1F;
         } else if ((Boolean)this.pauseSneak.getValue() && mc.player.isSneaking()) {
            mc.player.stepHeight = 0.1F;
         } else if ((Boolean)this.onlyMoving.getValue() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F) {
            mc.player.stepHeight = 0.1F;
         } else if (!mc.player.isInWater() && !mc.player.isInLava() && !mc.player.isOnLadder() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            if (this.mode.getValue() == Step.Mode.Normal) {
               mc.player.stepHeight = 0.6F;
               double[] dir = forward(0.1D);
               boolean one = false;
               boolean onefive = false;
               boolean two = false;
               boolean twofive = false;
               AxisAlignedBB bb = mc.player.getEntityBoundingBox();
               if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.0D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 0.6D, dir[1])).isEmpty()) {
                  one = true;
               }

               if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.4D, dir[1])).isEmpty()) {
                  onefive = true;
               }

               if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.1D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.9D, dir[1])).isEmpty()) {
                  two = true;
               }

               if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.4D, dir[1])).isEmpty()) {
                  twofive = true;
               }

               if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
                  double[] var8;
                  int var9;
                  int var10;
                  double off;
                  if (one && (float)(Integer)this.height.getValue() >= 1.0F) {
                     var8 = this.oneBlockOffset;
                     var9 = var8.length;

                     for(var10 = 0; var10 < var9; ++var10) {
                        off = var8[var10];
                        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     }

                     mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ);
                  }

                  if (onefive && (float)(Integer)this.height.getValue() >= 1.5F) {
                     var8 = this.oneFiveOffset;
                     var9 = var8.length;

                     for(var10 = 0; var10 < var9; ++var10) {
                        off = var8[var10];
                        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     }

                     mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5D, mc.player.posZ);
                  }

                  if (two && (float)(Integer)this.height.getValue() >= 2.0F) {
                     var8 = this.twoOffset;
                     var9 = var8.length;

                     for(var10 = 0; var10 < var9; ++var10) {
                        off = var8[var10];
                        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     }

                     mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0D, mc.player.posZ);
                  }

                  if (twofive && (float)(Integer)this.height.getValue() >= 2.5F) {
                     var8 = this.twoFiveOffset;
                     var9 = var8.length;

                     for(var10 = 0; var10 < var9; ++var10) {
                        off = var8[var10];
                        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     }

                     mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5D, mc.player.posZ);
                  }
               }
            }

            if (this.mode.getValue() == Step.Mode.Vanilla) {
               mc.player.stepHeight = (float)(Integer)this.height.getValue();
            }

         } else {
            mc.player.stepHeight = 0.1F;
         }
      }
   }

   public void onDisable() {
      mc.player.stepHeight = 0.6F;
   }

   public static enum Mode {
      Vanilla,
      Normal;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockMove extends Module {
   public Setting<Boolean> middle = this.register(new Setting("Middle", true));
   public Setting<Integer> delay = this.register(new Setting("Delay", 250, 2000, 0));
   public Setting<Boolean> only = this.register(new Setting("Only In Block", true));
   public Setting<Boolean> avoid = this.register(new Setting("Avoid Out", true, (v) -> {
      return !(Boolean)this.only.getValue();
   }));
   public Setting<Float> verticalSpeed = this.register(new Setting("VerticalSpeed", 0.3F, 1.0F, 0.05F));
   public Setting<Boolean> voidSafe = this.register(new Setting("VoidSafe", true));
   public Setting<Float> voidY = this.register(new Setting("VoidY", 60.0F, 256.0F, 0.0F, (v) -> {
      return (Boolean)this.voidSafe.getValue();
   }));
   private final Timer timer = new Timer();
   private final Vec3d[] sides = new Vec3d[]{new Vec3d(0.24D, 0.0D, 0.24D), new Vec3d(-0.24D, 0.0D, 0.24D), new Vec3d(0.24D, 0.0D, -0.24D), new Vec3d(-0.24D, 0.0D, -0.24D)};

   public BlockMove() {
      super("BlockMove", Module.Categories.MOVEMENT, false, false);
   }

   public void onEnable() {
      super.onEnable();
      this.timer.reset();
   }

   @SubscribeEvent
   public void onInputUpdate(InputUpdateEvent event) {
      if (mc.player != null && mc.world != null) {
         double vy = (double)(Float)this.voidY.getValue();
         if ((Boolean)this.voidSafe.getValue() && mc.player.posY <= vy) {
            mc.player.setPosition(mc.player.posX, vy + 1.0D, mc.player.posZ);
            this.cancelInput(event);
            this.timer.reset();
         } else {
            Vec3d posVec = mc.player.getPositionVector();
            boolean air = true;
            AxisAlignedBB playerBox = mc.player.getEntityBoundingBox();
            Vec3d[] var7 = this.sides;
            int var8 = var7.length;

            int dz;
            for(int var9 = 0; var9 < var8; ++var9) {
               Vec3d side = var7[var9];
               if (!air) {
                  break;
               }

               for(dz = 0; dz < 2; ++dz) {
                  BlockPos pos = new BlockPos(posVec.add(side).add(0.0D, (double)dz, 0.0D));
                  if (!BlockUtil.isAir(pos)) {
                     AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
                     if (box != null && playerBox.intersects(box)) {
                        air = false;
                        break;
                     }
                  }
               }
            }

            if (!(Boolean)this.only.getValue() || !air) {
               MovementInput input = event.getMovementInput();
               if (this.timer.passedMs((long)(Integer)this.delay.getValue())) {
                  double nextY;
                  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                     nextY = mc.player.posY + (double)(Float)this.verticalSpeed.getValue();
                     mc.player.setPosition(mc.player.posX, nextY, mc.player.posZ);
                     this.timer.reset();
                     this.cancelInput(event);
                     return;
                  }

                  if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                     nextY = mc.player.posY - (double)(Float)this.verticalSpeed.getValue();
                     if ((Boolean)this.voidSafe.getValue() && nextY <= vy) {
                        nextY = vy + 1.0D;
                     }

                     mc.player.setPosition(mc.player.posX, nextY, mc.player.posZ);
                     this.timer.reset();
                     this.cancelInput(event);
                     return;
                  }

                  BlockPos playerPos = (Boolean)this.middle.getValue() ? PlayerUtil.getPlayerPos() : new BlockPos((double)Math.round(posVec.x), posVec.y, (double)Math.round(posVec.z));
                  EnumFacing facing = mc.player.getHorizontalFacing();
                  int dx = playerPos.offset(facing).getX() - playerPos.getX();
                  dz = playerPos.offset(facing).getZ() - playerPos.getZ();
                  boolean addX = dx != 0;
                  Vec3d target = posVec;
                  if (input.forwardKeyDown) {
                     target = this.moveTo(playerPos, addX, addX ? dx < 0 : dz < 0);
                  } else if (input.backKeyDown) {
                     target = this.moveTo(playerPos, addX, addX ? dx > 0 : dz > 0);
                  } else if (input.leftKeyDown) {
                     target = this.moveTo(playerPos, !addX, addX ? dx > 0 : dz < 0);
                  } else if (input.rightKeyDown) {
                     target = this.moveTo(playerPos, !addX, addX ? dx < 0 : dz > 0);
                  }

                  if (target != null) {
                     mc.player.setPosition(target.x, target.y, target.z);
                     this.timer.reset();
                     this.cancelInput(event);
                  }
               }

            }
         }
      }
   }

   private Vec3d moveTo(BlockPos base, boolean xDir, boolean negative) {
      return negative ? this.toVec3d(base.add(xDir ? -1 : 0, 0, xDir ? 0 : -1)) : this.toVec3d(base.add(xDir ? 1 : 0, 0, xDir ? 0 : 1));
   }

   private Vec3d toVec3d(BlockPos pos) {
      if ((Boolean)this.middle.getValue()) {
         return new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
      } else {
         Vec3d last = new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
         boolean any = !mc.world.isAirBlock(pos) || !mc.world.isAirBlock(pos.up());
         Vec3d v = new Vec3d((double)pos.getX() - 1.0E-8D, (double)pos.getY(), (double)pos.getZ());
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
            last = v;
         } else {
            any = true;
         }

         v = new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ() - 1.0E-8D);
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
            last = v;
         } else {
            any = true;
         }

         v = new Vec3d((double)pos.getX() - 1.0E-8D, (double)pos.getY(), (double)pos.getZ() - 1.0E-8D);
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
            last = v;
         } else {
            any = true;
         }

         return !(Boolean)this.only.getValue() && !any && (Boolean)this.avoid.getValue() ? null : last;
      }
   }

   private void cancelInput(InputUpdateEvent event) {
      event.getMovementInput().forwardKeyDown = false;
      event.getMovementInput().backKeyDown = false;
      event.getMovementInput().leftKeyDown = false;
      event.getMovementInput().rightKeyDown = false;
      event.getMovementInput().moveForward = 0.0F;
      event.getMovementInput().moveStrafe = 0.0F;
   }
}

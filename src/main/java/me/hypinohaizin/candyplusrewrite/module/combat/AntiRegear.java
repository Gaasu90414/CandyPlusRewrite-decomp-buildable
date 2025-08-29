//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;

public class AntiRegear extends Module {
   public Setting<Boolean> outline = this.register(new Setting("Outline", true));
   public Setting<Integer> red = this.register(new Setting("Red", 176, 255, 0));
   public Setting<Integer> green = this.register(new Setting("Green", 118, 255, 0));
   public Setting<Integer> blue = this.register(new Setting("Blue", 255, 255, 0));
   public Setting<Integer> alpha = this.register(new Setting("Alpha", 60, 255, 0));
   public Setting<Integer> outlineAlpha = this.register(new Setting("O-Alpha", 255, 255, 0));
   public Setting<Boolean> render = this.register(new Setting("Render", true));
   public Setting<Boolean> pickOnly = this.register(new Setting("Pickaxe Only", false));
   public Setting<Boolean> enderChest = this.register(new Setting("E-Chest Support", false));
   public Setting<Float> range = this.register(new Setting("Range", 5.0F, 6.0F, 1.0F));
   public Setting<Float> delay = this.register(new Setting("Delay", 1.0F, 5.0F, 0.0F));
   public Setting<Float> wallRange = this.register(new Setting("Wall-Range", 3.0F, 6.0F, 0.0F));
   public Setting<Float> targetRange = this.register(new Setting("Enemy-Range", 5.0F, 12.0F, 0.0F));
   public Setting<AntiRegear.RotationMode> rotationMode;
   public Setting<Boolean> strict;
   private final Timer timer = new Timer();
   private boolean isBreakingBlock = false;
   private BlockPos breakingPos = null;

   public AntiRegear() {
      super("AntiRegear", Module.Categories.COMBAT, false, false);
      this.rotationMode = this.register(new Setting("Rotation Mode", AntiRegear.RotationMode.Vanilla));
      this.strict = this.register(new Setting("Strict", true));
   }

   public void onRender3D() {
      if (!this.nullCheck()) {
         if ((Boolean)this.render.getValue() && this.isBreakingBlock && this.breakingPos != null) {
            Block block = mc.world.getBlockState(this.breakingPos).getBlock();
            if (block instanceof BlockShulkerBox || (Boolean)this.enderChest.getValue() && block instanceof BlockEnderChest) {
               Color boxColor = new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), (Integer)this.alpha.getValue());
               RenderUtil3D.drawBox(this.breakingPos, 1.0D, boxColor, (Integer)this.alpha.getValue());
               if ((Boolean)this.outline.getValue()) {
                  Color outlineColor = new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), (Integer)this.outlineAlpha.getValue());
                  RenderUtil3D.drawBoundingBox(this.breakingPos, 1.0D, 1.0F, outlineColor);
               }
            }

         }
      }
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if (this.timer.passedMs((long)((Float)this.delay.getValue() * 1000.0F))) {
            if (!(Boolean)this.strict.getValue() || mc.player.onGround) {
               this.isBreakingBlock = false;
               this.breakingPos = null;
               BlockPos playerPos = mc.player.getPosition();
               Iterator var2 = BlockPos.getAllInBox(playerPos.add((double)(-(Float)this.range.getValue()), (double)(-(Float)this.range.getValue()), (double)(-(Float)this.range.getValue())), playerPos.add((double)(Float)this.range.getValue(), (double)(Float)this.range.getValue(), (double)(Float)this.range.getValue())).iterator();

               while(var2.hasNext()) {
                  BlockPos pos = (BlockPos)var2.next();
                  Block block = mc.world.getBlockState(pos).getBlock();
                  if (block instanceof BlockShulkerBox || (Boolean)this.enderChest.getValue() && block instanceof BlockEnderChest) {
                     if ((Boolean)this.pickOnly.getValue() && !(mc.player.inventory.getCurrentItem().getItem() instanceof ItemPickaxe)) {
                        return;
                     }

                     if ((!this.isBlockBehindWall(pos) || !(pos.distanceSq(playerPos) > (double)((Float)this.wallRange.getValue() * (Float)this.wallRange.getValue()))) && this.isEnemyNearby(pos)) {
                        this.faceBlock(pos);
                        mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.timer.reset();
                        this.isBreakingBlock = true;
                        this.breakingPos = pos;
                        break;
                     }
                  }
               }

            }
         }
      }
   }

   private boolean isBlockBehindWall(BlockPos pos) {
      Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
      Vec3d posVec = new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
      RayTraceResult result = mc.world.rayTraceBlocks(eyesPos, posVec, false, true, false);
      return result != null && result.typeOfHit == Type.BLOCK && !result.getBlockPos().equals(pos);
   }

   private boolean isEnemyNearby(BlockPos pos) {
      Iterator var2 = mc.world.playerEntities.iterator();

      EntityPlayer player;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         player = (EntityPlayer)var2.next();
      } while(player == mc.player || FriendManager.isFriend(player.getName()) || !(player.getDistanceSq(pos) <= (double)((Float)this.targetRange.getValue() * (Float)this.targetRange.getValue())));

      return true;
   }

   private void faceBlock(BlockPos pos) {
      Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
      Vec3d posVec = new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
      double diffX = posVec.x - eyesPos.x;
      double diffY = posVec.y - eyesPos.y;
      double diffZ = posVec.z - eyesPos.z;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D));
      if (this.rotationMode.getValue() == AntiRegear.RotationMode.Vanilla) {
         mc.player.rotationYaw = yaw;
         mc.player.rotationPitch = pitch;
      }

   }

   public static enum RotationMode {
      Vanilla;
   }
}

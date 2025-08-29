//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockUtil implements Util {
   public static List<Block> emptyBlocks;
   public static final List<Block> blackList;
   public static List<Block> unSolidBlocks;
   public static final List<Block> unSafeBlocks;
   public static List<Block> rightclickableBlocks;

   public static AxisAlignedBB getBoundingBox(BlockPos pos) {
      if (pos == null) {
         return null;
      } else {
         AxisAlignedBB box = getState(pos).getCollisionBoundingBox(mc.world, pos);
         return box == null ? null : new AxisAlignedBB((double)pos.getX() + box.minX, (double)pos.getY() + box.minY, (double)pos.getZ() + box.minZ, (double)pos.getX() + box.maxX, (double)pos.getY() + box.maxY, (double)pos.getZ() + box.maxZ);
      }
   }

   public static boolean isAir(Block block) {
      return block == Blocks.AIR || block instanceof BlockAir;
   }

   public static boolean isAir(BlockPos pos) {
      return isAir(mc.world.getBlockState(pos).getBlock());
   }

   public static boolean isAirBlock(Block block) {
      return block == Blocks.AIR || block instanceof BlockAir;
   }

   public static boolean isAirBlock(BlockPos pos) {
      return isAirBlock(mc.world.getBlockState(pos).getBlock());
   }

   public static boolean placeBlock(BlockPos pos, boolean packet) {
      Block block = mc.world.getBlockState(pos).getBlock();
      if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
         return false;
      } else {
         EnumFacing side = getPlaceableSide(pos);
         if (side == null) {
            return false;
         } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            if (!canBeClicked(neighbour)) {
               return false;
            } else {
               Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
               Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
               if (packet) {
                  rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, opposite);
               } else {
                  mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
                  mc.player.swingArm(EnumHand.MAIN_HAND);
               }

               return true;
            }
         }
      }
   }

   public static void faceplace(BlockPos pos, EnumHand hand, EnumFacing facing, boolean packet) {
      if (packet) {
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.5F, 0.5F, 0.5F));
      } else {
         mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, new Vec3d(0.5D, 0.5D, 0.5D), hand);
      }

   }

   public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet) {
      EnumFacing side = getPlaceableSide(pos);
      if (side == null) {
         return false;
      } else {
         BlockPos neighbour = pos.offset(side);
         EnumFacing opposite = side.getOpposite();
         Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
         Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
         boolean sneaking = false;
         if (rightclickableBlocks.contains(neighbourBlock) && !mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
            sneaking = true;
         }

         if (rotate) {
            faceVector(hitVec, true);
         }

         if (packet) {
            rightClickBlock(neighbour, hitVec, hand, opposite, true);
         } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, hand);
            mc.player.swingArm(hand);
         }

         if (sneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
         }

         return true;
      }
   }

   public static void rightClickBlock(BlockPos pos, EnumFacing facing, boolean packet) {
      Vec3d hitVec = (new Vec3d(pos)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));
      if (packet) {
         rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
      } else {
         mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
         mc.player.swingArm(EnumHand.MAIN_HAND);
      }

   }

   public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet) {
      Vec3d hitVec = (new Vec3d(pos)).add(hVec).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));
      if (packet) {
         rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
      } else {
         mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
         mc.player.swingArm(EnumHand.MAIN_HAND);
      }

   }

   public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
      float f = (float)(vec.x - (double)pos.getX());
      float f2 = (float)(vec.y - (double)pos.getY());
      float f3 = (float)(vec.z - (double)pos.getZ());
      mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
   }

   public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
      if (packet) {
         float f = (float)(vec.x - (double)pos.getX());
         float f2 = (float)(vec.y - (double)pos.getY());
         float f3 = (float)(vec.z - (double)pos.getZ());
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
      } else {
         mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
      }

      mc.player.swingArm(EnumHand.MAIN_HAND);
   }

   public static boolean canRightClickForPlace(BlockPos pos) {
      return !rightclickableBlocks.contains(getBlock(pos));
   }

   public static boolean canBeClicked(BlockPos pos) {
      return getBlock(pos).canCollideCheck(getState(pos), false);
   }

   public static Block getBlock(BlockPos pos) {
      return getState(pos).getBlock();
   }

   public static Block getBlock(double x, double y, double z) {
      return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
   }

   private static IBlockState getState(BlockPos pos) {
      return mc.world.getBlockState(pos);
   }

   public static boolean checkForNeighbours(BlockPos blockPos) {
      if (!hasNeighbour(blockPos)) {
         EnumFacing[] var1 = EnumFacing.values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing side = var1[var3];
            BlockPos neighbour = blockPos.offset(side);
            if (hasNeighbour(neighbour)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public static EnumFacing getPlaceableSide(BlockPos pos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = pos.offset(side);
         if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable() && !blackList.contains(getBlock(neighbour))) {
               return side;
            }
         }
      }

      return null;
   }

   public static boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = blockPos.offset(side);
         if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable() && !blackList.contains(getBlock(neighbour))) {
            return true;
         }
      }

      return false;
   }

   public static boolean canBeNeighbour(BlockPos pos, BlockPos a) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos neighbour = pos.offset(side);
         if (a.equals(neighbour)) {
            return true;
         }
      }

      return false;
   }

   public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      ArrayList<BlockPos> circleblocks = new ArrayList();
      int cx = pos.getX();
      int cy = pos.getY();
      int cz = pos.getZ();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            int y = sphere ? cy - (int)r : cy;

            while(true) {
               float f = (float)y;
               float f2 = sphere ? (float)cy + r : (float)(cy + h);
               if (f >= f2) {
                  break;
               }

               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0F) * (r - 1.0F)))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }

               ++y;
            }
         }
      }

      return circleblocks;
   }

   public static boolean canPlaceBlock(BlockPos pos) {
      return (getBlock(pos) == Blocks.AIR || getBlock(pos) instanceof BlockLiquid) && hasNeighbour(pos) && !blackList.contains(getBlock(pos));
   }

   public static boolean canPlaceBlockFuture(BlockPos pos) {
      return (getBlock(pos) == Blocks.AIR || getBlock(pos) instanceof BlockLiquid) && !blackList.contains(getBlock(pos));
   }

   public static boolean isBlockSolid(BlockPos pos) {
      return !isBlockUnSolid(pos);
   }

   public static boolean isBlockUnSolid(BlockPos pos) {
      return isBlockUnSolid(mc.world.getBlockState(pos).getBlock());
   }

   public static boolean isBlockUnSolid(Block block) {
      return unSolidBlocks.contains(block);
   }

   public static boolean isBlockUnSafe(Block block) {
      return unSafeBlocks.contains(block);
   }

   public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
      BlockPos[] list = new BlockPos[vec3ds.length];

      for(int i = 0; i < vec3ds.length; ++i) {
         list[i] = new BlockPos(vec3ds[i]);
      }

      return list;
   }

   public static double getDistance(double blockposx, double blockposy, double blockposz, double blockposx1, double blockposy1, double blockposz1) {
      double deltaX = blockposx1 - blockposx;
      double deltaY = blockposy1 - blockposy;
      double deltaZ = blockposz1 - blockposz;
      return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
   }

   public static boolean canBreak(BlockPos pos) {
      IBlockState blockState = mc.world.getBlockState(pos);
      Block block = blockState.getBlock();
      return block.getBlockHardness(blockState, mc.world, pos) != -1.0F;
   }

   public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing) {
      RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY() - 0.5D, (double)pos.getZ() + 0.5D));
      EnumFacing facing = result != null && result.sideHit != null ? result.sideHit : EnumFacing.UP;
      mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
      if (swing) {
         mc.player.connection.sendPacket(new CPacketAnimation(hand));
      }

   }

   public static boolean placeBlock(BlockPos pos, int slot, boolean rotate, boolean rotateBack, EnumHand hand) {
      if (isBlockEmpty(pos)) {
         int old_slot = mc.player.inventory.currentItem;
         if (slot != mc.player.inventory.currentItem) {
            mc.player.inventory.currentItem = slot;
         }

         EnumFacing[] values = EnumFacing.values();
         EnumFacing[] var8 = values;
         int var9 = values.length;

         for (int var10 = 0; var10 < var9; ++var10) {
            EnumFacing f = var8[var10];
            Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();
            Vec3d vec = new Vec3d((double)pos.getX() + 0.5D + (double)f.getXOffset() * 0.5D, (double)pos.getY() + 0.5D + (double)f.getYOffset() * 0.5D, (double)pos.getZ() + 0.5D + (double)f.getZOffset() * 0.5D);
            if (!emptyBlocks.contains(neighborBlock) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25D) {
               float[] rot = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};
               if (rotate) {
                  rotatePacket(vec.x, vec.y, vec.z);
               }

               if (rightclickableBlocks.contains(neighborBlock)) {
                  mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
               }

               mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(f), f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
               if (rightclickableBlocks.contains(neighborBlock)) {
                  mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
               }

               if (rotateBack) {
                  mc.player.connection.sendPacket(new Rotation(rot[0], rot[1], mc.player.onGround));
               }

               mc.player.swingArm(EnumHand.MAIN_HAND);
               if (slot != old_slot) {
                  mc.player.inventory.currentItem = old_slot;
               }
               return true;
            }
         }
      }
      return false;
   }

   public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
      boolean sneaking = false;
      EnumFacing side = getFirstFacing(pos);
      if (side == null) {
         return isSneaking;
      } else {
         BlockPos neighbour = pos.offset(side);
         EnumFacing opposite = side.getOpposite();
         Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
         Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
         if (!mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
            mc.player.setSneaking(true);
            sneaking = true;
         }

         if (rotate) {
            faceVector(hitVec, true);
         }

         rightClickBlock(neighbour, hitVec, hand, opposite, packet);
         mc.player.swingArm(EnumHand.MAIN_HAND);
         return sneaking || isSneaking;
      }
   }

   public static void faceVector(Vec3d vec, boolean normalizeAngle) {
      float[] rotations = getLegitRotations(vec);
      mc.player.connection.sendPacket(new Rotation(rotations[0], normalizeAngle ? (float)MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1], mc.player.onGround));
   }

   public static float[] getLegitRotations(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.x - eyesPos.x;
      double diffY = vec.y - eyesPos.y;
      double diffZ = vec.z - eyesPos.z;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
   }

   public static EnumFacing getFirstFacing(BlockPos pos) {
      Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
      if (iterator.hasNext()) {
         EnumFacing facing = (EnumFacing)iterator.next();
         return facing;
      } else {
         return null;
      }
   }

   public static Vec3d getEyesPos() {
      return new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
   }

   public static List<EnumFacing> getPossibleSides(BlockPos pos) {
      List<EnumFacing> facings = new ArrayList();
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos neighbour = pos.offset(side);
         if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
               facings.add(side);
            }
         }
      }

      return facings;
   }

   public static void rotatePacket(double x, double y, double z) {
      double diffX = x - mc.player.posX;
      double diffY = y - (mc.player.posY + (double)mc.player.getEyeHeight());
      double diffZ = z - mc.player.posZ;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
   }

   public static boolean isBlockEmpty(BlockPos pos) {
      try {
         if (emptyBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
            AxisAlignedBB box = new AxisAlignedBB(pos);
            Iterator var2 = mc.world.loadedEntityList.iterator();

            Entity e;
            do {
               if (!var2.hasNext()) {
                  return true;
               }

               e = (Entity)var2.next();
            } while(!(e instanceof EntityLivingBase) || !box.intersects(e.getEntityBoundingBox()));

            return false;
         }
      } catch (Exception var4) {
      }

      return false;
   }

   public static EnumFacing getBackwardFacing(EnumFacing facing) {
      Vec3i vec = facing.getDirectionVec();
      return EnumFacing.getFacingFromVector((float)(vec.getX() * -1), (float)(vec.getY() * -1), (float)(vec.getZ() * -1));
   }

   public static float[] calcAngle(Vec3d from, Vec3d to) {
      double difX = to.x - from.x;
      double difY = (to.y - from.y) * -1.0D;
      double difZ = to.z - from.z;
      double dist = (double)MathHelper.sqrt(difX * difX + difZ * difZ);
      return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
   }

   public static Rotation getFaceVectorPacket(Vec3d vec, Boolean roundAngles) {
      float[] rotations = getNeededRotations2(vec);
      Rotation e = new Rotation(rotations[0], roundAngles ? (float)MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1], mc.player.onGround);
      mc.player.connection.sendPacket(e);
      return e;
   }

   public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
      double difX = to.x - from.x;
      double difZ = to.z - from.z;
      return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D)};
   }

   private static float[] getNeededRotations2(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.x - eyesPos.x;
      double diffY = vec.y - eyesPos.y;
      double diffZ = vec.z - eyesPos.z;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
   }

   public static double getMaxHeight(AxisAlignedBB box) {
      if (mc.player != null && mc.world != null) {
         List<AxisAlignedBB> collisions = mc.world.getCollisionBoxes(mc.player, box.offset(0.0D, -1.0D, 0.0D));
         double maxY = 0.0D;
         boolean updated = false;
         Iterator var5 = collisions.iterator();

         while(var5.hasNext()) {
            AxisAlignedBB collision = (AxisAlignedBB)var5.next();
            if (collision.maxY > maxY) {
               updated = true;
               maxY = collision.maxY;
            }
         }

         return updated ? maxY : Double.NaN;
      } else {
         return Double.NaN;
      }
   }

   static {
      emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
      blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
      unSolidBlocks = Arrays.asList(Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.AIR, Blocks.PORTAL, Blocks.END_PORTAL, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.SAPLING, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.VINE, Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH);
      unSafeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
      rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
   }
}

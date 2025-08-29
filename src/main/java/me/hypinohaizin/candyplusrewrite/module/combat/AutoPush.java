//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoPush extends Module {
   public Setting<Float> preDelay = this.register(new Setting("BlockDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> placeDelay = this.register(new Setting("PlaceDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", false));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", false));
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
   public Setting<AutoPush.RedStone> redStoneType;
   public Setting<AutoPush.Target> targetType;
   public Setting<Float> targetRange;
   public EntityPlayer target;
   public int pistonSlot;
   public int redstoneSlot;
   public int obbySlot;
   public BlockPos pistonPos;
   public BlockPos redstonePos;
   public int stage;
   public Timer preTimer;
   public Timer timer;
   public int oldslot;
   public EnumHand oldhand;
   public boolean isTorch;

   public AutoPush() {
      super("AutoPush", Module.Categories.COMBAT, false, false);
      this.redStoneType = this.register(new Setting("Redstone", AutoPush.RedStone.Both));
      this.targetType = this.register(new Setting("Target", AutoPush.Target.Nearest));
      this.targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
      this.target = null;
      this.obbySlot = -1;
      this.redstonePos = null;
      this.stage = 0;
      this.timer = null;
      this.oldslot = -1;
      this.oldhand = null;
      this.isTorch = false;
   }

   public void reset() {
      this.target = null;
      this.pistonSlot = -1;
      this.redstoneSlot = -1;
      this.obbySlot = -1;
      this.pistonPos = null;
      this.redstonePos = null;
      this.stage = 0;
      this.preTimer = null;
      this.timer = null;
      this.oldslot = -1;
      this.oldhand = null;
      this.isTorch = false;
   }

   public void onEnable() {
      this.reset();
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if (!this.findMaterials()) {
            this.sendMessage("Cannot find materials! disabling...");
            this.disable();
         } else {
            this.target = this.findTarget();
            if (this.target == null) {
               this.sendMessage("Cannot find target! disabling...");
               this.disable();
            } else if ((this.isNull(this.pistonPos) || this.isNull(this.redstonePos)) && !this.findSpace(this.target)) {
               this.sendMessage("Cannot find space! disabling...");
               this.disable();
            } else {
               if (this.preTimer == null) {
                  this.preTimer = new Timer();
               }

               if (this.preTimer.passedX((double)(Float)this.preDelay.getValue()) && !this.prepareBlock()) {
                  this.restoreItem();
               } else {
                  if (this.timer == null) {
                     this.timer = new Timer();
                  }

                  if (this.stage == 0 && this.timer.passedX((double)(Float)this.placeDelay.getValue())) {
                     this.setItem(this.pistonSlot);
                     Vec3d targetCenter = new Vec3d(this.target.posX, this.target.posY, this.target.posZ);
                     float[] rotation = this.calculatePistonRotation(this.pistonPos, targetCenter);
                     mc.player.connection.sendPacket(new Rotation(rotation[0], rotation[1], mc.player.onGround));
                     BlockUtil.placeBlock(this.pistonPos, (Boolean)this.packetPlace.getValue());
                     this.stage = 1;
                     this.timer.reset();
                  }

                  if (this.stage == 1 && this.timer.passedX((double)(Float)this.placeDelay.getValue())) {
                     this.setItem(this.redstoneSlot);
                     BlockUtil.placeBlock(this.redstonePos, (Boolean)this.packetPlace.getValue());
                     this.stage = 2;
                     this.disable();
                     this.reset();
                  }

                  this.restoreItem();
               }
            }
         }
      }
   }

   public void setItem(int slot) {
      if ((Boolean)this.silentSwitch.getValue()) {
         this.oldhand = null;
         if (mc.player.isHandActive()) {
            this.oldhand = mc.player.getActiveHand();
         }

         this.oldslot = mc.player.inventory.currentItem;
         mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
      } else {
         mc.player.inventory.currentItem = slot;
         mc.playerController.updateController();
      }

   }

   public void restoreItem() {
      if (this.oldslot != -1 && (Boolean)this.silentSwitch.getValue()) {
         if (this.oldhand != null) {
            mc.player.setActiveHand(this.oldhand);
         }

         mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
         this.oldslot = -1;
         this.oldhand = null;
      }

   }

   public boolean isNull(Object object) {
      return object == null;
   }

   public boolean findSpace(EntityPlayer target) {
      BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      List<AutoPush.AutoPushPos> poses = new ArrayList();
      BlockPos[] var6 = offsets;
      int var7 = offsets.length;

      label58:
      for(int var8 = 0; var8 < var7; ++var8) {
         BlockPos offset = var6[var8];
         AutoPush.AutoPushPos pos = new AutoPush.AutoPushPos();
         BlockPos base = targetPos.add(offset);
         if (BlockUtil.getBlock(base) != Blocks.AIR) {
            BlockPos pistonPos = base.add(0, 1, 0);
            if (BlockUtil.getBlock(pistonPos) == Blocks.AIR && !this.checkPos(pistonPos) && (PlayerUtil.getDistance(pistonPos) >= 3.6D || pistonPos.getY() <= mypos.getY() + 1) && BlockUtil.getBlock(targetPos.add(offset.getX() * -1, 1, offset.getZ() * -1)) == Blocks.AIR) {
               List<BlockPos> redstonePoses = new ArrayList();
               List<BlockPos> roffsets = this.getBlockPos();
               Iterator var15 = roffsets.iterator();

               while(true) {
                  BlockPos redstonePos;
                  do {
                     if (!var15.hasNext()) {
                        BlockPos redstonePos2 = (BlockPos)redstonePoses.stream().min(Comparator.comparing((b) -> {
                           return mc.player.getDistance((double)b.getX(), (double)b.getY(), (double)b.getZ());
                        })).orElse((BlockPos) null);
                        if (redstonePos2 != null) {
                           pos.setPiston(pistonPos);
                           pos.setRedStone(redstonePos2);
                           poses.add(pos);
                        }
                        continue label58;
                     }

                     BlockPos roffset = (BlockPos)var15.next();
                     redstonePos = pistonPos.add(roffset);
                  } while(redstonePos.getX() == targetPos.getX() && redstonePos.getZ() == targetPos.getZ());

                  if (!this.checkPos(redstonePos) && BlockUtil.getBlock(redstonePos) == Blocks.AIR) {
                     redstonePoses.add(redstonePos);
                  }
               }
            }
         }
      }

      AutoPush.AutoPushPos bestPos = (AutoPush.AutoPushPos)poses.stream().filter((p) -> {
         return p.getMaxRange() <= (double)(Float)this.range.getValue();
      }).min(Comparator.comparing((p) -> {
         return p.getMaxRange();
      })).orElse((AutoPushPos) null);
      if (bestPos != null) {
         this.pistonPos = bestPos.piston;
         this.redstonePos = bestPos.redstone;
         return true;
      } else {
         return false;
      }
   }

   private List<BlockPos> getBlockPos() {
      List<BlockPos> roffsets = new ArrayList();
      roffsets.add(new BlockPos(1, 0, 0));
      roffsets.add(new BlockPos(-1, 0, 0));
      roffsets.add(new BlockPos(0, 0, 1));
      roffsets.add(new BlockPos(0, 0, -1));
      if (this.redStoneType.getValue() == AutoPush.RedStone.Block) {
         roffsets.add(new BlockPos(0, 1, 0));
      }

      return roffsets;
   }

   public EntityPlayer findTarget() {
      EntityPlayer target = null;
      List<EntityPlayer> players = mc.world.playerEntities;
      if (this.targetType.getValue() == AutoPush.Target.Nearest) {
         target = PlayerUtil.getNearestPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AutoPush.Target.Looking) {
         target = PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AutoPush.Target.Best) {
         target = (EntityPlayer)players.stream().filter((p) -> {
            return p.getEntityId() != mc.player.getEntityId();
         }).filter((p) -> {
            return !FriendManager.isFriend(p.getName());
         }).filter((p) -> {
            return this.findSpace(p);
         }).min(Comparator.comparing((p) -> {
            return PlayerUtil.getDistance((Entity)p);
         })).orElse((EntityPlayer) null);
      }

      return target != null && FriendManager.isFriend(target.getName()) ? null : target;
   }

   public boolean findMaterials() {
      this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.PISTON);
      int redstoneBlock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
      int redstoneTorch = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_TORCH);
      this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
      if (this.itemCheck(this.pistonSlot)) {
         this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.STICKY_PISTON);
      }

      if (this.redStoneType.getValue() == AutoPush.RedStone.Block) {
         this.isTorch = false;
         this.redstoneSlot = redstoneBlock;
      }

      if (this.redStoneType.getValue() == AutoPush.RedStone.Torch) {
         this.isTorch = true;
         this.redstoneSlot = redstoneTorch;
      }

      if (this.redStoneType.getValue() == AutoPush.RedStone.Both) {
         this.isTorch = true;
         this.redstoneSlot = redstoneTorch;
         if (this.itemCheck(this.redstoneSlot)) {
            this.isTorch = false;
            this.redstoneSlot = redstoneBlock;
         }
      }

      return !this.itemCheck(this.redstoneSlot) && !this.itemCheck(this.pistonSlot) && !this.itemCheck(this.obbySlot);
   }

   public boolean checkPos(BlockPos pos) {
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      return pos.getX() == mypos.getX() && pos.getZ() == mypos.getZ();
   }

   public boolean itemCheck(int slot) {
      return slot == -1;
   }

   public boolean prepareBlock() {
      BlockPos piston = this.pistonPos.add(0, -1, 0);
      BlockPos redstone = this.redstonePos.add(0, -1, 0);
      if (BlockUtil.getBlock(piston) == Blocks.AIR) {
         this.setItem(this.obbySlot);
         BlockUtil.placeBlock(piston, (Boolean)this.packetPlace.getValue());
         if (this.delayCheck()) {
            return false;
         }
      }

      if (BlockUtil.getBlock(redstone) == Blocks.AIR) {
         this.setItem(this.obbySlot);
         BlockUtil.placeBlock(redstone, (Boolean)this.packetPlace.getValue());
         return !this.delayCheck();
      } else {
         return true;
      }
   }

   public float[] calculatePistonRotation(BlockPos pistonPos, Vec3d targetPos) {
      double deltaX = targetPos.x - ((double)pistonPos.getX() + 0.5D);
      double deltaY = targetPos.y - ((double)pistonPos.getY() + 0.5D);
      double deltaZ = targetPos.z - ((double)pistonPos.getZ() + 0.5D);
      double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
      float yaw = (float)Math.toDegrees(Math.atan2(-deltaX, deltaZ));
      float pitch = (float)Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));
      pitch = Math.max(-45.0F, Math.min(45.0F, pitch));
      return new float[]{yaw, pitch};
   }

   public boolean delayCheck() {
      return (Float)this.preDelay.getValue() != 0.0F;
   }

   public class AutoPushPos {
      public BlockPos piston;
      public BlockPos redstone;

      public double getMaxRange() {
         return this.piston != null && this.redstone != null ? Math.max(PlayerUtil.getDistance(this.piston), PlayerUtil.getDistance(this.redstone)) : 999999.0D;
      }

      public void setPiston(BlockPos piston) {
         this.piston = piston;
      }

      public void setRedStone(BlockPos redstone) {
         this.redstone = redstone;
      }
   }

   public static enum RedStone {
      Block,
      Torch,
      Both;
   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

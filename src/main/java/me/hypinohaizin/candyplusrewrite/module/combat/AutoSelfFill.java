//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.HoleUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoSelfFill extends Module {
   public Setting<Float> offset = this.register(new Setting("Offset", 0.6F, 1.0F, 0.0F));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", true));
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", true));
   public Setting<Float> detectRange = this.register(new Setting("DetectRange", 4.0F, 10.0F, 1.0F));
   public Setting<Integer> placeIntervalMs = this.register(new Setting("PlaceInterval", 200, 1000, 0));
   private EnumHand oldhand = null;
   private int oldslot = -1;
   private final Timer placeTimer = new Timer();

   public AutoSelfFill() {
      super("AutoSelfFill", Module.Categories.COMBAT, false, false);
   }

   public void onTick() {
      if (!this.nullCheck()) {
         BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         boolean inHole = HoleUtil.isObbyHole(playerPos) || HoleUtil.isBedrockHole(playerPos) || HoleUtil.isSafeHole(playerPos);
         if (inHole) {
            EntityPlayer target = (EntityPlayer)mc.world.playerEntities.stream().filter((p) -> {
               return p != mc.player;
            }).filter((p) -> {
               return !FriendManager.isFriend(p);
            }).filter((p) -> {
               return mc.player.getDistance(p) <= (Float)this.detectRange.getValue();
            }).findFirst().orElse((EntityPlayer) null);
            if (target != null) {
               int slot = InventoryUtil.findHotbarBlockWithClass(BlockTrapDoor.class);
               if (slot == -1) {
                  this.sendMessage("Cannot find TrapDoor! disabling");
                  this.disable();
               } else if (this.placeTimer.passedMs((long)(Integer)this.placeIntervalMs.getValue())) {
                  BlockPos trapPos = this.findPlacePosAround(playerPos);
                  if (trapPos != null) {
                     this.setItem(slot);
                     double x = mc.player.posX;
                     double y = mc.player.posY;
                     double z = mc.player.posZ;
                     mc.player.connection.sendPacket(new Position(x, y + (double)(Float)this.offset.getValue(), z, mc.player.onGround));
                     EnumFacing face = this.getFacingTowardPlayer(trapPos, playerPos);
                     if (face == null) {
                        face = EnumFacing.UP;
                     }

                     BlockUtil.rightClickBlock(trapPos, face, new Vec3d(0.5D, 0.6D, 0.5D), (Boolean)this.packetPlace.getValue());
                     mc.player.connection.sendPacket(new Position(x, y, z, mc.player.onGround));
                     this.restoreItem();
                     this.placeTimer.reset();
                  }
               }
            }
         }
      }
   }

   private BlockPos findPlacePosAround(BlockPos base) {
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      BlockPos[] var3 = offsets;
      int var4 = offsets.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockPos off = var3[var5];
         BlockPos pos = base.add(off);
         if (mc.world.isAirBlock(pos) && this.entityClear(pos) && BlockUtil.canPlaceBlock(pos)) {
            BlockPos attach = base.add(off.getX(), -1, off.getZ());
            if (!mc.world.isAirBlock(attach)) {
               return pos;
            }

            attach = base.add(off.getX(), 1, off.getZ());
            if (!mc.world.isAirBlock(attach)) {
               return pos;
            }

            BlockPos sideA = pos.offset(off.getX() == 0 ? EnumFacing.EAST : EnumFacing.NORTH);
            BlockPos sideB = pos.offset(off.getX() == 0 ? EnumFacing.WEST : EnumFacing.SOUTH);
            if (!mc.world.isAirBlock(sideA) || !mc.world.isAirBlock(sideB)) {
               return pos;
            }
         }
      }

      return null;
   }

   private EnumFacing getFacingTowardPlayer(BlockPos from, BlockPos playerPos) {
      EnumFacing[] var3 = EnumFacing.HORIZONTALS;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing f = var3[var5];
         if (from.offset(f).equals(playerPos)) {
            return f;
         }
      }

      return null;
   }

   private boolean entityClear(BlockPos pos) {
      AxisAlignedBB box = new AxisAlignedBB(pos);
      return mc.world.getEntitiesWithinAABB(Entity.class, box, (e) -> {
         return e instanceof EntityEnderCrystal || e instanceof EntityPlayer;
      }).isEmpty();
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
}

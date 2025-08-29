//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil extends Module {
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", true));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", true));
   public Setting<Boolean> entityCheck = this.register(new Setting("EntityCheck", true));
   public Setting<Boolean> crystalOnly = this.register(new Setting("CrystalOnly", false, (v) -> {
      return (Boolean)this.entityCheck.getValue();
   }));
   public BlockPos basePos;
   public int stage;
   private EnumHand oldhand = null;
   private int oldslot = -1;

   public SelfAnvil() {
      super("SelfAnvil", Module.Categories.COMBAT, false, false);
   }

   public void onEnable() {
      this.basePos = null;
      this.stage = 0;
   }

   public void onTick() {
      if (!this.nullCheck()) {
         int obby = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         int anvil = InventoryUtil.findHotbarBlock(Blocks.ANVIL);
         if (obby != -1 && anvil != -1) {
            BlockPos anvilPos = PlayerUtil.getPlayerPos().add(0, 2, 0);
            if (!BlockUtil.hasNeighbour(anvilPos)) {
               if ((this.basePos = this.findPos()) == null) {
                  this.sendMessage("Cannot find space! disabling");
                  this.disable();
                  return;
               }

               this.setItem(obby);
               BlockPos pos0 = this.basePos.add(0, 1, 0);
               BlockPos pos2 = this.basePos.add(0, 2, 0);
               BlockUtil.placeBlock(pos0, (Boolean)this.packetPlace.getValue());
               BlockUtil.rightClickBlock(pos0, EnumFacing.UP, (Boolean)this.packetPlace.getValue());
               this.setItem(anvil);
               EnumFacing facing = null;
               EnumFacing[] var7 = EnumFacing.values();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  EnumFacing f = var7[var9];
                  if (pos2.add(f.getDirectionVec()).equals(anvilPos)) {
                     facing = f;
                  }
               }

               BlockUtil.rightClickBlock(anvilPos, facing, (Boolean)this.packetPlace.getValue());
               this.restoreItem();
               this.disable();
            } else {
               this.setItem(anvil);
               BlockUtil.placeBlock(anvilPos, (Boolean)this.packetPlace.getValue());
               this.restoreItem();
               this.disable();
            }

         } else {
            this.sendMessage("Cannot find materials! disabling");
            this.disable();
         }
      }
   }

   public BlockPos findPos() {
      BlockPos playerPos = PlayerUtil.getPlayerPos();
      BlockPos lookingPos = playerPos.add(BlockUtil.getBackwardFacing(PlayerUtil.getLookingFacing()).getDirectionVec());
      List<BlockPos> possiblePlacePositions = new ArrayList();
      BlockPos[] array = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      BlockPos[] var6 = array;
      int var7 = array.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockPos offset = var6[var8];
         BlockPos pos = playerPos.add(offset);
         if (BlockUtil.getBlock(pos) != Blocks.AIR && BlockUtil.canRightClickForPlace(pos)) {
            BlockPos pos2 = pos.add(0, 1, 0);
            if (!this.entityCheck(pos2)) {
               BlockPos pos3 = pos2.add(0, 1, 0);
               if (!this.entityCheck(pos3)) {
                  BlockPos anvil = playerPos.add(0, 2, 0);
                  if (!this.entityCheck(anvil)) {
                     possiblePlacePositions.add(pos);
                  }
               }
            }
         }
      }

      return (BlockPos)possiblePlacePositions.stream().min(Comparator.comparing((b) -> {
         return lookingPos.getDistance(b.getX(), b.getY(), b.getZ());
      })).orElse((BlockPos) null);
   }

   public boolean entityCheck(BlockPos pos) {
      if (!(Boolean)this.entityCheck.getValue()) {
         return false;
      } else {
         Iterator var2 = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).iterator();

         Entity e;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            e = (Entity)var2.next();
         } while(!(e instanceof EntityEnderCrystal) && (Boolean)this.crystalOnly.getValue());

         return true;
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
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstantWither extends Module {
   public Setting<Integer> delay = this.register(new Setting("Delay", 2, 10, 0));
   private int tickDelay = 0;
   private int lastSlot = 0;

   public InstantWither() {
      super("InstantWither", Module.Categories.MISC, false, false);
   }

   public void onDisable() {
      this.tickDelay = 0;
      this.lastSlot = 0;
   }

   @SubscribeEvent
   public void onRightClickBlock(RightClickBlock event) {
      if (!this.nullCheck() && this.tickDelay <= 0) {
         if (mc.player.getHeldItem(event.getHand()).getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND)) {
            BlockPos base = event.getPos().offset(event.getFace());
            if (mc.world.getBlockState(base).getMaterial() != Material.AIR) {
               EnumFacing front = mc.player.getHorizontalFacing();
               EnumFacing left = front.rotateYCCW();
               int sandSlot = InventoryUtil.findHotbarBlock(Blocks.SOUL_SAND);
               int skullSlot = InventoryUtil.findHotbarItem(Item.getItemById(397));
               int[][] offset;
               int[][] var8;
               int var9;
               int var10;
               int[] pos;
               if (sandSlot != -1) {
                  offset = new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 0}, {-1, 1, 0}};
                  var8 = offset;
                  var9 = offset.length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     pos = var8[var10];
                     this.placeBlocks(base.up(pos[1]).offset(front, pos[2]).offset(left, pos[0]), sandSlot);
                  }
               }

               if (skullSlot != -1) {
                  offset = new int[][]{{1, 2, 0}, {0, 2, 0}, {-1, 2, 0}};
                  var8 = offset;
                  var9 = offset.length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     pos = var8[var10];
                     this.placeBlocks(base.up(pos[1]).offset(front, pos[2]).offset(left, pos[0]), skullSlot);
                  }
               }

            }
         }
      }
   }

   public void onTick() {
      if (this.tickDelay > 0) {
         --this.tickDelay;
      }

   }

   private void placeBlocks(BlockPos pos, int slot) {
      if (mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
         if (mc.player.inventory.currentItem != slot) {
            this.lastSlot = mc.player.inventory.currentItem;
            InventoryUtil.switchToHotbarSlot(slot, true);
         }

         BlockUtil.placeBlock(pos, false);
         InventoryUtil.switchToHotbarSlot(this.lastSlot, true);
         this.tickDelay = (Integer)this.delay.getValue();
      }

   }
}

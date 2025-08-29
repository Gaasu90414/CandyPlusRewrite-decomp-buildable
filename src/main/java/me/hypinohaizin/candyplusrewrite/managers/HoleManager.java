//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleManager extends Manager {
   private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(PlayerUtil.getOffsets(0, true));
   private final List<BlockPos> midSafety = new ArrayList();
   private List<BlockPos> holes = new ArrayList();

   public void update() {
      if (!this.nullCheck()) {
         this.holes = this.calcHoles();
      }

   }

   public List<BlockPos> getHoles() {
      return this.holes;
   }

   public List<BlockPos> getMidSafety() {
      return this.midSafety;
   }

   public List<BlockPos> getSortedHoles() {
      this.holes.sort(Comparator.comparingDouble((hole) -> {
         return mc.player.getDistanceSq(hole);
      }));
      return this.getHoles();
   }

   public List<BlockPos> calcHoles() {
      ArrayList<BlockPos> safeSpots = new ArrayList();
      this.midSafety.clear();
      List<BlockPos> positions = BlockUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), 6.0F, 6, false, true, 0);
      Iterator var3 = positions.iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     return safeSpots;
                  }

                  pos = (BlockPos)var3.next();
               } while(!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR));
            } while(!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR));
         } while(!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR));

         boolean isSafe = true;
         boolean midSafe = true;
         BlockPos[] var7 = surroundOffset;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockPos offset = var7[var9];
            Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
            if (BlockUtil.isBlockUnSolid(block)) {
               midSafe = false;
            }

            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
               isSafe = false;
            }
         }

         if (isSafe) {
            safeSpots.add(pos);
         }

         if (midSafe) {
            this.midSafety.add(pos);
         }
      }
   }

   public List<BlockPos> calcHoles(float range) {
      ArrayList<BlockPos> safeSpots = new ArrayList();
      this.midSafety.clear();
      List<BlockPos> positions = BlockUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), range, 6, false, true, 0);
      Iterator var4 = positions.iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return safeSpots;
                  }

                  pos = (BlockPos)var4.next();
               } while(!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR));
            } while(!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR));
         } while(!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR));

         boolean isSafe = true;
         boolean midSafe = true;
         BlockPos[] var8 = surroundOffset;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            BlockPos offset = var8[var10];
            Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
            if (BlockUtil.isBlockUnSolid(block)) {
               midSafe = false;
            }

            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
               isSafe = false;
            }
         }

         if (isSafe) {
            safeSpots.add(pos);
         }

         if (midSafe) {
            this.midSafety.add(pos);
         }
      }
   }

   public boolean isSafe(BlockPos pos) {
      boolean isSafe = true;
      BlockPos[] var3 = surroundOffset;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockPos offset = var3[var5];
         Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
         if (BlockUtil.isBlockUnSafe(block)) {
            isSafe = false;
            break;
         }
      }

      return isSafe;
   }

   public boolean inHole() {
      return mc.player != null && this.isSafe(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
   }

   public boolean inHole(EntityPlayer player) {
      return player != null && this.isSafe(new BlockPos(player.posX, player.posY, player.posZ));
   }
}

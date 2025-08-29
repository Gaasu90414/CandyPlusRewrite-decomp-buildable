//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleUtil implements Util {
   private static BlockPos[] surroundOffsets = BlockUtil.toBlockPos(PlayerUtil.getOffsets(0, true));

   public static boolean isObbyHole(BlockPos pos) {
      BlockPos[] var1 = surroundOffsets;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockPos offset = var1[var3];
         if (BlockUtil.getBlock(pos.add(offset)) != Blocks.OBSIDIAN) {
            return false;
         }
      }

      return true;
   }

   public static boolean isBedrockHole(BlockPos pos) {
      BlockPos[] var1 = surroundOffsets;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockPos offset = var1[var3];
         if (BlockUtil.getBlock(pos.add(offset)) != Blocks.BEDROCK) {
            return false;
         }
      }

      return true;
   }

   public static boolean isSafeHole(BlockPos pos) {
      BlockPos[] var1 = surroundOffsets;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockPos offset = var1[var3];
         Block block = BlockUtil.getBlock(pos.add(offset));
         if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) {
            return false;
         }
      }

      return true;
   }
}

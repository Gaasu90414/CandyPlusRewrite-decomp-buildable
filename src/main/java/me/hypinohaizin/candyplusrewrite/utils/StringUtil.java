//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import net.minecraft.util.math.BlockPos;

public class StringUtil implements Util {
   public static String getPositionString(BlockPos pos) {
      return "X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ();
   }

   public static String getName(String full) {
      StringBuilder r = new StringBuilder();
      boolean a = false;
      char[] var3 = full.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         if (!a) {
            r.append(String.valueOf(c).toUpperCase());
         } else {
            r.append(String.valueOf(c).toLowerCase());
         }

         a = true;
      }

      return r.toString();
   }
}

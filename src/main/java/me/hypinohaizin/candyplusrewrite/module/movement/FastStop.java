//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;

public class FastStop extends Module {
   public FastStop() {
      super("FastStop", Module.Categories.MOVEMENT, false, false);
   }

   public void onTick() {
      if (mc.player != null && mc.world != null) {
         if (mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F) {
            mc.player.motionX = 0.0D;
            mc.player.motionZ = 0.0D;
         }

      }
   }
}

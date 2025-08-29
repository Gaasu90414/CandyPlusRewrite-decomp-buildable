//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;

public class ReverseStep extends Module {
   public ReverseStep() {
      super("ReverseStep", Module.Categories.MOVEMENT, false, false);
   }

   public void onUpdate() {
      if (mc.player.onGround) {
         EntityPlayerSP player = mc.player;
         if (!EntityUtil.isInLiquid()) {
            --player.motionY;
         }
      }

   }
}

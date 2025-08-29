//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import java.util.Comparator;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.util.math.BlockPos;

public class HoleTP extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 1.0F, 3.0F, 0.1F));
   public Setting<Boolean> stopMotion = this.register(new Setting("StopMotion", false));

   public HoleTP() {
      super("HoleTP", Module.Categories.MOVEMENT, false, false);
   }

   public void onEnable() {
      BlockPos hole = (BlockPos)CandyPlusRewrite.m_hole.calcHoles().stream().min(Comparator.comparing((p) -> {
         return mc.player.getDistance((double)p.getX(), (double)p.getY(), (double)p.getZ());
      })).orElse((BlockPos) null);
      if (hole != null) {
         if (mc.player.getDistance((double)hole.getX(), (double)hole.getY(), (double)hole.getZ()) < (double)(Float)this.range.getValue() + 1.5D) {
            mc.player.setPosition((double)hole.getX() + 0.5D, mc.player.posY, (double)hole.getZ() + 0.5D);
            if ((Boolean)this.stopMotion.getValue()) {
               mc.player.motionX = 0.0D;
               mc.player.motionZ = 0.0D;
            }

            mc.player.motionY = -3.0D;
            this.sendMessage("Accepting teleport...");
         } else {
            this.sendMessage("Out of range! disabling...");
         }
      } else {
         this.sendMessage("Not found hole! disabling...");
      }

      this.disable();
   }
}

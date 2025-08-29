//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.MathUtil;
import net.minecraft.client.entity.EntityPlayerSP;

public class PhaseFly extends Module {
   public Setting<Float> speed = this.register(new Setting("Speed", 1.0F, 5.0F, 0.1F));
   public Setting<Float> vSpeed = this.register(new Setting("VerticalSpeed", 1.0F, 5.0F, 0.1F));
   public Setting<Boolean> onlyInBlock = this.register(new Setting("OnlyInBlock", false));

   public PhaseFly() {
      super("PhaseFly", Module.Categories.MOVEMENT, false, false);
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         EntityPlayerSP player = mc.player;
         boolean insideBlock = this.isInsideBlock(player);
         if ((Boolean)this.onlyInBlock.getValue() && !insideBlock) {
            player.noClip = false;
         } else {
            player.noClip = true;
            player.onGround = false;
            player.fallDistance = 0.0F;
            double[] mv = MathUtil.directionSpeed((double)(Float)this.speed.getValue());
            player.motionX = mv[0];
            player.motionZ = mv[1];
            double v = (double)(Float)this.vSpeed.getValue();
            if (player.movementInput.jump) {
               player.motionY = v;
            } else if (player.movementInput.sneak) {
               player.motionY = -v;
            } else {
               player.motionY = 0.0D;
            }

         }
      }
   }

   public void onDisable() {
      if (mc.player != null) {
         mc.player.noClip = false;
      }

   }

   private boolean isInsideBlock(EntityPlayerSP player) {
      return player.noClip || !player.world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty();
   }
}

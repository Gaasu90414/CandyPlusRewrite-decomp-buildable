//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.event.events.render.FlagGetEvent;
import me.hypinohaizin.candyplusrewrite.event.events.render.SwingAnimationEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animation extends Module {
   public static Animation INSTANCE;
   public Setting<Boolean> swingSpeed = this.register(new Setting("SwingSpeed", false));
   public Setting<Integer> speed = this.register(new Setting("Speed", 12, 6, 32, (v) -> {
      return (Boolean)this.swingSpeed.getValue();
   }));
   public Setting<Boolean> offhand = this.register(new Setting("Offhand", false));
   public Setting<Boolean> sneak = this.register(new Setting("Sneak", false));

   public Animation() {
      super("Animation", Module.Categories.RENDER, false, false);
      INSTANCE = this;
   }

   @SubscribeEvent
   public void onArmSwingAnim(SwingAnimationEvent event) {
      if ((Boolean)this.swingSpeed.getValue() && event.getEntity() == mc.player) {
         event.setSpeed((Integer)this.speed.getValue());
      }

   }

   public void onTick() {
      if ((Boolean)this.offhand.getValue() && mc.player != null && mc.player.isSwingInProgress) {
         mc.player.swingingHand = EnumHand.OFF_HAND;
      }

      if ((Boolean)this.swingSpeed.getValue()) {
         ((Integer)this.speed.getValue()).toString();
      }

   }

   @SubscribeEvent
   public void onFlagGet(FlagGetEvent event) {
      if (event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.player && event.getFlag() == 1 && (Boolean)this.sneak.getValue()) {
         event.setReturnValue(true);
      }

   }
}

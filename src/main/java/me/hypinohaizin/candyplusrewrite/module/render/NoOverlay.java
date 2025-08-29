//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;

public class NoOverlay extends Module {
   public NoOverlay() {
      super("NoOverlay", Module.Categories.RENDER, true, true);
   }

   public void onRender3D() {
      if (mc.player != null) {
         mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
         mc.player.removeActivePotionEffect(MobEffects.NAUSEA);
      }
   }

   public void onPacketReceive(PacketEvent.Receive event) {
      Packet<?> packet = event.packet;
      if (packet instanceof SPacketSpawnExperienceOrb || packet instanceof SPacketExplosion) {
         event.cancel();
      }

   }
}

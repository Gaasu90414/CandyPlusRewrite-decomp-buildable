//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;

public class AntiHunger extends Module {
   public AntiHunger() {
      super("AntiHunger", Module.Categories.MOVEMENT, false, false);
   }

   public void onEnable() {
      if (mc.player != null && mc.getConnection() != null) {
         mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SPRINTING));
      }

      super.onEnable();
   }

   public void onDisable() {
      if (mc.player != null && mc.getConnection() != null && mc.player.isSprinting()) {
         mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_SPRINTING));
      }

      super.onDisable();
   }

   public void onPacketSend(PacketEvent.Send event) {
      Packet<?> packet = event.getPacket();
      if (packet instanceof CPacketEntityAction) {
         CPacketEntityAction action = (CPacketEntityAction)packet;
         if (action.getAction() == Action.START_SPRINTING || action.getAction() == Action.STOP_SPRINTING) {
            event.cancel();
         }
      }

   }
}

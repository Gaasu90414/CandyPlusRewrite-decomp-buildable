package me.hypinohaizin.candyplusrewrite.event.events.network;

import me.hypinohaizin.candyplusrewrite.event.CandyEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PacketEvent extends CandyEvent {
   public final Packet<?> packet;

   public PacketEvent(Packet<?> packet) {
      this.packet = packet;
   }

   public Packet<?> getPacket() {
      return this.packet;
   }

   public class PlayerTravelEvent extends CandyEvent {
   }

   public static class Send extends PacketEvent {
      public Send(Packet<?> packet) {
         super(packet);
      }
   }

   public static class Receive extends PacketEvent {
      public Receive(Packet<?> packet) {
         super(packet);
      }
   }
}

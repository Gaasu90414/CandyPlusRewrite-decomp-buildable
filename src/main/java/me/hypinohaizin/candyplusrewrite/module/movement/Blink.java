//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;

public class Blink extends Module {
   public Setting<Boolean> noEntity = this.register(new Setting("NoEntity", false));
   public Setting<Boolean> limit = this.register(new Setting("Limit", false));
   public Setting<Integer> maxPackets = this.register(new Setting("MaxPackets", 20, 70, 10, (s) -> {
      return (Boolean)this.limit.getValue();
   }));
   public Setting<Boolean> all = this.register(new Setting("Cancel All", false));
   public Setting<Integer> skip = this.register(new Setting("Skip", 0, 3, 0));
   public EntityPlayer entity = null;
   public BlockPos startPos = null;
   public List<Packet<?>> packets = null;

   public Blink() {
      super("Blink", Module.Categories.MOVEMENT, false, false);
   }

   public void onEnable() {
      if (this.nullCheck()) {
         this.disable();
      } else {
         this.packets = new ArrayList();
         if (!(Boolean)this.noEntity.getValue()) {
            (this.entity = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile())).copyLocationAndAnglesFrom(mc.player);
            this.entity.rotationYaw = mc.player.rotationYaw;
            this.entity.rotationYawHead = mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(6942069, this.entity);
            this.startPos = mc.player.getPosition();
         }

      }
   }

   public void onDisable() {
      if (!this.nullCheck() && this.packets != null) {
         int counter = 0;

         for(Iterator var2 = this.packets.iterator(); var2.hasNext(); ++counter) {
            Packet packet = (Packet)var2.next();
            if ((Integer)this.skip.getValue() <= counter) {
               mc.player.connection.sendPacket(packet);
               counter = 0;
            }
         }

         mc.world.removeEntityFromWorld(this.entity.getEntityId());
      }
   }

   public void onUpdate() {
      if (!this.nullCheck() && this.packets != null) {
         if ((Boolean)this.limit.getValue() && this.packets.size() > (Integer)this.maxPackets.getValue()) {
            this.sendMessage("Packets size has reached the limit! disabling...");
            this.packets = new ArrayList();
            this.disable();
         }

      }
   }

   public void onPacketSend(PacketEvent.Send event) {
      if (!this.nullCheck() && this.packets != null) {
         Packet<?> packet = event.packet;
         if (!(Boolean)this.all.getValue()) {
            if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
               return;
            }

            this.packets.add(packet);
            event.cancel();
         } else if (packet instanceof CPacketPlayer) {
            this.packets.add(packet);
            event.cancel();
         }

      }
   }
}

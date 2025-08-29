//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
   Setting<Integer> horizontal_vel = this.register(new Setting("Horizontal", 0, 100, 0));
   Setting<Integer> vertical_vel = this.register(new Setting("Vertical", 0, 100, 0));
   Setting<Boolean> explosions = this.register(new Setting("Explosions", true));
   Setting<Boolean> bobbers = this.register(new Setting("Bobbers", true));

   public Velocity() {
      super("Velocity", Module.Categories.COMBAT, false, false);
   }

   public void onPacketReceive(PacketEvent.Receive event) {
      if (mc.player != null) {
         if (event.packet instanceof SPacketEntityStatus && (Boolean)this.bobbers.getValue()) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.packet;
            if (packet.getOpCode() == 31) {
               Entity entity = packet.getEntity(mc.world);
               if (entity != null && entity instanceof EntityFishHook) {
                  EntityFishHook fishHook = (EntityFishHook)entity;
                  if (fishHook.caughtEntity == mc.player) {
                     event.cancel();
                  }
               }
            }
         }

         if (event.packet instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet2 = (SPacketEntityVelocity)event.packet;
            if (packet2.getEntityID() == mc.player.getEntityId()) {
               if ((Integer)this.horizontal_vel.getValue() == 0 && (Integer)this.vertical_vel.getValue() == 0) {
                  event.cancel();
                  return;
               }

               if ((Integer)this.horizontal_vel.getValue() != 100) {
               }

               if ((Integer)this.vertical_vel.getValue() != 100) {
               }
            }
         }

         if (event.packet instanceof SPacketExplosion && (Boolean)this.explosions.getValue()) {
            SPacketExplosion packet3 = (SPacketExplosion)event.packet;
            if ((Integer)this.horizontal_vel.getValue() == 0 && (Integer)this.vertical_vel.getValue() == 0) {
               event.cancel();
               return;
            }

            if ((Integer)this.horizontal_vel.getValue() != 100) {
            }

            if ((Integer)this.vertical_vel.getValue() != 100) {
            }
         }

      }
   }
}

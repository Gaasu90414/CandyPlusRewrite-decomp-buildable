//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.mixin.mixins;

import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
   @Inject(
      method = {"handleEntityMetadata"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
      Minecraft mc = Minecraft.getMinecraft();
      Entity entity;
      EntityPlayer player;
      if (mc.world != null && (entity = mc.world.getEntityByID(packetIn.getEntityId())) instanceof EntityPlayer && (player = (EntityPlayer)entity).getHealth() <= 0.0F) {
         PlayerDeathEvent event = new PlayerDeathEvent(player);
         MinecraftForge.EVENT_BUS.post(event);
      }

   }
}

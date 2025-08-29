//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.Iterator;
import java.util.Map;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.mixin.mixins.IMixinMapData;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiMapBan extends Module {
   public AntiMapBan() {
      super("AntiMapBan", Module.Categories.MISC, false, false);
   }

   public void onTick() {
      if (!this.nullCheck()) {
         ItemStack currentItem = mc.player.inventory.getCurrentItem();
         if (!currentItem.isEmpty() && currentItem.getItem() instanceof ItemMap) {
            MapData mapData = ((ItemMap)currentItem.getItem()).getMapData(currentItem, mc.world);
            if (mapData != null) {
               this.getMapDecorations(mapData).clear();
            }
         }

         Iterator var7 = mc.world.loadedEntityList.iterator();

         while(var7.hasNext()) {
            Entity entity = (Entity)var7.next();
            if (entity instanceof EntityItemFrame) {
               EntityItemFrame frame = (EntityItemFrame)entity;
               ItemStack frameItem = frame.getDisplayedItem();
               if (!frameItem.isEmpty() && frameItem.getItem() instanceof ItemMap) {
                  MapData mapData = ((ItemMap)frameItem.getItem()).getMapData(frameItem, frame.world);
                  if (mapData != null) {
                     this.getMapDecorations(mapData).clear();
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketMaps) {
         event.cancel();
      }

   }

   public Map<String, MapDecoration> getMapDecorations(MapData mapData) {
      if (mapData instanceof IMixinMapData) {
         return ((IMixinMapData)mapData).getMapDecorations();
      } else {
         try {
            return mapData.mapDecorations;
         } catch (Throwable var3) {
            return null;
         }
      }
   }
}

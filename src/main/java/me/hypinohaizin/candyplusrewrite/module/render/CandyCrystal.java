//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.event.events.render.RenderEntityModelEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class CandyCrystal extends Module {
   public Setting<Boolean> chams = this.register(new Setting("Chams", false));
   public Setting<Boolean> throughWalls = this.register(new Setting("ThroughWalls", true));
   public Setting<Boolean> wireframeThroughWalls = this.register(new Setting("WireThroughWalls", true));
   public Setting<Boolean> glint = this.register(new Setting("Glint", false, (v) -> {
      return (Boolean)this.chams.getValue();
   }));
   public Setting<Boolean> wireframe = this.register(new Setting("Wireframe", false));
   public Setting<Float> scale = this.register(new Setting("Scale", 1.0F, 10.0F, 0.1F));
   public Setting<Float> lineWidth = this.register(new Setting("LineWidth", 1.0F, 3.0F, 0.1F));
   public Setting<Boolean> colorSync = this.register(new Setting("Sync", false));
   public Setting<Boolean> rainbow = this.register(new Setting("Rainbow", false));
   public Setting<Integer> saturation = this.register(new Setting("Saturation", 50, 100, 0, (v) -> {
      return (Boolean)this.rainbow.getValue();
   }));
   public Setting<Integer> brightness = this.register(new Setting("Brightness", 100, 100, 0, (v) -> {
      return (Boolean)this.rainbow.getValue();
   }));
   public Setting<Integer> speed = this.register(new Setting("Speed", 40, 100, 1, (v) -> {
      return (Boolean)this.rainbow.getValue();
   }));
   public Setting<Boolean> xqz = this.register(new Setting("XQZ", false, (v) -> {
      return !(Boolean)this.rainbow.getValue() && (Boolean)this.throughWalls.getValue();
   }));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 100), (v) -> {
      return !(Boolean)this.rainbow.getValue();
   }));
   public Setting<Color> hiddenColor = this.register(new Setting("Hidden Color", new Color(255, 255, 255, 100), (v) -> {
      return (Boolean)this.xqz.getValue();
   }));
   public Setting<Integer> alpha = this.register(new Setting("Alpha", 50, 255, 0, (v) -> {
      return !(Boolean)this.rainbow.getValue();
   }));
   public Map<EntityEnderCrystal, Float> scaleMap = new ConcurrentHashMap();
   public float hue;
   public Map<Integer, Integer> colorHeightMap = new HashMap();

   public CandyCrystal() {
      super("CandyCrystal", Module.Categories.RENDER, false, false);
   }

   public void onUpdate() {
      try {
         Iterator var1 = mc.world.loadedEntityList.iterator();

         while(var1.hasNext()) {
            Entity crystal = (Entity)var1.next();
            if (crystal instanceof EntityEnderCrystal) {
               if (!this.scaleMap.containsKey(crystal)) {
                  this.scaleMap.put((EntityEnderCrystal)crystal, 3.125E-4F);
               } else {
                  this.scaleMap.put((EntityEnderCrystal)crystal, (Float)this.scaleMap.get(crystal) + 3.125E-4F);
               }

               if (!((Float)this.scaleMap.get(crystal) < 0.0625F * (Float)this.scale.getValue())) {
                  this.scaleMap.remove(crystal);
               }
            }
         }
      } catch (Exception var3) {
      }

   }

   @SubscribeEvent
   public void onReceivePacket(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketDestroyEntities) {
         SPacketDestroyEntities packet = (SPacketDestroyEntities)event.getPacket();
         int[] var3 = packet.getEntityIDs();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int id = var3[var5];
            Entity entity = mc.world.getEntityByID(id);
            if (entity instanceof EntityEnderCrystal) {
               this.scaleMap.remove(entity);
            }
         }
      }

   }

   @SubscribeEvent
   public void onRenderModel(RenderEntityModelEvent event) {
      if (event.entity instanceof EntityEnderCrystal && (Boolean)this.wireframe.getValue()) {
         Color color = (Boolean)this.colorSync.getValue() ? this.getCurrentColor() : (Color)this.color.getValue();
         boolean fancyGraphics = mc.gameSettings.fancyGraphics;
         mc.gameSettings.fancyGraphics = false;
         float gamma = mc.gameSettings.gammaSetting;
         mc.gameSettings.gammaSetting = 10000.0F;
         GL11.glPushMatrix();
         GL11.glPushAttrib(1048575);
         GL11.glPolygonMode(1032, 6913);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         if ((Boolean)this.wireframeThroughWalls.getValue()) {
            GL11.glDisable(2929);
         }

         GL11.glEnable(2848);
         GL11.glEnable(3042);
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
         GlStateManager.glLineWidth((Float)this.lineWidth.getValue());
         event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
         GL11.glPopAttrib();
         GL11.glPopMatrix();
      }
   }

   public void onTick() {
      int colorSpeed = 101 - (Integer)this.speed.getValue();
      float hue = (float)(System.currentTimeMillis() % (long)(360 * colorSpeed)) / (360.0F * (float)colorSpeed);
      this.hue = hue;
      float tempHue = hue;

      for(int i = 0; i <= 510; ++i) {
         this.colorHeightMap.put(i, Color.HSBtoRGB(tempHue, (float)(Integer)this.saturation.getValue() / 255.0F, (float)(Integer)this.brightness.getValue() / 255.0F));
         tempHue += 0.0013071896F;
      }

   }

   public Color getCurrentColor() {
      return Color.getHSBColor(this.hue, (float)(Integer)this.saturation.getValue() / 255.0F, (float)(Integer)this.brightness.getValue() / 255.0F);
   }
}

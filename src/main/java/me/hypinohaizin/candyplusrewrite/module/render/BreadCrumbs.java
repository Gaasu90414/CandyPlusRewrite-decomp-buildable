//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.event.events.player.UpdateWalkingPlayerEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BreadCrumbs extends Module {
   public Setting<Color> color = this.register(new Setting("Color", new Color(130, 10, 220, 200)));
   public Setting<Float> fadeSpeed = this.register(new Setting("Fadeout Speed", 10.0F, 20.0F, 1.0F));
   public Setting<Float> thickness = this.register(new Setting("Thickness", 3.0F, 10.0F, 1.0F));
   public Setting<Float> offset = this.register(new Setting("OffsetY", 5.0F, 10.0F, 0.0F));
   public Setting<Boolean> other = this.register(new Setting("Other", false));
   public List<BreadCrumbs.Trace> traces = new ArrayList();

   public BreadCrumbs() {
      super("BreadCrumbs", Module.Categories.RENDER, false, false);
   }

   public void onRender3D(float ticks) {
      try {
         this.doRender(ticks);
      } catch (Exception var3) {
      }

   }

   @SubscribeEvent
   public void onUpdateWalkingEvent(UpdateWalkingPlayerEvent event) {
      this.traces.add(new BreadCrumbs.Trace(mc.player.posX, mc.player.posY + ((double)(Float)this.offset.getValue() - 5.0D), mc.player.posZ, (Color)this.color.getValue()));
   }

   public void doRender(float ticks) {
      if (!this.traces.isEmpty()) {
         GlStateManager.pushMatrix();
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.disableDepth();
         float posx = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)ticks);
         float posy = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)ticks);
         float posz = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)ticks);
         GlStateManager.translate(posx * -1.0F, posy * -1.0F, posz * -1.0F);
         Tessellator tessellator = new Tessellator(2097152);
         BufferBuilder worldRenderer = tessellator.getBuffer();
         float thickness = (Float)this.thickness.getValue();
         GL11.glEnable(2848);
         GL11.glLineWidth(thickness);

         try {
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            Iterator iterator = this.traces.iterator();

            while(iterator.hasNext()) {
               BreadCrumbs.Trace trace = (BreadCrumbs.Trace)iterator.next();
               int r = trace.r;
               int g = trace.g;
               int b = trace.b;
               int a = trace.a;
               worldRenderer.pos(trace.x, trace.y, trace.z).color(r, g, b, a).endVertex();
               trace.includeAlpha((Float)this.fadeSpeed.getValue());
            }

            tessellator.draw();
         } catch (IllegalStateException var15) {
            try {
               worldRenderer.finishDrawing();
            } catch (Exception var14) {
            }
         }

         GL11.glLineWidth(1.0F);
         GL11.glDisable(2848);
         GlStateManager.enableDepth();
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GlStateManager.popMatrix();
         this.traces.removeIf((t) -> {
            return t.a <= 0;
         });
      }
   }

   public class Trace {
      public double x;
      public double y;
      public double z;
      public int r;
      public int g;
      public int b;
      public int a;

      public Trace(double x, double y, double z, Color color) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.r = color.getRed();
         this.g = color.getGreen();
         this.b = color.getBlue();
         this.a = color.getAlpha();
      }

      public void includeAlpha(Float speed) {
         this.a -= speed.intValue();
         if (this.a < 0) {
            this.a = 0;
         }

      }
   }
}

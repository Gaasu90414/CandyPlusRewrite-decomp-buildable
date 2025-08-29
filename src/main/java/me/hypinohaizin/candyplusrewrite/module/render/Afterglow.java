//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.event.events.player.UpdateWalkingPlayerEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Afterglow extends Module {
   public Setting<Float> delay = this.register(new Setting("Delay", 10.0F, 20.0F, 1.0F));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 200)));
   public Setting<Float> fadeSpeed = this.register(new Setting("Fadeout Speed", 10.0F, 20.0F, 1.0F));
   public Setting<Float> thickness = this.register(new Setting("Thickness", 3.0F, 10.0F, 1.0F));
   public static RenderPlayer render = null;
   public List<Afterglow.AfterGlow> glows = new ArrayList();
   public Timer timer = new Timer();

   public Afterglow() {
      super("Afterglow", Module.Categories.RENDER, false, false);
   }

   @SubscribeEvent
   public void onUpdateWalkingEvent(UpdateWalkingPlayerEvent event) {
      if (this.timer == null) {
         this.timer = new Timer();
      }

      if (this.timer.passedDms((double)(Float)this.delay.getValue())) {
         if (mc.player.motionX == 0.0D && mc.player.motionZ == 0.0D) {
            return;
         }

         double[] forward = PlayerUtil.forward(-0.5D);
         this.glows.add(new Afterglow.AfterGlow(forward[0] + mc.player.posX, mc.player.posY, forward[1] + mc.player.posZ, mc.player.rotationYaw, new Color(((Color)this.color.getValue()).getRed(), ((Color)this.color.getValue()).getGreen(), ((Color)this.color.getValue()).getBlue(), 150)));
      }

   }

   public void onRender3D(float ticks) {
      if (render == null) {
         render = new RenderPlayer(mc.getRenderManager());
      }

      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.enableDepth();
      float posx = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)ticks);
      float posy = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)ticks);
      float posz = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)ticks);
      GlStateManager.translate(posx * -1.0F, posy * -1.0F, posz * -1.0F);
      Iterator var5 = this.glows.iterator();

      while(var5.hasNext()) {
         Afterglow.AfterGlow glow = (Afterglow.AfterGlow)var5.next();
         GL11.glPushMatrix();
         GL11.glDepthRange(0.0D, 0.01D);
         GL11.glDisable(2896);
         GL11.glDisable(3553);
         GL11.glPolygonMode(1032, 6913);
         GL11.glEnable(3008);
         GL11.glEnable(3042);
         GL11.glLineWidth((Float)this.thickness.getValue());
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
         GL11.glColor4f((float)glow.r / 255.0F, (float)glow.g / 255.0F, (float)glow.b / 255.0F, (float)glow.a / 255.0F);
         mc.getRenderManager().renderEntityStatic(mc.player, 0.0F, false);
         GL11.glHint(3154, 4352);
         GL11.glPolygonMode(1032, 6914);
         GL11.glEnable(2896);
         GL11.glDepthRange(0.0D, 1.0D);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
         glow.includeAlpha((Float)this.fadeSpeed.getValue());
      }

      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
      this.glows.removeIf((g) -> {
         return g.a >= 255;
      });
   }

   public class AfterGlow {
      public double x;
      public double y;
      public double z;
      public float yaw;
      public int r;
      public int g;
      public int b;
      public int a;

      public AfterGlow(double x, double y, double z, float yaw, Color color) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.yaw = yaw;
         this.r = color.getRed();
         this.g = color.getGreen();
         this.b = color.getBlue();
         this.a = color.getAlpha();
      }

      public void includeAlpha(Float speed) {
         this.a += speed.intValue();
         if (this.a > 255) {
            this.a = 255;
         }

      }
   }
}

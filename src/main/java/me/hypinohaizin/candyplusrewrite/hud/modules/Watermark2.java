//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.CandyDynamicTexture;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

public class Watermark2 extends Hud {
   public Setting<Float> scale = this.register(new Setting("Scale", 0.6F, 1.0F, 0.1F));
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
   public CandyDynamicTexture watermark;

   public Watermark2() {
      super("Watermark2", 10.0F, 10.0F);
      this.loadLogo();
   }

   public void onRender() {
      if (this.watermark == null) {
         this.loadLogo();
      } else {
         float width = (float)this.watermark.GetWidth() * (Float)this.scale.getValue();
         float height = (float)this.watermark.GetHeight() * (Float)this.scale.getValue();
         GlStateManager.enableTexture2D();
         GlStateManager.disableAlpha();
         RenderHelper.enableGUIStandardItemLighting();
         mc.renderEngine.bindTexture(this.watermark.GetResourceLocation());
         GlStateManager.glTexParameteri(3553, 10240, 9729);
         GlStateManager.glTexParameteri(3553, 10241, 9729);
         if ((Boolean)this.rainbow.getValue()) {
            Color color = new Color(RenderUtil.getRainbow((Integer)this.speed.getValue() * 100, 0, (float)(Integer)this.saturation.getValue() / 100.0F, (float)(Integer)this.brightness.getValue() / 100.0F));
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
         } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }

         GlStateManager.pushMatrix();
         RenderUtil.drawTexture((Float)this.x.getValue(), (Float)this.y.getValue(), width, height);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.popMatrix();
         this.width = width;
         this.height = height;
      }
   }

   public void loadLogo() {
      try {
         InputStream stream = Watermark2.class.getResourceAsStream("/assets/candyplusrewrite/textures/watermark2.png");
         Throwable var2 = null;

         try {
            BufferedImage image = ImageIO.read(stream);
            int height = image.getHeight();
            int width = image.getWidth();
            this.watermark = new CandyDynamicTexture(image, height, width);
            this.watermark.SetResourceLocation(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("candyplusrewrite/textures", this.watermark));
         } catch (Throwable var14) {
            var2 = var14;
            throw var14;
         } finally {
            if (stream != null) {
               if (var2 != null) {
                  try {
                     stream.close();
                  } catch (Throwable var13) {
                     var2.addSuppressed(var13);
                  }
               } else {
                  stream.close();
               }
            }

         }
      } catch (IOException var16) {
         var16.printStackTrace();
      }

   }
}

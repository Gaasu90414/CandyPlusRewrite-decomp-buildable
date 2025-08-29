//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.util.math.MathHelper;

public class Hud extends Module {
   public Setting<Float> x;
   public Setting<Float> y;
   public float width;
   public float height = 0.0F;
   public boolean selecting = false;
   private float diffX;
   private float diffY = 0.0F;
   public float scaledWidth;
   public float scaledHeight;
   public float scaleFactor = 0.0F;

   public Hud(String name, float x, float y) {
      super(name, Module.Categories.HUB, false, false);
      this.x = this.register(new Setting("X", x, 2000.0F, 0.0F));
      this.y = this.register(new Setting("Y", x, 2000.0F, 0.0F));
   }

   public void onRender2D() {
      if (CandyPlusRewrite.m_module.getModuleWithClass(HUDEditor.class).isEnable) {
         Color color = this.selecting ? new Color(20, 20, 20, 110) : new Color(20, 20, 20, 80);
         RenderUtil.drawRect((Float)this.x.getValue() - 10.0F, (Float)this.y.getValue() - 5.0F, this.width + 20.0F, this.height + 10.0F, ColorUtil.toRGBA(color));
      }

      this.onRender();
   }

   public void onRender() {
   }

   public void onUpdate() {
      this.updateResolution();
      this.x.maxValue = this.scaledWidth;
      this.y.maxValue = this.scaledHeight;
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isMouseHovering(mouseX, mouseY)) {
         this.diffX = (Float)this.x.getValue() - (float)mouseX;
         this.diffY = (Float)this.y.getValue() - (float)mouseY;
         this.selecting = true;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.selecting = false;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      if (this.selecting && clickedMouseButton == 0) {
         this.x.setValue((float)mouseX + this.diffX);
         this.y.setValue((float)mouseY + this.diffY);
      }

   }

   public Boolean isMouseHovering(int mouseX, int mouseY) {
      return (Float)this.x.getValue() - 10.0F < (float)mouseX && (Float)this.x.getValue() + this.width + 10.0F > (float)mouseX && (Float)this.y.getValue() - 10.0F < (float)mouseY && (Float)this.y.getValue() + this.height + 10.0F > (float)mouseY;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public void updateResolution() {
      this.scaledWidth = (float)mc.displayWidth;
      this.scaledHeight = (float)mc.displayHeight;
      this.scaleFactor = 1.0F;
      boolean flag = mc.isUnicode();
      int i = mc.gameSettings.guiScale;
      if (i == 0) {
         i = 1000;
      }

      while(this.scaleFactor < (float)i && this.scaledWidth / (this.scaleFactor + 1.0F) >= 320.0F && this.scaledHeight / (this.scaleFactor + 1.0F) >= 240.0F) {
         ++this.scaleFactor;
      }

      if (flag && this.scaleFactor % 2.0F != 0.0F && this.scaleFactor != 1.0F) {
         --this.scaleFactor;
      }

      double scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
      double scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
      this.scaledWidth = (float)MathHelper.ceil(scaledWidthD);
      this.scaledHeight = (float)MathHelper.ceil(scaledHeightD);
   }
}

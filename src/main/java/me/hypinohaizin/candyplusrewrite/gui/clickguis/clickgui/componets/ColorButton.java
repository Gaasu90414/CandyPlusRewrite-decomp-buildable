package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Panel;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class ColorButton extends Component {
   private final Module module;
   private final Setting<Color> setting;
   private boolean isOpening = false;
   private boolean redChanging;
   private boolean greenChanging;
   private boolean blueChanging;
   private boolean alphaChanging = false;
   private float rLx;
   private float gLx;
   private float bLx;
   private float aLx = 0.0F;

   public ColorButton(Module module, Setting<Color> setting, int x, int width, int height) {
      this.module = module;
      this.setting = setting;
      this.x = x;
      this.width = width;
      this.height = height;
      this.rLx = (float)((Color)setting.getValue()).getRed() / 255.0F;
      this.gLx = (float)((Color)setting.getValue()).getGreen() / 255.0F;
      this.bLx = (float)((Color)setting.getValue()).getBlue() / 255.0F;
      this.aLx = (float)((Color)setting.getValue()).getAlpha() / 255.0F;
   }

   public void onRender2D(int y) {
      this.visible = this.setting.visible();
      if (this.visible) {
         this.y = y;
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.enabledColor);
         float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
         float rcy1 = (float)y + (float)(this.height - 11) / 2.0F;
         float rcy2 = (float)y + (float)(this.height - 9) / 2.0F;
         RenderUtil.drawRect((float)(this.x + this.width - 21), rcy1, 12.0F, 12.0F, this.outlineColor);
         RenderUtil.drawRect((float)(this.x + this.width - 20), rcy2, 10.0F, 10.0F, ColorUtil.toRGBA(new Color(((Color)this.setting.getValue()).getRed(), ((Color)this.setting.getValue()).getGreen(), ((Color)this.setting.getValue()).getBlue(), ((Color)this.setting.getValue()).getAlpha())));
         RenderUtil.drawString(this.setting.name, (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
         if (this.isOpening) {
            float colorFieldHeight = 93.0F;
            Panel.Cy += 93;
            float colorFieldY = (float)(y + this.height);
            float colorFieldHeightY = colorFieldY + 93.0F;
            RenderUtil.drawRect((float)this.x, colorFieldY, (float)this.width, 93.0F, this.defaultColor);
            int gray = ColorUtil.toRGBA(50, 50, 50);
            RenderUtil.drawString("Red", (float)(this.x + 5), colorFieldY + 5.0F, this.whiteColor, false, 1.0F);
            RenderUtil.drawRect((float)(this.x + 10), colorFieldY + 15.0F, (float)(this.width - 20), 5.0F, gray);
            RenderUtil.drawRect((float)(this.x + 10) + (float)(this.width - 20) * this.rLx, colorFieldY + 13.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
            RenderUtil.drawString("Green", (float)(this.x + 5), colorFieldY + 27.0F, this.whiteColor, false, 1.0F);
            RenderUtil.drawRect((float)(this.x + 10), colorFieldY + 37.0F, (float)(this.width - 20), 5.0F, gray);
            RenderUtil.drawRect((float)(this.x + 10) + (float)(this.width - 20) * this.gLx, colorFieldY + 35.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
            RenderUtil.drawString("Blue", (float)(this.x + 5), colorFieldY + 49.0F, this.whiteColor, false, 1.0F);
            RenderUtil.drawRect((float)(this.x + 10), colorFieldY + 59.0F, (float)(this.width - 20), 5.0F, gray);
            RenderUtil.drawRect((float)(this.x + 10) + (float)(this.width - 20) * this.bLx, colorFieldY + 57.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
            RenderUtil.drawString("Alpha", (float)(this.x + 5), colorFieldY + 71.0F, this.whiteColor, false, 1.0F);
            RenderUtil.drawRect((float)(this.x + 10), colorFieldY + 81.0F, (float)(this.width - 20), 5.0F, gray);
            RenderUtil.drawRect((float)(this.x + 10) + (float)(this.width - 20) * this.aLx, colorFieldY + 79.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
            RenderUtil.drawLine((float)this.x, (float)y, (float)(this.x + this.width), (float)y, 2.0F, this.outlineColor);
            RenderUtil.drawLine((float)this.x, colorFieldY, (float)(this.x + this.width), colorFieldY, 2.0F, this.outlineColor);
            RenderUtil.drawLine((float)this.x, (float)y, (float)this.x, colorFieldHeightY, 2.0F, this.outlineColor);
            RenderUtil.drawLine((float)(this.x + this.width), (float)y, (float)(this.x + this.width), colorFieldHeightY, 2.0F, this.outlineColor);
         } else {
            this.drawOutLine();
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      float colorFieldHeight = 70.0F;
      float colorFieldY = (float)(this.y + this.height);
      float colorFieldHeightY = colorFieldY + 70.0F;
      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 1) {
         this.isOpening = !this.isOpening;
      }

      if (this.isOpening) {
         if (this.isMouseHovering(mouseX, mouseY, this.x + 8, (int)(colorFieldY + 7.0F), this.width - 12, 15)) {
            this.redChanging = true;
         }

         if (this.isMouseHovering(mouseX, mouseY, this.x + 8, (int)(colorFieldY + 29.0F), this.width - 12, 15)) {
            this.greenChanging = true;
         }

         if (this.isMouseHovering(mouseX, mouseY, this.x + 8, (int)(colorFieldY + 51.0F), this.width - 12, 15)) {
            this.blueChanging = true;
         }

         if (this.isMouseHovering(mouseX, mouseY, this.x + 8, (int)(colorFieldY + 73.0F), this.width - 12, 15)) {
            this.alphaChanging = true;
         }

      }
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.redChanging = false;
      this.greenChanging = false;
      this.blueChanging = false;
      this.alphaChanging = false;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      if (this.isOpening) {
         float colorFieldHeight = 70.0F;
         float colorFieldY = (float)(this.y + this.height);
         float colorFieldHeightY = colorFieldY + 70.0F;
         if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.redChanging) {
            this.rLx = ((float)mouseX - (float)(this.x + 10)) / ((float)this.width - 20.0F);
            if (this.rLx > 1.0F) {
               this.rLx = 1.0F;
            }

            if (this.rLx < 0.0F) {
               this.rLx = 0.0F;
            }

            this.setting.setValue(new Color((int)((float)Math.round(255.0F * this.rLx * 10.0F) / 10.0F), ((Color)this.setting.getValue()).getGreen(), ((Color)this.setting.getValue()).getBlue(), ((Color)this.setting.getValue()).getAlpha()));
         }

         if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.greenChanging) {
            this.gLx = ((float)mouseX - (float)(this.x + 10)) / ((float)this.width - 20.0F);
            if (this.gLx > 1.0F) {
               this.gLx = 1.0F;
            }

            if (this.gLx < 0.0F) {
               this.gLx = 0.0F;
            }

            this.setting.setValue(new Color(((Color)this.setting.getValue()).getRed(), (int)((float)Math.round(255.0F * this.gLx * 10.0F) / 10.0F), ((Color)this.setting.getValue()).getBlue(), ((Color)this.setting.getValue()).getAlpha()));
         }

         if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.blueChanging) {
            this.bLx = ((float)mouseX - (float)(this.x + 10)) / ((float)this.width - 20.0F);
            if (this.bLx > 1.0F) {
               this.bLx = 1.0F;
            }

            if (this.bLx < 0.0F) {
               this.bLx = 0.0F;
            }

            this.setting.setValue(new Color(((Color)this.setting.getValue()).getRed(), ((Color)this.setting.getValue()).getGreen(), (int)((float)Math.round(255.0F * this.bLx * 10.0F) / 10.0F), ((Color)this.setting.getValue()).getAlpha()));
         }

         if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.alphaChanging) {
            this.aLx = ((float)mouseX - (float)(this.x + 10)) / ((float)this.width - 20.0F);
            if (this.aLx > 1.0F) {
               this.aLx = 1.0F;
            }

            if (this.aLx < 0.0F) {
               this.aLx = 0.0F;
            }

            this.setting.setValue(new Color(((Color)this.setting.getValue()).getRed(), ((Color)this.setting.getValue()).getGreen(), ((Color)this.setting.getValue()).getBlue(), (int)((float)Math.round(255.0F * this.aLx * 10.0F) / 10.0F)));
         }

      }
   }
}

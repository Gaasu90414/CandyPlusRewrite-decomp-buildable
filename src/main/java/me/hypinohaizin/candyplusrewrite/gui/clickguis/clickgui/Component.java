package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui;

import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.font.CFontRenderer;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import me.hypinohaizin.candyplusrewrite.utils.Util;

public class Component implements Util {
   public int x;
   public int y;
   public int width;
   public int height;
   public int defaultColor = ColorUtil.toRGBA(25, 25, 25, 255);
   public int enabledColor = ColorUtil.toRGBA(230, 90, 100, 255);
   public int whiteColor = ColorUtil.toRGBA(255, 255, 255, 255);
   public int buttonColor = ColorUtil.toRGBA(35, 35, 35, 255);
   public int outlineColor = ColorUtil.toRGBA(210, 70, 80, 255);
   public CFontRenderer fontRenderer;
   public boolean visible;

   public Component() {
      this.fontRenderer = CandyPlusRewrite.m_font.fontRenderer;
   }

   public void onRender2D(int y) {
   }

   public Boolean isMouseHovering(int mouseX, int mouseY) {
      return this.x < mouseX && this.x + this.width > mouseX && this.y < mouseY && this.y + this.height > mouseY;
   }

   public Boolean isMouseHovering(int mouseX, int mouseY, int cx, int cy, int cw, int ch) {
      return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
   }

   public void onKeyTyped(char typedChar, int keyCode) {
   }

   public void drawOutLine() {
      RenderUtil.drawLine((float)this.x, (float)this.y, (float)(this.x + this.width), (float)this.y, 2.0F, this.outlineColor);
      RenderUtil.drawLine((float)this.x, (float)(this.y + this.height), (float)(this.x + this.width), (float)(this.y + this.height), 2.0F, this.outlineColor);
      RenderUtil.drawLine((float)this.x, (float)this.y, (float)this.x, (float)(this.y + this.height), 2.0F, this.outlineColor);
      RenderUtil.drawLine((float)(this.x + this.width), (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), 2.0F, this.outlineColor);
   }
}

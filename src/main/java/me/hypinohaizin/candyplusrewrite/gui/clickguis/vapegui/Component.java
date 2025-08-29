package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.render.ClickGUI;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;

public class Component {
   public float x;
   public float y;
   public float width;
   public float height;
   public int white = ColorUtil.toRGBA(255, 255, 255);
   public int gray = ColorUtil.toRGBA(200, 200, 200);
   public int panelColor = ColorUtil.toRGBA(19, 19, 19);
   public int baseColor = ColorUtil.toRGBA(25, 25, 25);
   public int mainColor;

   public Component() {
      this.updateColor();
   }

   public void onRender(int mouseX, int mouseY) {
      this.updateColor();
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
   }

   public void onMouseReleased(int mouseX, int mouseY, int state) {
   }

   public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
   }

   public float doRender(float y, int mouseX, int mouseY) {
      return 0.0F;
   }

   public float getCenter(float a, float b, float c) {
      return a + (b - c) / 2.0F;
   }

   public Boolean isMouseHovering(int mouseX, int mouseY) {
      return this.x < (float)mouseX && this.x + this.width > (float)mouseX ? this.y < (float)mouseY && this.y + this.height > (float)mouseY : false;
   }

   public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
      return cx < mouseX && cx + cw > mouseX ? cy < mouseY && cy + ch > mouseY : false;
   }

   public void updateColor() {
      this.mainColor = ColorUtil.toRGBA((Color)((ClickGUI)CandyPlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue());
   }
}

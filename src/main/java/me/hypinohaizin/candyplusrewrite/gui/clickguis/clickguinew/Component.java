package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.ClickGUI;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;

public class Component {
   public Module module;
   public float x;
   public float y;
   public float width;
   public float height;
   public Color color;
   public int color0;
   public int hovering;

   public Component() {
      this.color = (Color)((ClickGUI)CandyPlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue();
      this.color0 = ColorUtil.toRGBA(30, 35, 30);
      this.hovering = ColorUtil.toRGBA(170, 170, 170, 100);
   }

   public float doRender(int mouseX, int mouseY, float x, float y) {
      return 0.0F;
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
   }

   public void onMouseReleased(int mouseX, int mouseY, int state) {
   }

   public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
   }

   public void onKeyTyped(char typedChar, int keyCode) {
   }

   public float getCenter(float a, float b, float c) {
      return a + (b - c) / 2.0F;
   }

   public Boolean isMouseHovering(int mouseX, int mouseY) {
      return this.x < (float)mouseX && this.x + this.width > (float)mouseX && this.y < (float)mouseY && this.y + this.height > (float)mouseY;
   }

   public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
      return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
   }
}

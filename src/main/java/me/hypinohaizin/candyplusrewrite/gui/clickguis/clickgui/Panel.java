package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets.ModuleButton;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import me.hypinohaizin.candyplusrewrite.utils.Util;

public class Panel implements Util {
   public String name;
   public int x;
   public int y;
   public int tmpy = 0;
   public int width;
   public int height;
   public boolean opening;
   private boolean isMoving;
   private int diff_x;
   private int diff_y;
   public static int Cy = 0;
   public List<ModuleButton> buttons;

   public Panel(Module.Categories category, int x, int y, boolean isOpening) {
      this.name = category.name();
      this.x = x;
      this.y = y;
      this.width = 95;
      this.height = 17;
      this.opening = isOpening;
      this.isMoving = false;
      this.diff_x = 0;
      this.diff_y = 0;
      this.buttons = new ArrayList();
      List<Module> modules = new ArrayList(CandyPlusRewrite.m_module.getModulesWithCategories(category));
      Collections.sort(modules, (c1, c2) -> {
         return c1.name.compareToIgnoreCase(c2.name);
      });
      Iterator var7 = modules.iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         if (!m.hide) {
            this.buttons.add(new ModuleButton(m, x, this.width, 15));
         }
      }

   }

   public void onRender(int mouseX, int mouseY, float partialTicks) {
      RenderUtil.drawRect((float)this.x, (float)this.y, (float)this.width, (float)this.height, ColorUtil.toARGB(50, 50, 50, 255));
      float width = RenderUtil.getStringWidth(this.name, 1.0F);
      float centeredX = (float)this.x + 0.0F;
      float centeredY = (float)this.y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
      RenderUtil.drawString(this.name, centeredX, centeredY, ColorUtil.toARGB(255, 255, 255, 255), false, 1.0F);
      Cy = this.y + 2;
      if (this.opening) {
         Cy = this.y + 2;
         this.buttons.forEach((b) -> {
            b.onRender(Cy += 15);
         });
      }

      int outlineColor = ColorUtil.toRGBA(210, 70, 80, 255);
      float y1 = (float)(Cy - 2 + this.height);
      RenderUtil.drawLine((float)this.x, (float)this.y, (float)this.x, y1, 2.0F, outlineColor);
      RenderUtil.drawLine((float)this.x + width, (float)this.y, (float)this.x + width, y1, 2.0F, outlineColor);
      RenderUtil.drawLine((float)this.x, (float)this.y, (float)this.x + width, (float)this.y, 2.0F, outlineColor);
      RenderUtil.drawLine((float)this.x, y1, (float)this.x + width, y1, 2.0F, outlineColor);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      this.buttons.forEach((b) -> {
         b.mouseClicked(mouseX, mouseY, mouseButton);
      });
      if (this.x < mouseX && this.x + this.width > mouseX && mouseButton == 0 && !CandyGUI.isHovering && this.y < mouseY && this.y + this.height > mouseY) {
         this.isMoving = true;
         CandyGUI.isHovering = true;
         this.diff_x = this.x - mouseX;
         this.diff_y = this.y - mouseY;
      }

      if (this.x < mouseX && this.x + this.width > mouseX && mouseButton == 1 && this.y < mouseY && this.y + this.height > mouseY) {
         this.opening = !this.opening;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.buttons.forEach((b) -> {
         b.mouseReleased(mouseX, mouseY, state);
      });
      this.isMoving = false;
      CandyGUI.isHovering = false;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      this.buttons.forEach((b) -> {
         b.mouseClickMove(mouseX, mouseY, clickedMouseButton);
      });
      if (this.isMoving) {
         this.x = mouseX + this.diff_x;
         this.y = mouseY + this.diff_y;
         this.buttons.forEach((b) -> {
            b.changeX(this.x);
         });
      }

   }

   public void keyTyped(char typedChar, int keyCode) {
      this.buttons.forEach((b) -> {
         b.onKeyTyped(typedChar, keyCode);
      });
   }
}

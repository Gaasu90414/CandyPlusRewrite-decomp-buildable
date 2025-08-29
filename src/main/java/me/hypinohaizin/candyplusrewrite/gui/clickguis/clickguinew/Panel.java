package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.item.ModuleButton;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.ClickGUI;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import me.hypinohaizin.candyplusrewrite.utils.StringUtil;

public class Panel {
   public float x;
   public float y;
   public float width;
   public float height;
   public boolean open;
   public Color color;
   public Module.Categories category;
   public List<ModuleButton> modules = new ArrayList();
   public boolean moving = false;
   public float diffx;
   public float diffy = 0.0F;

   public Panel(float x, float y, Module.Categories category) {
      this.x = x;
      this.y = y;
      this.width = 100.0F;
      this.height = 15.0F;
      this.open = true;
      this.color = (Color)((ClickGUI)CandyPlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue();
      this.category = category;
      List<Module> modules = new ArrayList(CandyPlusRewrite.m_module.getModulesWithCategories(category));
      modules.sort((c1, c2) -> {
         return c1.name.compareToIgnoreCase(c2.name);
      });
      modules.forEach((m) -> {
         this.modules.add(new ModuleButton(m, x));
      });
   }

   public void onRender(int mouseX, int mouseY) {
      AtomicReference<Float> _y = new AtomicReference(this.y + 15.0F);
      if (this.open) {
         this.modules.forEach((m) -> {
            Float var10000 = (Float)_y.updateAndGet((v) -> {
               return v + m.onRender(mouseX, mouseY, this.x, (Float)_y.get());
            });
         });
      }

      String name = StringUtil.getName(this.category.name());
      RenderUtil.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.toRGBA(30, 30, 30));
      RenderUtil.drawLine(this.x, this.y + 15.0F, this.x + 100.0F, this.y + 15.0F, 2.0F, ColorUtil.toRGBA(this.color));
      float namex = this.getCenter(this.x, 100.0F, RenderUtil.getStringWidth(name, 1.0F));
      float namey = this.getCenter(this.y, 15.0F, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString(name, namex, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
      ClickGUI module = (ClickGUI)CandyPlusRewrite.m_module.getModuleWithClass(ClickGUI.class);
      if (module != null) {
         if ((Boolean)module.outline.getValue()) {
            RenderUtil.drawLine(this.x, this.y, this.x + 100.0F, this.y, 1.0F, ColorUtil.toRGBA(this.color));
            RenderUtil.drawLine(this.x, (Float)_y.get(), this.x + 100.0F, (Float)_y.get(), 1.0F, ColorUtil.toRGBA(this.color));
            RenderUtil.drawLine(this.x, this.y, this.x, (Float)_y.get(), 1.0F, ColorUtil.toRGBA(this.color));
            RenderUtil.drawLine(this.x + 100.0F, this.y, this.x + 100.0F, (Float)_y.get(), 1.0F, ColorUtil.toRGBA(this.color));
         }

      }
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isMouseHovering(mouseX, mouseY)) {
         this.moving = true;
         this.diffx = this.x - (float)mouseX;
         this.diffy = this.y - (float)mouseY;
      }

      if (mouseButton == 1 && this.isMouseHovering(mouseX, mouseY)) {
         this.open = !this.open;
      }

      if (this.open) {
         this.modules.forEach((m) -> {
            m.onMouseClicked(mouseX, mouseY, mouseButton);
         });
      }

   }

   public void onMouseReleased(int mouseX, int mouseY, int state) {
      this.moving = false;
      if (this.open) {
         this.modules.forEach((m) -> {
            m.onMouseReleased(mouseX, mouseY, state);
         });
      }

   }

   public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      if (clickedMouseButton == 0 && this.moving) {
         this.x = (float)mouseX + this.diffx;
         this.y = (float)mouseY + this.diffy;
      }

      if (this.open) {
         this.modules.forEach((m) -> {
            m.onMouseClickMove(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void keyTyped(char typedChar, int keyCode) {
      if (this.open) {
         this.modules.forEach((m) -> {
            m.onKeyTyped(typedChar, keyCode);
         });
      }

   }

   public Boolean isMouseHovering(int mouseX, int mouseY) {
      return this.x < (float)mouseX && this.x + this.width > (float)mouseX && this.y < (float)mouseY && this.y + this.height > (float)mouseY;
   }

   public float getCenter(float a, float b, float c) {
      return a + (b - c) / 2.0F;
   }
}

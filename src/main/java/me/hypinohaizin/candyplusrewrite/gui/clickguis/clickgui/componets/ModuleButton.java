package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Panel;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class ModuleButton extends Component {
   public List<Component> components = new ArrayList();
   public Module module;
   private boolean isOpening = false;

   public ModuleButton(Module module, int x, int width, int height) {
      this.module = module;
      this.x = x;
      this.width = width;
      this.height = height;
      module.settings.forEach((s) -> {
         this.addButtonBySetting(s);
      });
      if (module.getClass() != HUDEditor.class) {
         this.components.add(new KeybindButton(module, x, width, height));
      }

   }

   public void onRender(int y) {
      this.y = y;
      RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.defaultColor);
      if (this.module.isEnable) {
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.enabledColor);
      }

      float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
      RenderUtil.drawString(this.module.name, (float)(this.x + 3), centeredY, this.module.isEnable ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toARGB(255, 255, 255, 255), false, 1.0F);
      if (this.isOpening) {
         this.components.forEach((c) -> {
            c.onRender2D(Panel.Cy += this.height);
            if (!c.visible) {
               Panel.Cy -= this.height;
            }

         });
      }

   }

   public void addButtonBySetting(Setting s) {
      if (s.getValue() instanceof Boolean) {
         this.components.add(new BooleanButton(this.module, s, this.x, this.width, this.height));
      } else if (s.getValue() instanceof Integer) {
         this.components.add(new SliderButton(this.module, s, this.x, this.width, this.height));
      } else if (s.getValue() instanceof Float) {
         this.components.add(new FloatSliderButton(this.module, s, this.x, this.width, this.height));
      } else if (s.getValue() instanceof Color) {
         this.components.add(new ColorButton(this.module, s, this.x, this.width, this.height));
      } else {
         this.components.add(new EnumButton(this.module, s, this.x, this.width, this.height));
      }

   }

   public void changeX(int x) {
      this.x = x;
      this.components.forEach((c) -> {
         c.x = x;
      });
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isOpening) {
         this.components.forEach((c) -> {
            if (c.visible) {
               c.mouseClicked(mouseX, mouseY, mouseButton);
            }

         });
      }

      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
         this.module.toggle();
      }

      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 1) {
         this.isOpening = !this.isOpening;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.isOpening) {
         this.components.forEach((c) -> {
            c.mouseReleased(mouseX, mouseY, state);
         });
      }

   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      if (this.isOpening) {
         this.components.forEach((c) -> {
            c.mouseClickMove(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.isOpening) {
         this.components.forEach((c) -> {
            c.onKeyTyped(typedChar, keyCode);
         });
      }

   }
}

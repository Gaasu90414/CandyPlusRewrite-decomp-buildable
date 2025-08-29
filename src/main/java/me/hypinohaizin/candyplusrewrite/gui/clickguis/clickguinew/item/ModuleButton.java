package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class ModuleButton extends Component {
   public List<Component> componentList = new ArrayList();
   public boolean open;

   public ModuleButton(Module module, float x) {
      this.module = module;
      this.x = x;
      this.width = 100.0F;
      this.open = false;
      this.height = 16.0F;
      module.settings.forEach(this::addSetting);
      this.add(new BindButton(module, x));
   }

   public float onRender(int mouseX, int mouseY, float x, float y) {
      RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
      if (this.module.isEnable) {
         RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
      }

      if (this.isMouseHovering(mouseX, mouseY)) {
         RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
      }

      String name = this.module.name;
      float namey = this.getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString(name, x + 3.0F, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
      RenderUtil.drawString("...", x + 100.0F - RenderUtil.getStringWidth("...", 1.0F) - 3.0F, namey - 1.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
      AtomicReference<Float> height = new AtomicReference(16.0F);
      if (this.open) {
         this.componentList.forEach((c) -> {
            float h = c.doRender(mouseX, mouseY, x, y + (Float)height.get());
            RenderUtil.drawRect(x, y + (Float)height.get(), 2.0F, h, ColorUtil.toRGBA(this.color));
            height.updateAndGet((v) -> {
               return v + h;
            });
         });
      }

      return (Float)height.get();
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isMouseHovering(mouseX, mouseY)) {
         this.module.toggle();
      }

      if (mouseButton == 1 && this.isMouseHovering(mouseX, mouseY)) {
         this.open = !this.open;
      }

      if (this.open) {
         this.componentList.forEach((c) -> {
            c.onMouseClicked(mouseX, mouseY, mouseButton);
         });
      }

   }

   public void onMouseReleased(int mouseX, int mouseY, int state) {
      if (this.open) {
         this.componentList.forEach((c) -> {
            c.onMouseReleased(mouseX, mouseY, state);
         });
      }

   }

   public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      if (this.open) {
         this.componentList.forEach((c) -> {
            c.onMouseClickMove(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.open) {
         this.componentList.forEach((c) -> {
            c.onKeyTyped(typedChar, keyCode);
         });
      }

   }

   public void addSetting(Setting setting) {
      Object value = setting.value;
      if (value instanceof Boolean) {
         this.add(new BooleanButton(setting, this.x));
      } else if (value instanceof Integer) {
         this.add(new IntegerSlider(setting, this.x));
      } else if (value instanceof Float) {
         this.add(new FloatSlider(setting, this.x));
      } else if (value instanceof Color) {
         this.add(new ColorSlider(setting, this.x));
      } else {
         this.add(new EnumButton(setting, this.x));
      }

   }

   private void add(Component component) {
      this.componentList.add(component);
   }
}

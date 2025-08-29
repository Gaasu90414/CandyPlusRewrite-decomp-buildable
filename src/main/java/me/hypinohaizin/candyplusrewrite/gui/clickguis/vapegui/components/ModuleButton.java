package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class ModuleButton extends Component {
   public List<Component> componentList = new ArrayList();
   public Module module;
   public boolean open;

   public ModuleButton(Module module, float x) {
      this.module = module;
      this.x = x;
      this.width = 110.0F;
      this.height = 18.0F;
      this.open = false;
      module.settings.forEach(this::addSetting);
   }

   public float doRender(float y, int mouseX, int mouseY) {
      this.y = y;
      RenderUtil.drawRect(this.x, y, this.width, this.height, this.baseColor);
      boolean hovering = this.isMouseHovering(mouseX, mouseY);
      if (this.module.isEnable) {
         RenderUtil.drawRect(this.x, y, this.width, this.height - 0.2F, this.mainColor);
      }

      if (hovering) {
         RenderUtil.drawRect(this.x, y, this.width, this.height, ColorUtil.toRGBA(255, 255, 255, 50));
      }

      float namey = this.getCenter(y, this.height, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString(this.module.name, this.x + 5.0F, namey, !this.module.isEnable && !hovering ? this.gray : this.white, false, 1.0F);
      float x = this.x + this.width - RenderUtil.getStringWidth("...", 1.0F) - 3.0F;
      RenderUtil.drawString("...", x, namey, !this.module.isEnable && !hovering ? this.gray : this.white, false, 1.0F);
      AtomicReference<Float> _height = new AtomicReference(this.height);
      if (this.open) {
         this.componentList.forEach((c) -> {
            float h = c.doRender((Float)_height.get() + y, mouseX, mouseY);
            RenderUtil.drawRect(x, y + (Float)_height.get(), 2.0F, h, this.mainColor);
            _height.updateAndGet((v) -> {
               return v + h;
            });
         });
      }

      return (Float)_height.get();
   }

   public void onMouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
      if (this.isMouseHovering(mouseX, mouseY) && clickedMouseButton == 1) {
         this.open = !this.open;
      }

      if (this.open) {
         this.componentList.forEach((c) -> {
            c.onMouseClicked(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void addSetting(Setting stg) {
      if (stg.getValue() instanceof Boolean) {
         this.componentList.add(new BooleanButton(stg, this.x));
      }

      if (stg.getValue() instanceof Enum) {
         this.componentList.add(new EnumButton(stg, this.x));
      }

   }
}

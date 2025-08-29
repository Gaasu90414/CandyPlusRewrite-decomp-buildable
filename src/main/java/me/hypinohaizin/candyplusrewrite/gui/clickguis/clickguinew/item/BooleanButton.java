package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.item;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.Component;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class BooleanButton extends Component {
   public Setting<Boolean> setting;

   public BooleanButton(Setting<Boolean> setting, float x) {
      this.setting = setting;
      this.x = x;
      this.width = 100.0F;
      this.height = 16.0F;
   }

   public float doRender(int mouseX, int mouseY, float x, float y) {
      if (this.setting.visible()) {
         RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
         if ((Boolean)this.setting.getValue()) {
            RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
         }

         if (this.isMouseHovering(mouseX, mouseY)) {
            RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
         }

         String name = this.setting.name;
         float namey = this.getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(name, x + 6.0F, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         return 16.0F;
      } else {
         return 0.0F;
      }
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isMouseHovering(mouseX, mouseY)) {
         this.setting.setValue(!(Boolean)this.setting.getValue());
      }

   }
}

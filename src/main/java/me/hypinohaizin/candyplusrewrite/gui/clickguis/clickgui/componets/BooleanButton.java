package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class BooleanButton extends Component {
   public Module module;
   public Setting<Boolean> setting;

   public BooleanButton(Module module, Setting setting, int x, int width, int height) {
      this.module = module;
      this.setting = setting;
      this.x = x;
      this.width = width;
      this.height = height;
   }

   public void onRender2D(int y) {
      this.visible = this.setting.visible();
      if (this.visible) {
         this.y = y;
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, (Boolean)this.setting.getValue() ? this.enabledColor : this.buttonColor);
         float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
         RenderUtil.drawString(this.setting.name, (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
         if ((Boolean)this.setting.getValue()) {
            this.drawOutLine();
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
         this.setting.setValue(!(Boolean)this.setting.getValue());
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
   }
}

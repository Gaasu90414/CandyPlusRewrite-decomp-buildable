package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class EnumButton extends Component {
   private final Module module;
   private final Setting<Enum> setting;

   public EnumButton(Module m, Setting<Enum> setting, int x, int width, int height) {
      this.module = m;
      this.setting = setting;
      this.x = x;
      this.width = width;
      this.height = height;
   }

   public void onRender2D(int y) {
      this.visible = true;
      this.y = y;
      float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
      RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.enabledColor);
      RenderUtil.drawString(this.setting.name + " : " + ((Enum)this.setting.getValue()).name(), (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
      this.drawOutLine();
   }

   public void mouseClicked(int x, int y, int button) {
      if (this.isMouseHovering(x, y) && button == 0) {
         this.setting.increaseEnum();
      }

   }
}

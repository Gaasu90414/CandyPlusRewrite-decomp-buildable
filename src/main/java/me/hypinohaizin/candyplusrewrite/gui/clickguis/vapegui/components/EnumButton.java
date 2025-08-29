package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.components;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.Component;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class EnumButton extends Component {
   public Setting<Enum> setting;
   public boolean open;

   public EnumButton(Setting<Enum> setting, float x) {
      this.setting = setting;
      this.x = x;
      this.width = 110.0F;
      this.height = 18.0F;
      this.open = false;
   }

   public float doRender(float y, int mouseX, int mouseY) {
      this.y = y;
      float width = RenderUtil.getStringWidth(((Enum)this.setting.getValue()).name(), 1.0F);
      float namey = this.getCenter(y, this.height, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawRect(this.x, y, width, this.height, this.baseColor);
      RenderUtil.drawString(this.setting.name, this.x + 8.0F, namey, this.white, false, 1.0F);
      RenderUtil.drawString(((Enum)this.setting.getValue()).name(), this.x + width - width - 5.0F, namey, this.white, false, 1.0F);
      return this.height;
   }
}

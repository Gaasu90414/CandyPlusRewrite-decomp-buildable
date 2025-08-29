package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.components;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.Component;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class BooleanButton extends Component {
   public Setting<Boolean> setting;
   public float _x = 0.0F;

   public BooleanButton(Setting setting, float x) {
      this.setting = setting;
      this.x = x;
      this.width = 110.0F;
      this.height = 18.0F;
      this._x = this.getX();
   }

   public float doRender(float y, int mouseX, int mouseY) {
      this.y = y;
      RenderUtil.drawRect(this.x, y, this.width, this.height, this.baseColor);
      float namey = this.getCenter(y, this.height, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString(this.setting.name, this.x + 8.0F, namey, (Boolean)this.setting.getValue() ? this.white : this.gray, false, 1.0F);
      float linex = this.x + this.width - 20.0F;
      RenderUtil.drawRect(linex, namey - 3.0F, 15.0F, 8.0F, (Boolean)this.setting.getValue() ? this.mainColor : ColorUtil.toRGBA(60, 60, 60));
      RenderUtil.drawRect(this._x, namey - 2.0F, 4.0F, 6.0F, this.baseColor);
      if (this.isMouseHovering((float)mouseX, (float)mouseY, this._x, namey - 2.0F, 4.0F, 6.0F)) {
         RenderUtil.drawRect(this._x, namey - 2.0F, 4.0F, 6.0F, ColorUtil.toRGBA(255, 255, 255, 50));
      }

      this._x += (float)((double)(this.getX() - this._x) * 0.5D);
      return this.height;
   }

   public void onMouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
      float namey = this.getCenter(this.y, this.height, RenderUtil.getStringHeight(1.0F));
      if (this.isMouseHovering((float)mouseX, (float)mouseY, this._x, namey - 2.0F, 4.0F, 6.0F) && 0 == clickedMouseButton) {
         this.setting.setValue(!(Boolean)this.setting.getValue());
      }

   }

   public float getX() {
      return (Boolean)this.setting.getValue() ? this.x + this.width - 13.0F : this.x + this.width - 20.0F;
   }
}

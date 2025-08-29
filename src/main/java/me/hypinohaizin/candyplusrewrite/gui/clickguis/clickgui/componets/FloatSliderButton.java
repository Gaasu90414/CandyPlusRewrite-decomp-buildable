package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class FloatSliderButton extends Component {
   private float value;
   private final Module module;
   private final Setting<? extends Number> setting;
   private boolean changing;
   private final float diff;

   public FloatSliderButton(Module module, Setting<? extends Number> setting, int x, int width, int height) {
      this.module = module;
      this.setting = setting;
      this.x = x;
      this.width = width;
      this.height = height;
      this.changing = false;
      this.diff = ((Number)setting.maxValue).floatValue() - ((Number)setting.minValue).floatValue();
      this.value = this.getValue();
   }

   public void onRender2D(int y) {
      this.visible = this.setting.visible();
      if (this.visible) {
         this.y = y;
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.buttonColor);
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width * this.value, (float)this.height, this.enabledColor);
         float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
         RenderUtil.drawString(this.setting.name, (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
         String str = ((Number)this.setting.getValue()).toString();
         RenderUtil.drawString(str, (float)(this.x + this.width) - RenderUtil.getStringWidth(str, 1.0F) - 3.0F, centeredY, this.whiteColor, false, 1.0F);
         this.drawOutLine();
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
         this.changing = true;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.changing = false;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
      if (this.changing && clickedMouseButton == 0) {
         if (mouseX >= this.x - 10 && mouseX <= this.x + this.width + 10) {
            float v = (float)(mouseX - this.x) / (float)this.width;
            v = Math.max(0.0F, Math.min(1.0F, v));
            this.value = v;
            float newVal = (((Number)this.setting.maxValue).floatValue() - ((Number)this.setting.minValue).floatValue()) * this.value + ((Number)this.setting.minValue).floatValue();
            float rounded = (float)Math.round(newVal * 10.0F) / 10.0F;
            Number out = this.setting.getValue() instanceof Double ? Double.valueOf((double)rounded) : (double)rounded;
            this.setting.setValue(out);
         }
      }
   }

   private float getValue() {
      return (((Number)this.setting.getValue()).floatValue() - ((Number)this.setting.minValue).floatValue()) / this.diff;
   }
}

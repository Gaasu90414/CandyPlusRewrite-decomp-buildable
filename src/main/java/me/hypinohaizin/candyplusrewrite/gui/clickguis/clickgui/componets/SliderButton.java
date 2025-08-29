package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class SliderButton extends Component {
   private float value;
   private final Module module;
   private final Setting<Integer> setting;
   private boolean changing;
   private final int diff;

   public SliderButton(Module module, Setting<Integer> setting, int x, int width, int height) {
      this.module = module;
      this.setting = setting;
      this.x = x;
      this.width = width;
      this.height = height;
      this.changing = false;
      this.diff = (Integer)setting.maxValue - (Integer)setting.minValue;
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
         RenderUtil.drawString(String.valueOf(this.setting.getValue()), (float)(this.x + this.width) - RenderUtil.getStringWidth(String.valueOf(this.setting.getValue()), 1.0F) - 3.0F, centeredY, this.whiteColor, false, 1.0F);
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
      if (this.x - 10 < mouseX && this.x + this.width + 10 > mouseX && clickedMouseButton == 0 && this.changing) {
         this.value = ((float)mouseX - (float)this.x) / (float)this.width;
         if (this.value > 1.0F) {
            this.value = 1.0F;
         }

         if (this.value < 0.0F) {
            this.value = 0.0F;
         }

         this.setting.setValue((int)((float)((Integer)this.setting.maxValue - (Integer)this.setting.minValue) * this.value + (float)(Integer)this.setting.minValue));
      }

   }

   private float getValue() {
      return (float)(Integer)this.setting.getValue() / ((float)((Integer)this.setting.maxValue - (Integer)this.setting.minValue) * 1.0F);
   }
}

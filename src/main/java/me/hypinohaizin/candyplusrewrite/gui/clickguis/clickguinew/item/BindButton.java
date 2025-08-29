package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.item;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import org.lwjgl.input.Keyboard;

public class BindButton extends Component {
   public Module module;
   public boolean keyWaiting = false;

   public BindButton(Module module, float x) {
      this.module = module;
      this.x = x;
      this.width = 100.0F;
      this.height = 16.0F;
   }

   public float doRender(int mouseX, int mouseY, float x, float y) {
      RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
      if (this.keyWaiting) {
         RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
      }

      if (this.isMouseHovering(mouseX, mouseY)) {
         RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
      }

      float bindy = this.getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString("Bind : " + (this.keyWaiting ? "..." : (this.module.key.key == -1 ? "NONE" : Keyboard.getKeyName(this.module.key.key))), x + 6.0F, bindy, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
      return 16.0F;
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isMouseHovering(mouseX, mouseY)) {
         this.keyWaiting = !this.keyWaiting;
      }

   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.keyWaiting) {
         if (keyCode != 14 && keyCode != 1) {
            this.module.setKey(keyCode);
         } else {
            this.module.setKey(-1);
         }

         this.keyWaiting = false;
      }

   }
}

package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.componets;

import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.ClickGUI;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import org.lwjgl.input.Keyboard;

public class KeybindButton extends Component {
   private final Module module;
   private boolean isWaiting = false;

   public KeybindButton(Module m, int x, int width, int height) {
      this.module = m;
      this.x = x;
      this.width = width;
      this.height = height;
   }

   public void onRender2D(int y) {
      this.visible = true;
      this.y = y;
      float centeredY = (float)y + ((float)this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
      if (this.isWaiting) {
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.enabledColor);
         RenderUtil.drawString("Key : ...", (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
      } else {
         RenderUtil.drawRect((float)this.x, (float)y, (float)this.width, (float)this.height, this.buttonColor);
         RenderUtil.drawString("Key : " + (this.module.key.getKey() != -1 ? Keyboard.getKeyName(this.module.key.getKey()) : "NONE"), (float)(this.x + 5), centeredY, this.whiteColor, false, 1.0F);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
         this.isWaiting = !this.isWaiting;
      }

   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.isWaiting) {
         if (keyCode != 14 && keyCode != 1) {
            this.module.setKey(keyCode);
         } else if (!(this.module instanceof ClickGUI)) {
            this.module.setKey(-1);
         }

         this.isWaiting = false;
      }

   }
}

package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.Component;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import me.hypinohaizin.candyplusrewrite.utils.StringUtil;

public class Panel extends Component {
   public boolean open;
   public List<ModuleButton> moduleButtonList = new ArrayList();
   public Module.Categories category;

   public Panel(Module.Categories category, float x, float y) {
      this.category = category;
      this.x = x;
      this.y = y;
      this.width = 110.0F;
      this.height = 22.0F;
      this.open = true;
      CandyPlusRewrite.m_module.getModulesWithCategories(category).forEach((m) -> {
         this.moduleButtonList.add(new ModuleButton(m, x));
      });
   }

   public void onRender(int mouseX, int mouseY) {
      RenderUtil.drawRect(this.x, this.y, this.width, this.height, this.panelColor);
      float namey = this.getCenter(this.y, this.height, RenderUtil.getStringHeight(1.0F));
      RenderUtil.drawString(StringUtil.getName(this.category.name()), this.x + 5.0F, namey, this.white, false, 1.0F);
      AtomicReference<Float> y = new AtomicReference(this.y + this.height);
      this.moduleButtonList.forEach((m) -> {
         Float n = (Float)y.updateAndGet((v) -> {
            return v + m.doRender(v, mouseX, mouseY);
         });
      });
   }

   public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseHovering(mouseX, mouseY) && 1 == mouseButton) {
         this.open = !this.open;
      }

      if (this.open) {
         this.moduleButtonList.forEach((m) -> {
            m.onMouseClicked(mouseX, mouseY, mouseButton);
         });
      }

   }
}

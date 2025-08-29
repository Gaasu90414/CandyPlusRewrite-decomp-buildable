package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class ModuleList extends Hud {
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Boolean> background = this.register(new Setting("Background", true));
   public Setting<Color> backcolor = this.register(new Setting("BGColor", new Color(40, 40, 40, 70), (v) -> {
      return (Boolean)this.background.getValue();
   }));
   public Setting<Boolean> edge = this.register(new Setting("Edge", true));
   public Setting<Color> edgeColor = this.register(new Setting("EGColor", new Color(255, 255, 255, 150), (v) -> {
      return (Boolean)this.edge.getValue();
   }));

   public ModuleList() {
      super("ModuleList", 0.0F, 110.0F);
   }

   public void onRender() {
      float y = (Float)this.y.getValue();
      float size = 1.0F;
      float _width = 0.0F;
      float height = RenderUtil.getStringHeight(1.0F);
      List<Module> sortedModuleList = new ArrayList(CandyPlusRewrite.m_module.modules);
      Collections.sort(sortedModuleList, (c1, c2) -> {
         return c1.name.compareToIgnoreCase(c2.name);
      });
      Iterator var6 = sortedModuleList.iterator();

      while(var6.hasNext()) {
         Module module = (Module)var6.next();
         if (module.isEnable) {
            String name = module.name;
            float width = RenderUtil.getStringWidth(name, 1.0F);
            if (width > _width) {
               _width = width;
            }

            if ((Boolean)this.background.getValue()) {
               RenderUtil.drawRect((Float)this.x.getValue(), y, width + 20.0F, height + 10.0F, ColorUtil.toRGBA((Color)this.backcolor.getValue()));
               if ((Boolean)this.edge.getValue()) {
                  RenderUtil.drawRect((Float)this.x.getValue(), y, 2.0F, height + 10.0F, ColorUtil.toRGBA((Color)this.edgeColor.getValue()));
               }
            }

            RenderUtil.drawString(name, (Float)this.x.getValue() + 10.0F, y + 5.0F, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), 1.0F);
            y += height + 10.0F;
         }
      }

      y -= height + 11.0F;
      this.width = _width + 20.0F;
      this.height = y - (Float)this.y.getValue();
   }
}

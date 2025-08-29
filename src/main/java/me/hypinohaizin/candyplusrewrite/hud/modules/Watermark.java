package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class Watermark extends Hud {
   public Setting<Float> speed = this.register(new Setting("Speed", 4.0F, 10.0F, 0.1F));
   public Setting<Float> size = this.register(new Setting("Size", 1.0F, 5.0F, 0.5F));
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Boolean> background = this.register(new Setting("Background", false));
   public Setting<Color> backcolor = this.register(new Setting("BGColor", new Color(40, 40, 40, 60), (v) -> {
      return (Boolean)this.background.getValue();
   }));
   private float offsetx = 0.0F;

   public Watermark() {
      super("Watermark", 10.0F, 10.0F);
   }

   public void onRender() {
      String message = "Candy+ Rewrite v0.3.4";
      float size = (Float)this.size.getValue();
      float width = RenderUtil.getStringWidth("Candy+ Rewrite v0.3.4", size);
      float height = RenderUtil.getStringHeight(size);
      if ((Boolean)this.background.getValue()) {
         RenderUtil.drawRect((Float)this.x.getValue() + this.offsetx, (Float)this.y.getValue(), width + 20.0F * size, height + 10.0F * size, ColorUtil.toRGBA((Color)this.backcolor.getValue()));
      }

      RenderUtil.drawString("Candy+ Rewrite v0.3.4", (Float)this.x.getValue() + 10.0F + this.offsetx, (Float)this.y.getValue() + 5.0F, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), size);
      if (this.scaledWidth + RenderUtil.getStringWidth("Candy+ Rewrite v0.3.4", size) + 10.0F < this.offsetx) {
         this.offsetx = RenderUtil.getStringWidth("Candy+ Rewrite v0.3.4", size) * -1.0F - 10.0F;
      }

      this.width = width + 5.0F * size;
      this.height = height + 5.0F * size;
   }
}

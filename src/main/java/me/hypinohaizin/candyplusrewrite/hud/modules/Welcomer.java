//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class Welcomer extends Hud {
   public Setting<Boolean> scroll = this.register(new Setting("Scroll", false));
   public Setting<Float> speed = this.register(new Setting("Speed", 4.0F, 10.0F, 0.1F));
   public Setting<Float> size = new Setting("Size", 1.0F, 5.0F, 0.5F);
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Boolean> background = this.register(new Setting("Background", false));
   public Setting<Color> backcolor = this.register(new Setting("BGColor", new Color(40, 40, 40, 60), (v) -> {
      return (Boolean)this.background.getValue();
   }));
   private float offsetx = 0.0F;

   public Welcomer() {
      super("Welcomer", 5.0F, 5.0F);
   }

   public void onRender() {
      String message = "Welcome " + this.getPlayerName();
      float size = (Float)this.size.getValue();
      float width = RenderUtil.getStringWidth(message, size);
      float height = RenderUtil.getStringHeight(size);
      if ((Boolean)this.background.getValue()) {
         RenderUtil.drawRect((Float)this.x.getValue() + this.offsetx, (Float)this.y.getValue(), width + 20.0F * size, height + 10.0F * size, ColorUtil.toRGBA((Color)this.backcolor.getValue()));
      }

      RenderUtil.drawString(message, (Float)this.x.getValue() + 10.0F + this.offsetx, (Float)this.y.getValue() + 5.0F, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), size);
      if ((Boolean)this.scroll.getValue()) {
         this.offsetx += (Float)this.speed.getValue();
      } else {
         this.offsetx = 0.0F;
      }

      if (this.scaledWidth + RenderUtil.getStringWidth(message, size) + 10.0F < this.offsetx) {
         this.offsetx = RenderUtil.getStringWidth(message, size) * -1.0F - 10.0F;
      }

      this.width = width + 20.0F * size;
      this.height = height + 10.0F * size;
   }

   public String getPlayerName() {
      return mc.player.getName();
   }
}

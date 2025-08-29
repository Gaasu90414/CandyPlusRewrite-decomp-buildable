package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;

public class EnchantmentColor extends Module {
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 50)));
   public static EnchantmentColor INSTANCE;

   public EnchantmentColor() {
      super("EnchantmentColor", Module.Categories.RENDER, false, false);
      INSTANCE = this;
   }
}

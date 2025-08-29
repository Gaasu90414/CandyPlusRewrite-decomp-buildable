package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;

public class SmallShield extends Module {
   public Setting<Boolean> normalOffset = this.register(new Setting("OffNormal", false));
   public Setting<Float> offset = this.register(new Setting("Offset", 0.7F, 1.0F, 0.0F, (v) -> {
      return (Boolean)this.normalOffset.getValue();
   }));
   public Setting<Float> offX = this.register(new Setting("OffX", 0.0F, 1.0F, -1.0F, (v) -> {
      return !(Boolean)this.normalOffset.getValue();
   }));
   public Setting<Float> offY = this.register(new Setting("OffY", 0.0F, 1.0F, -1.0F, (v) -> {
      return !(Boolean)this.normalOffset.getValue();
   }));
   public Setting<Float> mainX = this.register(new Setting("MainX", 0.0F, 1.0F, -1.0F));
   public Setting<Float> mainY = this.register(new Setting("MainY", 0.0F, 1.0F, -1.0F));

   public SmallShield() {
      super("SmallShield", Module.Categories.RENDER, false, false);
   }

   public static SmallShield getINSTANCE() {
      return (SmallShield)CandyPlusRewrite.m_module.getModuleWithClass(SmallShield.class);
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         if ((Boolean)this.normalOffset.getValue()) {
         }

      }
   }
}

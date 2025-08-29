//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.settings.GameSettings.Options;

public class CustomFov extends Module {
   public Setting<Float> cfov = this.register(new Setting("Fov", 0.0F, 180.0F, 1.0F));

   public CustomFov() {
      super("CustomFov", Module.Categories.RENDER, false, false);
   }

   public void onUpdate() {
      mc.gameSettings.setOptionFloatValue(Options.FOV, (Float)this.cfov.getValue());
   }
}

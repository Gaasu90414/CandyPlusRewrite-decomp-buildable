//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class PlayerModel extends Hud {
   public Setting<Float> scale = this.register(new Setting("Scale", 50.0F, 100.0F, 30.0F));

   public PlayerModel() {
      super("PlayerModel", 100.0F, 150.0F);
   }

   public void onRender() {
      this.width = (mc.player.width + 0.5F) * (Float)this.scale.getValue() + 10.0F;
      this.height = (mc.player.height + 0.5F) * (Float)this.scale.getValue();
      RenderUtil.renderEntity(mc.player, (Float)this.x.getValue() + this.width - 30.0F, (Float)this.y.getValue() + this.height - 20.0F, (Float)this.scale.getValue());
   }
}

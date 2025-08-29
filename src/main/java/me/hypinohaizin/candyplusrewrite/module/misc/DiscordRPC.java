package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;

public class DiscordRPC extends Module {
   public Setting<Boolean> girl = this.register(new Setting("Girl", false));

   public DiscordRPC() {
      super("DiscordRPC", Module.Categories.MISC, false, false);
   }

   public void onEnable() {
      CandyPlusRewrite.m_rpc.enable(this);
   }

   public void onDisable() {
      CandyPlusRewrite.m_rpc.disable();
   }
}

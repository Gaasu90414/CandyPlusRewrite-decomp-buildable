package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;

public class Notification extends Module {
   public Setting<Integer> time = this.register(new Setting("Time", 2, 5, 1));
   public Setting<Boolean> togglE = this.register(new Setting("Toggle", false));
   public Setting<Boolean> chat = this.register(new Setting("ChatToggle", false));
   public Setting<Boolean> message = this.register(new Setting("Message", true));
   public Setting<Boolean> player = this.register(new Setting("Player", true));
   public Setting<Boolean> pop = this.register(new Setting("Totem", true));
   public Setting<Boolean> death = this.register(new Setting("Death", true));

   public Notification() {
      super("Notification", Module.Categories.RENDER, false, true);
   }
}

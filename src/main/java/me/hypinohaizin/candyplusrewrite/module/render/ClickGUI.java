//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.CGui;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui.CandyGUI;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew.CandyGUI2;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;

public class ClickGUI extends Module {
   private final Setting<ClickGUI.type> guiType;
   public Setting<Color> color;
   public Setting<Boolean> outline;

   public ClickGUI() {
      super("ClickGUI", Module.Categories.RENDER, 21, false, false);
      this.guiType = this.register(new Setting("Type", ClickGUI.type.New));
      this.color = this.register(new Setting("Color", new Color(210, 0, 130, 255), (v) -> {
         return this.guiType.getValue() != ClickGUI.type.Old;
      }));
      this.outline = this.register(new Setting("Outline", false, (v) -> {
         return this.guiType.getValue() == ClickGUI.type.New;
      }));
   }

   public void onEnable() {
      if (!this.nullCheck()) {
         if (!(mc.currentScreen instanceof CGui)) {
            if (this.guiType.getValue() == ClickGUI.type.New) {
               mc.displayGuiScreen(new CandyGUI2());
            } else {
               mc.displayGuiScreen(new CandyGUI());
            }
         }

      }
   }

   public void onUpdate() {
      if (!(mc.currentScreen instanceof CGui) && !HUDEditor.instance.isEnable) {
         this.disable();
      }

   }

   public static enum type {
      Old,
      New;
   }
}

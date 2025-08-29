//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickgui;

import java.util.ArrayList;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.CGui;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import org.lwjgl.input.Mouse;

public class CandyGUI extends CGui {
   private static List<Panel> panels = new ArrayList();
   private static Panel hubPanel = null;
   public static boolean isHovering = false;

   public void initGui() {
      if (panels.size() == 0) {
         int x = 30;
         Module.Categories[] var2 = Module.Categories.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Module.Categories c = var2[var4];
            if (c == Module.Categories.HUB) {
               hubPanel = new Panel(c, 30, 30, true);
            } else {
               panels.add(new Panel(c, x, 20, true));
               CandyPlusRewrite.Info("Loaded module panel : " + c.name());
               x += 120;
            }
         }
      }

   }

   public void onGuiClosed() {
      if (HUDEditor.instance.isEnable) {
         HUDEditor.instance.disable();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.scroll();
      if (HUDEditor.instance.isEnable) {
         hubPanel.onRender(mouseX, mouseY, partialTicks);
      } else {
         panels.forEach((p) -> {
            p.onRender(mouseX, mouseY, partialTicks);
         });
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.mouseClicked(mouseX, mouseY, mouseButton);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseClicked(mouseX, mouseY, mouseButton);
         });
      } else {
         panels.forEach((p) -> {
            p.mouseClicked(mouseX, mouseY, mouseButton);
         });
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.mouseReleased(mouseX, mouseY, state);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseReleased(mouseX, mouseY, state);
         });
      } else {
         panels.forEach((p) -> {
            p.mouseReleased(mouseX, mouseY, state);
         });
      }

   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.mouseClickMove(mouseX, mouseY, clickedMouseButton);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
         });
      } else {
         panels.forEach((p) -> {
            p.mouseClickMove(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void keyTyped(char typedChar, int keyCode) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.keyTyped(typedChar, keyCode);
      } else {
         panels.forEach((p) -> {
            p.keyTyped(typedChar, keyCode);
         });
      }

      try {
         super.keyTyped(typedChar, keyCode);
      } catch (Exception var4) {
      }

   }

   public void scroll() {
      int dWheel = Mouse.getDWheel();
      Panel hubPanel2;
      if (dWheel < 0) {
         if (HUDEditor.instance.isEnable) {
            hubPanel2 = CandyGUI.hubPanel;
            hubPanel2.y -= 15;
         } else {
            panels.forEach((p) -> {
               p.y -= 15;
            });
         }
      } else if (dWheel > 0) {
         if (HUDEditor.instance.isEnable) {
            hubPanel2 = CandyGUI.hubPanel;
            hubPanel2.y += 15;
         } else {
            panels.forEach((p) -> {
               p.y += 15;
            });
         }
      }

   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.gui.clickguis.clickguinew;

import java.util.ArrayList;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.CGui;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class CandyGUI2 extends CGui {
   public static List<Panel> panelList = new ArrayList();
   public static Panel hubPanel;

   public void initGui() {
      if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
         Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
      }

      Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      if (panelList.size() == 0) {
         int x = 50;
         Module.Categories[] var2 = Module.Categories.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Module.Categories category = var2[var4];
            if (category == Module.Categories.HUB) {
               hubPanel = new Panel(200.0F, 20.0F, category);
            } else {
               panelList.add(new Panel((float)x, 20.0F, category));
               x += 120;
            }
         }
      }

   }

   public void onGuiClosed() {
      if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
         Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.scroll();
      if (HUDEditor.instance.isEnable) {
         hubPanel.onRender(mouseX, mouseY);
      } else {
         panelList.forEach((p) -> {
            p.onRender(mouseX, mouseY);
         });
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.onMouseClicked(mouseX, mouseY, mouseButton);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseClicked(mouseX, mouseY, mouseButton);
         });
      } else {
         panelList.forEach((p) -> {
            p.onMouseClicked(mouseX, mouseY, mouseButton);
         });
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.onMouseReleased(mouseX, mouseY, state);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseReleased(mouseX, mouseY, state);
         });
      } else {
         panelList.forEach((p) -> {
            p.onMouseReleased(mouseX, mouseY, state);
         });
      }

   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      if (HUDEditor.instance.isEnable) {
         hubPanel.onMouseClickMove(mouseX, mouseY, clickedMouseButton);
         CandyPlusRewrite.m_module.modules.stream().filter((m) -> {
            return m instanceof Hud;
         }).forEach((m) -> {
            ((Hud)m).mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
         });
      } else {
         panelList.forEach((p) -> {
            p.onMouseClickMove(mouseX, mouseY, clickedMouseButton);
         });
      }

   }

   public void keyTyped(char typedChar, int keyCode) {
      if (1 != keyCode) {
         if (HUDEditor.instance.isEnable) {
            hubPanel.keyTyped(typedChar, keyCode);
         } else {
            panelList.forEach((p) -> {
               p.keyTyped(typedChar, keyCode);
            });
         }

      } else if (HUDEditor.instance.isEnable) {
         HUDEditor.instance.disable();
      } else {
         this.mc.displayGuiScreen((GuiScreen)null);
      }
   }

   public void scroll() {
      int dWheel = Mouse.getDWheel();
      Panel hubPanel2;
      if (dWheel < 0) {
         if (HUDEditor.instance.isEnable) {
            hubPanel2 = CandyGUI2.hubPanel;
            hubPanel2.y -= 15.0F;
         } else {
            panelList.forEach((p) -> {
               p.y -= 15.0F;
            });
         }
      } else if (dWheel > 0) {
         if (HUDEditor.instance.isEnable) {
            hubPanel2 = CandyGUI2.hubPanel;
            hubPanel2.y += 15.0F;
         } else {
            panelList.forEach((p) -> {
               p.y += 15.0F;
            });
         }
      }

   }
}

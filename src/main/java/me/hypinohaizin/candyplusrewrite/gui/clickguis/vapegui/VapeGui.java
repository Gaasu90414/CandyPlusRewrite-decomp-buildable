//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui;

import java.util.ArrayList;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.CGui;
import me.hypinohaizin.candyplusrewrite.gui.clickguis.vapegui.components.Panel;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class VapeGui extends CGui {
   public static List<Panel> panelList = new ArrayList();
   public Panel hudPanel;

   public void initGui() {
      if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
         Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
      }

      Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      if (panelList.isEmpty()) {
         int x = 50;
         Module.Categories[] var2 = Module.Categories.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Module.Categories category = var2[var4];
            if (category == Module.Categories.HUB) {
               this.hudPanel = new Panel(category, 100.0F, 10.0F);
            } else {
               panelList.add(new Panel(category, (float)x, 10.0F));
               x += 120;
            }
         }

      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.scroll();
      if (HUDEditor.instance.isEnable) {
         this.hudPanel.onRender(mouseX, mouseY);
      } else {
         panelList.forEach((p) -> {
            p.onRender(mouseX, mouseY);
         });
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (HUDEditor.instance.isEnable) {
         this.hudPanel.onMouseClicked(mouseX, mouseY, mouseButton);
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

   public void scroll() {
      int dWheel = Mouse.getDWheel();
      Panel hudPanel2;
      if (dWheel < 0) {
         if (HUDEditor.instance.isEnable) {
            hudPanel2 = this.hudPanel;
            hudPanel2.y -= 15.0F;
         } else {
            panelList.forEach((p) -> {
               p.y -= 15.0F;
            });
         }
      } else if (dWheel > 0) {
         if (HUDEditor.instance.isEnable) {
            hudPanel2 = this.hudPanel;
            hudPanel2.y += 15.0F;
         } else {
            panelList.forEach((p) -> {
               p.y += 15.0F;
            });
         }
      }

   }
}

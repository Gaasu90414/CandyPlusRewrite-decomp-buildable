//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.mainmenu;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainMenu extends GuiScreen {
   private final ResourceLocation background1 = new ResourceLocation("candyplusrewrite", "textures/mainmenu1.png");
   private final ResourceLocation background2 = new ResourceLocation("candyplusrewrite", "textures/mainmenu2.png");
   private final ResourceLocation background3 = new ResourceLocation("candyplusrewrite", "textures/mainmenu3.png");
   private float animatedX;
   private float animatedY;
   private List<MainMenu.CustomButton> buttons;
   private int tick;
   private int bg;
   private int textX;
   private int textY;
   private int textWidth;

   public void initGui() {
      this.tick = 0;
      this.buttons = new LinkedList();
      this.buttons.add(new MainMenu.CustomButton("SinglePlayer", new ResourceLocation("candyplusrewrite", "textures/icon/singleplayer.png"), new GuiWorldSelection(this)));
      this.buttons.add(new MainMenu.CustomButton("MultiPlayer", new ResourceLocation("candyplusrewrite", "textures/icon/multiplayer.png"), new GuiMultiplayer(this)));
      this.buttons.add(new MainMenu.CustomButton("Language", new ResourceLocation("candyplusrewrite", "textures/icon/language.png"), new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager())));
      this.buttons.add(new MainMenu.CustomButton("Settings", new ResourceLocation("candyplusrewrite", "textures/icon/setting.png"), new GuiOptions(this, this.mc.gameSettings)));
      this.buttons.add(new MainMenu.CustomButton("Quit", new ResourceLocation("candyplusrewrite", "textures/icon/quit.png"), (GuiScreen)null));
      super.initGui();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      switch(Character.toLowerCase(typedChar)) {
      case 'l':
         Minecraft.getMinecraft().displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
         break;
      case 'm':
         Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(this));
      case 'n':
      case 'p':
      case 'r':
      default:
         break;
      case 'o':
         Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 'q':
         Minecraft.getMinecraft().shutdown();
         break;
      case 's':
         Minecraft.getMinecraft().displayGuiScreen(new GuiWorldSelection(this));
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.disableLighting();
      ++this.tick;
      if (this.tick % 600 == 0) {
         this.bg = (this.bg + 1) % 3;
      }

      ResourceLocation[] backgrounds = new ResourceLocation[]{this.background1, this.background2, this.background3};
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.mc.getTextureManager().bindTexture(backgrounds[this.bg]);
      float bw = (float)sr.getScaledWidth() * 4.0F / 3.0F;
      float bh = (float)sr.getScaledHeight() * 4.0F / 3.0F;
      drawModalRectWithCustomSizedTexture(Math.round(-this.animatedX / 4.0F), Math.round(-this.animatedY / 3.0F), 0.0F, 0.0F, Math.round(bw), Math.round(bh), bw, bh);
      int xOffset = sr.getScaledWidth() / 2 - 180;
      int baseY = sr.getScaledHeight() / 2 - 20;

      for(Iterator var10 = this.buttons.iterator(); var10.hasNext(); xOffset += 80) {
         MainMenu.CustomButton cb = (MainMenu.CustomButton)var10.next();
         cb.drawScreen(xOffset, baseY, mouseX, mouseY);
      }

      String credit = "By CandyPlus Rewrite DEV Team";
      this.textWidth = this.mc.fontRenderer.getStringWidth(credit);
      this.textX = sr.getScaledWidth() - this.textWidth - 4;
      this.textY = sr.getScaledHeight() - this.mc.fontRenderer.FONT_HEIGHT - 6;
      this.mc.fontRenderer.drawString(credit, this.textX, this.textY, -788529153);
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.animatedX += ((float)mouseX - this.animatedX) * 0.12F;
      this.animatedY += ((float)mouseY - this.animatedY) * 0.12F;
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      Iterator var4 = this.buttons.iterator();

      while(var4.hasNext()) {
         MainMenu.CustomButton cb = (MainMenu.CustomButton)var4.next();
         cb.onClicked(mouseX, mouseY, mouseButton);
      }

      if (mouseX >= this.textX && mouseX <= this.textX + this.textWidth && mouseY >= this.textY && mouseY <= this.textY + this.mc.fontRenderer.FONT_HEIGHT) {
         this.openLink("https://discord.gg/ktQRKNZzbH");
      }

   }

   private void openLink(String url) {
      try {
         Desktop.getDesktop().browse(URI.create(url));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static class CustomButton {
      private final ResourceLocation resource;
      private final GuiScreen parent;
      private float animatedSize = 25.0F;
      private int anchorX;
      private int anchorY;
      private int renderX;
      private int renderY;
      private int renderSize;
      private final String name;

      public CustomButton(String name, ResourceLocation resource, GuiScreen parent) {
         this.resource = resource;
         this.parent = parent;
         this.name = name;
      }

      public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
         this.anchorX = posX;
         this.anchorY = posY;
         boolean hover = this.isHovering(mouseX, mouseY);
         this.animatedSize = animate(this.animatedSize, hover ? 30.0F : 25.0F);
         int size = Math.round(this.animatedSize * 1.5F);
         int cx = posX + 24;
         int cy = posY + 24;
         int rx = cx - size / 2;
         int ry = cy - size / 2;
         this.renderX = rx;
         this.renderY = ry;
         this.renderSize = size;
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.getMinecraft().getTextureManager().bindTexture(this.resource);
         Gui.drawModalRectWithCustomSizedTexture(rx, ry, 0.0F, 0.0F, size, size, (float)size, (float)size);
         if (hover) {
            int nameWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(this.name);
            int labelY = ry + size + 6;
            Minecraft.getMinecraft().fontRenderer.drawString(this.name, cx - nameWidth / 2, labelY, -1);
         }

      }

      public void onClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            if (this.parent == null) {
               Minecraft.getMinecraft().shutdown();
            } else {
               Minecraft.getMinecraft().displayGuiScreen(this.parent);
            }
         }

      }

      private boolean isHovering(int mouseX, int mouseY) {
         return mouseX >= this.renderX && mouseX <= this.renderX + this.renderSize && mouseY >= this.renderY && mouseY <= this.renderY + this.renderSize;
      }

      private static float animate(float current, float target) {
         float speed = 0.18F;
         return current + (target - current) * speed;
      }
   }
}

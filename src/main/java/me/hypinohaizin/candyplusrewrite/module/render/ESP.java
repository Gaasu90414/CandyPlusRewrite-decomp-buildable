//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class ESP extends Module {
   public Setting<Float> width = this.register(new Setting("Width", 1.5F, 5.0F, 0.5F));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));

   public ESP() {
      super("ESP", Module.Categories.RENDER, false, false);
   }

   public void onRender3D() {
      if (!this.nullCheck()) {
         Iterator var1 = ((List) mc.world.playerEntities.stream().filter((e) -> {
            return e.getEntityId() != mc.player.getEntityId();
         }).collect(Collectors.toList())).iterator();

         while (var1.hasNext()) {
            Object player = var1.next();
            this.drawESP((EntityPlayer) player);
         }

      }
   }

   public void drawESP(EntityPlayer player) {
      GlStateManager.pushMatrix();
      AxisAlignedBB bb = player.getCollisionBoundingBox();
      double z = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
      RenderUtil3D.drawLine(bb.minX, bb.maxY, z, bb.maxX, bb.maxY, z, (Color) this.color.getValue(), (Float) this.width.getValue());
      RenderUtil3D.drawLine(bb.minX, bb.minY, z, bb.maxX, bb.minY, z, (Color) this.color.getValue(), (Float) this.width.getValue());
      RenderUtil3D.drawLine(bb.minX, bb.minY, z, bb.minX, bb.maxY, z, (Color) this.color.getValue(), (Float) this.width.getValue());
      RenderUtil3D.drawLine(bb.maxX, bb.minY, z, bb.maxX, bb.maxY, z, (Color) this.color.getValue(), (Float) this.width.getValue());
      double x = bb.minX - 0.28D;
      double y = bb.minY;
      double width = 0.04D;
      double height = bb.maxY - bb.minY;
      RenderUtil3D.drawRect(x, y, z, 0.04D, height, new Color(0, 0, 0, 255), 255, 63);
      RenderUtil3D.drawRect(x, y, z, 0.04D, height * ((double) player.getHealth() / 36.0D), getHealthColor((int) player.getHealth()), 255, 63);
      GlStateManager.popMatrix();
   }

   private static Color getHealthColor(int health) {
      if (health > 36) {
         health = 36;
      }

      if (health < 0) {
         health = 0;
      }

      int red;   // ここで宣言
      int green; // ここで宣言
      if (health > 18) {
         red = (int) ((double) (36 - health) * 14.1666666667D);
         green = 255;
      } else {
         red = 255;
         green = (int) (255.0D - (double) (18 - health) * 14.1666666667D);
      }

      return new Color(red, green, 0, 255);
   }
}
//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.mixin.mixins;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.event.events.render.RenderEntityModelEvent;
import me.hypinohaizin.candyplusrewrite.module.render.CandyCrystal;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
   @Redirect(
      method = {"doRender"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"
)
   )
   public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      CandyCrystal candycrystal = (CandyCrystal)CandyPlusRewrite.m_module.getModuleWithClass(CandyCrystal.class);
      if (candycrystal != null) {
         GlStateManager.scale((Float)candycrystal.scale.getValue(), (Float)candycrystal.scale.getValue(), (Float)candycrystal.scale.getValue());
         if (candycrystal.isEnable && (Boolean)candycrystal.wireframe.getValue()) {
            RenderEntityModelEvent event = new RenderEntityModelEvent(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            MinecraftForge.EVENT_BUS.post(event);
         }

         if (candycrystal.isEnable && (Boolean)candycrystal.chams.getValue()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5F);
            GL11.glEnable(2960);
            Color visibleColor2;
            Color hiddenColor;
            if ((Boolean)candycrystal.rainbow.getValue()) {
               hiddenColor = (Boolean)candycrystal.colorSync.getValue() ? candycrystal.getCurrentColor() : new Color(RenderUtil.getRainbow((Integer)candycrystal.speed.getValue() * 100, 0, (float)(Integer)candycrystal.saturation.getValue() / 100.0F, (float)(Integer)candycrystal.brightness.getValue() / 100.0F));
               visibleColor2 = new Color(hiddenColor.getRed(), hiddenColor.getGreen(), hiddenColor.getBlue());
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glDisable(2929);
                  GL11.glDepthMask(false);
               }

               GL11.glEnable(10754);
               GL11.glColor4f((float)visibleColor2.getRed() / 255.0F, (float)visibleColor2.getGreen() / 255.0F, (float)visibleColor2.getBlue() / 255.0F, (float)(Integer)candycrystal.alpha.getValue() / 255.0F);
               model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glEnable(2929);
                  GL11.glDepthMask(true);
               }
            } else if ((Boolean)candycrystal.xqz.getValue() && (Boolean)candycrystal.throughWalls.getValue()) {
               hiddenColor = (Color)candycrystal.hiddenColor.getValue();
               visibleColor2 = (Color)candycrystal.color.getValue();
               Color visibleColor = new Color(visibleColor2.getRed(), visibleColor2.getGreen(), visibleColor2.getBlue(), visibleColor2.getAlpha());
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glDisable(2929);
                  GL11.glDepthMask(false);
               }

               GL11.glEnable(10754);
               GL11.glColor4f((float)hiddenColor.getRed() / 255.0F, (float)hiddenColor.getGreen() / 255.0F, (float)hiddenColor.getBlue() / 255.0F, (float)(Integer)candycrystal.alpha.getValue() / 255.0F);
               model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glEnable(2929);
                  GL11.glDepthMask(true);
               }

               GL11.glColor4f((float)visibleColor.getRed() / 255.0F, (float)visibleColor.getGreen() / 255.0F, (float)visibleColor.getBlue() / 255.0F, (float)(Integer)candycrystal.alpha.getValue() / 255.0F);
               model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
               visibleColor2 = (Boolean)candycrystal.colorSync.getValue() ? candycrystal.getCurrentColor() : (Color)candycrystal.color.getValue();
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glDisable(2929);
                  GL11.glDepthMask(false);
               }

               GL11.glEnable(10754);
               GL11.glColor4f((float)visibleColor2.getRed() / 255.0F, (float)visibleColor2.getGreen() / 255.0F, (float)visibleColor2.getBlue() / 255.0F, (float)(Integer)candycrystal.alpha.getValue() / 255.0F);
               model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               if ((Boolean)candycrystal.throughWalls.getValue()) {
                  GL11.glEnable(2929);
                  GL11.glDepthMask(true);
               }
            }

            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
            if ((Boolean)candycrystal.glint.getValue()) {
               GL11.glDisable(2929);
               GL11.glDepthMask(false);
               GlStateManager.enableAlpha();
               GlStateManager.color(1.0F, 0.0F, 0.0F, 0.13F);
               model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               GlStateManager.disableAlpha();
               GL11.glEnable(2929);
               GL11.glDepthMask(true);
            }
         } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
         }

         if (candycrystal.isEnable()) {
            GlStateManager.scale(1.0F / (Float)candycrystal.scale.getValue(), 1.0F / (Float)candycrystal.scale.getValue(), 1.0F / (Float)candycrystal.scale.getValue());
         }

      }
   }
}

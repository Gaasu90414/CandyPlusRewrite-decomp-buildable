//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.mixin.mixins;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.module.render.EnchantmentColor;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderItem.class})
public abstract class MixinRenderItem implements Util {
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

   @Shadow
   public abstract void renderModel(IBakedModel var1, int var2);

   private void renderEffect(IBakedModel model, CallbackInfo info) {
      int color = ColorUtil.toRGBA((Color)EnchantmentColor.INSTANCE.color.getValue());
      TextureManager textureManager = mc.getTextureManager();
      GlStateManager.depthMask(false);
      GlStateManager.depthFunc(514);
      GlStateManager.disableLighting();
      GlStateManager.blendFunc(SourceFactor.SRC_COLOR, DestFactor.ONE);
      textureManager.bindTexture(RES_ITEM_GLINT);
      GlStateManager.matrixMode(5890);
      GlStateManager.pushMatrix();
      GlStateManager.scale(8.0F, 8.0F, 8.0F);
      float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
      GlStateManager.translate(f, 0.0F, 0.0F);
      GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
      this.renderModel(model, color);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.scale(8.0F, 8.0F, 8.0F);
      float f2 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
      GlStateManager.translate(-f2, 0.0F, 0.0F);
      GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
      this.renderModel(model, color);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
      GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.enableLighting();
      GlStateManager.depthFunc(515);
      GlStateManager.depthMask(true);
      textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GlStateManager.popMatrix();
   }
}

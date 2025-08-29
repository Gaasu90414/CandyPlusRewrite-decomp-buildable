package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.HoleUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import net.minecraft.util.math.BlockPos;

public class HoleESP extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 12.0F, 1.0F));
   public Setting<Color> obby = this.register(new Setting("ObbyColor", new Color(230, 50, 50, 100)));
   public Setting<Color> bedrock = this.register(new Setting("BedrockColor", new Color(230, 150, 50, 100)));
   public Setting<HoleESP.type> renderType;
   public Setting<Boolean> outline;
   public Setting<Float> width;

   public HoleESP() {
      super("HoleESP", Module.Categories.RENDER, false, false);
      this.renderType = this.register(new Setting("RenderType", HoleESP.type.Down));
      this.outline = this.register(new Setting("Outline", false));
      this.width = this.register(new Setting("Width", 3.0F, 6.0F, 0.1F, (v) -> {
         return (Boolean)this.outline.getValue();
      }));
   }

   public void onRender3D() {
      try {
         List<BlockPos> holes = CandyPlusRewrite.m_hole.getHoles();
         Iterator var2 = holes.iterator();

         while(var2.hasNext()) {
            BlockPos hole = (BlockPos)var2.next();
            if (!(PlayerUtil.getDistance(hole) > (double)(Float)this.range.getValue())) {
               Color color = (Color)this.obby.getValue();
               if (HoleUtil.isBedrockHole(hole)) {
                  color = (Color)this.bedrock.getValue();
               }

               if (this.renderType.getValue() == HoleESP.type.Full) {
                  RenderUtil3D.drawBox(hole, 1.0D, color, 63);
               } else {
                  RenderUtil3D.drawBox(hole, 1.0D, color, 1);
               }

               if ((Boolean)this.outline.getValue()) {
                  RenderUtil3D.drawBoundingBox(hole, 1.0D, (Float)this.width.getValue(), color);
               }
            }
         }
      } catch (Exception var5) {
      }

   }

   public static enum type {
      Full,
      Down;
   }
}

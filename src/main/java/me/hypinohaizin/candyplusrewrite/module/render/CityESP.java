//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CityESP extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 12.0F, 1.0F));
   public Setting<Color> color = this.register(new Setting("Color", new Color(230, 50, 50, 100)));
   public Setting<Boolean> outline = this.register(new Setting("Outline", false));
   public Setting<Float> width = this.register(new Setting("Width", 3.0F, 6.0F, 0.1F, (v) -> {
      return (Boolean)this.outline.getValue();
   }));

   public CityESP() {
      super("CityESP", Module.Categories.RENDER, false, false);
   }

   public void onRender3D() {
      List<EntityPlayer> players = (List)mc.world.playerEntities.stream().filter((e) -> {
         return e.getEntityId() != mc.player.getEntityId();
      }).collect(Collectors.toList());
      Iterator var2 = players.iterator();

      while(var2.hasNext()) {
         EntityPlayer player = (EntityPlayer)var2.next();
         BlockPos[] array = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
         BlockPos[] var6 = array;
         int var7 = array.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockPos offset = var6[var8];
            BlockPos position = (new BlockPos(player.posX, player.posY, player.posZ)).add(offset);
            if (PlayerUtil.getDistance(position) <= (double)(Float)this.range.getValue() && BlockUtil.getBlock(position) == Blocks.OBSIDIAN) {
               RenderUtil3D.drawBox(position, 1.0D, (Color)this.color.getValue(), 63);
               if ((Boolean)this.outline.getValue()) {
                  RenderUtil3D.drawBoundingBox(position, 1.0D, (Float)this.width.getValue(), (Color)this.color.getValue());
               }
            }
         }
      }

   }
}

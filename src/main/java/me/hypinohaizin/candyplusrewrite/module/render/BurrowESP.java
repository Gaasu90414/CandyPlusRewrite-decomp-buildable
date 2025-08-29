//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BurrowESP extends Module {
   public Setting<Boolean> obby = this.register(new Setting("Only Obby", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 64, 207, 210)));

   public BurrowESP() {
      super("BurrowESP", Module.Categories.RENDER, false, false);
   }

   public void onRender3D() {
      List<EntityPlayer> players = new ArrayList(mc.world.playerEntities);
      Iterator var2 = players.iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               EntityPlayer player = (EntityPlayer)var2.next();
               pos = new BlockPos(player.posX, player.posY, player.posZ);
            } while(BlockUtil.getBlock(pos) == Blocks.AIR);
         } while((Boolean)this.obby.getValue() && BlockUtil.getBlock(pos) != Blocks.OBSIDIAN);

         RenderUtil3D.drawBox(pos, 1.0D, (Color)this.color.getValue(), 63);
      }
   }
}

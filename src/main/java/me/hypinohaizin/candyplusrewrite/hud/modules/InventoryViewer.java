//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryViewer extends Hud {
   public InventoryViewer() {
      super("InventoryViewer", 150.0F, 100.0F);
   }

   public void onRender() {
      if (!this.nullCheck()) {
         RenderUtil.drawRect((Float)this.x.getValue() - 6.0F, (Float)this.y.getValue() - 6.0F, 180.0F, 72.0F, ColorUtil.toRGBA(0, 0, 0));
         RenderUtil.drawRect((Float)this.x.getValue() - 5.0F, (Float)this.y.getValue() - 5.0F, 180.0F, 69.0F, ColorUtil.toRGBA(40, 40, 40));
         float _x = 0.0F;
         float _y = 0.0F;
         int c = 0;
         boolean scale = true;
         InventoryPlayer inv = mc.player.inventory;

         for(int i = 9; i < 36; ++i) {
            ItemStack item = inv.getStackInSlot(i);
            RenderUtil.renderItem(item, (Float)this.x.getValue() + _x + 3.0F, (Float)this.y.getValue() + _y + 3.0F);
            _x += 19.0F;
            ++c;
            if (c == 9) {
               _x = 0.0F;
               _y += 19.0F;
               c = 0;
            }
         }

         this.width = 168.0F;
         this.height = 60.0F;
      }
   }
}

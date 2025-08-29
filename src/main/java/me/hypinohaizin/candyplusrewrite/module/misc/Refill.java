//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.item.ItemStack;

public class Refill extends Module {
   public Setting<Float> delay = this.register(new Setting("Delay", 1.0F, 10.0F, 0.0F));
   public Timer timer = new Timer();

   public Refill() {
      super("Refill", Module.Categories.MISC, false, false);
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         if (this.timer == null) {
            this.timer = new Timer();
         }

         if (this.timer.passedDms((double)(Float)this.delay.getValue())) {
            this.timer.reset();

            for(int i = 0; i < 9; ++i) {
               ItemStack itemstack = (ItemStack)mc.player.inventory.mainInventory.get(i);
               if (!itemstack.isEmpty() && itemstack.isStackable() && itemstack.getCount() < itemstack.getMaxStackSize() && this.doRefill(itemstack)) {
                  break;
               }
            }
         }

      }
   }

   public boolean doRefill(ItemStack stack) {
      for(int i = 9; i < 36; ++i) {
         ItemStack item = mc.player.inventory.getStackInSlot(i);
         if (this.CanItemBeMergedWith(item, stack)) {
            InventoryUtil.moveItem(i);
            return true;
         }
      }

      return false;
   }

   private boolean CanItemBeMergedWith(ItemStack p_Source, ItemStack p_Target) {
      return p_Source.getItem() == p_Target.getItem() && p_Source.getDisplayName().equals(p_Target.getDisplayName());
   }
}

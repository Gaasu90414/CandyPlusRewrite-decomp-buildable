//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AutoTotem extends Module {
   public Setting<Boolean> packet = this.register(new Setting("Packet", true));
   public Setting<Boolean> doublE = this.register(new Setting("Double", true));

   public AutoTotem() {
      super("AutoTotem", Module.Categories.COMBAT, false, false);
   }

   public void onTotemPop(EntityPlayer player) {
      if (player.getEntityId() == mc.player.getEntityId()) {
         if ((Boolean)this.packet.getValue()) {
            this.doTotem();
         }

      }
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if (this.shouldTotem()) {
            this.doTotem();
         }

      }
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         if (this.shouldTotem() && (Boolean)this.doublE.getValue()) {
            this.doTotem();
         }

      }
   }

   public void doTotem() {
      int totem = this.findTotemSlot();
      if (totem != -1) {
         InventoryUtil.moveItemTo(totem, InventoryUtil.offhandSlot);
      }
   }

   public boolean shouldTotem() {
      return mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING;
   }

   public int findTotemSlot() {
      for(int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
         if (i != InventoryUtil.offhandSlot) {
            ItemStack stack = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
               return i;
            }
         }
      }

      return -1;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoMend;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoDrop extends Module {
   public Setting<Boolean> sword = this.register(new Setting("Sword", true));
   public Setting<Boolean> bow = this.register(new Setting("Bow", true));
   public Setting<Boolean> pickel = this.register(new Setting("Pickel", true));
   public Setting<Boolean> armor = this.register(new Setting("DamageArmor", true));
   public Setting<Boolean> autoMend = this.register(new Setting("PauseWhenAutoMend", true, (v) -> {
      return (Boolean)this.armor.getValue();
   }));
   public Setting<Float> damege = this.register(new Setting("MinDamege", 50.0F, 100.0F, 0.0F, (v) -> {
      return (Boolean)this.armor.getValue();
   }));
   public Setting<Float> delay = this.register(new Setting("Delay", 1.0F, 10.0F, 0.0F));
   public Timer timer = new Timer();

   public AutoDrop() {
      super("AutoDrop", Module.Categories.MISC, false, false);
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         if (this.timer == null) {
            this.timer = new Timer();
         }

         if (this.timer.passedDms((double)(Float)this.delay.getValue())) {
            this.timer.reset();

            for(int i = 9; i < 36; ++i) {
               ItemStack itemStack = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
               Item item = itemStack.getItem();
               if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) {
                  if (item instanceof ItemSword && (Boolean)this.sword.getValue()) {
                     InventoryUtil.dropItem(i);
                     break;
                  }

                  if (item instanceof ItemBow && (Boolean)this.bow.getValue()) {
                     InventoryUtil.dropItem(i);
                     break;
                  }

                  if (item instanceof ItemPickaxe && (Boolean)this.pickel.getValue()) {
                     InventoryUtil.dropItem(i);
                     break;
                  }

                  if (item instanceof ItemArmor && (Boolean)this.armor.getValue()) {
                     Module automend = CandyPlusRewrite.m_module.getModuleWithClass(AutoMend.class);
                     if ((!automend.isEnable || !(Boolean)this.autoMend.getValue()) && this.getDamage(itemStack) <= (Float)this.damege.getValue()) {
                        InventoryUtil.dropItem(i);
                        break;
                     }
                  }
               }
            }
         }

      }
   }

   public float getDamage(ItemStack itemStack) {
      return (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()) / ((float)itemStack.getMaxDamage() * 1.0F) * 100.0F;
   }
}

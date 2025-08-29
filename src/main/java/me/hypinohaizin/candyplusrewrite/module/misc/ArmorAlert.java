//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.HashMap;
import java.util.Map;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ArmorAlert extends Module {
   public Setting<Float> alertPercent = this.register(new Setting("AlertPercent", 20.0F, 100.0F, 1.0F));
   public Setting<Float> alertDelay = this.register(new Setting("AlertDelay", 2000.0F, 1000.0F, 60000.0F));
   private Map<EntityEquipmentSlot, Long> lastAlertTimes = new HashMap();

   public ArmorAlert() {
      super("ArmorAlert", Module.Categories.MISC, false, false);
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      if (mc.player != null) {
         long currentTime = System.currentTimeMillis();
         int alertPercentInt = ((Float)this.alertPercent.getValue()).intValue();
         int alertDelayInt = ((Float)this.alertDelay.getValue()).intValue();
         EntityEquipmentSlot[] var6 = EntityEquipmentSlot.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EntityEquipmentSlot slot = var6[var8];
            if (slot.getSlotType() == Type.ARMOR) {
               ItemStack armor = mc.player.getItemStackFromSlot(slot);
               if (!armor.isEmpty()) {
                  int maxDurability = armor.getMaxDamage();
                  int currentDamage = armor.getItemDamage();
                  int durabilityLeft = maxDurability - currentDamage;
                  int percent = durabilityLeft * 100 / maxDurability;
                  long lastAlertTime = (Long)this.lastAlertTimes.getOrDefault(slot, 0L);
                  if (percent <= alertPercentInt && currentTime - lastAlertTime >= (long)alertDelayInt) {
                     this.sendMessage(slot.name() + " armor durability is low: " + percent + "%");
                     this.lastAlertTimes.put(slot, currentTime);
                  }
               }
            }
         }

      }
   }
}

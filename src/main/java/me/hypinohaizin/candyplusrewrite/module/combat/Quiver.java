//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;

public class Quiver extends Module {
   private final Setting<Integer> tickDelay = this.register(new Setting("TickDelay", 3, 0, 8));
   private int lastSlot = -1;

   public Quiver() {
      super("Quiver", Module.Categories.COMBAT, false, false);
   }

   public void onUpdate() {
      if (mc.player != null) {
         if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= (Integer)this.tickDelay.getValue()) {
            mc.player.connection.sendPacket(new Rotation(mc.player.rotationYaw, -90.0F, mc.player.onGround));
            mc.playerController.onStoppedUsingItem(mc.player);
         }

         List<Integer> arrowSlots = this.getItemInventory(Items.TIPPED_ARROW);
         if (arrowSlots.size() != 1 || (Integer)arrowSlots.get(0) != -1) {
            int speedSlot = -1;
            boolean strengthSlot = true;
            Iterator var4 = arrowSlots.iterator();

            while(var4.hasNext()) {
               int slot = (Integer)var4.next();
               ResourceLocation loc = PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName();
               if (loc != null) {
                  String path = loc.getPath();
                  if (path.contains("swiftness")) {
                     speedSlot = slot;
                  } else if (path.contains("strength")) {
                     ;
                  }
               }
            }

            if (speedSlot != -1) {
               this.silentSwitchTo(speedSlot);
               mc.playerController.onStoppedUsingItem(mc.player);
               this.restoreSwitch();
            }

         }
      }
   }

   private List<Integer> getItemInventory(Item item) {
      List<Integer> slots = new ArrayList();

      for(int i = 9; i < 36; ++i) {
         if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
            slots.add(i);
         }
      }

      if (slots.isEmpty()) {
         slots.add(-1);
      }

      return slots;
   }

   private void silentSwitchTo(int slot) {
      if (slot >= 0) {
         this.lastSlot = mc.player.inventory.currentItem;
         mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
      }
   }

   private void restoreSwitch() {
      if (this.lastSlot != -1) {
         mc.player.connection.sendPacket(new CPacketHeldItemChange(this.lastSlot));
         this.lastSlot = -1;
      }

   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;

public class AutoXP extends Module {
   public Setting<Boolean> packetThrow = this.register(new Setting("PacketThrow", true));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", true));
   public Setting<Float> delay = this.register(new Setting("Delay", 0.0F, 25.0F, 0.0F));
   public int oldSlot = -1;
   public EnumHand oldHand = null;
   public Timer timer = new Timer();

   public AutoXP() {
      super("AutoXP", Module.Categories.COMBAT, false, false);
   }

   public void onEnable() {
      this.timer = new Timer();
   }

   public void onTick() {
      if (!this.nullCheck()) {
         int xp = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
         if (xp == -1) {
            this.restoreItem();
         } else {
            if (this.timer.passedX((double)(Float)this.delay.getValue())) {
               this.timer.reset();
               this.setItem(xp);
               mc.player.connection.sendPacket(new Rotation(0.0F, 90.0F, true));
               if ((Boolean)this.packetThrow.getValue()) {
                  mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
               } else {
                  mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
               }
            }

            this.restoreItem();
         }
      }
   }

   public void setItem(int slot) {
      if ((Boolean)this.silentSwitch.getValue()) {
         this.oldHand = null;
         if (mc.player.isHandActive()) {
            this.oldHand = mc.player.getActiveHand();
         }

         this.oldSlot = mc.player.inventory.currentItem;
         mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
      } else {
         mc.player.inventory.currentItem = slot;
         mc.playerController.updateController();
      }

   }

   public void restoreItem() {
      if (this.oldSlot != -1 && (Boolean)this.silentSwitch.getValue()) {
         if (this.oldHand != null) {
            mc.player.setActiveHand(this.oldHand);
         }

         mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
         this.oldSlot = -1;
         this.oldHand = null;
      }

   }
}

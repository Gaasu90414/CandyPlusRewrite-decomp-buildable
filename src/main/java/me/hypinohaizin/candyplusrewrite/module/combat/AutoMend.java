//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class AutoMend extends Module {
   public Setting<Float> delay = this.register(new Setting("Delay", 3.0F, 10.0F, 0.0F));
   public Setting<Float> armor = this.register(new Setting("ArmorDelay", 3.0F, 20.0F, 0.0F));
   public Setting<Float> pct = this.register(new Setting("Pct", 80.0F, 100.0F, 10.0F));
   public Setting<Boolean> press = this.register(new Setting("Press", true));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", true));
   public boolean toggleoff = false;
   public Map<Integer, Integer> armors = new HashMap();
   public Timer xpTimer;
   public Timer armorTimer = new Timer();
   private EnumHand oldhand = null;
   private int oldslot = -1;

   public AutoMend() {
      super("AutoMend", Module.Categories.COMBAT, false, false);
      this.disable();
   }

   public void onEnable() {
      this.xpTimer = new Timer();
      this.armorTimer = new Timer();
   }

   public void onDisable() {
      if (!this.toggleoff) {
         this.toggleoff = true;
         this.armors = new HashMap();
         this.enable();
      } else {
         this.toggleoff = false;
      }
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if (!this.toggleoff) {
            if (this.armorTimer == null) {
               this.armorTimer = new Timer();
            }

            this.armorTimer.reset();
            int xp = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
            if (xp == -1) {
               this.sendMessage("Cannot find XP! disabling");
               this.setDisable();
               return;
            }

            if (this.isDone()) {
               this.setDisable();
               return;
            }

            if (this.xpTimer == null) {
               this.xpTimer = new Timer();
            }

            if (this.xpTimer.passedX((double)(Float)this.delay.getValue())) {
               this.xpTimer.reset();
               this.setItem(xp);
               mc.player.connection.sendPacket(new Rotation(0.0F, 90.0F, false));
               mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
               this.restoreItem();
            }

            if (this.key.key != -1 && (Boolean)this.press.getValue() && !Keyboard.isKeyDown(this.key.key)) {
               this.setDisable();
            }
         } else {
            if (this.armorTimer == null) {
               this.armorTimer = new Timer();
            }

            if (this.armorTimer.passedDms((double)(Float)this.armor.getValue())) {
               this.armorTimer.reset();
               AtomicInteger c = new AtomicInteger();
               AtomicInteger key = new AtomicInteger();
               this.armors.forEach((k, v) -> {
                  if (c.get() == 0) {
                     InventoryUtil.moveItemTo(k, v);
                     key.set(k);
                  }

                  c.getAndIncrement();
               });
               if (c.get() == 0) {
                  this.disable();
               } else {
                  this.armors.remove(key.get());
               }
            }
         }

      }
   }

   public void onPacketSend(PacketEvent.Send event) {
      if (event.getPacket() instanceof CPacketPlayer) {
         CPacketPlayer var2 = (CPacketPlayer)event.getPacket();
      }

   }

   public void setDisable() {
      this.toggleoff = true;
   }

   public void moveArmorToSlot(int armor, int empty) {
      InventoryUtil.moveItemTo(armor, empty);
      this.armors.put(empty, armor);
   }

   public boolean isDone() {
      boolean done = true;

      for(int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
         ItemStack itemStack = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
         if (i > 4 && i < 9 && !itemStack.isEmpty()) {
            if (this.getRate(itemStack) < (Float)this.pct.getValue()) {
               done = false;
            } else {
               int slot = this.getFreeSlot();
               if (slot != -1) {
                  this.moveArmorToSlot(i, slot);
               }
            }
         }
      }

      return done;
   }

   public int getFreeSlot() {
      for(int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
         if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 7 && i != 8) {
            ItemStack stack = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
            if (stack.isEmpty() || stack.getItem() == Items.AIR) {
               return i;
            }
         }
      }

      return -1;
   }

   public float getRate(ItemStack itemStack) {
      return (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()) / ((float)itemStack.getMaxDamage() * 1.0F) * 100.0F;
   }

   public void setItem(int slot) {
      if ((Boolean)this.silentSwitch.getValue()) {
         this.oldhand = null;
         if (mc.player.isHandActive()) {
            this.oldhand = mc.player.getActiveHand();
         }

         this.oldslot = mc.player.inventory.currentItem;
         mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
      } else {
         mc.player.inventory.currentItem = slot;
         mc.playerController.updateController();
      }

   }

   public void restoreItem() {
      if (this.oldslot != -1 && (Boolean)this.silentSwitch.getValue()) {
         if (this.oldhand != null) {
            mc.player.setActiveHand(this.oldhand);
         }

         mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
         this.oldslot = -1;
         this.oldhand = null;
      }

   }
}

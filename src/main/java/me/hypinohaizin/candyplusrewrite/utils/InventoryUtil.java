//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil implements Util {
   public static int offhandSlot = 45;
   public static final int HOTBAR_SIZE = 9;
   public static final int INVENTORY_SIZE = 36;
   public static final int ARMOR_SIZE = 4;

   public static void switchToHotbarSlot(int slot, boolean silent) {
      if (mc.player.inventory.currentItem != slot && slot >= 0) {
         if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
         } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
         }

      }
   }

   public static int findHotbarBlock(Block blockIn) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block == blockIn) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int findHotbarItem(Item item) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   public static int findHotbarBlockWithClass(Class<?> clazz) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block.getClass().equals(clazz)) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int getItemHotbar(Item input) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() == input) {
            return i;
         }
      }

      return -1;
   }

   public static int findItem(Item item) {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   public static int findBlock(Block block) {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
            Block foundBlock = ((ItemBlock)stack.getItem()).getBlock();
            if (foundBlock == block) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int findItemAnywhere(Item item) {
      int slot = getItemHotbar(item);
      return slot != -1 ? slot : findItem(item);
   }

   public static int findBlockAnywhere(Block block) {
      int slot = findHotbarBlock(block);
      return slot != -1 ? slot : findBlock(block);
   }

   public static int getItemCount(Item item) {
      int count = 0;

      for(int i = 0; i < 36; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() == item) {
            count += stack.getCount();
         }
      }

      return count;
   }

   public static int getBlockCount(Block block) {
      int count = 0;

      for(int i = 0; i < 36; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
            Block foundBlock = ((ItemBlock)stack.getItem()).getBlock();
            if (foundBlock == block) {
               count += stack.getCount();
            }
         }
      }

      return count;
   }

   public static int findEmptyHotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (stack.isEmpty()) {
            return i;
         }
      }

      return -1;
   }

   public static int findEmptySlot() {
      for(int i = 9; i < 36; ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (stack.isEmpty()) {
            return i;
         }
      }

      return -1;
   }

   public static boolean hasItemInOffhand(Item item) {
      ItemStack offhandStack = mc.player.getHeldItemOffhand();
      return !offhandStack.isEmpty() && offhandStack.getItem() == item;
   }

   public static boolean hasBlockInOffhand(Block block) {
      ItemStack offhandStack = mc.player.getHeldItemOffhand();
      if (!offhandStack.isEmpty() && offhandStack.getItem() instanceof ItemBlock) {
         Block offhandBlock = ((ItemBlock)offhandStack.getItem()).getBlock();
         return offhandBlock == block;
      } else {
         return false;
      }
   }

   public static boolean moveToHotbar(int sourceSlot) {
      int emptySlot = findEmptyHotbar();
      if (emptySlot != -1) {
         moveItemTo(sourceSlot, emptySlot);
         return true;
      } else {
         return false;
      }
   }

   public static int getWindowSlot(int slot) {
      if (slot < 9) {
         return slot + 36;
      } else if (slot < 36) {
         return slot;
      } else {
         return slot == offhandSlot ? 45 : slot;
      }
   }

   public static boolean safeItemMove(int sourceSlot, int targetSlot) {
      try {
         if (sourceSlot >= 0 && targetSlot >= 0 && sourceSlot < 37 && targetSlot < 37) {
            moveItemTo(sourceSlot, targetSlot);
            return true;
         } else {
            return false;
         }
      } catch (Exception var3) {
         return false;
      }
   }

   public static void moveItemTo(int item, int empty) {
      int windowItem = getWindowSlot(item);
      int windowEmpty = getWindowSlot(empty);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowItem, 0, ClickType.PICKUP, mc.player);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowEmpty, 0, ClickType.PICKUP, mc.player);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowItem, 0, ClickType.PICKUP, mc.player);
      mc.playerController.updateController();
   }

   public static void moveItem(int item) {
      int windowSlot = getWindowSlot(item);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowSlot, 0, ClickType.QUICK_MOVE, mc.player);
      mc.playerController.updateController();
   }

   public static void dropItem(int item) {
      int windowSlot = getWindowSlot(item);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowSlot, 0, ClickType.THROW, mc.player);
      mc.playerController.updateController();
   }

   public static void dropStack(int item) {
      int windowSlot = getWindowSlot(item);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, windowSlot, 1, ClickType.THROW, mc.player);
      mc.playerController.updateController();
   }

   public static boolean isItemDamaged(int slot, double threshold) {
      ItemStack stack = mc.player.inventory.getStackInSlot(slot);
      if (!stack.isEmpty() && stack.isItemStackDamageable()) {
         double durabilityPercentage = (double)(stack.getMaxDamage() - stack.getItemDamage()) / (double)stack.getMaxDamage();
         return durabilityPercentage < threshold;
      } else {
         return false;
      }
   }

   public static boolean isHolding(Item item) {
      return isHolding(mc.player, item);
   }

   public static boolean isHolding(EntityLivingBase entity, Item item) {
      ItemStack mainHand = entity.getHeldItemMainhand();
      ItemStack offHand = entity.getHeldItemOffhand();
      return areSame(mainHand, item) || areSame(offHand, item);
   }

   public static void click(int slot) {
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
   }

   public static ItemStack get(int slot) {
      return slot == -2 ? mc.player.inventory.getItemStack() : (ItemStack)mc.player.inventoryContainer.getInventory().get(slot);
   }

   public static void put(int slot, ItemStack stack) {
      if (slot == -2) {
         mc.player.inventory.setItemStack(stack);
      }

      mc.player.inventoryContainer.putStackInSlot(slot, stack);
   }

   public static boolean validScreen() {
      return !(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof GuiInventory;
   }

   public static boolean equals(ItemStack stack1, ItemStack stack2) {
      if (stack1 == null) {
         return stack2 == null;
      } else if (stack2 == null) {
         return false;
      } else {
         boolean empty1 = stack1.isEmpty();
         boolean empty2 = stack2.isEmpty();
         return empty1 == empty2 && stack1.getDisplayName().equals(stack2.getDisplayName()) && stack1.getItem() == stack2.getItem() && stack1.getHasSubtypes() == stack2.getHasSubtypes() && stack1.getMetadata() == stack2.getMetadata() && ItemStack.areItemStackTagsEqual(stack1, stack2);
      }
   }

   public static void clickLocked(int slot, int to, Item inSlot, Item inTo) {
      if ((slot == -1 || get(slot).getItem() == inSlot) && get(to).getItem() == inTo) {
         click(to);
      }

   }

   public static void switchTo(int slot) {
      if (mc.player.inventory.currentItem != slot && slot > -1 && slot < 9) {
         mc.player.inventory.currentItem = slot;
         syncItem();
      }

   }

   public static void switchToAlt(int slot) {
      int altSlot = hotbarToInventory(slot);
      if (mc.player.inventory.currentItem != altSlot && altSlot > 35 && altSlot < 45) {
         mc.playerController.windowClick(0, altSlot, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
      }

   }

   public static boolean areSame(ItemStack stack, Item item) {
      return stack != null && Item.getIdFromItem(stack.getItem()) == Item.getIdFromItem(item);
   }

   public static int findInHotbar(Predicate<ItemStack> condition) {
      for(int i = 8; i >= 0; --i) {
         if (condition.test(mc.player.inventory.getStackInSlot(i)) && mc.player.inventory.currentItem == i) {
            return i;
         }
      }

      return -1;
   }

   public static void syncItem() {
      mc.playerController.updateController();
   }

   public static int hotbarToInventory(int slot) {
      if (slot == -2) {
         return 45;
      } else {
         return slot > -1 && slot < 9 ? 36 + slot : slot;
      }
   }

   public static boolean isFood(ItemStack stack) {
      return stack.getItem() instanceof ItemFood;
   }

   public static double getDamageInPercent(ItemStack stack) {
      double percent = (double)stack.getItemDamage() / (double)stack.getMaxDamage();
      if (percent == 0.0D) {
         return 100.0D;
      } else {
         return percent == 1.0D ? 0.0D : 100.0D - percent * 100.0D;
      }
   }

   public static enum SwapEnum {
      SILENT,
      ALTERNATIVE,
      NONE;
   }
}

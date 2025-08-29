//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleFill extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 6.0F, 12.0F, 1.0F));
   public Setting<Integer> place = this.register(new Setting("Place", 2, 10, 1));
   public Setting<Boolean> toggle = this.register(new Setting("Toggle", false));
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", false));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", false));
   public Setting<Boolean> autoMode = this.register(new Setting("AutoMode", false));
   public Setting<Float> detectRange = this.register(new Setting("DetectRange", 4.0F, 10.0F, 1.0F));
   public Setting<Integer> placeIntervalMs = this.register(new Setting("PlaceInterval", 75, 300, 0));
   private EnumHand oldhand = null;
   private int oldslot = -1;
   private final Timer placeTimer = new Timer();

   public HoleFill() {
      super("HoleFill", Module.Categories.COMBAT, false, false);
   }

   public void onTick() {
      if (!this.nullCheck()) {
         int slot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (slot == -1) {
            this.restoreItem();
            if ((Boolean)this.toggle.getValue()) {
               this.disable();
            }

         } else {
            List holes;
            if ((Boolean)this.autoMode.getValue()) {
               holes = (List)CandyPlusRewrite.m_hole.getHoles().stream().filter((h) -> {
                  return PlayerUtil.getDistance(h) < (double)(Float)this.range.getValue();
               }).filter((h) -> {
                  return !isAnyEntityInBlock(h);
               }).filter(this::isPlaceable).filter((h) -> {
                  return !this.isFriendNear(h, (double)(Float)this.detectRange.getValue());
               }).filter((h) -> {
                  Iterator var2 = mc.world.playerEntities.iterator();

                  while(var2.hasNext()) {
                     EntityPlayer p = (EntityPlayer)var2.next();
                     if (p != mc.player && !FriendManager.isFriend(p)) {
                        double dx = (double)h.getX() + 0.5D;
                        double dy = (double)h.getY() + 0.5D;
                        double dz = (double)h.getZ() + 0.5D;
                        if (p.getDistance(dx, dy, dz) < (double)(Float)this.detectRange.getValue()) {
                           return true;
                        }
                     }
                  }

                  return false;
               }).collect(Collectors.toList());
            } else {
               holes = (List)CandyPlusRewrite.m_hole.getHoles().stream().filter((h) -> {
                  return PlayerUtil.getDistance(h) < (double)(Float)this.range.getValue();
               }).filter((h) -> {
                  return !isAnyEntityInBlock(h);
               }).filter(this::isPlaceable).filter((h) -> {
                  return !this.isFriendNear(h, (double)(Float)this.detectRange.getValue());
               }).collect(Collectors.toList());
            }

            int counter = 0;
            if (!holes.isEmpty()) {
               this.setItem(slot);
            }

            Iterator var4 = holes.iterator();

            while(var4.hasNext()) {
               BlockPos hole = (BlockPos)var4.next();
               if (counter >= (Integer)this.place.getValue()) {
                  break;
               }

               if (this.placeTimer.passedMs((long)(Integer)this.placeIntervalMs.getValue()) && this.isPlaceable(hole) && !this.isFriendNear(hole, (double)(Float)this.detectRange.getValue())) {
                  BlockUtil.placeBlock(hole, (Boolean)this.packetPlace.getValue());
                  this.placeTimer.reset();
                  ++counter;
               }
            }

            if (!(Boolean)this.autoMode.getValue() && holes.isEmpty() && (Boolean)this.toggle.getValue()) {
               this.restoreItem();
               this.disable();
            } else {
               this.restoreItem();
            }
         }
      }
   }

   public boolean isPlaceable(BlockPos pos) {
      if (!mc.world.isAirBlock(pos) && !mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
         return false;
      } else {
         return !isAnyEntityInBlock(pos);
      }
   }

   public static boolean isAnyEntityInBlock(BlockPos pos) {
      AxisAlignedBB box = new AxisAlignedBB(pos);
      List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, box);
      Iterator var3 = list.iterator();

      Entity e;
      do {
         do {
            if (!var3.hasNext()) {
               return false;
            }

            e = (Entity)var3.next();
         } while(e instanceof EntityPlayer && e == mc.player);
      } while(e.isDead || !e.getEntityBoundingBox().intersects(box));

      return true;
   }

   private boolean isFriendNear(BlockPos pos, double range) {
      double cx = (double)pos.getX() + 0.5D;
      double cy = (double)pos.getY() + 0.5D;
      double cz = (double)pos.getZ() + 0.5D;
      Iterator var10 = mc.world.playerEntities.iterator();

      EntityPlayer p;
      do {
         if (!var10.hasNext()) {
            return false;
         }

         p = (EntityPlayer)var10.next();
      } while(p == mc.player || !FriendManager.isFriend(p) || !(p.getDistance(cx, cy, cz) <= range));

      return true;
   }

   public void setItem(int slot) {
      if (slot >= 0 && slot <= 8) {
         if ((Boolean)this.silentSwitch.getValue()) {
            if (mc.player.inventory.currentItem == slot) {
               return;
            }

            this.oldhand = null;
            if (mc.player.isHandActive()) {
               this.oldhand = mc.player.getActiveHand();
            }

            if (this.oldslot == -1) {
               this.oldslot = mc.player.inventory.currentItem;
            }

            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
            if (mc.player.inventory.currentItem == slot) {
               return;
            }

            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
         }

      }
   }

   public void restoreItem() {
      if ((Boolean)this.silentSwitch.getValue()) {
         if (this.oldslot != -1) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
            this.oldslot = -1;
         }

         if (this.oldhand != null) {
            mc.player.setActiveHand(this.oldhand);
            this.oldhand = null;
         }

      }
   }
}

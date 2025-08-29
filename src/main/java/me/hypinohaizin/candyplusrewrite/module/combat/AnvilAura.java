//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AnvilAura extends Module {
   public Setting<Float> targetRange = this.register(new Setting("Target Range", 5.0F, 0.0F, 10.0F));
   public Setting<Float> placeRange = this.register(new Setting("Place Range", 5.0F, 0.0F, 10.0F));
   public Setting<Boolean> rotate = this.register(new Setting("Rotate", true));
   public Setting<Boolean> packet = this.register(new Setting("Packet", true));
   public Setting<Integer> placeDelay = this.register(new Setting("Place Delay", 100, 0, 1000));
   public Setting<Integer> breakDelay = this.register(new Setting("Break Delay", 0, 0, 500));
   public Setting<Integer> stackHeight = this.register(new Setting("Stack Height", 5, 1, 15));
   public Setting<Float> maxTargetSpeed = this.register(new Setting("Max Target Speed", 10.0F, 0.0F, 30.0F));
   public Setting<Boolean> instantBreak = this.register(new Setting("InstantBreak", true));
   public Setting<Boolean> packetBreak = this.register(new Setting("PacketBreak", true));
   public Setting<Boolean> autoPlace = this.register(new Setting("Auto Place", true));
   public Setting<Boolean> continuousBreak = this.register(new Setting("Continuous Break", true));
   public Timer placeTimer = new Timer();
   public Timer breakTimer = new Timer();
   public BlockPos currentTarget = null;
   public BlockPos breakingPos = null;
   public EntityPlayer targetPlayer = null;
   private int oldSlot = -1;
   private boolean isBreaking = false;

   public AnvilAura() {
      super("AnvilAura", Module.Categories.COMBAT, false, false);
   }

   public void onTick() {
      if (!this.nullCheck()) {
         this.targetPlayer = this.findTarget();
         if (this.targetPlayer == null) {
            this.reset();
         } else {
            BlockPos playerPos = new BlockPos(this.targetPlayer.posX, this.targetPlayer.posY, this.targetPlayer.posZ);
            BlockPos headPos = playerPos.up(2);
            if ((Boolean)this.autoPlace.getValue() && this.placeTimer.passedMs((long)(Integer)this.placeDelay.getValue())) {
               this.placeAnvilStack(headPos);
            }

            if ((Boolean)this.continuousBreak.getValue() && this.breakTimer.passedMs((long)(Integer)this.breakDelay.getValue())) {
               this.breakFeetAnvil(playerPos);
            }

         }
      }
   }

   private EntityPlayer findTarget() {
      EntityPlayer bestTarget = null;
      double bestDistance = Double.MAX_VALUE;
      Iterator var4 = mc.world.playerEntities.iterator();

      while(var4.hasNext()) {
         EntityPlayer player = (EntityPlayer)var4.next();
         if (player != mc.player && !player.isDead && !(player.getHealth() <= 0.0F) && !FriendManager.isFriend(player)) {
            double distance = PlayerUtil.getDistance((Entity)player);
            if (!(distance > (double)(Float)this.targetRange.getValue()) && !(this.getSpeed(player) > (double)(Float)this.maxTargetSpeed.getValue()) && distance < bestDistance) {
               bestDistance = distance;
               bestTarget = player;
            }
         }
      }

      return bestTarget;
   }

   private void placeAnvilStack(BlockPos headPos) {
      int anvilSlot = InventoryUtil.findHotbarBlockWithClass(BlockAnvil.class);
      if (anvilSlot != -1) {
         int obsidianSlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (obsidianSlot != -1) {
            this.placeSupportBlocks(headPos, obsidianSlot);
         }

         boolean needsPlacement = false;

         int i;
         BlockPos placePos;
         for(i = 0; i < (Integer)this.stackHeight.getValue(); ++i) {
            placePos = headPos.up(i);
            if (mc.world.isAirBlock(placePos) && this.canPlace(placePos)) {
               needsPlacement = true;
               break;
            }
         }

         if (needsPlacement) {
            this.oldSlot = mc.player.inventory.currentItem;
            InventoryUtil.switchToHotbarSlot(anvilSlot, false);

            for(i = 0; i < (Integer)this.stackHeight.getValue(); ++i) {
               placePos = headPos.up(i);
               if (mc.world.isAirBlock(placePos) && this.canPlace(placePos)) {
                  BlockUtil.placeBlock(placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue());
                  this.placeTimer.reset();
                  break;
               }
            }

            if (this.oldSlot != -1) {
               InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
            }

         }
      }
   }

   private void placeSupportBlocks(BlockPos basePos, int slot) {
      this.oldSlot = mc.player.inventory.currentItem;
      InventoryUtil.switchToHotbarSlot(slot, false);
      if (mc.world.isAirBlock(basePos) && !this.canPlace(basePos)) {
         BlockPos feetPos = new BlockPos(this.targetPlayer.posX, this.targetPlayer.posY, this.targetPlayer.posZ);
         int maxHeight = feetPos.getY() + 2;
         BlockPos[] horizontalOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
         BlockPos farthestOffset = null;
         double farthestDistance = -1.0D;
         BlockPos[] var9 = horizontalOffsets;
         int i = horizontalOffsets.length;

         for(int var11 = 0; var11 < i; ++var11) {
            BlockPos offset = var9[var11];
            BlockPos posToCheck = feetPos.add(offset);
            double distance = mc.player.getDistance((double)posToCheck.getX() + 0.5D, (double)posToCheck.getY(), (double)posToCheck.getZ() + 0.5D);
            if (distance > farthestDistance) {
               farthestDistance = distance;
               farthestOffset = offset;
            }
         }

         if (farthestOffset == null) {
            if (this.oldSlot != -1) {
               InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
            }

         } else {
            BlockPos supportPos = basePos.add(farthestOffset).down(1);

            for(i = 0; i < (Integer)this.stackHeight.getValue(); ++i) {
               BlockPos placePos = supportPos.up(i);
               if (placePos.getY() > maxHeight) {
                  break;
               }

               if (mc.world.isAirBlock(placePos) && this.canPlace(placePos)) {
                  BlockUtil.placeBlock(placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue());
                  this.placeTimer.reset();
                  break;
               }
            }

            if (this.oldSlot != -1) {
               InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
            }

         }
      } else {
         if (this.oldSlot != -1) {
            InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
         }

      }
   }

   private void breakFeetAnvil(BlockPos feetPos) {
      if (BlockUtil.getBlock(feetPos) instanceof BlockAnvil) {
         boolean hasAnvilsAbove = false;

         int pickaxeSlot;
         for(pickaxeSlot = 1; pickaxeSlot <= (Integer)this.stackHeight.getValue(); ++pickaxeSlot) {
            if (BlockUtil.getBlock(feetPos.up(pickaxeSlot)) instanceof BlockAnvil) {
               hasAnvilsAbove = true;
               break;
            }
         }

         if (hasAnvilsAbove) {
            this.breakingPos = feetPos;
            pickaxeSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
            if (pickaxeSlot != -1) {
               int currentSlot = mc.player.inventory.currentItem;
               InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);

               try {
                  Module instantMineModule = CandyPlusRewrite.m_module.getModuleWithClass(InstantMine.class);
                  if ((Boolean)this.instantBreak.getValue() && instantMineModule != null) {
                     if (!this.isBreaking) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        instantMineModule.enable();
                        InstantMine.startBreak(feetPos, EnumFacing.DOWN);
                        this.isBreaking = true;
                     }

                     if (mc.world.isAirBlock(feetPos)) {
                        this.isBreaking = false;
                        InstantMine.resetMining();
                     }
                  } else {
                     mc.player.swingArm(EnumHand.MAIN_HAND);
                     if ((Boolean)this.packetBreak.getValue()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
                     } else {
                        mc.playerController.clickBlock(feetPos, EnumFacing.DOWN);
                     }
                  }

                  this.breakTimer.reset();
               } catch (Exception var6) {
                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
                  mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
               }

               InventoryUtil.switchToHotbarSlot(currentSlot, false);
            }
         }
      }
   }

   private boolean canPlace(BlockPos pos) {
      if (!BlockUtil.canPlaceBlock(pos)) {
         return false;
      } else {
         return mc.player.getDistance((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D) <= (double)(Float)this.placeRange.getValue();
      }
   }

   private double getSpeed(EntityPlayer player) {
      double dx = player.posX - player.prevPosX;
      double dz = player.posZ - player.prevPosZ;
      return Math.sqrt(dx * dx + dz * dz) * 20.0D;
   }

   private void reset() {
      this.currentTarget = null;
      this.breakingPos = null;
      this.targetPlayer = null;
      this.isBreaking = false;

      try {
         InstantMine.resetMining();
      } catch (Exception var2) {
      }

   }

   public void onDisable() {
      this.reset();
      if (this.oldSlot != -1) {
         InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
         this.oldSlot = -1;
      }

   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.module.exploit.SilentPickel;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.CrystalUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CevBreaker extends Module {
   public Setting<Float> preDelay = this.register(new Setting("BlockDelay", 0.0F, 20.0F, 0.0F));
   public Setting<Float> crystalDelay = this.register(new Setting("CrystalDelay", 0.0F, 20.0F, 0.0F));
   public Setting<Float> breakDelay = this.register(new Setting("BreakDelay", 0.0F, 20.0F, 0.0F));
   public Setting<Float> attackDelay = this.register(new Setting("AttackDelay", 3.0F, 20.0F, 0.0F));
   public Setting<Float> endDelay = this.register(new Setting("EndDelay", 0.0F, 20.0F, 0.0F));
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
   public Setting<Boolean> tick = this.register(new Setting("Tick", true));
   public Setting<Boolean> toggle = this.register(new Setting("Toggle", true));
   public Setting<Boolean> noSwingBlock = this.register(new Setting("NoSwingBlock", true));
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", false));
   public Setting<Boolean> packetCrystal = this.register(new Setting("PacketCrystal", true));
   public Setting<Boolean> instantBreak = this.register(new Setting("InstantBreak", false));
   public Setting<Boolean> toggleSilentPickel = this.register(new Setting("ToggleSilentPickel", false));
   public Setting<Boolean> packetBreak = this.register(new Setting("PacketBreak", true));
   public Setting<Boolean> offHandBreak = this.register(new Setting("OffhandBreak", true));
   public Setting<Boolean> skip = this.register(new Setting("Skip", false));
   public Setting<Integer> breakAttempts = this.register(new Setting("BreakAttempts", 7, 20, 1));
   public Setting<Float> targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
   public Setting<CevBreaker.Target> targetType;
   public Setting<Color> blockColor;
   public Setting<Boolean> outline;
   public Setting<Float> width;
   public BlockPos base;
   public BlockPos old;
   public boolean builtTrap;
   public boolean placedCrystal;
   public boolean brokeBlock;
   public boolean attackedCrystal;
   public boolean done;
   public boolean headMode;
   public boolean based;
   public int crystalSlot;
   public int obbySlot;
   public int pickelSlot;
   public int attempts;
   public static EntityPlayer target = null;
   public Timer blockTimer;
   public Timer crystalTimer;
   public Timer breakTimer;
   public Timer attackTimer;
   public Timer endTimer;
   public static boolean breaking = false;
   private Integer lockedTargetId = null;
   private BlockPos lockedHead = null;
   private int lockTicks = 0;
   private static final int LOCK_TICKS_DEFAULT = 20;

   public CevBreaker() {
      super("CevBreaker", Module.Categories.COMBAT, false, false);
      this.targetType = this.register(new Setting("Target", CevBreaker.Target.Nearest));
      this.blockColor = this.register(new Setting("Color", new Color(250, 0, 200, 50)));
      this.outline = this.register(new Setting("Outline", false));
      this.width = this.register(new Setting("Width", 2.0F, 5.0F, 0.1F, (v) -> {
         return (Boolean)this.outline.getValue();
      }));
      this.old = null;
      this.done = false;
      this.based = false;
      this.pickelSlot = -1;
      this.attempts = 0;
      this.endTimer = null;
   }

   public void onEnable() {
      this.reset();
      if ((Boolean)this.toggleSilentPickel.getValue()) {
         this.setToggleSilentPickel(true);
      }

   }

   public void onDisable() {
      if ((Boolean)this.toggleSilentPickel.getValue()) {
         this.setToggleSilentPickel(false);
      }

   }

   public void setToggleSilentPickel(boolean toggle) {
      Module silent = CandyPlusRewrite.m_module.getModuleWithClass(SilentPickel.class);
      if (toggle) {
         silent.enable();
      } else {
         silent.disable();
      }

   }

   public void onTick() {
      if ((Boolean)this.tick.getValue()) {
         this.doCB();
      }

   }

   public void onUpdate() {
      if (!(Boolean)this.tick.getValue()) {
         this.doCB();
      }

   }

   public void doCB() {
      if (!this.nullCheck()) {
         try {
            if (!this.findMaterials()) {
               if ((Boolean)this.toggle.getValue()) {
                  this.sendMessage("Cannot find materials! disabling...");
                  this.disable();
               }

               return;
            }

            if (this.lockedTargetId == null) {
               target = this.findTarget();
               if (this.isNull(target)) {
                  if ((Boolean)this.toggle.getValue()) {
                     this.sendMessage("Cannot find target! disabling...");
                     this.disable();
                  }

                  return;
               }

               if (FriendManager.isFriend(target.getName())) {
                  this.reset();
                  return;
               }

               this.lockedTargetId = target.getEntityId();
               this.lockTicks = 20;
            } else {
               Entity e = mc.world.getEntityByID(this.lockedTargetId);
               target = e instanceof EntityPlayer ? (EntityPlayer)e : null;
               if (this.isNull(target) || FriendManager.isFriend(target.getName())) {
                  this.reset();
                  return;
               }
            }

            if (this.isNull(this.base) && !this.findSpace(target)) {
               if ((Boolean)this.toggle.getValue()) {
                  this.sendMessage("Cannot find space! disabling...");
                  this.disable();
               }

               return;
            }

            BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
            BlockPos headPos = targetPos.add(0, 2, 0);
            if (this.lockedHead == null) {
               this.lockedHead = headPos;
               this.lockTicks = 20;
            } else if (this.lockTicks > 0) {
               --this.lockTicks;
            }

            if (this.headMode) {
               this.builtTrap = true;
               this.tryPlaceCrystalImmediate(this.lockedHead);
            }

            if (this.blockTimer == null) {
               this.blockTimer = new Timer();
            }

            if (!this.builtTrap) {
               if (BlockUtil.getBlock(this.base) == Blocks.AIR && !this.based && this.blockTimer.passedX((double)(Float)this.preDelay.getValue())) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(this.base, false);
                  this.blockTimer.reset();
               }

               if (BlockUtil.getBlock(this.base.add(0, 1, 0)) == Blocks.AIR && !this.based && this.blockTimer.passedX((double)(Float)this.preDelay.getValue())) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(this.base.add(0, 1, 0), false);
                  this.blockTimer.reset();
               }

               if (BlockUtil.getBlock(this.lockedHead) == Blocks.AIR && this.blockTimer.passedX((double)(Float)this.preDelay.getValue())) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(this.lockedHead, (Boolean)this.packetPlace.getValue());
                  this.blockTimer = null;
                  this.builtTrap = true;
                  this.tryPlaceCrystalImmediate(this.lockedHead);
               }
            }

            if (this.builtTrap && !this.base.equals(this.old) && (Boolean)this.skip.getValue()) {
               this.placedCrystal = true;
            }

            if (this.crystalTimer == null && this.builtTrap) {
               this.crystalTimer = new Timer();
            }

            if (this.builtTrap && !this.placedCrystal && this.crystalTimer.passedX((double)(Float)this.crystalDelay.getValue())) {
               if (CrystalUtil.canPlaceCrystal(this.lockedHead)) {
                  if (this.crystalSlot != 999) {
                     this.setItem(this.crystalSlot);
                  }

                  EnumHand hand = this.crystalSlot != 999 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                  mc.playerController.updateController();

                  try {
                     mc.rightClickDelayTimer = 0;
                  } catch (Throwable var6) {
                  }

                  if ((Boolean)this.packetCrystal.getValue()) {
                     mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.lockedHead, EnumFacing.UP, hand, 0.5F, 1.0F, 0.5F));
                  } else {
                     mc.playerController.processRightClickBlock(mc.player, mc.world, this.lockedHead, EnumFacing.UP, new Vec3d(0.5D, 1.0D, 0.5D), hand);
                  }

                  if (!(Boolean)this.noSwingBlock.getValue()) {
                     mc.player.swingArm(hand);
                  }

                  this.placedCrystal = true;
                  this.crystalTimer.reset();
               } else {
                  this.crystalTimer.reset();
               }
            }

            if (this.breakTimer == null && this.placedCrystal) {
               this.breakTimer = new Timer();
            }

            if (this.placedCrystal && !this.brokeBlock && this.breakTimer.passedX((double)(Float)this.breakDelay.getValue())) {
               this.setItem(this.pickelSlot);
               if (BlockUtil.getBlock(this.lockedHead) == Blocks.AIR) {
                  this.brokeBlock = true;
               }

               if (!breaking) {
                  if (!(Boolean)this.instantBreak.getValue()) {
                     if (!(Boolean)this.noSwingBlock.getValue()) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                     }

                     mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, this.lockedHead, EnumFacing.DOWN));
                     mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.lockedHead, EnumFacing.DOWN));
                  } else {
                     if (!(Boolean)this.noSwingBlock.getValue()) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                     }

                     CandyPlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
                     InstantMine.startBreak(this.lockedHead, EnumFacing.DOWN);
                  }

                  breaking = true;
               }
            }

            if (this.brokeBlock && !this.base.equals(this.old) && (Boolean)this.skip.getValue()) {
               this.attackedCrystal = true;
            }

            if (this.attackTimer == null && this.brokeBlock) {
               this.attackTimer = new Timer();
            }

            if (this.brokeBlock && !this.attackedCrystal) {
               breaking = false;
               if (this.attackTimer.passedX((double)(Float)this.attackDelay.getValue())) {
                  BlockPos plannedCrystalPos = this.lockedHead.add(0, 1, 0);
                  Entity crystal = (Entity)mc.world.loadedEntityList.stream().filter((ex) -> {
                     return ex instanceof EntityEnderCrystal;
                  }).filter((ex) -> {
                     return ex.getDistance((double)plannedCrystalPos.getX() + 0.5D, (double)plannedCrystalPos.getY(), (double)plannedCrystalPos.getZ() + 0.5D) < 2.0D;
                  }).min(Comparator.comparing((c) -> {
                     return c.getDistance(target);
                  })).orElse((Entity) null);
                  if (crystal == null) {
                     if (this.attempts >= (Integer)this.breakAttempts.getValue()) {
                        this.attackedCrystal = true;
                        this.attempts = 0;
                     } else {
                        ++this.attempts;
                        this.attackTimer.reset();
                     }

                     return;
                  }

                  EnumHand hand2 = (Boolean)this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                  if ((Boolean)this.packetBreak.getValue()) {
                     mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                  } else {
                     mc.playerController.attackEntity(mc.player, crystal);
                     mc.player.swingArm(hand2);
                  }

                  this.attackedCrystal = true;
                  this.attempts = 0;
               }
            }

            if (this.endTimer == null && this.attackedCrystal) {
               this.endTimer = new Timer();
            }

            if (this.attackedCrystal && !this.done && this.endTimer.passedX((double)(Float)this.endDelay.getValue())) {
               this.done = true;
               this.old = new BlockPos(this.base.getX(), this.base.getY(), this.base.getZ());
               this.reset();
            }

            this.restoreItem();
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   private void tryPlaceCrystalImmediate(BlockPos headPos) {
      if (!this.placedCrystal) {
         if (CrystalUtil.canPlaceCrystal(headPos)) {
            if (this.crystalSlot != 999) {
               this.setItem(this.crystalSlot);
            }

            EnumHand hand = this.crystalSlot != 999 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            mc.playerController.updateController();

            try {
               mc.rightClickDelayTimer = 0;
            } catch (Throwable var4) {
            }

            if ((Boolean)this.packetCrystal.getValue()) {
               mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(headPos, EnumFacing.UP, hand, 0.5F, 1.0F, 0.5F));
            } else {
               mc.playerController.processRightClickBlock(mc.player, mc.world, headPos, EnumFacing.UP, new Vec3d(0.5D, 1.0D, 0.5D), hand);
            }

            if (!(Boolean)this.noSwingBlock.getValue()) {
               mc.player.swingArm(hand);
            }

            this.placedCrystal = true;
            if (this.crystalTimer == null) {
               this.crystalTimer = new Timer();
            }

            this.crystalTimer.reset();
         }
      }
   }

   public void reset() {
      this.base = null;
      this.builtTrap = false;
      this.placedCrystal = false;
      this.brokeBlock = false;
      this.attackedCrystal = false;
      this.done = false;
      this.headMode = false;
      this.based = false;
      this.crystalSlot = -1;
      this.obbySlot = -1;
      this.pickelSlot = -1;
      this.attempts = 0;
      target = null;
      this.blockTimer = null;
      this.crystalTimer = null;
      this.breakTimer = null;
      this.attackTimer = null;
      this.endTimer = null;
      breaking = false;
      this.lockedTargetId = null;
      this.lockedHead = null;
      this.lockTicks = 0;
   }

   public void onRender3D() {
      try {
         if (this.isNull(target)) {
            return;
         }

         BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos headPos = targetPos.add(0, 2, 0);
         RenderUtil3D.drawBox(headPos, 1.0D, (Color)this.blockColor.getValue(), 63);
         if ((Boolean)this.outline.getValue()) {
            RenderUtil3D.drawBoundingBox(headPos, 1.0D, (Float)this.width.getValue(), (Color)this.blockColor.getValue());
         }
      } catch (Exception var3) {
      }

   }

   public void setItem(int slot) {
      mc.player.inventory.currentItem = slot;
      mc.playerController.updateController();
   }

   public void restoreItem() {
   }

   public boolean findSpace(EntityPlayer player) {
      BlockPos targetPos = new BlockPos(player.posX, player.posY, player.posZ);
      BlockPos headPos = targetPos.add(0, 2, 0);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      List<BlockPos> posess = new ArrayList();
      if (BlockUtil.getBlock(headPos) == Blocks.OBSIDIAN) {
         this.headMode = true;
         this.base = targetPos;
         return true;
      } else if (BlockUtil.canPlaceBlock(headPos)) {
         this.based = true;
         this.base = targetPos;
         return true;
      } else {
         BlockPos[] var6 = offsets;
         int var7 = offsets.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockPos offset = var6[var8];
            BlockPos basePos = targetPos.add(offset);
            if ((BlockUtil.getBlock(basePos) == Blocks.OBSIDIAN || BlockUtil.getBlock(basePos) == Blocks.BEDROCK) && BlockUtil.canPlaceBlockFuture(basePos.add(0, 1, 0)) && BlockUtil.canPlaceBlockFuture(basePos.add(0, 2, 0)) && BlockUtil.getBlock(headPos) == Blocks.AIR) {
               posess.add(basePos.add(0, 1, 0));
            }
         }

         this.base = (BlockPos)posess.stream().filter((p) -> {
            return mc.player.getDistance((double)p.getX(), (double)p.getY(), (double)p.getZ()) <= (double)(Float)this.range.getValue();
         }).max(Comparator.comparing(PlayerUtil::getDistanceI)).orElse((BlockPos) null);
         if (this.base == null) {
            return false;
         } else {
            this.headMode = false;
            return true;
         }
      }
   }

   public EntityPlayer findTarget() {
      List<EntityPlayer> candidates = new ArrayList(mc.world.playerEntities);
      candidates.removeIf((p) -> {
         return p == null || p == mc.player || p.isDead || FriendManager.isFriend(p.getName()) || mc.player.getDistance(p) > (Float)this.targetRange.getValue();
      });
      if (candidates.isEmpty()) {
         return null;
      } else {
         switch((CevBreaker.Target)this.targetType.getValue()) {
         case Nearest:
            return (EntityPlayer)candidates.stream().min(Comparator.comparingDouble((p) -> {
               return (double)mc.player.getDistance(p);
            })).orElse((EntityPlayer) null);
         case Looking:
            EntityPlayer looking = PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
            if (looking != null && !FriendManager.isFriend(looking.getName()) && candidates.contains(looking)) {
               return looking;
            }

            return null;
         case Best:
            return (EntityPlayer)candidates.stream().filter(this::findSpace).min(Comparator.comparingDouble(PlayerUtil::getDistance)).orElse((EntityPlayer) null);
         default:
            return null;
         }
      }
   }

   public boolean findMaterials() {
      this.crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
      this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
      this.pickelSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
      if (this.itemCheck(this.crystalSlot) && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
         this.crystalSlot = 999;
      }

      return !this.itemCheck(this.crystalSlot) && !this.itemCheck(this.obbySlot) && !this.itemCheck(this.pickelSlot);
   }

   public boolean itemCheck(int slot) {
      return slot == -1;
   }

   public boolean isNull(Object o) {
      return o == null;
   }

   public void placeBlock(BlockPos pos, Boolean packet) {
      BlockUtil.placeBlock(pos, packet);
      if (!(Boolean)this.noSwingBlock.getValue()) {
         mc.player.swingArm(EnumHand.MAIN_HAND);
      }

   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.MathUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.Level;

public class PistonAura extends Module {
   public Setting<Float> preDelay = this.register(new Setting("BlockDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> pistonDelay = this.register(new Setting("PistonDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> crystalDelay = this.register(new Setting("CrystalDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> redstoneDelay = this.register(new Setting("RedStoneDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> pushDelay = this.register(new Setting("PushDelay", 0.0F, 25.0F, 0.0F));
   public Setting<Float> breakDelay = this.register(new Setting("BreakDelay", 5.0F, 25.0F, 0.0F));
   public Setting<Float> targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
   public Setting<PistonAura.Target> targetType;
   public Setting<Float> range;
   public Setting<PistonAura.RedStone> redStoneType;
   public Setting<Boolean> antiBlock;
   public Setting<Boolean> noSwingBlock;
   public Setting<Boolean> silentSwitch;
   public Setting<Boolean> packetPlace;
   public Setting<Boolean> packetCrystal;
   public Setting<Boolean> packetBreak;
   public Setting<Boolean> offHandBreak;
   public Setting<Boolean> sidePiston;
   public Setting<Boolean> tick;
   public Setting<Boolean> toggle;
   public Setting<Boolean> trap;
   public Setting<Float> trapDelay;
   public Setting<Boolean> breakSync;
   public Setting<Float> maxDelay;
   public Setting<Integer> breakAttempts;
   public Setting<Integer> maxY;
   public Setting<Boolean> render;
   public Setting<Color> crystalColor;
   public Setting<Color> pistonColor;
   public Setting<Color> redstoneColor;
   public Setting<Boolean> line;
   public Setting<Float> width;
   public Setting<Boolean> debug;
   public List<BlockPos> debugPosess;
   public int oldslot;
   public EnumHand oldhand;
   public static EntityPlayer target;
   public BlockPos pistonPos;
   public BlockPos crystalPos;
   public BlockPos redStonePos;
   public boolean placedPiston;
   public boolean placedCrystal;
   public boolean placedRedStone;
   public boolean waitedPiston;
   public boolean brokeCrystal;
   public boolean builtTrap;
   public boolean done;
   public boolean retrying;
   public boolean digging;
   public Timer pistonTimer;
   public Timer crystalTimer;
   public Timer redStoneTimer;
   public Timer pistonCrystalTimer;
   public Timer breakTimer;
   public Timer preTimer;
   public Timer trapTimer;
   public Timer syncTimer;
   public int pistonSlot;
   public int crystalSlot;
   public int redstoneSlot;
   public int obbySlot;
   public int pickelSlot;
   public int trapTicks;
   public int attempts;
   public BlockPos oldPiston;
   public BlockPos oldRedstone;
   public int tmpSlot;

   public PistonAura() {
      super("PistonAura", Module.Categories.COMBAT, false, false);
      this.targetType = this.register(new Setting("Target", PistonAura.Target.Nearest));
      this.range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
      this.redStoneType = this.register(new Setting("Redstone", PistonAura.RedStone.Both));
      this.antiBlock = this.register(new Setting("AntiBlock", true));
      this.noSwingBlock = this.register(new Setting("NoSwingBlock", false));
      this.silentSwitch = this.register(new Setting("SilentSwitch", false));
      this.packetPlace = this.register(new Setting("PacketPlace", true));
      this.packetCrystal = this.register(new Setting("PacketCrystal", true));
      this.packetBreak = this.register(new Setting("PacketBreak", true));
      this.offHandBreak = this.register(new Setting("OffhandBreak", true));
      this.sidePiston = this.register(new Setting("SidePiston", false));
      this.tick = this.register(new Setting("Tick", true));
      this.toggle = this.register(new Setting("Toggle", true));
      this.trap = this.register(new Setting("Trap", false));
      this.trapDelay = this.register(new Setting("TrapDelay", 3.0F, 25.0F, 0.0F, (s) -> {
         return (Boolean)this.trap.getValue();
      }));
      this.breakSync = this.register(new Setting("BreakSync", true));
      this.maxDelay = this.register(new Setting("MaxDelay", 50.0F, 100.0F, 1.0F, (s) -> {
         return (Boolean)this.breakSync.getValue();
      }));
      this.breakAttempts = this.register(new Setting("BreakAttempts", 7, 20, 1));
      this.maxY = this.register(new Setting("MaxY", 2, 4, 1));
      this.render = this.register(new Setting("Render", true));
      this.crystalColor = this.register(new Setting("Crystal Color", new Color(250, 0, 200, 50), (s) -> {
         return (Boolean)this.render.getValue();
      }));
      this.pistonColor = this.register(new Setting("Piston Color", new Color(40, 170, 245, 50), (s) -> {
         return (Boolean)this.render.getValue();
      }));
      this.redstoneColor = this.register(new Setting("RedStone Color", new Color(252, 57, 50, 50), (s) -> {
         return (Boolean)this.render.getValue();
      }));
      this.line = this.register(new Setting("Line", false, (s) -> {
         return (Boolean)this.render.getValue();
      }));
      this.width = this.register(new Setting("Line Width", 2.0F, 5.0F, 0.1F, (s) -> {
         return (Boolean)this.line.getValue() && (Boolean)this.render.getValue();
      }));
      this.debug = this.register(new Setting("Debug", false));
      this.debugPosess = new ArrayList();
      this.oldslot = -1;
      this.oldhand = null;
      this.trapTicks = 0;
      this.attempts = 0;
      this.oldRedstone = null;
   }

   public void onEnable() {
      this.reset();
   }

   public void onUpdate() {
      if (!(Boolean)this.tick.getValue()) {
         this.doPA();
      }

   }

   public void onTick() {
      if ((Boolean)this.tick.getValue()) {
         this.doPA();
      }

   }

   public void doPA() {
      if (!this.nullCheck()) {
         try {
            if (!this.findMaterials()) {
               if ((Boolean)this.toggle.getValue()) {
                  this.sendMessage("Cannot find materials! disabling...");
                  this.disable();
               }

               return;
            }

            target = this.findTarget();
            if (this.isNull(target) || FriendManager.isFriend(target)) {
               if ((Boolean)this.toggle.getValue()) {
                  this.sendMessage("Cannot find target! disabling...");
                  this.disable();
               }

               return;
            }

            if ((this.isNull(this.pistonPos) || this.isNull(this.crystalPos) || this.isNull(this.redStonePos)) && !this.findSpace(target, (PistonAura.RedStone)this.redStoneType.getValue())) {
               if ((Boolean)this.toggle.getValue()) {
                  this.sendMessage("Cannot find space! disabling...");
                  this.disable();
               }

               return;
            }

            if (this.preTimer == null) {
               this.preTimer = new Timer();
            }

            if (this.preTimer.passedX((double)(Float)this.preDelay.getValue()) && !this.prepareBlock()) {
               this.restoreItem();
               return;
            }

            if (this.trapTimer == null) {
               this.trapTimer = new Timer();
            }

            if (!(Boolean)this.trap.getValue()) {
               this.builtTrap = true;
            }

            BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
            if (BlockUtil.getBlock(targetPos.add(0, 2, 0)) == Blocks.OBSIDIAN || this.pistonPos.getY() >= targetPos.add(0, 2, 0).getY()) {
               this.builtTrap = true;
            }

            if (!this.builtTrap && this.trapTimer.passedX((double)(Float)this.trapDelay.getValue())) {
               BlockPos offset = new BlockPos(this.crystalPos.getX() - targetPos.getX(), 0, this.crystalPos.getZ() - targetPos.getZ());
               BlockPos trapBase = targetPos.add(offset.getX() * -1, 0, offset.getZ() * -1);
               if (this.trapTicks == 0 && BlockUtil.getBlock(trapBase) == Blocks.AIR) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(trapBase, false);
                  this.trapTimer = new Timer();
                  this.trapTicks = 1;
               } else {
                  this.trapTicks = 1;
               }

               if (this.trapTicks == 1) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(trapBase.add(0, 1, 0), false);
                  this.trapTimer = new Timer();
                  this.trapTicks = 2;
               }

               if (this.trapTicks == 2) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(trapBase.add(0, 2, 0), false);
                  this.trapTimer = new Timer();
                  this.trapTicks = 3;
               }

               if (this.trapTicks == 3) {
                  this.setItem(this.obbySlot);
                  this.placeBlock(targetPos.add(0, 2, 0), false);
                  this.trapTimer = new Timer();
                  this.trapTicks = 4;
                  this.builtTrap = true;
               }

               this.restoreItem();
               return;
            }

            if (this.pistonTimer == null && this.builtTrap) {
               this.pistonTimer = new Timer();
            }

            if (this.builtTrap && !this.placedPiston && this.pistonTimer.passedX((double)(Float)this.pistonDelay.getValue())) {
               this.setItem(this.pistonSlot);
               float[] angle = MathUtil.calcAngle(new Vec3d(this.crystalPos), new Vec3d(targetPos));
               mc.player.connection.sendPacket(new Rotation(angle[0] + 180.0F, angle[1], true));
               this.placePiston(this.pistonPos, targetPos, (Boolean)this.packetPlace.getValue());
               this.placedPiston = true;
            }

            if (this.crystalTimer == null && this.placedPiston) {
               this.crystalTimer = new Timer();
            }

            if (this.placedPiston && !this.placedCrystal && this.crystalTimer.passedX((double)(Float)this.crystalDelay.getValue())) {
               if (this.crystalSlot != 999) {
                  this.setItem(this.crystalSlot);
               }

               EnumHand hand = this.crystalSlot != 999 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
               if ((Boolean)this.packetCrystal.getValue()) {
                  mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.crystalPos, EnumFacing.DOWN, hand, 0.0F, 0.0F, 0.0F));
               } else {
                  mc.playerController.processRightClickBlock(mc.player, mc.world, this.crystalPos, EnumFacing.DOWN, new Vec3d(0.0D, 0.0D, 0.0D), hand);
               }

               this.placedCrystal = true;
            }

            if (this.redStoneTimer == null && this.placedCrystal) {
               this.redStoneTimer = new Timer();
            }

            if (this.placedCrystal && !this.placedRedStone && this.redStoneTimer.passedX((double)(Float)this.redstoneDelay.getValue())) {
               this.setItem(this.redstoneSlot);
               this.placeBlock(this.redStonePos, (Boolean)this.packetPlace.getValue());
               this.placedRedStone = true;
            }

            if (this.pistonCrystalTimer == null && this.placedRedStone) {
               this.pistonCrystalTimer = new Timer();
            }

            if (this.placedRedStone && !this.waitedPiston && this.pistonCrystalTimer.passedX((double)(Float)this.pushDelay.getValue())) {
               this.waitedPiston = true;
            }

            if (this.retrying) {
               this.setItem(this.pickelSlot);
               if (!this.digging) {
                  mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, this.redStonePos, EnumFacing.DOWN));
                  this.digging = true;
               }

               if (this.digging && BlockUtil.getBlock(this.redStonePos) == Blocks.AIR) {
                  mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.redStonePos, EnumFacing.DOWN));
                  this.placedCrystal = false;
                  this.placedRedStone = false;
                  this.waitedPiston = false;
                  this.brokeCrystal = false;
                  this.done = false;
                  this.digging = false;
                  this.retrying = false;
                  this.crystalTimer = null;
                  this.redStoneTimer = null;
                  this.pistonCrystalTimer = null;
                  this.breakTimer = null;
                  this.attempts = 0;
               }

               this.restoreItem();
               return;
            }

            EnumHand hand2;
            Entity crystal;
            if (this.waitedPiston && !this.brokeCrystal) {
               crystal = (Entity)mc.world.loadedEntityList.stream().filter((e2) -> {
                  return e2 instanceof EntityEnderCrystal;
               }).filter((e2) -> {
                  return target.getDistance(e2) < 5.0F;
               }).min(Comparator.comparing((e2) -> {
                  return target.getDistance(e2);
               })).orElse((Entity) null);
               if (crystal == null) {
                  if (this.attempts < (Integer)this.breakAttempts.getValue()) {
                     ++this.attempts;
                  } else {
                     this.attempts = 0;
                     this.digging = false;
                     this.retrying = true;
                  }

                  this.restoreItem();
                  return;
               }

               hand2 = (Boolean)this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
               if ((Boolean)this.packetBreak.getValue()) {
                  mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
               } else {
                  mc.playerController.attackEntity(mc.player, crystal);
                  mc.player.swingArm(hand2);
               }

               this.brokeCrystal = true;
            }

            if (this.breakTimer == null && this.brokeCrystal) {
               this.breakTimer = new Timer();
            }

            if (this.brokeCrystal && !this.done && this.breakTimer.passedX((double)(Float)this.breakDelay.getValue())) {
               this.done = true;
            }

            if (this.done) {
               if ((BlockUtil.getBlock(this.redStonePos) == Blocks.REDSTONE_BLOCK || BlockUtil.getBlock(this.redStonePos) == Blocks.REDSTONE_TORCH) && (Boolean)this.breakSync.getValue()) {
                  if (this.syncTimer == null) {
                     this.syncTimer = new Timer();
                  }

                  if (this.syncTimer.passedDms((double)(Float)this.maxDelay.getValue()) && (Float)this.maxDelay.getValue() != -1.0F) {
                     this.reset();
                  } else {
                     crystal = (Entity)mc.world.loadedEntityList.stream().filter((e2) -> {
                        return e2 instanceof EntityEnderCrystal;
                     }).filter((e2) -> {
                        return target.getDistance(e2) < 5.0F;
                     }).min(Comparator.comparing((e2) -> {
                        return target.getDistance(e2);
                     })).orElse((Entity) null);
                     if (crystal == null) {
                        this.restoreItem();
                        return;
                     }

                     hand2 = (Boolean)this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                     if ((Boolean)this.packetBreak.getValue()) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                     } else {
                        mc.playerController.attackEntity(mc.player, crystal);
                        mc.player.swingArm(hand2);
                     }

                     this.breakTimer = null;
                     this.done = false;
                  }
               } else {
                  this.reset();
               }
            }

            this.restoreItem();
         } catch (Exception var4) {
            CandyPlusRewrite.Log(Level.ERROR, var4.getMessage());
         }

      }
   }

   public void onRender3D() {
      try {
         if (!(Boolean)this.render.getValue()) {
            return;
         }

         if (this.isNull(this.pistonPos) || this.isNull(this.crystalPos) || this.isNull(this.redStonePos)) {
            return;
         }

         if ((Boolean)this.line.getValue()) {
            RenderUtil3D.drawBoundingBox(this.crystalPos, 1.0D, (Float)this.width.getValue(), this.convert((Color)this.crystalColor.getValue()));
            RenderUtil3D.drawBoundingBox(this.pistonPos, 1.0D, (Float)this.width.getValue(), this.convert((Color)this.pistonColor.getValue()));
            RenderUtil3D.drawBoundingBox(this.redStonePos, 1.0D, (Float)this.width.getValue(), this.convert((Color)this.redstoneColor.getValue()));
         } else {
            RenderUtil3D.drawBox(this.crystalPos, 1.0D, (Color)this.crystalColor.getValue(), 63);
            RenderUtil3D.drawBox(this.pistonPos, 1.0D, (Color)this.pistonColor.getValue(), 63);
            RenderUtil3D.drawBox(this.redStonePos, 1.0D, (Color)this.redstoneColor.getValue(), 63);
         }

         if ((Boolean)this.debug.getValue()) {
            Iterator var1 = this.debugPosess.iterator();

            while(var1.hasNext()) {
               BlockPos pos = (BlockPos)var1.next();
               if (pos != null) {
                  RenderUtil3D.drawBoundingBox(pos, 1.0D, (Float)this.width.getValue(), new Color(230, 230, 230));
               }
            }
         }
      } catch (Exception var3) {
      }

   }

   public Color convert(Color c) {
      return new Color(c.getRed(), c.getGreen(), c.getBlue(), 240);
   }

   public static EnumFacing getFacingToTarget(BlockPos pistonPos, BlockPos targetPos) {
      int dx = targetPos.getX() - pistonPos.getX();
      int dz = targetPos.getZ() - pistonPos.getZ();
      if (Math.abs(dx) > Math.abs(dz)) {
         return dx > 0 ? EnumFacing.EAST : EnumFacing.WEST;
      } else if (dz != 0) {
         return dz > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
      } else {
         return EnumFacing.UP;
      }
   }

   public void placePiston(BlockPos pos, BlockPos targetPos, boolean usePacket) {
      BlockUtil.placeBlock(pos, usePacket);
      if (!(Boolean)this.noSwingBlock.getValue()) {
         mc.player.swingArm(EnumHand.MAIN_HAND);
      }

   }

   public boolean findSpace(EntityPlayer target, PistonAura.RedStone type) {
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      List<PistonAura.PistonAuraPos> poses = new ArrayList();
      BlockPos[] var7 = offsets;
      int var8 = offsets.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         BlockPos offset = var7[var9];

         for(int y = 0; y <= (Integer)this.maxY.getValue(); ++y) {
            PistonAura.PistonAuraPos pos = new PistonAura.PistonAuraPos();
            BlockPos cPos = base.add(offset.getX(), y, offset.getZ());
            if (BlockUtil.getBlock(cPos) == Blocks.ENDER_CHEST) {
               this.sendmsg("x");
            } else if ((BlockUtil.getBlock(cPos) == Blocks.OBSIDIAN || BlockUtil.getBlock(cPos) == Blocks.BEDROCK) && BlockUtil.getBlock(cPos.add(0, 1, 0)) == Blocks.AIR && BlockUtil.getBlock(cPos.add(0, 2, 0)) == Blocks.AIR) {
               if (mypos.getX() == cPos.getX() && mypos.getZ() == cPos.getZ()) {
                  this.sendmsg("a");
               } else {
                  pos.setCrystal(cPos);
                  List<BlockPos> pistonOffsets = new ArrayList();
                  pistonOffsets.add(new BlockPos(1, 0, 0));
                  pistonOffsets.add(new BlockPos(-1, 0, 0));
                  pistonOffsets.add(new BlockPos(0, 0, 1));
                  pistonOffsets.add(new BlockPos(0, 0, -1));
                  if (!(Boolean)this.antiBlock.getValue()) {
                     pistonOffsets.add(new BlockPos(0, 0, 0));
                  }

                  BlockPos pistonBase = base.add(offset.getX() * 2, y + 1, offset.getZ() * 2);
                  List<BlockPos> pistonPoses = new ArrayList();
                  Iterator var17 = pistonOffsets.iterator();

                  while(true) {
                     while(var17.hasNext()) {
                        BlockPos poff = (BlockPos)var17.next();
                        BlockPos pPos = pistonBase.add(poff);
                        if (BlockUtil.getBlock(pPos) != Blocks.AIR) {
                           this.sendmsg("b");
                        } else {
                           BlockPos checkPos_c = pPos.add(offset.getX() * -1, offset.getY(), offset.getZ() * -1);
                           BlockPos checkPos_r = pPos.add(offset.getX(), offset.getY(), offset.getZ());
                           if (mypos.getDistance(pPos.getX(), pPos.getY(), pPos.getZ()) < 3.6D + (double)(pPos.getY() - (mypos.getY() + 1)) && pPos.getY() > mypos.getY() + 1) {
                              this.sendmsg("++");
                           } else if (BlockUtil.getBlock(checkPos_c) == Blocks.AIR && BlockUtil.getBlock(checkPos_r) == Blocks.AIR && (pPos.getX() != cPos.getX() || pPos.getZ() != cPos.getZ()) && (mypos.getX() != pPos.getX() || mypos.getZ() != pPos.getZ()) && (mypos.getX() != checkPos_r.getX() || mypos.getZ() != checkPos_r.getZ()) && (mypos.getX() != checkPos_c.getX() || mypos.getZ() != checkPos_c.getZ())) {
                              pistonPoses.add(pPos);
                           } else {
                              this.sendmsg("d = " + checkPos_c);
                           }
                        }
                     }

                     pos.setPiston((BlockPos)pistonPoses.stream().min(Comparator.comparing((p) -> {
                        return mypos.getDistance(p.getX(), p.getY(), p.getZ());
                     })).orElse((BlockPos) null));
                     if (this.isNull(pos.piston)) {
                        this.sendmsg("e");
                        break;
                     }

                     BlockPos redstonePos;
                     if (cPos.getX() != pos.piston.getX() && cPos.getZ() != pos.piston.getZ() || pos.piston.getX() - cPos.getX() != 0 && pos.piston.getZ() - cPos.getZ() == 0 || pos.piston.getZ() - cPos.getZ() != 0 && pos.piston.getX() - cPos.getX() == 0 && (offset.getX() != 0 || offset.getZ() != 0)) {
                        redstonePos = pos.piston.add(offset);
                     } else {
                        redstonePos = pos.piston.add(new BlockPos(pos.piston.getX() - pos.crystal.getX(), 0, pos.piston.getZ() - pos.crystal.getZ()));
                     }

                     if (BlockUtil.getBlock(redstonePos) != Blocks.AIR) {
                        this.sendmsg("f");
                     } else {
                        pos.setRedStone(redstonePos);
                        poses.add(pos);
                     }
                     break;
                  }
               }
            } else {
               this.sendmsg("y");
            }
         }
      }

      if (poses.isEmpty()) {
         return false;
      } else {
         PistonAura.PistonAuraPos bestPos = (PistonAura.PistonAuraPos)poses.stream().filter((p) -> {
            return p.getMaxRange() <= (double)(Float)this.range.getValue();
         }).min(Comparator.comparing(PistonAura.PistonAuraPos::getMaxRange)).orElse((PistonAuraPos) null);
         if (bestPos == null) {
            return false;
         } else {
            this.pistonPos = bestPos.piston;
            this.crystalPos = bestPos.crystal;
            this.redStonePos = bestPos.redstone;
            return true;
         }
      }
   }

   public EntityPlayer findTarget() {
      EntityPlayer t = null;
      List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
      players.removeIf((p) -> {
         return p == mc.player || FriendManager.isFriend(p) || p.isDead || p.getHealth() <= 0.0F;
      });
      players.removeIf((p) -> {
         return mc.player.getDistance(p) > (Float)this.targetRange.getValue();
      });
      Stream<EntityPlayer> var10000;
      EntityPlayerSP var10001;
      if (this.targetType.getValue() == PistonAura.Target.Nearest) {
         var10000 = players.stream();
         var10001 = mc.player;
         var10001.getClass();
         t = var10000.min(Comparator.comparingDouble(p -> var10001.getDistance(p))).orElse(null);
      } else if (this.targetType.getValue() == PistonAura.Target.Looking) {
         EntityPlayer looking = PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
         if (looking != null && !FriendManager.isFriend(looking) && players.contains(looking)) {
            t = looking;
         } else {
            var10000 = players.stream();
            var10001 = mc.player;
            var10001.getClass();
            t = var10000.min(Comparator.comparingDouble(p -> var10001.getDistance(p))).orElse(null);
         }
      } else if (this.targetType.getValue() == PistonAura.Target.Best) {
         var10000 = players.stream().filter((p) -> {
            return this.findSpace(p, (PistonAura.RedStone)this.redStoneType.getValue());
         });
         var10001 = mc.player;
         var10001.getClass();
         t = var10000.min(Comparator.comparingDouble(p -> var10001.getDistance(p))).orElse(null);
      }

      return t;
   }

   public boolean findMaterials() {
      this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.PISTON);
      this.crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
      int redstoneBlock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
      int redstoneTorch = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_TORCH);
      this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
      this.pickelSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
      if (this.itemCheck(this.crystalSlot) && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
         this.crystalSlot = 999;
      }

      if (this.itemCheck(this.pistonSlot)) {
         this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.STICKY_PISTON);
      }

      if (this.redStoneType.getValue() == PistonAura.RedStone.Block) {
         this.redstoneSlot = redstoneBlock;
      }

      if (this.redStoneType.getValue() == PistonAura.RedStone.Torch) {
         this.redstoneSlot = redstoneTorch;
      }

      if (this.redStoneType.getValue() == PistonAura.RedStone.Both) {
         this.redstoneSlot = redstoneTorch;
         if (this.itemCheck(this.redstoneSlot)) {
            this.redstoneSlot = redstoneBlock;
         }
      }

      return !this.itemCheck(this.crystalSlot) && !this.itemCheck(this.obbySlot) && !this.itemCheck(this.pickelSlot) && !this.itemCheck(this.redstoneSlot) && !this.itemCheck(this.pistonSlot);
   }

   public boolean itemCheck(int slot) {
      return slot == -1;
   }

   public boolean prepareBlock() {
      BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
      BlockPos crystal = this.crystalPos;
      BlockPos piston = this.pistonPos.add(0, -1, 0);
      BlockPos redstone = this.redStonePos.add(0, -1, 0);
      if (BlockUtil.getBlock(crystal) == Blocks.AIR) {
         this.setItem(this.obbySlot);
         this.placeBlock(crystal, (Boolean)this.packetPlace.getValue());
         if ((Float)this.preDelay.getValue() != 0.0F) {
            this.preTimer.reset();
         }

         return false;
      } else if (!BlockUtil.hasNeighbour(piston)) {
         this.setItem(this.obbySlot);
         BlockPos base = crystal.add(crystal.getX() - targetPos.getX(), 0, crystal.getZ() - targetPos.getZ());
         this.placeBlock(base, (Boolean)this.packetPlace.getValue());
         if ((Float)this.preDelay.getValue() != 0.0F) {
            this.preTimer.reset();
         }

         return false;
      } else if (BlockUtil.getBlock(piston) == Blocks.AIR) {
         this.setItem(this.obbySlot);
         this.placeBlock(piston, (Boolean)this.packetPlace.getValue());
         if ((Float)this.preDelay.getValue() != 0.0F) {
            this.preTimer.reset();
         }

         return false;
      } else if (BlockUtil.getBlock(redstone) == Blocks.AIR && (piston.getX() != redstone.getX() || piston.getZ() != redstone.getZ())) {
         this.setItem(this.obbySlot);
         this.placeBlock(redstone, (Boolean)this.packetPlace.getValue());
         if ((Float)this.preDelay.getValue() != 0.0F) {
            this.preTimer.reset();
         }

         return false;
      } else {
         return true;
      }
   }

   public Vec3i getOffset(BlockPos base, int x, int z) {
      return new Vec3i(base.getX() * x, 0, base.getZ() * z);
   }

   public boolean isNull(Object b) {
      return b == null;
   }

   public void setTmp() {
      this.tmpSlot = mc.player.inventory.currentItem;
   }

   public void updateItem() {
      mc.player.inventory.currentItem = this.tmpSlot;
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

   public void sendmsg(String s) {
      if ((Boolean)this.debug.getValue()) {
         this.sendMessage(s);
      }

   }

   public void placeBlock(BlockPos pos, Boolean packet) {
      BlockUtil.placeBlock(pos, packet);
      if (!(Boolean)this.noSwingBlock.getValue()) {
         mc.player.swingArm(EnumHand.MAIN_HAND);
      }

   }

   public void reset() {
      this.oldPiston = this.pistonPos;
      this.oldRedstone = this.redStonePos;
      target = null;
      this.pistonPos = null;
      this.crystalPos = null;
      this.redStonePos = null;
      this.placedPiston = false;
      this.placedCrystal = false;
      this.placedRedStone = false;
      this.waitedPiston = false;
      this.brokeCrystal = false;
      this.builtTrap = false;
      this.done = false;
      this.digging = false;
      this.retrying = false;
      this.pistonTimer = null;
      this.crystalTimer = null;
      this.redStoneTimer = null;
      this.pistonCrystalTimer = null;
      this.breakTimer = null;
      this.preTimer = null;
      this.trapTimer = null;
      this.syncTimer = null;
      this.pistonSlot = -1;
      this.crystalSlot = -1;
      this.redstoneSlot = -1;
      this.obbySlot = -1;
      this.pickelSlot = -1;
      this.trapTicks = 0;
      this.attempts = 0;
   }

   public class PistonAuraPos {
      private BlockPos piston;
      private BlockPos crystal;
      private BlockPos redstone;

      public void setPiston(BlockPos piston) {
         this.piston = piston;
      }

      public void setCrystal(BlockPos crystal) {
         this.crystal = crystal;
      }

      public void setRedStone(BlockPos redstone) {
         this.redstone = redstone;
      }

      public BlockPos getCrystalPos() {
         return this.crystal;
      }

      public BlockPos getPistonPos() {
         return this.piston;
      }

      public BlockPos getRedstone() {
         return this.redstone;
      }

      public double getMaxRange() {
         double p = PlayerUtil.getDistanceI(this.piston);
         double c = PlayerUtil.getDistanceI(this.crystal);
         double r = PlayerUtil.getDistanceI(this.redstone);
         return Math.max(Math.max(p, c), r);
      }
   }

   public static enum RedStone {
      Block,
      Torch,
      Both;
   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

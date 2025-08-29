//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.CrystalUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Blocker extends Module {
   public Setting<Boolean> piston = this.register(new Setting("Piston", true));
   public Setting<Boolean> crystalSync = this.register(new Setting("CrystalSyncPA", false, (v) -> {
      return (Boolean)this.piston.getValue();
   }));
   public Setting<Boolean> breakCrystalPA = this.register(new Setting("BreakCrystalPA", false, (v) -> {
      return (Boolean)this.piston.getValue();
   }));
   public Setting<Boolean> teleportPA = this.register(new Setting("FlightBreakPA", true, (v) -> {
      return (Boolean)this.piston.getValue() && (Boolean)this.breakCrystalPA.getValue();
   }));
   public Setting<Integer> limitPA = this.register(new Setting("LimitPA", 3, 10, 1, (v) -> {
      return (Boolean)this.piston.getValue() && (Boolean)this.breakCrystalPA.getValue() && (Boolean)this.teleportPA.getValue();
   }));
   public Setting<Float> crystalDelayPA = this.register(new Setting("CrystalDelayPA", 3.0F, 25.0F, 0.0F, (v) -> {
      return (Boolean)this.piston.getValue() && (Boolean)this.breakCrystalPA.getValue();
   }));
   public Setting<Float> range = this.register(new Setting("Range", 7.0F, 13.0F, 0.0F, (v) -> {
      return (Boolean)this.piston.getValue();
   }));
   public Setting<Integer> maxY = this.register(new Setting("MaxY", 4, 6, 2, (v) -> {
      return (Boolean)this.piston.getValue();
   }));
   public Setting<Boolean> cev = this.register(new Setting("CevBreaker", true));
   public Setting<Float> crystalDelayCEV = this.register(new Setting("CrystalDelayCEV", 3.0F, 25.0F, 0.0F, (v) -> {
      return (Boolean)this.cev.getValue();
   }));
   public Setting<Boolean> teleportCEV = this.register(new Setting("FlightBreakCEV", true, (v) -> {
      return (Boolean)this.cev.getValue();
   }));
   public Setting<Integer> limitCEV = this.register(new Setting("LimitCEV", 3, 10, 1, (v) -> {
      return (Boolean)this.piston.getValue() && (Boolean)this.teleportCEV.getValue();
   }));
   public Setting<Boolean> civ = this.register(new Setting("CivBreaker", true));
   public Setting<Float> crystalDelayCIV = this.register(new Setting("CrystalDelayCIV", 3.0F, 25.0F, 0.0F, (v) -> {
      return (Boolean)this.cev.getValue();
   }));
   public Setting<Float> placeDelay = this.register(new Setting("PlaceDelay", 3.0F, 25.0F, 0.0F));
   public Setting<Boolean> packetPlace = this.register(new Setting("PacketPlace", false));
   public Setting<Boolean> packetBreak = this.register(new Setting("PacketBreak", true));
   public Setting<Blocker.Arm> swingArm;
   public Setting<Boolean> silentSwitch;
   public Setting<Boolean> tick;
   public Setting<Boolean> render;
   public Setting<Color> pistonColor;
   public Entity PAcrystal;
   public List<BlockPos> pistonPos;
   public Timer crystalTimerPA;
   public int oldCrystal;
   public int limitCounterPA;
   public boolean needBlockCEV;
   public Timer crystalTimerCEV;
   public int limitCounterCEV;
   public int stage;
   public Timer timerCiv;
   public List<BlockPos> detectedPosCiv;
   public Timer placeTimer;
   private int oldslot;
   private EnumHand oldhand;

   public Blocker() {
      super("Blocker", Module.Categories.COMBAT, false, false);
      this.swingArm = this.register(new Setting("SwingArm", Blocker.Arm.None));
      this.silentSwitch = this.register(new Setting("SilentSwitch", false));
      this.tick = this.register(new Setting("Tick", true));
      this.render = this.register(new Setting("Render", true));
      this.pistonColor = this.register(new Setting("PistonColor", new Color(230, 10, 10, 50), (v) -> {
         return (Boolean)this.piston.getValue();
      }));
      this.PAcrystal = null;
      this.pistonPos = new ArrayList();
      this.crystalTimerPA = new Timer();
      this.oldCrystal = -1;
      this.limitCounterPA = 0;
      this.needBlockCEV = false;
      this.crystalTimerCEV = new Timer();
      this.limitCounterCEV = 0;
      this.stage = 0;
      this.timerCiv = new Timer();
      this.detectedPosCiv = new ArrayList();
      this.placeTimer = new Timer();
      this.oldslot = -1;
      this.oldhand = null;
   }

   public void onEnable() {
      this.PAcrystal = null;
      this.pistonPos = new ArrayList();
      this.crystalTimerPA = new Timer();
      this.needBlockCEV = false;
      this.crystalTimerCEV = new Timer();
      this.limitCounterCEV = 0;
      this.stage = 0;
      this.timerCiv = new Timer();
      this.detectedPosCiv = new ArrayList();
      this.placeTimer = new Timer();
   }

   public void onTick() {
      if ((Boolean)this.tick.getValue()) {
         this.doBlock();
      }

   }

   public void onUpdate() {
      if (!(Boolean)this.tick.getValue()) {
         this.doBlock();
      }

   }

   public void doBlock() {
      if (!this.nullCheck()) {
         int obby = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (obby != -1) {
            Module pa = CandyPlusRewrite.m_module.getModuleWithClass(PistonAura.class);
            if ((Boolean)this.piston.getValue() && !pa.isEnable) {
               this.execute(() -> {
                  this.detectPA();
                  this.blockPA(obby);
               });
            }

            if ((Boolean)this.cev.getValue()) {
               this.execute(() -> {
                  this.blockCEV(obby);
               });
            }

            Module civ = CandyPlusRewrite.m_module.getModuleWithClass(CivBreaker.class);
            if ((Boolean)this.civ.getValue() && !civ.isEnable && !pa.isEnable) {
               this.execute(() -> {
                  this.blockCIV(obby);
               });
            }

            this.restoreItem();
         }
      }
   }

   public void execute(Runnable action) {
      try {
         action.run();
      } catch (Exception var3) {
      }

   }

   public void onRender3D() {
      try {
         if (this.pistonPos != null && (Boolean)this.piston.getValue() && (Boolean)this.render.getValue()) {
            Iterator var1 = this.pistonPos.iterator();

            while(var1.hasNext()) {
               BlockPos piston = (BlockPos)var1.next();
               RenderUtil3D.drawBox(piston, 1.0D, (Color)this.pistonColor.getValue(), 63);
            }
         }
      } catch (Exception var3) {
      }

   }

   public void blockCIV(int obby) {
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos[] array = new BlockPos[]{new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1), new BlockPos(1, 1, 1), new BlockPos(1, 1, -1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, -1)};
      BlockPos[] var5 = array;
      int var6 = array.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockPos offset = var5[var7];
         BlockPos base = mypos.add(offset);
         List<Entity> crystals = (List)mc.world.loadedEntityList.stream().filter((e) -> {
            return e instanceof EntityEnderCrystal;
         }).collect(Collectors.toList());
         if (BlockUtil.getBlock(base) == Blocks.OBSIDIAN) {
            Iterator var11 = crystals.iterator();

            while(var11.hasNext()) {
               Entity crystal = (Entity)var11.next();
               BlockPos crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
               if (base.equals(crystalPos.add(0, -1, 0))) {
                  mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                  this.detectedPosCiv.add(crystalPos);
               }
            }
         }
      }

      if (this.timerCiv.passedX((double)(Float)this.crystalDelayCIV.getValue())) {
         this.timerCiv.reset();
         Iterator poses = this.detectedPosCiv.iterator();

         while(poses.hasNext()) {
            BlockPos pos = (BlockPos)poses.next();
            if (BlockUtil.getBlock(pos) == Blocks.AIR) {
               this.setItem(obby);
               if (BlockUtil.getBlock(pos.add(0, -1, 0)) == Blocks.AIR) {
                  BlockUtil.placeBlock(pos.add(0, -1, 0), (Boolean)this.packetPlace.getValue());
               }

               BlockUtil.placeBlock(pos, (Boolean)this.packetPlace.getValue());
               poses.remove();
            }
         }
      }

   }

   public void blockCEV(int obby) {
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos ceilPos = mypos.add(0, 2, 0);
      if (this.placeTimer.passedX((double)(Float)this.crystalDelayCEV.getValue())) {
         if (this.stage == 1) {
            this.crystalTimerCEV.reset();
            this.setItem(obby);
            BlockUtil.placeBlock(ceilPos.add(0, 1, 0), (Boolean)this.packetPlace.getValue());
            this.stage = 0;
         }

         if (BlockUtil.getBlock(ceilPos) == Blocks.OBSIDIAN && CrystalUtil.hasCrystal(ceilPos) && (Boolean)this.teleportCEV.getValue() && this.limitCounterCEV < (Integer)this.limitCEV.getValue()) {
            this.crystalTimerCEV.reset();
            ++this.limitCounterCEV;
            mc.player.connection.sendPacket(new Position(mc.player.posX, (double)mypos.getY() + 0.2D, mc.player.posZ, false));
            this.breakCrystal(CrystalUtil.getCrystal(ceilPos));
            this.swingArm();
            this.stage = 1;
         } else {
            this.limitCounterCEV = 0;
         }
      }

   }

   public void blockPA(int obby) {
      BlockPos pos;
      if (this.crystalTimerPA.passedX((double)(Float)this.crystalDelayPA.getValue()) && (Boolean)this.piston.getValue() && (Boolean)this.breakCrystalPA.getValue() && this.PAcrystal != null) {
         this.crystalTimerPA.reset();
         if (this.PAcrystal.getEntityId() == this.oldCrystal) {
            ++this.limitCounterPA;
         } else {
            this.limitCounterPA = 0;
         }

         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         pos = new BlockPos(this.PAcrystal.posX, this.PAcrystal.posY, this.PAcrystal.posZ);
         if (mypos.getY() + 2 == pos.getY() && BlockUtil.getBlock(mypos.add(0, 2, 0)) == Blocks.OBSIDIAN && (Boolean)this.teleportPA.getValue() && this.limitCounterPA <= (Integer)this.limitPA.getValue()) {
            double offsetx = (double)(mypos.getX() - pos.getX()) * 0.4D;
            double offsetz = (double)(mypos.getZ() - pos.getZ()) * 0.4D;
            mc.player.connection.sendPacket(new Position((double)mypos.getX() + 0.5D + offsetx, (double)mypos.getY() + 0.2D, (double)mypos.getZ() + 0.5D + offsetz, false));
         }

         this.breakCrystal(this.PAcrystal);
         this.swingArm();
         this.oldCrystal = this.PAcrystal.getEntityId();
         this.PAcrystal = null;
      }

      if (this.placeTimer.passedX((double)(Float)this.placeDelay.getValue()) && (Boolean)this.piston.getValue()) {
         this.placeTimer.reset();
         Iterator pistons = this.pistonPos.iterator();

         while(pistons.hasNext()) {
            pos = (BlockPos)pistons.next();
            if (BlockUtil.getBlock(pos) == Blocks.AIR) {
               this.setItem(obby);
               if (BlockUtil.hasNeighbour(pos)) {
                  BlockUtil.placeBlock(pos, (Boolean)this.packetPlace.getValue());
               } else {
                  BlockUtil.placeBlock(pos.add(0, -1, 0), (Boolean)this.packetPlace.getValue());
                  BlockUtil.rightClickBlock(pos.add(0, -1, 0), EnumFacing.UP, (Boolean)this.packetPlace.getValue());
               }

               pistons.remove();
            }
         }
      }

   }

   public void detectPA() {
      List<BlockPos> tmp = new ArrayList();

      BlockPos mypos;
      for(Iterator iterator = this.pistonPos.iterator(); iterator.hasNext(); tmp.add(mypos)) {
         mypos = (BlockPos)iterator.next();
         if (tmp.contains(mypos) || PlayerUtil.getDistance(mypos) > (double)(Float)this.range.getValue()) {
            iterator.remove();
         }
      }

      mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};

      for(int y = 0; y <= (Integer)this.maxY.getValue(); ++y) {
         BlockPos[] var6 = offsets;
         int var7 = offsets.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockPos offset = var6[var8];
            BlockPos crystalPos = mypos.add(offset.getX(), y, offset.getZ());
            if (CrystalUtil.hasCrystal(crystalPos) || !(Boolean)this.crystalSync.getValue()) {
               List<BlockPos> pistonPos = new ArrayList();
               BlockPos noOldCandy = crystalPos.add(offset);
               BlockPos sidePos0 = crystalPos.add(offset.getZ(), 0, offset.getX());
               BlockPos sidePos2 = crystalPos.add(offset.getZ() * -1, 0, offset.getX() * -1);
               BlockPos noSushi0 = noOldCandy.add(offset);
               BlockPos noSushi2 = noOldCandy.add(offset.getZ(), 0, offset.getX());
               BlockPos noSushi3 = noOldCandy.add(offset.getZ() * -1, 0, offset.getX() * -1);
               BlockPos noSushi4 = noSushi2.add(offset);
               BlockPos noSushi5 = noSushi3.add(offset);
               this.add(pistonPos, noOldCandy);
               this.add(pistonPos, sidePos0);
               this.add(pistonPos, sidePos2);
               this.add(pistonPos, noSushi0);
               this.add(pistonPos, noSushi2);
               this.add(pistonPos, noSushi3);
               this.add(pistonPos, noSushi4);
               this.add(pistonPos, noSushi5);
               List<BlockPos> imNoob = new ArrayList();
               pistonPos.forEach((b) -> {
                  imNoob.add(b.add(0, 1, 0));
               });
               imNoob.forEach((b) -> {
                  pistonPos.add(b);
               });
               Iterator var21 = pistonPos.iterator();

               while(var21.hasNext()) {
                  BlockPos piston = (BlockPos)var21.next();
                  if (this.isPiston(piston)) {
                     pistonPos.add(piston);
                     if (CrystalUtil.hasCrystal(crystalPos)) {
                        this.PAcrystal = CrystalUtil.getCrystal(crystalPos);
                     }
                  }
               }
            }
         }
      }

   }

   public void add(List<BlockPos> target, BlockPos base) {
      target.add(base.add(0, 1, 0));
   }

   public boolean isPiston(BlockPos pos) {
      return BlockUtil.getBlock(pos) == Blocks.PISTON || BlockUtil.getBlock(pos) == Blocks.STICKY_PISTON;
   }

   public void swingArm() {
      EnumHand arm = this.swingArm.getValue() == Blocker.Arm.Offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
      if (this.swingArm.getValue() != Blocker.Arm.None) {
         mc.player.swingArm(arm);
      }

   }

   public void breakCrystal(Entity crystal) {
      if (crystal instanceof EntityEnderCrystal) {
         if ((Boolean)this.packetBreak.getValue()) {
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
         } else {
            mc.playerController.attackEntity(mc.player, crystal);
         }

      }
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

   public static enum Arm {
      Mainhand,
      Offhand,
      None;
   }
}

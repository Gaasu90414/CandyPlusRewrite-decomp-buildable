//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCity extends Module {
   public Setting<AutoCity.Target> targetType;
   public Setting<Float> targetRange;
   public Setting<Float> range;
   public Setting<Boolean> instantBreak;
   public Setting<Boolean> noSwing;
   public Setting<Boolean> switcH;
   public Setting<Boolean> noSuicide;
   public EntityPlayer target;
   public BlockPos breakPos;

   public AutoCity() {
      super("AutoCity", Module.Categories.COMBAT, false, false);
      this.targetType = this.register(new Setting("Target", AutoCity.Target.Nearest));
      this.targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
      this.range = this.register(new Setting("Range", 10.0F, 20.0F, 0.0F));
      this.instantBreak = this.register(new Setting("InstantBreak", true));
      this.noSwing = this.register(new Setting("NoSwing", false));
      this.switcH = this.register(new Setting("Switch", false));
      this.noSuicide = this.register(new Setting("NoSuicide", true));
      this.target = null;
      this.breakPos = null;
   }

   public void onEnable() {
      if (!this.nullCheck()) {
         this.target = this.findTarget();
         if (this.target == null) {
            this.sendMessage("Cannot find target! disabling...");
            this.disable();
         } else if (this.findSpace(this.target) == -1) {
            this.sendMessage("Cannot find space! disabling...");
            this.disable();
         } else {
            this.sendMessage("Breaking...");
            if (!(Boolean)this.instantBreak.getValue()) {
               if (!(Boolean)this.noSwing.getValue()) {
                  mc.player.swingArm(EnumHand.MAIN_HAND);
               }

               mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
               mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
            } else {
               if (!(Boolean)this.noSwing.getValue()) {
                  mc.player.swingArm(EnumHand.MAIN_HAND);
               }

               CandyPlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
               InstantMine.startBreak(this.breakPos, EnumFacing.DOWN);
            }

            if ((Boolean)this.switcH.getValue()) {
               int pickel = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
               if (pickel == -1) {
                  return;
               }

               mc.player.inventory.currentItem = pickel;
               mc.playerController.updateController();
            }

            this.disable();
         }
      }
   }

   public int findSpace(EntityPlayer target) {
      BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
      BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      List<AutoCity.CitySpace> spaces = new ArrayList();
      BlockPos s = null;
      BlockPos[] var7 = offsets;
      int var8 = offsets.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         BlockPos offset = var7[var9];
         AutoCity.CitySpace pos = new AutoCity.CitySpace();
         BlockPos breakPos = base.add(offset);
         if (BlockUtil.getBlock(breakPos) == Blocks.OBSIDIAN || BlockUtil.getBlock(breakPos) == Blocks.ENDER_CHEST) {
            if ((Boolean)this.noSuicide.getValue()) {
               boolean shouldSkip = false;
               BlockPos[] array2 = offsets;
               int length2 = offsets.length;

               for(int j = 0; j < length2; ++j) {
                  s = array2[j];
                  BlockPos spos = mypos.add(s);
                  if (spos.equals(breakPos)) {
                     shouldSkip = true;
                  }
               }

               if (shouldSkip) {
                  continue;
               }
            }

            pos.setPos(breakPos);
            BlockPos levelPos = breakPos.add(offset);
            if (BlockUtil.getBlock(levelPos) != Blocks.AIR) {
               pos.setLevel(0);
            } else if (BlockUtil.getBlock(levelPos.add(0, 1, 0)) != Blocks.AIR) {
               pos.setLevel(1);
            } else {
               pos.setLevel(2);
            }

            spaces.add(pos);
         }
      }

      AutoCity.CitySpace space = (AutoCity.CitySpace)spaces.stream().filter((s2) -> {
         return PlayerUtil.getDistance(s2.pos) <= (double)(Float)this.range.getValue();
      }).max(Comparator.comparing((s2) -> {
         return (double)s2.level + ((double)(Float)this.range.getValue() - PlayerUtil.getDistance(s2.pos));
      })).orElse((CitySpace) null);
      if (space == null) {
         return -1;
      } else {
         this.breakPos = space.pos;
         return space.level;
      }
   }

   public EntityPlayer findTarget() {
      EntityPlayer target = null;
      List<EntityPlayer> players = new ArrayList(mc.world.playerEntities);
      players.removeIf((p) -> {
         return p == mc.player || FriendManager.isFriend(p.getName());
      });
      if (this.targetType.getValue() == AutoCity.Target.Nearest) {
         target = PlayerUtil.getNearestPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AutoCity.Target.Looking) {
         target = PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AutoCity.Target.Best) {
         target = (EntityPlayer)players.stream().max(Comparator.comparing(this::findSpace)).orElse((EntityPlayer) null);
      }

      return target;
   }

   public class CitySpace {
      public BlockPos pos;
      public int level = -1;

      public void setPos(BlockPos pos) {
         this.pos = pos;
      }

      public void setLevel(int level) {
         this.level = level;
      }
   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

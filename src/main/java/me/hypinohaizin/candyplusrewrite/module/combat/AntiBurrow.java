//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiBurrow extends Module {
   public Setting<AntiBurrow.Target> targetType;
   public Setting<Float> targetRange;
   public Setting<Float> range;
   public Setting<Boolean> instantBreak;
   public Setting<Boolean> noSwing;
   public Setting<Boolean> switcH;
   public Setting<Boolean> obby;
   public Setting<Boolean> echest;
   public EntityPlayer target;
   public BlockPos breakPos;

   public AntiBurrow() {
      super("AntiBurrow", Module.Categories.COMBAT, false, false);
      this.targetType = this.register(new Setting("Target", AntiBurrow.Target.Nearest));
      this.targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
      this.range = this.register(new Setting("Range", 10.0F, 20.0F, 0.0F));
      this.instantBreak = this.register(new Setting("InstantBreak", true));
      this.noSwing = this.register(new Setting("NoSwing", false));
      this.switcH = this.register(new Setting("Switch", false));
      this.obby = this.register(new Setting("Obby", false));
      this.echest = this.register(new Setting("EChest", false));
      this.target = null;
      this.breakPos = null;
   }

   public void onEnable() {
      if (!this.nullCheck()) {
         this.target = this.findTarget();
         if (this.target == null) {
            this.sendMessage("Cannot find target! disabling");
            this.disable();
         } else if (!this.canBreak(this.getPos(this.target))) {
            this.sendMessage("Target is not in block! disabling");
         } else {
            this.breakPos = this.getPos(this.target);
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

   public EntityPlayer findTarget() {
      EntityPlayer target = null;
      List<EntityPlayer> players = new ArrayList(mc.world.playerEntities);
      if (this.targetType.getValue() == AntiBurrow.Target.Nearest) {
         target = PlayerUtil.getNearestPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AntiBurrow.Target.Looking) {
         target = PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
      }

      if (this.targetType.getValue() == AntiBurrow.Target.Best) {
         target = (EntityPlayer)players.stream().filter((e) -> {
            return this.canBreak(this.getPos(e));
         }).min(Comparator.comparing(PlayerUtil::getDistance)).orElse(null);
      }

      return target;
   }

   public BlockPos getPos(EntityPlayer player) {
      return new BlockPos(player.posX, player.posY, player.posZ);
   }

   public boolean canBreak(BlockPos pos) {
      Block block = BlockUtil.getBlock(pos);
      boolean can = block == Blocks.ANVIL;
      if (block == Blocks.ENDER_CHEST && (Boolean)this.echest.getValue()) {
         can = true;
      }

      if (block == Blocks.OBSIDIAN && (Boolean)this.obby.getValue()) {
         can = true;
      }

      return can;
   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

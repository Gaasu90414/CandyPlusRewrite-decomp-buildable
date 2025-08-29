//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCityRewrite extends Module {
   public Setting<AutoCityRewrite.Target> targetType;
   public Setting<Float> targetRange;
   public Setting<Float> range;
   public Setting<Boolean> instantBreak;
   public Setting<Boolean> noSwing;
   public Setting<Boolean> switcH;
   public Setting<Boolean> noSuicide;
   public Setting<Boolean> burrow;
   public Setting<Boolean> multi;
   public Setting<Integer> delay;
   public Setting<Boolean> tick;
   public Setting<Color> renderColor;
   public Setting<Boolean> outline;
   public Setting<Float> width;
   public EntityPlayer target;
   public BlockPos breakPos;
   public Timer timer = new Timer();

   public AutoCityRewrite() {
      super("AutoCityRewrite", Module.Categories.COMBAT, false, false);
      this.targetType = this.register(new Setting("Target", AutoCityRewrite.Target.Nearest));
      this.targetRange = this.register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
      this.range = this.register(new Setting("Range", 6.0F, 12.0F, 1.0F));
      this.instantBreak = this.register(new Setting("InstantBreak", true));
      this.noSwing = this.register(new Setting("NoSwing", false));
      this.switcH = this.register(new Setting("Switch", false));
      this.noSuicide = this.register(new Setting("NoSuicide", true));
      this.burrow = this.register(new Setting("MineBurrow", true));
      this.multi = this.register(new Setting("Multi", false));
      this.delay = this.register(new Setting("Delay", 200, 0, 1000));
      this.tick = this.register(new Setting("Tick", true));
      this.renderColor = this.register(new Setting("Color", new Color(230, 50, 50, 100)));
      this.outline = this.register(new Setting("Outline", false));
      this.width = this.register(new Setting("Width", 3.0F, 6.0F, 0.1F, (v) -> {
         return (Boolean)this.outline.getValue();
      }));
   }

   public void onEnable() {
      this.reset();
   }

   public void onDisable() {
      this.reset();
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if ((Boolean)this.tick.getValue()) {
            this.run();
         }

      }
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         if (!(Boolean)this.tick.getValue()) {
            this.run();
         }

      }
   }

   private void run() {
      if (!this.nullCheck() && this.timer.passedMs((long)(Integer)this.delay.getValue())) {
         this.target = this.findTarget();
         if (this.target == null) {
            this.disable();
         } else {
            BlockPos base = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
            if ((Boolean)this.burrow.getValue() && this.isBurrowed(this.target)) {
               this.mineBlock(base);
               this.breakPos = base;
               this.timer.reset();
            } else {
               List<BlockPos> cityList = this.findCityBlocks(this.target);
               if (cityList.isEmpty()) {
                  this.sendMessage("Cannot find cityable block! disabling...");
                  this.disable();
               } else {
                  BlockPos pos;
                  if ((Boolean)this.multi.getValue()) {
                     for(Iterator var3 = cityList.iterator(); var3.hasNext(); this.breakPos = pos) {
                        pos = (BlockPos)var3.next();
                        this.mineBlock(pos);
                     }
                  } else {
                     this.mineBlock((BlockPos)cityList.get(0));
                     this.breakPos = (BlockPos)cityList.get(0);
                  }

                  this.timer.reset();
                  if (!(Boolean)this.tick.getValue()) {
                     this.disable();
                  }

               }
            }
         }
      }
   }

   private boolean isBurrowed(EntityPlayer player) {
      BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
      Block block = mc.world.getBlockState(pos).getBlock();
      return block != Blocks.AIR && block != Blocks.BEDROCK && block != Blocks.WATER && block != Blocks.LAVA;
   }

   private List<BlockPos> findCityBlocks(EntityPlayer target) {
      List<BlockPos> result = new ArrayList();
      BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
      BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
      BlockPos[] var5 = offsets;
      int var6 = offsets.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockPos offset = var5[var7];
         BlockPos breakPos = base.add(offset);
         Block block = mc.world.getBlockState(breakPos).getBlock();
         if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST) {
            BlockPos up1;
            if ((Boolean)this.noSuicide.getValue()) {
               up1 = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
               if (up1.equals(breakPos)) {
                  continue;
               }
            }

            up1 = breakPos.up();
            BlockPos up2 = up1.up();
            if (mc.world.getBlockState(up1).getBlock() == Blocks.AIR && mc.world.getBlockState(up2).getBlock() == Blocks.AIR) {
               result.add(breakPos);
            }
         }
      }

      return result;
   }

   private void mineBlock(BlockPos pos) {
      if ((Boolean)this.switcH.getValue()) {
         int pickel = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
         if (pickel != -1) {
            mc.player.inventory.currentItem = pickel;
            mc.playerController.updateController();
         }
      }

      if (!(Boolean)this.instantBreak.getValue()) {
         if (!(Boolean)this.noSwing.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
         }

         mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
         mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
      } else {
         if (!(Boolean)this.noSwing.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
         }

         CandyPlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
         InstantMine.startBreak(pos, EnumFacing.DOWN);
      }

   }

   private EntityPlayer findTarget() {
      List<EntityPlayer> players = new ArrayList(mc.world.playerEntities);
      players.removeIf((p) -> {
         return p == mc.player || FriendManager.isFriend(p.getName()) || p.isDead || p.getHealth() <= 0.0F;
      });
      if (players.isEmpty()) {
         return null;
      } else if (this.targetType.getValue() == AutoCityRewrite.Target.Nearest) {
         return (EntityPlayer)players.stream().min(Comparator.comparing((p) -> {
            return mc.player.getDistance(p);
         })).orElse((EntityPlayer) null);
      } else if (this.targetType.getValue() == AutoCityRewrite.Target.Looking) {
         return PlayerUtil.getLookingPlayer((double)(Float)this.targetRange.getValue());
      } else {
         return this.targetType.getValue() == AutoCityRewrite.Target.Best ? (EntityPlayer)players.stream().max(Comparator.comparing(this::scoreCity)).orElse((EntityPlayer) null) : null;
      }
   }

   private int scoreCity(EntityPlayer p) {
      List<BlockPos> list = this.findCityBlocks(p);
      return list.size();
   }

   private void reset() {
      this.target = null;
      this.breakPos = null;
      this.timer.reset();
   }

   public void onRender3D() {
      if (this.breakPos != null) {
         RenderUtil3D.drawBox(this.breakPos, 1.0D, (Color)this.renderColor.getValue(), 63);
         if ((Boolean)this.outline.getValue()) {
            RenderUtil3D.drawBoundingBox(this.breakPos, 1.0D, (Float)this.width.getValue(), (Color)this.renderColor.getValue());
         }
      }

   }

   public static enum Target {
      Nearest,
      Looking,
      Best;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.CrystalUtil;
import me.hypinohaizin.candyplusrewrite.utils.InventoryUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalAura extends Module {
   public Setting<Boolean> place = this.register(new Setting("Place", true));
   public Setting<Float> placeDelay = this.register(new Setting("PlaceDelay", 6.0F, 16.0F, 0.0F, (v) -> {
      return (Boolean)this.place.getValue();
   }));
   public Setting<Float> placeRange = this.register(new Setting("PlaceRange", 7.0F, 16.0F, 1.0F, (v) -> {
      return (Boolean)this.place.getValue();
   }));
   public Setting<Float> wallRangePlace = this.register(new Setting("WallRangePlace", 4.0F, 16.0F, 1.0F, (v) -> {
      return (Boolean)this.place.getValue();
   }));
   public Setting<Boolean> placeSwing = this.register(new Setting("Swing", false));
   public Setting<Boolean> autoSwitch = this.register(new Setting("Switch", true));
   public Setting<Boolean> silentSwitch = this.register(new Setting("SilentSwitch", false, (v) -> {
      return (Boolean)this.autoSwitch.getValue();
   }));
   public Setting<Boolean> opPlace = this.register(new Setting("1.13", false, (v) -> {
      return (Boolean)this.place.getValue();
   }));
   public Setting<Boolean> explode = this.register(new Setting("Explode", true));
   public Setting<Boolean> predict = this.register(new Setting("Predict", true));
   public Setting<Float> explodeDelay = this.register(new Setting("ExplodeDelay", 6.0F, 16.0F, 0.0F, (v) -> {
      return (Boolean)this.explode.getValue();
   }));
   public Setting<Float> breakRange = this.register(new Setting("ExplodeRange", 6.0F, 16.0F, 1.0F, (v) -> {
      return (Boolean)this.explode.getValue();
   }));
   public Setting<Float> wallRangeBreak = this.register(new Setting("WallRangeBreak", 3.0F, 16.0F, 1.0F, (v) -> {
      return (Boolean)this.explode.getValue();
   }));
   public Setting<Boolean> packetBreak = this.register(new Setting("PacketBreak", true));
   public Setting<CrystalAura.swingArm> swing;
   public Setting<Boolean> packetSwing;
   public Setting<Boolean> predictHit;
   public Setting<Integer> amount;
   public Setting<Integer> amountOffset;
   public Setting<Boolean> checkOtherEntity;
   public Setting<Boolean> ignoreSelfDmg;
   public Setting<Float> maxSelf;
   public Setting<Float> minDmg;
   public Setting<Boolean> smartMode;
   public Setting<Float> dmgError;
   public Setting<Boolean> antiSuicide;
   public Setting<Float> pauseHealth;
   public Setting<Boolean> betterFps;
   public Setting<Color> color;
   public Setting<Boolean> avoidFriends;
   public EntityPlayer target;
   public int lastEntityID;
   public Timer placeTimer;
   public Timer breakTimer;
   private EnumHand oldhand;
   private int oldslot;
   public BlockPos renderPos = null;

   public CrystalAura() {
      super("CrystalAura", Module.Categories.COMBAT, false, false);
      this.swing = this.register(new Setting("SwingArm", CrystalAura.swingArm.Mainhand));
      this.packetSwing = this.register(new Setting("PacketSwing", true, (v) -> {
         return this.swing.getValue() != CrystalAura.swingArm.None;
      }));
      this.predictHit = this.register(new Setting("PredictHit", false));
      this.amount = this.register(new Setting("Amount", 1, 15, 1, (v) -> {
         return (Boolean)this.predictHit.getValue();
      }));
      this.amountOffset = this.register(new Setting("Offset", 1, 10, 0, (v) -> {
         return (Boolean)this.predictHit.getValue();
      }));
      this.checkOtherEntity = this.register(new Setting("OtherEntity", false, (v) -> {
         return (Boolean)this.predictHit.getValue();
      }));
      this.ignoreSelfDmg = this.register(new Setting("IgnoreSelfDamage", false));
      this.maxSelf = this.register(new Setting("MaxSelfDamage", 5.0F, 36.0F, 0.0F, (v) -> {
         return !(Boolean)this.ignoreSelfDmg.getValue();
      }));
      this.minDmg = this.register(new Setting("MinDamage", 3.0F, 36.0F, 0.0F));
      this.smartMode = this.register(new Setting("SmartMode", true));
      this.dmgError = this.register(new Setting("DamageError", 3.0F, 15.0F, 1.0F, (v) -> {
         return (Boolean)this.smartMode.getValue();
      }));
      this.antiSuicide = this.register(new Setting("AntiSuicide", true));
      this.pauseHealth = this.register(new Setting("RequireHealth", 3.0F, 36.0F, 0.0F));
      this.betterFps = this.register(new Setting("BetterFps", true));
      this.color = this.register(new Setting("Color", new Color(230, 50, 50, 100)));
      this.avoidFriends = this.register(new Setting("AvoidFriends", true));
      this.lastEntityID = -1;
      this.placeTimer = new Timer();
      this.breakTimer = new Timer();
      this.oldhand = null;
      this.oldslot = -1;
   }

   public void onTick() {
      this.doCrystalAura();
   }

   public void doCrystalAura() {
      try {
         if (this.nullCheck()) {
            return;
         }

         this.target = this.getNearestEnemy(35.0D);
         if (this.target == null) {
            this.renderPos = null;
            return;
         }

         if ((Float)this.pauseHealth.getValue() > mc.player.getHealth()) {
            this.renderPos = null;
            return;
         }

         if ((Boolean)this.place.getValue()) {
            this.doPlace();
         }

         if ((Boolean)this.explode.getValue()) {
            this.doBreak();
         }
      } catch (Exception var2) {
      }

   }

   public void doPlace() {
      if (this.placeTimer.passedDms((double)(Float)this.placeDelay.getValue())) {
         this.placeTimer.reset();
         EnumHand hand;
         if ((Boolean)this.autoSwitch.getValue()) {
            if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
               hand = EnumHand.OFF_HAND;
            } else {
               int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
               if (crystalSlot == -1) {
                  this.renderPos = null;
                  return;
               }

               this.setItem(crystalSlot);
               hand = EnumHand.MAIN_HAND;
            }
         } else if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            hand = EnumHand.MAIN_HAND;
         } else {
            if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
               this.renderPos = null;
               return;
            }

            hand = EnumHand.OFF_HAND;
         }

         double maxDmg = 0.0D;
         BlockPos bestPos = null;
         Iterator var5 = CrystalUtil.possiblePlacePositions((Float)this.placeRange.getValue(), true, (Boolean)this.opPlace.getValue()).iterator();

         while(true) {
            BlockPos pos;
            double enemyDamage;
            double selfDamage;
            do {
               do {
                  do {
                     EntityPlayer near;
                     do {
                        do {
                           if (!var5.hasNext()) {
                              if (bestPos == null) {
                                 this.renderPos = null;
                                 this.restoreItem();
                                 return;
                              }

                              this.renderPos = bestPos;
                              mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bestPos, EnumFacing.UP, hand, 0.5F, 0.5F, 0.5F));
                              if ((Boolean)this.placeSwing.getValue()) {
                                 mc.player.connection.sendPacket(new CPacketAnimation(hand));
                              }

                              if ((Boolean)this.predictHit.getValue() && (Boolean)this.packetBreak.getValue() && this.lastEntityID != -1) {
                                 for(int i = (Integer)this.amountOffset.getValue(); i < (Integer)this.amount.getValue(); ++i) {
                                    Entity e = mc.world.getEntityByID(this.lastEntityID + i);
                                    if (e instanceof EntityEnderCrystal) {
                                       mc.player.connection.sendPacket(new CPacketUseEntity(e));
                                       EnumHand h = this.swing.getValue() == CrystalAura.swingArm.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                                       if (this.swing.getValue() != CrystalAura.swingArm.None) {
                                          if ((Boolean)this.packetSwing.getValue()) {
                                             mc.player.connection.sendPacket(new CPacketAnimation(h));
                                          } else {
                                             mc.player.swingArm(h);
                                          }
                                       }
                                    }
                                 }
                              }

                              this.restoreItem();
                              return;
                           }

                           pos = (BlockPos)var5.next();
                        } while(!CrystalUtil.canSeePos(pos) && PlayerUtil.getDistance(pos) > (double)(Float)this.wallRangePlace.getValue());

                        if (!(Boolean)this.avoidFriends.getValue()) {
                           break;
                        }

                        near = this.getClosestEnemy(pos, 4.0D);
                     } while(near == null);

                     selfDamage = (double)CrystalUtil.calculateDamage((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D, mc.player);
                  } while(selfDamage > (double)(Float)this.maxSelf.getValue() && !(Boolean)this.ignoreSelfDmg.getValue());

                  enemyDamage = (double)CrystalUtil.calculateDamage((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D, this.target);
               } while(enemyDamage < (double)(Float)this.minDmg.getValue());
            } while(selfDamage > enemyDamage && Math.abs(selfDamage - enemyDamage) >= (double)(Float)this.dmgError.getValue() && (Boolean)this.smartMode.getValue());

            if (enemyDamage > maxDmg) {
               maxDmg = enemyDamage;
               bestPos = pos;
            }
         }
      }
   }

   public void doBreak() {
      if (this.breakTimer.passedDms((double)(Float)this.explodeDelay.getValue())) {
         this.breakTimer.reset();
         AxisAlignedBB box = new AxisAlignedBB(mc.player.posX - (double)(Float)this.breakRange.getValue(), mc.player.posY - (double)(Float)this.breakRange.getValue(), mc.player.posZ - (double)(Float)this.breakRange.getValue(), mc.player.posX + (double)(Float)this.breakRange.getValue(), mc.player.posY + (double)(Float)this.breakRange.getValue(), mc.player.posZ + (double)(Float)this.breakRange.getValue());
         List<EntityEnderCrystal> crystals = mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, box);
         EntityEnderCrystal bestCrystal = (EntityEnderCrystal)crystals.stream().filter((c) -> {
            return this.isValidCrystal(c.posX, c.posY, c.posZ);
         }).min(Comparator.comparingDouble(PlayerUtil::getDistance)).orElse((EntityEnderCrystal) null);
         if (bestCrystal != null) {
            if ((Boolean)this.packetBreak.getValue()) {
               mc.player.connection.sendPacket(new CPacketUseEntity(bestCrystal));
            } else {
               mc.playerController.attackEntity(mc.player, bestCrystal);
            }

            EnumHand hand = this.swing.getValue() == CrystalAura.swingArm.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            if (this.swing.getValue() != CrystalAura.swingArm.None) {
               if ((Boolean)this.packetSwing.getValue()) {
                  mc.player.connection.sendPacket(new CPacketAnimation(hand));
               } else {
                  mc.player.swingArm(hand);
               }
            }

         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketSpawnObject) {
         SPacketSpawnObject p = (SPacketSpawnObject)event.getPacket();
         if (p.getType() == 51 && (Boolean)this.explode.getValue() && (Boolean)this.predict.getValue() && (Boolean)this.packetBreak.getValue() && this.target != null) {
            this.lastEntityID = p.getEntityID();
            if (!this.isValidCrystal(p.getX(), p.getY(), p.getZ())) {
               return;
            }

            Iterator var3 = mc.world.loadedEntityList.iterator();

            while(var3.hasNext()) {
               Entity entity = (Entity)var3.next();
               if (entity instanceof EntityEnderCrystal) {
                  double dx = entity.posX - p.getX();
                  double dy = entity.posY - p.getY();
                  double dz = entity.posZ - p.getZ();
                  if (dx * dx + dy * dy + dz * dz < 1.0D) {
                     mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                     EnumHand hand = this.swing.getValue() == CrystalAura.swingArm.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                     if (this.swing.getValue() != CrystalAura.swingArm.None) {
                        if ((Boolean)this.packetSwing.getValue()) {
                           mc.player.connection.sendPacket(new CPacketAnimation(hand));
                        } else {
                           mc.player.swingArm(hand);
                        }
                     }
                     break;
                  }
               }
            }
         }
      }

      if ((Boolean)this.checkOtherEntity.getValue()) {
         if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.lastEntityID = ((SPacketSpawnExperienceOrb)event.getPacket()).getEntityID();
         }

         if (event.getPacket() instanceof SPacketSpawnMob) {
            this.lastEntityID = ((SPacketSpawnMob)event.getPacket()).getEntityID();
         }

         if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.lastEntityID = ((SPacketSpawnPainting)event.getPacket()).getEntityID();
         }

         if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.lastEntityID = ((SPacketSpawnPlayer)event.getPacket()).getEntityID();
         }
      }

   }

   public boolean isValidCrystal(double posX, double posY, double posZ) {
      if (this.target == null) {
         return false;
      } else if ((Boolean)this.avoidFriends.getValue() && FriendManager.isFriend(this.target.getName())) {
         return false;
      } else {
         BlockPos pos = new BlockPos(posX, posY, posZ);
         if (PlayerUtil.getDistance(pos) > (double)(Float)this.breakRange.getValue()) {
            return false;
         } else if (!CrystalUtil.canSeePos(pos) && PlayerUtil.getDistance(pos) > (double)(Float)this.wallRangeBreak.getValue()) {
            return false;
         } else {
            double selfDamage = (double)CrystalUtil.calculateDamage(posX, posY, posZ, mc.player);
            if (selfDamage > (double)(Float)this.maxSelf.getValue() && !(Boolean)this.ignoreSelfDmg.getValue()) {
               return false;
            } else if ((double)mc.player.getHealth() - selfDamage <= 0.0D && (Boolean)this.antiSuicide.getValue()) {
               return false;
            } else {
               double enemyDamage = (double)CrystalUtil.calculateDamage(posX, posY, posZ, this.target);
               return enemyDamage >= (double)(Float)this.minDmg.getValue() && (selfDamage <= enemyDamage || Math.abs(selfDamage - enemyDamage) < (double)(Float)this.dmgError.getValue() || !(Boolean)this.smartMode.getValue());
            }
         }
      }
   }

   public void setItem(int slot) {
      if ((Boolean)this.autoSwitch.getValue()) {
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

   public void onRender3D() {
      if (this.renderPos != null) {
         RenderUtil3D.drawBox(this.renderPos, 1.0D, (Color)this.color.getValue(), 63);
      }

   }

   private EntityPlayer getNearestEnemy(double maxRange) {
      List<EntityPlayer> list = (List)mc.world.playerEntities.stream().filter((p) -> {
         return p != null && p != mc.player && !p.isDead && !FriendManager.isFriend(p.getName());
      }).filter((p) -> {
         return PlayerUtil.getDistance((Entity)p) <= maxRange;
      }).sorted(Comparator.comparingDouble(PlayerUtil::getDistance)).collect(Collectors.toList());
      return list.isEmpty() ? null : (EntityPlayer)list.get(0);
   }

   private EntityPlayer getClosestEnemy(BlockPos pos, double radius) {
      double r2 = radius * radius;
      EntityPlayer closest = null;
      double best = Double.MAX_VALUE;
      Iterator var9 = mc.world.playerEntities.iterator();

      while(var9.hasNext()) {
         EntityPlayer p = (EntityPlayer)var9.next();
         if (p != null && p != mc.player && !p.isDead && !FriendManager.isFriend(p.getName())) {
            double d = p.getDistanceSq(pos);
            if (d <= r2 && d < best) {
               best = d;
               closest = p;
            }
         }
      }

      return closest;
   }

   public class CrystalPos {
      public BlockPos pos;
      public double dmg;

      public CrystalPos(BlockPos pos, double dmg) {
         this.pos = pos;
         this.dmg = dmg;
      }
   }

   public static enum swingArm {
      Mainhand,
      Offhand,
      None;
   }
}

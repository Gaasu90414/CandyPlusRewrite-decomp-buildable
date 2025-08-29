//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;

public class Aura extends Module {
   public Setting<Boolean> targetPlayer = this.register(new Setting("Player", true));
   public Setting<Boolean> targetMob = this.register(new Setting("Mob", false));
   public Setting<Boolean> targetPassive = this.register(new Setting("Passive", false));
   public Setting<Boolean> targetNeutral = this.register(new Setting("Neutral", false));
   public Setting<Boolean> targetHostile = this.register(new Setting("Hostile", false));
   public Setting<Float> range = this.register(new Setting("Range", 4.5F, 10.0F, 1.0F));
   public Setting<Float> wallRange = this.register(new Setting("WallRange", 3.0F, 6.0F, 1.0F));
   public Setting<Boolean> preferAxe = this.register(new Setting("PreferAxe", true));
   public Setting<Float> enemyHp = this.register(new Setting("EnemyHP", 6.0F, 20.0F, 0.0F));
   public Setting<Float> minSelfHp = this.register(new Setting("MinSelfHP", 6.0F, 20.0F, 0.0F));
   public Setting<Integer> delayMs = this.register(new Setting("DelayMs", 50, 1000, 0));
   public Setting<Boolean> autoSwap = this.register(new Setting("AutoSwap", true));
   public Setting<Boolean> swordOnly = this.register(new Setting("SwordOnly", true));
   public Setting<Boolean> criticals = this.register(new Setting("Criticals", true));
   public Setting<Integer> critPackets = this.register(new Setting("CritPackets", 2, 1, 4));
   private long lastAttackTime = 0L;

   public Aura() {
      super("Aura", Module.Categories.COMBAT, false, false);
   }

   public void onEnable() {
      if ((Boolean)this.autoSwap.getValue()) {
         int swordSlot = this.findWeaponSlot(ItemSword.class);
         if (swordSlot != -1) {
            mc.player.inventory.currentItem = swordSlot;
            mc.playerController.updateController();
         }
      }

   }

   public void onUpdate() {
      if (mc.player != null && mc.world != null) {
         if (!(mc.player.getHealth() <= (Float)this.minSelfHp.getValue())) {
            if (!(Boolean)this.swordOnly.getValue() || mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
               List<EntityLivingBase> possibleTargets = new ArrayList();
               EntityLivingBase lowHpTarget = null;
               List<EntityLivingBase> entities = mc.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(mc.player.posX - (double)(Float)this.range.getValue(), mc.player.posY - (double)(Float)this.range.getValue(), mc.player.posZ - (double)(Float)this.range.getValue(), mc.player.posX + (double)(Float)this.range.getValue(), mc.player.posY + (double)(Float)this.range.getValue(), mc.player.posZ + (double)(Float)this.range.getValue()));
               Iterator var4 = entities.iterator();

               while(var4.hasNext()) {
                  EntityLivingBase entity = (EntityLivingBase)var4.next();
                  if (entity != mc.player && this.isValidTarget(entity)) {
                     double distSq = mc.player.getDistanceSq(entity);
                     if (!(distSq > (double)((Float)this.range.getValue() * (Float)this.range.getValue())) && (this.canSeeEntity(entity) || !(distSq > (double)((Float)this.wallRange.getValue() * (Float)this.wallRange.getValue())))) {
                        possibleTargets.add(entity);
                        if (entity.getHealth() < (Float)this.enemyHp.getValue()) {
                           lowHpTarget = entity;
                           break;
                        }
                     }
                  }
               }

               EntityLivingBase target;
               if (lowHpTarget != null) {
                  target = lowHpTarget;
               } else {
                  if (possibleTargets.isEmpty()) {
                     return;
                  }

                  EntityPlayerSP var10001 = mc.player;
                  var10001.getClass();
                  possibleTargets.sort(Comparator.comparingDouble(var10001::getDistanceSq));
                  target = (EntityLivingBase)possibleTargets.get(0);
               }

               long currentTime = System.currentTimeMillis();
               if (currentTime - this.lastAttackTime >= (long)(Integer)this.delayMs.getValue()) {
                  if ((Boolean)this.criticals.getValue() && mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava()) {
                     this.sendCriticalPackets((Integer)this.critPackets.getValue());
                  }

                  mc.playerController.attackEntity(mc.player, target);
                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  this.lastAttackTime = currentTime;
               }
            }
         }
      }
   }

   private void sendCriticalPackets(int mode) {
      switch(mode) {
      case 1:
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.10000000149011612D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         break;
      case 2:
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 1.1E-5D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         break;
      case 3:
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.0125D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         break;
      case 4:
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.1625D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 4.0E-6D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 1.0E-6D, mc.player.posZ, false));
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
      }

   }

   private boolean isValidTarget(EntityLivingBase entity) {
      if (entity != null && !entity.isDead && entity.isEntityAlive()) {
         if (entity instanceof EntityPlayer) {
            if (!(Boolean)this.targetPlayer.getValue()) {
               return false;
            } else if (entity == mc.player) {
               return false;
            } else {
               return !FriendManager.isFriend(entity.getName());
            }
         } else {
            return (Boolean)this.targetMob.getValue();
         }
      } else {
         return false;
      }
   }

   private boolean canSeeEntity(EntityLivingBase entity) {
      Vec3d eyesPos = mc.player.getPositionEyes(1.0F);
      Vec3d entityPos = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
      RayTraceResult result = mc.world.rayTraceBlocks(eyesPos, entityPos, false, true, false);
      return result == null || result.typeOfHit == Type.MISS;
   }

   private int findWeaponSlot(Class<?> weaponClass) {
      for(int i = 0; i < 9; ++i) {
         if (weaponClass.isAssignableFrom(mc.player.inventory.getStackInSlot(i).getItem().getClass())) {
            return i;
         }
      }

      return -1;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class CrystalUtil implements Util {
   public static Boolean getArmourFucker(EntityPlayer player, float percent) {
      ItemStack stack;
      float var4;
      for(Iterator var2 = player.getArmorInventoryList().iterator(); var2.hasNext(); var4 = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0F) {
         stack = (ItemStack)var2.next();
         if (stack == null || stack.getItem() == Items.AIR) {
            return true;
         }
      }

      return false;
   }

   public static boolean canPlaceCrystalForPistonAura(BlockPos pos) {
      return BlockUtil.getBlock(pos.add(0, 1, 0)) == Blocks.AIR && BlockUtil.getBlock(pos.add(0, 2, 0)) == Blocks.AIR;
   }

   public static boolean canSeePos(BlockPos pos) {
      return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), false, true, false) == null;
   }

   public static boolean canPlaceCrystal(BlockPos pos) {
      return BlockUtil.getBlock(pos.add(0, 1, 0)) == Blocks.AIR && BlockUtil.getBlock(pos.add(0, 2, 0)) == Blocks.AIR;
   }

   public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
      NonNullList<BlockPos> positions = NonNullList.create();
      List<BlockPos> filtered = (List)getSphere(PlayerUtil.getPlayerPos(), placeRange, (int)placeRange, false, true, 0).stream().filter((pos) -> {
         return mc.world.getBlockState(pos).getBlock() != Blocks.AIR;
      }).filter((pos) -> {
         return canPlaceCrystal(pos, specialEntityCheck, oneDot15);
      }).collect(Collectors.toList());
      positions.addAll(filtered);
      return positions;
   }

   public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      ArrayList<BlockPos> circleblocks = new ArrayList();
      int cx = pos.getX();
      int cy = pos.getY();
      int cz = pos.getZ();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            int y = sphere ? cy - (int)r : cy;

            while(true) {
               float f = (float)y;
               float f2 = sphere ? (float)cy + r : (float)(cy + h);
               if (f >= f2) {
                  break;
               }

               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0F) * (r - 1.0F)))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }

               ++y;
            }
         }
      }

      return circleblocks;
   }

   public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean onepointThirteen) {
      BlockPos boost = blockPos.add(0, 1, 0);
      BlockPos boost2 = blockPos.add(0, 2, 0);

      try {
         Iterator var5;
         Entity entity;
         if (!onepointThirteen) {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
               return false;
            }

            if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
               return false;
            }

            if (!specialEntityCheck) {
               return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }

            var5 = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).iterator();

            while(var5.hasNext()) {
               entity = (Entity)var5.next();
               if (!(entity instanceof EntityEnderCrystal)) {
                  return false;
               }
            }

            var5 = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).iterator();

            while(var5.hasNext()) {
               entity = (Entity)var5.next();
               if (!(entity instanceof EntityEnderCrystal)) {
                  return false;
               }
            }
         } else {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
               return false;
            }

            if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
               return false;
            }

            if (!specialEntityCheck) {
               return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
            }

            var5 = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).iterator();

            while(var5.hasNext()) {
               entity = (Entity)var5.next();
               if (!(entity instanceof EntityEnderCrystal)) {
                  return false;
               }
            }
         }

         return true;
      } catch (Exception var7) {
         var7.printStackTrace();
         return false;
      }
   }

   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
      float doubleExplosionSize = 12.0F;
      double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = 0.0D;

      try {
         blockDensity = (double)entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
      } catch (Exception var18) {
      }

      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
      double finald = 1.0D;
      if (entity instanceof EntityLivingBase) {
         finald = (double)getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.world, (Entity)null, posX, posY, posZ, 6.0F, false, true));
      }

      return (float)finald;
   }

   private static float getDamageMultiplied(float damage) {
      int diff = mc.world.getDifficulty().getId();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
      float damage;
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         DamageSource ds = DamageSource.causeExplosionDamage(explosion);
         damage = CombatRules.getDamageAfterAbsorb(damageI, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
         int k = 0;

         try {
            k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
         } catch (Exception var8) {
         }

         float f = MathHelper.clamp((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            damage -= damage / 4.0F;
         }

         damage = Math.max(damage, 0.0F);
         return damage;
      } else {
         damage = CombatRules.getDamageAfterAbsorb(damageI, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
         return damage;
      }
   }

   public static boolean hasCrystal(BlockPos pos) {
      List<Entity> crystals = (List)mc.world.loadedEntityList.stream().filter((e) -> {
         return e instanceof EntityEnderCrystal;
      }).collect(Collectors.toList());
      Iterator var2 = crystals.iterator();

      BlockPos crystalPos;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         Entity crystal = (Entity)var2.next();
         crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
      } while(!pos.equals(crystalPos.add(0, -1, 0)));

      return true;
   }

   public static Entity getCrystal(BlockPos pos) {
      List<Entity> crystals = (List)mc.world.loadedEntityList.stream().filter((e) -> {
         return e instanceof EntityEnderCrystal;
      }).collect(Collectors.toList());
      Iterator var2 = crystals.iterator();

      Entity crystal;
      BlockPos crystalPos;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         crystal = (Entity)var2.next();
         crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
      } while(!pos.equals(crystalPos.add(0, -1, 0)));

      return crystal;
   }
}

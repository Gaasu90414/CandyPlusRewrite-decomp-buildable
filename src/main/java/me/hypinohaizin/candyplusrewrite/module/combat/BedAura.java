//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.MathUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class BedAura extends Module {
   public Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
   public Setting<Boolean> swing = this.register(new Setting("Swing", true));
   public Setting<Boolean> airPlace = this.register(new Setting("AirPlace", false));
   public Setting<Float> breakRange = this.register(new Setting("BreakRange", 7.0F, 7.0F, 0.1F));
   public Setting<Float> placeRange = this.register(new Setting("PlaceRange", 7.0F, 7.0F, 0.1F));
   public Setting<Integer> breakSpeed = this.register(new Setting("BreakSpeed", 20, 20, 1));
   public Setting<Integer> placeSpeed = this.register(new Setting("PlaceSpeed", 20, 20, 1));
   public Setting<Boolean> autoSwitch = this.register(new Setting("AutoSwitch", true));
   public Setting<Boolean> autoMove = this.register(new Setting("AutoMove", true));
   private Timer hitTimer = new Timer();
   private Timer placeTimer = new Timer();
   private BlockPos breakPos = null;
   private BlockPos finalPos = null;
   private EnumFacing finalFacing = null;
   private int priorSlot = -1;
   private static boolean isSpoofingAngles;
   private static double yaw;
   private static double pitch;

   public BedAura() {
      super("BedAura", Module.Categories.COMBAT, false, false);
   }

   public void onTick() {
      if (mc.player != null && mc.world != null) {
         this.breakPos = null;
         this.finalPos = null;
         if (mc.player.dimension != 0) {
            if (this.hitTimer.passedMs((long)(1000 - (Integer)this.breakSpeed.getValue() * 50))) {
               this.breakPos = this.findBedTarget();
            }

            if (this.breakPos == null && this.placeTimer.passedMs((long)(1000 - (Integer)this.placeSpeed.getValue() * 50))) {
               if (mc.player.inventory.getCurrentItem().getItem() != Items.BED && !this.isOffhand()) {
                  if (!this.getTargets().isEmpty() && (Boolean)this.autoSwitch.getValue() && !this.isOffhand()) {
                     int i;
                     for(i = 0; i < 9; ++i) {
                        ItemStack stack = (ItemStack)mc.player.inventory.mainInventory.get(i);
                        if (stack.getItem() == Items.BED) {
                           this.priorSlot = mc.player.inventory.currentItem;
                           mc.player.inventory.currentItem = i;
                           mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                           this.findPlaceTarget();
                           break;
                        }
                     }

                     if ((Boolean)this.autoMove.getValue() && mc.player.inventory.getCurrentItem().getItem() != Items.BED) {
                        for(i = 9; i <= 35; ++i) {
                           if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                              mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
                              mc.playerController.windowClick(0, mc.player.inventory.currentItem < 9 ? mc.player.inventory.currentItem + 36 : mc.player.inventory.currentItem, 0, ClickType.PICKUP, mc.player);
                              mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
                           }
                        }
                     }
                  }
               } else {
                  this.findPlaceTarget();
               }
            } else if (this.breakPos != null) {
               float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(1.0F), (new Vec3d(this.breakPos)).add(0.5D, 0.5D, 0.5D));
               yaw = (double)angle[0];
               pitch = (double)angle[1];
               isSpoofingAngles = true;
            }

            if (isSpoofingAngles) {
               mc.player.rotationYaw = (float)yaw;
               mc.player.rotationPitch = (float)pitch;
            }

         }
      }
   }

   public void onUpdate() {
      if (this.breakPos != null) {
         this.breakBed(this.breakPos);
      } else if (this.finalPos != null) {
         this.placeBed();
      }

      if (this.priorSlot != -1 && !this.isOffhand()) {
         mc.player.inventory.currentItem = this.priorSlot;
         mc.player.connection.sendPacket(new CPacketHeldItemChange(this.priorSlot));
         this.priorSlot = -1;
      }

   }

   public boolean isOffhand() {
      return mc.player.getHeldItemOffhand().getItem() instanceof ItemBed;
   }

   private void breakBed(BlockPos bed) {
      if (bed != null) {
         Vec3d hitVec = (new Vec3d(bed)).add(0.5D, 0.5D, 0.5D);
         EnumFacing facing = EnumFacing.UP;
         mc.playerController.processRightClickBlock(mc.player, mc.world, bed, facing, hitVec, EnumHand.MAIN_HAND);
         if ((Boolean)this.swing.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
         }

         this.hitTimer.reset();
      }
   }

   private void placeBed() {
      Vec3d hitVec = (new Vec3d(this.finalPos.down())).add(0.5D, 0.5D, 0.5D).add((new Vec3d(this.finalFacing.getOpposite().getDirectionVec())).scale(0.5D));
      mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
      mc.playerController.processRightClickBlock(mc.player, mc.world, this.finalPos.down(), EnumFacing.UP, hitVec, this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
      mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
      this.placeTimer.reset();
      this.finalPos = null;
   }

   private BlockPos findBedTarget() {
      TileEntityBed bed = (TileEntityBed)mc.world.loadedTileEntityList.stream().filter((e) -> {
         return e instanceof TileEntityBed;
      }).filter((e) -> {
         return ((TileEntityBed)e).isHeadPiece();
      }).filter((e) -> {
         return mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ()) <= (double)(Float)this.breakRange.getValue();
      }).filter((e) -> {
         return this.suicideCheck(e.getPos());
      }).min(Comparator.comparing((e) -> {
         return mc.player.getDistance((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ());
      })).orElse((TileEntity) null);
      return bed != null ? bed.getPos() : null;
   }

   private void findPlaceTarget() {
      List<EntityPlayer> targets = this.getTargets();
      if (!targets.isEmpty()) {
         this.checkTarget(new BlockPos((Entity)targets.get(0)), true);
      }
   }

   private void checkTarget(BlockPos pos, boolean firstCheck) {
      if (mc.world.getBlockState(pos).getBlock() != Blocks.BED) {
         float damage = this.calculateDamage(pos, mc.player);
         if ((double)damage > (double)mc.player.getHealth() + (double)mc.player.getAbsorptionAmount() + 0.5D) {
            if (firstCheck && (Boolean)this.airPlace.getValue()) {
               this.checkTarget(pos.up(), false);
            }

         } else if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            if (firstCheck && (Boolean)this.airPlace.getValue()) {
               this.checkTarget(pos.up(), false);
            }

         } else {
            ArrayList<BlockPos> positions = new ArrayList();
            HashMap<BlockPos, EnumFacing> facings = new HashMap();
            EnumFacing[] var6 = EnumFacing.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               EnumFacing facing = var6[var8];
               if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
                  BlockPos position = pos.offset(facing);
                  if (!(mc.player.getDistanceSq(position) > Math.pow((double)(Float)this.placeRange.getValue(), 2.0D)) && mc.world.getBlockState(position).getMaterial().isReplaceable() && !mc.world.getBlockState(position.down()).getMaterial().isReplaceable()) {
                     positions.add(position);
                     facings.put(position, facing.getOpposite());
                  }
               }
            }

            if (positions.isEmpty()) {
               if (firstCheck && (Boolean)this.airPlace.getValue()) {
                  this.checkTarget(pos.up(), false);
               }

            } else {
               positions.sort(Comparator.comparingDouble((pos2) -> {
                  return mc.player.getDistanceSq(pos2);
               }));
               this.finalPos = (BlockPos)positions.get(0);
               this.finalFacing = (EnumFacing)facings.get(this.finalPos);
               float[] rotation = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};
               if ((Boolean)this.rotate.getValue()) {
                  rotation = calculateAngle(mc.player.getPositionEyes(1.0F), new Vec3d((double)this.finalPos.down().getX() + 0.5D, (double)(this.finalPos.down().getY() + 1), (double)this.finalPos.down().getZ() + 0.5D));
               }

               yaw = (double)rotation[0];
               pitch = (double)rotation[1];
               isSpoofingAngles = true;
            }
         }
      }
   }

   public List<EntityPlayer> getTargets() {
      return (List)mc.world.playerEntities.stream().filter((e) -> {
         return !e.isDead;
      }).filter((e) -> {
         return !FriendManager.isFriend(e.getName());
      }).filter((e) -> {
         return e != mc.player;
      }).filter((e) -> {
         return mc.player.getDistance(e) < (Float)this.placeRange.getValue() + 2.0F;
      }).sorted(Comparator.comparing((e) -> {
         return mc.player.getDistance(e);
      })).collect(Collectors.toList());
   }

   private boolean suicideCheck(BlockPos pos) {
      return (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount() - calculateDamage((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D, mc.player)) > 0.5D;
   }

   public float calculateDamage(BlockPos bedPos, Entity entity) {
      return calculateDamage((double)bedPos.getX() + 0.5D, (double)bedPos.getY() + 1.0D, (double)bedPos.getZ() + 0.5D, entity);
   }

   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
      float doubleExplosionSize = 12.0F;
      double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = (double)entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
      double finald = 1.0D;
      if (entity instanceof EntityLivingBase) {
         finald = (double)getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.world, (Entity)null, posX, posY, posZ, 6.0F, false, true));
      }

      return (float)finald;
   }

   public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         DamageSource ds = DamageSource.causeExplosionDamage(explosion);
         damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
         int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
         float f = MathHelper.clamp((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (entity.isPotionActive(Potion.getPotionById(11))) {
            damage -= damage / 4.0F;
         }

         return damage;
      } else {
         damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
         return damage;
      }
   }

   private static float getDamageMultiplied(float damage) {
      int diff = mc.world.getDifficulty().getId();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static float[] calculateAngle(Vec3d from, Vec3d to) {
      double diffX = to.x - from.x;
      double diffY = to.y - from.y;
      double diffZ = to.z - from.z;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
      return new float[]{yaw, pitch};
   }
}

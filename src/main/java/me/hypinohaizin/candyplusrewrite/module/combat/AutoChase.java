//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.managers.HoleManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RotationUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoChase extends Module {
   public Setting<Integer> targetRange = this.register(new Setting("Target Range", 16, 256, 0));
   public Setting<Integer> fixedRange = this.register(new Setting("Fixed Target Range", 16, 256, 0));
   public Setting<Integer> cancelRange = this.register(new Setting("Cancel Range", 6, 16, 0));
   public Setting<Integer> downRange = this.register(new Setting("Down Range", 5, 8, 0));
   public Setting<Integer> upRange = this.register(new Setting("Up Range", 1, 8, 0));
   public Setting<Float> hRange = this.register(new Setting("H Range", 4.0F, 8.0F, 1.0F));
   public Setting<Float> timer = this.register(new Setting("Timer", 2.0F, 50.0F, 1.0F));
   public Setting<Float> speed = this.register(new Setting("Speed", 2.0F, 10.0F, 0.0F));
   public Setting<Boolean> step = this.register(new Setting("Step", true));
   public Setting<AutoChase.ModeType> mode;
   public Setting<Float> height;
   public Setting<Float> vHeight;
   public Setting<Boolean> abnormal;
   public Setting<Integer> centerSpeed;
   public Setting<Boolean> only;
   public Setting<Boolean> single;
   public Setting<Boolean> twoBlocks;
   public Setting<Boolean> custom;
   public Setting<Boolean> four;
   public Setting<Boolean> near;
   public Setting<Boolean> disable;
   private int stuckTicks;
   private BlockPos originPos;
   private BlockPos startPos;
   private boolean isActive;
   private boolean wasInHole;
   private boolean slowDown;
   private double playerSpeed;
   private EntityPlayer target;
   private Timer gameTimer;
   private HoleManager holeManager;
   private final double[] pointFiveToOne = new double[]{0.41999998688698D};
   private final double[] one = new double[]{0.41999998688698D, 0.7531999805212D};
   private final double[] oneFive = new double[]{0.42D, 0.753D, 1.001D, 1.084D, 1.006D};
   private final double[] oneSixTwoFive = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D};
   private final double[] oneEightSevenFive = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D};
   private final double[] two = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D};
   private final double[] twoFive = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D};
   private final double[] threeStep = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D};
   private final double[] fourStep = new double[]{0.42D, 0.75D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D, 2.78D, 2.63D, 2.51D, 2.9D, 3.21D, 3.45D, 3.43D};
   private final double[] betaShared = new double[]{0.419999986887D, 0.7531999805212D, 1.0013359791121D, 1.1661092609382D, 1.249187078744682D, 1.176759275064238D};
   private final double[] betaTwo = new double[]{1.596759261951216D, 1.929959255585439D};
   private final double[] betaTwoFive = new double[]{1.596759261951216D, 1.929959255585439D, 2.178095254176385D, 2.3428685360024515D, 2.425946353808919D};

   public AutoChase() {
      super("AutoChase", Module.Categories.COMBAT, false, false);
      this.mode = this.register(new Setting("Mode", AutoChase.ModeType.NCP));
      this.height = this.register(new Setting("NCP Height", 2.5F, 4.0F, 1.0F, (v) -> {
         return this.mode.getValue() == AutoChase.ModeType.NCP && (Boolean)this.step.getValue();
      }));
      this.vHeight = this.register(new Setting("Vanilla Height", 2.5F, 4.0F, 1.0F, (v) -> {
         return this.mode.getValue() == AutoChase.ModeType.VANILLA && (Boolean)this.step.getValue();
      }));
      this.abnormal = this.register(new Setting("Abnormal", false, (v) -> {
         return this.mode.getValue() != AutoChase.ModeType.VANILLA && (Boolean)this.step.getValue();
      }));
      this.centerSpeed = this.register(new Setting("Center Speed", 2, 10, 1));
      this.only = this.register(new Setting("Only 1x1", true));
      this.single = this.register(new Setting("Single Hole", true, (v) -> {
         return !(Boolean)this.only.getValue();
      }));
      this.twoBlocks = this.register(new Setting("Double Hole", true, (v) -> {
         return !(Boolean)this.only.getValue();
      }));
      this.custom = this.register(new Setting("Custom Hole", true, (v) -> {
         return !(Boolean)this.only.getValue();
      }));
      this.four = this.register(new Setting("Four Blocks", true, (v) -> {
         return !(Boolean)this.only.getValue();
      }));
      this.near = this.register(new Setting("Near Target", true));
      this.disable = this.register(new Setting("Disable", true));
      this.gameTimer = new Timer();
      this.holeManager = new HoleManager();
   }

   public void onEnable() {
      super.onEnable();
      this.sendMessage("This Module is Beta Module");
      this.wasInHole = false;
      BlockPos playerPos = PlayerUtil.getPlayerPos();
      this.originPos = playerPos;
      this.startPos = playerPos;
      this.stuckTicks = 0;
      this.isActive = false;
      this.gameTimer.reset();
   }

   public void onDisable() {
      super.onDisable();
      this.isActive = false;
      this.stuckTicks = 0;
      this.resetTimer();
      if (mc.player != null) {
         if (mc.player.getRidingEntity() != null) {
            mc.player.getRidingEntity().stepHeight = 1.0F;
         }

         mc.player.stepHeight = 0.6F;
      }

   }

   @SubscribeEvent
   public void onInputUpdate(InputUpdateEvent event) {
      if (this.isActive && event.getMovementInput() != null) {
         event.getMovementInput().jump = false;
         event.getMovementInput().sneak = false;
         event.getMovementInput().forwardKeyDown = false;
         event.getMovementInput().backKeyDown = false;
         event.getMovementInput().leftKeyDown = false;
         event.getMovementInput().rightKeyDown = false;
         event.getMovementInput().moveForward = 0.0F;
         event.getMovementInput().moveStrafe = 0.0F;
      }

   }

   @SubscribeEvent
   public void onPlayerTick(PlayerTickEvent event) {
      if (!this.nullCheck()) {
         if (event.phase == Phase.START && mc.player != null && mc.world != null) {
            this.isActive = false;
            this.resetTimer();
            if (mc.player.isEntityAlive() && !mc.player.isElytraFlying() && !mc.player.capabilities.isFlying) {
               double currentSpeed = Math.hypot(mc.player.motionX, mc.player.motionZ);
               if (currentSpeed <= 0.05D) {
                  this.originPos = PlayerUtil.getPlayerPos();
               }

               this.target = this.getNearestPlayer(this.target);
               if (this.target != null) {
                  double range = (double)mc.player.getDistance(this.target);
                  boolean inRange = range <= (double)(Integer)this.cancelRange.getValue();
                  if (this.shouldDisable(currentSpeed, inRange)) {
                     if ((Boolean)this.disable.getValue()) {
                        this.disable();
                     }

                  } else {
                     BlockPos hole = this.findHoles(this.target, inRange);
                     if (hole != null) {
                        double x = (double)hole.getX() + 0.5D;
                        double y = (double)hole.getY();
                        double z = (double)hole.getZ() + 0.5D;
                        if (this.checkYRange((int)mc.player.posY, hole.getY())) {
                           Vec3d playerPos = mc.player.getPositionVector();
                           double yawRad = Math.toRadians((double)RotationUtil.getRotationTo(playerPos, new Vec3d(x, y, z)).x);
                           double dist = Math.hypot(x - playerPos.x, z - playerPos.z);
                           if (mc.player.onGround) {
                              this.playerSpeed = this.getBaseMoveSpeed() * (this.isCollidingWithLiquid() && !this.isInLiquid() ? 0.91D : (double)(Float)this.speed.getValue());
                              this.slowDown = true;
                           }

                           double moveSpeed = Math.min(dist, this.playerSpeed);
                           mc.player.motionX = -Math.sin(yawRad) * moveSpeed;
                           mc.player.motionZ = Math.cos(yawRad) * moveSpeed;
                           if (moveSpeed != 0.0D && (-Math.sin(yawRad) != 0.0D || Math.cos(yawRad) != 0.0D)) {
                              this.setTimer((float)(50.0D / (double)(Float)this.timer.getValue()));
                              this.isActive = true;
                           }
                        }
                     }

                     if (mc.player.collidedHorizontally && hole == null) {
                        ++this.stuckTicks;
                     } else {
                        this.stuckTicks = 0;
                     }

                     if (this.canStep()) {
                        mc.player.stepHeight = this.getHeight();
                     } else {
                        if (mc.player.getRidingEntity() != null) {
                           mc.player.getRidingEntity().stepHeight = 1.0F;
                        }

                        mc.player.stepHeight = 0.6F;
                     }

                     if (this.target == null) {
                        this.isActive = false;
                     }

                  }
               }
            }
         }
      }
   }

   private EntityPlayer getNearestPlayer(EntityPlayer currentTarget) {
      return currentTarget != null && mc.player.getDistance(currentTarget) <= (float)(Integer)this.fixedRange.getValue() && !this.isInvalidTarget(currentTarget) ? currentTarget : (EntityPlayer)mc.world.playerEntities.stream().filter((p) -> {
         return mc.player.getDistance(p) <= (float)(Integer)this.targetRange.getValue();
      }).filter((p) -> {
         return mc.player.getEntityId() != p.getEntityId();
      }).filter((p) -> {
         return !this.isInvalidTarget(p);
      }).min(Comparator.comparing((p) -> {
         return mc.player.getDistance(p);
      })).orElse((EntityPlayer) null);
   }

   private boolean isInvalidTarget(EntityPlayer player) {
      return player.isDead || player.getHealth() <= 0.0F || FriendManager.isFriend(player.getName());
   }

   private BlockPos findHoles(EntityPlayer target, boolean inRange) {
      if (inRange && this.wasInHole) {
         return null;
      } else {
         this.wasInHole = false;
         List<BlockPos> holes = new ArrayList();
         List<BlockPos> blockPosList = this.getSphere(this.getPlayerPos(target), (double)(Float)this.hRange.getValue(), 8.0D, false, true, 0);
         Iterator var5 = blockPosList.iterator();

         while(true) {
            BlockPos pos;
            do {
               do {
                  do {
                     do {
                        do {
                           do {
                              if (!var5.hasNext()) {
                                 return (BlockPos)holes.stream().min(Comparator.comparing((p) -> {
                                    return (Boolean)this.near.getValue() ? target.getDistance((double)p.getX() + 0.5D, (double)p.getY(), (double)p.getZ() + 0.5D) : mc.player.getDistance((double)p.getX() + 0.5D, (double)p.getY(), (double)p.getZ() + 0.5D);
                                 })).orElse((BlockPos) null);
                              }

                              pos = (BlockPos)var5.next();
                           } while(!this.checkYRange((int)mc.player.posY, pos.getY()));
                        } while(!mc.world.isAirBlock(PlayerUtil.getPlayerPos().up(2)) && (int)mc.player.posY < pos.getY());
                     } while(!this.holeManager.isSafe(pos));
                  } while(!mc.world.isAirBlock(pos));
               } while(!mc.world.isAirBlock(pos.up()));
            } while(!mc.world.isAirBlock(pos.up(2)));

            boolean valid = true;

            for(int high = 0; (double)high < mc.player.posY - (double)pos.getY(); ++high) {
               if (high != 0) {
                  if (mc.player.posY > (double)pos.getY() && !mc.world.isAirBlock(new BlockPos(pos.getX(), pos.getY() + high, pos.getZ()))) {
                     valid = false;
                     break;
                  }

                  if (mc.player.posY < (double)pos.getY()) {
                     BlockPos newPos = new BlockPos(pos.getX(), pos.getY() + high, pos.getZ());
                     if (mc.world.isAirBlock(newPos) && (mc.world.isAirBlock(newPos.down()) || mc.world.isAirBlock(newPos.up()))) {
                        valid = false;
                        break;
                     }
                  }
               }
            }

            if (valid) {
               holes.add(pos);
            }
         }
      }
   }

   private boolean shouldDisable(Double currentSpeed, boolean inRange) {
      if (this.isActive) {
         return false;
      } else if (!mc.player.onGround) {
         return false;
      } else if (this.stuckTicks > 5 && currentSpeed < 0.05D) {
         return true;
      } else if (this.holeManager.isSafe(new BlockPos((double)PlayerUtil.getPlayerPos().getX(), (double)PlayerUtil.getPlayerPos().getY() + 0.5D, (double)PlayerUtil.getPlayerPos().getZ())) && inRange) {
         BlockPos holePos = new BlockPos(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY(), PlayerUtil.getPlayerPos().getZ());
         Vec3d center = new Vec3d((double)holePos.getX() + 0.5D, (double)holePos.getY(), (double)holePos.getZ() + 0.5D);
         double xDiff = Math.abs(center.x - mc.player.posX);
         double zDiff = Math.abs(center.z - mc.player.posZ);
         if ((xDiff > 0.3D || zDiff > 0.3D) && !this.wasInHole) {
            double motionX = center.x - mc.player.posX;
            double motionZ = center.z - mc.player.posZ;
            mc.player.motionX = motionX / (double)(Integer)this.centerSpeed.getValue();
            mc.player.motionZ = motionZ / (double)(Integer)this.centerSpeed.getValue();
         }

         return this.wasInHole = true;
      } else {
         return false;
      }
   }

   private boolean checkYRange(int playerY, int holeY) {
      if (playerY >= holeY) {
         return playerY - holeY <= (Integer)this.downRange.getValue();
      } else {
         return holeY - playerY <= (Integer)this.upRange.getValue();
      }
   }

   public float getHeight() {
      return this.mode.getValue() == AutoChase.ModeType.VANILLA ? (Float)this.vHeight.getValue() : (Float)this.height.getValue();
   }

   private boolean canStep() {
      return !mc.player.isInWater() && mc.player.onGround && !mc.player.isOnLadder() && !mc.player.movementInput.jump && mc.player.collidedVertically && (double)mc.player.fallDistance < 0.1D && (Boolean)this.step.getValue() && this.isActive;
   }

   private void sendOffsets(double[] offsets) {
      double[] var2 = offsets;
      int var3 = offsets.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double offset = var2[var4];
         mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
      }

   }

   private double getBaseMoveSpeed() {
      return 0.2873D;
   }

   private boolean isCollidingWithLiquid() {
      return false;
   }

   private boolean isInLiquid() {
      return mc.player.isInWater() || mc.player.isInLava();
   }

   private void setTimer(float tickLength) {
   }

   private void resetTimer() {
      this.setTimer(50.0F);
   }

   private List<BlockPos> getSphere(BlockPos center, double radius, double height, boolean hollow, boolean sphere, int plusY) {
      List<BlockPos> result = new ArrayList();
      int cx = center.getX();
      int cy = center.getY();
      int cz = center.getZ();

      for(int x = cx - (int)radius; (double)x <= (double)cx + radius; ++x) {
         for(int z = cz - (int)radius; (double)z <= (double)cz + radius; ++z) {
            for(int y = sphere ? cy - (int)radius : cy; (double)y <= (sphere ? (double)cy + radius : (double)cy + height); ++y) {
               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < radius * radius && (!hollow || dist >= (radius - 1.0D) * (radius - 1.0D))) {
                  result.add(new BlockPos(x, y + plusY, z));
               }
            }
         }
      }

      return result;
   }

   private BlockPos getPlayerPos(EntityPlayer player) {
      return new BlockPos(player.posX, player.posY, player.posZ);
   }

   public static enum ModeType {
      NCP,
      VANILLA;
   }
}

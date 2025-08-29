//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.MathUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;

public class Flight extends Module {
   public Setting<Flight.FlyMode> mode;
   public Setting<Float> speed;
   public Setting<Boolean> viewBob;
   public Setting<Float> bobAmount;
   public Setting<Boolean> damageBoost;
   public Setting<Integer> jpSpeed;
   public Setting<Integer> teleportTime;
   private BlockPos startPos;
   private double startY;
   private boolean onDamage = false;
   private Timer timer = new Timer();

   public Flight() {
      super("Flight", Module.Categories.MOVEMENT, false, false);
      this.mode = this.register(new Setting("Mode", Flight.FlyMode.CREATIVE));
      this.speed = this.register(new Setting("Speed", 0.1F, 5.0F, 0.0F, (v) -> {
         return this.mode.getValue() == Flight.FlyMode.CREATIVE;
      }));
      this.viewBob = this.register(new Setting("ViewBob", false, (v) -> {
         return this.mode.getValue() == Flight.FlyMode.CREATIVE;
      }));
      this.bobAmount = this.register(new Setting("BobAmount", 0.1F, 2.0F, 0.0F, (v) -> {
         return (Boolean)this.viewBob.getValue() && this.mode.getValue() == Flight.FlyMode.CREATIVE;
      }));
      this.damageBoost = this.register(new Setting("DamageBoost", false, (v) -> {
         return this.mode.getValue() == Flight.FlyMode.CREATIVE;
      }));
      this.jpSpeed = this.register(new Setting("TeleportAmount", 5, 20, 0, (v) -> {
         return this.mode.getValue() == Flight.FlyMode._2B2TJB;
      }));
      this.teleportTime = this.register(new Setting("TeleportTime", 750, 5000, 0, (v) -> {
         return this.mode.getValue() == Flight.FlyMode._2B2TJB;
      }));
   }

   public void onEnable() {
      if (mc.player != null) {
         super.onEnable();
         this.startPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         this.startY = mc.player.posY;
         this.onDamage = false;
         if (this.mode.getValue() == Flight.FlyMode.CREATIVE && (Boolean)this.damageBoost.getValue()) {
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 3.0001D, 0.0D)).isEmpty()) {
               mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 3.0001D, mc.player.posZ, false));
               mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
               mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
            }

            mc.player.setPosition(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ);
            this.onDamage = true;
         }

      }
   }

   public void onTick() {
      if (mc.player != null && mc.world != null) {
         switch((Flight.FlyMode)this.mode.getValue()) {
         case CREATIVE:
            if (!(Boolean)this.damageBoost.getValue() || this.onDamage) {
               mc.player.setVelocity(0.0D, 0.0D, 0.0D);
               mc.player.jumpMovementFactor = (Float)this.speed.getValue();
               double[] mv = MathUtil.directionSpeed((double)(Float)this.speed.getValue());
               if (mc.player.movementInput.moveStrafe != 0.0F || mc.player.movementInput.moveForward != 0.0F) {
                  if ((Boolean)this.viewBob.getValue()) {
                     mc.player.cameraYaw = (Float)this.bobAmount.getValue() / 10.0F;
                  }

                  mc.player.motionX = mv[0];
                  mc.player.motionZ = mv[1];
               }

               EntityPlayerSP var10000;
               if (mc.gameSettings.keyBindJump.isKeyDown()) {
                  var10000 = mc.player;
                  var10000.motionY += (double)(Float)this.speed.getValue();
               }

               if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                  var10000 = mc.player;
                  var10000.motionY -= (double)(Float)this.speed.getValue();
               }
            }
            break;
         case JUMP:
            if (mc.player.posY < this.startY) {
               mc.player.jump();
            }
            break;
         case _2B2TJB:
            double yaw = Math.cos(Math.toRadians((double)(mc.player.rotationYaw + 90.0F)));
            double pitch = Math.sin(Math.toRadians((double)(mc.player.rotationYaw + 90.0F)));
            mc.player.setPosition(mc.player.posX + (double)(Integer)this.jpSpeed.getValue() * yaw, mc.player.posY, mc.player.posZ + (double)(Integer)this.jpSpeed.getValue() * pitch);
            if (this.timer.passedMs((long)(Integer)this.teleportTime.getValue())) {
               mc.player.setPosition(mc.player.posX, mc.player.posY + 30.0D, mc.player.posZ);
               this.timer.reset();
            }

            mc.player.motionY = 0.0D;
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      mc.player.motionX = 0.0D;
      mc.player.motionY = 0.0D;
      mc.player.motionZ = 0.0D;
   }

   public static enum FlyMode {
      CREATIVE,
      JUMP,
      _2B2TJB;
   }
}

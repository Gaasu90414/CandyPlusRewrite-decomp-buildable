//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class TPAura extends Module {
   private Setting<Integer> maxRange = this.register(new Setting("MaxRange", 10, 50, 2));
   private Setting<Boolean> onlyPlayer = this.register(new Setting("OnlyPlayer", true));
   private Setting<Boolean> strafe = this.register(new Setting("Strafe", false));
   private Setting<Boolean> look = this.register(new Setting("LookRotate", true));
   private Setting<Boolean> motionTp = this.register(new Setting("MotionTp", false));
   private Setting<Integer> attackDelay = this.register(new Setting("Delay", 500, 2000, 0));
   private Setting<Boolean> swing = this.register(new Setting("Swing", true));
   private Setting<Boolean> onGround = this.register(new Setting("OnGround", false));
   private Setting<Integer> strafeKey = this.register(new Setting("StrafeKey", 0, 0, 256));
   private final Timer timer = new Timer();
   private final List<Entity> targets = new ArrayList();

   public TPAura() {
      super("TPAura", Module.Categories.COMBAT, false, false);
   }

   public void onTick() {
      if (mc.player != null && mc.world != null) {
         this.targets.clear();
         Iterator var1 = mc.world.loadedEntityList.iterator();

         while(var1.hasNext()) {
            Entity e = (Entity)var1.next();
            if (e != mc.player && e.isEntityAlive() && (!(Boolean)this.onlyPlayer.getValue() || e instanceof EntityPlayer) && (!(e instanceof EntityPlayer) || !FriendManager.isFriend((EntityPlayer)e)) && !(mc.player.getDistance(e) > (float)(Integer)this.maxRange.getValue())) {
               this.targets.add(e);
               break;
            }
         }

         if (!this.targets.isEmpty()) {
            Entity target = (Entity)this.targets.get(0);
            if (target != null && target.isEntityAlive()) {
               if (this.timer.passedMs((long)(Integer)this.attackDelay.getValue())) {
                  if ((Boolean)this.look.getValue()) {
                     this.rotateTo(target);
                  }

                  double px = mc.player.posX;
                  double py = mc.player.posY;
                  double pz = mc.player.posZ;
                  this.tpTo(target.posX, target.posY, target.posZ);
                  mc.playerController.attackEntity(mc.player, target);
                  if ((Boolean)this.swing.getValue()) {
                     mc.player.swingArm(EnumHand.MAIN_HAND);
                  }

                  this.tpTo(px, py, pz);
                  if ((Boolean)this.strafe.getValue() && Keyboard.isKeyDown((Integer)this.strafeKey.getValue())) {
                     this.strafeAround(target);
                  }

                  this.timer.reset();
               }
            }
         }
      }
   }

   private void tpTo(double x, double y, double z) {
      EntityPlayerSP p = mc.player;
      if (p != null) {
         double dx = x - p.posX;
         double dy = y - p.posY;
         double dz = z - p.posZ;
         double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
         if (!(dist < 1.0E-4D)) {
            int steps = (Boolean)this.motionTp.getValue() ? Math.max(1, (int)Math.ceil(dist / 0.35D)) : Math.max(1, (int)Math.ceil(dist / 1.2D));
            double sx = dx / (double)steps;
            double sy = dy / (double)steps;
            double sz = dz / (double)steps;

            for(int i = 0; i < steps; ++i) {
               double nx = p.posX + sx;
               double ny = p.posY + sy;
               double nz = p.posZ + sz;
               p.setPosition(nx, ny, nz);
               p.connection.sendPacket(new Position(nx, ny, nz, (Boolean)this.onGround.getValue()));
            }

         }
      }
   }

   private void rotateTo(Entity t) {
      double dx = t.posX - mc.player.posX;
      double dz = t.posZ - mc.player.posZ;
      double dy = t.posY + (double)t.getEyeHeight() / 2.0D - (mc.player.posY + (double)mc.player.getEyeHeight());
      double distxz = Math.sqrt(dx * dx + dz * dz);
      float yaw = (float)(Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(dy, distxz) * 180.0D / 3.141592653589793D));
      mc.player.rotationYaw = yaw;
      mc.player.rotationPitch = pitch;
   }

   private void strafeAround(Entity t) {
      double angle = (double)(mc.player.ticksExisted % 360) * 3.141592653589793D / 18.0D;
      double radius = 8.0D;
      double tx = t.posX + Math.cos(angle) * radius;
      double tz = t.posZ + Math.sin(angle) * radius;
      double dx = tx - mc.player.posX;
      double dz = tz - mc.player.posZ;
      double dy = t.posY - mc.player.posY;
      double max = 1.0D;
      mc.player.motionX = this.clamp(dx, -max, max);
      mc.player.motionZ = this.clamp(dz, -max, max);
      mc.player.motionY = this.clamp(dy, -1.0D, 1.0D);
   }

   private double clamp(double v, double min, double max) {
      return v < min ? min : (v > max ? max : v);
   }
}

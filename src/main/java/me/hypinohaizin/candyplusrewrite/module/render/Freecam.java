//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Freecam extends Module {
   public Setting<Freecam.Modes> mode;
   public Setting<Float> speed;
   public Setting<Boolean> noClip;
   public Setting<Boolean> freeze;
   private Entity riding;
   private EntityOtherPlayerMP camera;
   private Vec3d position;
   private float yaw;
   private float pitch;
   private boolean wasFlying;
   private float oldFlySpeed;

   public Freecam() {
      super("FreeCam", Module.Categories.RENDER, false, false);
      this.mode = this.register(new Setting("Mode", Freecam.Modes.Camera));
      this.speed = this.register(new Setting("Speed", 1.0F, 5.0F, 0.1F));
      this.noClip = this.register(new Setting("NoClip", true));
      this.freeze = this.register(new Setting("Freeze", true));
   }

   public void onEnable() {
      super.onEnable();
      if (this.nullCheck()) {
         this.disable();
      } else {
         this.position = mc.player.getPositionVector();
         this.yaw = mc.player.rotationYaw;
         this.pitch = mc.player.rotationPitch;
         this.wasFlying = mc.player.capabilities.isFlying;
         this.oldFlySpeed = mc.player.capabilities.getFlySpeed();
         if (this.mode.getValue() == Freecam.Modes.Normal) {
            this.setupNormalMode();
         } else {
            this.setupCameraMode();
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      if (!this.nullCheck()) {
         if (this.mode.getValue() == Freecam.Modes.Normal) {
            this.cleanupNormalMode();
         } else {
            this.cleanupCameraMode();
         }

         if (this.position != null) {
            mc.player.setPosition(this.position.x, this.position.y, this.position.z);
            mc.player.rotationYaw = this.yaw;
            mc.player.rotationPitch = this.pitch;
            mc.player.prevRotationYaw = this.yaw;
            mc.player.prevRotationPitch = this.pitch;
         }

         mc.player.capabilities.isFlying = this.wasFlying;
         mc.player.capabilities.setFlySpeed(this.oldFlySpeed);
         mc.player.capabilities.allowFlying = mc.player.capabilities.isCreativeMode;
         this.position = null;
         this.camera = null;
         this.riding = null;
      }
   }

   private void setupNormalMode() {
      this.riding = null;
      if (mc.player.getRidingEntity() != null) {
         this.riding = mc.player.getRidingEntity();
         mc.player.dismountRidingEntity();
      }

      this.camera = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
      this.camera.copyLocationAndAnglesFrom(mc.player);
      this.camera.prevRotationYaw = mc.player.rotationYaw;
      this.camera.rotationYawHead = mc.player.rotationYawHead;
      this.camera.inventory.copyInventory(mc.player.inventory);
      mc.world.addEntityToWorld(-69, this.camera);
      if ((Boolean)this.noClip.getValue()) {
         mc.player.noClip = true;
      }

      mc.player.capabilities.isFlying = true;
      mc.player.capabilities.allowFlying = true;
      mc.player.capabilities.setFlySpeed((Float)this.speed.getValue() / 10.0F);
   }

   private void setupCameraMode() {
      this.camera = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
      this.camera.copyLocationAndAnglesFrom(mc.player);
      this.camera.prevRotationYaw = mc.player.rotationYaw;
      this.camera.rotationYawHead = mc.player.rotationYawHead;
      this.camera.inventory.copyInventory(mc.player.inventory);
      if ((Boolean)this.noClip.getValue()) {
         this.camera.noClip = true;
      }

      mc.world.addEntityToWorld(-69, this.camera);
      mc.setRenderViewEntity(this.camera);
   }

   private void cleanupNormalMode() {
      if (this.camera != null) {
         mc.world.removeEntityFromWorld(-69);
      }

      if (this.riding != null) {
         mc.player.startRiding(this.riding);
      }

      mc.player.noClip = false;
   }

   private void cleanupCameraMode() {
      if (this.camera != null) {
         mc.world.removeEntityFromWorld(-69);
      }

      mc.setRenderViewEntity(mc.player);
   }

   @SubscribeEvent
   public void onUpdate(TickEvent event) {
      if (!this.nullCheck()) {
         if (this.mode.getValue() == Freecam.Modes.Normal) {
            this.updateNormalMode();
         } else {
            this.updateCameraMode();
         }

      }
   }

   private void updateNormalMode() {
      mc.player.capabilities.setFlySpeed((Float)this.speed.getValue() / 10.0F);
      if (this.camera != null && this.position != null) {
         this.camera.setPosition(this.position.x, this.position.y, this.position.z);
         this.camera.rotationYaw = this.yaw;
         this.camera.rotationPitch = this.pitch;
         this.camera.prevRotationYaw = this.yaw;
         this.camera.prevRotationPitch = this.pitch;
      }

   }

   private void updateCameraMode() {
      if (this.camera != null) {
         this.handleCameraMovement();
         float forward = 0.0F;
         float strafe = 0.0F;
         float vertical = 0.0F;
         if (mc.gameSettings.keyBindForward.isKeyDown()) {
            ++forward;
         }

         if (mc.gameSettings.keyBindBack.isKeyDown()) {
            --forward;
         }

         if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            ++strafe;
         }

         if (mc.gameSettings.keyBindRight.isKeyDown()) {
            --strafe;
         }

         if (mc.gameSettings.keyBindJump.isKeyDown()) {
            ++vertical;
         }

         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            --vertical;
         }

         if (forward != 0.0F || strafe != 0.0F || vertical != 0.0F) {
            double moveSpeed = (double)(Float)this.speed.getValue() * 0.1D;
            double yawRad = Math.toRadians((double)this.camera.rotationYaw);
            double motionX = ((double)strafe * Math.cos(yawRad) - (double)forward * Math.sin(yawRad)) * moveSpeed;
            double motionZ = ((double)forward * Math.cos(yawRad) + (double)strafe * Math.sin(yawRad)) * moveSpeed;
            double motionY = (double)vertical * moveSpeed;
            this.camera.setPosition(this.camera.posX + motionX, this.camera.posY + motionY, this.camera.posZ + motionZ);
         }

      }
   }

   private void handleCameraMovement() {
      if (this.camera != null) {
         float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float f = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F;
         if (!(Boolean)this.freeze.getValue()) {
            this.camera.rotationYaw = mc.player.rotationYaw;
            this.camera.rotationPitch = mc.player.rotationPitch;
            this.camera.prevRotationYaw = mc.player.prevRotationYaw;
            this.camera.prevRotationPitch = mc.player.prevRotationPitch;
         }

      }
   }

   @SubscribeEvent
   public void onPacketSend(PacketEvent event) {
      if (!this.nullCheck()) {
         if ((Boolean)this.freeze.getValue() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (this.position != null) {
               if (packet instanceof Position || packet instanceof PositionRotation) {
                  packet.x = this.position.x;
                  packet.y = this.position.y;
                  packet.z = this.position.z;
               }

               if (packet instanceof Rotation || packet instanceof PositionRotation) {
                  packet.yaw = this.yaw;
                  packet.pitch = this.pitch;
               }
            }
         }

      }
   }

   public static enum Modes {
      Normal,
      Camera;
   }
}

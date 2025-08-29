//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import me.hypinohaizin.candyplusrewrite.module.render.Notification;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NotificationManager extends Manager {
   public List<NotificationManager.Notif> notifs = new ArrayList();
   public List<EntityPlayer> players = new ArrayList();
   private final Map<String, Integer> popCounter = new HashMap();
   private int scaledWidth;
   private int scaledHeight;
   private int scaleFactor = 0;

   public void showNotification(String msg) {
      if (mc.player != null) {
         if (CandyPlusRewrite.m_module.getModuleWithClass(Notification.class).isEnable) {
            NotificationManager.Notif notif = new NotificationManager.Notif(msg);

            NotificationManager.Notif notif2;
            for(Iterator var3 = this.notifs.iterator(); var3.hasNext(); notif2.y -= (float)(CandyPlusRewrite.m_font.getHeight() + 40)) {
               notif2 = (NotificationManager.Notif)var3.next();
            }

            this.updateResolution();
            notif.y = (float)(this.scaledHeight - 50);
            notif._y = (float)(this.scaledHeight - 50);
            this.notifs.add(notif);
         }
      }
   }

   public void onUpdate() {
      if (mc.world != null) {
         Iterator var1 = (new ArrayList(mc.world.playerEntities)).iterator();

         while(var1.hasNext()) {
            EntityPlayer player = (EntityPlayer)var1.next();
            if (player != mc.player && !this.players.contains(player)) {
               this.showNotification(player.getName() + " is coming towards you!");
            }
         }

         this.players = new ArrayList(mc.world.playerEntities);
      }
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketEntityStatus) {
         SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
         if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)packet.getEntity(mc.world);
            Notification notification = (Notification)CandyPlusRewrite.m_module.getModuleWithClass(Notification.class);
            if ((Boolean)notification.pop.getValue()) {
               int pop = this.countPop(player.getName());
               if (pop == 1) {
                  this.showNotification(player.getName() + " popped a totem!");
               } else {
                  this.showNotification(player.getName() + " popped " + pop + " totems!");
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onPlayerDeath(PlayerDeathEvent event) {
      Notification notification = (Notification)CandyPlusRewrite.m_module.getModuleWithClass(Notification.class);
      if ((Boolean)notification.death.getValue()) {
         EntityPlayer player = event.player;
         if (player == null) {
            return;
         }

         int pop = this.getPop(player.getName());
         if (pop == 0) {
            this.showNotification(ChatFormatting.RED + player.getName() + " dead!");
         } else {
            this.showNotification(ChatFormatting.RED + player.getName() + " dead after " + pop + " pop!");
         }

         this.popCounter.remove(player.getName());
      }

   }

   public int countPop(String name) {
      if (!this.popCounter.containsKey(name)) {
         this.popCounter.put(name, 1);
         return 1;
      } else {
         this.popCounter.replace(name, (Integer)this.popCounter.get(name) + 1);
         return (Integer)this.popCounter.get(name);
      }
   }

   public int getPop(String name) {
      return !this.popCounter.containsKey(name) ? 0 : (Integer)this.popCounter.get(name);
   }

   public void onRender2D() {
      try {
         if (mc.player == null) {
            return;
         }

         Iterator var1 = this.notifs.iterator();

         while(var1.hasNext()) {
            NotificationManager.Notif notification = (NotificationManager.Notif)var1.next();
            this.updateResolution();
            String msg = notification.msg;
            int width = CandyPlusRewrite.m_font.getWidth(msg);
            RenderUtil.drawRect((float)(this.scaledWidth - width - 26) + notification.offsetX, notification._y - 21.0F, (float)(width + 27), (float)(CandyPlusRewrite.m_font.getHeight() + 12), ColorUtil.toRGBA(new Color(35, 35, 35, 255)));
            RenderUtil.drawRect((float)(this.scaledWidth - width - 25) + notification.offsetX, notification._y - 20.0F, (float)(width + 25), (float)(CandyPlusRewrite.m_font.getHeight() + 10), ColorUtil.toRGBA(new Color(45, 45, 45, 255)));
            RenderUtil.drawRect((float)(this.scaledWidth - width - 26) + notification.offsetX, notification._y - 20.0F + (float)CandyPlusRewrite.m_font.getHeight() + 10.0F, (float)(width + 26) * ((notification.max - notification.ticks) / notification.max), 1.0F, ColorUtil.toRGBA(new Color(170, 170, 170, 255)));
            RenderUtil.drawString(msg, (float)(this.scaledWidth - width - 20) + notification.offsetX, notification._y - 10.0F - 3.0F, ColorUtil.toRGBA(255, 255, 255), false, 1.0F);
            if (notification.ticks <= 0.0F) {
               notification.offsetX += (500.0F - notification.offsetX) / 10.0F;
            } else {
               --notification.ticks;
               notification.offsetX += (0.0F - notification.offsetX) / 4.0F;
               notification._y += (notification.y - notification._y) / 4.0F;
            }
         }

         this.notifs = (List)this.notifs.stream().filter((n) -> {
            return (n.offsetX < 450.0F || n.ticks != 0.0F) && n._y >= -100.0F;
         }).collect(Collectors.toList());
      } catch (Exception var8) {
      }

   }

   public void updateResolution() {
      this.scaledWidth = mc.displayWidth;
      this.scaledHeight = mc.displayHeight;
      this.scaleFactor = 1;
      boolean flag = mc.isUnicode();
      int i = mc.gameSettings.guiScale;
      if (i == 0) {
         i = 1000;
      }

      while(this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
         ++this.scaleFactor;
      }

      if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
         --this.scaleFactor;
      }

      double scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
      double scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
      this.scaledWidth = MathHelper.ceil(scaledWidthD);
      this.scaledHeight = MathHelper.ceil(scaledHeightD);
   }

   public class Notif {
      public String msg;
      public float offsetX = 300.0F;
      public float y = 0.0F;
      public float _y = 0.0F;
      public float ticks = 0.0F;
      public float max = 0.0F;

      public Notif(String msg) {
         this.msg = msg;
         int fps = Minecraft.getDebugFPS();
         if (fps == 0) {
            fps = 60;
         }

         int seconds = (Integer)((Notification)CandyPlusRewrite.m_module.getModuleWithClass(Notification.class)).time.getValue();
         this.ticks = (float)(seconds * fps);
         this.max = (float)(seconds * fps);
      }
   }
}

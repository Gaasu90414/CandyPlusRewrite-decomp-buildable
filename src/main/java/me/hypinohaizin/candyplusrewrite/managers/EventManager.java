//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.command.CommandManager;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.lwjgl.input.Keyboard;

public class EventManager extends Manager {
   @SubscribeEvent
   public void onUpdate(LivingUpdateEvent event) {
      if (!this.nullCheck()) {
         CandyPlusRewrite.m_notif.onUpdate();
         CandyPlusRewrite.m_rotate.updateRotations();
         CandyPlusRewrite.m_hole.update();
         CandyPlusRewrite.m_module.onUpdate();
      }

   }

   @SubscribeEvent
   public void onConnect(ClientConnectedToServerEvent event) {
      CandyPlusRewrite.m_module.onConnect();
   }

   @SubscribeEvent
   public void onDisconnect(ClientDisconnectionFromServerEvent event) {
      CandyPlusRewrite.Info("Saving configs...");
      ConfigManager.saveConfigs();
      CandyPlusRewrite.Info("Successfully save configs!");
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      CandyPlusRewrite.m_module.onTick();
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL,
      receiveCanceled = true
   )
   public void onKeyInput(KeyInputEvent event) {
      if (Keyboard.getEventKeyState()) {
         CandyPlusRewrite.m_module.onKeyInput(Keyboard.getEventKey());
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onChatSent(ClientChatEvent event) {
      String msg = event.getMessage();
      String prefix = CommandManager.getCommandPrefix();
      if (msg.startsWith(prefix)) {
         event.setCanceled(true);
         Minecraft mc = Minecraft.getMinecraft();
         mc.ingameGUI.getChatGUI().addToSentMessages(msg);

         try {
            CommandManager.callCommand(msg.substring(prefix.length()), false);
         } catch (Exception var6) {
            var6.printStackTrace();
            if (mc.player != null) {
               mc.player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Error: " + var6.getMessage()));
            }
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.LOW
   )
   public void onRenderGameOverlayEvent(Text event) {
      if (event.getType().equals(ElementType.TEXT)) {
         new ScaledResolution(mc);
         CandyPlusRewrite.m_module.onRender2D();
         CandyPlusRewrite.m_notif.onRender2D();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @SubscribeEvent
   public void onWorldRender(RenderWorldLastEvent event) {
      if (!event.isCanceled()) {
         if (mc.player != null && mc.world != null) {
            mc.profiler.startSection("candy");
            mc.profiler.startSection("setup");
            RenderUtil3D.prepare();
            mc.profiler.endSection();
            CandyPlusRewrite.m_module.onRender3D();
            CandyPlusRewrite.m_module.onRender3D(event.getPartialTicks());
            mc.profiler.startSection("release");
            RenderUtil3D.release();
            mc.profiler.endSection();
            mc.profiler.endSection();
         }
      }
   }

   @SubscribeEvent
   public void onPacketSend(PacketEvent.Send event) {
      CandyPlusRewrite.m_module.onPacketSend(event);
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive event) {
      CandyPlusRewrite.m_module.onPacketReceive(event);
      if (event.getPacket() instanceof SPacketEntityStatus) {
         SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
         if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)packet.getEntity(mc.world);
            CandyPlusRewrite.m_module.onTotemPop(player);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerDeath(PlayerDeathEvent event) {
      CandyPlusRewrite.m_module.onPlayerDeath(event);
   }
}

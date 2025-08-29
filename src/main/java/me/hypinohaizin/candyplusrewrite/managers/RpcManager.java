//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.combat.CevBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.CivBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.PistonAura;
import me.hypinohaizin.candyplusrewrite.module.misc.DiscordRPC;

public class RpcManager extends Manager {
   private Thread _thread = null;

   public void enable(DiscordRPC module) {
      club.minnced.discord.rpc.DiscordRPC lib = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
      String applicationId = "1401898002305650799";
      String steamId = "";
      DiscordEventHandlers handlers = new DiscordEventHandlers();
      lib.Discord_Initialize("1401898002305650799", handlers, true, "");
      DiscordRichPresence presence = new DiscordRichPresence();
      presence.startTimestamp = System.currentTimeMillis() / 1000L;
      lib.Discord_UpdatePresence(presence);
      presence.largeImageText = "";
      (this._thread = new Thread(() -> {
         while(!Thread.currentThread().isInterrupted()) {
            lib.Discord_RunCallbacks();
            if ((Boolean)module.girl.getValue()) {
               presence.largeImageKey = "icon";
            } else {
               presence.largeImageKey = "icon";
            }

            presence.details = "Playing Candy+ Rewrite";
            presence.state = this.getState();
            lib.Discord_UpdatePresence(presence);

            try {
               Thread.sleep(3000L);
            } catch (InterruptedException var5) {
            }
         }

      }, "RPC-Callback-Handler")).start();
   }

   public void disable() {
      club.minnced.discord.rpc.DiscordRPC.INSTANCE.Discord_Shutdown();
      this._thread = null;
   }

   public String getState() {
      if (mc.player == null) {
         return "Main Menu";
      } else {
         String state = "HP : " + Math.round(mc.player.getHealth() + mc.player.getAbsorptionAmount()) + " / " + Math.round(mc.player.getMaxHealth() + mc.player.getAbsorptionAmount());
         Module piston = CandyPlusRewrite.m_module.getModuleWithClass(PistonAura.class);
         Module cev = CandyPlusRewrite.m_module.getModuleWithClass(CevBreaker.class);
         Module civ = CandyPlusRewrite.m_module.getModuleWithClass(CivBreaker.class);
         if (piston != null && cev != null && civ != null) {
            if (piston.isEnable) {
               state = "Pushing crystal";
            }

            if (cev.isEnable) {
               state = "Breaking ceil";
            }

            if (civ.isEnable) {
               state = "Attacking side";
            }

            return state;
         } else {
            return state;
         }
      }
   }
}

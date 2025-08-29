//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.command.CommandManager;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatSuffix extends Module {
   public ChatSuffix() {
      super("ChatSuffix", Module.Categories.MISC, false, false);
   }

   public void onPacketSend(PacketEvent.Send event) {
      if (event.packet instanceof CPacketChatMessage) {
         CPacketChatMessage p = (CPacketChatMessage)event.packet;
         String msg = p.getMessage();
         if (msg.startsWith("/") || msg.startsWith(CommandManager.getCommandPrefix())) {
            return;
         }

         String suffixCheck = "Candy+ Rewrite".toLowerCase();
         if (msg.contains(" || " + this.toUnicode(suffixCheck))) {
            return;
         }

         String suffix = this.toUnicode(" || " + suffixCheck);
         String newMsg = msg + suffix;
         if (newMsg.length() > 255) {
            return;
         }

         event.cancel();
         mc.player.connection.sendPacket(new CPacketChatMessage(newMsg));
      }

   }

   public String toUnicode(String s) {
      return s.toLowerCase().replace("a", "á´?").replace("b", "Ê?").replace("c", "á´?").replace("d", "á´?").replace("e", "á´?").replace("f", "êœ°").replace("g", "É¢").replace("h", "Ê?").replace("i", "Éª").replace("j", "á´?").replace("k", "á´?").replace("l", "Ê?").replace("m", "á´?").replace("n", "É´").replace("o", "á´?").replace("p", "á´?").replace("q", "Ç«").replace("r", "Ê?").replace("s", "êœ±").replace("t", "á´?").replace("u", "á´?").replace("v", "á´?").replace("w", "á´¡").replace("x", "Ë£").replace("y", "Ê?").replace("z", "á´¢");
   }
}

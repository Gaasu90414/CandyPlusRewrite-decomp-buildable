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
      return s.toLowerCase().replace("a", "�?").replace("b", "�?").replace("c", "�?").replace("d", "�?").replace("e", "�?").replace("f", "ꜰ").replace("g", "ɢ").replace("h", "�?").replace("i", "ɪ").replace("j", "�?").replace("k", "�?").replace("l", "�?").replace("m", "�?").replace("n", "ɴ").replace("o", "�?").replace("p", "�?").replace("q", "ǫ").replace("r", "�?").replace("s", "ꜱ").replace("t", "�?").replace("u", "�?").replace("v", "�?").replace("w", "ᴡ").replace("x", "ˣ").replace("y", "�?").replace("z", "ᴢ");
   }
}

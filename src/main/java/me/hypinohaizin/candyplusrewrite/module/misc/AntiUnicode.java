//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiUnicode extends Module {
   public final String[] list = new String[]{"☻", "⑥", "♻", "┥", "?��", "?��", "縺", "ⰷ", "蒹", "ꈥ", "蒹", "�?", "ㅾ", "ⰶ", "�?", "㑾", "♳", "�?", "瑥", "�?", "?��", "?��", "眽", "敫", "�?", "�?", "剢", "䨰", "�?", "慳", "�?", "�?", "䐹", "�?", "�?", "䌶", "�?", "�?", "�?", "楰", "�?", "�?", "堸", "畬", "橮", "㉨", "䐹", "䕹", "偶", "䱴", "砵", "�?", "卭", "≦", "�?", "?��", "?��", "縺", "㤵", "㔵", "�?", "�?", "�?", "�?", "�?", "\u0601", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "?�?", "?�?", "?�?", "?�?", "?�?", "???", "?��", "???", "?��", "?��", "?��", "???", "?��", "???", "?�?", "?�?", "?�?", "?�?", "?�?", "�?", "�?", "�?", "�?", "�?"};

   public AntiUnicode() {
      super("AntiUnicode", Module.Categories.MISC, false, false);
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketChat) {
         String message = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
         String[] var3 = this.list;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String unicode = var3[var5];
            if (message.contains(unicode)) {
               ((SPacketChat)event.getPacket()).chatComponent = new TextComponentString("(lag message)");
               event.setCanceled(true);
               return;
            }
         }
      }

   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiUnicode extends Module {
   public final String[] list = new String[]{"â˜»", "â‘¥", "â™»", "â”¥", "?¦¢", "?„", "ç¸º", "â°·", "è’¹", "êˆ¥", "è’¹", "ã«?", "ã…¾", "â°¶", "ã«?", "ã‘¾", "â™³", "ç?", "ç‘¥", "âˆ?", "?¦¢", "?„", "çœ½", "æ•«", "ã‘?", "æ½?", "å‰¢", "ä¨°", "ç…?", "æ…³", "ã?", "ç‰?", "ä¹", "ä¥?", "ç©?", "äŒ¶", "å•?", "æ‘?", "æ?", "æ¥°", "ã?", "ã…?", "å ¸", "ç•¬", "æ©®", "ã‰¨", "ä¹", "ä•¹", "å¶", "ä±´", "ç µ", "ä¡?", "å­", "â‰¦", "â”?", "?¦¢", "?„", "ç¸º", "ã¤µ", "ã”µ", "Ä?", "È?", "Ì?", "Ğ?", "Ô?", "\u0601", "Ü?", "à ?", "à¤?", "à¨?", "à¬?", "à¸?", "à¼?", "á€?", "á„?", "áˆ?", "áŒ?", "á?", "á”?", "á˜?", "áœ?", "á ?", "á¤?", "á¨?", "á¬?", "á°?", "á´?", "á¸?", "ç€?", "ç„?", "çˆ?", "çŒ?", "ç?", "ç”?", "ç˜?", "çœ?", "ç ?", "ç¤?", "ç¨?", "ç¬?", "ç°?", "ç´?", "ç¸?", "ç¼?", "è€?", "è„?", "èˆ?", "èŒ?", "è?", "è”?", "è˜?", "èœ?", "è ?", "è¤?", "è¨?", "è¬?", "è°?", "è´?", "è¸?", "è¼?", "é€?", "é„?", "éˆ?", "éŒ?", "é?", "é”?", "é˜?", "éœ?", "é ?", "é¤?", "é¨?", "é¬?", "é°?", "é´?", "é¸?", "é¼?", "ê€?", "ê„?", "êˆ?", "êŒ?", "ê?", "ê”?", "ê˜?", "êœ?", "ê ?", "ê¤?", "?¨?", "?°?", "?´?", "?¸?", "?¼?", "???", "?„", "???", "?Œ", "?", "?”", "???", "?œ", "???", "?¤?", "?¨?", "?¬?", "?°?", "?´?", "æ”?", "çŒ?", "ç?", "çˆ?", "ç”?"};

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

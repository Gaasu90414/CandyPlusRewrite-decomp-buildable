//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatLag extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
   protected static final String LAG_MESSAGE = "āȁ�?Ё\u0601܁�?�ँਁଁก༁�?�ᄁሁጁᐁᔁᘁᜁ᠁ᤁᨁᬁᰁᴁḁ�? ℁�?�⌁␁━✁�?�⤁⨁⬁Ⰱⴁ⸁⼁�?�\u3101㈁㌁㐁㔁㘁㜁㠁㤁㨁㬁㰁㴁㸁㼁�?�䄁䈁䌁䐁䔁䘁䜁䠁䤁䨁䬁䰁䴁丁企�?�儁刁匁吁唁嘁圁堁夁威嬁封崁币弁�?�愁戁持搁攁昁朁栁椁樁欁氁洁渁漁�?�焁爁猁琁甁瘁省码礁稁笁簁紁縁缁�?�脁舁茁萁蔁蘁蜁蠁褁訁謁谁贁踁�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�??�??�??�??�??�??�???��???��?��?��???��???�?�?�?�?�?";

   public ChatLag() {
      super("ChatLag", Module.Categories.MISC, false, false);
   }

   public void onTotemPop(EntityPlayer player) {
      if (player != null && !this.nullCheck()) {
         if (!(mc.player.getDistance(player) > (Float)this.range.getValue())) {
            String name = player.getName();
            if (player != mc.player) {
               if (!FriendManager.isFriend(name)) {
                  String msg = "/msg " + name + " " + "āȁ�?Ё\u0601܁�?�ँਁଁก༁�?�ᄁሁጁᐁᔁᘁᜁ᠁ᤁᨁᬁᰁᴁḁ�? ℁�?�⌁␁━✁�?�⤁⨁⬁Ⰱⴁ⸁⼁�?�\u3101㈁㌁㐁㔁㘁㜁㠁㤁㨁㬁㰁㴁㸁㼁�?�䄁䈁䌁䐁䔁䘁䜁䠁䤁䨁䬁䰁䴁丁企�?�儁刁匁吁唁嘁圁堁夁威嬁封崁币弁�?�愁戁持搁攁昁朁栁椁樁欁氁洁渁漁�?�焁爁猁琁甁瘁省码礁稁笁簁紁縁缁�?�脁舁茁萁蔁蘁蜁蠁褁訁謁谁贁踁�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�?�??�??�??�??�??�??�???��???��?��?��???��???�?�?�?�?�?";
                  this.sendChat(msg);
               }
            }
         }
      }
   }

   public void sendChat(String str) {
      mc.player.connection.sendPacket(new CPacketChatMessage(str));
   }
}

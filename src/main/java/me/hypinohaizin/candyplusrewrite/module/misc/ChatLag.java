//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatLag extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
   protected static final String LAG_MESSAGE = "ﾄ∧⇔?ﾐ―u0601ﾜ≒?≒､≒ｨ≒ｬ≒ｸ≒ｼ≪?≪п瘉≪戟瘰≪煤癢≪怐癶≪､≪ｨ≪ｬ≪ｰ≪ｴ≪ｸ≪ｼ? 邃≫?≫戟竦≫煤笨≫?≫､≫ｨ≫ｬ≫ｰ≫ｴ≫ｸ≫ｼ√?―u3101繹√戟辮√煤纔√怐罌√､√ｨ√ｬ√ｰ√ｴ√ｸ√ｼ∽?∽п艾∽戟苣∽煤苻∽怐茴∽､∽ｨ∽ｬ∽ｰ∽ｴ∽ｸ∽ｼ∝?∝п蛻∝戟蜷∝煤蝌∝怐蝣∝､∝ｨ∝ｬ∝ｰ∝ｴ∝ｸ∝ｼ∵?∵п謌∵戟謳∵煤譏∵怐譬∵､∵ｨ∵ｬ∵ｰ∵ｴ∵ｸ∵ｼ∫?∫п辷∫戟逅∫煤逖∫怐遐∫､∫ｨ∫ｬ∫ｰ∫ｴ∫ｸ∫ｼ∬?∬п闊∬戟關∬煤陂∬怐陟∬､∬ｨ∬ｬ∬ｰ∬ｴ∬ｸ∬ｼ?騾?驗?驤?骭?髏?髞?髦?髴?鬆?鬢?鬨?鬯?魏?魘?鮑?鮠?黴?黻?鼈?鼬?齔?齡?齪?龕?槇?熙??ｨ??ｬ??ｰ??ｴ??ｸ??ｼ???п???戟?吹?煤???怐???､?ｨ?ｬ?ｰ?ｴ?";

   public ChatLag() {
      super("ChatLag", Module.Categories.MISC, false, false);
   }

   public void onTotemPop(EntityPlayer player) {
      if (player != null && !this.nullCheck()) {
         if (!(mc.player.getDistance(player) > (Float)this.range.getValue())) {
            String name = player.getName();
            if (player != mc.player) {
               if (!FriendManager.isFriend(name)) {
                  String msg = "/msg " + name + " " + "ﾄ∧⇔?ﾐ―u0601ﾜ≒?≒､≒ｨ≒ｬ≒ｸ≒ｼ≪?≪п瘉≪戟瘰≪煤癢≪怐癶≪､≪ｨ≪ｬ≪ｰ≪ｴ≪ｸ≪ｼ? 邃≫?≫戟竦≫煤笨≫?≫､≫ｨ≫ｬ≫ｰ≫ｴ≫ｸ≫ｼ√?―u3101繹√戟辮√煤纔√怐罌√､√ｨ√ｬ√ｰ√ｴ√ｸ√ｼ∽?∽п艾∽戟苣∽煤苻∽怐茴∽､∽ｨ∽ｬ∽ｰ∽ｴ∽ｸ∽ｼ∝?∝п蛻∝戟蜷∝煤蝌∝怐蝣∝､∝ｨ∝ｬ∝ｰ∝ｴ∝ｸ∝ｼ∵?∵п謌∵戟謳∵煤譏∵怐譬∵､∵ｨ∵ｬ∵ｰ∵ｴ∵ｸ∵ｼ∫?∫п辷∫戟逅∫煤逖∫怐遐∫､∫ｨ∫ｬ∫ｰ∫ｴ∫ｸ∫ｼ∬?∬п闊∬戟關∬煤陂∬怐陟∬､∬ｨ∬ｬ∬ｰ∬ｴ∬ｸ∬ｼ?騾?驗?驤?骭?髏?髞?髦?髴?鬆?鬢?鬨?鬯?魏?魘?鮑?鮠?黴?黻?鼈?鼬?齔?齡?齪?龕?槇?熙??ｨ??ｬ??ｰ??ｴ??ｸ??ｼ???п???戟?吹?煤???怐???､?ｨ?ｬ?ｰ?ｴ?";
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

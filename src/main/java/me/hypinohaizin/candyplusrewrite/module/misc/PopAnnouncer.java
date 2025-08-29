//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.HashMap;
import java.util.Map;
import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class PopAnnouncer extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 1.0F));
   private final Map<String, Integer> popCount = new HashMap();

   public PopAnnouncer() {
      super("PopAnnouncer", Module.Categories.MISC, false, false);
   }

   public void onTotemPop(EntityPlayer player) {
      if (player != null && !this.nullCheck()) {
         if (!(mc.player.getDistance(player) > (Float)this.range.getValue())) {
            String name = player.getName();
            if (player != mc.player) {
               if (!FriendManager.isFriend(name)) {
                  int count = (Integer)this.popCount.getOrDefault(name, 0) + 1;
                  this.popCount.put(name, count);
                  String msg = "ez pop " + name + " [" + count + "]";
                  this.sendChat(msg);
               }
            }
         }
      }
   }

   public void onPlayerDeath(PlayerDeathEvent event) {
      if (event.player != null) {
         this.popCount.remove(event.player.getName());
      }

   }

   public void onPlayerLogout(EntityPlayer player) {
      if (player != null) {
         this.popCount.remove(player.getName());
      }

   }

   public void sendChat(String str) {
      mc.player.connection.sendPacket(new CPacketChatMessage(str));
   }
}

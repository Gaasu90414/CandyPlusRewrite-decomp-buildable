//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoEZ extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 50.0F, 1.0F));

   public AutoEZ() {
      super("AutoEZ", Module.Categories.MISC, false, false);
   }

   public void onPlayerDeath(PlayerDeathEvent event) {
      if (!this.nullCheck()) {
         EntityPlayer player = event.player;
         if (!(player.getHealth() > 0.0F)) {
            if (!(mc.player.getDistance(player) > (Float)this.range.getValue())) {
               if (player != mc.player) {
                  if (!FriendManager.isFriend(player.getName())) {
                     this.EZ();
                  }
               }
            }
         }
      }
   }

   public void EZ() {
      this.sendChat("you just got ez'd by candy+ rewrite");
   }

   public void sendChat(String str) {
      mc.player.connection.sendPacket(new CPacketChatMessage(str));
   }
}

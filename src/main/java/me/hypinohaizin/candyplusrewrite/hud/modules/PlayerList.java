//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerList extends Hud {
   public Setting<Integer> maxPlayers = this.register(new Setting("MaxPlayers", 5, 10, 3));
   public Setting<Boolean> health = this.register(new Setting("Health", true));
   public Setting<Boolean> distance = this.register(new Setting("Distance", true));
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));

   public PlayerList() {
      super("PlayerList", 50.0F, 50.0F);
   }

   public void onRender() {
      try {
         List<EntityPlayer> players = this.getPlayerList();
         float w = 0.0F;
         float h = 0.0F;
         float textSize = 1.0F;
         float lineHeight = RenderUtil.getStringHeight(textSize) + 4.0F;

         for(Iterator var6 = players.iterator(); var6.hasNext(); h += lineHeight) {
            EntityPlayer p = (EntityPlayer)var6.next();
            int hp = PlayerUtil.getHealth(p);
            double dist = PlayerUtil.getDistance((Entity)p);
            String str = p.getName();
            if ((Boolean)this.health.getValue()) {
               str = str + " " + this.getHealthColor(hp) + hp;
            }

            if ((Boolean)this.distance.getValue()) {
               str = str + " " + this.getDistanceColor(dist) + (int)dist;
            }

            float textW = RenderUtil.getStringWidth(str, textSize);
            if (textW > w) {
               w = textW;
            }

            RenderUtil.drawString(str, (Float)this.x.getValue(), (Float)this.y.getValue() + h, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), textSize);
         }

         this.width = w;
         this.height = h;
      } catch (Exception var13) {
      }

   }

   private ChatFormatting getDistanceColor(double d) {
      if (d > 20.0D) {
         return ChatFormatting.GREEN;
      } else {
         return d > 6.0D ? ChatFormatting.YELLOW : ChatFormatting.RED;
      }
   }

   private ChatFormatting getHealthColor(int hp) {
      if (hp > 23) {
         return ChatFormatting.GREEN;
      } else {
         return hp > 7 ? ChatFormatting.YELLOW : ChatFormatting.RED;
      }
   }

   private List<EntityPlayer> getPlayerList() {
      List<EntityPlayer> list = new ArrayList(mc.world.playerEntities);
      list.removeIf((p) -> {
         return p == mc.player;
      });
      list.sort(Comparator.comparingDouble(PlayerUtil::getDistance));
      return list.size() > (Integer)this.maxPlayers.getValue() ? new ArrayList(list.subList(0, (Integer)this.maxPlayers.getValue())) : list;
   }
}

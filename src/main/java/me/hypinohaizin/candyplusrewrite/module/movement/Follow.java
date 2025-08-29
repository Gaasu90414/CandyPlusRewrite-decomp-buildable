//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import java.util.Comparator;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Follow extends Module {
   public Setting<Follow.ModeType> mode;

   public Follow() {
      super("Follow", Module.Categories.MOVEMENT, false, false);
      this.mode = this.register(new Setting("Mode", Follow.ModeType.MOTION));
   }

   private EntityPlayer getTarget() {
      return (EntityPlayer)mc.world.playerEntities.stream().filter((p) -> {
         return p.getEntityId() != mc.player.getEntityId();
      }).filter((p) -> {
         return !p.isDead && p.getHealth() > 0.0F;
      }).filter((p) -> {
         return !FriendManager.isFriend(p.getName());
      }).min(Comparator.comparingDouble((p) -> {
         return (double)mc.player.getDistance(p);
      })).orElse((EntityPlayer) null);
   }

   @SubscribeEvent
   public void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.START && mc.player != null && mc.world != null) {
         EntityPlayer target = this.getTarget();
         if (target != null) {
            switch((Follow.ModeType)this.mode.getValue()) {
            case MOTION:
               EntityUtil.motion(target.posX, target.posY, target.posZ);
               break;
            case SET_POSITION:
               mc.player.setPosition(target.posX, target.posY, target.posZ);
               break;
            case SET_POS_UPDATE:
               mc.player.setPositionAndUpdate(target.posX, target.posY, target.posZ);
            }

         }
      }
   }

   public static enum ModeType {
      MOTION,
      SET_POSITION,
      SET_POS_UPDATE;
   }
}

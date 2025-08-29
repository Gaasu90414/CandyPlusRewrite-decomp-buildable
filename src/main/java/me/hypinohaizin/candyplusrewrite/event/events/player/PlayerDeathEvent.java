package me.hypinohaizin.candyplusrewrite.event.events.player;

import me.hypinohaizin.candyplusrewrite.event.CandyEvent;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerDeathEvent extends CandyEvent {
   public EntityPlayer player;

   public PlayerDeathEvent(EntityPlayer player) {
      this.player = player;
   }
}

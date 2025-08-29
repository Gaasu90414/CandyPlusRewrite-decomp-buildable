package me.hypinohaizin.candyplusrewrite.event.events.player;

import me.hypinohaizin.candyplusrewrite.event.CandyEvent;

public class UpdateWalkingPlayerEvent extends CandyEvent {
   public int stage;

   public UpdateWalkingPlayerEvent(int stage) {
      this.stage = stage;
   }
}

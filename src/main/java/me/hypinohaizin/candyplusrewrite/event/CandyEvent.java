package me.hypinohaizin.candyplusrewrite.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CandyEvent extends Event {
   private boolean cancelled;
   private final EventStage stage;

   public CandyEvent() {
      this(EventStage.PRE);
   }

   public CandyEvent(EventStage stage) {
      this.cancelled = false;
      this.stage = stage;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public void cancel() {
      this.cancelled = true;
   }

   public EventStage getStage() {
      return this.stage;
   }
}

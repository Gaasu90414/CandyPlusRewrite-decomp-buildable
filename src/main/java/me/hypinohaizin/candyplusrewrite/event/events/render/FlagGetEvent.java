package me.hypinohaizin.candyplusrewrite.event.events.render;

import me.hypinohaizin.candyplusrewrite.event.CandyEvent;
import net.minecraft.entity.Entity;

public class FlagGetEvent extends CandyEvent {
   private final Entity entity;
   private final int flag;
   private boolean returnValue;

   public FlagGetEvent(Entity entity, int flag, boolean returnValue) {
      this.entity = entity;
      this.flag = flag;
      this.returnValue = returnValue;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public int getFlag() {
      return this.flag;
   }

   public boolean getReturnValue() {
      return this.returnValue;
   }

   public void setReturnValue(boolean returnValue) {
      this.returnValue = returnValue;
   }
}

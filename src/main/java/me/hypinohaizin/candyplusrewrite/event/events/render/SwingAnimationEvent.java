package me.hypinohaizin.candyplusrewrite.event.events.render;

import me.hypinohaizin.candyplusrewrite.event.CandyEvent;
import net.minecraft.entity.EntityLivingBase;

public class SwingAnimationEvent extends CandyEvent {
   private final EntityLivingBase entity;
   private int speed;

   public SwingAnimationEvent(EntityLivingBase entity, Integer speed) {
      this.entity = entity;
      this.speed = speed;
   }

   public int getSpeed() {
      return this.speed;
   }

   public void setSpeed(int speed) {
      this.speed = speed;
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }
}

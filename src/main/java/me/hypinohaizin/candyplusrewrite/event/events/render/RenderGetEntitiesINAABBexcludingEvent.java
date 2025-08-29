package me.hypinohaizin.candyplusrewrite.event.events.render;

import com.google.common.base.Predicate;
import me.hypinohaizin.candyplusrewrite.event.CandyEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

public class RenderGetEntitiesINAABBexcludingEvent extends CandyEvent {
   public RenderGetEntitiesINAABBexcludingEvent(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
   }
}

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemViewModel extends Module {
   public Setting<Float> fov = this.register(new Setting("Fov", 120.0F, 300.0F, 0.0F));

   public ItemViewModel() {
      super("ItemViewModel", Module.Categories.RENDER, false, false);
   }

   @SubscribeEvent
   public void onEntityViewRender(FOVModifier event) {
      event.setFOV((Float)this.fov.getValue());
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.mixin.mixins;

import me.hypinohaizin.candyplusrewrite.event.events.render.FlagGetEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public class MixinEntity {
   @Shadow
   public EntityDataManager dataManager;
   @Shadow
   @Final
   public static DataParameter<Byte> FLAGS;

   @Inject(
      method = {"getFlag"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getFlag(int flag, CallbackInfoReturnable<Boolean> cir) {
      FlagGetEvent flagGetEvent = new FlagGetEvent((Entity)Entity.class.cast(this), flag, ((Byte)this.dataManager.get(FLAGS) & 1 << flag) != 0);
      MinecraftForge.EVENT_BUS.post(flagGetEvent);
      cir.setReturnValue(flagGetEvent.getReturnValue());
   }
}

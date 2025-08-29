package me.hypinohaizin.candyplusrewrite.mixin.mixins;

import java.util.Map;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MapData.class})
public interface IMixinMapData {
   @Accessor("mapDecorations")
   Map<String, MapDecoration> getMapDecorations();
}

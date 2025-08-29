package me.hypinohaizin.loader.mixin;

import java.util.Map;
import me.hypinohaizin.candyplusrewrite.asm.CandyAccessTransformer;
import me.hypinohaizin.candyplusrewrite.asm.CandyTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

@TransformerExclusions({"me.hypinohaizin.candyplusrewrite.asm"})
@MCVersion("1.12.2")
@Name("CandyPlusRewrite")
@SortingIndex(1001)
public class MixinLoader implements IFMLLoadingPlugin {
   private static boolean isObfuscatedEnvironment = false;

   public MixinLoader() {
      MixinBootstrap.init();
      Mixins.addConfiguration("mixins.candyplusrewrite.json");
      MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
   }

   public String[] getASMTransformerClass() {
      return new String[]{CandyTransformer.class.getName()};
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return null;
   }

   public void injectData(Map<String, Object> data) {
   }

   public String getAccessTransformerClass() {
      return CandyAccessTransformer.class.getName();
   }
}

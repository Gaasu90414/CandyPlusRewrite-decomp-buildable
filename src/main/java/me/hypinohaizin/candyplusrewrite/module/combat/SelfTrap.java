package me.hypinohaizin.candyplusrewrite.module.combat;

import java.util.HashMap;
import java.util.Map;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.util.math.BlockPos;

public class SelfTrap extends Module {
   private final Setting<Integer> blocksPerTick = this.register(new Setting("BlocksPerTick", 8, 1, 20));
   private final Setting<Integer> delay = this.register(new Setting("Delay", 50, 0, 250));
   private final Setting<Boolean> rotate = this.register(new Setting("Rotate", true));
   private final Setting<Integer> disableTime = this.register(new Setting("DisableTime", 200, 50, 300));
   private final Setting<Boolean> disable = this.register(new Setting("AutoDisable", true));
   private final Setting<Boolean> packet = this.register(new Setting("PacketPlace", false));
   private final Timer offTimer = new Timer();
   private final Timer timer = new Timer();
   private final Map<BlockPos, Integer> retries = new HashMap();
   private final Timer retryTimer = new Timer();
   private final int blocksThisTick = 0;
   private boolean isSneaking;
   private final boolean hasOffhand = false;

   public SelfTrap() {
      super("SelfTrap", Module.Categories.COMBAT, false, false);
   }
}

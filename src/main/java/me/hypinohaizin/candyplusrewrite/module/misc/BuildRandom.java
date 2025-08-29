//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.Random;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.BlockUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class BuildRandom extends Module {
   public Setting<Integer> range = this.register(new Setting("Range", 4, 10, 1));
   public Setting<Integer> delay = this.register(new Setting("Delay", 80, 500, 10));
   private final Random random = new Random();
   private final Timer timer = new Timer();

   public BuildRandom() {
      super("BuildRandom", Module.Categories.MISC, false, false);
   }

   public void onTick() {
      if (!this.nullCheck()) {
         if (this.checkHeldItem()) {
            if (this.timer.passedMs((long)(Integer)this.delay.getValue())) {
               int bound = (Integer)this.range.getValue() * 2 + 1;
               int attempts = 0;

               BlockPos pos;
               try {
                  do {
                     pos = (new BlockPos(mc.player.getPosition())).add(this.random.nextInt(bound) - (Integer)this.range.getValue(), this.random.nextInt(bound) - (Integer)this.range.getValue(), this.random.nextInt(bound) - (Integer)this.range.getValue());
                     ++attempts;
                  } while(attempts < 128 && !this.tryToPlaceBlock(pos));
               } catch (Exception var5) {
                  var5.printStackTrace();
               }

            }
         }
      }
   }

   private boolean tryToPlaceBlock(BlockPos pos) {
      if (pos != null && mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
         double maxReach = (double)(Integer)this.range.getValue() + 1.5D;
         if (mc.player.getDistanceSq(pos) > maxReach * maxReach) {
            return false;
         } else {
            boolean result = BlockUtil.placeBlock(pos, false);
            if (result) {
               this.timer.reset();
            }

            return result;
         }
      } else {
         return false;
      }
   }

   private boolean checkHeldItem() {
      ItemStack stack = mc.player.inventory.getCurrentItem();
      return !stack.isEmpty() && (stack.getItem() instanceof ItemBlock || stack.getItem() instanceof ItemSkull);
   }
}

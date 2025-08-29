//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumHand;

public class TPCart extends Module {
   public Setting<Float> range = this.register(new Setting("Range", 10.0F, 20.0F, 2.0F));
   public Setting<Float> delay = this.register(new Setting("Delay", 10.0F, 30.0F, 1.0F));
   public Timer timer = new Timer();

   public TPCart() {
      super("TPCart", Module.Categories.MOVEMENT, false, false);
   }

   public void onTick() {
      try {
         if (this.nullCheck()) {
            return;
         }

         if (this.timer == null) {
            this.timer = new Timer();
         }

         if (this.timer.passedX((double)(Float)this.delay.getValue())) {
            List<Entity> carts = (List)mc.world.loadedEntityList.stream().filter((e) -> {
               return e instanceof EntityMinecart;
            }).filter((e) -> {
               return !e.equals(mc.player.getRidingEntity());
            }).filter((e) -> {
               return PlayerUtil.getDistance(e) <= (double)(Float)this.range.getValue();
            }).collect(Collectors.toList());
            Entity minecart = (Entity)carts.get((new Random()).nextInt(carts.size()));
            if (minecart == null) {
               return;
            }

            mc.playerController.interactWithEntity(mc.player, minecart, EnumHand.MAIN_HAND);
         }
      } catch (Exception var3) {
      }

   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.StringUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;

public class DonkeyNotifier extends Module {
   private final Setting<Boolean> donkey = this.register(new Setting("Donkey", true));
   private final Setting<Boolean> llama = this.register(new Setting("Llama", true));
   private List<Entity> entities = new ArrayList();

   public DonkeyNotifier() {
      super("DonkeyNotifer", Module.Categories.MISC, false, false);
   }

   public void onDisable() {
      this.entities = new ArrayList();
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         List<Entity> donkeys = new ArrayList(mc.world.loadedEntityList);
         donkeys.removeIf((e2) -> {
            return (!(Boolean)this.donkey.getValue() || !(e2 instanceof EntityDonkey)) && (!(Boolean)this.llama.value || !(e2 instanceof EntityLlama));
         });
         Iterator iterator = donkeys.iterator();

         while(iterator.hasNext()) {
            Entity e = (Entity)iterator.next();
            if (!this.entities.contains(e)) {
               this.entities.add(e);
               this.sendMessage("Found a " + (e instanceof EntityDonkey ? "Donkey" : "Llama") + " at " + StringUtil.getPositionString(e.getPosition()));
            }
         }

      }
   }
}

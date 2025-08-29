//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.utils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;

public class ResistanceDetector implements Util {
   public static HashMap<String, Integer> resistanceList = new HashMap();

   public static void init() {
      ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
      scheduler.scheduleAtFixedRate(() -> {
         try {
            HashMap a = new HashMap();
            ArrayList i = new ArrayList();
            resistanceList.forEach((k, v) -> {
               if (v > 0) {
                  a.put(k, v - 1);
               } else {
                  i.add(k);
               }

            });
            a.forEach((k, v) -> {
               if (resistanceList.containsKey(k)) {
                  resistanceList.replace((String)k, (Integer)v);
               }

            });
            a.clear();
            i.forEach((w) -> {
               resistanceList.remove(i);
            });
         } catch (ConcurrentModificationException var2) {
         }

      }, 0L, 1L, TimeUnit.SECONDS);
   }

   public static void onUpdate() {
      if (mc.world != null && mc.player != null) {
         Iterator var0 = mc.world.playerEntities.iterator();

         while(var0.hasNext()) {
            EntityPlayer uwu = (EntityPlayer)var0.next();
            if (!(uwu.getAbsorptionAmount() < 9.0F)) {
               resistanceList.remove(uwu.getName());
               resistanceList.put(uwu.getName(), 180);
            }
         }
      }

   }
}

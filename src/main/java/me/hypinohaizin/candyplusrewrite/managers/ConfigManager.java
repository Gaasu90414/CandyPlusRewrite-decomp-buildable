package me.hypinohaizin.candyplusrewrite.managers;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.module.Module;

public class ConfigManager {
   public static void saveConfigs() {
      String folder = "candyplusrewrite/";
      File dir = new File("candyplusrewrite/");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      Module.Categories[] var2 = Module.Categories.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Module.Categories category = var2[var4];
         File categoryDir = new File("candyplusrewrite/" + category.name().toLowerCase());
         if (!categoryDir.exists()) {
            categoryDir.mkdirs();
         }
      }

      List<Module> modules = CandyPlusRewrite.m_module.modules;
      Iterator var9 = modules.iterator();

      while(var9.hasNext()) {
         Module module = (Module)var9.next();

         try {
            module.saveConfig();
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   public static void loadConfigs() {
      List<Module> modules = CandyPlusRewrite.m_module.modules;
      Iterator var1 = modules.iterator();

      while(var1.hasNext()) {
         Module module = (Module)var1.next();

         try {
            module.loadConfig();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }
}

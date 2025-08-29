package me.hypinohaizin.candyplusrewrite.command.impl;

import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.command.Command;
import me.hypinohaizin.candyplusrewrite.managers.ModuleManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

@Command.Declaration(
   name = "Bind",
   syntax = "bind [module] key",
   alias = {"bind", "b", "setbind", "key"}
)
public class BindCommand extends Command {
   public void onCommand(String label, String[] args, boolean clientSide) {
      if (args.length < 2) {
         ChatUtil.sendMessage(this.getSyntax());
      } else {
         String moduleName = args[0];
         Module m = ModuleManager.getModule(moduleName);
         if (m == null) {
            Iterator var6 = ModuleManager.getModules().iterator();

            while(var6.hasNext()) {
               Module mod = (Module)var6.next();
               if (mod.getName().equalsIgnoreCase(moduleName)) {
                  m = mod;
                  break;
               }
            }
         }

         if (m == null) {
            ChatUtil.sendMessage("Module " + moduleName + " is Not Found");
         } else {
            String value = args[1].toUpperCase();
            if ("NONE".equalsIgnoreCase(value)) {
               m.setKey(-1);
               ChatUtil.sendMessage("Module " + m.getName() + " bind set to: NONE!");
            } else if (value.length() == 1) {
               int key = Keyboard.getKeyIndex(value);
               if (key == 0) {
                  ChatUtil.sendMessage(this.getSyntax());
               } else {
                  m.setKey(key);
                  ChatUtil.sendMessage("Module " + m.getName() + " bind set to: " + value + "!");
               }
            } else {
               ChatUtil.sendMessage(this.getSyntax());
            }

         }
      }
   }
}

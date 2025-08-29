package me.hypinohaizin.candyplusrewrite.command.impl;

import java.util.Optional;
import me.hypinohaizin.candyplusrewrite.command.Command;
import me.hypinohaizin.candyplusrewrite.managers.ModuleManager;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;

@Command.Declaration(
   name = "Toggle",
   syntax = "toggle [module]",
   alias = {"toggle", "t", "enable", "disable"}
)
public class ToggleCommand extends Command {
   public void onCommand(String label, String[] args, boolean clientSide) {
      if (args.length < 1) {
         ChatUtil.sendMessage(this.getSyntax());
      } else {
         String moduleName = args[0];
         Module module = ModuleManager.getModule(moduleName);
         if (module == null) {
            Optional<Module> match = ModuleManager.getModules().stream().filter((m) -> {
               return m.getName().equalsIgnoreCase(moduleName);
            }).findFirst();
            if (match.isPresent()) {
               module = (Module)match.get();
            }
         }

         if (module == null) {
            ChatUtil.sendMessage(this.getSyntax());
         } else {
            module.toggle();
         }
      }
   }
}

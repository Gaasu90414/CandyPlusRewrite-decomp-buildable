package me.hypinohaizin.candyplusrewrite.command.impl;

import me.hypinohaizin.candyplusrewrite.command.Command;
import me.hypinohaizin.candyplusrewrite.command.CommandManager;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;

@Command.Declaration(
   name = "Prefix",
   syntax = "prefix <value> (no letters or numbers)",
   alias = {"prefix", "setprefix", "cmdprefix", "commandprefix"}
)
public class PrefixCommand extends Command {
   public void onCommand(String label, String[] args, boolean clientSide) {
      if (args.length < 1) {
         ChatUtil.sendMessage(this.getSyntax());
      } else {
         String val = args[0];
         if (val.length() == 1 && !Character.isLetterOrDigit(val.charAt(0))) {
            CommandManager.setCommandPrefix(val);
            ChatUtil.sendMessage("Prefix set: \"" + val + "\"!");
         } else {
            ChatUtil.sendMessage(this.getSyntax());
         }

      }
   }
}

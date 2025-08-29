package me.hypinohaizin.candyplusrewrite.command.impl;

import java.util.ArrayList;
import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.command.Command;
import me.hypinohaizin.candyplusrewrite.command.CommandManager;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;

@Command.Declaration(
   name = "Commands",
   syntax = "commands",
   alias = {"commands", "cmds", "help"}
)
public class CommandsCommand extends Command {
   public void onCommand(String label, String[] args, boolean clientSide) {
      ArrayList<Command> cmds = CommandManager.getCommands();
      StringBuilder sb = new StringBuilder("Commands: ");
      Iterator var6 = cmds.iterator();

      while(var6.hasNext()) {
         Command cmd = (Command)var6.next();
         sb.append(cmd.getName()).append(", ");
      }

      if (sb.length() > 11) {
         sb.setLength(sb.length() - 2);
      }

      ChatUtil.sendMessage(sb.toString());
   }
}

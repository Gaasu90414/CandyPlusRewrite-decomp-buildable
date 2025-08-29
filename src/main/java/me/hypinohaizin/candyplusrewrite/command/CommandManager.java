package me.hypinohaizin.candyplusrewrite.command;

import java.util.ArrayList;
import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.command.impl.FriendCommand;
import me.hypinohaizin.candyplusrewrite.command.impl.PrefixCommand;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;

public class CommandManager {
   private static String commandPrefix = "-";
   public static final ArrayList<Command> commands = new ArrayList();
   public static boolean isValidCommand = false;

   public static void init() {
      registerCommand(new PrefixCommand());
      registerCommand(new FriendCommand());
   }

   public static void registerCommand(Command command) {
      commands.add(command);
   }

   public static ArrayList<Command> getCommands() {
      return commands;
   }

   public static String getCommandPrefix() {
      return commandPrefix;
   }

   public static void setCommandPrefix(String prefix) {
      commandPrefix = prefix;
   }

   public static void callCommand(String input, boolean clientSide) {
      String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
      String commandLabel = split[0];
      String argsString = input.substring(commandLabel.length()).trim();
      isValidCommand = false;
      Iterator var5 = commands.iterator();

      while(var5.hasNext()) {
         Command command = (Command)var5.next();
         String[] var7 = command.getAlias();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String alias = var7[var9];
            if (alias.equalsIgnoreCase(commandLabel)) {
               isValidCommand = true;

               try {
                  command.onCommand(argsString, argsString.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"), clientSide);
               } catch (Exception var12) {
                  ChatUtil.sendMessage(command.getSyntax());
               }
               break;
            }
         }

         if (isValidCommand) {
            break;
         }
      }

      if (!isValidCommand) {
         ChatUtil.sendMessage("Error! Invalid command!");
      }

   }
}

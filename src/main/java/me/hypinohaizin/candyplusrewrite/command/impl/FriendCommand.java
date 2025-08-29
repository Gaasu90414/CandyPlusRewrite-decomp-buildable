package me.hypinohaizin.candyplusrewrite.command.impl;

import me.hypinohaizin.candyplusrewrite.command.Command;
import me.hypinohaizin.candyplusrewrite.managers.FriendManager;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;

@Command.Declaration(
   name = "Friend",
   syntax = "friend list/add/del [player]",
   alias = {"friend", "friends", "f"}
)
public class FriendCommand extends Command {
   public void onCommand(String label, String[] args, boolean clientSide) {
      if (args.length < 1) {
         ChatUtil.sendMessage(this.getSyntax());
      } else {
         String action = args[0].toLowerCase();
         byte var7 = -1;
         switch(action.hashCode()) {
         case 96417:
            if (action.equals("add")) {
               var7 = 1;
            }
            break;
         case 99339:
            if (action.equals("del")) {
               var7 = 2;
            }
            break;
         case 3322014:
            if (action.equals("list")) {
               var7 = 0;
            }
         }

         String msg;
         String name;
         switch(var7) {
         case 0:
            msg = "Friends: " + FriendManager.getFriendsByName() + ".";
            break;
         case 1:
            if (args.length < 2) {
               msg = this.getSyntax();
            } else {
               name = args[1];
               if (FriendManager.isFriend(name)) {
                  msg = name + " is already your friend.";
               } else {
                  FriendManager.addFriend(name);
                  msg = "Added friend: " + name + ".";
               }
            }
            break;
         case 2:
            if (args.length < 2) {
               msg = this.getSyntax();
            } else {
               name = args[1];
               if (FriendManager.isFriend(name)) {
                  FriendManager.delFriend(name);
                  msg = "Deleted friend: " + name + ".";
               } else {
                  msg = name + " isn't your friend.";
               }
            }
            break;
         default:
            msg = this.getSyntax();
         }

         ChatUtil.sendMessage(msg);
      }
   }
}

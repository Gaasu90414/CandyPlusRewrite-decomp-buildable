//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.entity.player.EntityPlayer;

public class FriendManager {
   private static final ArrayList<FriendManager.Friend> friends = new ArrayList();
   private static final ArrayList<FriendManager.Ignore> ignores = new ArrayList();
   private static final File FILE = new File("candyplusrewrite/friend.json");
   private static final Gson GSON = new Gson();

   public static ArrayList<FriendManager.Friend> getFriends() {
      return friends;
   }

   public static ArrayList<FriendManager.Ignore> getIgnores() {
      return ignores;
   }

   public static ArrayList<String> getFriendsByName() {
      ArrayList<String> names = new ArrayList();
      Iterator var1 = getFriends().iterator();

      while(var1.hasNext()) {
         FriendManager.Friend f = (FriendManager.Friend)var1.next();
         names.add(f.getName());
      }

      return names;
   }

   public static ArrayList<String> getIgnoresByName() {
      ArrayList<String> names = new ArrayList();
      Iterator var1 = getIgnores().iterator();

      while(var1.hasNext()) {
         FriendManager.Ignore i = (FriendManager.Ignore)var1.next();
         names.add(i.getName());
      }

      return names;
   }

   public static boolean isFriend(String name) {
      return isOnFriendList(name);
   }

   public static boolean isFriend(EntityPlayer p) {
      return p == null ? false : isOnFriendList(p.getName());
   }

   public static boolean isOnFriendList(String name) {
      String n = normalize(name);
      Iterator var2 = getFriends().iterator();

      FriendManager.Friend f;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         f = (FriendManager.Friend)var2.next();
      } while(!normalize(f.getName()).equals(n));

      return true;
   }

   public static boolean isIgnore(String name) {
      String n = normalize(name);
      Iterator var2 = getIgnores().iterator();

      FriendManager.Ignore i;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         i = (FriendManager.Ignore)var2.next();
      } while(!normalize(i.getName()).equals(n));

      return true;
   }

   public static boolean isOnIgnoreList(String name) {
      String n = normalize(name);
      Iterator var2 = getIgnores().iterator();

      FriendManager.Ignore i;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         i = (FriendManager.Ignore)var2.next();
      } while(!normalize(i.getName()).equals(n));

      return true;
   }

   public static FriendManager.Friend getFriend(String name) {
      String n = normalize(name);
      Iterator var2 = getFriends().iterator();

      FriendManager.Friend f;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         f = (FriendManager.Friend)var2.next();
      } while(!normalize(f.getName()).equals(n));

      return f;
   }

   public static FriendManager.Ignore getIgnore(String name) {
      String n = normalize(name);
      Iterator var2 = getIgnores().iterator();

      FriendManager.Ignore i;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         i = (FriendManager.Ignore)var2.next();
      } while(!normalize(i.getName()).equals(n));

      return i;
   }

   public static void addFriend(String name) {
      if (!isOnFriendList(name)) {
         friends.add(new FriendManager.Friend(name));
         save();
      }

   }

   public static void delFriend(String name) {
      FriendManager.Friend f = getFriend(name);
      if (f != null) {
         friends.remove(f);
         save();
      }

   }

   public static void addIgnore(String name) {
      if (!isOnIgnoreList(name)) {
         ignores.add(new FriendManager.Ignore(name));
         save();
      }

   }

   public static void delIgnore(String name) {
      FriendManager.Ignore i = getIgnore(name);
      if (i != null) {
         ignores.remove(i);
         save();
      }

   }

   public static void clearIgnoreList() {
      ignores.clear();
      save();
   }

   private static String normalize(String s) {
      if (s == null) {
         return "";
      } else {
         String t = s.trim();
         StringBuilder b = new StringBuilder(t.length());
         boolean code = false;

         for(int i = 0; i < t.length(); ++i) {
            char c = t.charAt(i);
            if (c == 167) {
               code = true;
            } else if (code) {
               code = false;
            } else {
               b.append(c);
            }
         }

         return b.toString().toLowerCase(Locale.ROOT);
      }
   }

   public static void save() {
      try {
         if (!FILE.getParentFile().exists()) {
            FILE.getParentFile().mkdirs();
         }

         FriendManager.FriendData data = new FriendManager.FriendData(friends, ignores);
         Writer writer = new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8);
         Throwable var2 = null;

         try {
            GSON.toJson(data, writer);
         } catch (Throwable var12) {
            var2 = var12;
            throw var12;
         } finally {
            if (writer != null) {
               if (var2 != null) {
                  try {
                     writer.close();
                  } catch (Throwable var11) {
                     var2.addSuppressed(var11);
                  }
               } else {
                  writer.close();
               }
            }

         }
      } catch (IOException var14) {
         var14.printStackTrace();
      }

   }

   public static void load() {
      if (FILE.exists()) {
         try {
            Reader reader = new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8);
            Throwable var1 = null;

            try {
               Type type = (new TypeToken<FriendManager.FriendData>() {
               }).getType();
               FriendManager.FriendData data = (FriendManager.FriendData)GSON.fromJson(reader, type);
               friends.clear();
               ignores.clear();
               if (data != null) {
                  if (data.friends != null) {
                     friends.addAll(data.friends);
                  }

                  if (data.ignores != null) {
                     ignores.addAll(data.ignores);
                  }
               }
            } catch (Throwable var12) {
               var1 = var12;
               throw var12;
            } finally {
               if (reader != null) {
                  if (var1 != null) {
                     try {
                        reader.close();
                     } catch (Throwable var11) {
                        var1.addSuppressed(var11);
                     }
                  } else {
                     reader.close();
                  }
               }

            }
         } catch (IOException var14) {
            var14.printStackTrace();
         }

      }
   }

   static {
      load();
   }

   private static class FriendData {
      ArrayList<FriendManager.Friend> friends;
      ArrayList<FriendManager.Ignore> ignores;

      FriendData(ArrayList<FriendManager.Friend> friends, ArrayList<FriendManager.Ignore> ignores) {
         this.friends = new ArrayList(friends);
         this.ignores = new ArrayList(ignores);
      }
   }

   public static class Ignore {
      private final String name;

      public Ignore(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }

   public static class Friend {
      private final String name;

      public Friend(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }
}

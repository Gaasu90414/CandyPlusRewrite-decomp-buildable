//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.client.Minecraft;

public abstract class Command {
   protected static final Minecraft mc = Minecraft.getMinecraft();
   private final String name;
   private final String[] alias;
   private final String syntax;

   public Command() {
      Command.Declaration dec = (Command.Declaration)this.getClass().getAnnotation(Command.Declaration.class);
      this.name = dec.name();
      this.alias = dec.alias();
      this.syntax = dec.syntax();
   }

   public String getName() {
      return this.name;
   }

   public String[] getAlias() {
      return this.alias;
   }

   public String getSyntax() {
      return CommandManager.getCommandPrefix() + this.syntax;
   }

   public abstract void onCommand(String var1, String[] var2, boolean var3);

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface Declaration {
      String name();

      String syntax();

      String[] alias() default {};
   }
}

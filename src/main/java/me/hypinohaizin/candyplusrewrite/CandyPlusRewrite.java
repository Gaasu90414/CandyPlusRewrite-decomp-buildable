//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite;

import me.hypinohaizin.candyplusrewrite.command.CommandManager;
import me.hypinohaizin.candyplusrewrite.mainmenu.MainMenu;
import me.hypinohaizin.candyplusrewrite.managers.ConfigManager;
import me.hypinohaizin.candyplusrewrite.managers.EventManager;
import me.hypinohaizin.candyplusrewrite.managers.FontManager;
import me.hypinohaizin.candyplusrewrite.managers.HoleManager;
import me.hypinohaizin.candyplusrewrite.managers.ModuleManager;
import me.hypinohaizin.candyplusrewrite.managers.NotificationManager;
import me.hypinohaizin.candyplusrewrite.managers.RotateManager;
import me.hypinohaizin.candyplusrewrite.managers.RpcManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(
   modid = "candyplusrewrite",
   name = "Candy+ Rewrite",
   version = "0.3.4"
)
public class CandyPlusRewrite {
   public static final String MODID = "candyplusrewrite";
   public static final String NAME = "Candy+ Rewrite";
   public static final String NAME2 = "Candy+R";
   public static final String VERSION = "0.3.4";
   public static final String NV = "Candy+ Rewrite v0.3.4";
   public static ModuleManager m_module = new ModuleManager();
   public static EventManager m_event = new EventManager();
   public static FontManager m_font = new FontManager();
   public static HoleManager m_hole = new HoleManager();
   public static RpcManager m_rpc = new RpcManager();
   public static RotateManager m_rotate = new RotateManager();
   public static NotificationManager m_notif = new NotificationManager();
   private static Logger logger;
   private static boolean savedConfig = false;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      logger = event.getModLog();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      try {
      } catch (Exception var3) {
         logger.log(Level.FATAL, "Failed to initialize CandyPlusRewrite loader. The game will now exit.", var3);
         Runtime.getRuntime().halt(1);
         return;
      }

      Info("loading Candy+ Rewrite ...");
      Display.setTitle("Candy+ Rewrite v0.3.4");
      m_event.load();
      m_module.load();
      m_font.load();
      m_hole.load();
      m_rpc.load();
      m_rotate.load();
      m_notif.load();
      CommandManager.init();
      Info("Loading Configs...");
      ConfigManager.loadConfigs();
      Info("Successfully Load CandyPlusRewrite!");
      if (event.getSide().isClient()) {
         Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
         MinecraftForge.EVENT_BUS.register(new CandyPlusRewrite.GuiEventHandler());
      }

   }

   public static void unload() {
      if (!savedConfig) {
         Info("Saving Configs...");
         ConfigManager.saveConfigs();
         Info("Successfully Save Configs!");
         savedConfig = true;
      }

   }

   public static void Info(String msg) {
      if (logger != null) {
         logger.info(msg);
      }
   }

   public static void Log(Level level, String msg) {
      logger.log(level, msg);
   }

   public static class GuiEventHandler {
      @SubscribeEvent
      public void onGuiOpen(GuiOpenEvent event) {
         if (event.getGui() instanceof GuiMainMenu) {
            event.setGui(new MainMenu());
         }

      }
   }
}

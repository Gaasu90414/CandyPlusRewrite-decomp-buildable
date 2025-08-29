//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.event.events.player.PlayerDeathEvent;
import me.hypinohaizin.candyplusrewrite.hud.modules.CombatInfo;
import me.hypinohaizin.candyplusrewrite.hud.modules.InventoryViewer;
import me.hypinohaizin.candyplusrewrite.hud.modules.ModuleList;
import me.hypinohaizin.candyplusrewrite.hud.modules.PlayerList;
import me.hypinohaizin.candyplusrewrite.hud.modules.PlayerModel;
import me.hypinohaizin.candyplusrewrite.hud.modules.PvPResources;
import me.hypinohaizin.candyplusrewrite.hud.modules.TargetHUD;
import me.hypinohaizin.candyplusrewrite.hud.modules.Watermark;
import me.hypinohaizin.candyplusrewrite.hud.modules.Watermark2;
import me.hypinohaizin.candyplusrewrite.hud.modules.Welcomer;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.combat.AntiBurrow;
import me.hypinohaizin.candyplusrewrite.module.combat.AntiRegear;
import me.hypinohaizin.candyplusrewrite.module.combat.AnvilAura;
import me.hypinohaizin.candyplusrewrite.module.combat.Aura;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoChase;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoCity;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoCityRewrite;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoMend;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoPush;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoSelfFill;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoTotem;
import me.hypinohaizin.candyplusrewrite.module.combat.BedAura;
import me.hypinohaizin.candyplusrewrite.module.combat.Blocker;
import me.hypinohaizin.candyplusrewrite.module.combat.BowSpam;
import me.hypinohaizin.candyplusrewrite.module.combat.CevBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.CivBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.CrystalAura;
import me.hypinohaizin.candyplusrewrite.module.combat.HoleFill;
import me.hypinohaizin.candyplusrewrite.module.combat.PistonAura;
import me.hypinohaizin.candyplusrewrite.module.combat.Quiver;
import me.hypinohaizin.candyplusrewrite.module.combat.SelfAnvil;
import me.hypinohaizin.candyplusrewrite.module.combat.TPAura;
import me.hypinohaizin.candyplusrewrite.module.combat.Velocity;
import me.hypinohaizin.candyplusrewrite.module.exploit.Burrow;
import me.hypinohaizin.candyplusrewrite.module.exploit.CornerClip;
import me.hypinohaizin.candyplusrewrite.module.exploit.InstantMine;
import me.hypinohaizin.candyplusrewrite.module.exploit.NoMiningTrace;
import me.hypinohaizin.candyplusrewrite.module.exploit.PacketCanceller;
import me.hypinohaizin.candyplusrewrite.module.exploit.PingBypass;
import me.hypinohaizin.candyplusrewrite.module.exploit.SilentPickel;
import me.hypinohaizin.candyplusrewrite.module.exploit.TrapPhase;
import me.hypinohaizin.candyplusrewrite.module.exploit.XCarry;
import me.hypinohaizin.candyplusrewrite.module.misc.AntiMapBan;
import me.hypinohaizin.candyplusrewrite.module.misc.AntiUnicode;
import me.hypinohaizin.candyplusrewrite.module.misc.ArmorAlert;
import me.hypinohaizin.candyplusrewrite.module.misc.AutoDrop;
import me.hypinohaizin.candyplusrewrite.module.misc.AutoEZ;
import me.hypinohaizin.candyplusrewrite.module.misc.BaseFinder;
import me.hypinohaizin.candyplusrewrite.module.misc.BuildRandom;
import me.hypinohaizin.candyplusrewrite.module.misc.ChatLag;
import me.hypinohaizin.candyplusrewrite.module.misc.ChatSuffix;
import me.hypinohaizin.candyplusrewrite.module.misc.DiscordRPC;
import me.hypinohaizin.candyplusrewrite.module.misc.DonkeyNotifier;
import me.hypinohaizin.candyplusrewrite.module.misc.FakePlayer;
import me.hypinohaizin.candyplusrewrite.module.misc.HoleBreakAlert;
import me.hypinohaizin.candyplusrewrite.module.misc.InstantWither;
import me.hypinohaizin.candyplusrewrite.module.misc.PopAnnouncer;
import me.hypinohaizin.candyplusrewrite.module.misc.Refill;
import me.hypinohaizin.candyplusrewrite.module.misc.Spammer;
import me.hypinohaizin.candyplusrewrite.module.movement.AntiHunger;
import me.hypinohaizin.candyplusrewrite.module.movement.Blink;
import me.hypinohaizin.candyplusrewrite.module.movement.BlockMove;
import me.hypinohaizin.candyplusrewrite.module.movement.FastStop;
import me.hypinohaizin.candyplusrewrite.module.movement.Flight;
import me.hypinohaizin.candyplusrewrite.module.movement.Follow;
import me.hypinohaizin.candyplusrewrite.module.movement.HoleTP;
import me.hypinohaizin.candyplusrewrite.module.movement.NoSlowdown;
import me.hypinohaizin.candyplusrewrite.module.movement.PhaseFly;
import me.hypinohaizin.candyplusrewrite.module.movement.PhaseWalk;
import me.hypinohaizin.candyplusrewrite.module.movement.ReverseStep;
import me.hypinohaizin.candyplusrewrite.module.movement.Speed;
import me.hypinohaizin.candyplusrewrite.module.movement.Step;
import me.hypinohaizin.candyplusrewrite.module.movement.TPCart;
import me.hypinohaizin.candyplusrewrite.module.render.Afterglow;
import me.hypinohaizin.candyplusrewrite.module.render.Animation;
import me.hypinohaizin.candyplusrewrite.module.render.BreadCrumbs;
import me.hypinohaizin.candyplusrewrite.module.render.BurrowESP;
import me.hypinohaizin.candyplusrewrite.module.render.CandyCrystal;
import me.hypinohaizin.candyplusrewrite.module.render.CityESP;
import me.hypinohaizin.candyplusrewrite.module.render.ClickGUI;
import me.hypinohaizin.candyplusrewrite.module.render.CrystalSpawns;
import me.hypinohaizin.candyplusrewrite.module.render.CustomFov;
import me.hypinohaizin.candyplusrewrite.module.render.EnchantmentColor;
import me.hypinohaizin.candyplusrewrite.module.render.Freecam;
import me.hypinohaizin.candyplusrewrite.module.render.HUDEditor;
import me.hypinohaizin.candyplusrewrite.module.render.HoleESP;
import me.hypinohaizin.candyplusrewrite.module.render.ItemViewModel;
import me.hypinohaizin.candyplusrewrite.module.render.NoOverlay;
import me.hypinohaizin.candyplusrewrite.module.render.Notification;
import me.hypinohaizin.candyplusrewrite.module.render.SmallShield;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleManager extends Manager {
   public static LinkedHashMap<Class<? extends Module>, Module> modulesClassMap = new LinkedHashMap();
   public static LinkedHashMap<String, Module> modulesNameMap = new LinkedHashMap();
   public List<Module> modules = new ArrayList();

   public void load() {
      this.register(new AntiBurrow());
      this.register(new Aura());
      this.register(new AntiRegear());
      this.register(new AnvilAura());
      this.register(new AutoChase());
      this.register(new AutoCity());
      this.register(new AutoCityRewrite());
      this.register(new AutoMend());
      this.register(new AutoPush());
      this.register(new AutoSelfFill());
      this.register(new AutoTotem());
      this.register(new BedAura());
      this.register(new Blocker());
      this.register(new BowSpam());
      this.register(new CevBreaker());
      this.register(new CivBreaker());
      this.register(new CrystalAura());
      this.register(new HoleFill());
      this.register(new PistonAura());
      this.register(new Quiver());
      this.register(new SelfAnvil());
      this.register(new TPAura());
      this.register(new Velocity());
      this.register(new Burrow());
      this.register(new CornerClip());
      this.register(new InstantMine());
      this.register(new NoMiningTrace());
      this.register(new PacketCanceller());
      this.register(new PingBypass());
      this.register(new SilentPickel());
      this.register(new TrapPhase());
      this.register(new XCarry());
      this.register(new AntiMapBan());
      this.register(new AntiUnicode());
      this.register(new AutoDrop());
      this.register(new AutoEZ());
      this.register(new ArmorAlert());
      this.register(new BaseFinder());
      this.register(new BuildRandom());
      this.register(new ChatLag());
      this.register(new ChatSuffix());
      this.register(new DiscordRPC());
      this.register(new DonkeyNotifier());
      this.register(new FakePlayer());
      this.register(new HoleBreakAlert());
      this.register(new InstantWither());
      this.register(new PopAnnouncer());
      this.register(new Refill());
      this.register(new Spammer());
      this.register(new AntiHunger());
      this.register(new Blink());
      this.register(new BlockMove());
      this.register(new HoleTP());
      this.register(new FastStop());
      this.register(new Follow());
      this.register(new NoSlowdown());
      this.register(new PhaseWalk());
      this.register(new PhaseFly());
      this.register(new ReverseStep());
      this.register(new Step());
      this.register(new Flight());
      this.register(new Speed());
      this.register(new TPCart());
      this.register(new Afterglow());
      this.register(new Animation());
      this.register(new BreadCrumbs());
      this.register(new BurrowESP());
      this.register(new CandyCrystal());
      this.register(new CityESP());
      this.register(new ClickGUI());
      this.register(new CrystalSpawns());
      this.register(new CustomFov());
      this.register(new EnchantmentColor());
      this.register(new Freecam());
      this.register(new HoleESP());
      this.register(new HUDEditor());
      this.register(new ItemViewModel());
      this.register(new NoOverlay());
      this.register(new Notification());
      this.register(new SmallShield());
      this.register(new Watermark());
      this.register(new Watermark2());
      this.register(new PvPResources());
      this.register(new PlayerList());
      this.register(new CombatInfo());
      this.register(new PlayerModel());
      this.register(new TargetHUD());
      this.register(new ModuleList());
      this.register(new InventoryViewer());
      this.register(new Welcomer());
   }

   public void register(Module module) {
      this.modules.add(module);
   }

   public void onUpdate() {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onUpdate();
      });
   }

   public void onTick() {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onTick();
      });
   }

   public void onConnect() {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onConnect();
      });
   }

   public void onRender2D() {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onRender2D();
      });
   }

   public void onRender3D() {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         mc.profiler.startSection(m.name);
         m.onRender3D();
         mc.profiler.endSection();
      });
   }

   public void onRender3D(float ticks) {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         mc.profiler.startSection(m.name);
         m.onRender3D(ticks);
         mc.profiler.endSection();
      });
   }

   public void onPacketSend(PacketEvent.Send event) {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onPacketSend(event);
      });
   }

   public void onPacketReceive(PacketEvent.Receive event) {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onPacketReceive(event);
      });
   }

   public void onTotemPop(EntityPlayer player) {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onTotemPop(player);
      });
   }

   public void onPlayerDeath(PlayerDeathEvent event) {
      this.modules.stream().filter((m) -> {
         return m.isEnable;
      }).forEach((m) -> {
         m.onPlayerDeath(event);
      });
   }

   public void onKeyInput(int key) {
      this.modules.stream().filter((m) -> {
         return m.key.getKey() == key;
      }).forEach((m) -> {
         m.toggle();
      });
   }

   public List<Module> getModulesWithCategories(Module.Categories c) {
      List<Module> moduleList = new ArrayList();
      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         if (m.category == c) {
            moduleList.add(m);
         }
      }

      return moduleList;
   }

   public Module getModuleWithName(String name) {
      Module r = null;
      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         if (m.name.equalsIgnoreCase(name)) {
            r = m;
         }
      }

      return r;
   }

   public Module getModuleWithClass(Class<? extends Module> clazz) {
      Module r = null;
      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         if (m.getClass() == clazz) {
            r = m;
         }
      }

      return r;
   }

   public static Collection<Module> getModules() {
      return modulesClassMap.values();
   }

   public static ArrayList<Module> getModulesInCategory(Module.Categories category) {
      ArrayList<Module> list = new ArrayList();
      Iterator var2 = modulesClassMap.values().iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.category == category) {
            list.add(module);
         }
      }

      return list;
   }

   public static <T extends Module> T getModule(Class<T> clazz) {
      return clazz.cast(modulesClassMap.get(clazz));
   }

   public static Module getModule(String name) {
      return name == null ? null : modulesNameMap.get(name.toLowerCase(Locale.ROOT));
   }

   public static boolean isModuleEnabled(Class<? extends Module> clazz) {
      Module module = getModule(clazz);
      return module != null && module.isEnable();
   }

   public static boolean isModuleEnabled(String name) {
      Module module = getModule(name);
      return module != null && module.isEnable();
   }
}

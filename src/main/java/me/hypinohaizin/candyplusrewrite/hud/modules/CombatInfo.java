package me.hypinohaizin.candyplusrewrite.hud.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.CandyPlusRewrite;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.module.combat.AutoMend;
import me.hypinohaizin.candyplusrewrite.module.combat.Blocker;
import me.hypinohaizin.candyplusrewrite.module.combat.CevBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.CivBreaker;
import me.hypinohaizin.candyplusrewrite.module.combat.HoleFill;
import me.hypinohaizin.candyplusrewrite.module.combat.PistonAura;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;

public class CombatInfo extends Hud {
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", true));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Boolean> mend = this.register(new Setting("AutoMend", true));
   public Setting<Boolean> blocker = this.register(new Setting("Blocker", true));
   public Setting<Boolean> cev = this.register(new Setting("CevBreaker", true));
   public Setting<Boolean> civ = this.register(new Setting("CivBreaker", true));
   public Setting<Boolean> holefill = this.register(new Setting("HoleFill", true));
   public Setting<Boolean> pa = this.register(new Setting("PistonAura", true));

   public CombatInfo() {
      super("CombatInfo", 50.0F, 10.0F);
   }

   public void onRender() {
      try {
         Module mend = this.getModule(AutoMend.class);
         Module blocker = this.getModule(Blocker.class);
         Module cev = this.getModule(CevBreaker.class);
         Module civ = this.getModule(CivBreaker.class);
         Module holefill = this.getModule(HoleFill.class);
         Module pa = this.getModule(PistonAura.class);
         float width = 0.0F;
         float height = 0.0F;
         float _width;
         if ((Boolean)this.mend.getValue()) {
            _width = this.drawModuleInfo(mend, height);
            if (width < _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         if ((Boolean)this.blocker.getValue()) {
            _width = this.drawModuleInfo(blocker, height);
            if (width < _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         if ((Boolean)this.cev.getValue()) {
            _width = this.drawModuleInfo(cev, height);
            if (width < _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         if ((Boolean)this.civ.getValue()) {
            _width = this.drawModuleInfo(civ, height);
            if (width < _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         if ((Boolean)this.holefill.getValue()) {
            _width = this.drawModuleInfo(holefill, height);
            if (width < _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         if ((Boolean)this.pa.getValue()) {
            _width = RenderUtil.drawString(pa.name + " : " + (pa.isEnable ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"), (Float)this.x.getValue(), (Float)this.y.getValue() + height, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), 1.0F);
            if (width > _width) {
               width = _width;
            }

            height += RenderUtil.getStringHeight(1.0F) + 5.0F;
         }

         this.width = width - (Float)this.x.getValue();
         this.height = height;
      } catch (Exception var10) {
      }

   }

   public float drawModuleInfo(Module module, float offset) {
      return RenderUtil.drawString(module.name + " : " + (module.isEnable ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"), (Float)this.x.getValue(), (Float)this.y.getValue() + offset, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), 1.0F);
   }

   public Module getModule(Class clazz) {
      return CandyPlusRewrite.m_module.getModuleWithClass(clazz);
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.PlayerUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TargetHUD extends Hud {
   public static EntityPlayer target;
   public double health = 36.0D;
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", true));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Float> size = this.register(new Setting("Size", 1.0F, 2.0F, 0.5F));
   private final float baseWidth = 200.0F;
   private final float baseHeight = 70.0F;

   public TargetHUD() {
      super("TargetHud", 100.0F, 50.0F);
   }

   public void onRender() {
      try {
         if (this.nullCheck()) {
            return;
         }

         float scale = (Float)this.size.getValue();
         float width = 200.0F * scale;
         float height = 70.0F * scale;
         this.width = width;
         this.height = height;
         target = PlayerUtil.getNearestPlayer(30.0D);
         if (target == null) {
            return;
         }

         float startX = (Float)this.x.getValue();
         float startY = (Float)this.y.getValue();
         RenderUtil.drawRect(startX, startY, width, height, ColorUtil.toRGBA(40, 40, 40));
         RenderUtil.renderEntity(target, startX + 30.0F * scale, startY + height - 7.0F * scale, 30.0F * scale);
         float targetHealth = target.getHealth() + target.getAbsorptionAmount();
         this.health += ((double)targetHealth - this.health) * 0.4D;
         double lineWidth = (double)(width * (targetHealth / 36.0F));
         RenderUtil.drawGradientRect(startX, startY + height - scale, startX + (float)lineWidth, startY + height, ColorUtil.toRGBA(255, 0, 0), ColorUtil.toRGBA(getHealthColor((int)targetHealth)));
         int white = ColorUtil.toRGBA(255, 255, 255);
         float fontSpacing = 12.0F * scale;
         RenderUtil.drawString(target.getName(), startX + 60.0F * scale, startY + 10.0F * scale, white, (Boolean)this.shadow.getValue(), scale);
         float itemY = startY + 20.0F * scale;
         this.renderItem(this.getArmorInv(3), (int)(60.0F * scale), (int)itemY);
         this.renderItem(this.getArmorInv(2), (int)(80.0F * scale), (int)itemY);
         this.renderItem(this.getArmorInv(1), (int)(100.0F * scale), (int)itemY);
         this.renderItem(this.getArmorInv(0), (int)(120.0F * scale), (int)itemY);
         this.renderItem(target.getHeldItemMainhand(), (int)(140.0F * scale), (int)itemY);
         this.renderItem(target.getHeldItemOffhand(), (int)(160.0F * scale), (int)itemY);
         RenderUtil.drawString("Health : " + (int)targetHealth, startX + 60.0F * scale, startY + 42.0F * scale, white, (Boolean)this.shadow.getValue(), scale);
         RenderUtil.drawString("Distance : " + (int)PlayerUtil.getDistance((Entity)target), startX + 60.0F * scale, startY + 42.0F * scale + fontSpacing, white, (Boolean)this.shadow.getValue(), scale);
      } catch (Exception var12) {
      }

   }

   public void renderItem(ItemStack item, int offsetX, int offsetY) {
      if (item != null && !item.isEmpty()) {
         RenderUtil.renderItem(item, (Float)this.x.getValue() + (float)offsetX, (Float)this.y.getValue() + (float)offsetY - 4.0F);
      }
   }

   public ItemStack getArmorInv(int slot) {
      InventoryPlayer inv = target.inventory;
      return inv.armorItemInSlot(slot);
   }

   public float getItemDmg(ItemStack is) {
      return (float)(is.getMaxDamage() - is.getItemDamage()) / (float)is.getMaxDamage() * 100.0F;
   }

   public int getItemDmgColor(ItemStack is) {
      float maxDmg = (float)is.getMaxDamage();
      float dmg = maxDmg - (float)is.getItemDamage();
      double offset = (double)(255.0F / (maxDmg / 2.0F));
      int red;
      int green;
      if (dmg > maxDmg / 2.0F) {
         red = (int)((double)(maxDmg - dmg) * offset);
         green = 255;
      } else {
         red = 255;
         green = (int)(255.0D - ((double)maxDmg / 2.0D - (double)dmg) * offset);
      }

      return ColorUtil.toRGBA(red, green, 0, 255);
   }

   private static Color getHealthColor(int health) {
      health = Math.max(0, Math.min(health, 36));
      int red;
      int green;
      if (health > 18) {
         red = (int)((double)(36 - health) * 14.1666667D);
         green = 255;
      } else {
         red = 255;
         green = (int)(255.0D - (double)(18 - health) * 14.1666667D);
      }

      return new Color(red, green, 0, 255);
   }
}

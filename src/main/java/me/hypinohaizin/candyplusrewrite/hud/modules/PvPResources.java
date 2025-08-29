//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.hud.modules;

import java.awt.Color;
import me.hypinohaizin.candyplusrewrite.hud.Hud;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ColorUtil;
import me.hypinohaizin.candyplusrewrite.utils.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PvPResources extends Hud {
   public Setting<Boolean> crystal = this.register(new Setting("Crystal", true));
   public Setting<Boolean> xp = this.register(new Setting("Xp", true));
   public Setting<Boolean> gap = this.register(new Setting("Gap", true));
   public Setting<Boolean> totem = this.register(new Setting("Totem", true));
   public Setting<Boolean> obby = this.register(new Setting("Obsidian", true));
   public Setting<Boolean> piston = this.register(new Setting("Piston", true));
   public Setting<Boolean> redstone = this.register(new Setting("RedStone", true));
   public Setting<Boolean> torch = this.register(new Setting("Torch", true, (v) -> {
      return (Boolean)this.redstone.getValue();
   }));
   public Setting<Boolean> block = this.register(new Setting("Block", true, (v) -> {
      return (Boolean)this.redstone.getValue();
   }));
   public Setting<Boolean> shadow = this.register(new Setting("Shadow", false));
   public Setting<Color> color = this.register(new Setting("Color", new Color(255, 255, 255, 255)));
   public Setting<Boolean> background = this.register(new Setting("Background", false));
   public Setting<Color> backcolor = this.register(new Setting("BGColor", new Color(40, 40, 40, 60), (v) -> {
      return (Boolean)this.background.getValue();
   }));

   public PvPResources() {
      super("PvPResources", 300.0F, 100.0F);
   }

   public void onRender() {
      float x = (Float)this.x.getValue();
      float y = (Float)this.y.getValue();
      if ((Boolean)this.crystal.getValue()) {
         this.renderItem(Items.END_CRYSTAL, this.getItemCount(Items.END_CRYSTAL), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.xp.getValue()) {
         this.renderItem(Items.EXPERIENCE_BOTTLE, this.getItemCount(Items.EXPERIENCE_BOTTLE), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.gap.getValue()) {
         this.renderItem(Items.GOLDEN_APPLE, this.getItemCount(Items.GOLDEN_APPLE), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.totem.getValue()) {
         this.renderItem(Items.TOTEM_OF_UNDYING, this.getItemCount(Items.TOTEM_OF_UNDYING), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.obby.getValue()) {
         this.renderBlock(Blocks.OBSIDIAN, this.getBlockCount(Blocks.OBSIDIAN), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.piston.getValue()) {
         this.renderBlock(Blocks.PISTON, this.getBlockCount(Blocks.PISTON) + this.getBlockCount(Blocks.STICKY_PISTON), x, y);
         y += RenderUtil.getStringHeight(1.0F) + 13.0F;
      }

      if ((Boolean)this.redstone.getValue()) {
         if ((Boolean)this.block.getValue()) {
            this.renderBlock(Blocks.REDSTONE_BLOCK, this.getBlockCount(Blocks.REDSTONE_BLOCK), x, y);
            y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }

         if ((Boolean)this.torch.getValue()) {
            this.renderBlock(Blocks.REDSTONE_TORCH, this.getBlockCount(Blocks.REDSTONE_TORCH), x, y);
            y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
      }

      y -= RenderUtil.getStringHeight(1.0F) + 13.0F;
      this.width = x + 20.0F + RenderUtil.getStringWidth(" : 64", 1.0F) - (Float)this.x.getValue();
      this.height = y - (Float)this.y.getValue();
   }

   public void renderItem(Item item, int count, float x, float y) {
      RenderUtil.renderItem(new ItemStack(item), x, y - 8.0F, false);
      RenderUtil.drawString(" : " + count, x + 20.0F, y, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), 1.0F);
   }

   public void renderBlock(Block block, int count, float x, float y) {
      RenderUtil.renderItem(new ItemStack(block), x, y - 10.0F, false);
      RenderUtil.drawString(" : " + count, x + 20.0F, y, ColorUtil.toRGBA((Color)this.color.getValue()), (Boolean)this.shadow.getValue(), 1.0F);
   }

   public int getItemCount(Item item) {
      int count = 0;

      for(int i = 0; i < mc.player.inventory.getSizeInventory(); ++i) {
         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
         if (itemStack.getItem() == item) {
            count += itemStack.getCount();
         }
      }

      return count;
   }

   public int getBlockCount(Block block) {
      int count = 0;

      for(int i = 0; i < mc.player.inventory.getSizeInventory(); ++i) {
         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
         if (itemStack.getItem() instanceof ItemBlock && ((ItemBlock)itemStack.getItem()).getBlock() == block) {
            count += itemStack.getCount();
         }
      }

      return count;
   }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.ChatUtil;
import me.hypinohaizin.candyplusrewrite.utils.StringUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class BaseFinder extends Module {
   private final Setting<Boolean> chest = this.register(new Setting("Chest", true));
   private final Setting<Boolean> hopper = this.register(new Setting("Hopper", true));
   private final Setting<Boolean> furnace = this.register(new Setting("Furnace", true));
   private final Setting<Boolean> shulker = this.register(new Setting("Shulker", true));
   private List<TileEntity> notified = new ArrayList();

   public BaseFinder() {
      super("BaseFinder", Module.Categories.MISC, false, false);
   }

   public void onDisable() {
      this.notified = new ArrayList();
   }

   public void onUpdate() {
      if (!this.nullCheck()) {
         List<TileEntity> tiles = new ArrayList(mc.world.loadedTileEntityList);
         tiles.removeIf((tex) -> {
            return (!(tex instanceof TileEntityChest) || !(Boolean)this.chest.getValue()) && (!(tex instanceof TileEntityHopper) || !(Boolean)this.hopper.getValue()) && (!(tex instanceof TileEntityFurnace) || !(Boolean)this.furnace.getValue()) && (!(tex instanceof TileEntityShulkerBox) || !(Boolean)this.shulker.getValue());
         });
         Iterator var2 = tiles.iterator();

         while(var2.hasNext()) {
            TileEntity te = (TileEntity)var2.next();
            if (!this.notified.contains(te)) {
               this.notified.add(te);
               String name;
               if (te instanceof TileEntityChest) {
                  name = "Chest";
               } else if (te instanceof TileEntityHopper) {
                  name = "Hopper";
               } else if (te instanceof TileEntityFurnace) {
                  name = "Furnace";
               } else {
                  name = "ShulkerBox";
               }

               this.sendMessage(name + ": " + StringUtil.getPositionString(te.getPos()));
               ChatUtil.sendMessage(name + ": " + StringUtil.getPositionString(te.getPos()));
            }
         }

      }
   }
}

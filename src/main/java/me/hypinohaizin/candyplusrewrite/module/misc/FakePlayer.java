//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;

public class FakePlayer extends Module {
   public Setting<Boolean> copyInv = this.register(new Setting("CopyInv", true));
   private EntityOtherPlayerMP _fakePlayer;

   public FakePlayer() {
      super("FakePlayer", Module.Categories.MISC, false, false);
   }

   public void onEnable() {
      if (mc.player == null) {
         this.disable();
      } else {
         this._fakePlayer = null;
         if (mc.player != null) {
            WorldClient world = mc.world;
            UUID fromString = UUID.fromString("53f654cb-e42a-4a67-8072-4bb70ed76230");
            this.getClass();
            (this._fakePlayer = new EntityOtherPlayerMP(world, new GameProfile(fromString, "8x0f"))).copyLocationAndAnglesFrom(mc.player);
            this._fakePlayer.rotationYawHead = mc.player.rotationYawHead;
            if ((Boolean)this.copyInv.getValue()) {
               this._fakePlayer.inventory.copyInventory(mc.player.inventory);
            }

            mc.world.addEntityToWorld(-100, this._fakePlayer);
         }

      }
   }

   public void onDisable() {
      if (mc.world != null && mc.player != null) {
         super.onDisable();

         try {
            mc.world.removeEntity(this._fakePlayer);
         } catch (Exception var2) {
         }
      }

   }
}

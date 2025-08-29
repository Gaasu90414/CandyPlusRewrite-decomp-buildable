//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.movement;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowdown extends Module {
   public final Setting<Float> webHorizontalFactor = this.register(new Setting("WebHSpeed", 2.0F, 50.0F, 0.0F));
   public final Setting<Float> webVerticalFactor = this.register(new Setting("WebVSpeed", 2.0F, 50.0F, 0.0F));
   public Setting<Boolean> noSlow = this.register(new Setting("NoSlow", true));
   public Setting<Boolean> strict = this.register(new Setting("Strict", false));
   public Setting<Boolean> sneakPacket = this.register(new Setting("SneakPacket", false));
   public Setting<Boolean> webs = this.register(new Setting("Webs", false));
   private boolean sneaking = false;

   public NoSlowdown() {
      super("NoSlowdown", Module.Categories.MOVEMENT, false, false);
   }

   public void onUpdate() {
      Item item = mc.player.getActiveItemStack().getItem();
      if (this.sneaking && !mc.player.isHandActive() && (Boolean)this.sneakPacket.getValue()) {
         mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
         this.sneaking = false;
      }

   }

   @SubscribeEvent
   public void onUseItem(RightClickItem event) {
      Item item = mc.player.getHeldItem(event.getHand()).getItem();
      if ((item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion && (Boolean)this.sneakPacket.getValue()) && !this.sneaking) {
         mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
         this.sneaking = true;
      }

   }

   @SubscribeEvent
   public void onPacket(PacketEvent.Send event) {
      if (event.getPacket() instanceof CPacketPlayer && (Boolean)this.strict.getValue() && (Boolean)this.noSlow.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
         mc.player.connection.sendPacket(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), EnumFacing.DOWN));
      }

   }
}

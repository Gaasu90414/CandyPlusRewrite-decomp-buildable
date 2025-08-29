//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.event.events.network.PacketEvent;
import me.hypinohaizin.candyplusrewrite.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleBreakAlert extends Module {
   public HoleBreakAlert() {
      super("HoleBreakAlert", Module.Categories.MISC, false, false);
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof SPacketBlockBreakAnim) {
         SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim)event.getPacket();
         if (this.isHoleBlock(packet.getPosition())) {
            int breakerId = packet.getBreakerId();
            String breakerName = "Unknown";
            if (mc.world.getEntityByID(breakerId) != null) {
               breakerName = mc.world.getEntityByID(breakerId).getName();
            }

            this.sendMessage("The hole block to your " + this.getBlockDirectionFromPlayer(packet.getPosition()) + " is being broken by " + breakerName);
         }
      }

   }

   private String getBlockDirectionFromPlayer(BlockPos pos) {
      double posX = Math.floor(mc.player.posX);
      double posZ = Math.floor(mc.player.posZ);
      double x = posX - (double)pos.getX();
      double z = posZ - (double)pos.getZ();
      switch(mc.player.getHorizontalFacing()) {
      case SOUTH:
         if (x == 1.0D) {
            return "right";
         }

         if (x == -1.0D) {
            return "left";
         }

         if (z == 1.0D) {
            return "back";
         }

         if (z == -1.0D) {
            return "front";
         }
         break;
      case WEST:
         if (x == 1.0D) {
            return "front";
         }

         if (x == -1.0D) {
            return "back";
         }

         if (z == 1.0D) {
            return "right";
         }

         if (z == -1.0D) {
            return "left";
         }
         break;
      case NORTH:
         if (x == 1.0D) {
            return "left";
         }

         if (x == -1.0D) {
            return "right";
         }

         if (z == 1.0D) {
            return "front";
         }

         if (z == -1.0D) {
            return "back";
         }
         break;
      case EAST:
         if (x == 1.0D) {
            return "back";
         }

         if (x == -1.0D) {
            return "front";
         }

         if (z == 1.0D) {
            return "left";
         }

         if (z == -1.0D) {
            return "right";
         }
         break;
      default:
         return "undetermined";
      }

      return "undetermined";
   }

   private boolean isHoleBlock(BlockPos pos) {
      double posX = Math.floor(mc.player.posX);
      double posZ = Math.floor(mc.player.posZ);
      Block block = mc.world.getBlockState(pos).getBlock();
      if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) {
         if ((double)pos.getX() == posX + 1.0D && pos.getY() == mc.player.getPosition().getY()) {
            return true;
         }

         if ((double)pos.getX() == posX - 1.0D && pos.getY() == mc.player.getPosition().getY()) {
            return true;
         }

         if ((double)pos.getZ() == posZ + 1.0D && pos.getY() == mc.player.getPosition().getY()) {
            return true;
         }

         if ((double)pos.getZ() == posZ - 1.0D && pos.getY() == mc.player.getPosition().getY()) {
            return true;
         }
      }

      return false;
   }
}

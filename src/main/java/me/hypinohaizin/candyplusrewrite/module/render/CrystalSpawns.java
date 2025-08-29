//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

public class CrystalSpawns extends Module {
   private final Setting<Boolean> rainbow = this.register(new Setting("Rainbow", false));
   private final Setting<Integer> red = this.register(new Setting("Red", 0, 255, 0));
   private final Setting<Integer> green = this.register(new Setting("Green", 0, 255, 0));
   private final Setting<Integer> blue = this.register(new Setting("Blue", 0, 255, 0));
   private static CrystalSpawns INSTANCE;
   public static HashMap<UUID, CrystalSpawns.Thingering> thingers = new HashMap();

   public CrystalSpawns() {
      super("CrystalSpawns", Module.Categories.RENDER, false, false);
      this.setInstance();
   }

   public static CrystalSpawns getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new CrystalSpawns();
      }

      return INSTANCE;
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public void onTick() {
      if (mc.world != null) {
         Iterator var1 = mc.world.loadedEntityList.iterator();

         while(var1.hasNext()) {
            Entity entity = (Entity)var1.next();
            if (entity instanceof EntityEnderCrystal && !thingers.containsKey(entity.getUniqueID())) {
               thingers.put(entity.getUniqueID(), new CrystalSpawns.Thingering(entity));
               ((CrystalSpawns.Thingering)thingers.get(entity.getUniqueID())).starTime = System.currentTimeMillis();
            }
         }

      }
   }

   public void onRender3D() {
      if (mc.player != null && mc.world != null) {
         Iterator var1 = thingers.entrySet().iterator();

         while(var1.hasNext()) {
            Entry<UUID, CrystalSpawns.Thingering> entry = (Entry)var1.next();
            long time = System.currentTimeMillis();
            long duration = time - ((CrystalSpawns.Thingering)entry.getValue()).starTime;
            if (duration < 1500L) {
               float cycle = (float)(duration % 1500L) / 1500.0F;
               float moveY = (float)Math.sin(3.141592653589793D * (double)cycle) * 1.5F;
               float scale = 0.6F + 0.2F * (float)Math.sin(3.141592653589793D * (double)cycle);
               float opacity = 1.0F - (float)duration / 1500.0F;
               this.drawCircle(((CrystalSpawns.Thingering)entry.getValue()).entity, mc.getRenderPartialTicks(), (double)scale, moveY, opacity);
            }
         }

      }
   }

   public void drawCircle(Entity entity, float partialTicks, double rad, float moveY, float alpha) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      startSmooth();
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(2.5F);
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
      GL11.glBegin(3);

      for(int i = 0; i <= 90; ++i) {
         Color color = (Boolean)this.rainbow.getValue() ? this.rainbowColor(i * 4) : new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue());
         GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha);
         double theta = (double)(i * 2) * 3.141592653589793D / 90.0D;
         GL11.glVertex3d(x + rad * Math.cos(theta), y + (double)moveY, z + rad * Math.sin(theta));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      endSmooth();
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   private Color rainbowColor(int delay) {
      float hue = (float)(System.currentTimeMillis() % 11520L + (long)delay) / 11520.0F;
      return Color.getHSBColor(hue, 0.8F, 1.0F);
   }

   public static void startSmooth() {
      GL11.glEnable(2848);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glHint(3154, 4354);
   }

   public static void endSmooth() {
      GL11.glDisable(2848);
      GL11.glDisable(3042);
   }

   public static class Thingering {
      public final Entity entity;
      public long starTime;

      public Thingering(Entity entity) {
         this.entity = entity;
         this.starTime = 0L;
      }
   }
}

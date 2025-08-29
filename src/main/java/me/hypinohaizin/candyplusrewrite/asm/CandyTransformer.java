package me.hypinohaizin.candyplusrewrite.asm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.hypinohaizin.candyplusrewrite.asm.api.ClassPatch;
import me.hypinohaizin.candyplusrewrite.asm.impl.PatchEntityRenderer;
import net.minecraft.launchwrapper.IClassTransformer;

public class CandyTransformer implements IClassTransformer {
   public static final List<ClassPatch> patches;

   public byte[] transform(String name, String transformedName, byte[] bytes) {
      if (bytes == null) {
         return null;
      } else {
         Iterator var4 = patches.iterator();

         ClassPatch it;
         do {
            if (!var4.hasNext()) {
               return bytes;
            }

            it = (ClassPatch)var4.next();
         } while(!it.className.equals(transformedName));

         return it.transform(bytes);
      }
   }

   static {
      (patches = new ArrayList()).add(new PatchEntityRenderer());
   }
}

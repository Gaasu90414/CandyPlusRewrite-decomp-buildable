//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.asm.impl;

import java.util.Iterator;
import me.hypinohaizin.candyplusrewrite.asm.api.ClassPatch;
import me.hypinohaizin.candyplusrewrite.asm.api.MappingName;
import me.hypinohaizin.candyplusrewrite.utils.ASMUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class PatchEntityRenderer extends ClassPatch {
   public PatchEntityRenderer() {
      super("net.minecraft.client.renderer.EntityRenderer", "buq");
   }

   public byte[] transform(byte[] bytes) {
      ClassNode classNode = new ClassNode();
      ClassReader classReader = new ClassReader(bytes);
      classReader.accept(classNode, 0);
      MappingName method = new MappingName("updateCameraAndRender", "a", "updateCameraAndRender");
      String desc = "(FJ)V";
      Iterator var6 = classNode.methods.iterator();

      while(var6.hasNext()) {
         MethodNode it = (MethodNode)var6.next();
         if (method.equalName(it.name) && it.desc.equals("(FJ)V")) {
            this.patchUpdateCameraAndRender(it);
         }
      }

      ClassWriter writer = new ClassWriter(0);
      classNode.accept(writer);
      return writer.toByteArray();
   }

   public void patchUpdateCameraAndRender(MethodNode methodNode) {
      AbstractInsnNode target = ASMUtil.findMethodInsn(methodNode, 182, "biq", "a", "(F)V");
      if (target != null) {
         InsnList insnList = new InsnList();
         insnList.add(new VarInsnNode(23, 1));
         insnList.add(new MethodInsnNode(184, Type.getInternalName(this.getClass()), "updateCameraAndRenderHook", "(F)V", false));
         methodNode.instructions.insert(target, insnList);
      }

   }

   public static void updateCameraAndRenderHook(float partialTicks) {
   }
}

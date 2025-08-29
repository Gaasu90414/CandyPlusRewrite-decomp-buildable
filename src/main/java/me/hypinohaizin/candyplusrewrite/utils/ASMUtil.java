package me.hypinohaizin.candyplusrewrite.utils;

import java.util.Iterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtil {
   public static AbstractInsnNode findMethodInsn(MethodNode mn, int opcode, String owner, String name, String desc) {
      AbstractInsnNode[] var5 = mn.instructions.toArray();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         AbstractInsnNode insn = var5[var7];
         if (insn instanceof MethodInsnNode) {
            MethodInsnNode method = (MethodInsnNode)insn;
            if (method.getOpcode() == opcode && method.owner.equals(owner) && method.name.equals(name) && method.desc.equals(desc)) {
               return insn;
            }
         }
      }

      return null;
   }

   public static byte[] toBytes(ClassNode classNode) {
      ClassWriter writer = new ClassWriter(3);
      classNode.accept(writer);
      return writer.toByteArray();
   }

   public static MethodNode findMethod(ClassNode classNode, String name, String desc) {
      Iterator var3 = classNode.methods.iterator();

      MethodNode methodNode;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         methodNode = (MethodNode)var3.next();
      } while(!methodNode.name.equals(name) || !methodNode.desc.equals(desc));

      return methodNode;
   }

   public static ClassNode getNode(byte[] classBuffer) {
      ClassNode classNode = new ClassNode();
      ClassReader reader = new ClassReader(classBuffer);
      reader.accept(classNode, 0);
      return classNode;
   }
}

package me.hypinohaizin.candyplusrewrite.module.render;

import me.hypinohaizin.candyplusrewrite.module.Module;

public class HUDEditor extends Module {
   public static HUDEditor instance = null;

   public HUDEditor() {
      super("HUDEditor", Module.Categories.RENDER, false, false);
      instance = this;
   }
}

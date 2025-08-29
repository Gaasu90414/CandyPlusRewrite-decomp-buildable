//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\gaasu\Downloads\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

package me.hypinohaizin.candyplusrewrite.module.misc;

import me.hypinohaizin.candyplusrewrite.module.Module;
import me.hypinohaizin.candyplusrewrite.setting.Setting;
import me.hypinohaizin.candyplusrewrite.utils.MathUtil;
import me.hypinohaizin.candyplusrewrite.utils.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Spammer extends Module {
   public Setting<Float> delay = this.register(new Setting("Delay", 50.0F, 100.0F, 1.0F));
   public Setting<Spammer.type> spam;
   public Setting<Boolean> suffix;
   public Timer timer;
   public final String chinese = "è¿™æ˜¯ä¸?é¦–çˆ±æƒ?ä¹‹æ­Œå—?¼? æˆ–è??æ˜¯ä¸?é¦–å?³äºæ¢¦æƒ³çš?æ­Œï¼Ÿæ— è®ºæ€æ?·?¼Œä»–ä»¬éƒ½å¾ˆéš¾è¿½ä¸Šã?? è€Œæ?‘ä»¬åˆ™ç»§ç»­å¾˜å¾Šã?? æ¢¦æƒ³æ€»æ˜¯åœ¨åé¢ã€? åªæœ‰å½“æ?‘ä»¬è¿½ä¸Šä»–ä»¬æ—¶?¼Œæ?‘ä»¬æ‰è?½ä»æ­£é¢çœ‹åˆ°ä»–ä»¬ã€? åªæœ‰åˆ°é‚£æ—¶?¼Œæ?‘ä»¬æ‰æ„è¯?åˆ°è¿™å¼?è„¸æ˜¯æˆ‘ä»¬è‡ªå·±çš?ã€? \"çº¯æ´å°±åƒç«ç‘°?¼Œç¾ä¸½è€Œåˆºçœ¼ã€? å‡?åŒ–ä½?çš?æ¢¦æƒ³ ............. ä½?çš?è¿½æ•è¡ŒåŠ¨å°?æŒç»­å¤šä¹??¼?";
   public final String korean = "ú®? ?¿¤ ?¸° ?£¼ ?¿? ?‘?¥´ ?³? ?¹? ?©ˆì¹« ?¶??ƒ ú¹‘ì¸ ú«° ?¸ ?¥´ ?‘?š© ?„¸ ?š°?³? ú¦¸ ?£Œì¼? ?°??Š¤ ?‹¤?´?£¨ ?§?˜ ??? ?§Œë° ?˜¤??? ú¬?£¨ ?³? ?°??Š¤ ??ˆì¿? ?ª¨?Š¤ ?¬¸ ??¡ ?Š¹?¸° ?¡°?•„?„¸ã€‚êµ­?„ ?¶”ì¦? ?°”ë‹¤?—??? ?°??‹œ ?´‘ì„ ?´ ?›ƒ?Œ. ?¹?Š¤?“¬úº? ?’¤?ª½?˜ ?—´?³‘ì€ ú£´?³??¥¼ ?±°?“­ú±œë‹¤.\n";
   public final String[] amongus;
   public int index;

   public Spammer() {
      super("Spammer", Module.Categories.MISC, false, false);
      this.spam = this.register(new Setting("Type", Spammer.type.chinese));
      this.suffix = this.register(new Setting("Suffix", true));
      this.timer = new Timer();
      this.amongus = new String[]{"â ?â ?â ?â ?â ?â ?â ?â£?â£?â£â¡?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â ?â¢?â ?â£?â£¶â£¿â£¿â£¿â ¿â ¿â£›â£‚â£?â£?â¡’â?¶â£¶â£¤â£¤â£¬â£?â¡?â ?â¢?â ?â ?â ?â ?â ?â ?â ?â ?â ?â¢?â£¾â£¿â£¿â£¿â¡Ÿâ¢¡â¢¾â£¿â£¿â£¿â£¿â£¿â£¿â£¶â£Œâ?»â£¿â£¿â£¿â£¿â£·â£¦â£?â¡?â ?â ?â ?â ?â ?â ?â ?â£ˆâ£‰â¡›â£¿â£¿â£¿â¡Œâ¢?â¢»â£¿â£¿â£¿â£¿â£¿â ¿â ›â£¡â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¦â£?â ?â ?â ?â ?â ºâ Ÿâ£‰â£´â¡¿â ›â£©â£¾â£â?³â ¿â ›â£‹â£©â£´â£¾â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£?â ?â ?â ?â ?â ?â ˜â¢‹â£´â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â¡?â ?â ?â ?â¢?â¢?â£¾â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â¡?â ?â ?â ?â ?â£¾â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â ?â£?â ?â ?â ?â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â¡¿â ?â ˜â?›â??â ?â ?â¢»â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â Ÿâ?‹â£?â£?â£?â£¤â ?â ?â£?â£?â¡™â?»â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â£¿â ¿â¢›â£©â ¤â ¾â ?â ›â?‹â?‰â¢‰â??â ºâ ¿â ›â?›â??â ?â ‰â?™â?›â?›â?»â ¿â ¿â ¿â Ÿâ?›â?›â?›â?‰â?â??â ?â£?â£?â£?â£¤â£?â£´â£¶â£¼â£¿"};
      this.index = 0;
   }

   public void onEnable() {
      this.index = 0;
   }

   public void onUpdate() {
      if (this.timer == null) {
         this.timer = new Timer();
      }

      if (this.timer.passedDms((double)(Float)this.delay.getValue())) {
         this.timer.reset();
         String msg = "";
         if (this.spam.getValue() == Spammer.type.chinese) {
            msg = msg + "è¿™æ˜¯ä¸?é¦–çˆ±æƒ?ä¹‹æ­Œå—?¼? æˆ–è??æ˜¯ä¸?é¦–å?³äºæ¢¦æƒ³çš?æ­Œï¼Ÿæ— è®ºæ€æ?·?¼Œä»–ä»¬éƒ½å¾ˆéš¾è¿½ä¸Šã?? è€Œæ?‘ä»¬åˆ™ç»§ç»­å¾˜å¾Šã?? æ¢¦æƒ³æ€»æ˜¯åœ¨åé¢ã€? åªæœ‰å½“æ?‘ä»¬è¿½ä¸Šä»–ä»¬æ—¶?¼Œæ?‘ä»¬æ‰è?½ä»æ­£é¢çœ‹åˆ°ä»–ä»¬ã€? åªæœ‰åˆ°é‚£æ—¶?¼Œæ?‘ä»¬æ‰æ„è¯?åˆ°è¿™å¼?è„¸æ˜¯æˆ‘ä»¬è‡ªå·±çš?ã€? \"çº¯æ´å°±åƒç«ç‘°?¼Œç¾ä¸½è€Œåˆºçœ¼ã€? å‡?åŒ–ä½?çš?æ¢¦æƒ³ ............. ä½?çš?è¿½æ•è¡ŒåŠ¨å°?æŒç»­å¤šä¹??¼?";
         }

         if (this.spam.getValue() == Spammer.type.korean) {
            msg = msg + "ú®? ?¿¤ ?¸° ?£¼ ?¿? ?‘?¥´ ?³? ?¹? ?©ˆì¹« ?¶??ƒ ú¹‘ì¸ ú«° ?¸ ?¥´ ?‘?š© ?„¸ ?š°?³? ú¦¸ ?£Œì¼? ?°??Š¤ ?‹¤?´?£¨ ?§?˜ ??? ?§Œë° ?˜¤??? ú¬?£¨ ?³? ?°??Š¤ ??ˆì¿? ?ª¨?Š¤ ?¬¸ ??¡ ?Š¹?¸° ?¡°?•„?„¸ã€‚êµ­?„ ?¶”ì¦? ?°”ë‹¤?—??? ?°??‹œ ?´‘ì„ ?´ ?›ƒ?Œ. ?¹?Š¤?“¬úº? ?’¤?ª½?˜ ?—´?³‘ì€ ú£´?³??¥¼ ?±°?“­ú±œë‹¤.\n";
         }

         if (this.spam.getValue() == Spammer.type.amongus) {
            msg = msg + this.amongus[this.index];
            ++this.index;
            if (this.amongus.length <= this.index) {
               this.index = 0;
            }
         }

         if ((Boolean)this.suffix.getValue()) {
            msg = msg + MathUtil.getRandom(1000, 10000);
         }

         mc.player.connection.sendPacket(new CPacketChatMessage(msg));
      }

   }

   public static enum type {
      chinese,
      korean,
      amongus;
   }
}

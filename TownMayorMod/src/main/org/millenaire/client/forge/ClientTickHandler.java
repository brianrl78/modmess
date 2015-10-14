/*    */ package org.millenaire.client.forge;
/*    */ 
/*    */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*    */ import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.forge.CommonProxy;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ 
/*    */ public class ClientTickHandler
/*    */ {
/*    */   private boolean startupMessageShow;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void tickStart(TickEvent.ClientTickEvent event)
/*    */   {
/* 17 */     if ((Mill.clientWorld == null) || (!Mill.clientWorld.millenaireEnabled) || (Minecraft.func_71410_x().field_71439_g == null)) {
/* 18 */       return;
/*    */     }
/*    */     
/* 21 */     boolean onSurface = Minecraft.func_71410_x().field_71439_g.field_71093_bK == 0;
/*    */     
/* 23 */     Mill.clientWorld.updateWorldClient(onSurface);
/*    */     
/* 25 */     if (!this.startupMessageShow) {
/* 26 */       org.millenaire.client.gui.DisplayActions.displayStartupOrError(Minecraft.func_71410_x().field_71439_g, Mill.startupError);
/* 27 */       this.startupMessageShow = true;
/*    */     }
/*    */     
/* 30 */     Mill.proxy.handleClientGameUpdate();
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\client\forge\ClientTickHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
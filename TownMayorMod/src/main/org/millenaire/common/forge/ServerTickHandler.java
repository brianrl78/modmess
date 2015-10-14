/*    */ package org.millenaire.common.forge;
/*    */ 
/*    */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*    */ import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
/*    */ import org.millenaire.common.MillWorld;
/*    */ 
/*    */ 
/*    */ public class ServerTickHandler
/*    */ {
/*    */   @SubscribeEvent
/*    */   public void tickStart(TickEvent.ServerTickEvent event)
/*    */   {
/* 13 */     if (Mill.startupError) {
/* 14 */       return;
/*    */     }
/*    */     
/* 17 */     for (MillWorld mw : Mill.serverWorlds) {
/* 18 */       mw.updateWorldServer();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\forge\ServerTickHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
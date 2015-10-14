/*    */ package org.millenaire.common.network;
/*    */ 
/*    */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*    */ import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
/*    */ import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/*    */ import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MillWorld;
/*    */ import org.millenaire.common.UserProfile;
/*    */ import org.millenaire.common.forge.Mill;
/*    */ 
/*    */ public class ConnectionEventHandler
/*    */ {
/*    */   @SubscribeEvent
/*    */   public void clientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event)
/*    */   {
/* 18 */     Mill.proxy.handleClientLogin();
/*    */   }
/*    */   
/*    */ 
/*    */   @SubscribeEvent
/*    */   public void connectionClosed(FMLNetworkEvent.ServerDisconnectionFromClientEvent event)
/*    */   {
/* 25 */     for (MillWorld mw : Mill.serverWorlds) {
/* 26 */       mw.checkConnections();
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
/*    */   {
/*    */     try {
/* 34 */       UserProfile profile = org.millenaire.common.core.MillCommonUtilities.getServerProfile(event.player.field_70170_p, event.player.getDisplayName());
/* 35 */       if (profile != null) {
/* 36 */         profile.connectUser();
/*    */       } else {
/* 38 */         MLN.error(this, "Could not get profile on login for user: " + event.player.getDisplayName());
/*    */       }
/*    */     } catch (Exception e) {
/* 41 */       MLN.printException("Error in ConnectionHandler.playerLoggedIn:", e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\network\ConnectionEventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
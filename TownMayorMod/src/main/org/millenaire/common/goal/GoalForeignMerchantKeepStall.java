/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class GoalForeignMerchantKeepStall extends Goal
/*    */ {
/*    */   public int actionDuration(MillVillager villager) throws Exception
/*    */   {
/* 10 */     return 60000;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 16 */     if (villager.foreignMerchantStallId >= villager.getHouse().getResManager().stalls.size()) {
/* 17 */       return null;
/*    */     }
/*    */     
/* 20 */     return packDest((org.millenaire.common.Point)villager.getHouse().getResManager().stalls.get(villager.foreignMerchantStallId), villager.getHouse());
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*    */   {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public boolean lookAtPlayer()
/*    */   {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager) throws Exception
/*    */   {
/* 35 */     return MillCommonUtilities.chanceOn(600);
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 40 */     return MillCommonUtilities.randomInt(50);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalForeignMerchantKeepStall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import org.millenaire.common.MillVillager;
/*    */ 
/*    */ public class GoalHide extends Goal
/*    */ {
/*    */   public boolean canBeDoneAtNight()
/*    */   {
/*  9 */     return true;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*    */   {
/* 14 */     if (villager.getPos().distanceToSquared(villager.getTownHall().getResManager().getShelterPos()) <= 9.0D) {
/* 15 */       return null;
/*    */     }
/*    */     
/* 18 */     return packDest(villager.getTownHall().getResManager().getShelterPos(), villager.getTownHall());
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*    */   {
/* 23 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isStillValidSpecific(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 29 */     return villager.getTownHall().underAttack;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager) throws Exception
/*    */   {
/* 34 */     return false;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 39 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.millenaire.common.goal.leasure;
/*    */ 
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.building.Building;
/*    */ 
/*    */ public class GoalGoRest extends org.millenaire.common.goal.Goal
/*    */ {
/*    */   public GoalGoRest()
/*    */   {
/* 10 */     this.leasure = true;
/*    */   }
/*    */   
/*    */   public int actionDuration(MillVillager villager)
/*    */   {
/* 15 */     return 10000;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 25 */     return packDest(villager.getHouse().getResManager().getSleepingPos());
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 30 */     return villager.getHouse().getPos().distanceTo(villager) > 5.0D;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 40 */     return 0;
/*    */   }
/*    */   
/*    */   public int range(MillVillager villager)
/*    */   {
/* 45 */     return 10;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalGoRest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
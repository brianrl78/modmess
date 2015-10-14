/*    */ package org.millenaire.common.goal.leasure;
/*    */ 
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.goal.Goal;
/*    */ 
/*    */ public class GoalChildGoPlay extends Goal
/*    */ {
/*    */   public GoalChildGoPlay()
/*    */   {
/* 11 */     this.leasure = true;
/*    */   }
/*    */   
/*    */   public int actionDuration(MillVillager villager)
/*    */   {
/* 16 */     return 10000;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 21 */     return true;
/*    */   }
/*    */   
/*    */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 26 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 41 */     return MillCommonUtilities.randomInt(5);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalChildGoPlay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
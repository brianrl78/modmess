/*    */ package org.millenaire.common.goal.leasure;
/*    */ 
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.goal.Goal;
/*    */ import org.millenaire.common.goal.Goal.GoalInformation;
/*    */ 
/*    */ public class GoalGoPray
/*    */   extends Goal
/*    */ {
/*    */   public int actionDuration(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 17 */     return 30000;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 28 */     if (villager.canMeditate()) {
/* 29 */       Building temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*    */       
/* 31 */       if (temple != null) {
/* 32 */         return packDest(temple.getResManager().getCraftingPos(), temple);
/*    */       }
/*    */     }
/*    */     
/* 36 */     if (villager.canPerformSacrifices()) {
/* 37 */       Building temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*    */       
/* 39 */       if (temple != null) {
/* 40 */         return packDest(temple.getResManager().getCraftingPos(), temple);
/*    */       }
/*    */     }
/*    */     
/* 44 */     return packDest(villager.getTownHall().getRandomLocationWithTag("Praying"));
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 50 */     if ((villager.hasPrayedToday) && (!villager.canMeditate()) && (!villager.canPerformSacrifices())) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     Point p = villager.getTownHall().getRandomLocationWithTag("Praying");
/*    */     
/* 56 */     if (p == null) {
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     return villager.getPos().distanceTo(p) > 5.0D;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 66 */     villager.hasPrayedToday = true;
/*    */     
/* 68 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 75 */     if ((villager.canMeditate()) || (villager.canPerformSacrifices())) {
/* 76 */       return 200;
/*    */     }
/*    */     
/* 79 */     if (villager.isPriest()) {
/* 80 */       return MillCommonUtilities.randomInt(50) - 25;
/*    */     }
/*    */     
/* 83 */     return MillCommonUtilities.randomInt(20) - 18;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalGoPray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
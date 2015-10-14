/*    */ package org.millenaire.common.goal.leasure;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.goal.Goal;
/*    */ 
/*    */ public class GoalGoSocialise extends Goal
/*    */ {
/*    */   public GoalGoSocialise()
/*    */   {
/* 16 */     this.leasure = true;
/*    */   }
/*    */   
/*    */   public int actionDuration(MillVillager villager)
/*    */   {
/* 21 */     return 10000;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 26 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 32 */     Point dest = null;
/* 33 */     Building destB = null;
/*    */     
/* 35 */     List<Building> possibleDests = new ArrayList();
/*    */     
/* 37 */     for (Building b : villager.getTownHall().getBuildings()) {
/* 38 */       if (b.location.tags.contains("leasure")) {
/* 39 */         possibleDests.add(b);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 44 */     if (possibleDests.isEmpty()) {
/* 45 */       return null;
/*    */     }
/*    */     
/* 48 */     destB = (Building)possibleDests.get(MillCommonUtilities.randomInt(possibleDests.size()));
/* 49 */     dest = destB.getResManager().getLeasurePos();
/*    */     
/* 51 */     if (dest == null) {
/* 52 */       dest = villager.getTownHall().getResManager().getLeasurePos();
/* 53 */       destB = villager.getTownHall();
/*    */     }
/*    */     
/* 56 */     return packDest(dest, destB);
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 66 */     return 5;
/*    */   }
/*    */   
/*    */   public int range(MillVillager villager)
/*    */   {
/* 71 */     return 5;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalGoSocialise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
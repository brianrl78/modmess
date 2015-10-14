/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import org.millenaire.common.InvItem;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.building.Building;
/*    */ 
/*    */ public class GoalGetResourcesForBuild extends Goal
/*    */ {
/*    */   public int actionDuration(MillVillager villager)
/*    */   {
/* 11 */     return 2000;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 16 */     return packDest(villager.getTownHall().getResManager().getSellingPos(), villager.getTownHall());
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 21 */     if ((villager.getTownHall().builder != null) || (villager.getTownHall().buildingLocationIP == null) || (villager.getTownHall().getBblocks() == null)) {
/* 22 */       return false;
/*    */     }
/*    */     
/* 25 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 26 */       if ((Goal.getResourcesForBuild.key.equals(v.goalKey)) || (Goal.construction.key.equals(v.goalKey))) {
/* 27 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isStillValidSpecific(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 37 */     if ((villager.getTownHall().builder != null) && (villager.getTownHall().builder != villager)) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public String nextGoal(MillVillager villager)
/*    */   {
/* 45 */     return Goal.construction.key;
/*    */   }
/*    */   
/*    */   public void onAccept(MillVillager villager)
/*    */   {
/* 50 */     villager.getTownHall().builder = villager;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 56 */     if (villager.getTownHall().getCurrentBuildingPlan() == null) {
/* 57 */       return true;
/*    */     }
/*    */     org.millenaire.common.building.BuildingPlan plan;
/* 60 */     if (villager.getTownHall().canAffordBuild(villager.getTownHall().getCurrentBuildingPlan())) {
/* 61 */       plan = villager.getTownHall().getCurrentBuildingPlan();
/*    */       
/* 63 */       for (InvItem key : plan.resCost.keySet()) {
/* 64 */         villager.takeFromBuilding(villager.getTownHall(), key.getItem(), key.meta, ((Integer)plan.resCost.get(key)).intValue());
/*    */       }
/*    */     }
/*    */     
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 73 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGetResourcesForBuild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
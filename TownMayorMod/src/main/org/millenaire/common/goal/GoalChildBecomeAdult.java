/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MLN.MillenaireException;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingLocation;
/*    */ 
/*    */ public class GoalChildBecomeAdult extends Goal
/*    */ {
/*    */   public GoalChildBecomeAdult()
/*    */   {
/* 16 */     this.maxSimultaneousInBuilding = 1;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 21 */     return true;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */     throws MLN.MillenaireException
/*    */   {
/* 27 */     if (villager.size < 20) {
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     List<Point> possibleDest = new ArrayList();
/* 32 */     List<Point> possibleDestBuilding = new ArrayList();
/* 33 */     int maxPriority = 0;
/*    */     
/* 35 */     for (Building house : villager.getTownHall().getBuildings())
/*    */     {
/* 37 */       if ((house != null) && (!house.equals(villager.getHouse())) && (house.isHouse()))
/*    */       {
/* 39 */         if ((house.canChildMoveIn(villager.gender, villager.familyName)) && (house.location.priorityMoveIn >= maxPriority) && (validateDest(villager, house))) {
/* 40 */           if (house.location.priorityMoveIn > maxPriority) {
/* 41 */             possibleDest.clear();
/* 42 */             possibleDestBuilding.clear();
/* 43 */             maxPriority = house.location.priorityMoveIn;
/*    */           }
/* 45 */           possibleDest.add(house.getResManager().getSleepingPos());
/* 46 */           possibleDestBuilding.add(house.getPos());
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 51 */     if (possibleDest.size() > 0)
/*    */     {
/* 53 */       int rand = org.millenaire.common.core.MillCommonUtilities.randomInt(possibleDest.size());
/*    */       
/* 55 */       return packDest((Point)possibleDest.get(rand), (Point)possibleDestBuilding.get(rand));
/*    */     }
/*    */     
/*    */ 
/* 59 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws MLN.MillenaireException
/*    */   {
/* 64 */     return getDestination(villager) != null;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */     throws MLN.MillenaireException
/*    */   {
/* 70 */     Building house = villager.getGoalBuildingDest();
/*    */     
/* 72 */     if ((house != null) && 
/* 73 */       (house.canChildMoveIn(villager.gender, villager.familyName))) {
/* 74 */       if (MLN.LogChildren >= 1) {
/* 75 */         MLN.major(this, "Adding new adult to house of type " + house.location + ". Gender: " + villager.gender);
/*    */       }
/* 77 */       house.addAdult(villager);
/*    */     }
/*    */     
/*    */ 
/* 81 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 86 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalChildBecomeAdult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
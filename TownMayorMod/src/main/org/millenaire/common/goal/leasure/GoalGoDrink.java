/*    */ package org.millenaire.common.goal.leasure;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.goal.Goal;
/*    */ import org.millenaire.common.goal.Goal.GoalInformation;
/*    */ 
/*    */ public class GoalGoDrink
/*    */   extends Goal
/*    */ {
/*    */   public int actionDuration(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 17 */     return 10000;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 22 */     return true;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 27 */     return packDest(villager.getTownHall().getRandomLocationWithTag("Drinking"));
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 33 */     if (villager.field_70170_p.func_72820_D() % 24000L < 10000L) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (villager.hasDrunkToday) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     Point p = villager.getTownHall().getRandomLocationWithTag("Drinking");
/*    */     
/* 43 */     if (p == null) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     return villager.getPos().distanceTo(p) > 5.0D;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 52 */     villager.hasDrunkToday = true;
/* 53 */     return MillCommonUtilities.chanceOn(600);
/*    */   }
/*    */   
/*    */ 
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 59 */     if (villager.isReligious()) {
/* 60 */       return MillCommonUtilities.randomInt(20) - 10;
/*    */     }
/*    */     
/* 63 */     return MillCommonUtilities.randomInt(10) - 7;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalGoDrink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
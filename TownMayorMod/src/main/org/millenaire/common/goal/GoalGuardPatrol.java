/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class GoalGuardPatrol extends Goal
/*    */ {
/*    */   GoalGuardPatrol()
/*    */   {
/* 14 */     this.leasure = true;
/*    */   }
/*    */   
/*    */   public boolean allowRandomMoves()
/*    */   {
/* 19 */     return true;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 24 */     return packDest(villager.getTownHall().getRandomLocationWithTag("Patrol"));
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 29 */     return new ItemStack[] { villager.getWeapon() };
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 35 */     if ((villager.lastGoalTime.containsKey(this)) && (((Long)villager.lastGoalTime.get(this)).longValue() > villager.field_70170_p.func_72820_D() + 2000L)) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     Point p = villager.getTownHall().getRandomLocationWithTag("Patrol");
/*    */     
/* 41 */     if (p == null) {
/* 42 */       return false;
/*    */     }
/*    */     
/* 45 */     return villager.getPos().distanceTo(p) > 5.0D;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 50 */     return MillCommonUtilities.chanceOn(600);
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 55 */     return MillCommonUtilities.randomInt(20) - 10;
/*    */   }
/*    */   
/*    */ 
/*    */   public int range(MillVillager villager)
/*    */   {
/* 61 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGuardPatrol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
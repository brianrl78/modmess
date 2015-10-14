/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalPlantNetherWarts extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 14 */     return packDest(villager.getHouse().getResManager().getNetherWartsPlantingLocation(), villager.getHouse());
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 19 */     return villager.getBestHoeStack();
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 24 */     return getDestination(villager) != null;
/*    */   }
/*    */   
/*    */   public boolean lookAtGoal()
/*    */   {
/* 29 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 35 */     net.minecraft.block.Block block = villager.getBlock(villager.getGoalDestPoint());
/*    */     
/* 37 */     Point cropPoint = villager.getGoalDestPoint().getAbove();
/*    */     
/* 39 */     block = villager.getBlock(cropPoint);
/* 40 */     if (block == Blocks.field_150350_a) {
/* 41 */       villager.setBlockAndMetadata(cropPoint, Blocks.field_150388_bm, 0);
/*    */       
/* 43 */       villager.func_71038_i();
/*    */     }
/*    */     
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 51 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalPlantNetherWarts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
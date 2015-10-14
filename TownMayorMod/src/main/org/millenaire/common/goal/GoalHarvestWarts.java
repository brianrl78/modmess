/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ 
/*    */ public class GoalHarvestWarts extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 14 */     return packDest(villager.getHouse().getResManager().getNetherWartsHarvestLocation(), villager.getHouse());
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
/* 35 */     Point cropPoint = villager.getGoalDestPoint().getAbove();
/*    */     
/* 37 */     if ((villager.getBlock(cropPoint) == Blocks.field_150388_bm) && (villager.getBlockMeta(cropPoint) == 3)) {
/* 38 */       villager.setBlockAndMetadata(cropPoint, Blocks.field_150350_a, 0);
/* 39 */       villager.getHouse().storeGoods(Items.field_151075_bm, 1);
/*    */       
/* 41 */       villager.func_71038_i();
/*    */     }
/*    */     
/* 44 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 49 */     int p = 100 - villager.getHouse().countGoods(Items.field_151075_bm) * 4;
/*    */     
/* 51 */     return p;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalHarvestWarts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
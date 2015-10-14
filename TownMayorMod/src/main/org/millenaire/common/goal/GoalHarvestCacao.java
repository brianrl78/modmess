/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.block.BlockCocoa;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalHarvestCacao extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 15 */     Point p = villager.getHouse().getResManager().getCocoaHarvestLocation();
/*    */     
/* 17 */     return packDest(p, villager.getHouse());
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 22 */     return villager.getBestHoeStack();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 28 */     return getDestination(villager).getDest() != null;
/*    */   }
/*    */   
/*    */   public boolean lookAtGoal()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 39 */     Point cropPoint = villager.getGoalDestPoint();
/*    */     
/* 41 */     if (cropPoint.getBlock(villager.field_70170_p) == Blocks.field_150375_by) {
/* 42 */       int meta = cropPoint.getMeta(villager.field_70170_p);
/*    */       
/* 44 */       if (BlockCocoa.func_149987_c(meta) >= 2) {
/* 45 */         villager.setBlockAndMetadata(cropPoint, Blocks.field_150350_a, 0);
/*    */         
/* 47 */         int nbcrop = 2;
/* 48 */         float irrigation = villager.getTownHall().getVillageIrrigation();
/* 49 */         double rand = Math.random();
/* 50 */         if (rand < irrigation / 100.0F) {
/* 51 */           nbcrop++;
/*    */         }
/* 53 */         villager.addToInv(net.minecraft.init.Items.field_151100_aR, 3, nbcrop);
/*    */         
/* 55 */         villager.func_71038_i();
/*    */       }
/*    */     }
/*    */     
/* 59 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 64 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalHarvestCacao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
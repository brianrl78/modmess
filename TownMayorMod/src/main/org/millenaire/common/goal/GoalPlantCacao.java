/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalPlantCacao extends Goal
/*    */ {
/* 13 */   private static ItemStack[] cacao = { new ItemStack(Blocks.field_150375_by, 1) };
/*    */   
/*    */   private int getCocoaMeta(World world, Point p)
/*    */   {
/* 17 */     Block var5 = p.getRelative(0.0D, 0.0D, -1.0D).getBlock(world);
/* 18 */     Block var6 = p.getRelative(0.0D, 0.0D, 1.0D).getBlock(world);
/* 19 */     Block var7 = p.getRelative(-1.0D, 0.0D, 0.0D).getBlock(world);
/* 20 */     Block var8 = p.getRelative(1.0D, 0.0D, 0.0D).getBlock(world);
/* 21 */     byte meta = 0;
/*    */     
/* 23 */     if (var5 == Blocks.field_150364_r) {
/* 24 */       meta = 2;
/*    */     }
/*    */     
/* 27 */     if (var6 == Blocks.field_150364_r) {
/* 28 */       meta = 0;
/*    */     }
/*    */     
/* 31 */     if (var7 == Blocks.field_150364_r) {
/* 32 */       meta = 1;
/*    */     }
/*    */     
/* 35 */     if (var8 == Blocks.field_150364_r) {
/* 36 */       meta = 3;
/*    */     }
/*    */     
/* 39 */     return meta;
/*    */   }
/*    */   
/*    */ 
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 45 */     Point p = villager.getHouse().getResManager().getCocoaPlantingLocation();
/*    */     
/* 47 */     return packDest(p, villager.getHouse());
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 52 */     return cacao;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 58 */     return getDestination(villager).getDest() != null;
/*    */   }
/*    */   
/*    */   public boolean lookAtGoal()
/*    */   {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 69 */     Block block = villager.getBlock(villager.getGoalDestPoint());
/*    */     
/* 71 */     Point cropPoint = villager.getGoalDestPoint();
/*    */     
/* 73 */     block = villager.getBlock(cropPoint);
/* 74 */     if (block == Blocks.field_150350_a) {
/* 75 */       villager.setBlockAndMetadata(cropPoint, Blocks.field_150375_by, getCocoaMeta(villager.field_70170_p, cropPoint));
/*    */       
/* 77 */       villager.func_71038_i();
/*    */     }
/*    */     
/* 80 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 85 */     return 120;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalPlantCacao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
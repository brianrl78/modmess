/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.InvItem;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*    */ 
/*    */ public class GoalGatherGoods extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 15 */     EntityItem item = villager.getClosestItemVertical(villager.getGoodsToCollect(), villager.getGatheringRange(), 10);
/* 16 */     if (item == null) {
/* 17 */       return null;
/*    */     }
/*    */     
/* 20 */     return packDest(new Point(item));
/*    */   }
/*    */   
/*    */   public AStarConfig getPathingConfig()
/*    */   {
/* 25 */     return JPS_CONFIG_WIDE;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 31 */     if (villager.getGoodsToCollect().length == 0) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     EntityItem item = villager.getClosestItemVertical(villager.getGoodsToCollect(), villager.getGatheringRange(), 10);
/* 36 */     return item != null;
/*    */   }
/*    */   
/*    */   public boolean lookAtGoal()
/*    */   {
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 51 */     return 100;
/*    */   }
/*    */   
/*    */   public int range(MillVillager villager)
/*    */   {
/* 56 */     return 5;
/*    */   }
/*    */   
/*    */   public boolean stuckAction(MillVillager villager)
/*    */   {
/* 61 */     InvItem[] goods = villager.getGoodsToCollect();
/*    */     
/* 63 */     if (goods != null) {
/* 64 */       EntityItem item = MillCommonUtilities.getClosestItemVertical(villager.field_70170_p, villager.getGoalDestPoint(), goods, 3, 20);
/* 65 */       if (item != null) {
/* 66 */         item.func_70106_y();
/* 67 */         villager.addToInv(item.func_92059_d().func_77973_b(), item.func_92059_d().func_77960_j(), 1);
/* 68 */         return true;
/*    */       }
/*    */     }
/* 71 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGatherGoods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
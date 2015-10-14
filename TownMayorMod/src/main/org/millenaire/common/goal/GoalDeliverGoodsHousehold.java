/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.InvItem;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalDeliverGoodsHousehold extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*    */   {
/* 15 */     return packDest(villager.getHouse().getResManager().getSellingPos(), villager.getHouse());
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 21 */     List<ItemStack> items = new java.util.ArrayList();
/*    */     
/* 23 */     for (MillVillager v : villager.getHouse().villagers)
/*    */     {
/* 25 */       for (InvItem key : v.requiresGoods().keySet()) {
/* 26 */         if (villager.countInv(key.getItem(), key.meta) > 0) {
/* 27 */           items.add(new ItemStack(key.getItem(), 1, key.meta));
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 33 */     return (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*    */   {
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 44 */     for (MillVillager v : villager.getHouse().villagers) {
/* 45 */       for (InvItem key : v.requiresGoods().keySet()) {
/* 46 */         villager.putInBuilding(villager.getHouse(), key.getItem(), key.meta, 256);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 56 */     return 100;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalDeliverGoodsHousehold.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
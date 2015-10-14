/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalMerchantVisitInn extends Goal
/*     */ {
/*     */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*     */   {
/*  17 */     return packDest(villager.getHouse().getResManager().getSellingPos(), villager.getHouse());
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  23 */     List<ItemStack> items = new java.util.ArrayList();
/*     */     
/*  25 */     for (InvItem good : villager.getInventoryKeys()) {
/*  26 */       if (villager.countInv(good.getItem(), good.meta) > 0) {
/*  27 */         items.add(new ItemStack(good.getItem(), 1, good.meta));
/*     */       }
/*     */     }
/*     */     
/*  31 */     return (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  39 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  40 */       delayOver = true;
/*     */     } else {
/*  42 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  45 */     int nb = 0;
/*     */     
/*  47 */     for (InvItem good : villager.getInventoryKeys()) {
/*  48 */       int nbcount = villager.countInv(good.getItem(), good.meta);
/*  49 */       if ((nbcount > 0) && (villager.getTownHall().nbGoodNeeded(good.getItem(), good.meta) == 0))
/*     */       {
/*  51 */         nb += nbcount;
/*     */         
/*  53 */         if (delayOver) {
/*  54 */           return true;
/*     */         }
/*  56 */         if (nb > 64) {
/*  57 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  62 */     for (Goods good : villager.getTownHall().culture.goodsList) {
/*  63 */       if ((villager.getHouse().countGoods(good.item.getItem(), good.item.meta) > 0) && (villager.countInv(good.item.getItem(), good.item.meta) < villager.getTownHall().nbGoodNeeded(good.item.getItem(), good.item.meta)))
/*     */       {
/*  65 */         if (MLN.LogMerchant >= 1) {
/*  66 */           MLN.major(this, "Visiting the Inn to take imports");
/*     */         }
/*  68 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  78 */     String s = "";
/*     */     
/*  80 */     for (InvItem good : villager.getInventoryKeys()) {
/*  81 */       if ((villager.countInv(good.getItem(), good.meta) > 0) && (villager.getTownHall().nbGoodNeeded(good.getItem(), good.meta) == 0)) {
/*  82 */         int nb = villager.putInBuilding(villager.getHouse(), good.getItem(), good.meta, 99999999);
/*     */         
/*  84 */         if (nb > 0) {
/*  85 */           s = s + ";" + good.getItem() + "/" + good.meta + "/" + nb;
/*     */         }
/*     */       }
/*     */     }
/*  89 */     if (s.length() > 0) {
/*  90 */       villager.getHouse().visitorsList.add("storedexports;" + villager.getName() + s);
/*     */     }
/*     */     
/*  93 */     s = "";
/*     */     
/*  95 */     for (Goods good : villager.getTownHall().culture.goodsList) {
/*  96 */       int nbNeeded = villager.getTownHall().nbGoodNeeded(good.item.getItem(), good.item.meta);
/*  97 */       if (villager.countInv(good.item.getItem(), good.item.meta) < nbNeeded) {
/*  98 */         int nb = villager.takeFromBuilding(villager.getHouse(), good.item.getItem(), good.item.meta, nbNeeded - villager.countInv(good.item.getItem(), good.item.meta));
/*     */         
/* 100 */         if (nb > 0) {
/* 101 */           s = s + ";" + good.item.getItem() + "/" + good.item.meta + "/" + nb;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 106 */     if (s.length() > 0) {
/* 107 */       villager.getHouse().visitorsList.add("broughtimport;" + villager.getName() + s);
/*     */     }
/*     */     
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 115 */     return 100;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalMerchantVisitInn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ 
/*     */ public class GoalDeliverResourcesShop extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  16 */     return 2000;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  21 */     return getDestination(villager, false);
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager, boolean test)
/*     */   {
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  28 */     if (!test) {
/*  29 */       delayOver = true; } else { boolean delayOver;
/*  30 */       if (!villager.lastGoalTime.containsKey(this)) {
/*  31 */         delayOver = true;
/*     */       } else {
/*  33 */         delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */       }
/*     */     }
/*  36 */     for (java.util.Iterator i$ = villager.getTownHall().getShops().iterator(); i$.hasNext();) { shop = (Building)i$.next();
/*     */       
/*  38 */       nb = 0;
/*     */       
/*  40 */       if (villager.getCulture().shopNeeds.containsKey(shop.location.shop))
/*  41 */         for (InvItem item : (List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/*  42 */           int nbcount = villager.countInv(item.getItem(), item.meta);
/*  43 */           if (nbcount > 0) {
/*  44 */             nb += nbcount;
/*  45 */             if (delayOver) {
/*  46 */               return packDest(shop.getResManager().getSellingPos(), shop);
/*     */             }
/*  48 */             if (nb > 16)
/*  49 */               return packDest(shop.getResManager().getSellingPos(), shop);
/*     */           }
/*     */         }
/*     */     }
/*     */     Building shop;
/*     */     int nb;
/*  55 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  61 */     List<ItemStack> items = new java.util.ArrayList();
/*     */     
/*  63 */     Building shop = villager.getGoalBuildingDest();
/*     */     
/*  65 */     if ((shop != null) && 
/*  66 */       (villager.getCulture().shopNeeds.containsKey(shop.location.shop))) {
/*  67 */       for (InvItem item : (List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/*  68 */         if (villager.countInv(item.getItem(), item.meta) > 0) {
/*  69 */           items.add(new ItemStack(item.getItem(), 1, item.meta));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  75 */     return (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  81 */     return getDestination(villager, true) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  88 */     Building shop = villager.getGoalBuildingDest();
/*     */     
/*  90 */     if ((shop != null) && 
/*  91 */       (villager.getCulture().shopNeeds.containsKey(shop.location.shop))) {
/*  92 */       for (InvItem item : (List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/*  93 */         villager.putInBuilding(shop, item.getItem(), item.meta, 256);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 103 */     int priority = 0;
/*     */     
/* 105 */     for (Building shop : villager.getTownHall().getShops()) {
/* 106 */       if (villager.getCulture().shopNeeds.containsKey(shop.location.shop)) {
/* 107 */         for (InvItem item : (List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/* 108 */           priority += villager.countInv(item.getItem(), item.meta) * 10;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 113 */     return priority;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalDeliverResourcesShop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ public class GoalGetResourcesForShops extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  12 */     return 2000;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  17 */     return getDestination(villager, false);
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager, boolean test)
/*     */   {
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  24 */     if (!test) {
/*  25 */       delayOver = true; } else { boolean delayOver;
/*  26 */       if (!villager.lastGoalTime.containsKey(this)) {
/*  27 */         delayOver = true;
/*     */       } else {
/*  29 */         delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */       }
/*     */     }
/*  32 */     for (Iterator i$ = villager.getTownHall().getShops().iterator(); i$.hasNext();) { shop = (Building)i$.next();
/*  33 */       nb = 0;
/*  34 */       if (villager.getCulture().shopNeeds.containsKey(shop.location.shop)) {
/*  35 */         for (InvItem item : (java.util.List)villager.getCulture().shopNeeds.get(shop.location.shop))
/*     */         {
/*  37 */           if (shop != villager.getHouse())
/*     */           {
/*  39 */             int nbcount = villager.getHouse().nbGoodAvailable(item, false, true);
/*  40 */             if (nbcount > 0)
/*     */             {
/*  42 */               nb += nbcount;
/*  43 */               if ((delayOver) || (nb > 16)) {
/*  44 */                 return packDest(villager.getHouse().getResManager().getSellingPos(), villager.getHouse());
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*  49 */           if ((villager.getTownHall() != shop) && (villager.getTownHall().nbGoodAvailable(item, false, true) > 0)) {
/*  50 */             nb += villager.getTownHall().nbGoodAvailable(item, false, true);
/*     */             
/*  52 */             if ((delayOver) || (nb > 16))
/*  53 */               return packDest(villager.getTownHall().getResManager().getSellingPos(), villager.getTownHall());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Building shop;
/*     */     int nb;
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  65 */     return getDestination(villager, true) != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  71 */     Building dest = villager.getGoalBuildingDest();
/*     */     
/*  73 */     if (dest == null) {
/*  74 */       org.millenaire.common.MLN.error(villager, "Invalid destination for GoalGetResourcesForShops goal: " + villager.getGoalBuildingDestPoint() + " (house: " + villager.getHouse().getPos() + ", TH: " + villager.getTownHall().getPos() + "), pathDestPoint: " + villager.getGoalDestPoint());
/*     */       
/*  76 */       return true;
/*     */     }
/*     */     
/*  79 */     for (Building shop : villager.getTownHall().getShops()) {
/*  80 */       if ((!shop.getPos().equals(villager.getGoalDestPoint())) && (villager.getCulture().shopNeeds.containsKey(shop.location.shop))) {
/*  81 */         for (InvItem item : (java.util.List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/*  82 */           villager.takeFromBuilding(dest, item.getItem(), item.meta, dest.nbGoodAvailable(item, false, true));
/*     */         }
/*     */       }
/*     */     }
/*  86 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int priority(MillVillager villager)
/*     */   {
/*  92 */     int priority = 0;
/*     */     
/*  94 */     for (Iterator i$ = villager.getTownHall().getShops().iterator(); i$.hasNext();) { shop = (Building)i$.next();
/*  95 */       if (villager.getCulture().shopNeeds.containsKey(shop.location.shop)) {
/*  96 */         for (InvItem item : (java.util.List)villager.getCulture().shopNeeds.get(shop.location.shop)) {
/*  97 */           priority += villager.getHouse().countGoods(item.getItem(), item.meta) * 5;
/*  98 */           if (villager.getTownHall() != shop) {
/*  99 */             priority += villager.getTownHall().countGoods(item.getItem(), item.meta) * 5;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Building shop;
/* 105 */     return priority;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGetResourcesForShops.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
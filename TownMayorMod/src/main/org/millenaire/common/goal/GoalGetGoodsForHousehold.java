/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ public class GoalGetGoodsForHousehold extends Goal
/*     */ {
/*     */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*     */   {
/*  14 */     return getDestination(villager, false);
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager, boolean test) throws Exception {
/*  18 */     List<Building> buildings = null;
/*     */     
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  22 */     if (!test) {
/*  23 */       delayOver = true; } else { boolean delayOver;
/*  24 */       if (!villager.lastGoalTime.containsKey(this)) {
/*  25 */         delayOver = true;
/*     */       } else {
/*  27 */         delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */       }
/*     */     }
/*  30 */     for (MillVillager v : villager.getHouse().villagers)
/*     */     {
/*  32 */       goods = v.requiresGoods();
/*     */       
/*  34 */       nb = 0;
/*     */       
/*  36 */       for (i$ = goods.keySet().iterator(); i$.hasNext();) { key = (InvItem)i$.next();
/*  37 */         if (villager.getHouse().countGoods(key.getItem(), key.meta) < ((Integer)goods.get(key)).intValue() / 2) {
/*  38 */           if (buildings == null) {
/*  39 */             buildings = villager.getTownHall().getBuildings();
/*     */           }
/*     */           
/*  42 */           for (Building building : buildings) {
/*  43 */             int nbav = building.nbGoodAvailable(key, false, false);
/*  44 */             if ((nbav > 0) && (building != villager.getHouse())) {
/*  45 */               nb += nbav;
/*     */               
/*  47 */               if ((delayOver) || (nb > 16))
/*  48 */                 return packDest(building.getResManager().getSellingPos(), building);
/*     */             }
/*     */           }
/*     */         } } }
/*     */     HashMap<InvItem, Integer> goods;
/*     */     int nb;
/*     */     Iterator i$;
/*     */     InvItem key;
/*  56 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  62 */     return getDestination(villager, true) != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  67 */     return true;
/*     */   }
/*     */   
/*     */   public String nextGoal(MillVillager villager) throws Exception
/*     */   {
/*  72 */     return Goal.deliverGoodsHousehold.key;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  78 */     Building shop = villager.getGoalBuildingDest();
/*     */     
/*  80 */     if ((shop == null) || (shop == villager.getHouse())) {
/*  81 */       return true;
/*     */     }
/*     */     
/*  84 */     for (MillVillager v : villager.getHouse().villagers)
/*     */     {
/*  86 */       goods = v.requiresGoods();
/*     */       
/*  88 */       for (InvItem key : goods.keySet()) {
/*  89 */         if (villager.getHouse().countGoods(key.getItem(), key.meta) < ((Integer)goods.get(key)).intValue())
/*     */         {
/*  91 */           int nb = Math.min(shop.nbGoodAvailable(key, false, false), ((Integer)goods.get(key)).intValue());
/*     */           
/*  93 */           villager.takeFromBuilding(shop, key.getItem(), key.meta, nb);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     HashMap<InvItem, Integer> goods;
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 105 */     int nb = 0;
/*     */     
/* 107 */     List<Building> shops = villager.getTownHall().getShops();
/*     */     
/* 109 */     for (MillVillager v : villager.getHouse().villagers)
/*     */     {
/* 111 */       goods = v.requiresGoods();
/*     */       
/* 113 */       for (i$ = goods.keySet().iterator(); i$.hasNext();) { key = (InvItem)i$.next();
/* 114 */         if (villager.getHouse().countGoods(key.getItem(), key.meta) < ((Integer)goods.get(key)).intValue() / 2)
/* 115 */           for (Building shop : shops) {
/* 116 */             int nbav = shop.nbGoodAvailable(key, false, false);
/* 117 */             if ((nbav > 0) && (shop != villager.getHouse()))
/* 118 */               nb += nbav;
/*     */           }
/*     */       }
/*     */     }
/*     */     HashMap<InvItem, Integer> goods;
/*     */     Iterator i$;
/*     */     InvItem key;
/* 125 */     return nb * 20;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGetGoodsForHousehold.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
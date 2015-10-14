/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalMerchantVisitBuilding extends Goal
/*     */ {
/*     */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*     */   {
/*  20 */     for (Goods good : villager.getTownHall().culture.goodsList) {
/*  21 */       if ((villager.countInv(good.item.getItem(), good.item.meta) > 0) && (villager.getTownHall().nbGoodNeeded(good.item.getItem(), good.item.meta) > 0))
/*     */       {
/*  23 */         if (MLN.LogMerchant >= 3) {
/*  24 */           MLN.debug(villager, "TH needs " + villager.getTownHall().nbGoodNeeded(good.item.getItem(), good.item.meta) + " good " + good.item.getName() + ", merchant has " + villager.countInv(good.item.getItem(), good.item.meta));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*  29 */         return packDest(villager.getTownHall().getResManager().getSellingPos(), villager.getTownHall());
/*     */       }
/*     */     }
/*     */     
/*  33 */     HashMap<Goods, Integer> neededGoods = villager.getTownHall().getImportsNeededbyOtherVillages();
/*     */     
/*  35 */     for (Iterator i$ = villager.getTownHall().getBuildings().iterator(); i$.hasNext();) { shop = (Building)i$.next();
/*  36 */       for (Goods good : villager.getTownHall().culture.goodsList) {
/*  37 */         if ((!shop.isInn) && (shop.nbGoodAvailable(good.item.getItem(), good.item.meta, true, false) > 0) && (neededGoods.containsKey(good)) && (((Integer)neededGoods.get(good)).intValue() > villager.getHouse().countGoods(good.item.getItem(), good.item.meta) + villager.countInv(good.item.getItem(), good.item.meta)))
/*     */         {
/*     */ 
/*  40 */           if (MLN.LogMerchant >= 3) {
/*  41 */             MLN.debug(villager, "Shop " + shop + " has " + shop.nbGoodAvailable(good.item.getItem(), good.item.meta, true, false) + " good to pick up.");
/*     */           }
/*     */           
/*  44 */           return packDest(shop.getResManager().getSellingPos(), shop);
/*     */         }
/*     */       }
/*     */     }
/*     */     Building shop;
/*  49 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  55 */     List<ItemStack> items = new ArrayList();
/*     */     
/*  57 */     for (InvItem item : villager.getInventoryKeys()) {
/*  58 */       if (villager.countInv(item) > 0) {
/*  59 */         items.add(new ItemStack(item.getItem(), 1, item.meta));
/*     */       }
/*     */     }
/*     */     
/*  63 */     return (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/*  68 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  74 */     Building shop = villager.getGoalBuildingDest();
/*  75 */     HashMap<Goods, Integer> neededGoods = villager.getTownHall().getImportsNeededbyOtherVillages();
/*     */     
/*  77 */     if ((shop == null) || (shop.isInn)) {
/*  78 */       return true;
/*     */     }
/*     */     
/*  81 */     if (shop.isTownhall) {
/*  82 */       for (Goods good : villager.getTownHall().culture.goodsList) {
/*  83 */         int nbNeeded = shop.nbGoodNeeded(good.item.getItem(), good.item.meta);
/*  84 */         if (nbNeeded > 0) {
/*  85 */           int nb = villager.putInBuilding(shop, good.item.getItem(), good.item.meta, nbNeeded);
/*  86 */           if ((nb > 0) && (MLN.LogMerchant >= 2)) {
/*  87 */             MLN.minor(shop, villager + " delivered " + nb + " " + good.getName() + ".");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     for (Goods good : villager.getTownHall().culture.goodsList) {
/*  94 */       if ((neededGoods.containsKey(good)) && 
/*  95 */         (shop.nbGoodAvailable(good.item.getItem(), good.item.meta, true, false) > 0) && (villager.getHouse().countGoods(good.item.getItem(), good.item.meta) + villager.countInv(good.item.getItem(), good.item.meta) < ((Integer)neededGoods.get(good)).intValue()))
/*     */       {
/*     */ 
/*  98 */         int nb = Math.min(shop.nbGoodAvailable(good.item.getItem(), good.item.meta, true, false), ((Integer)neededGoods.get(good)).intValue() - villager.getHouse().countGoods(good.item.getItem(), good.item.meta) - villager.countInv(good.item.getItem(), good.item.meta));
/*     */         
/* 100 */         nb = villager.takeFromBuilding(shop, good.item.getItem(), good.item.meta, nb);
/* 101 */         if (MLN.LogMerchant >= 2) {
/* 102 */           MLN.minor(shop, villager + " took " + nb + " " + good.getName() + " for trading.");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 113 */     return 100;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalMerchantVisitBuilding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
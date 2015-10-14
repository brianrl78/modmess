/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import net.minecraft.item.Item;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ 
/*     */ public class GoalGetTool extends Goal
/*     */ {
/*  11 */   private static final Item[][] classes = { MillVillager.axes, MillVillager.pickaxes, MillVillager.shovels, MillVillager.hoes, MillVillager.helmets, MillVillager.chestplates, MillVillager.legs, MillVillager.boots, MillVillager.weaponsSwords, MillVillager.weaponsRanged };
/*     */   
/*     */   public GoalGetTool()
/*     */   {
/*  15 */     this.maxSimultaneousInBuilding = 2;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  21 */     for (Building shop : villager.getTownHall().getShops()) {
/*  22 */       for (InvItem key : villager.getToolsNeeded()) {
/*  23 */         if ((villager.countInv(key.getItem(), key.meta) == 0) && (shop.countGoods(key.getItem(), key.meta) > 0) && (!hasBetterTool(villager, key)) && (validateDest(villager, shop))) {
/*  24 */           return packDest(shop.getResManager().getSellingPos(), shop);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  29 */     return null;
/*     */   }
/*     */   
/*     */   private boolean hasBetterTool(MillVillager villager, InvItem key)
/*     */   {
/*  34 */     if (key.meta > 0) {
/*  35 */       return false;
/*     */     }
/*     */     
/*  38 */     for (Item[] toolclass : classes)
/*     */     {
/*  40 */       int targetPos = -1;
/*     */       
/*  42 */       for (int i = 0; (i < toolclass.length) && (targetPos == -1); i++) {
/*  43 */         if (toolclass[i] == key.getItem()) {
/*  44 */           targetPos = i;
/*     */         }
/*     */       }
/*     */       
/*  48 */       if (targetPos != -1) {
/*  49 */         for (int i = 0; i < targetPos; i++) {
/*  50 */           if (villager.countInv(toolclass[i], 0) > 0) {
/*  51 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  62 */     if (villager.getToolsNeeded().length == 0) {
/*  63 */       return false;
/*     */     }
/*     */     
/*  66 */     for (Building shop : villager.getTownHall().getShops()) {
/*  67 */       for (InvItem key : villager.getToolsNeeded()) {
/*  68 */         if ((villager.countInv(key.getItem(), key.meta) == 0) && (shop.countGoods(key.getItem(), key.meta) > 0) && (!hasBetterTool(villager, key)) && (validateDest(villager, shop))) {
/*  69 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  85 */     Building shop = villager.getGoalBuildingDest();
/*     */     
/*  87 */     if (shop == null) {
/*  88 */       return true;
/*     */     }
/*     */     
/*  91 */     for (InvItem key : villager.getToolsNeeded()) {
/*  92 */       if ((villager.countInv(key.getItem(), key.meta) == 0) && (shop.countGoods(key.getItem(), key.meta) > 0) && (!hasBetterTool(villager, key))) {
/*  93 */         villager.takeFromBuilding(shop, key.getItem(), key.meta, 1);
/*     */       }
/*     */     }
/*     */     
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 102 */     return 100;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalGetTool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
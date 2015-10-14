/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ 
/*     */ public class GoalBringBackResourcesHome extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  15 */     return 2000;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  20 */     return packDest(villager.getHouse().getResManager().getSellingPos(), villager.getHouse());
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  26 */     List<ItemStack> items = new ArrayList();
/*     */     
/*  28 */     for (InvItem key : villager.getInventoryKeys()) {
/*  29 */       for (InvItem key2 : villager.getGoodsToBringBackHome()) {
/*  30 */         if (key2.equals(key)) {
/*  31 */           items.add(new ItemStack(key.getItem(), 1, key.meta));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  36 */     return (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  42 */     if (villager.getGoodsToBringBackHome().length == 0) {
/*  43 */       return false;
/*     */     }
/*     */     
/*  46 */     int nb = 0;
/*     */     
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  50 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  51 */       delayOver = true;
/*     */     } else {
/*  53 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  56 */     for (InvItem key : villager.getInventoryKeys()) {
/*  57 */       if (villager.countInv(key) > 0) {
/*  58 */         for (InvItem key2 : villager.getGoodsToBringBackHome()) {
/*  59 */           if (key2.matches(key)) {
/*  60 */             nb += villager.countInv(key);
/*     */             
/*  62 */             if (delayOver) {
/*  63 */               return true;
/*     */             }
/*  65 */             if (nb > 16) {
/*  66 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  78 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  83 */     for (InvItem key : villager.getInventoryKeys()) {
/*  84 */       for (InvItem key2 : villager.getGoodsToBringBackHome()) {
/*  85 */         if (key2.matches(key)) {
/*  86 */           villager.putInBuilding(villager.getHouse(), key.getItem(), key.meta, 256);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  91 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int priority(MillVillager villager)
/*     */   {
/*  97 */     int nbGoods = 0;
/*     */     
/*  99 */     for (InvItem key : villager.getInventoryKeys()) {
/* 100 */       for (InvItem key2 : villager.getGoodsToBringBackHome()) {
/* 101 */         if (key2.matches(key)) {
/* 102 */           nbGoods += villager.countInv(key);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 107 */     return 10 + nbGoods * 3;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBringBackResourcesHome.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
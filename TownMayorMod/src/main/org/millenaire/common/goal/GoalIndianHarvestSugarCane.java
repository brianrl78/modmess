/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ 
/*     */ public class GoalIndianHarvestSugarCane extends Goal
/*     */ {
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  18 */     List<Point> vp = new ArrayList();
/*  19 */     List<Point> buildingp = new ArrayList();
/*  20 */     for (Building plantation : villager.getTownHall().getBuildingsWithTag("sugarplantation")) {
/*  21 */       Point p = plantation.getResManager().getSugarCaneHarvestLocation();
/*  22 */       if (p != null) {
/*  23 */         vp.add(p);
/*  24 */         buildingp.add(plantation.getPos());
/*     */       }
/*     */     }
/*     */     
/*  28 */     if (vp.isEmpty()) {
/*  29 */       return null;
/*     */     }
/*     */     
/*  32 */     Point p = (Point)vp.get(0);
/*  33 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  35 */     for (int i = 1; i < vp.size(); i++) {
/*  36 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  37 */         p = (Point)vp.get(i);
/*  38 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  41 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public net.minecraft.item.ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  46 */     return villager.getBestHoeStack();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  52 */     int nbsimultaneous = 0;
/*  53 */     for (MillVillager v : villager.getTownHall().villagers) {
/*  54 */       if ((v != villager) && (this.key.equals(v.goalKey))) {
/*  55 */         nbsimultaneous++;
/*     */       }
/*     */     }
/*  58 */     if (nbsimultaneous > 2) {
/*  59 */       return false;
/*     */     }
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  63 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  64 */       delayOver = true;
/*     */     } else {
/*  66 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  69 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("sugarplantation")) {
/*  70 */       int nb = kiln.getResManager().getNbSugarCaneHarvestLocation();
/*     */       
/*  72 */       if ((nb > 0) && (delayOver)) {
/*  73 */         return true;
/*     */       }
/*  75 */       if (nb > 4) {
/*  76 */         return true;
/*     */       }
/*     */     }
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  84 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  90 */     Point cropPoint = villager.getGoalDestPoint().getRelative(0.0D, 3.0D, 0.0D);
/*     */     
/*  92 */     if (villager.getBlock(cropPoint) == Blocks.field_150436_aH) {
/*  93 */       villager.setBlockAndMetadata(cropPoint, Blocks.field_150350_a, 0);
/*     */       
/*  95 */       int nbcrop = 1;
/*  96 */       float irrigation = villager.getTownHall().getVillageIrrigation();
/*  97 */       double rand = Math.random();
/*  98 */       if (rand < irrigation / 100.0F) {
/*  99 */         nbcrop++;
/*     */       }
/*     */       
/* 102 */       villager.addToInv(Items.field_151120_aE, nbcrop);
/*     */     }
/*     */     
/* 105 */     cropPoint = villager.getGoalDestPoint().getRelative(0.0D, 2.0D, 0.0D);
/*     */     
/* 107 */     if (villager.getBlock(cropPoint) == Blocks.field_150436_aH) {
/* 108 */       villager.setBlockAndMetadata(cropPoint, Blocks.field_150350_a, 0);
/*     */       
/* 110 */       int nbcrop = 1;
/* 111 */       float irrigation = villager.getTownHall().getVillageIrrigation();
/* 112 */       double rand = Math.random();
/* 113 */       if (rand < irrigation / 100.0F) {
/* 114 */         nbcrop++;
/*     */       }
/*     */       
/* 117 */       villager.func_71038_i();
/*     */       
/* 119 */       villager.addToInv(Items.field_151120_aE, nbcrop);
/*     */     }
/*     */     
/* 122 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 127 */     int p = 200 - villager.getTownHall().nbGoodAvailable(Items.field_151120_aE, 0, false, false) * 4;
/*     */     
/* 129 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 130 */       if (this.key.equals(v.goalKey)) {
/* 131 */         p /= 2;
/*     */       }
/*     */     }
/*     */     
/* 135 */     return p;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalIndianHarvestSugarCane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
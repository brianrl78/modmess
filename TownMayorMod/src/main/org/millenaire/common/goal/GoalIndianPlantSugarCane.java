/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ 
/*     */ public class GoalIndianPlantSugarCane extends Goal
/*     */ {
/*  17 */   private static ItemStack[] sugarcane = { new ItemStack(net.minecraft.init.Items.field_151120_aE, 1) };
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  21 */     List<Point> vp = new ArrayList();
/*  22 */     List<Point> buildingp = new ArrayList();
/*  23 */     for (Building plantation : villager.getTownHall().getBuildingsWithTag("sugarplantation")) {
/*  24 */       Point p = plantation.getResManager().getSugarCanePlantingLocation();
/*  25 */       if (p != null) {
/*  26 */         vp.add(p);
/*  27 */         buildingp.add(plantation.getPos());
/*     */       }
/*     */     }
/*     */     
/*  31 */     if (vp.isEmpty()) {
/*  32 */       return null;
/*     */     }
/*     */     
/*  35 */     Point p = (Point)vp.get(0);
/*  36 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  38 */     for (int i = 1; i < vp.size(); i++) {
/*  39 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  40 */         p = (Point)vp.get(i);
/*  41 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  44 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  49 */     return sugarcane;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  55 */     int nbsimultaneous = 0;
/*  56 */     for (MillVillager v : villager.getTownHall().villagers) {
/*  57 */       if ((v != villager) && (this.key.equals(v.goalKey))) {
/*  58 */         nbsimultaneous++;
/*     */       }
/*     */     }
/*  61 */     if (nbsimultaneous > 2) {
/*  62 */       return false;
/*     */     }
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  66 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  67 */       delayOver = true;
/*     */     } else {
/*  69 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  72 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("sugarplantation")) {
/*  73 */       int nb = kiln.getResManager().getNbSugarCanePlantingLocation();
/*     */       
/*  75 */       if ((nb > 0) && (delayOver)) {
/*  76 */         return true;
/*     */       }
/*  78 */       if (nb > 4) {
/*  79 */         return true;
/*     */       }
/*     */     }
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  93 */     Block block = villager.getBlock(villager.getGoalDestPoint());
/*     */     
/*  95 */     Point cropPoint = villager.getGoalDestPoint().getAbove();
/*     */     
/*  97 */     block = villager.getBlock(cropPoint);
/*  98 */     if ((block == Blocks.field_150350_a) || (block == Blocks.field_150362_t)) {
/*  99 */       villager.setBlock(cropPoint, Blocks.field_150436_aH);
/*     */       
/* 101 */       villager.func_71038_i();
/*     */     }
/*     */     
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 109 */     int p = 120;
/*     */     
/* 111 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 112 */       if (this.key.equals(v.goalKey)) {
/* 113 */         p /= 2;
/*     */       }
/*     */     }
/*     */     
/* 117 */     return p;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalIndianPlantSugarCane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
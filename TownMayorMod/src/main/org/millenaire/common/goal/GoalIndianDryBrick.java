/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class GoalIndianDryBrick extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  19 */     return 1000;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  25 */     List<Point> vp = new ArrayList();
/*  26 */     List<Point> buildingp = new ArrayList();
/*  27 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("brickkiln")) {
/*  28 */       Point p = kiln.getResManager().getEmptyBrickLocation();
/*  29 */       if (p != null) {
/*  30 */         vp.add(p);
/*  31 */         buildingp.add(kiln.getPos());
/*     */       }
/*     */     }
/*     */     
/*  35 */     if (vp.isEmpty()) {
/*  36 */       return null;
/*     */     }
/*     */     
/*  39 */     Point p = (Point)vp.get(0);
/*  40 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  42 */     for (int i = 1; i < vp.size(); i++) {
/*  43 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  44 */         p = (Point)vp.get(i);
/*  45 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  48 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  53 */     return new ItemStack[] { new ItemStack(Mill.brickmould, 1, 0) };
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  59 */     int nbsimultaneous = 0;
/*  60 */     for (MillVillager v : villager.getTownHall().villagers) {
/*  61 */       if ((v != villager) && (this.key.equals(v.goalKey))) {
/*  62 */         nbsimultaneous++;
/*     */       }
/*     */     }
/*  65 */     if (nbsimultaneous > 2) {
/*  66 */       return false;
/*     */     }
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  70 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  71 */       delayOver = true;
/*     */     } else {
/*  73 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  76 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("brickkiln")) {
/*  77 */       int nb = kiln.getResManager().getNbEmptyBrickLocation();
/*     */       
/*  79 */       if ((nb > 0) && (delayOver)) {
/*  80 */         return true;
/*     */       }
/*  82 */       if (nb > 4) {
/*  83 */         return true;
/*     */       }
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  96 */     if (MillCommonUtilities.getBlock(villager.field_70170_p, villager.getGoalDestPoint()) == net.minecraft.init.Blocks.field_150350_a) {
/*  97 */       villager.setBlockAndMetadata(villager.getGoalDestPoint(), Mill.earth_decoration, 0);
/*     */       
/*  99 */       villager.func_71038_i();
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 106 */     int p = 120;
/*     */     
/* 108 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 109 */       if (this.key.equals(v.goalKey)) {
/* 110 */         p /= 2;
/*     */       }
/*     */     }
/*     */     
/* 114 */     return p;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalIndianDryBrick.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
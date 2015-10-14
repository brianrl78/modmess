/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalLumbermanPlantSaplings extends Goal
/*     */ {
/*     */   public GoalLumbermanPlantSaplings()
/*     */   {
/*  20 */     this.maxSimultaneousInBuilding = 1;
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  25 */     return 1000;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  31 */     List<Point> vp = new ArrayList();
/*  32 */     List<Point> buildingp = new ArrayList();
/*  33 */     for (Building grove : villager.getTownHall().getBuildingsWithTag("grove")) {
/*  34 */       Point p = grove.getResManager().getPlantingLocation();
/*  35 */       if (p != null) {
/*  36 */         vp.add(p);
/*  37 */         buildingp.add(grove.getPos());
/*     */       }
/*     */     }
/*     */     
/*  41 */     if (vp.isEmpty()) {
/*  42 */       return null;
/*     */     }
/*     */     
/*  45 */     Point p = (Point)vp.get(0);
/*  46 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  48 */     for (int i = 1; i < vp.size(); i++) {
/*  49 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  50 */         p = (Point)vp.get(i);
/*  51 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  54 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  60 */     int meta = 0;
/*  61 */     if (villager.takeFromInv(Blocks.field_150345_g, 0, 1) == 1) {
/*  62 */       meta = 0;
/*  63 */     } else if (villager.takeFromInv(Blocks.field_150345_g, 1, 1) == 1) {
/*  64 */       meta = 1;
/*  65 */     } else if (villager.takeFromInv(Blocks.field_150345_g, 2, 1) == 1) {
/*  66 */       meta = 2;
/*  67 */     } else if (villager.takeFromInv(Blocks.field_150345_g, 3, 1) == 1) {
/*  68 */       meta = 3;
/*     */     }
/*     */     
/*  71 */     return new ItemStack[] { new ItemStack(Blocks.field_150345_g, 1, meta) };
/*     */   }
/*     */   
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/*  76 */     return JPS_CONFIG_WIDE;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  82 */     for (Building grove : villager.getTownHall().getBuildingsWithTag("grove")) {
/*  83 */       Point p = grove.getResManager().getPlantingLocation();
/*  84 */       if (p != null) {
/*  85 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  94 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/* 100 */     Block block = MillCommonUtilities.getBlock(villager.field_70170_p, villager.getGoalDestPoint());
/* 101 */     if ((block == Blocks.field_150350_a) || (block == Blocks.field_150433_aE))
/*     */     {
/* 103 */       int metaStart = MillCommonUtilities.randomInt(4);
/* 104 */       int chosenMeta = -1;
/*     */       
/* 106 */       for (int i = metaStart; (i < 4) && (chosenMeta == -1); i++) {
/* 107 */         if (villager.takeFromInv(Blocks.field_150345_g, i, 1) == 1) {
/* 108 */           chosenMeta = i;
/*     */         }
/*     */       }
/*     */       
/* 112 */       for (int i = 0; (i < metaStart) && (chosenMeta == -1); i++) {
/* 113 */         if (villager.takeFromInv(Blocks.field_150345_g, i, 1) == 1) {
/* 114 */           chosenMeta = i;
/*     */         }
/*     */       }
/*     */       
/* 118 */       if (chosenMeta == -1) {
/* 119 */         chosenMeta = 0;
/*     */       }
/*     */       
/* 122 */       villager.setBlockAndMetadata(villager.getGoalDestPoint(), Blocks.field_150345_g, chosenMeta);
/*     */       
/* 124 */       villager.func_71038_i();
/*     */       
/* 126 */       if ((MLN.LogLumberman >= 3) && (villager.extraLog)) {
/* 127 */         MLN.debug(this, "Planted at: " + villager.getGoalDestPoint());
/*     */       }
/* 129 */     } else if ((MLN.LogLumberman >= 3) && (villager.extraLog)) {
/* 130 */       MLN.debug(this, "Failed to plant at: " + villager.getGoalDestPoint());
/*     */     }
/*     */     
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 138 */     return 120;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 143 */     return 5;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalLumbermanPlantSaplings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
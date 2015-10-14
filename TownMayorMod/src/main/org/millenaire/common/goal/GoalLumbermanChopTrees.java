/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorldInfo;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalLumbermanChopTrees extends Goal
/*     */ {
/*     */   public GoalLumbermanChopTrees()
/*     */   {
/*  24 */     this.maxSimultaneousInBuilding = 1;
/*     */     try {
/*  26 */       this.townhallLimit.put(new org.millenaire.common.InvItem(Blocks.field_150364_r, -1), Integer.valueOf(4096));
/*     */     } catch (MLN.MillenaireException e) {
/*  28 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  34 */     int toolEfficiency = (int)villager.getBestAxe().getDigSpeed(new ItemStack(villager.getBestAxe(), 1), Blocks.field_150364_r, 0);
/*     */     
/*  36 */     return 1000 - toolEfficiency * 40;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  42 */     List<Point> vp = new ArrayList();
/*  43 */     List<Point> buildingp = new ArrayList();
/*  44 */     for (Building grove : villager.getTownHall().getBuildingsWithTag("grove")) {
/*  45 */       if (grove.getWoodCount() > 4) {
/*  46 */         Point p = grove.getWoodLocation();
/*  47 */         if (p != null) {
/*  48 */           vp.add(p);
/*  49 */           buildingp.add(grove.getPos());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  54 */     if (vp.isEmpty()) {
/*  55 */       return null;
/*     */     }
/*     */     
/*  58 */     Point p = (Point)vp.get(0);
/*  59 */     Point buildingP = (Point)buildingp.get(0);
/*  60 */     for (int i = 1; i < vp.size(); i++) {
/*  61 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  62 */         p = (Point)vp.get(i);
/*  63 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  66 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  71 */     return villager.getBestAxeStack();
/*     */   }
/*     */   
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/*  76 */     return JPS_CONFIG_CHOPLUMBER;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  82 */     if (villager.countInv(Blocks.field_150364_r, -1) > 64) {
/*  83 */       return false;
/*     */     }
/*     */     
/*  86 */     if (getDestination(villager) == null) {
/*  87 */       return false;
/*     */     }
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  94 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 100 */     boolean woodFound = false;
/*     */     
/* 102 */     if (MLN.LogLumberman >= 3) {
/* 103 */       MLN.debug(this, "Attempting to gather wood at: " + villager.getGoalDestPoint());
/*     */     }
/*     */     
/* 106 */     MillWorldInfo winfo = villager.getTownHall().winfo;
/*     */     
/* 108 */     for (int i = 12; i > -12; i--) {
/* 109 */       for (int j = -3; j < 4; j++) {
/* 110 */         for (int k = -3; k < 4; k++) {
/* 111 */           Point p = villager.getGoalDestPoint().getRelative(j, i, k);
/*     */           
/* 113 */           if (!winfo.isConstructionOrLoggingForbiddenHere(p))
/*     */           {
/* 115 */             Block block = villager.getBlock(p);
/*     */             
/* 117 */             if ((block == Blocks.field_150364_r) || (block == Blocks.field_150362_t)) {
/* 118 */               if (!woodFound) {
/* 119 */                 if (block == Blocks.field_150364_r) {
/* 120 */                   int meta = villager.getBlockMeta(p) & 0x3;
/* 121 */                   villager.setBlock(p, Blocks.field_150350_a);
/*     */                   
/* 123 */                   villager.func_71038_i();
/*     */                   
/* 125 */                   villager.addToInv(Blocks.field_150364_r, meta, 1);
/* 126 */                   woodFound = true;
/*     */                   
/* 128 */                   if (MLN.LogLumberman >= 3) {
/* 129 */                     MLN.debug(this, "Gathered wood at: " + villager.getGoalDestPoint());
/*     */                   }
/*     */                 }
/*     */                 else {
/* 133 */                   if (MillCommonUtilities.randomInt(4) == 0) {
/* 134 */                     villager.addToInv(Blocks.field_150345_g, MillCommonUtilities.getBlockMeta(villager.field_70170_p, p) & 0x3, 1);
/*     */                   }
/* 136 */                   villager.setBlock(p, Blocks.field_150350_a);
/*     */                   
/* 138 */                   villager.func_71038_i();
/*     */                   
/* 140 */                   if ((villager.gathersApples()) && (MillCommonUtilities.chanceOn(16))) {
/* 141 */                     villager.addToInv(Mill.ciderapple, 1);
/*     */                   }
/*     */                   
/* 144 */                   if ((MLN.LogLumberman >= 3) && (villager.extraLog)) {
/* 145 */                     MLN.debug(this, "Destroyed leaves at: " + villager.getGoalDestPoint());
/*     */                   }
/*     */                 }
/*     */               } else {
/* 149 */                 if ((MLN.LogLumberman >= 3) && (villager.extraLog)) {
/* 150 */                   MLN.debug(this, "More wood found.");
/*     */                 }
/* 152 */                 return false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 160 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 165 */     return Math.max(10, 125 - villager.countInv(Blocks.field_150364_r, -1));
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 170 */     return ACTIVATION_RANGE + 2;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalLumbermanChopTrees.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
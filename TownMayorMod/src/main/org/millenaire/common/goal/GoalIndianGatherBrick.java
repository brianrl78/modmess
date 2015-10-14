/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class GoalIndianGatherBrick extends Goal
/*     */ {
/*     */   public GoalIndianGatherBrick()
/*     */   {
/*  21 */     this.maxSimultaneousInBuilding = 2;
/*     */     try {
/*  23 */       this.townhallLimit.put(new InvItem(Mill.stone_decoration, 1), Integer.valueOf(4096));
/*     */     } catch (MLN.MillenaireException e) {
/*  25 */       org.millenaire.common.MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  31 */     return 1000;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  37 */     List<Point> vp = new ArrayList();
/*  38 */     List<Point> buildingp = new ArrayList();
/*  39 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("brickkiln")) {
/*  40 */       Point p = kiln.getResManager().getFullBrickLocation();
/*  41 */       if (p != null) {
/*  42 */         vp.add(p);
/*  43 */         buildingp.add(kiln.getPos());
/*     */       }
/*     */     }
/*     */     
/*  47 */     if (vp.isEmpty()) {
/*  48 */       return null;
/*     */     }
/*     */     
/*  51 */     Point p = (Point)vp.get(0);
/*  52 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  54 */     for (int i = 1; i < vp.size(); i++) {
/*  55 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  56 */         p = (Point)vp.get(i);
/*  57 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  60 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  65 */     return villager.getBestPickaxeStack();
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager) {
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  71 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  72 */       delayOver = true;
/*     */     } else {
/*  74 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  77 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("brickkiln")) {
/*  78 */       int nb = kiln.getResManager().getNbFullBrickLocation();
/*     */       
/*  80 */       if ((nb > 0) && (delayOver)) {
/*  81 */         return true;
/*     */       }
/*  83 */       if (nb > 4) {
/*  84 */         return true;
/*     */       }
/*     */     }
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/*  97 */     if ((MillCommonUtilities.getBlock(villager.field_70170_p, villager.getGoalDestPoint()) == Mill.stone_decoration) && (MillCommonUtilities.getBlockMeta(villager.field_70170_p, villager.getGoalDestPoint()) == 1))
/*     */     {
/*  99 */       villager.addToInv(Mill.stone_decoration, MillCommonUtilities.getBlockMeta(villager.field_70170_p, villager.getGoalDestPoint()), 1);
/* 100 */       villager.setBlockAndMetadata(villager.getGoalDestPoint(), net.minecraft.init.Blocks.field_150350_a, 0);
/*     */       
/* 102 */       villager.func_71038_i();
/* 103 */       return false;
/*     */     }
/* 105 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 113 */     int p = 100 - villager.getTownHall().nbGoodAvailable(Mill.stone_decoration, 1, false, false) * 2;
/*     */     
/* 115 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 116 */       if (this.key.equals(v.goalKey)) {
/* 117 */         p /= 2;
/*     */       }
/*     */     }
/*     */     
/* 121 */     return p;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalIndianGatherBrick.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
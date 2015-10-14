/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalClearOldPath extends Goal
/*     */ {
/*     */   public GoalClearOldPath()
/*     */   {
/*  18 */     this.maxSimultaneousTotal = 1;
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  23 */     int toolEfficiency = (int)villager.getBestShovel().getDigSpeed(new ItemStack(villager.getBestShovel(), 1), Blocks.field_150346_d, 0);
/*     */     
/*  25 */     return 100 - toolEfficiency * 5;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  31 */     Point p = villager.getTownHall().getCurrentClearPathPoint();
/*     */     
/*  33 */     if (p != null) {
/*  34 */       return packDest(p);
/*     */     }
/*     */     
/*  37 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  42 */     return villager.getBestShovelStack();
/*     */   }
/*     */   
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/*  47 */     return JPS_CONFIG_BUILDING;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  52 */     return (MLN.BuildVillagePaths) && (villager.getTownHall().getCurrentClearPathPoint() != null);
/*     */   }
/*     */   
/*     */   public boolean isStillValidSpecific(MillVillager villager) throws Exception
/*     */   {
/*  57 */     return villager.getTownHall().getCurrentClearPathPoint() != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  68 */     Point p = villager.getTownHall().getCurrentClearPathPoint();
/*     */     
/*  70 */     if (p == null) {
/*  71 */       return true;
/*     */     }
/*     */     
/*  74 */     if (MLN.LogVillagePaths >= 3) {
/*  75 */       MLN.debug(villager, "Clearing old path block: " + p);
/*     */     }
/*     */     
/*  78 */     Block block = p.getBlock(villager.field_70170_p);
/*  79 */     int meta = p.getMeta(villager.field_70170_p);
/*     */     
/*  81 */     if (meta < 8) {
/*  82 */       if (block == Mill.pathSlab) {
/*  83 */         p.setBlock(villager.field_70170_p, Blocks.field_150350_a, 0, true, false);
/*  84 */       } else if (block == Mill.path)
/*     */       {
/*  86 */         Block blockBelow = p.getBelow().getBlock(villager.field_70170_p);
/*  87 */         if (MillCommonUtilities.getBlockIdValidGround(blockBelow, true) != null) {
/*  88 */           p.setBlock(villager.field_70170_p, MillCommonUtilities.getBlockIdValidGround(blockBelow, true), 0, true, false);
/*     */         } else {
/*  90 */           p.setBlock(villager.field_70170_p, Blocks.field_150346_d, 0, true, false);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  95 */     villager.getTownHall().oldPathPointsToClearIndex += 1;
/*     */     
/*  97 */     p = villager.getTownHall().getCurrentClearPathPoint();
/*     */     
/*  99 */     villager.func_71038_i();
/*     */     
/* 101 */     if (p != null) {
/* 102 */       villager.setGoalDestPoint(p);
/* 103 */       return false;
/*     */     }
/* 105 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 111 */     return 40;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 116 */     return ACTIVATION_RANGE + 2;
/*     */   }
/*     */   
/*     */   public boolean stopMovingWhileWorking()
/*     */   {
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 127 */     performAction(villager);
/*     */     
/* 129 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalClearOldPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
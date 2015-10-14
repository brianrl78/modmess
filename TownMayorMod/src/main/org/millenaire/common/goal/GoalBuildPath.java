/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingBlock;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalBuildPath extends Goal
/*     */ {
/*     */   public GoalBuildPath()
/*     */   {
/*  16 */     this.maxSimultaneousTotal = 1;
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  21 */     int toolEfficiency = (int)villager.getBestShovel().getDigSpeed(new ItemStack(villager.getBestShovel(), 1), Blocks.field_150346_d, 0);
/*     */     
/*  23 */     return 100 - toolEfficiency * 5;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  29 */     BuildingBlock b = villager.getTownHall().getCurrentPathBuildingBlock();
/*     */     
/*  31 */     if (b != null) {
/*  32 */       return packDest(b.p);
/*     */     }
/*     */     
/*  35 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  41 */     BuildingBlock bblock = villager.getTownHall().getCurrentPathBuildingBlock();
/*     */     
/*  43 */     if (bblock != null) {
/*  44 */       if (bblock.block == Blocks.field_150350_a) {
/*  45 */         return villager.getBestShovelStack();
/*     */       }
/*  47 */       return new ItemStack[] { new ItemStack(Item.func_150898_a(bblock.block), 1, bblock.meta) };
/*     */     }
/*     */     
/*  50 */     return villager.getBestShovelStack();
/*     */   }
/*     */   
/*     */ 
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/*  56 */     return JPS_CONFIG_BUILDING;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  61 */     return (MLN.BuildVillagePaths) && (villager.getTownHall().getCurrentPathBuildingBlock() != null);
/*     */   }
/*     */   
/*     */   public boolean isStillValidSpecific(MillVillager villager) throws Exception
/*     */   {
/*  66 */     return villager.getTownHall().getCurrentPathBuildingBlock() != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  77 */     BuildingBlock bblock = villager.getTownHall().getCurrentPathBuildingBlock();
/*     */     
/*  79 */     if (bblock == null) {
/*  80 */       return true;
/*     */     }
/*     */     
/*  83 */     if (MLN.LogVillagePaths >= 3) {
/*  84 */       MLN.debug(villager, "Building path block: " + bblock);
/*     */     }
/*     */     
/*  87 */     bblock.pathBuild(villager.getTownHall());
/*     */     
/*  89 */     villager.getTownHall().pathsToBuildPathIndex += 1;
/*     */     
/*  91 */     BuildingBlock b = villager.getTownHall().getCurrentPathBuildingBlock();
/*     */     
/*  93 */     villager.func_71038_i();
/*     */     
/*  95 */     if (b != null) {
/*  96 */       villager.setGoalDestPoint(b.p);
/*  97 */       return false;
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 105 */     return 50;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 110 */     return ACTIVATION_RANGE + 2;
/*     */   }
/*     */   
/*     */   public boolean stopMovingWhileWorking()
/*     */   {
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 121 */     performAction(villager);
/*     */     
/* 123 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBuildPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
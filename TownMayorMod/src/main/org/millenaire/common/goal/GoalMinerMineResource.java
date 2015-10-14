/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*     */ 
/*     */ public class GoalMinerMineResource extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  22 */     Block block = villager.getBlock(villager.getGoalDestPoint());
/*     */     
/*  24 */     if ((block == Blocks.field_150348_b) || (block == Blocks.field_150322_A)) {
/*  25 */       int toolEfficiency = (int)villager.getBestPickaxe().getDigSpeed(new ItemStack(villager.getBestPickaxe(), 1), Blocks.field_150322_A, 0);
/*     */       
/*  27 */       return 7000 - 200 * toolEfficiency;
/*     */     }
/*  29 */     if ((block == Blocks.field_150354_m) || (block == Blocks.field_150435_aG) || (block == Blocks.field_150351_n)) {
/*  30 */       int toolEfficiency = (int)villager.getBestShovel().getDigSpeed(new ItemStack(villager.getBestShovel(), 1), Blocks.field_150354_m, 0);
/*     */       
/*  32 */       return 7000 - 200 * toolEfficiency;
/*     */     }
/*  34 */     return 0;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  40 */     List<List<Point>> sources = villager.getHouse().getResManager().sources;
/*     */     
/*  42 */     List<Point> validSources = new ArrayList();
/*     */     
/*  44 */     for (int i = 0; i < sources.size(); i++) {
/*  45 */       for (int j = 0; j < ((List)sources.get(i)).size(); j++) {
/*  46 */         Block block = villager.getBlock((Point)((List)sources.get(i)).get(j));
/*     */         
/*  48 */         if ((block == Blocks.field_150348_b) || (block == Blocks.field_150322_A) || (block == Blocks.field_150354_m) || (block == Blocks.field_150435_aG) || (block == Blocks.field_150351_n)) {
/*  49 */           validSources.add(((List)sources.get(i)).get(j));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  55 */     if (validSources.isEmpty()) {
/*  56 */       return null;
/*     */     }
/*     */     
/*  59 */     return packDest((Point)validSources.get(MillCommonUtilities.randomInt(validSources.size())), villager.getHouse());
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  65 */     if ((villager.getBlock(villager.getGoalDestPoint()) == Blocks.field_150354_m) || (villager.getBlock(villager.getGoalDestPoint()) == Blocks.field_150435_aG) || (villager.getBlock(villager.getGoalDestPoint()) == Blocks.field_150351_n))
/*     */     {
/*  67 */       return villager.getBestShovelStack();
/*     */     }
/*     */     
/*  70 */     return villager.getBestPickaxeStack();
/*     */   }
/*     */   
/*     */   public AStarConfig getPathingConfig()
/*     */   {
/*  75 */     return JPS_CONFIG_WIDE;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/*  80 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  91 */     Block block = villager.getBlock(villager.getGoalDestPoint());
/*     */     
/*  93 */     if (block == Blocks.field_150354_m) {
/*  94 */       villager.addToInv(Blocks.field_150354_m, 1);
/*     */       
/*  96 */       MillCommonUtilities.playSoundBlockBreaking(villager.field_70170_p, villager.getGoalDestPoint(), Blocks.field_150354_m, 4.0F);
/*     */       
/*  98 */       if ((MLN.LogMiner >= 3) && (villager.extraLog)) {
/*  99 */         MLN.debug(this, "Gathered sand at: " + villager.getGoalDestPoint());
/*     */       }
/* 101 */     } else if (block == Blocks.field_150348_b) {
/* 102 */       villager.addToInv(Blocks.field_150347_e, 1);
/*     */       
/* 104 */       MillCommonUtilities.playSoundBlockBreaking(villager.field_70170_p, villager.getGoalDestPoint(), Blocks.field_150348_b, 4.0F);
/*     */       
/* 106 */       if ((MLN.LogMiner >= 3) && (villager.extraLog)) {
/* 107 */         MLN.debug(this, "Gather cobblestone at: " + villager.getGoalDestPoint());
/*     */       }
/* 109 */     } else if (block == Blocks.field_150322_A) {
/* 110 */       villager.addToInv(Blocks.field_150322_A, 1);
/*     */       
/* 112 */       MillCommonUtilities.playSoundBlockBreaking(villager.field_70170_p, villager.getGoalDestPoint(), Blocks.field_150322_A, 4.0F);
/*     */       
/* 114 */       if ((MLN.LogMiner >= 3) && (villager.extraLog)) {
/* 115 */         MLN.debug(this, "Gather sand stone at: " + villager.getGoalDestPoint());
/*     */       }
/* 117 */     } else if (block == Blocks.field_150435_aG) {
/* 118 */       villager.addToInv(Items.field_151119_aD, 1);
/*     */       
/* 120 */       MillCommonUtilities.playSoundBlockBreaking(villager.field_70170_p, villager.getGoalDestPoint(), Blocks.field_150435_aG, 4.0F);
/*     */       
/* 122 */       if ((MLN.LogMiner >= 3) && (villager.extraLog)) {
/* 123 */         MLN.debug(this, "Gather clay at: " + villager.getGoalDestPoint());
/*     */       }
/* 125 */     } else if (block == Blocks.field_150351_n) {
/* 126 */       villager.addToInv(Blocks.field_150351_n, 1);
/*     */       
/* 128 */       MillCommonUtilities.playSoundBlockBreaking(villager.field_70170_p, villager.getGoalDestPoint(), Blocks.field_150351_n, 4.0F);
/*     */       
/* 130 */       if ((MLN.LogMiner >= 3) && (villager.extraLog)) {
/* 131 */         MLN.debug(this, "Gather gravel at: " + villager.getGoalDestPoint());
/*     */       }
/*     */     }
/*     */     
/* 135 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 140 */     return 30;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 145 */     return 5;
/*     */   }
/*     */   
/*     */   public boolean stuckAction(MillVillager villager) throws Exception
/*     */   {
/* 150 */     return performAction(villager);
/*     */   }
/*     */   
/*     */   public boolean swingArms()
/*     */   {
/* 155 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalMinerMineResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
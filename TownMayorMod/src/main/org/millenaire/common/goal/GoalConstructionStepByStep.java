/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingBlock;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class GoalConstructionStepByStep extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  22 */     BuildingBlock bblock = villager.getTownHall().getCurrentBlock();
/*     */     
/*  24 */     if (bblock == null) {
/*  25 */       return 0;
/*     */     }
/*     */     
/*  28 */     int toolEfficiency = (int)villager.getBestShovel().getDigSpeed(new ItemStack(villager.getBestShovel(), 1), Blocks.field_150346_d, 0);
/*     */     
/*  30 */     if ((bblock.block == Blocks.field_150350_a) || (bblock.block == Blocks.field_150346_d)) {
/*  31 */       return 100 - toolEfficiency * 5;
/*     */     }
/*     */     
/*  34 */     return 500 - toolEfficiency * 20;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  40 */     BuildingBlock bblock = villager.getTownHall().getCurrentBlock();
/*     */     
/*  42 */     if (bblock == null) {
/*  43 */       return null;
/*     */     }
/*     */     
/*  46 */     return packDest(bblock.p);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  52 */     BuildingBlock bblock = villager.getTownHall().getCurrentBlock();
/*     */     
/*  54 */     if (bblock != null) {
/*  55 */       if ((bblock.block == Blocks.field_150350_a) || (net.minecraft.item.Item.func_150898_a(bblock.block) == null)) {
/*  56 */         return villager.getBestShovelStack();
/*     */       }
/*  58 */       return new ItemStack[] { new ItemStack(bblock.block, 1, bblock.meta) };
/*     */     }
/*     */     
/*  61 */     return villager.getBestShovelStack();
/*     */   }
/*     */   
/*     */ 
/*     */   public org.millenaire.common.pathing.atomicstryker.AStarConfig getPathingConfig()
/*     */   {
/*  67 */     return JPS_CONFIG_BUILDING;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  72 */     if ((villager.getTownHall().builder != null) || (villager.getTownHall().buildingLocationIP == null) || (villager.getTownHall().getBblocks() == null)) {
/*  73 */       return false;
/*     */     }
/*     */     
/*  76 */     for (MillVillager v : villager.getTownHall().villagers) {
/*  77 */       if ((Goal.getResourcesForBuild.key.equals(v.goalKey)) || (Goal.construction.key.equals(v.goalKey))) {
/*  78 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  82 */     for (InvItem key : villager.getTownHall().getCurrentBuildingPlan().resCost.keySet()) {
/*  83 */       if (villager.countInv(key) < ((Integer)villager.getTownHall().getCurrentBuildingPlan().resCost.get(key)).intValue()) {
/*  84 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isStillValidSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  94 */     if ((villager.getTownHall().builder != null) && (villager.getTownHall().builder != villager)) {
/*  95 */       return false;
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public void onAccept(MillVillager villager)
/*     */   {
/* 107 */     villager.getTownHall().builder = villager;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 113 */     BuildingBlock bblock = villager.getTownHall().getCurrentBlock();
/*     */     
/* 115 */     if (bblock == null) {
/* 116 */       return true;
/*     */     }
/*     */     
/* 119 */     if (MLN.LogWifeAI >= 2) {
/* 120 */       MLN.minor(villager, "Setting block at " + bblock.p + " type: " + bblock.block + " replacing: " + villager.getBlock(bblock.p) + " distance: " + bblock.p.distanceTo(villager));
/*     */     }
/*     */     
/* 123 */     if ((bblock.p.horizontalDistanceTo(villager) < 1.0D) && (bblock.p.getiY() > villager.field_70163_u) && (bblock.p.getiY() < villager.field_70163_u + 2.0D)) {
/* 124 */       boolean jumped = false;
/* 125 */       World world = villager.field_70170_p;
/* 126 */       if ((!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX() + 1, villager.getPos().getiY() + 1, villager.getPos().getiZ())) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX() + 1, villager.getPos().getiY() + 2, villager.getPos().getiZ())))
/*     */       {
/*     */ 
/* 129 */         villager.func_70107_b(villager.getPos().getiX() + 1, villager.getPos().getiY() + 1, villager.getPos().getiZ());
/* 130 */         jumped = true;
/*     */       }
/* 132 */       if ((!jumped) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX() - 1, villager.getPos().getiY() + 1, villager.getPos().getiZ())) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX() - 1, villager.getPos().getiY() + 2, villager.getPos().getiZ())))
/*     */       {
/*     */ 
/* 135 */         villager.func_70107_b(villager.getPos().getiX() - 1, villager.getPos().getiY() + 1, villager.getPos().getiZ());
/* 136 */         jumped = true;
/*     */       }
/* 138 */       if ((!jumped) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX(), villager.getPos().getiY(), villager.getPos().getiZ() + 1)) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX(), villager.getPos().getiY() + 2, villager.getPos().getiZ() + 1)))
/*     */       {
/*     */ 
/* 141 */         villager.func_70107_b(villager.getPos().getiX(), villager.getPos().getiY() + 1, villager.getPos().getiZ() + 1);
/* 142 */         jumped = true;
/*     */       }
/* 144 */       if ((!jumped) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX(), villager.getPos().getiY() + 1, villager.getPos().getiZ() - 1)) && (!MillCommonUtilities.isBlockOpaqueCube(world, villager.getPos().getiX(), villager.getPos().getiY() + 2, villager.getPos().getiZ() - 1)))
/*     */       {
/*     */ 
/* 147 */         villager.func_70107_b(villager.getPos().getiX(), villager.getPos().getiY() + 1, villager.getPos().getiZ() - 1);
/* 148 */         jumped = true;
/*     */       }
/* 150 */       if ((!jumped) && (MLN.LogWifeAI >= 1)) {
/* 151 */         MLN.major(villager, "Tried jumping in construction but couldn't");
/*     */       }
/*     */     }
/*     */     
/* 155 */     bblock.build(villager.field_70170_p, false, false);
/*     */     
/* 157 */     villager.func_71038_i();
/*     */     
/* 159 */     boolean foundNextBlock = false;
/*     */     
/* 161 */     while ((!foundNextBlock) && (villager.getTownHall().areBlocksLeft())) {
/* 162 */       villager.getTownHall().incrementBblockPos();
/*     */       
/* 164 */       BuildingBlock bb = villager.getTownHall().getCurrentBlock();
/* 165 */       if ((bb != null) && (!bb.alreadyDone(villager.field_70170_p))) {
/* 166 */         villager.setGoalDestPoint(bb.p);
/* 167 */         foundNextBlock = true;
/*     */       }
/*     */     }
/*     */     
/* 171 */     if (!villager.getTownHall().areBlocksLeft()) {
/* 172 */       if (MLN.LogBuildingPlan >= 1) {
/* 173 */         MLN.major(this, "Villager " + villager + " laid last block in " + villager.getTownHall().buildingLocationIP.planKey + " at " + bblock.p);
/*     */       }
/* 175 */       villager.getTownHall().setBblocks(null);
/* 176 */       BuildingPlan plan = villager.getTownHall().getCurrentBuildingPlan();
/*     */       
/* 178 */       for (InvItem key : plan.resCost.keySet()) {
/* 179 */         villager.takeFromInv(key.getItem(), key.meta, ((Integer)plan.resCost.get(key)).intValue());
/*     */       }
/*     */       
/* 182 */       if ((villager.getTownHall().buildingLocationIP != null) && (villager.getTownHall().buildingLocationIP.level == 0)) {
/* 183 */         villager.getTownHall().initialiseCurrentConstruction(bblock.p);
/*     */       }
/*     */     }
/*     */     
/* 187 */     if (!foundNextBlock) {
/* 188 */       villager.setGoalDestPoint(null);
/*     */     }
/*     */     
/* 191 */     if ((MLN.LogWifeAI >= 2) && (villager.extraLog)) {
/* 192 */       MLN.minor(villager, "Reseting actionStart after " + (System.currentTimeMillis() - villager.actionStart));
/*     */     }
/*     */     
/* 195 */     villager.actionStart = 0L;
/*     */     
/* 197 */     return !villager.getTownHall().areBlocksLeft();
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 202 */     return 150;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 207 */     return ACTIVATION_RANGE + 2;
/*     */   }
/*     */   
/*     */   public boolean stopMovingWhileWorking()
/*     */   {
/* 212 */     return false;
/*     */   }
/*     */   
/*     */   public boolean stuckAction(MillVillager villager) throws MLN.MillenaireException
/*     */   {
/* 217 */     if (villager.getGoalDestPoint().distanceTo(villager) < 30.0D) {
/* 218 */       if (MLN.LogWifeAI >= 2) {
/* 219 */         MLN.major(villager, "Putting block at a distance: " + villager.getGoalDestPoint().distanceTo(villager));
/*     */       }
/* 221 */       performAction(villager);
/*     */       
/* 223 */       return true;
/*     */     }
/* 225 */     return false;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 231 */     performAction(villager);
/*     */     
/* 233 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalConstructionStepByStep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
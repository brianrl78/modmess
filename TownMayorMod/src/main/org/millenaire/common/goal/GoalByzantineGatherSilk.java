/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class GoalByzantineGatherSilk extends Goal
/*     */ {
/*  20 */   private static ItemStack[] shears = { new ItemStack(net.minecraft.init.Items.field_151097_aZ, 1) };
/*     */   
/*     */   public GoalByzantineGatherSilk() {
/*  23 */     this.maxSimultaneousInBuilding = 2;
/*     */     try {
/*  25 */       this.buildingLimit.put(new InvItem(Mill.silk), Integer.valueOf(128));
/*  26 */       this.townhallLimit.put(new InvItem(Mill.silk), Integer.valueOf(128));
/*     */     } catch (MLN.MillenaireException e) {
/*  28 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  35 */     return 1000;
/*     */   }
/*     */   
/*     */ 
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  41 */     List<Point> vp = new ArrayList();
/*  42 */     List<Point> buildingp = new ArrayList();
/*  43 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("silkwormfarm")) {
/*  44 */       Point p = kiln.getResManager().getSilkwormHarvestLocation();
/*  45 */       if (p != null) {
/*  46 */         vp.add(p);
/*  47 */         buildingp.add(kiln.getPos());
/*     */       }
/*     */     }
/*     */     
/*  51 */     if (vp.isEmpty()) {
/*  52 */       return null;
/*     */     }
/*     */     
/*  55 */     Point p = (Point)vp.get(0);
/*  56 */     Point buildingP = (Point)buildingp.get(0);
/*     */     
/*  58 */     for (int i = 1; i < vp.size(); i++) {
/*  59 */       if (((Point)vp.get(i)).horizontalDistanceToSquared(villager) < p.horizontalDistanceToSquared(villager)) {
/*  60 */         p = (Point)vp.get(i);
/*  61 */         buildingP = (Point)buildingp.get(i);
/*     */       }
/*     */     }
/*  64 */     return packDest(p, buildingP);
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */   {
/*  69 */     return shears;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*     */     boolean delayOver;
/*     */     boolean delayOver;
/*  76 */     if (!villager.lastGoalTime.containsKey(this)) {
/*  77 */       delayOver = true;
/*     */     } else {
/*  79 */       delayOver = villager.field_70170_p.func_72820_D() > ((Long)villager.lastGoalTime.get(this)).longValue() + 2000L;
/*     */     }
/*     */     
/*  82 */     for (Building kiln : villager.getTownHall().getBuildingsWithTag("silkwormfarm")) {
/*  83 */       int nb = kiln.getResManager().getNbSilkWormHarvestLocation();
/*     */       
/*  85 */       if ((nb > 0) && (delayOver)) {
/*  86 */         return true;
/*     */       }
/*  88 */       if (nb > 4) {
/*  89 */         return true;
/*     */       }
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/* 102 */     if ((MillCommonUtilities.getBlock(villager.field_70170_p, villager.getGoalDestPoint()) == Mill.wood_decoration) && (MillCommonUtilities.getBlockMeta(villager.field_70170_p, villager.getGoalDestPoint()) == 4))
/*     */     {
/* 104 */       villager.addToInv(Mill.silk, 0, 1);
/* 105 */       villager.setBlockAndMetadata(villager.getGoalDestPoint(), Mill.wood_decoration, 3);
/*     */       
/* 107 */       villager.func_71038_i();
/*     */       
/* 109 */       return false;
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int priority(MillVillager villager)
/*     */   {
/*     */     int p;
/*     */     try
/*     */     {
/* 120 */       p = 100 - villager.getTownHall().nbGoodAvailable(new InvItem(Mill.silk, 1), false, false) * 2;
/*     */     } catch (MLN.MillenaireException e) {
/* 122 */       MLN.printException(e);
/* 123 */       p = 0;
/*     */     }
/* 125 */     for (MillVillager v : villager.getTownHall().villagers) {
/* 126 */       if (this.key.equals(v.goalKey)) {
/* 127 */         p /= 2;
/*     */       }
/*     */     }
/*     */     
/* 131 */     return p;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager)
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalByzantineGatherSilk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
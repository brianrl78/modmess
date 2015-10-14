/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.InvItem;
/*    */ import org.millenaire.common.MLN.MillenaireException;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class GoalFish extends Goal
/*    */ {
/* 17 */   private static ItemStack[] fishingRod = { new ItemStack(Items.field_151112_aM, 1) };
/*    */   
/*    */   public GoalFish() {
/*    */     try {
/* 21 */       this.buildingLimit.put(new InvItem(Items.field_151115_aP, 0), Integer.valueOf(512));
/* 22 */       this.buildingLimit.put(new InvItem(Items.field_151101_aQ, 0), Integer.valueOf(512));
/*    */     } catch (MLN.MillenaireException e) {
/* 24 */       org.millenaire.common.MLN.printException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int actionDuration(MillVillager villager)
/*    */   {
/* 31 */     return 25000;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 37 */     List<Building> vb = villager.getTownHall().getBuildingsWithTag("fishingspot");
/*    */     
/* 39 */     Building closest = null;
/*    */     
/* 41 */     for (Building b : vb) {
/* 42 */       if ((closest == null) || (villager.getPos().horizontalDistanceToSquared(b.getResManager().getSleepingPos()) < villager.getPos().horizontalDistanceToSquared(closest.getResManager().getSleepingPos())))
/*    */       {
/* 44 */         closest = b;
/*    */       }
/*    */     }
/*    */     
/* 48 */     if ((closest == null) || (closest.getResManager().fishingspots.size() == 0)) {
/* 49 */       return null;
/*    */     }
/*    */     
/* 52 */     return packDest((Point)closest.getResManager().fishingspots.get(MillCommonUtilities.randomInt(closest.getResManager().fishingspots.size())), closest);
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager) throws Exception
/*    */   {
/* 57 */     return fishingRod;
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 63 */     for (Building b : villager.getTownHall().getBuildings()) {
/* 64 */       if (b.getResManager().fishingspots.size() > 0) {
/* 65 */         return true;
/*    */       }
/*    */     }
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public boolean lookAtGoal()
/*    */   {
/* 73 */     return true;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 79 */     villager.addToInv(Items.field_151115_aP, 1);
/*    */     
/* 81 */     villager.func_71038_i();
/*    */     
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 89 */     if (villager.getGoalBuildingDest() == null) {
/* 90 */       return 20;
/*    */     }
/*    */     
/* 93 */     return 100 - villager.getGoalBuildingDest().countGoods(Items.field_151115_aP);
/*    */   }
/*    */   
/*    */   public boolean stuckAction(MillVillager villager) throws Exception
/*    */   {
/* 98 */     return performAction(villager);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalFish.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
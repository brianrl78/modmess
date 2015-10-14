/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.init.Blocks;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class GoalShearSheep extends Goal
/*     */ {
/*     */   public GoalShearSheep()
/*     */   {
/*     */     try
/*     */     {
/*  22 */       this.buildingLimit.put(new InvItem(Blocks.field_150325_L, 0), Integer.valueOf(1024));
/*  23 */       this.townhallLimit.put(new InvItem(Blocks.field_150325_L, 0), Integer.valueOf(1024));
/*     */     } catch (MLN.MillenaireException e) {
/*  25 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  32 */     Point pos = villager.getPos();
/*  33 */     Entity closestSheep = null;
/*  34 */     double sheepBestDist = Double.MAX_VALUE;
/*     */     
/*  36 */     List<Entity> sheep = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, EntitySheep.class, villager.getHouse().getPos(), 30, 10);
/*     */     
/*  38 */     for (Entity ent : sheep) {
/*  39 */       if ((!((EntitySheep)ent).func_70892_o()) && (!((EntitySheep)ent).func_70631_g_()) && (
/*  40 */         (closestSheep == null) || (pos.distanceTo(ent) < sheepBestDist))) {
/*  41 */         closestSheep = ent;
/*  42 */         sheepBestDist = pos.distanceTo(ent);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  47 */     if (closestSheep != null) {
/*  48 */       return packDest(null, villager.getHouse(), closestSheep);
/*     */     }
/*     */     
/*  51 */     return null;
/*     */   }
/*     */   
/*     */   public org.millenaire.common.pathing.atomicstryker.AStarConfig getPathingConfig()
/*     */   {
/*  56 */     return JPS_CONFIG_WIDE;
/*     */   }
/*     */   
/*     */   public boolean isFightingGoal()
/*     */   {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  67 */     if (!villager.getHouse().location.tags.contains("sheeps")) {
/*  68 */       return false;
/*     */     }
/*     */     
/*  71 */     List<Entity> sheep = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, EntitySheep.class, villager.getHouse().getPos(), 30, 10);
/*     */     
/*  73 */     if (sheep == null) {
/*  74 */       return false;
/*     */     }
/*     */     
/*  77 */     for (Entity ent : sheep)
/*     */     {
/*  79 */       EntitySheep asheep = (EntitySheep)ent;
/*     */       
/*  81 */       if ((!asheep.func_70631_g_()) && (!asheep.field_70128_L))
/*     */       {
/*  83 */         if (!((EntitySheep)ent).func_70892_o()) {
/*  84 */           return true;
/*     */         }
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
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 100 */     List<Entity> sheep = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, EntitySheep.class, villager.getPos(), 4, 4);
/*     */     
/* 102 */     for (Entity ent : sheep) {
/* 103 */       if (!ent.field_70128_L)
/*     */       {
/* 105 */         EntitySheep animal = (EntitySheep)ent;
/*     */         
/* 107 */         if ((!animal.func_70631_g_()) && 
/* 108 */           (!animal.func_70892_o())) {
/* 109 */           villager.addToInv(Blocks.field_150325_L, ((EntitySheep)ent).func_70896_n(), 3);
/* 110 */           ((EntitySheep)ent).func_70893_e(true);
/* 111 */           if ((MLN.LogCattleFarmer >= 1) && (villager.extraLog)) {
/* 112 */             MLN.major(this, "Shearing: " + ent);
/*     */           }
/*     */           
/* 115 */           villager.func_71038_i();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 126 */     return 50;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 131 */     return 5;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalShearSheep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ 
/*     */ public class GoalSleep extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager) throws Exception
/*     */   {
/*  19 */     return 50;
/*     */   }
/*     */   
/*     */   public boolean allowRandomMoves() throws Exception
/*     */   {
/*  24 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canBeDoneAtNight()
/*     */   {
/*  29 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canBeDoneInDayTime()
/*     */   {
/*  34 */     return false;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  40 */     World world = villager.field_70170_p;
/*     */     
/*  42 */     Point sleepPos = villager.getHouse().getResManager().getSleepingPos();
/*     */     
/*  44 */     List<Point> beds = new ArrayList();
/*     */     
/*  46 */     for (int i = 0; i < 6; i++) {
/*  47 */       for (int j = 0; j < 6; j++) {
/*  48 */         for (int k = 0; k < 6; k++) {
/*  49 */           for (int l = 0; l < 8; l++) {
/*  50 */             Point p = sleepPos.getRelative(i * (1 - (l & 0x1) * 2), j * (1 - (l & 0x2)), k * (1 - (l & 0x4) / 2));
/*     */             
/*  52 */             Block block = MillCommonUtilities.getBlock(world, p);
/*     */             
/*  54 */             if (block == Blocks.field_150324_C) {
/*  55 */               int meta = MillCommonUtilities.getBlockMeta(world, p);
/*     */               
/*  57 */               if (!BlockBed.func_149975_b(meta))
/*     */               {
/*  59 */                 boolean alreadyTaken = false;
/*     */                 
/*  61 */                 for (MillVillager v : villager.getHouse().villagers) {
/*  62 */                   if ((v != villager) && (v.getGoalDestPoint() != null) && 
/*  63 */                     (v.getGoalDestPoint().equals(p))) {
/*  64 */                     alreadyTaken = true;
/*     */                   }
/*     */                 }
/*     */                 
/*     */ 
/*  69 */                 if (!alreadyTaken) {
/*  70 */                   beds.add(p);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  79 */     if (beds.size() > 0) {
/*  80 */       return packDest((Point)beds.get(0), villager.getHouse());
/*     */     }
/*     */     
/*  83 */     List<Point> feetPos = new ArrayList();
/*     */     
/*  85 */     for (int i = 0; i < 6; i++) {
/*  86 */       for (int j = 0; j < 6; j++) {
/*  87 */         for (int k = 0; k < 6; k++) {
/*  88 */           for (int l = 0; l < 8; l++) {
/*  89 */             Point p = sleepPos.getRelative(i * (1 - (l & 0x1) * 2), j * (1 - (l & 0x2)), k * (1 - (l & 0x4) / 2));
/*     */             
/*     */ 
/*  92 */             if ((!p.isBlockPassable(world)) && (p.getAbove().isBlockPassable(world)) && (p.getRelative(0.0D, 2.0D, 0.0D).isBlockPassable(world)))
/*     */             {
/*  94 */               Point topBlock = MillCommonUtilities.findTopNonPassableBlock(world, p.getiX(), p.getiZ());
/*     */               
/*     */ 
/*     */ 
/*  98 */               if ((topBlock != null) && (topBlock.y > p.y + 1.0D))
/*     */               {
/* 100 */                 float angle = villager.getBedOrientationInDegrees();
/*     */                 
/* 102 */                 int dx = 0;int dz = 0;
/*     */                 
/* 104 */                 if (angle == 0.0F) {
/* 105 */                   dx = 1;
/* 106 */                 } else if (angle == 90.0F) {
/* 107 */                   dz = 1;
/* 108 */                 } else if (angle == 180.0F) {
/* 109 */                   dx = -1;
/* 110 */                 } else if (angle == 270.0F) {
/* 111 */                   dz = -1;
/*     */                 }
/*     */                 
/* 114 */                 Point p2 = p.getRelative(dx, 0.0D, dz);
/*     */                 
/* 116 */                 if ((!p2.isBlockPassable(world)) && (p2.getAbove().isBlockPassable(world)) && (p2.getRelative(0.0D, 2.0D, 0.0D).isBlockPassable(world)))
/*     */                 {
/* 118 */                   topBlock = MillCommonUtilities.findTopNonPassableBlock(world, p2.getiX(), p2.getiZ());
/*     */                   
/* 120 */                   if ((topBlock != null) && (topBlock.y > p2.y + 1.0D))
/*     */                   {
/* 122 */                     p = p.getAbove();
/*     */                     
/* 124 */                     boolean alreadyTaken = false;
/*     */                     
/* 126 */                     for (MillVillager v : villager.getHouse().villagers) {
/* 127 */                       if ((v != villager) && (v.getGoalDestPoint() != null)) {
/* 128 */                         if (v.getGoalDestPoint().equals(p)) {
/* 129 */                           alreadyTaken = true;
/*     */                         }
/* 131 */                         if (v.getGoalDestPoint().equals(p.getRelative(1.0D, 0.0D, 0.0D))) {
/* 132 */                           alreadyTaken = true;
/*     */                         }
/* 134 */                         if (v.getGoalDestPoint().equals(p.getRelative(0.0D, 0.0D, 1.0D))) {
/* 135 */                           alreadyTaken = true;
/*     */                         }
/* 137 */                         if (v.getGoalDestPoint().equals(p.getRelative(-1.0D, 0.0D, 0.0D))) {
/* 138 */                           alreadyTaken = true;
/*     */                         }
/* 140 */                         if (v.getGoalDestPoint().equals(p.getRelative(0.0D, 0.0D, -1.0D))) {
/* 141 */                           alreadyTaken = true;
/*     */                         }
/*     */                       }
/*     */                     }
/* 145 */                     if (!alreadyTaken) {
/* 146 */                       feetPos.add(p);
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 157 */     for (MillVillager v : villager.getHouse().villagers) {
/* 158 */       if ((v != villager) && (v.getGoalDestPoint() != null)) {
/* 159 */         feetPos.remove(v.getGoalDestPoint());
/* 160 */         feetPos.remove(v.getGoalDestPoint().getRelative(1.0D, 0.0D, 0.0D));
/* 161 */         feetPos.remove(v.getGoalDestPoint().getRelative(0.0D, 0.0D, 1.0D));
/* 162 */         feetPos.remove(v.getGoalDestPoint().getRelative(-1.0D, 0.0D, 0.0D));
/* 163 */         feetPos.remove(v.getGoalDestPoint().getRelative(0.0D, 0.0D, -1.0D));
/*     */       }
/*     */     }
/*     */     
/* 167 */     if (feetPos.size() > 0) {
/* 168 */       return packDest((Point)feetPos.get(0), villager.getHouse());
/*     */     }
/*     */     
/* 171 */     return packDest(sleepPos, villager.getHouse());
/*     */   }
/*     */   
/*     */   public String labelKeyWhileTravelling(MillVillager villager)
/*     */   {
/* 176 */     return this.key + "_travelling";
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 182 */     if (!villager.nightActionPerformed) {
/* 183 */       villager.nightActionPerformed = villager.performNightAction();
/*     */     }
/*     */     
/* 186 */     villager.shouldLieDown = true;
/*     */     
/* 188 */     float angle = villager.getBedOrientationInDegrees();
/*     */     
/* 190 */     double dx = 0.5D;double dz = 0.5D;double fdx = 0.0D;double fdz = 0.0D;
/*     */     
/* 192 */     if (angle == 0.0F) {
/* 193 */       dx = 0.95D;
/* 194 */       fdx = -10.0D;
/* 195 */     } else if (angle == 90.0F) {
/* 196 */       dz = 0.95D;
/* 197 */       fdz = -10.0D;
/* 198 */     } else if (angle == 180.0F) {
/* 199 */       dx = 0.05D;
/* 200 */       fdx = 10.0D;
/* 201 */     } else if (angle == 270.0F) {
/* 202 */       dz = 0.05D;
/* 203 */       fdz = 10.0D;
/*     */     }
/*     */     
/*     */     float floatingHeight;
/*     */     float floatingHeight;
/* 208 */     if (villager.getBlock(villager.getGoalDestPoint()) == Blocks.field_150324_C) {
/* 209 */       floatingHeight = 0.7F;
/*     */     } else {
/* 211 */       floatingHeight = 0.2F;
/*     */     }
/*     */     
/* 214 */     villager.func_70107_b(villager.getGoalDestPoint().x + dx, villager.getGoalDestPoint().y + floatingHeight, villager.getGoalDestPoint().z + dz);
/* 215 */     villager.facePoint(villager.getPos().getRelative(fdx, 1.0D, fdz), 100.0F, 100.0F);
/*     */     
/* 217 */     return false;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 222 */     return 50;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 227 */     return 2;
/*     */   }
/*     */   
/*     */   public boolean shouldVillagerLieDown()
/*     */   {
/* 232 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalSleep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
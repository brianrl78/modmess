/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.goal.Goal.GoalInformation;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalGenericHarvestCrop extends GoalGeneric
/*     */ {
/*     */   public static int getCropBlockRipeMeta(String cropType)
/*     */   {
/*  24 */     return 7;
/*     */   }
/*     */   
/*     */   public static GoalGenericHarvestCrop loadGenericHarvestCropGoal(File file)
/*     */   {
/*  29 */     GoalGenericHarvestCrop g = new GoalGenericHarvestCrop();
/*     */     
/*  31 */     g.key = file.getName().split("\\.")[0].toLowerCase();
/*     */     try
/*     */     {
/*  34 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */       
/*     */       String line;
/*     */       
/*  38 */       while ((line = reader.readLine()) != null) {
/*  39 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  40 */           String[] temp = line.split("=");
/*  41 */           if (temp.length != 2) {
/*  42 */             MLN.error(null, "Invalid line when loading generic harvest goal " + file.getName() + ": " + line);
/*     */           } else {
/*  44 */             String key = temp[0].trim().toLowerCase();
/*  45 */             String value = temp[1].trim();
/*     */             
/*  47 */             if (!GoalGeneric.readGenericGoalConfigLine(g, key, value, file, line)) {
/*  48 */               if (key.equals("soilname")) {
/*  49 */                 g.soilName = value.trim().toLowerCase();
/*  50 */               } else if (key.equals("croptype")) {
/*  51 */                 g.cropType = value.trim().toLowerCase();
/*  52 */               } else if (key.equals("irrigationbonuscrop")) {
/*  53 */                 value = value.trim().toLowerCase();
/*  54 */                 if (Goods.goodsName.containsKey(value)) {
/*  55 */                   g.irrigationBonusCrop = ((InvItem)Goods.goodsName.get(value));
/*     */                 } else {
/*  57 */                   MLN.error(null, "Unknown irrigationbonuscrop in generic harvest goal " + file.getName() + ": " + line);
/*     */                 }
/*  59 */               } else if (key.equals("harvestitem")) {
/*  60 */                 String[] temp2 = value.split(",");
/*     */                 
/*  62 */                 if (temp2.length != 2) {
/*  63 */                   MLN.error(null, "harvestitem must take the form of harvestitem=goodname,chanceon100 (ex: wheat,50) in generic harbest goal " + file.getName() + ": " + line);
/*     */                 }
/*  65 */                 else if (Goods.goodsName.containsKey(temp2[0])) {
/*  66 */                   g.harvestItems.add(Goods.goodsName.get(temp2[0]));
/*  67 */                   g.harvestItemsChance.add(Integer.valueOf(Integer.parseInt(temp2[1])));
/*     */                 } else {
/*  69 */                   MLN.error(null, "Unknown harvestitem item in generic harvest goal " + file.getName() + ": " + line);
/*     */                 }
/*     */               }
/*     */               else {
/*  73 */                 MLN.error(null, "Unknown line in generic harvest goal " + file.getName() + ": " + line);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  80 */       if (g.soilName == null) {
/*  81 */         MLN.error(null, "The soilname is mandatory in custom harvest goals " + file.getName());
/*  82 */         return null;
/*     */       }
/*  84 */       if (g.cropType == null) {
/*  85 */         MLN.error(null, "The croptype is mandatory in custom harvest goals " + file.getName());
/*  86 */         return null;
/*     */       }
/*     */       
/*  89 */       reader.close();
/*     */     } catch (Exception e) {
/*  91 */       MLN.printException(e);
/*     */       
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     return g;
/*     */   }
/*     */   
/*  99 */   public String soilName = null; public String cropType = null;
/*     */   
/* 101 */   public List<InvItem> harvestItems = new ArrayList();
/*     */   
/* 103 */   public List<Integer> harvestItemsChance = new ArrayList();
/*     */   
/* 105 */   public InvItem irrigationBonusCrop = null;
/*     */   
/*     */   public GoalGenericHarvestCrop()
/*     */   {
/* 109 */     this.duration = 100;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 115 */     Point dest = null;
/* 116 */     Building destBuilding = null;
/*     */     
/* 118 */     List<Building> buildings = getBuildings(villager);
/*     */     
/* 120 */     for (Iterator i$ = buildings.iterator(); i$.hasNext();) { buildingDest = (Building)i$.next();
/*     */       
/* 122 */       if (isDestPossible(villager, buildingDest)) {
/* 123 */         List<Point> soils = buildingDest.getResManager().getSoilPoints(this.soilName);
/*     */         
/* 125 */         if (soils != null) {
/* 126 */           for (Point p : soils) {
/* 127 */             if ((isValidHarvestSoil(villager.field_70170_p, p)) && (
/* 128 */               (dest == null) || (p.distanceTo(villager) < dest.distanceTo(villager)))) {
/* 129 */               dest = p;
/* 130 */               destBuilding = buildingDest;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     Building buildingDest;
/* 138 */     if (dest == null) {
/* 139 */       return null;
/*     */     }
/*     */     
/* 142 */     return packDest(dest, destBuilding);
/*     */   }
/*     */   
/*     */   public net.minecraft.item.ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 148 */     if (this.heldItems != null) {
/* 149 */       return this.heldItems;
/*     */     }
/*     */     
/* 152 */     return villager.getBestHoeStack();
/*     */   }
/*     */   
/*     */   public boolean isDestPossibleSpecific(MillVillager villager, Building b)
/*     */   {
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleGenericGoal(MillVillager villager) throws Exception
/*     */   {
/* 162 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   private boolean isValidHarvestSoil(World world, Point p) {
/* 166 */     return (p.getAbove().getBlock(world) == GoalGenericPlantCrop.getCropBlock(this.cropType)) && (p.getAbove().getMeta(world) == getCropBlockRipeMeta(this.cropType));
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/* 171 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/* 177 */     if (isValidHarvestSoil(villager.field_70170_p, villager.getGoalDestPoint()))
/*     */     {
/* 179 */       if (this.irrigationBonusCrop != null) {
/* 180 */         float irrigation = villager.getTownHall().getVillageIrrigation();
/* 181 */         double rand = Math.random();
/* 182 */         if (rand < irrigation / 100.0F) {
/* 183 */           villager.addToInv(this.irrigationBonusCrop, 1);
/*     */         }
/*     */       }
/*     */       
/* 187 */       for (int i = 0; i < this.harvestItems.size(); i++) {
/* 188 */         if (MillCommonUtilities.randomInt(100) < ((Integer)this.harvestItemsChance.get(i)).intValue()) {
/* 189 */           villager.addToInv((InvItem)this.harvestItems.get(i), 1);
/*     */         }
/*     */       }
/*     */       
/* 193 */       villager.setBlockAndMetadata(villager.getGoalDestPoint().getAbove(), net.minecraft.init.Blocks.field_150350_a, 0);
/*     */       
/* 195 */       villager.func_71038_i();
/*     */     }
/*     */     
/* 198 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 204 */     Goal.GoalInformation info = getDestination(villager);
/*     */     
/* 206 */     if ((info == null) || (info.getDest() == null)) {
/* 207 */       return -1;
/*     */     }
/*     */     
/* 210 */     return (int)(30.0D - villager.getPos().distanceTo(info.getDest()));
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGenericHarvestCrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
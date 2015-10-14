/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.goal.Goal.GoalInformation;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalGenericPlantCrop extends GoalGeneric
/*     */ {
/*     */   public static net.minecraft.block.Block getCropBlock(String cropType)
/*     */   {
/*  24 */     if (cropType.equals("wheat")) {
/*  25 */       return Blocks.field_150464_aj;
/*     */     }
/*  27 */     if (cropType.equals("carrot")) {
/*  28 */       return Blocks.field_150459_bM;
/*     */     }
/*  30 */     if (cropType.equals("potato")) {
/*  31 */       return Blocks.field_150469_bN;
/*     */     }
/*  33 */     if (cropType.equals("rice")) {
/*  34 */       return Mill.cropRice;
/*     */     }
/*  36 */     if (cropType.equals("turmeric")) {
/*  37 */       return Mill.cropTurmeric;
/*     */     }
/*  39 */     if (cropType.equals("maize")) {
/*  40 */       return Mill.cropMaize;
/*     */     }
/*  42 */     if (cropType.equals("vine")) {
/*  43 */       return Mill.cropVine;
/*     */     }
/*  45 */     return null;
/*     */   }
/*     */   
/*     */   public static int getCropBlockMeta(String cropType) {
/*  49 */     return 0;
/*     */   }
/*     */   
/*     */   public static GoalGenericPlantCrop loadGenericPlantCropGoal(File file)
/*     */   {
/*  54 */     GoalGenericPlantCrop g = new GoalGenericPlantCrop();
/*     */     
/*  56 */     g.key = file.getName().split("\\.")[0].toLowerCase();
/*     */     try
/*     */     {
/*  59 */       BufferedReader reader = org.millenaire.common.core.MillCommonUtilities.getReader(file);
/*     */       
/*     */       String line;
/*     */       
/*  63 */       while ((line = reader.readLine()) != null) {
/*  64 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  65 */           String[] temp = line.split("=");
/*  66 */           if (temp.length != 2) {
/*  67 */             MLN.error(null, "Invalid line when loading generic plating goal " + file.getName() + ": " + line);
/*     */           } else {
/*  69 */             String key = temp[0].trim().toLowerCase();
/*  70 */             String value = temp[1].trim();
/*     */             
/*  72 */             if (!GoalGeneric.readGenericGoalConfigLine(g, key, value, file, line)) {
/*  73 */               if (key.equals("soilname")) {
/*  74 */                 g.soilName = value.trim().toLowerCase();
/*  75 */               } else if (key.equals("croptype")) {
/*  76 */                 g.cropType = value.trim().toLowerCase();
/*  77 */               } else if (key.equals("seed")) {
/*  78 */                 value = value.trim().toLowerCase();
/*  79 */                 if (Goods.goodsName.containsKey(value)) {
/*  80 */                   g.seedItem = ((InvItem)Goods.goodsName.get(value));
/*     */                 } else {
/*  82 */                   MLN.error(null, "Unknown seed in generic planting goal " + file.getName() + ": " + line);
/*     */                 }
/*     */               } else {
/*  85 */                 MLN.error(null, "Unknown line in generic planting goal " + file.getName() + ": " + line);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  92 */       if (g.soilName == null) {
/*  93 */         MLN.error(null, "The soilname is mandatory in custom planting goals " + file.getName());
/*  94 */         return null;
/*     */       }
/*  96 */       if (g.cropType == null) {
/*  97 */         MLN.error(null, "The croptype is mandatory in custom planting goals " + file.getName());
/*  98 */         return null;
/*     */       }
/*     */       
/* 101 */       reader.close();
/*     */     } catch (Exception e) {
/* 103 */       MLN.printException(e);
/*     */       
/* 105 */       return null;
/*     */     }
/*     */     
/* 108 */     return g;
/*     */   }
/*     */   
/* 111 */   public String soilName = null; public String cropType = null;
/*     */   
/* 113 */   public InvItem seedItem = null;
/*     */   
/*     */   public GoalGenericPlantCrop()
/*     */   {
/* 117 */     this.duration = 100;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 123 */     Point dest = null;
/* 124 */     Building destBuilding = null;
/*     */     
/* 126 */     List<Building> buildings = getBuildings(villager);
/*     */     
/* 128 */     for (Iterator i$ = buildings.iterator(); i$.hasNext();) { buildingDest = (Building)i$.next();
/*     */       
/* 130 */       if (isDestPossible(villager, buildingDest))
/*     */       {
/* 132 */         List<Point> soils = buildingDest.getResManager().getSoilPoints(this.soilName);
/*     */         
/* 134 */         if (soils != null) {
/* 135 */           for (Point p : soils) {
/* 136 */             if ((isValidPlantingLocation(villager.field_70170_p, p)) && (
/* 137 */               (dest == null) || (p.distanceTo(villager) < dest.distanceTo(villager)))) {
/* 138 */               dest = p;
/* 139 */               destBuilding = buildingDest;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Building buildingDest;
/* 146 */     if (dest == null) {
/* 147 */       return null;
/*     */     }
/*     */     
/* 150 */     return packDest(dest, destBuilding);
/*     */   }
/*     */   
/*     */   public boolean isDestPossibleSpecific(MillVillager villager, Building b)
/*     */   {
/* 155 */     if ((this.seedItem != null) && (b.countGoods(this.seedItem) + villager.countInv(this.seedItem) == 0)) {
/* 156 */       return false;
/*     */     }
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleGenericGoal(MillVillager villager) throws Exception
/*     */   {
/* 163 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   private boolean isValidPlantingLocation(World world, Point p) {
/* 167 */     if (((p.getAbove().getBlock(world) == Blocks.field_150350_a) || (p.getAbove().getBlock(world) == Blocks.field_150433_aE) || (p.getAbove().getBlock(world) == Blocks.field_150362_t)) && ((p.getBlock(world) == Blocks.field_150349_c) || (p.getBlock(world) == Blocks.field_150346_d) || (p.getBlock(world) == Blocks.field_150458_ak)))
/*     */     {
/* 169 */       return true;
/*     */     }
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/* 176 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/* 182 */     Building dest = villager.getGoalBuildingDest();
/*     */     
/* 184 */     if (dest == null) {
/* 185 */       return true;
/*     */     }
/*     */     
/* 188 */     if (!isValidPlantingLocation(villager.field_70170_p, villager.getGoalDestPoint())) {
/* 189 */       return true;
/*     */     }
/*     */     
/* 192 */     if (this.seedItem != null) {
/* 193 */       int taken = villager.takeFromInv(this.seedItem, 1);
/* 194 */       if (taken == 0) {
/* 195 */         dest.takeGoods(this.seedItem, 1);
/*     */       }
/*     */     }
/*     */     
/* 199 */     if (villager.getGoalDestPoint().getBlock(villager.field_70170_p) != Blocks.field_150458_ak) {
/* 200 */       villager.setBlockAndMetadata(villager.getGoalDestPoint(), Blocks.field_150458_ak, 0);
/*     */     }
/*     */     
/* 203 */     villager.setBlockAndMetadata(villager.getGoalDestPoint().getAbove(), getCropBlock(this.cropType), getCropBlockMeta(this.cropType));
/*     */     
/* 205 */     villager.func_71038_i();
/*     */     
/* 207 */     return true;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 213 */     Goal.GoalInformation info = getDestination(villager);
/*     */     
/* 215 */     if ((info == null) || (info.getDest() == null)) {
/* 216 */       return -1;
/*     */     }
/*     */     
/* 219 */     return (int)(30.0D - villager.getPos().distanceTo(info.getDest()));
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGenericPlantCrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
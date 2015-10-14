/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalGenericSlaughterAnimal extends GoalGeneric
/*     */ {
/*     */   public static GoalGenericSlaughterAnimal loadGenericSlaughterAnimalGoal(File file)
/*     */   {
/*  26 */     GoalGenericSlaughterAnimal g = new GoalGenericSlaughterAnimal();
/*     */     
/*  28 */     g.key = file.getName().split("\\.")[0].toLowerCase();
/*     */     try
/*     */     {
/*  31 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */       
/*     */       String line;
/*     */       
/*  35 */       while ((line = reader.readLine()) != null) {
/*  36 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  37 */           String[] temp = line.split("=");
/*  38 */           if (temp.length != 2) {
/*  39 */             MLN.error(null, "Invalid line when loading generic slaughter goal " + file.getName() + ": " + line);
/*     */           } else {
/*  41 */             String key = temp[0].trim().toLowerCase();
/*  42 */             String value = temp[1].trim();
/*     */             
/*  44 */             if (!GoalGeneric.readGenericGoalConfigLine(g, key, value, file, line)) {
/*  45 */               if (key.equals("animalkey")) {
/*  46 */                 if (EntityList.field_75625_b.containsKey(value)) {
/*  47 */                   g.animalKey = value;
/*     */                 } else {
/*  49 */                   MLN.error(null, "Unknown animalkey in generic slaughter goal " + file.getName() + ": " + line + ". Careful, it is case-sensitive.");
/*     */                 }
/*  51 */               } else if (key.equals("bonusitem")) {
/*  52 */                 String[] temp2 = value.split(",");
/*     */                 
/*  54 */                 if ((temp2.length != 3) && (temp2.length != 2)) {
/*  55 */                   MLN.error(null, "bonusitem must take the form of bonusitem=goodname,chanceon100 or bonusitem=goodname,chanceon100,requiredtag (ex: leather,50 or tripes,10,oven) in generic slaughter goal " + file.getName() + ": " + line);
/*     */ 
/*     */ 
/*     */                 }
/*  59 */                 else if (Goods.goodsName.containsKey(temp2[0])) {
/*  60 */                   g.extraItems.add(Goods.goodsName.get(temp2[0]));
/*  61 */                   g.extraItemsChance.add(Integer.valueOf(Integer.parseInt(temp2[1])));
/*  62 */                   if (temp2.length == 3) {
/*  63 */                     g.extraItemsTag.add(temp2[2].trim());
/*     */                   } else {
/*  65 */                     g.extraItemsTag.add(null);
/*     */                   }
/*     */                 } else {
/*  68 */                   MLN.error(null, "Unknown bonusitem item in generic slaughter goal " + file.getName() + ": " + line);
/*     */                 }
/*     */               }
/*     */               else
/*     */               {
/*  73 */                 MLN.error(null, "Unknown line in generic slaughter goal " + file.getName() + ": " + line);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  80 */       if (g.animalKey == null) {
/*  81 */         MLN.error(null, "The animalKey is mandatory in custom slaughter goals " + file.getName());
/*  82 */         return null;
/*     */       }
/*     */       
/*  85 */       reader.close();
/*     */     } catch (Exception e) {
/*  87 */       MLN.printException(e);
/*     */       
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     return g;
/*     */   }
/*     */   
/*     */ 
/*  96 */   public String animalKey = null;
/*  97 */   public List<InvItem> extraItems = new ArrayList();
/*  98 */   public List<Integer> extraItemsChance = new ArrayList();
/*     */   
/* 100 */   public List<String> extraItemsTag = new ArrayList();
/*     */   
/*     */   public GoalGenericSlaughterAnimal()
/*     */   {
/* 104 */     this.duration = 100;
/*     */   }
/*     */   
/*     */ 
/*     */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 111 */     Point pos = villager.getPos();
/* 112 */     Entity closest = null;
/* 113 */     Building destBuilding = null;
/* 114 */     double bestDist = Double.MAX_VALUE;
/*     */     
/* 116 */     List<Building> buildings = getBuildings(villager);
/*     */     
/* 118 */     for (Iterator i$ = buildings.iterator(); i$.hasNext();) { dest = (Building)i$.next();
/*     */       
/* 120 */       if (isDestPossible(villager, dest))
/*     */       {
/* 122 */         List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, (Class)EntityList.field_75625_b.get(this.animalKey), dest.getPos(), 15, 10);
/*     */         
/* 124 */         for (Entity ent : animals) {
/* 125 */           if ((!ent.field_70128_L) && (!isEntityChild(ent)) && (
/* 126 */             (closest == null) || (pos.distanceTo(ent) < bestDist))) {
/* 127 */             closest = ent;
/* 128 */             destBuilding = dest;
/* 129 */             bestDist = pos.distanceTo(ent);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     Building dest;
/* 136 */     if (closest == null) {
/* 137 */       return null;
/*     */     }
/*     */     
/* 140 */     return packDest(null, destBuilding, closest);
/*     */   }
/*     */   
/*     */   public org.millenaire.common.pathing.atomicstryker.AStarConfig getPathingConfig()
/*     */   {
/* 145 */     if (this.animalKey.equals("Squid")) {
/* 146 */       return JPS_CONFIG_SLAUGHTERSQUIDS;
/*     */     }
/*     */     
/* 149 */     return JPS_CONFIG_TIGHT;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isDestPossibleSpecific(MillVillager villager, Building b)
/*     */   {
/* 155 */     List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, (Class)EntityList.field_75625_b.get(this.animalKey), b.getPos(), 15, 10);
/*     */     
/* 157 */     if (animals == null) {
/* 158 */       return false;
/*     */     }
/*     */     
/* 161 */     int nbanimals = 0;
/*     */     
/* 163 */     for (Entity ent : animals)
/*     */     {
/* 165 */       if ((!ent.field_70128_L) && (!isEntityChild(ent))) {
/* 166 */         nbanimals++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 171 */     int targetAnimals = 0;
/*     */     
/* 173 */     for (int i = 0; i < b.getResManager().spawns.size(); i++) {
/* 174 */       if (((String)b.getResManager().spawnTypes.get(i)).equals(this.animalKey)) {
/* 175 */         targetAnimals = ((List)b.getResManager().spawns.get(i)).size();
/*     */       }
/*     */     }
/* 178 */     return nbanimals > targetAnimals;
/*     */   }
/*     */   
/*     */   private boolean isEntityChild(Entity ent) {
/* 182 */     if (!(ent instanceof EntityAgeable)) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     EntityAgeable animal = (EntityAgeable)ent;
/*     */     
/* 188 */     return animal.func_70631_g_();
/*     */   }
/*     */   
/*     */   public boolean isFightingGoal()
/*     */   {
/* 193 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleGenericGoal(MillVillager villager) throws Exception
/*     */   {
/* 198 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/* 203 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 210 */     Building dest = villager.getGoalBuildingDest();
/*     */     
/* 212 */     if (dest == null) {
/* 213 */       return true;
/*     */     }
/*     */     
/* 216 */     List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, (Class)EntityList.field_75625_b.get(this.animalKey), villager.getPos(), 2, 5);
/*     */     
/* 218 */     for (Entity ent : animals) {
/* 219 */       if ((!ent.field_70128_L) && 
/* 220 */         (!isEntityChild(ent)) && 
/* 221 */         (villager.func_70685_l(ent))) {
/* 222 */         villager.setEntityToAttack(ent);
/*     */         
/* 224 */         for (int i = 0; i < this.extraItems.size(); i++) {
/* 225 */           if (((this.extraItemsTag.get(i) == null) || (dest.location.tags.contains(this.extraItemsTag.get(i)))) && 
/* 226 */             (MillCommonUtilities.randomInt(100) < ((Integer)this.extraItemsChance.get(i)).intValue())) {
/* 227 */             villager.addToInv((InvItem)this.extraItems.get(i), 1);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 232 */         villager.func_71038_i();
/*     */         
/* 234 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 240 */     animals = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, (Class)EntityList.field_75625_b.get(this.animalKey), villager.getPos(), 2, 5);
/*     */     
/* 242 */     for (Entity ent : animals) {
/* 243 */       if ((!ent.field_70128_L) && 
/* 244 */         (!isEntityChild(ent)) && 
/* 245 */         (villager.func_70685_l(ent))) {
/* 246 */         villager.setEntityToAttack(ent);
/*     */         
/* 248 */         for (int i = 0; i < this.extraItems.size(); i++) {
/* 249 */           if (((this.extraItemsTag.get(i) == null) || (dest.location.tags.contains(this.extraItemsTag.get(i)))) && 
/* 250 */             (MillCommonUtilities.randomInt(100) < ((Integer)this.extraItemsChance.get(i)).intValue())) {
/* 251 */             villager.addToInv((InvItem)this.extraItems.get(i), 1);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 256 */         villager.func_71038_i();
/*     */         
/* 258 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 264 */     return true;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 269 */     return 2;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGenericSlaughterAnimal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
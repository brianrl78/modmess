/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.goal.Goal;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public abstract class GoalGeneric extends Goal
/*     */ {
/*     */   private static List<File> getGenericGoalFiles(String directoryName)
/*     */   {
/*  21 */     List<File> genericGoalFile = new ArrayList();
/*     */     
/*  23 */     for (File loadDir : org.millenaire.common.forge.Mill.loadingDirs)
/*     */     {
/*  25 */       File dir = new File(new File(loadDir, "goals"), directoryName);
/*     */       
/*  27 */       if (dir.exists()) {
/*  28 */         for (File file : dir.listFiles(new org.millenaire.common.core.MillCommonUtilities.ExtFileFilter("txt"))) {
/*  29 */           genericGoalFile.add(file);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  35 */     return genericGoalFile;
/*     */   }
/*     */   
/*     */   public static void loadGenericGoals() {
/*  39 */     for (File file : getGenericGoalFiles("genericcrafting")) {
/*     */       try {
/*  41 */         GoalGenericCrafting goal = GoalGenericCrafting.loadGenericCraftingGoal(file);
/*  42 */         if (goal != null) {
/*  43 */           if (MLN.LogGeneralAI >= 1) {
/*  44 */             MLN.major(goal, "loaded crafting goal");
/*     */           }
/*  46 */           goals.put(goal.key, goal);
/*     */         }
/*     */       } catch (Exception e) {
/*  49 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  53 */     for (File file : getGenericGoalFiles("genericcooking")) {
/*     */       try {
/*  55 */         GoalGenericCooking goal = GoalGenericCooking.loadGenericCookingGoal(file);
/*  56 */         if (goal != null) {
/*  57 */           if (MLN.LogGeneralAI >= 1) {
/*  58 */             MLN.major(goal, "loaded cooking goal");
/*     */           }
/*  60 */           goals.put(goal.key, goal);
/*     */         }
/*     */       } catch (Exception e) {
/*  63 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  67 */     for (File file : getGenericGoalFiles("genericslaughteranimal")) {
/*     */       try {
/*  69 */         GoalGenericSlaughterAnimal goal = GoalGenericSlaughterAnimal.loadGenericSlaughterAnimalGoal(file);
/*  70 */         if (goal != null) {
/*  71 */           if (MLN.LogGeneralAI >= 1) {
/*  72 */             MLN.major(goal, "loaded slaughtering goal");
/*     */           }
/*  74 */           goals.put(goal.key, goal);
/*     */         }
/*     */       } catch (Exception e) {
/*  77 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  81 */     for (File file : getGenericGoalFiles("genericplanting")) {
/*     */       try {
/*  83 */         GoalGenericPlantCrop goal = GoalGenericPlantCrop.loadGenericPlantCropGoal(file);
/*  84 */         if (goal != null) {
/*  85 */           if (MLN.LogGeneralAI >= 1) {
/*  86 */             MLN.major(goal, "loaded planting goal");
/*     */           }
/*  88 */           goals.put(goal.key, goal);
/*     */         }
/*     */       } catch (Exception e) {
/*  91 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  95 */     for (File file : getGenericGoalFiles("genericharvesting")) {
/*     */       try {
/*  97 */         GoalGenericHarvestCrop goal = GoalGenericHarvestCrop.loadGenericHarvestCropGoal(file);
/*  98 */         if (goal != null) {
/*  99 */           if (MLN.LogGeneralAI >= 1) {
/* 100 */             MLN.major(goal, "loaded harvesting goal");
/*     */           }
/* 102 */           goals.put(goal.key, goal);
/*     */         }
/*     */       } catch (Exception e) {
/* 105 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean readGenericGoalConfigLine(GoalGeneric g, String key, String value, File file, String line) {
/* 111 */     if (key.equals("buildingtag")) {
/* 112 */       g.buildingTag = value.toLowerCase();
/* 113 */     } else if (key.equals("townhallgoal")) {
/* 114 */       g.townHallGoal = Boolean.parseBoolean(value);
/* 115 */     } else if (key.equals("requiredtag")) {
/* 116 */       g.requiredTag = value.toLowerCase();
/* 117 */     } else if (key.equals("sentencekey")) {
/* 118 */       g.sentenceKey = value.toLowerCase();
/* 119 */     } else if (key.equals("labelkey")) {
/* 120 */       g.labelKey = value.toLowerCase();
/* 121 */     } else if (key.equals("priority")) {
/* 122 */       g.priority = Integer.parseInt(value);
/* 123 */     } else if (key.equals("maxsimultaneousinbuilding")) {
/* 124 */       g.maxSimultaneousInBuilding = Integer.parseInt(value);
/* 125 */     } else if (key.equals("maxsimultaneoustotal")) {
/* 126 */       g.maxSimultaneousTotal = Integer.parseInt(value);
/* 127 */     } else if (key.equals("duration")) {
/* 128 */       g.duration = Integer.parseInt(value);
/* 129 */     } else if (key.equals("sound")) {
/* 130 */       g.sound = value;
/* 131 */     } else if (key.equals("helditems")) {
/* 132 */       String[] temp2 = value.split(",");
/*     */       
/* 134 */       g.heldItems = new ItemStack[temp2.length];
/*     */       
/* 136 */       for (int i = 0; i < temp2.length; i++) {
/* 137 */         if (Goods.goodsName.containsKey(temp2[i])) {
/* 138 */           g.heldItems[i] = ((InvItem)Goods.goodsName.get(temp2[i])).getItemStack();
/* 139 */           if (g.heldItems[i].func_77973_b() == null) {
/* 140 */             MLN.error(null, "Held item with null item: " + temp2[i] + " in generic goal " + file.getName() + ": " + line);
/*     */           }
/*     */         } else {
/* 143 */           g.heldItems[i] = null;
/* 144 */           MLN.error(null, "Unknown held item in generic goal " + file.getName() + ": " + line);
/*     */         }
/*     */       }
/*     */     }
/* 148 */     else if (key.equals("buildinglimit")) {
/* 149 */       String[] temp2 = value.split(",");
/*     */       
/* 151 */       if (temp2.length != 2) {
/* 152 */         MLN.error(null, "buildinglimits must take the form of buildinglimit=goodname,goodquatity in generic goal " + file.getName() + ": " + line);
/*     */       }
/* 154 */       else if (Goods.goodsName.containsKey(temp2[0])) {
/* 155 */         g.buildingLimit.put(Goods.goodsName.get(temp2[0]), Integer.valueOf(Integer.parseInt(temp2[1])));
/*     */       } else {
/* 157 */         MLN.error(null, "Unknown buildinglimits item in generic goal " + file.getName() + ": " + line);
/*     */       }
/*     */     }
/* 160 */     else if (key.equals("townhalllimit")) {
/* 161 */       String[] temp2 = value.split(",");
/*     */       
/* 163 */       if (temp2.length != 2) {
/* 164 */         MLN.error(null, "townhalllimits must take the form of townhalllimit=goodname,goodquatity in generic goal " + file.getName() + ": " + line);
/*     */       }
/* 166 */       else if (Goods.goodsName.containsKey(temp2[0])) {
/* 167 */         g.townhallLimit.put(Goods.goodsName.get(temp2[0]), Integer.valueOf(Integer.parseInt(temp2[1])));
/*     */       } else {
/* 169 */         MLN.error(null, "Unknown townhalllimits item in generic goal " + file.getName() + ": " + line);
/*     */       }
/*     */     }
/* 172 */     else if (key.equals("itemsbalance"))
/*     */     {
/* 174 */       String[] temp2 = value.split(",");
/*     */       
/* 176 */       if (temp2.length != 2) {
/* 177 */         MLN.error(null, "itemsbalance must take the form of itemsbalance=firstgood,secondgood in generic goal " + file.getName() + ": " + line);
/*     */       }
/* 179 */       else if ((Goods.goodsName.containsKey(temp2[0])) || (Goods.goodsName.containsKey(temp2[1]))) {
/* 180 */         g.balanceOutput1 = ((InvItem)Goods.goodsName.get(temp2[0]));
/* 181 */         g.balanceOutput2 = ((InvItem)Goods.goodsName.get(temp2[1]));
/*     */       } else {
/* 183 */         MLN.error(null, "Unknown itemsbalance item in generic goal " + file.getName() + ": " + line);
/*     */       }
/*     */     }
/*     */     else {
/* 187 */       return false;
/*     */     }
/* 189 */     return true;
/*     */   }
/*     */   
/* 192 */   public String buildingTag = null;
/* 193 */   public String requiredTag = null;
/*     */   
/* 195 */   public boolean townHallGoal = false;
/*     */   
/* 197 */   public int priority = 50;
/*     */   
/* 199 */   public int duration = 5000;
/*     */   
/* 201 */   public String sentenceKey = null; public String labelKey = null;
/*     */   
/* 203 */   public ItemStack[] heldItems = null;
/*     */   
/* 205 */   public String sound = null;
/*     */   
/*     */   public int actionDuration(MillVillager villager) throws Exception
/*     */   {
/* 209 */     return this.duration;
/*     */   }
/*     */   
/*     */   public List<Building> getBuildings(MillVillager villager) {
/* 213 */     List<Building> buildings = new ArrayList();
/*     */     
/* 215 */     if (this.townHallGoal) {
/* 216 */       if ((this.requiredTag == null) || (villager.getTownHall().location.tags.contains(this.requiredTag))) {
/* 217 */         buildings.add(villager.getTownHall());
/*     */       }
/* 219 */     } else if (this.buildingTag == null) {
/* 220 */       if ((this.requiredTag == null) || (villager.getHouse().location.tags.contains(this.requiredTag))) {
/* 221 */         buildings.add(villager.getHouse());
/*     */       }
/*     */     }
/*     */     else {
/* 225 */       for (Building b : villager.getTownHall().getBuildingsWithTag(this.buildingTag)) {
/* 226 */         if ((this.requiredTag == null) || (b.location.tags.contains(this.requiredTag))) {
/* 227 */           buildings.add(b);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 232 */     return buildings;
/*     */   }
/*     */   
/*     */   public ItemStack[] getHeldItemsTravelling(MillVillager villager) throws Exception
/*     */   {
/* 237 */     return this.heldItems;
/*     */   }
/*     */   
/*     */   public final boolean isDestPossible(MillVillager villager, Building dest) throws MLN.MillenaireException {
/* 241 */     return (validateDest(villager, dest)) && (isDestPossibleSpecific(villager, dest));
/*     */   }
/*     */   
/*     */   public abstract boolean isDestPossibleSpecific(MillVillager paramMillVillager, Building paramBuilding);
/*     */   
/*     */   public abstract boolean isPossibleGenericGoal(MillVillager paramMillVillager)
/*     */     throws Exception;
/*     */   
/*     */   public final boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/* 251 */     if (!isPossibleGenericGoal(villager)) {
/* 252 */       return false;
/*     */     }
/*     */     
/* 255 */     List<Building> buildings = getBuildings(villager);
/*     */     
/* 257 */     boolean destFound = false;
/*     */     
/* 259 */     if (!buildings.isEmpty()) {
/* 260 */       for (Building dest : buildings) {
/* 261 */         if (!destFound) {
/* 262 */           destFound = isDestPossible(villager, dest);
/*     */         }
/*     */       }
/*     */       
/* 266 */       return destFound;
/*     */     }
/* 268 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String labelKey(MillVillager villager)
/*     */   {
/* 275 */     if (this.labelKey == null) {
/* 276 */       return this.key;
/*     */     }
/*     */     
/* 279 */     return this.labelKey;
/*     */   }
/*     */   
/*     */   public String labelKeyWhileTravelling(MillVillager villager)
/*     */   {
/* 284 */     if (this.labelKey == null) {
/* 285 */       return this.key;
/*     */     }
/*     */     
/* 288 */     return this.labelKey;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 293 */     return this.priority + villager.func_70681_au().nextInt(10);
/*     */   }
/*     */   
/*     */   public String sentenceKey()
/*     */   {
/* 298 */     if (this.sentenceKey == null) {
/* 299 */       return this.key;
/*     */     }
/*     */     
/* 302 */     return this.sentenceKey;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGeneric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
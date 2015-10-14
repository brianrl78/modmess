/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalGenericCrafting extends GoalGeneric
/*     */ {
/*     */   public static GoalGenericCrafting loadGenericCraftingGoal(File file)
/*     */   {
/*  19 */     GoalGenericCrafting g = new GoalGenericCrafting();
/*     */     
/*  21 */     g.key = file.getName().split("\\.")[0].toLowerCase();
/*     */     try
/*     */     {
/*  24 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */       
/*     */       String line;
/*     */       
/*  28 */       while ((line = reader.readLine()) != null) {
/*  29 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  30 */           String[] temp = line.split("=");
/*  31 */           if (temp.length != 2) {
/*  32 */             MLN.error(null, "Invalid line when loading generic crafting goal " + file.getName() + ": " + line);
/*     */           } else {
/*  34 */             String key = temp[0].toLowerCase();
/*  35 */             String value = temp[1];
/*     */             
/*  37 */             if (!GoalGeneric.readGenericGoalConfigLine(g, key, value, file, line)) {
/*  38 */               if (key.equals("input")) {
/*  39 */                 String[] temp2 = value.split(",");
/*     */                 
/*  41 */                 if (temp2.length != 2) {
/*  42 */                   MLN.error(null, "Inputs must take the form of input=goodname,goodquatity in generic crafting goal " + file.getName() + ": " + line);
/*     */                 }
/*  44 */                 else if (Goods.goodsName.containsKey(temp2[0])) {
/*  45 */                   g.inputs.put(Goods.goodsName.get(temp2[0]), Integer.valueOf(Integer.parseInt(temp2[1])));
/*     */                 } else {
/*  47 */                   MLN.error(null, "Unknown input item in generic crafting goal " + file.getName() + ": " + line);
/*     */                 }
/*     */               }
/*  50 */               else if (key.equals("output")) {
/*  51 */                 String[] temp2 = value.split(",");
/*     */                 
/*  53 */                 if (temp2.length != 2) {
/*  54 */                   MLN.error(null, "Outputs must take the form of input=goodname,goodquatity in generic crafting goal " + file.getName() + ": " + line);
/*     */                 }
/*  56 */                 else if (Goods.goodsName.containsKey(temp2[0])) {
/*  57 */                   g.outputs.put(Goods.goodsName.get(temp2[0]), Integer.valueOf(Integer.parseInt(temp2[1])));
/*     */                 } else {
/*  59 */                   MLN.error(null, "Unknown output item in generic crafting goal " + file.getName() + ": " + line);
/*     */                 }
/*     */               }
/*     */               else {
/*  63 */                 MLN.error(null, "Unknown line in generic crafting goal " + file.getName() + ": " + line);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  70 */       reader.close();
/*     */     } catch (Exception e) {
/*  72 */       MLN.printException(e);
/*     */       
/*  74 */       return null;
/*     */     }
/*     */     
/*  77 */     return g;
/*     */   }
/*     */   
/*     */ 
/*  81 */   public HashMap<InvItem, Integer> inputs = new HashMap();
/*  82 */   public HashMap<InvItem, Integer> outputs = new HashMap();
/*     */   
/*     */   public int actionDuration(MillVillager villager) throws Exception
/*     */   {
/*  86 */     return this.duration;
/*     */   }
/*     */   
/*     */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*     */   {
/*  91 */     List<Building> buildings = getBuildings(villager);
/*     */     
/*  93 */     for (Building dest : buildings)
/*     */     {
/*  95 */       if (isDestPossible(villager, dest)) {
/*  96 */         return packDest(dest.getResManager().getCraftingPos(), dest);
/*     */       }
/*     */     }
/*     */     
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDestPossibleSpecific(MillVillager villager, Building b)
/*     */   {
/* 105 */     for (InvItem item : this.inputs.keySet()) {
/* 106 */       if (villager.countInv(item) + b.countGoods(item) < ((Integer)this.inputs.get(item)).intValue()) {
/* 107 */         return false;
/*     */       }
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleGenericGoal(MillVillager villager) throws Exception
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 121 */     Building dest = villager.getGoalBuildingDest();
/*     */     
/* 123 */     if (dest == null) {
/* 124 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 128 */     for (InvItem item : this.inputs.keySet()) {
/* 129 */       if (villager.countInv(item) + dest.countGoods(item) < ((Integer)this.inputs.get(item)).intValue()) {
/* 130 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 134 */     for (InvItem item : this.inputs.keySet()) {
/* 135 */       int nbTaken = villager.takeFromInv(item, 1024);
/* 136 */       dest.storeGoods(item, nbTaken);
/*     */       
/* 138 */       dest.takeGoods(item, ((Integer)this.inputs.get(item)).intValue());
/*     */     }
/*     */     
/* 141 */     for (InvItem item : this.outputs.keySet()) {
/* 142 */       dest.storeGoods(item, ((Integer)this.outputs.get(item)).intValue());
/*     */     }
/*     */     
/* 145 */     if (this.sound != null) {
/* 146 */       MillCommonUtilities.playSoundByMillName(villager.field_70170_p, villager.getPos(), this.sound, 10.0F);
/*     */     }
/*     */     
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   public boolean swingArms()
/*     */   {
/* 154 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGenericCrafting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
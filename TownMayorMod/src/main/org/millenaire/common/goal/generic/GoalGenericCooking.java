/*     */ package org.millenaire.common.goal.generic;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class GoalGenericCooking extends GoalGeneric
/*     */ {
/*     */   public static GoalGenericCooking loadGenericCookingGoal(File file)
/*     */   {
/*  24 */     GoalGenericCooking g = new GoalGenericCooking();
/*     */     
/*  26 */     g.key = file.getName().split("\\.")[0].toLowerCase();
/*     */     try
/*     */     {
/*  29 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */       
/*     */       String line;
/*     */       
/*  33 */       while ((line = reader.readLine()) != null) {
/*  34 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  35 */           String[] temp = line.split("=");
/*  36 */           if (temp.length != 2) {
/*  37 */             MLN.error(null, "Invalid line when loading generic cooking goal " + file.getName() + ": " + line);
/*     */           } else {
/*  39 */             String key = temp[0].trim().toLowerCase();
/*  40 */             String value = temp[1].trim();
/*     */             
/*  42 */             if (!GoalGeneric.readGenericGoalConfigLine(g, key, value, file, line)) {
/*  43 */               if (key.equals("itemtocook")) {
/*  44 */                 if (Goods.goodsName.containsKey(value)) {
/*  45 */                   g.itemToCook = ((InvItem)Goods.goodsName.get(value));
/*     */                 } else {
/*  47 */                   MLN.error(null, "Unknown itemToCook item in generic cooking goal " + file.getName() + ": " + line);
/*     */                 }
/*  49 */               } else if (key.equals("minimumtocook")) {
/*  50 */                 g.minimumToCook = Integer.parseInt(value);
/*     */               } else {
/*  52 */                 MLN.error(null, "Unknown line in generic cooking goal " + file.getName() + ": " + line);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  59 */       if (g.itemToCook == null) {
/*  60 */         MLN.error(null, "The itemtocook id is mandatory in custom cooking goals " + file.getName());
/*  61 */         return null;
/*     */       }
/*     */       
/*  64 */       reader.close();
/*     */     } catch (Exception e) {
/*  66 */       MLN.printException(e);
/*     */       
/*  68 */       return null;
/*     */     }
/*     */     
/*  71 */     return g;
/*     */   }
/*     */   
/*     */ 
/*  75 */   public InvItem itemToCook = null;
/*     */   
/*  77 */   public int minimumToCook = 16;
/*     */   
/*     */   public org.millenaire.common.goal.Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  82 */     List<Building> buildings = getBuildings(villager);
/*     */     
/*  84 */     for (Iterator i$ = buildings.iterator(); i$.hasNext();) { dest = (Building)i$.next();
/*     */       
/*  86 */       if (isDestPossible(villager, dest))
/*     */       {
/*  88 */         for (Point p : dest.getResManager().furnaces)
/*     */         {
/*  90 */           TileEntityFurnace furnace = p.getFurnace(villager.field_70170_p);
/*     */           
/*  92 */           if (furnace != null)
/*     */           {
/*  94 */             if (((furnace.func_70301_a(1) == null) || (furnace.func_70301_a(1).field_77994_a < 32)) && (dest.countGoods(Blocks.field_150364_r, -1) > 4)) {
/*  95 */               return packDest(p, dest);
/*     */             }
/*     */             
/*     */ 
/*  99 */             if ((dest.countGoods(this.itemToCook) >= this.minimumToCook) && ((furnace.func_70301_a(0) == null) || ((furnace.func_70301_a(0).func_77973_b() == this.itemToCook.getItem()) && (furnace.func_70301_a(0).func_77960_j() == this.itemToCook.meta) && (furnace.func_70301_a(0).field_77994_a < 32))))
/*     */             {
/*     */ 
/* 102 */               return packDest(p, dest);
/*     */             }
/*     */             
/*     */ 
/* 106 */             if ((furnace.func_70301_a(2) != null) && (furnace.func_70301_a(2).field_77994_a >= this.minimumToCook)) {
/* 107 */               return packDest(p, dest);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     Building dest;
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDestPossibleSpecific(MillVillager villager, Building b)
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPossibleGenericGoal(MillVillager villager) throws Exception
/*     */   {
/* 125 */     if (getDestination(villager) == null) {
/* 126 */       return false;
/*     */     }
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 134 */     TileEntityFurnace furnace = villager.getGoalDestPoint().getFurnace(villager.field_70170_p);
/*     */     
/* 136 */     Building dest = villager.getGoalBuildingDest();
/*     */     
/* 138 */     if ((furnace != null) && (dest != null)) {
/* 139 */       if (((furnace.func_70301_a(0) == null) && (dest.countGoods(this.itemToCook) >= this.minimumToCook)) || ((furnace.func_70301_a(0) != null) && (furnace.func_70301_a(0).func_77973_b() == this.itemToCook.getItem()) && (furnace.func_70301_a(0).func_77960_j() == this.itemToCook.meta) && (furnace.func_70301_a(0).field_77994_a < 64) && (dest.countGoods(this.itemToCook) > 0)))
/*     */       {
/*     */ 
/* 142 */         if (furnace.func_70301_a(0) == null) {
/* 143 */           int nb = Math.min(64, dest.countGoods(this.itemToCook));
/*     */           
/* 145 */           furnace.func_70299_a(0, new ItemStack(this.itemToCook.getItem(), nb, this.itemToCook.meta));
/* 146 */           dest.takeGoods(this.itemToCook, nb);
/*     */         } else {
/* 148 */           int nb = Math.min(64 - furnace.func_70301_a(0).field_77994_a, villager.getHouse().countGoods(this.itemToCook));
/* 149 */           furnace.func_70301_a(0).field_77994_a += nb;
/* 150 */           dest.takeGoods(this.itemToCook, nb);
/*     */         }
/*     */       }
/*     */       
/* 154 */       if (furnace.func_70301_a(2) != null) {
/* 155 */         Item item = furnace.func_70301_a(2).func_77973_b();
/* 156 */         int meta = furnace.func_70301_a(2).func_77960_j();
/*     */         
/* 158 */         dest.storeGoods(item, meta, furnace.func_70301_a(2).field_77994_a);
/* 159 */         furnace.func_70299_a(2, null);
/*     */       }
/*     */     }
/*     */     
/* 163 */     if (dest.countGoods(Blocks.field_150364_r, -1) > 0) {
/* 164 */       if (furnace.func_70301_a(1) == null) {
/* 165 */         int nbplanks = Math.min(64, dest.countGoods(Blocks.field_150364_r, -1) * 4);
/*     */         
/* 167 */         furnace.func_70299_a(1, new ItemStack(Blocks.field_150344_f, nbplanks));
/* 168 */         dest.takeGoods(Blocks.field_150364_r, -1, nbplanks / 4);
/*     */       }
/* 170 */       else if (furnace.func_70301_a(1).field_77994_a < 64)
/*     */       {
/* 172 */         int nbplanks = Math.min(64 - furnace.func_70301_a(1).field_77994_a, dest.countGoods(Blocks.field_150364_r, -1) * 4);
/*     */         
/* 174 */         furnace.func_70299_a(1, new ItemStack(Blocks.field_150344_f, furnace.func_70301_a(1).field_77994_a + nbplanks));
/* 175 */         dest.takeGoods(Blocks.field_150364_r, -1, nbplanks / 4);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\generic\GoalGenericCooking.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
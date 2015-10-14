/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntityBrewingStand;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalBrewPotions extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*    */   {
/* 15 */     int nbWarts = villager.getHouse().countGoods(Items.field_151075_bm);
/* 16 */     int nbBottles = villager.getHouse().countGoods(Items.field_151069_bo);
/* 17 */     int nbPotions = villager.getHouse().countGoods(Items.field_151068_bn, -1);
/*    */     
/* 19 */     for (Point p : villager.getHouse().getResManager().brewingStands)
/*    */     {
/* 21 */       TileEntityBrewingStand brewingStand = p.getBrewingStand(villager.field_70170_p);
/*    */       
/* 23 */       if ((brewingStand != null) && (brewingStand.func_145935_i() == 0)) {
/* 24 */         if ((brewingStand.func_70301_a(3) == null) && (nbWarts > 0) && (nbPotions < 64)) {
/* 25 */           return packDest(p, villager.getHouse());
/*    */         }
/*    */         
/* 28 */         if ((nbBottles > 2) && ((brewingStand.func_70301_a(0) == null) || (brewingStand.func_70301_a(1) == null) || (brewingStand.func_70301_a(2) == null)) && (nbPotions < 64)) {
/* 29 */           return packDest(p, villager.getHouse());
/*    */         }
/*    */         
/* 32 */         for (int i = 0; i < 3; i++) {
/* 33 */           if ((brewingStand.func_70301_a(i) != null) && (brewingStand.func_70301_a(i).func_77973_b() == Items.field_151068_bn) && (brewingStand.func_70301_a(i).func_77960_j() == 16)) {
/* 34 */             return packDest(p, villager.getHouse());
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*    */   {
/* 45 */     return getDestination(villager) != null;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 51 */     int nbWarts = villager.getHouse().countGoods(Items.field_151075_bm);
/* 52 */     int nbBottles = villager.getHouse().countGoods(Items.field_151069_bo);
/* 53 */     int nbPotions = villager.getHouse().countGoods(Items.field_151068_bn);
/*    */     
/* 55 */     TileEntityBrewingStand brewingStand = villager.getGoalDestPoint().getBrewingStand(villager.field_70170_p);
/*    */     
/* 57 */     if (brewingStand == null) {
/* 58 */       return true;
/*    */     }
/*    */     
/* 61 */     if (brewingStand.func_145935_i() == 0) {
/* 62 */       if ((brewingStand.func_70301_a(3) == null) && (nbWarts > 0) && (nbPotions < 64)) {
/* 63 */         brewingStand.func_70299_a(3, new ItemStack(Items.field_151075_bm, 1));
/* 64 */         villager.getHouse().takeGoods(Items.field_151075_bm, 1);
/*    */       }
/*    */       
/* 67 */       if ((nbBottles > 2) && (nbPotions < 64)) {
/* 68 */         for (int i = 0; i < 3; i++) {
/* 69 */           if (brewingStand.func_70301_a(i) == null) {
/* 70 */             brewingStand.func_70299_a(i, new ItemStack(Items.field_151068_bn, 1));
/* 71 */             villager.getHouse().takeGoods(Items.field_151069_bo, 1);
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 76 */       for (int i = 0; i < 3; i++) {
/* 77 */         if ((brewingStand.func_70301_a(i) != null) && (brewingStand.func_70301_a(i).func_77973_b() == Items.field_151068_bn) && (brewingStand.func_70301_a(i).func_77960_j() == 16)) {
/* 78 */           brewingStand.func_70299_a(i, null);
/* 79 */           villager.getHouse().storeGoods(Items.field_151068_bn, 16, 1);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 85 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 90 */     return 100;
/*    */   }
/*    */   
/*    */   public boolean swingArms()
/*    */   {
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBrewPotions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
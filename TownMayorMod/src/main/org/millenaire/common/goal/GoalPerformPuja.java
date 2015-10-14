/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.PujaSacrifice;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ public class GoalPerformPuja extends Goal
/*     */ {
/*     */   public int actionDuration(MillVillager villager) throws Exception
/*     */   {
/*  14 */     return 100;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  20 */     Building temple = null;
/*     */     
/*  22 */     if (villager.canMeditate()) {
/*  23 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  24 */     } else if (villager.canPerformSacrifices()) {
/*  25 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/*  28 */     if ((temple != null) && (temple.pujas != null) && ((temple.pujas.priest == null) || (temple.pujas.priest == villager)) && (temple.pujas.canPray())) {
/*  29 */       return packDest(temple.getResManager().getCraftingPos(), temple);
/*     */     }
/*     */     
/*  32 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] getHeldItemsDestination(MillVillager villager)
/*     */   {
/*  38 */     Building temple = null;
/*     */     
/*  40 */     if (villager.canMeditate()) {
/*  41 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  42 */     } else if (villager.canPerformSacrifices()) {
/*  43 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/*  46 */     if (temple.pujas.func_70301_a(0) != null) {
/*  47 */       return new ItemStack[] { temple.pujas.func_70301_a(0) };
/*     */     }
/*     */     
/*  50 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  56 */     if (villager.canMeditate()) {
/*  57 */       if (!villager.mw.isGlobalTagSet("pujas")) {
/*  58 */         return false;
/*     */       }
/*  60 */     } else if ((villager.canPerformSacrifices()) && 
/*  61 */       (!villager.mw.isGlobalTagSet("mayansacrifices"))) {
/*  62 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  66 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public String labelKey(MillVillager villager)
/*     */   {
/*  71 */     if ((villager != null) && (villager.canPerformSacrifices())) {
/*  72 */       return "performsacrifices";
/*     */     }
/*     */     
/*  75 */     return this.key;
/*     */   }
/*     */   
/*     */   public String labelKeyWhileTravelling(MillVillager villager)
/*     */   {
/*  80 */     if ((villager != null) && (villager.canPerformSacrifices())) {
/*  81 */       return "performsacrifices";
/*     */     }
/*     */     
/*  84 */     return this.key;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  95 */     Building temple = null;
/*     */     
/*  97 */     if (villager.canMeditate()) {
/*  98 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  99 */     } else if (villager.canPerformSacrifices()) {
/* 100 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/* 103 */     boolean canContinue = temple.pujas.performPuja(villager);
/*     */     
/* 105 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72890_a(villager, 16.0D);
/*     */     
/* 107 */     if (player != null) {
/* 108 */       temple.sendBuildingPacket(player, false);
/*     */     }
/*     */     
/* 111 */     if (!canContinue) {
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 120 */     return 500;
/*     */   }
/*     */   
/*     */   public boolean swingArms()
/*     */   {
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalPerformPuja.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
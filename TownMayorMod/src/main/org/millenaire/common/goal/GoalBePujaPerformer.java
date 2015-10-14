/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.PujaSacrifice;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingResManager;
/*     */ 
/*     */ public class GoalBePujaPerformer extends Goal
/*     */ {
/*     */   public static final int sellingRadius = 7;
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */   {
/*  17 */     Building temple = null;
/*     */     
/*  19 */     if (villager.canMeditate()) {
/*  20 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  21 */     } else if (villager.canPerformSacrifices()) {
/*  22 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/*  25 */     if ((temple != null) && (temple.pujas != null) && ((temple.pujas.priest == null) || (temple.pujas.priest == villager)))
/*     */     {
/*  27 */       if (MLN.LogPujas >= 3) {
/*  28 */         MLN.debug(villager, "Destination for bepujaperformer: " + temple);
/*     */       }
/*     */       
/*  31 */       return packDest(temple.getResManager().getCraftingPos(), temple);
/*     */     }
/*     */     
/*  34 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPossibleSpecific(MillVillager villager)
/*     */   {
/*  40 */     Building temple = null;
/*     */     
/*  42 */     if (villager.canMeditate()) {
/*  43 */       if (!villager.mw.isGlobalTagSet("pujas")) {
/*  44 */         return false;
/*     */       }
/*     */       
/*  47 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  48 */     } else if (villager.canPerformSacrifices()) {
/*  49 */       if (!villager.mw.isGlobalTagSet("mayansacrifices")) {
/*  50 */         return false;
/*     */       }
/*     */       
/*  53 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/*  56 */     if (temple == null) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(temple.getResManager().getCraftingPos().getiX(), temple.getResManager().getCraftingPos().getiY(), temple.getResManager().getCraftingPos().getiZ(), 7.0D);
/*     */     
/*     */ 
/*  63 */     boolean valid = (player != null) && (temple.getResManager().getCraftingPos().distanceTo(player) < 7.0D);
/*     */     
/*  65 */     if (!valid) {
/*  66 */       return false;
/*     */     }
/*     */     
/*  69 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean isStillValidSpecific(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  75 */     Building temple = null;
/*     */     
/*  77 */     if (villager.canMeditate()) {
/*  78 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/*  79 */     } else if (villager.canPerformSacrifices()) {
/*  80 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/*  83 */     if (temple == null) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(temple.getResManager().getCraftingPos().getiX(), temple.getResManager().getCraftingPos().getiY(), temple.getResManager().getCraftingPos().getiZ(), 7.0D);
/*     */     
/*     */ 
/*  90 */     boolean valid = (player != null) && (temple.getResManager().getCraftingPos().distanceTo(player) < 7.0D);
/*     */     
/*  92 */     if ((!valid) && (MLN.LogPujas >= 1)) {
/*  93 */       MLN.major(this, "Be Puja Performer no longer valid.");
/*     */     }
/*     */     
/*     */ 
/*  97 */     return (valid) && (!temple.pujas.canPray());
/*     */   }
/*     */   
/*     */   public String labelKey(MillVillager villager)
/*     */   {
/* 102 */     if ((villager != null) && (villager.canPerformSacrifices())) {
/* 103 */       return "besacrificeperformer";
/*     */     }
/*     */     
/* 106 */     return this.key;
/*     */   }
/*     */   
/*     */   public String labelKeyWhileTravelling(MillVillager villager)
/*     */   {
/* 111 */     if ((villager != null) && (villager.canPerformSacrifices())) {
/* 112 */       return "besacrificeperformer";
/*     */     }
/*     */     
/* 115 */     return this.key;
/*     */   }
/*     */   
/*     */   public boolean lookAtPlayer()
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onAccept(MillVillager villager)
/*     */   {
/* 126 */     Building temple = null;
/*     */     
/* 128 */     if (villager.canMeditate()) {
/* 129 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/* 130 */     } else if (villager.canPerformSacrifices()) {
/* 131 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/* 134 */     if (temple == null) {
/* 135 */       return;
/*     */     }
/*     */     
/* 138 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(temple.getResManager().getCraftingPos().getiX(), temple.getResManager().getCraftingPos().getiY(), temple.getResManager().getCraftingPos().getiZ(), 7.0D);
/*     */     
/*     */ 
/* 141 */     if (villager.canMeditate()) {
/* 142 */       org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "pujas.priestcoming", new String[] { villager.getName() });
/* 143 */     } else if (villager.canPerformSacrifices()) {
/* 144 */       org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "sacrifices.priestcoming", new String[] { villager.getName() });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean performAction(MillVillager villager)
/*     */   {
/* 152 */     Building temple = null;
/*     */     
/* 154 */     if (villager.canMeditate()) {
/* 155 */       temple = villager.getTownHall().getFirstBuildingWithTag("pujas");
/* 156 */     } else if (villager.canPerformSacrifices()) {
/* 157 */       temple = villager.getTownHall().getFirstBuildingWithTag("sacrifices");
/*     */     }
/*     */     
/* 160 */     if (temple == null) {
/* 161 */       return true;
/*     */     }
/*     */     
/* 164 */     temple.pujas.priest = villager;
/*     */     
/*     */ 
/* 167 */     return temple.pujas.canPray();
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager)
/*     */   {
/* 172 */     return 300;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 177 */     return 2;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBePujaPerformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
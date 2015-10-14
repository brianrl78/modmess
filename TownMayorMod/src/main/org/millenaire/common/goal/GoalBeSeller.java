/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.building.BuildingResManager;
/*    */ 
/*    */ public class GoalBeSeller extends Goal
/*    */ {
/*    */   public static final int sellingRadius = 7;
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager)
/*    */   {
/* 15 */     return packDest(villager.getTownHall().sellingPlace);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isPossibleSpecific(MillVillager villager)
/*    */   {
/* 21 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isStillValidSpecific(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 27 */     if (villager.getTownHall().sellingPlace == null) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(villager.getTownHall().sellingPlace.getiX(), villager.getTownHall().sellingPlace.getiY(), villager.getTownHall().sellingPlace.getiZ(), 7.0D);
/*    */     
/*    */ 
/* 34 */     boolean valid = (player != null) && (villager.getTownHall().sellingPlace.distanceTo(player) < 7.0D);
/*    */     
/* 36 */     if ((!valid) && (org.millenaire.common.MLN.LogWifeAI >= 1)) {
/* 37 */       org.millenaire.common.MLN.major(this, "Selling goal no longer valid.");
/*    */     }
/*    */     
/* 40 */     return valid;
/*    */   }
/*    */   
/*    */   public boolean lookAtPlayer()
/*    */   {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public void onAccept(MillVillager villager)
/*    */   {
/* 50 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(villager.getTownHall().sellingPlace.getiX(), villager.getTownHall().sellingPlace.getiY(), villager.getTownHall().sellingPlace.getiZ(), 7.0D);
/*    */     
/* 52 */     org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "ui.sellercoming", new String[] { villager.getName() });
/*    */   }
/*    */   
/*    */ 
/*    */   public void onComplete(MillVillager villager)
/*    */   {
/* 58 */     net.minecraft.entity.player.EntityPlayer player = villager.field_70170_p.func_72977_a(villager.getTownHall().getResManager().getSellingPos().getiX(), villager.getTownHall().getResManager().getSellingPos().getiY(), villager.getTownHall().getResManager().getSellingPos().getiZ(), 17.0D);
/*    */     
/*    */ 
/* 61 */     org.millenaire.common.network.ServerSender.sendTranslatedSentence(player, 'f', "ui.tradecomplete", new String[] { villager.getName() });
/* 62 */     villager.getTownHall().seller = null;
/* 63 */     villager.getTownHall().sellingPlace = null;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager)
/*    */   {
/* 68 */     if (villager.getTownHall().sellingPlace == null) {
/* 69 */       org.millenaire.common.MLN.error(this, "villager.townHall.sellingPlace is null.");
/* 70 */       return true;
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager)
/*    */   {
/* 77 */     return 0;
/*    */   }
/*    */   
/*    */   public int range(MillVillager villager)
/*    */   {
/* 82 */     return 2;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalBeSeller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
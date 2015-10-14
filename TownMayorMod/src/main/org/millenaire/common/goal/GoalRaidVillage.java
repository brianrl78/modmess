/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.building.Building;
/*    */ 
/*    */ public class GoalRaidVillage extends Goal
/*    */ {
/*    */   public boolean canBeDoneAtNight()
/*    */   {
/* 11 */     return true;
/*    */   }
/*    */   
/*    */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*    */   {
/* 16 */     return packDest(villager.getTownHall().getResManager().getDefendingPos(), villager.getTownHall());
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 21 */     return new ItemStack[] { villager.getWeapon() };
/*    */   }
/*    */   
/*    */   public boolean isFightingGoal()
/*    */   {
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isStillValidSpecific(MillVillager villager) throws Exception
/*    */   {
/* 31 */     return villager.getTownHall().underAttack;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager) throws Exception
/*    */   {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 41 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalRaidVillage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
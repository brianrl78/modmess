/*    */ package org.millenaire.common.goal;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.monster.EntityMob;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.millenaire.common.MLN;
/*    */ import org.millenaire.common.MillVillager;
/*    */ import org.millenaire.common.Point;
/*    */ import org.millenaire.common.building.Building;
/*    */ import org.millenaire.common.core.MillCommonUtilities;
/*    */ 
/*    */ public class GoalHuntMonster extends Goal
/*    */ {
/*    */   public Goal.GoalInformation getDestination(MillVillager villager) throws Exception
/*    */   {
/* 17 */     List<Entity> mobs = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, EntityMob.class, villager.getTownHall().getPos(), 50, 10);
/*    */     
/* 19 */     if (mobs == null) {
/* 20 */       return null;
/*    */     }
/*    */     
/* 23 */     int bestDist = Integer.MAX_VALUE;
/* 24 */     Entity target = null;
/*    */     
/* 26 */     for (Entity ent : mobs)
/*    */     {
/* 28 */       if (((ent instanceof EntityMob)) && 
/* 29 */         (villager.getPos().distanceToSquared(ent) < bestDist) && (villager.getTownHall().getAltitude((int)ent.field_70165_t, (int)ent.field_70161_v) < ent.field_70163_u)) {
/* 30 */         target = ent;
/* 31 */         bestDist = (int)villager.getPos().distanceToSquared(ent);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 36 */     if (target == null) {
/* 37 */       return null;
/*    */     }
/*    */     
/* 40 */     return packDest(null, null, target);
/*    */   }
/*    */   
/*    */   public ItemStack[] getHeldItemsTravelling(MillVillager villager)
/*    */   {
/* 45 */     return new ItemStack[] { villager.getWeapon() };
/*    */   }
/*    */   
/*    */   public boolean isFightingGoal()
/*    */   {
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isPossibleSpecific(MillVillager villager) throws Exception
/*    */   {
/* 55 */     return getDestination(villager) != null;
/*    */   }
/*    */   
/*    */   public boolean isStillValidSpecific(MillVillager villager)
/*    */     throws Exception
/*    */   {
/* 61 */     if (villager.field_70170_p.func_72820_D() % 10L == 0L) {
/* 62 */       setVillagerDest(villager);
/*    */     }
/*    */     
/* 65 */     return villager.getGoalDestPoint() != null;
/*    */   }
/*    */   
/*    */   public boolean performAction(MillVillager villager) throws Exception
/*    */   {
/* 70 */     List<Entity> mobs = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, EntityMob.class, villager.getPos(), 4, 4);
/*    */     
/* 72 */     for (Entity ent : mobs) {
/* 73 */       if ((!ent.field_70128_L) && ((ent instanceof EntityMob)) && (villager.func_70685_l(ent))) {
/* 74 */         villager.setEntityToAttack(ent);
/* 75 */         if (MLN.LogGeneralAI >= 1) {
/* 76 */           MLN.major(this, "Attacking entity: " + ent);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 81 */     return true;
/*    */   }
/*    */   
/*    */   public int priority(MillVillager villager) throws Exception
/*    */   {
/* 86 */     return 50;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\GoalHuntMonster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
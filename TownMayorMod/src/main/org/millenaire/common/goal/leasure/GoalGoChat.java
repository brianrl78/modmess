/*     */ package org.millenaire.common.goal.leasure;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.Culture.CultureLanguage.Dialogue;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.goal.Goal;
/*     */ import org.millenaire.common.goal.Goal.GoalInformation;
/*     */ 
/*     */ public class GoalGoChat extends Goal
/*     */ {
/*  15 */   private final char[] chatColours = { 'f', '3', 'a', '7', 'c' };
/*     */   
/*     */   public GoalGoChat() {
/*  18 */     this.leasure = true;
/*     */   }
/*     */   
/*     */   public int actionDuration(MillVillager villager)
/*     */   {
/*  23 */     return 1000;
/*     */   }
/*     */   
/*     */   public Goal.GoalInformation getDestination(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  29 */     for (MillVillager v : villager.getTownHall().villagers) {
/*  30 */       if ((v != villager) && (Goal.gosocialise.key.equals(v.goalKey)) && (v.getPos().distanceToSquared(villager) < 25.0D)) {
/*  31 */         return packDest(null, null, v);
/*     */       }
/*     */     }
/*     */     
/*  35 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/*  40 */     return getDestination(villager) != null;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal()
/*     */   {
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   public void onAccept(MillVillager villager)
/*     */     throws Exception
/*     */   {
/*  51 */     Goal.GoalInformation info = getDestination(villager);
/*     */     
/*  53 */     if (info != null)
/*     */     {
/*  55 */       MillVillager target = (MillVillager)info.getTargetEnt();
/*     */       
/*  57 */       target.clearGoal();
/*  58 */       target.goalKey = this.key;
/*  59 */       target.setGoalDestEntity(villager);
/*     */       
/*  61 */       Culture.CultureLanguage.Dialogue dialog = villager.getCulture().getDialog(villager, target);
/*     */       
/*  63 */       if (dialog != null)
/*     */       {
/*  65 */         int role = dialog.validRoleFor(villager);
/*     */         
/*  67 */         villager.setGoalInformation(null);
/*  68 */         villager.setGoalDestEntity(target);
/*     */         
/*  70 */         char col = this.chatColours[MillCommonUtilities.randomInt(this.chatColours.length)];
/*     */         
/*  72 */         col = 'f';
/*     */         
/*  74 */         if (dialog != null)
/*     */         {
/*  76 */           List<Entity> entities = MillCommonUtilities.getEntitiesWithinAABB(villager.field_70170_p, MillVillager.class, villager.getPos(), 5, 5);
/*     */           
/*  78 */           boolean dialogueChat = true;
/*     */           
/*     */ 
/*  81 */           for (Entity ent : entities) {
/*  82 */             if ((ent != villager) && (ent != target)) {
/*  83 */               MillVillager v = (MillVillager)ent;
/*     */               
/*  85 */               if ((this.key.equals(v.goalKey)) && (v.dialogueChat)) {
/*  86 */                 dialogueChat = false;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*  91 */           villager.dialogueKey = dialog.key;
/*  92 */           villager.dialogueRole = role;
/*  93 */           villager.dialogueStart = villager.field_70170_p.func_72820_D();
/*  94 */           villager.dialogueColour = col;
/*  95 */           villager.dialogueChat = dialogueChat;
/*     */           
/*  97 */           target.dialogueKey = dialog.key;
/*  98 */           target.dialogueRole = (3 - role);
/*  99 */           target.dialogueStart = villager.field_70170_p.func_72820_D();
/* 100 */           target.dialogueColour = col;
/* 101 */           target.dialogueChat = dialogueChat;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 106 */     super.onAccept(villager);
/*     */   }
/*     */   
/*     */   public boolean performAction(MillVillager villager) throws Exception
/*     */   {
/* 111 */     return villager.dialogueKey == null;
/*     */   }
/*     */   
/*     */   public int priority(MillVillager villager) throws Exception
/*     */   {
/* 116 */     return 10;
/*     */   }
/*     */   
/*     */   public int range(MillVillager villager)
/*     */   {
/* 121 */     return 2;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\leasure\GoalGoChat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
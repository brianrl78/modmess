/*    */ package org.millenaire.common.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*    */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*    */ import net.minecraft.entity.ai.EntityAISwimming;
/*    */ import net.minecraft.entity.ai.EntityAITasks;
/*    */ import net.minecraft.entity.ai.EntityAIWander;
/*    */ import net.minecraft.entity.monster.EntitySkeleton;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityTargetedWitherSkeleton extends EntitySkeleton
/*    */ {
/*    */   public EntityTargetedWitherSkeleton(World par1World)
/*    */   {
/* 19 */     super(par1World);
/*    */     
/* 21 */     func_70088_a();
/*    */   }
/*    */   
/*    */   protected boolean func_70692_ba()
/*    */   {
/* 26 */     return false;
/*    */   }
/*    */   
/*    */   public void func_70088_a()
/*    */   {
/* 31 */     super.func_70088_a();
/*    */     
/* 33 */     func_82201_a(1);
/* 34 */     func_70062_b(0, new ItemStack(Items.field_151052_q));
/*    */     
/* 36 */     this.field_70714_bg.field_75782_a.clear();
/* 37 */     this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
/* 38 */     this.field_70714_bg.func_75776_a(5, new EntityAIWander(this, 1.0D));
/* 39 */     this.field_70714_bg.func_75776_a(6, new net.minecraft.entity.ai.EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
/* 40 */     this.field_70714_bg.func_75776_a(6, new net.minecraft.entity.ai.EntityAILookIdle(this));
/* 41 */     this.field_70714_bg.func_75776_a(4, new net.minecraft.entity.ai.EntityAIAttackOnCollide(this, EntityPlayer.class, 0.3100000023841858D, false));
/*    */     
/* 43 */     this.field_70715_bh.field_75782_a.clear();
/* 44 */     this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false));
/* 45 */     this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 10, true));
/*    */   }
/*    */   
/*    */   public void func_70636_d()
/*    */   {
/* 50 */     super.func_70636_d();
/*    */     
/* 52 */     func_70066_B();
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\entity\EntityTargetedWitherSkeleton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
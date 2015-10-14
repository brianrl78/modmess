/*    */ package org.millenaire.common.entity;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.monster.EntityGhast;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.Point;
/*    */ 
/*    */ public class EntityTargetedGhast extends EntityGhast
/*    */ {
/* 11 */   public Point target = null;
/*    */   
/*    */   public EntityTargetedGhast(World par1World) {
/* 14 */     super(par1World);
/*    */   }
/*    */   
/*    */   protected boolean func_70692_ba()
/*    */   {
/* 19 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_70020_e(NBTTagCompound par1nbtTagCompound)
/*    */   {
/* 25 */     super.func_70020_e(par1nbtTagCompound);
/*    */     
/* 27 */     this.target = Point.read(par1nbtTagCompound, "targetPoint");
/*    */   }
/*    */   
/*    */   protected void func_70626_be()
/*    */   {
/* 32 */     if (this.target != null)
/*    */     {
/* 34 */       if (this.target.distanceTo(this) > 20.0D) {
/* 35 */         this.field_70795_b = this.target.x;
/* 36 */         this.field_70796_c = this.target.y;
/* 37 */         this.field_70793_d = this.target.z;
/* 38 */         this.field_70797_a = 0;
/* 39 */       } else if (this.target.distanceTo(this) < 10.0D) {
/* 40 */         this.field_70795_b = (this.field_70165_t + (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 41 */         this.field_70796_c = (this.field_70163_u + (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 42 */         this.field_70793_d = (this.field_70161_v + (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 43 */         this.field_70797_a = 0;
/*    */       }
/*    */     }
/*    */     
/* 47 */     super.func_70626_be();
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_70109_d(NBTTagCompound par1nbtTagCompound)
/*    */   {
/* 53 */     super.func_70109_d(par1nbtTagCompound);
/*    */     
/* 55 */     if (this.target != null) {
/* 56 */       this.target.write(par1nbtTagCompound, "targetPoint");
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\entity\EntityTargetedGhast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.millenaire.common.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.monster.EntityBlaze;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.world.World;
/*    */ import org.millenaire.common.Point;
/*    */ 
/*    */ public class EntityTargetedBlaze extends EntityBlaze
/*    */ {
/* 12 */   public Point target = null;
/*    */   
/*    */   public EntityTargetedBlaze(World par1World) {
/* 15 */     super(par1World);
/*    */   }
/*    */   
/*    */   protected boolean func_70692_ba()
/*    */   {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
/* 24 */     double d4 = (this.target.x - this.field_70165_t) / par7;
/* 25 */     double d5 = (this.target.y - this.field_70163_u) / par7;
/* 26 */     double d6 = (this.target.z - this.field_70161_v) / par7;
/* 27 */     AxisAlignedBB axisalignedbb = this.field_70121_D.func_72329_c();
/*    */     
/* 29 */     for (int i = 1; i < par7; i++) {
/* 30 */       axisalignedbb.func_72317_d(d4, d5, d6);
/*    */       
/* 32 */       if (!this.field_70170_p.func_72945_a(this, axisalignedbb).isEmpty()) {
/* 33 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public boolean func_70026_G()
/*    */   {
/* 42 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_70020_e(NBTTagCompound par1nbtTagCompound)
/*    */   {
/* 48 */     super.func_70020_e(par1nbtTagCompound);
/*    */     
/* 50 */     this.target = Point.read(par1nbtTagCompound, "targetPoint");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void func_70626_be()
/*    */   {
/* 56 */     if ((this.target != null) && (this.target.distanceTo(this) > 20.0D))
/*    */     {
/* 58 */       double d0 = this.target.x - this.field_70165_t;
/* 59 */       double d1 = this.target.y - this.field_70163_u;
/* 60 */       double d2 = this.target.z - this.field_70161_v;
/* 61 */       double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*    */       
/* 63 */       if (isCourseTraversable(this.target.x, this.target.y, this.target.z, d3)) {
/* 64 */         this.field_70159_w += d0 / d3 * 0.1D;
/* 65 */         this.field_70181_x += d1 / d3 * 0.1D;
/* 66 */         this.field_70179_y += d2 / d3 * 0.1D;
/*    */       }
/*    */     }
/*    */     
/* 70 */     super.func_70626_be();
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_70109_d(NBTTagCompound par1nbtTagCompound)
/*    */   {
/* 76 */     super.func_70109_d(par1nbtTagCompound);
/*    */     
/* 78 */     if (this.target != null) {
/* 79 */       this.target.write(par1nbtTagCompound, "targetPoint");
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\entity\EntityTargetedBlaze.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
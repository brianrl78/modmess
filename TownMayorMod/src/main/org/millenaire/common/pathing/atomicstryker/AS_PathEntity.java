/*    */ package org.millenaire.common.pathing.atomicstryker;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.pathfinding.PathEntity;
/*    */ import net.minecraft.pathfinding.PathPoint;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AS_PathEntity
/*    */   extends PathEntity
/*    */ {
/* 17 */   private long timeLastPathIncrement = 0L;
/*    */   public final PathPoint[] pointsCopy;
/*    */   private int pathIndexCopy;
/*    */   
/*    */   public AS_PathEntity(PathPoint[] points) {
/* 22 */     super(points);
/* 23 */     this.timeLastPathIncrement = System.currentTimeMillis();
/* 24 */     this.pointsCopy = points;
/* 25 */     this.pathIndexCopy = 0;
/*    */   }
/*    */   
/*    */   public void advancePathIndex() {
/* 29 */     this.timeLastPathIncrement = System.currentTimeMillis();
/* 30 */     this.pathIndexCopy += 1;
/* 31 */     func_75872_c(this.pathIndexCopy);
/*    */   }
/*    */   
/*    */   public PathPoint getCurrentTargetPathPoint() {
/* 35 */     if (func_75879_b()) {
/* 36 */       return null;
/*    */     }
/* 38 */     return this.pointsCopy[func_75873_e()];
/*    */   }
/*    */   
/*    */   public PathPoint getFuturePathPoint(int jump) {
/* 42 */     if (func_75873_e() >= this.pointsCopy.length - jump) {
/* 43 */       return null;
/*    */     }
/* 45 */     return this.pointsCopy[(func_75873_e() + jump)];
/*    */   }
/*    */   
/*    */   public PathPoint getNextTargetPathPoint() {
/* 49 */     if (func_75873_e() >= this.pointsCopy.length - 1) {
/* 50 */       return null;
/*    */     }
/* 52 */     return this.pointsCopy[(func_75873_e() + 1)];
/*    */   }
/*    */   
/*    */   public PathPoint getPastTargetPathPoint(int jump) {
/* 56 */     if ((func_75873_e() < jump) || (this.pointsCopy.length == 0)) {
/* 57 */       return null;
/*    */     }
/* 59 */     return this.pointsCopy[(func_75873_e() - jump)];
/*    */   }
/*    */   
/*    */   public Vec3 func_75878_a(Entity var1)
/*    */   {
/* 64 */     if (super.func_75879_b()) {
/* 65 */       return null;
/*    */     }
/* 67 */     return super.func_75878_a(var1);
/*    */   }
/*    */   
/*    */   public PathPoint getPreviousTargetPathPoint() {
/* 71 */     if ((func_75873_e() < 1) || (this.pointsCopy.length == 0)) {
/* 72 */       return null;
/*    */     }
/* 74 */     return this.pointsCopy[(func_75873_e() - 1)];
/*    */   }
/*    */   
/*    */   public long getTimeSinceLastPathIncrement() {
/* 78 */     return System.currentTimeMillis() - this.timeLastPathIncrement;
/*    */   }
/*    */   
/*    */   public void func_75872_c(int par1)
/*    */   {
/* 83 */     this.timeLastPathIncrement = System.currentTimeMillis();
/* 84 */     this.pathIndexCopy = par1;
/* 85 */     super.func_75872_c(par1);
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AS_PathEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
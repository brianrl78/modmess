/*    */ package org.millenaire.common.pathing.atomicstryker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AStarConfig
/*    */ {
/* 14 */   public boolean canUseDoors = false;
/*    */   
/* 16 */   public boolean canTakeDiagonals = false;
/*    */   
/* 18 */   public boolean allowDropping = false;
/*    */   
/* 20 */   public boolean canSwim = false;
/*    */   
/* 22 */   public boolean tolerance = false;
/*    */   
/*    */ 
/* 25 */   public int toleranceHorizontal = 0;
/*    */   
/* 27 */   public int toleranceVertical = 0;
/*    */   
/*    */   public AStarConfig(boolean canUseDoors, boolean makePathDiagonals, boolean allowDropping, boolean canSwim) {
/* 30 */     this.canUseDoors = canUseDoors;
/* 31 */     this.canTakeDiagonals = makePathDiagonals;
/* 32 */     this.allowDropping = allowDropping;
/*    */   }
/*    */   
/*    */   public AStarConfig(boolean canUseDoors, boolean makePathDiagonals, boolean allowDropping, boolean canSwim, int toleranceHorizontal, int toleranceVertical) {
/* 36 */     this.canUseDoors = canUseDoors;
/* 37 */     this.canTakeDiagonals = makePathDiagonals;
/* 38 */     this.allowDropping = allowDropping;
/* 39 */     this.toleranceHorizontal = toleranceHorizontal;
/* 40 */     this.toleranceVertical = toleranceVertical;
/* 41 */     this.tolerance = true;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.millenaire.common.pathing;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathingPathCalcTile
/*    */ {
/*    */   public boolean ladder;
/*    */   
/*    */ 
/*    */   public boolean isWalkable;
/*    */   
/*    */   public short[] position;
/*    */   
/*    */ 
/*    */   public PathingPathCalcTile(boolean walkable, boolean lad, short[] pos)
/*    */   {
/* 17 */     this.ladder = lad;
/*    */     
/* 19 */     if (this.ladder == true) {
/* 20 */       this.isWalkable = false;
/* 21 */     } else if (((!this.ladder ? 1 : 0) & (walkable == true ? 1 : 0)) != 0) {
/* 22 */       this.isWalkable = true;
/*    */     }
/* 24 */     this.position = ((short[])pos.clone());
/*    */   }
/*    */   
/*    */   public PathingPathCalcTile(PathingPathCalcTile c)
/*    */   {
/* 29 */     this.ladder = c.ladder;
/* 30 */     this.isWalkable = c.isWalkable;
/* 31 */     this.position = ((short[])c.position.clone());
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\PathingPathCalcTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
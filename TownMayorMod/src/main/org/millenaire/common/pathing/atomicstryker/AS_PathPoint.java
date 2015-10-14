/*    */ package org.millenaire.common.pathing.atomicstryker;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import net.minecraft.pathfinding.PathPoint;
/*    */ import org.millenaire.common.MLN;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AS_PathPoint
/*    */   extends PathPoint
/*    */ {
/* 17 */   private static boolean init = false;
/*    */   private static Field fieldIndex;
/*    */   private static Field fieldTotalDistance;
/*    */   private static Field fieldDistanceToNext;
/*    */   private static Field fieldDistanceToTarget;
/*    */   private static Field fieldPrevPathPoint;
/*    */   
/*    */   public AS_PathPoint(int par1, int par2, int par3)
/*    */   {
/* 26 */     super(par1, par2, par3);
/*    */     
/* 28 */     if (!init) {
/* 29 */       Class ppClass = getClass().getSuperclass();
/* 30 */       fieldIndex = ppClass.getDeclaredFields()[4];
/* 31 */       fieldIndex.setAccessible(true);
/* 32 */       fieldTotalDistance = ppClass.getDeclaredFields()[5];
/* 33 */       fieldTotalDistance.setAccessible(true);
/* 34 */       fieldDistanceToNext = ppClass.getDeclaredFields()[6];
/* 35 */       fieldDistanceToNext.setAccessible(true);
/* 36 */       fieldDistanceToTarget = ppClass.getDeclaredFields()[7];
/* 37 */       fieldDistanceToTarget.setAccessible(true);
/* 38 */       fieldPrevPathPoint = ppClass.getDeclaredFields()[8];
/* 39 */       fieldPrevPathPoint.setAccessible(true);
/* 40 */       init = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void setDistanceToNext(float f) {
/* 45 */     setFieldValue(fieldDistanceToNext, Float.valueOf(f));
/*    */   }
/*    */   
/*    */   public void setDistanceToTarget(float f) {
/* 49 */     setFieldValue(fieldDistanceToTarget, Float.valueOf(f));
/*    */   }
/*    */   
/*    */   private void setFieldValue(Field f, Object v) {
/*    */     try {
/* 54 */       f.set(this, v);
/*    */     } catch (Exception e) {
/* 56 */       MLN.printException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setIndex(int i) {
/* 61 */     setFieldValue(fieldIndex, Integer.valueOf(i));
/*    */   }
/*    */   
/*    */   public void setPrevious(PathPoint pp) {
/* 65 */     setFieldValue(fieldPrevPathPoint, pp);
/*    */   }
/*    */   
/*    */   public void setTotalPathDistance(float f) {
/* 69 */     setFieldValue(fieldTotalDistance, Float.valueOf(f));
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AS_PathPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
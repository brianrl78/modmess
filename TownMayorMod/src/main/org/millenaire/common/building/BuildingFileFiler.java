/*    */ package org.millenaire.common.building;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class BuildingFileFiler implements java.io.FilenameFilter
/*    */ {
/*    */   String end;
/*    */   
/*    */   public BuildingFileFiler(String ending)
/*    */   {
/* 11 */     this.end = ending;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean accept(File file, String name)
/*    */   {
/* 17 */     if (!name.endsWith(this.end)) {
/* 18 */       return false;
/*    */     }
/*    */     
/* 21 */     if (name.startsWith(".")) {
/* 22 */       return false;
/*    */     }
/*    */     
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingFileFiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
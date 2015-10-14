/*    */ package org.millenaire.common.pathing;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
/*    */ import org.millenaire.common.MLN;
/*    */ 
/*    */ public class PathingTestMode
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 15 */     File file = new File(args[0]);
/*    */     try {
/* 17 */       BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/*    */       
/* 19 */       String s = reader.readLine();
/* 20 */       PathingPathCalcTile[][][] region = new PathingPathCalcTile[Integer.parseInt(s.split("/")[0])][Integer.parseInt(s.split("/")[1])][Integer.parseInt(s.split("/")[2])];
/* 21 */       s = reader.readLine();
/* 22 */       short j = 0;short i = 0;short line = 0;
/* 23 */       while ((s = reader.readLine()) != null) {
/* 24 */         line = (short)(line + 1);
/* 25 */         if (s.length() == 0) {
/* 26 */           j = (short)(j + 1);
/* 27 */           i = 0;
/* 28 */           System.out.println("New level on line " + line + ": " + j);
/*    */         } else {
/* 30 */           for (short k = 0; k < s.length(); k = (short)(k + 1)) {
/* 31 */             if (s.charAt(k) == 'x') {
/* 32 */               region[i][j][k] = new PathingPathCalcTile(false, false, new short[] { i, j, k });
/* 33 */             } else if (s.charAt(k) == 'w') {
/* 34 */               region[i][j][k] = new PathingPathCalcTile(true, false, new short[] { i, j, k });
/* 35 */             } else if (s.charAt(k) == 'l') {
/* 36 */               region[i][j][k] = new PathingPathCalcTile(true, true, new short[] { i, j, k });
/*    */             } else {
/* 38 */               region[i][j][k] = null;
/*    */             }
/*    */           }
/* 41 */           i = (short)(i + 1);
/*    */         }
/*    */       }
/*    */       
/* 45 */       reader.close();
/*    */       
/* 47 */       PathingSurface surface = new PathingSurface(region, region[(region.length / 2)][(region[0].length / 2)][(region[0][0].length / 2)]);
/*    */       
/* 49 */       System.out.println("Surface loaded.");
/*    */       
/* 51 */       file = new File(args[1]);
/* 52 */       reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 53 */       while ((s = reader.readLine().trim()) != null) {
/* 54 */         if (!s.startsWith("//")) {
/* 55 */           if (s.split(";").length > 1) {
/* 56 */             short[] start = { (short)Integer.parseInt(s.split(";")[0].split("/")[0]), (short)Integer.parseInt(s.split(";")[0].split("/")[1]), (short)Integer.parseInt(s.split(";")[0].split("/")[2]) };
/*    */             
/* 58 */             short[] end = { (short)Integer.parseInt(s.split(";")[1].split("/")[0]), (short)Integer.parseInt(s.split(";")[1].split("/")[1]), (short)Integer.parseInt(s.split(";")[1].split("/")[2]) };
/*    */             
/* 60 */             System.out.println("Calling getPath for: " + start[0] + "/" + start[1] + "/" + start[2] + " to " + end[0] + "/" + end[1] + "/" + end[2]);
/*    */             
/* 62 */             long startTime = System.nanoTime();
/* 63 */             List<short[]> binaryPath = surface.getPath(start, end);
/* 64 */             System.out.println("Time to calculate path (result: " + (binaryPath == null ? "null" : Integer.valueOf(binaryPath.size())) + "): " + (System.nanoTime() - startTime) / 1000000.0D);
/*    */           } else {
/* 66 */             short[] p = { (short)Integer.parseInt(s.split("/")[0]), (short)Integer.parseInt(s.split("/")[1]), (short)Integer.parseInt(s.split("/")[2]) };
/* 67 */             System.out.println("Calling contains for: " + p[0] + "/" + p[1] + "/" + p[2]);
/*    */             
/* 69 */             long startTime = System.nanoTime();
/* 70 */             boolean contains = surface.contains(p);
/* 71 */             System.out.println("Time to validate point (result: " + contains + "): " + (System.nanoTime() - startTime) / 1000000.0D);
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 76 */       reader.close();
/*    */     } catch (Exception e) {
/* 78 */       MLN.printException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\PathingTestMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
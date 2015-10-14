/*     */ package org.millenaire.common.pathing;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.Point;
/*     */ 
/*     */ public class PathingBinary
/*     */ {
/*     */   private PathingSurface surface;
/*     */   private int surfaceXstart;
/*     */   private int surfaceYstart;
/*     */   private int surfaceZstart;
/*     */   private final World world;
/*     */   private final HashMap<PathKey, CachedPath> cache;
/*     */   
/*     */   public static class CachedPath
/*     */   {
/*     */     PathingBinary.PathKey key;
/*     */     List<PathPoint> path;
/*     */     
/*     */     CachedPath(List<PathPoint> path)
/*     */     {
/*  30 */       this.path = path;
/*  31 */       this.key = new PathingBinary.PathKey((PathPoint)path.get(0), (PathPoint)path.get(path.size() - 1));
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/*  36 */       if (this == obj) {
/*  37 */         return true;
/*     */       }
/*  39 */       if (obj == null) {
/*  40 */         return false;
/*     */       }
/*  42 */       if (getClass() != obj.getClass()) {
/*  43 */         return false;
/*     */       }
/*  45 */       CachedPath other = (CachedPath)obj;
/*  46 */       return this.key.equals(other.key);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  51 */       return this.key.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PathKey {
/*  56 */     static boolean log = false;
/*     */     PathPoint start;
/*     */     PathPoint end;
/*     */     int hash;
/*     */     
/*  61 */     PathKey(PathPoint start, PathPoint end) { this.start = start;
/*  62 */       this.end = end;
/*  63 */       this.hash = (start.hashCode() + (end.hashCode() << 16));
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/*  68 */       if (obj == this) {
/*  69 */         return true;
/*     */       }
/*  71 */       if (!(obj instanceof PathKey)) {
/*  72 */         return false;
/*     */       }
/*  74 */       PathKey p = (PathKey)obj;
/*  75 */       return (this.hash == p.hash) && (this.start.equals(p.start)) && (this.end.equals(p.end));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  80 */       return this.hash;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  85 */       return this.start + ", " + this.end;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   private static final String EOL = System.getProperty("line.separator");
/*     */   
/*     */   public PathingBinary(World world) {
/*  99 */     this.world = world;
/* 100 */     this.cache = new HashMap();
/*     */   }
/*     */   
/*     */   public List<PathPoint> calculatePath(org.millenaire.common.building.Building building, org.millenaire.common.MillVillager villager, Point start, Point dest, boolean extraLog) throws Exception
/*     */   {
/* 105 */     long startTime = System.nanoTime();
/*     */     
/* 107 */     PathKey key = new PathKey(start.getPathPoint(), dest.getPathPoint());
/*     */     
/* 109 */     if (this.cache.containsKey(key)) {
/* 110 */       if (this.cache.get(key) == null) {
/* 111 */         return null;
/*     */       }
/* 113 */       return ((CachedPath)this.cache.get(key)).path;
/*     */     }
/*     */     
/* 116 */     short[] startCoord = { (short)(start.getiX() - this.surfaceXstart), (short)(building.getAltitude(start.getiX(), start.getiZ()) - this.surfaceYstart - 1), (short)(start.getiZ() - this.surfaceZstart) };
/*     */     
/* 118 */     short[] endCoord = { (short)(dest.getiX() - this.surfaceXstart), (short)(building.getAltitude(dest.getiX(), dest.getiZ()) - this.surfaceYstart - 1), (short)(dest.getiZ() - this.surfaceZstart) };
/*     */     
/* 120 */     startCoord = validatePoint(startCoord);
/* 121 */     if (startCoord == null) {
/* 122 */       if (MLN.LogConnections >= 1) {
/* 123 */         MLN.major(this, "No valid start found from " + start + " to " + dest + " for " + villager + ": " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */       }
/* 125 */       this.cache.put(key, null);
/* 126 */       return null;
/*     */     }
/*     */     
/* 129 */     endCoord = validatePoint(endCoord);
/* 130 */     if (endCoord == null) {
/* 131 */       if (MLN.LogConnections >= 1) {
/* 132 */         MLN.major(this, "No valid dest found from " + start + " to " + dest + " for " + villager + ": " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */       }
/* 134 */       this.cache.put(key, null);
/* 135 */       return null;
/*     */     }
/*     */     
/* 138 */     if (MLN.LogConnections >= 1) {
/* 139 */       MLN.major(this, "Time to find start and end: " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */     }
/* 141 */     startTime = System.nanoTime();
/*     */     
/* 143 */     if (MLN.DEV) {
/* 144 */       File file = new File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "paths_" + hashCode() + ".txt");
/* 145 */       FileWriter writer = new FileWriter(file, true);
/* 146 */       writer.write(startCoord[0] + "/" + startCoord[1] + "/" + startCoord[2] + ";" + endCoord[0] + "/" + endCoord[1] + "/" + endCoord[2] + EOL);
/* 147 */       writer.flush();
/* 148 */       writer.close();
/*     */     }
/*     */     
/* 151 */     List<short[]> binaryPath = this.surface.getPath(startCoord, endCoord);
/*     */     
/* 153 */     if (MLN.DEV) {
/* 154 */       File file = new File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "paths_" + hashCode() + ".txt");
/* 155 */       FileWriter writer = new FileWriter(file, true);
/* 156 */       writer.write("//result of getPath: " + (binaryPath == null ? "null" : Integer.valueOf(binaryPath.size())) + " time: " + (System.nanoTime() - startTime) / 1000000.0D + EOL);
/* 157 */       writer.flush();
/* 158 */       writer.close();
/*     */     }
/*     */     
/* 161 */     if (MLN.LogConnections >= 1) {
/* 162 */       MLN.major(this, "Time to calculate path from " + start + " to " + dest + " for " + villager + " with binary pathing (result: " + (binaryPath == null ? "null" : Integer.valueOf(binaryPath.size())) + "): " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */     }
/*     */     
/*     */ 
/* 166 */     if ((binaryPath == null) || (binaryPath.size() == 0)) {
/* 167 */       this.cache.put(key, null);
/* 168 */       return null;
/*     */     }
/*     */     
/* 171 */     List<PathPoint> path = new java.util.ArrayList();
/*     */     
/* 173 */     for (short[] p : binaryPath) {
/* 174 */       path.add(new PathPoint(p[0] + this.surfaceXstart, p[1] + this.surfaceYstart + 1, p[2] + this.surfaceZstart));
/*     */     }
/*     */     
/* 177 */     this.cache.put(key, new CachedPath(path));
/*     */     
/* 179 */     for (int i = 0; i < path.size() - 1; i++) {
/* 180 */       this.cache.put(new PathKey((PathPoint)path.get(i), (PathPoint)path.get(path.size() - 1)), new CachedPath(path.subList(i, path.size())));
/*     */     }
/*     */     
/* 183 */     return path;
/*     */   }
/*     */   
/*     */   public boolean isReady() {
/* 187 */     return this.surface != null;
/*     */   }
/*     */   
/*     */   public void updatePathing(Point centre, int hradius, int vradius) {
/* 191 */     long startTime = System.nanoTime();
/*     */     
/* 193 */     PathingPathCalcTile[][][] region = new PathingPathCalcTile[hradius * 2][vradius * 2][hradius * 2];
/*     */     
/* 195 */     this.surfaceXstart = (centre.getiX() - hradius);
/* 196 */     this.surfaceYstart = (centre.getiY() - vradius);
/* 197 */     this.surfaceZstart = (centre.getiZ() - hradius);
/*     */     
/* 199 */     for (short i = 0; i < region.length; i = (short)(i + 1)) {
/* 200 */       for (short j = 0; j < region[0].length; j = (short)(j + 1)) {
/* 201 */         for (short k = 0; k < region[0][0].length; k = (short)(k + 1)) {
/* 202 */           net.minecraft.block.Block block = this.world.func_147439_a(this.surfaceXstart + i, this.surfaceYstart + j, this.surfaceZstart + k);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 207 */           if ((block == Blocks.field_150358_i) || (block == Blocks.field_150355_j) || (block == Blocks.field_150353_l) || (block == Blocks.field_150356_k) || (block == Blocks.field_150422_aJ)) {
/* 208 */             region[i][j][k] = new PathingPathCalcTile(false, false, new short[] { i, j, k });
/* 209 */           } else if ((block == Blocks.field_150350_a) || (!block.func_149688_o().func_76230_c())) {
/* 210 */             region[i][j][k] = null;
/*     */           } else {
/* 212 */             region[i][j][k] = new PathingPathCalcTile(true, false, new short[] { i, j, k });
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 218 */     if (MLN.LogConnections >= 1) {
/* 219 */       MLN.major(this, "Time to generate region: " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */     }
/*     */     
/* 222 */     startTime = System.nanoTime();
/*     */     
/* 224 */     this.surface = new PathingSurface(region, region[hradius][vradius][hradius]);
/*     */     
/* 226 */     if (MLN.LogConnections >= 1) {
/* 227 */       MLN.major(this, "Time taken to compute surface: " + (System.nanoTime() - startTime) / 1000000.0D);
/*     */     }
/*     */     
/* 230 */     if (MLN.DEV)
/*     */     {
/* 232 */       File file = new File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "region_" + hashCode() + ".txt");
/*     */       try
/*     */       {
/* 235 */         BufferedWriter writer = org.millenaire.common.core.MillCommonUtilities.getWriter(file);
/* 236 */         writer.write(region.length + "/" + region[0].length + "/" + region[0][0].length + EOL);
/*     */         
/* 238 */         for (short j = 0; j < region[0].length; j = (short)(j + 1)) {
/* 239 */           writer.write(EOL);
/* 240 */           for (short i = 0; i < region.length; i = (short)(i + 1)) {
/* 241 */             String s = "";
/* 242 */             for (short k = 0; k < region[0][0].length; k = (short)(k + 1)) {
/* 243 */               if (region[i][j][k] == null) {
/* 244 */                 s = s + "-";
/* 245 */               } else if (region[i][j][k].ladder) {
/* 246 */                 s = s + "l";
/* 247 */               } else if (region[i][j][k].isWalkable) {
/* 248 */                 s = s + "w";
/*     */               } else {
/* 250 */                 s = s + "x";
/*     */               }
/*     */             }
/* 253 */             writer.write(s + EOL);
/*     */           }
/*     */         }
/* 256 */         writer.flush();
/* 257 */         writer.close();
/*     */       } catch (Exception e) {
/* 259 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/* 263 */     this.cache.clear();
/*     */   }
/*     */   
/*     */   private short[] validatePoint(short[] p)
/*     */   {
/* 268 */     if (MLN.LogConnections >= 1) {
/* 269 */       MLN.major(this, "Validating point: " + p[0] + "/" + p[1] + "/" + p[2]);
/*     */     }
/*     */     
/* 272 */     writeLine(p[0] + "/" + p[1] + "/" + p[2]);
/* 273 */     boolean res = this.surface.contains(p);
/* 274 */     writeLine("//result of surface.contains: " + res);
/*     */     
/* 276 */     if (res) {
/* 277 */       return p;
/*     */     }
/*     */     
/* 280 */     short[] newP = { p[0], p[1], p[2] };
/*     */     
/* 282 */     for (short i = 10; i > -10; i = (short)(i - 1)) {
/* 283 */       newP[1] = ((short)(p[1] + i));
/* 284 */       if (MLN.DEV) {
/* 285 */         writeLine(newP[0] + "/" + newP[1] + "/" + newP[2]);
/*     */       }
/* 287 */       res = this.surface.contains(newP);
/* 288 */       writeLine("//result of surface.contains: " + res);
/*     */       
/* 290 */       if (res) {
/* 291 */         if (MLN.LogConnections >= 1) {
/* 292 */           MLN.major(this, "Found valid point. offset: " + i);
/*     */         }
/* 294 */         return newP;
/*     */       }
/*     */     }
/* 297 */     for (short i = -2; i < 3; i = (short)(i + 1)) {
/* 298 */       newP[0] = ((short)(p[0] + i));
/* 299 */       for (short j = -2; j < 3; j = (short)(j + 1)) {
/* 300 */         newP[2] = ((short)(p[2] + j));
/* 301 */         if ((i != 0) || (j != 0)) {
/* 302 */           for (short k = 10; k > -10; k = (short)(k - 1))
/*     */           {
/*     */ 
/* 305 */             if ((org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(p[0] + i, p[1] + k, p[2] + j))) && (!org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(p[0] + i, p[1] + k + 1, p[2] + j))) && (!org.millenaire.common.core.MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(p[0] + i, p[1] + k + 2, p[2] + j))))
/*     */             {
/*     */ 
/* 308 */               newP[1] = ((short)(p[1] + k));
/* 309 */               if (MLN.DEV) {
/* 310 */                 writeLine(newP[0] + "/" + newP[1] + "/" + newP[2]);
/* 311 */                 res = this.surface.contains(newP);
/* 312 */                 writeLine("//result of surface.contains: " + res);
/*     */               }
/*     */               
/* 315 */               if (res) {
/* 316 */                 if (MLN.LogConnections >= 1) {
/* 317 */                   MLN.major(this, "Found valid point. offset: " + i + "/" + k + "/" + j);
/*     */                 }
/* 319 */                 return newP;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 326 */     return null;
/*     */   }
/*     */   
/*     */   private void writeLine(String s)
/*     */   {
/*     */     try {
/* 332 */       File file = new File(org.millenaire.common.forge.Mill.proxy.getBaseDir(), "paths_" + hashCode() + ".txt");
/* 333 */       FileWriter writer = new FileWriter(file, true);
/* 334 */       writer.write(s + EOL);
/* 335 */       writer.flush();
/* 336 */       writer.close();
/*     */     } catch (java.io.IOException e) {
/* 338 */       MLN.printException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\PathingBinary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
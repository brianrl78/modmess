/*     */ package org.millenaire.common.pathing;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathingSurface
/*     */ {
/*     */   public LinkedList<ExtendedPathTile> alltiles;
/*     */   
/*     */   public class ExtendedPathTile
/*     */     extends PathingPathCalcTile
/*     */     implements Comparable<ExtendedPathTile>
/*     */   {
/*     */     public LinkedList<ExtendedPathTile> neighbors;
/*     */     public short distance;
/*     */     public int key;
/*     */     
/*     */     public ExtendedPathTile(boolean walkable, boolean lad, short[] pos)
/*     */     {
/*  27 */       super(lad, pos);
/*  28 */       this.neighbors = new LinkedList();
/*  29 */       this.key = (pos[0] + (pos[1] << 10) + (pos[2] << 20));
/*  30 */       this.distance = Short.MAX_VALUE;
/*     */     }
/*     */     
/*     */     public ExtendedPathTile(PathingPathCalcTile c) {
/*  34 */       super();
/*  35 */       this.neighbors = new LinkedList();
/*  36 */       this.key = (c.position[0] + (c.position[1] << 10) + (c.position[2] << 20));
/*  37 */       this.distance = Short.MAX_VALUE;
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(ExtendedPathTile arg0)
/*     */     {
/*  43 */       if (this.key == arg0.key)
/*  44 */         return 0;
/*  45 */       if (this.key > arg0.key) {
/*  46 */         return 1;
/*     */       }
/*  48 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/*  55 */       if ((o == null) || (!(o instanceof ExtendedPathTile))) {
/*  56 */         return false;
/*     */       }
/*     */       
/*  59 */       return this.key == ((ExtendedPathTile)o).key;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  64 */       return this.key;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathingSurface(PathingPathCalcTile[][][] region, PathingPathCalcTile ct)
/*     */   {
/*  85 */     ExtendedPathTile[][][] surface = new ExtendedPathTile[region.length][region[0].length][region[0][0].length];
/*     */     
/*     */ 
/*     */ 
/*  89 */     for (int i = 0; i < region.length; i++) {
/*  90 */       for (int j = 0; j < region[0].length - 2; j++) {
/*  91 */         for (int k = 0; k < region[0][0].length; k++)
/*     */         {
/*     */ 
/*     */ 
/*  95 */           if ((region[i][j][k] != null) && (((j + 2 < region[0].length) && (region[i][(j + 1)][k] == null) && (region[i][(j + 2)][k] == null)) || ((region[i][j][k].ladder) && ((region[i][(j + 1)][k] == null) || (region[i][(j + 1)][k].ladder)))))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */             surface[i][j][k] = new ExtendedPathTile(region[i][j][k]);
/*     */           } else {
/* 108 */             surface[i][j][k] = null;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 113 */     ExtendedPathTile centraltile = new ExtendedPathTile(ct);
/*     */     
/*     */ 
/* 116 */     this.alltiles = new LinkedList();
/*     */     
/*     */ 
/*     */ 
/* 120 */     LinkedList<ExtendedPathTile> toprocess = new LinkedList();
/*     */     
/* 122 */     if (surface[centraltile.position[0]][centraltile.position[1]][centraltile.position[2]] != null) {
/* 123 */       toprocess.add(surface[centraltile.position[0]][centraltile.position[1]][centraltile.position[2]]); ExtendedPathTile 
/* 124 */         tmp337_336 = surface[centraltile.position[0]][centraltile.position[1]][centraltile.position[2]];tmp337_336.distance = ((short)(tmp337_336.distance - 1));
/*     */     }
/*     */     
/* 127 */     while (!toprocess.isEmpty())
/*     */     {
/* 129 */       ExtendedPathTile current = (ExtendedPathTile)toprocess.pollFirst();
/* 130 */       this.alltiles.add(current);
/* 131 */       short i = current.position[0];
/* 132 */       short j = current.position[1];
/* 133 */       short k = current.position[2];
/*     */       
/*     */ 
/* 136 */       for (byte t = -1; t <= 1; t = (byte)(t + 1))
/*     */       {
/*     */ 
/*     */ 
/* 140 */         if (surface[i][j][k].ladder) {
/* 141 */           if ((j + t >= 0) && (j + t < surface[0].length)) {
/* 142 */             if (surface[i][(j + 1)][k].ladder) {
/* 143 */               if (surface[i][(j + t)][k].distance == Short.MAX_VALUE) {
/* 144 */                 toprocess.add(surface[i][(j + t)][k]); ExtendedPathTile 
/* 145 */                   tmp519_518 = surface[i][(j + t)][k];tmp519_518.distance = ((short)(tmp519_518.distance - 1));
/*     */               }
/* 147 */               current.neighbors.add(surface[i][(j + t)][k]);
/*     */             }
/* 149 */             if (surface[i][(j - 1)][k] != null) {
/* 150 */               if (surface[i][(j + t)][k].distance == Short.MAX_VALUE) {
/* 151 */                 toprocess.add(surface[i][(j + t)][k]); ExtendedPathTile 
/* 152 */                   tmp620_619 = surface[i][(j + t)][k];tmp620_619.distance = ((short)(tmp620_619.distance - 1));
/*     */               }
/* 154 */               current.neighbors.add(surface[i][(j + t)][k]);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/* 159 */         else if ((j + t >= 0) && (j + t < surface[0].length)) {
/* 160 */           if ((i + 1 < surface.length) && 
/* 161 */             (surface[(i + 1)][(j + t)][k] != null))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */             if (surface[(i + 1)][(j + t)][k].ladder) {
/* 169 */               if ((t == 1) || ((t == 0) && (surface[(i + 1)][(j + t + 2)][k] == null))) {
/* 170 */                 if (surface[(i + 1)][(j + t)][k].distance == Short.MAX_VALUE) {
/* 171 */                   toprocess.add(surface[(i + 1)][(j + t)][k]); ExtendedPathTile 
/* 172 */                     tmp814_813 = surface[(i + 1)][(j + t)][k];tmp814_813.distance = ((short)(tmp814_813.distance - 1));
/*     */                 }
/* 174 */                 current.neighbors.add(surface[(i + 1)][(j + t)][k]);
/*     */               }
/*     */             } else {
/* 177 */               if (surface[(i + 1)][(j + t)][k].distance == Short.MAX_VALUE) {
/* 178 */                 toprocess.add(surface[(i + 1)][(j + t)][k]); ExtendedPathTile 
/* 179 */                   tmp911_910 = surface[(i + 1)][(j + t)][k];tmp911_910.distance = ((short)(tmp911_910.distance - 1));
/*     */               }
/*     */               
/*     */ 
/* 183 */               if (t == 0) {
/* 184 */                 current.neighbors.add(surface[(i + 1)][(j + t)][k]);
/* 185 */               } else if ((t == 1) && (surface[i][(j + 3)][k] == null)) {
/* 186 */                 current.neighbors.add(surface[(i + 1)][(j + t)][k]);
/* 187 */               } else if ((t == -1) && (surface[(i + 1)][(j + t + 3)][k] == null)) {
/* 188 */                 current.neighbors.add(surface[(i + 1)][(j + t)][k]);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 194 */           if ((i - 1 >= 0) && 
/* 195 */             (surface[(i - 1)][(j + t)][k] != null)) {
/* 196 */             if (surface[(i - 1)][(j + t)][k].ladder) {
/* 197 */               if ((t == 1) || ((t == 0) && (surface[(i - 1)][(j + t + 2)][k] == null))) {
/* 198 */                 if (surface[(i - 1)][(j + t)][k].distance == Short.MAX_VALUE) {
/* 199 */                   toprocess.add(surface[(i - 1)][(j + t)][k]); ExtendedPathTile 
/* 200 */                     tmp1188_1187 = surface[(i - 1)][(j + t)][k];tmp1188_1187.distance = ((short)(tmp1188_1187.distance - 1));
/*     */                 }
/* 202 */                 current.neighbors.add(surface[(i - 1)][(j + t)][k]);
/*     */               }
/*     */             } else {
/* 205 */               if (surface[(i - 1)][(j + t)][k].distance == Short.MAX_VALUE) {
/* 206 */                 toprocess.add(surface[(i - 1)][(j + t)][k]); ExtendedPathTile 
/* 207 */                   tmp1285_1284 = surface[(i - 1)][(j + t)][k];tmp1285_1284.distance = ((short)(tmp1285_1284.distance - 1));
/*     */               }
/* 209 */               if (t == 0) {
/* 210 */                 current.neighbors.add(surface[(i - 1)][(j + t)][k]);
/* 211 */               } else if ((t == 1) && (surface[i][(j + 3)][k] == null)) {
/* 212 */                 current.neighbors.add(surface[(i - 1)][(j + t)][k]);
/* 213 */               } else if ((t == -1) && (surface[(i - 1)][(j + t + 3)][k] == null)) {
/* 214 */                 current.neighbors.add(surface[(i - 1)][(j + t)][k]);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 220 */           if ((k - 1 >= 0) && 
/* 221 */             (surface[i][(j + t)][(k - 1)] != null)) {
/* 222 */             if (surface[i][(j + t)][(k - 1)].ladder) {
/* 223 */               if ((t == 1) || ((t == 0) && (surface[i][(j + t + 2)][(k - 1)] == null))) {
/* 224 */                 if (surface[i][(j + t)][(k - 1)].distance == Short.MAX_VALUE) {
/* 225 */                   toprocess.add(surface[i][(j + t)][(k - 1)]); ExtendedPathTile 
/* 226 */                     tmp1562_1561 = surface[i][(j + t)][(k - 1)];tmp1562_1561.distance = ((short)(tmp1562_1561.distance - 1));
/*     */                 }
/* 228 */                 current.neighbors.add(surface[i][(j + t)][(k - 1)]);
/*     */               }
/*     */             } else {
/* 231 */               if (surface[i][(j + t)][(k - 1)].distance == Short.MAX_VALUE) {
/* 232 */                 toprocess.add(surface[i][(j + t)][(k - 1)]); ExtendedPathTile 
/* 233 */                   tmp1659_1658 = surface[i][(j + t)][(k - 1)];tmp1659_1658.distance = ((short)(tmp1659_1658.distance - 1));
/*     */               }
/* 235 */               if (t == 0) {
/* 236 */                 current.neighbors.add(surface[i][(j + t)][(k - 1)]);
/* 237 */               } else if ((t == 1) && (surface[i][(j + 3)][k] == null)) {
/* 238 */                 current.neighbors.add(surface[i][(j + t)][(k - 1)]);
/* 239 */               } else if ((t == -1) && (surface[i][(j + t + 3)][(k - 1)] == null)) {
/* 240 */                 current.neighbors.add(surface[i][(j + t)][(k - 1)]);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 246 */           if ((k + 1 < surface[0][0].length) && 
/* 247 */             (surface[i][(j + t)][(k + 1)] != null)) {
/* 248 */             if (surface[i][(j + t)][(k + 1)].ladder) {
/* 249 */               if ((t == 1) || ((t == 0) && (surface[i][(j + t + 2)][(k + 1)] == null))) {
/* 250 */                 if (surface[i][(j + t)][(k + 1)].distance == Short.MAX_VALUE) {
/* 251 */                   toprocess.add(surface[i][(j + t)][(k + 1)]); ExtendedPathTile 
/* 252 */                     tmp1942_1941 = surface[i][(j + t)][(k + 1)];tmp1942_1941.distance = ((short)(tmp1942_1941.distance - 1));
/*     */                 }
/* 254 */                 current.neighbors.add(surface[i][(j + t)][(k + 1)]);
/*     */               }
/*     */             } else {
/* 257 */               if (surface[i][(j + t)][(k + 1)].distance == Short.MAX_VALUE) {
/* 258 */                 toprocess.add(surface[i][(j + t)][(k + 1)]); ExtendedPathTile 
/* 259 */                   tmp2039_2038 = surface[i][(j + t)][(k + 1)];tmp2039_2038.distance = ((short)(tmp2039_2038.distance - 1));
/*     */               }
/* 261 */               if (t == 0) {
/* 262 */                 current.neighbors.add(surface[i][(j + t)][(k + 1)]);
/* 263 */               } else if ((t == 1) && (surface[i][(j + 3)][k] == null)) {
/* 264 */                 current.neighbors.add(surface[i][(j + t)][(k + 1)]);
/* 265 */               } else if ((t == -1) && (surface[i][(j + t + 3)][(k + 1)] == null)) {
/* 266 */                 current.neighbors.add(surface[i][(j + t)][(k + 1)]);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 276 */     Collections.sort(this.alltiles);
/*     */   }
/*     */   
/*     */   public boolean contains(short[] pos) {
/* 280 */     boolean contains = false;
/*     */     
/*     */ 
/* 283 */     int targetkey = pos[0] + (pos[1] << 10) + (pos[2] << 20);
/* 284 */     int currentindex = this.alltiles.size() / 2;
/*     */     
/* 286 */     int change = currentindex;
/*     */     
/* 288 */     ExtendedPathTile current = (ExtendedPathTile)this.alltiles.get(currentindex);
/*     */     
/* 290 */     if (current.key == ((ExtendedPathTile)this.alltiles.get(0)).key) {
/* 291 */       current = (ExtendedPathTile)this.alltiles.get(0);
/*     */     } else {
/* 293 */       while ((current.key != targetkey) && (change > 1))
/*     */       {
/* 295 */         if (current.key > targetkey) {
/* 296 */           currentindex -= change / 2;
/* 297 */           change = (change + 1) / 2;
/*     */         }
/* 299 */         if (current.key < targetkey) {
/* 300 */           currentindex += change / 2;
/* 301 */           change = (change + 1) / 2;
/*     */         }
/* 303 */         current = (ExtendedPathTile)this.alltiles.get(currentindex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 308 */     if ((current.position[0] == pos[0]) && (current.position[1] == pos[1]) && (current.position[2] == pos[2])) {
/* 309 */       contains = true;
/*     */     }
/* 311 */     return contains;
/*     */   }
/*     */   
/*     */   public boolean containsSimple(short[] pos) {
/* 315 */     for (ExtendedPathTile tile : this.alltiles) {
/* 316 */       if ((tile.position[0] == pos[0]) && (tile.position[1] == pos[1]) && (tile.position[2] == pos[2])) {
/* 317 */         return true;
/*     */       }
/*     */     }
/* 320 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedList<short[]> getPath(short[] start, short[] target)
/*     */   {
/* 336 */     LinkedList<short[]> way = new LinkedList();
/*     */     
/*     */ 
/* 339 */     int targetkey = target[0] + (target[1] << 10) + (target[2] << 20);
/* 340 */     int currentindex = this.alltiles.size() / 2;
/*     */     
/* 342 */     int change = currentindex;
/*     */     
/* 344 */     ExtendedPathTile current = (ExtendedPathTile)this.alltiles.get(currentindex);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 349 */     if (targetkey == ((ExtendedPathTile)this.alltiles.get(0)).key) {
/* 350 */       current = (ExtendedPathTile)this.alltiles.get(0);
/*     */     } else {
/* 352 */       while ((current.key != targetkey) && (change > 1))
/*     */       {
/* 354 */         if (current.key > targetkey) {
/* 355 */           currentindex -= change / 2;
/* 356 */           change = (change + 1) / 2;
/*     */         }
/* 358 */         if (current.key < targetkey) {
/* 359 */           currentindex += change / 2;
/* 360 */           change = (change + 1) / 2;
/*     */         }
/* 362 */         current = (ExtendedPathTile)this.alltiles.get(currentindex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 368 */     LinkedList<ExtendedPathTile> processing = new LinkedList();
/* 369 */     LinkedList<ExtendedPathTile> processing2 = new LinkedList();
/*     */     
/* 371 */     Boolean wayfound = Boolean.valueOf(false);
/* 372 */     if ((current.position[0] == target[0]) && (current.position[1] == target[1]) && (current.position[2] == target[2])) {
/* 373 */       processing.add(current);
/* 374 */       processing2.add(current);
/* 375 */       current.distance = 0;
/*     */     } else {
/* 377 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 381 */     while (!processing.isEmpty()) {
/* 382 */       current = (ExtendedPathTile)processing.pollFirst();
/* 383 */       for (int i = 0; i < current.neighbors.size(); i++) {
/* 384 */         if (((ExtendedPathTile)current.neighbors.get(i)).distance > current.distance + 1) {
/* 385 */           ((ExtendedPathTile)current.neighbors.get(i)).distance = ((short)(current.distance + 1));
/* 386 */           processing.add(current.neighbors.get(i));
/* 387 */           processing2.add(current.neighbors.get(i));
/*     */         }
/*     */       }
/*     */       
/* 391 */       if ((current.position[0] == start[0]) && (current.position[1] == start[1]) && (current.position[2] == start[2])) {
/* 392 */         wayfound = Boolean.valueOf(true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 399 */     ExtendedPathTile nexttile = current;
/*     */     
/*     */ 
/* 402 */     if (wayfound.booleanValue()) {
/* 403 */       way.addLast(current.position);
/* 404 */       while (current.distance > 0) {
/* 405 */         for (int i = 0; i < current.neighbors.size(); i++) {
/* 406 */           if (((ExtendedPathTile)current.neighbors.get(i)).distance < nexttile.distance) {
/* 407 */             nexttile = (ExtendedPathTile)current.neighbors.get(i);
/*     */           }
/*     */         }
/* 410 */         current = nexttile;
/* 411 */         way.addLast(current.position);
/*     */       }
/*     */     }
/*     */     
/* 415 */     while (!processing2.isEmpty()) {
/* 416 */       current = (ExtendedPathTile)processing2.pollFirst();
/* 417 */       current.distance = 32766;
/*     */     }
/* 419 */     return way;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\PathingSurface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
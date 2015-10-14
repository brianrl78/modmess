/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ public class MillWorldInfo implements Cloneable
/*     */ {
/*     */   private static final int MAP_MARGIN = 10;
/*     */   private static final int BUILDING_MARGIN = 5;
/*     */   private static final int VALID_HEIGHT_DIFF = 10;
/*     */   public static final int UPDATE_FREQUENCY = 1000;
/*     */   
/*     */   public class UpdateThread extends Thread
/*     */   {
/*     */     int x;
/*     */     int z;
/*     */     
/*     */     public UpdateThread() {}
/*     */     
/*     */     public void run()
/*     */     {
/*  30 */       MillWorldInfo.this.updateChunk(this.x, this.z);
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
/*     */   public static boolean[][] booleanArrayDeepClone(boolean[][] source)
/*     */   {
/*  44 */     boolean[][] target = new boolean[source.length][];
/*     */     
/*  46 */     for (int i = 0; i < source.length; i++) {
/*  47 */       target[i] = ((boolean[])source[i].clone());
/*     */     }
/*     */     
/*  50 */     return target;
/*     */   }
/*     */   
/*     */   public static byte[][] byteArrayDeepClone(byte[][] source)
/*     */   {
/*  55 */     byte[][] target = new byte[source.length][];
/*     */     
/*  57 */     for (int i = 0; i < source.length; i++) {
/*  58 */       target[i] = ((byte[])source[i].clone());
/*     */     }
/*     */     
/*  61 */     return target;
/*     */   }
/*     */   
/*     */   public static boolean isForbiddenBlockForConstruction(Block block) {
/*  65 */     return (block == Blocks.field_150355_j) || (block == Blocks.field_150358_i) || (block == Blocks.field_150432_aD) || (block == Blocks.field_150356_k) || (block == Blocks.field_150353_l) || (block == Blocks.field_150344_f) || (block == Blocks.field_150347_e) || (block == Blocks.field_150336_V) || (block == Blocks.field_150486_ae) || (block == Blocks.field_150359_w) || (block == Mill.earth_decoration) || (block == Mill.stone_decoration) || (block == Mill.wood_decoration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static short[][] shortArrayDeepClone(short[][] source)
/*     */   {
/*  72 */     short[][] target = new short[source.length][];
/*     */     
/*  74 */     for (int i = 0; i < source.length; i++) {
/*  75 */       target[i] = ((short[])source[i].clone());
/*     */     }
/*     */     
/*  78 */     return target;
/*     */   }
/*     */   
/*  81 */   public int length = 0;
/*  82 */   public int width = 0;
/*  83 */   public int chunkStartX = 0; public int chunkStartZ = 0;
/*  84 */   public int mapStartX = 0; public int mapStartZ = 0;
/*  85 */   public int yBaseline = 0;
/*     */   
/*     */   public short[][] topGround;
/*     */   
/*     */   public short[][] constructionHeight;
/*     */   
/*     */   public short[][] spaceAbove;
/*     */   
/*     */   public boolean[][] danger;
/*     */   
/*     */   public boolean[][] buildingLoc;
/*     */   public boolean[][] canBuild;
/*     */   public boolean[][] buildingForbidden;
/*     */   public boolean[][] water;
/*     */   public boolean[][] tree;
/* 100 */   public boolean[][] buildTested = (boolean[][])null;
/*     */   
/*     */   public boolean[][] topAdjusted;
/*     */   
/*     */   public boolean[][] path;
/*     */   
/* 106 */   public int frequency = 10;
/*     */   
/* 108 */   private List<BuildingLocation> buildingLocations = new ArrayList();
/*     */   
/*     */   public BuildingLocation locationIP;
/* 111 */   public int nbLoc = 0;
/*     */   
/*     */ 
/*     */   public World world;
/*     */   
/*     */   public int lastUpdatedX;
/*     */   
/*     */   public int lastUpdatedZ;
/*     */   
/*     */   private int updateCounter;
/*     */   
/*     */ 
/*     */   public MillWorldInfo clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 126 */     MillWorldInfo o = (MillWorldInfo)super.clone();
/* 127 */     o.topGround = shortArrayDeepClone(this.topGround);
/* 128 */     o.constructionHeight = shortArrayDeepClone(this.constructionHeight);
/* 129 */     o.spaceAbove = shortArrayDeepClone(this.spaceAbove);
/* 130 */     o.danger = booleanArrayDeepClone(this.danger);
/* 131 */     o.buildingLoc = booleanArrayDeepClone(this.buildingLoc);
/* 132 */     o.canBuild = booleanArrayDeepClone(this.canBuild);
/* 133 */     o.buildingForbidden = booleanArrayDeepClone(this.buildingForbidden);
/* 134 */     o.water = booleanArrayDeepClone(this.water);
/* 135 */     o.tree = booleanArrayDeepClone(this.tree);
/* 136 */     o.path = booleanArrayDeepClone(this.path);
/* 137 */     o.buildingLocations = new ArrayList();
/* 138 */     o.buildingLocations.addAll(this.buildingLocations);
/* 139 */     return o;
/*     */   }
/*     */   
/*     */   private void createWorldInfo(List<BuildingLocation> locations, BuildingLocation blIP, int pstartX, int pstartZ, int endX, int endZ) throws MLN.MillenaireException
/*     */   {
/* 144 */     if (MLN.LogWorldInfo >= 2) {
/* 145 */       MLN.minor(this, "Creating world info: " + pstartX + "/" + pstartZ + "/" + endX + "/" + endZ);
/*     */     }
/*     */     
/* 148 */     this.chunkStartX = (pstartX >> 4);
/* 149 */     this.chunkStartZ = (pstartZ >> 4);
/* 150 */     this.mapStartX = (this.chunkStartX << 4);
/* 151 */     this.mapStartZ = (this.chunkStartZ << 4);
/*     */     
/* 153 */     this.length = (((endX >> 4) + 1 << 4) - this.mapStartX);
/* 154 */     this.width = (((endZ >> 4) + 1 << 4) - this.mapStartZ);
/*     */     
/* 156 */     this.frequency = ((int)Math.max(1000.0D / (this.length * this.width / 256), 10.0D));
/*     */     
/* 158 */     if (this.frequency == 0) {
/* 159 */       throw new MLN.MillenaireException("Null frequency in createWorldInfo.");
/*     */     }
/*     */     
/* 162 */     if (MLN.LogWorldInfo >= 1) {
/* 163 */       MLN.major(this, "Creating world info: " + this.mapStartX + "/" + this.mapStartZ + "/" + this.length + "/" + this.width);
/*     */     }
/*     */     
/* 166 */     this.topGround = new short[this.length][this.width];
/* 167 */     this.constructionHeight = new short[this.length][this.width];
/* 168 */     this.spaceAbove = new short[this.length][this.width];
/* 169 */     this.danger = new boolean[this.length][this.width];
/* 170 */     this.buildingLoc = new boolean[this.length][this.width];
/* 171 */     this.buildingForbidden = new boolean[this.length][this.width];
/* 172 */     this.canBuild = new boolean[this.length][this.width];
/* 173 */     this.buildTested = new boolean[this.length][this.width];
/* 174 */     this.water = new boolean[this.length][this.width];
/* 175 */     this.tree = new boolean[this.length][this.width];
/* 176 */     this.path = new boolean[this.length][this.width];
/* 177 */     this.topAdjusted = new boolean[this.length][this.width];
/*     */     
/* 179 */     this.buildingLocations = new ArrayList();
/*     */     
/* 181 */     for (int i = 0; i < this.length; i++) {
/* 182 */       for (int j = 0; j < this.width; j++) {
/* 183 */         this.buildingLoc[i][j] = 0;
/* 184 */         this.canBuild[i][j] = 0;
/*     */       }
/*     */     }
/*     */     
/* 188 */     for (BuildingLocation location : locations) {
/* 189 */       registerBuildingLocation(location);
/*     */     }
/*     */     
/*     */ 
/* 193 */     this.locationIP = blIP;
/* 194 */     if (this.locationIP != null) {
/* 195 */       registerBuildingLocation(this.locationIP);
/*     */     }
/*     */     
/* 198 */     for (int i = 0; i < this.length; i += 16) {
/* 199 */       for (int j = 0; j < this.width; j += 16) {
/* 200 */         updateChunk(i, j);
/*     */       }
/*     */     }
/* 203 */     this.lastUpdatedX = 0;
/* 204 */     this.lastUpdatedZ = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BuildingLocation getLocationAtCoordPlanar(Point p)
/*     */   {
/* 211 */     if ((this.locationIP != null) && (this.locationIP.isInsidePlanar(p))) {
/* 212 */       return this.locationIP;
/*     */     }
/*     */     
/* 215 */     for (BuildingLocation bl : this.buildingLocations) {
/* 216 */       if (bl.isInsidePlanar(p)) {
/* 217 */         return bl;
/*     */       }
/*     */     }
/*     */     
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isConstructionOrLoggingForbiddenHere(Point p)
/*     */   {
/* 226 */     if ((p.getiX() < this.mapStartX) || (p.getiZ() < this.mapStartZ) || (p.getiX() >= this.mapStartX + this.length) || (p.getiZ() >= this.mapStartZ + this.width)) {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     return this.buildingForbidden[(p.getiX() - this.mapStartX)][(p.getiZ() - this.mapStartZ)];
/*     */   }
/*     */   
/*     */   private void registerBuildingLocation(BuildingLocation bl)
/*     */   {
/* 235 */     if (MLN.LogWorldInfo >= 1) {
/* 236 */       MLN.major(this, "Registering building location: " + bl);
/*     */     }
/*     */     
/* 239 */     this.buildingLocations.add(bl);
/*     */     
/* 241 */     int sx = Math.max(bl.minxMargin - this.mapStartX, 0);
/* 242 */     int sz = Math.max(bl.minzMargin - this.mapStartZ, 0);
/* 243 */     int ex = Math.min(bl.maxxMargin - this.mapStartX, this.length + 1);
/* 244 */     int ez = Math.min(bl.maxzMargin - this.mapStartZ, this.width + 1);
/*     */     
/* 246 */     for (int i = sx; i < ex; i++) {
/* 247 */       for (int j = sz; j < ez; j++) {
/* 248 */         this.buildingLoc[i][j] = 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeBuildingLocation(BuildingLocation bl) {
/* 254 */     for (BuildingLocation l : this.buildingLocations) {
/* 255 */       if (l.isLocationSamePlace(bl)) {
/* 256 */         this.buildingLocations.remove(l);
/* 257 */         break;
/*     */       }
/*     */     }
/*     */     
/* 261 */     int sx = Math.max(bl.minxMargin - this.mapStartX, 0);
/* 262 */     int sz = Math.max(bl.minzMargin - this.mapStartZ, 0);
/* 263 */     int ex = Math.min(bl.maxxMargin - this.mapStartX, this.length);
/* 264 */     int ez = Math.min(bl.maxzMargin - this.mapStartZ, this.width);
/*     */     
/* 266 */     for (int i = sx; i < ex; i++) {
/* 267 */       for (int j = sz; j < ez; j++) {
/* 268 */         this.buildingLoc[i][j] = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean update(World world, List<BuildingLocation> locations, BuildingLocation blIP, Point centre, int radius) throws MLN.MillenaireException
/*     */   {
/* 275 */     this.world = world;
/* 276 */     this.yBaseline = centre.getiY();
/* 277 */     this.locationIP = blIP;
/*     */     
/* 279 */     if ((this.buildingLocations != null) && (this.buildingLocations.size() > 0) && (this.buildingLocations.size() == locations.size())) {
/* 280 */       this.buildingLocations = locations;
/* 281 */       updateNextChunk();
/* 282 */       return false;
/*     */     }
/*     */     
/* 285 */     int startX = centre.getiX();int startZ = centre.getiZ();int endX = centre.getiX();int endZ = centre.getiZ();
/*     */     
/* 287 */     BuildingLocation blStartX = null;BuildingLocation blStartZ = null;BuildingLocation blEndX = null;BuildingLocation blEndZ = null;
/*     */     
/* 289 */     for (BuildingLocation location : locations) {
/* 290 */       if (location != null) {
/* 291 */         if (location.pos.getiX() - location.length / 2 < startX) {
/* 292 */           startX = location.pos.getiX() - location.length / 2;
/* 293 */           blStartX = location;
/*     */         }
/* 295 */         if (location.pos.getiX() + location.length / 2 > endX) {
/* 296 */           endX = location.pos.getiX() + location.length / 2;
/* 297 */           blEndX = location;
/*     */         }
/* 299 */         if (location.pos.getiZ() - location.width / 2 < startZ) {
/* 300 */           startZ = location.pos.getiZ() - location.width / 2;
/* 301 */           blStartZ = location;
/*     */         }
/* 303 */         if (location.pos.getiZ() + location.width / 2 > endZ) {
/* 304 */           endZ = location.pos.getiZ() + location.width / 2;
/* 305 */           blEndZ = location;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 310 */     if (blIP != null) {
/* 311 */       if (blIP.pos.getiX() - blIP.length / 2 < startX) {
/* 312 */         startX = blIP.pos.getiX() - blIP.length / 2;
/* 313 */         blStartX = blIP;
/*     */       }
/* 315 */       if (blIP.pos.getiX() + blIP.length / 2 > endX) {
/* 316 */         endX = blIP.pos.getiX() + blIP.length / 2;
/* 317 */         blEndX = blIP;
/*     */       }
/* 319 */       if (blIP.pos.getiZ() - blIP.width / 2 < startZ) {
/* 320 */         startZ = blIP.pos.getiZ() - blIP.width / 2;
/* 321 */         blStartZ = blIP;
/*     */       }
/* 323 */       if (blIP.pos.getiZ() + blIP.width / 2 > endZ) {
/* 324 */         endZ = blIP.pos.getiZ() + blIP.width / 2;
/* 325 */         blEndZ = blIP;
/*     */       }
/*     */     }
/*     */     
/* 329 */     if (MLN.LogWorldInfo >= 1)
/*     */     {
/* 331 */       MLN.major(this, "WorldInfo Centre: " + centre);
/*     */       
/* 333 */       if (startX - 5 < centre.getiX() - radius - 10) {
/* 334 */         MLN.major(this, "Pushing startX down by " + (startX - 5 - (centre.getiX() - radius - 10)) + " due to " + blStartX);
/*     */       } else {
/* 336 */         MLN.major(this, "Using default value of " + (centre.getiX() - radius - 10) + " for startX");
/*     */       }
/*     */       
/* 339 */       if (startZ - 5 < centre.getiZ() - radius - 10) {
/* 340 */         MLN.major(this, "Pushing startZ down by " + (startZ - 5 - (centre.getiZ() - radius - 10)) + " due to " + blStartZ);
/*     */       } else {
/* 342 */         MLN.major(this, "Using default value of " + (centre.getiZ() - radius - 10) + " for startZ");
/*     */       }
/*     */       
/* 345 */       if (endX + 5 > centre.getiX() + radius + 10) {
/* 346 */         MLN.major(this, "Pushing endX up by " + (endX + 5 - (centre.getiX() + radius + 10)) + " due to " + blEndX);
/*     */       } else {
/* 348 */         MLN.major(this, "Using default value of " + (centre.getiX() + radius + 10) + " for endX");
/*     */       }
/*     */       
/* 351 */       if (endZ + 5 > centre.getiZ() + radius + 10) {
/* 352 */         MLN.major(this, "Pushing endZ up by " + (endZ + 5 - (centre.getiZ() + radius + 10)) + " due to " + blEndZ);
/*     */       } else {
/* 354 */         MLN.major(this, "Using default value of " + (centre.getiZ() + radius + 10) + " for endZ");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 359 */     startX = Math.min(startX - 5, centre.getiX() - radius - 10);
/* 360 */     startZ = Math.min(startZ - 5, centre.getiZ() - radius - 10);
/* 361 */     endX = Math.max(endX + 5, centre.getiX() + radius + 10);
/* 362 */     endZ = Math.max(endZ + 5, centre.getiZ() + radius + 10);
/*     */     
/* 364 */     if (MLN.LogWorldInfo >= 1) {
/* 365 */       MLN.major(this, "Values: " + startX + "/" + startZ + "/" + endX + "/" + endZ);
/*     */     }
/*     */     
/* 368 */     int chunkStartXTemp = startX >> 4;
/* 369 */     int chunkStartZTemp = startZ >> 4;
/* 370 */     int mapStartXTemp = chunkStartXTemp << 4;
/* 371 */     int mapStartZTemp = chunkStartZTemp << 4;
/* 372 */     int lengthTemp = ((endX >> 4) + 1 << 4) - mapStartXTemp;
/* 373 */     int widthTemp = ((endZ >> 4) + 1 << 4) - mapStartZTemp;
/*     */     
/* 375 */     if (MLN.LogWorldInfo >= 1) {
/* 376 */       MLN.major(this, "Values after chunks: " + mapStartXTemp + "/" + mapStartZTemp + "/" + (mapStartXTemp + lengthTemp) + "/" + (mapStartZTemp + widthTemp));
/*     */     }
/*     */     
/* 379 */     if ((lengthTemp != this.length) || (widthTemp != this.width)) {
/* 380 */       createWorldInfo(locations, blIP, startX, startZ, endX, endZ);
/* 381 */       return true;
/*     */     }
/*     */     
/* 384 */     this.buildingLocations = new ArrayList();
/* 385 */     for (BuildingLocation location : locations) {
/* 386 */       registerBuildingLocation(location);
/*     */     }
/*     */     
/* 389 */     updateNextChunk();
/* 390 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateChunk(int startX, int startZ)
/*     */   {
/* 399 */     for (int i = -1; i < 2; i++) {
/* 400 */       for (int j = -1; j < 2; j++) {
/* 401 */         if (!this.world.func_72863_F().func_73149_a((startX + this.mapStartX >> 4) + i, (startZ + this.mapStartZ >> 4) + j)) {
/* 402 */           if (MLN.LogWorldInfo >= 3) {
/* 403 */             MLN.debug(this, "Chunk is not loaded.");
/*     */           }
/* 405 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 410 */     Chunk chunk = this.world.func_72938_d(startX + this.mapStartX, startZ + this.mapStartZ);
/*     */     
/* 412 */     if (MLN.LogWorldInfo >= 3) {
/* 413 */       MLN.debug(this, "Updating chunk: " + startX + "/" + startZ + "/" + this.yBaseline + "/" + chunk.field_76635_g + "/" + chunk.field_76647_h);
/*     */     }
/*     */     
/* 416 */     for (int i = 0; i < 16; i++) {
/* 417 */       for (int j = 0; j < 16; j++) {
/* 418 */         short miny = (short)Math.max(this.yBaseline - 25, 1);
/* 419 */         short maxy = (short)Math.min(this.yBaseline + 25, 255);
/*     */         
/* 421 */         int mx = i + startX;
/* 422 */         int mz = j + startZ;
/*     */         
/* 424 */         this.canBuild[mx][mz] = 0;
/* 425 */         this.buildingForbidden[mx][mz] = 0;
/* 426 */         this.water[mx][mz] = 0;
/* 427 */         this.topAdjusted[mx][mz] = 0;
/*     */         
/* 429 */         short y = maxy;
/*     */         
/*     */ 
/*     */ 
/* 433 */         short ceilingSize = 0;
/* 434 */         Block tblock = chunk.func_150810_a(i, y, j);
/*     */         
/* 436 */         while ((y >= miny) && (!MillCommonUtilities.isBlockIdGround(tblock))) {
/* 437 */           if (MillCommonUtilities.isBlockIdGroundOrCeiling(tblock)) {
/* 438 */             ceilingSize = (short)(ceilingSize + 1);
/*     */           } else {
/* 440 */             ceilingSize = 0;
/*     */           }
/*     */           
/* 443 */           y = (short)(y - 1);
/*     */           
/* 445 */           if (ceilingSize > 3) {
/*     */             break;
/*     */           }
/*     */           
/* 449 */           tblock = chunk.func_150810_a(i, y, j);
/*     */         }
/*     */         
/* 452 */         this.constructionHeight[mx][mz] = y;
/*     */         
/* 454 */         boolean heightDone = false;
/*     */         Block block;
/* 456 */         if ((y <= maxy) && (y > 1)) {
/* 457 */           block = chunk.func_150810_a(i, y, j);
/*     */         } else {
/* 459 */           block = null;
/*     */         }
/*     */         
/* 462 */         boolean onground = true;
/*     */         
/* 464 */         short lastLiquid = -1;
/*     */         
/* 466 */         while ((block != null) && ((MillCommonUtilities.isBlockIdSolid(block)) || (MillCommonUtilities.isBlockIdLiquid(block)) || (!onground)))
/*     */         {
/* 468 */           if (block == Blocks.field_150364_r) {
/* 469 */             heightDone = true;
/* 470 */           } else if (!heightDone) {
/* 471 */             int tmp473_471 = mz; short[] tmp473_470 = this.constructionHeight[mx];tmp473_470[tmp473_471] = ((short)(tmp473_470[tmp473_471] + 1));
/*     */           } else {
/* 473 */             heightDone = true;
/*     */           }
/*     */           
/* 476 */           if (isForbiddenBlockForConstruction(block)) {
/* 477 */             this.buildingForbidden[mx][mz] = 1;
/*     */           }
/*     */           
/* 480 */           if (MillCommonUtilities.isBlockIdLiquid(block)) {
/* 481 */             onground = false;
/* 482 */             lastLiquid = y;
/* 483 */           } else if (MillCommonUtilities.isBlockIdSolid(block)) {
/* 484 */             onground = true;
/*     */           }
/*     */           
/* 487 */           y = (short)(y + 1);
/*     */           
/* 489 */           if ((y <= maxy) && (y > 1)) {
/* 490 */             block = chunk.func_150810_a(i, y, j);
/*     */           } else {
/* 492 */             block = null;
/*     */           }
/*     */         }
/*     */         
/* 496 */         if (!onground) {
/* 497 */           y = lastLiquid;
/*     */         }
/*     */         
/* 500 */         while ((y <= maxy) && (y > 1) && ((MillCommonUtilities.isBlockIdSolid(chunk.func_150810_a(i, y, j))) || (MillCommonUtilities.isBlockIdSolid(chunk.func_150810_a(i, y + 1, j))))) {
/* 501 */           y = (short)(y + 1);
/*     */         }
/*     */         
/* 504 */         y = (short)(byte)Math.max(1, y);
/*     */         
/* 506 */         this.topGround[mx][mz] = y;
/* 507 */         this.spaceAbove[mx][mz] = 0;
/*     */         
/* 509 */         Block soilBlock = chunk.func_150810_a(i, y - 1, j);
/* 510 */         Block block = chunk.func_150810_a(i, y, j);
/*     */         
/* 512 */         if ((block == Blocks.field_150358_i) || (block == Blocks.field_150355_j)) {
/* 513 */           this.water[mx][mz] = 1;
/*     */         }
/*     */         
/* 516 */         if (soilBlock == Blocks.field_150364_r) {
/* 517 */           this.tree[mx][mz] = 1;
/*     */         } else {
/* 519 */           this.tree[mx][mz] = 0;
/*     */         }
/*     */         
/* 522 */         if ((soilBlock == Mill.path) || (soilBlock == Mill.pathSlab)) {
/* 523 */           this.path[mx][mz] = 1;
/*     */         } else {
/* 525 */           this.path[mx][mz] = 0;
/*     */         }
/*     */         
/* 528 */         boolean blocked = false;
/*     */         
/* 530 */         if ((soilBlock != Blocks.field_150422_aJ) && (!MillCommonUtilities.isBlockIdSolid(block)) && (block != Blocks.field_150358_i) && (soilBlock != Blocks.field_150355_j)) {
/* 531 */           this.spaceAbove[mx][mz] = 1;
/*     */         } else {
/* 533 */           blocked = true;
/*     */         }
/*     */         
/* 536 */         if ((block == Blocks.field_150356_k) || (block == Blocks.field_150353_l)) {
/* 537 */           if (MLN.LogWorldInfo >= 3) {
/* 538 */             MLN.debug(this, "Found danger: " + block);
/*     */           }
/* 540 */           this.danger[mx][mz] = 1;
/*     */         } else {
/* 542 */           this.danger[mx][mz] = 0;
/* 543 */           for (Block forbiddenBlock : MLN.forbiddenBlocks) {
/* 544 */             if (forbiddenBlock == block) {
/* 545 */               this.danger[mx][mz] = 1;
/*     */             }
/* 547 */             if (soilBlock == block) {
/* 548 */               this.danger[mx][mz] = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 553 */         if ((this.danger[mx][mz] == 0) && (this.buildingLoc[mx][mz] == 0) && 
/* 554 */           (this.constructionHeight[mx][mz] > this.yBaseline - 10) && (this.constructionHeight[mx][mz] < this.yBaseline + 10)) {
/* 555 */           this.canBuild[mx][mz] = 1;
/*     */         }
/*     */         
/*     */ 
/* 559 */         if (isForbiddenBlockForConstruction(block)) {
/* 560 */           this.buildingForbidden[mx][mz] = 1;
/*     */         }
/*     */         
/* 563 */         y = (short)(y + 1);
/*     */         
/* 565 */         while ((y < maxy) && (y > 0)) {
/* 566 */           block = chunk.func_150810_a(i, y, j);
/*     */           
/* 568 */           if ((!blocked) && (this.spaceAbove[mx][mz] < 3) && (!MillCommonUtilities.isBlockIdSolid(block))) {
/* 569 */             int tmp1159_1157 = mz; short[] tmp1159_1156 = this.spaceAbove[mx];tmp1159_1156[tmp1159_1157] = ((short)(tmp1159_1156[tmp1159_1157] + 1));
/*     */           } else {
/* 571 */             blocked = true;
/*     */           }
/*     */           
/* 574 */           if (isForbiddenBlockForConstruction(block)) {
/* 575 */             this.buildingForbidden[mx][mz] = 1;
/*     */           }
/*     */           
/* 578 */           y = (short)(y + 1);
/*     */         }
/*     */         
/* 581 */         if (this.buildingForbidden[mx][mz] != 0) {
/* 582 */           this.canBuild[mx][mz] = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 595 */     boolean gapFilled = true;
/*     */     
/* 597 */     while (gapFilled) {
/* 598 */       gapFilled = false;
/* 599 */       for (int i = -5; i < 21; i++) {
/* 600 */         for (int j = -5; j < 21; j++) {
/* 601 */           int mx = i + startX;
/* 602 */           int mz = j + startZ;
/*     */           
/* 604 */           if ((mz >= 0) && (mz < this.width) && 
/* 605 */             (mx > 1) && (mx < this.length - 1) && 
/* 606 */             (Math.abs(this.topGround[(mx - 1)][mz] - this.topGround[(mx + 1)][mz]) < 2) && ((this.topGround[(mx - 1)][mz] + 2 < this.topGround[mx][mz]) || (this.topGround[(mx + 1)][mz] + 2 < this.topGround[mx][mz])))
/*     */           {
/* 608 */             short ntg = this.topGround[(mx - 1)][mz];
/* 609 */             boolean samesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg, startZ + this.mapStartZ + j));
/* 610 */             boolean belowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 1, startZ + this.mapStartZ + j));
/* 611 */             boolean below2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 2, startZ + this.mapStartZ + j));
/* 612 */             boolean abovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 1, startZ + this.mapStartZ + j));
/* 613 */             boolean above2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 2, startZ + this.mapStartZ + j));
/* 614 */             boolean above3solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 3, startZ + this.mapStartZ + j));
/*     */             
/*     */ 
/* 617 */             if ((Math.abs(this.topGround[(mx - 1)][mz] - this.topGround[(mx + 1)][mz]) < 2) && (belowsolid) && (!samesolid) && (!abovesolid))
/*     */             {
/*     */ 
/* 620 */               this.topGround[mx][mz] = ntg;
/* 621 */               if (!above2solid) {
/* 622 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 624 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 626 */               gapFilled = true;
/* 627 */               this.topAdjusted[mx][mz] = 1;
/* 628 */             } else if ((this.topGround[(mx + 1)][mz] <= this.topGround[(mx - 1)][mz]) && (below2solid) && (!belowsolid) && (!samesolid) && (!abovesolid))
/*     */             {
/*     */ 
/* 631 */               this.topGround[mx][mz] = ((short)(ntg - 1));
/* 632 */               if (!abovesolid) {
/* 633 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 635 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 637 */               gapFilled = true;
/* 638 */               this.topAdjusted[mx][mz] = 1;
/* 639 */             } else if ((this.topGround[(mx + 1)][mz] >= this.topGround[(mx - 1)][mz]) && (samesolid) && (!abovesolid) && (!above2solid))
/*     */             {
/*     */ 
/* 642 */               this.topGround[mx][mz] = ((short)(ntg + 1));
/* 643 */               if (!above3solid) {
/* 644 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 646 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 648 */               gapFilled = true;
/* 649 */               this.topAdjusted[mx][mz] = 1;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 654 */           if ((mx >= 0) && (mx < this.length) && 
/* 655 */             (mz > 1) && (mz < this.width - 1) && 
/* 656 */             (Math.abs(this.topGround[mx][(mz - 1)] - this.topGround[mx][(mz + 1)]) < 3) && ((this.topGround[mx][(mz - 1)] + 2 < this.topGround[mx][mz]) || (this.topGround[mx][(mz + 1)] + 2 < this.topGround[mx][mz])))
/*     */           {
/* 658 */             short ntg = this.topGround[mx][(mz - 1)];
/* 659 */             boolean samesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg, startZ + this.mapStartZ + j));
/* 660 */             boolean belowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 1, startZ + this.mapStartZ + j));
/* 661 */             boolean below2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 2, startZ + this.mapStartZ + j));
/* 662 */             boolean abovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 1, startZ + this.mapStartZ + j));
/* 663 */             boolean above2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 2, startZ + this.mapStartZ + j));
/* 664 */             boolean above3solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 3, startZ + this.mapStartZ + j));
/*     */             
/*     */ 
/* 667 */             if ((Math.abs(this.topGround[mx][(mz - 1)] - this.topGround[mx][(mz + 1)]) < 2) && (belowsolid) && (!samesolid) && (!abovesolid))
/*     */             {
/*     */ 
/* 670 */               this.topGround[mx][mz] = ntg;
/* 671 */               if (!above2solid) {
/* 672 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 674 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 676 */               gapFilled = true;
/* 677 */               this.topAdjusted[mx][mz] = 1;
/* 678 */             } else if ((this.topGround[mx][(mz + 1)] <= this.topGround[mx][(mz - 1)]) && (below2solid) && (!belowsolid) && (!samesolid) && (!abovesolid))
/*     */             {
/*     */ 
/* 681 */               this.topGround[mx][mz] = ((short)(ntg - 1));
/* 682 */               if (!abovesolid) {
/* 683 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 685 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 687 */               gapFilled = true;
/* 688 */               this.topAdjusted[mx][mz] = 1;
/* 689 */             } else if ((this.topGround[mx][(mz + 1)] >= this.topGround[mx][(mz - 1)]) && (samesolid) && (!abovesolid) && (!above2solid))
/*     */             {
/*     */ 
/* 692 */               this.topGround[mx][mz] = ((short)(ntg + 1));
/* 693 */               if (!above3solid) {
/* 694 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 696 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/* 698 */               gapFilled = true;
/* 699 */               this.topAdjusted[mx][mz] = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 711 */       for (int i = -5; i < 21; i++) {
/* 712 */         for (int j = -5; j < 21; j++) {
/* 713 */           int mx = i + startX;
/* 714 */           int mz = j + startZ;
/*     */           
/* 716 */           if ((mz >= 0) && (mz < this.width) && 
/* 717 */             (mx > 1) && (mx < this.length - 2) && 
/* 718 */             (this.topGround[(mx - 1)][mz] == this.topGround[(mx + 2)][mz]) && (this.topGround[(mx - 1)][mz] < this.topGround[mx][mz]) && (this.topGround[(mx - 1)][mz] < this.topGround[(mx + 1)][mz]))
/*     */           {
/* 720 */             short ntg = this.topGround[(mx - 1)][mz];
/* 721 */             boolean samesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg, startZ + this.mapStartZ + j));
/* 722 */             boolean belowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 1, startZ + this.mapStartZ + j));
/* 723 */             boolean abovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 1, startZ + this.mapStartZ + j));
/* 724 */             boolean above2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 2, startZ + this.mapStartZ + j));
/*     */             
/*     */ 
/*     */ 
/* 728 */             boolean nextsamesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i + 1, ntg, startZ + this.mapStartZ + j));
/* 729 */             boolean nextbelowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i + 1, ntg - 1, startZ + this.mapStartZ + j));
/* 730 */             boolean nextabovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i + 1, ntg + 1, startZ + this.mapStartZ + j));
/* 731 */             boolean nextabove2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i + 1, ntg + 2, startZ + this.mapStartZ + j));
/*     */             
/*     */ 
/* 734 */             if ((belowsolid) && (nextbelowsolid) && (!samesolid) && (!nextsamesolid) && (!abovesolid) && (!nextabovesolid))
/*     */             {
/*     */ 
/* 737 */               this.topGround[mx][mz] = ntg;
/* 738 */               this.topGround[(mx + 1)][mz] = ntg;
/* 739 */               if (!above2solid) {
/* 740 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 742 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/*     */               
/* 745 */               if (!nextabove2solid) {
/* 746 */                 this.spaceAbove[(mx + 1)][mz] = 3;
/*     */               } else {
/* 748 */                 this.spaceAbove[(mx + 1)][mz] = 2;
/*     */               }
/* 750 */               gapFilled = true;
/* 751 */               this.topAdjusted[mx][mz] = 1;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 756 */           if ((mx >= 0) && (mx < this.length) && 
/* 757 */             (mz > 1) && (mz < this.width - 2) && 
/* 758 */             (this.topGround[mx][(mz - 1)] == this.topGround[mx][(mz + 2)]) && (this.topGround[mx][(mz - 1)] < this.topGround[mx][mz]) && (this.topGround[mx][(mz - 1)] < this.topGround[mx][(mz + 1)]))
/*     */           {
/* 760 */             short ntg = this.topGround[mx][(mz - 1)];
/* 761 */             boolean samesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg, startZ + this.mapStartZ + j));
/* 762 */             boolean belowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 1, startZ + this.mapStartZ + j));
/* 763 */             boolean abovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 1, startZ + this.mapStartZ + j));
/* 764 */             boolean above2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 2, startZ + this.mapStartZ + j));
/*     */             
/*     */ 
/*     */ 
/* 768 */             boolean nextsamesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg, startZ + this.mapStartZ + j + 1));
/* 769 */             boolean nextbelowsolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg - 1, startZ + this.mapStartZ + j + 1));
/* 770 */             boolean nextabovesolid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 1, startZ + this.mapStartZ + j + 1));
/* 771 */             boolean nextabove2solid = MillCommonUtilities.isBlockIdSolid(this.world.func_147439_a(startX + this.mapStartX + i, ntg + 2, startZ + this.mapStartZ + j + 1));
/*     */             
/*     */ 
/* 774 */             if ((belowsolid) && (nextbelowsolid) && (!samesolid) && (!nextsamesolid) && (!abovesolid) && (!nextabovesolid))
/*     */             {
/*     */ 
/* 777 */               this.topGround[mx][mz] = ntg;
/* 778 */               this.topGround[mx][(mz + 1)] = ntg;
/* 779 */               if (!above2solid) {
/* 780 */                 this.spaceAbove[mx][mz] = 3;
/*     */               } else {
/* 782 */                 this.spaceAbove[mx][mz] = 2;
/*     */               }
/*     */               
/* 785 */               if (!nextabove2solid) {
/* 786 */                 this.spaceAbove[mx][(mz + 1)] = 3;
/*     */               } else {
/* 788 */                 this.spaceAbove[mx][(mz + 1)] = 2;
/*     */               }
/* 790 */               gapFilled = true;
/* 791 */               this.topAdjusted[mx][mz] = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 800 */     for (int i = 0; i < 16; i++) {
/* 801 */       for (int j = 0; j < 16; j++)
/*     */       {
/* 803 */         int mx = i + startX;
/* 804 */         int mz = j + startZ;
/*     */         
/* 806 */         if (this.danger[mx][mz] != 0) {
/* 807 */           for (int k = -2; k < 3; k++) {
/* 808 */             for (int l = -2; l < 3; l++) {
/* 809 */               if ((k >= 0) && (l >= 0) && (k < this.length) && (l < this.width)) {
/* 810 */                 this.spaceAbove[mx][mz] = 0;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateNextChunk()
/*     */   {
/* 821 */     this.updateCounter = ((this.updateCounter + 1) % this.frequency);
/*     */     
/* 823 */     if (this.updateCounter != 0) {
/* 824 */       return;
/*     */     }
/*     */     
/* 827 */     this.lastUpdatedX += 1;
/* 828 */     if (this.lastUpdatedX * 16 >= this.length) {
/* 829 */       this.lastUpdatedX = 0;
/* 830 */       this.lastUpdatedZ += 1;
/*     */     }
/*     */     
/* 833 */     if (this.lastUpdatedZ * 16 >= this.width) {
/* 834 */       this.lastUpdatedZ = 0;
/*     */     }
/*     */     
/* 837 */     UpdateThread thread = new UpdateThread();
/* 838 */     thread.setPriority(1);
/* 839 */     thread.x = (this.lastUpdatedX << 4);
/* 840 */     thread.z = (this.lastUpdatedZ << 4);
/*     */     
/* 842 */     thread.start();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MillWorldInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
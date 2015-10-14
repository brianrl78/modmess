/*     */ package org.millenaire.common.pathing.atomicstryker;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
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
/*     */ public class AStarStatic
/*     */ {
/*  25 */   static final int[][] candidates = { { 0, 0, -1, 1 }, { 0, 0, 1, 1 }, { 0, 1, 0, 1 }, { 1, 0, 0, 1 }, { -1, 0, 0, 1 }, { 1, 1, 0, 2 }, { -1, 1, 0, 2 }, { 0, 1, 1, 2 }, { 0, 1, -1, 2 }, { 1, -1, 0, 1 }, { -1, -1, 0, 1 }, { 0, -1, 1, 1 }, { 0, -1, -1, 1 } };
/*     */   
/*     */ 
/*  28 */   static final int[][] candidates_allowdrops = { { 0, 0, -1, 1 }, { 0, 0, 1, 1 }, { 1, 0, 0, 1 }, { -1, 0, 0, 1 }, { 1, 1, 0, 2 }, { -1, 1, 0, 2 }, { 0, 1, 1, 2 }, { 0, 1, -1, 2 }, { 1, -1, 0, 1 }, { -1, -1, 0, 1 }, { 0, -1, 1, 1 }, { 0, -1, -1, 1 }, { 1, -2, 0, 1 }, { -1, -2, 0, 1 }, { 0, -2, 1, 1 }, { 0, -2, -1, 1 } };
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
/*     */   public static AStarNode[] getAccessNodesSorted(World worldObj, int workerX, int workerY, int workerZ, int posX, int posY, int posZ, AStarConfig config)
/*     */   {
/*  58 */     ArrayList<AStarNode> resultList = new ArrayList();
/*     */     
/*     */ 
/*  61 */     for (int xIter = -2; xIter <= 2; xIter++) {
/*  62 */       for (int zIter = -2; zIter <= 2; zIter++) {
/*  63 */         for (int yIter = -3; yIter <= 2; yIter++) {
/*  64 */           AStarNode check = new AStarNode(posX + xIter, posY + yIter, posZ + zIter, Math.abs(xIter) + Math.abs(yIter), null);
/*  65 */           if (isViable(worldObj, check, 1, config)) {
/*  66 */             resultList.add(check);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  72 */     Collections.sort(resultList);
/*     */     
/*  74 */     int count = 0;
/*  75 */     AStarNode[] returnVal = new AStarNode[resultList.size()];
/*  76 */     AStarNode check; while ((!resultList.isEmpty()) && ((check = (AStarNode)resultList.get(0)) != null)) {
/*  77 */       returnVal[count] = check;
/*  78 */       resultList.remove(0);
/*  79 */       count++;
/*     */     }
/*     */     
/*  82 */     return returnVal;
/*     */   }
/*     */   
/*     */   public static double getDistanceBetweenCoords(int x, int y, int z, int posX, int posY, int posZ) {
/*  86 */     return Math.sqrt(Math.pow(x - posX, 2.0D) + Math.pow(y - posY, 2.0D) + Math.pow(z - posZ, 2.0D));
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
/*     */   public static double getDistanceBetweenNodes(AStarNode a, AStarNode b)
/*     */   {
/*  99 */     return Math.sqrt(Math.pow(a.x - b.x, 2.0D) + Math.pow(a.y - b.y, 2.0D) + Math.pow(a.z - b.z, 2.0D));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double getEntityLandSpeed(EntityLiving entLiving)
/*     */   {
/* 110 */     return Math.sqrt(entLiving.field_70159_w * entLiving.field_70159_w + entLiving.field_70179_y * entLiving.field_70179_y);
/*     */   }
/*     */   
/*     */   public static int getIntCoordFromDoubleCoord(double input) {
/* 114 */     return MathHelper.func_76128_c(input);
/*     */   }
/*     */   
/*     */   public static boolean isLadder(World world, Block b, int x, int y, int z) {
/* 118 */     if (b != null) {
/* 119 */       return b.isLadder(world, x, y, z, null);
/*     */     }
/* 121 */     return false;
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
/*     */ 
/*     */   public static boolean isPassableBlock(World worldObj, int ix, int iy, int iz, AStarConfig config)
/*     */   {
/* 142 */     if (iy > 0) {
/* 143 */       Block block = worldObj.func_147439_a(ix, iy - 1, iz);
/*     */       
/* 145 */       if ((block == Blocks.field_150422_aJ) || (block == Blocks.field_150411_aY) || (block == Blocks.field_150386_bk)) {
/* 146 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 150 */     Block block = worldObj.func_147439_a(ix, iy, iz);
/* 151 */     if (block != null)
/*     */     {
/*     */ 
/* 154 */       if ((config.canUseDoors) && (
/* 155 */         (block == Blocks.field_150466_ao) || (block == Blocks.field_150396_be))) {
/* 156 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 160 */       return !block.func_149688_o().func_76220_a();
/*     */     }
/*     */     
/* 163 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isViable(World worldObj, AStarNode target, int yoffset, AStarConfig config)
/*     */   {
/* 170 */     return isViable(worldObj, target.x, target.y, target.z, yoffset, config);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isViable(World worldObj, int x, int y, int z, int yoffset, AStarConfig config)
/*     */   {
/* 193 */     Block block = worldObj.func_147439_a(x, y, z);
/*     */     
/* 195 */     if ((block == Blocks.field_150468_ap) && (isPassableBlock(worldObj, x, y + 1, z, config))) {
/* 196 */       return true;
/*     */     }
/*     */     
/* 199 */     if ((!isPassableBlock(worldObj, x, y, z, config)) || (!isPassableBlock(worldObj, x, y + 1, z, config)) || ((isPassableBlock(worldObj, x, y - 1, z, config)) && ((block != Blocks.field_150355_j) || (block != Blocks.field_150358_i))))
/*     */     {
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     if ((!config.canSwim) && ((block == Blocks.field_150355_j) || (block == Blocks.field_150358_i))) {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     if (yoffset < 0) {
/* 209 */       yoffset *= -1;
/*     */     }
/* 211 */     int ycheckhigher = 1;
/* 212 */     while (ycheckhigher <= yoffset) {
/* 213 */       if (!isPassableBlock(worldObj, x, y + yoffset, z, config)) {
/* 214 */         return false;
/*     */       }
/* 216 */       ycheckhigher++;
/*     */     }
/*     */     
/* 219 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AS_PathEntity translateAStarPathtoPathEntity(World world, List<AStarNode> input, AStarConfig config)
/*     */   {
/* 230 */     if (!config.canTakeDiagonals) {
/* 231 */       List<AStarNode> oldInput = input;
/* 232 */       input = new ArrayList();
/* 233 */       for (int i = 0; i < oldInput.size() - 1; i++) {
/* 234 */         input.add(oldInput.get(i));
/* 235 */         if ((((AStarNode)oldInput.get(i)).x != ((AStarNode)oldInput.get(i + 1)).x) && (((AStarNode)oldInput.get(i)).z != ((AStarNode)oldInput.get(i + 1)).z) && 
/* 236 */           (((AStarNode)oldInput.get(i)).y == ((AStarNode)oldInput.get(i + 1)).y))
/*     */         {
/*     */ 
/* 239 */           if ((!isPassableBlock(world, ((AStarNode)oldInput.get(i)).x, ((AStarNode)oldInput.get(i)).y - 1, ((AStarNode)oldInput.get(i + 1)).z, config)) && (isPassableBlock(world, ((AStarNode)oldInput.get(i)).x, ((AStarNode)oldInput.get(i)).y, ((AStarNode)oldInput.get(i + 1)).z, config)) && (isPassableBlock(world, ((AStarNode)oldInput.get(i)).x, ((AStarNode)oldInput.get(i)).y + 1, ((AStarNode)oldInput.get(i + 1)).z, config)))
/*     */           {
/*     */ 
/* 242 */             AStarNode newNode = new AStarNode(((AStarNode)oldInput.get(i)).x, ((AStarNode)oldInput.get(i)).y, ((AStarNode)oldInput.get(i + 1)).z, 0, null);
/* 243 */             input.add(newNode);
/*     */           }
/*     */           else {
/* 246 */             AStarNode newNode = new AStarNode(((AStarNode)oldInput.get(i + 1)).x, ((AStarNode)oldInput.get(i)).y, ((AStarNode)oldInput.get(i)).z, 0, null);
/* 247 */             input.add(newNode);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 254 */     AS_PathPoint[] points = new AS_PathPoint[input.size()];
/*     */     
/* 256 */     int i = 0;
/* 257 */     int size = input.size();
/*     */     
/*     */ 
/* 260 */     while (size > 0) {
/* 261 */       AStarNode reading = (AStarNode)input.get(size - 1);
/* 262 */       points[i] = new AS_PathPoint(reading.x, reading.y, reading.z);
/* 263 */       points[i].field_75842_i = (i == 0);
/* 264 */       points[i].setIndex(i);
/* 265 */       points[i].setTotalPathDistance(i);
/* 266 */       points[i].setDistanceToNext(1.0F);
/* 267 */       points[i].setDistanceToTarget(size);
/*     */       
/* 269 */       if (i > 0) {
/* 270 */         points[i].setPrevious(points[(i - 1)]);
/*     */       }
/*     */       
/*     */ 
/* 274 */       input.remove(size - 1);
/* 275 */       size--;
/* 276 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 282 */     return new AS_PathEntity(points);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarStatic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
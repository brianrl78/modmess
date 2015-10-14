/*     */ package org.millenaire.common.pathing.atomicstryker;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.PriorityQueue;
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
/*     */ public class AStarWorkerJPS
/*     */   extends AStarWorker
/*     */ {
/*     */   private static final int MAX_SKIP_DISTANCE = 25;
/*  21 */   private static final int[][] neighbourOffsets = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
/*     */   private final PriorityQueue<AStarNode> openQueue;
/*     */   private AStarNode currentNode;
/*     */   
/*     */   public AStarWorkerJPS(AStarPathPlanner creator)
/*     */   {
/*  27 */     super(creator);
/*  28 */     this.openQueue = new PriorityQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addOrUpdateNode(AStarNode newNode)
/*     */   {
/*  40 */     boolean found = false;
/*  41 */     Iterator<AStarNode> iter = this.closedNodes.iterator();
/*  42 */     while (iter.hasNext()) {
/*  43 */       AStarNode toUpdate = (AStarNode)iter.next();
/*     */       
/*  45 */       if (newNode.equals(toUpdate)) {
/*  46 */         toUpdate.updateDistance(newNode.getG(), newNode.parent);
/*  47 */         found = true;
/*  48 */         break;
/*     */       }
/*     */     }
/*  51 */     if (!found)
/*     */     {
/*  53 */       this.openQueue.offer(newNode);
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
/*     */   private ArrayList<AStarNode> backTrace(AStarNode start)
/*     */   {
/*  66 */     ArrayList<AStarNode> foundpath = new ArrayList();
/*  67 */     foundpath.add(this.currentNode);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */     while (!this.currentNode.equals(start)) {
/*  78 */       int x = this.currentNode.x;
/*  79 */       int y = this.currentNode.y;
/*  80 */       int z = this.currentNode.z;
/*  81 */       int px = this.currentNode.parent.x;
/*  82 */       int pz = this.currentNode.parent.z;
/*     */       
/*     */ 
/*  85 */       int dx = (px - x) / Math.max(Math.abs(x - px), 1);
/*  86 */       int dz = (pz - z) / Math.max(Math.abs(z - pz), 1);
/*     */       
/*  88 */       x += dx;
/*  89 */       z += dz;
/*     */       
/*  91 */       while ((x != px) || (z != pz)) {
/*  92 */         y = getGroundNodeHeight(x, y, z);
/*  93 */         foundpath.add(new AStarNode(x, y, z, 0, null));
/*  94 */         x += dx;
/*  95 */         z += dz;
/*     */       }
/*     */       
/*  98 */       foundpath.add(this.currentNode.parent);
/*  99 */       this.currentNode = this.currentNode.parent;
/*     */     }
/* 101 */     return foundpath;
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
/*     */   private ArrayList<AStarNode> findNeighbours(AStarNode node)
/*     */   {
/* 114 */     ArrayList<AStarNode> r = new ArrayList();
/*     */     
/* 116 */     int x = node.x;
/* 117 */     int y = node.y;
/* 118 */     int z = node.z;
/* 119 */     int dist = node.getG();
/*     */     
/* 121 */     if (node.parent != null) {
/* 122 */       int px = node.parent.x;
/* 123 */       int py = node.parent.y;
/* 124 */       int pz = node.parent.z;
/* 125 */       boolean stairs = py != y;
/*     */       
/*     */ 
/*     */ 
/* 129 */       int dx = (x - px) / Math.max(Math.abs(x - px), 1);
/* 130 */       int dz = (z - pz) / Math.max(Math.abs(z - pz), 1);
/*     */       
/* 132 */       if ((dx != 0) && (dz != 0))
/*     */       {
/* 134 */         if (stairs) {
/* 135 */           return getAllNeighborsWithoutParent(x, y, z, dx, dz, node);
/*     */         }
/*     */         
/* 138 */         int left = 0;
/* 139 */         int right = 0;
/* 140 */         int nY = getGroundNodeHeight(x, y, z + dz);
/* 141 */         if (nY > 0) {
/* 142 */           left = nY;
/* 143 */           r.add(new AStarNode(x, nY, z + dz, dist + 1, node));
/*     */         }
/* 145 */         nY = getGroundNodeHeight(x + dx, y, z);
/* 146 */         if (nY > 0) {
/* 147 */           right = nY;
/* 148 */           r.add(new AStarNode(x + dx, nY, z, dist + 1, node));
/*     */         }
/* 150 */         if ((left != 0) || (right != 0)) {
/* 151 */           r.add(new AStarNode(x + dx, Math.max(left, right), z + dz, dist + 2, node));
/*     */         }
/* 153 */         if ((left != 0) && 
/* 154 */           (getGroundNodeHeight(x - dx, py, z) == 0)) {
/* 155 */           r.add(new AStarNode(x - dx, left, z + dz, dist + 2, node));
/*     */         }
/*     */         
/* 158 */         if ((right != 0) && 
/* 159 */           (getGroundNodeHeight(x, py, z - dz) == 0)) {
/* 160 */           r.add(new AStarNode(x + dx, right, z - dz, dist + 2, node));
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 165 */       else if (dx == 0)
/*     */       {
/* 167 */         int nY = getGroundNodeHeight(x, y, z + dz);
/* 168 */         if (nY > 0)
/*     */         {
/* 170 */           r.add(new AStarNode(x, nY, z + dz, dist + 1, node));
/* 171 */           if (stairs) {
/* 172 */             r.add(new AStarNode(x + 1, nY, z + dz, dist + 2, node));
/* 173 */             r.add(new AStarNode(x - 1, nY, z + dz, dist + 2, node));
/*     */           } else {
/* 175 */             int nnY = getGroundNodeHeight(x + 1, nY, z);
/* 176 */             if (nnY == 0)
/*     */             {
/* 178 */               r.add(new AStarNode(x + 1, nY, z + dz, dist + 2, node));
/*     */             }
/* 180 */             nnY = getGroundNodeHeight(x - 1, nY, z);
/* 181 */             if (nnY == 0)
/*     */             {
/* 183 */               r.add(new AStarNode(x - 1, nY, z + dz, dist + 2, node));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 189 */         int nY = getGroundNodeHeight(x + dx, y, z);
/* 190 */         if (nY > 0)
/*     */         {
/* 192 */           r.add(new AStarNode(x + dx, nY, z, dist + 1, node));
/* 193 */           if (stairs) {
/* 194 */             r.add(new AStarNode(x + dx, nY, z + 1, dist + 2, node));
/* 195 */             r.add(new AStarNode(x + dx, nY, z - 1, dist + 2, node));
/*     */           } else {
/* 197 */             int nnY = getGroundNodeHeight(x, nY, z + 1);
/* 198 */             if (nnY == 0)
/*     */             {
/* 200 */               r.add(new AStarNode(x + dx, nY, z + 1, dist + 2, node));
/*     */             }
/* 202 */             nnY = getGroundNodeHeight(x, nY, z - 1);
/* 203 */             if (nnY == 0)
/*     */             {
/* 205 */               r.add(new AStarNode(x + dx, nY, z - 1, dist + 2, node));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 212 */       for (int[] offset : neighbourOffsets)
/*     */       {
/*     */ 
/* 215 */         int nY = getGroundNodeHeight(x + offset[0], y, z + offset[1]);
/* 216 */         if (nY > 0) {
/* 217 */           r.add(new AStarNode(x + offset[0], nY, z + offset[1], nY, node));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 222 */     return r;
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
/*     */   private ArrayList<AStarNode> getAllNeighborsWithoutParent(int x, int y, int z, int dx, int dz, AStarNode node)
/*     */   {
/* 245 */     ArrayList<AStarNode> r = new ArrayList();
/* 246 */     for (int[] offset : neighbourOffsets) {
/* 247 */       if ((offset[0] != -dx) || (offset[1] != -dz))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 252 */         int nY = getGroundNodeHeight(x + offset[0], y, z + offset[1]);
/* 253 */         if (nY > 0)
/* 254 */           r.add(new AStarNode(x + offset[0], nY, z + offset[1], nY, node));
/*     */       }
/*     */     }
/* 257 */     return r;
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
/*     */   private int getGroundNodeHeight(int xN, int yN, int zN)
/*     */   {
/* 273 */     if (AStarStatic.isViable(this.worldObj, xN, yN, zN, 0, this.boss.config))
/*     */     {
/* 275 */       return yN;
/*     */     }
/* 277 */     if (AStarStatic.isViable(this.worldObj, xN, yN - 1, zN, -1, this.boss.config))
/*     */     {
/* 279 */       return yN - 1;
/*     */     }
/* 281 */     if (AStarStatic.isViable(this.worldObj, xN, yN + 1, zN, 1, this.boss.config))
/*     */     {
/* 283 */       return yN + 1;
/*     */     }
/*     */     
/* 286 */     return 0;
/*     */   }
/*     */   
/*     */   public ArrayList<AStarNode> getPath(AStarNode start, AStarNode end, boolean searchMode)
/*     */   {
/* 291 */     this.openQueue.offer(start);
/* 292 */     this.targetNode = end;
/* 293 */     this.currentNode = start;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 298 */     while ((!this.openQueue.isEmpty()) && (!shouldInterrupt())) {
/* 299 */       this.currentNode = ((AStarNode)this.openQueue.poll());
/*     */       
/* 301 */       this.closedNodes.add(this.currentNode);
/*     */       
/*     */ 
/*     */ 
/* 305 */       if ((isNodeEnd(this.currentNode, end)) || (identifySuccessors(this.currentNode)))
/*     */       {
/* 307 */         return backTrace(start);
/*     */       }
/*     */     }
/*     */     
/* 311 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean identifySuccessors(AStarNode node)
/*     */   {
/* 323 */     int x = node.x;
/* 324 */     int y = node.y;
/* 325 */     int z = node.z;
/*     */     
/* 327 */     ArrayList<AStarNode> successors = findNeighbours(node);
/* 328 */     for (AStarNode s : successors)
/*     */     {
/* 330 */       AStarNode jumpPoint = jump(s.x, s.y, s.z, x, y, z);
/* 331 */       if (jumpPoint != null) {
/* 332 */         if (!this.closedNodes.contains(jumpPoint))
/*     */         {
/*     */ 
/* 335 */           addOrUpdateNode(jumpPoint);
/*     */         }
/*     */       }
/*     */     }
/* 339 */     return false;
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
/*     */   private AStarNode jump(int nextX, int nextY, int nextZ, int px, int py, int pz)
/*     */   {
/* 356 */     int x = nextX;
/* 357 */     int y = nextY;
/* 358 */     int z = nextZ;
/* 359 */     int dist = this.currentNode.getG() + Math.abs(x - this.currentNode.x) + Math.abs(y - this.currentNode.y) + Math.abs(z - this.currentNode.z);
/*     */     
/* 361 */     int dx = x - px;
/* 362 */     int dz = z - pz;
/* 363 */     py = y;
/* 364 */     y = getGroundNodeHeight(x, py, z);
/*     */     
/* 366 */     if (y == 0)
/* 367 */       return null;
/* 368 */     if ((isCoordsEnd(x, y, z, this.targetNode)) || (dist >= 25)) {
/* 369 */       return new AStarNode(x, y, z, dist, this.currentNode, this.targetNode);
/*     */     }
/*     */     
/*     */ 
/* 373 */     int nxY = dx != 0 ? getGroundNodeHeight(x + dx, y, z) : 0;
/* 374 */     int nzY = dz != 0 ? getGroundNodeHeight(x, y, z + dz) : 0;
/*     */     
/*     */ 
/* 377 */     if ((dx != 0) && (dz != 0)) {
/* 378 */       if (((getGroundNodeHeight(x - dx, y, z + dz) != 0) && (getGroundNodeHeight(x - dx, py, z) == 0)) || ((getGroundNodeHeight(x + dx, y, z - dz) != 0) && (getGroundNodeHeight(x, py, z - dz) == 0))) {
/* 379 */         return new AStarNode(x, y, z, dist, this.currentNode, this.targetNode);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 384 */     else if (dx != 0) {
/* 385 */       if ((nxY != y) || ((getGroundNodeHeight(x, y, z + 1) == 0) && (getGroundNodeHeight(x + dx, nxY, z + 1) != 0)) || ((getGroundNodeHeight(x, y, z - 1) == 0) && (getGroundNodeHeight(x + dx, nxY, z - 1) != 0)))
/*     */       {
/* 387 */         return new AStarNode(x, y, z, dist, this.currentNode, this.targetNode);
/*     */       }
/*     */     }
/* 390 */     else if ((nzY != y) || ((getGroundNodeHeight(x + 1, y, z) == 0) && (getGroundNodeHeight(x + 1, nzY, z + dz) != 0)) || ((getGroundNodeHeight(x - 1, y, z) == 0) && (getGroundNodeHeight(x - 1, nzY, z + dz) != 0)))
/*     */     {
/* 392 */       return new AStarNode(x, y, z, dist, this.currentNode, this.targetNode);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 399 */     if ((dx != 0) && (dz != 0)) {
/* 400 */       AStarNode jx = jump(x + dx, y, z, x, y, z);
/* 401 */       AStarNode jy = jump(x, y, z + dz, x, y, z);
/* 402 */       if ((jx != null) || (jy != null)) {
/* 403 */         return new AStarNode(x, y, z, dist, this.currentNode, this.targetNode);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 409 */     if ((nxY != 0) || (nzY != 0)) {
/* 410 */       return jump(x + dx, y, z + dz, x, y, z);
/*     */     }
/* 412 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarWorkerJPS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
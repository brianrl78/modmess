/*     */ package org.millenaire.common.pathing.atomicstryker;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AStarNode
/*     */   implements Comparable
/*     */ {
/*     */   public final int x;
/*     */   
/*     */ 
/*     */   public final int y;
/*     */   
/*     */ 
/*     */   public final int z;
/*     */   
/*     */ 
/*     */   final AStarNode target;
/*     */   
/*     */ 
/*     */   public AStarNode parent;
/*     */   
/*     */ 
/*     */   private int g;
/*     */   
/*     */ 
/*     */   private double h;
/*     */   
/*     */ 
/*     */   public AStarNode(int ix, int iy, int iz)
/*     */   {
/*  31 */     this.x = ix;
/*  32 */     this.y = iy;
/*  33 */     this.z = iz;
/*  34 */     this.g = 0;
/*  35 */     this.parent = null;
/*  36 */     this.target = null;
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
/*     */   public AStarNode(int ix, int iy, int iz, int dist, AStarNode p)
/*     */   {
/*  54 */     this.x = ix;
/*  55 */     this.y = iy;
/*  56 */     this.z = iz;
/*  57 */     this.g = dist;
/*  58 */     this.parent = p;
/*  59 */     this.target = null;
/*     */   }
/*     */   
/*     */   public AStarNode(int ix, int iy, int iz, int dist, AStarNode p, AStarNode t) {
/*  63 */     this.x = ix;
/*  64 */     this.y = iy;
/*  65 */     this.z = iz;
/*  66 */     this.g = dist;
/*  67 */     this.parent = p;
/*  68 */     this.target = t;
/*  69 */     updateTargetCostEstimate();
/*     */   }
/*     */   
/*     */   public AStarNode clone()
/*     */   {
/*  74 */     return new AStarNode(this.x, this.y, this.z, this.g, this.parent);
/*     */   }
/*     */   
/*     */   public int compareTo(Object o)
/*     */   {
/*  79 */     if ((o instanceof AStarNode)) {
/*  80 */       AStarNode other = (AStarNode)o;
/*  81 */       if (getF() < other.getF())
/*  82 */         return -1;
/*  83 */       if (getF() > other.getF()) {
/*  84 */         return 1;
/*     */       }
/*     */     }
/*     */     
/*  88 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object checkagainst)
/*     */   {
/*  93 */     if ((checkagainst instanceof AStarNode)) {
/*  94 */       AStarNode check = (AStarNode)checkagainst;
/*  95 */       if ((check.x == this.x) && (check.y == this.y) && (check.z == this.z)) {
/*  96 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public double getF() {
/* 104 */     return this.g + this.h;
/*     */   }
/*     */   
/*     */   public int getG() {
/* 108 */     return this.g;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 113 */     return this.x << 16 ^ this.z ^ this.y << 24;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 118 */     if (this.parent == null) {
/* 119 */       return String.format("[%d|%d|%d], dist %d, F: %f", new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), Integer.valueOf(this.g), Double.valueOf(getF()) });
/*     */     }
/* 121 */     return String.format("[%d|%d|%d], dist %d, parent [%d|%d|%d], F: %f", new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), Integer.valueOf(this.g), Integer.valueOf(this.parent.x), Integer.valueOf(this.parent.y), Integer.valueOf(this.parent.z), Double.valueOf(getF()) });
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
/*     */   public boolean updateDistance(int checkingDistance, AStarNode parentOtherNode)
/*     */   {
/* 137 */     if (checkingDistance < this.g) {
/* 138 */       this.g = checkingDistance;
/* 139 */       this.parent = parentOtherNode;
/* 140 */       updateTargetCostEstimate();
/* 141 */       return true;
/*     */     }
/*     */     
/* 144 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateTargetCostEstimate()
/*     */   {
/* 152 */     if (this.target != null)
/*     */     {
/*     */ 
/* 155 */       this.h = (this.g + AStarStatic.getDistanceBetweenNodes(this, this.target) * 10.0D);
/*     */     } else {
/* 157 */       this.h = 0.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common.pathing.atomicstryker;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.PriorityQueue;
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
/*     */ 
/*     */ public class AStarWorker
/*     */   implements Runnable
/*     */ {
/*  22 */   private final long SEARCH_TIME_LIMIT = 150L;
/*     */   
/*     */ 
/*     */   public AStarPathPlanner boss;
/*     */   
/*     */ 
/*  28 */   public boolean isRunning = false;
/*     */   
/*     */   public final ArrayList<AStarNode> closedNodes;
/*     */   
/*     */   private AStarNode startNode;
/*     */   protected AStarNode targetNode;
/*     */   private boolean allowDropping;
/*     */   protected World worldObj;
/*     */   private long timeLimit;
/*     */   private final PriorityQueue<AStarNode> queue;
/*  38 */   private boolean isBusy = false;
/*     */   
/*     */   public AStarWorker(AStarPathPlanner creator) {
/*  41 */     this.boss = creator;
/*  42 */     this.closedNodes = new ArrayList();
/*  43 */     this.queue = new PriorityQueue(500);
/*     */   }
/*     */   
/*     */   private void addToBinaryHeap(AStarNode input)
/*     */   {
/*  48 */     this.queue.offer(input);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkPossibleLadder(AStarNode node)
/*     */   {
/*  59 */     int x = node.x;
/*  60 */     int y = node.y;
/*  61 */     int z = node.z;
/*     */     
/*  63 */     if (AStarStatic.isLadder(this.worldObj, this.worldObj.func_147439_a(x, y, z), x, y, z)) {
/*  64 */       AStarNode ladder = null;
/*  65 */       if (AStarStatic.isLadder(this.worldObj, this.worldObj.func_147439_a(x, y + 1, z), x, y + 1, z)) {}
/*     */       
/*     */ 
/*     */ 
/*  69 */       ladder = new AStarNode(x, y + 1, z, node.getG() + 2, node, this.targetNode);
/*  70 */       if (!tryToUpdateExistingHeapNode(node, ladder)) {
/*  71 */         addToBinaryHeap(ladder);
/*     */       }
/*     */       
/*  74 */       if (AStarStatic.isLadder(this.worldObj, this.worldObj.func_147439_a(x, y - 1, z), x, y - 1, z)) {}
/*     */       
/*     */ 
/*     */ 
/*  78 */       ladder = new AStarNode(x, y - 1, z, node.getG() + 2, node, this.targetNode);
/*  79 */       if (!tryToUpdateExistingHeapNode(node, ladder)) {
/*  80 */         addToBinaryHeap(ladder);
/*     */       }
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
/*     */   private int getCostNodeToNode(AStarNode a, AStarNode b)
/*     */   {
/*  97 */     return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getNextCandidates(AStarNode parent, boolean droppingAllowed)
/*     */   {
/* 109 */     int x = parent.x;
/* 110 */     int y = parent.y;
/* 111 */     int z = parent.z;
/* 112 */     int[][] c = droppingAllowed ? AStarStatic.candidates_allowdrops : AStarStatic.candidates;
/*     */     
/*     */ 
/* 115 */     for (int i = 0; i < c.length; i++) {
/* 116 */       AStarNode check = new AStarNode(x + c[i][0], y + c[i][1], z + c[i][2], parent.getG() + c[i][3], parent, this.targetNode);
/*     */       
/* 118 */       boolean found = false;
/* 119 */       Iterator<AStarNode> iter = this.closedNodes.iterator();
/* 120 */       while (iter.hasNext()) {
/* 121 */         AStarNode toUpdate = (AStarNode)iter.next();
/* 122 */         if (check.equals(toUpdate)) {
/* 123 */           toUpdate.updateDistance(check.getG() + getCostNodeToNode(toUpdate, check), parent);
/* 124 */           found = true;
/* 125 */           break;
/*     */         }
/*     */       }
/*     */       
/* 129 */       if ((!found) && (!tryToUpdateExistingHeapNode(parent, check)) && 
/* 130 */         (AStarStatic.isViable(this.worldObj, check, c[i][1], this.boss.config))) {
/* 131 */         addToBinaryHeap(check);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<AStarNode> getPath(AStarNode start, AStarNode end, boolean searchMode)
/*     */   {
/* 138 */     this.queue.offer(start);
/* 139 */     this.targetNode = end;
/*     */     
/* 141 */     AStarNode current = start;
/*     */     
/*     */ 
/*     */ 
/* 145 */     while (!isNodeEnd(current, end)) {
/* 146 */       this.closedNodes.add(this.queue.poll());
/*     */       
/* 148 */       checkPossibleLadder(current);
/* 149 */       getNextCandidates(current, searchMode);
/*     */       
/* 151 */       if ((this.queue.isEmpty()) || (shouldInterrupt()))
/*     */       {
/* 153 */         return null;
/*     */       }
/* 155 */       current = (AStarNode)this.queue.peek();
/*     */     }
/*     */     
/*     */ 
/* 159 */     ArrayList<AStarNode> foundpath = new ArrayList();
/* 160 */     foundpath.add(current);
/* 161 */     while (current != start) {
/* 162 */       foundpath.add(current.parent);
/* 163 */       current = current.parent;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 168 */     return foundpath;
/*     */   }
/*     */   
/*     */   public boolean isBusy() {
/* 172 */     return this.isBusy;
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
/*     */   protected boolean isCoordsEnd(int x, int y, int z, AStarNode end)
/*     */   {
/* 187 */     if (!this.boss.config.tolerance)
/*     */     {
/* 189 */       return (x == end.x) && (y == end.y) && (z == end.z);
/*     */     }
/*     */     
/* 192 */     if ((Math.abs(x - end.x) <= this.boss.config.toleranceHorizontal) && (Math.abs(z - end.z) <= this.boss.config.toleranceHorizontal) && (Math.abs(y - end.y) <= this.boss.config.toleranceVertical))
/*     */     {
/*     */ 
/* 195 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 199 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isNodeEnd(AStarNode cn, AStarNode end)
/*     */   {
/* 205 */     return isCoordsEnd(cn.x, cn.y, cn.z, end);
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/* 210 */     this.isBusy = true;
/* 211 */     this.timeLimit = (System.currentTimeMillis() + 150L);
/*     */     
/* 213 */     ArrayList<AStarNode> result = null;
/*     */     
/* 215 */     result = getPath(this.startNode, this.targetNode, this.allowDropping);
/*     */     
/*     */ 
/* 218 */     if (result == null)
/*     */     {
/*     */ 
/* 221 */       this.boss.onNoPathAvailable();
/*     */     }
/*     */     else
/*     */     {
/* 225 */       this.boss.onFoundPath(result);
/*     */     }
/* 227 */     this.isBusy = false;
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
/*     */   public void setup(World winput, AStarNode start, AStarNode end, boolean mode)
/*     */   {
/* 243 */     this.worldObj = winput;
/* 244 */     this.startNode = start;
/* 245 */     this.targetNode = end;
/* 246 */     this.allowDropping = mode;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean shouldInterrupt()
/*     */   {
/* 252 */     return System.currentTimeMillis() > this.timeLimit;
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
/*     */   private boolean tryToUpdateExistingHeapNode(AStarNode parent, AStarNode checkedOne)
/*     */   {
/* 266 */     Iterator<AStarNode> iter = this.queue.iterator();
/*     */     
/* 268 */     while (iter.hasNext()) {
/* 269 */       AStarNode itNode = (AStarNode)iter.next();
/* 270 */       if (itNode.equals(checkedOne)) {
/* 271 */         itNode.updateDistance(checkedOne.getG(), parent);
/* 272 */         return true;
/*     */       }
/*     */     }
/* 275 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
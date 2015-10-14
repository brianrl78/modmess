/*      */ package org.millenaire.common.pathing;
/*      */ 
/*      */ import java.util.List;
/*      */ import org.millenaire.common.MLN;
/*      */ 
/*      */ public class AStarPathing
/*      */ {
/*      */   public org.millenaire.common.MillWorldInfo winfo;
/*      */   public boolean[][] top;
/*      */   public boolean[][] bottom;
/*      */   public boolean[][] left;
/*      */   public boolean[][] right;
/*      */   public short[][] topGround;
/*      */   public byte[][] regions;
/*      */   public byte thRegion;
/*      */   public List<Node> nodes;
/*      */   
/*      */   public static class CachedPath
/*      */   {
/*   20 */     AStarPathing.Point2D[] points = null;
/*   21 */     long age = 0L;
/*      */     
/*      */     CachedPath() {
/*   24 */       this.age = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     CachedPath(CachedPath cp, int from) {
/*   28 */       this.points = new AStarPathing.Point2D[cp.points.length - from];
/*      */       
/*   30 */       int i = 0;
/*   31 */       for (AStarPathing.Point2D p : cp.points) {
/*   32 */         if (i >= from) {
/*   33 */           this.points[(i - from)] = p;
/*      */         }
/*   35 */         i++;
/*      */       }
/*      */       
/*   38 */       this.age = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     CachedPath(List<AStarPathing.Point2D> v)
/*      */     {
/*   43 */       List<AStarPathing.Point2D> v2 = new java.util.ArrayList();
/*      */       
/*   45 */       for (int i = 0; i < v.size(); i++) {
/*   46 */         AStarPathing.Point2D p = (AStarPathing.Point2D)v.get(i);
/*      */         
/*      */ 
/*   49 */         if (i > 0) {
/*   50 */           AStarPathing.Point2D prevp = (AStarPathing.Point2D)v2.get(v2.size() - 1);
/*      */           
/*      */           try
/*      */           {
/*   54 */             List<AStarPathing.Point2D> v3 = fillPoints(prevp, p);
/*   55 */             for (AStarPathing.Point2D fp : v3) {
/*   56 */               v2.add(fp);
/*      */             }
/*      */           } catch (Exception e) {
/*   59 */             MLN.printException(e);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*   64 */         v2.add(p);
/*      */       }
/*      */       
/*      */ 
/*   68 */       this.points = new AStarPathing.Point2D[v2.size()];
/*      */       
/*   70 */       int i = 0;
/*   71 */       for (AStarPathing.Point2D p : v2) {
/*   72 */         this.points[i] = p;
/*   73 */         i++;
/*      */       }
/*      */       
/*   76 */       this.age = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     CachedPath(List<AStarPathing.Point2D> v, CachedPath cp) {
/*   80 */       this.points = new AStarPathing.Point2D[v.size() + cp.points.length - 1];
/*      */       
/*   82 */       int i = 0;
/*   83 */       for (AStarPathing.Point2D p : v) {
/*   84 */         this.points[i] = p;
/*   85 */         i++;
/*      */       }
/*      */       
/*   88 */       boolean first = true;
/*   89 */       for (AStarPathing.Point2D p : cp.points) {
/*   90 */         if (first) {
/*   91 */           first = false;
/*      */         } else {
/*   93 */           this.points[i] = p;
/*   94 */           i++;
/*      */         }
/*      */       }
/*      */       
/*   98 */       this.age = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     private List<AStarPathing.Point2D> fillPoints(AStarPathing.Point2D p1, AStarPathing.Point2D p2) throws Exception
/*      */     {
/*  103 */       List<AStarPathing.Point2D> v = new java.util.ArrayList();
/*      */       
/*  105 */       int xdist = p2.x - p1.x;
/*  106 */       int zdist = p2.z - p1.z;
/*      */       
/*  108 */       if ((xdist == 0) && (zdist == 0)) {
/*  109 */         return v;
/*      */       }
/*      */       
/*  112 */       int xsign = 1;
/*  113 */       int zsign = 1;
/*      */       
/*  115 */       if (xdist < 0) {
/*  116 */         xsign = -1;
/*      */       }
/*  118 */       if (zdist < 0) {
/*  119 */         zsign = -1;
/*      */       }
/*      */       
/*  122 */       int x = p1.x;
/*  123 */       int z = p1.z;
/*      */       
/*  125 */       int xdone = 0;
/*  126 */       int zdone = 0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  132 */       while ((x != p2.x) || (z != p2.z))
/*      */       {
/*  134 */         if ((zdone != zdist) && ((xdist == 0) || (zdone * 1.0F / zdist < xdone * 1.0F / xdist))) {
/*  135 */           int nz = z + zsign;
/*  136 */           int nx = x;
/*  137 */           zdone += zsign;
/*  138 */         } else if (xdone != xdist) {
/*  139 */           int nx = x + xsign;
/*  140 */           int nz = z;
/*  141 */           xdone += xsign;
/*      */         } else {
/*  143 */           throw new org.millenaire.common.MLN.MillenaireException("Error in fillPoints: from " + p1 + " to " + p2 + " did " + xdone + "/" + zdone + " and could find nothing else to do."); }
/*      */         int nz;
/*      */         int nx;
/*  146 */         x = nx;
/*  147 */         z = nz;
/*      */         
/*  149 */         if ((x != p2.x) || (z != p2.z)) {
/*  150 */           v.add(new AStarPathing.Point2D(nx, nz));
/*      */         }
/*      */       }
/*      */       
/*  154 */       return v;
/*      */     }
/*      */     
/*      */     public String fullString() {
/*  158 */       String s = "";
/*      */       
/*  160 */       for (AStarPathing.Point2D p : this.points) {
/*  161 */         s = s + p + " ";
/*      */       }
/*      */       
/*  164 */       return s;
/*      */     }
/*      */     
/*      */     public AStarPathing.Point2D getEnd() {
/*  168 */       return this.points[(this.points.length - 1)];
/*      */     }
/*      */     
/*      */     public AStarPathing.PathKey getKey() {
/*  172 */       return new AStarPathing.PathKey(this.points[0], this.points[(this.points.length - 1)]);
/*      */     }
/*      */     
/*      */     public AStarPathing.Point2D getStart() {
/*  176 */       return this.points[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  181 */     public String toString() { return this.points.length + " - " + getStart() + " - " + getEnd(); } }
/*      */   
/*      */   protected static class Node { AStarPathing.Point2D pos;
/*      */     List<Node> neighbours;
/*      */     java.util.HashMap<Node, Integer> costs;
/*      */     Node from;
/*      */     int id;
/*      */     int fromDist;
/*      */     int toDist;
/*      */     int cornerSide;
/*  191 */     int region = 0;
/*  192 */     boolean temp = false;
/*      */     
/*      */     public Node(AStarPathing.Point2D p, int pid, boolean ptemp) {
/*  195 */       this.pos = p;
/*  196 */       this.id = pid;
/*  197 */       this.cornerSide = 0;
/*  198 */       this.temp = ptemp;
/*  199 */       this.neighbours = new java.util.ArrayList();
/*  200 */       this.costs = new java.util.HashMap();
/*      */     }
/*      */     
/*      */     public Node(AStarPathing.Point2D p, int pid, int cornerSide, boolean ptemp) {
/*  204 */       this.pos = p;
/*  205 */       this.id = pid;
/*  206 */       this.temp = ptemp;
/*  207 */       this.cornerSide = cornerSide;
/*  208 */       this.neighbours = new java.util.ArrayList();
/*  209 */       this.costs = new java.util.HashMap();
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/*  214 */       if (obj.getClass() != getClass()) {
/*  215 */         return false;
/*      */       }
/*      */       
/*  218 */       Node n = (Node)obj;
/*      */       
/*  220 */       return n.hashCode() == hashCode();
/*      */     }
/*      */     
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  226 */       return this.pos.x + (this.pos.z << 16);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  231 */       return "Node " + this.id + ": " + this.pos + " group: " + this.region + " neighbours: " + this.neighbours.size() + "(fromDist: " + this.fromDist + ", toDist: " + this.toDist + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PathingException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 6212915693102946545L;
/*  239 */     public static int UNREACHABLE_START = 0;
/*  240 */     public static int INVALID_GOAL = 1;
/*      */     public int errorCode;
/*      */     
/*      */     public PathingException(String message, int code)
/*      */     {
/*  245 */       super();
/*      */       
/*  247 */       this.errorCode = code;
/*      */     }
/*      */   }
/*      */   
/*      */   public class PathingWorker extends Thread {
/*      */     private static final int NODE_WARNING_LEVEL = 100;
/*      */     private static final int MAX_NODE_VISIT = 1500;
/*      */     org.millenaire.common.MillVillager villager;
/*      */     int pStartX;
/*      */     int pStartZ;
/*      */     int pDestX;
/*      */     int pDestZ;
/*      */     int updateCounterStart;
/*      */     org.millenaire.common.Point villagerPosStart;
/*      */     
/*  262 */     private PathingWorker(org.millenaire.common.MillVillager villager, int pStartX, int pStartZ, int pDestX, int pDestZ) { this.villager = villager;
/*  263 */       this.pStartX = pStartX;
/*  264 */       this.pStartZ = pStartZ;
/*  265 */       this.pDestX = pDestX;
/*  266 */       this.pDestZ = pDestZ;
/*      */       
/*  268 */       this.updateCounterStart = villager.updateCounter;
/*  269 */       this.villagerPosStart = villager.getPos();
/*      */     }
/*      */     
/*      */     public List<net.minecraft.pathfinding.PathPoint> getPathViaNodes(org.millenaire.common.MillVillager villager, int pStartX, int pStartZ, int pDestX, int pDestZ) throws Exception
/*      */     {
/*  274 */       long startTime = System.nanoTime();
/*  275 */       long currentAge = System.currentTimeMillis();
/*      */       
/*  277 */       int startX = pStartX - AStarPathing.this.winfo.mapStartX;
/*  278 */       int startZ = pStartZ - AStarPathing.this.winfo.mapStartZ;
/*  279 */       int destX = pDestX - AStarPathing.this.winfo.mapStartX;
/*  280 */       int destZ = pDestZ - AStarPathing.this.winfo.mapStartZ;
/*      */       
/*  282 */       AStarPathing.Node start = new AStarPathing.Node(new AStarPathing.Point2D(startX, startZ), 0, true);
/*  283 */       AStarPathing.Node end = new AStarPathing.Node(new AStarPathing.Point2D(destX, destZ), 0, true);
/*      */       
/*  285 */       AStarPathing.Point2D originalDest = new AStarPathing.Point2D(destX, destZ);
/*      */       
/*  287 */       AStarPathing.PathKey key = new AStarPathing.PathKey(start.pos, end.pos);
/*  288 */       if ((AStarPathing.this.cache.containsKey(key)) && (currentAge - ((AStarPathing.CachedPath)AStarPathing.this.cache.get(key)).age < 30000L)) {
/*  289 */         if ((MLN.DEV) && (villager != null)) {
/*  290 */           villager.getTownHall().monitor.nbPathing += 1;
/*  291 */           villager.getTownHall().monitor.nbCached += 1;
/*      */         }
/*      */         
/*  294 */         if (((AStarPathing.CachedPath)AStarPathing.this.cache.get(key)).points != null) {
/*  295 */           return AStarPathing.this.buildFinalPath(villager, (AStarPathing.CachedPath)AStarPathing.this.cache.get(key));
/*      */         }
/*  297 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  301 */       if (AStarPathing.this.canSee(start.pos, end.pos)) {
/*  302 */         end.from = start;
/*      */         
/*  304 */         AStarPathing.CachedPath path = new AStarPathing.CachedPath(AStarPathing.buildPointsNode(end));
/*  305 */         AStarPathing.this.cache.put(new AStarPathing.PathKey(start.pos, end.pos), path);
/*      */         
/*  307 */         if ((MLN.DEV) && (villager != null)) {
/*  308 */           villager.getTownHall().monitor.nbPathing += 1;
/*      */           
/*  310 */           double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*  311 */           villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  312 */             tmp404_403 = villager;tmp404_403.pathingTime = ((tmp404_403.pathingTime + timeInMl));
/*  313 */           villager.nbPathsCalculated += 1;
/*  314 */           AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  315 */           AStarPathing.this.firstDemandOutcome.add("Trivial path");
/*      */         }
/*      */         
/*  318 */         return AStarPathing.this.buildFinalPath(villager, path);
/*      */       }
/*      */       
/*  321 */       if (Thread.interrupted()) {
/*  322 */         throw new InterruptedException();
/*      */       }
/*      */       
/*  325 */       for (int i = AStarPathing.this.nodes.size() - 1; i >= 0; i--) {
/*  326 */         if (((AStarPathing.Node)AStarPathing.this.nodes.get(i)).temp) {
/*  327 */           AStarPathing.this.nodes.remove(i);
/*      */         }
/*      */       }
/*      */       
/*  331 */       for (AStarPathing.Node n : AStarPathing.this.nodes) {
/*  332 */         if (AStarPathing.this.canSee(n.pos, start.pos)) {
/*  333 */           start.neighbours.add(n);
/*  334 */           start.region = n.region;
/*  335 */           n.neighbours.add(start);
/*  336 */           int dist = start.pos.distanceTo(n.pos);
/*  337 */           n.costs.put(start, Integer.valueOf(dist));
/*  338 */           start.costs.put(n, Integer.valueOf(dist));
/*      */         }
/*  340 */         if (AStarPathing.this.canSee(n.pos, end.pos)) {
/*  341 */           end.neighbours.add(n);
/*  342 */           end.region = n.region;
/*  343 */           n.neighbours.add(end);
/*  344 */           int dist = end.pos.distanceTo(n.pos);
/*  345 */           n.costs.put(end, Integer.valueOf(dist));
/*  346 */           end.costs.put(n, Integer.valueOf(dist));
/*      */         }
/*      */         
/*  349 */         if (Thread.interrupted()) {
/*  350 */           throw new InterruptedException();
/*      */         }
/*      */       }
/*      */       
/*  354 */       if (start.region != end.region) {
/*  355 */         if ((MLN.LogGetPath >= 1) && (villager.extraLog)) {
/*  356 */           MLN.major(villager, "Start and end nodes in different groups: " + start + "/" + end);
/*      */         }
/*  358 */         end.neighbours.clear();
/*      */       }
/*      */       
/*  361 */       if (start.neighbours.size() == 0)
/*      */       {
/*  363 */         boolean foundStartNode = false;
/*      */         
/*  365 */         for (int i = -1; (i < 2) && (!foundStartNode); i++) {
/*  366 */           for (int j = -1; (j < 2) && (!foundStartNode); j++) {
/*  367 */             if (((i == 0) || (j == 0)) && 
/*  368 */               (startX + i >= 0) && (startZ + j >= 0) && (startX + i < AStarPathing.this.winfo.length) && (startZ + j < AStarPathing.this.winfo.width) && 
/*  369 */               (AStarPathing.this.winfo.topGround[startX][startZ] - AStarPathing.this.winfo.topGround[(startX + i)][(startZ + j)] < 3) && (AStarPathing.this.winfo.topGround[startX][startZ] - AStarPathing.this.winfo.topGround[(startX + i)][(startZ + j)] > -3)) {
/*  370 */               start = new AStarPathing.Node(new AStarPathing.Point2D(startX + i, startZ + j), 0, true);
/*      */               
/*  372 */               for (AStarPathing.Node n : AStarPathing.this.nodes) {
/*  373 */                 if (AStarPathing.this.canSee(n.pos, start.pos)) {
/*  374 */                   start.neighbours.add(n);
/*  375 */                   start.region = n.region;
/*  376 */                   n.neighbours.add(start);
/*  377 */                   int dist = start.pos.distanceTo(n.pos);
/*  378 */                   n.costs.put(start, Integer.valueOf(dist));
/*  379 */                   start.costs.put(n, Integer.valueOf(dist));
/*      */                 }
/*      */               }
/*  382 */               if (start.neighbours.size() > 0) {
/*  383 */                 foundStartNode = true;
/*  384 */                 if ((MLN.LogGetPath >= 2) && (villager.extraLog)) {
/*  385 */                   MLN.minor(this, "Found alternative start: " + start);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  391 */             if (Thread.interrupted()) {
/*  392 */               throw new InterruptedException();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  397 */         if (!foundStartNode) {
/*  398 */           if ((MLN.LogGetPath >= 2) && (villager.extraLog)) {
/*  399 */             MLN.minor(villager, "Start node " + start + " unreachable.");
/*      */           }
/*      */           
/*  402 */           AStarPathing.this.cache.put(new AStarPathing.PathKey(start.pos, end.pos), new AStarPathing.CachedPath());
/*      */           
/*  404 */           if ((MLN.DEV) && (villager != null)) {
/*  405 */             villager.getTownHall().monitor.nbPathing += 1;
/*      */             
/*  407 */             double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*  408 */             villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  409 */               tmp1479_1478 = villager;tmp1479_1478.pathingTime = ((tmp1479_1478.pathingTime + timeInMl));
/*  410 */             villager.nbPathsCalculated += 1;
/*  411 */             villager.nbPathNoStart += 1;
/*  412 */             AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  413 */             AStarPathing.this.firstDemandOutcome.add("No start node");
/*      */           }
/*  415 */           throw new AStarPathing.PathingException("Start node " + start + " unreachable.", AStarPathing.PathingException.UNREACHABLE_START);
/*      */         }
/*      */       }
/*  418 */       if (end.neighbours.size() == 0) {
/*  419 */         if ((MLN.LogGetPath >= 2) && (villager.extraLog)) {
/*  420 */           MLN.minor(this, "End node " + end + " unreachable.");
/*      */         }
/*      */         
/*  423 */         boolean foundEndNode = false;
/*  424 */         boolean foundEndNodeInOtherGroup = false;
/*      */         
/*  426 */         List<AStarPathing.Node> testedNodes = new java.util.ArrayList();
/*      */         
/*  428 */         for (int i = 0; (i < 4) && (!foundEndNode); i++) {
/*  429 */           for (int j = 0; (j <= i) && (!foundEndNode); j++) {
/*  430 */             if ((i == 0) || (j == 0)) {
/*  431 */               for (int cpt = 0; cpt < 8; cpt++) {
/*  432 */                 if (cpt == 0) {
/*  433 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX + i, destZ + j), 0, true);
/*  434 */                 } else if (cpt == 1) {
/*  435 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX - i, destZ + j), 0, true);
/*  436 */                 } else if (cpt == 2) {
/*  437 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX + i, destZ - j), 0, true);
/*  438 */                 } else if (cpt == 3) {
/*  439 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX - i, destZ - j), 0, true);
/*  440 */                 } else if (cpt == 4) {
/*  441 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX + j, destZ + i), 0, true);
/*  442 */                 } else if (cpt == 5) {
/*  443 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX - j, destZ + i), 0, true);
/*  444 */                 } else if (cpt == 6) {
/*  445 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX + j, destZ - i), 0, true);
/*  446 */                 } else if (cpt == 7) {
/*  447 */                   end = new AStarPathing.Node(new AStarPathing.Point2D(destX + j, destZ - i), 0, true);
/*      */                 }
/*      */                 
/*  450 */                 if (!testedNodes.contains(end))
/*      */                 {
/*  452 */                   for (AStarPathing.Node n : AStarPathing.this.nodes) {
/*  453 */                     if (AStarPathing.this.canSee(n.pos, end.pos)) {
/*  454 */                       end.neighbours.add(n);
/*  455 */                       end.region = n.region;
/*  456 */                       n.neighbours.add(end);
/*  457 */                       int dist = end.pos.distanceTo(n.pos);
/*  458 */                       n.costs.put(end, Integer.valueOf(dist));
/*  459 */                       end.costs.put(n, Integer.valueOf(dist));
/*      */                     }
/*      */                   }
/*  462 */                   if ((end.neighbours.size() > 0) && (((AStarPathing.Node)end.neighbours.get(0)).region == start.region)) {
/*  463 */                     foundEndNode = true;
/*  464 */                     if ((MLN.LogGetPath >= 2) && (villager.extraLog)) {
/*  465 */                       MLN.minor(villager, "Found alternative end: " + end);
/*      */                     }
/*  467 */                   } else if (end.neighbours.size() > 0) {
/*  468 */                     foundEndNodeInOtherGroup = true;
/*      */                   }
/*      */                   
/*  471 */                   testedNodes.add(end);
/*      */                 }
/*      */               }
/*      */             }
/*  475 */             if (Thread.interrupted()) {
/*  476 */               throw new InterruptedException();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  481 */         if (!foundEndNode)
/*      */         {
/*  483 */           AStarPathing.this.cache.put(new AStarPathing.PathKey(start.pos, end.pos), new AStarPathing.CachedPath());
/*      */           
/*  485 */           if ((MLN.DEV) && (villager != null)) {
/*  486 */             villager.getTownHall().monitor.nbPathing += 1;
/*      */             
/*  488 */             double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*  489 */             villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  490 */               tmp2403_2402 = villager;tmp2403_2402.pathingTime = ((tmp2403_2402.pathingTime + timeInMl));
/*  491 */             villager.nbPathsCalculated += 1;
/*  492 */             villager.nbPathNoEnd += 1;
/*  493 */             AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  494 */             AStarPathing.this.firstDemandOutcome.add("No end node");
/*      */           }
/*      */           
/*  497 */           if (foundEndNodeInOtherGroup) {
/*  498 */             return null;
/*      */           }
/*  500 */           throw new AStarPathing.PathingException("End pos not connected to any node", AStarPathing.PathingException.INVALID_GOAL);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  505 */       AStarPathing.this.nodes.add(start);
/*  506 */       AStarPathing.this.nodes.add(end);
/*      */       
/*  508 */       for (AStarPathing.Node n : AStarPathing.this.nodes) {
/*  509 */         n.from = null;
/*  510 */         n.fromDist = -1;
/*  511 */         n.toDist = -1;
/*      */       }
/*      */       
/*  514 */       List<AStarPathing.Node> open = new java.util.ArrayList();
/*  515 */       java.util.HashMap<AStarPathing.Node, AStarPathing.Node> closed = new java.util.HashMap();
/*      */       
/*  517 */       open.add(start);
/*  518 */       start.fromDist = 0;
/*  519 */       start.toDist = start.pos.distanceTo(end.pos);
/*      */       
/*  521 */       int nbNodesVisited = 0;
/*      */       
/*  523 */       AStarPathing.Node closest = start;
/*      */       AStarPathing.Node cn;
/*  525 */       while (open.size() > 0)
/*      */       {
/*  527 */         if (Thread.interrupted()) {
/*  528 */           throw new InterruptedException();
/*      */         }
/*      */         
/*  531 */         nbNodesVisited++;
/*      */         
/*  533 */         if (nbNodesVisited > 1500) {
/*  534 */           if ((MLN.LogGetPath >= 2) && (villager.extraLog)) {
/*  535 */             MLN.minor(villager, "Aborting after :" + nbNodesVisited + ", " + startX + "/" + startZ + " - " + destX + "/" + destZ + ". Stopping at: " + closest);
/*      */           }
/*      */           
/*  538 */           if ((MLN.LogGetPath >= 3) && (villager.extraLog)) {
/*  539 */             MLN.debug(villager, "closest.equals(end): " + closest.equals(end));
/*  540 */             MLN.debug(villager, "start.toDist: " + start.toDist);
/*      */           }
/*  542 */           AStarPathing.CachedPath cpath = new AStarPathing.CachedPath(AStarPathing.buildPointsNode(closest));
/*      */           
/*  544 */           AStarPathing.this.storeInCache(cpath, originalDest);
/*      */           
/*  546 */           if ((MLN.DEV) && (villager != null)) {
/*  547 */             villager.getTownHall().monitor.nbPathing += 1;
/*      */             
/*  549 */             double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*  550 */             villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  551 */               tmp2931_2930 = villager;tmp2931_2930.pathingTime = ((tmp2931_2930.pathingTime + timeInMl));
/*  552 */             villager.nbPathsCalculated += 1;
/*  553 */             villager.nbPathAborted += 1;
/*  554 */             villager.abortedKeys.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  555 */             if (MLN.LogGetPath >= 1) {
/*  556 */               MLN.major(villager, "Caching aborted path: " + cpath.getKey() + " and " + new AStarPathing.PathKey(cpath.getStart(), originalDest) + ", failing took " + timeInMl + " ms. " + "Regions: " + AStarPathing.this.regions[start.pos.x][start.pos.z] + " - " + AStarPathing.this.regions[end.pos.x][end.pos.z]);
/*      */             }
/*      */             
/*  559 */             AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  560 */             AStarPathing.this.firstDemandOutcome.add("Aborted");
/*      */           }
/*      */           
/*  563 */           return AStarPathing.this.buildFinalPath(villager, cpath);
/*      */         }
/*      */         
/*  566 */         cn = null;
/*  567 */         int bestdistance = -1;
/*  568 */         for (AStarPathing.Node n : open)
/*      */         {
/*  570 */           if (Thread.interrupted()) {
/*  571 */             throw new InterruptedException();
/*      */           }
/*      */           
/*  574 */           if (n.toDist == -1) {
/*  575 */             n.toDist = n.pos.distanceTo(end.pos);
/*      */           }
/*  577 */           int distance = n.fromDist + n.toDist;
/*  578 */           if ((MLN.LogGetPath >= 3) && (villager.extraLog)) {
/*  579 */             MLN.debug(this, "Testing " + n + ")");
/*      */           }
/*  581 */           if (n.equals(end))
/*      */           {
/*  583 */             AStarPathing.CachedPath cpath = new AStarPathing.CachedPath(AStarPathing.buildPointsNode(n));
/*      */             
/*  585 */             AStarPathing.this.storeInCache(cpath, originalDest);
/*  586 */             AStarPathing.this.cache.put(new AStarPathing.PathKey(start.pos, end.pos), cpath);
/*  587 */             AStarPathing.this.cache.put(new AStarPathing.PathKey(new AStarPathing.Point2D(startX, startZ), new AStarPathing.Point2D(destX, destZ)), cpath);
/*      */             
/*  589 */             if ((MLN.DEV) && (villager != null)) {
/*  590 */               villager.getTownHall().monitor.nbPathing += 1;
/*      */               
/*  592 */               double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*      */               
/*  594 */               if ((nbNodesVisited > 100) && (MLN.LogGetPath >= 1)) {
/*  595 */                 MLN.major(villager, "Success after: " + nbNodesVisited + " time: " + timeInMl + " ms between " + start.pos + " and " + end.pos + " goal: " + villager.goalKey);
/*  596 */               } else if ((MLN.LogGetPath >= 3) && (villager.extraLog)) {
/*  597 */                 MLN.debug(villager, "Success after: " + nbNodesVisited + " time: " + timeInMl + " ms between " + start.pos + " and " + end.pos + " goal: " + villager.goalKey);
/*      */               }
/*      */               
/*  600 */               villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  601 */                 tmp3675_3674 = villager;tmp3675_3674.pathingTime = ((tmp3675_3674.pathingTime + timeInMl));
/*  602 */               villager.nbPathsCalculated += 1;
/*  603 */               AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*  604 */               AStarPathing.this.firstDemandOutcome.add("Success");
/*      */             }
/*      */             
/*  607 */             if (MLN.LogGetPath >= 2) {
/*  608 */               MLN.minor(villager, "Path calculation took " + (villager.updateCounter - this.updateCounterStart) + " update cycles. Start pos: " + this.villagerPosStart + " and pos: " + villager.getPos());
/*      */             }
/*      */             
/*      */ 
/*  612 */             return AStarPathing.this.buildFinalPath(villager, cpath);
/*      */           }
/*      */           
/*  615 */           if ((bestdistance == -1) || (distance < bestdistance)) {
/*  616 */             cn = n;
/*  617 */             bestdistance = distance;
/*      */           }
/*      */           
/*  620 */           if (n.toDist < closest.toDist) {
/*  621 */             closest = n;
/*      */           }
/*      */         }
/*      */         
/*  625 */         if ((closest.toDist == 0) && (MLN.LogGetPath >= 1) && (villager.extraLog)) {
/*  626 */           MLN.major(this, "Picked: " + cn + " " + "cn.equals(end): " + cn.equals(end));
/*  627 */           MLN.major(this, "Should have reached: " + closest + " " + "closest.equals(end): " + closest.equals(end));
/*      */         }
/*      */         
/*      */ 
/*  631 */         if ((MLN.LogGetPath >= 3) && (villager.extraLog)) {
/*  632 */           MLN.debug(this, "Selected: " + cn);
/*      */         }
/*      */         
/*  635 */         open.remove(cn);
/*  636 */         closed.put(cn, cn);
/*      */         
/*  638 */         for (AStarPathing.Node n : cn.neighbours) {
/*  639 */           Integer cost = (Integer)cn.costs.get(n);
/*      */           
/*  641 */           if (!closed.containsKey(n))
/*      */           {
/*      */ 
/*      */ 
/*  645 */             if (!open.contains(n)) {
/*  646 */               cn.fromDist += cost.intValue();
/*  647 */               n.toDist = n.pos.distanceTo(end.pos);
/*  648 */               n.from = cn;
/*  649 */               open.add(n);
/*  650 */             } else if (cn.fromDist + cost.intValue() < n.fromDist) {
/*  651 */               cn.fromDist += cost.intValue();
/*  652 */               n.from = cn;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  659 */       if ((MLN.LogGetPath >= 1) && (villager.extraLog)) {
/*  660 */         MLN.major(villager, "Failure after :" + nbNodesVisited + ", " + startX + "/" + startZ + " - " + destX + "/" + destZ);
/*      */       }
/*      */       
/*  663 */       AStarPathing.this.cache.put(new AStarPathing.PathKey(start.pos, end.pos), new AStarPathing.CachedPath());
/*  664 */       AStarPathing.this.cache.put(new AStarPathing.PathKey(new AStarPathing.Point2D(startX, startZ), new AStarPathing.Point2D(destX, destZ)), new AStarPathing.CachedPath());
/*      */       
/*  666 */       if ((MLN.DEV) && (villager != null)) {
/*  667 */         villager.getTownHall().monitor.nbPathing += 1;
/*      */         
/*  669 */         double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*  670 */         villager.getTownHall().monitor.pathingTime += timeInMl; org.millenaire.common.MillVillager 
/*  671 */           tmp4433_4432 = villager;tmp4433_4432.pathingTime = ((tmp4433_4432.pathingTime + timeInMl));
/*  672 */         villager.nbPathsCalculated += 1;
/*  673 */         villager.nbPathFailure += 1;
/*  674 */         villager.getTownHall().monitor.noPathFoundTime += timeInMl;
/*  675 */         AStarPathing.this.firstDemandOutcome.add("Failure");
/*  676 */         AStarPathing.this.firstDemand.add(new AStarPathing.PathKey(start.pos, end.pos));
/*      */       }
/*      */       
/*  679 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public void run()
/*      */     {
/*  685 */       synchronized (AStarPathing.this)
/*      */       {
/*      */         try {
/*  688 */           List<net.minecraft.pathfinding.PathPoint> result = getPathViaNodes(this.villager, this.pStartX, this.pStartZ, this.pDestX, this.pDestZ);
/*  689 */           this.villager.registerNewPath(result);
/*      */         } catch (InterruptedException e) {
/*  691 */           this.villager.registerNewPathInterrupt(this);
/*      */         } catch (Exception e) {
/*  693 */           this.villager.registerNewPathException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PathKey
/*      */   {
/*      */     static final boolean log = false;
/*      */     AStarPathing.Point2D start;
/*      */     AStarPathing.Point2D end;
/*      */     
/*      */     PathKey(AStarPathing.Point2D start, AStarPathing.Point2D end) {
/*  706 */       this.start = start;
/*  707 */       this.end = end;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/*  712 */       if (!(obj instanceof PathKey)) {
/*  713 */         return false;
/*      */       }
/*      */       
/*  716 */       PathKey p = (PathKey)obj;
/*  717 */       return (this.start.equals(p.start)) && (this.end.equals(p.end));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  728 */       return this.start.x + (this.start.z << 8) + (this.end.x << 16) + (this.end.z << 24);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  733 */       return this.start + ", " + this.end;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Point2D
/*      */   {
/*      */     int x;
/*      */     int z;
/*      */     
/*      */     public Point2D(int px, int pz) {
/*  743 */       this.x = px;
/*  744 */       this.z = pz;
/*      */     }
/*      */     
/*      */     public int distanceTo(Point2D p)
/*      */     {
/*  749 */       int d = p.x - this.x;
/*  750 */       int d1 = p.z - this.z;
/*      */       
/*  752 */       return (int)Math.sqrt(d * d + d1 * d1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/*  759 */       if (!(obj instanceof Point2D)) {
/*  760 */         return false;
/*      */       }
/*      */       
/*  763 */       Point2D p = (Point2D)obj;
/*  764 */       return (this.x == p.x) && (this.z == p.z);
/*      */     }
/*      */     
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  770 */       return this.x << 16 & this.z;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  775 */       return this.x + "/" + this.z;
/*      */     }
/*      */   }
/*      */   
/*      */   private static List<Point2D> buildPointsNode(Node end)
/*      */   {
/*  781 */     Node n = end;
/*      */     
/*  783 */     if (n.from != null)
/*      */     {
/*      */ 
/*  786 */       List<Point2D> path = buildPointsNode(n.from);
/*      */       
/*  788 */       path.add(n.pos);
/*  789 */       return path;
/*      */     }
/*  791 */     List<Point2D> path = new java.util.ArrayList();
/*      */     
/*  793 */     path.add(n.pos);
/*  794 */     return path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  815 */   public java.util.HashMap<PathKey, CachedPath> cache = new java.util.HashMap();
/*      */   
/*      */ 
/*      */   public List<PathKey> firstDemand;
/*      */   
/*      */ 
/*      */   public List<String> firstDemandOutcome;
/*      */   
/*      */ 
/*      */   private int boolDisplay(boolean a, boolean b, boolean c, boolean d)
/*      */   {
/*  826 */     int i = a ? 1 : 0;
/*  827 */     i += (b ? 2 : 0);
/*  828 */     i += (c ? 4 : 0);
/*  829 */     i += (d ? 8 : 0);
/*  830 */     return i;
/*      */   }
/*      */   
/*      */   private List<net.minecraft.pathfinding.PathPoint> buildFinalPath(org.millenaire.common.MillVillager villager, CachedPath path)
/*      */     throws Exception
/*      */   {
/*  836 */     List<net.minecraft.pathfinding.PathPoint> ppoints = new java.util.ArrayList();
/*      */     
/*  838 */     for (int i = 0; i < path.points.length; i++) {
/*  839 */       Point2D p = path.points[i];
/*      */       
/*  841 */       int oldGround = this.topGround[p.x][p.z];
/*  842 */       int newGround = this.winfo.topGround[p.x][p.z];
/*      */       
/*      */ 
/*  845 */       if ((newGround < oldGround + 3) && (newGround > oldGround - 3)) {
/*  846 */         oldGround = newGround;
/*      */       }
/*      */       
/*  849 */       net.minecraft.pathfinding.PathPoint np = new net.minecraft.pathfinding.PathPoint(p.x + this.winfo.mapStartX, oldGround, p.z + this.winfo.mapStartZ);
/*  850 */       ppoints.add(np);
/*      */     }
/*      */     
/*      */ 
/*  854 */     if ((villager.extraLog) && (MLN.LogPathing >= 1) && (path.points.length > 1) && (ppoints.size() < 2)) {
/*  855 */       MLN.major(this, "buildFinalPath returned a path of size " + ppoints.size() + " from " + path.points.length);
/*      */     }
/*      */     
/*  858 */     return ppoints;
/*      */   }
/*      */   
/*      */   private void buildNodes()
/*      */   {
/*  863 */     for (int i = 0; i < this.winfo.length; i++) {
/*  864 */       for (int j = 0; j < this.winfo.width; j++) {
/*  865 */         boolean isNode = false;
/*  866 */         int cornerSide = 0;
/*      */         
/*  868 */         if ((i > 0) && (j > 0) && 
/*  869 */           (this.top[i][j] != 0) && (this.left[i][j] != 0) && ((this.left[(i - 1)][j] == 0) || (this.top[i][(j - 1)] == 0))) {
/*  870 */           isNode = true;
/*  871 */           cornerSide |= 0x1;
/*      */         }
/*      */         
/*  874 */         if ((i < this.winfo.length - 1) && (j > 0) && 
/*  875 */           (this.bottom[i][j] != 0) && (this.left[i][j] != 0) && ((this.left[(i + 1)][j] == 0) || (this.bottom[i][(j - 1)] == 0))) {
/*  876 */           isNode = true;
/*  877 */           cornerSide += 2;
/*  878 */           cornerSide |= 0x2;
/*      */         }
/*      */         
/*  881 */         if ((i > 0) && (j < this.winfo.width - 1) && 
/*  882 */           (this.top[i][j] != 0) && (this.right[i][j] != 0) && ((this.right[(i - 1)][j] == 0) || (this.top[i][(j + 1)] == 0))) {
/*  883 */           isNode = true;
/*  884 */           cornerSide |= 0x4;
/*      */         }
/*      */         
/*  887 */         if ((i < this.winfo.length - 1) && (j < this.winfo.width - 1) && 
/*  888 */           (this.bottom[i][j] != 0) && (this.right[i][j] != 0) && ((this.right[(i + 1)][j] == 0) || (this.bottom[i][(j + 1)] == 0))) {
/*  889 */           isNode = true;
/*  890 */           cornerSide |= 0x8;
/*      */         }
/*      */         
/*      */ 
/*  894 */         if (isNode) {
/*  895 */           this.nodes.add(new Node(new Point2D(i, j), this.nodes.size(), cornerSide, false));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  902 */     for (Node n : this.nodes)
/*      */     {
/*  904 */       if ((n.cornerSide == 1) && (n.pos.x < this.winfo.length - 1) && (n.pos.z < this.winfo.width - 1) && 
/*  905 */         (this.bottom[n.pos.x][n.pos.z] != 0) && (this.right[n.pos.x][n.pos.z] != 0) && (this.bottom[n.pos.x][(n.pos.z + 1)] != 0) && (this.right[(n.pos.x + 1)][n.pos.z] != 0))
/*      */       {
/*      */ 
/*  908 */         int tx = n.pos.x + 1;
/*  909 */         int tz = n.pos.z + 1;
/*  910 */         if ((tx < this.winfo.length - 1) && (tz < this.winfo.width - 1) && (this.bottom[tx][tz] != 0) && (this.right[tx][tz] != 0)) {
/*  911 */           n.pos.x = tx;
/*  912 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  916 */       if ((n.cornerSide == 2) && (n.pos.x > 0) && (n.pos.z < this.winfo.width - 1) && 
/*  917 */         (this.top[n.pos.x][n.pos.z] != 0) && (this.right[n.pos.x][n.pos.z] != 0) && (this.top[n.pos.x][(n.pos.z + 1)] != 0) && (this.right[(n.pos.x - 1)][n.pos.z] != 0)) {
/*  918 */         int tx = n.pos.x - 1;
/*  919 */         int tz = n.pos.z + 1;
/*  920 */         if ((tx > 0) && (tz < this.winfo.width - 1) && (this.top[tx][tz] != 0) && (this.right[tx][tz] != 0)) {
/*  921 */           n.pos.x = tx;
/*  922 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  926 */       if ((n.cornerSide == 4) && (n.pos.x < this.winfo.length - 1) && (n.pos.z > 0) && 
/*  927 */         (this.bottom[n.pos.x][n.pos.z] != 0) && (this.left[n.pos.x][n.pos.z] != 0) && (this.bottom[n.pos.x][(n.pos.z - 1)] != 0) && (this.left[(n.pos.x + 1)][n.pos.z] != 0))
/*      */       {
/*      */ 
/*  930 */         int tx = n.pos.x + 1;
/*  931 */         int tz = n.pos.z - 1;
/*  932 */         if ((tx < this.winfo.length - 1) && (tz > 0) && (this.bottom[tx][tz] != 0) && (this.left[tx][tz] != 0)) {
/*  933 */           n.pos.x = tx;
/*  934 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  938 */       if ((n.cornerSide == 8) && (n.pos.x > 0) && (n.pos.z > 0) && 
/*  939 */         (this.top[n.pos.x][n.pos.z] != 0) && (this.left[n.pos.x][n.pos.z] != 0) && (this.top[n.pos.x][(n.pos.z - 1)] != 0) && (this.left[(n.pos.x - 1)][n.pos.z] != 0))
/*      */       {
/*      */ 
/*  942 */         int tx = n.pos.x - 1;
/*  943 */         int tz = n.pos.z - 1;
/*  944 */         if ((tx > 0) && (tz > 0) && (this.top[tx][tz] != 0) && (this.left[tx][tz] != 0)) {
/*  945 */           n.pos.x = tx;
/*  946 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  952 */       if ((n.cornerSide == 3) && (n.pos.z < this.winfo.width - 1) && 
/*  953 */         (this.right[n.pos.x][n.pos.z] != 0)) {
/*  954 */         int tx = n.pos.x;
/*  955 */         int tz = n.pos.z + 1;
/*  956 */         if ((tz < this.winfo.width - 1) && (this.bottom[tx][tz] != 0) && (this.right[tx][tz] != 0) && (this.top[tx][tz] != 0)) {
/*  957 */           n.pos.x = tx;
/*  958 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  962 */       if ((n.cornerSide == 5) && (n.pos.x < this.winfo.length - 1) && 
/*  963 */         (this.bottom[n.pos.x][n.pos.z] != 0)) {
/*  964 */         int tx = n.pos.x + 1;
/*  965 */         int tz = n.pos.z;
/*  966 */         if ((tx < this.winfo.length - 1) && (this.bottom[tx][tz] != 0) && (this.right[tx][tz] != 0) && (this.left[tx][tz] != 0)) {
/*  967 */           n.pos.x = tx;
/*  968 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  972 */       if ((n.cornerSide == 10) && (n.pos.x > 0) && 
/*  973 */         (this.top[n.pos.x][n.pos.z] != 0)) {
/*  974 */         int tx = n.pos.x - 1;
/*  975 */         int tz = n.pos.z;
/*  976 */         if ((tx > 0) && (this.top[tx][tz] != 0) && (this.right[tx][tz] != 0) && (this.left[tx][tz] != 0)) {
/*  977 */           n.pos.x = tx;
/*  978 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */       
/*  982 */       if ((n.cornerSide == 12) && (n.pos.z > 0) && 
/*  983 */         (this.left[n.pos.x][n.pos.z] != 0)) {
/*  984 */         int tx = n.pos.x;
/*  985 */         int tz = n.pos.z - 1;
/*  986 */         if ((tx > 0) && (this.top[tx][tz] != 0) && (this.bottom[tx][tz] != 0) && (this.left[tx][tz] != 0)) {
/*  987 */           n.pos.x = tx;
/*  988 */           n.pos.z = tz;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  995 */     for (int i = this.nodes.size() - 1; i > -1; i--) {
/*  996 */       for (int j = i - 1; j > -1; j--) {
/*  997 */         if (((Node)this.nodes.get(i)).equals(this.nodes.get(j))) {
/*  998 */           this.nodes.remove(i);
/*  999 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canSee(Point2D p1, Point2D p2)
/*      */   {
/* 1007 */     int xdist = p2.x - p1.x;
/* 1008 */     int zdist = p2.z - p1.z;
/*      */     
/* 1010 */     if ((xdist == 0) && (zdist == 0)) {
/* 1011 */       return true;
/*      */     }
/*      */     
/* 1014 */     int xsign = 1;
/* 1015 */     int zsign = 1;
/*      */     
/* 1017 */     if (xdist < 0) {
/* 1018 */       xsign = -1;
/*      */     }
/* 1020 */     if (zdist < 0) {
/* 1021 */       zsign = -1;
/*      */     }
/*      */     
/* 1024 */     int x = p1.x;
/* 1025 */     int z = p1.z;
/*      */     
/* 1027 */     int xdone = 0;
/* 1028 */     int zdone = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1034 */     while ((x != p2.x) || (z != p2.z)) {
/*      */       int nx;
/*      */       int nz;
/* 1037 */       if ((xdist == 0) || ((zdist != 0) && (xdone * 1000 / xdist > zdone * 1000 / zdist))) {
/* 1038 */         int nz = z + zsign;
/* 1039 */         int nx = x;
/* 1040 */         zdone += zsign;
/*      */         
/*      */ 
/*      */ 
/* 1044 */         if ((zsign == 1) && (this.right[x][z] == 0))
/* 1045 */           return false;
/* 1046 */         if ((zsign == -1) && (this.left[x][z] == 0)) {
/* 1047 */           return false;
/*      */         }
/*      */       } else {
/* 1050 */         nx = x + xsign;
/* 1051 */         nz = z;
/* 1052 */         xdone += xsign;
/*      */         
/*      */ 
/*      */ 
/* 1056 */         if ((xsign == 1) && (this.bottom[x][z] == 0))
/* 1057 */           return false;
/* 1058 */         if ((xsign == -1) && (this.top[x][z] == 0)) {
/* 1059 */           return false;
/*      */         }
/*      */       }
/* 1062 */       x = nx;
/* 1063 */       z = nz;
/*      */     }
/*      */     
/*      */ 
/* 1067 */     return true;
/*      */   }
/*      */   
/*      */   public boolean createConnectionsTable(org.millenaire.common.MillWorldInfo winfo, org.millenaire.common.Point thStanding) throws org.millenaire.common.MLN.MillenaireException
/*      */   {
/* 1072 */     long startTime = System.nanoTime();
/* 1073 */     long totalStartTime = startTime;
/*      */     
/* 1075 */     this.winfo = winfo;
/*      */     
/* 1077 */     this.top = new boolean[winfo.length][winfo.width];
/* 1078 */     this.bottom = new boolean[winfo.length][winfo.width];
/* 1079 */     this.left = new boolean[winfo.length][winfo.width];
/* 1080 */     this.right = new boolean[winfo.length][winfo.width];
/* 1081 */     this.regions = new byte[winfo.length][winfo.width];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1091 */     this.topGround = org.millenaire.common.MillWorldInfo.shortArrayDeepClone(winfo.topGround);
/*      */     
/* 1093 */     this.nodes = new java.util.ArrayList();
/* 1094 */     this.firstDemand = new java.util.ArrayList();
/* 1095 */     this.firstDemandOutcome = new java.util.ArrayList();
/*      */     
/* 1097 */     for (int i = 0; i < winfo.length; i++) {
/* 1098 */       for (int j = 0; j < winfo.width; j++) {
/* 1099 */         int y = winfo.topGround[i][j];
/* 1100 */         int space = winfo.spaceAbove[i][j];
/*      */         
/* 1102 */         if (space > 1)
/*      */         {
/* 1104 */           if (i > 0) {
/* 1105 */             int ny = winfo.topGround[(i - 1)][j];
/* 1106 */             int nspace = winfo.spaceAbove[(i - 1)][j];
/*      */             
/* 1108 */             boolean connected = false;
/*      */             
/* 1110 */             if ((ny == y) && (nspace > 1)) {
/* 1111 */               connected = true;
/* 1112 */             } else if ((ny == y - 1) && (nspace > 2)) {
/* 1113 */               connected = true;
/* 1114 */             } else if ((ny == y + 1) && (nspace > 1) && (space > 2)) {
/* 1115 */               connected = true;
/*      */             }
/*      */             
/* 1118 */             if (connected) {
/* 1119 */               this.top[i][j] = 1;
/* 1120 */               this.bottom[(i - 1)][j] = 1;
/*      */             }
/*      */           }
/* 1123 */           if (j > 0) {
/* 1124 */             int ny = winfo.topGround[i][(j - 1)];
/* 1125 */             int nspace = winfo.spaceAbove[i][(j - 1)];
/*      */             
/* 1127 */             boolean connected = false;
/*      */             
/* 1129 */             if ((ny == y) && (nspace > 1)) {
/* 1130 */               connected = true;
/* 1131 */             } else if ((ny == y - 1) && (nspace > 2)) {
/* 1132 */               connected = true;
/* 1133 */             } else if ((ny == y + 1) && (nspace > 1) && (space > 2)) {
/* 1134 */               connected = true;
/*      */             }
/*      */             
/* 1137 */             if (connected) {
/* 1138 */               this.left[i][j] = 1;
/* 1139 */               this.right[i][(j - 1)] = 1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1146 */     if (MLN.LogConnections >= 2) {
/* 1147 */       MLN.minor(this, "Time taken for connection building: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */     }
/*      */     
/* 1150 */     startTime = System.nanoTime();
/*      */     
/* 1152 */     buildNodes();
/* 1153 */     if (MLN.LogConnections >= 2) {
/* 1154 */       MLN.minor(this, "Time taken for nodes finding: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */     }
/*      */     
/* 1157 */     startTime = System.nanoTime();
/*      */     
/* 1159 */     for (java.util.Iterator i$ = this.nodes.iterator(); i$.hasNext();) { n = (Node)i$.next();
/* 1160 */       for (Node n2 : this.nodes) {
/* 1161 */         if ((n.id < n2.id) && 
/* 1162 */           (canSee(n.pos, n2.pos))) {
/* 1163 */           Integer distance = Integer.valueOf(n.pos.distanceTo(n2.pos));
/* 1164 */           n.costs.put(n2, distance);
/* 1165 */           n.neighbours.add(n2);
/* 1166 */           n2.costs.put(n, distance);
/* 1167 */           n2.neighbours.add(n);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     Node n;
/* 1173 */     if (MLN.LogConnections >= 2) {
/* 1174 */       MLN.minor(this, "Time taken for nodes linking: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */     }
/*      */     
/* 1177 */     startTime = System.nanoTime();
/*      */     
/* 1179 */     findRegions(thStanding);
/* 1180 */     if (MLN.LogConnections >= 2) {
/* 1181 */       MLN.minor(this, "Time taken for group finding: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */     }
/*      */     
/* 1184 */     if (MLN.LogConnections >= 1) {
/* 1185 */       MLN.major(this, "Node graph complete. Size: " + this.nodes.size() + " Time taken: " + (System.nanoTime() - totalStartTime) / 1000000.0D);
/*      */     }
/*      */     
/* 1188 */     if ((MLN.LogConnections >= 3) && (MLN.DEV)) {
/* 1189 */       MLN.major(this, "Calling displayConnectionsLog");
/* 1190 */       displayConnectionsLog();
/*      */     }
/*      */     
/* 1193 */     return true;
/*      */   }
/*      */   
/*      */   public PathingWorker createWorkerForPath(org.millenaire.common.MillVillager villager, int pStartX, int pStartZ, int pDestX, int pDestZ) {
/* 1197 */     PathingWorker worker = new PathingWorker(villager, pStartX, pStartZ, pDestX, pDestZ, null);
/* 1198 */     worker.start();
/* 1199 */     return worker;
/*      */   }
/*      */   
/*      */   private void displayConnectionsLog()
/*      */   {
/* 1204 */     long startTime = System.nanoTime();
/*      */     
/* 1206 */     MLN.minor(this, "Connections:");
/*      */     
/* 1208 */     String s = "    ";
/* 1209 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1210 */       s = s + net.minecraft.util.MathHelper.func_76128_c(j / 10) % 10;
/*      */     }
/* 1212 */     MLN.minor(this, s);
/*      */     
/* 1214 */     s = "    ";
/* 1215 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1216 */       s = s + j % 10;
/*      */     }
/* 1218 */     MLN.minor(this, s);
/*      */     
/* 1220 */     for (int i = 0; i < this.winfo.length; i++) {
/* 1221 */       if (i < 10) {
/* 1222 */         s = i + "   ";
/* 1223 */       } else if (i < 100) {
/* 1224 */         s = i + "  ";
/*      */       } else {
/* 1226 */         s = i + " ";
/*      */       }
/* 1228 */       for (int j = 0; j < this.winfo.width; j++) {
/* 1229 */         s = s + Integer.toHexString(boolDisplay(this.top[i][j], this.left[i][j], this.bottom[i][j], this.right[i][j]));
/*      */       }
/* 1231 */       if (i < 10) {
/* 1232 */         s = s + "   " + i;
/* 1233 */       } else if (i < 100) {
/* 1234 */         s = s + "  " + i;
/*      */       } else {
/* 1236 */         s = s + " " + i;
/*      */       }
/* 1238 */       MLN.minor(this, s);
/*      */     }
/*      */     
/* 1241 */     MLN.minor(this, "spaceAbove:");
/* 1242 */     s = "    ";
/* 1243 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1244 */       s = s + net.minecraft.util.MathHelper.func_76128_c(j / 10) % 10;
/*      */     }
/* 1246 */     MLN.minor(this, s);
/* 1247 */     s = "    ";
/* 1248 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1249 */       s = s + j % 10;
/*      */     }
/* 1251 */     MLN.minor(this, s);
/* 1252 */     for (int i = 0; i < this.winfo.length; i++) {
/* 1253 */       if (i < 10) {
/* 1254 */         s = i + "   ";
/* 1255 */       } else if (i < 100) {
/* 1256 */         s = i + "  ";
/*      */       } else {
/* 1258 */         s = i + " ";
/*      */       }
/* 1260 */       for (int j = 0; j < this.winfo.width; j++) {
/* 1261 */         s = s + this.winfo.spaceAbove[i][j];
/*      */       }
/* 1263 */       if (i < 10) {
/* 1264 */         s = s + "   " + i;
/* 1265 */       } else if (i < 100) {
/* 1266 */         s = s + "  " + i;
/*      */       } else {
/* 1268 */         s = s + " " + i;
/*      */       }
/* 1270 */       MLN.minor(this, s);
/*      */     }
/*      */     
/* 1273 */     MLN.minor(this, "Y pos:");
/* 1274 */     s = "    ";
/* 1275 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1276 */       s = s + net.minecraft.util.MathHelper.func_76128_c(j / 10) % 10;
/*      */     }
/* 1278 */     MLN.minor(this, s);
/* 1279 */     s = "    ";
/* 1280 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1281 */       s = s + j % 10;
/*      */     }
/* 1283 */     MLN.minor(this, s);
/* 1284 */     for (int i = 0; i < this.winfo.length; i++) {
/* 1285 */       if (i < 10) {
/* 1286 */         s = i + "   ";
/* 1287 */       } else if (i < 100) {
/* 1288 */         s = i + "  ";
/*      */       } else {
/* 1290 */         s = i + " ";
/*      */       }
/* 1292 */       for (int j = 0; j < this.winfo.width; j++) {
/* 1293 */         s = s + this.winfo.topGround[i][j] % 10;
/*      */       }
/* 1295 */       if (i < 10) {
/* 1296 */         s = s + "   " + i;
/* 1297 */       } else if (i < 100) {
/* 1298 */         s = s + "  " + i;
/*      */       } else {
/* 1300 */         s = s + " " + i;
/*      */       }
/* 1302 */       MLN.minor(this, s);
/*      */     }
/*      */     
/* 1305 */     MLN.minor(this, "Nodes:");
/* 1306 */     s = "    ";
/* 1307 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1308 */       s = s + net.minecraft.util.MathHelper.func_76128_c(j / 10) % 10;
/*      */     }
/* 1310 */     MLN.minor(this, s);
/* 1311 */     s = "    ";
/* 1312 */     for (int j = 0; j < this.winfo.width; j++) {
/* 1313 */       s = s + j % 10;
/*      */     }
/* 1315 */     MLN.minor(this, s);
/* 1316 */     for (int i = 0; i < this.winfo.length; i++) {
/* 1317 */       if (i < 10) {
/* 1318 */         s = i + "   ";
/* 1319 */       } else if (i < 100) {
/* 1320 */         s = i + "  ";
/*      */       } else {
/* 1322 */         s = i + " ";
/*      */       }
/* 1324 */       for (int j = 0; j < this.winfo.width; j++)
/*      */       {
/* 1326 */         boolean found = false;
/* 1327 */         for (Node n : this.nodes) {
/* 1328 */           if ((n.pos.x == i) && (n.pos.z == j)) {
/* 1329 */             s = s + Integer.toHexString(n.id % 10);
/* 1330 */             found = true;
/*      */           }
/*      */         }
/*      */         
/* 1334 */         if (!found) {
/* 1335 */           if ((this.top[i][j] == 0) && (this.bottom[i][j] == 0) && (this.left[i][j] == 0) && (this.right[i][j] == 0)) {
/* 1336 */             s = s + "#";
/* 1337 */           } else if ((this.top[i][j] == 0) || (this.bottom[i][j] == 0) || (this.left[i][j] == 0) || (this.right[i][j] == 0)) {
/* 1338 */             s = s + ".";
/*      */           } else {
/* 1340 */             s = s + " ";
/*      */           }
/*      */         }
/*      */       }
/* 1344 */       if (i < 10) {
/* 1345 */         s = s + "   " + i;
/* 1346 */       } else if (i < 100) {
/* 1347 */         s = s + "  " + i;
/*      */       } else {
/* 1349 */         s = s + " " + i;
/*      */       }
/* 1351 */       MLN.minor(this, s);
/*      */     }
/*      */     
/* 1354 */     MLN.minor(this, "Displaying connections finished. Time taken: " + (System.nanoTime() - startTime) / 1000000.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void findRegions(org.millenaire.common.Point thStanding)
/*      */     throws org.millenaire.common.MLN.MillenaireException
/*      */   {
/* 1375 */     int nodesMarked = 0;int nodeGroup = 0;
/*      */     
/* 1377 */     while (nodesMarked < this.nodes.size())
/*      */     {
/* 1379 */       nodeGroup++;
/*      */       
/* 1381 */       List<Node> toVisit = new java.util.ArrayList();
/*      */       
/* 1383 */       Node fn = null;
/*      */       
/* 1385 */       int i = 0;
/* 1386 */       while (fn == null) {
/* 1387 */         if (((Node)this.nodes.get(i)).region == 0) {
/* 1388 */           fn = (Node)this.nodes.get(i);
/*      */         }
/* 1390 */         i++;
/*      */       }
/*      */       
/* 1393 */       fn.region = nodeGroup;
/* 1394 */       nodesMarked++;
/*      */       
/* 1396 */       toVisit.add(fn);
/*      */       
/* 1398 */       while (toVisit.size() > 0)
/*      */       {
/* 1400 */         for (Node n : ((Node)toVisit.get(0)).neighbours) {
/* 1401 */           if (n.region == 0) {
/* 1402 */             n.region = nodeGroup;
/* 1403 */             toVisit.add(n);
/* 1404 */             nodesMarked++;
/* 1405 */           } else if (n.region != nodeGroup) {
/* 1406 */             throw new org.millenaire.common.MLN.MillenaireException("Node belongs to group " + n.region + " but reached from " + nodeGroup);
/*      */           }
/*      */         }
/* 1409 */         toVisit.remove(0);
/*      */       }
/*      */     }
/*      */     
/* 1413 */     for (int i = 0; i < this.winfo.length; i++) {
/* 1414 */       for (int j = 0; j < this.winfo.width; j++) {
/* 1415 */         this.regions[i][j] = -1;
/*      */       }
/*      */     }
/*      */     
/* 1419 */     for (Node n : this.nodes) {
/* 1420 */       this.regions[n.pos.x][n.pos.z] = ((byte)n.region);
/*      */     }
/*      */     
/* 1423 */     boolean spreaddone = true;
/*      */     
/* 1425 */     while (spreaddone) {
/* 1426 */       spreaddone = false;
/* 1427 */       for (int i = 0; i < this.winfo.length; i++) {
/* 1428 */         for (int j = 0; j < this.winfo.width; j++) {
/* 1429 */           if (this.regions[i][j] > 0) {
/* 1430 */             byte regionid = this.regions[i][j];
/* 1431 */             int x = i;
/* 1432 */             while ((x > 1) && (this.top[x][j] != 0) && (this.regions[(x - 1)][j] == -1)) {
/* 1433 */               x--;
/* 1434 */               this.regions[x][j] = regionid;
/* 1435 */               spreaddone = true;
/*      */             }
/* 1437 */             x = i;
/* 1438 */             while ((x < this.winfo.length - 1) && (this.bottom[x][j] != 0) && (this.regions[(x + 1)][j] == -1)) {
/* 1439 */               x++;
/* 1440 */               this.regions[x][j] = regionid;
/* 1441 */               spreaddone = true;
/*      */             }
/* 1443 */             x = j;
/* 1444 */             while ((x > 1) && (this.left[i][x] != 0) && (this.regions[i][(x - 1)] == -1)) {
/* 1445 */               x--;
/* 1446 */               this.regions[i][x] = regionid;
/* 1447 */               spreaddone = true;
/*      */             }
/* 1449 */             x = j;
/* 1450 */             while ((x < this.winfo.width - 1) && (this.right[i][x] != 0) && (this.regions[i][(x + 1)] == -1)) {
/* 1451 */               x++;
/* 1452 */               this.regions[i][x] = regionid;
/* 1453 */               spreaddone = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1461 */     this.thRegion = this.regions[(thStanding.getiX() - this.winfo.mapStartX)][(thStanding.getiZ() - this.winfo.mapStartZ)];
/*      */     
/* 1463 */     if (MLN.LogConnections >= 2) {
/* 1464 */       MLN.minor(this, nodeGroup + " node groups found.");
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isInArea(org.millenaire.common.Point p)
/*      */   {
/* 1470 */     return (p.x >= this.winfo.mapStartX) && (p.x < this.winfo.mapStartX + this.winfo.length) && (p.z >= this.winfo.mapStartZ) && (p.z < this.winfo.mapStartZ + this.winfo.width);
/*      */   }
/*      */   
/*      */   public boolean isValidPoint(org.millenaire.common.Point p)
/*      */   {
/* 1475 */     if (!isInArea(p)) {
/* 1476 */       return false;
/*      */     }
/*      */     
/* 1479 */     return this.winfo.spaceAbove[(p.getiX() - this.winfo.mapStartX)][(p.getiZ() - this.winfo.mapStartZ)] > 1;
/*      */   }
/*      */   
/*      */   private void storeInCache(CachedPath path, Point2D dest)
/*      */     throws Exception
/*      */   {
/* 1485 */     for (Point2D p : path.points) {
/* 1486 */       if (p == null) {
/* 1487 */         throw new Exception("Null node in path to cache: " + path);
/*      */       }
/*      */     }
/*      */     
/* 1491 */     this.cache.put(path.getKey(), path);
/*      */     
/* 1493 */     if (!dest.equals(path.getEnd())) {
/* 1494 */       this.cache.put(new PathKey(path.getStart(), dest), path);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1500 */     for (int i = 1; i < path.points.length - 2; i++) {
/* 1501 */       CachedPath cp = new CachedPath(path, i);
/*      */       
/* 1503 */       for (Point2D p : cp.points) {
/* 1504 */         if (p == null) {
/* 1505 */           throw new Exception("Null node in path to cache: " + path);
/*      */         }
/*      */       }
/*      */       
/* 1509 */       this.cache.put(cp.getKey(), cp);
/*      */       
/* 1511 */       if (!dest.equals(cp.getEnd())) {
/* 1512 */         this.cache.put(new PathKey(cp.getStart(), dest), cp);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\AStarPathing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common.pathing.atomicstryker;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.MLN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AStarPathPlanner
/*     */ {
/*  19 */   private static ExecutorService executorService = ;
/*     */   private AStarWorker worker;
/*     */   private final World worldObj;
/*     */   private final IAStarPathedEntity pathedEntity;
/*     */   private boolean accesslock;
/*     */   private boolean isJPS;
/*     */   private AStarNode lastStart;
/*     */   private AStarNode lastEnd;
/*     */   public AStarConfig config;
/*     */   
/*     */   public AStarPathPlanner(World world, IAStarPathedEntity ent)
/*     */   {
/*  31 */     this.worker = new AStarWorker(this);
/*  32 */     this.worldObj = world;
/*  33 */     this.accesslock = false;
/*  34 */     this.pathedEntity = ent;
/*  35 */     this.isJPS = true;
/*     */   }
/*     */   
/*     */   private void flushWorker() {
/*  39 */     if (!this.accesslock)
/*     */     {
/*  41 */       this.worker = (this.isJPS ? new AStarWorkerJPS(this) : new AStarWorker(this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void getPath(AStarNode start, AStarNode end, AStarConfig config) {
/*  46 */     if (isBusy()) {
/*  47 */       stopPathSearch(true);
/*     */     }
/*     */     
/*  50 */     while (this.accesslock) {
/*  51 */       Thread.yield();
/*     */     }
/*  53 */     flushWorker();
/*  54 */     this.accesslock = true;
/*     */     
/*  56 */     this.lastStart = start;
/*  57 */     this.lastEnd = end;
/*     */     
/*  59 */     this.config = config;
/*     */     
/*  61 */     this.worker.setup(this.worldObj, start, end, config.allowDropping);
/*     */     try {
/*  63 */       this.worker.isRunning = true;
/*  64 */       executorService.submit(this.worker);
/*     */     } catch (Exception e) {
/*  66 */       MLN.printException(e);
/*     */     }
/*     */     
/*     */ 
/*  70 */     this.accesslock = false;
/*     */   }
/*     */   
/*     */   public void getPath(int startx, int starty, int startz, int destx, int desty, int destz, AStarConfig config)
/*     */   {
/*  75 */     if (!AStarStatic.isViable(this.worldObj, startx, starty, startz, 0, config)) {
/*  76 */       starty--;
/*     */     }
/*  78 */     if (!AStarStatic.isViable(this.worldObj, startx, starty, startz, 0, config)) {
/*  79 */       starty += 2;
/*     */     }
/*  81 */     if (!AStarStatic.isViable(this.worldObj, startx, starty, startz, 0, config)) {
/*  82 */       starty--;
/*     */     }
/*     */     
/*  85 */     AStarNode starter = new AStarNode(startx, starty, startz, 0, null);
/*  86 */     AStarNode finish = new AStarNode(destx, desty, destz, -1, null);
/*     */     
/*  88 */     getPath(starter, finish, config);
/*     */   }
/*     */   
/*     */   public boolean isBusy()
/*     */   {
/*  93 */     return this.worker.isBusy();
/*     */   }
/*     */   
/*     */   public void onFoundPath(ArrayList<AStarNode> result) {
/*  97 */     setJPS(true);
/*  98 */     if (this.pathedEntity != null) {
/*  99 */       this.pathedEntity.onFoundPath(result);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onNoPathAvailable() {
/* 104 */     if (this.isJPS)
/*     */     {
/* 106 */       setJPS(false);
/* 107 */       getPath(this.lastStart, this.lastEnd, this.config);
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     if (this.pathedEntity != null) {
/* 112 */       this.pathedEntity.onNoPathAvailable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setJPS(boolean b) {
/* 117 */     this.isJPS = b;
/* 118 */     flushWorker();
/*     */   }
/*     */   
/*     */   public void stopPathSearch(boolean interrupted) {
/* 122 */     flushWorker();
/* 123 */     if ((this.pathedEntity != null) && (!interrupted)) {
/* 124 */       this.pathedEntity.onNoPathAvailable();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\pathing\atomicstryker\AStarPathPlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
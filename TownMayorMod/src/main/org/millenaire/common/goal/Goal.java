/*     */ package org.millenaire.common.goal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MillVillager;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.building.Building;
/*     */ 
/*     */ public abstract class Goal
/*     */ {
/*     */   public static final int STANDARD_DELAY = 2000;
/*     */   public static HashMap<String, Goal> goals;
/*     */   public static GoalBeSeller beSeller;
/*     */   public static Goal construction;
/*     */   public static Goal deliverGoodsHousehold;
/*     */   public static Goal getResourcesForBuild;
/*     */   public static Goal raidVillage;
/*     */   public static Goal defendVillage;
/*     */   public static Goal hide;
/*     */   public static Goal sleep;
/*     */   public static Goal gettool;
/*     */   public static Goal gosocialise;
/*     */   
/*     */   public static class GoalInformation
/*     */   {
/*     */     private Point dest;
/*     */     private Point destBuildingPos;
/*     */     private net.minecraft.entity.Entity targetEnt;
/*     */     
/*     */     public GoalInformation(Point dest, Point buildingPos, net.minecraft.entity.Entity targetEnt)
/*     */     {
/*  32 */       this.dest = dest;
/*  33 */       this.destBuildingPos = buildingPos;
/*  34 */       this.targetEnt = targetEnt;
/*     */     }
/*     */     
/*     */     public Point getDest() {
/*  38 */       return this.dest;
/*     */     }
/*     */     
/*     */     public Point getDestBuildingPos() {
/*  42 */       return this.destBuildingPos;
/*     */     }
/*     */     
/*     */     public net.minecraft.entity.Entity getTargetEnt() {
/*  46 */       return this.targetEnt;
/*     */     }
/*     */     
/*     */     public void setDest(Point dest) {
/*  50 */       this.dest = dest;
/*     */     }
/*     */     
/*     */     public void setDestBuildingPos(Point destBuildingPos) {
/*  54 */       this.destBuildingPos = destBuildingPos;
/*     */     }
/*     */     
/*     */     public void setTargetEnt(net.minecraft.entity.Entity targetEnt) {
/*  58 */       this.targetEnt = targetEnt;
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
/*  76 */   public static final org.millenaire.common.pathing.atomicstryker.AStarConfig JPS_CONFIG_TIGHT = new org.millenaire.common.pathing.atomicstryker.AStarConfig(true, false, false, true);
/*  77 */   public static final org.millenaire.common.pathing.atomicstryker.AStarConfig JPS_CONFIG_WIDE = new org.millenaire.common.pathing.atomicstryker.AStarConfig(true, false, false, true, 2, 10);
/*  78 */   public static final org.millenaire.common.pathing.atomicstryker.AStarConfig JPS_CONFIG_BUILDING = new org.millenaire.common.pathing.atomicstryker.AStarConfig(true, false, false, true, 2, 20);
/*  79 */   public static final org.millenaire.common.pathing.atomicstryker.AStarConfig JPS_CONFIG_CHOPLUMBER = new org.millenaire.common.pathing.atomicstryker.AStarConfig(true, false, false, true, 4, 20);
/*     */   
/*  81 */   public static final org.millenaire.common.pathing.atomicstryker.AStarConfig JPS_CONFIG_SLAUGHTERSQUIDS = new org.millenaire.common.pathing.atomicstryker.AStarConfig(true, false, false, true, 6, 4);
/*     */   
/*  83 */   protected static final Point[] EMPTY_DEST = { null, null };
/*     */   public String key;
/*     */   
/*  86 */   public static void initGoals() { goals = new HashMap();
/*     */     
/*  88 */     goals.put("gorest", new org.millenaire.common.goal.leasure.GoalGoRest());
/*  89 */     goals.put("godrink", new org.millenaire.common.goal.leasure.GoalGoDrink());
/*  90 */     goals.put("gopray", new org.millenaire.common.goal.leasure.GoalGoPray());
/*     */     
/*  92 */     gosocialise = new org.millenaire.common.goal.leasure.GoalGoSocialise();
/*  93 */     goals.put("gosocialise", gosocialise);
/*  94 */     goals.put("chat", new org.millenaire.common.goal.leasure.GoalGoChat());
/*     */     
/*  96 */     goals.put("gathergoods", new GoalGatherGoods());
/*  97 */     goals.put("bringbackresourceshome", new GoalBringBackResourcesHome());
/*  98 */     gettool = new GoalGetTool();
/*  99 */     goals.put("getitemtokeep", gettool);
/* 100 */     goals.put("huntmonster", new GoalHuntMonster());
/* 101 */     goals.put("getgoodshousehold", new GoalGetGoodsForHousehold());
/*     */     
/* 103 */     sleep = new GoalSleep();
/* 104 */     goals.put("sleep", sleep);
/*     */     
/* 106 */     deliverGoodsHousehold = new GoalDeliverGoodsHousehold();
/* 107 */     goals.put("delivergoodshousehold", deliverGoodsHousehold);
/* 108 */     goals.put("gethousethresources", new GoalGetResourcesForShops());
/* 109 */     goals.put("deliverresourcesshop", new GoalDeliverResourcesShop());
/*     */     
/* 111 */     goals.put("choptrees", new GoalLumbermanChopTrees());
/* 112 */     goals.put("plantsaplings", new GoalLumbermanPlantSaplings());
/*     */     
/* 114 */     getResourcesForBuild = new GoalGetResourcesForBuild();
/* 115 */     goals.put("getresourcesforbuild", getResourcesForBuild);
/* 116 */     beSeller = new GoalBeSeller();
/* 117 */     goals.put("beseller", beSeller);
/*     */     
/* 119 */     construction = new GoalConstructionStepByStep();
/* 120 */     goals.put("construction", construction);
/* 121 */     goals.put("buildpath", new GoalBuildPath());
/* 122 */     goals.put("clearoldpath", new GoalClearOldPath());
/*     */     
/* 124 */     raidVillage = new GoalRaidVillage();
/* 125 */     goals.put("raidvillage", raidVillage);
/* 126 */     defendVillage = new GoalDefendVillage();
/* 127 */     goals.put("defendvillage", defendVillage);
/* 128 */     hide = new GoalHide();
/* 129 */     goals.put("hide", hide);
/*     */     
/* 131 */     goals.put("goplay", new org.millenaire.common.goal.leasure.GoalChildGoPlay());
/* 132 */     goals.put("becomeadult", new GoalChildBecomeAdult());
/* 133 */     goals.put("patrol", new GoalGuardPatrol());
/* 134 */     goals.put("shearsheep", new GoalShearSheep());
/* 135 */     goals.put("breed", new GoalBreedAnimals());
/* 136 */     goals.put("mining", new GoalMinerMineResource());
/* 137 */     goals.put("visitinn", new GoalMerchantVisitInn());
/* 138 */     goals.put("visitbuilding", new GoalMerchantVisitBuilding());
/*     */     
/* 140 */     goals.put("keepstall", new GoalForeignMerchantKeepStall());
/* 141 */     goals.put("drybrick", new GoalIndianDryBrick());
/* 142 */     goals.put("gatherbrick", new GoalIndianGatherBrick());
/* 143 */     goals.put("plantsugarcane", new GoalIndianPlantSugarCane());
/* 144 */     goals.put("harvestsugarcane", new GoalIndianHarvestSugarCane());
/* 145 */     goals.put("performpujas", new GoalPerformPuja());
/* 146 */     goals.put("bepujaperformer", new GoalBePujaPerformer());
/*     */     
/* 148 */     goals.put("fish", new GoalFish());
/*     */     
/* 150 */     goals.put("harvestwarts", new GoalHarvestWarts());
/* 151 */     goals.put("plantwarts", new GoalPlantNetherWarts());
/* 152 */     goals.put("brewpotions", new GoalBrewPotions());
/*     */     
/* 154 */     goals.put("gathersilk", new GoalByzantineGatherSilk());
/*     */     
/* 156 */     goals.put("plantcocoa", new GoalPlantCacao());
/* 157 */     goals.put("harvestcocoa", new GoalHarvestCacao());
/*     */     
/* 159 */     org.millenaire.common.goal.generic.GoalGeneric.loadGenericGoals();
/*     */     
/* 161 */     for (String s : goals.keySet()) {
/* 162 */       ((Goal)goals.get(s)).key = s;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 169 */   public boolean leasure = false;
/* 170 */   public HashMap<InvItem, Integer> buildingLimit = new HashMap();
/*     */   
/* 172 */   public HashMap<InvItem, Integer> townhallLimit = new HashMap();
/* 173 */   public int maxSimultaneousInBuilding = 0;
/*     */   
/* 175 */   public int maxSimultaneousTotal = 0;
/*     */   
/* 177 */   public InvItem balanceOutput1 = null; public InvItem balanceOutput2 = null;
/*     */   
/* 179 */   protected static int ACTIVATION_RANGE = 3;
/*     */   
/*     */ 
/*     */   public int actionDuration(MillVillager villager)
/*     */     throws Exception
/*     */   {
/* 185 */     return 500;
/*     */   }
/*     */   
/*     */   public boolean allowRandomMoves() throws Exception {
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canBeDoneAtNight() {
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canBeDoneInDayTime() {
/* 197 */     return true;
/*     */   }
/*     */   
/*     */   public String gameName(MillVillager villager)
/*     */   {
/* 202 */     if ((villager != null) && (getCurrentGoalTarget(villager) != null) && (getCurrentGoalTarget(villager).horizontalDistanceTo(villager) > range(villager))) {
/* 203 */       return org.millenaire.common.MLN.string("goal." + labelKeyWhileTravelling(villager));
/*     */     }
/*     */     
/* 206 */     return org.millenaire.common.MLN.string("goal." + labelKey(villager));
/*     */   }
/*     */   
/*     */   public Point getCurrentGoalTarget(MillVillager villager)
/*     */   {
/* 211 */     if (villager.getGoalDestEntity() != null) {
/* 212 */       return new Point(villager.getGoalDestEntity());
/*     */     }
/*     */     
/* 215 */     return villager.getGoalDestPoint();
/*     */   }
/*     */   
/*     */   public abstract GoalInformation getDestination(MillVillager paramMillVillager) throws Exception;
/*     */   
/*     */   public net.minecraft.item.ItemStack[] getHeldItemsDestination(MillVillager villager) throws Exception
/*     */   {
/* 222 */     return getHeldItemsTravelling(villager);
/*     */   }
/*     */   
/*     */   public net.minecraft.item.ItemStack[] getHeldItemsTravelling(MillVillager villager) throws Exception {
/* 226 */     return null;
/*     */   }
/*     */   
/*     */   public org.millenaire.common.pathing.atomicstryker.AStarConfig getPathingConfig() {
/* 230 */     return MillVillager.DEFAULT_JPS_CONFIG;
/*     */   }
/*     */   
/*     */   public net.minecraft.entity.Entity getTargetEntity(MillVillager villager) {
/* 234 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isFightingGoal() {
/* 238 */     return false;
/*     */   }
/*     */   
/*     */   public final boolean isPossible(MillVillager villager)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       if ((villager.field_70170_p.func_72935_r()) && (!canBeDoneInDayTime())) {
/* 246 */         return false;
/*     */       }
/*     */       
/* 249 */       if ((!villager.field_70170_p.func_72935_r()) && (!canBeDoneAtNight())) {
/* 250 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 254 */       for (InvItem item : this.townhallLimit.keySet()) {
/* 255 */         if (villager.getTownHall().countGoods(item) > ((Integer)this.townhallLimit.get(item)).intValue()) {
/* 256 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 260 */       if ((this.balanceOutput1 != null) && 
/* 261 */         (villager.getTownHall().nbGoodAvailable(new InvItem(this.balanceOutput1.item, this.balanceOutput1.meta), false, false) < villager.getTownHall().nbGoodAvailable(new InvItem(this.balanceOutput2.item, this.balanceOutput2.meta), false, false)))
/*     */       {
/* 263 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 267 */       if (this.maxSimultaneousTotal > 0)
/*     */       {
/* 269 */         int nbSame = 0;
/*     */         
/* 271 */         for (MillVillager v : villager.getTownHall().villagers) {
/* 272 */           if ((v != villager) && (this.key.equals(v.goalKey))) {
/* 273 */             nbSame++;
/*     */           }
/*     */         }
/*     */         
/* 277 */         if (nbSame >= this.maxSimultaneousTotal) {
/* 278 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 282 */       return isPossibleSpecific(villager);
/*     */     }
/*     */     catch (Exception e) {
/* 285 */       org.millenaire.common.MLN.printException("Exception in isPossible() for goal: " + this.key + " and villager: " + villager, e); }
/* 286 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isPossibleSpecific(MillVillager villager) throws Exception
/*     */   {
/* 291 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean isStillValid(MillVillager villager) throws Exception
/*     */   {
/* 296 */     if ((villager.field_70170_p.func_72935_r()) && (!canBeDoneInDayTime())) {
/* 297 */       return false;
/*     */     }
/*     */     
/* 300 */     if ((!villager.field_70170_p.func_72935_r()) && (!canBeDoneAtNight())) {
/* 301 */       return false;
/*     */     }
/*     */     
/* 304 */     if (this.leasure) {
/* 305 */       for (Goal g : villager.getGoals()) {
/* 306 */         if ((!g.leasure) && (g.isPossible(villager))) {
/* 307 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 313 */     if ((villager.getGoalDestPoint() == null) && (villager.getGoalDestEntity() == null)) {
/* 314 */       return false;
/*     */     }
/*     */     
/* 317 */     return isStillValidSpecific(villager);
/*     */   }
/*     */   
/*     */   protected boolean isStillValidSpecific(MillVillager villager) throws Exception {
/* 321 */     return true;
/*     */   }
/*     */   
/*     */   public String labelKey(MillVillager villager) {
/* 325 */     return this.key;
/*     */   }
/*     */   
/*     */   public String labelKeyWhileTravelling(MillVillager villager) {
/* 329 */     return this.key;
/*     */   }
/*     */   
/*     */   public boolean lookAtGoal() {
/* 333 */     return false;
/*     */   }
/*     */   
/*     */   public boolean lookAtPlayer() {
/* 337 */     return false;
/*     */   }
/*     */   
/*     */   public String nextGoal(MillVillager villager) throws Exception {
/* 341 */     return null;
/*     */   }
/*     */   
/*     */   public void onAccept(MillVillager villager) throws Exception
/*     */   {}
/*     */   
/*     */   public void onComplete(MillVillager villager) throws Exception
/*     */   {}
/*     */   
/*     */   protected GoalInformation packDest(Point p) {
/* 351 */     return new GoalInformation(p, null, null);
/*     */   }
/*     */   
/*     */   protected GoalInformation packDest(Point p, Building b) {
/* 355 */     return new GoalInformation(p, b.getPos(), null);
/*     */   }
/*     */   
/*     */   protected GoalInformation packDest(Point p, Building b, net.minecraft.entity.Entity ent) {
/* 359 */     if (b == null) {
/* 360 */       return new GoalInformation(p, null, ent);
/*     */     }
/*     */     
/* 363 */     return new GoalInformation(p, b.getPos(), ent);
/*     */   }
/*     */   
/*     */   protected GoalInformation packDest(Point p, Point p2) {
/* 367 */     return new GoalInformation(p, p2, null);
/*     */   }
/*     */   
/*     */   public abstract boolean performAction(MillVillager paramMillVillager) throws Exception;
/*     */   
/*     */   public abstract int priority(MillVillager paramMillVillager) throws Exception;
/*     */   
/*     */   public int range(MillVillager villager) {
/* 375 */     return ACTIVATION_RANGE;
/*     */   }
/*     */   
/*     */   public String sentenceKey() {
/* 379 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setVillagerDest(MillVillager villager) throws Exception {
/* 383 */     villager.setGoalInformation(getDestination(villager));
/*     */   }
/*     */   
/*     */   public boolean shouldVillagerLieDown() {
/* 387 */     return false;
/*     */   }
/*     */   
/*     */   public boolean stopMovingWhileWorking() {
/* 391 */     return true;
/*     */   }
/*     */   
/*     */   public boolean stuckAction(MillVillager villager) throws Exception {
/* 395 */     return false;
/*     */   }
/*     */   
/*     */   public long stuckDelay(MillVillager villager) {
/* 399 */     return 10000L;
/*     */   }
/*     */   
/*     */   public boolean swingArms() {
/* 403 */     return false;
/*     */   }
/*     */   
/*     */   public boolean swingArms(MillVillager villager)
/*     */   {
/* 408 */     if ((villager != null) && (getCurrentGoalTarget(villager) != null) && (getCurrentGoalTarget(villager).horizontalDistanceTo(villager) > range(villager))) {
/* 409 */       return swingArmsWhileTravelling();
/*     */     }
/*     */     
/* 412 */     return swingArms();
/*     */   }
/*     */   
/*     */   public boolean swingArmsWhileTravelling() {
/* 416 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 421 */     return "goal:" + this.key;
/*     */   }
/*     */   
/*     */   public boolean unreachableDestination(MillVillager villager) throws Exception
/*     */   {
/* 426 */     if ((villager.getGoalDestPoint() == null) && (villager.getGoalDestEntity() == null)) {
/* 427 */       return false;
/*     */     }
/*     */     
/* 430 */     int[] jumpTo = org.millenaire.common.core.MillCommonUtilities.getJumpDestination(villager.field_70170_p, villager.getPathDestPoint().getiX(), villager.getTownHall().getAltitude(villager.getPathDestPoint().getiX(), villager.getPathDestPoint().getiZ()), villager.getPathDestPoint().getiZ());
/*     */     
/*     */ 
/* 433 */     if (jumpTo != null) {
/* 434 */       if ((org.millenaire.common.MLN.LogPathing >= 2) && (villager.extraLog)) {
/* 435 */         org.millenaire.common.MLN.minor(this, "Dest unreachable. Jumping " + villager + " from " + villager.getPos() + " to " + jumpTo[0] + "/" + jumpTo[1] + "/" + jumpTo[2]);
/*     */       }
/* 437 */       villager.func_70107_b(jumpTo[0] + 0.5D, jumpTo[1] + 0.5D, jumpTo[2] + 0.5D);
/* 438 */       return true;
/*     */     }
/*     */     
/* 441 */     if ((org.millenaire.common.MLN.LogPathing >= 2) && (villager.extraLog)) {
/* 442 */       org.millenaire.common.MLN.minor(this, "Dest unreachable. Couldn't jump " + villager + " from " + villager.getPos() + " to " + villager.getPathDestPoint());
/*     */     }
/* 444 */     return false;
/*     */   }
/*     */   
/*     */   public boolean validateDest(MillVillager villager, Building dest)
/*     */     throws org.millenaire.common.MLN.MillenaireException
/*     */   {
/* 450 */     if (dest == null) {
/* 451 */       throw new org.millenaire.common.MLN.MillenaireException("Given null dest in validateDest for goal: " + this.key);
/*     */     }
/*     */     
/* 454 */     for (InvItem item : this.buildingLimit.keySet()) {
/* 455 */       if (dest.countGoods(item) > ((Integer)this.buildingLimit.get(item)).intValue()) {
/* 456 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 460 */     int nbSameBuilding = 0;
/*     */     
/* 462 */     if (this.maxSimultaneousInBuilding > 0)
/*     */     {
/* 464 */       for (MillVillager v : villager.getTownHall().villagers) {
/* 465 */         if ((v != villager) && (this.key.equals(v.goalKey)) && 
/* 466 */           (v.getGoalBuildingDest() == dest)) {
/* 467 */           nbSameBuilding++;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 472 */       if (nbSameBuilding >= this.maxSimultaneousInBuilding) {
/* 473 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 477 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\goal\Goal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
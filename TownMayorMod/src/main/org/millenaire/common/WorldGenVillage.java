/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.building.BuildingPlan.LocationBuildingPair;
/*     */ import org.millenaire.common.building.BuildingPlan.LocationReturn;
/*     */ import org.millenaire.common.building.BuildingPlanSet;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.building.BuildingProject.EnumProjects;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.network.ServerSender;
/*     */ import org.millenaire.common.pathing.AStarPathing;
/*     */ 
/*     */ public class WorldGenVillage implements cpw.mods.fml.common.IWorldGenerator
/*     */ {
/*     */   private static final double MINIMUM_USABLE_BLOCK_PERC = 0.7D;
/*  33 */   public static HashSet<Integer> coordsTried = new HashSet();
/*     */   
/*     */   public static boolean generateBedrockLoneBuilding(Point p, World world, VillageType village, Random random, int minDistance, int maxDistance, EntityPlayer player)
/*     */     throws MLN.MillenaireException
/*     */   {
/*  38 */     if (world.field_72995_K) {
/*  39 */       return false;
/*     */     }
/*     */     
/*  42 */     if (p.horizontalDistanceTo(world.func_72861_E()) < MLN.spawnProtectionRadius) {
/*  43 */       return false;
/*     */     }
/*     */     
/*  46 */     if (village.centreBuilding == null) {
/*  47 */       MLN.printException(new MLN.MillenaireException("Tried to create a bedrock lone building without a centre."));
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     if (MLN.LogWorldGeneration >= 1) {
/*  52 */       MLN.major(null, "Generating bedrockbuilding: " + village);
/*     */     }
/*     */     
/*  55 */     BuildingPlan plan = village.centreBuilding.getRandomStartingPlan();
/*  56 */     BuildingLocation location = null;
/*     */     
/*  58 */     for (int i = 0; (i < 100) && (location == null); i++) {
/*  59 */       int x = minDistance + MillCommonUtilities.randomInt(maxDistance - minDistance);
/*  60 */       int z = minDistance + MillCommonUtilities.randomInt(maxDistance - minDistance);
/*     */       
/*  62 */       if (MillCommonUtilities.chanceOn(2)) {
/*  63 */         x = -x;
/*     */       }
/*  65 */       if (MillCommonUtilities.chanceOn(2)) {
/*  66 */         z = -z;
/*     */       }
/*     */       
/*  69 */       BuildingPlan.LocationReturn lr = plan.testSpotBedrock(world, p.getiX() + x, p.getiZ() + z);
/*  70 */       location = lr.location;
/*     */     }
/*     */     
/*  73 */     if (location == null) {
/*  74 */       MLN.major(null, "No spot found for: " + village);
/*     */       
/*  76 */       int x = minDistance + MillCommonUtilities.randomInt(maxDistance - minDistance);
/*  77 */       int z = minDistance + MillCommonUtilities.randomInt(maxDistance - minDistance);
/*     */       
/*  79 */       if (MillCommonUtilities.chanceOn(2)) {
/*  80 */         x = -x;
/*     */       }
/*  82 */       if (MillCommonUtilities.chanceOn(2)) {
/*  83 */         z = -z;
/*     */       }
/*     */       
/*  86 */       location = new BuildingLocation(plan, new Point(p.getiX() + x, 2.0D, p.getiZ() + z), 0);
/*  87 */       location.bedrocklevel = true;
/*     */     }
/*     */     
/*  90 */     List<BuildingPlan.LocationBuildingPair> lbps = village.centreBuilding.buildLocation(Mill.getMillWorld(world), village, location, true, true, null, false, null);
/*     */     
/*  92 */     Building townHallEntity = ((BuildingPlan.LocationBuildingPair)lbps.get(0)).building;
/*     */     
/*  94 */     if (MLN.LogWorldGeneration >= 1) {
/*  95 */       MLN.major(null, "Registering building: " + townHallEntity);
/*     */     }
/*  97 */     townHallEntity.villageType = village;
/*     */     
/*  99 */     townHallEntity.findName(null);
/* 100 */     townHallEntity.initialiseBuildingProjects();
/* 101 */     townHallEntity.registerBuildingLocation(location);
/*     */     
/* 103 */     for (BuildingPlan.LocationBuildingPair lbp : lbps) {
/* 104 */       if (lbp != lbps.get(0)) {
/* 105 */         townHallEntity.registerBuildingEntity(lbp.building);
/* 106 */         townHallEntity.registerBuildingLocation(lbp.location);
/*     */       }
/*     */     }
/* 109 */     townHallEntity.initialiseVillage();
/*     */     
/* 111 */     String playerName = null;
/*     */     
/* 113 */     if (player != null) {
/* 114 */       playerName = player.getDisplayName();
/*     */     }
/*     */     
/* 117 */     Mill.getMillWorld(world).registerLoneBuildingsLocation(world, townHallEntity.getPos(), townHallEntity.getVillageQualifiedName(), townHallEntity.villageType, townHallEntity.culture, true, playerName);
/*     */     
/*     */ 
/* 120 */     MLN.major(null, "Finished bedrock building " + village + " at " + townHallEntity.getPos());
/*     */     
/* 122 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
/*     */   {
/* 128 */     if (world.field_73011_w.field_76574_g != 0) {
/* 129 */       return;
/*     */     }
/*     */     
/*     */ 
/* 133 */     StackTraceElement[] trace = Thread.currentThread().getStackTrace();
/*     */     
/* 135 */     for (int i = 2; i < trace.length; i++) {
/* 136 */       if (trace[i].getClassName().equals(getClass().getName())) {
/* 137 */         return;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 142 */       generateVillageAtPoint(world, random, chunkX * 16, 0, chunkZ * 16, null, true, false, Integer.MAX_VALUE, null, null, null);
/*     */     } catch (Exception e) {
/* 144 */       MLN.printException("Exception when attempting to generate village in " + world + " (dimension: " + world.func_72912_H().func_76076_i() + ")", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean generateCustomVillage(Point p, World world, VillageType villageType, EntityPlayer player, Random random)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 153 */     long startTime = System.nanoTime();
/*     */     
/* 155 */     MillWorld mw = Mill.getMillWorld(world);
/*     */     
/* 157 */     BuildingLocation location = new BuildingLocation(villageType.customCentre, p, true);
/*     */     
/* 159 */     Building townHall = new Building(mw, villageType.culture, villageType, location, true, true, p, p);
/*     */     
/* 161 */     villageType.customCentre.registerResources(townHall, location);
/*     */     
/* 163 */     townHall.initialise(player, true);
/*     */     
/* 165 */     BuildingProject project = new BuildingProject(villageType.customCentre, location);
/*     */     
/* 167 */     if (!townHall.buildingProjects.containsKey(BuildingProject.EnumProjects.CUSTOMBUILDINGS)) {
/* 168 */       townHall.buildingProjects.put(BuildingProject.EnumProjects.CUSTOMBUILDINGS, new ArrayList());
/*     */     }
/*     */     
/* 171 */     ((List)townHall.buildingProjects.get(BuildingProject.EnumProjects.CUSTOMBUILDINGS)).add(project);
/*     */     
/* 173 */     townHall.initialiseVillage();
/*     */     
/* 175 */     mw.registerVillageLocation(world, townHall.getPos(), townHall.getVillageQualifiedName(), townHall.villageType, townHall.culture, true, player.getDisplayName());
/* 176 */     townHall.initialiseRelations(null);
/* 177 */     townHall.updateWorldInfo();
/*     */     
/* 179 */     townHall.storeGoods(Mill.parchmentVillageScroll, mw.villagesList.pos.size() - 1, 1);
/*     */     
/* 181 */     if (MLN.LogWorldGeneration >= 1) {
/* 182 */       MLN.major(this, "New custom village generated at " + p + ", took: " + (System.nanoTime() - startTime));
/*     */     }
/*     */     
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   private void generateHamlet(World world, VillageType hamlet, Point centralVillage, String name, Random random)
/*     */   {
/* 190 */     boolean generated = false;
/*     */     
/* 192 */     int minRadius = 130;
/* 193 */     while ((!generated) && (minRadius < 200))
/*     */     {
/* 195 */       double angle = 0.06280000000000001D * MillCommonUtilities.randomInt(100);
/* 196 */       int attempts = 0;
/*     */       
/* 198 */       while ((!generated) && (attempts < 300)) {
/* 199 */         angle += 0.020933333333333335D;
/* 200 */         int radius = minRadius + MillCommonUtilities.randomInt(40);
/*     */         
/* 202 */         int dx = (int)(Math.cos(angle) * radius);
/*     */         
/* 204 */         int dz = (int)(Math.sin(angle) * radius);
/*     */         
/* 206 */         if (MLN.LogWorldGeneration >= 1) {
/* 207 */           MLN.major(this, "Trying to generate a hamlet " + hamlet + " around: " + (centralVillage.getiX() + dx) + "/" + (centralVillage.getiZ() + dz));
/*     */         }
/* 209 */         generated = generateVillageAtPoint(world, random, centralVillage.getiX() + dx, 0, centralVillage.getiZ() + dz, null, false, true, 100, hamlet, name, centralVillage);
/*     */         
/* 211 */         attempts++;
/*     */       }
/* 213 */       minRadius += 50;
/*     */     }
/*     */     
/* 216 */     if ((!generated) && (MLN.LogWorldGeneration >= 1)) {
/* 217 */       MLN.major(this, "Could not generate hamlet " + hamlet);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean generateVillage(Point p, World world, VillageType village, EntityPlayer player, EntityPlayer closestPlayer, Random random, int minDistance, String name, boolean loneBuildings, Point parentVillage)
/*     */     throws MLN.MillenaireException
/*     */   {
/* 229 */     MillWorldInfo winfo = new MillWorldInfo();
/* 230 */     List<BuildingLocation> plannedBuildings = new ArrayList();
/*     */     
/* 232 */     MillWorld mw = Mill.getMillWorld(world);
/*     */     
/* 234 */     p = new Point(p.x, MillCommonUtilities.findTopSoilBlock(world, p.getiX(), p.getiZ()), p.z);
/*     */     
/* 236 */     winfo.update(world, plannedBuildings, null, p, village.radius);
/*     */     
/* 238 */     for (int x = p.getChunkX() - village.radius / 16 - 1; x <= p.getChunkX() + village.radius / 16; x++) {
/* 239 */       for (int z = p.getChunkZ() - village.radius / 16 - 1; z <= p.getChunkZ() + village.radius / 16; z++) {
/* 240 */         if (!world.func_72964_e(x, z).field_76636_d) {
/* 241 */           world.func_72863_F().func_73158_c(x, z);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 246 */     if ((player == null) && 
/* 247 */       (!isAppropriateArea(winfo, p, village.radius))) {
/* 248 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 252 */     long startTime = System.nanoTime();
/*     */     
/* 254 */     BuildingLocation location = village.centreBuilding.getRandomStartingPlan().findBuildingLocation(winfo, null, p, village.radius, random, 3);
/*     */     
/* 256 */     if (location == null) {
/* 257 */       if (MLN.LogWorldGeneration >= 2) {
/* 258 */         MLN.minor(this, "Could not find place for central building: " + village.centreBuilding);
/*     */       }
/*     */       
/* 261 */       if (player != null) {
/* 262 */         ServerSender.sendTranslatedSentence(player, '6', "ui.generatenotenoughspace", new String[0]);
/*     */       }
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     if (MLN.LogWorldGeneration >= 2) {
/* 268 */       MLN.minor(this, "Place found for TownHall (village type: " + village.key + "). Checking for the rest.");
/*     */     }
/*     */     
/* 271 */     p = location.pos;
/*     */     
/* 273 */     plannedBuildings.add(location);
/* 274 */     winfo.update(world, plannedBuildings, null, p, village.radius);
/*     */     
/* 276 */     boolean couldBuildKeyBuildings = true;
/*     */     
/* 278 */     AStarPathing pathing = new AStarPathing();
/*     */     
/* 280 */     pathing.createConnectionsTable(winfo, p);
/*     */     
/* 282 */     for (BuildingPlanSet planSet : village.startBuildings) {
/* 283 */       location = planSet.getRandomStartingPlan().findBuildingLocation(winfo, pathing, p, village.radius, random, -1);
/* 284 */       if (location != null) {
/* 285 */         plannedBuildings.add(location);
/* 286 */         winfo.update(world, plannedBuildings, null, p, village.radius);
/*     */       } else {
/* 288 */         couldBuildKeyBuildings = false;
/* 289 */         if (MLN.LogWorldGeneration >= 2) {
/* 290 */           MLN.minor(this, "Couldn't build " + planSet.key + ".");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 295 */     if (MLN.LogWorldGeneration >= 3) {
/* 296 */       MLN.debug(this, "Time taken for finding if building possible: " + (System.nanoTime() - startTime));
/*     */     }
/*     */     
/* 299 */     if (!couldBuildKeyBuildings) {
/* 300 */       if (player != null) {
/* 301 */         ServerSender.sendTranslatedSentence(player, '6', "ui.generatenotenoughspacevillage", new String[0]);
/*     */       }
/*     */       
/* 304 */       return false;
/*     */     }
/*     */     int minDistanceWithLoneBuildings;
/* 307 */     if (player == null)
/*     */     {
/*     */       int minDistanceWithLoneBuildings;
/*     */       
/*     */       int minDistanceWithVillages;
/* 312 */       if (loneBuildings) { int minDistanceWithLoneBuildings;
/* 313 */         if (village.isKeyLoneBuildingForGeneration(closestPlayer)) {
/* 314 */           int minDistanceWithVillages = Math.min(minDistance, MLN.minDistanceBetweenVillagesAndLoneBuildings) / 2;
/* 315 */           minDistanceWithLoneBuildings = Math.min(minDistance, MLN.minDistanceBetweenLoneBuildings) / 2;
/*     */         } else {
/* 317 */           int minDistanceWithVillages = Math.min(minDistance, MLN.minDistanceBetweenVillagesAndLoneBuildings);
/* 318 */           minDistanceWithLoneBuildings = Math.min(minDistance, MLN.minDistanceBetweenLoneBuildings);
/*     */         }
/*     */       } else {
/* 321 */         minDistanceWithVillages = Math.min(minDistance, MLN.minDistanceBetweenVillages);
/* 322 */         minDistanceWithLoneBuildings = Math.min(minDistance, MLN.minDistanceBetweenVillagesAndLoneBuildings);
/*     */       }
/*     */       
/* 325 */       for (Point thp : mw.villagesList.pos) {
/* 326 */         if (p.distanceTo(thp) < minDistanceWithVillages) {
/* 327 */           if (MLN.LogWorldGeneration >= 1) {
/* 328 */             MLN.major(this, "Found a nearby village on second attempt.");
/*     */           }
/* 330 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 334 */       for (Point thp : mw.loneBuildingsList.pos) {
/* 335 */         if (p.distanceTo(thp) < minDistanceWithLoneBuildings) {
/* 336 */           if (MLN.LogWorldGeneration >= 1) {
/* 337 */             MLN.major(this, "Found a nearby lone building on second attempt.");
/*     */           }
/* 339 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 344 */     if (MLN.LogWorldGeneration >= 1) {
/* 345 */       MLN.major(this, p + ": Generating village");
/*     */     }
/*     */     
/* 348 */     if (MLN.LogWorldGeneration >= 1) {
/* 349 */       for (BuildingLocation bl : plannedBuildings) {
/* 350 */         MLN.major(this, "Building " + bl.planKey + ": " + bl.minx + "/" + bl.minz + " to " + bl.maxx + "/" + bl.maxz);
/*     */       }
/*     */     }
/* 353 */     startTime = System.nanoTime();
/*     */     
/* 355 */     List<BuildingPlan.LocationBuildingPair> lbps = village.centreBuilding.buildLocation(mw, village, (BuildingLocation)plannedBuildings.get(0), true, true, null, false, player);
/*     */     
/* 357 */     Building townHallEntity = ((BuildingPlan.LocationBuildingPair)lbps.get(0)).building;
/*     */     
/* 359 */     if (MLN.LogWorldGeneration >= 1) {
/* 360 */       MLN.major(this, "Registering building: " + townHallEntity);
/*     */     }
/* 362 */     townHallEntity.villageType = village;
/* 363 */     townHallEntity.findName(name);
/* 364 */     townHallEntity.initialiseBuildingProjects();
/* 365 */     townHallEntity.registerBuildingLocation((BuildingLocation)plannedBuildings.get(0));
/*     */     
/* 367 */     for (BuildingPlan.LocationBuildingPair lbp : lbps) {
/* 368 */       if (lbp != lbps.get(0))
/*     */       {
/* 370 */         townHallEntity.registerBuildingEntity(lbp.building);
/* 371 */         townHallEntity.registerBuildingLocation(lbp.location);
/*     */       }
/*     */     }
/*     */     
/* 375 */     for (int i = 1; i < plannedBuildings.size(); i++)
/*     */     {
/* 377 */       BuildingLocation bl = (BuildingLocation)plannedBuildings.get(i);
/*     */       
/* 379 */       lbps = village.culture.getBuildingPlanSet(bl.planKey).buildLocation(mw, village, bl, true, false, townHallEntity.getPos(), false, player);
/* 380 */       if (MLN.LogWorldGeneration >= 1) {
/* 381 */         MLN.major(this, "Registering building: " + bl.planKey);
/*     */       }
/* 383 */       for (BuildingPlan.LocationBuildingPair lbp : lbps)
/*     */       {
/* 385 */         townHallEntity.registerBuildingEntity(lbp.building);
/* 386 */         townHallEntity.registerBuildingLocation(lbp.location);
/*     */       }
/*     */     }
/*     */     
/* 390 */     townHallEntity.initialiseVillage();
/*     */     
/* 392 */     String playerName = null;
/*     */     
/* 394 */     if (closestPlayer != null) {
/* 395 */       playerName = closestPlayer.getDisplayName();
/*     */     }
/*     */     
/* 398 */     if (loneBuildings) {
/* 399 */       mw.registerLoneBuildingsLocation(world, townHallEntity.getPos(), townHallEntity.getVillageQualifiedName(), townHallEntity.villageType, townHallEntity.culture, true, playerName);
/*     */     } else {
/* 401 */       mw.registerVillageLocation(world, townHallEntity.getPos(), townHallEntity.getVillageQualifiedName(), townHallEntity.villageType, townHallEntity.culture, true, playerName);
/* 402 */       townHallEntity.initialiseRelations(parentVillage);
/* 403 */       if (village.playerControlled) {
/* 404 */         townHallEntity.storeGoods(Mill.parchmentVillageScroll, mw.villagesList.pos.size() - 1, 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 409 */     if (MLN.LogWorldGeneration >= 1) {
/* 410 */       MLN.major(this, "New village generated at " + p + ", took: " + (System.nanoTime() - startTime));
/*     */     }
/*     */     
/* 413 */     for (String key : village.hamlets)
/*     */     {
/* 415 */       VillageType hamlet = village.culture.getVillageType(key);
/*     */       
/* 417 */       if (hamlet != null) {
/* 418 */         if (MLN.LogWorldGeneration >= 1) {
/* 419 */           MLN.major(this, "Trying to generate a hamlet: " + hamlet);
/*     */         }
/* 421 */         generateHamlet(world, hamlet, townHallEntity.getPos(), townHallEntity.getVillageNameWithoutQualifier(), random);
/*     */       }
/*     */     }
/*     */     
/* 425 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean generateVillageAtPoint(World world, Random random, int x, int y, int z, EntityPlayer generatingPlayer, boolean checkForUnloaded, boolean alwaysGenerate, int minDistance, VillageType villageType, String name, Point parentVillage)
/*     */   {
/* 451 */     if ((!Mill.loadingComplete) || ((!MLN.generateVillages) && (!MLN.generateLoneBuildings) && (!alwaysGenerate))) {
/* 452 */       return false;
/*     */     }
/*     */     
/* 455 */     if (world.field_72995_K) {
/* 456 */       return false;
/*     */     }
/*     */     
/* 459 */     MillWorld mw = Mill.getMillWorld(world);
/*     */     
/* 461 */     if (mw == null) {
/* 462 */       return false;
/*     */     }
/*     */     
/* 465 */     Point p = new Point(x, y, z);
/*     */     
/* 467 */     EntityPlayer closestPlayer = generatingPlayer;
/*     */     
/* 469 */     if (closestPlayer == null) {
/* 470 */       closestPlayer = world.func_72977_a(x, 64.0D, z, 200.0D);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 475 */       if (MLN.LogWorldGeneration >= 3) {
/* 476 */         MLN.debug(this, "Called for point: " + x + "/" + y + "/" + z);
/*     */       }
/*     */       
/* 479 */       MillCommonUtilities.random = random;
/*     */       
/*     */ 
/* 482 */       if (checkForUnloaded) {
/* 483 */         p = generateVillageAtPoint_checkForUnloaded(world, x, y, z, generatingPlayer, p);
/*     */         
/* 485 */         if (p == null) {
/* 486 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 490 */       long startTime = System.nanoTime();
/*     */       
/* 492 */       coordsTried.add(Integer.valueOf(x + (z << 16)));
/*     */       
/* 494 */       if ((MLN.generateVillages) || (alwaysGenerate)) {
/* 495 */         boolean canAttemptVillage = generateVillageAtPoint_canAttemptVillage(world, generatingPlayer, minDistance, mw, p, startTime);
/*     */         
/* 497 */         if (canAttemptVillage)
/*     */         {
/*     */           VillageType village;
/*     */           VillageType village;
/* 501 */           if (villageType == null) {
/* 502 */             village = generateVillageAtPoint_findVillageType(world, x, z, mw, closestPlayer);
/*     */           } else {
/* 504 */             village = villageType;
/*     */           }
/*     */           
/* 507 */           if (village != null)
/*     */           {
/*     */             boolean result;
/*     */             boolean result;
/* 511 */             if (village.customCentre == null) {
/* 512 */               result = generateVillage(p, world, village, generatingPlayer, closestPlayer, random, minDistance, name, false, parentVillage);
/*     */             } else {
/* 514 */               result = generateCustomVillage(p, world, village, generatingPlayer, random);
/*     */             }
/*     */             
/* 517 */             if (result) {
/* 518 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 524 */       if ((generatingPlayer != null) || (!MLN.generateLoneBuildings) || (villageType != null)) {
/* 525 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 530 */       boolean keyLoneBuildingsOnly = false;
/*     */       
/* 532 */       int minDistanceWithVillages = Math.min(minDistance, MLN.minDistanceBetweenVillagesAndLoneBuildings);
/* 533 */       int minDistanceWithLoneBuildings = Math.min(minDistance, MLN.minDistanceBetweenLoneBuildings);
/*     */       
/* 535 */       for (Point thp : mw.villagesList.pos) {
/* 536 */         if (p.distanceTo(thp) < minDistanceWithVillages / 2) {
/* 537 */           if (MLN.LogWorldGeneration >= 3) {
/* 538 */             MLN.debug(this, "Time taken for finding near villages: " + (System.nanoTime() - startTime));
/*     */           }
/* 540 */           return false; }
/* 541 */         if (p.distanceTo(thp) < minDistanceWithVillages) {
/* 542 */           keyLoneBuildingsOnly = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 547 */       for (Point thp : mw.loneBuildingsList.pos) {
/* 548 */         if (p.distanceTo(thp) < minDistanceWithLoneBuildings / 4) {
/* 549 */           if (MLN.LogWorldGeneration >= 3) {
/* 550 */             MLN.debug(this, "Time taken for finding near villages: " + (System.nanoTime() - startTime));
/*     */           }
/* 552 */           return false; }
/* 553 */         if (p.distanceTo(thp) < minDistanceWithLoneBuildings) {
/* 554 */           keyLoneBuildingsOnly = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 559 */       if (MLN.LogWorldGeneration >= 3) {
/* 560 */         MLN.debug(this, "Time taken for finding near villages (not found): " + (System.nanoTime() - startTime));
/*     */       }
/*     */       
/* 563 */       String biomeName = world.func_72959_q().func_76935_a(x, z).field_76791_y.toLowerCase();
/*     */       
/* 565 */       List<VillageType> acceptableLoneBuildingsType = new ArrayList();
/*     */       
/*     */ 
/* 568 */       HashMap<String, Integer> nbLoneBuildings = new HashMap();
/* 569 */       for (String type : mw.loneBuildingsList.types) {
/* 570 */         if (nbLoneBuildings.containsKey(type)) {
/* 571 */           nbLoneBuildings.put(type, Integer.valueOf(((Integer)nbLoneBuildings.get(type)).intValue() + 1));
/*     */         } else {
/* 573 */           nbLoneBuildings.put(type, Integer.valueOf(1));
/*     */         }
/*     */       }
/*     */       
/* 577 */       for (Culture c : Culture.ListCultures) {
/* 578 */         for (VillageType vt : c.listLoneBuildingTypes) {
/* 579 */           if (vt.isValidForGeneration(mw, closestPlayer, nbLoneBuildings, new Point(x, 60.0D, z), biomeName, keyLoneBuildingsOnly)) {
/* 580 */             acceptableLoneBuildingsType.add(vt);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 585 */       if (acceptableLoneBuildingsType.size() == 0) {
/* 586 */         return false;
/*     */       }
/*     */       
/* 589 */       VillageType loneBuilding = (VillageType)MillCommonUtilities.getWeightedChoice(acceptableLoneBuildingsType, closestPlayer);
/*     */       
/* 591 */       if (MLN.LogWorldGeneration >= 2) {
/* 592 */         MLN.minor(null, "Attempting to find lone building: " + loneBuilding);
/*     */       }
/*     */       
/* 595 */       if (loneBuilding == null) {
/* 596 */         return false;
/*     */       }
/*     */       
/* 599 */       if ((loneBuilding.isKeyLoneBuildingForGeneration(closestPlayer)) && 
/* 600 */         (MLN.LogWorldGeneration >= 1)) {
/* 601 */         MLN.major(null, "Attempting to generate key lone building: " + loneBuilding.key);
/*     */       }
/*     */       
/*     */ 
/* 605 */       boolean success = generateVillage(p, world, loneBuilding, generatingPlayer, closestPlayer, random, minDistance, name, true, null);
/*     */       
/* 607 */       if ((success) && (closestPlayer != null) && (loneBuilding.isKeyLoneBuildingForGeneration(closestPlayer)) && (loneBuilding.keyLoneBuildingGenerateTag != null))
/*     */       {
/* 609 */         UserProfile profile = mw.getProfile(closestPlayer.getDisplayName());
/* 610 */         profile.clearTag(loneBuilding.keyLoneBuildingGenerateTag);
/*     */       }
/*     */       
/* 613 */       return success;
/*     */     } catch (Exception e) {
/* 615 */       MLN.printException("Exception when generating village:", e);
/*     */     }
/*     */     
/* 618 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean generateVillageAtPoint_canAttemptVillage(World world, EntityPlayer generatingPlayer, int minDistance, MillWorld mw, Point p, long startTime)
/*     */   {
/* 625 */     boolean canAttemptVillage = true;
/*     */     
/* 627 */     int minDistanceVillages = Math.min(minDistance, MLN.minDistanceBetweenVillages);
/* 628 */     int minDistanceLoneBuildings = Math.min(minDistance, MLN.minDistanceBetweenVillagesAndLoneBuildings);
/*     */     
/* 630 */     if (generatingPlayer == null)
/*     */     {
/* 632 */       if (p.horizontalDistanceTo(world.func_72861_E()) < MLN.spawnProtectionRadius) {
/* 633 */         canAttemptVillage = false;
/*     */       }
/*     */       
/* 636 */       for (Point thp : mw.villagesList.pos) {
/* 637 */         if (p.distanceTo(thp) < minDistanceVillages) {
/* 638 */           if (MLN.LogWorldGeneration >= 3) {
/* 639 */             MLN.debug(this, "Time taken for finding near villages: " + (System.nanoTime() - startTime));
/*     */           }
/* 641 */           canAttemptVillage = false;
/*     */         }
/*     */       }
/*     */       
/* 645 */       for (Point thp : mw.loneBuildingsList.pos) {
/* 646 */         if (p.distanceTo(thp) < minDistanceLoneBuildings) {
/* 647 */           if (MLN.LogWorldGeneration >= 3) {
/* 648 */             MLN.debug(this, "Time taken for finding near lone buildings: " + (System.nanoTime() - startTime));
/*     */           }
/* 650 */           canAttemptVillage = false;
/*     */         }
/*     */       }
/*     */     }
/* 654 */     if (MLN.LogWorldGeneration >= 3) {
/* 655 */       MLN.debug(this, "Time taken for finding near villages (not found): " + (System.nanoTime() - startTime));
/*     */     }
/* 657 */     return canAttemptVillage;
/*     */   }
/*     */   
/*     */   private Point generateVillageAtPoint_checkForUnloaded(World world, int x, int y, int z, EntityPlayer generatingPlayer, Point p)
/*     */   {
/* 662 */     boolean areaLoaded = false;
/*     */     
/* 664 */     if (!world.func_72904_c(x - 80, y, z - 80, x + 80, y, z + 80))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 670 */       for (int i = -6; (i < 7) && (!areaLoaded); i++) {
/* 671 */         for (int j = -6; (j < 7) && (!areaLoaded); j++) {
/* 672 */           int tx = x + i * 16;
/* 673 */           int tz = z + j * 16;
/* 674 */           if ((!coordsTried.contains(Integer.valueOf(tx + (tz << 16)))) && 
/* 675 */             (world.func_72904_c(tx - 80, y, tz - 80, tx + 80, y, tz + 80))) {
/* 676 */             areaLoaded = true;
/* 677 */             p = new Point((tx >> 4) * 16 + 8, 0.0D, (tz >> 4) * 16 + 8);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     } else {
/* 683 */       areaLoaded = true;
/*     */     }
/*     */     
/* 686 */     if (!areaLoaded) {
/* 687 */       if (generatingPlayer != null) {
/* 688 */         ServerSender.sendTranslatedSentence(generatingPlayer, '6', "ui.worldnotgenerated", new String[0]);
/*     */       }
/* 690 */       return null;
/*     */     }
/*     */     
/* 693 */     if ((p.horizontalDistanceTo(world.func_72861_E()) < MLN.spawnProtectionRadius) && (Mill.proxy.isTrueServer())) {
/* 694 */       if (generatingPlayer != null) {
/* 695 */         ServerSender.sendTranslatedSentence(generatingPlayer, '6', "ui.tooclosetospawn", new String[0]);
/*     */       }
/* 697 */       return null;
/*     */     }
/* 699 */     return p;
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
/*     */   private VillageType generateVillageAtPoint_findVillageType(World world, int x, int z, MillWorld mw, EntityPlayer closestPlayer)
/*     */   {
/* 712 */     String biomeName = world.func_72959_q().func_76935_a(x, z).field_76791_y.toLowerCase();
/*     */     
/* 714 */     List<VillageType> acceptableVillageType = new ArrayList();
/*     */     
/* 716 */     HashMap<String, Integer> nbVillages = new HashMap();
/* 717 */     for (String type : mw.villagesList.types) {
/* 718 */       if (nbVillages.containsKey(type)) {
/* 719 */         nbVillages.put(type, Integer.valueOf(((Integer)nbVillages.get(type)).intValue() + 1));
/*     */       } else {
/* 721 */         nbVillages.put(type, Integer.valueOf(1));
/*     */       }
/*     */     }
/*     */     
/* 725 */     for (Culture c : Culture.ListCultures) {
/* 726 */       for (VillageType vt : c.listVillageTypes) {
/* 727 */         if (vt.isValidForGeneration(Mill.getMillWorld(world), closestPlayer, nbVillages, new Point(x, 60.0D, z), biomeName, false))
/* 728 */           acceptableVillageType.add(vt);
/*     */       }
/*     */     }
/*     */     VillageType village;
/*     */     VillageType village;
/* 733 */     if (acceptableVillageType.size() != 0) {
/* 734 */       village = (VillageType)MillCommonUtilities.getWeightedChoice(acceptableVillageType, closestPlayer);
/*     */     } else {
/* 736 */       village = null;
/*     */     }
/* 738 */     return village;
/*     */   }
/*     */   
/*     */   private boolean isAppropriateArea(MillWorldInfo winfo, Point centre, int radius)
/*     */   {
/* 743 */     int nbtiles = 0;int usabletiles = 0;
/*     */     
/* 745 */     for (int i = 0; i < winfo.length; i++) {
/* 746 */       for (int j = 0; j < winfo.width; j++) {
/* 747 */         nbtiles++;
/* 748 */         if (winfo.canBuild[i][j] != 0) {
/* 749 */           usabletiles++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 754 */     return usabletiles * 1.0D / nbtiles > 0.7D;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 759 */     return getClass().getSimpleName() + "@" + hashCode();
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\WorldGenVillage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package org.millenaire.common.building;
/*      */ 
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutput;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.monster.EntityCreeper;
/*      */ import net.minecraft.entity.monster.EntityEnderman;
/*      */ import net.minecraft.entity.monster.EntityMob;
/*      */ import net.minecraft.entity.passive.EntityCow;
/*      */ import net.minecraft.entity.passive.EntityPig;
/*      */ import net.minecraft.entity.passive.EntitySheep;
/*      */ import net.minecraft.entity.passive.EntitySquid;
/*      */ import net.minecraft.entity.passive.IAnimals;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.nbt.CompressedStreamTools;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.tileentity.TileEntityChest;
/*      */ import net.minecraft.tileentity.TileEntityDispenser;
/*      */ import net.minecraft.tileentity.TileEntityFurnace;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.gen.feature.WorldGenForest;
/*      */ import net.minecraft.world.gen.feature.WorldGenTaiga2;
/*      */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*      */ import net.minecraft.world.gen.feature.WorldGenerator;
/*      */ import org.millenaire.common.Culture;
/*      */ import org.millenaire.common.InvItem;
/*      */ import org.millenaire.common.MLN;
/*      */ import org.millenaire.common.MLN.MillenaireException;
/*      */ import org.millenaire.common.MillMapInfo;
/*      */ import org.millenaire.common.MillVillager;
/*      */ import org.millenaire.common.MillWorld;
/*      */ import org.millenaire.common.MillWorldInfo;
/*      */ import org.millenaire.common.Point;
/*      */ import org.millenaire.common.PujaSacrifice;
/*      */ import org.millenaire.common.TileEntityMillChest;
/*      */ import org.millenaire.common.TileEntityPanel;
/*      */ import org.millenaire.common.UserProfile;
/*      */ import org.millenaire.common.VillageType;
/*      */ import org.millenaire.common.VillagerRecord;
/*      */ import org.millenaire.common.VillagerType;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*      */ import org.millenaire.common.forge.BuildingChunkLoader;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.forge.MillAchievements;
/*      */ import org.millenaire.common.goal.Goal;
/*      */ import org.millenaire.common.goal.GoalBeSeller;
/*      */ import org.millenaire.common.item.Goods;
/*      */ import org.millenaire.common.network.ServerSender;
/*      */ import org.millenaire.common.network.StreamReadWrite;
/*      */ import org.millenaire.common.pathing.AStarPathing;
/*      */ import org.millenaire.common.pathing.AStarPathing.PathingWorker;
/*      */ import org.millenaire.common.pathing.AStarPathing.Point2D;
/*      */ import org.millenaire.common.pathing.PathingBinary;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarNode;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarPathPlanner;
/*      */ import org.millenaire.common.pathing.atomicstryker.IAStarPathedEntity;
/*      */ 
/*      */ public class Building
/*      */ {
/*      */   public static enum EnumSignType
/*      */   {
/*   96 */     DEFAULT,  HOUSE,  TOWNHALL,  INN,  MARKET,  ARCHIVES;
/*      */     
/*      */     private EnumSignType() {}
/*      */   }
/*      */   
/*      */   private class PathCreator implements IAStarPathedEntity {
/*      */     final Building.PathCreatorInfo info;
/*      */     final InvItem pathConstructionGood;
/*      */     final int pathWidth;
/*      */     final Building destination;
/*      */     
/*  107 */     PathCreator(Building.PathCreatorInfo info, InvItem pathConstructionGood, int pathWidth, Building destination) { this.pathConstructionGood = pathConstructionGood;
/*  108 */       this.pathWidth = pathWidth;
/*  109 */       this.destination = destination;
/*  110 */       this.info = info;
/*      */     }
/*      */     
/*      */     private void checkForRebuild() {
/*  114 */       if (this.info.nbPathsReceived == this.info.nbPathsExpected)
/*      */       {
/*      */ 
/*  117 */         Collections.reverse(this.info.pathsReceived);
/*      */         
/*  119 */         Building.this.pathsToBuild = this.info.pathsReceived;
/*  120 */         Building.this.pathsToBuildIndex = 0;
/*  121 */         Building.this.pathsToBuildPathIndex = 0;
/*      */         
/*  123 */         Building.this.calculatePathsToClear();
/*      */         
/*  125 */         Building.this.pathsChanged = true;
/*  126 */         this.info.pathsReceived = null;
/*      */         
/*  128 */         this.info.creationComplete = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void onFoundPath(List<AStarNode> result)
/*      */     {
/*  135 */       if (this.info.creationComplete) {
/*  136 */         MLN.error(Building.this, "onFoundPath triggered on completed info object.");
/*  137 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  142 */       this.info.pathsReceived.add(MillCommonUtilities.buildPath(Building.this, result, this.pathConstructionGood.block, this.pathConstructionGood.meta, this.pathWidth));
/*  143 */       Building.PathCreatorInfo.access$008(this.info);
/*      */       
/*  145 */       checkForRebuild();
/*      */     }
/*      */     
/*      */ 
/*      */     public void onNoPathAvailable()
/*      */     {
/*  151 */       if (this.info.creationComplete) {
/*  152 */         MLN.error(Building.this, "onNoPathAvailable triggered on completed info object.");
/*  153 */         return;
/*      */       }
/*      */       
/*  156 */       Building.PathCreatorInfo.access$008(this.info);
/*  157 */       if (MLN.LogVillagePaths >= 2) {
/*  158 */         MLN.minor(Building.this, "Path calculation failed. Target: " + this.destination);
/*      */       }
/*      */       
/*  161 */       checkForRebuild();
/*      */     }
/*      */   }
/*      */   
/*      */   private class PathCreatorInfo
/*      */   {
/*      */     private final int nbPathsExpected;
/*  168 */     private int nbPathsReceived = 0;
/*  169 */     private List<List<BuildingBlock>> pathsReceived = new ArrayList();
/*      */     
/*  171 */     private boolean creationComplete = false;
/*      */     
/*      */     PathCreatorInfo(int nbPathsExpected) {
/*  174 */       this.nbPathsExpected = nbPathsExpected;
/*      */     }
/*      */   }
/*      */   
/*      */   public class PathingThread extends Thread
/*      */   {
/*      */     MillWorldInfo winfo;
/*      */     
/*      */     public PathingThread(MillWorldInfo wi)
/*      */     {
/*  184 */       this.winfo = wi;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*  189 */       AStarPathing temp = new AStarPathing();
/*  190 */       if (MLN.LogPathing >= 1) {
/*  191 */         MLN.major(this, "Start");
/*      */       }
/*  193 */       long tm = System.currentTimeMillis();
/*      */       try {
/*  195 */         if (temp.createConnectionsTable(this.winfo, Building.this.resManager.getSleepingPos())) {
/*  196 */           Building.this.pathing = temp;
/*  197 */           Building.this.lastPathingUpdate = System.currentTimeMillis();
/*      */         } else {
/*  199 */           Building.this.lastPathingUpdate = System.currentTimeMillis();
/*  200 */           Building.this.pathing = null;
/*      */         }
/*      */       } catch (MLN.MillenaireException e) {
/*  203 */         MLN.printException(e);
/*      */       }
/*  205 */       if (MLN.LogPathing >= 1) {
/*  206 */         MLN.major(this, "Done: " + (System.currentTimeMillis() - tm) * 1.0D / 1000.0D);
/*      */       }
/*  208 */       Building.this.rebuildingPathing = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PerformanceMonitor
/*      */   {
/*  214 */     public HashMap<String, Double> goalTime = new HashMap();
/*      */     public int nbPathing;
/*      */     public int nbCached;
/*      */     public int nbPartlyCached;
/*      */     public int nbReused;
/*      */     
/*      */     PerformanceMonitor(Building townHall)
/*      */     {
/*  222 */       this.townHall = townHall;
/*  223 */       reset(); }
/*      */     
/*      */     public double totalTime;
/*      */     public double pathingTime;
/*      */     
/*  228 */     public void addToGoal(String key, double time) { this.totalTime += time;
/*      */       
/*  230 */       if (this.goalTime.containsKey(key)) {
/*  231 */         time = ((Double)this.goalTime.get(key)).doubleValue() + time;
/*      */       }
/*  233 */       this.goalTime.put(key, Double.valueOf(time));
/*      */     }
/*      */     
/*      */     public double noPathFoundTime;
/*      */     Building townHall;
/*  238 */     public String getStats() { if (this.townHall.pathing == null) {
/*  239 */         return null;
/*      */       }
/*      */       
/*  242 */       String s = Math.round(this.totalTime) + "/" + Math.round(this.pathingTime) + "/" + Math.round(this.noPathFoundTime) + " - " + this.nbPathing + "(" + Math.round(this.nbReused * 10000 / (this.nbPathing + 1)) * 1.0F / 100.0F + "% / " + Math.round(this.nbCached * 10000 / (this.nbPathing + 1)) * 1.0F / 100.0F + "% / " + Math.round(this.nbPartlyCached * 10000 / (this.nbPathing + 1)) * 1.0F / 100.0F + "%) Nb cached: " + this.townHall.pathing.cache.size() + " ";
/*      */       
/*      */ 
/*  245 */       for (String goalKey : this.goalTime.keySet()) {
/*  246 */         if (Goal.goals.containsKey(goalKey)) {
/*  247 */           s = s + ((Goal)Goal.goals.get(goalKey)).gameName(null) + ": " + this.goalTime.get(goalKey) + " ";
/*      */         } else {
/*  249 */           s = s + "No goal: " + this.goalTime.get(goalKey) + " ";
/*      */         }
/*      */       }
/*      */       
/*  253 */       for (int i = 0; i < this.townHall.pathing.firstDemand.size(); i++) {}
/*      */       
/*      */ 
/*      */ 
/*  257 */       return s;
/*      */     }
/*      */     
/*      */     public void reset() {
/*  261 */       this.goalTime = new HashMap();
/*  262 */       this.totalTime = 0.0D;
/*  263 */       this.pathingTime = 0.0D;
/*  264 */       this.nbPathing = 0;
/*  265 */       this.nbCached = 0;
/*  266 */       this.noPathFoundTime = 0.0D;
/*  267 */       this.nbPartlyCached = 0;
/*  268 */       this.nbReused = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   private class SaveWorker extends Thread
/*      */   {
/*      */     private final String reason;
/*      */     
/*      */     private SaveWorker(String reason) {
/*  277 */       this.reason = reason;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*  282 */       if (!Building.this.isTownhall) {
/*  283 */         return;
/*      */       }
/*      */       
/*  286 */       long startTime = System.currentTimeMillis();
/*      */       
/*  288 */       NBTTagCompound mainTag = new NBTTagCompound();
/*      */       
/*      */ 
/*      */ 
/*  292 */       NBTTagList nbttaglist = new NBTTagList();
/*  293 */       for (int i = 0; i < Building.this.buildings.size(); i++) {
/*  294 */         Point p = (Point)Building.this.buildings.get(i);
/*  295 */         if (p != null) {
/*  296 */           Building b = Building.this.mw.getBuilding(p);
/*  297 */           if (b != null) {
/*  298 */             NBTTagCompound buildingTag = new NBTTagCompound();
/*  299 */             b.writeToNBT(buildingTag);
/*  300 */             nbttaglist.func_74742_a(buildingTag);
/*      */           }
/*      */         }
/*      */       }
/*  304 */       mainTag.func_74782_a("buildings", nbttaglist);
/*      */       
/*  306 */       File millenaireDir = Building.this.mw.millenaireDir;
/*      */       
/*  308 */       if (!millenaireDir.exists()) {
/*  309 */         millenaireDir.mkdir();
/*      */       }
/*      */       
/*  312 */       File buildingsDir = new File(millenaireDir, "buildings");
/*      */       
/*  314 */       if (!buildingsDir.exists()) {
/*  315 */         buildingsDir.mkdir();
/*      */       }
/*      */       
/*  318 */       File file1 = new File(buildingsDir, Building.this.getPos().getPathString() + "_temp.gz");
/*      */       try {
/*  320 */         FileOutputStream fileoutputstream = new FileOutputStream(file1);
/*  321 */         CompressedStreamTools.func_74799_a(mainTag, fileoutputstream);
/*      */         
/*  323 */         file1.renameTo(new File(buildingsDir, Building.this.getPos().getPathString() + ".gz"));
/*      */       } catch (IOException e) {
/*  325 */         MLN.printException(e);
/*      */       }
/*      */       
/*  328 */       if (MLN.LogHybernation >= 1) {
/*  329 */         MLN.major(Building.this, "Saved " + Building.this.buildings.size() + " buildings in " + (System.currentTimeMillis() - startTime) + " ms due to " + this.reason + " (" + Building.this.saveReason + ").");
/*      */       }
/*      */       
/*  332 */       Building.this.lastSaved = Building.this.worldObj.func_72820_D();
/*  333 */       Building.this.saveNeeded = false;
/*  334 */       Building.this.saveReason = null;
/*      */       
/*  336 */       Building.this.saveWorker = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  341 */   public static final AStarConfig PATH_BUILDER_JPS_CONFIG = new AStarConfig(true, false, false, false);
/*      */   
/*      */   public static final int INVADER_SPAWNING_DELAY = 500;
/*      */   
/*      */   public static final int RELATION_FAIR = 10;
/*      */   
/*      */   public static final int RELATION_DECENT = 30;
/*      */   
/*      */   public static final int RELATION_GOOD = 50;
/*      */   
/*      */   public static final int RELATION_VERYGOOD = 70;
/*      */   
/*      */   public static final int RELATION_EXCELLENT = 90;
/*      */   public static final int RELATION_CHILLY = -10;
/*      */   public static final int RELATION_BAD = -30;
/*      */   public static final int RELATION_VERYBAD = -50;
/*      */   public static final int RELATION_ATROCIOUS = -70;
/*      */   public static final int RELATION_OPENCONFLICT = -90;
/*      */   public static final int RELATION_MAX = 100;
/*      */   public static final int RELATION_MIN = -100;
/*      */   public static final String blArmoury = "armoury";
/*      */   public static final String blBakery = "bakery";
/*      */   public static final String blCattleFarm = "cattlefarm";
/*      */   public static final String blChurch = "church";
/*      */   public static final String blFarm = "farm";
/*      */   public static final String blForge = "forge";
/*      */   public static final String blLumbermanhut = "lumbermanhut";
/*      */   public static final String blMarket = "market";
/*      */   public static final String blPigFarm = "pigfarm";
/*      */   public static final String blPresbytery = "presbytery";
/*      */   public static final String blSheepChickenFarm = "sheepchickenfarm";
/*      */   public static final String blTavern = "tavern";
/*      */   public static final String blTownhall = "townHall";
/*      */   public static final String blWatchtower = "watchtower";
/*      */   private static final int LOCATION_SEARCH_DELAY = 80000;
/*      */   public static final int MIN_REPUTATION_FOR_TRADE = -1024;
/*      */   public static final int MAX_REPUTATION = 32768;
/*      */   private static final int PATHING_REBUILD_DELAY = 60000;
/*      */   public static final String tagAlembic = "Alembic";
/*      */   public static final String tagArchives = "archives";
/*      */   public static final String tagBakery = "bakery";
/*      */   public static final String tagCattle = "cattle";
/*      */   public static final String tagCider = "cider";
/*      */   public static final String tagDrinking = "Drinking";
/*      */   public static final String tagFishingSpot = "fishingspot";
/*      */   public static final String tagGrove = "grove";
/*      */   public static final String tagInn = "inn";
/*      */   public static final String tagKiln = "brickkiln";
/*      */   public static final String tagMaizeFarm = "maizefarm";
/*      */   public static final String tagMarket = "market";
/*      */   public static final String tagOven = "Oven";
/*      */   public static final String tagPaddy = "paddy";
/*      */   public static final String tagPatrol = "Patrol";
/*      */   public static final String tagPigs = "pigs";
/*      */   public static final String tagChicken = "chicken";
/*      */   public static final String tagPraying = "Praying";
/*      */   public static final String tagSheeps = "sheeps";
/*      */   public static final String tagSpiceGarden = "spicegarden";
/*      */   public static final String tagSugarPlantation = "sugarplantation";
/*      */   public static final String tagHoF = "hof";
/*      */   public static final String tagPujas = "pujas";
/*      */   public static final String tagSacrifices = "sacrifices";
/*      */   public static final String tagVineyard = "vineyard";
/*      */   public static final String tagSilkwormFarm = "silkwormfarm";
/*      */   public static final String tagDespawnAllMobs = "despawnallmobs";
/*      */   public static final String tagLeasure = "leasure";
/*      */   public static final String tagNoPaths = "nopaths";
/*      */   public static final String versionCompatibility = "1.0";
/*      */   
/*      */   public static void readBuildingPacket(MillWorld mw, ByteBufInputStream ds)
/*      */   {
/*  412 */     Point pos = null;
/*      */     try {
/*  414 */       pos = StreamReadWrite.readNullablePoint(ds);
/*      */     } catch (IOException e) {
/*  416 */       MLN.printException(e);
/*  417 */       return;
/*      */     }
/*      */     
/*  420 */     Building building = mw.getBuilding(pos);
/*      */     
/*  422 */     boolean newbuilding = false;
/*      */     
/*  424 */     if (building == null) {
/*  425 */       building = new Building(mw);
/*  426 */       newbuilding = true;
/*      */     }
/*      */     
/*  429 */     building.pos = pos;
/*      */     try
/*      */     {
/*  432 */       building.isTownhall = ds.readBoolean();
/*  433 */       building.chestLocked = ds.readBoolean();
/*  434 */       building.controlledBy = StreamReadWrite.readNullableString(ds);
/*  435 */       building.controlledByName = StreamReadWrite.readNullableString(ds);
/*  436 */       building.townHallPos = StreamReadWrite.readNullablePoint(ds);
/*  437 */       building.culture = Culture.getCultureByName(StreamReadWrite.readNullableString(ds));
/*  438 */       String vtype = StreamReadWrite.readNullableString(ds);
/*  439 */       if ((building.culture != null) && (building.culture.getVillageType(vtype) != null)) {
/*  440 */         building.villageType = building.culture.getVillageType(vtype);
/*  441 */       } else if ((building.culture != null) && (building.culture.getLoneBuildingType(vtype) != null)) {
/*  442 */         building.villageType = building.culture.getLoneBuildingType(vtype);
/*      */       }
/*      */       
/*  445 */       building.location = StreamReadWrite.readNullableBuildingLocation(ds);
/*      */       
/*  447 */       building.buildingGoal = StreamReadWrite.readNullableString(ds);
/*  448 */       building.buildingGoalIssue = StreamReadWrite.readNullableString(ds);
/*  449 */       building.buildingGoalLevel = ds.readInt();
/*  450 */       building.buildingGoalVariation = ds.readInt();
/*  451 */       building.buildingGoalLocation = StreamReadWrite.readNullableBuildingLocation(ds);
/*  452 */       building.buildingLocationIP = StreamReadWrite.readNullableBuildingLocation(ds);
/*  453 */       building.buildingProjects = StreamReadWrite.readProjectListList(ds, building.culture);
/*      */       
/*  455 */       building.buildings = StreamReadWrite.readPointList(ds);
/*      */       
/*  457 */       building.buildingsBought = StreamReadWrite.readStringList(ds);
/*      */       
/*  459 */       building.relations = StreamReadWrite.readPointIntegerMap(ds);
/*  460 */       building.raidsPerformed = StreamReadWrite.readStringList(ds);
/*  461 */       building.raidsSuffered = StreamReadWrite.readStringList(ds);
/*      */       
/*  463 */       building.vrecords = StreamReadWrite.readVillagerRecordList(mw, ds);
/*      */       
/*  465 */       building.pujas = StreamReadWrite.readOrUpdateNullablePuja(ds, building, building.pujas);
/*      */       
/*  467 */       building.visitorsList = StreamReadWrite.readStringList(ds);
/*  468 */       building.imported = StreamReadWrite.readInventory(ds);
/*  469 */       building.exported = StreamReadWrite.readInventory(ds);
/*      */       
/*  471 */       building.name = StreamReadWrite.readNullableString(ds);
/*  472 */       building.qualifier = StreamReadWrite.readNullableString(ds);
/*      */       
/*  474 */       building.raidTarget = StreamReadWrite.readNullablePoint(ds);
/*  475 */       building.raidPlanningStart = ds.readLong();
/*  476 */       building.raidStart = ds.readLong();
/*      */       
/*  478 */       building.resManager.readDataStream(ds);
/*      */     }
/*      */     catch (IOException e) {
/*  481 */       MLN.printException(e);
/*      */     }
/*      */     
/*  484 */     if (newbuilding) {
/*  485 */       mw.addBuilding(building, pos);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void readShopPacket(MillWorld mw, ByteBufInputStream ds)
/*      */   {
/*  491 */     Point pos = null;
/*      */     try {
/*  493 */       pos = StreamReadWrite.readNullablePoint(ds);
/*      */     } catch (IOException e) {
/*  495 */       MLN.printException(e);
/*  496 */       return;
/*      */     }
/*      */     
/*  499 */     Building building = mw.getBuilding(pos);
/*      */     
/*  501 */     if (building == null) {
/*  502 */       MLN.error(null, "Received shop packet for null building at: " + pos);
/*  503 */       return;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  508 */       int nbSells = ds.readInt();
/*      */       
/*  510 */       if (nbSells > 0) {
/*  511 */         LinkedHashMap<Goods, Integer> shopSellsPlayer = new LinkedHashMap();
/*  512 */         for (int i = 0; i < nbSells; i++) {
/*  513 */           Goods g = StreamReadWrite.readNullableGoods(ds);
/*  514 */           shopSellsPlayer.put(g, Integer.valueOf(ds.readInt()));
/*      */         }
/*  516 */         building.shopSells.put(Mill.proxy.getSinglePlayerName(), shopSellsPlayer);
/*      */       }
/*      */       
/*  519 */       int nbBuys = ds.readInt();
/*      */       
/*  521 */       if (nbBuys > 0) {
/*  522 */         LinkedHashMap<Goods, Integer> shopBuysPlayer = new LinkedHashMap();
/*  523 */         for (int i = 0; i < nbBuys; i++) {
/*  524 */           Goods g = StreamReadWrite.readNullableGoods(ds);
/*  525 */           shopBuysPlayer.put(g, Integer.valueOf(ds.readInt()));
/*      */         }
/*  527 */         building.shopBuys.put(Mill.proxy.getSinglePlayerName(), shopBuysPlayer);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  531 */       MLN.printException(e);
/*      */     } catch (MLN.MillenaireException e) {
/*  533 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*  537 */   private BuildingBlock[] bblocks = null;
/*  538 */   public int bblocksPos = 0;
/*  539 */   private boolean bblocksChanged = false; private boolean pathsChanged = false;
/*      */   
/*  541 */   private PathingBinary binaryPathing = null;
/*  542 */   public MillVillager builder = null;
/*      */   public String buildingGoal;
/*  544 */   public String buildingGoalIssue; public int buildingGoalLevel = 0;
/*  545 */   public BuildingLocation buildingGoalLocation = null;
/*  546 */   public int buildingGoalVariation = 0;
/*  547 */   public BuildingLocation buildingLocationIP = null;
/*  548 */   public Map<BuildingProject.EnumProjects, List<BuildingProject>> buildingProjects = new HashMap();
/*  549 */   public List<Point> buildings = new ArrayList();
/*      */   
/*  551 */   public List<String> buildingsBought = new ArrayList();
/*      */   public Culture culture;
/*  553 */   private boolean declaredPos = false;
/*      */   
/*  555 */   public HashMap<InvItem, Integer> exported = new HashMap();
/*  556 */   public HashMap<InvItem, Integer> imported = new HashMap();
/*  557 */   public boolean isActive = false; public boolean isAreaLoaded = false;
/*  558 */   public boolean chestLocked; public boolean isTownhall = false; public boolean isInn = false; public boolean isMarket = false;
/*  559 */   private long lastFailedOtherLocationSearch = 0L;
/*  560 */   private long lastFailedProjectLocationSearch = 0L;
/*      */   
/*      */   public long lastPathingUpdate;
/*      */   public long lastPing;
/*  564 */   private long lastSaved = 0L;
/*  565 */   public long lastVillagerRecordsRepair = 0L;
/*  566 */   public long lastWoodLocations = 0L; public long lastPlantingLocations = 0L; public long lastSignUpdate = 0L;
/*      */   public BuildingLocation location;
/*  568 */   public VillagerRecord merchantRecord = null;
/*  569 */   public boolean metadataMismatch = false;
/*      */   public PerformanceMonitor monitor;
/*  571 */   private String name = null; private String qualifier = "";
/*  572 */   public int nbNightsMerchant = 0;
/*  573 */   private HashMap<Goods, Integer> neededGoodsCached = new HashMap();
/*  574 */   private long neededGoodsLastGenerated = 0L;
/*  575 */   public boolean thNightActionPerformed = false;
/*  576 */   public boolean noProjectsLeft = false;
/*  577 */   public AStarPathing pathing = null;
/*  578 */   public EntityPlayer closestPlayer = null;
/*  579 */   private Point pos = null;
/*  580 */   private boolean rebuildingPathing = false;
/*  581 */   private boolean saveNeeded = false;
/*      */   
/*  583 */   private String saveReason = null;
/*  584 */   public MillVillager seller = null;
/*      */   
/*  586 */   public Point sellingPlace = null;
/*  587 */   private Point townHallPos = null;
/*      */   
/*  589 */   public List<MillVillager> villagers = new ArrayList();
/*      */   
/*  591 */   public List<String> visitorsList = new ArrayList();
/*  592 */   public List<VillagerRecord> vrecords = new ArrayList();
/*      */   
/*  594 */   public VillageType villageType = null;
/*  595 */   private HashMap<Point, Integer> relations = new HashMap();
/*  596 */   public Point parentVillage = null;
/*  597 */   public MillWorldInfo winfo = new MillWorldInfo();
/*      */   
/*      */ 
/*      */ 
/*  601 */   public MillMapInfo mapInfo = null;
/*      */   
/*      */   public MillWorld mw;
/*      */   
/*      */   public World worldObj;
/*      */   private boolean nightBackgroundActionPerformed;
/*      */   private boolean updateRaidPerformed;
/*  608 */   public List<String> raidsPerformed = new ArrayList();
/*  609 */   public List<String> raidsSuffered = new ArrayList();
/*      */   
/*      */   public Point raidTarget;
/*  612 */   public long raidStart = 0L;
/*      */   
/*      */   public long raidPlanningStart;
/*  615 */   public boolean underAttack = false;
/*      */   
/*      */   private int nbAnimalsRespawned;
/*  618 */   public PujaSacrifice pujas = null;
/*  619 */   public String controlledBy = null;
/*      */   
/*  621 */   public String controlledByName = null;
/*      */   
/*  623 */   private SaveWorker saveWorker = null;
/*      */   
/*  625 */   private long lastGoodsRefresh = 0L;
/*      */   
/*      */   private boolean refreshGoodsNightActionPerformed;
/*      */   
/*  629 */   private BuildingChunkLoader chunkLoader = null;
/*      */   
/*  631 */   public List<List<BuildingBlock>> pathsToBuild = null;
/*      */   
/*  633 */   public int pathsToBuildIndex = 0; public int pathsToBuildPathIndex = 0;
/*      */   
/*  635 */   public List<Point> oldPathPointsToClear = null;
/*      */   
/*  637 */   public int oldPathPointsToClearIndex = 0;
/*  638 */   private boolean autobuildPaths = false;
/*      */   
/*  640 */   private final HashMap<String, LinkedHashMap<Goods, Integer>> shopBuys = new HashMap();
/*      */   
/*  642 */   private final HashMap<String, LinkedHashMap<Goods, Integer>> shopSells = new HashMap();
/*      */   
/*  644 */   private final BuildingResManager resManager = new BuildingResManager(this);
/*      */   
/*  646 */   public List<Point> subBuildings = new ArrayList();
/*      */   
/*  648 */   public EnumSignType signType = EnumSignType.DEFAULT;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Building(MillWorld mw)
/*      */   {
/*  656 */     this.mw = mw;
/*  657 */     this.worldObj = mw.world;
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
/*      */   public Building(MillWorld mw, Culture c, VillageType villageType, BuildingLocation l, boolean townHall, boolean villageCreation, Point p, Point townHallPos)
/*      */   {
/*  675 */     this.pos = p;
/*  676 */     this.mw = mw;
/*  677 */     this.worldObj = mw.world;
/*  678 */     this.location = l;
/*  679 */     this.culture = c;
/*  680 */     this.villageType = villageType;
/*      */     
/*  682 */     if (this.worldObj == null) {
/*  683 */       MLN.MillenaireException e = new MLN.MillenaireException("Null worldObj!");
/*  684 */       MLN.printException(e);
/*      */     }
/*  686 */     if (this.pos == null) {
/*  687 */       MLN.MillenaireException e = new MLN.MillenaireException("Null pos!");
/*  688 */       MLN.printException(e);
/*      */     }
/*  690 */     if (this.location == null) {
/*  691 */       MLN.MillenaireException e = new MLN.MillenaireException("Null location!");
/*  692 */       MLN.printException(e);
/*      */     }
/*  694 */     if (this.culture == null) {
/*  695 */       MLN.MillenaireException e = new MLN.MillenaireException("Null culture!");
/*  696 */       MLN.printException(e);
/*      */     }
/*      */     
/*  699 */     mw.addBuilding(this, p);
/*      */     
/*  701 */     if (MLN.DEV) {
/*  702 */       this.monitor = new PerformanceMonitor(this);
/*      */     }
/*      */     
/*  705 */     this.isTownhall = townHall;
/*      */     
/*  707 */     this.pathing = null;
/*      */     
/*  709 */     if (this.isTownhall) {
/*  710 */       this.townHallPos = getPos();
/*      */     } else {
/*  712 */       this.townHallPos = townHallPos;
/*      */     }
/*      */     
/*  715 */     this.location = l;
/*  716 */     this.isTownhall = townHall;
/*      */     
/*  718 */     if ((l.tags.contains("inn")) && (!this.isTownhall)) {
/*  719 */       this.isInn = true;
/*      */     }
/*      */     
/*  722 */     if ((l.tags.contains("market")) && (!this.isTownhall)) {
/*  723 */       this.isMarket = true;
/*      */     }
/*      */     
/*  726 */     if (l.tags.contains("pujas")) {
/*  727 */       this.pujas = new PujaSacrifice(this, (short)0);
/*      */     }
/*      */     
/*  730 */     if (l.tags.contains("sacrifices")) {
/*  731 */       this.pujas = new PujaSacrifice(this, (short)1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Building(MillWorld mw, NBTTagCompound nbttagcompound)
/*      */   {
/*  743 */     this.mw = mw;
/*  744 */     this.worldObj = mw.world;
/*  745 */     readFromNBT(nbttagcompound);
/*      */     
/*  747 */     if (MLN.DEV) {
/*  748 */       this.monitor = new PerformanceMonitor(this);
/*      */     }
/*      */     
/*  751 */     if (this.pos == null) {
/*  752 */       MLN.MillenaireException e = new MLN.MillenaireException("Null pos!");
/*  753 */       MLN.printException(e);
/*      */     }
/*      */     
/*  756 */     mw.addBuilding(this, this.pos);
/*      */   }
/*      */   
/*      */   public void addAdult(MillVillager child) throws MLN.MillenaireException
/*      */   {
/*  761 */     String type = "";
/*      */     
/*  763 */     HashMap<String, Integer> villagerCount = new HashMap();
/*  764 */     HashMap<String, Integer> residentCount = new HashMap();
/*      */     
/*      */     List<String> residents;
/*      */     List<String> residents;
/*  768 */     if (child.gender == 1) {
/*  769 */       residents = this.location.maleResident;
/*      */     } else {
/*  771 */       residents = this.location.femaleResident;
/*      */     }
/*      */     
/*  774 */     for (String s : residents) {
/*  775 */       if (residentCount.containsKey(s)) {
/*  776 */         residentCount.put(s, Integer.valueOf(((Integer)residentCount.get(s)).intValue() + 1));
/*      */       } else {
/*  778 */         residentCount.put(s, Integer.valueOf(1));
/*      */       }
/*      */     }
/*      */     
/*  782 */     for (VillagerRecord vr : this.vrecords) {
/*  783 */       if (villagerCount.containsKey(vr.type)) {
/*  784 */         villagerCount.put(vr.type, Integer.valueOf(((Integer)villagerCount.get(vr.type)).intValue() + 1));
/*      */       } else {
/*  786 */         villagerCount.put(vr.type, Integer.valueOf(1));
/*      */       }
/*      */     }
/*      */     
/*  790 */     for (String s : residentCount.keySet()) {
/*  791 */       if (!villagerCount.containsKey(s)) {
/*  792 */         type = s;
/*  793 */       } else if (((Integer)villagerCount.get(s)).intValue() < ((Integer)residentCount.get(s)).intValue()) {
/*  794 */         type = s;
/*      */       }
/*      */     }
/*      */     
/*  798 */     child.getHouse().removeVillagerRecord(child.villager_id);
/*      */     
/*  800 */     if (MLN.LogWorldGeneration >= 1) {
/*  801 */       MLN.major(this, "Creating " + type + " with child " + child.getName() + "/" + child.villager_id);
/*      */     }
/*  803 */     MillVillager adult = MillVillager.createVillager(this.culture, type, child.gender, this.worldObj, child.getPos(), getPos(), this.townHallPos, false, child.firstName, child.familyName);
/*      */     
/*  805 */     if (adult == null) {
/*  806 */       MLN.error(this, "Couldn't create adult of type " + type + " from child " + child);
/*  807 */       return;
/*      */     }
/*      */     
/*  810 */     adult.villager_id = child.villager_id;
/*      */     
/*  812 */     VillagerRecord adultRecord = adult.getRecord();
/*  813 */     if (adultRecord == null) {
/*  814 */       adultRecord = new VillagerRecord(this.mw, adult);
/*      */     }
/*  816 */     adultRecord.updateRecord(adult);
/*      */     
/*  818 */     addOrReplaceVillager(adult);
/*  819 */     getTownHall().addOrReplaceVillager(adult);
/*  820 */     addOrReplaceRecord(adultRecord);
/*  821 */     getTownHall().addOrReplaceRecord(adultRecord);
/*      */     
/*  823 */     for (VillagerRecord vr : this.vrecords) {
/*  824 */       if (vr.gender != adult.gender) {
/*  825 */         if (adult.gender == 2) {
/*  826 */           adultRecord.maidenName = adultRecord.familyName;
/*  827 */           adultRecord.familyName = vr.familyName;
/*  828 */           adult.familyName = vr.familyName;
/*      */         }
/*      */         
/*  831 */         if (vr.gender == 2)
/*      */         {
/*  833 */           vr.maidenName = vr.familyName;
/*  834 */           vr.familyName = adult.familyName;
/*  835 */           MillVillager spouse = getVillagerById(vr.id);
/*  836 */           if (spouse != null) {
/*  837 */             spouse.familyName = vr.familyName;
/*      */           }
/*      */         }
/*  840 */         adultRecord.spousesName = vr.getName();
/*  841 */         vr.spousesName = adult.getName();
/*      */       }
/*      */     }
/*      */     
/*  845 */     child.despawnVillager();
/*      */     
/*  847 */     this.worldObj.func_72838_d(adult);
/*      */     
/*  849 */     if (this.isInn) {
/*  850 */       merchantCreated();
/*      */     } else {
/*  852 */       updateSigns();
/*      */     }
/*      */   }
/*      */   
/*      */   public void addCustomBuilding(BuildingCustomPlan customBuilding, Point pos) throws MLN.MillenaireException
/*      */   {
/*  858 */     BuildingLocation location = new BuildingLocation(customBuilding, pos, false);
/*      */     
/*  860 */     Building building = new Building(this.mw, this.culture, this.villageType, location, false, false, pos, getPos());
/*      */     
/*  862 */     customBuilding.registerResources(building, location);
/*      */     
/*  864 */     building.initialise(null, false);
/*  865 */     registerBuildingEntity(building);
/*      */     
/*  867 */     BuildingProject project = new BuildingProject(customBuilding, location);
/*      */     
/*  869 */     if (!this.buildingProjects.containsKey(BuildingProject.EnumProjects.CUSTOMBUILDINGS)) {
/*  870 */       this.buildingProjects.put(BuildingProject.EnumProjects.CUSTOMBUILDINGS, new ArrayList());
/*      */     }
/*      */     
/*  873 */     ((List)this.buildingProjects.get(BuildingProject.EnumProjects.CUSTOMBUILDINGS)).add(project);
/*      */     
/*  875 */     if (MLN.LogBuildingPlan >= 1) {
/*  876 */       MLN.major(this, "Created new Custom Building Entity: " + customBuilding.buildingKey + " at " + pos);
/*      */     }
/*      */   }
/*      */   
/*      */   public void addOrReplaceRecord(VillagerRecord vr) throws MLN.MillenaireException {
/*  881 */     if (vr == null) {
/*  882 */       throw new MLN.MillenaireException("Attempting to insert null VR");
/*      */     }
/*      */     
/*  885 */     while (this.vrecords.remove(vr)) {}
/*      */     
/*      */ 
/*  888 */     this.vrecords.add(vr);
/*      */   }
/*      */   
/*      */   public void addOrReplaceVillager(MillVillager villager)
/*      */   {
/*  893 */     if (villager.vtype == null) {
/*  894 */       MLN.printException("Tried adding a villager, but his type is null: " + villager, new Exception());
/*  895 */       return;
/*      */     }
/*      */     
/*  898 */     for (int i = this.villagers.size() - 1; i >= 0; i--) {
/*  899 */       if (((MillVillager)this.villagers.get(i)).villager_id == villager.villager_id) {
/*  900 */         if (this.villagers.get(i) == villager) {
/*  901 */           this.villagers.remove(i);
/*  902 */         } else if (((MillVillager)this.villagers.get(i)).func_145782_y() == villager.func_145782_y()) {
/*  903 */           if (MLN.LogVillager >= 1) {
/*  904 */             MLN.major(this.villagers.get(i), "Two copies with same entityId!");
/*      */           }
/*  906 */           this.villagers.remove(i);
/*      */         }
/*  908 */         else if (villager.client_lastupdated > ((MillVillager)this.villagers.get(i)).client_lastupdated) {
/*  909 */           if (MLN.LogVillagerSpawn >= 2) {
/*  910 */             Exception e = new Exception();
/*  911 */             MLN.printException("addOrReplaceVillager in " + this + ": Found an other copy of " + villager + " in village " + ((MillVillager)this.villagers.get(i)).getTownHall() + ": " + this.villagers.get(i), e);
/*      */           }
/*      */           
/*  914 */           ((MillVillager)this.villagers.get(i)).despawnVillagerSilent();
/*  915 */           this.villagers.remove(i);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  920 */     this.villagers.add(villager);
/*      */   }
/*      */   
/*      */   public void addToExports(InvItem good, int quantity) {
/*  924 */     if (this.exported.containsKey(good)) {
/*  925 */       this.exported.put(good, Integer.valueOf(((Integer)this.exported.get(good)).intValue() + quantity));
/*      */     } else {
/*  927 */       this.exported.put(good, Integer.valueOf(quantity));
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToImports(InvItem good, int quantity) {
/*  932 */     if (this.imported.containsKey(good)) {
/*  933 */       this.imported.put(good, Integer.valueOf(((Integer)this.imported.get(good)).intValue() + quantity));
/*      */     } else {
/*  935 */       this.imported.put(good, Integer.valueOf(quantity));
/*      */     }
/*      */   }
/*      */   
/*      */   public void adjustLanguage(EntityPlayer player, int l) {
/*  940 */     this.mw.getProfile(player.getDisplayName()).adjustLanguage(getTownHall().culture.key, l);
/*      */   }
/*      */   
/*      */   public void adjustRelation(Point villagePos, int change, boolean reset)
/*      */   {
/*  945 */     int relation = change;
/*      */     
/*  947 */     if ((this.relations.containsKey(villagePos)) && (!reset)) {
/*  948 */       relation += ((Integer)this.relations.get(villagePos)).intValue();
/*      */     }
/*      */     
/*  951 */     if (relation > 100) {
/*  952 */       relation = 100;
/*  953 */     } else if (relation < -100) {
/*  954 */       relation = -100;
/*      */     }
/*      */     
/*  957 */     this.relations.put(villagePos, Integer.valueOf(relation));
/*      */     
/*  959 */     this.saveNeeded = true;
/*      */     
/*      */ 
/*  962 */     Building village = this.mw.getBuilding(villagePos);
/*      */     
/*  964 */     if (village == null) {
/*  965 */       MLN.error(this, "Could not find village at " + villagePos + " in order to adjust relation.");
/*      */     } else {
/*  967 */       village.relations.put(getPos(), Integer.valueOf(relation));
/*  968 */       village.saveTownHall("distance relation change");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void adjustReputation(EntityPlayer player, int l)
/*      */   {
/*  978 */     this.mw.getProfile(player.getDisplayName()).adjustReputation(getTownHall(), l);
/*      */   }
/*      */   
/*      */   public boolean areBlocksLeft() {
/*  982 */     if (this.bblocks == null) {
/*  983 */       return false;
/*      */     }
/*      */     
/*  986 */     if (this.bblocksPos >= this.bblocks.length) {
/*  987 */       return false;
/*      */     }
/*      */     
/*  990 */     return true;
/*      */   }
/*      */   
/*      */   public void attemptMerchantMove(boolean forced)
/*      */   {
/*  995 */     List<Building> targets = new ArrayList();
/*  996 */     List<Building> backupTargets = new ArrayList();
/*      */     
/*  998 */     for (Point vp : getTownHall().relations.keySet()) {
/*  999 */       townHall = this.mw.getBuilding(vp);
/* 1000 */       if ((townHall != null) && (getTownHall() != null) && (townHall.villageType != getTownHall().villageType) && ((((Integer)getTownHall().relations.get(vp)).intValue() >= 90) || ((((Integer)getTownHall().relations.get(vp)).intValue() >= 50) && (townHall.culture == this.culture))) && (getPos().distanceTo(townHall.getPos()) < 2000.0D))
/*      */       {
/*      */ 
/*      */ 
/* 1004 */         if (MLN.LogMerchant >= 3) {
/* 1005 */           MLN.debug(this, "Considering village " + townHall.getVillageQualifiedName() + " for merchant : " + this.merchantRecord);
/*      */         }
/*      */         
/* 1008 */         for (Building inn : townHall.getBuildingsWithTag("inn")) {
/* 1009 */           boolean moveNeeded = false;
/*      */           
/* 1011 */           HashMap<InvItem, Integer> content = this.resManager.getChestsContent();
/*      */           
/* 1013 */           for (InvItem good : content.keySet()) {
/* 1014 */             if ((((Integer)content.get(good)).intValue() > 0) && (inn.getTownHall().nbGoodNeeded(good.getItem(), good.meta) > 0)) {
/* 1015 */               moveNeeded = true;
/* 1016 */               break;
/*      */             }
/*      */           }
/*      */           
/* 1020 */           if (moveNeeded) {
/* 1021 */             if (inn.merchantRecord == null)
/*      */             {
/* 1023 */               targets.add(inn);
/* 1024 */               targets.add(inn);
/* 1025 */               targets.add(inn);
/* 1026 */             } else if ((inn.nbNightsMerchant > 1) || (forced)) {
/* 1027 */               targets.add(inn);
/*      */             }
/*      */             
/* 1030 */             if (MLN.LogMerchant >= 3) {
/* 1031 */               MLN.debug(this, "Found good move in " + townHall.getVillageQualifiedName() + " for merchant : " + this.merchantRecord);
/*      */             }
/*      */           }
/* 1034 */           else if (this.nbNightsMerchant > 3) {
/* 1035 */             backupTargets.add(inn);
/*      */             
/* 1037 */             if (MLN.LogMerchant >= 3) {
/* 1038 */               MLN.debug(this, "Found backup move in " + townHall.getVillageQualifiedName() + " for merchant : " + this.merchantRecord);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     Building townHall;
/* 1045 */     if ((targets.size() == 0) && (backupTargets.size() == 0)) {
/* 1046 */       if (MLN.LogMerchant >= 2) {
/* 1047 */         MLN.minor(this, "Failed to find a destination for merchant: " + this.merchantRecord);
/*      */       }
/*      */       
/*      */       return;
/*      */     }
/*      */     Building inn;
/*      */     Building inn;
/* 1054 */     if (targets.size() > 0) {
/* 1055 */       inn = (Building)targets.get(MillCommonUtilities.randomInt(targets.size()));
/*      */     } else {
/* 1057 */       inn = (Building)backupTargets.get(MillCommonUtilities.randomInt(backupTargets.size()));
/*      */     }
/*      */     
/* 1060 */     if (inn.merchantRecord == null) {
/* 1061 */       moveMerchant(inn);
/* 1062 */     } else if ((inn.nbNightsMerchant > 1) || (forced)) {
/* 1063 */       swapMerchants(inn);
/*      */     }
/*      */   }
/*      */   
/*      */   private void attemptPlanNewRaid()
/*      */   {
/* 1069 */     for (VillagerRecord vr : this.vrecords)
/*      */     {
/* 1071 */       if (vr.raidingVillage) {
/* 1072 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1078 */     int raidingStrength = (int)(getVillageRaidingStrength() * 2.0F);
/*      */     
/* 1080 */     if (MLN.LogDiplomacy >= 3) {
/* 1081 */       MLN.debug(this, "Checking out for new raid, strength: " + raidingStrength);
/*      */     }
/*      */     
/* 1084 */     if (raidingStrength > 0) {
/* 1085 */       List<Building> targets = new ArrayList();
/*      */       
/* 1087 */       if (this.villageType.lonebuilding) {
/* 1088 */         for (Building distantVillage : this.mw.allBuildings()) {
/* 1089 */           if ((distantVillage != null) && (distantVillage.isTownhall) && (distantVillage.villageType != null) && (!distantVillage.villageType.lonebuilding) && (getPos().distanceTo(distantVillage.getPos()) < MLN.BanditRaidRadius) && (distantVillage.getVillageDefendingStrength() < raidingStrength))
/*      */           {
/*      */ 
/* 1092 */             if (MLN.LogDiplomacy >= 3) {
/* 1093 */               MLN.debug(this, "Lone building valid target: " + distantVillage);
/*      */             }
/*      */             
/* 1096 */             targets.add(distantVillage);
/*      */           }
/*      */           
/*      */         }
/*      */       } else {
/* 1101 */         for (Point p : this.relations.keySet()) {
/* 1102 */           if (((Integer)this.relations.get(p)).intValue() < -90) {
/* 1103 */             Building distantVillage = this.mw.getBuilding(p);
/*      */             
/* 1105 */             if (distantVillage != null)
/*      */             {
/* 1107 */               if (MLN.LogDiplomacy >= 3) {
/* 1108 */                 MLN.debug(this, "Testing village valid target: " + distantVillage + "/" + distantVillage.getVillageDefendingStrength());
/*      */               }
/*      */               
/* 1111 */               if (distantVillage.getVillageDefendingStrength() < raidingStrength) {
/* 1112 */                 if (MLN.LogDiplomacy >= 3) {
/* 1113 */                   MLN.debug(this, "Village valid target: " + distantVillage);
/*      */                 }
/*      */                 
/* 1116 */                 targets.add(distantVillage);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1124 */       if (!targets.isEmpty()) {
/* 1125 */         Building target = (Building)targets.get(MillCommonUtilities.randomInt(targets.size()));
/*      */         
/*      */ 
/* 1128 */         if ((this.isActive) || (target.isActive)) {
/* 1129 */           planRaid(target);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public List<Goods> calculateBuyingGoods(IInventory playerInventory)
/*      */   {
/* 1137 */     if ((!this.culture.shopBuys.containsKey(this.location.shop)) && (!this.culture.shopBuysOptional.containsKey(this.location.shop))) {
/* 1138 */       return null;
/*      */     }
/*      */     
/* 1141 */     List<Goods> baseGoods = (List)this.culture.shopBuys.get(this.location.shop);
/*      */     
/* 1143 */     List<Goods> extraGoods = new ArrayList();
/*      */     
/* 1145 */     if (this.culture.shopBuysOptional.containsKey(this.location.shop)) {
/* 1146 */       for (Goods g : (List)this.culture.shopBuysOptional.get(this.location.shop))
/*      */       {
/* 1148 */         if ((playerInventory == null) || (MillCommonUtilities.countChestItems(playerInventory, g.item.getItem(), g.item.meta) > 0)) {
/* 1149 */           extraGoods.add(g);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1154 */     if (this.isTownhall) {
/* 1155 */       BuildingPlan goalPlan = getCurrentGoalBuildingPlan();
/*      */       
/* 1157 */       if (goalPlan != null) {
/* 1158 */         for (InvItem key : goalPlan.resCost.keySet()) {
/* 1159 */           if (key.meta != -1) {
/* 1160 */             boolean found = false;
/* 1161 */             for (Goods tg : baseGoods) {
/* 1162 */               if ((tg.item.getItem() == key.getItem()) && (tg.item.meta == key.meta)) {
/* 1163 */                 found = true;
/*      */               }
/*      */             }
/* 1166 */             if (!found) {
/* 1167 */               if (this.culture.goodsByItem.containsKey(key)) {
/* 1168 */                 extraGoods.add(this.culture.goodsByItem.get(key));
/*      */               } else {
/* 1170 */                 extraGoods.add(new Goods(key));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1178 */     if (extraGoods.size() == 0) {
/* 1179 */       return baseGoods;
/*      */     }
/* 1181 */     List<Goods> finalGoods = new ArrayList();
/*      */     
/* 1183 */     for (Goods good : baseGoods) {
/* 1184 */       finalGoods.add(good);
/*      */     }
/*      */     
/* 1187 */     for (Goods good : extraGoods) {
/* 1188 */       finalGoods.add(good);
/*      */     }
/*      */     
/* 1191 */     return finalGoods;
/*      */   }
/*      */   
/*      */ 
/*      */   public AStarPathing.PathingWorker calculatePath(MillVillager villager, Point start, Point dest, boolean extraLog)
/*      */   {
/* 1197 */     if ((!MillVillager.usingBinaryPathing) && (MillVillager.usingCustomPathing)) {
/* 1198 */       if (this.pathing == null) {
/*      */         try {
/* 1200 */           rebuildPathing(true);
/*      */         } catch (MLN.MillenaireException e) {
/* 1202 */           MLN.printException("Error when rebuilding pathing:", e);
/*      */         }
/*      */       }
/*      */       
/* 1206 */       if (this.pathing == null) {
/* 1207 */         if ((MLN.LogPathing >= 1) && (extraLog)) {
/* 1208 */           MLN.major(this, "Can't do pathing as can't generate connections.");
/*      */         }
/* 1210 */         return null;
/*      */       }
/*      */       
/* 1213 */       if (!this.pathing.isInArea(start)) {
/* 1214 */         if ((MLN.LogPathing >= 1) && (extraLog)) {
/* 1215 */           MLN.major(this, "Start outside of TH area.");
/*      */         }
/* 1217 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1221 */       if (!this.pathing.isInArea(dest)) {
/* 1222 */         if ((MLN.LogPathing >= 1) && (extraLog)) {
/* 1223 */           MLN.major(this, "Dest outside of TH area.");
/*      */         }
/* 1225 */         return null;
/*      */       }
/*      */       
/* 1228 */       if ((MLN.LogConnections >= 1) && (extraLog)) {
/* 1229 */         MLN.major(this, "calling getPath: " + (start.getiX() - this.winfo.mapStartX) + "/" + (start.getiZ() - this.winfo.mapStartZ) + " to " + (dest.getiX() - this.winfo.mapStartX) + "/" + (dest.getiZ() - this.winfo.mapStartZ));
/*      */       }
/*      */       
/*      */ 
/* 1233 */       return this.pathing.createWorkerForPath(villager, start.getiX(), start.getiZ(), dest.getiX(), dest.getiZ());
/*      */     }
/*      */     
/* 1236 */     return null;
/*      */   }
/*      */   
/*      */   public void calculatePathsToClear()
/*      */   {
/* 1241 */     if (this.pathsToBuild != null)
/*      */     {
/* 1243 */       List<List<BuildingBlock>> pathsToBuildLocal = this.pathsToBuild;
/*      */       
/* 1245 */       long startTime = System.currentTimeMillis();
/*      */       
/* 1247 */       List<Point> oldPathPointsToClearNew = new ArrayList();
/*      */       
/* 1249 */       HashSet<Point> newPathPoints = new HashSet();
/*      */       
/* 1251 */       for (List<BuildingBlock> path : pathsToBuildLocal) {
/* 1252 */         for (BuildingBlock bp : path) {
/* 1253 */           newPathPoints.add(bp.p);
/*      */         }
/*      */       }
/*      */       
/* 1257 */       for (int x = this.winfo.mapStartX; x < this.winfo.mapStartX + this.winfo.length; x++) {
/* 1258 */         for (int z = this.winfo.mapStartZ; z < this.winfo.mapStartZ + this.winfo.width; z++) {
/* 1259 */           int basey = this.winfo.topGround[(x - this.winfo.mapStartX)][(z - this.winfo.mapStartZ)];
/*      */           
/* 1261 */           for (int dy = -2; dy < 3; dy++) {
/* 1262 */             int y = dy + basey;
/*      */             
/* 1264 */             Block block = this.worldObj.func_147439_a(x, y, z);
/* 1265 */             int meta = this.worldObj.func_72805_g(x, y, z);
/*      */             
/* 1267 */             if (((block == Mill.path) || (block == Mill.pathSlab)) && (meta < 8)) {
/* 1268 */               Point p = new Point(x, y, z);
/* 1269 */               if (!newPathPoints.contains(p)) {
/* 1270 */                 oldPathPointsToClearNew.add(p);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1277 */       this.oldPathPointsToClearIndex = 0;
/* 1278 */       this.oldPathPointsToClear = oldPathPointsToClearNew;
/*      */       
/* 1280 */       if (MLN.LogVillagePaths >= 2) {
/* 1281 */         MLN.minor(this, "Finished looking for paths to clear. Found: " + this.oldPathPointsToClear.size() + ". Duration: " + (System.currentTimeMillis() - startTime) + " ms.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public List<Goods> calculateSellingGoods(IInventory playerInventory)
/*      */   {
/* 1288 */     if (!this.culture.shopSells.containsKey(this.location.shop)) {
/* 1289 */       return null;
/*      */     }
/*      */     
/* 1292 */     return (List)this.culture.shopSells.get(this.location.shop);
/*      */   }
/*      */   
/*      */   public void callForHelp(Entity attacker) {
/* 1296 */     if (MLN.LogGeneralAI >= 3) {
/* 1297 */       MLN.debug(this, "Calling for help among: " + this.villagers.size() + " villagers.");
/*      */     }
/* 1299 */     for (MillVillager villager : this.villagers) {
/* 1300 */       if (MLN.LogGeneralAI >= 3) {
/* 1301 */         MLN.debug(villager, "Testing villager. Will fight? " + villager.helpsInAttacks() + ". Current target? " + villager.func_70777_m() + ". Distance to threat: " + villager.getPos().distanceTo(attacker));
/*      */       }
/*      */       
/* 1304 */       if ((villager.func_70777_m() == null) && (villager.helpsInAttacks()) && (!villager.isRaider))
/*      */       {
/* 1306 */         if (villager.getPos().distanceTo(attacker) < 80.0D) {
/* 1307 */           if (MLN.LogGeneralAI >= 1) {
/* 1308 */             MLN.major(villager, "Off to help a friend attacked by attacking: " + attacker);
/*      */           }
/* 1310 */           villager.setEntityToAttack(attacker);
/* 1311 */           villager.clearGoal();
/* 1312 */           villager.speakSentence("calltoarms", 0, 50, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canAffordBuild(BuildingPlan plan)
/*      */   {
/* 1320 */     for (InvItem key : plan.resCost.keySet()) {
/* 1321 */       if (((Integer)plan.resCost.get(key)).intValue() > countGoods(key.getItem(), key.meta)) {
/* 1322 */         return false;
/*      */       }
/*      */     }
/* 1325 */     return true;
/*      */   }
/*      */   
/*      */   public boolean canAffordBuildAfterGoal(BuildingPlan plan)
/*      */   {
/* 1330 */     BuildingPlan goalPlan = getCurrentGoalBuildingPlan();
/*      */     
/* 1332 */     for (InvItem key : plan.resCost.keySet()) {
/* 1333 */       if ((goalPlan != null) && (goalPlan.resCost.containsKey(key))) {
/* 1334 */         if (((Integer)plan.resCost.get(key)).intValue() + ((Integer)goalPlan.resCost.get(key)).intValue() > countGoods(key.getItem(), key.meta)) {
/* 1335 */           return false;
/*      */         }
/*      */       }
/* 1338 */       else if (((Integer)plan.resCost.get(key)).intValue() > countGoods(key.getItem(), key.meta)) {
/* 1339 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1343 */     return true;
/*      */   }
/*      */   
/*      */   public void cancelBuilding(BuildingLocation location)
/*      */   {
/* 1348 */     if (location.isLocationSamePlace(this.buildingLocationIP)) {
/* 1349 */       this.buildingLocationIP = null;
/*      */     }
/*      */     
/* 1352 */     if (location.isLocationSamePlace(this.buildingGoalLocation)) {
/* 1353 */       this.buildingGoalLocation = null;
/* 1354 */       this.buildingGoal = null;
/*      */     }
/*      */     
/* 1357 */     for (Iterator i$ = this.buildingProjects.values().iterator(); i$.hasNext();) { projects = (List)i$.next();
/* 1358 */       for (BuildingProject project : projects) {
/* 1359 */         if (project.location == location) {
/* 1360 */           projects.remove(project);
/* 1361 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     List<BuildingProject> projects;
/* 1366 */     this.buildings.remove(location.pos);
/*      */     
/* 1368 */     this.winfo.removeBuildingLocation(location);
/*      */     
/* 1370 */     this.mw.removeBuilding(location.pos);
/*      */   }
/*      */   
/*      */ 
/*      */   public void cancelRaid()
/*      */   {
/* 1376 */     if (MLN.LogDiplomacy >= 1) {
/* 1377 */       MLN.major(this, "Cancelling raid");
/*      */     }
/*      */     
/* 1380 */     this.raidPlanningStart = 0L;
/* 1381 */     this.raidStart = 0L;
/* 1382 */     this.raidTarget = null;
/*      */   }
/*      */   
/*      */   public boolean canChildMoveIn(int pGender, String familyName)
/*      */   {
/* 1387 */     if ((pGender == 2) && (this.location.femaleResident.size() == 0)) {
/* 1388 */       return false;
/*      */     }
/*      */     
/* 1391 */     if ((pGender == 1) && (this.location.maleResident.size() == 0)) {
/* 1392 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1396 */     for (VillagerRecord vr : this.vrecords) {
/* 1397 */       if ((vr.gender != pGender) && (!vr.getType().isChild) && (vr.familyName.equals(familyName))) {
/* 1398 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1402 */     int nbAdultSameSex = 0;
/* 1403 */     for (VillagerRecord vr : this.vrecords) {
/* 1404 */       if ((vr.gender == pGender) && (vr.getType() != null) && (!vr.getType().isChild)) {
/* 1405 */         nbAdultSameSex++;
/*      */       }
/*      */     }
/*      */     
/* 1409 */     if ((pGender == 1) && (nbAdultSameSex >= this.location.maleResident.size())) {
/* 1410 */       return false;
/*      */     }
/*      */     
/* 1413 */     if ((pGender == 2) && (nbAdultSameSex >= this.location.femaleResident.size())) {
/* 1414 */       return false;
/*      */     }
/*      */     
/* 1417 */     return true;
/*      */   }
/*      */   
/*      */   public boolean canSee(int x1, int z1, int x2, int z2)
/*      */   {
/* 1422 */     if ((x1 < this.winfo.mapStartX) || (x1 >= this.winfo.mapStartX + this.winfo.length) || (z1 < this.winfo.mapStartZ) || (z1 >= this.winfo.mapStartZ + this.winfo.width)) {
/* 1423 */       if (MLN.LogPathing >= 3) {
/* 1424 */         MLN.debug(this, "Start outside of TH area.");
/*      */       }
/* 1426 */       return false;
/*      */     }
/*      */     
/* 1429 */     if ((x2 < this.winfo.mapStartX) || (x2 >= this.winfo.mapStartX + this.winfo.length) || (z1 < this.winfo.mapStartZ) || (z2 >= this.winfo.mapStartZ + this.winfo.width)) {
/* 1430 */       if (MLN.LogPathing >= 3) {
/* 1431 */         MLN.debug(this, "Dest outside of TH area.");
/*      */       }
/* 1433 */       return false;
/*      */     }
/*      */     
/* 1436 */     if (this.pathing == null) {
/* 1437 */       return false;
/*      */     }
/* 1439 */     x1 -= this.winfo.mapStartX;
/* 1440 */     z1 -= this.winfo.mapStartZ;
/* 1441 */     x2 -= this.winfo.mapStartX;
/* 1442 */     z2 -= this.winfo.mapStartZ;
/* 1443 */     return this.pathing.canSee(new AStarPathing.Point2D(x1, z2), new AStarPathing.Point2D(x2, z2));
/*      */   }
/*      */   
/*      */   public void changeVillageName(String s) {
/* 1447 */     this.name = s;
/*      */   }
/*      */   
/*      */   public void changeVillageQualifier(String s) {
/* 1451 */     this.qualifier = s;
/*      */   }
/*      */   
/*      */   public void checkBattleStatus() {
/* 1455 */     int nbAttackers = 0;
/* 1456 */     int nbLiveAttackers = 0;
/* 1457 */     int nbLiveDefenders = 0;
/* 1458 */     Point attackingVillagePos = null;
/*      */     
/* 1460 */     for (VillagerRecord vr : this.vrecords) {
/* 1461 */       if (vr.raidingVillage) {
/* 1462 */         nbAttackers++;
/* 1463 */         if (!vr.killed) {
/* 1464 */           nbLiveAttackers++;
/*      */         }
/*      */         
/* 1467 */         attackingVillagePos = vr.originalVillagePos;
/* 1468 */       } else if ((vr.getType().helpInAttacks) && (!vr.killed) && (!vr.awayraiding) && (!vr.awayhired)) {
/* 1469 */         nbLiveDefenders++;
/*      */       }
/*      */     }
/*      */     
/* 1473 */     if (this.isTownhall) {
/* 1474 */       if ((this.chestLocked) && (nbLiveDefenders == 0)) {
/* 1475 */         unlockAllChests();
/*      */         
/* 1477 */         ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '4', "ui.allchestsunlocked", new String[] { getVillageQualifiedName() });
/* 1478 */       } else if ((!this.chestLocked) && (nbLiveDefenders > 0)) {
/* 1479 */         lockAllBuildingsChests();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1486 */     if (nbAttackers > 0) {
/* 1487 */       this.underAttack = true;
/* 1488 */       if ((nbLiveAttackers == 0) || (nbLiveDefenders == 0))
/*      */       {
/* 1490 */         boolean finish = false;
/*      */         
/*      */ 
/*      */ 
/* 1494 */         if (nbLiveAttackers > 0) {
/* 1495 */           for (MillVillager v : this.villagers) {
/* 1496 */             if ((!finish) && (v.isRaider) && (this.resManager.getDefendingPos().distanceToSquared(v) < 25.0D)) {
/* 1497 */               finish = true;
/*      */             }
/*      */           }
/*      */         } else {
/* 1501 */           finish = true;
/*      */         }
/*      */         
/* 1504 */         if (finish) {
/* 1505 */           if (attackingVillagePos == null) {
/* 1506 */             MLN.error(this, "Wanted to end raid but can't find originating village's position.");
/* 1507 */             clearAllAttackers();
/*      */           } else {
/* 1509 */             Building attackingVillage = this.mw.getBuilding(attackingVillagePos);
/*      */             
/* 1511 */             if (attackingVillage == null)
/*      */             {
/*      */ 
/* 1514 */               clearAllAttackers();
/*      */             } else {
/* 1516 */               boolean endedProperly = attackingVillage.endRaid();
/*      */               
/*      */ 
/*      */ 
/* 1520 */               if (!endedProperly) {
/* 1521 */                 clearAllAttackers();
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/* 1528 */       this.underAttack = false;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkExploreTag(EntityPlayer player) {
/* 1533 */     if ((player != null) && (this.location.getPlan() != null) && (!this.mw.getProfile(player.getDisplayName()).isTagSet(this.location.getPlan().exploreTag)))
/*      */     {
/* 1535 */       if (this.resManager.getSleepingPos().distanceToSquared(player) < 16.0D)
/*      */       {
/*      */ 
/*      */ 
/* 1539 */         boolean valid = true;
/* 1540 */         int x = this.resManager.getSleepingPos().getiX();
/* 1541 */         int z = this.resManager.getSleepingPos().getiZ();
/*      */         
/* 1543 */         while ((valid) && ((x != (int)player.field_70165_t) || (z != (int)player.field_70161_v))) {
/* 1544 */           Block block = this.worldObj.func_147439_a(x, this.resManager.getSleepingPos().getiY() + 1, z);
/*      */           
/* 1546 */           if ((block != Blocks.field_150350_a) && (block.func_149688_o().func_76230_c())) {
/* 1547 */             valid = false;
/*      */           }
/* 1549 */           else if (x > (int)player.field_70165_t) {
/* 1550 */             x--;
/* 1551 */           } else if (x < (int)player.field_70165_t) {
/* 1552 */             x++;
/*      */           }
/* 1554 */           else if (z > (int)player.field_70161_v) {
/* 1555 */             z--;
/* 1556 */           } else if (z < (int)player.field_70161_v) {
/* 1557 */             z++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1563 */         if (valid) {
/* 1564 */           this.mw.getProfile(player.getDisplayName()).setTag(this.location.getPlan().exploreTag);
/*      */           
/* 1566 */           ServerSender.sendTranslatedSentence(player, '2', "other.exploredbuilding", new String[] { this.location.getPlan().nativeName });
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkSeller()
/*      */   {
/* 1574 */     if ((!this.worldObj.func_72935_r()) || (this.underAttack)) {
/* 1575 */       return;
/*      */     }
/*      */     
/* 1578 */     if ((this.closestPlayer == null) || (controlledBy(this.closestPlayer.getDisplayName()))) {
/* 1579 */       return;
/*      */     }
/*      */     
/* 1582 */     if ((this.closestPlayer != null) && (this.seller == null) && (getReputation(this.closestPlayer.getDisplayName()) >= 64512) && (this.chestLocked)) {
/* 1583 */       this.sellingPlace = null;
/*      */       
/* 1585 */       for (BuildingLocation l : getLocations()) {
/* 1586 */         if ((l.level >= 0) && (l.chestPos != null) && (l.shop != null) && (l.shop.length() > 0)) {
/* 1587 */           if ((l.getSellingPos() != null) && (l.getSellingPos().distanceTo(this.closestPlayer) < 3.0D)) {
/* 1588 */             this.sellingPlace = l.getSellingPos();
/* 1589 */           } else if ((l.getSellingPos() == null) && (l.sleepingPos.distanceTo(this.closestPlayer) < 3.0D)) {
/* 1590 */             this.sellingPlace = l.sleepingPos;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1595 */       if (this.sellingPlace != null) {
/* 1596 */         for (MillVillager villager : this.villagers) {
/* 1597 */           if ((villager.isSeller()) && 
/* 1598 */             (this.builder != villager) && ((this.seller == null) || (this.sellingPlace.distanceToSquared(villager) < this.sellingPlace.distanceToSquared(this.seller)))) {
/* 1599 */             this.seller = villager;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1604 */         if (this.seller != null) {
/* 1605 */           this.seller.clearGoal();
/* 1606 */           this.seller.goalKey = Goal.beSeller.key;
/* 1607 */           Goal.beSeller.onAccept(this.seller);
/* 1608 */           if (MLN.LogSelling >= 3) {
/* 1609 */             MLN.debug(this, "Sending seller: " + this.seller);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkWorkers()
/*      */   {
/* 1618 */     if ((this.seller != null) && ((!Goal.beSeller.key.equals(this.seller.goalKey)) || (this.seller.field_70128_L))) {
/* 1619 */       this.seller = null;
/*      */     }
/*      */     
/* 1622 */     if ((this.builder != null) && ((this.builder.field_70128_L) || ((!Goal.getResourcesForBuild.key.equals(this.builder.goalKey)) && (!Goal.construction.key.equals(this.builder.goalKey))))) {
/* 1623 */       if (MLN.LogBuildingPlan >= 1) {
/* 1624 */         MLN.major(this, this.builder.getName() + " is no longer building.");
/*      */       }
/* 1626 */       this.builder = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private void clearAllAttackers() {
/* 1631 */     int nbCleared = 0;
/*      */     
/* 1633 */     for (int i = this.vrecords.size() - 1; i >= 0; i--) {
/* 1634 */       VillagerRecord vr = (VillagerRecord)this.vrecords.get(i);
/* 1635 */       if (vr.raidingVillage) {
/* 1636 */         this.vrecords.remove(i);
/*      */         
/* 1638 */         if (vr.getHouse().getTownHall() != this) {
/* 1639 */           if (MLN.LogDiplomacy >= 1) {
/* 1640 */             MLN.error(this, "Tried clearing attacker record but its house is set to " + vr.getHouse() + " from village " + vr.getHouse().getTownHall() + ". TH is: " + vr.getTownHall());
/*      */           }
/*      */         } else {
/* 1643 */           vr.getHouse().removeVillagerRecord(vr.id);
/*      */         }
/*      */         
/* 1646 */         nbCleared++;
/*      */       }
/*      */     }
/*      */     
/* 1650 */     if (MLN.LogDiplomacy >= 1) {
/* 1651 */       MLN.major(this, "Cleared " + nbCleared + " attackers.");
/*      */     }
/*      */     
/* 1654 */     for (int i = this.villagers.size() - 1; i >= 0; i--) {
/* 1655 */       MillVillager v = (MillVillager)this.villagers.get(i);
/* 1656 */       if (v.isRaider) {
/* 1657 */         this.villagers.remove(i);
/* 1658 */         v.getHouse().villagers.remove(v);
/* 1659 */         v.despawnVillagerSilent();
/* 1660 */         if (MLN.LogDiplomacy >= 1) {
/* 1661 */           MLN.major(this, "Despawning invader: " + v);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearOldPaths() {
/* 1668 */     if (this.oldPathPointsToClear != null) {
/* 1669 */       for (Point p : this.oldPathPointsToClear) {
/* 1670 */         Block block = p.getBlock(this.worldObj);
/* 1671 */         Block blockBelow = p.getBelow().getBlock(this.worldObj);
/*      */         
/* 1673 */         if (block == Mill.pathSlab) {
/* 1674 */           p.setBlock(this.worldObj, block, 0, true, false);
/* 1675 */         } else if (block == Mill.path) {
/* 1676 */           if (MillCommonUtilities.getBlockIdValidGround(blockBelow, true) != null) {
/* 1677 */             p.setBlock(this.worldObj, MillCommonUtilities.getBlockIdValidGround(blockBelow, true), 0, true, false);
/*      */           } else {
/* 1679 */             p.setBlock(this.worldObj, Blocks.field_150346_d, 0, true, false);
/*      */           }
/*      */         }
/*      */       }
/* 1683 */       this.oldPathPointsToClear = null;
/* 1684 */       this.pathsChanged = true;
/* 1685 */       requestSave("paths clearing rushed");
/*      */     }
/*      */   }
/*      */   
/*      */   private void completeConstruction() throws MLN.MillenaireException {
/* 1690 */     if ((this.buildingLocationIP != null) && (getBblocks() == null))
/*      */     {
/* 1692 */       BuildingPlan plan = getCurrentBuildingPlan();
/*      */       
/* 1694 */       registerBuildingLocation(this.buildingLocationIP);
/* 1695 */       updateWorldInfo();
/*      */       
/* 1697 */       if (this.buildingLocationIP.isSameLocation(this.buildingGoalLocation)) {
/* 1698 */         this.buildingGoalLocation = null;
/* 1699 */         this.buildingGoal = null;
/* 1700 */         this.buildingGoalIssue = null;
/* 1701 */         this.buildingGoalLevel = -1;
/*      */       }
/*      */       
/* 1704 */       this.builder = null;
/* 1705 */       this.buildingLocationIP = null;
/*      */       
/* 1707 */       if (plan.rebuildPath) {
/* 1708 */         recalculatePaths(false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void computeShopGoods(EntityPlayer player)
/*      */   {
/* 1715 */     List<Goods> sellingGoods = calculateSellingGoods(player.field_71071_by);
/*      */     
/* 1717 */     if (sellingGoods != null) {
/* 1718 */       LinkedHashMap<Goods, Integer> shopSellsPlayer = new LinkedHashMap();
/* 1719 */       for (Goods g : sellingGoods) {
/* 1720 */         if (g.getBasicSellingPrice(this) > 0) {
/* 1721 */           shopSellsPlayer.put(g, Integer.valueOf(g.getBasicSellingPrice(this)));
/*      */         }
/*      */       }
/* 1724 */       this.shopSells.put(player.getDisplayName(), shopSellsPlayer);
/*      */     }
/*      */     
/* 1727 */     List<Goods> buyingGoods = calculateBuyingGoods(player.field_71071_by);
/*      */     
/* 1729 */     if (buyingGoods != null) {
/* 1730 */       LinkedHashMap<Goods, Integer> shopBuysPlayer = new LinkedHashMap();
/* 1731 */       for (Goods g : buyingGoods) {
/* 1732 */         if (g.getBasicBuyingPrice(this) > 0) {
/* 1733 */           shopBuysPlayer.put(g, Integer.valueOf(g.getBasicBuyingPrice(this)));
/*      */         }
/*      */       }
/* 1736 */       this.shopBuys.put(player.getDisplayName(), shopBuysPlayer);
/*      */     }
/*      */   }
/*      */   
/*      */   public void constructCalculatedPaths()
/*      */   {
/* 1742 */     if (this.pathsToBuild != null)
/*      */     {
/* 1744 */       if (MLN.LogVillagePaths >= 2) {
/* 1745 */         MLN.minor(this, "Rebuilding calculated paths.");
/*      */       }
/*      */       
/* 1748 */       for (List<BuildingBlock> path : this.pathsToBuild) {
/* 1749 */         if (!path.isEmpty()) {
/* 1750 */           for (BuildingBlock bp : path) {
/* 1751 */             bp.pathBuild(this);
/*      */           }
/*      */         }
/*      */       }
/* 1755 */       this.pathsToBuild = null;
/* 1756 */       this.pathsChanged = true;
/* 1757 */       requestSave("paths rushed");
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean controlledBy(String playerName) {
/* 1762 */     if ((!this.isTownhall) && (getTownHall() != null)) {
/* 1763 */       return getTownHall().controlledBy(playerName);
/*      */     }
/*      */     
/* 1766 */     return (this.controlledBy != null) && (this.controlledBy.equals(this.mw.getProfile(playerName).key));
/*      */   }
/*      */   
/*      */   public int countChildren() {
/* 1770 */     int nb = 0;
/* 1771 */     for (VillagerRecord vr : this.vrecords) {
/* 1772 */       if ((vr.getType() != null) && (vr.getType().isChild)) {
/* 1773 */         nb++;
/*      */       }
/*      */     }
/* 1776 */     return nb;
/*      */   }
/*      */   
/*      */   public int countGoods(Block block) {
/* 1780 */     return countGoods(Item.func_150898_a(block), 0);
/*      */   }
/*      */   
/*      */   public int countGoods(Block block, int meta) {
/* 1784 */     return countGoods(Item.func_150898_a(block), meta);
/*      */   }
/*      */   
/*      */   public int countGoods(InvItem iv) {
/* 1788 */     return countGoods(iv.getItem(), iv.meta);
/*      */   }
/*      */   
/*      */   public int countGoods(Item item) {
/* 1792 */     return countGoods(item, 0);
/*      */   }
/*      */   
/*      */   public int countGoods(Item item, int meta) {
/* 1796 */     int count = 0;
/*      */     
/* 1798 */     for (Point p : this.resManager.chests) {
/* 1799 */       TileEntityChest chest = p.getMillChest(this.worldObj);
/* 1800 */       count += MillCommonUtilities.countChestItems(chest, item, meta);
/*      */     }
/*      */     
/* 1803 */     for (Point p : this.resManager.furnaces) {
/* 1804 */       TileEntityFurnace furnace = p.getFurnace(this.worldObj);
/* 1805 */       count += MillCommonUtilities.countFurnaceItems(furnace, item, meta);
/*      */     }
/*      */     
/* 1808 */     return count;
/*      */   }
/*      */   
/*      */   public MillVillager createChild(MillVillager mother, Building townHall, String fathersName) {
/*      */     try {
/* 1813 */       if (MLN.LogWorldGeneration >= 2) {
/* 1814 */         MLN.minor(this, "Creating child: " + mother.familyName);
/*      */       }
/*      */       
/* 1817 */       int gender = getNewGender();
/* 1818 */       String type = gender == 1 ? mother.getMaleChild() : mother.getFemaleChild();
/*      */       
/* 1820 */       MillVillager child = MillVillager.createVillager(townHall.culture, type, gender, this.worldObj, this.resManager.getSleepingPos(), getPos(), this.townHallPos, false, null, mother.familyName);
/*      */       
/* 1822 */       if (child == null) {
/* 1823 */         throw new MLN.MillenaireException("Child not instancied in createVillager");
/*      */       }
/*      */       
/* 1826 */       addOrReplaceVillager(child);
/* 1827 */       townHall.addOrReplaceVillager(child);
/* 1828 */       VillagerRecord vr = new VillagerRecord(this.mw, child);
/* 1829 */       vr.fathersName = fathersName;
/* 1830 */       vr.mothersName = mother.getName();
/* 1831 */       addOrReplaceRecord(vr);
/* 1832 */       townHall.addOrReplaceRecord(vr);
/* 1833 */       this.worldObj.func_72838_d(child);
/*      */       
/* 1835 */       return child;
/*      */     } catch (Exception e) {
/* 1837 */       Mill.proxy.sendChatAdmin("Error in createChild(). Check millenaire.log.");
/* 1838 */       MLN.error(this, "Exception in createChild.onUpdate(): ");
/* 1839 */       MLN.printException(e);
/*      */     }
/*      */     
/* 1842 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MillVillager createNewVillager(String type)
/*      */     throws MLN.MillenaireException
/*      */   {
/* 1853 */     MillVillager villager = MillVillager.createVillager(this.culture, type, 0, this.worldObj, this.resManager.getSleepingPos(), getPos(), this.townHallPos, false, null, null);
/* 1854 */     addOrReplaceVillager(villager);
/* 1855 */     VillagerRecord vr = new VillagerRecord(this.mw, villager);
/* 1856 */     addOrReplaceRecord(vr);
/* 1857 */     this.worldObj.func_72838_d(villager);
/*      */     
/*      */ 
/*      */ 
/* 1861 */     if (villager.vtype.isChild) {
/* 1862 */       villager.size = 20;
/* 1863 */       villager.growSize();
/*      */     }
/*      */     
/* 1866 */     return villager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String createResidents()
/*      */     throws MLN.MillenaireException
/*      */   {
/* 1877 */     if (this.location.maleResident.size() + this.location.femaleResident.size() == 0) {
/* 1878 */       return null;
/*      */     }
/*      */     
/* 1881 */     String familyName = null;
/*      */     
/*      */ 
/*      */ 
/* 1885 */     String husbandType = null;
/* 1886 */     if ((this.location.maleResident.size() > 0) && (!this.culture.getVillagerType((String)this.location.maleResident.get(0)).isChild)) {
/* 1887 */       husbandType = (String)this.location.maleResident.get(0);
/*      */     }
/* 1889 */     String wifeType = null;
/* 1890 */     if ((this.location.femaleResident.size() > 0) && (!this.culture.getVillagerType((String)this.location.femaleResident.get(0)).isChild)) {
/* 1891 */       wifeType = (String)this.location.femaleResident.get(0);
/*      */     }
/*      */     
/* 1894 */     if (MLN.LogMerchant >= 2) {
/* 1895 */       MLN.minor(this, "Creating " + husbandType + " and " + wifeType + ": " + familyName);
/*      */     }
/*      */     
/* 1898 */     VillagerRecord husbandRecord = null;VillagerRecord wifeRecord = null;
/*      */     
/* 1900 */     if (this.resManager.getSleepingPos() == null) {
/* 1901 */       MLN.error(this, "Wanted to create villagers but sleepingPos is null!");
/* 1902 */       return "";
/*      */     }
/*      */     
/* 1905 */     if (husbandType != null) {
/* 1906 */       MillVillager husband = MillVillager.createVillager(this.culture, husbandType, 1, this.worldObj, this.resManager.getSleepingPos(), getPos(), this.townHallPos, false, null, null);
/* 1907 */       familyName = husband.familyName;
/* 1908 */       addOrReplaceVillager(husband);
/* 1909 */       husbandRecord = new VillagerRecord(this.mw, husband);
/* 1910 */       addOrReplaceRecord(husbandRecord);
/* 1911 */       this.worldObj.func_72838_d(husband);
/*      */     }
/*      */     
/* 1914 */     if (wifeType != null)
/*      */     {
/* 1916 */       MillVillager wife = MillVillager.createVillager(this.culture, wifeType, 2, this.worldObj, this.resManager.getSleepingPos(), getPos(), this.townHallPos, false, null, familyName);
/*      */       
/* 1918 */       addOrReplaceVillager(wife);
/* 1919 */       wifeRecord = new VillagerRecord(this.mw, wife);
/* 1920 */       addOrReplaceRecord(wifeRecord);
/* 1921 */       this.worldObj.func_72838_d(wife);
/*      */     }
/* 1923 */     if (MLN.LogWorldGeneration >= 1) {
/* 1924 */       MLN.major(this, "Records: " + wifeRecord + "/" + husbandRecord);
/*      */     }
/*      */     
/* 1927 */     if ((wifeRecord != null) && (husbandRecord != null)) {
/* 1928 */       wifeRecord.spousesName = husbandRecord.getName();
/* 1929 */       husbandRecord.spousesName = wifeRecord.getName();
/*      */     }
/*      */     
/*      */ 
/* 1933 */     int startPos = husbandType == null ? 0 : 1;
/* 1934 */     for (int i = startPos; i < this.location.maleResident.size(); i++) {
/* 1935 */       createNewVillager((String)this.location.maleResident.get(i));
/*      */     }
/* 1937 */     startPos = wifeType == null ? 0 : 1;
/* 1938 */     for (int i = startPos; i < this.location.femaleResident.size(); i++) {
/* 1939 */       createNewVillager((String)this.location.femaleResident.get(i));
/*      */     }
/*      */     
/* 1942 */     if (this.isInn) {
/* 1943 */       merchantCreated();
/*      */     } else {
/* 1945 */       updateSigns();
/*      */     }
/*      */     
/* 1948 */     return familyName;
/*      */   }
/*      */   
/*      */   public void deleteVillager(MillVillager villager) {
/* 1952 */     while (this.villagers.remove(villager)) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void deleteVillagerFromRecords(MillVillager villager)
/*      */   {
/* 1959 */     for (int i = this.vrecords.size() - 1; i >= 0; i--) {
/* 1960 */       VillagerRecord vr = (VillagerRecord)this.vrecords.get(i);
/* 1961 */       if (vr.matches(villager)) {
/* 1962 */         this.vrecords.remove(i);
/*      */       }
/*      */     }
/*      */     
/* 1966 */     this.saveNeeded = true;
/* 1967 */     this.saveReason = "Deleted villager";
/*      */   }
/*      */   
/*      */   public void destroyVillage()
/*      */   {
/* 1972 */     if (MLN.LogVillage >= 1) {
/* 1973 */       MLN.major(this, "Destroying the village!");
/*      */     }
/*      */     
/*      */ 
/* 1977 */     for (Point p : this.resManager.chests) {
/* 1978 */       TileEntityMillChest chest = p.getMillChest(this.worldObj);
/* 1979 */       if (chest != null) {
/* 1980 */         chest.buildingPos = null;
/*      */       }
/*      */     }
/* 1983 */     for (Point p : this.buildings) {
/* 1984 */       Building building = this.mw.getBuilding(p);
/* 1985 */       if (building != null) {
/* 1986 */         for (Point p2 : this.resManager.chests) {
/* 1987 */           TileEntityMillChest chest = p2.getMillChest(this.worldObj);
/* 1988 */           if (chest != null) {
/* 1989 */             chest.buildingPos = null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1996 */     for (MillVillager villager : this.villagers) {
/* 1997 */       villager.despawnVillager();
/*      */     }
/*      */     
/*      */ 
/* 2001 */     for (Point p : this.buildings) {
/* 2002 */       this.mw.removeBuilding(p);
/*      */     }
/*      */     
/*      */ 
/* 2006 */     this.mw.removeVillageOrLoneBuilding(getPos());
/*      */     
/*      */ 
/* 2009 */     File millenaireDir = this.mw.millenaireDir;
/*      */     
/* 2011 */     if (!millenaireDir.exists()) {
/* 2012 */       millenaireDir.mkdir();
/*      */     }
/* 2014 */     File buildingsDir = new File(millenaireDir, "buildings");
/*      */     
/* 2016 */     if (!buildingsDir.exists()) {
/* 2017 */       buildingsDir.mkdir();
/*      */     }
/* 2019 */     File file1 = new File(buildingsDir, getPos().getPathString() + ".gz");
/*      */     
/* 2021 */     if (file1.exists()) {
/* 2022 */       file1.delete();
/*      */     }
/*      */   }
/*      */   
/*      */   public void displayInfos(EntityPlayer player)
/*      */   {
/* 2028 */     if (this.location == null) {
/* 2029 */       return;
/*      */     }
/*      */     
/* 2032 */     int nbAdults = 0;int nbGrownChild = 0;
/*      */     
/* 2034 */     for (MillVillager villager : this.villagers) {
/* 2035 */       if (!villager.func_70631_g_()) {
/* 2036 */         nbAdults++;
/*      */       }
/* 2038 */       else if (villager.size == 20) {
/* 2039 */         nbGrownChild++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2044 */     ServerSender.sendChat(player, EnumChatFormatting.GREEN, "It has " + this.villagers.size() + " villagers registered. (" + nbAdults + " adults, " + nbGrownChild + " grown children)");
/* 2045 */     ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Pos: " + getPos() + " sell pos:" + this.resManager.getSellingPos());
/*      */     
/* 2047 */     if (this.isTownhall) {
/* 2048 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "It has " + this.buildings.size() + " houses registered.");
/* 2049 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Connections build: " + (this.pathing != null));
/* 2050 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Village name: " + getVillageQualifiedName());
/* 2051 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Current building plan: " + getCurrentBuildingPlan() + " at " + this.buildingLocationIP);
/* 2052 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Current builder: " + this.builder);
/* 2053 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Current seller: " + this.seller);
/* 2054 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Rep: " + getReputation(player.getDisplayName()) + " bought: " + this.buildingsBought);
/*      */     }
/*      */     
/* 2057 */     if (this.isInn) {
/* 2058 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Merchant: " + this.merchantRecord);
/* 2059 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Merchant nights: " + this.nbNightsMerchant);
/*      */     }
/*      */     
/* 2062 */     if (this.location.tags == null) {
/* 2063 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "UNKNOWN TAGS");
/*      */     }
/* 2065 */     else if (this.location.tags.size() > 0) {
/* 2066 */       String s = "Tags: ";
/* 2067 */       for (String tag : this.location.tags) {
/* 2068 */         s = s + tag + " ";
/*      */       }
/* 2070 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2073 */     if (this.resManager.chests.size() > 1) {
/* 2074 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Chests registered: " + this.resManager.chests.size());
/*      */     }
/* 2076 */     if (this.resManager.furnaces.size() > 1) {
/* 2077 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Furnaces registered: " + this.resManager.furnaces.size());
/*      */     }
/*      */     
/* 2080 */     for (int i = 0; i < this.resManager.soilTypes.size(); i++) {
/* 2081 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Fields registered: " + (String)this.resManager.soilTypes.get(i) + ": " + ((List)this.resManager.soils.get(i)).size());
/*      */     }
/*      */     
/* 2084 */     if (this.resManager.sugarcanesoils.size() > 0) {
/* 2085 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Sugar cane soils registered: " + this.resManager.sugarcanesoils.size());
/*      */     }
/*      */     
/* 2088 */     if (this.resManager.fishingspots.size() > 0) {
/* 2089 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Fishing spots registered: " + this.resManager.fishingspots.size());
/*      */     }
/* 2091 */     if (this.resManager.stalls.size() > 0) {
/* 2092 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Stalls registered: " + this.resManager.stalls.size());
/*      */     }
/* 2094 */     if (this.resManager.woodspawn.size() > 0) {
/* 2095 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, "Wood spawn registered: " + this.resManager.woodspawn.size());
/*      */     }
/* 2097 */     if (this.resManager.spawns.size() > 0) {
/* 2098 */       String s = "Pens: ";
/* 2099 */       for (int i = 0; i < this.resManager.spawns.size(); i++) {
/* 2100 */         s = s + (String)this.resManager.spawnTypes.get(i) + ": " + ((List)this.resManager.spawns.get(i)).size() + " ";
/*      */       }
/* 2102 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2105 */     if (this.resManager.mobSpawners.size() > 0) {
/* 2106 */       String s = "Mob spawners: ";
/* 2107 */       for (int i = 0; i < this.resManager.mobSpawners.size(); i++) {
/* 2108 */         s = s + (String)this.resManager.mobSpawnerTypes.get(i) + ": " + ((List)this.resManager.mobSpawners.get(i)).size() + " ";
/*      */       }
/* 2110 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2113 */     if (this.resManager.sources.size() > 0) {
/* 2114 */       String s = "Sources: ";
/* 2115 */       for (int i = 0; i < this.resManager.sources.size(); i++) {
/* 2116 */         s = s + ((Block)this.resManager.sourceTypes.get(i)).func_149739_a() + ": " + ((List)this.resManager.sources.get(i)).size() + " ";
/*      */       }
/* 2118 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2121 */     for (MillVillager villager : this.villagers) {
/* 2122 */       if (villager == null) {
/* 2123 */         ServerSender.sendChat(player, EnumChatFormatting.GREEN, "NULL villager!");
/*      */       } else {
/* 2125 */         ServerSender.sendChat(player, EnumChatFormatting.GREEN, villager.getClass().getSimpleName() + ": " + villager.getPos() + (villager.func_70089_S() ? "" : " DEAD") + " " + villager.getGoalLabel(villager.goalKey));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2130 */     String s = "LKey: " + this.location.planKey + " Shop: " + this.location.shop + " special: ";
/* 2131 */     if (this.isTownhall) {
/* 2132 */       s = s + "Town Hall ";
/*      */     }
/* 2134 */     if (this.isInn) {
/* 2135 */       s = s + "Inn ";
/*      */     }
/* 2137 */     if (this.isMarket) {
/* 2138 */       s = s + "Market";
/*      */     }
/*      */     
/* 2141 */     if (this.pujas != null) {
/* 2142 */       s = s + "Shrine ";
/*      */     }
/*      */     
/* 2145 */     if (!s.equals("")) {
/* 2146 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2149 */     if ((this.pathsToBuild != null) || (this.oldPathPointsToClear != null))
/*      */     {
/* 2151 */       if (this.pathsToBuild != null) {
/* 2152 */         s = "pathsToBuild: " + this.pathsToBuild.size() + " " + this.pathsToBuildIndex + "/" + this.pathsToBuildPathIndex;
/*      */       } else {
/* 2154 */         s = "pathsToBuild:null";
/*      */       }
/*      */       
/* 2157 */       if (this.oldPathPointsToClear != null) {
/* 2158 */         s = s + " oldPathPointsToClear: " + this.oldPathPointsToClear.size() + " " + this.oldPathPointsToClearIndex;
/*      */       } else {
/* 2160 */         s = s + " oldPathPointsToClear:null";
/*      */       }
/*      */       
/* 2163 */       ServerSender.sendChat(player, EnumChatFormatting.GREEN, s);
/*      */     }
/*      */     
/* 2166 */     validateVillagerList();
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean endRaid()
/*      */   {
/* 2172 */     Building targetVillage = this.mw.getBuilding(this.raidTarget);
/*      */     
/* 2174 */     if (targetVillage == null) {
/* 2175 */       MLN.error(this, "endRaid() called but couldn't find raidTarget at: " + this.raidTarget);
/* 2176 */       return false;
/*      */     }
/* 2178 */     if (MLN.LogDiplomacy >= 1) {
/* 2179 */       MLN.major(this, "Called to end raid on " + targetVillage);
/*      */     }
/*      */     
/*      */ 
/* 2183 */     float defendingForce = targetVillage.getVillageDefendingStrength() * (1.0F + MillCommonUtilities.random.nextFloat());
/* 2184 */     float attackingForce = targetVillage.getVillageAttackerStrength() * (1.0F + MillCommonUtilities.random.nextFloat());
/*      */     
/*      */     boolean attackersWon;
/*      */     boolean attackersWon;
/* 2188 */     if (attackingForce == 0.0F) {
/* 2189 */       attackersWon = false; } else { boolean attackersWon;
/* 2190 */       if (defendingForce == 0.0F) {
/* 2191 */         attackersWon = true;
/*      */       } else {
/* 2193 */         float ratio = attackingForce / defendingForce;
/* 2194 */         attackersWon = ratio > 1.2D;
/*      */       }
/*      */     }
/* 2197 */     if (MLN.LogDiplomacy >= 1) {
/* 2198 */       MLN.major(this, "Result of raid: " + attackersWon + " (" + attackingForce + "/" + attackingForce + ")");
/*      */     }
/*      */     
/*      */ 
/* 2202 */     for (VillagerRecord vr : this.vrecords) {
/* 2203 */       if (vr.awayraiding) {
/* 2204 */         vr.awayraiding = false;
/* 2205 */         VillagerRecord awayRecord = targetVillage.getVillagerRecordById(vr.id);
/* 2206 */         if (awayRecord != null) {
/* 2207 */           vr.killed = awayRecord.killed;
/*      */         } else {
/* 2209 */           vr.killed = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2215 */     targetVillage.clearAllAttackers();
/*      */     
/*      */ 
/* 2218 */     for (MillVillager v : targetVillage.villagers) {
/* 2219 */       if ((v.func_70777_m() != null) && ((v.func_70777_m() instanceof MillVillager))) {
/* 2220 */         v.setEntityToAttack(null);
/*      */       }
/*      */     }
/*      */     
/* 2224 */     cancelRaid();
/*      */     
/* 2226 */     targetVillage.underAttack = false;
/*      */     
/* 2228 */     if (attackersWon) {
/* 2229 */       int nbStolen = 0;
/* 2230 */       String taken = "";
/*      */       
/* 2232 */       for (Goods good : this.culture.goodsList) {
/* 2233 */         if (nbStolen <= 1024) {
/* 2234 */           int nbToTake = nbGoodNeeded(good.item.getItem(), good.item.meta);
/* 2235 */           nbToTake = Math.min(nbToTake, Math.max(0, 1024 - nbStolen));
/*      */           
/* 2237 */           if (nbToTake > 0) {
/* 2238 */             nbToTake = Math.min(nbToTake, targetVillage.countGoods(good.item));
/*      */             
/* 2240 */             if (nbToTake > 0)
/*      */             {
/* 2242 */               if (MLN.LogDiplomacy >= 3) {
/* 2243 */                 MLN.debug(this, "Able to take: " + nbToTake + " " + Mill.proxy.getInvItemName(good.item));
/*      */               }
/*      */               
/* 2246 */               targetVillage.takeGoods(good.item, nbToTake);
/* 2247 */               storeGoods(good.item, nbToTake);
/* 2248 */               nbStolen += nbToTake;
/*      */               
/* 2250 */               taken = taken + ";" + good.item.getItem() + "/" + good.item.meta + "/" + nbToTake;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2256 */       this.raidsPerformed.add("success;" + targetVillage.getVillageQualifiedName() + taken);
/* 2257 */       targetVillage.raidsSuffered.add("success;" + getVillageQualifiedName() + taken);
/*      */       
/* 2259 */       if (MLN.LogDiplomacy >= 1) {
/* 2260 */         MLN.major(this, "Raid on " + targetVillage + " successfull (" + attackingForce + "/" + defendingForce + ")");
/*      */       }
/*      */       
/* 2263 */       ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '4', "raid.raidsuccesfull", new String[] { getVillageQualifiedName(), targetVillage.getVillageQualifiedName(), "" + nbStolen });
/*      */     }
/*      */     else {
/* 2266 */       this.raidsPerformed.add("failure;" + targetVillage.getVillageQualifiedName());
/* 2267 */       targetVillage.raidsSuffered.add("failure;" + getVillageQualifiedName());
/*      */       
/* 2269 */       if (MLN.LogDiplomacy >= 1) {
/* 2270 */         MLN.major(this, "Raid on " + targetVillage + " failed (" + attackingForce + "/" + defendingForce + ")");
/*      */       }
/*      */       
/* 2273 */       ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '4', "raid.raidfailed", new String[] { getVillageQualifiedName(), targetVillage.getVillageQualifiedName() });
/*      */     }
/*      */     
/*      */ 
/* 2277 */     MLN.major(this, "Finished ending raid. Records: " + this.vrecords.size());
/*      */     
/* 2279 */     targetVillage.saveTownHall("Raid on village ended");
/* 2280 */     this.saveNeeded = true;
/* 2281 */     this.saveReason = "Raid finished";
/*      */     
/* 2283 */     return true;
/*      */   }
/*      */   
/*      */   private void fillinBuildingLocation(BuildingLocation location)
/*      */   {
/* 2288 */     this.mw.testLocations("fillinBuildingLocation start");
/*      */     
/* 2290 */     boolean registered = false;
/* 2291 */     List<BuildingProject> projectsLevel; for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 2292 */       if (this.buildingProjects.containsKey(ep)) {
/* 2293 */         projectsLevel = (List)this.buildingProjects.get(ep);
/*      */         
/* 2295 */         List<BuildingProject> temp = new ArrayList(projectsLevel);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2300 */         for (BuildingProject project : temp) {
/* 2301 */           int pos = 0;
/*      */           
/* 2303 */           if ((!registered) && (project.location == null) && (location.planKey.equals(project.key))) {
/* 2304 */             project.location = location;
/* 2305 */             registered = true;
/* 2306 */             if (MLN.LogBuildingPlan >= 2) {
/* 2307 */               MLN.minor(this, "Registered building: " + location + " (level " + location.level + ", variation: " + location.getVariation() + ")");
/*      */             }
/*      */             
/* 2310 */             if (project.location.level >= 0) {
/* 2311 */               for (String s : project.location.subBuildings) {
/* 2312 */                 BuildingProject newproject = new BuildingProject(this.culture.getBuildingPlanSet(s));
/* 2313 */                 newproject.location = location.createLocationForSubBuilding(s);
/* 2314 */                 projectsLevel.add(pos + 1, newproject);
/* 2315 */                 if (MLN.LogBuildingPlan >= 1) {
/* 2316 */                   MLN.major(this, "Adding sub-building to project list: " + newproject + " at pos " + pos + " in " + projectsLevel);
/*      */                 }
/*      */               }
/*      */             }
/* 2320 */             pos++;
/* 2321 */           } else if ((!registered) && (project.location != null) && (project.location.level < 0) && (location.planKey.equals(project.key))) {
/* 2322 */             project.location = location;
/* 2323 */             registered = true;
/* 2324 */             if (MLN.LogBuildingPlan >= 1) {
/* 2325 */               MLN.major(this, "Registered subbuilding: " + location + " (level " + location.level + ", variation: " + location.getVariation() + ")");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2332 */     if (!registered)
/*      */     {
/* 2334 */       if (location.isCustomBuilding) {
/* 2335 */         BuildingProject project = new BuildingProject(this.culture.getBuildingCustom(location.planKey), location);
/*      */         
/* 2337 */         ((List)this.buildingProjects.get(BuildingProject.EnumProjects.CUSTOMBUILDINGS)).add(project);
/*      */       } else {
/* 2339 */         BuildingProject project = new BuildingProject(this.culture.getBuildingPlanSet(location.planKey));
/* 2340 */         project.location = location;
/*      */         
/* 2342 */         if (this.villageType.playerControlled) {
/* 2343 */           ((List)this.buildingProjects.get(BuildingProject.EnumProjects.CORE)).add(project);
/*      */         }
/*      */         else {
/* 2346 */           ((List)this.buildingProjects.get(BuildingProject.EnumProjects.EXTRA)).add(project);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2352 */     this.mw.testLocations("fillinBuildingLocation end");
/*      */   }
/*      */   
/*      */ 
/*      */   public void fillStartingGoods()
/*      */   {
/* 2358 */     if (this.location.getPlan() == null) {
/* 2359 */       return;
/*      */     }
/*      */     
/* 2362 */     for (Point p : this.resManager.chests) {
/* 2363 */       TileEntityMillChest chest = p.getMillChest(this.worldObj);
/* 2364 */       if (chest != null) {
/* 2365 */         for (int i = 0; i < chest.func_70302_i_(); i++) {
/* 2366 */           chest.func_70299_a(i, null);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2371 */     for (BuildingPlan.StartingGood sg : this.location.getPlan().startingGoods) {
/* 2372 */       if (MillCommonUtilities.probability(sg.probability)) {
/* 2373 */         int nb = sg.fixedNumber;
/* 2374 */         if (sg.randomNumber > 0) {
/* 2375 */           nb += MillCommonUtilities.randomInt(sg.randomNumber + 1);
/*      */         }
/* 2377 */         if (nb > 0) {
/* 2378 */           int chestId = MillCommonUtilities.randomInt(this.resManager.chests.size());
/*      */           
/* 2380 */           TileEntityMillChest chest = ((Point)this.resManager.chests.get(chestId)).getMillChest(this.worldObj);
/* 2381 */           if (chest != null) {
/* 2382 */             MillCommonUtilities.putItemsInChest(chest, sg.item.getItem(), sg.item.meta, nb);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2389 */     if (MLN.DEV) {
/* 2390 */       testModeGoods();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private Point findAttackerSpawnPoint(Point origin)
/*      */   {
/*      */     int x;
/*      */     
/*      */     int x;
/* 2400 */     if (origin.getiX() > this.pos.getiX()) {
/* 2401 */       x = Math.min(this.winfo.length - 5, this.winfo.length / 2 + 50);
/*      */     } else
/* 2403 */       x = Math.max(5, this.winfo.length / 2 - 50);
/*      */     int z;
/*      */     int z;
/* 2406 */     if (origin.getiZ() > this.pos.getiZ()) {
/* 2407 */       z = Math.min(this.winfo.width - 5, this.winfo.width / 2 + 50);
/*      */     } else {
/* 2409 */       z = Math.max(5, this.winfo.width / 2 - 50);
/*      */     }
/*      */     
/*      */ 
/* 2413 */     for (int i = 0; i < 40; i++) {
/* 2414 */       int tx = x + MillCommonUtilities.randomInt(5 + i) - MillCommonUtilities.randomInt(5 + i);
/* 2415 */       int tz = z + MillCommonUtilities.randomInt(5 + i) - MillCommonUtilities.randomInt(5 + i);
/*      */       
/* 2417 */       tx = Math.max(Math.min(tx, this.winfo.length - 1), 0);
/* 2418 */       tz = Math.max(Math.min(tz, this.winfo.width - 1), 0);
/*      */       
/* 2420 */       tx = Math.min(tx, this.winfo.length / 2 + 50);
/* 2421 */       tx = Math.max(tx, this.winfo.length / 2 - 50);
/*      */       
/* 2423 */       tz = Math.min(tz, this.winfo.width / 2 + 50);
/* 2424 */       tz = Math.max(tz, this.winfo.width / 2 - 50);
/*      */       
/* 2426 */       if (this.winfo.canBuild[tx][tz] != 0) {
/* 2427 */         Chunk chunk = this.worldObj.func_72938_d(this.winfo.mapStartX + tx, this.winfo.mapStartZ + tz);
/* 2428 */         if (chunk.field_76636_d) {
/* 2429 */           return new Point(this.winfo.mapStartX + tx, MillCommonUtilities.findTopSoilBlock(this.worldObj, this.winfo.mapStartX + tx, this.winfo.mapStartZ + tz) + 1, this.winfo.mapStartZ + tz);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2435 */     return this.resManager.getDefendingPos();
/*      */   }
/*      */   
/*      */   private void findBuildingConstruction()
/*      */   {
/* 2440 */     if ((this.buildingGoal == null) || (this.buildingLocationIP != null)) {
/* 2441 */       return;
/*      */     }
/*      */     
/* 2444 */     BuildingProject goalProject = null;
/*      */     
/* 2446 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 2447 */       if (this.buildingProjects.containsKey(ep)) {
/* 2448 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 2449 */         for (BuildingProject project : projectsLevel) {
/* 2450 */           if ((this.buildingGoalLocation != null) && (this.buildingGoalLocation.isSameLocation(project.location))) {
/* 2451 */             goalProject = project;
/* 2452 */           } else if ((this.buildingGoalLocation == null) && (project.location == null) && (this.buildingGoal.equals(project.key))) {
/* 2453 */             goalProject = project;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2460 */     if (MLN.LogBuildingPlan >= 3) {
/* 2461 */       MLN.debug(this, "Building goal project: " + goalProject + " ");
/*      */     }
/*      */     
/* 2464 */     if (goalProject == null) {
/* 2465 */       MLN.error(this, "Could not find building project for " + this.buildingGoal + " and " + this.buildingGoalLocation + ", cancelling goal.");
/* 2466 */       this.buildingGoal = null;
/* 2467 */       return;
/*      */     }
/*      */     
/* 2470 */     if ((goalProject.location != null) && (goalProject.location.level >= 0) && (goalProject.location.upgradesAllowed))
/*      */     {
/*      */ 
/* 2473 */       if (canAffordBuild(goalProject.getPlan(this.buildingGoalVariation, this.buildingGoalLevel))) {
/* 2474 */         if (this.buildingGoalLocation != null) {
/* 2475 */           this.buildingLocationIP = this.buildingGoalLocation;
/*      */         } else {
/* 2477 */           this.buildingLocationIP = goalProject.location;
/*      */         }
/*      */         
/* 2480 */         setBblocks(goalProject.getPlan(this.buildingGoalVariation, this.buildingGoalLevel).getBuildingPoints(this.worldObj, this.buildingLocationIP, false));
/*      */         
/* 2482 */         if (MLN.LogBuildingPlan >= 1) {
/* 2483 */           MLN.major(this, "Upgrade project possible at: " + this.location + " for level " + this.buildingGoalLevel);
/*      */         }
/*      */         
/* 2486 */         if (getBblocks().length == 0) {
/* 2487 */           MLN.error(this, "No bblocks for  " + this.buildingLocationIP);
/*      */           try {
/* 2489 */             rushBuilding();
/*      */           } catch (Exception e) {
/* 2491 */             MLN.printException("Exception when trying to rush building:", e);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 2496 */         this.buildingGoalIssue = "ui.lackingresources";
/*      */       }
/* 2498 */     } else if ((goalProject.location != null) && (goalProject.location.level < 0))
/*      */     {
/*      */ 
/* 2501 */       if (canAffordBuild(goalProject.getPlan(this.buildingGoalVariation, this.buildingGoalLevel))) {
/* 2502 */         if (this.buildingGoalLocation != null) {
/* 2503 */           this.buildingLocationIP = this.buildingGoalLocation;
/*      */         } else {
/* 2505 */           this.buildingLocationIP = goalProject.location;
/*      */         }
/*      */         
/* 2508 */         setBblocks(goalProject.getPlan(this.buildingGoalVariation, this.buildingGoalLevel).getBuildingPoints(this.worldObj, this.buildingLocationIP, false));
/*      */         
/* 2510 */         if (getBblocks().length == 0) {
/* 2511 */           MLN.error(this, "No bblocks for  " + this.buildingLocationIP);
/*      */         }
/*      */       }
/*      */       else {
/* 2515 */         this.buildingGoalIssue = "ui.lackingresources";
/*      */       }
/*      */     }
/* 2518 */     else if (goalProject.location == null) {
/* 2519 */       boolean canAffordProject = canAffordBuild(goalProject.getPlan(this.buildingGoalVariation, 0));
/* 2520 */       if ((System.currentTimeMillis() - this.lastFailedProjectLocationSearch > 80000L) && (canAffordProject))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2525 */         BuildingLocation location = goalProject.getPlan(this.buildingGoalVariation, 0).findBuildingLocation(this.winfo, this.pathing, this.location.pos, this.villageType.radius, MillCommonUtilities.getRandom(), -1);
/*      */         
/*      */ 
/* 2528 */         this.lastFailedProjectLocationSearch = System.currentTimeMillis();
/* 2529 */         if (location != null) {
/* 2530 */           this.lastFailedProjectLocationSearch = 0L;
/* 2531 */           this.buildingLocationIP = location;
/* 2532 */           this.buildingGoalLocation = location;
/* 2533 */           setBblocks(goalProject.getPlan(this.buildingGoalVariation, 0).getBuildingPoints(this.worldObj, this.buildingLocationIP, false));
/*      */           
/* 2535 */           if (MLN.LogBuildingPlan >= 1) {
/* 2536 */             MLN.major(this, "New project location: Loaded " + getBblocks().length + " building blocks for " + goalProject.getPlan(this.buildingGoalVariation, 0).planName);
/*      */           }
/*      */           
/* 2539 */           int groundLevel = MillCommonUtilities.findTopSoilBlock(this.worldObj, location.pos.getiX(), location.pos.getiZ());
/*      */           
/* 2541 */           for (int i = groundLevel + 1; i < location.pos.getiY(); i++) {
/* 2542 */             MillCommonUtilities.setBlockAndMetadata(this.worldObj, location.pos, Blocks.field_150346_d, 0);
/*      */           }
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
/* 2554 */           if (MLN.LogBuildingPlan >= 1) {
/* 2555 */             MLN.major(this, "Found location for building project: " + location);
/*      */           }
/*      */         } else {
/* 2558 */           this.buildingGoalIssue = "ui.nospace";
/* 2559 */           this.lastFailedProjectLocationSearch = System.currentTimeMillis();
/* 2560 */           if (MLN.LogBuildingPlan >= 1) {
/* 2561 */             MLN.major(this, "Searching for a location for the new project failed.");
/*      */           }
/*      */         }
/* 2564 */       } else if (!canAffordProject) {
/* 2565 */         this.buildingGoalIssue = "ui.lackingresources";
/* 2566 */         if (MLN.LogBuildingPlan >= 3) {
/* 2567 */           MLN.debug(this, "Cannot afford building project.");
/*      */         }
/*      */       } else {
/* 2570 */         this.buildingGoalIssue = "ui.nospace";
/*      */       }
/*      */     }
/*      */     
/* 2574 */     if (this.buildingLocationIP != null) {
/* 2575 */       return;
/*      */     }
/*      */     
/* 2578 */     boolean attemptedConstruction = false;
/*      */     
/* 2580 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 2581 */       if (this.buildingProjects.containsKey(ep)) {
/* 2582 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 2583 */         for (BuildingProject project : projectsLevel) {
/* 2584 */           if ((project.planSet != null) && ((goalProject == null) || (project != goalProject))) {
/* 2585 */             if ((project.location == null) || (project.location.level < 0)) {
/* 2586 */               BuildingPlan plan = project.planSet.getRandomStartingPlan();
/* 2587 */               if (isValidProject(project))
/*      */               {
/* 2589 */                 BuildingLocation location = null;
/* 2590 */                 if ((project.location == null) && (System.currentTimeMillis() - this.lastFailedOtherLocationSearch > 80000L) && (canAffordBuildAfterGoal(plan))) {
/* 2591 */                   location = plan.findBuildingLocation(this.winfo, this.pathing, this.location.pos, this.villageType.radius, MillCommonUtilities.getRandom(), -1);
/* 2592 */                 } else if ((project.location != null) && (canAffordBuildAfterGoal(plan))) {
/* 2593 */                   location = project.location.createLocationForLevel(0);
/*      */                 }
/*      */                 
/* 2596 */                 if (location != null) {
/* 2597 */                   this.lastFailedOtherLocationSearch = 0L;
/*      */                   
/* 2599 */                   this.buildingLocationIP = location;
/*      */                   
/* 2601 */                   setBblocks(plan.getBuildingPoints(this.worldObj, this.buildingLocationIP, false));
/*      */                   
/* 2603 */                   if (MLN.LogBuildingPlan >= 1) {
/* 2604 */                     MLN.major(this, "New location non-project: Loaded " + getBblocks().length + " building blocks for " + plan.planName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2627 */                   attemptedConstruction = true;
/*      */                 }
/*      */               }
/*      */             } else {
/* 2631 */               int level = project.location.level + 1;
/* 2632 */               int variation = project.location.getVariation();
/*      */               
/* 2634 */               if ((level < project.getLevelsNumber(variation)) && (project.location.upgradesAllowed) && (canAffordBuildAfterGoal(project.getPlan(variation, level)))) {
/* 2635 */                 this.buildingLocationIP = project.location.createLocationForLevel(level);
/* 2636 */                 setBblocks(project.getPlan(variation, level).getBuildingPoints(this.worldObj, this.buildingLocationIP, false));
/* 2637 */                 if (MLN.LogBuildingPlan >= 1) {
/* 2638 */                   MLN.major(this, "Upgrade non-project: Loaded " + getBblocks().length + " building blocks for " + project.getPlan(variation, level).planName + " upgrade. Old level: " + project.location.level + " New level: " + level);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2645 */           if (this.buildingLocationIP != null) {
/*      */             break;
/*      */           }
/*      */         }
/* 2649 */         if (this.buildingLocationIP != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 2654 */     if (attemptedConstruction) {
/* 2655 */       this.lastFailedOtherLocationSearch = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */   
/*      */   private void findBuildingProject()
/*      */   {
/* 2661 */     if ((this.buildingGoal != null) && (this.buildingGoal.length() > 0)) {
/* 2662 */       return;
/*      */     }
/*      */     
/* 2665 */     if (this.noProjectsLeft) {
/* 2666 */       return;
/*      */     }
/*      */     
/* 2669 */     this.buildingGoal = null;
/* 2670 */     this.buildingGoalLocation = null;
/*      */     
/* 2672 */     if (MLN.LogBuildingPlan >= 2) {
/* 2673 */       MLN.minor(this, "Searching for new building goal");
/*      */     }
/*      */     
/* 2676 */     List<BuildingProject> possibleProjects = new ArrayList();
/*      */     
/* 2678 */     boolean foundNewBuildingsLevel = false;
/*      */     
/* 2680 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 2681 */       if (this.buildingProjects.containsKey(ep)) {
/* 2682 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/*      */         
/* 2684 */         boolean includedNewBuildings = false;
/*      */         
/* 2686 */         for (BuildingProject project : projectsLevel)
/*      */         {
/* 2688 */           if (((project.location == null) || (project.location.level < 0)) && (!foundNewBuildingsLevel)) {
/* 2689 */             if (isValidProject(project)) {
/* 2690 */               possibleProjects.add(project);
/* 2691 */               includedNewBuildings = true;
/* 2692 */               if (MLN.LogBuildingPlan >= 3) {
/* 2693 */                 MLN.debug(this, "Found a new building to add: " + project);
/*      */               }
/* 2695 */               if ((MLN.LogBuildingPlan >= 2) && (project.getChoiceWeight(null) < 1)) {
/* 2696 */                 MLN.minor(this, "Project has null or negative weight: " + project + ": " + project.getChoiceWeight(null));
/*      */               }
/*      */             }
/* 2699 */           } else if ((project.location != null) && (project.location.level >= 0) && (project.location.level < project.getLevelsNumber(project.location.getVariation())) && (project.location.upgradesAllowed) && (project.getChoiceWeight(null) > 0))
/*      */           {
/* 2701 */             possibleProjects.add(project);
/*      */           }
/*      */         }
/*      */         
/* 2705 */         if (includedNewBuildings) {
/* 2706 */           foundNewBuildingsLevel = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2711 */     if (possibleProjects.size() == 0) {
/* 2712 */       this.noProjectsLeft = true;
/* 2713 */       return;
/*      */     }
/*      */     
/* 2716 */     BuildingProject project = BuildingProject.getRandomProject(possibleProjects);
/* 2717 */     BuildingPlan plan = project.getNextBuildingPlan();
/*      */     
/* 2719 */     this.buildingGoal = project.key;
/* 2720 */     this.buildingGoalLevel = plan.level;
/* 2721 */     this.buildingGoalVariation = plan.variation;
/* 2722 */     if (project.location == null) {
/* 2723 */       this.buildingGoalLocation = null;
/*      */     } else {
/* 2725 */       this.buildingGoalLocation = project.location.createLocationForLevel(this.buildingGoalLevel);
/*      */     }
/*      */     
/* 2728 */     if (MLN.LogBuildingPlan >= 1) {
/* 2729 */       MLN.major(this, "Picked new upgrade goal: " + this.buildingGoal + " level: " + this.buildingGoalLevel + " buildingGoalLocation: " + this.buildingGoalLocation);
/*      */     }
/*      */   }
/*      */   
/*      */   public void findName(String pname)
/*      */   {
/* 2735 */     if (pname != null) {
/* 2736 */       this.name = pname;
/*      */     } else {
/* 2738 */       if (this.villageType.nameList == null) {
/* 2739 */         this.name = null;
/* 2740 */         return;
/*      */       }
/* 2742 */       this.name = this.culture.getRandomNameFromList(this.villageType.nameList);
/*      */     }
/*      */     
/* 2745 */     List<String> qualifiers = new ArrayList();
/*      */     
/* 2747 */     for (String s : this.villageType.qualifiers) {
/* 2748 */       qualifiers.add(s);
/*      */     }
/*      */     
/* 2751 */     if ((this.villageType.hillQualifier != null) && (this.pos.getiY() > 75) && (this.pos.getiY() < 85)) {
/* 2752 */       qualifiers.add(this.villageType.hillQualifier);
/* 2753 */     } else if ((this.villageType.mountainQualifier != null) && (this.pos.getiY() >= 85)) {
/* 2754 */       qualifiers.add(this.villageType.mountainQualifier);
/*      */     }
/*      */     
/* 2757 */     if ((this.villageType.desertQualifier != null) || (this.villageType.forestQualifier != null) || (this.villageType.lavaQualifier != null) || (this.villageType.lakeQualifier != null) || (this.villageType.oceanQualifier != null)) {
/* 2758 */       int cactus = 0;int wood = 0;int lake = 0;int ocean = 0;int lava = 0;
/* 2759 */       for (int i = -50; i < 50; i++) {
/* 2760 */         for (int j = -10; j < 20; j++) {
/* 2761 */           for (int k = -50; k < 50; k++) {
/* 2762 */             Block block = this.worldObj.func_147439_a(i + this.pos.getiX(), j + this.pos.getiY(), k + this.pos.getiZ());
/* 2763 */             if (block == Blocks.field_150434_aF) {
/* 2764 */               cactus++;
/* 2765 */             } else if (block == Blocks.field_150364_r) {
/* 2766 */               wood++;
/* 2767 */             } else if (block == Blocks.field_150353_l) {
/* 2768 */               lava++;
/* 2769 */             } else if ((block == Blocks.field_150355_j) && 
/* 2770 */               (this.worldObj.func_147439_a(i + this.pos.getiX(), j + this.pos.getiY() + 1, k + this.pos.getiZ()) == Blocks.field_150350_a)) {
/* 2771 */               if (j + this.pos.getiY() < 65) {
/* 2772 */                 ocean++;
/*      */               } else {
/* 2774 */                 lake++;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2781 */       if ((this.villageType.desertQualifier != null) && (cactus > 0)) {
/* 2782 */         qualifiers.add(this.villageType.desertQualifier);
/*      */       }
/* 2784 */       if ((this.villageType.forestQualifier != null) && (wood > 40)) {
/* 2785 */         qualifiers.add(this.villageType.forestQualifier);
/*      */       }
/* 2787 */       if ((this.villageType.lavaQualifier != null) && (lava > 0)) {
/* 2788 */         qualifiers.add(this.villageType.lavaQualifier);
/*      */       }
/* 2790 */       if ((this.villageType.lakeQualifier != null) && (lake > 0)) {
/* 2791 */         qualifiers.add(this.villageType.lakeQualifier);
/*      */       }
/* 2793 */       if ((this.villageType.oceanQualifier != null) && (ocean > 0)) {
/* 2794 */         qualifiers.add(this.villageType.oceanQualifier);
/*      */       }
/*      */     }
/*      */     
/* 2798 */     if (qualifiers.size() > 0) {
/* 2799 */       this.qualifier = ((String)qualifiers.get(MillCommonUtilities.randomInt(qualifiers.size())));
/*      */     } else {
/* 2801 */       this.qualifier = "";
/*      */     }
/*      */   }
/*      */   
/*      */   public int getAltitude(int x, int z)
/*      */   {
/* 2807 */     if (this.winfo == null) {
/* 2808 */       return -1;
/*      */     }
/*      */     
/* 2811 */     if ((x < this.winfo.mapStartX) || (x >= this.winfo.mapStartX + this.winfo.length) || (z < this.winfo.mapStartZ) || (z >= this.winfo.mapStartZ + this.winfo.width)) {
/* 2812 */       return -1;
/*      */     }
/*      */     
/* 2815 */     return this.winfo.topGround[(x - this.winfo.mapStartX)][(z - this.winfo.mapStartZ)];
/*      */   }
/*      */   
/*      */   public BuildingBlock[] getBblocks() {
/* 2819 */     return this.bblocks;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Building getBuildingAtCoordPlanar(Point p)
/*      */   {
/* 2827 */     for (Building b : getBuildings()) {
/* 2828 */       if (b.location.isInsidePlanar(p)) {
/* 2829 */         return b;
/*      */       }
/*      */     }
/*      */     
/* 2833 */     return null;
/*      */   }
/*      */   
/*      */   public List<Building> getBuildings() {
/* 2837 */     List<Building> vbuildings = new ArrayList();
/*      */     
/* 2839 */     for (Point p : this.buildings) {
/* 2840 */       Building building = this.mw.getBuilding(p);
/* 2841 */       if ((building != null) && (building.location != null)) {
/* 2842 */         vbuildings.add(building);
/*      */       }
/*      */     }
/* 2845 */     return vbuildings;
/*      */   }
/*      */   
/*      */   public List<Building> getBuildingsWithTag(String s) {
/* 2849 */     List<Building> matches = new ArrayList();
/* 2850 */     for (Point p : this.buildings) {
/* 2851 */       Building building = this.mw.getBuilding(p);
/* 2852 */       if ((building != null) && (building.location != null) && (building.location.tags != null) && (building.location.tags.contains(s))) {
/* 2853 */         matches.add(building);
/*      */       }
/*      */     }
/* 2856 */     return matches;
/*      */   }
/*      */   
/*      */   public Set<Goods> getBuyingGoods(EntityPlayer player)
/*      */   {
/* 2861 */     if (!this.shopBuys.containsKey(player.getDisplayName())) {
/* 2862 */       return null;
/*      */     }
/*      */     
/* 2865 */     return ((LinkedHashMap)this.shopBuys.get(player.getDisplayName())).keySet();
/*      */   }
/*      */   
/*      */   public int getBuyingPrice(Goods g, EntityPlayer player) {
/* 2869 */     if (!this.shopBuys.containsKey(player.getDisplayName())) {
/* 2870 */       return 0;
/*      */     }
/*      */     
/* 2873 */     return ((Integer)((LinkedHashMap)this.shopBuys.get(player.getDisplayName())).get(g)).intValue();
/*      */   }
/*      */   
/*      */   public Point getClosestBlockAround(Point p, Block[] blocks, int hlimit, int vstart, int vend)
/*      */   {
/* 2878 */     if (this.pathing == null) {
/* 2879 */       return null;
/*      */     }
/*      */     
/* 2882 */     int cx = p.getiX();
/* 2883 */     int cz = p.getiZ();
/* 2884 */     int x = cx;int z = cz;
/* 2885 */     int dir = 3;int radius = 0;
/*      */     
/* 2887 */     vstart = p.getiY() + vstart;
/* 2888 */     vend = p.getiY() + vend;
/*      */     
/* 2890 */     while (radius < hlimit)
/*      */     {
/* 2892 */       if ((x > this.winfo.mapStartX) && (x < this.winfo.mapStartX + this.winfo.length) && (z > this.winfo.mapStartZ) && (z < this.winfo.mapStartZ + this.winfo.width)) {
/* 2893 */         for (int i = vend; i >= vstart; i--) {
/* 2894 */           Block block = this.worldObj.func_147439_a(x, i, z);
/* 2895 */           for (Block tblock : blocks) {
/* 2896 */             if (block == tblock) {
/* 2897 */               Point np = new Point(x, i, z);
/* 2898 */               if (this.pathing.isValidPoint(np)) {
/* 2899 */                 return np;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2905 */       if (dir == 0) {
/* 2906 */         if (x - cx < radius) {
/* 2907 */           x++;
/*      */         } else {
/* 2909 */           dir = 1;
/* 2910 */           z++;
/*      */         }
/* 2912 */       } else if (dir == 1) {
/* 2913 */         if (z - cz < radius) {
/* 2914 */           z++;
/*      */         } else {
/* 2916 */           dir = 2;
/* 2917 */           x--;
/*      */         }
/* 2919 */       } else if (dir == 2) {
/* 2920 */         if (cx - x < radius) {
/* 2921 */           x--;
/*      */         } else {
/* 2923 */           dir = 3;
/* 2924 */           z--;
/*      */         }
/*      */       }
/* 2927 */       else if (cz - z < radius) {
/* 2928 */         z--;
/*      */       } else {
/* 2930 */         dir = 0;
/* 2931 */         radius++;
/* 2932 */         x = cx - radius;
/* 2933 */         z = cz - radius;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2938 */     return null;
/*      */   }
/*      */   
/*      */   public BuildingBlock getCurrentBlock() {
/* 2942 */     if (this.bblocks == null) {
/* 2943 */       return null;
/*      */     }
/*      */     
/* 2946 */     if (this.bblocksPos >= this.bblocks.length) {
/* 2947 */       return null;
/*      */     }
/*      */     
/* 2950 */     return this.bblocks[this.bblocksPos];
/*      */   }
/*      */   
/*      */   public BuildingPlan getCurrentBuildingPlan() {
/* 2954 */     if (this.buildingLocationIP == null) {
/* 2955 */       return null;
/*      */     }
/*      */     
/* 2958 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 2959 */       if (this.buildingProjects.containsKey(ep)) {
/* 2960 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 2961 */         for (BuildingProject project : projectsLevel) {
/* 2962 */           if ((this.buildingLocationIP.level == 0) && ((project.location == null) || (project.location.level < 0)) && (project.key.equals(this.buildingLocationIP.planKey))) {
/* 2963 */             if (MLN.LogBuildingPlan >= 3) {
/* 2964 */               MLN.debug(this, "Returning building plan for " + this.buildingLocationIP + ": " + project.getPlan(this.buildingLocationIP.getVariation(), this.buildingLocationIP.level));
/*      */             }
/* 2966 */             return project.getPlan(this.buildingLocationIP.getVariation(), this.buildingLocationIP.level); }
/* 2967 */           if (this.buildingLocationIP.isSameLocation(project.location)) {
/* 2968 */             if (MLN.LogBuildingPlan >= 3) {
/* 2969 */               MLN.debug(this, "Returning building plan for " + this.buildingLocationIP + ": " + project.getPlan(this.buildingLocationIP.getVariation(), this.buildingLocationIP.level));
/*      */             }
/* 2971 */             return project.getPlan(this.buildingLocationIP.getVariation(), this.buildingLocationIP.level);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2977 */     MLN.error(this, "Could not find plan for current building location: " + this.buildingLocationIP);
/* 2978 */     return null;
/*      */   }
/*      */   
/*      */   public Point getCurrentClearPathPoint()
/*      */   {
/* 2983 */     if (this.oldPathPointsToClear == null) {
/* 2984 */       return null;
/*      */     }
/*      */     
/* 2987 */     if (this.oldPathPointsToClearIndex >= this.oldPathPointsToClear.size()) {
/* 2988 */       this.oldPathPointsToClear = null;
/* 2989 */       return null;
/*      */     }
/*      */     
/* 2992 */     return (Point)this.oldPathPointsToClear.get(this.oldPathPointsToClearIndex);
/*      */   }
/*      */   
/*      */   public BuildingPlan getCurrentGoalBuildingPlan() {
/* 2996 */     if (this.buildingGoal == null) {
/* 2997 */       return null;
/*      */     }
/*      */     
/* 3000 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 3001 */       if (this.buildingProjects.containsKey(ep)) {
/* 3002 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 3003 */         for (BuildingProject project : projectsLevel) {
/* 3004 */           if (project.key.equals(this.buildingGoal)) {
/* 3005 */             if (this.buildingGoalLocation == null) {
/* 3006 */               return project.getPlan(this.buildingGoalVariation, 0);
/*      */             }
/* 3008 */             return project.getPlan(this.buildingGoalVariation, this.buildingGoalLocation.level);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3014 */     return null;
/*      */   }
/*      */   
/*      */   public BuildingBlock getCurrentPathBuildingBlock()
/*      */   {
/* 3019 */     if (this.pathsToBuild == null) {
/* 3020 */       return null;
/*      */     }
/*      */     for (;;)
/*      */     {
/* 3024 */       if (this.pathsToBuildIndex >= this.pathsToBuild.size()) {
/* 3025 */         this.pathsToBuild = null;
/* 3026 */         return null; }
/* 3027 */       if (this.pathsToBuildPathIndex >= ((List)this.pathsToBuild.get(this.pathsToBuildIndex)).size()) {
/* 3028 */         this.pathsToBuildIndex += 1;
/* 3029 */         this.pathsToBuildPathIndex = 0;
/*      */       } else {
/* 3031 */         BuildingBlock b = (BuildingBlock)((List)this.pathsToBuild.get(this.pathsToBuildIndex)).get(this.pathsToBuildPathIndex);
/*      */         
/* 3033 */         Block block = b.p.getBlock(this.worldObj);
/* 3034 */         int meta = b.p.getMeta(this.worldObj);
/*      */         
/* 3036 */         if ((MillCommonUtilities.canPathBeBuiltHere(block, meta)) && ((block != b.block) || (meta != b.meta))) {
/* 3037 */           return b;
/*      */         }
/*      */         
/* 3040 */         this.pathsToBuildPathIndex += 1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Building getFirstBuildingWithTag(String s)
/*      */   {
/* 3047 */     for (Point p : this.buildings) {
/* 3048 */       Building building = this.mw.getBuilding(p);
/* 3049 */       if ((building != null) && (building.location != null) && (building.location.tags != null) && (building.location.tags.contains(s))) {
/* 3050 */         return building;
/*      */       }
/*      */     }
/* 3053 */     return null;
/*      */   }
/*      */   
/*      */   public List<BuildingProject> getFlatProjectList() {
/* 3057 */     List<BuildingProject> projects = new ArrayList();
/*      */     
/* 3059 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 3060 */       if (this.buildingProjects.containsKey(ep)) {
/* 3061 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 3062 */         for (BuildingProject project : projectsLevel) {
/* 3063 */           projects.add(project);
/*      */         }
/*      */       }
/*      */     }
/* 3067 */     return projects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getGameBuildingName()
/*      */   {
/* 3078 */     return this.location.getGameName();
/*      */   }
/*      */   
/*      */   public HashMap<Goods, Integer> getImportsNeededbyOtherVillages()
/*      */   {
/* 3083 */     if ((this.neededGoodsCached != null) && (System.currentTimeMillis() < this.neededGoodsLastGenerated + 60000L)) {
/* 3084 */       return this.neededGoodsCached;
/*      */     }
/*      */     
/* 3087 */     this.neededGoodsCached = new HashMap();
/*      */     
/* 3089 */     for (Point vp : this.mw.villagesList.pos) {
/* 3090 */       Chunk chunk = this.worldObj.func_72938_d(vp.getiX(), vp.getiZ());
/* 3091 */       if (chunk.field_76636_d) {
/* 3092 */         Building townHall = this.mw.getBuilding(vp);
/* 3093 */         if ((townHall != null) && (getTownHall() != null) && (townHall.villageType != getTownHall().villageType) && (townHall.culture == getTownHall().culture) && 
/* 3094 */           (townHall.getBuildingsWithTag("inn").size() > 0)) {
/* 3095 */           townHall.getNeededImportGoods(this.neededGoodsCached);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3101 */     this.neededGoodsLastGenerated = System.currentTimeMillis();
/*      */     
/* 3103 */     return this.neededGoodsCached;
/*      */   }
/*      */   
/*      */   public Set<Point> getKnownVillages() {
/* 3107 */     return this.relations.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BuildingLocation getLocationAtCoord(Point p)
/*      */   {
/* 3117 */     if ((this.buildingLocationIP != null) && (this.buildingLocationIP.isInside(p))) {
/* 3118 */       return this.buildingLocationIP;
/*      */     }
/*      */     
/* 3121 */     for (BuildingLocation bl : getLocations()) {
/* 3122 */       if (bl.isInside(p)) {
/* 3123 */         return bl;
/*      */       }
/*      */     }
/*      */     
/* 3127 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BuildingLocation getLocationAtCoordPlanar(Point p)
/*      */   {
/* 3137 */     if ((this.buildingLocationIP != null) && (this.buildingLocationIP.isInsidePlanar(p))) {
/* 3138 */       return this.buildingLocationIP;
/*      */     }
/*      */     
/* 3141 */     for (BuildingLocation bl : getLocations()) {
/* 3142 */       if (bl.isInsidePlanar(p)) {
/* 3143 */         return bl;
/*      */       }
/*      */     }
/*      */     
/* 3147 */     return null;
/*      */   }
/*      */   
/*      */   public List<BuildingLocation> getLocations()
/*      */   {
/* 3152 */     List<BuildingLocation> locations = new ArrayList();
/*      */     
/* 3154 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 3155 */       if (this.buildingProjects.containsKey(ep)) {
/* 3156 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 3157 */         for (BuildingProject project : projectsLevel) {
/* 3158 */           if (project.location != null) {
/* 3159 */             locations.add(project.location);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3165 */     return locations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNativeBuildingName()
/*      */   {
/* 3177 */     return this.location.getNativeName();
/*      */   }
/*      */   
/*      */   public int getNbProjects() {
/* 3181 */     int nb = 0;
/*      */     
/* 3183 */     for (List<BuildingProject> projects : this.buildingProjects.values()) {
/* 3184 */       nb += projects.size();
/*      */     }
/*      */     
/* 3187 */     return nb;
/*      */   }
/*      */   
/*      */   public void getNeededImportGoods(HashMap<Goods, Integer> neededGoods)
/*      */   {
/* 3192 */     for (Goods good : this.culture.goodsList) {
/* 3193 */       int nbneeded = nbGoodNeeded(good.item.getItem(), good.item.meta);
/* 3194 */       if (nbneeded > 0) {
/* 3195 */         if (MLN.LogMerchant >= 3) {
/* 3196 */           MLN.debug(this, "Import needed: " + good.getName() + " - " + nbneeded);
/*      */         }
/*      */         
/* 3199 */         if (neededGoods.containsKey(good)) {
/* 3200 */           neededGoods.put(good, Integer.valueOf(((Integer)neededGoods.get(good)).intValue() + nbneeded));
/*      */         } else {
/* 3202 */           neededGoods.put(good, Integer.valueOf(nbneeded));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public int getNewGender()
/*      */   {
/* 3210 */     int nbmales = 0;int nbfemales = 0;
/*      */     
/* 3212 */     for (VillagerRecord vr : this.vrecords) {
/* 3213 */       if (vr.gender == 1) {
/* 3214 */         nbmales++;
/*      */       } else {
/* 3216 */         nbfemales++;
/*      */       }
/*      */     }
/*      */     
/* 3220 */     int maleChance = 3 + nbfemales - nbmales;
/*      */     
/* 3222 */     return MillCommonUtilities.randomInt(6) < maleChance ? 1 : 2;
/*      */   }
/*      */   
/*      */   public Point getPos() {
/* 3226 */     return this.pos;
/*      */   }
/*      */   
/*      */   public Point getRandomLocationWithTag(String tag) {
/* 3230 */     List<Point> v = new ArrayList();
/*      */     
/* 3232 */     for (BuildingLocation l : getLocations()) {
/* 3233 */       if ((l.level >= 0) && (l.tags.contains(tag)) && (l.sleepingPos != null)) {
/* 3234 */         v.add(l.sleepingPos);
/*      */       }
/*      */     }
/*      */     
/* 3238 */     if (v.size() == 0) {
/* 3239 */       return null;
/*      */     }
/*      */     
/* 3242 */     return (Point)v.get(MillCommonUtilities.randomInt(v.size()));
/*      */   }
/*      */   
/*      */   public int getRelationWithVillage(Point p) {
/* 3246 */     if (this.relations.containsKey(p)) {
/* 3247 */       return ((Integer)this.relations.get(p)).intValue();
/*      */     }
/* 3249 */     return 0;
/*      */   }
/*      */   
/*      */   public int getReputation(String playerName) {
/* 3253 */     return this.mw.getProfile(playerName).getReputation(this);
/*      */   }
/*      */   
/*      */   public String getReputationLevelDesc(String playerName) {
/* 3257 */     return this.culture.getReputationLevelDesc(getReputation(playerName));
/*      */   }
/*      */   
/*      */   public String getReputationLevelLabel(String playerName) {
/* 3261 */     return this.culture.getReputationLevelLabel(getReputation(playerName));
/*      */   }
/*      */   
/*      */   public BuildingResManager getResManager() {
/* 3265 */     return this.resManager;
/*      */   }
/*      */   
/*      */   public Set<Goods> getSellingGoods(EntityPlayer player)
/*      */   {
/* 3270 */     if (!this.shopSells.containsKey(player.getDisplayName())) {
/* 3271 */       return null;
/*      */     }
/*      */     
/* 3274 */     return ((LinkedHashMap)this.shopSells.get(player.getDisplayName())).keySet();
/*      */   }
/*      */   
/*      */   public int getSellingPrice(Goods g, EntityPlayer player) {
/* 3278 */     if ((player == null) || (!this.shopSells.containsKey(player.getDisplayName()))) {
/* 3279 */       return 0;
/*      */     }
/*      */     
/* 3282 */     return ((Integer)((LinkedHashMap)this.shopSells.get(player.getDisplayName())).get(g)).intValue();
/*      */   }
/*      */   
/*      */   public Building getShop(String shop) {
/* 3286 */     for (BuildingLocation l : getLocations()) {
/* 3287 */       if ((l.level >= 0) && (shop.equals(l.shop))) {
/* 3288 */         return l.getBuilding(this.worldObj);
/*      */       }
/*      */     }
/*      */     
/* 3292 */     return null;
/*      */   }
/*      */   
/*      */   public Point getShopPos(String shop) {
/* 3296 */     for (BuildingLocation l : getLocations()) {
/* 3297 */       if ((l.level >= 0) && (shop.equals(l.shop))) {
/* 3298 */         return l.chestPos;
/*      */       }
/*      */     }
/*      */     
/* 3302 */     return null;
/*      */   }
/*      */   
/*      */   public List<Building> getShops() {
/* 3306 */     List<Building> shops = new ArrayList();
/*      */     
/* 3308 */     for (Point p : this.buildings) {
/* 3309 */       Building building = this.mw.getBuilding(p);
/* 3310 */       if ((building != null) && (building.location != null) && (building.location.shop != null) && (building.location.shop.length() > 0)) {
/* 3311 */         shops.add(building);
/*      */       }
/*      */     }
/* 3314 */     return shops;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private EnumSignType getSignType()
/*      */   {
/* 3321 */     if (this.isTownhall) {
/* 3322 */       if (this.location.showTownHallSigns)
/* 3323 */         return EnumSignType.TOWNHALL;
/* 3324 */       if ((this.location.maleResident.size() > 0) || (this.location.femaleResident.size() > 0)) {
/* 3325 */         return EnumSignType.HOUSE;
/*      */       }
/* 3327 */       return EnumSignType.DEFAULT;
/*      */     }
/*      */     
/* 3330 */     if (this.isMarket)
/* 3331 */       return EnumSignType.MARKET;
/* 3332 */     if (this.isInn)
/* 3333 */       return EnumSignType.INN;
/* 3334 */     if (this.location.tags.contains("archives"))
/* 3335 */       return EnumSignType.ARCHIVES;
/* 3336 */     if ((this.location.maleResident.size() > 0) || (this.location.femaleResident.size() > 0)) {
/* 3337 */       return EnumSignType.HOUSE;
/*      */     }
/* 3339 */     return EnumSignType.DEFAULT;
/*      */   }
/*      */   
/*      */ 
/*      */   public Building getTownHall()
/*      */   {
/* 3345 */     if (this.townHallPos == null) {
/* 3346 */       return null;
/*      */     }
/*      */     
/* 3349 */     return this.mw.getBuilding(this.townHallPos);
/*      */   }
/*      */   
/*      */   public int getVillageAttackerStrength() {
/* 3353 */     int strength = 0;
/*      */     
/* 3355 */     for (VillagerRecord vr : this.vrecords) {
/* 3356 */       if ((vr.raidingVillage) && (!vr.killed)) {
/* 3357 */         strength += vr.getMilitaryStrength();
/*      */       }
/*      */     }
/*      */     
/* 3361 */     return strength;
/*      */   }
/*      */   
/*      */   public int getVillageDefendingStrength() {
/* 3365 */     int strength = 0;
/*      */     
/* 3367 */     for (VillagerRecord vr : this.vrecords) {
/* 3368 */       if ((vr.getType() != null) && (vr.getType().helpInAttacks) && (!vr.killed) && (!vr.raidingVillage)) {
/* 3369 */         strength += vr.getMilitaryStrength();
/*      */       }
/*      */     }
/*      */     
/* 3373 */     return strength;
/*      */   }
/*      */   
/*      */   public int getVillageIrrigation()
/*      */   {
/* 3378 */     int irrigation = 0;
/*      */     
/* 3380 */     for (BuildingLocation bl : getLocations()) {
/* 3381 */       if (bl.getPlan() != null) {
/* 3382 */         irrigation += bl.getPlan().irrigation;
/*      */       }
/*      */     }
/*      */     
/* 3386 */     return irrigation;
/*      */   }
/*      */   
/*      */   public String getVillageNameWithoutQualifier() {
/* 3390 */     if ((this.name == null) || (this.name.length() == 0)) {
/* 3391 */       if (this.villageType != null) {
/* 3392 */         return this.villageType.name;
/*      */       }
/* 3394 */       return getNativeBuildingName();
/*      */     }
/*      */     
/*      */ 
/* 3398 */     return this.name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getVillageQualifiedName()
/*      */   {
/* 3406 */     if ((this.name == null) || (this.name.length() == 0)) {
/* 3407 */       if (this.villageType != null) {
/* 3408 */         return this.villageType.name;
/*      */       }
/* 3410 */       return getNativeBuildingName();
/*      */     }
/*      */     
/*      */ 
/* 3414 */     if ((this.qualifier == null) || (this.qualifier.length() == 0)) {
/* 3415 */       return this.name;
/*      */     }
/* 3417 */     return this.name + this.culture.qualifierSeparator + this.qualifier;
/*      */   }
/*      */   
/*      */   public int getVillageRaidingStrength()
/*      */   {
/* 3422 */     int strength = 0;
/*      */     
/* 3424 */     for (VillagerRecord vr : this.vrecords) {
/* 3425 */       if ((vr.getType() != null) && (vr.getType().isRaider) && (!vr.killed) && (!vr.raidingVillage)) {
/* 3426 */         strength += vr.getMilitaryStrength();
/*      */       }
/*      */     }
/*      */     
/* 3430 */     return strength;
/*      */   }
/*      */   
/*      */   public MillVillager getVillagerById(long id)
/*      */   {
/* 3435 */     for (MillVillager v : this.villagers) {
/* 3436 */       if (v.villager_id == id) {
/* 3437 */         return v;
/*      */       }
/*      */     }
/*      */     
/* 3441 */     return null;
/*      */   }
/*      */   
/*      */   public VillagerRecord getVillagerRecordById(long id)
/*      */   {
/* 3446 */     for (VillagerRecord vr : this.vrecords) {
/* 3447 */       if (vr.id == id) {
/* 3448 */         return vr;
/*      */       }
/*      */     }
/*      */     
/* 3452 */     return null;
/*      */   }
/*      */   
/*      */   public int getWoodCount() {
/* 3456 */     if (!this.location.tags.contains("grove")) {
/* 3457 */       return 0;
/*      */     }
/*      */     
/* 3460 */     int nb = 0;
/*      */     
/* 3462 */     for (int i = this.location.minx - 3; i < this.location.maxx + 3; i++) {
/* 3463 */       for (int j = this.location.pos.getiY() - 1; j < this.location.pos.getiY() + 10; j++) {
/* 3464 */         for (int k = this.location.minz - 3; k < this.location.maxz + 3; k++) {
/* 3465 */           if (this.worldObj.func_147439_a(i, j, k) == Blocks.field_150364_r) {
/* 3466 */             nb++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3471 */     return nb;
/*      */   }
/*      */   
/*      */   public Point getWoodLocation() {
/* 3475 */     if (!this.location.tags.contains("grove")) {
/* 3476 */       return null;
/*      */     }
/*      */     
/* 3479 */     for (int i = this.location.minx - 3; i < this.location.maxx + 3; i++) {
/* 3480 */       for (int j = this.location.pos.getiY() - 1; j < this.location.pos.getiY() + 10; j++) {
/* 3481 */         for (int k = this.location.minz - 3; k < this.location.maxz + 3; k++) {
/* 3482 */           if (this.worldObj.func_147439_a(i, j, k) == Blocks.field_150364_r) {
/* 3483 */             return new Point(i, j, k);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3488 */     return null;
/*      */   }
/*      */   
/*      */   public void growTree(World world, int i, int j, int k, Random random) {
/* 3492 */     int meta = world.func_72805_g(i, j, k) & 0x3;
/* 3493 */     MillCommonUtilities.setBlockAndMetadata(this.worldObj, i, j, k, Blocks.field_150350_a, 0, true, false);
/* 3494 */     WorldGenerator obj = null;
/* 3495 */     if (meta == 1) {
/* 3496 */       obj = new WorldGenTaiga2(true);
/* 3497 */     } else if (meta == 2) {
/* 3498 */       obj = new WorldGenForest(true, true);
/*      */     }
/* 3500 */     else if (meta == 3) {
/* 3501 */       obj = new WorldGenTrees(true, 4, 3, 3, false);
/*      */     } else {
/* 3503 */       obj = new WorldGenTrees(true);
/*      */     }
/* 3505 */     obj.func_76484_a(world, random, i, j, k);
/*      */   }
/*      */   
/*      */   public void incrementBblockPos() {
/* 3509 */     this.bblocksPos += 1;
/*      */     
/* 3511 */     if (!areBlocksLeft()) {
/* 3512 */       this.bblocks = null;
/* 3513 */       this.bblocksPos = 0;
/* 3514 */       this.bblocksChanged = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void initialise(EntityPlayer owner, boolean villageCreation) {
/* 3519 */     if (MLN.LogWorldGeneration >= 1) {
/* 3520 */       MLN.major(this, "Initialising building at " + getPos() + ", TH pos: " + this.townHallPos + ", TH: " + getTownHall());
/*      */     }
/*      */     
/* 3523 */     if (isHouse()) {
/*      */       try {
/* 3525 */         initialiseHouse(villageCreation);
/*      */       } catch (Exception e) {
/* 3527 */         MLN.printException("Error when trying to create a building: " + this.name, e);
/*      */       }
/* 3529 */       updateSigns();
/*      */     }
/*      */     
/* 3532 */     if (this.isTownhall) {
/* 3533 */       initialiseTownHall(owner);
/*      */     } else {
/* 3535 */       this.chestLocked = getTownHall().chestLocked;
/* 3536 */       if (!this.chestLocked) {
/* 3537 */         unlockChests();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void initialiseBuildingProjects() {
/* 3543 */     if (this.villageType == null) {
/* 3544 */       MLN.error(this, "villageType is null!");
/* 3545 */       return;
/*      */     }
/* 3547 */     this.buildingProjects = this.villageType.getBuildingProjects();
/*      */   }
/*      */   
/*      */   public void initialiseCurrentConstruction(Point refPos) throws MLN.MillenaireException {
/* 3551 */     boolean isTownHall = false;
/* 3552 */     if (this.buildingLocationIP.equals(this.location)) {
/* 3553 */       isTownHall = true;
/*      */     }
/*      */     
/*      */     Building building;
/*      */     Building building;
/* 3558 */     if (this.buildingLocationIP.level == 0) {
/* 3559 */       building = new Building(this.mw, this.culture, this.villageType, this.buildingLocationIP, isTownHall, false, refPos, getPos());
/*      */     } else {
/* 3561 */       building = this.mw.getBuilding(refPos);
/*      */     }
/*      */     
/* 3564 */     BuildingPlan plan = getCurrentBuildingPlan();
/*      */     
/* 3566 */     plan.referenceBuildingPoints(this.worldObj, building, this.buildingLocationIP);
/*      */     
/* 3568 */     if (this.buildingLocationIP.level == 0) {
/* 3569 */       building.initialise(null, false);
/* 3570 */       registerBuildingEntity(building);
/* 3571 */       if (MLN.LogBuildingPlan >= 1) {
/* 3572 */         MLN.major(this, "Created new Building Entity: " + plan.planName + " at " + refPos);
/*      */       }
/*      */     }
/*      */     
/* 3576 */     completeConstruction();
/*      */   }
/*      */   
/*      */   private void initialiseHouse(boolean villageCreation) throws MLN.MillenaireException {
/* 3580 */     if (villageCreation) {
/* 3581 */       createResidents();
/*      */     }
/*      */   }
/*      */   
/*      */   public void initialiseRelations(Point parentVillage)
/*      */   {
/* 3587 */     if (this.villageType.lonebuilding) {
/* 3588 */       return;
/*      */     }
/*      */     
/* 3591 */     this.parentVillage = parentVillage;
/*      */     
/* 3593 */     for (Point p : this.mw.villagesList.pos) {
/* 3594 */       if ((!this.pos.sameBlock(p)) && (this.pos.distanceToSquared(p) < MLN.BackgroundRadius * MLN.BackgroundRadius)) {
/* 3595 */         Building distantVillage = this.mw.getBuilding(p);
/* 3596 */         if (distantVillage != null)
/*      */         {
/* 3598 */           if ((parentVillage != null) && ((p.sameBlock(parentVillage)) || (parentVillage.sameBlock(distantVillage.parentVillage)))) {
/* 3599 */             adjustRelation(p, 100, true);
/*      */ 
/*      */           }
/* 3602 */           else if ((this.villageType.playerControlled) && (this.controlledBy.equals(distantVillage.controlledBy))) {
/* 3603 */             adjustRelation(p, 100, true);
/* 3604 */           } else if (distantVillage.culture == this.culture) {
/* 3605 */             adjustRelation(p, 50, true);
/*      */           } else {
/* 3607 */             adjustRelation(p, -30, true);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void initialiseTownHall(EntityPlayer controller)
/*      */   {
/* 3617 */     if (this.name == null) {
/* 3618 */       findName(null);
/*      */     }
/*      */     
/* 3621 */     if (MLN.LogWorldGeneration >= 1) {
/* 3622 */       MLN.major(this, "Initialising town hall: " + getVillageQualifiedName());
/*      */     }
/*      */     
/* 3625 */     this.buildings.add(getPos());
/*      */     
/* 3627 */     if ((this.villageType.playerControlled) && (controller != null)) {
/* 3628 */       UserProfile profile = this.mw.getProfile(controller.getDisplayName());
/* 3629 */       this.controlledBy = profile.key;
/* 3630 */       profile.adjustReputation(this, 131072);
/*      */     }
/*      */   }
/*      */   
/*      */   public void initialiseVillage()
/*      */   {
/* 3636 */     boolean noMenLeft = true;
/*      */     
/* 3638 */     for (int i = this.vrecords.size() - 1; i >= 0; i--) {
/* 3639 */       VillagerRecord vr = (VillagerRecord)this.vrecords.get(i);
/* 3640 */       if ((vr.gender == 1) && (!vr.getType().isChild)) {
/* 3641 */         noMenLeft = false;
/*      */       }
/*      */     }
/*      */     
/* 3645 */     for (Point p : this.buildings) {
/* 3646 */       Building b = this.mw.getBuilding(p);
/* 3647 */       if (b != null) {
/* 3648 */         if (noMenLeft) {
/* 3649 */           b.unlockChests();
/*      */         } else {
/* 3651 */           b.lockChests();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3656 */     recalculatePaths(true);
/*      */   }
/*      */   
/*      */   public boolean isDisplayableProject(BuildingProject project)
/*      */   {
/* 3661 */     if (project.getPlan(0, 0).requiredTag != null) {
/* 3662 */       if (!this.mw.isGlobalTagSet(project.getPlan(0, 0).requiredTag)) {
/* 3663 */         return false;
/*      */       }
/* 3665 */     } else if ((project.getPlan(0, 0).isgift) && (!MLN.bonusEnabled)) {
/* 3666 */       return false;
/*      */     }
/* 3668 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isHouse() {
/* 3672 */     return (this.location != null) && ((this.location.maleResident.size() > 0) || (this.location.femaleResident.size() > 0));
/*      */   }
/*      */   
/*      */   public boolean isPointProtectedFromPathBuilding(Point p) {
/* 3676 */     Point above = p.getAbove();Point below = p.getBelow();
/*      */     
/* 3678 */     for (Building b : getBuildings()) {
/* 3679 */       if ((b.location != null) && (b.location.isInsidePlanar(p)))
/*      */       {
/* 3681 */         if (b.location.tags.contains("nopaths")) {
/* 3682 */           return true;
/*      */         }
/*      */         
/* 3685 */         if (b.resManager.soils != null) {
/* 3686 */           for (List<Point> vpoints : b.resManager.soils) {
/* 3687 */             if ((vpoints.contains(p)) || (vpoints.contains(above)) || (vpoints.contains(below))) {
/* 3688 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 3693 */         if (b.resManager.sources != null) {
/* 3694 */           for (List<Point> vpoints : b.resManager.sources) {
/* 3695 */             if ((vpoints.contains(p)) || (vpoints.contains(above)) || (vpoints.contains(below))) {
/* 3696 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3703 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isReachableFromRegion(byte regionId)
/*      */   {
/* 3708 */     if (getTownHall().pathing == null) {
/* 3709 */       return true;
/*      */     }
/*      */     
/* 3712 */     if (getTownHall().pathing.regions[(this.resManager.getSleepingPos().getiX() - getTownHall().winfo.mapStartX)][(this.resManager.getSleepingPos().getiZ() - getTownHall().winfo.mapStartZ)] != regionId) {
/* 3713 */       return false;
/*      */     }
/*      */     
/* 3716 */     if (getTownHall().pathing.regions[(this.resManager.getSellingPos().getiX() - getTownHall().winfo.mapStartX)][(this.resManager.getSellingPos().getiZ() - getTownHall().winfo.mapStartZ)] != regionId) {
/* 3717 */       return false;
/*      */     }
/*      */     
/* 3720 */     if (getTownHall().pathing.regions[(this.resManager.getDefendingPos().getiX() - getTownHall().winfo.mapStartX)][(this.resManager.getDefendingPos().getiZ() - getTownHall().winfo.mapStartZ)] != regionId) {
/* 3721 */       return false;
/*      */     }
/*      */     
/* 3724 */     if (getTownHall().pathing.regions[(this.resManager.getShelterPos().getiX() - getTownHall().winfo.mapStartX)][(this.resManager.getShelterPos().getiZ() - getTownHall().winfo.mapStartZ)] != regionId) {
/* 3725 */       return false;
/*      */     }
/*      */     
/* 3728 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isValidProject(BuildingProject project)
/*      */   {
/* 3733 */     if ((!this.villageType.playerControlled) && ((project.getPlan(0, 0).price > 0) || (project.getPlan(0, 0).isgift)) && (!this.buildingsBought.contains(project.key))) {
/* 3734 */       return false;
/*      */     }
/*      */     
/* 3737 */     if ((project.getPlan(0, 0).requiredTag != null) && (!this.mw.isGlobalTagSet(project.getPlan(0, 0).requiredTag))) {
/* 3738 */       return false;
/*      */     }
/*      */     
/* 3741 */     return true;
/*      */   }
/*      */   
/*      */   private boolean isVillageChunksLoaded() {
/* 3745 */     for (int x = this.winfo.mapStartX; x < this.winfo.mapStartX + this.winfo.width; x += 16) {
/* 3746 */       for (int z = this.winfo.mapStartZ; z < this.winfo.mapStartZ + this.winfo.length; z += 16) {
/* 3747 */         if (!this.worldObj.func_72863_F().func_73149_a(x / 16, z / 16)) {
/* 3748 */           return false;
/*      */         }
/*      */         
/* 3751 */         if (!this.worldObj.func_72938_d(x, z).field_76636_d) {
/* 3752 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 3756 */     return true;
/*      */   }
/*      */   
/*      */   private void killMobs()
/*      */   {
/* 3761 */     if (this.winfo == null) {
/* 3762 */       return;
/*      */     }
/*      */     
/* 3765 */     for (Point buildingPos : this.buildings)
/*      */     {
/* 3767 */       Building b = this.mw.getBuilding(buildingPos);
/*      */       
/* 3769 */       if ((b != null) && (b.location != null))
/*      */       {
/* 3771 */         int radius = Math.max(b.location.length, b.location.width) / 2 + 8;
/*      */         
/* 3773 */         Point start = new Point(b.location.pos.x - radius, b.location.pos.getiY() - 10, b.location.pos.z - radius);
/* 3774 */         Point end = new Point(b.location.pos.x + radius, b.location.pos.getiY() + 30, b.location.pos.z + radius);
/*      */         
/* 3776 */         if (this.location.tags.contains("despawnallmobs")) {
/* 3777 */           List<Entity> mobs = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, EntityMob.class, start, end);
/*      */           
/* 3779 */           for (Entity ent : mobs) {
/* 3780 */             if (!ent.field_70128_L) {
/* 3781 */               if (MLN.LogTileEntityBuilding >= 3) {
/* 3782 */                 MLN.debug(this, "Killing mob " + ent + " at " + ent.field_70165_t + "/" + ent.field_70163_u + "/" + ent.field_70161_v);
/*      */               }
/* 3784 */               ent.func_70106_y();
/*      */             }
/*      */           }
/*      */         } else {
/* 3788 */           List<Entity> creepers = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, EntityCreeper.class, start, end);
/*      */           
/* 3790 */           for (Entity ent : creepers) {
/* 3791 */             if (!ent.field_70128_L) {
/* 3792 */               if (MLN.LogTileEntityBuilding >= 3) {
/* 3793 */                 MLN.debug(this, "Killing creeper " + ent + " at " + ent.field_70165_t + "/" + ent.field_70163_u + "/" + ent.field_70161_v);
/*      */               }
/* 3795 */               ent.func_70106_y();
/*      */             }
/*      */           }
/*      */           
/* 3799 */           List<Entity> endermen = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, EntityEnderman.class, start, end);
/*      */           
/* 3801 */           for (Entity ent : endermen) {
/* 3802 */             if (!ent.field_70128_L) {
/* 3803 */               if (MLN.LogTileEntityBuilding >= 3) {
/* 3804 */                 MLN.debug(this, "Killing enderman " + ent + " at " + ent.field_70165_t + "/" + ent.field_70163_u + "/" + ent.field_70161_v);
/*      */               }
/* 3806 */               ent.func_70106_y();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void loadChunks()
/*      */   {
/* 3816 */     if ((this.winfo != null) && (this.winfo.width > 0)) {
/* 3817 */       if (this.chunkLoader == null) {
/* 3818 */         this.chunkLoader = new BuildingChunkLoader(this);
/*      */       }
/* 3820 */       if (!this.chunkLoader.chunksLoaded) {
/* 3821 */         this.chunkLoader.loadChunks();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void lockAllBuildingsChests()
/*      */   {
/* 3828 */     for (Point p : this.buildings) {
/* 3829 */       Building b = this.mw.getBuilding(p);
/* 3830 */       if (b != null) {
/* 3831 */         b.lockChests();
/*      */       }
/*      */     }
/*      */     
/* 3835 */     this.saveNeeded = true;
/* 3836 */     this.saveReason = "Locking chests";
/*      */   }
/*      */   
/*      */   public void lockChests()
/*      */   {
/* 3841 */     this.chestLocked = true;
/* 3842 */     for (Point p : this.resManager.chests) {
/* 3843 */       TileEntityMillChest chest = p.getMillChest(this.worldObj);
/* 3844 */       if (chest != null) {
/* 3845 */         chest.buildingPos = getPos();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean lockedForPlayer(String playerName) {
/* 3851 */     if (!this.chestLocked) {
/* 3852 */       return false;
/*      */     }
/*      */     
/* 3855 */     return !controlledBy(playerName);
/*      */   }
/*      */   
/*      */   private void merchantCreated()
/*      */   {
/* 3860 */     if (MLN.LogMerchant >= 2) {
/* 3861 */       MLN.minor(this, "Creating a new merchant");
/*      */     }
/*      */     
/* 3864 */     this.merchantRecord = ((VillagerRecord)this.vrecords.get(this.vrecords.size() - 1));
/* 3865 */     this.visitorsList.add("panels.startedtrading;" + this.merchantRecord.getName() + ";" + this.merchantRecord.getNativeOccupationName());
/*      */   }
/*      */   
/*      */   private void moveMerchant(Building destInn)
/*      */   {
/* 3870 */     HashMap<InvItem, Integer> contents = this.resManager.getChestsContent();
/*      */     
/*      */ 
/* 3873 */     for (InvItem key : contents.keySet()) {
/* 3874 */       int nb = takeGoods(key.getItem(), key.meta, 9999999);
/* 3875 */       destInn.storeGoods(key.getItem(), key.meta, nb);
/* 3876 */       destInn.addToImports(key, nb);
/* 3877 */       addToExports(key, nb);
/*      */     }
/* 3879 */     transferVillager(this.merchantRecord, destInn, false);
/*      */     
/* 3881 */     this.visitorsList.add("panels.merchantmovedout;" + this.merchantRecord.getName() + ";" + this.merchantRecord.getNativeOccupationName() + ";" + destInn.getTownHall().getVillageQualifiedName() + ";" + this.nbNightsMerchant);
/*      */     
/*      */ 
/* 3884 */     destInn.visitorsList.add("panels.merchantarrived;" + this.merchantRecord.getName() + ";" + this.merchantRecord.getNativeOccupationName() + ";" + getTownHall().getVillageQualifiedName());
/*      */     
/* 3886 */     if (MLN.LogMerchant >= 1) {
/* 3887 */       MLN.major(this, "Moved merchant " + this.merchantRecord + " to " + destInn.getTownHall());
/*      */     }
/*      */     
/* 3890 */     destInn.merchantRecord = this.merchantRecord;
/* 3891 */     this.merchantRecord = null;
/*      */     
/* 3893 */     this.nbNightsMerchant = 0;
/*      */   }
/*      */   
/*      */   public int nbGoodAvailable(Block block, boolean forExport, boolean forShop) {
/*      */     try {
/* 3898 */       return nbGoodAvailable(new InvItem(block), forExport, forShop);
/*      */     } catch (MLN.MillenaireException e) {
/* 3900 */       MLN.printException(e); }
/* 3901 */     return 0;
/*      */   }
/*      */   
/*      */   public int nbGoodAvailable(Block block, int meta, boolean forExport, boolean forShop)
/*      */   {
/*      */     try {
/* 3907 */       return nbGoodAvailable(new InvItem(block, meta), forExport, forShop);
/*      */     } catch (MLN.MillenaireException e) {
/* 3909 */       MLN.printException(e); }
/* 3910 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int nbGoodAvailable(InvItem ii, boolean forExport, boolean forShop)
/*      */   {
/* 3919 */     if ((forShop) && 
/* 3920 */       (this.culture.shopNeeds.containsKey(this.location.shop))) {
/* 3921 */       for (InvItem item : (List)this.culture.shopNeeds.get(this.location.shop)) {
/* 3922 */         if (item.matches(ii)) {
/* 3923 */           return 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3929 */     int nb = countGoods(ii.getItem(), ii.meta);
/*      */     
/* 3931 */     if ((this.builder != null) && (this.buildingLocationIP != null) && (this.buildingLocationIP.planKey.equals(this.buildingGoal))) {
/* 3932 */       nb += this.builder.countInv(ii);
/*      */     }
/*      */     
/* 3935 */     if (nb == 0) {
/* 3936 */       return 0;
/*      */     }
/*      */     
/* 3939 */     int reserveAmount = 0;
/*      */     
/* 3941 */     boolean tradedHere = false;
/*      */     
/* 3943 */     if ((this.location.shop != null) && (this.culture.shopSells.containsKey(this.location.shop)))
/*      */     {
/* 3945 */       for (Goods g : (List)this.culture.shopSells.get(this.location.shop)) {
/* 3946 */         if (g.item.matches(ii)) {
/* 3947 */           tradedHere = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3953 */     if ((this.isTownhall) || (tradedHere) || (forExport)) {
/* 3954 */       if (ii.meta == -1) {
/* 3955 */         for (int i = 0; i < 16; i++) {
/*      */           try
/*      */           {
/* 3958 */             InvItem nitem = new InvItem(ii.item, i);
/*      */             
/* 3960 */             if (this.culture.goodsByItem.containsKey(nitem)) {
/* 3961 */               Goods good = (Goods)this.culture.goodsByItem.get(nitem);
/* 3962 */               if (good != null) {
/* 3963 */                 if (forExport) {
/* 3964 */                   reserveAmount = good.targetQuantity;
/*      */                 } else {
/* 3966 */                   reserveAmount = good.reservedQuantity;
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (MLN.MillenaireException e) {
/* 3971 */             MLN.printException(e);
/* 3972 */             return 0;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 3977 */       else if (this.culture.goodsByItem.containsKey(ii)) {
/* 3978 */         Goods good = (Goods)this.culture.goodsByItem.get(ii);
/* 3979 */         if (good != null) {
/* 3980 */           if (forExport) {
/* 3981 */             reserveAmount = good.targetQuantity;
/*      */           } else {
/* 3983 */             reserveAmount = good.reservedQuantity;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3990 */     for (Iterator i$ = this.vrecords.iterator(); i$.hasNext();) { vr = (VillagerRecord)i$.next();
/*      */       
/* 3992 */       if ((vr.housePos != null) && (vr.housePos.equals(getPos())) && (vr.getType() != null)) {
/* 3993 */         for (InvItem requiredItem : vr.getType().requiredFoodAndGoods.keySet()) {
/* 3994 */           if (ii.matches(requiredItem)) {
/* 3995 */             reserveAmount += ((Integer)vr.getType().requiredFoodAndGoods.get(requiredItem)).intValue();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     VillagerRecord vr;
/* 4002 */     if (MLN.LogMerchant >= 3) {
/* 4003 */       MLN.debug(this, "Reserved amount: " + ii.getName() + ": " + reserveAmount + "/" + nb);
/*      */     }
/*      */     
/* 4006 */     BuildingPlan project = getCurrentGoalBuildingPlan();
/*      */     
/* 4008 */     if (project != null) {
/* 4009 */       for (InvItem key : project.resCost.keySet()) {
/* 4010 */         if (key.matches(ii))
/*      */         {
/* 4012 */           if (MLN.LogMerchant >= 3) {
/* 4013 */             MLN.debug(this, "Needed for project: " + project.resCost.get(key));
/*      */           }
/*      */           
/* 4016 */           if (((Integer)project.resCost.get(key)).intValue() + reserveAmount >= nb) {
/* 4017 */             return 0;
/*      */           }
/* 4019 */           return nb - ((Integer)project.resCost.get(key)).intValue() - reserveAmount;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4025 */     if (reserveAmount < nb) {
/* 4026 */       return nb - reserveAmount;
/*      */     }
/*      */     
/* 4029 */     return 0;
/*      */   }
/*      */   
/*      */   public int nbGoodAvailable(Item item, boolean forExport, boolean forShop) {
/*      */     try {
/* 4034 */       return nbGoodAvailable(new InvItem(item), forExport, forShop);
/*      */     } catch (MLN.MillenaireException e) {
/* 4036 */       MLN.printException(e); }
/* 4037 */     return 0;
/*      */   }
/*      */   
/*      */   public int nbGoodAvailable(Item item, int meta, boolean forExport, boolean forShop)
/*      */   {
/*      */     try {
/* 4043 */       return nbGoodAvailable(new InvItem(item, meta), forExport, forShop);
/*      */     } catch (MLN.MillenaireException e) {
/* 4045 */       MLN.printException(e); }
/* 4046 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int nbGoodNeeded(Item item, int meta)
/*      */   {
/* 4052 */     int nb = countGoods(item, meta);
/*      */     
/* 4054 */     if ((this.builder != null) && (this.buildingLocationIP != null) && (this.buildingLocationIP.planKey.equals(this.buildingGoal))) {
/* 4055 */       nb += this.builder.countInv(item, meta);
/*      */     }
/*      */     
/* 4058 */     int targetAmount = 0;
/*      */     InvItem invitem;
/*      */     try {
/* 4061 */       invitem = new InvItem(item, meta);
/*      */       
/* 4063 */       if (meta == -1) {
/* 4064 */         for (int i = 0; i < 16; i++) {
/* 4065 */           if (this.culture.goodsByItem.containsKey(invitem)) {
/* 4066 */             Goods good = (Goods)this.culture.goodsByItem.get(new InvItem(item, i));
/* 4067 */             if (good != null) {
/* 4068 */               targetAmount += good.targetQuantity;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 4073 */       else if (this.culture.goodsByItem.containsKey(invitem)) {
/* 4074 */         Goods good = (Goods)this.culture.goodsByItem.get(invitem);
/* 4075 */         if (good != null) {
/* 4076 */           targetAmount = good.targetQuantity;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (MLN.MillenaireException e) {
/* 4081 */       MLN.printException(e);
/* 4082 */       return 0;
/*      */     }
/*      */     
/* 4085 */     BuildingPlan project = getCurrentGoalBuildingPlan();
/*      */     
/* 4087 */     int neededForProject = 0;
/*      */     
/* 4089 */     if (project != null) {
/* 4090 */       for (InvItem key : project.resCost.keySet()) {
/* 4091 */         if ((key.getItem() == item) && ((key.meta == meta) || (meta == -1) || (key.meta == -1))) {
/* 4092 */           neededForProject += ((Integer)project.resCost.get(key)).intValue();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4097 */     if (MLN.LogMerchant >= 3) {
/* 4098 */       MLN.debug(this, "Goods needed: " + invitem.getName() + ": " + targetAmount + "/" + neededForProject + "/" + nb);
/*      */     }
/*      */     
/* 4101 */     return Math.max(neededForProject + targetAmount - nb, 0);
/*      */   }
/*      */   
/*      */   public void planRaid(Building target) {
/* 4105 */     this.raidPlanningStart = this.worldObj.func_72820_D();
/* 4106 */     this.raidStart = 0L;
/* 4107 */     this.raidTarget = target.getPos();
/*      */     
/* 4109 */     if (MLN.LogDiplomacy >= 1) {
/* 4110 */       MLN.major(this, "raidTarget set: " + this.raidTarget + " name: " + target.name);
/*      */     }
/*      */     
/* 4113 */     this.saveNeeded = true;
/* 4114 */     this.saveReason = "Raid planned";
/*      */     
/* 4116 */     ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '4', "raid.planningstarted", new String[] { getVillageQualifiedName(), target.getVillageQualifiedName() });
/*      */   }
/*      */   
/*      */   private void readBblocks()
/*      */   {
/* 4121 */     File buildingsDir = MillCommonUtilities.getBuildingsDir(this.worldObj);
/* 4122 */     File file1 = new File(buildingsDir, getPos().getPathString() + "_bblocks.bin");
/*      */     
/* 4124 */     if (file1.exists()) {
/*      */       try {
/* 4126 */         FileInputStream fis = new FileInputStream(file1);
/* 4127 */         DataInputStream ds = new DataInputStream(fis);
/*      */         
/* 4129 */         int size = ds.readInt();
/*      */         
/* 4131 */         this.bblocks = new BuildingBlock[size];
/*      */         
/* 4133 */         for (int i = 0; i < size; i++) {
/* 4134 */           Point p = new Point(ds.readInt(), ds.readShort(), ds.readInt());
/* 4135 */           BuildingBlock b = new BuildingBlock(p, ds.readInt(), ds.readByte(), ds.readByte());
/* 4136 */           this.bblocks[i] = b;
/*      */         }
/*      */         
/* 4139 */         if (this.bblocks.length == 0) {
/* 4140 */           MLN.error(this, "Saved bblocks had zero elements. Rushing construction.");
/*      */           try {
/* 4142 */             rushBuilding();
/*      */           } catch (Exception e) {
/* 4144 */             MLN.printException("Exception when trying to rush building:", e);
/*      */           }
/*      */         }
/*      */         
/* 4148 */         ds.close();
/*      */       }
/*      */       catch (Exception e) {
/* 4151 */         MLN.printException("Error when reading bblocks: ", e);
/* 4152 */         this.bblocks = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean readFromNBT(NBTTagCompound nbttagcompound)
/*      */   {
/*      */     try {
/* 4160 */       String version = nbttagcompound.func_74779_i("versionCompatibility");
/*      */       
/* 4162 */       if (!version.equals("1.0")) {
/* 4163 */         MLN.error(this, "Tried to load building with incompatible version: " + version);
/* 4164 */         return false;
/*      */       }
/*      */       
/* 4167 */       if (this.pos == null) {
/* 4168 */         this.pos = Point.read(nbttagcompound, "pos");
/*      */       }
/*      */       
/* 4171 */       this.chestLocked = nbttagcompound.func_74767_n("chestLocked");
/*      */       
/* 4173 */       this.location = BuildingLocation.read(nbttagcompound, "buildingLocation", "self");
/*      */       
/* 4175 */       if (this.location == null) {
/* 4176 */         MLN.error(this, "No location found!");
/* 4177 */         return false;
/*      */       }
/*      */       
/* 4180 */       this.culture = Culture.getCultureByName(nbttagcompound.func_74779_i("culture"));
/*      */       
/* 4182 */       if (this.culture == null) {
/* 4183 */         MLN.error(this, "Could not load culture: " + nbttagcompound.func_74779_i("culture") + ", skipping building.");
/* 4184 */         return false;
/*      */       }
/*      */       
/* 4187 */       if (nbttagcompound.func_74764_b("isTownhall")) {
/* 4188 */         this.isTownhall = nbttagcompound.func_74767_n("isTownhall");
/*      */       } else {
/* 4190 */         this.isTownhall = this.location.planKey.equals("townHall");
/*      */       }
/*      */       
/* 4193 */       this.townHallPos = Point.read(nbttagcompound, "townHallPos");
/*      */       
/* 4195 */       this.thNightActionPerformed = nbttagcompound.func_74767_n("nightActionPerformed");
/* 4196 */       this.nightBackgroundActionPerformed = nbttagcompound.func_74767_n("nightBackgroundActionPerformed");
/*      */       
/* 4198 */       this.nbAnimalsRespawned = nbttagcompound.func_74762_e("nbAnimalsRespawned");
/*      */       
/* 4200 */       NBTTagList nbttaglist = nbttagcompound.func_150295_c("villagersrecords", 10);
/* 4201 */       for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4202 */         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4203 */         VillagerRecord vr = VillagerRecord.read(this.mw, this.culture, this.townHallPos, nbttagcompound1, "vr");
/*      */         
/* 4205 */         if (vr == null) {
/* 4206 */           MLN.error(this, "Couldn't load VR record.");
/*      */         } else {
/* 4208 */           addOrReplaceRecord(vr);
/* 4209 */           if (MLN.LogHybernation >= 2) {
/* 4210 */             MLN.minor(this, "Loaded VR: " + vr);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4215 */       nbttaglist = nbttagcompound.func_150295_c("visitorsList", 10);
/* 4216 */       for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4217 */         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4218 */         this.visitorsList.add(nbttagcompound1.func_74779_i("visitor"));
/*      */       }
/*      */       
/* 4221 */       nbttaglist = nbttagcompound.func_150295_c("subBuildings", 10);
/* 4222 */       for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4223 */         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4224 */         Point p = Point.read(nbttagcompound1, "pos");
/* 4225 */         if (p != null) {
/* 4226 */           this.subBuildings.add(p);
/*      */         }
/*      */       }
/*      */       
/* 4230 */       if ((this.location.tags.contains("pujas")) || (this.location.tags.contains("sacrifices"))) {
/* 4231 */         this.pujas = new PujaSacrifice(this, nbttagcompound.func_74775_l("pujas"));
/* 4232 */         if (MLN.LogPujas >= 2) {
/* 4233 */           MLN.minor(this, "read pujas object");
/*      */         }
/*      */       }
/*      */       
/* 4237 */       this.lastGoodsRefresh = nbttagcompound.func_74763_f("lastGoodsRefresh");
/*      */       
/* 4239 */       if ((this.location.tags.contains("inn")) && (!this.isTownhall)) {
/* 4240 */         this.isInn = true;
/* 4241 */         readInn(nbttagcompound);
/*      */       }
/*      */       
/* 4244 */       if ((this.isInn) && (this.vrecords.size() > 0)) {
/* 4245 */         this.merchantRecord = ((VillagerRecord)this.vrecords.get(0));
/*      */       }
/*      */       
/* 4248 */       if ((this.location.tags.contains("market")) && (!this.isTownhall)) {
/* 4249 */         this.isMarket = true;
/*      */       }
/*      */       
/* 4252 */       if (this.isTownhall) {
/* 4253 */         if (MLN.LogHybernation >= 1) {
/* 4254 */           MLN.major(this, "Loading Townhall data.");
/*      */         }
/* 4256 */         readTownHall(nbttagcompound);
/*      */       }
/*      */       
/* 4259 */       this.resManager.readFromNBT(nbttagcompound);
/*      */       
/* 4261 */       if (MLN.LogTileEntityBuilding >= 3) {
/* 4262 */         MLN.debug(this, "Loading building. Type: " + this.location + ", pos: " + getPos());
/*      */       }
/*      */       
/* 4265 */       return true;
/*      */     }
/*      */     catch (Exception e) {
/* 4268 */       Mill.proxy.sendChatAdmin("Error when trying to load building. Check millenaire.log.");
/* 4269 */       MLN.error(this, "Error when trying to load building of type: " + this.location);
/* 4270 */       MLN.printException(e);
/*      */     }
/* 4272 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readInn(NBTTagCompound nbttagcompound)
/*      */     throws MLN.MillenaireException
/*      */   {
/* 4281 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c("importedGoods", 10);
/* 4282 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4283 */       NBTTagCompound tag = nbttaglist.func_150305_b(i);
/* 4284 */       InvItem good = new InvItem(Item.func_150899_d(tag.func_74762_e("itemid")), tag.func_74762_e("itemmeta"));
/* 4285 */       this.imported.put(good, Integer.valueOf(tag.func_74762_e("quantity")));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4291 */     nbttaglist = nbttagcompound.func_150295_c("exportedGoods", 10);
/* 4292 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4293 */       NBTTagCompound tag = nbttaglist.func_150305_b(i);
/* 4294 */       InvItem good = new InvItem(Item.func_150899_d(tag.func_74762_e("itemid")), tag.func_74762_e("itemmeta"));
/* 4295 */       this.exported.put(good, Integer.valueOf(tag.func_74762_e("quantity")));
/*      */     }
/*      */     
/* 4298 */     nbttaglist = nbttagcompound.func_150295_c("importedGoodsNew", 10);
/* 4299 */     MillCommonUtilities.readInventory(nbttaglist, this.imported);
/*      */     
/* 4301 */     nbttaglist = nbttagcompound.func_150295_c("exportedGoodsNew", 10);
/* 4302 */     MillCommonUtilities.readInventory(nbttaglist, this.exported);
/*      */   }
/*      */   
/*      */   private void readPaths() {
/* 4306 */     File buildingsDir = MillCommonUtilities.getBuildingsDir(this.worldObj);
/* 4307 */     File file1 = new File(buildingsDir, getPos().getPathString() + "_paths.bin");
/*      */     
/* 4309 */     if (file1.exists()) {
/*      */       try {
/* 4311 */         FileInputStream fis = new FileInputStream(file1);
/* 4312 */         DataInputStream ds = new DataInputStream(fis);
/*      */         
/* 4314 */         int size = ds.readInt();
/*      */         
/* 4316 */         this.pathsToBuild = new ArrayList();
/*      */         
/* 4318 */         for (int i = 0; i < size; i++)
/*      */         {
/* 4320 */           List<BuildingBlock> path = new ArrayList();
/*      */           
/* 4322 */           int sizePath = ds.readInt();
/*      */           
/* 4324 */           for (int j = 0; j < sizePath; j++) {
/* 4325 */             Point p = new Point(ds.readInt(), ds.readShort(), ds.readInt());
/* 4326 */             BuildingBlock b = new BuildingBlock(p, ds.readInt(), ds.readByte(), ds.readByte());
/* 4327 */             path.add(b);
/*      */           }
/* 4329 */           this.pathsToBuild.add(path);
/*      */         }
/*      */         
/* 4332 */         ds.close();
/*      */       }
/*      */       catch (Exception e) {
/* 4335 */         MLN.printException("Error when reading pathsToBuild: ", e);
/* 4336 */         this.bblocks = null;
/*      */       }
/*      */     }
/*      */     
/* 4340 */     file1 = new File(buildingsDir, getPos().getPathString() + "_pathstoclear.bin");
/*      */     
/* 4342 */     if (file1.exists()) {
/*      */       try {
/* 4344 */         FileInputStream fis = new FileInputStream(file1);
/* 4345 */         DataInputStream ds = new DataInputStream(fis);
/*      */         
/* 4347 */         int size = ds.readInt();
/*      */         
/* 4349 */         this.oldPathPointsToClear = new ArrayList();
/*      */         
/* 4351 */         for (int i = 0; i < size; i++) {
/* 4352 */           Point p = new Point(ds.readInt(), ds.readShort(), ds.readInt());
/* 4353 */           this.oldPathPointsToClear.add(p);
/*      */         }
/*      */         
/* 4356 */         ds.close();
/*      */       }
/*      */       catch (Exception e) {
/* 4359 */         MLN.printException("Error when reading oldPathPointsToClear: ", e);
/* 4360 */         this.bblocks = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void readTownHall(NBTTagCompound nbttagcompound)
/*      */   {
/* 4368 */     this.name = nbttagcompound.func_74779_i("name");
/* 4369 */     this.qualifier = nbttagcompound.func_74779_i("qualifier");
/* 4370 */     String vtype = nbttagcompound.func_74779_i("villageType");
/*      */     
/* 4372 */     if (vtype.length() == 0) {
/* 4373 */       this.villageType = this.culture.getRandomVillage();
/* 4374 */     } else if (this.culture.getVillageType(vtype) != null) {
/* 4375 */       this.villageType = this.culture.getVillageType(vtype);
/* 4376 */     } else if (this.culture.getLoneBuildingType(vtype) != null) {
/* 4377 */       this.villageType = this.culture.getLoneBuildingType(vtype);
/*      */     } else {
/* 4379 */       this.villageType = this.culture.getRandomVillage();
/*      */     }
/*      */     
/* 4382 */     this.controlledBy = nbttagcompound.func_74779_i("controlledBy");
/*      */     
/*      */ 
/* 4385 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c("houses", 10);
/* 4386 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4387 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4388 */       Point p = Point.read(nbttagcompound1, "pos");
/* 4389 */       if (p != null) {
/* 4390 */         this.buildings.add(p);
/*      */       }
/*      */     }
/*      */     
/* 4394 */     nbttaglist = nbttagcompound.func_150295_c("buildings", 10);
/* 4395 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4396 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4397 */       Point p = Point.read(nbttagcompound1, "pos");
/* 4398 */       if (p != null) {
/* 4399 */         this.buildings.add(p);
/*      */       }
/*      */     }
/*      */     
/* 4403 */     initialiseBuildingProjects();
/*      */     
/* 4405 */     nbttaglist = nbttagcompound.func_150295_c("locations", 10);
/* 4406 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4407 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4408 */       BuildingLocation location = BuildingLocation.read(nbttagcompound1, "location", "locations");
/* 4409 */       if (location == null) {
/* 4410 */         MLN.error(this, "Could not load building location. Skipping.");
/*      */       } else {
/* 4412 */         fillinBuildingLocation(location);
/*      */       }
/*      */     }
/*      */     
/* 4416 */     for (int i = this.buildings.size() - 1; i >= 0; i--) {
/* 4417 */       boolean foundLocation = false;
/*      */       
/* 4419 */       for (BuildingLocation l : getLocations()) {
/* 4420 */         if (((Point)this.buildings.get(i)).equals(l.chestPos)) {
/* 4421 */           foundLocation = true;
/*      */         }
/*      */       }
/* 4424 */       if (!foundLocation) {
/* 4425 */         MLN.error(this, "Deleting building as could not find the location for it at: " + this.buildings.get(i));
/* 4426 */         this.buildings.remove(i);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     Iterator i$;
/*      */     
/*      */ 
/* 4435 */     if (this.villageType.playerControlled) {
/* 4436 */       for (i$ = this.buildingProjects.values().iterator(); i$.hasNext();) { level = (List)i$.next();
/* 4437 */         List<BuildingProject> toDelete = new ArrayList();
/*      */         
/* 4439 */         for (BuildingProject project : level) {
/* 4440 */           if (project.location == null) {
/* 4441 */             toDelete.add(project);
/*      */           }
/*      */         }
/*      */         
/* 4445 */         for (BuildingProject project : toDelete) {
/* 4446 */           level.remove(project);
/*      */         }
/*      */       }
/*      */     }
/*      */     List<BuildingProject> level;
/* 4451 */     this.buildingGoal = nbttagcompound.func_74779_i("buildingGoal");
/* 4452 */     if (this.culture.getBuildingPlanSet(this.buildingGoal) == null) {
/* 4453 */       this.buildingGoal = null;
/* 4454 */       this.buildingGoalLevel = 0;
/* 4455 */       this.buildingGoalVariation = 0;
/* 4456 */       if (MLN.LogHybernation >= 1) {
/* 4457 */         MLN.major(this, "No goal found: " + this.buildingGoal);
/*      */       }
/*      */     } else {
/* 4460 */       this.buildingGoalLevel = nbttagcompound.func_74762_e("buildingGoalLevel");
/* 4461 */       this.buildingGoalVariation = nbttagcompound.func_74762_e("buildingGoalVariation");
/* 4462 */       if (MLN.LogHybernation >= 1) {
/* 4463 */         MLN.major(this, "Reading building goal: " + this.buildingGoal);
/*      */       }
/*      */     }
/*      */     
/* 4467 */     this.buildingGoalLocation = BuildingLocation.read(nbttagcompound, "buildingGoalLocation", "buildingGoalLocation");
/* 4468 */     if ((this.buildingGoalLocation != null) && 
/* 4469 */       (MLN.LogHybernation >= 1)) {
/* 4470 */       MLN.major(this, "Loaded buildingGoalLocation: " + this.buildingGoalLocation);
/*      */     }
/*      */     
/* 4473 */     this.buildingGoalIssue = nbttagcompound.func_74779_i("buildingGoalIssue");
/*      */     
/* 4475 */     this.buildingLocationIP = BuildingLocation.read(nbttagcompound, "buildingLocationIP", "buildingLocationIP");
/*      */     
/* 4477 */     if (this.buildingLocationIP != null)
/*      */     {
/* 4479 */       if (this.culture.getBuildingPlanSet(this.buildingLocationIP.planKey) == null) {
/* 4480 */         this.buildingLocationIP = null;
/*      */       } else {
/* 4482 */         BuildingPlanSet set = this.culture.getBuildingPlanSet(this.buildingLocationIP.planKey);
/* 4483 */         if (this.buildingLocationIP.level >= ((BuildingPlan[])set.plans.get(this.buildingLocationIP.getVariation())).length) {
/* 4484 */           this.buildingLocationIP = null;
/*      */         }
/*      */       }
/*      */       
/* 4488 */       readBblocks();
/* 4489 */       this.bblocksPos = nbttagcompound.func_74762_e("bblocksPos");
/*      */     }
/*      */     
/* 4492 */     nbttaglist = nbttagcompound.func_150295_c("buildingsBought", 10);
/* 4493 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4494 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4495 */       this.buildingsBought.add(nbttagcompound1.func_74779_i("key"));
/*      */     }
/*      */     
/* 4498 */     this.parentVillage = Point.read(nbttagcompound, "parentVillage");
/*      */     
/* 4500 */     if (nbttagcompound.func_74764_b("relations")) {
/* 4501 */       nbttaglist = nbttagcompound.func_150295_c("relations", 10);
/* 4502 */       for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4503 */         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */         
/* 4505 */         this.relations.put(Point.read(nbttagcompound1, "pos"), Integer.valueOf(nbttagcompound1.func_74762_e("value")));
/*      */       }
/*      */     }
/*      */     
/* 4509 */     this.updateRaidPerformed = nbttagcompound.func_74767_n("updateRaidPerformed");
/* 4510 */     this.nightBackgroundActionPerformed = nbttagcompound.func_74767_n("nightBackgroundActionPerformed");
/* 4511 */     this.thNightActionPerformed = nbttagcompound.func_74767_n("nightActionPerformed");
/*      */     
/* 4513 */     this.raidTarget = Point.read(nbttagcompound, "raidTarget");
/* 4514 */     this.raidPlanningStart = nbttagcompound.func_74763_f("raidPlanningStart");
/* 4515 */     this.raidStart = nbttagcompound.func_74763_f("raidStart");
/* 4516 */     this.underAttack = nbttagcompound.func_74767_n("underAttack");
/*      */     
/* 4518 */     nbttaglist = nbttagcompound.func_150295_c("raidsPerformed", 10);
/* 4519 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4520 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4521 */       this.raidsPerformed.add(nbttagcompound1.func_74779_i("raid"));
/*      */     }
/*      */     
/* 4524 */     nbttaglist = nbttagcompound.func_150295_c("raidsTaken", 10);
/* 4525 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 4526 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 4527 */       this.raidsSuffered.add(nbttagcompound1.func_74779_i("raid"));
/*      */     }
/*      */     
/* 4530 */     this.pathsToBuildIndex = nbttagcompound.func_74762_e("pathsToBuildIndex");
/* 4531 */     this.pathsToBuildPathIndex = nbttagcompound.func_74762_e("pathsToBuildPathIndex");
/* 4532 */     this.oldPathPointsToClearIndex = nbttagcompound.func_74762_e("oldPathPointsToClearIndex");
/*      */     
/* 4534 */     readPaths();
/*      */   }
/*      */   
/*      */   public boolean rebuildPathing(boolean sync)
/*      */     throws MLN.MillenaireException
/*      */   {
/* 4540 */     if (MLN.jpsPathing) {
/* 4541 */       return false;
/*      */     }
/*      */     
/* 4544 */     if (sync) {
/* 4545 */       AStarPathing temp = new AStarPathing();
/*      */       
/* 4547 */       if (temp.createConnectionsTable(this.winfo, this.resManager.getSleepingPos())) {
/* 4548 */         this.pathing = temp;
/* 4549 */         this.lastPathingUpdate = System.currentTimeMillis();
/* 4550 */         return true;
/*      */       }
/* 4552 */       this.pathing = null;
/* 4553 */       this.lastPathingUpdate = System.currentTimeMillis();
/* 4554 */       return false;
/*      */     }
/* 4556 */     if (!this.rebuildingPathing)
/*      */     {
/*      */       try {
/* 4559 */         this.rebuildingPathing = true;
/* 4560 */         PathingThread thread = new PathingThread(this.winfo.clone());
/* 4561 */         thread.setPriority(1);
/* 4562 */         if (MLN.LogPathing >= 1) {
/* 4563 */           MLN.major(this, "Thread starting.");
/*      */         }
/* 4565 */         thread.start();
/* 4566 */         if (MLN.LogPathing >= 1) {
/* 4567 */           MLN.major(this, "Thread started.");
/*      */         }
/*      */       } catch (CloneNotSupportedException e) {
/* 4570 */         MLN.printException(e);
/*      */       }
/* 4572 */       return true;
/*      */     }
/* 4574 */     return true;
/*      */   }
/*      */   
/*      */   public void rebuildSurfacePathing() {
/* 4578 */     if (this.binaryPathing == null) {
/* 4579 */       this.binaryPathing = new PathingBinary(this.worldObj);
/*      */     }
/*      */     
/* 4582 */     this.binaryPathing.updatePathing(getPos(), this.villageType.radius + 20, 20);
/*      */     
/* 4584 */     this.lastPathingUpdate = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   public void recalculatePaths(boolean autobuild)
/*      */   {
/* 4589 */     if (!MLN.BuildVillagePaths) {
/* 4590 */       return;
/*      */     }
/*      */     
/* 4593 */     int nbPaths = 0;
/*      */     
/* 4595 */     for (Building b : getBuildings()) {
/* 4596 */       if ((b != this) && (b.location != null) && (b.location.getPlan() != null) && (!b.location.getPlan().isSubBuilding()) && (b.resManager.getPathStartPos() != null)) {
/* 4597 */         nbPaths++;
/*      */       }
/*      */     }
/*      */     
/* 4601 */     PathCreatorInfo info = new PathCreatorInfo(nbPaths);
/* 4602 */     this.autobuildPaths = autobuild;
/*      */     
/* 4604 */     Point start = this.resManager.getPathStartPos();
/*      */     
/* 4606 */     if (MLN.LogVillagePaths >= 2) {
/* 4607 */       MLN.minor(this, "Launching path rebuild, expected paths number: " + nbPaths);
/*      */     }
/*      */     
/* 4610 */     for (Building b : getBuildings()) {
/* 4611 */       if ((b != this) && (b.location != null) && (b.location.getPlan() != null) && (!b.location.getPlan().isSubBuilding()) && (b.resManager.getPathStartPos() != null))
/*      */       {
/* 4613 */         InvItem pathMaterial = (InvItem)this.villageType.pathMaterial.get(0);
/*      */         
/* 4615 */         if (b.location.getPlan().pathLevel < this.villageType.pathMaterial.size()) {
/* 4616 */           pathMaterial = (InvItem)this.villageType.pathMaterial.get(b.location.getPlan().pathLevel);
/*      */         }
/*      */         
/* 4619 */         PathCreator pathCreator = new PathCreator(info, pathMaterial, b.location.getPlan().pathWidth, b);
/*      */         
/* 4621 */         AStarPathPlanner jpsPathPlanner = new AStarPathPlanner(this.worldObj, pathCreator);
/*      */         
/* 4623 */         jpsPathPlanner.getPath(start.getiX(), start.getiY(), start.getiZ(), b.resManager.getPathStartPos().getiX(), b.resManager.getPathStartPos().getiY(), b.resManager.getPathStartPos().getiZ(), PATH_BUILDER_JPS_CONFIG);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void refreshGoods()
/*      */   {
/* 4631 */     if ((this.location == null) || (this.location.getPlan() == null) || (this.location.getPlan().startingGoods.size() == 0)) {
/* 4632 */       return;
/*      */     }
/*      */     
/* 4635 */     if (this.worldObj.func_72935_r()) {
/* 4636 */       this.refreshGoodsNightActionPerformed = false;
/*      */     }
/* 4638 */     else if (!this.refreshGoodsNightActionPerformed)
/*      */     {
/*      */       long interval;
/*      */       long interval;
/* 4642 */       if (this.chestLocked)
/*      */       {
/* 4644 */         interval = 20L;
/*      */       }
/*      */       else {
/* 4647 */         interval = 100L;
/*      */       }
/*      */       
/* 4650 */       if ((this.lastGoodsRefresh + interval * 24000L < this.worldObj.func_72820_D()) && (this.chestLocked)) {
/* 4651 */         fillStartingGoods();
/* 4652 */         this.lastGoodsRefresh = this.worldObj.func_72820_D();
/*      */       }
/*      */       
/* 4655 */       this.refreshGoodsNightActionPerformed = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void registerBuildingEntity(Building buildingEntity)
/*      */     throws MLN.MillenaireException
/*      */   {
/* 4662 */     if (buildingEntity != this) {
/* 4663 */       for (MillVillager v : buildingEntity.villagers)
/*      */       {
/* 4665 */         addOrReplaceVillager(v);
/* 4666 */         if (v.getRecord() != null) {
/* 4667 */           addOrReplaceRecord(v.getRecord());
/*      */         } else {
/* 4669 */           addOrReplaceRecord(new VillagerRecord(this.mw, v));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4674 */     this.buildings.add(buildingEntity.getPos());
/* 4675 */     this.saveNeeded = true;
/* 4676 */     this.saveReason = "Registering building";
/*      */   }
/*      */   
/*      */   public void registerBuildingLocation(BuildingLocation location)
/*      */   {
/* 4681 */     boolean registered = false;
/* 4682 */     int nbProjects = 0;
/*      */     
/*      */ 
/* 4685 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 4686 */       if (this.buildingProjects.containsKey(ep)) {
/* 4687 */         List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 4688 */         for (BuildingProject project : projectsLevel) {
/* 4689 */           if (location.level == 0) {
/* 4690 */             if ((project.key.equals(location.planKey)) && ((project.location == null) || ((project.location.level < 0) && (project.location.isSameLocation(location))))) {
/* 4691 */               if (project.location != null) {
/* 4692 */                 location.upgradesAllowed = project.location.upgradesAllowed;
/*      */               }
/* 4694 */               project.location = location;
/* 4695 */               registered = true;
/* 4696 */               if (MLN.LogBuildingPlan >= 1) {
/* 4697 */                 MLN.major(this, "Updated building project: " + project + " with initial location.");
/*      */               }
/*      */             }
/*      */           }
/* 4701 */           else if (location.isSameLocation(project.location)) {
/* 4702 */             if (MLN.LogBuildingPlan >= 1) {
/* 4703 */               MLN.major(this, "Updated building project: " + project + " from level " + project.location.level + " to " + location.level);
/*      */             }
/*      */             
/* 4706 */             location.upgradesAllowed = project.location.upgradesAllowed;
/* 4707 */             project.location = location;
/* 4708 */             registered = true;
/*      */           }
/*      */           
/* 4711 */           nbProjects++;
/*      */           
/* 4713 */           if (registered) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/* 4718 */       if (registered) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 4723 */     if (registered) {
/* 4724 */       if (MLN.LogBuildingPlan >= 1) {
/* 4725 */         MLN.major(this, "Registered building location: " + location);
/*      */       }
/*      */     } else {
/* 4728 */       MLN.error(this, "Could not register building location: " + location + " amoung " + nbProjects + " projects.");
/*      */     }
/*      */     
/*      */ 
/* 4732 */     if (getCurrentBuildingPlan() != null) {
/* 4733 */       for (Point p : this.buildings) {
/* 4734 */         Building building = this.mw.getBuilding(p);
/* 4735 */         if ((building != null) && (building.location != null) && (building.location.isSameLocation(location))) {
/* 4736 */           building.location = location;
/* 4737 */           getCurrentBuildingPlan().referenceBuildingPoints(this.worldObj, building, location);
/* 4738 */           if (MLN.LogBuildingPlan >= 1) {
/* 4739 */             MLN.major(this, "Updated building location for building: " + building + " now at upgrade: " + location.level);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4746 */     for (String s : location.subBuildings) {
/* 4747 */       boolean found = false;
/* 4748 */       List<BuildingProject> parentProjectLevel = null;
/* 4749 */       int parentPos = 0;
/*      */       List<BuildingProject> projectsLevel;
/* 4751 */       int pos; for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 4752 */         if (this.buildingProjects.containsKey(ep)) {
/* 4753 */           projectsLevel = (List)this.buildingProjects.get(ep);
/* 4754 */           pos = 0;
/* 4755 */           for (BuildingProject project : projectsLevel) {
/* 4756 */             if (project.location != null) {
/* 4757 */               if ((project.location.isLocationSamePlace(location)) && (project.key.equals(s))) {
/* 4758 */                 found = true;
/* 4759 */               } else if (project.location.isSameLocation(location)) {
/* 4760 */                 parentProjectLevel = projectsLevel;
/* 4761 */                 parentPos = pos;
/*      */               }
/*      */             }
/* 4764 */             pos++;
/*      */           }
/*      */         }
/*      */       }
/* 4768 */       if ((!found) && (parentProjectLevel != null))
/*      */       {
/* 4770 */         if (this.culture.getBuildingPlanSet(s) == null) {
/* 4771 */           MLN.error(this, "Could not find plan for finished building: " + s);
/* 4772 */           return;
/*      */         }
/*      */         
/* 4775 */         BuildingProject project = new BuildingProject(this.culture.getBuildingPlanSet(s));
/* 4776 */         project.location = location.createLocationForSubBuilding(s);
/* 4777 */         parentProjectLevel.add(parentPos + 1, project);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4782 */     this.saveNeeded = true;
/* 4783 */     this.saveReason = "Registering location";
/*      */   }
/*      */   
/*      */   public void registerVillager(MillVillager villager) throws MLN.MillenaireException
/*      */   {
/* 4788 */     if (MLN.LogGeneralAI >= 3) {
/* 4789 */       MLN.debug(this, "Registering villager " + villager);
/*      */     }
/*      */     
/* 4792 */     addOrReplaceVillager(villager);
/*      */     
/* 4794 */     int nbFound = 0;
/* 4795 */     VillagerRecord vrfound = null;
/*      */     
/* 4797 */     for (int i = 0; i < this.vrecords.size(); i++) {
/* 4798 */       VillagerRecord vr = (VillagerRecord)this.vrecords.get(i);
/* 4799 */       if (vr.id == villager.villager_id) {
/* 4800 */         nbFound++;
/* 4801 */         vrfound = vr;
/*      */       }
/*      */     }
/*      */     
/* 4805 */     if (vrfound != null) {
/* 4806 */       vrfound.updateRecord(villager);
/*      */     }
/*      */     
/* 4809 */     if (nbFound == 0) {
/* 4810 */       VillagerRecord vr = villager.getRecord();
/*      */       
/*      */ 
/* 4813 */       if (vr != null) {
/* 4814 */         if (MLN.LogGeneralAI >= 1) {
/* 4815 */           MLN.major(this, "Adding record " + vr);
/*      */         }
/* 4817 */         addOrReplaceRecord(vr);
/*      */       } else {
/* 4819 */         if (MLN.LogGeneralAI >= 1) {
/* 4820 */           MLN.major(this, "Adding new record " + new VillagerRecord(this.mw, villager));
/*      */         }
/* 4822 */         addOrReplaceRecord(new VillagerRecord(this.mw, villager));
/*      */       }
/* 4824 */       this.saveNeeded = true;
/* 4825 */       this.saveReason = "Registering villager";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean removeVillagerRecord(long vid)
/*      */   {
/* 4832 */     for (VillagerRecord vr : this.vrecords) {
/* 4833 */       if (vr.id == vid) {
/* 4834 */         this.vrecords.remove(vr);
/* 4835 */         return true;
/*      */       }
/*      */     }
/* 4838 */     return false;
/*      */   }
/*      */   
/*      */   private void repairVillagerList()
/*      */     throws MLN.MillenaireException
/*      */   {
/* 4844 */     List entities = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, MillVillager.class, getPos(), this.villageType.radius + 20, 50);
/*      */     
/* 4846 */     for (Object o : entities) {
/* 4847 */       MillVillager v = (MillVillager)o;
/*      */       
/* 4849 */       if ((v.getTownHall() == this) && 
/* 4850 */         (!this.villagers.contains(v)) && (v.vtype != null)) {
/* 4851 */         this.villagers.add(v);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4856 */     for (int i = this.villagers.size() - 1; i > -1; i--) {
/* 4857 */       if (((MillVillager)this.villagers.get(i)).field_70128_L) {
/* 4858 */         this.villagers.remove(i);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4864 */     int time = (int)(this.worldObj.func_72820_D() % 24000L);
/*      */     
/* 4866 */     boolean resurect = (time >= 13000) && (time < 13100);
/*      */     
/* 4868 */     for (VillagerRecord vr : this.vrecords) {
/* 4869 */       List<MillVillager> found = new ArrayList();
/* 4870 */       for (MillVillager v : this.villagers) {
/* 4871 */         if (vr.matches(v)) {
/* 4872 */           found.add(v);
/*      */         }
/*      */       }
/*      */       
/* 4876 */       if (found.size() == 0) {
/* 4877 */         boolean respawn = false;
/*      */         
/* 4879 */         if (!vr.flawedRecord) {
/* 4880 */           if (vr.raidingVillage)
/*      */           {
/*      */ 
/*      */ 
/* 4884 */             if ((!vr.killed) && (this.worldObj.func_72820_D() > vr.raiderSpawn + 500L)) {
/* 4885 */               respawn = true;
/*      */             }
/* 4887 */           } else if ((!vr.awayraiding) && (!vr.awayhired) && (!vr.getType().noResurrect))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 4892 */             if ((!vr.killed) || (resurect)) {
/* 4893 */               respawn = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 4898 */         if (respawn) {
/* 4899 */           if (MLN.LogGeneralAI >= 1) {
/* 4900 */             MLN.major(this, "Recreating missing villager from record " + vr + ". Killed: " + vr.killed);
/*      */           }
/* 4902 */           if (this.mw.getBuilding(vr.housePos) == null) {
/* 4903 */             MLN.error(this, "Error when trying to recreate a villager from record " + vr + ": couldn't load house at " + vr.housePos + ".");
/*      */           }
/*      */           else
/*      */           {
/*      */             Point villagerPos;
/*      */             Point villagerPos;
/* 4909 */             if ((vr.raidingVillage) && (vr.originalVillagePos != null)) {
/* 4910 */               villagerPos = findAttackerSpawnPoint(vr.originalVillagePos);
/*      */             } else { Point villagerPos;
/* 4912 */               if (this.underAttack) { Point villagerPos;
/* 4913 */                 if (vr.getType().helpInAttacks) {
/* 4914 */                   villagerPos = this.resManager.getDefendingPos();
/*      */                 } else {
/* 4916 */                   villagerPos = this.resManager.getShelterPos();
/*      */                 }
/*      */               } else {
/* 4919 */                 villagerPos = this.mw.getBuilding(vr.housePos).resManager.getSleepingPos();
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 4924 */             MillVillager villager = MillVillager.createVillager(vr.culture, vr.type, vr.gender, this.worldObj, villagerPos, vr.housePos, getPos(), true, vr.firstName, vr.familyName);
/*      */             
/* 4926 */             if (villager == null) {
/* 4927 */               MLN.error(this, "Could not recreate villager " + vr + " of type " + vr.type);
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/* 4936 */               vr.nameKey = villager.getNameKey();
/*      */               
/* 4938 */               if (!vr.killed)
/*      */               {
/* 4940 */                 if (MLN.LogGeneralAI >= 1) {
/* 4941 */                   MLN.major(this, "Giving the villager back " + vr.inventory.size() + " item types.");
/*      */                 }
/*      */                 
/* 4944 */                 for (InvItem iv : vr.inventory.keySet()) {
/* 4945 */                   villager.addToInv(iv, ((Integer)vr.inventory.get(iv)).intValue());
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 4950 */               vr.killed = false;
/*      */               
/* 4952 */               if (villager.getHouse() != null) {
/* 4953 */                 villager.setTexture(vr.texture);
/* 4954 */                 villager.villager_id = vr.id;
/* 4955 */                 villager.size = vr.villagerSize;
/* 4956 */                 villager.isRaider = vr.raidingVillage;
/*      */                 
/* 4958 */                 if (!villager.isTextureValid(villager.getTexture().func_110623_a())) {
/* 4959 */                   villager.setTexture(villager.getNewTexture());
/*      */                 }
/*      */                 
/* 4962 */                 if (villager.func_70631_g_()) {
/* 4963 */                   villager.adjustSize();
/*      */                 }
/*      */                 
/* 4966 */                 boolean spawned = this.worldObj.func_72838_d(villager);
/*      */                 
/* 4968 */                 if (spawned) {
/* 4969 */                   registerVillager(villager);
/* 4970 */                   villager.getHouse().registerVillager(villager);
/* 4971 */                   villager.registerInGlobalList();
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 4978 */       else if (found.size() > 1) {
/* 4979 */         if (MLN.LogGeneralAI >= 1) {
/* 4980 */           MLN.major(this, "Found " + found.size() + " villagers for record " + vr + ", killing the extras.");
/*      */         }
/*      */         
/* 4983 */         for (int i = found.size() - 1; i > 0; i--) {
/* 4984 */           MillVillager v = (MillVillager)found.get(i);
/* 4985 */           this.villagers.remove(i);
/* 4986 */           v.getHouse().villagers.remove(v);
/* 4987 */           v.despawnVillager();
/*      */         }
/* 4989 */       } else if ((found.size() == 1) && (
/* 4990 */         (vr.housePos == null) || (vr.texture == null) || (vr.nameKey == null) || (vr.nameKey.length() == 0) || (vr.nameKey.equals("villager")))) {
/* 4991 */         MLN.major(this, "Updating record for villager: " + found.get(0));
/* 4992 */         vr.updateRecord((MillVillager)found.get(0));
/* 4993 */         vr.flawedRecord = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void repairVillagerListClient()
/*      */   {
/* 5000 */     for (int i = this.villagers.size() - 1; i > -1; i--) {
/* 5001 */       if (((MillVillager)this.villagers.get(i)).field_70128_L) {
/* 5002 */         if (MLN.LogGeneralAI >= 1) {
/* 5003 */           MLN.major(this, "Client detected a dead villager in the list, removing it: " + this.villagers.get(i));
/*      */         }
/* 5005 */         this.villagers.remove(i);
/*      */       }
/*      */     }
/*      */     
/* 5009 */     for (VillagerRecord vr : this.vrecords) {
/* 5010 */       List<MillVillager> found = new ArrayList();
/* 5011 */       for (MillVillager v : this.villagers) {
/* 5012 */         if (vr.matches(v)) {
/* 5013 */           found.add(v);
/*      */         }
/*      */       }
/*      */       
/* 5017 */       if (found.size() > 1) {
/* 5018 */         if (MLN.LogGeneralAI >= 1) {
/* 5019 */           MLN.major(this, "Client found " + found.size() + " villagers for record " + vr + ", killing the extras.");
/*      */         }
/*      */         
/* 5022 */         long mostrecent = 0L;
/* 5023 */         int mostrecentId = 0;
/*      */         
/* 5025 */         for (int i = 0; i < found.size(); i++)
/*      */         {
/* 5027 */           if (((MillVillager)found.get(i)).client_lastupdated >= mostrecent) {
/* 5028 */             mostrecent = ((MillVillager)found.get(i)).client_lastupdated;
/* 5029 */             mostrecentId = i;
/*      */           }
/*      */         }
/*      */         
/* 5033 */         for (int i = found.size(); i > 0; i--) {
/* 5034 */           MillVillager v = (MillVillager)found.get(i);
/*      */           
/* 5036 */           if (i != mostrecentId)
/*      */           {
/* 5038 */             this.villagers.remove(i);
/* 5039 */             v.getHouse().villagers.remove(v);
/* 5040 */             v.despawnVillager();
/*      */           }
/*      */         }
/*      */       }
/* 5044 */       else if ((found.size() == 1) && (
/* 5045 */         (vr.housePos == null) || (vr.texture == null) || (vr.nameKey == null) || (vr.nameKey.length() == 0) || (vr.nameKey.equals("villager")))) {
/* 5046 */         MLN.major(this, "Updating record for villager: " + found.get(0));
/* 5047 */         vr.updateRecord((MillVillager)found.get(0));
/* 5048 */         vr.flawedRecord = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void requestSave(String reason)
/*      */   {
/* 5055 */     this.saveNeeded = true;
/* 5056 */     this.saveReason = reason;
/*      */   }
/*      */   
/*      */   public void rushBuilding() throws MLN.MillenaireException
/*      */   {
/* 5061 */     if (this.buildingLocationIP != null) {
/* 5062 */       BuildingPlan plan = getCurrentBuildingPlan();
/*      */       
/*      */       List<BuildingPlan.LocationBuildingPair> lbps;
/*      */       List<BuildingPlan.LocationBuildingPair> lbps;
/* 5066 */       if (this.buildingLocationIP.isSameLocation(this.location)) {
/* 5067 */         lbps = plan.build(this.mw, this.villageType, this.buildingLocationIP, false, true, this.pos, false, null, true);
/*      */       } else {
/* 5069 */         lbps = plan.build(this.mw, this.villageType, this.buildingLocationIP, false, false, this.pos, false, null, true);
/*      */       }
/*      */       
/* 5072 */       for (BuildingPlan.LocationBuildingPair b : lbps) {
/* 5073 */         registerBuildingEntity(b.building);
/*      */       }
/*      */       
/* 5076 */       setBblocks(null);
/*      */     }
/* 5078 */     completeConstruction();
/*      */   }
/*      */   
/*      */   public void saveTownHall(String reason)
/*      */   {
/* 5083 */     if ((!this.worldObj.field_72995_K) && 
/* 5084 */       (this.saveWorker == null)) {
/* 5085 */       this.saveWorker = new SaveWorker(reason, null);
/* 5086 */       this.saveWorker.start();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void sendBuildingPacket(EntityPlayer player, boolean sendChest)
/*      */   {
/* 5093 */     if (this.worldObj.field_72995_K) {
/* 5094 */       return;
/*      */     }
/*      */     
/* 5097 */     if (sendChest) {
/* 5098 */       for (Point p : this.resManager.chests) {
/* 5099 */         TileEntityMillChest chest = p.getMillChest(this.worldObj);
/*      */         
/* 5101 */         if (chest != null) {
/* 5102 */           chest.buildingPos = getPos();
/* 5103 */           chest.sendUpdatePacket(player);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5108 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*      */     
/*      */     try
/*      */     {
/* 5112 */       data.write(2);
/*      */       
/* 5114 */       StreamReadWrite.writeNullablePoint(getPos(), data);
/* 5115 */       data.writeBoolean(this.isTownhall);
/* 5116 */       data.writeBoolean(this.chestLocked);
/* 5117 */       StreamReadWrite.writeNullableString(this.controlledBy, data);
/* 5118 */       StreamReadWrite.writeNullableString(this.controlledByName, data);
/* 5119 */       StreamReadWrite.writeNullablePoint(this.townHallPos, data);
/* 5120 */       StreamReadWrite.writeNullableString(this.culture.key, data);
/* 5121 */       String vtype = null;
/* 5122 */       if (this.villageType != null) {
/* 5123 */         vtype = this.villageType.key;
/*      */       }
/* 5125 */       StreamReadWrite.writeNullableString(vtype, data);
/* 5126 */       StreamReadWrite.writeNullableBuildingLocation(this.location, data);
/*      */       
/* 5128 */       StreamReadWrite.writeNullableString(this.buildingGoal, data);
/* 5129 */       StreamReadWrite.writeNullableString(this.buildingGoalIssue, data);
/* 5130 */       data.writeInt(this.buildingGoalLevel);
/* 5131 */       data.writeInt(this.buildingGoalVariation);
/* 5132 */       StreamReadWrite.writeNullableBuildingLocation(this.buildingGoalLocation, data);
/* 5133 */       StreamReadWrite.writeNullableBuildingLocation(this.buildingLocationIP, data);
/* 5134 */       StreamReadWrite.writeProjectListList(this.buildingProjects, data);
/*      */       
/* 5136 */       StreamReadWrite.writePointList(this.buildings, data);
/* 5137 */       StreamReadWrite.writeStringList(this.buildingsBought, data);
/*      */       
/* 5139 */       StreamReadWrite.writePointIntegerMap(this.relations, data);
/* 5140 */       StreamReadWrite.writeStringList(this.raidsPerformed, data);
/* 5141 */       StreamReadWrite.writeStringList(this.raidsSuffered, data);
/*      */       
/* 5143 */       StreamReadWrite.writeVillagerRecordList(this.vrecords, data);
/*      */       
/* 5145 */       StreamReadWrite.writeNullablePuja(this.pujas, data);
/*      */       
/* 5147 */       StreamReadWrite.writeStringList(this.visitorsList, data);
/* 5148 */       StreamReadWrite.writeInventory(this.imported, data);
/* 5149 */       StreamReadWrite.writeInventory(this.exported, data);
/*      */       
/* 5151 */       StreamReadWrite.writeNullableString(this.name, data);
/* 5152 */       StreamReadWrite.writeNullableString(this.qualifier, data);
/*      */       
/* 5154 */       StreamReadWrite.writeNullablePoint(this.raidTarget, data);
/* 5155 */       data.writeLong(this.raidPlanningStart);
/* 5156 */       data.writeLong(this.raidStart);
/*      */       
/* 5158 */       this.resManager.sendBuildingPacket(data);
/*      */     }
/*      */     catch (IOException e) {
/* 5161 */       MLN.printException(this + ": Error in sendUpdatePacket", e);
/*      */     }
/*      */     
/* 5164 */     this.mw.getProfile(player.getDisplayName()).buildingsSent.put(this.pos, Long.valueOf(this.mw.world.func_72820_D()));
/*      */     
/* 5166 */     ServerSender.createAndSendPacketToPlayer(data, player);
/*      */   }
/*      */   
/*      */   private void sendInitialBuildingPackets() {
/* 5170 */     for (EntityPlayer player : MillCommonUtilities.getServerPlayers(this.mw.world)) {
/* 5171 */       if (this.pos.distanceToSquared(player) < 256.0D) {
/* 5172 */         UserProfile profile = MillCommonUtilities.getServerProfile(this.mw.world, player.getDisplayName());
/*      */         
/* 5174 */         if (!profile.buildingsSent.containsKey(this.pos)) {
/* 5175 */           sendBuildingPacket(player, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendMapInfo(EntityPlayer player) {
/* 5182 */     if (this.winfo != null) {
/* 5183 */       MillMapInfo minfo = new MillMapInfo(this, this.winfo);
/* 5184 */       minfo.sendMapInfoPacket(player);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendShopPacket(EntityPlayer player) {
/* 5189 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*      */     
/*      */     try
/*      */     {
/* 5193 */       data.write(11);
/*      */       
/* 5195 */       StreamReadWrite.writeNullablePoint(getPos(), data);
/*      */       
/* 5197 */       if (this.shopSells.containsKey(player.getDisplayName())) {
/* 5198 */         data.writeInt(((LinkedHashMap)this.shopSells.get(player.getDisplayName())).size());
/*      */         
/* 5200 */         for (Goods g : ((LinkedHashMap)this.shopSells.get(player.getDisplayName())).keySet()) {
/* 5201 */           StreamReadWrite.writeNullableGoods(g, data);
/* 5202 */           data.writeInt(((Integer)((LinkedHashMap)this.shopSells.get(player.getDisplayName())).get(g)).intValue());
/*      */         }
/*      */       } else {
/* 5205 */         data.writeInt(0);
/*      */       }
/*      */       
/* 5208 */       if (this.shopBuys.containsKey(player.getDisplayName())) {
/* 5209 */         data.writeInt(((LinkedHashMap)this.shopBuys.get(player.getDisplayName())).size());
/*      */         
/* 5211 */         for (Goods g : ((LinkedHashMap)this.shopBuys.get(player.getDisplayName())).keySet()) {
/* 5212 */           StreamReadWrite.writeNullableGoods(g, data);
/* 5213 */           data.writeInt(((Integer)((LinkedHashMap)this.shopBuys.get(player.getDisplayName())).get(g)).intValue());
/*      */         }
/*      */       } else {
/* 5216 */         data.writeInt(0);
/*      */       }
/*      */     } catch (IOException e) {
/* 5219 */       MLN.printException(this + ": Error in sendShopPacket", e);
/*      */     }
/*      */     
/* 5222 */     ServerSender.createAndSendPacketToPlayer(data, player);
/*      */   }
/*      */   
/*      */   public void setBblocks(BuildingBlock[] bblocks) {
/* 5226 */     this.bblocks = bblocks;
/* 5227 */     this.bblocksPos = 0;
/* 5228 */     this.bblocksChanged = true;
/*      */   }
/*      */   
/*      */   public void setGoods(Item item, int newVal) {
/* 5232 */     setGoods(item, 0, newVal);
/*      */   }
/*      */   
/*      */   public void setGoods(Item item, int meta, int newVal) {
/* 5236 */     int nb = countGoods(item, meta);
/*      */     
/* 5238 */     if (nb < newVal) {
/* 5239 */       storeGoods(item, meta, newVal - nb);
/*      */     } else {
/* 5241 */       takeGoods(item, meta, nb - newVal);
/*      */     }
/*      */   }
/*      */   
/*      */   private void startRaid() {
/* 5246 */     Building distantVillage = this.mw.getBuilding(this.raidTarget);
/*      */     
/* 5248 */     if ((this.relations.get(this.raidTarget) != null) && (((Integer)this.relations.get(this.raidTarget)).intValue() > -90))
/*      */     {
/* 5250 */       cancelRaid();
/*      */     }
/*      */     
/* 5253 */     if (distantVillage != null)
/*      */     {
/* 5255 */       this.raidStart = this.worldObj.func_72820_D();
/*      */       
/* 5257 */       int nbRaider = 0;
/*      */       
/* 5259 */       for (VillagerRecord vr : this.vrecords) {
/* 5260 */         if ((vr.getType().isRaider) && (!vr.killed) && (!vr.raidingVillage) && (!vr.awayraiding) && (!vr.awayhired))
/*      */         {
/* 5262 */           if (MLN.LogDiplomacy >= 2) {
/* 5263 */             MLN.minor(this, "Need to transfer raider; " + vr);
/*      */           }
/*      */           
/* 5266 */           vr.getHouse().transferVillager(vr, distantVillage, true);
/* 5267 */           nbRaider++;
/*      */         }
/*      */       }
/*      */       
/* 5271 */       if (nbRaider > 0) {
/* 5272 */         ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '4', "raid.started", new String[] { getVillageQualifiedName(), distantVillage.getVillageQualifiedName(), "" + nbRaider });
/*      */         
/* 5274 */         distantVillage.cancelRaid();
/* 5275 */         distantVillage.underAttack = true;
/*      */       } else {
/* 5277 */         cancelRaid();
/*      */       }
/*      */     } else {
/* 5280 */       cancelRaid();
/*      */     }
/*      */   }
/*      */   
/*      */   public int storeGoods(Block block, int toPut) {
/* 5285 */     return storeGoods(Item.func_150898_a(block), 0, toPut);
/*      */   }
/*      */   
/*      */   public int storeGoods(Block block, int meta, int toPut) {
/* 5289 */     return storeGoods(Item.func_150898_a(block), meta, toPut);
/*      */   }
/*      */   
/*      */   public int storeGoods(InvItem item, int toPut) {
/* 5293 */     return storeGoods(item.getItem(), item.meta, toPut);
/*      */   }
/*      */   
/*      */   public int storeGoods(Item item, int toPut) {
/* 5297 */     return storeGoods(item, 0, toPut);
/*      */   }
/*      */   
/*      */   public int storeGoods(Item item, int meta, int toPut)
/*      */   {
/* 5302 */     int stored = 0;
/*      */     
/* 5304 */     int i = 0;
/* 5305 */     while ((stored < toPut) && (i < this.resManager.chests.size()))
/*      */     {
/* 5307 */       TileEntityChest chest = ((Point)this.resManager.chests.get(i)).getMillChest(this.worldObj);
/*      */       
/* 5309 */       if (chest != null)
/*      */       {
/* 5311 */         stored += MillCommonUtilities.putItemsInChest(chest, item, meta, toPut - stored);
/*      */       }
/*      */       
/* 5314 */       i++;
/*      */     }
/*      */     
/* 5317 */     return stored;
/*      */   }
/*      */   
/*      */   private void swapMerchants(Building destInn)
/*      */   {
/* 5322 */     HashMap<InvItem, Integer> contents = this.resManager.getChestsContent();
/* 5323 */     HashMap<InvItem, Integer> destContents = destInn.resManager.getChestsContent();
/*      */     
/*      */ 
/* 5326 */     for (InvItem key : contents.keySet()) {
/* 5327 */       int nb = takeGoods(key.getItem(), key.meta, ((Integer)contents.get(key)).intValue());
/* 5328 */       destInn.storeGoods(key.getItem(), key.meta, nb);
/* 5329 */       destInn.addToImports(key, nb);
/* 5330 */       addToExports(key, nb);
/*      */     }
/*      */     
/*      */ 
/* 5334 */     for (InvItem key : destContents.keySet()) {
/* 5335 */       int nb = destInn.takeGoods(key.getItem(), key.meta, ((Integer)destContents.get(key)).intValue());
/* 5336 */       storeGoods(key.getItem(), key.meta, nb);
/* 5337 */       destInn.addToExports(key, nb);
/* 5338 */       addToImports(key, nb);
/*      */     }
/*      */     
/* 5341 */     VillagerRecord oldMerchant = this.merchantRecord;
/* 5342 */     VillagerRecord newMerchant = destInn.merchantRecord;
/*      */     
/* 5344 */     transferVillager(this.merchantRecord, destInn, false);
/* 5345 */     destInn.transferVillager(destInn.merchantRecord, this, false);
/*      */     
/* 5347 */     this.visitorsList.add("panels.merchantmovedout;" + oldMerchant.getName() + ";" + oldMerchant.getNativeOccupationName() + ";" + destInn.getTownHall().getVillageQualifiedName() + ";" + this.nbNightsMerchant);
/*      */     
/*      */ 
/* 5350 */     destInn.visitorsList.add("panels.merchantmovedout;" + newMerchant.getName() + ";" + newMerchant.getNativeOccupationName() + ";" + getTownHall().getVillageQualifiedName() + ";" + this.nbNightsMerchant);
/*      */     
/*      */ 
/* 5353 */     this.visitorsList.add("panels.merchantarrived;" + newMerchant.getName() + ";" + newMerchant.getNativeOccupationName() + ";" + destInn.getTownHall().getVillageQualifiedName());
/*      */     
/* 5355 */     destInn.visitorsList.add("panels.merchantarrived;" + oldMerchant.getName() + ";" + oldMerchant.getNativeOccupationName() + ";" + getTownHall().getVillageQualifiedName());
/*      */     
/* 5357 */     if (MLN.LogMerchant >= 1) {
/* 5358 */       MLN.major(this, "Swaped merchant " + oldMerchant + " and " + newMerchant + " with " + destInn.getTownHall());
/*      */     }
/*      */     
/* 5361 */     this.merchantRecord = newMerchant;
/* 5362 */     destInn.merchantRecord = oldMerchant;
/*      */     
/* 5364 */     this.nbNightsMerchant = 0;
/* 5365 */     destInn.nbNightsMerchant = 0;
/*      */     
/* 5367 */     destInn.saveTownHall("merchant moved");
/* 5368 */     this.saveNeeded = true;
/* 5369 */     this.saveReason = "Swapped merchant";
/*      */   }
/*      */   
/*      */   public int takeGoods(Block block, int toTake) {
/* 5373 */     return takeGoods(Item.func_150898_a(block), 0, toTake);
/*      */   }
/*      */   
/*      */   public int takeGoods(Block block, int meta, int toTake) {
/* 5377 */     return takeGoods(Item.func_150898_a(block), meta, toTake);
/*      */   }
/*      */   
/*      */   public int takeGoods(InvItem item, int toTake) {
/* 5381 */     return takeGoods(item.getItem(), item.meta, toTake);
/*      */   }
/*      */   
/*      */   public int takeGoods(Item item, int toTake) {
/* 5385 */     return takeGoods(item, 0, toTake);
/*      */   }
/*      */   
/*      */   public int takeGoods(Item item, int meta, int toTake) {
/* 5389 */     int taken = 0;
/*      */     
/* 5391 */     int i = 0;
/* 5392 */     while ((taken < toTake) && (i < this.resManager.chests.size())) {
/* 5393 */       TileEntityChest chest = ((Point)this.resManager.chests.get(i)).getMillChest(this.worldObj);
/*      */       
/* 5395 */       if (chest != null) {
/* 5396 */         taken += MillCommonUtilities.getItemsFromChest(chest, item, meta, toTake - taken);
/*      */       }
/*      */       
/* 5399 */       i++;
/*      */     }
/*      */     
/* 5402 */     i = 0;
/* 5403 */     while ((taken < toTake) && (i < this.resManager.furnaces.size())) {
/* 5404 */       TileEntityFurnace furnace = (TileEntityFurnace)this.worldObj.func_147438_o(((Point)this.resManager.furnaces.get(i)).getiX(), ((Point)this.resManager.furnaces.get(i)).getiY(), ((Point)this.resManager.furnaces.get(i)).getiZ());
/*      */       
/* 5406 */       if (furnace != null) {
/* 5407 */         taken += MillCommonUtilities.getItemsFromFurnace(furnace, item, toTake - taken);
/*      */       }
/*      */       
/* 5410 */       i++;
/*      */     }
/*      */     
/* 5413 */     return taken;
/*      */   }
/*      */   
/*      */   public void testModeGoods() {
/* 5417 */     if ((this.isTownhall) && (!this.villageType.lonebuilding)) {
/* 5418 */       int stored = storeGoods(Mill.denier_or, 64);
/*      */       
/* 5420 */       if (stored < 64) {
/* 5421 */         MLN.error(this, "Should have stored 64 test goods but stored only " + stored);
/*      */       }
/*      */       
/* 5424 */       storeGoods(Mill.summoningWand, 1);
/* 5425 */       if (this.culture.key.equals("hindi")) {
/* 5426 */         storeGoods(Mill.indianstatue, 64);
/* 5427 */         storeGoods(Mill.stone_decoration, 0, 2048);
/* 5428 */         storeGoods(Mill.stone_decoration, 1, 2048);
/* 5429 */         storeGoods(Blocks.field_150322_A, 2048);
/* 5430 */         storeGoods(Blocks.field_150348_b, 2048);
/* 5431 */         storeGoods(Blocks.field_150347_e, 512);
/* 5432 */       } else if (this.culture.key.equals("mayan")) {
/* 5433 */         storeGoods(Blocks.field_150322_A, 512);
/* 5434 */         storeGoods(Blocks.field_150348_b, 3500);
/* 5435 */         storeGoods(Blocks.field_150347_e, 2048);
/* 5436 */         storeGoods(Mill.stone_decoration, 2, 64);
/* 5437 */         storeGoods(Blocks.field_150364_r, 1, 512);
/* 5438 */         storeGoods(Blocks.field_150364_r, 3, 1024);
/* 5439 */       } else if (this.culture.key.equals("japanese")) {
/* 5440 */         storeGoods(Blocks.field_150345_g, 64);
/* 5441 */         storeGoods(Mill.wood_decoration, 2, 2048);
/* 5442 */         storeGoods(Blocks.field_150351_n, 512);
/* 5443 */         storeGoods(Mill.paperWall, 2048);
/* 5444 */         storeGoods(Blocks.field_150348_b, 2048);
/* 5445 */         storeGoods(Blocks.field_150347_e, 1024);
/* 5446 */         storeGoods(Mill.wood_decoration, 0, 512);
/* 5447 */         storeGoods(Mill.wood_decoration, 1, 512);
/* 5448 */         storeGoods(Blocks.field_150364_r, 1, 512);
/* 5449 */       } else if (this.culture.key.equals("byzantines")) {
/* 5450 */         storeGoods(Blocks.field_150359_w, 512);
/* 5451 */         storeGoods(Blocks.field_150347_e, 1500);
/* 5452 */         storeGoods(Blocks.field_150348_b, 1500);
/* 5453 */         storeGoods(Blocks.field_150336_V, 512);
/* 5454 */         storeGoods(Blocks.field_150322_A, 512);
/* 5455 */         storeGoods(Blocks.field_150325_L, 11, 64);
/* 5456 */         storeGoods(Blocks.field_150325_L, 14, 64);
/* 5457 */         storeGoods(Blocks.field_150364_r, 2, 128);
/* 5458 */         storeGoods(Blocks.field_150342_X, 0, 64);
/* 5459 */         storeGoods(Mill.byzantine_tiles, 128);
/* 5460 */         storeGoods(Mill.byzantine_tile_slab, 128);
/* 5461 */         storeGoods(Mill.byzantine_stone_tiles, 128);
/*      */       }
/*      */       else {
/* 5464 */         storeGoods(Blocks.field_150359_w, 512);
/* 5465 */         storeGoods(Blocks.field_150347_e, 2048);
/* 5466 */         storeGoods(Blocks.field_150348_b, 3500);
/* 5467 */         storeGoods(Mill.wood_decoration, 0, 2048);
/* 5468 */         storeGoods(Mill.wood_decoration, 1, 2048);
/* 5469 */         storeGoods(Blocks.field_150325_L, 11, 64);
/* 5470 */         storeGoods(Blocks.field_150325_L, 14, 64);
/*      */       }
/* 5472 */       storeGoods(Blocks.field_150364_r, 1024);
/* 5473 */       storeGoods(Items.field_151042_j, 256);
/* 5474 */       storeGoods(Blocks.field_150325_L, 64);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void testShowGroundLevel()
/*      */   {
/* 5481 */     for (int i = 0; i < this.winfo.length; i++) {
/* 5482 */       for (int j = 0; j < this.winfo.width; j++)
/*      */       {
/* 5484 */         Point p = new Point(this.winfo.mapStartX + i, this.winfo.topGround[i][j] - 1, this.winfo.mapStartZ + j);
/*      */         
/* 5486 */         if (MillCommonUtilities.getBlock(this.worldObj, p) != Mill.lockedChest) {
/* 5487 */           if (this.winfo.topAdjusted[i][j] == 0) {
/* 5488 */             MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Blocks.field_150325_L, this.pathing.regions[i][j] % 16);
/*      */           } else {
/* 5490 */             MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Blocks.field_150339_S, 0);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 5500 */     if (this.location != null) {
/* 5501 */       return "(" + this.location + "/" + getVillageQualifiedName() + "/" + this.worldObj + ")";
/*      */     }
/* 5503 */     return super.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   public void transferVillager(VillagerRecord vr, Building dest, boolean raid)
/*      */   {
/* 5509 */     if (MLN.LogDiplomacy >= 2) {
/* 5510 */       MLN.minor(this, "Transfering villager " + vr + " to " + dest + ". Raid: " + raid);
/*      */     }
/*      */     
/* 5513 */     if (!raid) {
/* 5514 */       removeVillagerRecord(vr.id);
/* 5515 */       getTownHall().removeVillagerRecord(vr.id);
/*      */     } else {
/* 5517 */       vr.awayraiding = true;
/* 5518 */       getTownHall().getVillagerRecordById(vr.id).awayraiding = true;
/*      */       
/* 5520 */       if (MLN.LogDiplomacy >= 3) {
/* 5521 */         MLN.debug(this, "Set record to away raiding");
/*      */       }
/*      */     }
/*      */     
/* 5525 */     VillagerRecord newRecord = vr.clone();
/* 5526 */     newRecord.housePos = dest.getPos();
/* 5527 */     newRecord.townHallPos = dest.getTownHall().getPos();
/* 5528 */     newRecord.raidingVillage = raid;
/* 5529 */     newRecord.awayraiding = false;
/*      */     
/* 5531 */     if (raid) {
/* 5532 */       newRecord.originalVillagePos = getTownHall().getPos();
/* 5533 */       newRecord.raiderSpawn = this.worldObj.func_72820_D();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5539 */     dest.vrecords.add(newRecord);
/* 5540 */     if (!dest.isTownhall) {
/* 5541 */       dest.getTownHall().vrecords.add(newRecord.clone());
/*      */     }
/*      */     
/* 5544 */     MillVillager v = getVillagerById(vr.id);
/* 5545 */     if (v != null) {
/* 5546 */       this.villagers.remove(v);
/* 5547 */       getTownHall().villagers.remove(v);
/*      */       
/* 5549 */       if ((!raid) && (dest.getTownHall().isActive)) {
/* 5550 */         v.setHousePoint(dest.getPos());
/* 5551 */         v.setTownHallPoint(dest.getTownHall().getPos());
/*      */         
/* 5553 */         v.isRaider = false;
/* 5554 */         v.func_70107_b(dest.resManager.getSleepingPos().getiX(), dest.resManager.getSleepingPos().getiY(), dest.resManager.getSleepingPos().getiZ());
/*      */         
/* 5556 */         dest.villagers.add(v);
/* 5557 */         dest.getTownHall().villagers.add(v);
/*      */         
/* 5559 */         if (MLN.LogDiplomacy >= 3) {
/* 5560 */           MLN.debug(this, "Villager entity moved.");
/*      */         }
/*      */       } else {
/* 5563 */         v.despawnVillager();
/*      */         
/* 5565 */         if (MLN.LogDiplomacy >= 3) {
/* 5566 */           MLN.debug(this, "Villager entity despawned.");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5572 */     dest.getTownHall().saveTownHall("incoming villager");
/*      */   }
/*      */   
/*      */   private void unloadChunks() {
/* 5576 */     if ((this.chunkLoader != null) && (this.chunkLoader.chunksLoaded)) {
/* 5577 */       this.chunkLoader.unloadChunks();
/*      */     }
/*      */   }
/*      */   
/*      */   public void unlockAllChests() {
/* 5582 */     this.chestLocked = false;
/* 5583 */     for (Point p : this.buildings) {
/* 5584 */       Building b = this.mw.getBuilding(p);
/* 5585 */       if (b != null) {
/* 5586 */         b.unlockChests();
/*      */       }
/*      */     }
/*      */     
/* 5590 */     if (countGoods(Mill.negationWand) == 0) {
/* 5591 */       storeGoods(Mill.negationWand, 1);
/*      */     }
/*      */   }
/*      */   
/*      */   public void unlockChests()
/*      */   {
/* 5597 */     if (!this.isMarket) {
/* 5598 */       this.chestLocked = false;
/* 5599 */       for (Point p : this.resManager.chests) {
/* 5600 */         TileEntityMillChest chest = p.getMillChest(this.worldObj);
/* 5601 */         if (chest != null) {
/* 5602 */           chest.buildingPos = getPos();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateAchievements()
/*      */   {
/* 5610 */     if (this.villageType == null) {
/* 5611 */       return;
/*      */     }
/*      */     
/* 5614 */     List<Entity> players = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, EntityPlayer.class, getPos(), this.villageType.radius, 20);
/*      */     
/* 5616 */     for (Entity ent : players)
/*      */     {
/* 5618 */       EntityPlayer player = (EntityPlayer)ent;
/*      */       
/* 5620 */       if (this.villageType.lonebuilding) {
/* 5621 */         player.func_71064_a(MillAchievements.explorer, 1);
/*      */       }
/*      */       
/* 5624 */       if (this.location.tags.contains("hof")) {
/* 5625 */         player.func_71064_a(MillAchievements.pantheon, 1);
/*      */       }
/*      */       
/* 5628 */       int nbcultures = this.mw.nbCultureInGeneratedVillages();
/*      */       
/* 5630 */       if (nbcultures >= 3) {
/* 5631 */         player.func_71064_a(MillAchievements.marcopolo, 1);
/*      */       }
/* 5633 */       if (nbcultures >= Culture.ListCultures.size()) {
/* 5634 */         player.func_71064_a(MillAchievements.magellan, 1);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateArchiveSigns()
/*      */   {
/* 5643 */     if (this.worldObj.field_72995_K) {
/* 5644 */       return;
/*      */     }
/*      */     
/* 5647 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 16.0D);
/*      */     
/* 5649 */     if (player == null) {
/* 5650 */       return;
/*      */     }
/*      */     
/* 5653 */     if (System.currentTimeMillis() - this.lastSignUpdate < 10000L) {
/* 5654 */       return;
/*      */     }
/*      */     
/* 5657 */     if (this.resManager.signs.size() == 0) {
/* 5658 */       return;
/*      */     }
/*      */     
/* 5661 */     for (int i = 0; i < this.resManager.signs.size(); i++) {
/* 5662 */       Point p = (Point)this.resManager.signs.get(i);
/* 5663 */       if ((p != null) && (MillCommonUtilities.getBlock(this.worldObj, p) != Mill.panel)) {
/* 5664 */         int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */         
/* 5666 */         if (meta > 0) {
/* 5667 */           MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5672 */     int signId = 0;
/*      */     
/* 5674 */     for (VillagerRecord vr : getTownHall().vrecords)
/*      */     {
/* 5676 */       if ((!vr.raidingVillage) && (!vr.getType().visitor) && (this.resManager.signs.get(signId) != null)) {
/* 5677 */         TileEntityPanel sign = ((Point)this.resManager.signs.get(signId)).getPanel(this.worldObj);
/*      */         
/* 5679 */         if (sign != null)
/*      */         {
/* 5681 */           String[][] lines = (String[][])null;
/*      */           
/* 5683 */           if (vr.awayraiding) {
/* 5684 */             lines = new String[][] { { vr.firstName }, { vr.familyName }, { "" }, { "panels.awayraiding" } };
/* 5685 */           } else if (vr.awayhired) {
/* 5686 */             lines = new String[][] { { vr.firstName }, { vr.familyName }, { "" }, { "panels.awayhired" } };
/* 5687 */           } else if (vr.killed) {
/* 5688 */             lines = new String[][] { { vr.firstName }, { vr.familyName }, { "" }, { "panels.dead" } };
/*      */           } else {
/* 5690 */             MillVillager villager = getTownHall().getVillagerById(vr.id);
/* 5691 */             if (villager == null) {
/* 5692 */               lines = new String[][] { { vr.firstName }, { vr.familyName }, { "" }, { "panels.missing" } };
/*      */             }
/* 5694 */             else if (!villager.isVisitor()) {
/* 5695 */               String distance = "" + Math.floor(getPos().distanceTo(villager));
/*      */               
/* 5697 */               String direction = getPos().directionTo(villager.getPos());
/* 5698 */               String occupation = "";
/*      */               
/* 5700 */               if ((villager.goalKey != null) && (Goal.goals.containsKey(villager.goalKey))) {
/* 5701 */                 occupation = "goal." + villager.goalKey;
/*      */               }
/*      */               
/* 5704 */               if (occupation.length() > 15) {
/* 5705 */                 occupation = occupation.substring(0, 10) + "(...)";
/*      */               }
/*      */               
/* 5708 */               lines = new String[][] { { vr.firstName }, { vr.familyName }, { "other.shortdistancedirection", distance, direction }, { occupation } };
/*      */             }
/* 5710 */             ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(signId), lines, 7, this.townHallPos, vr.id);
/*      */           }
/*      */         }
/*      */       }
/* 5714 */       signId++;
/*      */       
/* 5716 */       if (signId >= this.resManager.signs.size()) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 5721 */     for (int i = signId; i < this.resManager.signs.size(); i++) {
/* 5722 */       if (this.resManager.signs.get(i) != null) {
/* 5723 */         TileEntityPanel sign = ((Point)this.resManager.signs.get(i)).getPanel(this.worldObj);
/*      */         
/* 5725 */         if (sign != null)
/*      */         {
/* 5727 */           String[][] lines = { { "ui.reservedforvillager1" }, { "ui.reservedforvillager2" }, { "" }, { "#" + (i + 1) } };
/* 5728 */           ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(i), lines, 0, this.townHallPos, 0L);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5733 */     this.lastSignUpdate = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   public void updateBackgroundVillage()
/*      */   {
/* 5738 */     if (this.worldObj.field_72995_K) {
/* 5739 */       return;
/*      */     }
/*      */     
/* 5742 */     if ((this.villageType == null) || (!this.isTownhall) || (this.location == null)) {
/* 5743 */       return;
/*      */     }
/*      */     
/* 5746 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), MLN.BackgroundRadius);
/*      */     
/*      */ 
/* 5749 */     if (player != null) {
/* 5750 */       if (this.worldObj.func_72935_r()) {
/* 5751 */         this.nightBackgroundActionPerformed = false;
/* 5752 */       } else if (!this.nightBackgroundActionPerformed) {
/* 5753 */         if ((this.villageType.carriesRaid) && (this.raidTarget == null) && (MillCommonUtilities.randomInt(100) < MLN.RaidingRate)) {
/* 5754 */           if (MLN.LogDiplomacy >= 3) {
/* 5755 */             MLN.debug(this, "Calling attemptPlanNewRaid");
/*      */           }
/* 5757 */           attemptPlanNewRaid();
/*      */         }
/* 5759 */         this.nightBackgroundActionPerformed = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5764 */     if ((this.worldObj.func_72820_D() % 24000L > 23500L) && (this.raidTarget != null)) {
/* 5765 */       if (!this.updateRaidPerformed)
/*      */       {
/* 5767 */         if (MLN.LogDiplomacy >= 3) {
/* 5768 */           MLN.debug(this, "Calling updateRaid for raid: " + this.raidPlanningStart + "/" + this.raidStart + "/" + this.worldObj.func_72820_D());
/*      */         }
/*      */         
/* 5771 */         updateRaid();
/* 5772 */         this.updateRaidPerformed = true;
/*      */       }
/*      */     } else {
/* 5775 */       this.updateRaidPerformed = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateBuildingClient()
/*      */   {
/* 5784 */     if ((this.location != null) && (this.pos != null) && 
/* 5785 */       (this.isTownhall) && (this.villageType != null) && (this.location != null)) {
/* 5786 */       if (this.lastVillagerRecordsRepair == 0L) {
/* 5787 */         this.lastVillagerRecordsRepair = this.worldObj.func_72820_D();
/* 5788 */       } else if (this.worldObj.func_72820_D() - this.lastVillagerRecordsRepair >= 100L) {
/* 5789 */         repairVillagerListClient();
/* 5790 */         this.lastVillagerRecordsRepair = this.worldObj.func_72820_D();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateBuildingServer()
/*      */   {
/* 5799 */     if (this.worldObj.field_72995_K) {
/* 5800 */       updateBuildingClient();
/* 5801 */       return;
/*      */     }
/*      */     
/* 5804 */     if (this.location == null) {
/* 5805 */       return;
/*      */     }
/*      */     
/* 5808 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), MLN.KeepActiveRadius);
/*      */     
/* 5810 */     if (this.isTownhall) {
/* 5811 */       if ((player != null) && (getPos().distanceTo(player) < MLN.KeepActiveRadius)) {
/* 5812 */         loadChunks();
/* 5813 */       } else if ((player == null) || (getPos().distanceTo(player) > MLN.KeepActiveRadius + 32)) {
/* 5814 */         unloadChunks();
/*      */       }
/* 5816 */       this.isAreaLoaded = isVillageChunksLoaded();
/*      */       
/* 5818 */       if ((this.isActive) && (!this.isAreaLoaded)) {
/* 5819 */         this.isActive = false;
/*      */         
/* 5821 */         for (Object o : this.worldObj.field_73010_i) {
/* 5822 */           EntityPlayer p = (EntityPlayer)o;
/* 5823 */           sendBuildingPacket(p, false);
/*      */         }
/*      */         
/* 5826 */         saveTownHall("becoming inactive");
/* 5827 */       } else if ((!this.isActive) && (this.isAreaLoaded))
/*      */       {
/* 5829 */         for (Object o : this.worldObj.field_73010_i) {
/* 5830 */           EntityPlayer p = (EntityPlayer)o;
/* 5831 */           sendBuildingPacket(p, false);
/*      */         }
/*      */         
/* 5834 */         this.isActive = true;
/*      */       }
/* 5836 */       if (this.isActive) {}
/*      */ 
/*      */ 
/*      */     }
/* 5840 */     else if ((getTownHall() == null) || (!getTownHall().isActive)) {
/* 5841 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5845 */     if (this.location == null) {
/* 5846 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 5850 */       if ((this.isTownhall) && (this.villageType != null)) {
/* 5851 */         updateTownHall();
/*      */       }
/*      */       
/* 5854 */       if (this.location.tags.contains("grove")) {
/* 5855 */         updateGrove();
/*      */       }
/*      */       
/* 5858 */       if (this.location.tags.contains("brickkiln")) {
/* 5859 */         updateKiln();
/*      */       }
/*      */       
/* 5862 */       if (this.resManager.spawns.size() > 0) {
/* 5863 */         updatePens();
/*      */       }
/*      */       
/* 5866 */       if (this.resManager.healingspots.size() > 0) {
/* 5867 */         updateHealingSpots();
/*      */       }
/*      */       
/* 5870 */       if ((this.resManager.mobSpawners.size() > 0) && (player != null) && (this.pos.distanceToSquared(player) < 400.0D)) {
/* 5871 */         updateMobSpawners();
/*      */       }
/*      */       
/* 5874 */       if (this.resManager.dispenderUnknownPowder.size() > 0) {
/* 5875 */         updateDispensers();
/*      */       }
/*      */       
/* 5878 */       if (this.resManager.netherwartsoils.size() > 0) {
/* 5879 */         updateNetherWartSoils();
/*      */       }
/*      */       
/* 5882 */       if (this.isInn) {
/* 5883 */         updateInn();
/*      */       }
/*      */       
/* 5886 */       if (this.isMarket) {
/* 5887 */         updateMarket(false);
/*      */       }
/*      */       
/* 5890 */       updateSigns();
/*      */       
/* 5892 */       if (this.isTownhall) {
/* 5893 */         if (this.saveNeeded) {
/* 5894 */           saveTownHall("Save needed");
/* 5895 */         } else if (this.worldObj.func_72820_D() - this.lastSaved > 1000L) {
/* 5896 */           saveTownHall("Delay up");
/*      */         }
/*      */       }
/*      */       
/* 5900 */       if ((player != null) && (this.location.getPlan() != null) && (this.location.getPlan().exploreTag != null)) {
/* 5901 */         checkExploreTag(player);
/*      */       }
/*      */       
/* 5904 */       sendInitialBuildingPackets();
/*      */       
/* 5906 */       if (MillCommonUtilities.chanceOn(100)) {
/* 5907 */         for (Point p : this.resManager.chests) {
/* 5908 */           if (p.getMillChest(this.worldObj) != null) {
/* 5909 */             p.getMillChest(this.worldObj).buildingPos = getPos();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 5914 */       refreshGoods();
/*      */     }
/*      */     catch (Exception e) {
/* 5917 */       Mill.proxy.sendChatAdmin(MLN.string("ui.updateEntity"));
/* 5918 */       MLN.error(this, "Exception in TileEntityBuilding.onUpdate(): ");
/* 5919 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateDefaultSign()
/*      */   {
/* 5925 */     if (this.worldObj.field_72995_K) {
/* 5926 */       return;
/*      */     }
/*      */     
/* 5929 */     if (this.resManager.signs.size() == 0) {
/* 5930 */       return;
/*      */     }
/*      */     
/* 5933 */     if ((this.pos == null) || (this.location == null)) {
/* 5934 */       return;
/*      */     }
/*      */     
/* 5937 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 16.0D);
/*      */     
/* 5939 */     if (player == null) {
/* 5940 */       return;
/*      */     }
/*      */     
/* 5943 */     if (System.currentTimeMillis() - this.lastSignUpdate < 10000L) {
/* 5944 */       return;
/*      */     }
/*      */     
/* 5947 */     Point p = (Point)this.resManager.signs.get(0);
/*      */     
/* 5949 */     if (p == null) {
/* 5950 */       return;
/*      */     }
/*      */     
/* 5953 */     if (this.worldObj.func_147439_a(p.getiX(), p.getiY(), p.getiZ()) != Mill.panel)
/*      */     {
/* 5955 */       int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */       
/* 5957 */       if (meta > 0) {
/* 5958 */         MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */       }
/*      */     }
/*      */     
/* 5962 */     TileEntityPanel panel = p.getPanel(this.worldObj);
/*      */     
/* 5964 */     if (panel == null) {
/* 5965 */       MLN.error(this, "No TileEntitySign at: " + p);
/*      */     }
/*      */     else {
/* 5968 */       String[][] lines = (String[][])null;
/*      */       
/* 5970 */       lines = new String[][] { { "" }, { getNativeBuildingName() }, { "" }, { "" } };
/*      */       
/* 5972 */       ServerSender.updatePanel(this.mw, p, lines, 0, getPos(), 0L);
/*      */     }
/*      */     
/* 5975 */     this.lastSignUpdate = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   private void updateDispensers()
/*      */   {
/* 5980 */     for (Point p : this.resManager.dispenderUnknownPowder)
/*      */     {
/* 5982 */       if (MillCommonUtilities.chanceOn(5000))
/*      */       {
/* 5984 */         TileEntityDispenser dispenser = p.getDispenser(this.worldObj);
/* 5985 */         if (dispenser != null) {
/* 5986 */           MillCommonUtilities.putItemsInChest(dispenser, Mill.unknownPowder, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateGrove()
/*      */   {
/* 5994 */     for (Point p : this.resManager.woodspawn) {
/* 5995 */       if ((MillCommonUtilities.chanceOn(4000)) && (MillCommonUtilities.getBlock(this.worldObj, p) == Blocks.field_150345_g)) {
/* 5996 */         growTree(this.worldObj, p.getiX(), p.getiY(), p.getiZ(), MillCommonUtilities.random);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateHealingSpots()
/*      */   {
/* 6003 */     if (this.worldObj.func_72820_D() % 100L == 0L)
/*      */     {
/* 6005 */       for (Point p : this.resManager.healingspots)
/*      */       {
/* 6007 */         EntityPlayer player = this.worldObj.func_72977_a(p.getiX(), p.getiY(), p.getiZ(), 4.0D);
/*      */         
/* 6009 */         if ((player != null) && (player.func_110143_aJ() < player.func_110138_aP())) {
/* 6010 */           player.func_70606_j(player.func_110143_aJ() + 1.0F);
/* 6011 */           ServerSender.sendTranslatedSentence(player, 'a', "other.buildinghealing", new String[] { getNativeBuildingName() });
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateHouseSign()
/*      */   {
/* 6021 */     if (this.worldObj.field_72995_K) {
/* 6022 */       return;
/*      */     }
/*      */     
/* 6025 */     if (this.resManager.signs.size() == 0) {
/* 6026 */       return;
/*      */     }
/*      */     
/* 6029 */     if ((this.pos == null) || (this.location == null)) {
/* 6030 */       return;
/*      */     }
/*      */     
/* 6033 */     if ((this.isTownhall) && (this.location.showTownHallSigns)) {
/* 6034 */       return;
/*      */     }
/*      */     
/* 6037 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 16.0D);
/*      */     
/* 6039 */     if (player == null) {
/* 6040 */       return;
/*      */     }
/*      */     
/* 6043 */     if (System.currentTimeMillis() - this.lastSignUpdate < 10000L) {
/* 6044 */       return;
/*      */     }
/*      */     
/* 6047 */     VillagerRecord wife = null;VillagerRecord husband = null;
/*      */     
/* 6049 */     for (VillagerRecord vr : getTownHall().vrecords) {
/* 6050 */       if (getPos().equals(vr.housePos)) {
/* 6051 */         if ((vr.gender == 2) && ((vr.getType() == null) || (!vr.getType().isChild))) {
/* 6052 */           wife = vr;
/*      */         }
/* 6054 */         if ((vr.gender == 1) && ((vr.getType() == null) || (!vr.getType().isChild))) {
/* 6055 */           husband = vr;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6060 */     Point p = (Point)this.resManager.signs.get(0);
/*      */     
/* 6062 */     if (p == null) {
/* 6063 */       return;
/*      */     }
/*      */     
/* 6066 */     if (this.worldObj.func_147439_a(p.getiX(), p.getiY(), p.getiZ()) != Mill.panel)
/*      */     {
/* 6068 */       int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */       
/* 6070 */       if (meta > 0) {
/* 6071 */         MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */       }
/*      */     }
/*      */     
/* 6075 */     TileEntityPanel panel = p.getPanel(this.worldObj);
/*      */     
/* 6077 */     if (panel == null) {
/* 6078 */       MLN.error(this, "No TileEntitySign at: " + p);
/*      */     }
/*      */     else {
/* 6081 */       String[][] lines = (String[][])null;
/*      */       
/* 6083 */       if ((husband != null) && (wife != null)) {
/* 6084 */         lines = new String[][] { { "panels.nameand", wife.firstName }, { husband.firstName }, { husband.familyName }, { "" } };
/* 6085 */       } else if (husband != null) {
/* 6086 */         lines = new String[][] { { husband.firstName }, { "" }, { husband.familyName }, { "" } };
/* 6087 */       } else if (wife != null) {
/* 6088 */         lines = new String[][] { { wife.firstName }, { "" }, { wife.familyName }, { "" } };
/*      */       } else {
/* 6090 */         lines = new String[][] { { "ui.currentlyempty1" }, { "" }, { "ui.currentlyempty2" }, { "" } };
/*      */       }
/*      */       
/* 6093 */       ServerSender.updatePanel(this.mw, p, lines, 5, getPos(), 0L);
/*      */     }
/*      */     
/* 6096 */     this.lastSignUpdate = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   private void updateInn()
/*      */   {
/* 6101 */     if (this.worldObj.func_72935_r()) {
/* 6102 */       this.thNightActionPerformed = false;
/* 6103 */     } else if (!this.thNightActionPerformed) {
/* 6104 */       if (this.merchantRecord != null) {
/* 6105 */         this.nbNightsMerchant += 1;
/*      */         
/* 6107 */         if (this.nbNightsMerchant > 1) {
/* 6108 */           attemptMerchantMove(false);
/*      */         }
/*      */       }
/* 6111 */       this.thNightActionPerformed = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateInnSign()
/*      */   {
/* 6117 */     if (this.worldObj.field_72995_K) {
/* 6118 */       return;
/*      */     }
/*      */     
/* 6121 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 20.0D);
/*      */     
/* 6123 */     if (player == null) {
/* 6124 */       return;
/*      */     }
/*      */     
/* 6127 */     if (this.resManager.signs.size() == 0) {
/* 6128 */       return;
/*      */     }
/*      */     
/* 6131 */     for (int i = 0; i < this.resManager.signs.size(); i++) {
/* 6132 */       Point p = (Point)this.resManager.signs.get(i);
/* 6133 */       if ((p != null) && (MillCommonUtilities.getBlock(this.worldObj, p) != Mill.panel)) {
/* 6134 */         int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */         
/* 6136 */         if (meta > 0) {
/* 6137 */           MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6142 */     TileEntityPanel sign = ((Point)this.resManager.signs.get(0)).getPanel(this.worldObj);
/*      */     
/* 6144 */     if (sign != null) {
/* 6145 */       String[][] lines = { { getNativeBuildingName() }, { "" }, { "ui.visitorslist1" }, { "ui.visitorslist2" } };
/*      */       
/* 6147 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(0), lines, 11, getPos(), 0L);
/*      */     }
/*      */     
/* 6150 */     if (this.resManager.signs.size() < 2) {
/* 6151 */       return;
/*      */     }
/*      */     
/* 6154 */     sign = ((Point)this.resManager.signs.get(1)).getPanel(this.worldObj);
/*      */     
/* 6156 */     if (sign != null) {
/* 6157 */       String[][] lines = { { "ui.goodstraded" }, { "" }, { "ui.import_total", "" + MillCommonUtilities.getInvItemHashTotal(this.imported) }, { "ui.export_total", "" + MillCommonUtilities.getInvItemHashTotal(this.exported) } };
/*      */       
/*      */ 
/* 6160 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(1), lines, 10, getPos(), 0L);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateKiln() {
/* 6165 */     for (Point p : this.resManager.brickspot) {
/* 6166 */       if (MillCommonUtilities.getBlock(this.worldObj, p) == Blocks.field_150349_c) {
/* 6167 */         MillCommonUtilities.setBlock(this.worldObj, p, Blocks.field_150346_d);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateMarket(boolean devAttempt) throws MLN.MillenaireException
/*      */   {
/* 6174 */     if ((this.worldObj.func_72935_r()) && (!devAttempt)) {
/* 6175 */       this.thNightActionPerformed = false;
/* 6176 */     } else if ((!this.thNightActionPerformed) || (devAttempt))
/*      */     {
/* 6178 */       int maxMerchants = this.resManager.stalls.size();
/*      */       
/* 6180 */       if (this.vrecords.size() < maxMerchants) {
/* 6181 */         if (MLN.LogMerchant >= 1) {
/* 6182 */           MLN.major(this, "Attempting to create a foreign merchant.");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 6187 */         List<VillagerType> merchantTypesOtherVillages = new ArrayList();
/* 6188 */         for (Point p : getTownHall().relations.keySet()) {
/* 6189 */           if (((Integer)getTownHall().relations.get(p)).intValue() > 70) {
/* 6190 */             Building distantVillage = this.mw.getBuilding(p);
/* 6191 */             if ((distantVillage != null) && (distantVillage.culture != getTownHall().culture) && (distantVillage.getBuildingsWithTag("market").size() > 0)) {
/* 6192 */               merchantTypesOtherVillages.add(distantVillage.culture.getRandomForeignMerchant());
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 6199 */         int foreignChance = Math.min(1 + merchantTypesOtherVillages.size(), 5);
/*      */         VillagerType type;
/* 6201 */         VillagerType type; if ((merchantTypesOtherVillages.size() > 0) && (MillCommonUtilities.randomInt(11) < foreignChance)) {
/*      */           VillagerType type;
/* 6203 */           if (merchantTypesOtherVillages.size() == 0) {
/* 6204 */             type = this.culture.getRandomForeignMerchant();
/*      */           } else {
/* 6206 */             type = (VillagerType)merchantTypesOtherVillages.get(MillCommonUtilities.randomInt(merchantTypesOtherVillages.size()));
/*      */           }
/*      */         }
/*      */         else {
/* 6210 */           type = this.culture.getRandomForeignMerchant();
/*      */         }
/*      */         
/* 6213 */         MillVillager merchant = MillVillager.createVillager(type.culture, type.key, 0, this.worldObj, this.resManager.getSleepingPos(), getPos(), this.townHallPos, false, null, null);
/*      */         
/* 6215 */         addOrReplaceVillager(merchant);
/* 6216 */         VillagerRecord merchantRecord = new VillagerRecord(this.mw, merchant);
/* 6217 */         addOrReplaceRecord(merchantRecord);
/* 6218 */         this.worldObj.func_72838_d(merchant);
/*      */         
/* 6220 */         for (InvItem iv : merchant.vtype.foreignMerchantStock.keySet()) {
/* 6221 */           storeGoods(iv.getItem(), iv.meta, ((Integer)merchant.vtype.foreignMerchantStock.get(iv)).intValue());
/*      */         }
/*      */         
/* 6224 */         if (MLN.LogMerchant >= 1) {
/* 6225 */           MLN.major(this, "Created foreign merchant: " + merchantRecord);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 6230 */       this.thNightActionPerformed = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateMarketSigns()
/*      */   {
/* 6236 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 20.0D);
/*      */     
/* 6238 */     if (player == null) {
/* 6239 */       return;
/*      */     }
/*      */     
/* 6242 */     if (this.resManager.signs.size() == 0) {
/* 6243 */       return;
/*      */     }
/*      */     
/* 6246 */     for (int i = 0; i < this.resManager.signs.size(); i++) {
/* 6247 */       Point p = (Point)this.resManager.signs.get(i);
/* 6248 */       if ((p != null) && (MillCommonUtilities.getBlock(this.worldObj, p) != Mill.panel)) {
/* 6249 */         int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */         
/* 6251 */         if (meta > 0) {
/* 6252 */           MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6257 */     TileEntityPanel sign = ((Point)this.resManager.signs.get(0)).getPanel(this.worldObj);
/*      */     
/* 6259 */     if (sign != null)
/*      */     {
/* 6261 */       String[][] lines = { { getNativeBuildingName() }, { "" }, { "ui.merchants" }, { "" + this.vrecords.size() } };
/* 6262 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(0), lines, 12, getPos(), 0L);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateMobSpawners()
/*      */   {
/* 6268 */     for (int i = 0; i < this.resManager.mobSpawners.size(); i++) {
/* 6269 */       for (int j = 0; j < ((List)this.resManager.mobSpawners.get(i)).size(); j++) {
/* 6270 */         if (MillCommonUtilities.chanceOn(180)) {
/* 6271 */           Block block = MillCommonUtilities.getBlock(this.worldObj, (Point)((List)this.resManager.mobSpawners.get(i)).get(j));
/*      */           
/* 6273 */           if (block == Blocks.field_150474_ac)
/*      */           {
/* 6275 */             List<Entity> mobs = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, EntityMob.class, (Point)((List)this.resManager.mobSpawners.get(i)).get(j), 10, 5);
/* 6276 */             int nbmob = 0;
/* 6277 */             for (Entity ent : mobs) {
/* 6278 */               if (EntityList.func_75621_b(ent).equals(this.resManager.mobSpawnerTypes.get(i))) {
/* 6279 */                 nbmob++;
/*      */               }
/*      */             }
/*      */             
/* 6283 */             if (nbmob < 4) {
/* 6284 */               MillCommonUtilities.spawnMobsSpawner(this.worldObj, (Point)((List)this.resManager.mobSpawners.get(i)).get(j), (String)this.resManager.mobSpawnerTypes.get(i));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateNetherWartSoils() {
/* 6293 */     for (Point p : this.resManager.netherwartsoils)
/*      */     {
/* 6295 */       if ((MillCommonUtilities.chanceOn(1000)) && 
/* 6296 */         (MillCommonUtilities.getBlock(this.worldObj, p.getAbove()) == Blocks.field_150388_bm)) {
/* 6297 */         int meta = MillCommonUtilities.getBlockMeta(this.worldObj, p.getAbove());
/*      */         
/* 6299 */         if (meta < 3) {
/* 6300 */           MillCommonUtilities.setBlockMetadata(this.worldObj, p.getAbove(), meta + 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updatePens()
/*      */   {
/* 6310 */     if ((!this.worldObj.func_72935_r()) && ((this.vrecords.size() > 0) || ((this.location.maleResident.isEmpty()) && (this.location.femaleResident.isEmpty()))) && (!this.worldObj.field_72995_K))
/*      */     {
/* 6312 */       int nbMaxRespawn = 0;
/* 6313 */       for (List<Point> spawnPoints : this.resManager.spawns) {
/* 6314 */         nbMaxRespawn += spawnPoints.size();
/*      */       }
/*      */       
/* 6317 */       if (this.nbAnimalsRespawned <= nbMaxRespawn) {
/* 6318 */         int sheep = 0;int cow = 0;int pig = 0;int chicken = 0;int squid = 0;
/*      */         
/* 6320 */         List<Entity> animals = MillCommonUtilities.getEntitiesWithinAABB(this.worldObj, IAnimals.class, getPos(), 15, 5);
/*      */         
/* 6322 */         for (Entity animal : animals) {
/* 6323 */           if ((animal instanceof EntitySheep)) {
/* 6324 */             sheep++;
/* 6325 */           } else if ((animal instanceof EntityPig)) {
/* 6326 */             pig++;
/* 6327 */           } else if ((animal instanceof EntityCow)) {
/* 6328 */             cow++;
/* 6329 */           } else if ((animal instanceof net.minecraft.entity.passive.EntityChicken)) {
/* 6330 */             chicken++;
/* 6331 */           } else if ((animal instanceof EntitySquid)) {
/* 6332 */             squid++;
/*      */           }
/*      */         }
/*      */         
/* 6336 */         for (int i = 0; i < this.resManager.spawns.size(); i++) {
/* 6337 */           int nb = 0;
/* 6338 */           if (((String)this.resManager.spawnTypes.get(i)).equals("Sheep")) {
/* 6339 */             nb = sheep;
/* 6340 */           } else if (((String)this.resManager.spawnTypes.get(i)).equals("Chicken")) {
/* 6341 */             nb = chicken;
/* 6342 */           } else if (((String)this.resManager.spawnTypes.get(i)).equals("Pig")) {
/* 6343 */             nb = pig;
/* 6344 */           } else if (((String)this.resManager.spawnTypes.get(i)).equals("Cow")) {
/* 6345 */             nb = cow;
/* 6346 */           } else if (((String)this.resManager.spawnTypes.get(i)).equals("Squid")) {
/* 6347 */             nb = squid;
/*      */           }
/*      */           
/* 6350 */           int multipliyer = 1;
/*      */           
/* 6352 */           if (((String)this.resManager.spawnTypes.get(i)).equals("Squid")) {
/* 6353 */             multipliyer = 2;
/*      */           }
/*      */           
/* 6356 */           for (int j = 0; j < ((List)this.resManager.spawns.get(i)).size() * multipliyer - nb; j++) {
/* 6357 */             if (MillCommonUtilities.chanceOn(100)) {
/* 6358 */               EntityCreature animal = (EntityCreature)EntityList.func_75620_a((String)this.resManager.spawnTypes.get(i), this.worldObj);
/* 6359 */               Point pen = (Point)((List)this.resManager.spawns.get(i)).get(MillCommonUtilities.randomInt(((List)this.resManager.spawns.get(i)).size()));
/* 6360 */               animal.func_70107_b(pen.getiX() + 0.5D, pen.getiY(), pen.getiZ() + 0.5D);
/* 6361 */               this.worldObj.func_72838_d(animal);
/* 6362 */               this.nbAnimalsRespawned += 1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/* 6368 */       this.nbAnimalsRespawned = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateRaid()
/*      */   {
/* 6375 */     if ((this.worldObj.func_72820_D() > this.raidPlanningStart + 24000L) && (this.raidStart == 0L))
/*      */     {
/* 6377 */       if (MLN.LogDiplomacy >= 2) {
/* 6378 */         MLN.minor(this, "Starting raid on " + this.mw.getBuilding(this.raidTarget));
/*      */       }
/*      */       
/* 6381 */       startRaid();
/*      */     }
/* 6383 */     else if ((this.raidStart > 0L) && (this.worldObj.func_72820_D() > this.raidStart + 23000L))
/*      */     {
/* 6385 */       Building distantVillage = this.mw.getBuilding(this.raidTarget);
/*      */       
/* 6387 */       if (distantVillage != null)
/*      */       {
/*      */ 
/* 6390 */         if (!distantVillage.isActive) {
/* 6391 */           endRaid();
/*      */         }
/*      */       } else {
/* 6394 */         cancelRaid();
/* 6395 */         for (VillagerRecord vr : this.vrecords) {
/* 6396 */           vr.awayraiding = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateSigns() {
/* 6403 */     EnumSignType type = getSignType();
/*      */     
/* 6405 */     if (type == EnumSignType.TOWNHALL) {
/* 6406 */       updateTownHallSigns(false);
/* 6407 */     } else if (type == EnumSignType.ARCHIVES) {
/* 6408 */       updateArchiveSigns();
/* 6409 */     } else if (type == EnumSignType.MARKET) {
/* 6410 */       updateMarketSigns();
/* 6411 */     } else if (type == EnumSignType.INN) {
/* 6412 */       updateInnSign();
/* 6413 */     } else if (type == EnumSignType.HOUSE) {
/* 6414 */       updateHouseSign();
/* 6415 */     } else if (type == EnumSignType.DEFAULT) {
/* 6416 */       updateDefaultSign();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateTownHall() throws MLN.MillenaireException {
/* 6421 */     if (this.vrecords.size() > 0) {
/* 6422 */       updateWorldInfo();
/*      */     }
/*      */     
/* 6425 */     this.closestPlayer = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 100.0D);
/*      */     
/* 6427 */     completeConstruction();
/*      */     
/* 6429 */     findBuildingProject();
/* 6430 */     findBuildingConstruction();
/*      */     
/* 6432 */     checkSeller();
/* 6433 */     checkWorkers();
/*      */     
/* 6435 */     if (this.worldObj.func_72820_D() % 10L == 0L) {
/* 6436 */       checkBattleStatus();
/*      */     }
/*      */     
/*      */ 
/* 6440 */     killMobs();
/*      */     
/* 6442 */     if ((this.vrecords.size() > 0) && (!this.worldObj.func_72935_r()) && (System.currentTimeMillis() - this.lastPathingUpdate > 60000L)) {
/* 6443 */       rebuildPathing(false);
/*      */     }
/*      */     
/* 6446 */     if ((!this.declaredPos) && (this.worldObj != null)) {
/* 6447 */       if (this.villageType.lonebuilding) {
/* 6448 */         this.mw.registerLoneBuildingsLocation(this.worldObj, getPos(), getVillageQualifiedName(), this.villageType, this.culture, false, null);
/*      */       } else {
/* 6450 */         this.mw.registerVillageLocation(this.worldObj, getPos(), getVillageQualifiedName(), this.villageType, this.culture, false, null);
/*      */       }
/* 6452 */       this.declaredPos = true;
/*      */     }
/*      */     
/* 6455 */     if (this.lastVillagerRecordsRepair == 0L) {
/* 6456 */       this.lastVillagerRecordsRepair = this.worldObj.func_72820_D();
/* 6457 */     } else if (this.worldObj.func_72820_D() - this.lastVillagerRecordsRepair >= 100L) {
/* 6458 */       repairVillagerList();
/* 6459 */       this.lastVillagerRecordsRepair = this.worldObj.func_72820_D();
/*      */     }
/*      */     
/* 6462 */     if (this.worldObj.func_72935_r()) {
/* 6463 */       this.thNightActionPerformed = false;
/* 6464 */     } else if (!this.thNightActionPerformed)
/*      */     {
/* 6466 */       if ((!this.villageType.playerControlled) && (!this.villageType.lonebuilding))
/*      */       {
/* 6468 */         for (EntityPlayer player : MillCommonUtilities.getServerPlayers(this.worldObj)) {
/* 6469 */           UserProfile profile = MillCommonUtilities.getServerProfile(this.worldObj, player.getDisplayName());
/* 6470 */           profile.adjustDiplomacyPoint(this, 5);
/*      */         }
/*      */         
/* 6473 */         for (Point p : this.relations.keySet()) {
/* 6474 */           if (MillCommonUtilities.chanceOn(10)) {
/* 6475 */             Building village = this.mw.getBuilding(p);
/* 6476 */             if (village != null)
/*      */             {
/* 6478 */               int relation = ((Integer)this.relations.get(p)).intValue();
/*      */               
/*      */               int improveChance;
/*      */               int improveChance;
/* 6482 */               if (relation < -90) {
/* 6483 */                 improveChance = 0; } else { int improveChance;
/* 6484 */                 if (relation < -50) {
/* 6485 */                   improveChance = 30; } else { int improveChance;
/* 6486 */                   if (relation < 0) {
/* 6487 */                     improveChance = 40; } else { int improveChance;
/* 6488 */                     if (relation > 90) {
/* 6489 */                       improveChance = 100; } else { int improveChance;
/* 6490 */                       if (relation > 50) {
/* 6491 */                         improveChance = 70;
/*      */                       } else
/* 6493 */                         improveChance = 60;
/*      */                     }
/*      */                   } } }
/* 6496 */               if (MillCommonUtilities.randomInt(100) < improveChance) {
/* 6497 */                 if (((Integer)this.relations.get(p)).intValue() < 100) {
/* 6498 */                   adjustRelation(p, 10 + MillCommonUtilities.randomInt(10), false);
/* 6499 */                   ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '2', "ui.relationfriendly", new String[] { getVillageQualifiedName(), village.getVillageQualifiedName(), MillCommonUtilities.getRelationName(((Integer)this.relations.get(p)).intValue()) });
/*      */                 }
/*      */                 
/*      */               }
/* 6503 */               else if (((Integer)this.relations.get(p)).intValue() > -100) {
/* 6504 */                 adjustRelation(p, -10 - MillCommonUtilities.randomInt(10), false);
/* 6505 */                 ServerSender.sendTranslatedSentenceInRange(this.worldObj, getPos(), MLN.BackgroundRadius, '6', "ui.relationunfriendly", new String[] { getVillageQualifiedName(), village.getVillageQualifiedName(), MillCommonUtilities.getRelationName(((Integer)this.relations.get(p)).intValue()) });
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 6514 */       this.thNightActionPerformed = true;
/*      */     }
/*      */     
/* 6517 */     if ((this.villageType.playerControlled) && (this.worldObj.func_72820_D() % 1000L == 0L) && (countGoods(Mill.parchmentVillageScroll, -1) == 0)) {
/* 6518 */       for (int i = 0; i < this.mw.villagesList.pos.size(); i++) {
/* 6519 */         Point p = (Point)this.mw.villagesList.pos.get(i);
/* 6520 */         if (getPos().sameBlock(p)) {
/* 6521 */           storeGoods(Mill.parchmentVillageScroll, i, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6526 */     if ((this.villageType.playerControlled) && (this.worldObj.func_72820_D() % 1000L == 0L) && (countGoods(Mill.negationWand) == 0)) {
/* 6527 */       storeGoods(Mill.negationWand, 1);
/*      */     }
/*      */     
/* 6530 */     if ((this.controlledBy != null) && (this.controlledBy.length() > 0) && (this.controlledByName == null)) {
/* 6531 */       UserProfile profile = MillCommonUtilities.getServerProfile(this.worldObj, this.controlledBy);
/* 6532 */       this.controlledByName = profile.playerName;
/*      */     }
/*      */     
/* 6535 */     if (this.worldObj.func_72820_D() % 200L == 0L) {
/* 6536 */       updateAchievements();
/*      */     }
/*      */     
/* 6539 */     if (this.autobuildPaths) {
/* 6540 */       clearOldPaths();
/* 6541 */       constructCalculatedPaths();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateTownHallSigns(boolean forced)
/*      */   {
/* 6547 */     if (this.worldObj.field_72995_K) {
/* 6548 */       return;
/*      */     }
/*      */     
/* 6551 */     EntityPlayer player = this.worldObj.func_72977_a(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), 20.0D);
/*      */     
/* 6553 */     if (player == null) {
/* 6554 */       return;
/*      */     }
/*      */     
/* 6557 */     if ((!forced) && (System.currentTimeMillis() - this.lastSignUpdate < 2000L)) {
/* 6558 */       return;
/*      */     }
/*      */     
/* 6561 */     if ((this.resManager.signs.size() == 0) || (this.resManager.signs.get(0) == null)) {
/* 6562 */       return;
/*      */     }
/*      */     
/* 6565 */     for (int i = 0; i < this.resManager.signs.size(); i++) {
/* 6566 */       Point p = (Point)this.resManager.signs.get(i);
/* 6567 */       if ((p != null) && (MillCommonUtilities.getBlock(this.worldObj, p) != Mill.panel)) {
/* 6568 */         int meta = MillCommonUtilities.guessSignMetaData(this.worldObj, p);
/*      */         
/* 6570 */         if (meta > 0) {
/* 6571 */           MillCommonUtilities.setBlockAndMetadata(this.worldObj, p, Mill.panel, meta);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6576 */     TileEntityPanel sign = (TileEntityPanel)this.worldObj.func_147438_o(((Point)this.resManager.signs.get(0)).getiX(), ((Point)this.resManager.signs.get(0)).getiY(), ((Point)this.resManager.signs.get(0)).getiZ());
/*      */     
/* 6578 */     if (sign != null)
/*      */     {
/* 6580 */       int nbvill = 0;
/*      */       
/* 6582 */       for (VillagerRecord vr : this.vrecords) {
/* 6583 */         if (vr != null) {
/* 6584 */           boolean belongsToVillage = (!vr.raidingVillage) && (vr.getType() != null) && (!vr.getType().visitor);
/*      */           
/* 6586 */           if (belongsToVillage) {
/* 6587 */             nbvill++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       String[][] lines;
/*      */       String[][] lines;
/* 6594 */       if ((this.controlledBy != null) && (this.controlledBy.length() > 0)) {
/* 6595 */         lines = new String[][] { { getVillageNameWithoutQualifier() }, { this.qualifier }, { this.villageType.name }, { this.controlledByName } };
/*      */       } else {
/* 6597 */         lines = new String[][] { { getVillageNameWithoutQualifier() }, { this.qualifier }, { this.villageType.name }, { "ui.populationnumber", "" + nbvill } };
/*      */       }
/* 6599 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(0), lines, 1, getPos(), 0L);
/*      */     }
/*      */     
/* 6602 */     if (this.resManager.signs.size() == 1) {
/* 6603 */       return;
/*      */     }
/*      */     
/* 6606 */     BuildingPlan goalPlan = getCurrentGoalBuildingPlan();
/*      */     
/* 6608 */     List<InvItem> res = new ArrayList();
/* 6609 */     List<Integer> resCost = new ArrayList();
/* 6610 */     List<Integer> resHas = new ArrayList();
/*      */     
/* 6612 */     if (goalPlan != null)
/*      */     {
/* 6614 */       for (InvItem key : goalPlan.resCost.keySet()) {
/* 6615 */         res.add(key);
/* 6616 */         resCost.add(goalPlan.resCost.get(key));
/* 6617 */         int has = countGoods(key.getItem(), key.meta);
/* 6618 */         if ((this.builder != null) && (this.buildingLocationIP != null) && (this.buildingLocationIP.planKey.equals(this.buildingGoal))) {
/* 6619 */           has += this.builder.countInv(key.getItem(), key.meta);
/*      */         }
/* 6621 */         if (has > ((Integer)goalPlan.resCost.get(key)).intValue()) {
/* 6622 */           has = ((Integer)goalPlan.resCost.get(key)).intValue();
/*      */         }
/*      */         
/* 6625 */         resHas.add(Integer.valueOf(has));
/*      */       }
/*      */     }
/*      */     
/* 6629 */     for (int i = 1; (i < 4) && (i < this.resManager.signs.size()); i++) {
/* 6630 */       sign = ((Point)this.resManager.signs.get(i)).getPanel(this.worldObj);
/*      */       
/* 6632 */       if (sign != null) {
/*      */         String[][] lines;
/*      */         String[][] lines;
/* 6635 */         if ((goalPlan == null) || (res.size() < i * 2 - 1)) {
/* 6636 */           lines = new String[][] { { "" }, { "" }, { "" }, { "" } };
/*      */         } else { String[][] lines;
/* 6638 */           if ((res.size() > 6) && (i == 3))
/*      */           {
/* 6640 */             lines = new String[][] { { ((InvItem)res.get(i * 2 - 2)).getTranslationKey() }, { "ui.xoutofy", "" + resHas.get(i * 2 - 2), "" + resCost.get(i * 2 - 2) }, { "ui.extraresneeded1", "" + (res.size() - 5) }, { "ui.extraresneeded2", "" + (res.size() - 5) } };
/*      */           } else { String[][] lines;
/* 6642 */             if (res.size() > i * 2 - 1)
/*      */             {
/* 6644 */               lines = new String[][] { { ((InvItem)res.get(i * 2 - 2)).getTranslationKey() }, { "ui.xoutofy", "" + resHas.get(i * 2 - 2), "" + resCost.get(i * 2 - 2) }, { ((InvItem)res.get(i * 2 - 1)).getTranslationKey() }, { "ui.xoutofy", "" + resHas.get(i * 2 - 1), "" + resCost.get(i * 2 - 1) } };
/*      */             }
/*      */             else {
/* 6647 */               lines = new String[][] { { ((InvItem)res.get(i * 2 - 2)).getTranslationKey() }, { "ui.xoutofy", "" + resHas.get(i * 2 - 2), "" + resCost.get(i * 2 - 2) }, { "" }, { "" } };
/*      */             }
/*      */           }
/*      */         }
/* 6651 */         ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(i), lines, 6, getPos(), 0L);
/*      */       }
/*      */     }
/*      */     
/* 6655 */     if (this.resManager.signs.size() < 5) {
/* 6656 */       return;
/*      */     }
/*      */     
/*      */ 
/* 6660 */     sign = ((Point)this.resManager.signs.get(4)).getPanel(this.worldObj);
/*      */     
/* 6662 */     if (sign != null) { String[][] lines;
/*      */       String[][] lines;
/* 6664 */       if (this.buildingGoal == null)
/*      */       {
/* 6666 */         lines = new String[][] { { "" }, { "ui.goalscompleted1" }, { "ui.goalscompleted2" }, { "" } };
/*      */       }
/*      */       else {
/* 6669 */         BuildingPlan goal = getCurrentGoalBuildingPlan();
/*      */         String[] status;
/*      */         String[] status;
/* 6672 */         if ((this.buildingLocationIP != null) && (this.buildingLocationIP.planKey.equals(this.buildingGoal))) { String[] status;
/* 6673 */           if (this.buildingLocationIP.level == 0) {
/* 6674 */             status = new String[] { "ui.inconstruction" };
/*      */           } else {
/* 6676 */             status = new String[] { "ui.upgrading", "" + this.buildingLocationIP.level };
/*      */           }
/*      */         } else {
/* 6679 */           status = new String[] { this.buildingGoalIssue };
/*      */         }
/*      */         
/* 6682 */         lines = new String[][] { { "ui.project" }, { goal.nativeName }, { goal.getGameNameKey() }, status };
/*      */       }
/*      */       int type;
/*      */       int type;
/* 6686 */       if (this.villageType.playerControlled) {
/* 6687 */         type = 4;
/*      */       } else {
/* 6689 */         type = 3;
/*      */       }
/* 6691 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(4), lines, type, getPos(), 0L);
/*      */     }
/*      */     
/* 6694 */     if (this.resManager.signs.size() < 6) {
/* 6695 */       return;
/*      */     }
/*      */     
/*      */ 
/* 6699 */     sign = ((Point)this.resManager.signs.get(5)).getPanel(this.worldObj);
/*      */     
/* 6701 */     if (sign != null) { String[][] lines;
/*      */       String[][] lines;
/* 6703 */       if (this.buildingLocationIP == null) {
/* 6704 */         lines = new String[][] { { "" }, { "ui.noconstruction1" }, { "ui.noconstruction2" }, { "" } };
/*      */       } else {
/* 6706 */         String planName = this.culture.getBuildingPlanSet(this.buildingLocationIP.planKey).getNativeName();
/*      */         String[] status;
/*      */         String[] status;
/* 6709 */         if (this.buildingLocationIP.level == 0) {
/* 6710 */           status = new String[] { "ui.inconstruction" };
/*      */         } else {
/* 6712 */           status = new String[] { "ui.upgrading", "" + this.buildingLocationIP.level };
/*      */         }
/*      */         
/*      */         String[] loc;
/*      */         String[] loc;
/* 6717 */         if (this.buildingLocationIP != null)
/*      */         {
/* 6719 */           int distance = MathHelper.func_76128_c(getPos().distanceTo(this.buildingLocationIP.pos));
/*      */           
/* 6721 */           String direction = getPos().directionTo(this.buildingLocationIP.pos);
/*      */           
/* 6723 */           loc = new String[] { "other.shortdistancedirection", "" + distance, "" + direction };
/*      */         } else {
/* 6725 */           loc = new String[0];
/*      */         }
/*      */         
/*      */         String[] constr;
/*      */         String[] constr;
/* 6730 */         if ((getBblocks() != null) && (getBblocks().length > 0)) {
/* 6731 */           constr = new String[] { "ui.construction", "" + (int)Math.floor(this.bblocksPos * 100 / getBblocks().length) };
/*      */         } else {
/* 6733 */           constr = new String[] { "ui.inconstruction" };
/*      */         }
/*      */         
/* 6736 */         lines = new String[][] { constr, { planName }, status, loc };
/*      */       }
/*      */       
/* 6739 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(5), lines, 2, getPos(), 0L);
/*      */     }
/*      */     
/* 6742 */     if (this.resManager.signs.size() < 7) {
/* 6743 */       return;
/*      */     }
/*      */     
/*      */ 
/* 6747 */     sign = ((Point)this.resManager.signs.get(6)).getPanel(this.worldObj);
/*      */     
/* 6749 */     if (sign != null)
/*      */     {
/* 6751 */       int nbMen = 0;int nbFemale = 0;int nbGrownBoy = 0;int nbGrownGirl = 0;int nbBoy = 0;int nbGirl = 0;
/*      */       
/*      */ 
/*      */ 
/* 6755 */       for (VillagerRecord vr : this.vrecords) {
/* 6756 */         boolean belongsToVillage = (vr.getType() != null) && (!vr.getType().visitor) && (!vr.raidingVillage);
/*      */         
/* 6758 */         if (belongsToVillage)
/*      */         {
/* 6760 */           if (!vr.getType().isChild) {
/* 6761 */             if (vr.gender == 1) {
/* 6762 */               nbMen++;
/*      */             } else {
/* 6764 */               nbFemale++;
/*      */             }
/*      */           }
/* 6767 */           else if (vr.villagerSize == 20) {
/* 6768 */             if (vr.gender == 1) {
/* 6769 */               nbGrownBoy++;
/*      */             } else {
/* 6771 */               nbGrownGirl++;
/*      */             }
/*      */           }
/* 6774 */           else if (vr.gender == 1) {
/* 6775 */             nbBoy++;
/*      */           } else {
/* 6777 */             nbGirl++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 6785 */       String[][] lines = { { "ui.population" }, { "ui.adults", "" + (nbMen + nbFemale), "" + nbMen, "" + nbFemale }, { "ui.teens", "" + (nbGrownBoy + nbGrownGirl), "" + nbGrownBoy, "" + nbGrownGirl }, { "ui.children", "" + (nbBoy + nbGirl), "" + nbBoy, "" + nbGirl } };
/*      */       
/*      */ 
/* 6788 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(6), lines, 1, getPos(), 0L);
/*      */     }
/*      */     
/*      */ 
/* 6792 */     if (this.resManager.signs.size() < 8) {
/* 6793 */       return;
/*      */     }
/*      */     
/*      */ 
/* 6797 */     sign = ((Point)this.resManager.signs.get(7)).getPanel(this.worldObj);
/*      */     
/* 6799 */     if (sign != null)
/*      */     {
/* 6801 */       String[][] lines = { { "ui.villagemap" }, { "" }, { "ui.nbbuildings", "" + this.buildings.size() }, { "" } };
/*      */       
/* 6803 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(7), lines, 8, getPos(), 0L);
/*      */     }
/*      */     
/* 6806 */     if (this.resManager.signs.size() < 9) {
/* 6807 */       return;
/*      */     }
/*      */     
/*      */ 
/* 6811 */     sign = ((Point)this.resManager.signs.get(8)).getPanel(this.worldObj);
/*      */     
/* 6813 */     if (sign != null)
/*      */     {
/* 6815 */       String status = "";
/*      */       
/* 6817 */       if (this.raidTarget != null) {
/* 6818 */         if (this.raidStart > 0L) {
/* 6819 */           status = "panels.raidinprogress";
/*      */         } else {
/* 6821 */           status = "panels.planningraid";
/*      */         }
/*      */       }
/* 6824 */       else if (this.underAttack) {
/* 6825 */         status = "panels.underattack";
/*      */       }
/*      */       
/*      */ 
/* 6829 */       String[][] lines = { { "panels.military" }, { status }, { "panels.offense", "" + getVillageRaidingStrength() }, { "panels.defense", "" + getVillageDefendingStrength() } };
/*      */       
/*      */       int type;
/*      */       int type;
/* 6833 */       if (this.villageType.playerControlled) {
/* 6834 */         type = 13;
/*      */       } else {
/* 6836 */         type = 9;
/*      */       }
/* 6838 */       ServerSender.updatePanel(this.mw, (Point)this.resManager.signs.get(8), lines, type, getPos(), 0L);
/*      */     }
/*      */     
/* 6841 */     this.lastSignUpdate = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   public void updateVillagerRecord(MillVillager v) {
/* 6845 */     VillagerRecord vr = getVillagerRecordById(v.villager_id);
/*      */     
/* 6847 */     if (vr != null) {
/* 6848 */       vr.updateRecord(v);
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateWorldInfo() throws MLN.MillenaireException
/*      */   {
/* 6854 */     if (this.villageType == null) {
/* 6855 */       MLN.error(this, "updateWorldInfo: villageType is null");
/* 6856 */       return;
/*      */     }
/*      */     
/* 6859 */     boolean areaChanged = this.winfo.update(this.worldObj, getLocations(), this.buildingLocationIP, this.location.pos, this.villageType.radius);
/*      */     
/* 6861 */     if (areaChanged) {
/* 6862 */       rebuildPathing(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void validateVillagerList()
/*      */   {
/* 6868 */     for (MillVillager v : this.villagers) {
/* 6869 */       if (v == null) {
/* 6870 */         MLN.error(this, "Null value in villager list");
/*      */       }
/* 6872 */       if ((v.field_70128_L) && (MLN.LogTileEntityBuilding >= 2)) {
/* 6873 */         MLN.minor(this, "Villager " + v + " is dead.");
/*      */       }
/*      */       
/* 6876 */       List<VillagerRecord> found = new ArrayList();
/* 6877 */       for (VillagerRecord vr : this.vrecords) {
/* 6878 */         if (vr.matches(v)) {
/* 6879 */           found.add(vr);
/*      */         }
/*      */       }
/*      */       
/* 6883 */       if (found.size() == 0) {
/* 6884 */         MLN.error(this, "Villager " + v + " not present in records.");
/* 6885 */       } else if (found.size() > 1) {
/* 6886 */         MLN.error(this, "Villager " + v + " present " + found.size() + " times in records: ");
/* 6887 */         for (VillagerRecord vr : found) {
/* 6888 */           MLN.major(this, vr.toString() + " / " + vr.hashCode());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6893 */     for (VillagerRecord vr : this.vrecords) {
/* 6894 */       List<MillVillager> found = new ArrayList();
/*      */       
/* 6896 */       if (vr.housePos == null) {
/* 6897 */         MLN.error(this, "Record " + vr + " has no house.");
/*      */       }
/*      */       
/* 6900 */       for (MillVillager v : this.villagers) {
/* 6901 */         if (vr.matches(v)) {
/* 6902 */           found.add(v);
/*      */         }
/*      */       }
/* 6905 */       if (found.size() != vr.nb) {
/* 6906 */         MLN.error(this, "Record " + vr + " present " + found.size() + " times in villagers, previously: " + vr.nb + ". Villagers: ");
/* 6907 */         for (MillVillager v : found) {
/* 6908 */           MLN.major(this, v.toString() + " / " + v.hashCode());
/*      */         }
/* 6910 */         vr.nb = found.size();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeBblocks()
/*      */   {
/* 6917 */     File buildingsDir = MillCommonUtilities.getBuildingsDir(this.worldObj);
/*      */     
/* 6919 */     File file1 = new File(buildingsDir, getPos().getPathString() + "_bblocks.bin");
/*      */     
/* 6921 */     BuildingBlock[] blocks = getBblocks();
/*      */     
/* 6923 */     if (blocks != null) {
/*      */       try {
/* 6925 */         FileOutputStream fos = new FileOutputStream(file1);
/*      */         
/* 6927 */         DataOutputStream ds = new DataOutputStream(fos);
/*      */         
/* 6929 */         ds.writeInt(blocks.length);
/*      */         
/* 6931 */         for (int i = 0; i < blocks.length; i++) {
/* 6932 */           BuildingBlock b = blocks[i];
/* 6933 */           ds.writeInt(b.p.getiX());
/* 6934 */           ds.writeShort(b.p.getiY());
/* 6935 */           ds.writeInt(b.p.getiZ());
/* 6936 */           ds.writeInt(MillCommonUtilities.getBlockId(b.block));
/* 6937 */           ds.writeByte(b.meta);
/* 6938 */           ds.writeByte(b.special);
/*      */         }
/*      */         
/* 6941 */         ds.close();
/* 6942 */         fos.close();
/*      */       }
/*      */       catch (IOException e) {
/* 6945 */         MLN.printException("Error when writing bblocks: ", e);
/*      */       }
/*      */     } else {
/* 6948 */       file1.delete();
/*      */     }
/* 6950 */     this.bblocksChanged = false;
/*      */   }
/*      */   
/*      */   private void writePaths() {
/* 6954 */     File buildingsDir = MillCommonUtilities.getBuildingsDir(this.worldObj);
/*      */     
/* 6956 */     File file1 = new File(buildingsDir, getPos().getPathString() + "_paths.bin");
/*      */     
/* 6958 */     if (this.pathsToBuild != null) {
/*      */       try
/*      */       {
/* 6961 */         FileOutputStream fos = new FileOutputStream(file1);
/*      */         
/* 6963 */         DataOutputStream ds = new DataOutputStream(fos);
/*      */         
/* 6965 */         ds.writeInt(this.pathsToBuild.size());
/*      */         
/* 6967 */         for (List<BuildingBlock> path : this.pathsToBuild) {
/* 6968 */           ds.writeInt(path.size());
/*      */           
/* 6970 */           for (BuildingBlock b : path) {
/* 6971 */             ds.writeInt(b.p.getiX());
/* 6972 */             ds.writeShort(b.p.getiY());
/* 6973 */             ds.writeInt(b.p.getiZ());
/* 6974 */             ds.writeInt(Block.func_149682_b(b.block));
/* 6975 */             ds.writeByte(b.meta);
/* 6976 */             ds.writeByte(b.special);
/*      */           }
/*      */         }
/* 6979 */         ds.close();
/* 6980 */         fos.close();
/*      */       }
/*      */       catch (IOException e) {
/* 6983 */         MLN.printException("Error when writing pathsToBuild: ", e);
/*      */       }
/*      */     } else {
/* 6986 */       file1.delete();
/*      */     }
/*      */     
/* 6989 */     file1 = new File(buildingsDir, getPos().getPathString() + "_pathstoclear.bin");
/*      */     
/* 6991 */     if (this.oldPathPointsToClear != null) {
/*      */       try {
/* 6993 */         FileOutputStream fos = new FileOutputStream(file1);
/*      */         
/* 6995 */         DataOutputStream ds = new DataOutputStream(fos);
/*      */         
/* 6997 */         ds.writeInt(this.oldPathPointsToClear.size());
/*      */         
/* 6999 */         for (Point p : this.oldPathPointsToClear) {
/* 7000 */           ds.writeInt(p.getiX());
/* 7001 */           ds.writeShort(p.getiY());
/* 7002 */           ds.writeInt(p.getiZ());
/*      */         }
/* 7004 */         ds.close();
/* 7005 */         fos.close();
/*      */       }
/*      */       catch (IOException e) {
/* 7008 */         MLN.printException("Error when writing oldPathPointsToClear: ", e);
/*      */       }
/*      */     } else {
/* 7011 */       file1.delete();
/*      */     }
/*      */     
/* 7014 */     this.pathsChanged = false;
/*      */   }
/*      */   
/*      */   public void writeToNBT(NBTTagCompound nbttagcompound)
/*      */   {
/* 7019 */     if (this.location == null) {
/* 7020 */       MLN.error(this, "Null location. Skipping write.");
/* 7021 */       return;
/*      */     }
/*      */     
/* 7024 */     nbttagcompound.func_74778_a("versionCompatibility", "1.0");
/*      */     
/*      */     try
/*      */     {
/* 7028 */       this.pos.write(nbttagcompound, "pos");
/*      */       
/* 7030 */       this.location.write(nbttagcompound, "buildingLocation", "self");
/*      */       
/* 7032 */       nbttagcompound.func_74757_a("chestLocked", this.chestLocked);
/*      */       
/* 7034 */       if ((this.name != null) && (this.name.length() > 0)) {
/* 7035 */         nbttagcompound.func_74778_a("name", this.name);
/*      */       }
/* 7037 */       nbttagcompound.func_74778_a("qualifier", this.qualifier);
/* 7038 */       nbttagcompound.func_74757_a("isTownhall", this.isTownhall);
/* 7039 */       nbttagcompound.func_74778_a("culture", this.culture.key);
/*      */       
/* 7041 */       if (this.villageType != null) {
/* 7042 */         nbttagcompound.func_74778_a("villageType", this.villageType.key);
/*      */       }
/*      */       
/* 7045 */       if (this.controlledBy != null) {
/* 7046 */         nbttagcompound.func_74778_a("controlledBy", this.controlledBy);
/*      */       }
/*      */       
/* 7049 */       if (this.townHallPos != null) {
/* 7050 */         this.townHallPos.write(nbttagcompound, "townHallPos");
/*      */       }
/*      */       
/* 7053 */       nbttagcompound.func_74757_a("nightActionPerformed", this.thNightActionPerformed);
/* 7054 */       nbttagcompound.func_74757_a("nightBackgroundActionPerformed", this.nightBackgroundActionPerformed);
/*      */       
/*      */ 
/*      */ 
/* 7058 */       nbttagcompound.func_74768_a("nbAnimalsRespawned", this.nbAnimalsRespawned);
/*      */       
/* 7060 */       NBTTagList nbttaglist = new NBTTagList();
/* 7061 */       for (Point p : this.buildings) {
/* 7062 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7063 */         p.write(nbttagcompound1, "pos");
/* 7064 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/* 7066 */       nbttagcompound.func_74782_a("buildings", nbttaglist);
/*      */       
/* 7068 */       nbttagcompound.func_74768_a("bblocksPos", this.bblocksPos);
/*      */       
/* 7070 */       if (this.bblocksChanged) {
/* 7071 */         writeBblocks();
/* 7072 */         if (MLN.LogHybernation >= 1) {
/* 7073 */           MLN.major(this, "Saved bblocks.");
/*      */         }
/*      */       }
/*      */       
/* 7077 */       nbttaglist = new NBTTagList();
/* 7078 */       for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 7079 */         if (this.buildingProjects.containsKey(ep)) {
/* 7080 */           List<BuildingProject> projectsLevel = (List)this.buildingProjects.get(ep);
/* 7081 */           for (BuildingProject project : projectsLevel) {
/* 7082 */             if (project.location != null) {
/* 7083 */               NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7084 */               project.location.write(nbttagcompound1, "location", "buildingProjects");
/* 7085 */               nbttaglist.func_74742_a(nbttagcompound1);
/* 7086 */               if (MLN.LogHybernation >= 1) {
/* 7087 */                 MLN.major(this, "Writing building location: " + project.location + " (level: " + project.location.level + ", variation: " + project.location.getVariation() + ")");
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 7093 */       nbttagcompound.func_74782_a("locations", nbttaglist);
/*      */       
/* 7095 */       if (this.buildingGoal != null) {
/* 7096 */         nbttagcompound.func_74778_a("buildingGoal", this.buildingGoal);
/* 7097 */         if (MLN.LogHybernation >= 1) {
/* 7098 */           MLN.major(this, "Writing building goal: " + this.buildingGoal);
/*      */         }
/*      */       }
/* 7101 */       nbttagcompound.func_74768_a("buildingGoalLevel", this.buildingGoalLevel);
/* 7102 */       nbttagcompound.func_74768_a("buildingGoalVariation", this.buildingGoalVariation);
/*      */       
/* 7104 */       if (this.buildingGoalIssue != null) {
/* 7105 */         nbttagcompound.func_74778_a("buildingGoalIssue", this.buildingGoalIssue);
/*      */       }
/* 7107 */       if (this.buildingGoalLocation != null) {
/* 7108 */         this.buildingGoalLocation.write(nbttagcompound, "buildingGoalLocation", "buildingGoalLocation");
/* 7109 */         if (MLN.LogHybernation >= 1) {
/* 7110 */           MLN.major(this, "Writing buildingGoalLocation: " + this.buildingGoalLocation);
/*      */         }
/*      */       }
/*      */       
/* 7114 */       if (this.buildingLocationIP != null) {
/* 7115 */         this.buildingLocationIP.write(nbttagcompound, "buildingLocationIP", "buildingLocationIP");
/*      */         
/* 7117 */         if (MLN.LogHybernation >= 1) {
/* 7118 */           MLN.major(this, "Writing buildingLocationIP: " + this.buildingLocationIP);
/*      */         }
/*      */       }
/*      */       
/* 7122 */       nbttaglist = new NBTTagList();
/* 7123 */       for (VillagerRecord vr : this.vrecords) {
/* 7124 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7125 */         vr.write(nbttagcompound1, "vr");
/* 7126 */         nbttaglist.func_74742_a(nbttagcompound1);
/* 7127 */         if (MLN.LogHybernation >= 3) {
/* 7128 */           MLN.debug(this, "Writing VR: " + vr);
/*      */         }
/*      */       }
/* 7131 */       nbttagcompound.func_74782_a("villagersrecords", nbttaglist);
/*      */       
/* 7133 */       nbttaglist = new NBTTagList();
/* 7134 */       for (String s : this.visitorsList) {
/* 7135 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7136 */         nbttagcompound1.func_74778_a("visitor", s);
/* 7137 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/* 7139 */       nbttagcompound.func_74782_a("visitorsList", nbttaglist);
/*      */       
/* 7141 */       nbttaglist = new NBTTagList();
/* 7142 */       for (String s : this.buildingsBought) {
/* 7143 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7144 */         nbttagcompound1.func_74778_a("key", s);
/* 7145 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/* 7147 */       nbttagcompound.func_74782_a("buildingsBought", nbttaglist);
/*      */       
/* 7149 */       nbttagcompound.func_74757_a("updateRaidPerformed", this.updateRaidPerformed);
/* 7150 */       nbttagcompound.func_74757_a("nightBackgroundActionPerformed", this.nightBackgroundActionPerformed);
/* 7151 */       nbttagcompound.func_74757_a("nightActionPerformed", this.thNightActionPerformed);
/* 7152 */       nbttagcompound.func_74757_a("underAttack", this.underAttack);
/*      */       
/* 7154 */       if (this.raidTarget != null) {
/* 7155 */         this.raidTarget.write(nbttagcompound, "raidTarget");
/* 7156 */         nbttagcompound.func_74772_a("raidPlanningStart", this.raidPlanningStart);
/* 7157 */         nbttagcompound.func_74772_a("raidStart", this.raidStart);
/*      */       }
/*      */       
/* 7160 */       nbttaglist = new NBTTagList();
/* 7161 */       for (String s : this.raidsPerformed) {
/* 7162 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7163 */         nbttagcompound1.func_74778_a("raid", s);
/* 7164 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/* 7166 */       nbttagcompound.func_74782_a("raidsPerformed", nbttaglist);
/*      */       
/* 7168 */       nbttaglist = new NBTTagList();
/* 7169 */       for (String s : this.raidsSuffered) {
/* 7170 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7171 */         nbttagcompound1.func_74778_a("raid", s);
/* 7172 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/* 7174 */       nbttagcompound.func_74782_a("raidsTaken", nbttaglist);
/*      */       
/* 7176 */       if ((this.villageType != null) && (!this.villageType.lonebuilding))
/*      */       {
/* 7178 */         nbttaglist = new NBTTagList();
/* 7179 */         for (Point p : this.relations.keySet())
/*      */         {
/* 7181 */           Building dv = this.mw.getBuilding(p);
/*      */           
/* 7183 */           if ((dv != null) && (!dv.villageType.lonebuilding)) {
/* 7184 */             NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7185 */             p.write(nbttagcompound1, "pos");
/* 7186 */             nbttagcompound1.func_74768_a("value", ((Integer)this.relations.get(p)).intValue());
/* 7187 */             nbttaglist.func_74742_a(nbttagcompound1);
/*      */           }
/*      */         }
/* 7190 */         nbttagcompound.func_74782_a("relations", nbttaglist);
/*      */       }
/*      */       
/* 7193 */       if (this.parentVillage != null) {
/* 7194 */         this.parentVillage.write(nbttagcompound, "parentVillage");
/*      */       }
/*      */       
/* 7197 */       nbttaglist = MillCommonUtilities.writeInventory(this.imported);
/* 7198 */       nbttagcompound.func_74782_a("importedGoodsNew", nbttaglist);
/*      */       
/* 7200 */       nbttaglist = MillCommonUtilities.writeInventory(this.exported);
/* 7201 */       nbttagcompound.func_74782_a("exportedGoodsNew", nbttaglist);
/*      */       
/* 7203 */       if (MLN.LogTileEntityBuilding >= 3) {
/* 7204 */         MLN.debug(this, "Saving building. Location: " + this.location + ", pos: " + getPos());
/*      */       }
/*      */       
/* 7207 */       nbttaglist = new NBTTagList();
/* 7208 */       for (Point p : this.subBuildings) {
/* 7209 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 7210 */         p.write(nbttagcompound1, "pos");
/* 7211 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/*      */       
/* 7214 */       nbttagcompound.func_74782_a("subBuildings", nbttaglist);
/*      */       
/* 7216 */       if (this.pujas != null) {
/* 7217 */         NBTTagCompound tag = new NBTTagCompound();
/* 7218 */         this.pujas.writeToNBT(tag);
/* 7219 */         nbttagcompound.func_74782_a("pujas", tag);
/*      */       }
/*      */       
/* 7222 */       nbttagcompound.func_74772_a("lastGoodsRefresh", this.lastGoodsRefresh);
/*      */       
/* 7224 */       nbttagcompound.func_74768_a("pathsToBuildIndex", this.pathsToBuildIndex);
/* 7225 */       nbttagcompound.func_74768_a("pathsToBuildPathIndex", this.pathsToBuildPathIndex);
/* 7226 */       nbttagcompound.func_74768_a("oldPathPointsToClearIndex", this.oldPathPointsToClearIndex);
/*      */       
/* 7228 */       this.resManager.writeToNBT(nbttagcompound);
/*      */       
/* 7230 */       if (this.pathsChanged) {
/* 7231 */         writePaths();
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/* 7235 */       Mill.proxy.sendChatAdmin("Error when trying to save building. Check millenaire.log.");
/* 7236 */       MLN.error(this, "Exception in Villager.onUpdate(): ");
/* 7237 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\Building.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
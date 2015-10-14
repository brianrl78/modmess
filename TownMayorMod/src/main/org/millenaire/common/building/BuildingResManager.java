/*      */ package org.millenaire.common.building;
/*      */ 
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockCocoa;
/*      */ import net.minecraft.block.BlockLog;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.tileentity.TileEntityChest;
/*      */ import net.minecraft.world.World;
/*      */ import org.millenaire.common.InvItem;
/*      */ import org.millenaire.common.MLN;
/*      */ import org.millenaire.common.MLN.MillenaireException;
/*      */ import org.millenaire.common.MillWorld;
/*      */ import org.millenaire.common.Point;
/*      */ import org.millenaire.common.TileEntityMillChest;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.network.StreamReadWrite;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BuildingResManager
/*      */ {
/*   32 */   public List<Point> brickspot = new ArrayList();
/*   33 */   public List<Point> chests = new ArrayList();
/*      */   
/*   35 */   public List<Point> fishingspots = new ArrayList();
/*   36 */   public List<Point> sugarcanesoils = new ArrayList();
/*   37 */   public List<Point> healingspots = new ArrayList();
/*   38 */   public List<Point> furnaces = new ArrayList();
/*   39 */   public List<Point> brewingStands = new ArrayList();
/*      */   
/*   41 */   public List<Point> signs = new ArrayList();
/*   42 */   public List<List<Point>> sources = new ArrayList();
/*   43 */   public List<Block> sourceTypes = new ArrayList();
/*   44 */   public List<List<Point>> spawns = new ArrayList();
/*   45 */   public List<String> spawnTypes = new ArrayList();
/*   46 */   public List<List<Point>> mobSpawners = new ArrayList();
/*   47 */   public List<String> mobSpawnerTypes = new ArrayList();
/*   48 */   public List<List<Point>> soils = new ArrayList();
/*   49 */   public List<String> soilTypes = new ArrayList();
/*      */   
/*   51 */   public List<Point> stalls = new ArrayList();
/*      */   
/*   53 */   public List<Point> woodspawn = new ArrayList();
/*      */   
/*   55 */   public List<Point> netherwartsoils = new ArrayList();
/*      */   
/*   57 */   public List<Point> silkwormblock = new ArrayList();
/*   58 */   public List<Point> dispenderUnknownPowder = new ArrayList();
/*      */   
/*   60 */   private Point sleepingPos = null; private Point sellingPos = null; private Point craftingPos = null; private Point defendingPos = null; private Point shelterPos = null; private Point pathStartPos = null; private Point leasurePos = null;
/*      */   private final Building building;
/*      */   
/*      */   public BuildingResManager(Building b)
/*      */   {
/*   65 */     this.building = b;
/*      */   }
/*      */   
/*      */   public void addMobSpawnerPoint(String type, Point p) {
/*   69 */     if (!this.mobSpawnerTypes.contains(type)) {
/*   70 */       List<Point> spawnsPoint = new ArrayList();
/*   71 */       spawnsPoint.add(p);
/*   72 */       this.mobSpawners.add(spawnsPoint);
/*   73 */       this.mobSpawnerTypes.add(type);
/*      */     } else {
/*   75 */       for (int i = 0; i < this.mobSpawnerTypes.size(); i++) {
/*   76 */         if ((((String)this.mobSpawnerTypes.get(i)).equals(type)) && 
/*   77 */           (!((List)this.mobSpawners.get(i)).contains(p))) {
/*   78 */           ((List)this.mobSpawners.get(i)).add(p);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void addSoilPoint(String type, Point p)
/*      */   {
/*   86 */     if (!this.soilTypes.contains(type)) {
/*   87 */       List<Point> spawnsPoint = new ArrayList();
/*   88 */       spawnsPoint.add(p);
/*   89 */       this.soils.add(spawnsPoint);
/*   90 */       this.soilTypes.add(type);
/*      */     } else {
/*   92 */       for (int i = 0; i < this.soilTypes.size(); i++) {
/*   93 */         if ((((String)this.soilTypes.get(i)).equals(type)) && 
/*   94 */           (!((List)this.soils.get(i)).contains(p))) {
/*   95 */           ((List)this.soils.get(i)).add(p);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void addSourcePoint(Block block, Point p)
/*      */   {
/*  103 */     if (!this.sourceTypes.contains(block)) {
/*  104 */       List<Point> spawnsPoint = new ArrayList();
/*  105 */       spawnsPoint.add(p);
/*  106 */       this.sources.add(spawnsPoint);
/*  107 */       this.sourceTypes.add(block);
/*      */     } else {
/*  109 */       for (int i = 0; i < this.sourceTypes.size(); i++) {
/*  110 */         if ((((Block)this.sourceTypes.get(i)).equals(block)) && 
/*  111 */           (!((List)this.sources.get(i)).contains(p))) {
/*  112 */           ((List)this.sources.get(i)).add(p);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void addSpawnPoint(String type, Point p)
/*      */   {
/*  120 */     if (!this.spawnTypes.contains(type)) {
/*  121 */       List<Point> spawnsPoint = new ArrayList();
/*  122 */       spawnsPoint.add(p);
/*  123 */       this.spawns.add(spawnsPoint);
/*  124 */       this.spawnTypes.add(type);
/*      */     } else {
/*  126 */       for (int i = 0; i < this.spawnTypes.size(); i++) {
/*  127 */         if ((((String)this.spawnTypes.get(i)).equals(type)) && 
/*  128 */           (!((List)this.spawns.get(i)).contains(p))) {
/*  129 */           ((List)this.spawns.get(i)).add(p);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public HashMap<InvItem, Integer> getChestsContent()
/*      */   {
/*  138 */     HashMap<InvItem, Integer> contents = new HashMap();
/*      */     
/*  140 */     for (Point p : this.chests) {
/*  141 */       TileEntityChest chest = p.getMillChest(this.building.worldObj);
/*  142 */       if (chest != null) {
/*  143 */         for (int i = 0; i < chest.func_70302_i_(); i++) {
/*  144 */           ItemStack stack = chest.func_70301_a(i);
/*  145 */           if (stack != null) {
/*      */             try
/*      */             {
/*  148 */               InvItem key = new InvItem(stack);
/*  149 */               if (stack != null) {
/*  150 */                 if (contents.containsKey(key)) {
/*  151 */                   contents.put(key, Integer.valueOf(stack.field_77994_a + ((Integer)contents.get(key)).intValue()));
/*      */                 } else {
/*  153 */                   contents.put(key, Integer.valueOf(stack.field_77994_a));
/*      */                 }
/*      */               }
/*      */             } catch (MLN.MillenaireException e) {
/*  157 */               MLN.printException(e);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  163 */     return contents;
/*      */   }
/*      */   
/*      */   public Point getCocoaHarvestLocation() {
/*  167 */     for (int i = 0; i < this.soilTypes.size(); i++) {
/*  168 */       if (((String)this.soilTypes.get(i)).equals("cacao")) {
/*  169 */         for (Point p : (List)this.soils.get(i)) {
/*  170 */           if (p.getBlock(this.building.worldObj) == Blocks.field_150375_by) {
/*  171 */             int meta = p.getMeta(this.building.worldObj);
/*      */             
/*  173 */             if (BlockCocoa.func_149987_c(meta) >= 2) {
/*  174 */               return p;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  182 */     return null;
/*      */   }
/*      */   
/*      */   public Point getCocoaPlantingLocation() {
/*  186 */     for (int i = 0; i < this.soilTypes.size(); i++) {
/*  187 */       if (((String)this.soilTypes.get(i)).equals("cacao")) {
/*  188 */         for (Point p : (List)this.soils.get(i)) {
/*  189 */           if (p.getBlock(this.building.worldObj) == Blocks.field_150350_a) {
/*  190 */             if ((p.getNorth().getBlock(this.building.worldObj) == Blocks.field_150364_r) && (BlockLog.func_150165_c(p.getNorth().getMeta(this.building.worldObj)) == 3)) {
/*  191 */               return p;
/*      */             }
/*  193 */             if ((p.getEast().getBlock(this.building.worldObj) == Blocks.field_150364_r) && (BlockLog.func_150165_c(p.getEast().getMeta(this.building.worldObj)) == 3)) {
/*  194 */               return p;
/*      */             }
/*  196 */             if ((p.getSouth().getBlock(this.building.worldObj) == Blocks.field_150364_r) && (BlockLog.func_150165_c(p.getSouth().getMeta(this.building.worldObj)) == 3)) {
/*  197 */               return p;
/*      */             }
/*  199 */             if ((p.getWest().getBlock(this.building.worldObj) == Blocks.field_150364_r) && (BlockLog.func_150165_c(p.getWest().getMeta(this.building.worldObj)) == 3)) {
/*  200 */               return p;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  208 */     return null;
/*      */   }
/*      */   
/*      */   public Point getCraftingPos() {
/*  212 */     if (this.craftingPos != null) {
/*  213 */       return this.craftingPos;
/*      */     }
/*      */     
/*  216 */     if (this.sellingPos != null) {
/*  217 */       return this.sellingPos;
/*      */     }
/*      */     
/*  220 */     return this.sleepingPos;
/*      */   }
/*      */   
/*      */   public Point getDefendingPos()
/*      */   {
/*  225 */     if (this.defendingPos != null) {
/*  226 */       return this.defendingPos;
/*      */     }
/*      */     
/*  229 */     if (this.sellingPos != null) {
/*  230 */       return this.sellingPos;
/*      */     }
/*      */     
/*  233 */     return this.sleepingPos;
/*      */   }
/*      */   
/*      */   public Point getEmptyBrickLocation() {
/*  237 */     if (this.brickspot.size() == 0) {
/*  238 */       return null;
/*      */     }
/*      */     
/*  241 */     int start = MillCommonUtilities.randomInt(this.brickspot.size());
/*  242 */     for (int i = start; i < this.brickspot.size(); i++) {
/*  243 */       Point p = (Point)this.brickspot.get(i);
/*  244 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p) == Blocks.field_150350_a) {
/*  245 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  249 */     for (int i = 0; i < start; i++) {
/*  250 */       Point p = (Point)this.brickspot.get(i);
/*  251 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p) == Blocks.field_150350_a) {
/*  252 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  256 */     return null;
/*      */   }
/*      */   
/*      */   public Point getFullBrickLocation() {
/*  260 */     if (this.brickspot.size() == 0) {
/*  261 */       return null;
/*      */     }
/*      */     
/*  264 */     int start = MillCommonUtilities.randomInt(this.brickspot.size());
/*  265 */     for (int i = start; i < this.brickspot.size(); i++) {
/*  266 */       Point p = (Point)this.brickspot.get(i);
/*  267 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.stone_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 1)) {
/*  268 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  272 */     for (int i = 0; i < start; i++) {
/*  273 */       Point p = (Point)this.brickspot.get(i);
/*  274 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.stone_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 1)) {
/*  275 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  279 */     return null;
/*      */   }
/*      */   
/*      */   public Point getLeasurePos()
/*      */   {
/*  284 */     if (this.leasurePos != null) {
/*  285 */       return this.leasurePos;
/*      */     }
/*      */     
/*  288 */     return getSellingPos();
/*      */   }
/*      */   
/*      */   public int getNbEmptyBrickLocation() {
/*  292 */     if (this.brickspot.size() == 0) {
/*  293 */       return 0;
/*      */     }
/*      */     
/*  296 */     int nb = 0;
/*      */     
/*  298 */     for (int i = 0; i < this.brickspot.size(); i++) {
/*  299 */       Point p = (Point)this.brickspot.get(i);
/*  300 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p) == Blocks.field_150350_a) {
/*  301 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  305 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbFullBrickLocation() {
/*  309 */     if (this.brickspot.size() == 0) {
/*  310 */       return 0;
/*      */     }
/*      */     
/*  313 */     int nb = 0;
/*      */     
/*  315 */     for (int i = 0; i < this.brickspot.size(); i++) {
/*  316 */       Point p = (Point)this.brickspot.get(i);
/*  317 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.stone_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 1)) {
/*  318 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  322 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbNetherWartHarvestLocation() {
/*  326 */     if (this.netherwartsoils.size() == 0) {
/*  327 */       return 0;
/*      */     }
/*      */     
/*  330 */     int nb = 0;
/*  331 */     for (int i = 0; i < this.netherwartsoils.size(); i++) {
/*  332 */       Point p = (Point)this.netherwartsoils.get(i);
/*  333 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150388_bm) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p.getAbove()) >= 3)) {
/*  334 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  338 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbNetherWartPlantingLocation() {
/*  342 */     if (this.netherwartsoils.size() == 0) {
/*  343 */       return 0;
/*      */     }
/*      */     
/*  346 */     int nb = 0;
/*  347 */     for (int i = 0; i < this.netherwartsoils.size(); i++) {
/*  348 */       Point p = (Point)this.netherwartsoils.get(i);
/*  349 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) {
/*  350 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  354 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbSilkWormHarvestLocation() {
/*  358 */     if (this.silkwormblock.size() == 0) {
/*  359 */       return 0;
/*      */     }
/*      */     
/*  362 */     int nb = 0;
/*  363 */     for (int i = 0; i < this.silkwormblock.size(); i++) {
/*  364 */       Point p = (Point)this.silkwormblock.get(i);
/*  365 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.wood_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 4)) {
/*  366 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  370 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbSugarCaneHarvestLocation() {
/*  374 */     if (this.sugarcanesoils.size() == 0) {
/*  375 */       return 0;
/*      */     }
/*      */     
/*  378 */     int nb = 0;
/*  379 */     for (int i = 0; i < this.sugarcanesoils.size(); i++) {
/*  380 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  381 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getRelative(0.0D, 2.0D, 0.0D)) == Blocks.field_150436_aH) {
/*  382 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  386 */     return nb;
/*      */   }
/*      */   
/*      */   public int getNbSugarCanePlantingLocation() {
/*  390 */     if (this.sugarcanesoils.size() == 0) {
/*  391 */       return 0;
/*      */     }
/*      */     
/*  394 */     int nb = 0;
/*  395 */     for (int i = 0; i < this.sugarcanesoils.size(); i++) {
/*  396 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  397 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) {
/*  398 */         nb++;
/*      */       }
/*      */     }
/*      */     
/*  402 */     return nb;
/*      */   }
/*      */   
/*      */   public Point getNetherWartsHarvestLocation() {
/*  406 */     if (this.netherwartsoils.size() == 0) {
/*  407 */       return null;
/*      */     }
/*      */     
/*  410 */     int start = MillCommonUtilities.randomInt(this.netherwartsoils.size());
/*  411 */     for (int i = start; i < this.netherwartsoils.size(); i++) {
/*  412 */       Point p = (Point)this.netherwartsoils.get(i);
/*  413 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150388_bm) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p.getAbove()) == 3)) {
/*  414 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  418 */     for (int i = 0; i < start; i++) {
/*  419 */       Point p = (Point)this.netherwartsoils.get(i);
/*  420 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150388_bm) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p.getAbove()) == 3)) {
/*  421 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  425 */     return null;
/*      */   }
/*      */   
/*      */   public Point getNetherWartsPlantingLocation() {
/*  429 */     if (this.netherwartsoils.size() == 0) {
/*  430 */       return null;
/*      */     }
/*      */     
/*  433 */     int start = MillCommonUtilities.randomInt(this.netherwartsoils.size());
/*  434 */     for (int i = start; i < this.netherwartsoils.size(); i++) {
/*  435 */       Point p = (Point)this.netherwartsoils.get(i);
/*  436 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) && (MillCommonUtilities.getBlock(this.building.worldObj, p) == Blocks.field_150425_aM)) {
/*  437 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  441 */     for (int i = 0; i < start; i++) {
/*  442 */       Point p = (Point)this.netherwartsoils.get(i);
/*  443 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) && (MillCommonUtilities.getBlock(this.building.worldObj, p) == Blocks.field_150425_aM)) {
/*  444 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  448 */     return null;
/*      */   }
/*      */   
/*      */   public Point getPathStartPos()
/*      */   {
/*  453 */     if (this.pathStartPos != null) {
/*  454 */       return this.pathStartPos;
/*      */     }
/*      */     
/*  457 */     return getSellingPos();
/*      */   }
/*      */   
/*      */   public Point getPlantingLocation() {
/*  461 */     for (Point p : this.woodspawn) {
/*  462 */       Block block = MillCommonUtilities.getBlock(this.building.worldObj, p);
/*  463 */       if ((block == Blocks.field_150350_a) || (block == Blocks.field_150433_aE)) {
/*  464 */         return p;
/*      */       }
/*      */     }
/*  467 */     return null;
/*      */   }
/*      */   
/*      */   public Point getSellingPos() {
/*  471 */     if (this.sellingPos != null) {
/*  472 */       return this.sellingPos;
/*      */     }
/*      */     
/*  475 */     return this.sleepingPos;
/*      */   }
/*      */   
/*      */ 
/*      */   public Point getShelterPos()
/*      */   {
/*  481 */     if (this.shelterPos != null) {
/*  482 */       return this.shelterPos;
/*      */     }
/*      */     
/*  485 */     return this.sleepingPos;
/*      */   }
/*      */   
/*      */   public Point getSilkwormHarvestLocation() {
/*  489 */     if (this.silkwormblock.size() == 0) {
/*  490 */       return null;
/*      */     }
/*      */     
/*  493 */     int start = MillCommonUtilities.randomInt(this.silkwormblock.size());
/*  494 */     for (int i = start; i < this.silkwormblock.size(); i++) {
/*  495 */       Point p = (Point)this.silkwormblock.get(i);
/*  496 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.wood_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 4)) {
/*  497 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  501 */     for (int i = 0; i < start; i++) {
/*  502 */       Point p = (Point)this.silkwormblock.get(i);
/*  503 */       if ((MillCommonUtilities.getBlock(this.building.worldObj, p) == Mill.wood_decoration) && (MillCommonUtilities.getBlockMeta(this.building.worldObj, p) == 4)) {
/*  504 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  508 */     return null;
/*      */   }
/*      */   
/*      */   public Point getSleepingPos() {
/*  512 */     return this.sleepingPos;
/*      */   }
/*      */   
/*      */   public List<Point> getSoilPoints(String type)
/*      */   {
/*  517 */     for (int i = 0; i < this.soilTypes.size(); i++) {
/*  518 */       if (((String)this.soilTypes.get(i)).equals(type)) {
/*  519 */         return (List)this.soils.get(i);
/*      */       }
/*      */     }
/*      */     
/*  523 */     return null;
/*      */   }
/*      */   
/*      */   public Point getSugarCaneHarvestLocation() {
/*  527 */     if (this.sugarcanesoils.size() == 0) {
/*  528 */       return null;
/*      */     }
/*      */     
/*  531 */     int start = MillCommonUtilities.randomInt(this.sugarcanesoils.size());
/*  532 */     for (int i = start; i < this.sugarcanesoils.size(); i++) {
/*  533 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  534 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getRelative(0.0D, 2.0D, 0.0D)) == Blocks.field_150436_aH) {
/*  535 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  539 */     for (int i = 0; i < start; i++) {
/*  540 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  541 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getRelative(0.0D, 2.0D, 0.0D)) == Blocks.field_150436_aH) {
/*  542 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  546 */     return null;
/*      */   }
/*      */   
/*      */   public Point getSugarCanePlantingLocation() {
/*  550 */     if (this.sugarcanesoils.size() == 0) {
/*  551 */       return null;
/*      */     }
/*      */     
/*  554 */     int start = MillCommonUtilities.randomInt(this.sugarcanesoils.size());
/*  555 */     for (int i = start; i < this.sugarcanesoils.size(); i++) {
/*  556 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  557 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) {
/*  558 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  562 */     for (int i = 0; i < start; i++) {
/*  563 */       Point p = (Point)this.sugarcanesoils.get(i);
/*  564 */       if (MillCommonUtilities.getBlock(this.building.worldObj, p.getAbove()) == Blocks.field_150350_a) {
/*  565 */         return p;
/*      */       }
/*      */     }
/*      */     
/*  569 */     return null;
/*      */   }
/*      */   
/*      */   public void readDataStream(ByteBufInputStream ds) throws IOException {
/*  573 */     this.chests = StreamReadWrite.readPointList(ds);
/*  574 */     this.furnaces = StreamReadWrite.readPointList(ds);
/*  575 */     this.signs = StreamReadWrite.readPointList(ds);
/*      */     
/*  577 */     for (Point p : this.chests) {
/*  578 */       TileEntityMillChest chest = p.getMillChest(this.building.mw.world);
/*  579 */       if (chest != null) {
/*  580 */         chest.buildingPos = this.building.getPos();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void readFromNBT(NBTTagCompound nbttagcompound) {
/*  586 */     this.sleepingPos = Point.read(nbttagcompound, "spawnPos");
/*  587 */     this.sellingPos = Point.read(nbttagcompound, "sellingPos");
/*  588 */     this.craftingPos = Point.read(nbttagcompound, "craftingPos");
/*  589 */     this.defendingPos = Point.read(nbttagcompound, "defendingPos");
/*  590 */     this.shelterPos = Point.read(nbttagcompound, "shelterPos");
/*  591 */     this.pathStartPos = Point.read(nbttagcompound, "pathStartPos");
/*  592 */     this.leasurePos = Point.read(nbttagcompound, "leasurePos");
/*      */     
/*  594 */     if (this.sleepingPos == null) {
/*  595 */       this.sleepingPos = this.building.getPos().getAbove();
/*      */     }
/*      */     
/*  598 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c("chests", 10);
/*  599 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  600 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  601 */       Point p = Point.read(nbttagcompound1, "pos");
/*  602 */       if ((p != null) && 
/*  603 */         (!this.chests.contains(p))) {
/*  604 */         this.chests.add(p);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  609 */     if (!this.chests.contains(this.building.getPos())) {
/*  610 */       this.chests.add(0, this.building.getPos());
/*      */     }
/*      */     
/*  613 */     nbttaglist = nbttagcompound.func_150295_c("furnaces", 10);
/*  614 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  615 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  616 */       Point p = Point.read(nbttagcompound1, "pos");
/*  617 */       if (p != null) {
/*  618 */         this.furnaces.add(p);
/*      */       }
/*      */     }
/*      */     
/*  622 */     nbttaglist = nbttagcompound.func_150295_c("brewingStands", 10);
/*  623 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  624 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  625 */       Point p = Point.read(nbttagcompound1, "pos");
/*  626 */       if (p != null) {
/*  627 */         this.brewingStands.add(p);
/*      */       }
/*      */     }
/*      */     
/*  631 */     nbttaglist = nbttagcompound.func_150295_c("signs", 10);
/*  632 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  633 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  634 */       Point p = Point.read(nbttagcompound1, "pos");
/*  635 */       if (p != null) {
/*  636 */         this.signs.add(p);
/*      */       }
/*      */     }
/*      */     
/*  640 */     nbttaglist = nbttagcompound.func_150295_c("netherwartsoils", 10);
/*  641 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  642 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  643 */       Point p = Point.read(nbttagcompound1, "pos");
/*  644 */       if (p != null) {
/*  645 */         this.netherwartsoils.add(p);
/*      */       }
/*      */     }
/*      */     
/*  649 */     nbttaglist = nbttagcompound.func_150295_c("silkwormblock", 10);
/*  650 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  651 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  652 */       Point p = Point.read(nbttagcompound1, "pos");
/*  653 */       if (p != null) {
/*  654 */         this.silkwormblock.add(p);
/*      */       }
/*      */     }
/*      */     
/*  658 */     nbttaglist = nbttagcompound.func_150295_c("sugarcanesoils", 10);
/*  659 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  660 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  661 */       Point p = Point.read(nbttagcompound1, "pos");
/*  662 */       if (p != null) {
/*  663 */         this.sugarcanesoils.add(p);
/*      */       }
/*      */     }
/*      */     
/*  667 */     nbttaglist = nbttagcompound.func_150295_c("fishingspots", 10);
/*  668 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  669 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  670 */       Point p = Point.read(nbttagcompound1, "pos");
/*  671 */       if (p != null) {
/*  672 */         this.fishingspots.add(p);
/*      */       }
/*      */     }
/*      */     
/*  676 */     nbttaglist = nbttagcompound.func_150295_c("healingspots", 10);
/*  677 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  678 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  679 */       Point p = Point.read(nbttagcompound1, "pos");
/*  680 */       if (p != null) {
/*  681 */         this.healingspots.add(p);
/*      */       }
/*      */     }
/*      */     
/*  685 */     nbttaglist = nbttagcompound.func_150295_c("stalls", 10);
/*  686 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  687 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  688 */       Point p = Point.read(nbttagcompound1, "pos");
/*  689 */       if (p != null) {
/*  690 */         this.stalls.add(p);
/*      */       }
/*      */     }
/*      */     
/*  694 */     nbttaglist = nbttagcompound.func_150295_c("woodspawn", 10);
/*  695 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  696 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  697 */       Point p = Point.read(nbttagcompound1, "pos");
/*  698 */       if (p != null) {
/*  699 */         this.woodspawn.add(p);
/*      */       }
/*      */     }
/*      */     
/*  703 */     nbttaglist = nbttagcompound.func_150295_c("brickspot", 10);
/*  704 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  705 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  706 */       Point p = Point.read(nbttagcompound1, "pos");
/*  707 */       if (p != null) {
/*  708 */         this.brickspot.add(p);
/*      */       }
/*      */     }
/*      */     
/*  712 */     nbttaglist = nbttagcompound.func_150295_c("spawns", 10);
/*  713 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  714 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/*  716 */       String spawnType = nbttagcompound1.func_74779_i("type");
/*      */       
/*      */ 
/*  719 */       if (spawnType.equals("ml_FarmPig")) {
/*  720 */         spawnType = "Pig";
/*  721 */       } else if (spawnType.equals("ml_FarmCow")) {
/*  722 */         spawnType = "Cow";
/*  723 */       } else if (spawnType.equals("ml_FarmChicken")) {
/*  724 */         spawnType = "Chicken";
/*  725 */       } else if (spawnType.equals("ml_FarmSheep")) {
/*  726 */         spawnType = "Sheep";
/*      */       }
/*      */       
/*  729 */       this.spawnTypes.add(spawnType);
/*  730 */       List<Point> v = new ArrayList();
/*      */       
/*  732 */       NBTTagList nbttaglist2 = nbttagcompound1.func_150295_c("points", 10);
/*  733 */       for (int j = 0; j < nbttaglist2.func_74745_c(); j++) {
/*  734 */         NBTTagCompound nbttagcompound2 = nbttaglist2.func_150305_b(j);
/*  735 */         Point p = Point.read(nbttagcompound2, "pos");
/*  736 */         if (p != null) {
/*  737 */           v.add(p);
/*  738 */           if (MLN.LogHybernation >= 2) {
/*  739 */             MLN.minor(this, "Loaded spawn point: " + p);
/*      */           }
/*      */         }
/*      */       }
/*  743 */       this.spawns.add(v);
/*  744 */       if (MLN.LogHybernation >= 2) {
/*  745 */         MLN.minor(this, "Loaded " + v.size() + " spawn points for " + (String)this.spawnTypes.get(i));
/*      */       }
/*      */     }
/*      */     
/*  749 */     nbttaglist = nbttagcompound.func_150295_c("mobspawns", 10);
/*  750 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  751 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/*  753 */       this.mobSpawnerTypes.add(nbttagcompound1.func_74779_i("type"));
/*  754 */       List<Point> v = new ArrayList();
/*      */       
/*  756 */       NBTTagList nbttaglist2 = nbttagcompound1.func_150295_c("points", 10);
/*  757 */       for (int j = 0; j < nbttaglist2.func_74745_c(); j++) {
/*  758 */         NBTTagCompound nbttagcompound2 = nbttaglist2.func_150305_b(j);
/*  759 */         Point p = Point.read(nbttagcompound2, "pos");
/*  760 */         if (p != null) {
/*  761 */           v.add(p);
/*  762 */           if (MLN.LogHybernation >= 2) {
/*  763 */             MLN.minor(this, "Loaded spawn point: " + p);
/*      */           }
/*      */         }
/*      */       }
/*  767 */       this.mobSpawners.add(v);
/*  768 */       if (MLN.LogHybernation >= 2) {
/*  769 */         MLN.minor(this, "Loaded " + v.size() + " mob spawn points for " + (String)this.spawnTypes.get(i));
/*      */       }
/*      */     }
/*      */     
/*  773 */     nbttaglist = nbttagcompound.func_150295_c("sources", 10);
/*  774 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  775 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/*  777 */       this.sourceTypes.add(Block.func_149729_e(nbttagcompound1.func_74762_e("type")));
/*  778 */       List<Point> v = new ArrayList();
/*      */       
/*  780 */       NBTTagList nbttaglist2 = nbttagcompound1.func_150295_c("points", 10);
/*  781 */       for (int j = 0; j < nbttaglist2.func_74745_c(); j++) {
/*  782 */         NBTTagCompound nbttagcompound2 = nbttaglist2.func_150305_b(j);
/*  783 */         Point p = Point.read(nbttagcompound2, "pos");
/*  784 */         if (p != null) {
/*  785 */           v.add(p);
/*  786 */           if (MLN.LogHybernation >= 3) {
/*  787 */             MLN.debug(this, "Loaded source point: " + p);
/*      */           }
/*      */         }
/*      */       }
/*  791 */       this.sources.add(v);
/*  792 */       if (MLN.LogHybernation >= 1) {
/*  793 */         MLN.debug(this, "Loaded " + v.size() + " sources points for " + ((Block)this.sourceTypes.get(i)).func_149739_a());
/*      */       }
/*      */     }
/*      */     
/*  797 */     nbttaglist = nbttagcompound.func_150295_c("genericsoils", 10);
/*  798 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  799 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/*  801 */       String type = nbttagcompound1.func_74779_i("type");
/*      */       
/*  803 */       NBTTagList nbttaglist2 = nbttagcompound1.func_150295_c("points", 10);
/*  804 */       for (int j = 0; j < nbttaglist2.func_74745_c(); j++) {
/*  805 */         NBTTagCompound nbttagcompound2 = nbttaglist2.func_150305_b(j);
/*  806 */         Point p = Point.read(nbttagcompound2, "pos");
/*  807 */         if (p != null) {
/*  808 */           addSoilPoint(type, p);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  813 */     for (Point p : this.chests) {
/*  814 */       if ((this.building.worldObj.func_72899_e(p.getiX(), p.getiY(), p.getiZ())) && (p.getMillChest(this.building.worldObj) != null)) {
/*  815 */         p.getMillChest(this.building.worldObj).buildingPos = this.building.getPos();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendBuildingPacket(DataOutput data) throws IOException
/*      */   {
/*  822 */     StreamReadWrite.writePointList(this.chests, data);
/*  823 */     StreamReadWrite.writePointList(this.furnaces, data);
/*  824 */     StreamReadWrite.writePointList(this.signs, data);
/*      */   }
/*      */   
/*      */   public void setCraftingPos(Point p) {
/*  828 */     this.craftingPos = p;
/*      */   }
/*      */   
/*      */   public void setDefendingPos(Point p) {
/*  832 */     this.defendingPos = p;
/*      */   }
/*      */   
/*      */   public void setLeasurePos(Point p) {
/*  836 */     this.leasurePos = p;
/*      */   }
/*      */   
/*      */   public void setPathStartPos(Point p) {
/*  840 */     this.pathStartPos = p;
/*      */   }
/*      */   
/*      */   public void setSellingPos(Point p) {
/*  844 */     this.sellingPos = p;
/*      */   }
/*      */   
/*      */   public void setShelterPos(Point p) {
/*  848 */     this.shelterPos = p;
/*      */   }
/*      */   
/*      */   public void setSleepingPos(Point p) {
/*  852 */     this.sleepingPos = p;
/*      */   }
/*      */   
/*      */   public void writeToNBT(NBTTagCompound nbttagcompound) {
/*  856 */     if (this.sleepingPos != null) {
/*  857 */       this.sleepingPos.write(nbttagcompound, "spawnPos");
/*      */     }
/*  859 */     if (this.sellingPos != null) {
/*  860 */       this.sellingPos.write(nbttagcompound, "sellingPos");
/*      */     }
/*  862 */     if (this.craftingPos != null) {
/*  863 */       this.craftingPos.write(nbttagcompound, "craftingPos");
/*      */     }
/*  865 */     if (this.defendingPos != null) {
/*  866 */       this.defendingPos.write(nbttagcompound, "defendingPos");
/*      */     }
/*  868 */     if (this.shelterPos != null) {
/*  869 */       this.shelterPos.write(nbttagcompound, "shelterPos");
/*      */     }
/*  871 */     if (this.pathStartPos != null) {
/*  872 */       this.pathStartPos.write(nbttagcompound, "pathStartPos");
/*      */     }
/*  874 */     if (this.leasurePos != null) {
/*  875 */       this.leasurePos.write(nbttagcompound, "leasurePos");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  880 */     NBTTagList nbttaglist = new NBTTagList();
/*  881 */     for (Point p : this.signs) {
/*  882 */       if (p != null) {
/*  883 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  884 */         p.write(nbttagcompound1, "pos");
/*  885 */         nbttaglist.func_74742_a(nbttagcompound1);
/*      */       }
/*      */     }
/*  888 */     nbttagcompound.func_74782_a("signs", nbttaglist);
/*      */     
/*  890 */     nbttaglist = new NBTTagList();
/*  891 */     for (Point p : this.netherwartsoils) {
/*  892 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  893 */       p.write(nbttagcompound1, "pos");
/*  894 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  897 */     nbttagcompound.func_74782_a("netherwartsoils", nbttaglist);
/*      */     
/*  899 */     nbttaglist = new NBTTagList();
/*  900 */     for (Point p : this.silkwormblock) {
/*  901 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  902 */       p.write(nbttagcompound1, "pos");
/*  903 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  906 */     nbttagcompound.func_74782_a("silkwormblock", nbttaglist);
/*      */     
/*  908 */     nbttaglist = new NBTTagList();
/*  909 */     for (Point p : this.sugarcanesoils) {
/*  910 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  911 */       p.write(nbttagcompound1, "pos");
/*  912 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  915 */     nbttagcompound.func_74782_a("sugarcanesoils", nbttaglist);
/*      */     
/*  917 */     nbttaglist = new NBTTagList();
/*  918 */     for (Point p : this.fishingspots) {
/*  919 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  920 */       p.write(nbttagcompound1, "pos");
/*  921 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  924 */     nbttagcompound.func_74782_a("fishingspots", nbttaglist);
/*      */     
/*  926 */     nbttaglist = new NBTTagList();
/*  927 */     for (Point p : this.healingspots) {
/*  928 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  929 */       p.write(nbttagcompound1, "pos");
/*  930 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  933 */     nbttagcompound.func_74782_a("healingspots", nbttaglist);
/*      */     
/*  935 */     nbttaglist = new NBTTagList();
/*  936 */     for (Point p : this.stalls) {
/*  937 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  938 */       p.write(nbttagcompound1, "pos");
/*  939 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  942 */     nbttagcompound.func_74782_a("stalls", nbttaglist);
/*      */     
/*  944 */     nbttaglist = new NBTTagList();
/*  945 */     for (Point p : this.woodspawn) {
/*  946 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  947 */       p.write(nbttagcompound1, "pos");
/*  948 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  951 */     nbttagcompound.func_74782_a("woodspawn", nbttaglist);
/*      */     
/*  953 */     nbttaglist = new NBTTagList();
/*  954 */     for (Point p : this.brickspot) {
/*  955 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  956 */       p.write(nbttagcompound1, "pos");
/*  957 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/*  960 */     nbttagcompound.func_74782_a("brickspot", nbttaglist);
/*      */     
/*  962 */     nbttaglist = new NBTTagList();
/*  963 */     for (int i = 0; i < this.spawns.size(); i++) {
/*  964 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  965 */       nbttagcompound1.func_74778_a("type", (String)this.spawnTypes.get(i));
/*  966 */       NBTTagList nbttaglist2 = new NBTTagList();
/*  967 */       for (Point p : (List)this.spawns.get(i)) {
/*  968 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/*  969 */         p.write(nbttagcompound2, "pos");
/*  970 */         nbttaglist2.func_74742_a(nbttagcompound2);
/*      */       }
/*  972 */       nbttagcompound1.func_74782_a("points", nbttaglist2);
/*  973 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*  975 */     nbttagcompound.func_74782_a("spawns", nbttaglist);
/*      */     
/*  977 */     nbttaglist = new NBTTagList();
/*  978 */     for (int i = 0; i < this.soilTypes.size(); i++) {
/*  979 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  980 */       nbttagcompound1.func_74778_a("type", (String)this.soilTypes.get(i));
/*  981 */       NBTTagList nbttaglist2 = new NBTTagList();
/*  982 */       for (Point p : (List)this.soils.get(i)) {
/*  983 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/*  984 */         p.write(nbttagcompound2, "pos");
/*  985 */         nbttaglist2.func_74742_a(nbttagcompound2);
/*      */       }
/*  987 */       nbttagcompound1.func_74782_a("points", nbttaglist2);
/*  988 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*  990 */     nbttagcompound.func_74782_a("genericsoils", nbttaglist);
/*      */     
/*  992 */     nbttaglist = new NBTTagList();
/*  993 */     for (int i = 0; i < this.mobSpawners.size(); i++) {
/*  994 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  995 */       nbttagcompound1.func_74778_a("type", (String)this.mobSpawnerTypes.get(i));
/*  996 */       NBTTagList nbttaglist2 = new NBTTagList();
/*  997 */       for (Point p : (List)this.mobSpawners.get(i)) {
/*  998 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/*  999 */         p.write(nbttagcompound2, "pos");
/* 1000 */         nbttaglist2.func_74742_a(nbttagcompound2);
/*      */       }
/* 1002 */       nbttagcompound1.func_74782_a("points", nbttaglist2);
/* 1003 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/* 1005 */     nbttagcompound.func_74782_a("mobspawns", nbttaglist);
/*      */     
/* 1007 */     nbttaglist = new NBTTagList();
/* 1008 */     for (int i = 0; i < this.sources.size(); i++) {
/* 1009 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1010 */       nbttagcompound1.func_74768_a("type", Block.func_149682_b((Block)this.sourceTypes.get(i)));
/* 1011 */       NBTTagList nbttaglist2 = new NBTTagList();
/* 1012 */       for (Point p : (List)this.sources.get(i)) {
/* 1013 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 1014 */         p.write(nbttagcompound2, "pos");
/* 1015 */         nbttaglist2.func_74742_a(nbttagcompound2);
/*      */       }
/* 1017 */       nbttagcompound1.func_74782_a("points", nbttaglist2);
/* 1018 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/* 1020 */     nbttagcompound.func_74782_a("sources", nbttaglist);
/*      */     
/* 1022 */     nbttaglist = new NBTTagList();
/* 1023 */     for (Point p : this.chests) {
/* 1024 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1025 */       p.write(nbttagcompound1, "pos");
/* 1026 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/* 1029 */     nbttagcompound.func_74782_a("chests", nbttaglist);
/*      */     
/* 1031 */     nbttaglist = new NBTTagList();
/* 1032 */     for (Point p : this.furnaces) {
/* 1033 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1034 */       p.write(nbttagcompound1, "pos");
/* 1035 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/* 1038 */     nbttagcompound.func_74782_a("furnaces", nbttaglist);
/*      */     
/* 1040 */     nbttaglist = new NBTTagList();
/* 1041 */     for (Point p : this.brewingStands) {
/* 1042 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 1043 */       p.write(nbttagcompound1, "pos");
/* 1044 */       nbttaglist.func_74742_a(nbttagcompound1);
/*      */     }
/*      */     
/* 1047 */     nbttagcompound.func_74782_a("brewingStands", nbttaglist);
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingResManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
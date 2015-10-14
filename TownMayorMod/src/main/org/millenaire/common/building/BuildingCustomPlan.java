/*     */ package org.millenaire.common.building;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ public class BuildingCustomPlan
/*     */   implements IBuildingPlan
/*     */ {
/*     */   public static enum TypeRes
/*     */   {
/*  26 */     CHEST("chest"),  CRAFT("craft"),  SIGN("sign"),  FIELD("field"),  SPAWN("spawn"),  SAPLING("sapling"),  STALL("stall"),  MINING("mining"),  FURNACE("furnace"),  MUDBRICK("mudbrick"),  SUGAR("sugar"),  FISHING("fishing"), 
/*  27 */     SILK("silk"),  SQUID("squid"),  CACAO("cacao");
/*     */     
/*     */     public final String key;
/*     */     
/*     */     private TypeRes(String key) {
/*  32 */       this.key = key;
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
/*     */   public static Map<String, BuildingCustomPlan> loadCustomBuildings(List<File> culturesDirs, Culture culture)
/*     */   {
/*  45 */     Map<String, BuildingCustomPlan> buildingCustoms = new HashMap();
/*     */     
/*  47 */     List<File> dirs = new ArrayList();
/*     */     
/*  49 */     for (File cultureDir : culturesDirs)
/*     */     {
/*  51 */       File customDir = new File(cultureDir, "custombuildings");
/*     */       
/*  53 */       if (customDir.exists()) {
/*  54 */         dirs.add(customDir);
/*     */       }
/*     */     }
/*     */     
/*  58 */     BuildingFileFiler textFiles = new BuildingFileFiler(".txt");
/*     */     
/*  60 */     for (int i = 0; i < dirs.size(); i++)
/*     */     {
/*  62 */       for (File file : ((File)dirs.get(i)).listFiles(textFiles)) {
/*     */         try
/*     */         {
/*  65 */           if (MLN.LogBuildingPlan >= 1) {
/*  66 */             MLN.major(file, "Loaded custom building");
/*     */           }
/*     */           
/*  69 */           BuildingCustomPlan buildingCustom = new BuildingCustomPlan(file, culture);
/*     */           
/*  71 */           buildingCustoms.put(buildingCustom.buildingKey, buildingCustom);
/*     */         }
/*     */         catch (Exception e) {
/*  74 */           MLN.printException("Error when loading " + file.getAbsolutePath(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  80 */     return buildingCustoms;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public String shop = null; public String gameNameKey = null;
/*     */   
/*  88 */   public final Map<String, String> names = new HashMap();
/*     */   
/*  90 */   public List<String> maleResident = new ArrayList();
/*     */   
/*  92 */   public List<String> femaleResident = new ArrayList();
/*     */   
/*  94 */   public int priorityMoveIn = 1;
/*     */   
/*  96 */   public int radius = 6; public int heightRadius = 4;
/*  97 */   public List<String> tags = new ArrayList();
/*  98 */   public String cropType = null;
/*  99 */   public String spawnType = null;
/* 100 */   public Map<TypeRes, Integer> minResources = new HashMap();
/* 101 */   public Map<TypeRes, Integer> maxResources = new HashMap();
/*     */   
/*     */   public final Culture culture;
/*     */   
/*     */   public String nativeName;
/*     */   
/*     */   public String buildingKey;
/*     */   
/*     */   public BuildingCustomPlan(Culture culture, String key)
/*     */   {
/* 111 */     this.culture = culture;
/* 112 */     this.buildingKey = key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingCustomPlan(File file, Culture culture)
/*     */     throws IOException
/*     */   {
/* 123 */     this.culture = culture;
/* 124 */     this.buildingKey = file.getName().split("\\.")[0];
/*     */     
/* 126 */     BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */     
/* 128 */     String line = reader.readLine();
/*     */     
/* 130 */     readConfigLine(line);
/*     */     
/* 132 */     if (MLN.LogBuildingPlan >= 1) {
/* 133 */       MLN.major(this, "Loaded custom building " + this.buildingKey + this.nativeName + " pop: " + this.maleResident + "/" + this.femaleResident);
/*     */     }
/*     */     
/* 136 */     if (!this.minResources.containsKey(TypeRes.SIGN)) {
/* 137 */       MLN.error(this, "No signs in custom building.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void adjustLocationSize(BuildingLocation location, Map<TypeRes, List<Point>> resources)
/*     */   {
/* 148 */     int startX = location.pos.getiX();
/* 149 */     int startY = location.pos.getiY();
/* 150 */     int startZ = location.pos.getiZ();
/*     */     
/* 152 */     int endX = location.pos.getiX();
/* 153 */     int endY = location.pos.getiY();
/* 154 */     int endZ = location.pos.getiZ();
/*     */     
/* 156 */     for (TypeRes type : resources.keySet()) {
/* 157 */       for (Point p : (List)resources.get(type)) {
/* 158 */         if (startX >= p.getiX()) {
/* 159 */           startX = p.getiX();
/*     */         }
/* 161 */         if (startY >= p.getiY()) {
/* 162 */           startY = p.getiY();
/*     */         }
/* 164 */         if (startZ >= p.getiZ()) {
/* 165 */           startZ = p.getiZ();
/*     */         }
/* 167 */         if (endX <= p.getiX()) {
/* 168 */           endX = p.getiX();
/*     */         }
/* 170 */         if (endY <= p.getiY()) {
/* 171 */           endY = p.getiY();
/*     */         }
/* 173 */         if (endZ <= p.getiZ()) {
/* 174 */           endZ = p.getiZ();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 179 */     location.minx = (startX - 1);
/* 180 */     location.maxx = (endX + 1);
/* 181 */     location.miny = (startY - 1);
/* 182 */     location.maxy = (endY + 1);
/* 183 */     location.minz = (startZ - 1);
/* 184 */     location.maxz = (endZ + 1);
/*     */     
/* 186 */     location.length = (location.maxx - location.minx + 1);
/* 187 */     location.width = (location.maxz - location.minz + 1);
/*     */     
/* 189 */     location.computeMargins();
/*     */   }
/*     */   
/*     */   public Map<TypeRes, List<Point>> findResources(World world, Point pos, Building townHall, BuildingLocation currentLocation)
/*     */   {
/* 194 */     Map<TypeRes, List<Point>> resources = new HashMap();
/*     */     
/* 196 */     int currentRadius = 0;
/*     */     
/* 198 */     while (currentRadius < this.radius)
/*     */     {
/* 200 */       int y = pos.getiY() - this.heightRadius + 1;
/*     */       
/* 202 */       while (y < pos.getiY() + this.heightRadius + 1)
/*     */       {
/*     */ 
/*     */ 
/* 206 */         int z = pos.getiZ() - currentRadius;
/* 207 */         for (int x = pos.getiX() - currentRadius; x <= pos.getiX() + currentRadius; x++) {
/* 208 */           handlePoint(x, y, z, world, resources, townHall, currentLocation);
/*     */         }
/*     */         
/*     */ 
/* 212 */         x = pos.getiX() - currentRadius;
/* 213 */         for (z = pos.getiZ() - currentRadius + 1; z <= pos.getiZ() + currentRadius - 1; z++) {
/* 214 */           handlePoint(x, y, z, world, resources, townHall, currentLocation);
/*     */         }
/*     */         
/*     */ 
/* 218 */         z = pos.getiZ() + currentRadius;
/* 219 */         for (x = pos.getiX() - currentRadius; x <= pos.getiX() + currentRadius; x++) {
/* 220 */           handlePoint(x, y, z, world, resources, townHall, currentLocation);
/*     */         }
/*     */         
/*     */ 
/* 224 */         x = pos.getiX() + currentRadius;
/* 225 */         for (z = pos.getiZ() - currentRadius + 1; z <= pos.getiZ() + currentRadius - 1; z++) {
/* 226 */           handlePoint(x, y, z, world, resources, townHall, currentLocation);
/*     */         }
/*     */         
/* 229 */         y++;
/*     */       }
/*     */       
/* 232 */       currentRadius++;
/*     */     }
/* 234 */     return resources;
/*     */   }
/*     */   
/*     */   public Culture getCulture()
/*     */   {
/* 239 */     return this.culture;
/*     */   }
/*     */   
/*     */   public List<String> getFemaleResident()
/*     */   {
/* 244 */     return this.femaleResident;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFullDisplayName()
/*     */   {
/* 251 */     String name = this.nativeName;
/* 252 */     if ((getGameName() != null) && (getGameName().length() > 0)) {
/* 253 */       name = name + " (" + getGameName() + ")";
/*     */     }
/* 255 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGameName()
/*     */   {
/* 265 */     if (this.culture.canReadBuildingNames()) {
/* 266 */       return this.culture.getCustomBuildingGameName(this);
/*     */     }
/* 268 */     return "";
/*     */   }
/*     */   
/*     */   public List<String> getMaleResident()
/*     */   {
/* 273 */     return this.maleResident;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getNativeName()
/*     */   {
/* 283 */     return this.nativeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void handlePoint(int x, int y, int z, World world, Map<TypeRes, List<Point>> resources, Building townHall, BuildingLocation currentLocation)
/*     */   {
/* 292 */     Point p = new Point(x, y, z);
/*     */     
/*     */ 
/*     */ 
/* 296 */     if (townHall != null)
/*     */     {
/* 298 */       BuildingLocation locationAtPos = townHall.getLocationAtCoord(p);
/*     */       
/*     */ 
/*     */ 
/* 302 */       if ((locationAtPos == null) || (!locationAtPos.equals(currentLocation))) {
/* 303 */         for (BuildingLocation bl : townHall.getLocations())
/*     */         {
/*     */ 
/*     */ 
/* 307 */           if (((currentLocation == null) || (!currentLocation.isSameLocation(bl))) && (bl.isInsideZone(p))) {
/* 308 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 314 */     TypeRes res = identifyRes(world, p);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 320 */     if ((res != null) && (this.maxResources.containsKey(res))) {
/* 321 */       if ((resources.containsKey(res)) && (((List)resources.get(res)).size() < ((Integer)this.maxResources.get(res)).intValue())) {
/* 322 */         ((List)resources.get(res)).add(p);
/* 323 */       } else if (!resources.containsKey(res)) {
/* 324 */         List<Point> points = new ArrayList();
/* 325 */         points.add(p);
/* 326 */         resources.put(res, points);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private TypeRes identifyRes(World world, Point p)
/*     */   {
/* 333 */     Block b = p.getBlock(world);
/* 334 */     int meta = p.getMeta(world);
/*     */     
/* 336 */     if ((b.equals(Blocks.field_150486_ae)) || (b.equals(Mill.lockedChest))) {
/* 337 */       return TypeRes.CHEST;
/*     */     }
/*     */     
/* 340 */     if (b.equals(Blocks.field_150462_ai)) {
/* 341 */       return TypeRes.CRAFT;
/*     */     }
/*     */     
/* 344 */     if ((b.equals(Blocks.field_150444_as)) || (b.equals(Mill.panel))) {
/* 345 */       return TypeRes.SIGN;
/*     */     }
/* 347 */     if (b.equals(Blocks.field_150458_ak)) {
/* 348 */       return TypeRes.FIELD;
/*     */     }
/* 350 */     if (b.equals(Blocks.field_150407_cf)) {
/* 351 */       return TypeRes.SPAWN;
/*     */     }
/*     */     
/* 354 */     if ((b.equals(Blocks.field_150345_g)) || (((b.equals(Blocks.field_150364_r)) || (b.equals(Blocks.field_150363_s))) && (p.getBelow().getBlock(world).equals(Blocks.field_150346_d)))) {
/* 355 */       return TypeRes.SAPLING;
/*     */     }
/* 357 */     if ((b.equals(Blocks.field_150325_L)) && (p.getMeta(world) == 4)) {
/* 358 */       return TypeRes.STALL;
/*     */     }
/*     */     
/* 361 */     if ((b.equals(Blocks.field_150348_b)) || (b.equals(Blocks.field_150322_A)) || (b.equals(Blocks.field_150354_m)) || (b.equals(Blocks.field_150351_n)) || (b.equals(Blocks.field_150435_aG)))
/*     */     {
/* 363 */       if ((p.getAbove().getBlock(world).equals(Blocks.field_150350_a)) || (p.getRelative(1.0D, 0.0D, 0.0D).getBlock(world).equals(Blocks.field_150350_a)) || (p.getRelative(-1.0D, 0.0D, 0.0D).getBlock(world).equals(Blocks.field_150350_a)) || (p.getRelative(0.0D, 0.0D, 1.0D).getBlock(world).equals(Blocks.field_150350_a)) || (p.getRelative(0.0D, 0.0D, -1.0D).getBlock(world).equals(Blocks.field_150350_a)))
/*     */       {
/* 365 */         return TypeRes.MINING;
/*     */       }
/*     */     }
/* 368 */     if (b.equals(Blocks.field_150460_al)) {
/* 369 */       return TypeRes.FURNACE;
/*     */     }
/*     */     
/* 372 */     if ((b.equals(Mill.earth_decoration)) && (meta == 0)) {
/* 373 */       return TypeRes.MUDBRICK;
/*     */     }
/*     */     
/* 376 */     if ((b.equals(Blocks.field_150436_aH)) && (!p.getBelow().getBlock(world).equals(Blocks.field_150436_aH))) {
/* 377 */       return TypeRes.SUGAR;
/*     */     }
/* 379 */     if ((b.equals(Blocks.field_150325_L)) && (p.getMeta(world) == 3)) {
/* 380 */       return TypeRes.FISHING;
/*     */     }
/* 382 */     if ((b.equals(Blocks.field_150325_L)) && (p.getMeta(world) == 0)) {
/* 383 */       return TypeRes.SILK;
/*     */     }
/* 385 */     if ((b.equals(Blocks.field_150325_L)) && (p.getMeta(world) == 11)) {
/* 386 */       Point[] neighbours = { p.getRelative(1.0D, 0.0D, 0.0D), p.getRelative(-1.0D, 0.0D, 0.0D), p.getRelative(0.0D, 0.0D, 1.0D), p.getRelative(0.0D, 0.0D, -1.0D) };
/* 387 */       boolean waterAround = true;
/*     */       
/* 389 */       for (Point p2 : neighbours) {
/* 390 */         if (!p2.getBlock(world).equals(Blocks.field_150355_j)) {
/* 391 */           waterAround = false;
/*     */         }
/*     */       }
/*     */       
/* 395 */       if (waterAround) {
/* 396 */         return TypeRes.SQUID;
/*     */       }
/*     */     }
/* 399 */     if (b.equals(Blocks.field_150375_by)) {
/* 400 */       return TypeRes.CACAO;
/*     */     }
/* 402 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readConfigLine(String line)
/*     */   {
/* 413 */     String[] configs = line.split(";", -1);
/*     */     
/* 415 */     for (String config : configs) {
/* 416 */       if (config.split(":").length == 2) {
/* 417 */         String key = config.split(":")[0].toLowerCase();
/* 418 */         String value = config.split(":")[1];
/*     */         
/* 420 */         if (key.equalsIgnoreCase("moveinpriority")) {
/* 421 */           this.priorityMoveIn = Integer.parseInt(value);
/* 422 */         } else if (key.equalsIgnoreCase("radius")) {
/* 423 */           this.radius = Integer.parseInt(value);
/* 424 */         } else if (key.equalsIgnoreCase("heightradius")) {
/* 425 */           this.heightRadius = Integer.parseInt(value);
/* 426 */         } else if (key.equalsIgnoreCase("native")) {
/* 427 */           this.nativeName = value;
/* 428 */         } else if (key.equalsIgnoreCase("gameNameKey")) {
/* 429 */           this.gameNameKey = value;
/* 430 */         } else if (key.equalsIgnoreCase("cropType")) {
/* 431 */           this.cropType = value;
/* 432 */         } else if (key.equalsIgnoreCase("spawnType")) {
/* 433 */           this.spawnType = value;
/* 434 */         } else if (key.startsWith("name_")) {
/* 435 */           this.names.put(key, value);
/* 436 */         } else if (key.equalsIgnoreCase("male")) {
/* 437 */           if (this.culture.villagerTypes.containsKey(value.toLowerCase())) {
/* 438 */             this.maleResident.add(value.toLowerCase());
/*     */           } else {
/* 440 */             MLN.error(this, "Attempted to load unknown male villager: " + value);
/*     */           }
/* 442 */         } else if (key.equalsIgnoreCase("female")) {
/* 443 */           if (this.culture.villagerTypes.containsKey(value.toLowerCase())) {
/* 444 */             this.femaleResident.add(value.toLowerCase());
/*     */           } else {
/* 446 */             MLN.error(this, "Attempted to load unknown female villager: " + value);
/*     */           }
/* 448 */         } else if (key.equalsIgnoreCase("shop")) {
/* 449 */           if ((this.culture.shopBuys.containsKey(value)) || (this.culture.shopSells.containsKey(value)) || (this.culture.shopBuysOptional.containsKey(value))) {
/* 450 */             this.shop = value;
/*     */           } else {
/* 452 */             MLN.error(this, "Undefined shop type: " + value);
/*     */           }
/* 454 */         } else if (key.equalsIgnoreCase("tag")) {
/* 455 */           this.tags.add(value.toLowerCase());
/*     */         }
/*     */         else {
/* 458 */           boolean found = false;
/*     */           
/* 460 */           for (TypeRes typeRes : TypeRes.values()) {
/* 461 */             if (typeRes.key.equals(key)) {
/*     */               try
/*     */               {
/* 464 */                 found = true;
/*     */                 
/* 466 */                 if (value.contains("-")) {
/* 467 */                   this.minResources.put(typeRes, Integer.valueOf(Integer.parseInt(value.split("-")[0])));
/* 468 */                   this.maxResources.put(typeRes, Integer.valueOf(Integer.parseInt(value.split("-")[1])));
/*     */                 } else {
/* 470 */                   this.minResources.put(typeRes, Integer.valueOf(Integer.parseInt(value)));
/* 471 */                   this.maxResources.put(typeRes, Integer.valueOf(Integer.parseInt(value)));
/*     */                 }
/*     */               } catch (Exception e) {
/* 474 */                 MLN.printException("Exception while parsing res " + typeRes.key + " in custom file " + this.buildingKey + " of culture " + this.culture.key + ":", e);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 479 */           if (!found) {
/* 480 */             MLN.error(this, "Could not recognise key on line: " + config);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerResources(Building building, BuildingLocation location)
/*     */   {
/* 492 */     Map<TypeRes, List<Point>> resources = findResources(building.worldObj, location.pos, building.getTownHall(), location);
/*     */     
/* 494 */     adjustLocationSize(location, resources);
/*     */     
/* 496 */     building.getResManager().setSleepingPos(location.pos.getAbove());
/* 497 */     location.sleepingPos = location.pos.getAbove();
/*     */     
/* 499 */     if (resources.containsKey(TypeRes.CHEST)) {
/* 500 */       building.getResManager().chests.clear();
/* 501 */       for (Point chestP : (List)resources.get(TypeRes.CHEST))
/*     */       {
/*     */ 
/* 504 */         if (chestP.getBlock(building.worldObj).equals(Blocks.field_150486_ae)) {
/* 505 */           int meta = chestP.getMeta(building.worldObj);
/*     */           
/* 507 */           chestP.setBlock(building.worldObj, Mill.lockedChest, meta, false, false);
/*     */         }
/*     */         
/* 510 */         building.getResManager().chests.add(chestP);
/*     */       }
/*     */     }
/*     */     
/* 514 */     if ((resources.containsKey(TypeRes.CRAFT)) && (((List)resources.get(TypeRes.CRAFT)).size() > 0)) {
/* 515 */       location.craftingPos = ((Point)((List)resources.get(TypeRes.CRAFT)).get(0));
/* 516 */       building.getResManager().setCraftingPos((Point)((List)resources.get(TypeRes.CRAFT)).get(0));
/*     */     }
/*     */     
/* 519 */     registerSigns(building, resources);
/*     */     
/* 521 */     if ((this.cropType != null) && (resources.containsKey(TypeRes.FIELD))) {
/* 522 */       building.getResManager().soils.clear();
/* 523 */       building.getResManager().soilTypes.clear();
/* 524 */       for (Point p : (List)resources.get(TypeRes.FIELD)) {
/* 525 */         building.getResManager().addSoilPoint(this.cropType, p);
/*     */       }
/*     */     }
/* 528 */     if ((this.spawnType != null) && (resources.containsKey(TypeRes.SPAWN))) {
/* 529 */       building.getResManager().spawns.clear();
/* 530 */       building.getResManager().spawnTypes.clear();
/* 531 */       for (Point p : (List)resources.get(TypeRes.SPAWN)) {
/* 532 */         p.setBlock(building.worldObj, Blocks.field_150350_a, 0, true, false);
/* 533 */         building.getResManager().addSpawnPoint(this.spawnType, p);
/*     */       }
/*     */     }
/* 536 */     if (resources.containsKey(TypeRes.SAPLING)) {
/* 537 */       building.getResManager().woodspawn.clear();
/* 538 */       for (Point p : (List)resources.get(TypeRes.SAPLING)) {
/* 539 */         building.getResManager().woodspawn.add(p);
/*     */       }
/*     */     }
/* 542 */     if (resources.containsKey(TypeRes.STALL)) {
/* 543 */       building.getResManager().stalls.clear();
/* 544 */       for (Point p : (List)resources.get(TypeRes.STALL)) {
/* 545 */         p.setBlock(building.worldObj, Blocks.field_150350_a, 0, true, false);
/* 546 */         building.getResManager().stalls.add(p);
/*     */       }
/*     */     }
/* 549 */     if (resources.containsKey(TypeRes.MINING)) {
/* 550 */       building.getResManager().sources.clear();
/* 551 */       building.getResManager().sourceTypes.clear();
/* 552 */       for (Point p : (List)resources.get(TypeRes.MINING)) {
/* 553 */         building.getResManager().addSourcePoint(p.getBlock(building.worldObj), p);
/*     */       }
/*     */     }
/* 556 */     if (resources.containsKey(TypeRes.FURNACE)) {
/* 557 */       building.getResManager().furnaces.clear();
/* 558 */       for (Point p : (List)resources.get(TypeRes.FURNACE)) {
/* 559 */         building.getResManager().furnaces.add(p);
/*     */       }
/*     */     }
/* 562 */     if (resources.containsKey(TypeRes.MUDBRICK)) {
/* 563 */       building.getResManager().brickspot.clear();
/* 564 */       for (Point p : (List)resources.get(TypeRes.MUDBRICK)) {
/* 565 */         building.getResManager().brickspot.add(p);
/*     */       }
/*     */     }
/* 568 */     if (resources.containsKey(TypeRes.SUGAR)) {
/* 569 */       building.getResManager().sugarcanesoils.clear();
/* 570 */       for (Point p : (List)resources.get(TypeRes.SUGAR)) {
/* 571 */         building.getResManager().sugarcanesoils.add(p);
/*     */       }
/*     */     }
/* 574 */     if (resources.containsKey(TypeRes.FISHING)) {
/* 575 */       building.getResManager().fishingspots.clear();
/* 576 */       for (Point p : (List)resources.get(TypeRes.FISHING)) {
/* 577 */         p.setBlock(building.worldObj, Blocks.field_150350_a, 0, true, false);
/* 578 */         building.getResManager().fishingspots.add(p);
/*     */       }
/*     */     }
/* 581 */     if (resources.containsKey(TypeRes.SILK)) {
/* 582 */       building.getResManager().silkwormblock.clear();
/* 583 */       for (Point p : (List)resources.get(TypeRes.SILK)) {
/* 584 */         p.setBlock(building.worldObj, Mill.wood_decoration, 3, true, false);
/* 585 */         building.getResManager().silkwormblock.add(p);
/*     */       }
/*     */     }
/* 588 */     if (resources.containsKey(TypeRes.SQUID)) {
/* 589 */       int squidSpawnPos = -1;
/* 590 */       for (int i = 0; i < building.getResManager().spawnTypes.size(); i++) {
/* 591 */         if (((String)building.getResManager().spawnTypes.get(i)).equals("Squid")) {
/* 592 */           squidSpawnPos = i;
/*     */         }
/*     */       }
/* 595 */       if (squidSpawnPos > -1) {
/* 596 */         ((List)building.getResManager().spawns.get(squidSpawnPos)).clear();
/*     */       }
/*     */       
/* 599 */       for (Point p : (List)resources.get(TypeRes.SQUID)) {
/* 600 */         p.setBlock(building.worldObj, Blocks.field_150355_j, 0, true, false);
/* 601 */         building.getResManager().addSpawnPoint("Squid", p);
/*     */       }
/*     */     }
/* 604 */     if (resources.containsKey(TypeRes.CACAO)) {
/* 605 */       int cocoaSoilPos = -1;
/* 606 */       for (int i = 0; i < building.getResManager().soilTypes.size(); i++) {
/* 607 */         if (((String)building.getResManager().soilTypes.get(i)).equals("cacao")) {
/* 608 */           cocoaSoilPos = i;
/*     */         }
/*     */       }
/* 611 */       if (cocoaSoilPos > -1) {
/* 612 */         ((List)building.getResManager().soils.get(cocoaSoilPos)).clear();
/*     */       }
/*     */       
/* 615 */       for (Point p : (List)resources.get(TypeRes.CACAO)) {
/* 616 */         building.getResManager().addSoilPoint("cacao", p);
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
/*     */   private void registerSigns(Building building, Map<TypeRes, List<Point>> resources)
/*     */   {
/* 630 */     building.getResManager().signs.clear();
/*     */     
/* 632 */     Map<Integer, Point> signsWithPos = new HashMap();
/* 633 */     List<Point> otherSigns = new ArrayList();
/* 634 */     if (resources.containsKey(TypeRes.SIGN)) {
/* 635 */       for (Point signP : (List)resources.get(TypeRes.SIGN))
/*     */       {
/*     */ 
/* 638 */         TileEntitySign signEntity = signP.getSign(building.worldObj);
/*     */         
/* 640 */         int signPos = -1;
/*     */         
/* 642 */         if (signEntity != null)
/*     */         {
/*     */           try
/*     */           {
/* 646 */             signPos = Integer.parseInt(signEntity.field_145915_a[0]) - 1;
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */         
/*     */ 
/* 652 */         if ((signPos > -1) && (!signsWithPos.containsKey(Integer.valueOf(signPos)))) {
/* 653 */           signsWithPos.put(Integer.valueOf(signPos), signP);
/*     */         } else {
/* 655 */           otherSigns.add(signP);
/*     */         }
/*     */         
/* 658 */         if (signP.getBlock(building.worldObj).equals(Blocks.field_150444_as)) {
/* 659 */           int meta = signP.getMeta(building.worldObj);
/* 660 */           signP.setBlock(building.worldObj, Mill.panel, meta, true, false);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 666 */     int signNumber = signsWithPos.size() + otherSigns.size();
/*     */     
/* 668 */     for (int i = 0; i < signNumber; i++) {
/* 669 */       building.getResManager().signs.add(null);
/*     */     }
/*     */     
/* 672 */     for (Integer pos : signsWithPos.keySet()) {
/* 673 */       if (pos.intValue() < signNumber) {
/* 674 */         building.getResManager().signs.set(pos.intValue(), signsWithPos.get(pos));
/*     */       } else {
/* 676 */         otherSigns.add(signsWithPos.get(pos));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 682 */     int posInOthers = 0;
/* 683 */     for (int i = 0; i < signNumber; i++) {
/* 684 */       if (building.getResManager().signs.get(i) == null) {
/* 685 */         building.getResManager().signs.set(i, otherSigns.get(posInOthers));
/* 686 */         posInOthers++;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 693 */     return "custom:" + this.buildingKey + ":" + this.culture.key;
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingCustomPlan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
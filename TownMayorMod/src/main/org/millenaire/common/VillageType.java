/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.world.World;
/*     */ import org.millenaire.common.building.BuildingCustomPlan;
/*     */ import org.millenaire.common.building.BuildingPlanSet;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.building.BuildingProject.EnumProjects;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.core.MillCommonUtilities.VillageList;
/*     */ import org.millenaire.common.core.MillCommonUtilities.WeightedChoice;
/*     */ import org.millenaire.common.forge.CommonProxy;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ import org.millenaire.common.item.Goods;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ public class VillageType implements MillCommonUtilities.WeightedChoice
/*     */ {
/*     */   private static class VillageFileFiler implements FilenameFilter
/*     */   {
/*     */     String end;
/*     */     
/*     */     public VillageFileFiler(String ending)
/*     */     {
/*  35 */       this.end = ending;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean accept(File file, String name)
/*     */     {
/*  41 */       if (!name.endsWith(this.end)) {
/*  42 */         return false;
/*     */       }
/*     */       
/*  45 */       if (name.startsWith(".")) {
/*  46 */         return false;
/*     */       }
/*     */       
/*  49 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<VillageType> loadLoneBuildings(List<File> culturesDirs, Culture culture)
/*     */   {
/*  57 */     List<File> dirs = new ArrayList();
/*     */     
/*  59 */     for (File culturesDir : culturesDirs) {
/*  60 */       File dir = new File(culturesDir, "lonebuildings");
/*     */       
/*  62 */       if (dir.exists()) {
/*  63 */         dirs.add(dir);
/*     */       }
/*     */     }
/*     */     
/*  67 */     File dircusto = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), culture.key), "custom lonebuildings");
/*     */     
/*  69 */     if (dircusto.exists()) {
/*  70 */       dirs.add(dircusto);
/*     */     }
/*     */     
/*  73 */     List<VillageType> v = new ArrayList();
/*     */     
/*  75 */     VillageFileFiler filer = new VillageFileFiler(".txt");
/*     */     
/*  77 */     for (File villageDir : dirs)
/*     */     {
/*  79 */       villageDir.mkdirs();
/*  80 */       for (File file : villageDir.listFiles(filer))
/*     */       {
/*     */         try
/*     */         {
/*  84 */           if (MLN.LogVillage >= 1) {
/*  85 */             MLN.major(file, "Loading lone building: " + file.getAbsolutePath());
/*     */           }
/*     */           
/*  88 */           VillageType village = new VillageType(culture, file, true);
/*  89 */           village.lonebuilding = true;
/*     */           
/*  91 */           v.remove(village);
/*  92 */           v.add(village);
/*     */         }
/*     */         catch (MLN.MillenaireException e) {
/*  95 */           MLN.error(null, e.getMessage());
/*     */         } catch (Exception e) {
/*  97 */           MLN.printException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 104 */     return v;
/*     */   }
/*     */   
/*     */   public static List<VillageType> loadVillages(List<File> culturesDirs, Culture culture)
/*     */   {
/* 109 */     List<File> dirs = new ArrayList();
/*     */     
/* 111 */     for (File culturesDir : culturesDirs) {
/* 112 */       File dir = new File(culturesDir, "villages");
/*     */       
/* 114 */       if (dir.exists()) {
/* 115 */         dirs.add(dir);
/*     */       }
/*     */     }
/*     */     
/* 119 */     File dircusto = new File(new File(new File(Mill.proxy.getCustomDir(), "cultures"), culture.key), "custom villages");
/*     */     
/* 121 */     if (dircusto.exists()) {
/* 122 */       dirs.add(dircusto);
/*     */     }
/*     */     
/* 125 */     List<VillageType> v = new ArrayList();
/*     */     
/* 127 */     VillageFileFiler filer = new VillageFileFiler(".txt");
/*     */     
/* 129 */     for (File villageDir : dirs)
/*     */     {
/* 131 */       villageDir.mkdirs();
/* 132 */       for (File file : villageDir.listFiles(filer))
/*     */       {
/*     */         try
/*     */         {
/* 136 */           if (MLN.LogVillage >= 1) {
/* 137 */             MLN.major(file, "Loading village: " + file.getAbsolutePath());
/*     */           }
/*     */           
/* 140 */           VillageType village = new VillageType(culture, file, false);
/*     */           
/* 142 */           v.remove(village);
/* 143 */           v.add(village);
/*     */         }
/*     */         catch (MLN.MillenaireException e) {
/* 146 */           MLN.error(null, e.getMessage());
/*     */         } catch (Exception e) {
/* 148 */           MLN.printException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 155 */     return v;
/*     */   }
/*     */   
/*     */   public static List<VillageType> spawnableVillages(EntityPlayer player)
/*     */   {
/* 160 */     List<VillageType> villages = new ArrayList();
/*     */     
/* 162 */     UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/*     */     
/* 164 */     for (Culture culture : Culture.ListCultures) {
/* 165 */       for (VillageType village : culture.listVillageTypes) {
/* 166 */         if ((village.spawnable) && (village.playerControlled) && ((MLN.DEV) || (profile.isTagSet("culturecontrol_" + village.culture.key)))) {
/* 167 */           villages.add(village);
/*     */         }
/*     */       }
/* 170 */       for (VillageType village : culture.listVillageTypes) {
/* 171 */         if ((village.spawnable) && (!village.playerControlled)) {
/* 172 */           villages.add(village);
/*     */         }
/*     */       }
/* 175 */       for (VillageType village : culture.listLoneBuildingTypes) {
/* 176 */         if ((village.spawnable) && ((MLN.DEV) || (!village.playerControlled) || (profile.isTagSet("culturecontrol_" + village.culture.key)))) {
/* 177 */           villages.add(village);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 182 */     return villages;
/*     */   }
/*     */   
/* 185 */   public String name = null; public String key = null; public String type = null;
/* 186 */   public int radius = MLN.VillageRadius; public int max = 0;
/* 187 */   public BuildingCustomPlan customCentre = null;
/* 188 */   public BuildingPlanSet centreBuilding = null;
/* 189 */   public List<BuildingPlanSet> startBuildings = new ArrayList();
/* 190 */   public List<BuildingPlanSet> playerBuildings = new ArrayList();
/* 191 */   public List<BuildingPlanSet> coreBuildings = new ArrayList();
/* 192 */   public List<BuildingPlanSet> secondaryBuildings = new ArrayList();
/* 193 */   public List<BuildingPlanSet> extraBuildings = new ArrayList();
/* 194 */   public List<BuildingPlanSet> excludedBuildings = new ArrayList();
/* 195 */   public List<BuildingCustomPlan> customBuildings = new ArrayList();
/* 196 */   public List<String> hamlets = new ArrayList();
/* 197 */   public List<String> qualifiers = new ArrayList();
/* 198 */   public HashMap<InvItem, Integer> sellingPrices = new HashMap();
/*     */   
/* 200 */   public HashMap<InvItem, Integer> buyingPrices = new HashMap();
/* 201 */   public List<InvItem> pathMaterial = new ArrayList();
/*     */   
/*     */ 
/* 204 */   public List<String> biomes = new ArrayList();
/* 205 */   List<String> requiredTags = new ArrayList();
/* 206 */   List<String> forbiddenTags = new ArrayList();
/* 207 */   public String nameList = null;
/* 208 */   public String hillQualifier = null;
/* 209 */   public String mountainQualifier = null;
/* 210 */   public String desertQualifier = null;
/* 211 */   public String forestQualifier = null;
/*     */   
/* 213 */   public String lavaQualifier = null;
/* 214 */   public String lakeQualifier = null;
/* 215 */   public String oceanQualifier = null;
/* 216 */   public boolean lonebuilding = false;
/*     */   
/* 218 */   public boolean keyLonebuilding = false;
/*     */   
/* 220 */   public String keyLoneBuildingGenerateTag = null;
/* 221 */   public boolean spawnable = true;
/*     */   
/* 223 */   public boolean carriesRaid = false;
/* 224 */   public boolean playerControlled = false;
/*     */   
/* 226 */   public boolean generateOnServer = true;
/*     */   
/* 228 */   public int minDistanceFromSpawn = 0;
/*     */   
/* 230 */   public boolean generatedForPlayer = false;
/*     */   public static final String HAMEAU = "hameau";
/*     */   
/* 233 */   public VillageType(Culture c, File file, boolean lone) throws Exception { this.lonebuilding = lone;
/* 234 */     this.spawnable = (!this.lonebuilding);
/* 235 */     this.culture = c;
/*     */     
/* 237 */     this.key = file.getName().split("\\.")[0];
/*     */     
/* 239 */     if (this.lonebuilding) {
/* 240 */       this.nameList = null;
/*     */     } else {
/* 242 */       this.nameList = "villages";
/*     */     }
/*     */     
/* 245 */     BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */     
/*     */     String line;
/* 248 */     while ((line = reader.readLine()) != null) {
/* 249 */       if ((line.trim().length() > 0) && (!line.trim().startsWith("//"))) {
/* 250 */         String[] temp = line.trim().split("\\:");
/* 251 */         if (temp.length == 2)
/*     */         {
/* 253 */           String paramkey = temp[0].trim();
/* 254 */           String value = temp[1].trim();
/* 255 */           if (paramkey.equalsIgnoreCase("name")) {
/* 256 */             this.name = value;
/* 257 */           } else if (paramkey.equalsIgnoreCase("spawnable")) {
/* 258 */             this.spawnable = Boolean.parseBoolean(value);
/* 259 */           } else if (paramkey.equalsIgnoreCase("generateonserver")) {
/* 260 */             this.generateOnServer = Boolean.parseBoolean(value);
/* 261 */           } else if (paramkey.equalsIgnoreCase("generateforplayer")) {
/* 262 */             this.generatedForPlayer = Boolean.parseBoolean(value);
/* 263 */           } else if (paramkey.equalsIgnoreCase("carriesraid")) {
/* 264 */             this.carriesRaid = Boolean.parseBoolean(value);
/* 265 */           } else if (paramkey.equalsIgnoreCase("keyLoneBuilding")) {
/* 266 */             this.keyLonebuilding = Boolean.parseBoolean(value);
/* 267 */           } else if (paramkey.equalsIgnoreCase("keyLoneBuildingGenerateTag")) {
/* 268 */             this.keyLoneBuildingGenerateTag = value;
/* 269 */           } else if (paramkey.equalsIgnoreCase("playerControlled")) {
/* 270 */             this.playerControlled = Boolean.parseBoolean(value);
/* 271 */           } else if (paramkey.equalsIgnoreCase("weight")) {
/* 272 */             this.weight = Integer.parseInt(value);
/* 273 */           } else if (paramkey.equalsIgnoreCase("max")) {
/* 274 */             this.max = Integer.parseInt(value);
/* 275 */           } else if (paramkey.equalsIgnoreCase("radius")) {
/* 276 */             this.radius = Integer.parseInt(value);
/* 277 */           } else if (paramkey.equalsIgnoreCase("minDistanceFromSpawn")) {
/* 278 */             this.minDistanceFromSpawn = Integer.parseInt(value);
/* 279 */           } else if (paramkey.equalsIgnoreCase("qualifier")) {
/* 280 */             this.qualifiers.add(value);
/* 281 */           } else if (paramkey.equalsIgnoreCase("hameau")) {
/* 282 */             this.hamlets.add(value);
/* 283 */           } else if (paramkey.equalsIgnoreCase("type")) {
/* 284 */             this.type = value;
/* 285 */           } else if (paramkey.equalsIgnoreCase("nameList")) {
/* 286 */             this.nameList = value;
/* 287 */           } else if (paramkey.equalsIgnoreCase("biome")) {
/* 288 */             this.biomes.add(value.toLowerCase());
/* 289 */           } else if (paramkey.equalsIgnoreCase("requiredtag")) {
/* 290 */             this.requiredTags.add(value);
/* 291 */           } else if (paramkey.equalsIgnoreCase("forbiddentag")) {
/* 292 */             this.forbiddenTags.add(value);
/* 293 */           } else if (paramkey.equalsIgnoreCase("hillQualifier")) {
/* 294 */             this.hillQualifier = value;
/* 295 */           } else if (paramkey.equalsIgnoreCase("mountainQualifier")) {
/* 296 */             this.mountainQualifier = value;
/* 297 */           } else if (paramkey.equalsIgnoreCase("desertQualifier")) {
/* 298 */             this.desertQualifier = value;
/* 299 */           } else if (paramkey.equalsIgnoreCase("forestQualifier")) {
/* 300 */             this.forestQualifier = value;
/* 301 */           } else if (paramkey.equalsIgnoreCase("lavaQualifier")) {
/* 302 */             this.lavaQualifier = value;
/* 303 */           } else if (paramkey.equalsIgnoreCase("lakeQualifier")) {
/* 304 */             this.lakeQualifier = value;
/* 305 */           } else if (paramkey.equalsIgnoreCase("oceanQualifier")) {
/* 306 */             this.oceanQualifier = value;
/* 307 */           } else if (paramkey.equalsIgnoreCase("pathMaterial")) {
/* 308 */             if (Goods.goodsName.containsKey(value.toLowerCase())) {
/* 309 */               this.pathMaterial.add(Goods.goodsName.get(value.toLowerCase()));
/*     */             } else {
/* 311 */               MLN.error(this, "When loading village type " + this.key + " could not recognise path material: " + value);
/*     */             }
/* 313 */           } else if (paramkey.equalsIgnoreCase("centre")) {
/* 314 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 315 */               this.centreBuilding = this.culture.getBuildingPlanSet(value);
/* 316 */               if (MLN.LogVillage >= 2) {
/* 317 */                 MLN.minor(this, "Loading centre building: " + value);
/*     */               }
/*     */             } else {
/* 320 */               throw new MLN.MillenaireException("When loading village type " + this.key + " could not find centre building type " + value + ".");
/*     */             }
/* 322 */           } else if (paramkey.equalsIgnoreCase("customcentre")) {
/* 323 */             if (this.culture.getBuildingCustom(value) != null) {
/* 324 */               this.customCentre = this.culture.getBuildingCustom(value);
/* 325 */               if (MLN.LogVillage >= 2) {
/* 326 */                 MLN.minor(this, "Loading custom centre building: " + value);
/*     */               }
/*     */             } else {
/* 329 */               throw new MLN.MillenaireException("When loading village type " + this.key + " could not find custom centre building type " + value + ".");
/*     */             }
/* 331 */           } else if (paramkey.equalsIgnoreCase("start")) {
/* 332 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 333 */               this.startBuildings.add(this.culture.getBuildingPlanSet(value));
/* 334 */               if (MLN.LogVillage >= 2) {
/* 335 */                 MLN.minor(this, "Loading start building: " + value);
/*     */               }
/*     */             } else {
/* 338 */               MLN.error(this, "When loading village type " + this.key + " could not find start building type " + value + ".");
/*     */             }
/* 340 */           } else if (paramkey.equalsIgnoreCase("player")) {
/* 341 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 342 */               this.playerBuildings.add(this.culture.getBuildingPlanSet(value));
/* 343 */               if (MLN.LogVillage >= 2) {
/* 344 */                 MLN.minor(this, "Loading player building: " + value);
/*     */               }
/*     */             } else {
/* 347 */               MLN.error(this, "When loading village type " + this.key + " could not find player building type " + value + ".");
/*     */             }
/* 349 */           } else if (paramkey.equalsIgnoreCase("core")) {
/* 350 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 351 */               this.coreBuildings.add(this.culture.getBuildingPlanSet(value));
/* 352 */               if (MLN.LogVillage >= 2) {
/* 353 */                 MLN.minor(this, "Loading core building: " + value);
/*     */               }
/*     */             } else {
/* 356 */               MLN.error(this, "When loading village type " + this.key + " could not find core building type " + value + ".");
/*     */             }
/* 358 */           } else if (paramkey.equalsIgnoreCase("secondary")) {
/* 359 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 360 */               this.secondaryBuildings.add(this.culture.getBuildingPlanSet(value));
/* 361 */               if (MLN.LogVillage >= 2) {
/* 362 */                 MLN.minor(this, "Loading secondary building: " + value);
/*     */               }
/*     */             } else {
/* 365 */               MLN.error(this, "When loading village type " + this.key + " could not find secondary building type " + value + ".");
/*     */             }
/* 367 */           } else if (paramkey.equalsIgnoreCase("never")) {
/* 368 */             if (this.culture.getBuildingPlanSet(value) != null) {
/* 369 */               this.excludedBuildings.add(this.culture.getBuildingPlanSet(value));
/* 370 */               if (MLN.LogVillage >= 2) {
/* 371 */                 MLN.minor(this, "Loading excluded building: " + value);
/*     */               }
/*     */             } else {
/* 374 */               MLN.error(this, "When loading village type " + this.key + " could not find excluded building type " + value + ".");
/*     */             }
/* 376 */           } else if (paramkey.equalsIgnoreCase("customBuilding")) {
/* 377 */             if (this.culture.getBuildingCustom(value) != null) {
/* 378 */               this.customBuildings.add(this.culture.getBuildingCustom(value));
/* 379 */               if (MLN.LogVillage >= 2) {
/* 380 */                 MLN.minor(this, "Loading custom building: " + value);
/*     */               }
/*     */             } else {
/* 383 */               MLN.error(this, "When loading village type " + this.key + " could not find custom building type " + value + ".");
/*     */             }
/* 385 */           } else if (paramkey.equalsIgnoreCase("sellingPrice"))
/*     */           {
/* 387 */             String goodstr = value.split(",")[0].toLowerCase();
/*     */             
/* 389 */             if (Goods.goodsName.containsKey(goodstr))
/*     */             {
/* 391 */               InvItem item = (InvItem)Goods.goodsName.get(goodstr);
/*     */               
/* 393 */               int price = 0;
/*     */               try {
/* 395 */                 String[] pricestr = value.split(",")[1].split("/");
/* 396 */                 if (pricestr.length == 1) {
/* 397 */                   price = Integer.parseInt(pricestr[0]);
/* 398 */                 } else if (pricestr.length == 2) {
/* 399 */                   price = Integer.parseInt(pricestr[0]) * 64 + Integer.parseInt(pricestr[1]);
/* 400 */                 } else if (pricestr.length == 3) {
/* 401 */                   price = Integer.parseInt(pricestr[0]) * 64 * 64 + Integer.parseInt(pricestr[1]) * 64 + Integer.parseInt(pricestr[2]);
/*     */                 } else {
/* 403 */                   MLN.error(this, "Could not parse the price in line: " + line);
/*     */                 }
/*     */               } catch (Exception e) {
/* 406 */                 MLN.error(this, "Exception when parsing the price in line: " + line);
/*     */               }
/* 408 */               if (price > 0) {
/* 409 */                 this.sellingPrices.put(item, Integer.valueOf(price));
/*     */               }
/*     */             } else {
/* 412 */               MLN.error(this, "Could not find the good in line: " + line);
/*     */             }
/* 414 */           } else if (paramkey.equalsIgnoreCase("buyingPrice"))
/*     */           {
/* 416 */             String goodstr = value.split(",")[0].toLowerCase();
/*     */             
/* 418 */             if (Goods.goodsName.containsKey(goodstr))
/*     */             {
/* 420 */               InvItem item = (InvItem)Goods.goodsName.get(goodstr);
/*     */               try {
/* 422 */                 int price = 0;
/*     */                 
/* 424 */                 String[] pricestr = value.split(",")[1].split("/");
/* 425 */                 if (pricestr.length == 1) {
/* 426 */                   price = Integer.parseInt(pricestr[0]);
/* 427 */                 } else if (pricestr.length == 2) {
/* 428 */                   price = Integer.parseInt(pricestr[0]) * 64 + Integer.parseInt(pricestr[1]);
/* 429 */                 } else if (pricestr.length == 3) {
/* 430 */                   price = Integer.parseInt(pricestr[0]) * 64 * 64 + Integer.parseInt(pricestr[1]) * 64 + Integer.parseInt(pricestr[2]);
/*     */                 } else {
/* 432 */                   MLN.error(this, "Could not parse the price in line: " + line);
/*     */                 }
/*     */                 
/* 435 */                 if (price > 0) {
/* 436 */                   this.buyingPrices.put(item, Integer.valueOf(price));
/*     */                 }
/*     */               } catch (Exception e) {
/* 439 */                 MLN.error(this, "Exception when parsing the price in line: " + line);
/*     */               }
/*     */             } else {
/* 442 */               MLN.error(this, "Could not find the good in line: " + line);
/*     */             }
/*     */           }
/*     */           else {
/* 446 */             MLN.error(this, "Could not recognise parameter " + paramkey + ": " + line);
/*     */           }
/*     */         }
/*     */         else {
/* 450 */           MLN.error(this, "Could not understand line: " + line);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 455 */     if (this.name == null) {
/* 456 */       throw new MLN.MillenaireException("No name found for village: " + this.key);
/*     */     }
/* 458 */     if ((this.centreBuilding == null) && (this.customCentre == null)) {
/* 459 */       throw new MLN.MillenaireException("No central building found for village: " + this.key);
/*     */     }
/*     */     
/* 462 */     for (BuildingPlanSet set : this.culture.ListPlanSets)
/*     */     {
/* 464 */       if (!this.excludedBuildings.contains(set))
/*     */       {
/* 466 */         int nb = 0;
/*     */         
/* 468 */         for (BuildingPlanSet aset : this.startBuildings) {
/* 469 */           if (aset == set) {
/* 470 */             nb++;
/*     */           }
/*     */         }
/*     */         
/* 474 */         for (BuildingPlanSet aset : this.coreBuildings) {
/* 475 */           if (aset == set) {
/* 476 */             nb++;
/*     */           }
/*     */         }
/*     */         
/* 480 */         for (BuildingPlanSet aset : this.secondaryBuildings) {
/* 481 */           if (aset == set) {
/* 482 */             nb++;
/*     */           }
/*     */         }
/*     */         
/* 486 */         for (int i = nb; i < set.max; i++) {
/* 487 */           this.extraBuildings.add(set);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 492 */     if (this.pathMaterial.size() == 0) {
/* 493 */       this.pathMaterial.add(Goods.goodsName.get("pathgravel"));
/*     */     }
/*     */     
/* 496 */     if (MLN.LogVillage >= 1) {
/* 497 */       MLN.major(this, "Loaded village type " + this.name + ". NameList: " + this.nameList);
/*     */     }
/*     */   }
/*     */   
/*     */   public VillageType(Culture c, String key, boolean lone)
/*     */   {
/* 503 */     this.key = key;
/* 504 */     this.culture = c;
/* 505 */     this.lonebuilding = lone;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 511 */     if (obj == this) {
/* 512 */       return true;
/*     */     }
/*     */     
/* 515 */     if (!(obj instanceof VillageType)) {
/* 516 */       return false;
/*     */     }
/*     */     
/* 519 */     VillageType v = (VillageType)obj;
/*     */     
/* 521 */     return (v.culture == this.culture) && (v.key.equals(this.key));
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<BuildingProject.EnumProjects, List<BuildingProject>> getBuildingProjects()
/*     */   {
/* 527 */     List<BuildingProject> centre = new ArrayList();
/* 528 */     if (this.centreBuilding != null) {
/* 529 */       centre.add(this.centreBuilding.getBuildingProject());
/*     */     }
/*     */     
/* 532 */     List<BuildingProject> start = new ArrayList();
/* 533 */     for (BuildingPlanSet set : this.startBuildings) {
/* 534 */       start.add(set.getBuildingProject());
/*     */     }
/*     */     
/* 537 */     List<BuildingProject> players = new ArrayList();
/* 538 */     if (!this.playerControlled) {
/* 539 */       for (BuildingPlanSet set : this.playerBuildings) {
/* 540 */         players.add(set.getBuildingProject());
/*     */       }
/*     */     }
/*     */     
/* 544 */     List<BuildingProject> core = new ArrayList();
/* 545 */     if (!this.playerControlled) {
/* 546 */       for (BuildingPlanSet set : this.coreBuildings) {
/* 547 */         core.add(set.getBuildingProject());
/*     */       }
/*     */     }
/*     */     
/* 551 */     List<BuildingProject> secondary = new ArrayList();
/* 552 */     if (!this.playerControlled) {
/* 553 */       for (BuildingPlanSet set : this.secondaryBuildings) {
/* 554 */         secondary.add(set.getBuildingProject());
/*     */       }
/*     */     }
/*     */     
/* 558 */     List<BuildingProject> extra = new ArrayList();
/* 559 */     if ((!this.playerControlled) && ((this.type == null) || (!this.type.equalsIgnoreCase("hameau"))) && (!this.lonebuilding)) {
/* 560 */       for (BuildingPlanSet set : this.extraBuildings) {
/* 561 */         extra.add(set.getBuildingProject());
/*     */       }
/*     */     }
/*     */     
/* 565 */     Map<BuildingProject.EnumProjects, List<BuildingProject>> v = new HashMap();
/* 566 */     v.put(BuildingProject.EnumProjects.CENTRE, centre);
/* 567 */     v.put(BuildingProject.EnumProjects.START, start);
/* 568 */     v.put(BuildingProject.EnumProjects.PLAYER, players);
/* 569 */     v.put(BuildingProject.EnumProjects.CORE, core);
/* 570 */     v.put(BuildingProject.EnumProjects.SECONDARY, secondary);
/* 571 */     v.put(BuildingProject.EnumProjects.EXTRA, extra);
/* 572 */     v.put(BuildingProject.EnumProjects.CUSTOMBUILDINGS, new ArrayList());
/*     */     
/* 574 */     return v;
/*     */   }
/*     */   
/*     */   public int getChoiceWeight(EntityPlayer player)
/*     */   {
/* 579 */     if (isKeyLoneBuildingForGeneration(player)) {
/* 580 */       return 10000;
/*     */     }
/*     */     
/* 583 */     return this.weight;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 588 */     return this.culture.hashCode() + this.key.hashCode();
/*     */   }
/*     */   
/*     */   public boolean isKeyLoneBuildingForGeneration(EntityPlayer player) {
/* 592 */     if (this.keyLonebuilding) {
/* 593 */       return true;
/*     */     }
/*     */     
/* 596 */     if (player != null) {
/* 597 */       UserProfile profile = Mill.getMillWorld(player.field_70170_p).getProfile(player.getDisplayName());
/*     */       
/* 599 */       if ((this.keyLoneBuildingGenerateTag != null) && (profile.isTagSet(this.keyLoneBuildingGenerateTag))) {
/* 600 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 604 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isValidForGeneration(MillWorld mw, EntityPlayer player, HashMap<String, Integer> nbVillages, Point pos, String biome, boolean keyLoneBuildingsOnly)
/*     */   {
/* 610 */     if ((!this.generateOnServer) && (Mill.proxy.isTrueServer())) {
/* 611 */       return false;
/*     */     }
/*     */     
/* 614 */     if ((this.minDistanceFromSpawn > 0) && (pos.horizontalDistanceTo(mw.world.func_72861_E()) <= this.minDistanceFromSpawn)) {
/* 615 */       return false;
/*     */     }
/*     */     
/* 618 */     for (String tag : this.requiredTags) {
/* 619 */       if (!mw.isGlobalTagSet(tag)) {
/* 620 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 624 */     for (String tag : this.forbiddenTags) {
/* 625 */       if (mw.isGlobalTagSet(tag)) {
/* 626 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 630 */     if ((keyLoneBuildingsOnly) && (!isKeyLoneBuildingForGeneration(player))) {
/* 631 */       return false;
/*     */     }
/*     */     
/* 634 */     if (!this.biomes.contains(biome)) {
/* 635 */       return false;
/*     */     }
/*     */     
/* 638 */     if (!isKeyLoneBuildingForGeneration(player)) {
/* 639 */       if ((this.max != 0) && (nbVillages.containsKey(this.key)) && (((Integer)nbVillages.get(this.key)).intValue() >= this.max)) {
/* 640 */         return false;
/*     */       }
/*     */     } else {
/* 643 */       boolean existingOneInRange = false;
/*     */       
/* 645 */       for (int i = 0; i < mw.loneBuildingsList.pos.size(); i++) {
/* 646 */         if ((((String)mw.loneBuildingsList.types.get(i)).equals(this.key)) && 
/* 647 */           (pos.horizontalDistanceTo((Point)mw.loneBuildingsList.pos.get(i)) < 2000.0D)) {
/* 648 */           existingOneInRange = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 653 */       if (existingOneInRange) {
/* 654 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 658 */     return true;
/*     */   }
/*     */   
/*     */   public void readVillageTypeInfoPacket(ByteBufInputStream ds) throws IOException {
/* 662 */     this.playerControlled = ds.readBoolean();
/* 663 */     this.spawnable = ds.readBoolean();
/* 664 */     this.name = StreamReadWrite.readNullableString(ds);
/* 665 */     this.type = StreamReadWrite.readNullableString(ds);
/* 666 */     this.radius = ds.read();
/*     */   }
/*     */   
/*     */   public int weight;
/*     */   public Culture culture;
/*     */   public void sendVillageTypePacket(EntityPlayer player) {}
/*     */   
/*     */   public String toString()
/*     */   {
/* 675 */     return this.key;
/*     */   }
/*     */   
/*     */   public void writeVillageTypeInfo(DataOutput data) throws IOException {
/* 679 */     data.writeUTF(this.key);
/* 680 */     data.writeBoolean(this.playerControlled);
/* 681 */     data.writeBoolean(this.spawnable);
/* 682 */     StreamReadWrite.writeNullableString(this.name, data);
/* 683 */     StreamReadWrite.writeNullableString(this.type, data);
/* 684 */     data.write(this.radius);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\VillageType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
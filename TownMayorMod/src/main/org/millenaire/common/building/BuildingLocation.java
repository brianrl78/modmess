/*     */ package org.millenaire.common.building;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.Point;
/*     */ 
/*     */ public class BuildingLocation implements Cloneable
/*     */ {
/*     */   public String planKey;
/*     */   public String shop;
/*     */   public List<String> maleResident;
/*     */   public List<String> femaleResident;
/*     */   
/*     */   public static BuildingLocation read(NBTTagCompound nbttagcompound, String label, String debug)
/*     */   {
/*  20 */     if (!nbttagcompound.func_74764_b(label + "_key")) {
/*  21 */       return null;
/*     */     }
/*     */     
/*  24 */     BuildingLocation bl = new BuildingLocation();
/*     */     
/*  26 */     bl.pos = Point.read(nbttagcompound, label + "_pos");
/*     */     
/*     */ 
/*  29 */     if (nbttagcompound.func_74764_b(label + "_isCustomBuilding")) {
/*  30 */       bl.isCustomBuilding = nbttagcompound.func_74767_n(label + "_isCustomBuilding");
/*     */     }
/*     */     
/*  33 */     Culture culture = Culture.getCultureByName(nbttagcompound.func_74779_i(label + "_culture"));
/*  34 */     bl.culture = culture;
/*     */     
/*  36 */     bl.orientation = nbttagcompound.func_74762_e(label + "_orientation");
/*  37 */     bl.length = nbttagcompound.func_74762_e(label + "_length");
/*  38 */     bl.width = nbttagcompound.func_74762_e(label + "_width");
/*  39 */     bl.minx = nbttagcompound.func_74762_e(label + "_minx");
/*  40 */     bl.miny = nbttagcompound.func_74762_e(label + "_miny");
/*  41 */     bl.minz = nbttagcompound.func_74762_e(label + "_minz");
/*  42 */     bl.maxx = nbttagcompound.func_74762_e(label + "_maxx");
/*  43 */     bl.maxy = nbttagcompound.func_74762_e(label + "_maxy");
/*  44 */     bl.maxz = nbttagcompound.func_74762_e(label + "_maxz");
/*     */     
/*  46 */     bl.level = nbttagcompound.func_74762_e(label + "_level");
/*  47 */     bl.planKey = nbttagcompound.func_74779_i(label + "_key");
/*     */     
/*     */ 
/*     */ 
/*  51 */     bl.shop = nbttagcompound.func_74779_i(label + "_shop");
/*  52 */     if (Culture.oldShopConversion.containsKey(bl.shop)) {
/*  53 */       bl.shop = ((String)Culture.oldShopConversion.get(bl.shop));
/*     */     }
/*     */     
/*  56 */     bl.setVariation(nbttagcompound.func_74762_e(label + "_variation"));
/*     */     
/*     */ 
/*     */ 
/*  60 */     bl.reputation = nbttagcompound.func_74762_e(label + "_reputation");
/*  61 */     bl.priorityMoveIn = nbttagcompound.func_74762_e(label + "_priorityMoveIn");
/*  62 */     bl.price = nbttagcompound.func_74762_e(label + "_price");
/*     */     
/*  64 */     if (bl.pos == null) {
/*  65 */       MLN.error(null, "Null point loaded for: " + label + "_pos");
/*     */     }
/*     */     
/*  68 */     bl.sleepingPos = Point.read(nbttagcompound, label + "_standingPos");
/*  69 */     bl.sellingPos = Point.read(nbttagcompound, label + "_sellingPos");
/*  70 */     bl.craftingPos = Point.read(nbttagcompound, label + "_craftingPos");
/*  71 */     bl.shelterPos = Point.read(nbttagcompound, label + "_shelterPos");
/*  72 */     bl.defendingPos = Point.read(nbttagcompound, label + "_defendingPos");
/*  73 */     bl.chestPos = Point.read(nbttagcompound, label + "_chestPos");
/*     */     
/*  75 */     List<String> maleResident = new ArrayList();
/*     */     
/*     */ 
/*  78 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c("maleResidentList", 10);
/*  79 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  80 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  81 */       maleResident.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/*  84 */     nbttaglist = nbttagcompound.func_150295_c(label + "_maleResidentList", 10);
/*  85 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  86 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  87 */       maleResident.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/*     */ 
/*  91 */     String s = nbttagcompound.func_74779_i(label + "_maleResident");
/*  92 */     if ((s != null) && (s.length() > 0)) {
/*  93 */       maleResident.add(s);
/*     */     }
/*     */     
/*  96 */     bl.maleResident = maleResident;
/*     */     
/*  98 */     List<String> femaleResident = new ArrayList();
/*     */     
/*     */ 
/* 101 */     nbttaglist = nbttagcompound.func_150295_c("femaleResidentList", 10);
/* 102 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 103 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 104 */       femaleResident.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/* 107 */     nbttaglist = nbttagcompound.func_150295_c(label + "_femaleResidentList", 10);
/* 108 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 109 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 110 */       femaleResident.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/*     */ 
/* 114 */     s = nbttagcompound.func_74779_i(label + "_femaleResident");
/* 115 */     if ((s != null) && (s.length() > 0)) {
/* 116 */       femaleResident.add(s);
/*     */     }
/*     */     
/* 119 */     bl.femaleResident = femaleResident;
/*     */     
/* 121 */     List<String> tags = new ArrayList();
/*     */     
/*     */ 
/* 124 */     nbttaglist = nbttagcompound.func_150295_c("tags", 10);
/* 125 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 126 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 127 */       String value = nbttagcompound1.func_74779_i("value").toLowerCase();
/*     */       
/*     */ 
/* 130 */       if ((value.equals("market1")) || (value.equals("market2")) || (value.equals("market3"))) {
/* 131 */         tags.add("market");
/*     */       } else {
/* 133 */         tags.add(value);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 138 */     nbttaglist = nbttagcompound.func_150295_c(label + "_tags", 10);
/* 139 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 140 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 141 */       String value = nbttagcompound1.func_74779_i("value");
/*     */       
/* 143 */       tags.add(value);
/*     */     }
/*     */     
/* 146 */     bl.tags = tags;
/*     */     
/* 148 */     List<String> subb = new ArrayList();
/*     */     
/*     */ 
/* 151 */     nbttaglist = nbttagcompound.func_150295_c("subBuildings", 10);
/* 152 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 153 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 154 */       subb.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/*     */ 
/* 158 */     nbttaglist = nbttagcompound.func_150295_c(label + "_subBuildings", 10);
/* 159 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 160 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/* 161 */       subb.add(nbttagcompound1.func_74779_i("value"));
/*     */     }
/*     */     
/*     */ 
/* 165 */     bl.subBuildings = subb;
/*     */     
/* 167 */     bl.showTownHallSigns = nbttagcompound.func_74767_n(label + "_showTownHallSigns");
/*     */     
/* 169 */     if (nbttagcompound.func_74764_b(label + "_upgradesAllowed")) {
/* 170 */       bl.upgradesAllowed = nbttagcompound.func_74767_n(label + "_upgradesAllowed");
/*     */     }
/*     */     
/* 173 */     if ((bl.getPlan() == null) && (bl.getCustomPlan() == null)) {
/* 174 */       MLN.error(bl, "Unknown building type: " + bl.planKey + " Cancelling load.");
/* 175 */       return null;
/*     */     }
/*     */     
/* 178 */     if (bl.isCustomBuilding) {
/* 179 */       bl.initialisePlan();
/*     */     } else {
/* 181 */       bl.computeMargins();
/*     */     }
/*     */     
/* 184 */     return bl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public int priorityMoveIn = 10;
/*     */   public int minx;
/*     */   public int maxx;
/*     */   public int minz;
/*     */   public int maxz;
/*     */   public int miny;
/* 196 */   public int maxy; public int minxMargin; public int maxxMargin; public int minyMargin; public int maxyMargin; public int minzMargin; public int maxzMargin; public int orientation; public int length; public int width; public int level; public int reputation; public int price; private int variation; public boolean isCustomBuilding = false;
/*     */   public Point pos;
/* 198 */   public Point chestPos = null; public Point sleepingPos = null;
/* 199 */   public Point sellingPos = null; public Point craftingPos = null; public Point shelterPos = null; public Point defendingPos = null;
/*     */   
/*     */   public Culture culture;
/*     */   public List<String> tags;
/*     */   public List<String> subBuildings;
/* 204 */   public boolean upgradesAllowed = true;
/*     */   
/* 206 */   public boolean bedrocklevel = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean showTownHallSigns;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingLocation() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingLocation(BuildingCustomPlan customBuilding, Point pos, boolean isTownHall)
/*     */   {
/* 229 */     this.pos = pos;
/* 230 */     this.chestPos = pos;
/* 231 */     this.orientation = 0;
/*     */     
/* 233 */     this.planKey = customBuilding.buildingKey;
/* 234 */     this.isCustomBuilding = true;
/* 235 */     this.level = 0;
/* 236 */     this.tags = customBuilding.tags;
/* 237 */     this.subBuildings = new ArrayList();
/* 238 */     setVariation(0);
/* 239 */     this.maleResident = customBuilding.maleResident;
/* 240 */     this.femaleResident = customBuilding.femaleResident;
/* 241 */     this.shop = customBuilding.shop;
/* 242 */     this.reputation = 0;
/* 243 */     this.price = 0;
/* 244 */     this.showTownHallSigns = isTownHall;
/* 245 */     this.culture = customBuilding.culture;
/* 246 */     this.priorityMoveIn = customBuilding.priorityMoveIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuildingLocation(BuildingPlan plan, Point ppos, int porientation)
/*     */   {
/* 257 */     this.pos = ppos;
/*     */     
/* 259 */     if (this.pos == null) {
/* 260 */       MLN.error(this, "Attempting to create a location with a null position.");
/*     */     }
/*     */     
/* 263 */     this.orientation = porientation;
/* 264 */     this.length = plan.length;
/* 265 */     this.width = plan.width;
/* 266 */     this.planKey = plan.buildingKey;
/* 267 */     this.level = plan.level;
/* 268 */     this.tags = plan.tags;
/* 269 */     this.subBuildings = plan.subBuildings;
/* 270 */     setVariation(plan.variation);
/* 271 */     this.maleResident = plan.maleResident;
/* 272 */     this.femaleResident = plan.femaleResident;
/* 273 */     this.shop = plan.shop;
/* 274 */     this.reputation = plan.reputation;
/* 275 */     this.price = plan.price;
/* 276 */     this.showTownHallSigns = plan.showTownHallSigns;
/* 277 */     this.culture = plan.culture;
/* 278 */     this.priorityMoveIn = plan.priorityMoveIn;
/*     */     
/* 280 */     if (!this.isCustomBuilding) {
/* 281 */       initialisePlan();
/*     */     }
/*     */   }
/*     */   
/*     */   public BuildingLocation clone()
/*     */   {
/*     */     try {
/* 288 */       return (BuildingLocation)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {}
/*     */     
/* 292 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void computeMargins()
/*     */   {
/* 300 */     this.minxMargin = (this.minx - MLN.minDistanceBetweenBuildings + 1);
/* 301 */     this.minzMargin = (this.minz - MLN.minDistanceBetweenBuildings + 1);
/* 302 */     this.minyMargin = (this.miny - 3);
/* 303 */     this.maxyMargin = (this.maxy + 1);
/* 304 */     this.maxxMargin = (this.maxx + MLN.minDistanceBetweenBuildings + 1);
/* 305 */     this.maxzMargin = (this.maxz + MLN.minDistanceBetweenBuildings + 1);
/*     */   }
/*     */   
/*     */   public BuildingLocation createLocationForLevel(int plevel) {
/* 309 */     BuildingPlan plan = ((BuildingPlan[])this.culture.getBuildingPlanSet(this.planKey).plans.get(getVariation()))[plevel];
/*     */     
/* 311 */     BuildingLocation bl = clone();
/* 312 */     bl.level = plevel;
/* 313 */     bl.tags = plan.tags;
/* 314 */     bl.subBuildings = plan.subBuildings;
/*     */     
/* 316 */     return bl;
/*     */   }
/*     */   
/*     */   public BuildingLocation createLocationForStartingSubBuilding(String subkey) {
/* 320 */     BuildingLocation bl = createLocationForSubBuilding(subkey);
/* 321 */     bl.level = 0;
/*     */     
/* 323 */     return bl;
/*     */   }
/*     */   
/*     */   public BuildingLocation createLocationForSubBuilding(String subkey) {
/* 327 */     BuildingPlan plan = this.culture.getBuildingPlanSet(subkey).getRandomStartingPlan();
/*     */     
/* 329 */     BuildingLocation bl = clone();
/* 330 */     bl.planKey = subkey;
/* 331 */     bl.level = -1;
/* 332 */     bl.tags = plan.tags;
/* 333 */     bl.subBuildings = plan.subBuildings;
/* 334 */     bl.maleResident = plan.maleResident;
/* 335 */     bl.femaleResident = plan.femaleResident;
/* 336 */     bl.shop = plan.shop;
/* 337 */     bl.reputation = plan.reputation;
/* 338 */     bl.price = plan.price;
/* 339 */     bl.showTownHallSigns = plan.showTownHallSigns;
/* 340 */     return bl;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 346 */     if ((obj == null) || (!(obj instanceof BuildingLocation))) {
/* 347 */       return false;
/*     */     }
/*     */     
/* 350 */     BuildingLocation bl = (BuildingLocation)obj;
/*     */     
/* 352 */     return (this.planKey.equals(bl.planKey)) && (this.level == bl.level) && (this.pos.equals(bl.pos)) && (this.orientation == bl.orientation) && (getVariation() == bl.getVariation());
/*     */   }
/*     */   
/*     */   public Building getBuilding(net.minecraft.world.World world)
/*     */   {
/* 357 */     return org.millenaire.common.forge.Mill.getMillWorld(world).getBuilding(this.chestPos);
/*     */   }
/*     */   
/*     */   public List<String> getBuildingEffects(net.minecraft.world.World world) {
/* 361 */     List<String> effects = new ArrayList();
/*     */     
/* 363 */     Building building = getBuilding(world);
/*     */     
/* 365 */     if ((building != null) && 
/* 366 */       (building.isTownhall)) {
/* 367 */       effects.add(MLN.string("effect.towncentre"));
/*     */     }
/*     */     
/*     */ 
/* 371 */     if ((this.shop != null) && (this.shop.length() > 0)) {
/* 372 */       effects.add(MLN.string("effect.shop", new String[] { this.culture.getCultureString("shop." + this.shop) }));
/*     */     }
/*     */     
/* 375 */     if (this.tags.contains("pujas")) {
/* 376 */       effects.add(MLN.string("effect.pujalocation"));
/*     */     }
/*     */     
/* 379 */     if (this.tags.contains("sacrifices")) {
/* 380 */       effects.add(MLN.string("effect.sacrificeslocation"));
/*     */     }
/*     */     
/* 383 */     BuildingPlan plan = getPlan();
/*     */     
/* 385 */     if ((plan != null) && 
/* 386 */       (plan.irrigation > 0)) {
/* 387 */       effects.add(MLN.string("effect.irrigation", new String[] { "" + plan.irrigation }));
/*     */     }
/*     */     
/*     */ 
/* 391 */     if ((building != null) && 
/* 392 */       (building.getResManager().healingspots.size() > 0)) {
/* 393 */       effects.add(MLN.string("effect.healing"));
/*     */     }
/*     */     
/*     */ 
/* 397 */     return effects;
/*     */   }
/*     */   
/*     */   public Point[] getCorners()
/*     */   {
/* 402 */     Point[] corners = new Point[4];
/*     */     
/* 404 */     corners[0] = new Point(this.minxMargin, this.pos.getiY(), this.minzMargin);
/* 405 */     corners[1] = new Point(this.maxxMargin, this.pos.getiY(), this.minzMargin);
/* 406 */     corners[2] = new Point(this.minxMargin, this.pos.getiY(), this.maxzMargin);
/* 407 */     corners[3] = new Point(this.maxxMargin, this.pos.getiY(), this.maxzMargin);
/* 408 */     return corners;
/*     */   }
/*     */   
/*     */   public BuildingCustomPlan getCustomPlan() {
/* 412 */     if (this.culture == null) {
/* 413 */       MLN.error(this, "null culture");
/* 414 */       return null;
/*     */     }
/*     */     
/* 417 */     if (this.culture.getBuildingCustom(this.planKey) != null) {
/* 418 */       return this.culture.getBuildingCustom(this.planKey);
/*     */     }
/* 420 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullDisplayName()
/*     */   {
/* 432 */     if (this.isCustomBuilding) {
/* 433 */       return getCustomPlan().getFullDisplayName();
/*     */     }
/* 435 */     return getPlan().getFullDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGameName()
/*     */   {
/* 447 */     if (this.isCustomBuilding) {
/* 448 */       return getCustomPlan().getGameName();
/*     */     }
/* 450 */     return getPlan().getGameName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getNativeName()
/*     */   {
/* 462 */     if (this.isCustomBuilding) {
/* 463 */       return getCustomPlan().nativeName;
/*     */     }
/* 465 */     return getPlan().nativeName;
/*     */   }
/*     */   
/*     */   public BuildingPlan getPlan()
/*     */   {
/* 470 */     if (this.culture == null) {
/* 471 */       MLN.error(this, "null culture");
/* 472 */       return null;
/*     */     }
/*     */     
/* 475 */     if ((this.culture.getBuildingPlanSet(this.planKey) != null) && (this.culture.getBuildingPlanSet(this.planKey).plans.size() > getVariation())) {
/* 476 */       if (this.level < 0) {
/* 477 */         return ((BuildingPlan[])this.culture.getBuildingPlanSet(this.planKey).plans.get(getVariation()))[0];
/*     */       }
/* 479 */       if (((BuildingPlan[])this.culture.getBuildingPlanSet(this.planKey).plans.get(getVariation())).length > this.level) {
/* 480 */         return ((BuildingPlan[])this.culture.getBuildingPlanSet(this.planKey).plans.get(getVariation()))[this.level];
/*     */       }
/* 482 */       return null;
/*     */     }
/* 484 */     return null;
/*     */   }
/*     */   
/*     */   public Point getSellingPos()
/*     */   {
/* 489 */     if (this.sellingPos != null) {
/* 490 */       return this.sellingPos;
/*     */     }
/*     */     
/* 493 */     return this.sleepingPos;
/*     */   }
/*     */   
/*     */   public int getVariation() {
/* 497 */     return this.variation;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 502 */     return (this.planKey + "_" + this.level + " at " + this.pos + "/" + this.orientation + "/" + getVariation()).hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initialisePlan()
/*     */   {
/* 510 */     Point op1 = BuildingPlan.adjustForOrientation(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), this.length / 2, this.width / 2, this.orientation);
/* 511 */     Point op2 = BuildingPlan.adjustForOrientation(this.pos.getiX(), this.pos.getiY(), this.pos.getiZ(), -this.length / 2, -this.width / 2, this.orientation);
/*     */     
/* 513 */     if (op1.getiX() > op2.getiX()) {
/* 514 */       this.minx = op2.getiX();
/* 515 */       this.maxx = op1.getiX();
/*     */     } else {
/* 517 */       this.minx = op1.getiX();
/* 518 */       this.maxx = op2.getiX();
/*     */     }
/*     */     
/* 521 */     if (op1.getiZ() > op2.getiZ()) {
/* 522 */       this.minz = op2.getiZ();
/* 523 */       this.maxz = op1.getiZ();
/*     */     } else {
/* 525 */       this.minz = op1.getiZ();
/* 526 */       this.maxz = op2.getiZ();
/*     */     }
/*     */     
/* 529 */     if (getPlan() != null) {
/* 530 */       this.miny = (this.pos.getiY() + getPlan().startLevel);
/* 531 */       this.maxy = (this.miny + getPlan().nbfloors);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 536 */       this.miny = (this.pos.getiY() - 5);
/* 537 */       this.maxy = (this.pos.getiY() + 20);
/*     */     }
/*     */     
/* 540 */     computeMargins();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInside(Point p)
/*     */   {
/* 547 */     if ((this.minx < p.getiX()) && (p.getiX() <= this.maxx) && (this.miny < p.getiY()) && (p.getiY() <= this.maxy) && (this.minz < p.getiZ()) && (p.getiZ() <= this.maxz)) {
/* 548 */       return true;
/*     */     }
/* 550 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInsidePlanar(Point p)
/*     */   {
/* 557 */     if ((this.minx < p.getiX()) && (p.getiX() <= this.maxx) && (this.minz < p.getiZ()) && (p.getiZ() <= this.maxz)) {
/* 558 */       return true;
/*     */     }
/* 560 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInsideZone(Point p)
/*     */   {
/* 568 */     if ((this.minxMargin <= p.getiX()) && (p.getiX() <= this.maxxMargin) && (this.minyMargin <= p.getiY()) && (p.getiY() <= this.maxyMargin) && (this.minzMargin <= p.getiZ()) && (p.getiZ() <= this.maxzMargin)) {
/* 569 */       return true;
/*     */     }
/* 571 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInsideZonePlanar(Point p)
/*     */   {
/* 579 */     if ((this.minxMargin <= p.getiX()) && (p.getiX() <= this.maxxMargin) && (this.minzMargin <= p.getiZ()) && (p.getiZ() <= this.maxzMargin)) {
/* 580 */       return true;
/*     */     }
/* 582 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isLocationSamePlace(BuildingLocation l) {
/* 586 */     if (l == null) {
/* 587 */       return false;
/*     */     }
/*     */     
/* 590 */     return (this.pos.equals(l.pos)) && (this.orientation == l.orientation) && (getVariation() == l.getVariation());
/*     */   }
/*     */   
/*     */   public boolean isSameLocation(BuildingLocation l) {
/* 594 */     if (l == null) {
/* 595 */       return false;
/*     */     }
/*     */     
/* 598 */     boolean samePlanKey = ((this.planKey == null) && (l.planKey == null)) || (this.planKey.equals(l.planKey));
/*     */     
/* 600 */     return (this.pos.equals(l.pos)) && (samePlanKey) && (this.orientation == l.orientation) && (getVariation() == l.getVariation()) && (this.isCustomBuilding == l.isCustomBuilding);
/*     */   }
/*     */   
/*     */   public int oldHashCode() {
/* 604 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public void setVariation(int var) {
/* 608 */     this.variation = var;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 613 */     return this.planKey + "_" + this.level + " at " + this.pos + "/" + this.orientation + "/" + getVariation();
/*     */   }
/*     */   
/*     */   public void write(NBTTagCompound nbttagcompound, String label, String debug)
/*     */   {
/* 618 */     this.pos.write(nbttagcompound, label + "_pos");
/*     */     
/* 620 */     nbttagcompound.func_74757_a(label + "_isCustomBuilding", this.isCustomBuilding);
/* 621 */     nbttagcompound.func_74778_a(label + "_culture", this.culture.key);
/* 622 */     nbttagcompound.func_74768_a(label + "_orientation", this.orientation);
/* 623 */     nbttagcompound.func_74768_a(label + "_minx", this.minx);
/* 624 */     nbttagcompound.func_74768_a(label + "_miny", this.miny);
/* 625 */     nbttagcompound.func_74768_a(label + "_minz", this.minz);
/* 626 */     nbttagcompound.func_74768_a(label + "_maxx", this.maxx);
/* 627 */     nbttagcompound.func_74768_a(label + "_maxy", this.maxy);
/* 628 */     nbttagcompound.func_74768_a(label + "_maxz", this.maxz);
/* 629 */     nbttagcompound.func_74768_a(label + "_length", this.length);
/* 630 */     nbttagcompound.func_74768_a(label + "_width", this.width);
/* 631 */     nbttagcompound.func_74768_a(label + "_level", this.level);
/* 632 */     nbttagcompound.func_74778_a(label + "_key", this.planKey);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 637 */     nbttagcompound.func_74768_a(label + "_variation", getVariation());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 642 */     nbttagcompound.func_74768_a(label + "_reputation", this.reputation);
/* 643 */     nbttagcompound.func_74768_a(label + "_price", this.price);
/* 644 */     nbttagcompound.func_74768_a(label + "_priorityMoveIn", this.priorityMoveIn);
/* 645 */     if ((this.shop != null) && (this.shop.length() > 0)) {
/* 646 */       nbttagcompound.func_74778_a(label + "_shop", this.shop);
/*     */     }
/*     */     
/* 649 */     NBTTagList nbttaglist = new NBTTagList();
/* 650 */     for (String tag : this.maleResident)
/*     */     {
/* 652 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 653 */       nbttagcompound1.func_74778_a("value", tag);
/* 654 */       nbttaglist.func_74742_a(nbttagcompound1);
/*     */     }
/* 656 */     nbttagcompound.func_74782_a(label + "_maleResidentList", nbttaglist);
/*     */     
/* 658 */     nbttaglist = new NBTTagList();
/* 659 */     for (String tag : this.femaleResident)
/*     */     {
/* 661 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 662 */       nbttagcompound1.func_74778_a("value", tag);
/* 663 */       nbttaglist.func_74742_a(nbttagcompound1);
/*     */     }
/* 665 */     nbttagcompound.func_74782_a(label + "_femaleResidentList", nbttaglist);
/*     */     
/* 667 */     nbttaglist = new NBTTagList();
/* 668 */     for (String tag : this.tags)
/*     */     {
/* 670 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 671 */       nbttagcompound1.func_74778_a("value", tag);
/* 672 */       nbttaglist.func_74742_a(nbttagcompound1);
/*     */     }
/*     */     
/* 675 */     nbttagcompound.func_74782_a(label + "_tags", nbttaglist);
/*     */     
/* 677 */     nbttaglist = new NBTTagList();
/* 678 */     for (String subb : this.subBuildings)
/*     */     {
/* 680 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 681 */       nbttagcompound1.func_74778_a("value", subb);
/* 682 */       nbttaglist.func_74742_a(nbttagcompound1);
/*     */     }
/* 684 */     nbttagcompound.func_74782_a(label + "_subBuildings", nbttaglist);
/*     */     
/* 686 */     if (this.sleepingPos != null) {
/* 687 */       this.sleepingPos.write(nbttagcompound, label + "_standingPos");
/*     */     }
/* 689 */     if (this.sellingPos != null) {
/* 690 */       this.sellingPos.write(nbttagcompound, label + "_sellingPos");
/*     */     }
/* 692 */     if (this.craftingPos != null) {
/* 693 */       this.craftingPos.write(nbttagcompound, label + "_craftingPos");
/*     */     }
/* 695 */     if (this.defendingPos != null) {
/* 696 */       this.defendingPos.write(nbttagcompound, label + "_defendingPos");
/*     */     }
/* 698 */     if (this.shelterPos != null) {
/* 699 */       this.shelterPos.write(nbttagcompound, label + "_shelterPos");
/*     */     }
/* 701 */     if (this.chestPos != null) {
/* 702 */       this.chestPos.write(nbttagcompound, label + "_chestPos");
/*     */     }
/*     */     
/* 705 */     nbttagcompound.func_74757_a(label + "_showTownHallSigns", this.showTownHallSigns);
/* 706 */     nbttagcompound.func_74757_a(label + "_upgradesAllowed", this.upgradesAllowed);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\building\BuildingLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.millenaire.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.forge.Mill;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerRecord
/*     */   implements Cloneable
/*     */ {
/*     */   public static VillagerRecord read(MillWorld mw, Culture thCulture, Point th, NBTTagCompound nbttagcompound, String label)
/*     */   {
/*  25 */     if ((!nbttagcompound.func_74764_b(label + "_id")) && (!nbttagcompound.func_74764_b(label + "_lid"))) {
/*  26 */       return null;
/*     */     }
/*     */     
/*     */     VillagerRecord vr;
/*     */     VillagerRecord vr;
/*  31 */     if (nbttagcompound.func_74764_b(label + "_culture")) {
/*  32 */       vr = new VillagerRecord(mw, Culture.getCultureByName(nbttagcompound.func_74779_i(label + "_culture")));
/*     */     } else {
/*  34 */       vr = new VillagerRecord(mw, thCulture);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  39 */     if (nbttagcompound.func_74764_b(label + "_lid")) {
/*  40 */       vr.id = Math.abs(nbttagcompound.func_74763_f(label + "_lid"));
/*     */     }
/*     */     
/*  43 */     vr.nb = nbttagcompound.func_74762_e(label + "_nb");
/*  44 */     vr.gender = nbttagcompound.func_74762_e(label + "_gender");
/*  45 */     vr.type = nbttagcompound.func_74779_i(label + "_type").toLowerCase();
/*     */     
/*  47 */     vr.raiderSpawn = nbttagcompound.func_74763_f(label + "_raiderSpawn");
/*  48 */     vr.firstName = nbttagcompound.func_74779_i(label + "_firstName");
/*  49 */     vr.familyName = nbttagcompound.func_74779_i(label + "_familyName");
/*  50 */     vr.nameKey = nbttagcompound.func_74779_i(label + "_propertype");
/*  51 */     vr.occupation = nbttagcompound.func_74779_i(label + "_occupation");
/*  52 */     vr.texture = new ResourceLocation("millenaire", nbttagcompound.func_74779_i(label + "_texture"));
/*  53 */     vr.housePos = Point.read(nbttagcompound, label + "_housePos");
/*  54 */     vr.townHallPos = Point.read(nbttagcompound, label + "_townHallPos");
/*  55 */     vr.originalVillagePos = Point.read(nbttagcompound, label + "_originalVillagePos");
/*     */     
/*  57 */     if (vr.townHallPos == null) {
/*  58 */       vr.townHallPos = th;
/*     */     }
/*     */     
/*  61 */     vr.villagerSize = nbttagcompound.func_74762_e(label + "_size");
/*     */     
/*  63 */     vr.fathersName = nbttagcompound.func_74779_i(label + "_fathersName");
/*  64 */     vr.mothersName = nbttagcompound.func_74779_i(label + "_mothersName");
/*  65 */     vr.maidenName = nbttagcompound.func_74779_i(label + "_maidenName");
/*  66 */     vr.spousesName = nbttagcompound.func_74779_i(label + "_spousesName");
/*  67 */     vr.killed = nbttagcompound.func_74767_n(label + "_killed");
/*  68 */     vr.raidingVillage = nbttagcompound.func_74767_n(label + "_raidingVillage");
/*  69 */     vr.awayraiding = nbttagcompound.func_74767_n(label + "_awayraiding");
/*  70 */     vr.awayhired = nbttagcompound.func_74767_n(label + "_awayhired");
/*     */     
/*  72 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c(label + "questTags", 10);
/*  73 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  74 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*  75 */       vr.questTags.add(nbttagcompound1.func_74779_i("tag"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  81 */     nbttaglist = nbttagcompound.func_150295_c(label + "_inventory", 10);
/*  82 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/*  83 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*     */       try {
/*  85 */         vr.inventory.put(new InvItem(Item.func_150899_d(nbttagcompound1.func_74762_e("item")), nbttagcompound1.func_74762_e("meta")), Integer.valueOf(nbttagcompound1.func_74762_e("amount")));
/*     */       } catch (MLN.MillenaireException e) {
/*  87 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  91 */     nbttaglist = nbttagcompound.func_150295_c(label + "_inventoryNew", 10);
/*  92 */     MillCommonUtilities.readInventory(nbttaglist, vr.inventory);
/*     */     
/*  94 */     if (vr.getType() == null) {
/*  95 */       MLN.error(vr, "Could not find type " + vr.type + " for VR. Skipping.");
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     return vr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 104 */   public String fathersName = ""; public String mothersName = ""; public String spousesName = ""; public String maidenName = "";
/*     */   
/* 106 */   public boolean flawedRecord = false;
/* 107 */   public boolean killed = false;
/* 108 */   public boolean raidingVillage = false;
/* 109 */   public boolean awayraiding = false;
/* 110 */   public boolean awayhired = false;
/*     */   
/* 112 */   public long raiderSpawn = 0L;
/*     */   
/* 114 */   public HashMap<InvItem, Integer> inventory = new HashMap();
/*     */   
/* 116 */   public List<String> questTags = new ArrayList();
/*     */   
/*     */   public Culture culture;
/*     */   public Point housePos;
/*     */   public Point townHallPos;
/*     */   public Point originalVillagePos;
/*     */   public long id;
/*     */   public int nb;
/*     */   public int gender;
/*     */   public int villagerSize;
/*     */   public String type;
/*     */   
/*     */   public VillagerRecord(MillWorld mw)
/*     */   {
/* 130 */     this.mw = mw;
/*     */   }
/*     */   
/*     */   private VillagerRecord(MillWorld mw, Culture c) {
/* 134 */     this.culture = c;
/* 135 */     this.mw = mw;
/*     */   }
/*     */   
/*     */   public VillagerRecord(MillWorld mw, MillVillager v) {
/* 139 */     this.mw = mw;
/* 140 */     this.culture = v.getCulture();
/* 141 */     this.id = v.villager_id;
/* 142 */     if (v.vtype != null) {
/* 143 */       this.type = v.vtype.key;
/*     */     }
/* 145 */     this.firstName = v.firstName;
/* 146 */     this.familyName = v.familyName;
/* 147 */     this.nameKey = v.getNameKey();
/* 148 */     this.occupation = v.getNativeOccupationName();
/* 149 */     this.gender = v.gender;
/* 150 */     this.nb = 1;
/* 151 */     this.texture = v.getTexture();
/* 152 */     this.housePos = v.housePoint;
/* 153 */     this.townHallPos = v.townHallPoint;
/* 154 */     this.villagerSize = v.size;
/* 155 */     this.raidingVillage = v.isRaider;
/*     */     
/* 157 */     for (InvItem iv : v.getInventoryKeys()) {
/* 158 */       this.inventory.put(iv, Integer.valueOf(v.countInv(iv)));
/*     */     }
/*     */     
/* 161 */     if (this.housePos == null) {
/* 162 */       MLN.error(this, "Creation constructor: House position in record is null.");
/* 163 */       this.flawedRecord = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public VillagerRecord clone()
/*     */   {
/*     */     try {
/* 170 */       return (VillagerRecord)super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 172 */       MLN.printException(e);
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */   
/*     */   public int countInv(InvItem key) {
/* 178 */     if (this.inventory.containsKey(key)) {
/* 179 */       return ((Integer)this.inventory.get(key)).intValue();
/*     */     }
/* 181 */     return 0;
/*     */   }
/*     */   
/*     */   public int countInv(Item item)
/*     */   {
/* 186 */     return countInv(item, 0);
/*     */   }
/*     */   
/*     */   public int countInv(Item item, int meta)
/*     */   {
/*     */     try {
/* 192 */       InvItem key = new InvItem(item, meta);
/*     */       
/* 194 */       if (this.inventory.containsKey(key)) {
/* 195 */         return ((Integer)this.inventory.get(key)).intValue();
/*     */       }
/* 197 */       return 0;
/*     */     }
/*     */     catch (MLN.MillenaireException e) {
/* 200 */       MLN.printException(e);
/*     */     }
/* 202 */     return 0; }
/*     */   
/*     */   public String firstName;
/*     */   public String familyName;
/*     */   public String nameKey;
/*     */   public String occupation;
/*     */   
/* 209 */   public boolean equals(Object obj) { if (this == obj) {
/* 210 */       return true;
/*     */     }
/* 212 */     if (!(obj instanceof VillagerRecord)) {
/* 213 */       return false;
/*     */     }
/* 215 */     VillagerRecord other = (VillagerRecord)obj;
/*     */     
/* 217 */     return other.id == this.id; }
/*     */   
/*     */   public ResourceLocation texture;
/*     */   private Building house;
/*     */   
/* 222 */   public ItemStack getArmourPiece(int type) { if (type == 0) {
/* 223 */       for (Item weapon : MillVillager.helmets) {
/* 224 */         if (countInv(weapon) > 0) {
/* 225 */           return new ItemStack(weapon, 1);
/*     */         }
/*     */       }
/* 228 */       return null;
/*     */     }
/* 230 */     if (type == 1) {
/* 231 */       for (Item weapon : MillVillager.chestplates) {
/* 232 */         if (countInv(weapon) > 0) {
/* 233 */           return new ItemStack(weapon, 1);
/*     */         }
/*     */       }
/* 236 */       return null;
/*     */     }
/* 238 */     if (type == 2) {
/* 239 */       for (Item weapon : MillVillager.legs) {
/* 240 */         if (countInv(weapon) > 0) {
/* 241 */           return new ItemStack(weapon, 1);
/*     */         }
/*     */       }
/* 244 */       return null;
/*     */     }
/* 246 */     if (type == 3) {
/* 247 */       for (Item weapon : MillVillager.boots) {
/* 248 */         if (countInv(weapon) > 0) {
/* 249 */           return new ItemStack(weapon, 1);
/*     */         }
/*     */       }
/* 252 */       return null;
/*     */     }
/*     */     
/* 255 */     return null; }
/*     */   
/*     */   private Building townHall;
/*     */   private Building originalVillage;
/*     */   public MillWorld mw;
/* 260 */   public Item getBestMeleeWeapon() { double max = 1.0D;
/* 261 */     Item best = null;
/*     */     
/* 263 */     for (InvItem item : this.inventory.keySet()) {
/* 264 */       if (((Integer)this.inventory.get(item)).intValue() > 0) {
/* 265 */         if (item.getItem() == null) {
/* 266 */           MLN.error(this, "Attempting to check null melee weapon with id: " + this.inventory.get(item));
/*     */         }
/* 268 */         else if (MillCommonUtilities.getItemWeaponDamage(item.getItem()) > max) {
/* 269 */           max = MillCommonUtilities.getItemWeaponDamage(item.getItem());
/* 270 */           best = item.getItem();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 276 */     if ((getType() != null) && (getType().startingWeapon != null) && 
/* 277 */       (MillCommonUtilities.getItemWeaponDamage(getType().startingWeapon.getItem()) > max)) {
/* 278 */       max = MillCommonUtilities.getItemWeaponDamage(getType().startingWeapon.getItem());
/* 279 */       best = getType().startingWeapon.getItem();
/*     */     }
/*     */     
/*     */ 
/* 283 */     return best;
/*     */   }
/*     */   
/*     */   public String getGameOccupation(String username)
/*     */   {
/* 288 */     if ((this.culture == null) || (this.culture.getVillagerType(this.nameKey) == null)) {
/* 289 */       return "";
/*     */     }
/*     */     
/* 292 */     String s = this.culture.getVillagerType(this.nameKey).name;
/*     */     
/* 294 */     if (this.culture.canReadVillagerNames(username))
/*     */     {
/* 296 */       String game = this.culture.getCultureString("villager." + this.nameKey);
/*     */       
/* 298 */       if (!game.equals("")) {
/* 299 */         s = s + " (" + game + ")";
/*     */       }
/*     */     }
/*     */     
/* 303 */     return s;
/*     */   }
/*     */   
/*     */   public Building getHouse() {
/* 307 */     if (this.house != null) {
/* 308 */       return this.house;
/*     */     }
/* 310 */     if (MLN.LogVillager >= 3) {
/* 311 */       MLN.debug(this, "Seeking uncached house");
/*     */     }
/* 313 */     this.house = this.mw.getBuilding(this.housePos);
/*     */     
/* 315 */     return this.house;
/*     */   }
/*     */   
/*     */   public int getMaxHealth()
/*     */   {
/* 320 */     if (getType() == null) {
/* 321 */       return 20;
/*     */     }
/*     */     
/* 324 */     if (getType().isChild) {
/* 325 */       return 10 + this.villagerSize / 2;
/*     */     }
/*     */     
/* 328 */     return getType().health;
/*     */   }
/*     */   
/*     */   public int getMilitaryStrength()
/*     */   {
/* 333 */     int strength = getMaxHealth() / 2;
/*     */     
/* 335 */     int attack = getType().baseAttackStrength;
/*     */     
/* 337 */     Item bestMelee = getBestMeleeWeapon();
/*     */     
/* 339 */     if (bestMelee != null) {
/* 340 */       attack = (int)(attack + MillCommonUtilities.getItemWeaponDamage(bestMelee));
/*     */     }
/*     */     
/* 343 */     strength += attack * 2;
/*     */     
/* 345 */     if (((getType().isArcher) && (countInv(Items.field_151031_f) > 0)) || (countInv(Mill.yumiBow) > 0)) {
/* 346 */       strength += 10;
/*     */     }
/*     */     
/* 349 */     strength += getTotalArmorValue() * 2;
/*     */     
/* 351 */     return strength;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 355 */     return this.firstName + " " + this.familyName;
/*     */   }
/*     */   
/*     */   public String getNativeOccupationName() {
/* 359 */     if ((getType().isChild) && (this.villagerSize == 20)) {
/* 360 */       return getType().altname;
/*     */     }
/* 362 */     return getType().name;
/*     */   }
/*     */   
/*     */   public Building getOriginalVillage() {
/* 366 */     if (this.originalVillage != null)
/*     */     {
/* 368 */       return this.originalVillage;
/*     */     }
/*     */     
/* 371 */     if (MLN.LogVillager >= 3) {
/* 372 */       MLN.debug(this, "Seeking uncached originalVillage");
/*     */     }
/* 374 */     this.originalVillage = this.mw.getBuilding(this.originalVillagePos);
/*     */     
/* 376 */     return this.originalVillage;
/*     */   }
/*     */   
/*     */   public int getTotalArmorValue() {
/* 380 */     int total = 0;
/* 381 */     for (int i = 0; i < 4; i++) {
/* 382 */       ItemStack armour = getArmourPiece(i);
/*     */       
/* 384 */       if ((armour != null) && ((armour.func_77973_b() instanceof ItemArmor))) {
/* 385 */         total += ((ItemArmor)armour.func_77973_b()).field_77879_b;
/*     */       }
/*     */     }
/* 388 */     return total;
/*     */   }
/*     */   
/*     */   public Building getTownHall() {
/* 392 */     if (this.townHall != null)
/*     */     {
/* 394 */       return this.townHall;
/*     */     }
/*     */     
/* 397 */     if (MLN.LogVillager >= 3) {
/* 398 */       MLN.debug(this, "Seeking uncached townHall");
/*     */     }
/* 400 */     this.townHall = this.mw.getBuilding(this.townHallPos);
/*     */     
/* 402 */     return this.townHall;
/*     */   }
/*     */   
/*     */   public VillagerType getType() {
/* 406 */     if (this.culture.getVillagerType(this.type) == null) {
/* 407 */       for (Culture c : Culture.ListCultures) {
/* 408 */         if (c.getVillagerType(this.type) != null) {
/* 409 */           MLN.error(this, "Could not find villager type " + this.type + " in culture " + this.culture.key + " but could in " + c.key + " so switching.");
/* 410 */           this.culture = c;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 415 */     return this.culture.getVillagerType(this.type);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 420 */     return Long.valueOf(this.id).hashCode();
/*     */   }
/*     */   
/*     */   public boolean matches(MillVillager v) {
/* 424 */     return this.id == v.villager_id;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 429 */     return this.firstName + " " + this.familyName + "/" + this.type + "/" + this.nameKey + "/" + this.texture + "/" + this.id;
/*     */   }
/*     */   
/*     */   public void updateRecord(MillVillager v) {
/* 433 */     this.id = v.villager_id;
/* 434 */     if (v.vtype != null) {
/* 435 */       this.type = v.vtype.key;
/*     */     }
/* 437 */     this.firstName = v.firstName;
/* 438 */     this.familyName = v.familyName;
/* 439 */     this.nameKey = v.getNameKey();
/* 440 */     this.occupation = v.getNativeOccupationName();
/* 441 */     this.gender = v.gender;
/* 442 */     this.nb = 1;
/* 443 */     this.texture = v.getTexture();
/* 444 */     this.housePos = v.housePoint;
/* 445 */     this.townHallPos = v.townHallPoint;
/* 446 */     this.villagerSize = v.size;
/* 447 */     this.raidingVillage = v.isRaider;
/* 448 */     this.killed = v.field_70128_L;
/*     */     
/* 450 */     if (this.housePos == null) {
/* 451 */       MLN.error(this, "updateRecord(): House position in record is null.");
/* 452 */       this.flawedRecord = true;
/*     */     }
/*     */     
/* 455 */     this.inventory.clear();
/* 456 */     for (InvItem iv : v.getInventoryKeys()) {
/* 457 */       this.inventory.put(iv, Integer.valueOf(v.countInv(iv)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(NBTTagCompound nbttagcompound, String label) {
/* 462 */     nbttagcompound.func_74772_a(label + "_lid", this.id);
/* 463 */     nbttagcompound.func_74768_a(label + "_nb", this.nb);
/* 464 */     nbttagcompound.func_74778_a(label + "_type", this.type);
/* 465 */     nbttagcompound.func_74778_a(label + "_firstName", this.firstName);
/* 466 */     nbttagcompound.func_74778_a(label + "_familyName", this.familyName);
/* 467 */     nbttagcompound.func_74778_a(label + "_propertype", this.nameKey);
/* 468 */     nbttagcompound.func_74778_a(label + "_occupation", this.occupation);
/* 469 */     if ((this.fathersName != null) && (this.fathersName.length() > 0)) {
/* 470 */       nbttagcompound.func_74778_a(label + "_fathersName", this.fathersName);
/*     */     }
/* 472 */     if ((this.mothersName != null) && (this.mothersName.length() > 0)) {
/* 473 */       nbttagcompound.func_74778_a(label + "_mothersName", this.mothersName);
/*     */     }
/* 475 */     if ((this.maidenName != null) && (this.maidenName.length() > 0)) {
/* 476 */       nbttagcompound.func_74778_a(label + "_maidenName", this.maidenName);
/*     */     }
/* 478 */     if ((this.spousesName != null) && (this.spousesName.length() > 0)) {
/* 479 */       nbttagcompound.func_74778_a(label + "_spousesName", this.spousesName);
/*     */     }
/* 481 */     nbttagcompound.func_74768_a(label + "_gender", this.gender);
/* 482 */     nbttagcompound.func_74778_a(label + "_texture", this.texture.func_110623_a());
/*     */     
/* 484 */     nbttagcompound.func_74757_a(label + "_killed", this.killed);
/* 485 */     nbttagcompound.func_74757_a(label + "_raidingVillage", this.raidingVillage);
/* 486 */     nbttagcompound.func_74757_a(label + "_awayraiding", this.awayraiding);
/* 487 */     nbttagcompound.func_74757_a(label + "_awayhired", this.awayhired);
/* 488 */     nbttagcompound.func_74772_a(label + "_raiderSpawn", this.raiderSpawn);
/*     */     
/* 490 */     if (this.housePos != null) {
/* 491 */       this.housePos.write(nbttagcompound, label + "_housePos");
/*     */     }
/* 493 */     if (this.townHallPos != null) {
/* 494 */       this.townHallPos.write(nbttagcompound, label + "_townHallPos");
/*     */     }
/* 496 */     if (this.originalVillagePos != null) {
/* 497 */       this.originalVillagePos.write(nbttagcompound, label + "_originalVillagePos");
/*     */     }
/* 499 */     nbttagcompound.func_74768_a(label + "_size", this.villagerSize);
/*     */     
/* 501 */     NBTTagList nbttaglist = new NBTTagList();
/* 502 */     for (String tag : this.questTags) {
/* 503 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 504 */       nbttagcompound1.func_74778_a("tag", tag);
/* 505 */       nbttaglist.func_74742_a(nbttagcompound1);
/*     */     }
/* 507 */     nbttagcompound.func_74782_a(label + "questTags", nbttaglist);
/*     */     
/* 509 */     nbttaglist = MillCommonUtilities.writeInventory(this.inventory);
/* 510 */     nbttagcompound.func_74782_a(label + "_inventoryNew", nbttaglist);
/*     */     
/* 512 */     nbttagcompound.func_74778_a(label + "_culture", this.culture.key);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\VillagerRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
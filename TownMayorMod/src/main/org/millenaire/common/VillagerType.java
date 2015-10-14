/*     */ package org.millenaire.common;
/*     */ 
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.Item;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.goal.Goal;
/*     */ import org.millenaire.common.item.Goods;
/*     */ import org.millenaire.common.network.StreamReadWrite;
/*     */ 
/*     */ public class VillagerType implements org.millenaire.common.core.MillCommonUtilities.WeightedChoice
/*     */ {
/*     */   private static final String TAG_LOCALMERCHANT = "localmerchant";
/*     */   private static final String TAG_FOREIGNMERCHANT = "foreignmerchant";
/*     */   private static final String TAG_CHILD = "child";
/*     */   private static final String TAG_RELIGIOUS = "religious";
/*     */   private static final String TAG_CHIEF = "chief";
/*     */   private static final String TAG_DRINKER = "heavydrinker";
/*     */   private static final String TAG_SELLER = "seller";
/*     */   private static final String TAG_MEDITATES = "meditates";
/*     */   private static final String TAG_SACRIFICES = "performssacrifices";
/*     */   private static final String TAG_VISITOR = "visitor";
/*     */   private static final String TAG_HELPSINATTACKS = "helpinattacks";
/*     */   private static final String TAG_GATHERSAPPLES = "gathersapples";
/*     */   private static final String TAG_HOSTILE = "hostile";
/*     */   private static final String TAG_NOLEAFCLEARING = "noleafclearing";
/*     */   private static final String TAG_ARCHER = "archer";
/*     */   private static final String TAG_RAIDER = "raider";
/*     */   private static final String TAG_NOTELEPORT = "noteleport";
/*     */   private static final String TAG_HIDENAME = "hidename";
/*     */   private static final String TAG_SHOWHEALTH = "showhealth";
/*     */   private static final String TAG_DEFENSIVE = "defensive";
/*     */   private static final String TAG_NORESURRECT = "noresurrect";
/*     */   public Culture culture;
/*     */   public String name;
/*     */   public String key;
/*     */   public String altname;
/*     */   public String altkey;
/*     */   
/*     */   public static VillagerType loadVillagerType(File file, Culture c)
/*     */   {
/*  48 */     VillagerType v = new VillagerType(c, file.getName().split("\\.")[0]);
/*     */     try
/*     */     {
/*  51 */       BufferedReader reader = MillCommonUtilities.getReader(file);
/*     */       
/*  53 */       List<Goal> goals = new ArrayList();
/*  54 */       List<String> textures = new ArrayList();
/*  55 */       List<InvItem> toolsNeeded = new ArrayList();
/*  56 */       List<InvItem> bringBackHomeGoods = new ArrayList();
/*  57 */       List<InvItem> collectGoods = new ArrayList();
/*     */       
/*     */       String line;
/*     */       
/*  61 */       while ((line = reader.readLine()) != null) {
/*  62 */         if ((line.trim().length() > 0) && (!line.startsWith("//"))) {
/*  63 */           String[] temp = line.split("=");
/*  64 */           if (temp.length != 2) {
/*  65 */             MLN.error(null, "Invalid line when loading villager type " + file.getName() + ": " + line);
/*     */           }
/*     */           else {
/*  68 */             String key = temp[0].toLowerCase();
/*  69 */             String value = temp[1].trim();
/*  70 */             if (key.equals("native_name")) {
/*  71 */               v.name = value;
/*  72 */             } else if (key.equals("alt_native_name")) {
/*  73 */               v.altname = value;
/*  74 */             } else if (key.equals("alt_key")) {
/*  75 */               v.altkey = value;
/*  76 */             } else if (key.equals("model")) {
/*  77 */               v.model = value.toLowerCase();
/*  78 */             } else if (key.equals("goal")) {
/*  79 */               if (Goal.goals.containsKey(value.toLowerCase())) {
/*  80 */                 goals.add(Goal.goals.get(value.toLowerCase()));
/*     */               } else {
/*  82 */                 MLN.error(null, "Unknown goal found when loading villager type " + file.getName() + ": " + value + " amoung " + Goal.goals.size());
/*     */               }
/*  84 */             } else if (key.equals("texture")) {
/*  85 */               textures.add(value);
/*  86 */             } else if (key.equals("requiredgood"))
/*     */             {
/*  88 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/*  89 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/*  90 */                 v.requiredGoods.put(iv, Integer.valueOf(Integer.parseInt(value.split(",")[1])));
/*  91 */                 v.requiredFoodAndGoods.put(iv, Integer.valueOf(Integer.parseInt(value.split(",")[1])));
/*     */               } else {
/*  93 */                 MLN.error(null, "Unknown required good found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/*  95 */             } else if (key.equals("requiredfood")) {
/*  96 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/*  97 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/*  98 */                 v.requiredFoodAndGoods.put(iv, Integer.valueOf(Integer.parseInt(value.split(",")[1])));
/*     */               } else {
/* 100 */                 MLN.error(null, "Unknown required good found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 102 */             } else if (key.equals("startinginv")) {
/* 103 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/* 104 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/* 105 */                 v.startingInv.put(iv, Integer.valueOf(Integer.parseInt(value.split(",")[1])));
/*     */               } else {
/* 107 */                 MLN.error(null, "Unknown starting inv found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 109 */             } else if (key.equals("merchantstock")) {
/* 110 */               if (Goods.goodsName.containsKey(value.split(",")[0].toLowerCase())) {
/* 111 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.split(",")[0].toLowerCase());
/* 112 */                 v.foreignMerchantStock.put(iv, Integer.valueOf(Integer.parseInt(value.split(",")[1])));
/*     */               } else {
/* 114 */                 MLN.error(null, "Unknown merchantstock found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 116 */             } else if (key.equals("toolneeded")) {
/* 117 */               if (Goods.goodsName.containsKey(value.toLowerCase())) {
/* 118 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.toLowerCase());
/* 119 */                 toolsNeeded.add(iv);
/*     */               } else {
/* 121 */                 MLN.error(null, "Unknown tool needed found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 123 */             } else if (key.equals("toolneededclass")) {
/* 124 */               if (value.equalsIgnoreCase("meleeweapons")) {
/* 125 */                 for (Item item : MillVillager.weaponsSwords) {
/* 126 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 128 */               } else if (value.equalsIgnoreCase("rangedweapons")) {
/* 129 */                 for (Item item : MillVillager.weaponsRanged) {
/* 130 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 132 */               } else if (value.equalsIgnoreCase("armour")) {
/* 133 */                 for (Item item : MillVillager.helmets) {
/* 134 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 136 */                 for (Item item : MillVillager.chestplates) {
/* 137 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 139 */                 for (Item item : MillVillager.legs) {
/* 140 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 142 */                 for (Item item : MillVillager.boots) {
/* 143 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 145 */               } else if (value.equalsIgnoreCase("pickaxes")) {
/* 146 */                 for (Item item : MillVillager.pickaxes) {
/* 147 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 149 */               } else if (value.equalsIgnoreCase("axes")) {
/* 150 */                 for (Item item : MillVillager.axes) {
/* 151 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 153 */               } else if (value.equalsIgnoreCase("shovels")) {
/* 154 */                 for (Item item : MillVillager.shovels) {
/* 155 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/* 157 */               } else if (value.equalsIgnoreCase("hoes")) {
/* 158 */                 for (Item item : MillVillager.hoes) {
/* 159 */                   toolsNeeded.add(new InvItem(item, 0));
/*     */                 }
/*     */               } else {
/* 162 */                 MLN.error(null, "Unknown tool class found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 164 */             } else if (key.equals("defaultweapon")) {
/* 165 */               if (Goods.goodsName.containsKey(value.toLowerCase())) {
/* 166 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.toLowerCase());
/* 167 */                 v.startingWeapon = iv;
/*     */               } else {
/* 169 */                 MLN.error(null, "Unknown default weapon found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 171 */             } else if (key.equals("bringbackhomegood")) {
/* 172 */               if (Goods.goodsName.containsKey(value.toLowerCase())) {
/* 173 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.toLowerCase());
/* 174 */                 bringBackHomeGoods.add(iv);
/*     */               } else {
/* 176 */                 MLN.error(null, "Unknown bring back home good found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 178 */             } else if (key.equals("collectgood")) {
/* 179 */               if (Goods.goodsName.containsKey(value.toLowerCase())) {
/* 180 */                 InvItem iv = (InvItem)Goods.goodsName.get(value.toLowerCase());
/* 181 */                 collectGoods.add(iv);
/*     */               } else {
/* 183 */                 MLN.error(null, "Unknown collect good found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 185 */             } else if (key.equals("gender")) {
/* 186 */               if (value.equals("male")) {
/* 187 */                 v.gender = 1;
/* 188 */               } else if (value.equals("female")) {
/* 189 */                 v.gender = 2;
/*     */               } else {
/* 191 */                 MLN.error(null, "Unknown gender found when loading villager type " + file.getName() + ": " + value);
/*     */               }
/* 193 */             } else if (key.equals("baseattackstrength")) {
/* 194 */               v.baseAttackStrength = Integer.parseInt(value);
/* 195 */             } else if (key.equals("experiencegiven")) {
/* 196 */               v.expgiven = Integer.parseInt(value);
/* 197 */             } else if (key.equals("familynamelist")) {
/* 198 */               v.familyNameList = value;
/* 199 */             } else if (key.equals("firstnamelist")) {
/* 200 */               v.firstNameList = value;
/* 201 */             } else if (key.equals("malechild")) {
/* 202 */               v.maleChild = value;
/* 203 */             } else if (key.equals("femalechild")) {
/* 204 */               v.femaleChild = value;
/* 205 */             } else if (key.equals("tag")) {
/* 206 */               v.tags.add(value.toLowerCase());
/* 207 */             } else if (key.equals("baseheight")) {
/* 208 */               v.baseScale = Float.parseFloat(value);
/* 209 */             } else if (key.equals("health")) {
/* 210 */               v.health = Integer.parseInt(value);
/* 211 */             } else if (key.equals("hiringcost")) {
/* 212 */               v.hireCost = Integer.parseInt(value);
/* 213 */             } else if (key.equals("chanceweight")) {
/* 214 */               v.chanceWeight = Integer.parseInt(value);
/* 215 */             } else if (key.equals("clothes")) {
/* 216 */               if (value.split(",").length < 2) {
/* 217 */                 MLN.error(null, "Two values are required for all clothes tag (cloth name, then texture file).");
/*     */               } else {
/* 219 */                 String clothname = value.split(",")[0];
/* 220 */                 String textpath = value.split(",")[1];
/*     */                 
/* 222 */                 if (!v.clothes.containsKey(clothname)) {
/* 223 */                   v.clothes.put(clothname, new ArrayList());
/*     */                 }
/*     */                 
/* 226 */                 ((List)v.clothes.get(clothname)).add(textpath);
/*     */               }
/*     */             }
/*     */             else {
/* 230 */               MLN.error(null, "Could not understand parameter when loading villager type " + file.getName() + ": " + line);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 235 */       reader.close();
/*     */       
/* 237 */       v.isChild = v.tags.contains("child");
/* 238 */       v.isChief = v.tags.contains("chief");
/* 239 */       v.isHeavyDrinker = v.tags.contains("heavydrinker");
/* 240 */       v.isReligious = v.tags.contains("religious");
/* 241 */       v.canSell = v.tags.contains("seller");
/* 242 */       v.canMeditate = v.tags.contains("meditates");
/* 243 */       v.canPerformSacrifices = v.tags.contains("performssacrifices");
/* 244 */       v.visitor = v.tags.contains("visitor");
/* 245 */       v.helpInAttacks = v.tags.contains("helpinattacks");
/* 246 */       v.isLocalMerchant = v.tags.contains("localmerchant");
/* 247 */       v.isForeignMerchant = v.tags.contains("foreignmerchant");
/* 248 */       v.gathersApples = v.tags.contains("gathersapples");
/* 249 */       v.hostile = v.tags.contains("hostile");
/* 250 */       v.noleafclearing = v.tags.contains("noleafclearing");
/* 251 */       v.isArcher = v.tags.contains("archer");
/* 252 */       v.isRaider = v.tags.contains("raider");
/* 253 */       v.noTeleport = v.tags.contains("noteleport");
/* 254 */       v.hideName = v.tags.contains("hidename");
/* 255 */       v.showHealth = v.tags.contains("showhealth");
/* 256 */       v.isDefensive = v.tags.contains("defensive");
/* 257 */       v.noResurrect = v.tags.contains("noresurrect");
/*     */       
/* 259 */       v.textures = ((String[])textures.toArray(new String[0]));
/* 260 */       v.toolsNeeded = ((InvItem[])toolsNeeded.toArray(new InvItem[0]));
/* 261 */       v.bringBackHomeGoods = ((InvItem[])bringBackHomeGoods.toArray(new InvItem[0]));
/* 262 */       v.collectGoods = ((InvItem[])collectGoods.toArray(new InvItem[0]));
/*     */       
/* 264 */       goals.add(Goal.sleep);
/*     */       
/* 266 */       if (v.toolsNeeded.length > 0) {
/* 267 */         boolean foundToolFetchingGoal = false;
/*     */         
/* 269 */         for (Goal g : goals) {
/* 270 */           if (g == Goal.gettool) {
/* 271 */             foundToolFetchingGoal = true;
/*     */           }
/*     */         }
/*     */         
/* 275 */         if (!foundToolFetchingGoal) {
/* 276 */           goals.add(Goal.gettool);
/*     */         }
/*     */       }
/*     */       
/* 280 */       v.goals = ((Goal[])goals.toArray(new Goal[0]));
/*     */       
/* 282 */       if (v.health == -1) {
/* 283 */         if (v.helpInAttacks) {
/* 284 */           v.health = 40;
/*     */         } else {
/* 286 */           v.health = 30;
/*     */         }
/*     */       }
/*     */       
/* 290 */       if (v.baseAttackStrength == -1) {
/* 291 */         if (v.helpInAttacks) {
/* 292 */           v.baseAttackStrength = 2;
/*     */         } else {
/* 294 */           v.baseAttackStrength = 1;
/*     */         }
/*     */       }
/*     */       
/* 298 */       for (InvItem item : v.foreignMerchantStock.keySet())
/*     */       {
/* 300 */         if (!c.goodsByItem.containsKey(item)) {
/* 301 */           MLN.warning(v, "Starting inv of foreign merchant countains non-tradeable good: " + item);
/* 302 */         } else if (((Goods)c.goodsByItem.get(item)).foreignMerchantPrice < 1) {
/* 303 */           MLN.warning(v, "Starting inv of foreign merchant countains good with null tradeable price: " + item);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 308 */       if (MLN.LogVillager >= 1) {
/* 309 */         MLN.major(v, "Loaded villager type: " + v.key + " " + v.helpInAttacks);
/*     */       }
/*     */       
/* 312 */       return v;
/*     */     }
/*     */     catch (Exception e) {
/* 315 */       MLN.printException(e);
/*     */     }
/* 317 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 325 */   public String model = null;
/* 326 */   public int baseAttackStrength = -1;
/* 327 */   public int health = -1;
/* 328 */   public boolean isChild = false;
/* 329 */   public boolean isReligious = false;
/* 330 */   public boolean isChief = false;
/* 331 */   public boolean isHeavyDrinker = false;
/* 332 */   public boolean canSell = false;
/* 333 */   public boolean canMeditate = false;
/* 334 */   public boolean canPerformSacrifices = false;
/* 335 */   public boolean visitor = false;
/* 336 */   public boolean helpInAttacks = false;
/* 337 */   public boolean isLocalMerchant = false;
/* 338 */   public boolean isForeignMerchant = false;
/* 339 */   public boolean gathersApples = false;
/* 340 */   public boolean hostile = false;
/* 341 */   public boolean noleafclearing = false;
/* 342 */   public boolean isArcher = false;
/* 343 */   public boolean isRaider = false;
/* 344 */   public boolean noTeleport = false;
/* 345 */   public boolean hideName = false;
/* 346 */   public boolean showHealth = false;
/* 347 */   public boolean isDefensive = false;
/* 348 */   public boolean noResurrect = false;
/* 349 */   public float baseScale = 1.0F;
/*     */   public String familyNameList;
/*     */   public String firstNameList;
/* 352 */   public int chanceWeight = 0;
/*     */   
/* 354 */   public int expgiven = 0;
/*     */   public Goal[] goals;
/*     */   public String[] textures;
/* 357 */   public HashMap<InvItem, Integer> requiredGoods = new HashMap();
/* 358 */   public HashMap<InvItem, Integer> requiredFoodAndGoods = new HashMap();
/* 359 */   public HashMap<InvItem, Integer> startingInv = new HashMap();
/* 360 */   public HashMap<InvItem, Integer> foreignMerchantStock = new HashMap();
/* 361 */   public HashMap<String, List<String>> clothes = new HashMap();
/*     */   
/*     */   public InvItem[] bringBackHomeGoods;
/*     */   
/*     */   public InvItem[] collectGoods;
/*     */   public InvItem startingWeapon;
/* 367 */   private final List<String> tags = new ArrayList();
/*     */   
/*     */   public InvItem[] toolsNeeded;
/*     */   public int gender;
/* 371 */   public String maleChild = null;
/* 372 */   public String femaleChild = null;
/*     */   public int hireCost;
/*     */   
/*     */   public VillagerType(Culture c, String key)
/*     */   {
/* 377 */     this.culture = c;
/* 378 */     this.key = key;
/*     */   }
/*     */   
/*     */   public int getChoiceWeight(net.minecraft.entity.player.EntityPlayer player)
/*     */   {
/* 383 */     return this.chanceWeight;
/*     */   }
/*     */   
/*     */   public String getEntityName() {
/* 387 */     if ("femaleasymmetrical".equals(this.model)) {
/* 388 */       return "ml_GenericAsimmFemale";
/*     */     }
/* 390 */     if ("femalesymmetrical".equals(this.model)) {
/* 391 */       return "ml_GenericSimmFemale";
/*     */     }
/* 393 */     if ("zombie".equals(this.model)) {
/* 394 */       return "ml_GenericZombie";
/*     */     }
/*     */     
/* 397 */     return "ml_GenericVillager";
/*     */   }
/*     */   
/*     */   public String getRandomClothTexture(String clothType) {
/* 401 */     if (this.clothes.containsKey(clothType)) {
/* 402 */       return (String)((List)this.clothes.get(clothType)).get(MillCommonUtilities.randomInt(((List)this.clothes.get(clothType)).size()));
/*     */     }
/* 404 */     return null;
/*     */   }
/*     */   
/*     */   public String getRandomFamilyName() {
/* 408 */     return this.culture.getRandomNameFromList(this.familyNameList);
/*     */   }
/*     */   
/*     */   public String getTexture() {
/* 412 */     return this.textures[MillCommonUtilities.randomInt(this.textures.length)];
/*     */   }
/*     */   
/*     */   public boolean isClothValid(String clothType, String texture)
/*     */   {
/* 417 */     if (!this.clothes.containsKey(clothType)) {
/* 418 */       return false;
/*     */     }
/*     */     
/* 421 */     for (String s : (List)this.clothes.get(clothType)) {
/* 422 */       if (s.equals(texture)) {
/* 423 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 427 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTextureValid(String texture) {
/* 431 */     for (String s : this.textures) {
/* 432 */       if (s.equals(texture)) {
/* 433 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 437 */     return false;
/*     */   }
/*     */   
/*     */   public void readVillagerTypeInfoPacket(ByteBufInputStream ds) throws IOException {
/* 441 */     this.name = StreamReadWrite.readNullableString(ds);
/* 442 */     this.altkey = StreamReadWrite.readNullableString(ds);
/* 443 */     this.altname = StreamReadWrite.readNullableString(ds);
/* 444 */     this.model = StreamReadWrite.readNullableString(ds);
/* 445 */     this.gender = ds.read();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 450 */     return "VT: " + this.culture.key + "/" + this.key;
/*     */   }
/*     */   
/*     */   public void writeVillagerTypeInfo(DataOutput data) throws IOException {
/* 454 */     data.writeUTF(this.key);
/* 455 */     StreamReadWrite.writeNullableString(this.name, data);
/* 456 */     StreamReadWrite.writeNullableString(this.altkey, data);
/* 457 */     StreamReadWrite.writeNullableString(this.altname, data);
/* 458 */     StreamReadWrite.writeNullableString(this.model, data);
/* 459 */     data.write(this.gender);
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\VillagerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
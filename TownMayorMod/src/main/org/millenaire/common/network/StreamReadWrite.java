/*     */ package org.millenaire.common.network;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTSizeTracker;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.millenaire.common.Culture;
/*     */ import org.millenaire.common.InvItem;
/*     */ import org.millenaire.common.MLN;
/*     */ import org.millenaire.common.MLN.MillenaireException;
/*     */ import org.millenaire.common.MillWorld;
/*     */ import org.millenaire.common.Point;
/*     */ import org.millenaire.common.PujaSacrifice;
/*     */ import org.millenaire.common.PujaSacrifice.PrayerTarget;
/*     */ import org.millenaire.common.Quest;
/*     */ import org.millenaire.common.Quest.QuestInstance;
/*     */ import org.millenaire.common.Quest.QuestInstanceVillager;
/*     */ import org.millenaire.common.UserProfile;
/*     */ import org.millenaire.common.VillagerRecord;
/*     */ import org.millenaire.common.building.Building;
/*     */ import org.millenaire.common.building.BuildingLocation;
/*     */ import org.millenaire.common.building.BuildingPlan;
/*     */ import org.millenaire.common.building.BuildingProject;
/*     */ import org.millenaire.common.building.BuildingProject.EnumProjects;
/*     */ import org.millenaire.common.core.MillCommonUtilities;
/*     */ import org.millenaire.common.item.Goods;
/*     */ 
/*     */ public class StreamReadWrite
/*     */ {
/*     */   public static Random random;
/*     */   
/*     */   public static BuildingPlan readBuildingPlanInfo(DataInput ds, Culture culture)
/*     */     throws IOException
/*     */   {
/*  46 */     String key = ds.readUTF();
/*  47 */     int level = ds.readInt();
/*  48 */     int variation = ds.readInt();
/*     */     
/*  50 */     BuildingPlan plan = new BuildingPlan(key, level, variation, culture);
/*     */     
/*  52 */     plan.planName = readNullableString(ds);
/*  53 */     plan.nativeName = readNullableString(ds);
/*  54 */     plan.requiredTag = readNullableString(ds);
/*  55 */     plan.shop = readNullableString(ds);
/*  56 */     plan.type = readNullableString(ds);
/*  57 */     plan.price = ds.readInt();
/*  58 */     plan.reputation = ds.readInt();
/*  59 */     plan.maleResident = readStringList(ds);
/*  60 */     plan.femaleResident = readStringList(ds);
/*  61 */     plan.startingSubBuildings = readStringList(ds);
/*  62 */     plan.subBuildings = readStringList(ds);
/*  63 */     plan.tags = readStringList(ds);
/*     */     
/*  65 */     return plan;
/*     */   }
/*     */   
/*     */   public static HashMap<InvItem, Integer> readInventory(DataInput ds) throws IOException
/*     */   {
/*  70 */     HashMap<InvItem, Integer> inv = new HashMap();
/*     */     
/*  72 */     int nb = ds.readInt();
/*     */     
/*  74 */     for (int i = 0; i < nb; i++) {
/*     */       try
/*     */       {
/*  77 */         InvItem item = new InvItem(Item.func_150899_d(ds.readInt()), ds.readInt());
/*  78 */         inv.put(item, Integer.valueOf(ds.readInt()));
/*     */       } catch (MLN.MillenaireException e) {
/*  80 */         MLN.printException(e);
/*     */       }
/*     */     }
/*     */     
/*  84 */     return inv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ItemStack readItemStack(DataInput par1DataInput)
/*     */     throws IOException
/*     */   {
/*  93 */     ItemStack is = null;
/*  94 */     int id = par1DataInput.readInt();
/*     */     
/*  96 */     if (id >= 0) {
/*  97 */       byte nb = par1DataInput.readByte();
/*  98 */       short meta = par1DataInput.readShort();
/*  99 */       is = new ItemStack(Item.func_150899_d(id), nb, meta);
/*     */       
/* 101 */       if (is.func_77973_b().func_77645_m()) {
/* 102 */         is.field_77990_d = readNBTTagCompound(par1DataInput);
/*     */       }
/*     */     }
/*     */     
/* 106 */     return is;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static NBTTagCompound readNBTTagCompound(DataInput par1DataInput)
/*     */     throws IOException
/*     */   {
/* 115 */     short var2 = par1DataInput.readShort();
/*     */     
/* 117 */     if (var2 < 0) {
/* 118 */       return null;
/*     */     }
/* 120 */     byte[] var3 = new byte[var2];
/* 121 */     par1DataInput.readFully(var3);
/* 122 */     return CompressedStreamTools.func_152457_a(var3, new NBTSizeTracker(2097152L));
/*     */   }
/*     */   
/*     */   public static BuildingLocation readNullableBuildingLocation(DataInput ds)
/*     */     throws IOException
/*     */   {
/* 128 */     boolean isnull = ds.readBoolean();
/*     */     
/* 130 */     if (isnull) {
/* 131 */       return null;
/*     */     }
/*     */     
/* 134 */     BuildingLocation bl = new BuildingLocation();
/* 135 */     bl.isCustomBuilding = ds.readBoolean();
/* 136 */     bl.planKey = readNullableString(ds);
/* 137 */     bl.shop = readNullableString(ds);
/*     */     
/* 139 */     bl.maleResident = readStringList(ds);
/* 140 */     bl.femaleResident = readStringList(ds);
/*     */     
/* 142 */     bl.minx = ds.readInt();
/* 143 */     bl.maxx = ds.readInt();
/* 144 */     bl.miny = ds.readInt();
/* 145 */     bl.maxy = ds.readInt();
/* 146 */     bl.minz = ds.readInt();
/* 147 */     bl.maxz = ds.readInt();
/*     */     
/* 149 */     bl.minxMargin = ds.readInt();
/* 150 */     bl.maxxMargin = ds.readInt();
/* 151 */     bl.minyMargin = ds.readInt();
/* 152 */     bl.maxyMargin = ds.readInt();
/* 153 */     bl.minzMargin = ds.readInt();
/* 154 */     bl.maxzMargin = ds.readInt();
/*     */     
/* 156 */     bl.orientation = ds.readInt();
/* 157 */     bl.length = ds.readInt();
/* 158 */     bl.width = ds.readInt();
/* 159 */     bl.level = ds.readInt();
/* 160 */     bl.setVariation(ds.readInt());
/* 161 */     bl.reputation = ds.readInt();
/* 162 */     bl.price = ds.readInt();
/*     */     
/* 164 */     bl.pos = readNullablePoint(ds);
/* 165 */     bl.chestPos = readNullablePoint(ds);
/* 166 */     bl.sleepingPos = readNullablePoint(ds);
/* 167 */     bl.sellingPos = readNullablePoint(ds);
/* 168 */     bl.craftingPos = readNullablePoint(ds);
/* 169 */     bl.shelterPos = readNullablePoint(ds);
/* 170 */     bl.defendingPos = readNullablePoint(ds);
/*     */     
/* 172 */     String cultureKey = readNullableString(ds);
/*     */     
/* 174 */     bl.culture = Culture.getCultureByName(cultureKey);
/*     */     
/* 176 */     bl.tags = readStringList(ds);
/* 177 */     bl.subBuildings = readStringList(ds);
/*     */     
/* 179 */     bl.showTownHallSigns = ds.readBoolean();
/* 180 */     bl.upgradesAllowed = ds.readBoolean();
/* 181 */     bl.bedrocklevel = ds.readBoolean();
/*     */     
/* 183 */     return bl;
/*     */   }
/*     */   
/*     */   public static BuildingProject readNullableBuildingProject(DataInput ds, Culture culture) throws IOException
/*     */   {
/* 188 */     boolean isnull = ds.readBoolean();
/*     */     
/* 190 */     if (isnull) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     BuildingProject bp = new BuildingProject();
/* 195 */     bp.isCustomBuilding = ds.readBoolean();
/* 196 */     bp.key = readNullableString(ds);
/* 197 */     bp.location = readNullableBuildingLocation(ds);
/* 198 */     if (culture != null) {
/* 199 */       if (bp.isCustomBuilding) {
/* 200 */         bp.customBuildingPlan = culture.getBuildingCustom(bp.key);
/*     */       } else {
/* 202 */         bp.planSet = culture.getBuildingPlanSet(bp.key);
/*     */       }
/*     */     }
/*     */     
/* 206 */     return bp;
/*     */   }
/*     */   
/*     */   public static Goods readNullableGoods(DataInput ds) throws IOException, MLN.MillenaireException
/*     */   {
/* 211 */     boolean isnull = ds.readBoolean();
/*     */     
/* 213 */     if (isnull) {
/* 214 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 218 */     InvItem iv = new InvItem(MillCommonUtilities.getItemById(ds.readInt()), ds.readByte());
/* 219 */     Goods g = new Goods(iv);
/* 220 */     g.requiredTag = readNullableString(ds);
/* 221 */     g.desc = readNullableString(ds);
/* 222 */     g.autoGenerate = ds.readBoolean();
/* 223 */     g.minReputation = ds.readInt();
/*     */     
/* 225 */     return g;
/*     */   }
/*     */   
/*     */   public static ItemStack readNullableItemStack(DataInput ds) throws IOException
/*     */   {
/* 230 */     boolean isnull = ds.readBoolean();
/*     */     
/* 232 */     if (isnull) {
/* 233 */       return null;
/*     */     }
/*     */     
/* 236 */     return readItemStack(ds);
/*     */   }
/*     */   
/*     */   public static Point readNullablePoint(DataInput ds) throws IOException
/*     */   {
/* 241 */     boolean isnull = ds.readBoolean();
/*     */     
/* 243 */     if (isnull) {
/* 244 */       return null;
/*     */     }
/*     */     
/* 247 */     int x = ds.readInt();
/* 248 */     int y = ds.readInt();
/* 249 */     int z = ds.readInt();
/*     */     
/* 251 */     return new Point(x, y, z);
/*     */   }
/*     */   
/*     */   public static Quest.QuestInstance readNullableQuestInstance(MillWorld mw, DataInput ds) throws IOException {
/* 255 */     boolean isnull = ds.readBoolean();
/*     */     
/* 257 */     if (isnull) {
/* 258 */       return null;
/*     */     }
/*     */     
/* 261 */     long id = ds.readLong();
/*     */     
/* 263 */     String questKey = ds.readUTF();
/*     */     
/* 265 */     if (!Quest.quests.containsKey(questKey)) {
/* 266 */       return null;
/*     */     }
/*     */     
/* 269 */     Quest quest = (Quest)Quest.quests.get(questKey);
/* 270 */     UserProfile profile = mw.getProfile(ds.readUTF());
/*     */     
/* 272 */     int currentStep = ds.readUnsignedByte();
/* 273 */     long startTime = ds.readLong();
/* 274 */     long currentStepStart = ds.readLong();
/*     */     
/* 276 */     HashMap<String, Quest.QuestInstanceVillager> villagers = new HashMap();
/*     */     
/* 278 */     int nb = ds.readUnsignedByte();
/*     */     
/* 280 */     for (int i = 0; i < nb; i++) {
/* 281 */       String key = ds.readUTF();
/* 282 */       villagers.put(key, readNullableQuestVillager(mw, ds));
/*     */     }
/*     */     
/* 285 */     Quest.QuestInstance qi = new Quest.QuestInstance(mw, quest, profile, villagers, startTime, currentStep, currentStepStart);
/*     */     
/* 287 */     qi.uniqueid = id;
/*     */     
/* 289 */     return qi;
/*     */   }
/*     */   
/*     */   public static Quest.QuestInstanceVillager readNullableQuestVillager(MillWorld mw, DataInput ds) throws IOException {
/* 293 */     boolean isnull = ds.readBoolean();
/*     */     
/* 295 */     if (isnull) {
/* 296 */       return null;
/*     */     }
/*     */     
/* 299 */     return new Quest.QuestInstanceVillager(mw, readNullablePoint(ds), ds.readLong());
/*     */   }
/*     */   
/*     */   public static ResourceLocation readNullableResourceLocation(DataInput ds) throws IOException
/*     */   {
/* 304 */     boolean isnull = ds.readBoolean();
/*     */     
/* 306 */     if (isnull) {
/* 307 */       return null;
/*     */     }
/*     */     
/* 310 */     return new ResourceLocation("millenaire", ds.readUTF());
/*     */   }
/*     */   
/*     */   public static String readNullableString(DataInput ds) throws IOException
/*     */   {
/* 315 */     boolean isnull = ds.readBoolean();
/*     */     
/* 317 */     if (isnull) {
/* 318 */       return null;
/*     */     }
/*     */     
/* 321 */     return ds.readUTF();
/*     */   }
/*     */   
/*     */   public static VillagerRecord readNullableVillagerRecord(MillWorld mw, DataInput ds) throws IOException
/*     */   {
/* 326 */     boolean isnull = ds.readBoolean();
/*     */     
/* 328 */     if (isnull) {
/* 329 */       return null;
/*     */     }
/*     */     
/* 332 */     VillagerRecord vr = new VillagerRecord(mw);
/*     */     
/* 334 */     vr.id = ds.readLong();
/* 335 */     vr.type = readNullableString(ds);
/* 336 */     vr.firstName = readNullableString(ds);
/* 337 */     vr.familyName = readNullableString(ds);
/* 338 */     vr.nameKey = readNullableString(ds);
/* 339 */     vr.occupation = readNullableString(ds);
/* 340 */     vr.texture = readNullableResourceLocation(ds);
/*     */     
/* 342 */     vr.nb = ds.readInt();
/* 343 */     vr.gender = ds.readInt();
/* 344 */     vr.villagerSize = ds.readInt();
/*     */     
/* 346 */     vr.culture = Culture.getCultureByName(readNullableString(ds));
/*     */     
/* 348 */     vr.fathersName = readNullableString(ds);
/* 349 */     vr.mothersName = readNullableString(ds);
/* 350 */     vr.spousesName = readNullableString(ds);
/* 351 */     vr.maidenName = readNullableString(ds);
/*     */     
/* 353 */     vr.killed = ds.readBoolean();
/* 354 */     vr.raidingVillage = ds.readBoolean();
/* 355 */     vr.awayraiding = ds.readBoolean();
/* 356 */     vr.awayhired = ds.readBoolean();
/*     */     
/* 358 */     vr.housePos = readNullablePoint(ds);
/* 359 */     vr.townHallPos = readNullablePoint(ds);
/* 360 */     vr.originalVillagePos = readNullablePoint(ds);
/*     */     
/* 362 */     vr.raiderSpawn = ds.readLong();
/*     */     
/* 364 */     vr.inventory = readInventory(ds);
/* 365 */     vr.questTags = readStringList(ds);
/*     */     
/* 367 */     return vr;
/*     */   }
/*     */   
/*     */   public static PujaSacrifice readOrUpdateNullablePuja(DataInput ds, Building b, PujaSacrifice puja) throws IOException
/*     */   {
/* 372 */     boolean isnull = ds.readBoolean();
/*     */     
/* 374 */     if (isnull) {
/* 375 */       return null;
/*     */     }
/*     */     
/* 378 */     short type = ds.readShort();
/*     */     
/* 380 */     if (puja == null) {
/* 381 */       puja = new PujaSacrifice(b, type);
/*     */     }
/*     */     
/* 384 */     int enchantmentId = ds.readShort();
/*     */     
/* 386 */     for (int i = 0; i < puja.getTargets().size(); i++) {
/* 387 */       if (((PujaSacrifice.PrayerTarget)puja.getTargets().get(i)).enchantment.field_77352_x == enchantmentId) {
/* 388 */         puja.currentTarget = ((PujaSacrifice.PrayerTarget)puja.getTargets().get(i));
/*     */       }
/*     */     }
/*     */     
/* 392 */     puja.pujaProgress = ds.readShort();
/* 393 */     puja.offeringNeeded = ds.readShort();
/* 394 */     puja.offeringProgress = ds.readShort();
/*     */     
/* 396 */     return puja;
/*     */   }
/*     */   
/*     */   public static HashMap<Point, Integer> readPointIntegerMap(DataInput ds) throws IOException
/*     */   {
/* 401 */     HashMap<Point, Integer> map = new HashMap();
/*     */     
/* 403 */     int nb = ds.readInt();
/*     */     
/* 405 */     for (int i = 0; i < nb; i++) {
/* 406 */       Point p = readNullablePoint(ds);
/* 407 */       map.put(p, Integer.valueOf(ds.readInt()));
/*     */     }
/*     */     
/* 410 */     return map;
/*     */   }
/*     */   
/*     */   public static List<Point> readPointList(DataInput ds) throws IOException
/*     */   {
/* 415 */     List<Point> v = new ArrayList();
/*     */     
/* 417 */     int nb = ds.readInt();
/*     */     
/* 419 */     for (int i = 0; i < nb; i++) {
/* 420 */       v.add(readNullablePoint(ds));
/*     */     }
/*     */     
/* 423 */     return v;
/*     */   }
/*     */   
/*     */   public static Map<BuildingProject.EnumProjects, List<BuildingProject>> readProjectListList(DataInput ds, Culture culture) throws IOException
/*     */   {
/* 428 */     Map<BuildingProject.EnumProjects, List<BuildingProject>> v = new HashMap();
/*     */     
/* 430 */     int nb = ds.readInt();
/*     */     
/* 432 */     for (int i = 0; i < nb; i++) {
/* 433 */       int nb2 = ds.readInt();
/* 434 */       List<BuildingProject> v2 = new ArrayList();
/* 435 */       for (int j = 0; j < nb2; j++) {
/* 436 */         v2.add(readNullableBuildingProject(ds, culture));
/*     */       }
/* 438 */       v.put(BuildingProject.EnumProjects.getById(i), v2);
/*     */     }
/*     */     
/* 441 */     return v;
/*     */   }
/*     */   
/*     */   public static List<String> readStringList(DataInput ds) throws IOException
/*     */   {
/* 446 */     List<String> v = new ArrayList();
/*     */     
/* 448 */     int nb = ds.readInt();
/*     */     
/* 450 */     for (int i = 0; i < nb; i++) {
/* 451 */       v.add(readNullableString(ds));
/*     */     }
/*     */     
/* 454 */     return v;
/*     */   }
/*     */   
/*     */   public static String[][] readStringStringArray(DataInput ds) throws IOException
/*     */   {
/* 459 */     String[][] strings = new String[ds.readInt()][];
/*     */     
/* 461 */     for (int i = 0; i < strings.length; i++) {
/* 462 */       String[] array = new String[ds.readInt()];
/* 463 */       for (int j = 0; j < array.length; j++) {
/* 464 */         array[j] = readNullableString(ds);
/*     */       }
/* 466 */       strings[i] = array;
/*     */     }
/*     */     
/* 469 */     return strings;
/*     */   }
/*     */   
/*     */   public static HashMap<String, List<String>> readStringStringListMap(DataInput ds) throws IOException
/*     */   {
/* 474 */     HashMap<String, List<String>> v = new HashMap();
/*     */     
/* 476 */     int nb = ds.readInt();
/*     */     
/* 478 */     for (int i = 0; i < nb; i++) {
/* 479 */       String key = ds.readUTF();
/* 480 */       v.put(key, readStringList(ds));
/*     */     }
/*     */     
/* 483 */     return v;
/*     */   }
/*     */   
/*     */   public static HashMap<String, String> readStringStringMap(DataInput ds) throws IOException
/*     */   {
/* 488 */     HashMap<String, String> v = new HashMap();
/*     */     
/* 490 */     int nb = ds.readInt();
/*     */     
/* 492 */     for (int i = 0; i < nb; i++) {
/* 493 */       String key = ds.readUTF();
/* 494 */       v.put(key, readNullableString(ds));
/*     */     }
/*     */     
/* 497 */     return v;
/*     */   }
/*     */   
/*     */   public static List<VillagerRecord> readVillagerRecordList(MillWorld mw, DataInput ds) throws IOException
/*     */   {
/* 502 */     List<VillagerRecord> v = new ArrayList();
/*     */     
/* 504 */     int nb = ds.readInt();
/*     */     
/* 506 */     for (int i = 0; i < nb; i++) {
/* 507 */       v.add(readNullableVillagerRecord(mw, ds));
/*     */     }
/*     */     
/* 510 */     return v;
/*     */   }
/*     */   
/*     */   public static void writeBuildingPlanInfo(BuildingPlan plan, DataOutput data) throws IOException {
/* 514 */     data.writeUTF(plan.buildingKey);
/*     */     
/* 516 */     data.writeInt(plan.level);
/* 517 */     data.writeInt(plan.variation);
/*     */     
/* 519 */     writeNullableString(plan.planName, data);
/* 520 */     writeNullableString(plan.nativeName, data);
/* 521 */     writeNullableString(plan.requiredTag, data);
/* 522 */     writeNullableString(plan.shop, data);
/* 523 */     writeNullableString(plan.type, data);
/* 524 */     data.writeInt(plan.price);
/* 525 */     data.writeInt(plan.reputation);
/* 526 */     writeStringList(plan.maleResident, data);
/* 527 */     writeStringList(plan.femaleResident, data);
/* 528 */     writeStringList(plan.startingSubBuildings, data);
/* 529 */     writeStringList(plan.subBuildings, data);
/* 530 */     writeStringList(plan.tags, data);
/*     */   }
/*     */   
/*     */   public static void writeInventory(HashMap<InvItem, Integer> inventory, DataOutput data) throws IOException {
/* 534 */     data.writeInt(inventory.size());
/*     */     
/* 536 */     for (InvItem key : inventory.keySet()) {
/* 537 */       data.writeInt(Item.func_150891_b(key.getItem()));
/* 538 */       data.writeInt(key.meta);
/* 539 */       data.writeInt(((Integer)inventory.get(key)).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void writeItemStack(ItemStack par1ItemStack, DataOutput par2DataOutput)
/*     */     throws IOException
/*     */   {
/* 549 */     if (par1ItemStack == null) {
/* 550 */       par2DataOutput.writeShort(-1);
/*     */     } else {
/* 552 */       par2DataOutput.writeInt(Item.func_150891_b(par1ItemStack.func_77973_b()));
/* 553 */       par2DataOutput.writeByte(par1ItemStack.field_77994_a);
/* 554 */       par2DataOutput.writeShort(par1ItemStack.func_77960_j());
/*     */       
/* 556 */       if (par1ItemStack.func_77973_b().func_77645_m()) {
/* 557 */         writeNBTTagCompound(par1ItemStack.field_77990_d, par2DataOutput);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void writeNBTTagCompound(NBTTagCompound par1NBTTagCompound, DataOutput par2DataOutput)
/*     */     throws IOException
/*     */   {
/* 568 */     if (par1NBTTagCompound == null) {
/* 569 */       par2DataOutput.writeShort(-1);
/*     */     } else {
/* 571 */       byte[] var3 = CompressedStreamTools.func_74798_a(par1NBTTagCompound);
/* 572 */       par2DataOutput.writeShort((short)var3.length);
/* 573 */       par2DataOutput.write(var3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableBuildingLocation(BuildingLocation bl, DataOutput data) throws IOException
/*     */   {
/* 579 */     data.writeBoolean(bl == null);
/*     */     
/* 581 */     if (bl != null) {
/* 582 */       data.writeBoolean(bl.isCustomBuilding);
/* 583 */       writeNullableString(bl.planKey, data);
/* 584 */       writeNullableString(bl.shop, data);
/* 585 */       writeStringList(bl.maleResident, data);
/* 586 */       writeStringList(bl.femaleResident, data);
/* 587 */       data.writeInt(bl.minx);
/* 588 */       data.writeInt(bl.maxx);
/* 589 */       data.writeInt(bl.miny);
/* 590 */       data.writeInt(bl.maxy);
/* 591 */       data.writeInt(bl.minz);
/* 592 */       data.writeInt(bl.maxz);
/*     */       
/* 594 */       data.writeInt(bl.minxMargin);
/* 595 */       data.writeInt(bl.maxxMargin);
/* 596 */       data.writeInt(bl.minyMargin);
/* 597 */       data.writeInt(bl.maxyMargin);
/* 598 */       data.writeInt(bl.minzMargin);
/* 599 */       data.writeInt(bl.maxzMargin);
/*     */       
/* 601 */       data.writeInt(bl.orientation);
/* 602 */       data.writeInt(bl.length);
/* 603 */       data.writeInt(bl.width);
/* 604 */       data.writeInt(bl.level);
/* 605 */       data.writeInt(bl.getVariation());
/* 606 */       data.writeInt(bl.reputation);
/* 607 */       data.writeInt(bl.price);
/*     */       
/* 609 */       writeNullablePoint(bl.pos, data);
/* 610 */       writeNullablePoint(bl.chestPos, data);
/* 611 */       writeNullablePoint(bl.sleepingPos, data);
/* 612 */       writeNullablePoint(bl.sellingPos, data);
/* 613 */       writeNullablePoint(bl.craftingPos, data);
/* 614 */       writeNullablePoint(bl.shelterPos, data);
/* 615 */       writeNullablePoint(bl.defendingPos, data);
/* 616 */       writeNullableString(bl.culture.key, data);
/* 617 */       writeStringList(bl.tags, data);
/* 618 */       writeStringList(bl.subBuildings, data);
/*     */       
/* 620 */       data.writeBoolean(bl.showTownHallSigns);
/* 621 */       data.writeBoolean(bl.upgradesAllowed);
/* 622 */       data.writeBoolean(bl.bedrocklevel);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableBuildingProject(BuildingProject bp, DataOutput data) throws IOException
/*     */   {
/* 628 */     data.writeBoolean(bp == null);
/*     */     
/* 630 */     if (bp != null) {
/* 631 */       data.writeBoolean(bp.isCustomBuilding);
/* 632 */       writeNullableString(bp.key, data);
/* 633 */       writeNullableBuildingLocation(bp.location, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableGoods(Goods g, DataOutput data) throws IOException {
/* 638 */     data.writeBoolean(g == null);
/*     */     
/* 640 */     if (g != null) {
/* 641 */       data.writeInt(Item.func_150891_b(g.item.getItem()));
/* 642 */       data.writeByte(g.item.meta);
/* 643 */       writeNullableString(g.requiredTag, data);
/* 644 */       writeNullableString(g.desc, data);
/* 645 */       data.writeBoolean(g.autoGenerate);
/* 646 */       data.writeInt(g.minReputation);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableItemStack(ItemStack is, DataOutput data) throws IOException
/*     */   {
/* 652 */     data.writeBoolean(is == null);
/*     */     
/* 654 */     if (is != null) {
/* 655 */       writeItemStack(is, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullablePoint(Point p, DataOutput data) throws IOException {
/* 660 */     data.writeBoolean(p == null);
/*     */     
/* 662 */     if (p != null) {
/* 663 */       data.writeInt(p.getiX());
/* 664 */       data.writeInt(p.getiY());
/* 665 */       data.writeInt(p.getiZ());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullablePuja(PujaSacrifice puja, DataOutput data) throws IOException {
/* 670 */     data.writeBoolean(puja == null);
/* 671 */     if (puja != null)
/*     */     {
/* 673 */       data.writeShort(puja.type);
/*     */       
/* 675 */       if (puja.currentTarget != null) {
/* 676 */         data.writeShort(puja.currentTarget.enchantment.field_77352_x);
/*     */       } else {
/* 678 */         data.writeShort(0);
/*     */       }
/* 680 */       data.writeShort(puja.pujaProgress);
/* 681 */       data.writeShort(puja.offeringNeeded);
/* 682 */       data.writeShort(puja.offeringProgress);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableQuestInstance(Quest.QuestInstance qi, DataOutput ds) throws IOException {
/* 687 */     ds.writeBoolean(qi == null);
/*     */     
/* 689 */     if (qi != null) {
/* 690 */       ds.writeLong(qi.uniqueid);
/* 691 */       ds.writeUTF(qi.quest.key);
/* 692 */       ds.writeUTF(qi.profile.key);
/* 693 */       ds.writeByte(qi.currentStep);
/* 694 */       ds.writeLong(qi.startTime);
/* 695 */       ds.writeLong(qi.currentStepStart);
/*     */       
/* 697 */       ds.writeByte(qi.villagers.size());
/* 698 */       for (String key : qi.villagers.keySet()) {
/* 699 */         ds.writeUTF(key);
/* 700 */         writeNullableQuestVillager((Quest.QuestInstanceVillager)qi.villagers.get(key), ds);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableQuestVillager(Quest.QuestInstanceVillager v, DataOutput data) throws IOException {
/* 706 */     data.writeBoolean(v == null);
/*     */     
/* 708 */     if (v != null) {
/* 709 */       writeNullablePoint(v.townHall, data);
/* 710 */       data.writeLong(v.id);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableResourceLocation(ResourceLocation rs, DataOutput data) throws IOException
/*     */   {
/* 716 */     data.writeBoolean(rs == null);
/*     */     
/* 718 */     if (rs != null) {
/* 719 */       data.writeUTF(rs.func_110623_a());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableString(String s, DataOutput data) throws IOException
/*     */   {
/* 725 */     data.writeBoolean(s == null);
/*     */     
/* 727 */     if (s != null) {
/* 728 */       data.writeUTF(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeNullableVillagerRecord(VillagerRecord vr, DataOutput data) throws IOException
/*     */   {
/* 734 */     data.writeBoolean(vr == null);
/*     */     
/* 736 */     if (vr != null)
/*     */     {
/* 738 */       data.writeLong(vr.id);
/*     */       
/* 740 */       writeNullableString(vr.type, data);
/* 741 */       writeNullableString(vr.firstName, data);
/* 742 */       writeNullableString(vr.familyName, data);
/* 743 */       writeNullableString(vr.nameKey, data);
/* 744 */       writeNullableString(vr.occupation, data);
/* 745 */       writeNullableResourceLocation(vr.texture, data);
/*     */       
/* 747 */       data.writeInt(vr.nb);
/* 748 */       data.writeInt(vr.gender);
/* 749 */       data.writeInt(vr.villagerSize);
/*     */       
/* 751 */       writeNullableString(vr.culture.key, data);
/*     */       
/* 753 */       writeNullableString(vr.fathersName, data);
/* 754 */       writeNullableString(vr.mothersName, data);
/* 755 */       writeNullableString(vr.spousesName, data);
/* 756 */       writeNullableString(vr.maidenName, data);
/*     */       
/* 758 */       data.writeBoolean(vr.killed);
/* 759 */       data.writeBoolean(vr.raidingVillage);
/* 760 */       data.writeBoolean(vr.awayraiding);
/* 761 */       data.writeBoolean(vr.awayhired);
/*     */       
/* 763 */       writeNullablePoint(vr.housePos, data);
/* 764 */       writeNullablePoint(vr.townHallPos, data);
/* 765 */       writeNullablePoint(vr.originalVillagePos, data);
/*     */       
/* 767 */       data.writeLong(vr.raiderSpawn);
/* 768 */       writeInventory(vr.inventory, data);
/* 769 */       writeStringList(vr.questTags, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writePointIntegerMap(HashMap<Point, Integer> map, DataOutput data) throws IOException {
/* 774 */     data.writeInt(map.size());
/*     */     
/* 776 */     for (Point p : map.keySet()) {
/* 777 */       writeNullablePoint(p, data);
/* 778 */       data.writeInt(((Integer)map.get(p)).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writePointList(List<Point> points, DataOutput data) throws IOException {
/* 783 */     data.writeInt(points.size());
/*     */     
/* 785 */     for (Point p : points) {
/* 786 */       writeNullablePoint(p, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeProjectListList(Map<BuildingProject.EnumProjects, List<BuildingProject>> projects, DataOutput data) throws IOException
/*     */   {
/* 792 */     data.writeInt(BuildingProject.EnumProjects.values().length);
/*     */     
/* 794 */     for (BuildingProject.EnumProjects ep : BuildingProject.EnumProjects.values()) {
/* 795 */       if (projects.containsKey(ep)) {
/* 796 */         data.writeInt(((List)projects.get(ep)).size());
/* 797 */         for (BuildingProject bp : (List)projects.get(ep)) {
/* 798 */           writeNullableBuildingProject(bp, data);
/*     */         }
/*     */       } else {
/* 801 */         data.writeInt(0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeStringList(List<String> strings, DataOutput data) throws IOException
/*     */   {
/* 808 */     data.writeInt(strings.size());
/*     */     
/* 810 */     for (String s : strings) {
/* 811 */       writeNullableString(s, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeStringStringArray(String[][] strings, DataOutput data) throws IOException {
/* 816 */     data.writeInt(strings.length);
/*     */     
/* 818 */     for (String[] array : strings) {
/* 819 */       data.writeInt(array.length);
/* 820 */       for (String s : array) {
/* 821 */         writeNullableString(s, data);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeStringStringListMap(Map<String, List<String>> strings, DataOutput data) throws IOException
/*     */   {
/* 828 */     if (strings == null) {
/* 829 */       data.writeInt(0);
/* 830 */       return;
/*     */     }
/*     */     
/* 833 */     data.writeInt(strings.size());
/*     */     
/* 835 */     for (String key : strings.keySet()) {
/* 836 */       data.writeUTF(key);
/* 837 */       writeStringList((List)strings.get(key), data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeStringStringMap(Map<String, String> strings, DataOutput data) throws IOException
/*     */   {
/* 843 */     if (strings == null) {
/* 844 */       data.writeInt(0);
/* 845 */       return;
/*     */     }
/*     */     
/* 848 */     data.writeInt(strings.size());
/*     */     
/* 850 */     for (String s : strings.keySet()) {
/* 851 */       data.writeUTF(s);
/* 852 */       writeNullableString((String)strings.get(s), data);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeVillagerRecordList(List<VillagerRecord> vrecords, DataOutput data) throws IOException {
/* 857 */     data.writeInt(vrecords.size());
/*     */     
/* 859 */     for (VillagerRecord vr : vrecords) {
/* 860 */       writeNullableVillagerRecord(vr, data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\network\StreamReadWrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
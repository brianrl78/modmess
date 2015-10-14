/*      */ package org.millenaire.common;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockFenceGate;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.monster.EntityCreeper;
/*      */ import net.minecraft.entity.monster.EntityMob;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemArmor;
/*      */ import net.minecraft.item.ItemAxe;
/*      */ import net.minecraft.item.ItemHoe;
/*      */ import net.minecraft.item.ItemPickaxe;
/*      */ import net.minecraft.item.ItemSpade;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.item.ItemTool;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.pathfinding.PathEntity;
/*      */ import net.minecraft.pathfinding.PathPoint;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import org.millenaire.common.building.Building;
/*      */ import org.millenaire.common.building.Building.PerformanceMonitor;
/*      */ import org.millenaire.common.building.BuildingLocation;
/*      */ import org.millenaire.common.building.BuildingResManager;
/*      */ import org.millenaire.common.core.MillCommonUtilities;
/*      */ import org.millenaire.common.forge.CommonProxy;
/*      */ import org.millenaire.common.forge.Mill;
/*      */ import org.millenaire.common.forge.MillAchievements;
/*      */ import org.millenaire.common.goal.Goal;
/*      */ import org.millenaire.common.goal.Goal.GoalInformation;
/*      */ import org.millenaire.common.item.Goods;
/*      */ import org.millenaire.common.item.Goods.ItemClothes;
/*      */ import org.millenaire.common.item.Goods.ItemMillenaireAxe;
/*      */ import org.millenaire.common.item.Goods.ItemMillenaireBow;
/*      */ import org.millenaire.common.item.Goods.ItemMillenaireHoe;
/*      */ import org.millenaire.common.item.Goods.ItemMillenairePickaxe;
/*      */ import org.millenaire.common.item.Goods.ItemMillenaireShovel;
/*      */ import org.millenaire.common.network.ServerSender;
/*      */ import org.millenaire.common.network.StreamReadWrite;
/*      */ import org.millenaire.common.pathing.AStarPathing.PathingException;
/*      */ import org.millenaire.common.pathing.AStarPathing.PathingWorker;
/*      */ import org.millenaire.common.pathing.atomicstryker.AS_PathEntity;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarConfig;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarNode;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarPathPlanner;
/*      */ import org.millenaire.common.pathing.atomicstryker.AStarStatic;
/*      */ 
/*      */ public abstract class MillVillager extends EntityCreature implements cpw.mods.fml.common.registry.IEntityAdditionalSpawnData, org.millenaire.common.pathing.atomicstryker.IAStarPathedEntity
/*      */ {
/*      */   private static final double MOVE_SPEED = 0.699D;
/*      */   private static final int ATTACK_RANGE_DEFENSIVE = 20;
/*      */   private static final String FREE_CLOTHES = "free";
/*      */   public static final int CONCEPTION_CHANCE = 6;
/*      */   public static final int FOREIGN_MERCHANT_NB_NIGHTS_BEFORE_LEAVING = 5;
/*      */   public static final int MALE = 1;
/*      */   public static final int FEMALE = 2;
/*      */   public static final String GENERIC_VILLAGER = "ml_GenericVillager";
/*      */   public static final String GENERIC_ASYMM_FEMALE = "ml_GenericAsimmFemale";
/*      */   public static final String GENERIC_SYMM_FEMALE = "ml_GenericSimmFemale";
/*      */   public static final String GENERIC_ZOMBIE = "ml_GenericZombie";
/*      */   
/*      */   public static class InvItemAlphabeticalComparator implements java.util.Comparator<InvItem>
/*      */   {
/*      */     public int compare(InvItem arg0, InvItem arg1)
/*      */     {
/*   94 */       return arg0.getName().compareTo(arg1.getName());
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MLEntityGenericAsymmFemale extends MillVillager
/*      */   {
/*      */     public MLEntityGenericAsymmFemale(World world) {
/*  101 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MLEntityGenericMale extends MillVillager {
/*      */     public MLEntityGenericMale(World world) {
/*  107 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MLEntityGenericSymmFemale extends MillVillager {
/*      */     public MLEntityGenericSymmFemale(World world) {
/*  113 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MLEntityGenericZombie extends MillVillager {
/*      */     public MLEntityGenericZombie(World world) {
/*  119 */       super();
/*      */     }
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
/*      */     public void readSpawnData(ByteBuf additionalData) {}
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
/*  143 */   public static ItemStack[] hoeWood = { new ItemStack(Items.field_151017_I, 1) };
/*  144 */   public static ItemStack[] hoeStone = { new ItemStack(Items.field_151018_J, 1) };
/*  145 */   public static ItemStack[] hoeSteel = { new ItemStack(Items.field_151019_K, 1) };
/*  146 */   public static ItemStack[] hoeNorman = { new ItemStack(Mill.normanHoe, 1) };
/*  147 */   public static ItemStack[] hoeMayan = { new ItemStack(Mill.mayanHoe, 1) };
/*      */   
/*  149 */   public static ItemStack[] shovelWood = { new ItemStack(Items.field_151038_n, 1) };
/*  150 */   public static ItemStack[] shovelStone = { new ItemStack(Items.field_151051_r, 1) };
/*  151 */   public static ItemStack[] shovelSteel = { new ItemStack(Items.field_151037_a, 1) };
/*  152 */   public static ItemStack[] shovelNorman = { new ItemStack(Mill.normanShovel, 1) };
/*  153 */   public static ItemStack[] shovelMayan = { new ItemStack(Mill.mayanShovel, 1) };
/*      */   
/*  155 */   public static ItemStack[] pickaxeWood = { new ItemStack(Items.field_151039_o, 1) };
/*  156 */   public static ItemStack[] pickaxeStone = { new ItemStack(Items.field_151050_s, 1) };
/*  157 */   public static ItemStack[] pickaxeSteel = { new ItemStack(Items.field_151035_b, 1) };
/*  158 */   public static ItemStack[] pickaxeNorman = { new ItemStack(Mill.normanPickaxe, 1) };
/*  159 */   public static ItemStack[] pickaxeMayan = { new ItemStack(Mill.mayanPickaxe, 1) };
/*      */   
/*  161 */   public static ItemStack[] axeWood = { new ItemStack(Items.field_151053_p, 1) };
/*  162 */   public static ItemStack[] axeStone = { new ItemStack(Items.field_151049_t, 1) };
/*  163 */   public static ItemStack[] axeSteel = { new ItemStack(Items.field_151036_c, 1) };
/*  164 */   public static ItemStack[] axeNorman = { new ItemStack(Mill.normanAxe, 1) };
/*  165 */   public static ItemStack[] axeMayan = { new ItemStack(Mill.mayanAxe, 1) };
/*      */   
/*  167 */   public static ItemStack[] swordWood = { new ItemStack(Items.field_151041_m, 1) };
/*  168 */   public static ItemStack[] swordStone = { new ItemStack(Items.field_151052_q, 1) };
/*  169 */   public static ItemStack[] swordSteel = { new ItemStack(Items.field_151040_l, 1) };
/*  170 */   public static ItemStack[] swordNorman = { new ItemStack(Mill.normanBroadsword, 1) };
/*  171 */   public static ItemStack[] swordMayan = { new ItemStack(Mill.mayanMace, 1) };
/*  172 */   public static ItemStack[] swordByzantine = { new ItemStack(Mill.byzantineMace, 1) };
/*      */   
/*      */ 
/*  175 */   public static final Item[] weapons = { Mill.normanBroadsword, Mill.tachiSword, Mill.byzantineMace, Items.field_151048_u, Mill.mayanMace, Items.field_151040_l, Items.field_151052_q, Mill.yumiBow, Items.field_151031_f, Mill.normanAxe, Mill.mayanAxe, Items.field_151036_c, Items.field_151049_t, Mill.normanPickaxe, Mill.mayanPickaxe, Items.field_151035_b, Items.field_151050_s, Mill.normanHoe, Mill.mayanHoe, Items.field_151019_K, Items.field_151018_J, Mill.normanShovel, Mill.mayanShovel, Items.field_151051_r, Items.field_151038_n };
/*      */   
/*      */ 
/*      */ 
/*  179 */   public static final Item[] weaponsHandToHand = { Mill.normanBroadsword, Mill.tachiSword, Mill.byzantineMace, Items.field_151048_u, Mill.mayanMace, Items.field_151040_l, Items.field_151052_q, Mill.normanAxe, Mill.mayanAxe, Items.field_151036_c, Items.field_151049_t, Mill.normanPickaxe, Mill.mayanPickaxe, Items.field_151035_b, Items.field_151050_s, Mill.normanHoe, Mill.mayanHoe, Items.field_151019_K, Items.field_151018_J, Mill.normanShovel, Mill.mayanShovel, Items.field_151051_r, Items.field_151038_n };
/*      */   
/*      */ 
/*      */ 
/*  183 */   public static final Item[] weaponsSwords = { Mill.normanBroadsword, Mill.tachiSword, Mill.byzantineMace, Items.field_151048_u, Mill.mayanMace, Items.field_151040_l, Items.field_151052_q, Items.field_151041_m };
/*      */   
/*      */ 
/*  186 */   public static final Item[] weaponsRanged = { Mill.yumiBow, Items.field_151031_f };
/*      */   
/*  188 */   private static final Item[] weaponsBow = { Mill.yumiBow, Items.field_151031_f };
/*      */   
/*  190 */   public static final Item[] helmets = { Mill.normanHelmet, Mill.byzantineHelmet, Mill.japaneseWarriorBlueHelmet, Mill.japaneseWarriorRedHelmet, Mill.japaneseGuardHelmet, Items.field_151161_ac, Items.field_151028_Y, Items.field_151020_U, Items.field_151169_ag, Items.field_151024_Q };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   public static final Item[] chestplates = { Mill.normanPlate, Mill.byzantinePlate, Mill.japaneseWarriorBluePlate, Mill.japaneseWarriorRedPlate, Mill.japaneseGuardPlate, Items.field_151163_ad, Items.field_151030_Z, Items.field_151023_V, Items.field_151171_ah, Items.field_151027_R };
/*      */   
/*      */ 
/*  200 */   public static final Item[] legs = { Mill.normanLegs, Mill.byzantineLegs, Mill.japaneseWarriorBlueLegs, Mill.japaneseWarriorRedLegs, Mill.japaneseGuardLegs, Items.field_151173_ae, Items.field_151165_aa, Items.field_151022_W, Items.field_151149_ai, Items.field_151026_S };
/*      */   
/*      */ 
/*  203 */   public static final Item[] boots = { Mill.normanBoots, Mill.byzantineBoots, Mill.japaneseWarriorBlueBoots, Mill.japaneseWarriorRedBoots, Mill.japaneseGuardBoots, Items.field_151175_af, Items.field_151167_ab, Items.field_151029_X, Items.field_151151_aj, Items.field_151021_T };
/*      */   
/*      */ 
/*  206 */   public static final Item[] pickaxes = { Mill.normanPickaxe, Items.field_151046_w, Items.field_151035_b, Items.field_151050_s, Items.field_151039_o };
/*      */   
/*  208 */   public static final Item[] axes = { Mill.normanAxe, Items.field_151056_x, Items.field_151036_c, Items.field_151049_t, Items.field_151053_p };
/*      */   
/*  210 */   public static final Item[] shovels = { Mill.normanShovel, Items.field_151047_v, Items.field_151037_a, Items.field_151051_r, Items.field_151038_n };
/*      */   
/*  212 */   public static final Item[] hoes = { Mill.normanHoe, Items.field_151012_L, Items.field_151019_K, Items.field_151018_J, Items.field_151017_I };
/*      */   
/*  214 */   private static final Item[] foodGrowth = { Items.field_151110_aK, Items.field_151025_P, Items.field_151083_be, Items.field_151147_al, Items.field_151077_bg, Items.field_151101_aQ, Items.field_151172_bF, Items.field_151168_bH, Mill.tripes, Mill.boudin, Mill.vegcurry, Mill.chickencurry, Mill.rice, Mill.masa, Mill.wah, Mill.udon, Mill.ikayaki, Mill.lambCooked, Mill.souvlaki };
/*      */   
/*      */ 
/*  217 */   private static final int[] foodGrowthValues = { 1, 2, 4, 4, 3, 3, 1, 2, 6, 4, 3, 5, 1, 3, 5, 5, 5, 3, 6 };
/*      */   
/*      */ 
/*      */ 
/*  221 */   private static final Item[] foodConception = { Mill.wineFancy, Items.field_151105_aU, Mill.calva, Mill.sake, Mill.cacauhaa, Mill.wineBasic, Mill.cider, Mill.rasgulla, Mill.feta, Items.field_151106_aX };
/*      */   
/*  223 */   private static final int[] foodConceptionChanceOn = { 2, 2, 2, 2, 2, 3, 3, 3, 3, 4 };
/*      */   
/*      */   static final int GATHER_RANGE = 20;
/*      */   
/*      */   private static final int HOLD_DURATION = 20;
/*      */   
/*      */   public static final int ATTACK_RANGE = 80;
/*      */   
/*  231 */   public static boolean usingCustomPathing = true;
/*  232 */   public static boolean usingBinaryPathing = false;
/*      */   public VillagerType vtype;
/*      */   
/*      */   public static MillVillager createVillager(Culture c, String type, int gender, World world, Point spawnPos, Point housePos, Point thPos, boolean respawn, String firstName, String familyName)
/*      */   {
/*  237 */     if ((world.field_72995_K) || (!(world instanceof WorldServer))) {
/*  238 */       MLN.printException("Tried creating a villager in client world: " + world, new Exception());
/*  239 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  244 */     if ((type == null) || (type.length() == 0)) {
/*  245 */       MLN.error(null, "Tried creating child of null type: " + type);
/*      */     }
/*      */     
/*  248 */     if (c.getVillagerType(type.toLowerCase()) == null) {
/*  249 */       for (Culture c2 : Culture.ListCultures) {
/*  250 */         if (c2.getVillagerType(type) != null) {
/*  251 */           MLN.error(null, "Could not find villager type " + type + " in culture " + c.key + " but could in " + c2.key + " so switching.");
/*  252 */           c = c2;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  257 */     if (c.getVillagerType(type.toLowerCase()) != null)
/*      */     {
/*  259 */       VillagerType vtype = c.getVillagerType(type.toLowerCase());
/*      */       
/*  261 */       MillVillager villager = (MillVillager)net.minecraft.entity.EntityList.func_75620_a(vtype.getEntityName(), world);
/*      */       
/*  263 */       if (villager == null) {
/*  264 */         MLN.error(c, "Could not create villager of dynamic type: " + type + " entity: " + vtype.getEntityName());
/*  265 */         return null;
/*      */       }
/*      */       
/*  268 */       villager.housePoint = housePos;
/*  269 */       villager.townHallPoint = thPos;
/*      */       
/*  271 */       if (familyName == null) {
/*  272 */         familyName = vtype.getRandomFamilyName();
/*      */       }
/*  274 */       villager.initialise(vtype, familyName, respawn);
/*      */       
/*  276 */       if (firstName != null) {
/*  277 */         villager.firstName = firstName;
/*      */       }
/*      */       
/*  280 */       villager.func_70107_b(spawnPos.x, spawnPos.y, spawnPos.z);
/*      */     }
/*      */     else {
/*  283 */       MLN.error(null, "Unknown villager type: " + type + " for culture " + c);
/*  284 */       return null;
/*      */     }
/*      */     MillVillager villager;
/*  287 */     return villager;
/*      */   }
/*      */   
/*      */   public static void readVillagerPacket(ByteBufInputStream data) {
/*      */     try {
/*  292 */       long villager_id = data.readLong();
/*      */       
/*  294 */       if (Mill.clientWorld.villagers.containsKey(Long.valueOf(villager_id))) {
/*  295 */         ((MillVillager)Mill.clientWorld.villagers.get(Long.valueOf(villager_id))).readVillagerStreamdata(data);
/*      */       }
/*  297 */       else if (MLN.LogNetwork >= 2) {
/*  298 */         MLN.minor(null, "readVillagerPacket for unknown villager: " + villager_id);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  302 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  307 */   public int action = 0;
/*  308 */   public String goalKey = null;
/*  309 */   private Goal.GoalInformation goalInformation = null;
/*      */   private Point pathDestPoint;
/*      */   public PathPoint prevPathPoint;
/*  312 */   private Building house = null; private Building townHall = null;
/*  313 */   public Point housePoint = null;
/*  314 */   public Point prevPoint = null;
/*  315 */   public Point townHallPoint = null;
/*  316 */   public boolean extraLog = false;
/*  317 */   public String firstName = "";
/*  318 */   public String familyName = "";
/*      */   public ItemStack heldItem;
/*  320 */   public long timer = 0L; public long actionStart = 0L;
/*  321 */   public boolean allowRandomMoves = false; public boolean stopMoving = false;
/*  322 */   public PathPoint lastJump = null;
/*  323 */   public float scale = 1.0F;
/*  324 */   public int gender = 0;
/*  325 */   public boolean noHouseorTH = false;
/*  326 */   public boolean registered = false;
/*      */   public int longDistanceStuck;
/*  328 */   public boolean nightActionPerformed = false;
/*  329 */   public long speech_started = 0L;
/*      */   public HashMap<InvItem, Integer> inventory;
/*      */   public Block previousBlock;
/*      */   public int previousBlockMeta;
/*  333 */   public int size = 0;
/*  334 */   public long pathingTime; public long timeSinceLastPathingTimeDisplay; public long villager_id = 0L;
/*  335 */   public int nbPathsCalculated = 0; public int nbPathNoStart = 0; public int nbPathNoEnd = 0; public int nbPathAborted = 0; public int nbPathFailure = 0;
/*  336 */   public List<org.millenaire.common.pathing.AStarPathing.PathKey> abortedKeys = new ArrayList();
/*  337 */   public long goalStarted = 0L;
/*      */   
/*  339 */   public boolean hasPrayedToday = false; public boolean hasDrunkToday = false;
/*      */   
/*  341 */   public int heldItemCount = 0; public int heldItemId = -1;
/*      */   
/*      */   public static final int MAX_CHILD_SIZE = 20;
/*  344 */   public String speech_key = null;
/*  345 */   public int speech_variant = 0;
/*      */   
/*  347 */   public String dialogueKey = null;
/*  348 */   public int dialogueRole = 0;
/*  349 */   public long dialogueStart = 0L;
/*  350 */   public char dialogueColour = 'f';
/*  351 */   public boolean dialogueChat = false;
/*      */   
/*      */ 
/*  354 */   public String dialogueTargetFirstName = null;
/*  355 */   public String dialogueTargetLastName = null;
/*      */   
/*  357 */   private Point doorToClose = null;
/*      */   
/*  359 */   public int foreignMerchantNbNights = 0;
/*  360 */   public int foreignMerchantStallId = -1;
/*      */   
/*  362 */   public boolean lastAttackByPlayer = false;
/*      */   
/*  364 */   public HashMap<Goal, Long> lastGoalTime = new HashMap();
/*      */   
/*  366 */   public String hiredBy = null;
/*      */   
/*  368 */   public boolean aggressiveStance = false;
/*      */   
/*  370 */   public long hiredUntil = 0L;
/*      */   public boolean isUsingBow;
/*      */   public boolean isUsingHandToHand;
/*  373 */   public boolean isRaider = false;
/*      */   private AStarPathing.PathingWorker pathingWorker;
/*      */   public AStarPathPlanner jpsPathPlanner;
/*  376 */   public static final AStarConfig DEFAULT_JPS_CONFIG = new AStarConfig(true, false, false, true);
/*      */   
/*      */   public AS_PathEntity pathEntity;
/*      */   
/*  380 */   public int updateCounter = 0;
/*      */   public long client_lastupdated;
/*  382 */   private boolean registeredInGlobalList = false;
/*      */   
/*      */   public MillWorld mw;
/*  385 */   public int pathfailure = 0;
/*      */   
/*  387 */   private boolean pathFailedSincelastTick = false;
/*      */   
/*  389 */   private List<AStarNode> pathCalculatedSinceLastTick = null;
/*      */   
/*  391 */   private int localStuck = 0;
/*      */   
/*  393 */   private long pathCalculationStartTime = 0L;
/*      */   
/*  395 */   private ResourceLocation clothTexture = null;
/*  396 */   private String clothName = null;
/*      */   
/*  398 */   public boolean shouldLieDown = false;
/*      */   
/*  400 */   public LinkedHashMap<Goods, Integer> merchantSells = new LinkedHashMap();
/*      */   
/*  402 */   public ResourceLocation texture = null;
/*      */   
/*      */   public MillVillager(World world)
/*      */   {
/*  406 */     super(world);
/*  407 */     this.field_70170_p = world;
/*      */     
/*  409 */     this.mw = Mill.getMillWorld(world);
/*      */     
/*  411 */     this.inventory = new HashMap();
/*  412 */     func_70606_j(func_110138_aP());
/*      */     
/*  414 */     this.field_70178_ae = true;
/*      */     
/*  416 */     this.client_lastupdated = world.func_72820_D();
/*      */     
/*  418 */     this.jpsPathPlanner = new AStarPathPlanner(world, this);
/*      */     
/*  420 */     func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.699D);
/*      */     
/*  422 */     if (MLN.LogVillagerSpawn >= 3) {
/*  423 */       Exception e = new Exception();
/*      */       
/*  425 */       MLN.printException("Creating villager " + this + " in world: " + world, e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToInv(Block block, int nb) {
/*  430 */     addToInv(Item.func_150898_a(block), 0, nb);
/*      */   }
/*      */   
/*      */   public void addToInv(Block block, int meta, int nb) {
/*  434 */     addToInv(Item.func_150898_a(block), meta, nb);
/*      */   }
/*      */   
/*      */   public void addToInv(InvItem iv, int nb) {
/*  438 */     addToInv(iv.getItem(), iv.meta, nb);
/*      */   }
/*      */   
/*      */   public void addToInv(Item item, int nb) {
/*  442 */     addToInv(item, 0, nb);
/*      */   }
/*      */   
/*      */   public void addToInv(Item item, int meta, int nb)
/*      */   {
/*      */     try {
/*  448 */       InvItem key = new InvItem(item, meta);
/*      */       
/*  450 */       if (this.inventory.containsKey(key)) {
/*  451 */         this.inventory.put(key, Integer.valueOf(((Integer)this.inventory.get(key)).intValue() + nb));
/*      */       } else {
/*  453 */         this.inventory.put(key, Integer.valueOf(nb));
/*      */       }
/*  455 */       if (getTownHall() != null) {
/*  456 */         getTownHall().updateVillagerRecord(this);
/*      */       } else {
/*  458 */         MLN.error(this, "Wanted to update VR after an addToInv but TH is null.");
/*      */       }
/*  460 */       updateClothTexturePath();
/*      */     } catch (MLN.MillenaireException e) {
/*  462 */       MLN.printException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void adjustSize()
/*      */   {
/*  468 */     this.scale = (0.5F + this.size / 100.0F);
/*      */   }
/*      */   
/*      */   protected void func_110147_ax()
/*      */   {
/*  473 */     super.func_110147_ax();
/*  474 */     func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.699D);
/*  475 */     func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(computeMaxHealth());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void applyPathCalculatedSinceLastTick()
/*      */   {
/*  482 */     AS_PathEntity path = AStarStatic.translateAStarPathtoPathEntity(this.field_70170_p, this.pathCalculatedSinceLastTick, getPathingConfig());
/*      */     try
/*      */     {
/*  485 */       registerNewPath(path);
/*      */       
/*  487 */       this.pathfailure = 0;
/*      */     }
/*      */     catch (Exception e) {
/*  490 */       MLN.printException("Exception when finding JPS path:", e);
/*      */     }
/*      */     
/*  493 */     this.pathCalculatedSinceLastTick = null;
/*      */   }
/*      */   
/*      */   public void func_70785_a(Entity entity, float f)
/*      */   {
/*  498 */     if ((this.vtype.isArcher) && (f > 5.0F) && (hasBow())) {
/*  499 */       attackEntityBow(entity, f);
/*  500 */       this.isUsingBow = true;
/*      */     } else {
/*  502 */       if ((this.field_70724_aR <= 0) && (f < 2.0F) && (entity.field_70121_D.field_72337_e > this.field_70121_D.field_72338_b) && (entity.field_70121_D.field_72338_b < this.field_70121_D.field_72337_e)) {
/*  503 */         this.field_70724_aR = 20;
/*  504 */         entity.func_70097_a(DamageSource.func_76358_a(this), getAttackStrength());
/*  505 */         func_71038_i();
/*      */       }
/*  507 */       this.isUsingHandToHand = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void attackEntityBow(Entity entity, float f) {
/*  512 */     if (!(entity instanceof EntityLivingBase)) {
/*  513 */       return;
/*      */     }
/*      */     
/*  516 */     if (f < 10.0F) {
/*  517 */       double d = entity.field_70165_t - this.field_70165_t;
/*  518 */       double d1 = entity.field_70161_v - this.field_70161_v;
/*  519 */       if (this.field_70724_aR == 0)
/*      */       {
/*  521 */         float speedFactor = 1.0F;
/*  522 */         float damageBonus = 0.0F;
/*      */         
/*  524 */         ItemStack weapon = getWeapon();
/*      */         
/*  526 */         if (weapon != null) {
/*  527 */           Item item = weapon.func_77973_b();
/*      */           
/*  529 */           if ((item instanceof Goods.ItemMillenaireBow)) {
/*  530 */             Goods.ItemMillenaireBow bow = (Goods.ItemMillenaireBow)item;
/*      */             
/*  532 */             if (bow.speedFactor > speedFactor) {
/*  533 */               speedFactor = bow.speedFactor;
/*      */             }
/*  535 */             if (bow.damageBonus > damageBonus) {
/*  536 */               damageBonus = bow.damageBonus;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  541 */         EntityArrow arrow = new EntityArrow(this.field_70170_p, this, (EntityLivingBase)entity, 1.6F, 12.0F);
/*      */         
/*  543 */         this.field_70170_p.func_72956_a(this, "random.bow", 1.0F, 1.0F / (func_70681_au().nextFloat() * 0.4F + 0.8F));
/*  544 */         this.field_70170_p.func_72838_d(arrow);
/*      */         
/*  546 */         this.field_70724_aR = 60;
/*      */         
/*      */ 
/*  549 */         arrow.field_70159_w *= speedFactor;
/*  550 */         arrow.field_70181_x *= speedFactor;
/*  551 */         arrow.field_70179_y *= speedFactor;
/*      */         
/*      */ 
/*  554 */         arrow.func_70239_b(arrow.func_70242_d() + damageBonus);
/*      */       }
/*  556 */       this.field_70177_z = ((float)(Math.atan2(d1, d) * 180.0D / 3.1415927410125732D) - 90.0F);
/*  557 */       this.field_70787_b = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_70097_a(DamageSource ds, float i)
/*      */   {
/*  564 */     if ((ds.func_76364_f() == null) && (ds != DamageSource.field_76380_i)) {
/*  565 */       return false;
/*      */     }
/*      */     
/*  568 */     boolean hadFullHealth = func_110138_aP() == func_110143_aJ();
/*      */     
/*  570 */     boolean b = super.func_70097_a(ds, i);
/*      */     
/*  572 */     Entity entity = ds.func_76364_f();
/*      */     
/*  574 */     this.lastAttackByPlayer = false;
/*      */     
/*  576 */     if ((entity != null) && 
/*  577 */       ((entity instanceof EntityLivingBase))) {
/*  578 */       if ((entity instanceof EntityPlayer)) {
/*  579 */         this.lastAttackByPlayer = true;
/*      */         
/*  581 */         EntityPlayer player = (EntityPlayer)entity;
/*      */         
/*  583 */         if (!this.isRaider) {
/*  584 */           if (!this.vtype.hostile) {
/*  585 */             MillCommonUtilities.getServerProfile(player.field_70170_p, player.getDisplayName()).adjustReputation(getTownHall(), (int)(-i * 10.0F));
/*      */           }
/*  587 */           if ((this.field_70170_p.field_73013_u != EnumDifficulty.PEACEFUL) && (func_110143_aJ() < func_110138_aP() - 10.0F)) {
/*  588 */             this.field_70789_a = entity;
/*  589 */             clearGoal();
/*  590 */             if (getTownHall() != null) {
/*  591 */               getTownHall().callForHelp(entity);
/*      */             }
/*      */           }
/*      */           
/*  595 */           if ((hadFullHealth) && ((player.func_70694_bm() == null) || (MillCommonUtilities.getItemWeaponDamage(player.func_70694_bm().func_77973_b()) <= 1.0D)) && (!this.field_70170_p.field_72995_K)) {
/*  596 */             ServerSender.sendTranslatedSentence(player, '6', "ui.communicationexplanations", new String[0]);
/*      */           }
/*      */         }
/*      */         
/*  600 */         if ((this.lastAttackByPlayer) && (func_110143_aJ() <= 0.0F)) {
/*  601 */           if (this.vtype.hostile) {
/*  602 */             player.func_71064_a(MillAchievements.selfdefense, 1);
/*      */           } else {
/*  604 */             player.func_71064_a(MillAchievements.darkside, 1);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  609 */         this.field_70789_a = entity;
/*  610 */         clearGoal();
/*      */         
/*  612 */         if (getTownHall() != null) {
/*  613 */           getTownHall().callForHelp(entity);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  620 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean attemptChildConception()
/*      */   {
/*  627 */     int nbChildren = 0;
/*      */     
/*  629 */     for (MillVillager villager : getHouse().villagers) {
/*  630 */       if (villager.func_70631_g_()) {
/*  631 */         nbChildren++;
/*      */       }
/*      */     }
/*      */     
/*  635 */     if (nbChildren > 1) {
/*  636 */       if (MLN.LogChildren >= 3) {
/*  637 */         MLN.debug(this, "Wife already has " + nbChildren + " children, no need for more.");
/*      */       }
/*  639 */       return true;
/*      */     }
/*      */     
/*  642 */     int nbChildVillage = getTownHall().countChildren();
/*      */     
/*  644 */     if (nbChildVillage > MLN.maxChildrenNumber) {
/*  645 */       if (MLN.LogChildren >= 3) {
/*  646 */         MLN.debug(this, "Village already has " + nbChildVillage + ", no need for more.");
/*      */       }
/*  648 */       return true;
/*      */     }
/*      */     
/*  651 */     boolean couldMoveIn = false;
/*      */     
/*  653 */     for (Point housePoint : getTownHall().buildings)
/*      */     {
/*  655 */       Building house = this.mw.getBuilding(housePoint);
/*  656 */       if ((house != null) && (!house.equals(getHouse())) && (house.isHouse()))
/*      */       {
/*  658 */         if ((house.canChildMoveIn(1, this.familyName)) || (house.canChildMoveIn(2, this.familyName))) {
/*  659 */           couldMoveIn = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  664 */     if ((nbChildVillage > 5) && (!couldMoveIn)) {
/*  665 */       if (MLN.LogChildren >= 3) {
/*  666 */         MLN.debug(this, "Village already has " + nbChildVillage + " and no slot is available for the new child.");
/*      */       }
/*  668 */       return true;
/*      */     }
/*      */     
/*  671 */     List<Entity> entities = MillCommonUtilities.getEntitiesWithinAABB(this.field_70170_p, MillVillager.class, getPos(), 4, 2);
/*      */     
/*  673 */     boolean manFound = false;
/*      */     
/*  675 */     for (Entity ent : entities) {
/*  676 */       MillVillager villager = (MillVillager)ent;
/*  677 */       if ((villager.gender == 1) && (!villager.func_70631_g_())) {
/*  678 */         manFound = true;
/*      */       }
/*      */     }
/*      */     
/*  682 */     if (!manFound) {
/*  683 */       return false;
/*      */     }
/*      */     
/*  686 */     if (MLN.LogChildren >= 3) {
/*  687 */       MLN.debug(this, "Less than two kids and man present, trying for new child.");
/*      */     }
/*      */     
/*  690 */     boolean createChild = false;
/*      */     
/*  692 */     boolean foundConceptionFood = false;
/*      */     
/*  694 */     for (int i = 0; (i < foodConception.length) && (!foundConceptionFood); i++) {
/*  695 */       if (getHouse().countGoods(foodConception[i]) > 0) {
/*  696 */         getHouse().takeGoods(foodConception[i], 1);
/*  697 */         foundConceptionFood = true;
/*  698 */         if (MillCommonUtilities.randomInt(foodConceptionChanceOn[i]) == 0) {
/*  699 */           createChild = true;
/*  700 */           if (MLN.LogChildren >= 2) {
/*  701 */             MLN.minor(this, "Conceiving child with help from: " + foodConception[i].func_77658_a());
/*  702 */           } else if (MLN.LogChildren >= 2) {
/*  703 */             MLN.minor(this, "Failed to conceive child even with help from: " + foodConception[i].func_77658_a());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  709 */     if (!foundConceptionFood) {
/*  710 */       if (MillCommonUtilities.randomInt(6) == 0) {
/*  711 */         createChild = true;
/*  712 */         if (MLN.LogChildren >= 2) {
/*  713 */           MLN.minor(this, "Conceiving child without help.");
/*      */         }
/*  715 */       } else if (MLN.LogChildren >= 2) {
/*  716 */         MLN.minor(this, "Failed to conceive child without help.");
/*      */       }
/*      */     }
/*      */     
/*  720 */     if (MLN.DEV) {
/*  721 */       createChild = true;
/*      */     }
/*      */     
/*  724 */     if (createChild) {
/*  725 */       getHouse().createChild(this, getTownHall(), getRecord().spousesName);
/*      */     }
/*      */     
/*  728 */     return true;
/*      */   }
/*      */   
/*      */   public void calculateMerchantGoods() {
/*  732 */     for (InvItem key : this.vtype.foreignMerchantStock.keySet()) {
/*  733 */       if ((getCulture().goodsByItem.containsKey(key)) && (getBasicForeignMerchantPrice(key) > 0)) {
/*  734 */         this.merchantSells.put(getCulture().goodsByItem.get(key), Integer.valueOf(getBasicForeignMerchantPrice(key)));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean func_70692_ba()
/*      */   {
/*  741 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canMeditate() {
/*  745 */     return this.vtype.canMeditate;
/*      */   }
/*      */   
/*      */   public boolean canPerformSacrifices() {
/*  749 */     return this.vtype.canPerformSacrifices;
/*      */   }
/*      */   
/*      */   public void checkGoals() throws Exception
/*      */   {
/*  754 */     Goal goal = (Goal)Goal.goals.get(this.goalKey);
/*      */     
/*  756 */     if (goal == null) {
/*  757 */       MLN.error(this, "Invalid goal key: " + this.goalKey);
/*  758 */       this.goalKey = null;
/*  759 */       return;
/*      */     }
/*      */     
/*  762 */     if (getGoalDestEntity() != null) {
/*  763 */       if (getGoalDestEntity().field_70128_L) {
/*  764 */         setGoalDestEntity(null);
/*  765 */         setPathDestPoint(null);
/*      */       } else {
/*  767 */         setPathDestPoint(new Point(getGoalDestEntity()));
/*      */       }
/*      */     }
/*      */     
/*  771 */     Point target = null;
/*      */     
/*  773 */     boolean continuingGoal = true;
/*      */     
/*  775 */     if (getPathDestPoint() != null) {
/*  776 */       target = getPathDestPoint();
/*  777 */       if ((this.pathEntity != null) && (this.pathEntity.func_75874_d() > 0)) {
/*  778 */         target = new Point(this.pathEntity.func_75870_c());
/*      */       }
/*      */     }
/*  781 */     speakSentence(goal.sentenceKey());
/*      */     
/*  783 */     if ((getGoalDestPoint() == null) && (getGoalDestEntity() == null)) {
/*  784 */       goal.setVillagerDest(this);
/*  785 */       if ((MLN.LogGeneralAI >= 2) && (this.extraLog)) {
/*  786 */         MLN.minor(this, "Goal destination: " + getGoalDestPoint() + "/" + getGoalDestEntity());
/*      */       }
/*  788 */     } else if ((target != null) && (target.horizontalDistanceTo(this) < goal.range(this))) {
/*  789 */       if (this.actionStart == 0L) {
/*  790 */         this.stopMoving = goal.stopMovingWhileWorking();
/*  791 */         this.actionStart = System.currentTimeMillis();
/*  792 */         this.shouldLieDown = goal.shouldVillagerLieDown();
/*      */         
/*  794 */         if ((MLN.LogGeneralAI >= 2) && (this.extraLog)) {
/*  795 */           MLN.minor(this, "Starting action: " + this.actionStart);
/*      */         }
/*      */       }
/*      */       
/*  799 */       if (System.currentTimeMillis() - this.actionStart >= goal.actionDuration(this)) {
/*  800 */         if (goal.performAction(this)) {
/*  801 */           clearGoal();
/*  802 */           this.goalKey = goal.nextGoal(this);
/*  803 */           this.stopMoving = false;
/*  804 */           this.shouldLieDown = false;
/*  805 */           this.heldItem = null;
/*  806 */           continuingGoal = false;
/*  807 */           if ((MLN.LogGeneralAI >= 2) && (this.extraLog)) {
/*  808 */             MLN.minor(this, "Goal performed. Now doing: " + this.goalKey);
/*      */           }
/*      */         } else {
/*  811 */           this.stopMoving = goal.stopMovingWhileWorking();
/*      */         }
/*  813 */         this.actionStart = 0L;
/*  814 */         this.goalStarted = System.currentTimeMillis();
/*      */       }
/*      */     } else {
/*  817 */       this.stopMoving = false;
/*  818 */       this.shouldLieDown = false;
/*      */     }
/*      */     
/*  821 */     if (!continuingGoal) {
/*  822 */       return;
/*      */     }
/*      */     
/*  825 */     if (goal.isStillValid(this))
/*      */     {
/*  827 */       if (System.currentTimeMillis() - this.goalStarted > goal.stuckDelay(this))
/*      */       {
/*  829 */         boolean actionDone = goal.stuckAction(this);
/*      */         
/*  831 */         if (actionDone) {
/*  832 */           this.goalStarted = System.currentTimeMillis();
/*      */         }
/*      */         
/*  835 */         if (goal.isStillValid(this)) {
/*  836 */           this.allowRandomMoves = goal.allowRandomMoves();
/*  837 */           if (this.stopMoving) {
/*  838 */             func_70778_a(null);
/*  839 */             this.pathEntity = null;
/*      */           }
/*  841 */           if (this.heldItemCount > 20) {
/*  842 */             ItemStack[] heldItems = null;
/*  843 */             if ((target != null) && (target.horizontalDistanceTo(this) < goal.range(this))) {
/*  844 */               heldItems = goal.getHeldItemsDestination(this);
/*      */             } else {
/*  846 */               heldItems = goal.getHeldItemsTravelling(this);
/*      */             }
/*  848 */             if ((heldItems != null) && (heldItems.length > 0)) {
/*  849 */               this.heldItemId = ((this.heldItemId + 1) % heldItems.length);
/*  850 */               this.heldItem = heldItems[this.heldItemId];
/*      */             }
/*  852 */             this.heldItemCount = 0;
/*      */           }
/*      */           
/*  855 */           if ((this.heldItemCount == 0) && (goal.swingArms(this))) {
/*  856 */             func_71038_i();
/*      */           }
/*      */           
/*  859 */           this.heldItemCount += 1;
/*      */         }
/*      */       }
/*      */       else {
/*  863 */         if (this.heldItemCount > 20) {
/*  864 */           ItemStack[] heldItems = null;
/*  865 */           if ((target != null) && (target.horizontalDistanceTo(this) < goal.range(this))) {
/*  866 */             heldItems = goal.getHeldItemsDestination(this);
/*      */           } else {
/*  868 */             heldItems = goal.getHeldItemsTravelling(this);
/*      */           }
/*  870 */           if ((heldItems != null) && (heldItems.length > 0)) {
/*  871 */             this.heldItemId = ((this.heldItemId + 1) % heldItems.length);
/*  872 */             this.heldItem = heldItems[this.heldItemId];
/*      */           }
/*  874 */           this.heldItemCount = 0;
/*      */         }
/*      */         
/*  877 */         if ((this.heldItemCount == 0) && (goal.swingArms(this))) {
/*  878 */           func_71038_i();
/*      */         }
/*      */         
/*  881 */         this.heldItemCount += 1;
/*      */       }
/*      */     } else {
/*  884 */       this.stopMoving = false;
/*  885 */       this.shouldLieDown = false;
/*  886 */       goal.onComplete(this);
/*  887 */       clearGoal();
/*  888 */       this.goalKey = goal.nextGoal(this);
/*  889 */       this.heldItemCount = 21;
/*  890 */       this.heldItemId = -1;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRegistration() throws MLN.MillenaireException
/*      */   {
/*  896 */     if ((!this.registered) || (MillCommonUtilities.chanceOn(100))) {
/*  897 */       if ((getHouse() != null) && 
/*  898 */         (!getHouse().villagers.contains(this))) {
/*  899 */         getHouse().registerVillager(this);
/*  900 */         if (MLN.LogOther >= 3) {
/*  901 */           MLN.debug(this, "Registering in house List.");
/*      */         }
/*      */       }
/*      */       
/*  905 */       if ((getTownHall() != null) && 
/*  906 */         (!getTownHall().villagers.contains(this))) {
/*  907 */         getTownHall().registerVillager(this);
/*  908 */         if (MLN.LogOther >= 3) {
/*  909 */           MLN.debug(this, "Registering in TH List.");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  914 */       this.registered = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearGoal() {
/*  919 */     setGoalDestPoint(null);
/*  920 */     setGoalBuildingDestPoint(null);
/*  921 */     setGoalDestEntity(null);
/*  922 */     this.goalKey = null;
/*  923 */     this.shouldLieDown = false;
/*      */   }
/*      */   
/*      */   private boolean closeFenceGate(int i, int j, int k) {
/*  927 */     int l = this.field_70170_p.func_72805_g(i, j, k);
/*  928 */     if (BlockFenceGate.func_149896_b(l)) {
/*  929 */       MillCommonUtilities.setBlockMetadata(this.field_70170_p, i, j, k, l & 0xFFFFFFFB, true);
/*      */       
/*  931 */       return true;
/*      */     }
/*  933 */     return false;
/*      */   }
/*      */   
/*      */   public float computeMaxHealth()
/*      */   {
/*  938 */     if (this.vtype == null) {
/*  939 */       return 40.0F;
/*      */     }
/*      */     
/*  942 */     if (func_70631_g_()) {
/*  943 */       return 10 + this.size;
/*      */     }
/*      */     
/*  946 */     return this.vtype.health;
/*      */   }
/*      */   
/*      */   private List<PathPoint> computeNewPath(Point dest)
/*      */   {
/*  951 */     if (getPos().sameBlock(dest)) {
/*  952 */       return null;
/*      */     }
/*      */     
/*  955 */     if (usingCustomPathing)
/*      */     {
/*  957 */       if (MLN.jpsPathing)
/*      */       {
/*  959 */         if (this.jpsPathPlanner.isBusy()) {
/*  960 */           this.jpsPathPlanner.stopPathSearch(true);
/*      */         }
/*      */         
/*  963 */         AStarNode destNode = null;
/*      */         
/*  965 */         AStarNode[] possibles = AStarStatic.getAccessNodesSorted(this.field_70170_p, doubleToInt(this.field_70165_t), doubleToInt(this.field_70163_u), doubleToInt(this.field_70161_v), getPathDestPoint().getiX(), getPathDestPoint().getiY(), getPathDestPoint().getiZ(), getPathingConfig());
/*      */         
/*  967 */         if (possibles.length != 0) {
/*  968 */           destNode = possibles[0];
/*      */         }
/*      */         
/*  971 */         if (destNode != null) {
/*  972 */           this.pathCalculationStartTime = System.currentTimeMillis();
/*  973 */           this.jpsPathPlanner.getPath(doubleToInt(this.field_70165_t), doubleToInt(this.field_70163_u) - 1, doubleToInt(this.field_70161_v), destNode.x, destNode.y, destNode.z, getPathingConfig());
/*      */         } else {
/*  975 */           onNoPathAvailable();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  980 */         if (this.pathingWorker != null) {
/*  981 */           this.pathingWorker.interrupt();
/*      */         }
/*      */         
/*  984 */         this.pathingWorker = this.townHall.calculatePath(this, getPos(), dest, this.extraLog);
/*      */       }
/*      */       
/*  987 */       return null;
/*      */     }
/*  989 */     List<PathPoint> pp = new ArrayList();
/*      */     
/*  991 */     PathEntity pe = this.field_70170_p.func_72844_a(this, dest.getiX(), dest.getiY(), dest.getiZ(), (float)(getPos().distanceTo(dest) + 16.0D), true, false, false, true);
/*      */     
/*  993 */     if (pe == null) {
/*  994 */       return null;
/*      */     }
/*      */     
/*  997 */     for (int i = 0; i < pe.func_75874_d(); i++) {
/*  998 */       pp.add(pe.func_75877_a(i));
/*      */     }
/*      */     
/* 1001 */     return pp;
/*      */   }
/*      */   
/*      */   public int countBlocksAround(int x, int y, int z, int rx, int ry, int rz)
/*      */   {
/* 1006 */     return MillCommonUtilities.countBlocksAround(this.field_70170_p, x, y, z, rx, ry, rz);
/*      */   }
/*      */   
/*      */   public int countInv(Block block, int meta) {
/*      */     try {
/* 1011 */       return countInv(new InvItem(Item.func_150898_a(block), meta));
/*      */     } catch (MLN.MillenaireException e) {
/* 1013 */       MLN.printException(e); }
/* 1014 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int countInv(InvItem key)
/*      */   {
/* 1020 */     if (key.meta == -1) {
/* 1021 */       int nb = 0;
/* 1022 */       for (int i = 0; i < 16; i++) {
/*      */         try
/*      */         {
/* 1025 */           InvItem tkey = new InvItem(key.item, i);
/* 1026 */           if (this.inventory.containsKey(tkey)) {
/* 1027 */             nb += ((Integer)this.inventory.get(tkey)).intValue();
/*      */           }
/*      */         } catch (MLN.MillenaireException e) {
/* 1030 */           MLN.printException(e);
/*      */         }
/*      */       }
/*      */       
/* 1034 */       return nb;
/*      */     }
/*      */     
/* 1037 */     if (this.inventory.containsKey(key)) {
/* 1038 */       return ((Integer)this.inventory.get(key)).intValue();
/*      */     }
/* 1040 */     return 0;
/*      */   }
/*      */   
/*      */   public int countInv(Item item)
/*      */   {
/* 1045 */     return countInv(item, 0);
/*      */   }
/*      */   
/*      */   public int countInv(Item item, int meta) {
/*      */     try {
/* 1050 */       return countInv(new InvItem(item, meta));
/*      */     } catch (MLN.MillenaireException e) {
/* 1052 */       MLN.printException(e); }
/* 1053 */     return 0;
/*      */   }
/*      */   
/*      */   public int countItemsAround(Item item, int radius)
/*      */   {
/* 1058 */     return countItemsAround(new Item[] { item }, radius);
/*      */   }
/*      */   
/*      */   public int countItemsAround(Item[] items, int radius) {
/* 1062 */     List<Entity> list = MillCommonUtilities.getEntitiesWithinAABB(this.field_70170_p, EntityItem.class, getPos(), radius, radius);
/*      */     
/* 1064 */     int count = 0;
/*      */     
/* 1066 */     if (list != null) {
/* 1067 */       for (int i = 0; i < list.size(); i++) {
/* 1068 */         if (((Entity)list.get(i)).getClass() == EntityItem.class) {
/* 1069 */           EntityItem entity = (EntityItem)list.get(i);
/*      */           
/* 1071 */           if (!entity.field_70128_L) {
/* 1072 */             for (Item id : items) {
/* 1073 */               if (id == entity.func_92059_d().func_77973_b()) {
/* 1074 */                 count++;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1081 */     return count;
/*      */   }
/*      */   
/*      */   public void despawnVillager()
/*      */   {
/* 1086 */     if (this.field_70170_p.field_72995_K) {
/* 1087 */       return;
/*      */     }
/*      */     
/* 1090 */     if (this.hiredBy != null) {
/* 1091 */       EntityPlayer owner = this.field_70170_p.func_72924_a(this.hiredBy);
/*      */       
/* 1093 */       if (owner != null) {
/* 1094 */         ServerSender.sendTranslatedSentence(owner, '4', "hire.hiredied", new String[] { getName() });
/*      */       }
/*      */     }
/*      */     
/* 1098 */     super.func_70106_y();
/*      */   }
/*      */   
/*      */   public void despawnVillagerSilent()
/*      */   {
/* 1103 */     if (MLN.LogVillagerSpawn >= 3) {
/* 1104 */       Exception e = new Exception();
/*      */       
/* 1106 */       MLN.printException("Despawning villager: " + this, e);
/*      */     }
/*      */     
/* 1109 */     super.func_70106_y();
/*      */   }
/*      */   
/*      */   public void detrampleCrops() {
/* 1113 */     if ((getPos().sameBlock(this.prevPoint)) && ((this.previousBlock == Blocks.field_150464_aj) || ((this.previousBlock instanceof org.millenaire.common.block.BlockMillCrops))) && (getBlock(getPos()) != Blocks.field_150350_a) && (getBlock(getPos().getBelow()) == Blocks.field_150346_d))
/*      */     {
/* 1115 */       setBlock(getPos(), this.previousBlock);
/* 1116 */       setBlockMetadata(getPos(), this.previousBlockMeta);
/* 1117 */       setBlock(getPos().getBelow(), Blocks.field_150458_ak);
/*      */     }
/*      */     
/* 1120 */     this.previousBlock = getBlock(getPos());
/* 1121 */     this.previousBlockMeta = getBlockMeta(getPos());
/*      */   }
/*      */   
/*      */   public int doubleToInt(double input) {
/* 1125 */     return AStarStatic.getIntCoordFromDoubleCoord(input);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1131 */     if ((obj == null) || (!(obj instanceof MillVillager))) {
/* 1132 */       return false;
/*      */     }
/*      */     
/* 1135 */     MillVillager v = (MillVillager)obj;
/*      */     
/* 1137 */     return this.villager_id == v.villager_id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void func_70625_a(Entity par1Entity, float par2, float par3) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void faceEntityMill(Entity par1Entity, float par2, float par3)
/*      */   {
/* 1149 */     double d0 = par1Entity.field_70165_t - this.field_70165_t;
/* 1150 */     double d1 = par1Entity.field_70161_v - this.field_70161_v;
/*      */     double d2;
/*      */     double d2;
/* 1153 */     if ((par1Entity instanceof EntityLivingBase)) {
/* 1154 */       EntityLivingBase entityliving = (EntityLivingBase)par1Entity;
/* 1155 */       d2 = entityliving.field_70163_u + entityliving.func_70047_e() - (this.field_70163_u + func_70047_e());
/*      */     } else {
/* 1157 */       d2 = (par1Entity.field_70121_D.field_72338_b + par1Entity.field_70121_D.field_72337_e) / 2.0D - (this.field_70163_u + func_70047_e());
/*      */     }
/*      */     
/* 1160 */     double d3 = MathHelper.func_76133_a(d0 * d0 + d1 * d1);
/* 1161 */     float f2 = (float)(Math.atan2(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/* 1162 */     float f3 = (float)-(Math.atan2(d2, d3) * 180.0D / 3.141592653589793D);
/* 1163 */     this.field_70125_A = func_70663_b(this.field_70125_A, f3, par3);
/* 1164 */     this.field_70177_z = func_70663_b(this.field_70177_z, f2, par2);
/*      */   }
/*      */   
/*      */   public void facePoint(Point p, float par2, float par3) {
/* 1168 */     double x = p.x - this.field_70165_t;
/* 1169 */     double z = p.z - this.field_70161_v;
/* 1170 */     double y = p.y - (this.field_70163_u + func_70047_e());
/*      */     
/* 1172 */     func_70671_ap().func_75650_a(x, y, z, 10.0F, func_70646_bf());
/*      */   }
/*      */   
/*      */   private boolean foreignMerchantNightAction() {
/* 1176 */     this.foreignMerchantNbNights += 1;
/*      */     
/* 1178 */     if (this.foreignMerchantNbNights > 5) {
/* 1179 */       leaveVillage();
/*      */     } else {
/* 1181 */       boolean hasItems = false;
/* 1182 */       for (InvItem key : this.vtype.foreignMerchantStock.keySet()) {
/* 1183 */         if (getHouse().countGoods(key) > 0) {
/* 1184 */           hasItems = true;
/*      */         }
/*      */       }
/* 1187 */       if (!hasItems) {
/* 1188 */         leaveVillage();
/*      */       }
/*      */     }
/*      */     
/* 1192 */     return true;
/*      */   }
/*      */   
/*      */   private void foreignMerchantUpdate() {
/* 1196 */     if (this.foreignMerchantStallId < 0) {
/* 1197 */       for (int i = 0; (i < getHouse().getResManager().stalls.size()) && (this.foreignMerchantStallId < 0); i++) {
/* 1198 */         boolean taken = false;
/* 1199 */         for (MillVillager v : getHouse().villagers) {
/* 1200 */           if (v.foreignMerchantStallId == i) {
/* 1201 */             taken = true;
/*      */           }
/*      */         }
/* 1204 */         if (!taken) {
/* 1205 */           this.foreignMerchantStallId = i;
/*      */         }
/*      */       }
/*      */     }
/* 1209 */     if (this.foreignMerchantStallId < 0) {
/* 1210 */       this.foreignMerchantStallId = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ItemStack func_130225_q(int type)
/*      */   {
/* 1217 */     if (type == 0) {
/* 1218 */       for (Item weapon : helmets) {
/* 1219 */         if (countInv(weapon) > 0) {
/* 1220 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/* 1223 */       return null;
/*      */     }
/* 1225 */     if (type == 1) {
/* 1226 */       for (Item weapon : chestplates) {
/* 1227 */         if (countInv(weapon) > 0) {
/* 1228 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/* 1231 */       return null;
/*      */     }
/* 1233 */     if (type == 2) {
/* 1234 */       for (Item weapon : legs) {
/* 1235 */         if (countInv(weapon) > 0) {
/* 1236 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/* 1239 */       return null;
/*      */     }
/* 1241 */     if (type == 3) {
/* 1242 */       for (Item weapon : boots) {
/* 1243 */         if (countInv(weapon) > 0) {
/* 1244 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/* 1247 */       return null;
/*      */     }
/*      */     
/* 1250 */     return null;
/*      */   }
/*      */   
/*      */   public boolean gathersApples() {
/* 1254 */     return this.vtype.gathersApples;
/*      */   }
/*      */   
/*      */   public String getActionLabel(int action) {
/* 1258 */     return "none";
/*      */   }
/*      */   
/*      */   public int getAttackStrength() {
/* 1262 */     int attackStrength = this.vtype.baseAttackStrength;
/*      */     
/* 1264 */     ItemStack weapon = getWeapon();
/* 1265 */     if (weapon != null) {
/* 1266 */       attackStrength = (int)(attackStrength + Math.ceil((float)MillCommonUtilities.getItemWeaponDamage(weapon.func_77973_b()) / 2.0F));
/*      */     }
/*      */     
/* 1269 */     return attackStrength;
/*      */   }
/*      */   
/*      */   public int getBasicForeignMerchantPrice(InvItem item)
/*      */   {
/* 1274 */     if (getTownHall() == null) {
/* 1275 */       return 0;
/*      */     }
/*      */     
/* 1278 */     if (getCulture().goodsByItem.containsKey(item))
/*      */     {
/* 1280 */       if (getCulture() != getTownHall().culture) {
/* 1281 */         return (int)(((Goods)getCulture().goodsByItem.get(item)).foreignMerchantPrice * 1.5D);
/*      */       }
/* 1283 */       return ((Goods)getCulture().goodsByItem.get(item)).foreignMerchantPrice;
/*      */     }
/*      */     
/*      */ 
/* 1287 */     return 0;
/*      */   }
/*      */   
/*      */   public float getBedOrientationInDegrees()
/*      */   {
/* 1292 */     Point ref = getPos();
/*      */     
/* 1294 */     if (getGoalDestPoint() != null) {
/* 1295 */       ref = getGoalDestPoint();
/*      */     }
/*      */     
/* 1298 */     int x = (int)ref.x;
/* 1299 */     int y = (int)ref.y;
/* 1300 */     int z = (int)ref.z;
/* 1301 */     Block block = this.field_70170_p.func_147439_a(x, y, z);
/*      */     
/* 1303 */     if (block == Blocks.field_150324_C) {
/* 1304 */       int var2 = block == null ? 0 : block.getBedDirection(this.field_70170_p, x, y, z);
/*      */       
/* 1306 */       switch (var2) {
/*      */       case 0: 
/* 1308 */         return 270.0F;
/*      */       case 1: 
/* 1310 */         return 0.0F;
/*      */       case 2: 
/* 1312 */         return 90.0F;
/*      */       case 3: 
/* 1314 */         return 180.0F;
/*      */       }
/*      */     }
/*      */     else {
/* 1318 */       if (this.field_70170_p.func_147439_a(x + 1, y, z) == Blocks.field_150350_a)
/* 1319 */         return 0.0F;
/* 1320 */       if (this.field_70170_p.func_147439_a(x, y, z + 1) == Blocks.field_150350_a)
/* 1321 */         return 90.0F;
/* 1322 */       if (this.field_70170_p.func_147439_a(x - 1, y, z) == Blocks.field_150350_a)
/* 1323 */         return 180.0F;
/* 1324 */       if (this.field_70170_p.func_147439_a(x, y, z - 1) == Blocks.field_150350_a) {
/* 1325 */         return 270.0F;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1330 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public ItemTool getBestAxe() {
/* 1334 */     ItemTool bestTool = (ItemTool)Items.field_151053_p;
/* 1335 */     float bestRating = 0.0F;
/*      */     
/* 1337 */     for (InvItem item : this.inventory.keySet()) {
/* 1338 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1339 */         (item.staticStack != null) && (((item.item instanceof ItemAxe)) || ((item.item instanceof Goods.ItemMillenaireAxe)))) {
/* 1340 */         ItemTool tool = (ItemTool)item.item;
/* 1341 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150364_r) > bestRating) {
/* 1342 */           bestTool = tool;
/* 1343 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1349 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemStack[] getBestAxeStack()
/*      */   {
/* 1354 */     ItemStack[] bestTool = axeWood;
/* 1355 */     float bestRating = 0.0F;
/*      */     
/* 1357 */     for (InvItem item : this.inventory.keySet()) {
/* 1358 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1359 */         (item.staticStack != null) && (((item.item instanceof ItemAxe)) || ((item.item instanceof Goods.ItemMillenaireAxe)))) {
/* 1360 */         ItemTool tool = (ItemTool)item.item;
/* 1361 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150364_r) > bestRating) {
/* 1362 */           bestTool = item.staticStackArray;
/* 1363 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1369 */     return bestTool;
/*      */   }
/*      */   
/*      */   public Item getBestHoe() {
/* 1373 */     Item bestTool = Items.field_151017_I;
/* 1374 */     float bestRating = 0.0F;
/*      */     
/* 1376 */     for (InvItem item : this.inventory.keySet()) {
/* 1377 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1378 */         (item.staticStack != null) && (((item.item instanceof ItemHoe)) || ((item.item instanceof Goods.ItemMillenaireHoe))))
/*      */       {
/* 1380 */         if (item.item.func_77612_l() > bestRating) {
/* 1381 */           bestTool = item.item;
/* 1382 */           bestRating = item.item.func_77612_l();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1388 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemStack[] getBestHoeStack() {
/* 1392 */     ItemStack[] bestTool = hoeWood;
/* 1393 */     float bestRating = 0.0F;
/*      */     
/* 1395 */     for (InvItem item : this.inventory.keySet()) {
/* 1396 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1397 */         (item.staticStack != null) && (((item.item instanceof ItemHoe)) || ((item.item instanceof Goods.ItemMillenaireHoe))))
/*      */       {
/* 1399 */         if (item.item.func_77612_l() > bestRating) {
/* 1400 */           bestTool = item.staticStackArray;
/* 1401 */           bestRating = item.item.func_77612_l();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1407 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemTool getBestPickaxe() {
/* 1411 */     ItemTool bestTool = (ItemTool)Items.field_151039_o;
/* 1412 */     float bestRating = 0.0F;
/*      */     
/* 1414 */     for (InvItem item : this.inventory.keySet()) {
/* 1415 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1416 */         (item.staticStack != null) && (((item.item instanceof ItemPickaxe)) || ((item.item instanceof Goods.ItemMillenairePickaxe)))) {
/* 1417 */         ItemTool tool = (ItemTool)item.item;
/* 1418 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150348_b) > bestRating) {
/* 1419 */           bestTool = tool;
/* 1420 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1426 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemStack[] getBestPickaxeStack()
/*      */   {
/* 1431 */     ItemStack[] bestTool = pickaxeWood;
/* 1432 */     float bestRating = 0.0F;
/*      */     
/* 1434 */     for (InvItem item : this.inventory.keySet()) {
/* 1435 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1436 */         (item.staticStack != null) && (((item.item instanceof ItemPickaxe)) || ((item.item instanceof Goods.ItemMillenairePickaxe)))) {
/* 1437 */         ItemTool tool = (ItemTool)item.item;
/* 1438 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150348_b) > bestRating) {
/* 1439 */           bestTool = item.staticStackArray;
/* 1440 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1446 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemTool getBestShovel() {
/* 1450 */     ItemTool bestTool = (ItemTool)Items.field_151038_n;
/* 1451 */     float bestRating = 0.0F;
/*      */     
/* 1453 */     for (InvItem item : this.inventory.keySet()) {
/* 1454 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1455 */         (item.staticStack != null) && (((item.item instanceof ItemSpade)) || ((item.item instanceof Goods.ItemMillenaireShovel)))) {
/* 1456 */         ItemTool tool = (ItemTool)item.item;
/* 1457 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150346_d) > bestRating) {
/* 1458 */           bestTool = tool;
/* 1459 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1465 */     return bestTool;
/*      */   }
/*      */   
/*      */   public ItemStack[] getBestShovelStack() {
/* 1469 */     ItemStack[] bestTool = shovelWood;
/* 1470 */     float bestRating = 0.0F;
/*      */     
/* 1472 */     for (InvItem item : this.inventory.keySet()) {
/* 1473 */       if ((((Integer)this.inventory.get(item)).intValue() > 0) && 
/* 1474 */         (item.staticStack != null) && (((item.item instanceof ItemSpade)) || ((item.item instanceof Goods.ItemMillenaireShovel)))) {
/* 1475 */         ItemTool tool = (ItemTool)item.item;
/* 1476 */         if (tool.func_150893_a(item.staticStack, Blocks.field_150346_d) > bestRating) {
/* 1477 */           bestTool = item.staticStackArray;
/* 1478 */           bestRating = tool.func_150893_a(item.staticStack, Blocks.field_150348_b);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1484 */     return bestTool;
/*      */   }
/*      */   
/*      */   public Block getBlock(Point p) {
/* 1488 */     return MillCommonUtilities.getBlock(this.field_70170_p, p);
/*      */   }
/*      */   
/*      */   public int getBlockMeta(Point p) {
/* 1492 */     return MillCommonUtilities.getBlockMeta(this.field_70170_p, p);
/*      */   }
/*      */   
/*      */ 
/*      */   public float func_70783_a(int i, int j, int k)
/*      */   {
/* 1498 */     if (!this.allowRandomMoves) {
/* 1499 */       if ((MLN.LogPathing >= 3) && (this.extraLog)) {
/* 1500 */         MLN.debug(this, "Forbiding random moves. Current goal: " + Goal.goals.get(this.goalKey) + " Returning: " + -99999.0F);
/*      */       }
/* 1502 */       return Float.NEGATIVE_INFINITY;
/*      */     }
/*      */     
/* 1505 */     Point rp = new Point(i, j, k);
/* 1506 */     double dist = rp.distanceTo(this.housePoint);
/* 1507 */     if (this.field_70170_p.func_147439_a(i, j - 1, k) == Blocks.field_150458_ak)
/* 1508 */       return -50.0F;
/* 1509 */     if (dist > 10.0D) {
/* 1510 */       return -(float)dist;
/*      */     }
/* 1512 */     return MillCommonUtilities.randomInt(10);
/*      */   }
/*      */   
/*      */   public Point getClosest(List<Point> points)
/*      */   {
/* 1517 */     double bestdist = Double.MAX_VALUE;
/* 1518 */     Point bp = null;
/*      */     
/*      */ 
/* 1521 */     for (Point p : points) {
/* 1522 */       double dist = p.distanceToSquared(this);
/* 1523 */       if (dist < bestdist) {
/* 1524 */         bestdist = dist;
/* 1525 */         bp = p;
/*      */       }
/*      */     }
/* 1528 */     return bp;
/*      */   }
/*      */   
/*      */   public Point getClosestBlock(Block[] blockIds, Point pos, int rx, int ry, int rz) {
/* 1532 */     return MillCommonUtilities.getClosestBlock(this.field_70170_p, blockIds, pos, rx, ry, rz);
/*      */   }
/*      */   
/*      */   public Point getClosestBlockMeta(Block[] blockIds, int meta, Point pos, int rx, int ry, int rz) {
/* 1536 */     return MillCommonUtilities.getClosestBlockMeta(this.field_70170_p, blockIds, meta, pos, rx, ry, rz);
/*      */   }
/*      */   
/*      */   public Point getClosestHorizontal(List<Point> points) {
/* 1540 */     double bestdist = Double.MAX_VALUE;
/* 1541 */     Point bp = null;
/*      */     
/* 1543 */     for (Point p : points) {
/* 1544 */       double dist = p.horizontalDistanceToSquared(this);
/* 1545 */       if (dist < bestdist) {
/* 1546 */         bestdist = dist;
/* 1547 */         bp = p;
/*      */       }
/*      */     }
/* 1550 */     return bp;
/*      */   }
/*      */   
/*      */   public Point getClosestHorizontalWithAltitudeCost(List<Point> points, float vCost) {
/* 1554 */     double bestdist = Double.MAX_VALUE;
/* 1555 */     Point bp = null;
/*      */     
/* 1557 */     for (Point p : points) {
/* 1558 */       double dist = p.horizontalDistanceToSquared(this);
/* 1559 */       dist += Math.abs(this.townHall.getAltitude((int)this.field_70165_t, (int)this.field_70161_v) - this.townHall.getAltitude(p.getiX(), p.getiZ())) * vCost;
/*      */       
/* 1561 */       if (dist < bestdist) {
/* 1562 */         bestdist = dist;
/* 1563 */         bp = p;
/*      */       }
/*      */     }
/* 1566 */     return bp;
/*      */   }
/*      */   
/*      */   public EntityItem getClosestItemVertical(InvItem item, int radius, int vertical) {
/* 1570 */     return getClosestItemVertical(new InvItem[] { item }, radius, vertical);
/*      */   }
/*      */   
/*      */   public EntityItem getClosestItemVertical(InvItem[] items, int radius, int vertical) {
/* 1574 */     return MillCommonUtilities.getClosestItemVertical(this.field_70170_p, getPos(), items, radius, vertical);
/*      */   }
/*      */   
/*      */   public Point getClosestToHouse(List<Point> points) {
/* 1578 */     double bestdist = Double.MAX_VALUE;
/* 1579 */     Point bp = null;
/*      */     
/*      */ 
/* 1582 */     for (Point p : points) {
/* 1583 */       double dist = p.distanceToSquared(this.house.getPos());
/* 1584 */       if (dist < bestdist) {
/* 1585 */         bestdist = dist;
/* 1586 */         bp = p;
/*      */       }
/*      */     }
/* 1589 */     return bp;
/*      */   }
/*      */   
/*      */   public ResourceLocation getClothTexturePath() {
/* 1593 */     return this.clothTexture;
/*      */   }
/*      */   
/*      */   public Culture getCulture() {
/* 1597 */     if (this.vtype == null) {
/* 1598 */       return null;
/*      */     }
/* 1600 */     return this.vtype.culture;
/*      */   }
/*      */   
/*      */   public Entity func_70777_m()
/*      */   {
/* 1605 */     return this.field_70789_a;
/*      */   }
/*      */   
/*      */   public ItemStack func_71124_b(int par1)
/*      */   {
/* 1610 */     if (par1 > 0) {
/* 1611 */       return func_130225_q(par1 - 1);
/*      */     }
/*      */     
/* 1614 */     return this.heldItem;
/*      */   }
/*      */   
/*      */   protected int func_70693_a(EntityPlayer par1EntityPlayer)
/*      */   {
/* 1619 */     return this.vtype.expgiven;
/*      */   }
/*      */   
/*      */   public String getFemaleChild() {
/* 1623 */     return this.vtype.femaleChild;
/*      */   }
/*      */   
/*      */   public String getGameOccupationName(String playername)
/*      */   {
/* 1628 */     if ((getCulture() == null) || (this.vtype == null)) {
/* 1629 */       return "";
/*      */     }
/*      */     
/* 1632 */     if (!getCulture().canReadVillagerNames(playername)) {
/* 1633 */       return "";
/*      */     }
/*      */     
/* 1636 */     if ((func_70631_g_()) && (this.size == 20)) {
/* 1637 */       return getCulture().getCultureString("villager." + this.vtype.altkey);
/*      */     }
/* 1639 */     return getCulture().getCultureString("villager." + this.vtype.key);
/*      */   }
/*      */   
/*      */   public String getGameSpeech(String playername)
/*      */   {
/* 1644 */     if (getCulture() == null) {
/* 1645 */       return null;
/*      */     }
/*      */     
/* 1648 */     String speech = MillCommonUtilities.getVillagerSentence(this, playername, false);
/*      */     
/* 1650 */     if (speech != null)
/*      */     {
/* 1652 */       int duration = 10 + speech.length() / 5;
/* 1653 */       duration = Math.min(duration, 30);
/*      */       
/* 1655 */       if (this.speech_started + 20 * duration < this.field_70170_p.func_72820_D()) {
/* 1656 */         return null;
/*      */       }
/*      */     }
/*      */     
/* 1660 */     return speech;
/*      */   }
/*      */   
/*      */   public int getGatheringRange() {
/* 1664 */     return 20;
/*      */   }
/*      */   
/*      */   public String getGenderString()
/*      */   {
/* 1669 */     if (this.gender == 1) {
/* 1670 */       return "male";
/*      */     }
/* 1672 */     return "female";
/*      */   }
/*      */   
/*      */   public Building getGoalBuildingDest()
/*      */   {
/* 1677 */     return this.mw.getBuilding(getGoalBuildingDestPoint());
/*      */   }
/*      */   
/*      */   public Point getGoalBuildingDestPoint() {
/* 1681 */     if (this.goalInformation == null) {
/* 1682 */       return null;
/*      */     }
/* 1684 */     return this.goalInformation.getDestBuildingPos();
/*      */   }
/*      */   
/*      */   public Entity getGoalDestEntity() {
/* 1688 */     if (this.goalInformation == null) {
/* 1689 */       return null;
/*      */     }
/* 1691 */     return this.goalInformation.getTargetEnt();
/*      */   }
/*      */   
/*      */   public Point getGoalDestPoint() {
/* 1695 */     if (this.goalInformation == null) {
/* 1696 */       return null;
/*      */     }
/* 1698 */     return this.goalInformation.getDest();
/*      */   }
/*      */   
/*      */   public String getGoalLabel(String goal) {
/* 1702 */     if (Goal.goals.containsKey(goal)) {
/* 1703 */       return ((Goal)Goal.goals.get(goal)).gameName(this);
/*      */     }
/* 1705 */     return "none";
/*      */   }
/*      */   
/*      */   public Goal[] getGoals()
/*      */   {
/* 1710 */     if (this.vtype != null) {
/* 1711 */       return this.vtype.goals;
/*      */     }
/* 1713 */     return null;
/*      */   }
/*      */   
/*      */   public InvItem[] getGoodsToBringBackHome()
/*      */   {
/* 1718 */     return this.vtype.bringBackHomeGoods;
/*      */   }
/*      */   
/*      */   public InvItem[] getGoodsToCollect() {
/* 1722 */     return this.vtype.collectGoods;
/*      */   }
/*      */   
/*      */   public ItemStack func_70694_bm()
/*      */   {
/* 1727 */     return this.heldItem;
/*      */   }
/*      */   
/*      */   public int getHireCost(EntityPlayer player)
/*      */   {
/* 1732 */     int cost = this.vtype.hireCost;
/*      */     
/* 1734 */     if (getTownHall().controlledBy(player.getDisplayName())) {
/* 1735 */       cost /= 2;
/*      */     }
/*      */     
/* 1738 */     return cost;
/*      */   }
/*      */   
/*      */   public Building getHouse() {
/* 1742 */     if (this.house != null) {
/* 1743 */       return this.house;
/*      */     }
/* 1745 */     if ((MLN.LogVillager >= 3) && (this.extraLog)) {
/* 1746 */       MLN.debug(this, "Seeking uncached house");
/*      */     }
/* 1748 */     if (this.mw != null) {
/* 1749 */       this.house = this.mw.getBuilding(this.housePoint);
/*      */       
/* 1751 */       return this.house;
/*      */     }
/*      */     
/* 1754 */     return null;
/*      */   }
/*      */   
/*      */   public java.util.Set<InvItem> getInventoryKeys() {
/* 1758 */     return this.inventory.keySet();
/*      */   }
/*      */   
/*      */   public String getMaleChild() {
/* 1762 */     return this.vtype.maleChild;
/*      */   }
/*      */   
/*      */   public int getMerchantSellPrice(Goods g) {
/* 1766 */     return ((Integer)this.merchantSells.get(g)).intValue();
/*      */   }
/*      */   
/*      */   public String getName() {
/* 1770 */     return this.firstName + " " + this.familyName;
/*      */   }
/*      */   
/*      */   public String getNameKey() {
/* 1774 */     if (this.vtype == null) {
/* 1775 */       return "";
/*      */     }
/* 1777 */     if ((func_70631_g_()) && (this.size == 20)) {
/* 1778 */       return this.vtype.altkey;
/*      */     }
/* 1780 */     return this.vtype.key;
/*      */   }
/*      */   
/*      */   public String getNativeOccupationName() {
/* 1784 */     if (this.vtype == null) {
/* 1785 */       return null;
/*      */     }
/*      */     
/* 1788 */     if ((func_70631_g_()) && (this.size == 20)) {
/* 1789 */       return this.vtype.altname;
/*      */     }
/* 1791 */     return this.vtype.name;
/*      */   }
/*      */   
/*      */   public String getNativeSpeech(String playername)
/*      */   {
/* 1796 */     if (getCulture() == null) {
/* 1797 */       return null;
/*      */     }
/*      */     
/* 1800 */     String speech = MillCommonUtilities.getVillagerSentence(this, playername, true);
/*      */     
/* 1802 */     if (speech != null)
/*      */     {
/* 1804 */       int duration = 10 + speech.length() / 5;
/* 1805 */       duration = Math.min(duration, 30);
/*      */       
/* 1807 */       if (this.speech_started + 20 * duration < this.field_70170_p.func_72820_D()) {
/* 1808 */         return null;
/*      */       }
/*      */     }
/*      */     
/* 1812 */     return speech;
/*      */   }
/*      */   
/*      */   public int getNewGender(Building th) {
/* 1816 */     return this.vtype.gender;
/*      */   }
/*      */   
/*      */   public String getNewName() {
/* 1820 */     return getCulture().getRandomNameFromList(this.vtype.firstNameList);
/*      */   }
/*      */   
/*      */   public ResourceLocation getNewTexture()
/*      */   {
/* 1825 */     if (this.vtype != null) {
/* 1826 */       return new ResourceLocation("millenaire", this.vtype.getTexture());
/*      */     }
/* 1828 */     return null;
/*      */   }
/*      */   
/*      */   public Point getPathDestPoint()
/*      */   {
/* 1833 */     return this.pathDestPoint;
/*      */   }
/*      */   
/*      */   private AStarConfig getPathingConfig()
/*      */   {
/* 1838 */     if (this.goalKey != null) {
/* 1839 */       return ((Goal)Goal.goals.get(this.goalKey)).getPathingConfig();
/*      */     }
/*      */     
/* 1842 */     return DEFAULT_JPS_CONFIG;
/*      */   }
/*      */   
/*      */   public PathPoint getPathPointPos() {
/* 1846 */     return new PathPoint(MathHelper.func_76128_c(this.field_70121_D.field_72340_a), MathHelper.func_76128_c(this.field_70121_D.field_72338_b), MathHelper.func_76128_c(this.field_70121_D.field_72339_c));
/*      */   }
/*      */   
/*      */   public Point getPos() {
/* 1850 */     return new Point(this.field_70165_t, this.field_70163_u, this.field_70161_v);
/*      */   }
/*      */   
/*      */   public String getRandomFamilyName() {
/* 1854 */     return getCulture().getRandomNameFromList(this.vtype.familyNameList);
/*      */   }
/*      */   
/*      */   public VillagerRecord getRecord()
/*      */   {
/* 1859 */     if ((getTownHall() != null) && (getTownHall().vrecords != null)) {
/* 1860 */       for (VillagerRecord vr : getTownHall().vrecords) {
/* 1861 */         if (vr.id == this.villager_id) {
/* 1862 */           return vr;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1867 */     if ((getHouse() != null) && (getHouse().vrecords != null)) {
/* 1868 */       for (VillagerRecord vr : getHouse().vrecords) {
/* 1869 */         if (vr.id == this.villager_id) {
/* 1870 */           return vr;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1875 */     return null;
/*      */   }
/*      */   
/*      */   public MillVillager getSpouse()
/*      */   {
/* 1880 */     if ((getHouse() == null) || (func_70631_g_())) {
/* 1881 */       return null;
/*      */     }
/*      */     
/* 1884 */     for (MillVillager v : getHouse().villagers) {
/* 1885 */       if ((!v.func_70631_g_()) && (v.gender != this.gender)) {
/* 1886 */         return v;
/*      */       }
/*      */     }
/*      */     
/* 1890 */     return null;
/*      */   }
/*      */   
/*      */   public ResourceLocation getTexture() {
/* 1894 */     return this.texture;
/*      */   }
/*      */   
/*      */   public InvItem[] getToolsNeeded() {
/* 1898 */     if (this.vtype != null) {
/* 1899 */       return this.vtype.toolsNeeded;
/*      */     }
/* 1901 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int func_70658_aO()
/*      */   {
/* 1907 */     int total = 0;
/* 1908 */     for (int i = 0; i < 4; i++) {
/* 1909 */       ItemStack armour = func_130225_q(i);
/*      */       
/* 1911 */       if ((armour != null) && ((armour.func_77973_b() instanceof ItemArmor))) {
/* 1912 */         total += ((ItemArmor)armour.func_77973_b()).field_77879_b;
/*      */       }
/*      */     }
/* 1915 */     return total;
/*      */   }
/*      */   
/*      */   public Building getTownHall() {
/* 1919 */     if (this.townHall != null)
/*      */     {
/* 1921 */       return this.townHall;
/*      */     }
/*      */     
/* 1924 */     if ((MLN.LogVillager >= 3) && (this.extraLog)) {
/* 1925 */       MLN.debug(this, "Seeking uncached townHall");
/*      */     }
/*      */     
/* 1928 */     if (this.mw != null)
/*      */     {
/* 1930 */       this.townHall = this.mw.getBuilding(this.townHallPoint);
/*      */       
/* 1932 */       return this.townHall;
/*      */     }
/*      */     
/* 1935 */     return null;
/*      */   }
/*      */   
/*      */   public ItemStack getWeapon()
/*      */   {
/* 1940 */     if (this.isUsingBow) {
/* 1941 */       for (Item weapon : weaponsBow) {
/* 1942 */         if (countInv(weapon) > 0) {
/* 1943 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1948 */     if ((this.isUsingHandToHand) || (!this.vtype.isArcher)) {
/* 1949 */       for (Item weapon : weaponsHandToHand) {
/* 1950 */         if (countInv(weapon) > 0) {
/* 1951 */           return new ItemStack(weapon, 1);
/*      */         }
/*      */       }
/*      */       
/* 1955 */       if ((this.vtype != null) && (this.vtype.startingWeapon != null)) {
/* 1956 */         return new ItemStack(this.vtype.startingWeapon.getItem(), 1, this.vtype.startingWeapon.meta);
/*      */       }
/*      */     }
/*      */     
/* 1960 */     for (Item weapon : weapons) {
/* 1961 */       if (countInv(weapon) > 0) {
/* 1962 */         return new ItemStack(weapon, 1);
/*      */       }
/*      */     }
/*      */     
/* 1966 */     if ((this.vtype != null) && (this.vtype.startingWeapon != null)) {
/* 1967 */       return new ItemStack(this.vtype.startingWeapon.getItem(), 1, this.vtype.startingWeapon.meta);
/*      */     }
/*      */     
/* 1970 */     return null;
/*      */   }
/*      */   
/*      */   public void growSize()
/*      */   {
/* 1975 */     int growth = 2;
/*      */     
/* 1977 */     int nb = 0;
/*      */     
/* 1979 */     nb = getHouse().takeGoods(Items.field_151110_aK, 1);
/* 1980 */     if (nb == 1) {
/* 1981 */       growth += 1 + MillCommonUtilities.randomInt(5);
/*      */     }
/*      */     
/* 1984 */     for (int i = 0; i < foodGrowth.length; i++) {
/* 1985 */       if ((growth < 10) && (this.size + growth < 20)) {
/* 1986 */         nb = getHouse().takeGoods(foodGrowth[i], 1);
/* 1987 */         if (nb == 1) {
/* 1988 */           growth += foodGrowthValues[i] + MillCommonUtilities.randomInt(foodGrowthValues[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1993 */     this.size += growth;
/*      */     
/* 1995 */     if (this.size > 20) {
/* 1996 */       this.size = 20;
/*      */     }
/*      */     
/* 1999 */     getRecord().villagerSize = this.size;
/*      */     
/* 2001 */     adjustSize();
/* 2002 */     if (MLN.LogChildren >= 2) {
/* 2003 */       MLN.minor(this, "Child growing by " + growth + ", new size: " + this.size);
/*      */     }
/*      */   }
/*      */   
/*      */   private void handleDoorsAndFenceGates() {
/* 2008 */     if (this.doorToClose != null) {
/* 2009 */       if ((this.pathEntity == null) || (this.pathEntity.func_75874_d() == 0) || ((this.pathEntity.getPastTargetPathPoint(2) != null) && (this.doorToClose.sameBlock(this.pathEntity.getPastTargetPathPoint(2)))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2014 */         if (getBlock(this.doorToClose) == Blocks.field_150466_ao) {
/* 2015 */           int meta = getBlockMeta(this.doorToClose);
/*      */           
/* 2017 */           if ((meta & 0x4) == 4) {
/* 2018 */             toggleDoor(this.doorToClose.getiX(), this.doorToClose.getiY(), this.doorToClose.getiZ());
/*      */           }
/* 2020 */           this.doorToClose = null;
/* 2021 */         } else if (getBlock(this.doorToClose) == Blocks.field_150396_be) {
/* 2022 */           if (closeFenceGate(this.doorToClose.getiX(), this.doorToClose.getiY(), this.doorToClose.getiZ())) {
/* 2023 */             this.doorToClose = null;
/*      */           }
/*      */         } else {
/* 2026 */           this.doorToClose = null;
/*      */         }
/*      */       }
/*      */     }
/* 2030 */     else if ((this.pathEntity != null) && (this.pathEntity.func_75874_d() > 0)) {
/* 2031 */       PathPoint p = null;
/*      */       
/* 2033 */       if ((this.pathEntity.getCurrentTargetPathPoint() != null) && (this.field_70170_p.func_147439_a(this.pathEntity.getCurrentTargetPathPoint().field_75839_a, this.pathEntity.getCurrentTargetPathPoint().field_75837_b, this.pathEntity.getCurrentTargetPathPoint().field_75838_c) == Blocks.field_150466_ao))
/*      */       {
/* 2035 */         p = this.pathEntity.getCurrentTargetPathPoint();
/* 2036 */       } else if ((this.pathEntity.getNextTargetPathPoint() != null) && (this.field_70170_p.func_147439_a(this.pathEntity.getNextTargetPathPoint().field_75839_a, this.pathEntity.getNextTargetPathPoint().field_75837_b, this.pathEntity.getNextTargetPathPoint().field_75838_c) == Blocks.field_150466_ao))
/*      */       {
/* 2038 */         p = this.pathEntity.getNextTargetPathPoint();
/*      */       }
/*      */       
/* 2041 */       if (p != null) {
/* 2042 */         int meta = this.field_70170_p.func_72805_g(p.field_75839_a, p.field_75837_b, p.field_75838_c);
/* 2043 */         if ((meta & 0x4) == 0) {
/* 2044 */           toggleDoor(p.field_75839_a, p.field_75837_b, p.field_75838_c);
/* 2045 */           this.doorToClose = new Point(p);
/*      */         }
/*      */       } else {
/* 2048 */         if ((this.pathEntity.getNextTargetPathPoint() != null) && (this.field_70170_p.func_147439_a(this.pathEntity.getNextTargetPathPoint().field_75839_a, this.pathEntity.getNextTargetPathPoint().field_75837_b, this.pathEntity.getNextTargetPathPoint().field_75838_c) == Blocks.field_150396_be))
/*      */         {
/* 2050 */           p = this.pathEntity.getNextTargetPathPoint();
/* 2051 */         } else if ((this.pathEntity.getCurrentTargetPathPoint() != null) && (this.field_70170_p.func_147439_a(this.pathEntity.getCurrentTargetPathPoint().field_75839_a, this.pathEntity.getCurrentTargetPathPoint().field_75837_b, this.pathEntity.getCurrentTargetPathPoint().field_75838_c) == Blocks.field_150396_be))
/*      */         {
/* 2053 */           p = this.pathEntity.getCurrentTargetPathPoint();
/*      */         }
/*      */         
/* 2056 */         if (p != null) {
/* 2057 */           openFenceGate(p.field_75839_a, p.field_75837_b, p.field_75838_c);
/* 2058 */           this.doorToClose = new Point(p);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean hasBow()
/*      */   {
/* 2066 */     for (Item weapon : weaponsBow) {
/* 2067 */       if (countInv(weapon) > 0) {
/* 2068 */         return true;
/*      */       }
/*      */     }
/* 2071 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasChildren() {
/* 2075 */     return (this.vtype.maleChild != null) && (this.vtype.femaleChild != null);
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 2080 */     return (int)this.villager_id;
/*      */   }
/*      */   
/*      */   public boolean helpsInAttacks() {
/* 2084 */     return this.vtype.helpInAttacks;
/*      */   }
/*      */   
/*      */   public void initialise(VillagerType v, String familyName, boolean respawn) {
/* 2088 */     this.vtype = v;
/* 2089 */     this.villager_id = Math.abs(MillCommonUtilities.randomLong());
/*      */     
/* 2091 */     this.gender = v.gender;
/* 2092 */     this.firstName = getNewName();
/* 2093 */     this.familyName = familyName;
/*      */     
/* 2095 */     this.texture = getNewTexture();
/* 2096 */     func_70606_j(func_110138_aP());
/*      */     
/* 2098 */     func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(v.health);
/*      */     
/* 2100 */     updateClothTexturePath();
/*      */     
/* 2102 */     if (func_70631_g_()) {
/* 2103 */       this.size = 0;
/* 2104 */       this.scale = 0.5F;
/*      */     } else {
/* 2106 */       this.scale = (v.baseScale + (MillCommonUtilities.randomInt(10) - 5) / 100);
/*      */     }
/*      */     
/* 2109 */     if (!respawn) {
/* 2110 */       for (InvItem item : v.startingInv.keySet()) {
/* 2111 */         addToInv(item.getItem(), item.meta, ((Integer)v.startingInv.get(item)).intValue());
/*      */       }
/*      */     }
/*      */     
/* 2115 */     registerInGlobalList();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_70085_c(EntityPlayer entityplayer)
/*      */   {
/* 2121 */     if ((this.field_70170_p.field_72995_K) || (isVillagerSleeping())) {
/* 2122 */       return true;
/*      */     }
/*      */     
/* 2125 */     UserProfile profile = this.mw.getProfile(entityplayer.getDisplayName());
/*      */     
/* 2127 */     entityplayer.func_71064_a(MillAchievements.firstContact, 1);
/*      */     
/* 2129 */     if ((this.vtype != null) && ((this.vtype.key.equals("indian_sadhu")) || (this.vtype.key.equals("alchemist")))) {
/* 2130 */       entityplayer.func_71064_a(MillAchievements.maitreapenser, 1);
/*      */     }
/*      */     
/* 2133 */     if (profile.villagersInQuests.containsKey(Long.valueOf(this.villager_id))) {
/* 2134 */       Quest.QuestInstance qi = (Quest.QuestInstance)profile.villagersInQuests.get(Long.valueOf(this.villager_id));
/* 2135 */       if (qi.getCurrentVillager().id == this.villager_id) {
/* 2136 */         ServerSender.displayQuestGUI(entityplayer, this);
/*      */       } else {
/* 2138 */         interactSpecial(entityplayer);
/*      */       }
/*      */     } else {
/* 2141 */       interactSpecial(entityplayer);
/*      */     }
/*      */     
/* 2144 */     if (MLN.DEV) {
/* 2145 */       interactDev(entityplayer);
/*      */     }
/*      */     
/* 2148 */     return true;
/*      */   }
/*      */   
/*      */   public void interactDev(EntityPlayer entityplayer) {
/* 2152 */     org.millenaire.common.core.DevModUtilities.villagerInteractDev(entityplayer, this);
/*      */   }
/*      */   
/*      */   public boolean interactSpecial(EntityPlayer entityplayer)
/*      */   {
/* 2157 */     if (getTownHall() == null) {
/* 2158 */       MLN.error(this, "Trying to interact with a villager with no TH.");
/*      */     }
/*      */     
/* 2161 */     if (isChief()) {
/* 2162 */       ServerSender.displayVillageChiefGUI(entityplayer, this);
/* 2163 */       return true;
/*      */     }
/*      */     
/* 2166 */     UserProfile profile = this.mw.getProfile(entityplayer.getDisplayName());
/*      */     
/* 2168 */     if (((canMeditate()) && (this.mw.isGlobalTagSet("pujas"))) || ((canPerformSacrifices()) && (this.mw.isGlobalTagSet("mayansacrifices"))))
/*      */     {
/* 2170 */       if (MLN.LogPujas >= 3) {
/* 2171 */         MLN.debug(this, "canMeditate");
/*      */       }
/*      */       
/* 2174 */       if (getTownHall().getReputation(entityplayer.getDisplayName()) >= 64512) {
/* 2175 */         for (BuildingLocation l : getTownHall().getLocations()) {
/* 2176 */           if ((l.level >= 0) && (l.getSellingPos() != null) && (l.getSellingPos().distanceTo(this) < 8.0D)) {
/* 2177 */             Building b = l.getBuilding(this.field_70170_p);
/* 2178 */             if (b.pujas != null)
/*      */             {
/* 2180 */               if (MLN.LogPujas >= 3) {
/* 2181 */                 MLN.debug(this, "Found shrine: " + b);
/*      */               }
/*      */               
/* 2184 */               Point p = b.getPos();
/*      */               
/* 2186 */               entityplayer.openGui(Mill.instance, 6, this.field_70170_p, p.getiX(), p.getiY(), p.getiZ());
/*      */               
/* 2188 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 2193 */         ServerSender.sendTranslatedSentence(entityplayer, 'f', "ui.sellerboycott", new String[] { getName() });
/* 2194 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 2198 */     if ((isSeller()) && (!getTownHall().controlledBy(entityplayer.getDisplayName()))) {
/* 2199 */       if ((getTownHall().getReputation(entityplayer.getDisplayName()) >= 64512) && (getTownHall().chestLocked)) {
/* 2200 */         for (BuildingLocation l : getTownHall().getLocations()) {
/* 2201 */           if ((l.level >= 0) && (l.shop != null) && (l.shop.length() > 0) && (
/* 2202 */             ((l.getSellingPos() != null) && (l.getSellingPos().distanceTo(this) < 5.0D)) || (l.sleepingPos.distanceTo(this) < 5.0D))) {
/* 2203 */             ServerSender.displayVillageTradeGUI(entityplayer, l.getBuilding(this.field_70170_p));
/* 2204 */             return true;
/*      */           }
/*      */         }
/*      */       } else {
/* 2208 */         if (!getTownHall().chestLocked) {
/* 2209 */           ServerSender.sendTranslatedSentence(entityplayer, 'f', "ui.sellernotcurrently possible", new String[] { getName() });
/* 2210 */           return false;
/*      */         }
/* 2212 */         ServerSender.sendTranslatedSentence(entityplayer, 'f', "ui.sellerboycott", new String[] { getName() });
/* 2213 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 2217 */     if (isForeignMerchant()) {
/* 2218 */       ServerSender.displayMerchantTradeGUI(entityplayer, this);
/* 2219 */       return true;
/*      */     }
/*      */     
/* 2222 */     if (this.vtype.hireCost > 0) {
/* 2223 */       if ((this.hiredBy == null) || (this.hiredBy.equals(entityplayer.getDisplayName()))) {
/* 2224 */         ServerSender.displayHireGUI(entityplayer, this);
/* 2225 */         return true;
/*      */       }
/* 2227 */       ServerSender.sendTranslatedSentence(entityplayer, 'f', "hire.hiredbyotherplayer", new String[] { getName(), this.hiredBy });
/* 2228 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2232 */     if ((isLocalMerchant()) && (!profile.villagersInQuests.containsKey(Long.valueOf(this.villager_id)))) {
/* 2233 */       ServerSender.sendTranslatedSentence(entityplayer, '6', "other.localmerchantinteract", new String[] { getName(), this.hiredBy });
/* 2234 */       return false;
/*      */     }
/*      */     
/* 2237 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isChief() {
/* 2241 */     return this.vtype.isChief;
/*      */   }
/*      */   
/*      */   public boolean func_70631_g_()
/*      */   {
/* 2246 */     if (this.vtype == null) {
/* 2247 */       return false;
/*      */     }
/* 2249 */     return this.vtype.isChild;
/*      */   }
/*      */   
/*      */   public boolean isForeignMerchant() {
/* 2253 */     return this.vtype.isForeignMerchant;
/*      */   }
/*      */   
/*      */   public boolean isHostile() {
/* 2257 */     return this.vtype.hostile;
/*      */   }
/*      */   
/*      */   public boolean isLocalMerchant() {
/* 2261 */     return this.vtype.isLocalMerchant;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean func_70610_aX()
/*      */   {
/* 2269 */     return (func_110143_aJ() <= 0.0F) || (isVillagerSleeping());
/*      */   }
/*      */   
/*      */   public boolean isPriest() {
/* 2273 */     return this.vtype.isReligious;
/*      */   }
/*      */   
/*      */   public boolean isReligious() {
/* 2277 */     return this.vtype.isReligious;
/*      */   }
/*      */   
/*      */   public boolean isSeller() {
/* 2281 */     return this.vtype.canSell;
/*      */   }
/*      */   
/*      */   public boolean isTextureValid(String texture) {
/* 2285 */     if (this.vtype != null) {
/* 2286 */       return this.vtype.isTextureValid(texture);
/*      */     }
/* 2288 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isVillagerSleeping()
/*      */   {
/* 2293 */     return this.shouldLieDown;
/*      */   }
/*      */   
/*      */   public boolean isVisitor() {
/* 2297 */     if (this.vtype == null) {
/* 2298 */       return false;
/*      */     }
/* 2300 */     return this.vtype.visitor;
/*      */   }
/*      */   
/*      */   private void jumpToDest()
/*      */   {
/* 2305 */     Point jumpTo = MillCommonUtilities.findVerticalStandingPos(this.field_70170_p, getPathDestPoint());
/*      */     
/* 2307 */     if ((jumpTo != null) && (jumpTo.distanceTo(getPathDestPoint()) < 4.0D)) {
/* 2308 */       if ((MLN.LogPathing >= 1) && (this.extraLog)) {
/* 2309 */         MLN.major(this, "Jumping from " + getPos() + " to " + jumpTo);
/*      */       }
/*      */       
/* 2312 */       func_70107_b(jumpTo.getiX() + 0.5D, jumpTo.getiY() + 0.5D, jumpTo.getiZ() + 0.5D);
/*      */       
/* 2314 */       this.longDistanceStuck = 0;
/* 2315 */       this.localStuck = 0;
/*      */     }
/* 2317 */     else if ((this.goalKey != null) && (Goal.goals.containsKey(this.goalKey))) {
/* 2318 */       Goal goal = (Goal)Goal.goals.get(this.goalKey);
/*      */       try {
/* 2320 */         goal.unreachableDestination(this);
/*      */       } catch (Exception e) {
/* 2322 */         MLN.printException(this + ": Exception in handling unreachable dest for goal " + this.goalKey, e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void killVillager()
/*      */   {
/* 2329 */     if ((this.field_70170_p.field_72995_K) || (!(this.field_70170_p instanceof WorldServer))) {
/* 2330 */       super.func_70106_y();
/* 2331 */       return;
/*      */     }
/*      */     
/* 2334 */     for (InvItem iv : this.inventory.keySet()) {
/* 2335 */       if (((Integer)this.inventory.get(iv)).intValue() > 0) {
/* 2336 */         MillCommonUtilities.spawnItem(this.field_70170_p, getPos(), new ItemStack(iv.getItem(), ((Integer)this.inventory.get(iv)).intValue(), iv.meta), 0.0F);
/*      */       }
/*      */     }
/*      */     
/* 2340 */     if (this.hiredBy != null) {
/* 2341 */       EntityPlayer owner = this.field_70170_p.func_72924_a(this.hiredBy);
/*      */       
/* 2343 */       if (owner != null) {
/* 2344 */         ServerSender.sendTranslatedSentence(owner, 'f', "hire.hiredied", new String[] { getName() });
/*      */       }
/*      */     }
/*      */     
/* 2348 */     if (getTownHall() != null) {
/* 2349 */       VillagerRecord vr = getTownHall().getVillagerRecordById(this.villager_id);
/*      */       
/* 2351 */       if (vr != null) {
/* 2352 */         if (MLN.LogGeneralAI >= 1) {
/* 2353 */           MLN.major(this, getTownHall() + ": Villager has been killed!");
/*      */         }
/* 2355 */         vr.killed = true;
/*      */       }
/*      */     }
/*      */     
/* 2359 */     super.func_70106_y();
/*      */   }
/*      */   
/*      */   private void leaveVillage()
/*      */   {
/* 2364 */     for (InvItem iv : this.vtype.foreignMerchantStock.keySet()) {
/* 2365 */       getHouse().takeGoods(iv.getItem(), iv.meta, ((Integer)this.vtype.foreignMerchantStock.get(iv)).intValue());
/*      */     }
/*      */     
/* 2368 */     getTownHall().deleteVillager(this);
/* 2369 */     getTownHall().removeVillagerRecord(this.villager_id);
/* 2370 */     getHouse().deleteVillager(this);
/* 2371 */     getHouse().removeVillagerRecord(this.villager_id);
/*      */     
/* 2373 */     despawnVillager();
/*      */   }
/*      */   
/*      */   public void localMerchantUpdate() throws Exception {
/* 2377 */     if ((getHouse() != null) && (getHouse() == getTownHall()))
/*      */     {
/* 2379 */       List<Building> buildings = getTownHall().getBuildingsWithTag("inn");
/* 2380 */       Building inn = null;
/*      */       
/* 2382 */       for (Building building : buildings) {
/* 2383 */         if (building.merchantRecord == null) {
/* 2384 */           inn = building;
/*      */         }
/*      */       }
/*      */       
/* 2388 */       if (inn == null) {
/* 2389 */         getHouse().removeVillagerRecord(this.villager_id);
/* 2390 */         getHouse().deleteVillager(this);
/* 2391 */         despawnVillager();
/* 2392 */         MLN.error(this, "Merchant had Town Hall as house and inn is full. Killing him.");
/*      */       } else {
/* 2394 */         setHousePoint(inn.getPos());
/* 2395 */         getHouse().addOrReplaceVillager(this);
/* 2396 */         getTownHall().removeVillagerRecord(this.villager_id);
/* 2397 */         VillagerRecord vr = new VillagerRecord(this.mw, this);
/* 2398 */         getHouse().addOrReplaceRecord(vr);
/* 2399 */         getTownHall().addOrReplaceRecord(vr);
/* 2400 */         MLN.error(this, "Merchant had Town Hall as house. Moving him to the inn.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void onFoundPath(List<AStarNode> result)
/*      */   {
/* 2407 */     this.pathCalculatedSinceLastTick = result;
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70636_d()
/*      */   {
/* 2413 */     super.func_70636_d();
/*      */     
/* 2415 */     func_82168_bl();
/*      */     
/* 2417 */     setFacingDirection();
/*      */     
/* 2419 */     if (isVillagerSleeping()) {
/* 2420 */       this.field_70159_w = 0.0D;
/* 2421 */       this.field_70181_x = 0.0D;
/* 2422 */       this.field_70179_y = 0.0D;
/*      */     }
/*      */   }
/*      */   
/*      */   public void onNoPathAvailable()
/*      */   {
/* 2428 */     this.pathFailedSincelastTick = true;
/* 2429 */     if (MLN.LogPathing >= 2) {
/* 2430 */       MLN.minor(this, "No path found between " + getPos() + " and " + getPathDestPoint() + " in " + (System.currentTimeMillis() - this.pathCalculationStartTime));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70071_h_()
/*      */   {
/*      */     try
/*      */     {
/* 2439 */       if (this.vtype == null) {
/* 2440 */         if (!this.field_70128_L) {
/* 2441 */           MLN.error(this, "Unknown villager type. Killing him.");
/* 2442 */           despawnVillagerSilent();
/*      */         }
/* 2444 */         return;
/*      */       }
/*      */       
/* 2447 */       registerInGlobalList();
/*      */       
/* 2449 */       if (this.pathFailedSincelastTick) {
/* 2450 */         pathFailedSinceLastTick();
/*      */       }
/*      */       
/* 2453 */       if (this.pathCalculatedSinceLastTick != null) {
/* 2454 */         applyPathCalculatedSinceLastTick();
/*      */       }
/*      */       
/* 2457 */       if ((MLN.DEV) && 
/* 2458 */         (this.goalKey != null))
/*      */       {
/* 2460 */         Goal goal = (Goal)Goal.goals.get(this.goalKey);
/*      */         
/* 2462 */         if ((getPathDestPoint() == null) || (this.jpsPathPlanner.isBusy()) || (this.pathEntity != null) || (this.stopMoving) || (this.goalKey == null) || (this.goalKey.equals("gorest")) || (getPathDestPoint().distanceTo(getPos()) <= goal.range(this))) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2470 */       if (this.field_70170_p.field_72995_K) {
/* 2471 */         super.func_70071_h_();
/*      */         try
/*      */         {
/* 2474 */           checkRegistration();
/*      */         } catch (MLN.MillenaireException e) {
/* 2476 */           MLN.printException(getName(), e);
/*      */         }
/* 2478 */         return;
/*      */       }
/*      */       
/* 2481 */       long startTime = System.nanoTime();
/*      */       
/* 2483 */       if (this.field_70128_L) {
/* 2484 */         super.func_70071_h_();
/* 2485 */         return;
/*      */       }
/*      */       
/* 2488 */       if (this.hiredBy != null) {
/* 2489 */         updateHired();
/* 2490 */         super.func_70071_h_();
/* 2491 */         return;
/*      */       }
/*      */       
/* 2494 */       if ((getTownHall() == null) || (getHouse() == null)) {
/* 2495 */         return;
/*      */       }
/*      */       
/* 2498 */       if ((getTownHall() != null) && (!getTownHall().isActive)) {
/* 2499 */         return;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 2504 */         this.timer += 1L;
/*      */         
/* 2506 */         if (((func_110143_aJ() < func_110138_aP() ? 1 : 0) & (MillCommonUtilities.randomInt(1600) == 0 ? 1 : 0)) != 0) {
/* 2507 */           func_70606_j(func_110143_aJ() + 1.0F);
/*      */         }
/*      */         
/* 2510 */         detrampleCrops();
/*      */         
/* 2512 */         this.allowRandomMoves = true;
/*      */         
/* 2514 */         if ((getTownHall() == null) || (getHouse() == null)) {
/* 2515 */           super.func_70071_h_();
/* 2516 */           return;
/*      */         }
/*      */         
/* 2519 */         checkRegistration();
/*      */         
/* 2521 */         if (Goal.beSeller.key.equals(this.goalKey)) {
/* 2522 */           this.townHall.seller = this;
/* 2523 */         } else if ((Goal.getResourcesForBuild.key.equals(this.goalKey)) || (Goal.construction.key.equals(this.goalKey))) {
/* 2524 */           if (MLN.LogTileEntityBuilding >= 3) {
/* 2525 */             MLN.debug(this, "Registering as builder for: " + this.townHall);
/*      */           }
/* 2527 */           this.townHall.builder = this;
/*      */         }
/*      */         
/* 2530 */         if (getTownHall().underAttack)
/*      */         {
/* 2532 */           if ((this.goalKey == null) || ((!this.goalKey.equals(Goal.raidVillage.key)) && (!this.goalKey.equals(Goal.defendVillage.key)) && (!this.goalKey.equals(Goal.hide.key))))
/*      */           {
/*      */ 
/*      */ 
/* 2536 */             clearGoal();
/*      */           }
/* 2538 */           if (this.isRaider) {
/* 2539 */             this.goalKey = Goal.raidVillage.key;
/* 2540 */             targetDefender();
/* 2541 */           } else if (helpsInAttacks()) {
/* 2542 */             this.goalKey = Goal.defendVillage.key;
/* 2543 */             targetRaider();
/*      */           } else {
/* 2545 */             this.goalKey = Goal.hide.key;
/*      */           }
/* 2547 */           checkGoals();
/*      */         }
/*      */         
/* 2550 */         if (this.field_70789_a != null)
/*      */         {
/* 2552 */           if ((this.vtype.isDefensive) && (getPos().distanceTo(getHouse().getResManager().getDefendingPos()) > 20.0D)) {
/* 2553 */             this.field_70789_a = null;
/* 2554 */           } else if ((!this.field_70789_a.func_70089_S()) || (getPos().distanceTo(this.field_70789_a) > 80.0D) || ((this.field_70170_p.field_73013_u == EnumDifficulty.PEACEFUL) && ((this.field_70789_a instanceof EntityPlayer))))
/*      */           {
/* 2556 */             this.field_70789_a = null;
/*      */           }
/*      */           
/* 2559 */           if (this.field_70789_a != null) {
/* 2560 */             this.shouldLieDown = false;
/*      */           }
/*      */           
/*      */         }
/* 2564 */         else if ((isHostile()) && (this.field_70170_p.field_73013_u != EnumDifficulty.PEACEFUL) && (getTownHall().closestPlayer != null) && (getPos().distanceTo(getTownHall().closestPlayer) <= 80.0D)) {
/* 2565 */           int range = 80;
/*      */           
/* 2567 */           if (this.vtype.isDefensive) {
/* 2568 */             range = 20;
/*      */           }
/*      */           
/* 2571 */           this.field_70789_a = this.field_70170_p.func_72977_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, range);
/* 2572 */           clearGoal();
/*      */         }
/*      */         
/*      */ 
/* 2576 */         if (this.field_70789_a != null) {
/* 2577 */           setGoalDestPoint(new Point(this.field_70789_a));
/* 2578 */           this.heldItem = getWeapon();
/*      */           
/* 2580 */           if ((this.goalKey != null) && 
/* 2581 */             (!((Goal)Goal.goals.get(this.goalKey)).isFightingGoal())) {
/* 2582 */             clearGoal();
/*      */           }
/*      */           
/*      */         }
/* 2586 */         else if (!getTownHall().underAttack)
/*      */         {
/* 2588 */           if (this.field_70170_p.func_72935_r())
/*      */           {
/* 2590 */             speakSentence("greeting", 12000, 3, 10);
/*      */             
/* 2592 */             this.nightActionPerformed = false;
/*      */             
/* 2594 */             InvItem[] goods = getGoodsToCollect();
/*      */             
/* 2596 */             if (goods != null) {
/* 2597 */               EntityItem item = getClosestItemVertical(goods, 3, 30);
/* 2598 */               if (item != null) {
/* 2599 */                 item.func_70106_y();
/* 2600 */                 if (item.func_92059_d().func_77973_b() == Item.func_150898_a(Blocks.field_150345_g)) {
/* 2601 */                   addToInv(item.func_92059_d().func_77973_b(), item.func_92059_d().func_77960_j() & 0x3, 1);
/*      */                 } else {
/* 2603 */                   addToInv(item.func_92059_d().func_77973_b(), item.func_92059_d().func_77960_j(), 1);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 2608 */             specificUpdate();
/*      */             
/* 2610 */             if (!this.isRaider) {
/* 2611 */               if (this.goalKey == null) {
/* 2612 */                 setNextGoal();
/*      */               }
/* 2614 */               if (this.goalKey != null) {
/* 2615 */                 checkGoals();
/*      */               } else {
/* 2617 */                 this.shouldLieDown = false;
/*      */               }
/*      */             }
/*      */           } else {
/* 2621 */             this.hasPrayedToday = false;
/* 2622 */             this.hasDrunkToday = false;
/*      */             
/* 2624 */             if (!this.isRaider) {
/* 2625 */               if (this.goalKey == null) {
/* 2626 */                 setNextGoal();
/*      */               }
/* 2628 */               if (this.goalKey != null) {
/* 2629 */                 checkGoals();
/*      */               } else {
/* 2631 */                 this.shouldLieDown = false;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 2637 */         if ((getPathDestPoint() != null) && (this.pathEntity != null) && (this.pathEntity.func_75874_d() > 0) && (!this.stopMoving))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2642 */           double olddistance = this.prevPoint.horizontalDistanceToSquared(getPathDestPoint());
/* 2643 */           double newdistance = getPos().horizontalDistanceToSquared(getPathDestPoint());
/*      */           
/* 2645 */           if (olddistance - newdistance < 2.0E-4D) {
/* 2646 */             this.longDistanceStuck += 1;
/*      */           } else {
/* 2648 */             this.longDistanceStuck -= 1;
/*      */           }
/*      */           
/* 2651 */           if (this.longDistanceStuck < 0) {
/* 2652 */             this.longDistanceStuck = 0;
/*      */           }
/* 2654 */           if ((this.pathEntity != null) && (this.pathEntity.func_75874_d() > 1) && 
/* 2655 */             (MLN.LogPathing >= 2) && (this.extraLog)) {
/* 2656 */             MLN.minor(this, "Stuck: " + this.longDistanceStuck + " pos " + getPos() + " node: " + this.pathEntity.getCurrentTargetPathPoint() + " next node: " + this.pathEntity.getNextTargetPathPoint() + " dest: " + getPathDestPoint());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2662 */           if ((this.longDistanceStuck > 3000) && ((!this.vtype.noTeleport) || ((getRecord() != null) && (getRecord().raidingVillage))))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2668 */             jumpToDest();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2674 */           PathPoint nextPoint = this.pathEntity.getNextTargetPathPoint();
/*      */           
/* 2676 */           if (nextPoint != null)
/*      */           {
/* 2678 */             olddistance = this.prevPoint.distanceToSquared(nextPoint);
/* 2679 */             newdistance = getPos().distanceToSquared(nextPoint);
/*      */             
/* 2681 */             if (olddistance - newdistance < 2.0E-4D) {
/* 2682 */               this.localStuck += 4;
/*      */             } else {
/* 2684 */               this.localStuck -= 1;
/*      */             }
/*      */             
/* 2687 */             if (this.localStuck < 0) {
/* 2688 */               this.localStuck = 0;
/*      */             }
/*      */             
/* 2691 */             if (this.localStuck > 30)
/*      */             {
/*      */ 
/*      */ 
/* 2695 */               func_70778_a(null);
/* 2696 */               this.pathEntity = null;
/*      */             }
/*      */             
/* 2699 */             if (this.localStuck > 100)
/*      */             {
/*      */ 
/*      */ 
/* 2703 */               func_70107_b(nextPoint.field_75839_a + 0.5D, nextPoint.field_75837_b + 0.5D, nextPoint.field_75838_c + 0.5D);
/* 2704 */               this.localStuck = 0;
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/* 2709 */           this.longDistanceStuck = 0;
/* 2710 */           this.localStuck = 0;
/*      */         }
/*      */         
/* 2713 */         if ((getPathDestPoint() != null) && (!this.stopMoving)) {
/* 2714 */           updatePathIfNeeded(getPathDestPoint());
/*      */         }
/* 2716 */         if ((this.stopMoving) || (this.jpsPathPlanner.isBusy())) {
/* 2717 */           func_70778_a(null);
/* 2718 */           this.pathEntity = null;
/*      */         }
/*      */         
/* 2721 */         this.prevPoint = getPos();
/*      */         
/* 2723 */         handleDoorsAndFenceGates();
/*      */         
/* 2725 */         if (System.currentTimeMillis() - this.timeSinceLastPathingTimeDisplay > 10000L) {
/* 2726 */           if (this.pathingTime > 500L) {
/* 2727 */             if (getPathDestPoint() != null) {
/* 2728 */               MLN.warning(this, "Pathing time in last 10 secs: " + this.pathingTime + " dest: " + getPathDestPoint() + " dest bid: " + MillCommonUtilities.getBlock(this.field_70170_p, getPathDestPoint()) + " above bid: " + MillCommonUtilities.getBlock(this.field_70170_p, getPathDestPoint().getAbove()));
/*      */             }
/*      */             else
/*      */             {
/* 2732 */               MLN.warning(this, "Pathing time in last 10 secs: " + this.pathingTime + " null dest point.");
/*      */             }
/*      */             
/* 2735 */             MLN.warning(this, "nbPathsCalculated: " + this.nbPathsCalculated + " nbPathNoStart: " + this.nbPathNoStart + " nbPathNoEnd: " + this.nbPathNoEnd + " nbPathAborted: " + this.nbPathAborted + " nbPathFailure: " + this.nbPathFailure);
/*      */             
/* 2737 */             String s = "";
/* 2738 */             for (org.millenaire.common.pathing.AStarPathing.PathKey p : this.abortedKeys) {
/* 2739 */               s = s + p + "     ";
/*      */             }
/* 2741 */             MLN.warning(this, "Aborted keys: " + s);
/*      */             
/* 2743 */             if (this.goalKey != null) {
/* 2744 */               MLN.warning(this, "Current goal: " + Goal.goals.get(this.goalKey));
/*      */             }
/*      */           }
/*      */           
/* 2748 */           this.timeSinceLastPathingTimeDisplay = System.currentTimeMillis();
/* 2749 */           this.pathingTime = 0L;
/* 2750 */           this.nbPathsCalculated = 0;
/* 2751 */           this.nbPathNoStart = 0;
/* 2752 */           this.nbPathNoEnd = 0;
/* 2753 */           this.nbPathAborted = 0;
/* 2754 */           this.nbPathFailure = 0;
/* 2755 */           this.abortedKeys.clear();
/*      */         }
/*      */         
/* 2758 */         sendVillagerPacket();
/*      */       }
/*      */       catch (MLN.MillenaireException e) {
/* 2761 */         Mill.proxy.sendChatAdmin(getName() + ": Error in onUpdate(). Check millenaire.log.");
/* 2762 */         MLN.error(this, e.getMessage());
/*      */       } catch (Exception e) {
/* 2764 */         Mill.proxy.sendChatAdmin(getName() + ": Error in onUpdate(). Check millenaire.log.");
/* 2765 */         MLN.error(this, "Exception in Villager.onUpdate(): ");
/* 2766 */         MLN.printException(e);
/*      */       }
/*      */       
/* 2769 */       triggerMobAttacks();
/*      */       
/* 2771 */       updateDialogue();
/*      */       
/* 2773 */       this.isUsingBow = false;
/*      */       
/*      */ 
/* 2776 */       this.isUsingHandToHand = false;
/*      */       
/* 2778 */       for (int i = 0; i < 5; i++) {
/* 2779 */         if ((func_71124_b(i) != null) && (func_71124_b(i).func_77973_b() == null)) {
/* 2780 */           MLN.printException("ItemStack with null item for villager " + this + ", goal: " + this.goalKey, new Exception());
/*      */         }
/*      */       }
/*      */       
/* 2784 */       super.func_70071_h_();
/*      */       
/* 2786 */       double timeInMl = (System.nanoTime() - startTime) / 1000000.0D;
/*      */       
/* 2788 */       if (MLN.DEV) {
/* 2789 */         getTownHall().monitor.addToGoal(this.goalKey, timeInMl);
/*      */         
/* 2791 */         if (((getPathDestPoint() != null) && (!this.jpsPathPlanner.isBusy()) && (this.pathEntity == null)) && (
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2796 */           (getPathDestPoint() == null) || (getGoalDestPoint() == null) || (getPathDestPoint().distanceTo(getGoalDestPoint()) <= 20.0D))) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2805 */       return;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 2803 */       MLN.printException("Exception in onUpdate() of villager: " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean openFenceGate(int i, int j, int k) {
/* 2808 */     int l = this.field_70170_p.func_72805_g(i, j, k);
/* 2809 */     if (!BlockFenceGate.func_149896_b(l)) {
/* 2810 */       int i1 = (MathHelper.func_76128_c(this.field_70177_z * 4.0F / 360.0F + 0.5D) & 0x3) % 4;
/* 2811 */       int j1 = net.minecraft.block.BlockDirectional.func_149895_l(l);
/* 2812 */       if (j1 == (i1 + 2) % 4) {
/* 2813 */         l = i1;
/*      */       }
/* 2815 */       MillCommonUtilities.setBlockMetadata(this.field_70170_p, i, j, k, l | 0x4, true);
/*      */     }
/*      */     
/*      */ 
/* 2819 */     return true;
/*      */   }
/*      */   
/*      */   private void pathFailedSinceLastTick() {
/* 2823 */     if ((this.pathfailure >= 20) && ((!this.vtype.noTeleport) || ((getRecord() != null) && (getRecord().raidingVillage)))) {
/* 2824 */       jumpToDest();
/* 2825 */       this.pathfailure = 0;
/*      */     } else {
/* 2827 */       this.pathfailure += 1;
/* 2828 */       Point p = MillCommonUtilities.findRandomStandingPosAround(this.field_70170_p, getPathDestPoint());
/* 2829 */       this.jpsPathPlanner.stopPathSearch(true);
/* 2830 */       if (p != null) {
/* 2831 */         computeNewPath(p);
/*      */       } else {
/* 2833 */         if ((!this.vtype.noTeleport) || ((getRecord() != null) && (getRecord().raidingVillage))) {
/* 2834 */           jumpToDest();
/*      */         }
/* 2836 */         this.pathfailure = 0;
/*      */       }
/*      */     }
/* 2839 */     this.pathFailedSincelastTick = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean performNightAction()
/*      */   {
/* 2847 */     if ((getRecord() == null) || (getHouse() == null) || (getTownHall() == null)) {
/* 2848 */       return false;
/*      */     }
/*      */     
/* 2851 */     if (func_70631_g_()) {
/* 2852 */       if (this.size < 20) {
/* 2853 */         growSize();
/*      */       } else {
/* 2855 */         teenagerNightAction();
/*      */       }
/*      */     }
/*      */     
/* 2859 */     if (isForeignMerchant()) {
/* 2860 */       foreignMerchantNightAction();
/*      */     }
/*      */     
/* 2863 */     if (hasChildren()) {
/* 2864 */       return attemptChildConception();
/*      */     }
/*      */     
/* 2867 */     return true;
/*      */   }
/*      */   
/*      */   public int putInBuilding(Building building, Item item, int nb) {
/* 2871 */     return putInBuilding(building, item, 0, nb);
/*      */   }
/*      */   
/*      */   public int putInBuilding(Building building, Item item, int meta, int nb) {
/* 2875 */     nb = takeFromInv(item, meta, nb);
/* 2876 */     building.storeGoods(item, meta, nb);
/*      */     
/* 2878 */     return nb;
/*      */   }
/*      */   
/*      */   public void func_70037_a(NBTTagCompound nbttagcompound)
/*      */   {
/* 2883 */     super.func_70037_a(nbttagcompound);
/*      */     
/* 2885 */     String type = nbttagcompound.func_74779_i("vtype");
/* 2886 */     String culture = nbttagcompound.func_74779_i("culture");
/*      */     
/* 2888 */     if (Culture.getCultureByName(culture) != null) {
/* 2889 */       if (Culture.getCultureByName(culture).getVillagerType(type) != null) {
/* 2890 */         this.vtype = Culture.getCultureByName(culture).getVillagerType(type);
/*      */       } else {
/* 2892 */         MLN.error(this, "Could not load dynamic NPC: unknown type: " + type + " in culture: " + culture);
/*      */       }
/*      */     } else {
/* 2895 */       MLN.error(this, "Could not load dynamic NPC: unknown culture: " + culture);
/*      */     }
/*      */     
/* 2898 */     this.texture = new ResourceLocation("millenaire", nbttagcompound.func_74779_i("texture"));
/* 2899 */     this.housePoint = Point.read(nbttagcompound, "housePos");
/* 2900 */     if (this.housePoint == null) {
/* 2901 */       MLN.error(this, "Error when loading villager: housePoint null");
/* 2902 */       Mill.proxy.sendChatAdmin(getName() + ": Could not load house position. Check millenaire.log");
/*      */     }
/*      */     
/* 2905 */     this.townHallPoint = Point.read(nbttagcompound, "townHallPos");
/*      */     
/* 2907 */     if (this.townHallPoint == null) {
/* 2908 */       MLN.error(this, "Error when loading villager: townHallPoint null");
/* 2909 */       Mill.proxy.sendChatAdmin(getName() + ": Could not load town hall position. Check millenaire.log");
/*      */     }
/*      */     
/* 2912 */     setGoalDestPoint(Point.read(nbttagcompound, "destPoint"));
/* 2913 */     setPathDestPoint(Point.read(nbttagcompound, "pathDestPoint"));
/* 2914 */     setGoalBuildingDestPoint(Point.read(nbttagcompound, "destBuildingPoint"));
/* 2915 */     this.prevPoint = Point.read(nbttagcompound, "prevPoint");
/* 2916 */     this.doorToClose = Point.read(nbttagcompound, "doorToClose");
/* 2917 */     this.action = nbttagcompound.func_74762_e("action");
/* 2918 */     this.goalKey = nbttagcompound.func_74779_i("goal");
/*      */     
/* 2920 */     if (this.goalKey.trim().length() == 0) {
/* 2921 */       this.goalKey = null;
/*      */     }
/*      */     
/* 2924 */     if ((this.goalKey != null) && (!Goal.goals.containsKey(this.goalKey))) {
/* 2925 */       this.goalKey = null;
/*      */     }
/*      */     
/* 2928 */     this.dialogueKey = nbttagcompound.func_74779_i("dialogueKey");
/* 2929 */     this.dialogueStart = nbttagcompound.func_74763_f("dialogueStart");
/* 2930 */     this.dialogueRole = nbttagcompound.func_74762_e("dialogueRole");
/* 2931 */     this.dialogueColour = ((char)nbttagcompound.func_74762_e("dialogueColour"));
/* 2932 */     this.dialogueChat = nbttagcompound.func_74767_n("dialogueChat");
/*      */     
/* 2934 */     if (this.dialogueKey.trim().length() == 0) {
/* 2935 */       this.dialogueKey = null;
/*      */     }
/*      */     
/* 2938 */     this.familyName = nbttagcompound.func_74779_i("familyName");
/* 2939 */     this.firstName = nbttagcompound.func_74779_i("firstName");
/* 2940 */     this.scale = nbttagcompound.func_74760_g("scale");
/* 2941 */     this.gender = nbttagcompound.func_74762_e("gender");
/*      */     
/*      */ 
/* 2944 */     if (nbttagcompound.func_74764_b("villager_lid")) {
/* 2945 */       this.villager_id = Math.abs(nbttagcompound.func_74763_f("villager_lid"));
/*      */     }
/*      */     
/* 2948 */     if (!isTextureValid(this.texture.func_110623_a())) {
/* 2949 */       this.texture = getNewTexture();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2955 */     NBTTagList nbttaglist = nbttagcompound.func_150295_c("inventory", 10);
/* 2956 */     for (int i = 0; i < nbttaglist.func_74745_c(); i++) {
/* 2957 */       NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
/*      */       
/* 2959 */       int itemID = nbttagcompound1.func_74762_e("item");
/* 2960 */       int itemMeta = nbttagcompound1.func_74762_e("meta");
/*      */       
/*      */ 
/* 2963 */       if (itemID == Block.func_149682_b(Blocks.field_150364_r)) {
/* 2964 */         itemMeta &= 0x3;
/*      */       }
/*      */       try
/*      */       {
/* 2968 */         this.inventory.put(new InvItem(Item.func_150899_d(itemID), itemMeta), Integer.valueOf(nbttagcompound1.func_74762_e("amount")));
/*      */       } catch (MLN.MillenaireException e) {
/* 2970 */         MLN.printException(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2977 */     nbttaglist = nbttagcompound.func_150295_c("inventoryNew", 10);
/* 2978 */     MillCommonUtilities.readInventory(nbttaglist, this.inventory);
/*      */     
/* 2980 */     this.previousBlock = Block.func_149729_e(nbttagcompound.func_74762_e("previousBlock"));
/* 2981 */     this.previousBlockMeta = nbttagcompound.func_74762_e("previousBlockMeta");
/* 2982 */     this.size = nbttagcompound.func_74762_e("size");
/*      */     
/* 2984 */     this.hasPrayedToday = nbttagcompound.func_74767_n("hasPrayedToday");
/* 2985 */     this.hasDrunkToday = nbttagcompound.func_74767_n("hasDrunkToday");
/*      */     
/* 2987 */     this.hiredBy = nbttagcompound.func_74779_i("hiredBy");
/* 2988 */     this.hiredUntil = nbttagcompound.func_74763_f("hiredUntil");
/* 2989 */     this.aggressiveStance = nbttagcompound.func_74767_n("aggressiveStance");
/* 2990 */     this.isRaider = nbttagcompound.func_74767_n("isRaider");
/*      */     
/* 2992 */     if (this.hiredBy.equals("")) {
/* 2993 */       this.hiredBy = null;
/*      */     }
/*      */     
/* 2996 */     this.clothName = nbttagcompound.func_74779_i("clothName");
/* 2997 */     this.clothTexture = new ResourceLocation("millenaire", nbttagcompound.func_74779_i("clothTexture"));
/*      */     
/* 2999 */     if (this.clothName.equals("")) {
/* 3000 */       this.clothName = null;
/* 3001 */       this.clothTexture = null;
/*      */     }
/*      */     
/* 3004 */     registerInGlobalList();
/* 3005 */     updateClothTexturePath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void readSpawnData(ByteBuf ds)
/*      */   {
/* 3012 */     ByteBufInputStream data = new ByteBufInputStream(ds);
/*      */     try {
/* 3014 */       this.villager_id = data.readLong();
/* 3015 */       readVillagerStreamdata(data);
/* 3016 */       registerInGlobalList();
/*      */     } catch (IOException e) {
/* 3018 */       MLN.printException("Error in readSpawnData for villager " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void readVillagerStreamdata(DataInput data)
/*      */     throws IOException
/*      */   {
/* 3025 */     Culture culture = Culture.getCultureByName(StreamReadWrite.readNullableString(data));
/*      */     
/* 3027 */     String vt = StreamReadWrite.readNullableString(data);
/* 3028 */     if (culture != null) {
/* 3029 */       this.vtype = culture.getVillagerType(vt);
/*      */     }
/*      */     
/* 3032 */     this.texture = StreamReadWrite.readNullableResourceLocation(data);
/*      */     
/* 3034 */     this.goalKey = StreamReadWrite.readNullableString(data);
/* 3035 */     this.housePoint = StreamReadWrite.readNullablePoint(data);
/* 3036 */     this.townHallPoint = StreamReadWrite.readNullablePoint(data);
/* 3037 */     this.firstName = StreamReadWrite.readNullableString(data);
/* 3038 */     this.familyName = StreamReadWrite.readNullableString(data);
/*      */     
/* 3040 */     this.scale = data.readFloat();
/* 3041 */     this.gender = data.readInt();
/* 3042 */     this.size = data.readInt();
/*      */     
/* 3044 */     this.hiredBy = StreamReadWrite.readNullableString(data);
/* 3045 */     this.aggressiveStance = data.readBoolean();
/* 3046 */     this.hiredUntil = data.readLong();
/* 3047 */     this.isUsingBow = data.readBoolean();
/* 3048 */     this.isUsingHandToHand = data.readBoolean();
/* 3049 */     this.speech_key = StreamReadWrite.readNullableString(data);
/* 3050 */     this.speech_variant = data.readInt();
/* 3051 */     this.speech_started = data.readLong();
/* 3052 */     this.heldItem = StreamReadWrite.readNullableItemStack(data);
/*      */     
/* 3054 */     this.inventory = StreamReadWrite.readInventory(data);
/*      */     
/* 3056 */     this.clothName = StreamReadWrite.readNullableString(data);
/* 3057 */     this.clothTexture = StreamReadWrite.readNullableResourceLocation(data);
/*      */     
/* 3059 */     setGoalDestPoint(StreamReadWrite.readNullablePoint(data));
/* 3060 */     this.shouldLieDown = data.readBoolean();
/*      */     
/* 3062 */     this.dialogueTargetFirstName = StreamReadWrite.readNullableString(data);
/* 3063 */     this.dialogueTargetLastName = StreamReadWrite.readNullableString(data);
/* 3064 */     this.dialogueColour = data.readChar();
/* 3065 */     this.dialogueChat = data.readBoolean();
/* 3066 */     func_70606_j(data.readFloat());
/*      */     
/* 3068 */     int nbMerchantSells = data.readInt();
/*      */     
/* 3070 */     if (nbMerchantSells > -1) {
/* 3071 */       this.merchantSells.clear();
/*      */       
/* 3073 */       for (int i = 0; i < nbMerchantSells; i++) {
/*      */         try
/*      */         {
/* 3076 */           Goods g = StreamReadWrite.readNullableGoods(data);
/* 3077 */           this.merchantSells.put(g, Integer.valueOf(data.readInt()));
/*      */         } catch (MLN.MillenaireException e) {
/* 3079 */           MLN.printException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3084 */     int goalDestEntityID = data.readInt();
/*      */     
/* 3086 */     if (goalDestEntityID != -1) {
/* 3087 */       Entity ent = this.field_70170_p.func_73045_a(goalDestEntityID);
/*      */       
/* 3089 */       if (ent != null) {
/* 3090 */         setGoalDestEntity(ent);
/*      */       }
/*      */     }
/*      */     
/* 3094 */     this.client_lastupdated = this.field_70170_p.func_72820_D();
/*      */   }
/*      */   
/*      */   public void registerInGlobalList()
/*      */   {
/* 3099 */     if (this.registeredInGlobalList) {
/* 3100 */       if (MillCommonUtilities.chanceOn(20)) {
/* 3101 */         if (!this.mw.villagers.containsKey(Long.valueOf(this.villager_id))) {
/* 3102 */           this.mw.villagers.put(Long.valueOf(this.villager_id), this);
/* 3103 */         } else if ((this.mw.villagers.get(Long.valueOf(this.villager_id)) != null) && (this.mw.villagers.get(Long.valueOf(this.villager_id)) != this) && (!((MillVillager)this.mw.villagers.get(Long.valueOf(this.villager_id))).field_70128_L))
/*      */         {
/*      */ 
/*      */ 
/* 3107 */           despawnVillagerSilent();
/*      */         }
/*      */       }
/* 3110 */       return;
/*      */     }
/*      */     
/* 3113 */     if (this.mw == null) {
/* 3114 */       MLN.error(this, "Could not register as mw is null");
/* 3115 */       return;
/*      */     }
/*      */     
/* 3118 */     if (this.mw.villagers.containsKey(Long.valueOf(this.villager_id))) {
/* 3119 */       ((MillVillager)this.mw.villagers.get(Long.valueOf(this.villager_id))).despawnVillagerSilent();
/*      */     }
/*      */     
/* 3122 */     this.mw.villagers.put(Long.valueOf(this.villager_id), this);
/*      */     
/* 3124 */     this.registeredInGlobalList = true;
/*      */   }
/*      */   
/*      */   public void registerNewPath(AS_PathEntity path) throws Exception
/*      */   {
/* 3129 */     if (path == null) {
/* 3130 */       boolean handled = false;
/* 3131 */       if (this.goalKey != null) {
/* 3132 */         Goal goal = (Goal)Goal.goals.get(this.goalKey);
/* 3133 */         handled = goal.unreachableDestination(this);
/*      */       }
/* 3135 */       if (!handled) {
/* 3136 */         clearGoal();
/*      */       }
/*      */     }
/*      */     else {
/* 3140 */       func_70778_a(path);
/* 3141 */       this.pathEntity = path;
/* 3142 */       this.field_70702_br = 0.0F;
/*      */     }
/*      */     
/* 3145 */     this.prevPathPoint = getPathPointPos();
/*      */     
/* 3147 */     this.pathingWorker = null;
/*      */   }
/*      */   
/*      */   public void registerNewPath(List<PathPoint> result)
/*      */     throws Exception
/*      */   {
/* 3153 */     AS_PathEntity path = null;
/*      */     
/* 3155 */     if (result != null) {
/* 3156 */       PathPoint[] pointsCopy = new PathPoint[result.size()];
/*      */       
/* 3158 */       int i = 0;
/* 3159 */       for (PathPoint p : result) {
/* 3160 */         if (p == null) {
/* 3161 */           pointsCopy[i] = null;
/*      */         } else {
/* 3163 */           PathPoint p2 = new PathPoint(p.field_75839_a, p.field_75837_b, p.field_75838_c);
/* 3164 */           pointsCopy[i] = p2;
/*      */         }
/* 3166 */         i++;
/*      */       }
/* 3168 */       path = new AS_PathEntity(pointsCopy);
/*      */     }
/*      */     
/* 3171 */     registerNewPath(path);
/*      */   }
/*      */   
/*      */   public void registerNewPathException(Exception e) {
/* 3175 */     if (((e instanceof AStarPathing.PathingException)) && (((AStarPathing.PathingException)e).errorCode == AStarPathing.PathingException.UNREACHABLE_START)) {
/* 3176 */       if ((MLN.LogPathing >= 1) && (this.extraLog)) {
/* 3177 */         MLN.major(this, "Unreachable start. Jumping back home.");
/*      */       }
/* 3179 */       func_70107_b(getHouse().getResManager().getSleepingPos().x + 0.5D, getHouse().getResManager().getSleepingPos().y + 1.0D, getHouse().getResManager().getSleepingPos().z + 0.5D);
/*      */     }
/* 3181 */     this.pathingWorker = null;
/*      */   }
/*      */   
/*      */   public void registerNewPathInterrupt(AStarPathing.PathingWorker worker) {
/* 3185 */     if (this.pathingWorker == worker) {
/* 3186 */       this.pathingWorker = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public HashMap<InvItem, Integer> requiresGoods() {
/* 3191 */     if ((func_70631_g_()) && (this.size < 20)) {
/* 3192 */       return this.vtype.requiredFoodAndGoods;
/*      */     }
/* 3194 */     if ((hasChildren()) && (getHouse().villagers.size() < 4)) {
/* 3195 */       return this.vtype.requiredFoodAndGoods;
/*      */     }
/*      */     
/* 3198 */     return this.vtype.requiredGoods;
/*      */   }
/*      */   
/*      */   private void sendVillagerPacket() {
/* 3202 */     DataOutput data = ServerSender.getNewByteBufOutputStream();
/*      */     try
/*      */     {
/* 3205 */       data.write(3);
/* 3206 */       writeVillagerStreamData(data, false);
/*      */     } catch (IOException e) {
/* 3208 */       MLN.printException(this + ": Error in sendVillagerPacket", e);
/*      */     }
/*      */     
/* 3211 */     ServerSender.sendPacketToPlayersInRange(data, getPos(), 30);
/*      */   }
/*      */   
/*      */   public boolean setBlock(Point p, Block block) {
/* 3215 */     return MillCommonUtilities.setBlock(this.field_70170_p, p, block, true, true);
/*      */   }
/*      */   
/*      */   public boolean setBlockAndMetadata(Point p, Block block, int metadata) {
/* 3219 */     return MillCommonUtilities.setBlockAndMetadata(this.field_70170_p, p, block, metadata, true, true);
/*      */   }
/*      */   
/*      */   public boolean setBlockMetadata(Point p, int metadata) {
/* 3223 */     return MillCommonUtilities.setBlockMetadata(this.field_70170_p, p, metadata);
/*      */   }
/*      */   
/*      */   public void func_70106_y()
/*      */   {
/* 3228 */     if (func_110143_aJ() <= 0.0F) {
/* 3229 */       killVillager();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setEntityToAttack(Entity ent) {
/* 3234 */     this.field_70789_a = ent;
/*      */   }
/*      */   
/*      */   private void setFacingDirection()
/*      */   {
/* 3239 */     if (this.field_70789_a != null) {
/* 3240 */       faceEntityMill(this.field_70789_a, 30.0F, 30.0F);
/* 3241 */       return;
/*      */     }
/*      */     
/* 3244 */     if ((this.goalKey != null) && ((getGoalDestPoint() != null) || (getGoalDestEntity() != null))) {
/* 3245 */       Goal goal = (Goal)Goal.goals.get(this.goalKey);
/*      */       
/* 3247 */       if (goal.lookAtGoal()) {
/* 3248 */         if ((getGoalDestEntity() != null) && (getPos().distanceTo(getGoalDestEntity()) < goal.range(this))) {
/* 3249 */           faceEntityMill(getGoalDestEntity(), 10.0F, 10.0F);
/* 3250 */         } else if ((getGoalDestPoint() != null) && (getPos().distanceTo(getGoalDestPoint()) < goal.range(this))) {
/* 3251 */           facePoint(getGoalDestPoint(), 10.0F, 10.0F);
/*      */         }
/*      */       }
/*      */       
/* 3255 */       if (goal.lookAtPlayer()) {
/* 3256 */         EntityPlayer player = this.field_70170_p.func_72890_a(this, 10.0D);
/* 3257 */         if (player != null) {
/* 3258 */           faceEntityMill(player, 10.0F, 10.0F);
/* 3259 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setGoalBuildingDestPoint(Point newDest) {
/* 3266 */     if (this.goalInformation == null) {
/* 3267 */       this.goalInformation = new Goal.GoalInformation(null, null, null);
/*      */     }
/*      */     
/* 3270 */     this.goalInformation.setDestBuildingPos(newDest);
/*      */   }
/*      */   
/*      */   public void setGoalDestEntity(Entity ent) {
/* 3274 */     if (this.goalInformation == null) {
/* 3275 */       this.goalInformation = new Goal.GoalInformation(null, null, null);
/*      */     }
/*      */     
/* 3278 */     this.goalInformation.setTargetEnt(ent);
/* 3279 */     if (ent != null) {
/* 3280 */       setPathDestPoint(new Point(ent));
/*      */     }
/*      */     
/* 3283 */     if ((ent instanceof MillVillager))
/*      */     {
/* 3285 */       MillVillager v = (MillVillager)ent;
/*      */       
/* 3287 */       this.dialogueTargetFirstName = v.firstName;
/* 3288 */       this.dialogueTargetLastName = v.familyName;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setGoalDestPoint(Point newDest)
/*      */   {
/* 3295 */     if (this.goalInformation == null) {
/* 3296 */       this.goalInformation = new Goal.GoalInformation(null, null, null);
/*      */     }
/*      */     
/* 3299 */     this.goalInformation.setDest(newDest);
/* 3300 */     setPathDestPoint(newDest);
/*      */   }
/*      */   
/*      */   public void setGoalInformation(Goal.GoalInformation info) {
/* 3304 */     this.goalInformation = info;
/* 3305 */     if (info != null) {
/* 3306 */       if (info.getTargetEnt() != null) {
/* 3307 */         setPathDestPoint(new Point(info.getTargetEnt()));
/* 3308 */       } else if (info.getDest() != null) {
/* 3309 */         setPathDestPoint(info.getDest());
/*      */       } else {
/* 3311 */         setPathDestPoint(null);
/*      */       }
/*      */     } else {
/* 3314 */       setPathDestPoint(null);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setHousePoint(Point p) {
/* 3319 */     this.housePoint = p;
/* 3320 */     this.house = null;
/*      */   }
/*      */   
/*      */   public void setInv(Item item, int nb) {
/* 3324 */     setInv(item, 0, nb);
/*      */   }
/*      */   
/*      */   public void setInv(Item item, int meta, int nb) {
/*      */     try {
/* 3329 */       this.inventory.put(new InvItem(item, meta), Integer.valueOf(nb));
/*      */     } catch (MLN.MillenaireException e) {
/* 3331 */       MLN.printException(e);
/*      */     }
/* 3333 */     if (getTownHall() != null) {
/* 3334 */       getTownHall().updateVillagerRecord(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNextGoal() throws Exception
/*      */   {
/* 3340 */     Goal nextGoal = null;
/* 3341 */     clearGoal();
/*      */     
/* 3343 */     for (Goal goal : getGoals()) {
/* 3344 */       if (goal.isPossible(this)) {
/* 3345 */         if ((MLN.LogGeneralAI >= 2) && (this.extraLog)) {
/* 3346 */           MLN.minor(this, "Priority for goal " + goal.gameName(this) + ": " + goal.priority(this));
/*      */         }
/* 3348 */         if ((nextGoal == null) || (nextGoal.priority(this) < goal.priority(this))) {
/* 3349 */           nextGoal = goal;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3354 */     if ((MLN.LogGeneralAI >= 2) && (this.extraLog)) {
/* 3355 */       MLN.minor(this, "Selected this: " + nextGoal);
/*      */     }
/*      */     
/* 3358 */     if ((MLN.LogBuildingPlan >= 1) && (nextGoal != null) && (nextGoal.key.equals(Goal.getResourcesForBuild.key))) {
/* 3359 */       MLN.major(this, getName() + " is new builder, for: " + this.townHall.getCurrentBuildingPlan() + ". Blocks loaded: " + this.townHall.getBblocks().length);
/*      */     }
/*      */     
/* 3362 */     if (nextGoal != null) {
/* 3363 */       speakSentence(nextGoal.key + ".chosen");
/* 3364 */       this.goalKey = nextGoal.key;
/* 3365 */       this.heldItem = null;
/* 3366 */       this.heldItemCount = Integer.MAX_VALUE;
/* 3367 */       nextGoal.onAccept(this);
/* 3368 */       this.goalStarted = System.currentTimeMillis();
/* 3369 */       this.lastGoalTime.put(nextGoal, Long.valueOf(this.field_70170_p.func_72820_D()));
/*      */     }
/*      */     else {
/* 3372 */       this.goalKey = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setPathDestPoint(Point newDest)
/*      */   {
/* 3378 */     if ((newDest == null) || (!newDest.equals(this.pathDestPoint))) {
/* 3379 */       func_70778_a(null);
/* 3380 */       this.pathEntity = null;
/*      */     }
/*      */     
/* 3383 */     this.pathDestPoint = newDest;
/*      */   }
/*      */   
/*      */   public void setTexture(ResourceLocation tx)
/*      */   {
/* 3388 */     this.texture = tx;
/*      */   }
/*      */   
/*      */   public void setTownHallPoint(Point p) {
/* 3392 */     this.townHallPoint = p;
/* 3393 */     this.townHall = null;
/*      */   }
/*      */   
/*      */   public void speakSentence(String key) {
/* 3397 */     speakSentence(key, 600, 3, 1);
/*      */   }
/*      */   
/*      */   public void speakSentence(String key, int chanceOn) {
/* 3401 */     speakSentence(key, 600, 3, chanceOn);
/*      */   }
/*      */   
/*      */   public void speakSentence(String key, int delay, int distance, int chanceOn)
/*      */   {
/* 3406 */     if (delay > this.field_70170_p.func_72820_D() - this.speech_started) {
/* 3407 */       return;
/*      */     }
/*      */     
/* 3410 */     if (!MillCommonUtilities.chanceOn(chanceOn)) {
/* 3411 */       return;
/*      */     }
/*      */     
/* 3414 */     if ((getTownHall() == null) || (getTownHall().closestPlayer == null) || (getPos().distanceTo(getTownHall().closestPlayer) > distance)) {
/* 3415 */       return;
/*      */     }
/*      */     
/* 3418 */     key = key.toLowerCase();
/*      */     
/* 3420 */     this.speech_key = null;
/*      */     
/* 3422 */     if (getCulture().hasSentences(getNameKey() + "." + key)) {
/* 3423 */       this.speech_key = (getNameKey() + "." + key);
/* 3424 */     } else if (getCulture().hasSentences(getGenderString() + "." + key)) {
/* 3425 */       this.speech_key = (getGenderString() + "." + key);
/* 3426 */     } else if (getCulture().hasSentences("villager." + key)) {
/* 3427 */       this.speech_key = ("villager." + key);
/*      */     }
/*      */     
/* 3430 */     if (this.speech_key != null) {
/* 3431 */       this.speech_variant = MillCommonUtilities.randomInt(getCulture().getSentences(this.speech_key).size());
/* 3432 */       this.speech_started = this.field_70170_p.func_72820_D();
/*      */       
/* 3434 */       sendVillagerPacket();
/*      */       
/* 3436 */       ServerSender.sendVillageSentenceInRange(this.field_70170_p, getPos(), 30, this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void specificUpdate() throws Exception
/*      */   {
/* 3442 */     if (isLocalMerchant()) {
/* 3443 */       localMerchantUpdate();
/*      */     }
/* 3445 */     if (isForeignMerchant()) {
/* 3446 */       foreignMerchantUpdate();
/*      */     }
/*      */   }
/*      */   
/*      */   public int takeFromBuilding(Building building, InvItem ii, int nb) {
/* 3451 */     return takeFromBuilding(building, ii.getItem(), ii.meta, nb);
/*      */   }
/*      */   
/*      */   public int takeFromBuilding(Building building, Item item, int nb) {
/* 3455 */     return takeFromBuilding(building, item, 0, nb);
/*      */   }
/*      */   
/*      */   public int takeFromBuilding(Building building, Item item, int meta, int nb) {
/* 3459 */     if ((item == Item.func_150898_a(Blocks.field_150364_r)) && (meta == -1)) {
/* 3460 */       int total = 0;
/* 3461 */       int nb2 = building.takeGoods(item, 0, nb);
/* 3462 */       addToInv(item, 0, nb2);
/* 3463 */       total += nb2;
/* 3464 */       nb2 = building.takeGoods(item, 0, nb - total);
/* 3465 */       addToInv(item, 0, nb2);
/* 3466 */       total += nb2;
/* 3467 */       nb2 = building.takeGoods(item, 0, nb - total);
/* 3468 */       addToInv(item, 0, nb2);
/* 3469 */       total += nb2;
/* 3470 */       return total;
/*      */     }
/* 3472 */     nb = building.takeGoods(item, meta, nb);
/* 3473 */     addToInv(item, meta, nb);
/* 3474 */     return nb;
/*      */   }
/*      */   
/*      */   public int takeFromInv(Block block, int nb) {
/* 3478 */     return takeFromInv(Item.func_150898_a(block), 0, nb);
/*      */   }
/*      */   
/*      */   public int takeFromInv(Block block, int meta, int nb) {
/* 3482 */     return takeFromInv(Item.func_150898_a(block), meta, nb);
/*      */   }
/*      */   
/*      */   public int takeFromInv(InvItem item, int nb) {
/* 3486 */     return takeFromInv(item.getItem(), item.meta, nb);
/*      */   }
/*      */   
/*      */   public int takeFromInv(Item item, int nb) {
/* 3490 */     return takeFromInv(item, 0, nb);
/*      */   }
/*      */   
/*      */   public int takeFromInv(Item item, int meta, int nb)
/*      */   {
/* 3495 */     if ((item == Item.func_150898_a(Blocks.field_150364_r)) && (meta == -1)) {
/* 3496 */       int total = 0;
/*      */       try
/*      */       {
/* 3499 */         for (int i = 0; i < 16; i++) {
/* 3500 */           InvItem key = new InvItem(item, i);
/* 3501 */           if (this.inventory.containsKey(key)) {
/* 3502 */             int nb2 = Math.min(nb, ((Integer)this.inventory.get(key)).intValue());
/* 3503 */             this.inventory.put(key, Integer.valueOf(((Integer)this.inventory.get(key)).intValue() - nb2));
/* 3504 */             total += nb2;
/*      */           }
/*      */         }
/* 3507 */         if (getTownHall() != null) {
/* 3508 */           getTownHall().updateVillagerRecord(this);
/*      */         }
/*      */       } catch (MLN.MillenaireException e) {
/* 3511 */         MLN.printException(e);
/*      */       }
/*      */       
/* 3514 */       return total;
/*      */     }
/*      */     try
/*      */     {
/* 3518 */       InvItem key = new InvItem(item, meta);
/* 3519 */       if (this.inventory.containsKey(key)) {
/* 3520 */         nb = Math.min(nb, ((Integer)this.inventory.get(key)).intValue());
/* 3521 */         this.inventory.put(key, Integer.valueOf(((Integer)this.inventory.get(key)).intValue() - nb));
/* 3522 */         if (getTownHall() != null) {
/* 3523 */           getTownHall().updateVillagerRecord(this);
/*      */         }
/*      */         
/* 3526 */         updateClothTexturePath();
/*      */         
/* 3528 */         return nb;
/*      */       }
/* 3530 */       return 0;
/*      */     }
/*      */     catch (MLN.MillenaireException e) {
/* 3533 */       MLN.printException(e); }
/* 3534 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void targetDefender()
/*      */   {
/* 3543 */     int bestDist = Integer.MAX_VALUE;
/* 3544 */     Entity target = null;
/*      */     
/* 3546 */     for (MillVillager v : getTownHall().villagers)
/*      */     {
/* 3548 */       if ((v.helpsInAttacks()) && (!v.isRaider))
/*      */       {
/* 3550 */         if (getPos().distanceToSquared(v) < bestDist) {
/* 3551 */           target = v;
/* 3552 */           bestDist = (int)getPos().distanceToSquared(v);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3557 */     if ((target != null) && (getPos().distanceToSquared(target) <= 25.0D)) {
/* 3558 */       this.field_70789_a = target;
/*      */     }
/*      */   }
/*      */   
/*      */   private void targetRaider()
/*      */   {
/* 3564 */     int bestDist = Integer.MAX_VALUE;
/* 3565 */     Entity target = null;
/*      */     
/* 3567 */     for (MillVillager v : getTownHall().villagers)
/*      */     {
/* 3569 */       if (v.isRaider)
/*      */       {
/* 3571 */         if (getPos().distanceToSquared(v) < bestDist) {
/* 3572 */           target = v;
/* 3573 */           bestDist = (int)getPos().distanceToSquared(v);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3578 */     if ((target != null) && (getPos().distanceToSquared(target) <= 25.0D)) {
/* 3579 */       this.field_70789_a = target;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void teenagerNightAction()
/*      */   {
/* 3590 */     for (Point p : getTownHall().getKnownVillages())
/*      */     {
/* 3592 */       if (getTownHall().getRelationWithVillage(p) > 90) {
/* 3593 */         Building distantVillage = this.mw.getBuilding(p);
/*      */         
/* 3595 */         if ((distantVillage != null) && (distantVillage.culture == getCulture()) && (distantVillage != getTownHall())) {
/* 3596 */           boolean canMoveIn = false;
/*      */           
/* 3598 */           if (MLN.LogChildren >= 1) {
/* 3599 */             MLN.major(this, "Attempting to move to village: " + distantVillage.getVillageQualifiedName());
/*      */           }
/*      */           
/* 3602 */           Building distantInn = null;
/* 3603 */           for (Building distantBuilding : distantVillage.getBuildings()) {
/* 3604 */             if ((!canMoveIn) && (distantBuilding != null) && (distantBuilding.isHouse())) {
/* 3605 */               if (distantBuilding.canChildMoveIn(this.gender, this.familyName)) {
/* 3606 */                 canMoveIn = true;
/*      */               }
/* 3608 */             } else if ((distantInn == null) && (distantBuilding.isInn) && 
/* 3609 */               (distantBuilding.vrecords.size() < 2)) {
/* 3610 */               distantInn = distantBuilding;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 3615 */           if ((canMoveIn) && (distantInn != null))
/*      */           {
/* 3617 */             if (MLN.LogChildren >= 1) {
/* 3618 */               MLN.major(this, "Moving to village: " + distantVillage.getVillageQualifiedName());
/*      */             }
/*      */             
/* 3621 */             getHouse().transferVillager(getHouse().getVillagerRecordById(this.villager_id), distantInn, false);
/* 3622 */             distantInn.visitorsList.add("panels.childarrived;" + getName() + ";" + getTownHall().getVillageQualifiedName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean teleportTo(double d, double d1, double d2)
/*      */   {
/* 3631 */     double d3 = this.field_70165_t;
/* 3632 */     double d4 = this.field_70163_u;
/* 3633 */     double d5 = this.field_70161_v;
/* 3634 */     this.field_70165_t = d;
/* 3635 */     this.field_70163_u = d1;
/* 3636 */     this.field_70161_v = d2;
/* 3637 */     boolean flag = false;
/* 3638 */     int i = MathHelper.func_76128_c(this.field_70165_t);
/* 3639 */     int j = MathHelper.func_76128_c(this.field_70163_u);
/* 3640 */     int k = MathHelper.func_76128_c(this.field_70161_v);
/* 3641 */     if (this.field_70170_p.func_72899_e(i, j, k))
/*      */     {
/* 3643 */       for (boolean flag1 = false; (!flag1) && (j > 0);) {
/* 3644 */         Block block = this.field_70170_p.func_147439_a(i, j - 1, k);
/* 3645 */         if ((block == Blocks.field_150350_a) || (!block.func_149688_o().func_76230_c())) {
/* 3646 */           this.field_70163_u -= 1.0D;
/* 3647 */           j--;
/*      */         } else {
/* 3649 */           flag1 = true;
/*      */         }
/*      */       }
/*      */       
/* 3653 */       if (flag1) {
/* 3654 */         func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
/* 3655 */         if ((this.field_70170_p.func_72945_a(this, this.field_70121_D).size() == 0) && (!this.field_70170_p.func_72953_d(this.field_70121_D))) {
/* 3656 */           flag = true;
/*      */         }
/*      */       }
/*      */     }
/* 3660 */     if (!flag) {
/* 3661 */       func_70107_b(d3, d4, d5);
/* 3662 */       return false;
/*      */     }
/*      */     
/* 3665 */     return true;
/*      */   }
/*      */   
/*      */   public boolean teleportToEntity(Entity entity) {
/* 3669 */     Vec3 vec3d = Vec3.func_72443_a(this.field_70165_t - entity.field_70165_t, this.field_70121_D.field_72338_b + this.field_70131_O / 2.0F - entity.field_70163_u + entity.func_70047_e(), this.field_70161_v - entity.field_70161_v);
/* 3670 */     vec3d = vec3d.func_72432_b();
/* 3671 */     double d = 16.0D;
/* 3672 */     double d1 = this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * 8.0D - vec3d.field_72450_a * 16.0D;
/* 3673 */     double d2 = this.field_70163_u + (this.field_70146_Z.nextInt(16) - 8) - vec3d.field_72448_b * 16.0D;
/* 3674 */     double d3 = this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * 8.0D - vec3d.field_72449_c * 16.0D;
/* 3675 */     return teleportTo(d1, d2, d3);
/*      */   }
/*      */   
/*      */   private void toggleDoor(int i, int j, int k)
/*      */   {
/* 3680 */     int l = this.field_70170_p.func_72805_g(i, j, k);
/*      */     
/* 3682 */     MillCommonUtilities.setBlockMetadata(this.field_70170_p, i, j, k, l ^ 0x4, true);
/* 3683 */     this.field_70170_p.func_147458_c(i, j - 1, k, i, j, k);
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 3689 */     return getClass().getSimpleName() + "@" + ": " + getName() + "/" + this.villager_id + "/" + this.field_70170_p;
/*      */   }
/*      */   
/*      */ 
/*      */   private void triggerMobAttacks()
/*      */   {
/* 3695 */     List<Entity> entities = MillCommonUtilities.getEntitiesWithinAABB(this.field_70170_p, EntityMob.class, getPos(), 16, 5);
/*      */     
/* 3697 */     for (Entity ent : entities)
/*      */     {
/* 3699 */       EntityMob mob = (EntityMob)ent;
/*      */       
/* 3701 */       if ((mob.func_70777_m() == null) && 
/* 3702 */         (mob.func_70685_l(this))) {
/* 3703 */         mob.func_70784_b(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateClothTexturePath()
/*      */   {
/* 3712 */     if (this.vtype == null) {
/* 3713 */       return;
/*      */     }
/*      */     
/* 3716 */     String bestClothName = null;
/* 3717 */     int clothLevel = -1;
/*      */     
/* 3719 */     if (this.vtype.getRandomClothTexture("free") != null) {
/* 3720 */       bestClothName = "free";
/* 3721 */       clothLevel = 0;
/*      */     }
/*      */     
/* 3724 */     for (InvItem iv : this.inventory.keySet()) {
/* 3725 */       if ((iv.item == Mill.clothes) && (((Integer)this.inventory.get(iv)).intValue() > 0) && 
/* 3726 */         (Mill.clothes.getClothPriority(iv.meta) > clothLevel))
/*      */       {
/*      */ 
/* 3729 */         if (this.vtype.getRandomClothTexture(Mill.clothes.getClothName(iv.meta)) != null) {
/* 3730 */           bestClothName = Mill.clothes.getClothName(iv.meta);
/* 3731 */           clothLevel = Mill.clothes.getClothPriority(iv.meta);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3738 */     if (bestClothName != null) {
/* 3739 */       if ((!bestClothName.equals(this.clothName)) || (!this.vtype.isClothValid(this.clothName, this.clothTexture.func_110623_a()))) {
/* 3740 */         this.clothName = bestClothName;
/* 3741 */         this.clothTexture = new ResourceLocation("millenaire", this.vtype.getRandomClothTexture(bestClothName));
/*      */       }
/*      */     } else {
/* 3744 */       this.clothName = null;
/* 3745 */       this.clothTexture = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateDialogue()
/*      */   {
/* 3751 */     if (this.dialogueKey == null) {
/* 3752 */       return;
/*      */     }
/*      */     
/* 3755 */     Culture.CultureLanguage.Dialogue d = getCulture().getDialogue(this.dialogueKey);
/*      */     
/* 3757 */     if (d == null) {
/* 3758 */       this.dialogueKey = null;
/* 3759 */       return;
/*      */     }
/*      */     
/* 3762 */     long timePassed = this.field_70170_p.func_72820_D() - this.dialogueStart;
/*      */     
/* 3764 */     if (((Integer)d.timeDelays.get(d.timeDelays.size() - 1)).intValue() + 100 < timePassed) {
/* 3765 */       this.dialogueKey = null;
/* 3766 */       return;
/*      */     }
/*      */     
/* 3769 */     String toSpeakKey = null;
/*      */     
/* 3771 */     for (int i = 0; i < d.speechBy.size(); i++) {
/* 3772 */       if ((this.dialogueRole == ((Integer)d.speechBy.get(i)).intValue()) && (timePassed >= ((Integer)d.timeDelays.get(i)).intValue())) {
/* 3773 */         toSpeakKey = "chat_" + d.key + "_" + i;
/*      */       }
/*      */     }
/*      */     
/* 3777 */     if ((toSpeakKey != null) && ((this.speech_key == null) || (!this.speech_key.contains(toSpeakKey)))) {
/* 3778 */       speakSentence(toSpeakKey, 0, 10, 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateHired()
/*      */   {
/*      */     try {
/* 3785 */       if (((func_110143_aJ() < func_110138_aP() ? 1 : 0) & (MillCommonUtilities.randomInt(1600) == 0 ? 1 : 0)) != 0) {
/* 3786 */         func_70606_j(func_110143_aJ() + 1.0F);
/*      */       }
/*      */       
/* 3789 */       EntityPlayer entityplayer = this.field_70170_p.func_72924_a(this.hiredBy);
/*      */       
/* 3791 */       if (this.field_70170_p.func_72820_D() > this.hiredUntil)
/*      */       {
/* 3793 */         if (entityplayer != null) {
/* 3794 */           ServerSender.sendTranslatedSentence(entityplayer, 'f', "hire.hireover", new String[] { getName() });
/*      */         }
/*      */         
/* 3797 */         this.hiredBy = null;
/* 3798 */         this.hiredUntil = 0L;
/*      */         
/* 3800 */         VillagerRecord vr = getTownHall().getVillagerRecordById(this.villager_id);
/* 3801 */         if (vr != null) {
/* 3802 */           vr.awayhired = false;
/*      */         }
/*      */         
/* 3805 */         return;
/*      */       }
/*      */       
/* 3808 */       if (this.field_70789_a != null) {
/* 3809 */         if ((getPos().distanceTo(this.field_70789_a) > 80.0D) || (this.field_70170_p.field_73013_u == EnumDifficulty.PEACEFUL)) {
/* 3810 */           this.field_70789_a = null;
/*      */         }
/*      */       }
/* 3813 */       else if ((isHostile()) && (this.field_70170_p.field_73013_u != EnumDifficulty.PEACEFUL) && (getTownHall().closestPlayer != null) && (getPos().distanceTo(getTownHall().closestPlayer) <= 80.0D)) {
/* 3814 */         this.field_70789_a = this.field_70170_p.func_72977_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, 100.0D);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3819 */       if (this.field_70789_a == null) {
/* 3820 */         List<?> list = this.field_70170_p.func_72872_a(EntityCreature.class, AxisAlignedBB.func_72330_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70165_t + 1.0D, this.field_70163_u + 1.0D, this.field_70161_v + 1.0D).func_72314_b(16.0D, 8.0D, 16.0D));
/*      */         
/*      */ 
/* 3823 */         for (Object o : list) {
/* 3824 */           if (this.field_70789_a == null) {
/* 3825 */             EntityCreature creature = (EntityCreature)o;
/*      */             
/* 3827 */             if ((creature.func_70777_m() == entityplayer) && (!(creature instanceof EntityCreeper))) {
/* 3828 */               this.field_70789_a = creature;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 3834 */         if ((this.field_70789_a == null) && (this.aggressiveStance)) {
/* 3835 */           list = this.field_70170_p.func_72872_a(EntityMob.class, AxisAlignedBB.func_72330_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70165_t + 1.0D, this.field_70163_u + 1.0D, this.field_70161_v + 1.0D).func_72314_b(16.0D, 8.0D, 16.0D));
/*      */           
/* 3837 */           if (!list.isEmpty()) {
/* 3838 */             this.field_70789_a = ((Entity)list.get(this.field_70170_p.field_73012_v.nextInt(list.size())));
/*      */             
/* 3840 */             if ((this.field_70789_a instanceof EntityCreeper)) {
/* 3841 */               this.field_70789_a = null;
/*      */             }
/*      */           }
/* 3844 */           if (this.field_70789_a == null) {
/* 3845 */             list = this.field_70170_p.func_72872_a(MillVillager.class, AxisAlignedBB.func_72330_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70165_t + 1.0D, this.field_70163_u + 1.0D, this.field_70161_v + 1.0D).func_72314_b(16.0D, 8.0D, 16.0D));
/*      */             
/* 3847 */             for (Object o : list) {
/* 3848 */               if (this.field_70789_a == null) {
/* 3849 */                 MillVillager villager = (MillVillager)o;
/*      */                 
/* 3851 */                 if (villager.isHostile()) {
/* 3852 */                   this.field_70789_a = villager;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3862 */       Entity target = null;
/*      */       
/* 3864 */       if (this.field_70789_a != null) {
/* 3865 */         target = this.field_70789_a;
/* 3866 */         this.heldItem = getWeapon();
/*      */         
/* 3868 */         PathEntity pathentity = this.field_70170_p.func_72865_a(this, target, 16.0F, true, false, false, true);
/* 3869 */         if (pathentity != null) {
/* 3870 */           func_70778_a(pathentity);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 3875 */         this.heldItem = null;
/* 3876 */         Entity player = (Entity)this.mw.world.field_73010_i.get(0);
/* 3877 */         target = player;
/*      */         
/* 3879 */         int dist = (int)getPos().distanceTo(target);
/*      */         
/* 3881 */         if (dist > 16) {
/* 3882 */           teleportToEntity(player);
/* 3883 */         } else if (dist > 4) {
/* 3884 */           PathEntity pathentity = this.field_70170_p.func_72865_a(this, target, 16.0F, true, false, false, true);
/* 3885 */           if (pathentity != null) {
/* 3886 */             func_70778_a(pathentity);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 3891 */       this.prevPoint = getPos();
/*      */       
/* 3893 */       handleDoorsAndFenceGates();
/*      */     }
/*      */     catch (Exception e) {
/* 3896 */       MLN.printException("Error in hired onUpdate():", e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updatePathIfNeeded(Point dest) throws Exception
/*      */   {
/* 3902 */     if (dest == null) {
/* 3903 */       return;
/*      */     }
/*      */     
/* 3906 */     if ((this.pathEntity != null) && (this.pathEntity.func_75874_d() > 0) && (!MillCommonUtilities.chanceOn(50)) && (this.pathEntity.getCurrentTargetPathPoint() != null))
/*      */     {
/* 3908 */       if (MLN.DEV) {
/* 3909 */         getTownHall().monitor.nbPathing += 1;
/* 3910 */         getTownHall().monitor.nbReused += 1;
/*      */       }
/* 3912 */       func_70778_a(this.pathEntity);
/*      */ 
/*      */     }
/* 3915 */     else if (MLN.jpsPathing) {
/* 3916 */       if (!this.jpsPathPlanner.isBusy()) {
/* 3917 */         computeNewPath(dest);
/*      */       }
/*      */     }
/* 3920 */     else if (this.pathingWorker == null) {
/* 3921 */       computeNewPath(dest);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public float func_70663_b(float f, float f1, float f2)
/*      */   {
/* 3929 */     for (float f3 = f1 - f; f3 < -180.0F; f3 += 360.0F) {}
/*      */     
/* 3931 */     while (f3 >= 180.0F) { f3 -= 360.0F;
/*      */     }
/* 3933 */     if (f3 > f2) {
/* 3934 */       f3 = f2;
/*      */     }
/* 3936 */     if (f3 < -f2) {
/* 3937 */       f3 = -f2;
/*      */     }
/* 3939 */     return f + f3;
/*      */   }
/*      */   
/*      */   public void func_70014_b(NBTTagCompound nbttagcompound)
/*      */   {
/*      */     try
/*      */     {
/* 3946 */       if (this.vtype == null) {
/* 3947 */         MLN.error(this, "Not saving villager due to null vtype.");
/* 3948 */         return;
/*      */       }
/*      */       
/* 3951 */       super.func_70014_b(nbttagcompound);
/*      */       
/* 3953 */       nbttagcompound.func_74778_a("vtype", this.vtype.key);
/*      */       
/* 3955 */       nbttagcompound.func_74778_a("culture", getCulture().key);
/*      */       
/* 3957 */       nbttagcompound.func_74778_a("texture", this.texture.func_110623_a());
/* 3958 */       if (this.housePoint != null) {
/* 3959 */         this.housePoint.write(nbttagcompound, "housePos");
/*      */       }
/* 3961 */       if (this.townHallPoint != null) {
/* 3962 */         this.townHallPoint.write(nbttagcompound, "townHallPos");
/*      */       }
/* 3964 */       if (getGoalDestPoint() != null) {
/* 3965 */         getGoalDestPoint().write(nbttagcompound, "destPoint");
/*      */       }
/* 3967 */       if (getGoalBuildingDestPoint() != null) {
/* 3968 */         getGoalBuildingDestPoint().write(nbttagcompound, "destBuildingPoint");
/*      */       }
/* 3970 */       if (getPathDestPoint() != null) {
/* 3971 */         getPathDestPoint().write(nbttagcompound, "pathDestPoint");
/*      */       }
/* 3973 */       if (this.prevPoint != null) {
/* 3974 */         this.prevPoint.write(nbttagcompound, "prevPoint");
/*      */       }
/* 3976 */       if (this.doorToClose != null) {
/* 3977 */         this.doorToClose.write(nbttagcompound, "doorToClose");
/*      */       }
/*      */       
/* 3980 */       nbttagcompound.func_74768_a("action", this.action);
/* 3981 */       if (this.goalKey != null) {
/* 3982 */         nbttagcompound.func_74778_a("goal", this.goalKey);
/*      */       }
/* 3984 */       nbttagcompound.func_74778_a("firstName", this.firstName);
/* 3985 */       nbttagcompound.func_74778_a("familyName", this.familyName);
/* 3986 */       nbttagcompound.func_74776_a("scale", this.scale);
/* 3987 */       nbttagcompound.func_74768_a("gender", this.gender);
/* 3988 */       nbttagcompound.func_74772_a("lastSpeechLong", this.speech_started);
/* 3989 */       nbttagcompound.func_74772_a("villager_lid", this.villager_id);
/*      */       
/* 3991 */       if (this.dialogueKey != null) {
/* 3992 */         nbttagcompound.func_74778_a("dialogueKey", this.dialogueKey);
/* 3993 */         nbttagcompound.func_74772_a("dialogueStart", this.dialogueStart);
/* 3994 */         nbttagcompound.func_74768_a("dialogueRole", this.dialogueRole);
/* 3995 */         nbttagcompound.func_74768_a("dialogueColour", this.dialogueColour);
/* 3996 */         nbttagcompound.func_74757_a("dialogueChat", this.dialogueChat);
/*      */       }
/*      */       
/*      */ 
/* 4000 */       NBTTagList nbttaglist = MillCommonUtilities.writeInventory(this.inventory);
/* 4001 */       nbttagcompound.func_74782_a("inventoryNew", nbttaglist);
/*      */       
/* 4003 */       nbttagcompound.func_74768_a("previousBlock", Block.func_149682_b(this.previousBlock));
/* 4004 */       nbttagcompound.func_74768_a("previousBlockMeta", this.previousBlockMeta);
/* 4005 */       nbttagcompound.func_74768_a("size", this.size);
/* 4006 */       nbttagcompound.func_74757_a("hasPrayedToday", this.hasPrayedToday);
/* 4007 */       nbttagcompound.func_74757_a("hasDrunkToday", this.hasDrunkToday);
/*      */       
/* 4009 */       if (this.hiredBy != null) {
/* 4010 */         nbttagcompound.func_74778_a("hiredBy", this.hiredBy);
/* 4011 */         nbttagcompound.func_74772_a("hiredUntil", this.hiredUntil);
/* 4012 */         nbttagcompound.func_74757_a("aggressiveStance", this.aggressiveStance);
/*      */       }
/*      */       
/* 4015 */       nbttagcompound.func_74757_a("isRaider", this.isRaider);
/*      */       
/* 4017 */       if (this.clothName != null) {
/* 4018 */         nbttagcompound.func_74778_a("clothName", this.clothName);
/* 4019 */         nbttagcompound.func_74778_a("clothTexture", this.clothTexture.func_110623_a());
/*      */       }
/*      */     } catch (Exception e) {
/* 4022 */       MLN.printException("Exception when attempting to save villager " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeSpawnData(ByteBuf ds)
/*      */   {
/* 4028 */     ByteBufOutputStream data = new ByteBufOutputStream(ds);
/*      */     try {
/* 4030 */       writeVillagerStreamData(data, true);
/*      */     } catch (IOException e) {
/* 4032 */       MLN.printException("Error in writeSpawnData for villager " + this, e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeVillagerStreamData(DataOutput data, boolean isSpawn) throws IOException
/*      */   {
/* 4038 */     if (this.vtype == null) {
/* 4039 */       MLN.error(this, "Cannot write stream data due to null vtype.");
/* 4040 */       return;
/*      */     }
/*      */     
/* 4043 */     data.writeLong(this.villager_id);
/* 4044 */     StreamReadWrite.writeNullableString(this.vtype.culture.key, data);
/* 4045 */     StreamReadWrite.writeNullableString(this.vtype.key, data);
/*      */     
/* 4047 */     StreamReadWrite.writeNullableResourceLocation(this.texture, data);
/*      */     
/* 4049 */     StreamReadWrite.writeNullableString(this.goalKey, data);
/* 4050 */     StreamReadWrite.writeNullablePoint(this.housePoint, data);
/* 4051 */     StreamReadWrite.writeNullablePoint(this.townHallPoint, data);
/* 4052 */     StreamReadWrite.writeNullableString(this.firstName, data);
/* 4053 */     StreamReadWrite.writeNullableString(this.familyName, data);
/* 4054 */     data.writeFloat(this.scale);
/* 4055 */     data.writeInt(this.gender);
/* 4056 */     data.writeInt(this.size);
/* 4057 */     StreamReadWrite.writeNullableString(this.hiredBy, data);
/* 4058 */     data.writeBoolean(this.aggressiveStance);
/* 4059 */     data.writeLong(this.hiredUntil);
/* 4060 */     data.writeBoolean(this.isUsingBow);
/* 4061 */     data.writeBoolean(this.isUsingHandToHand);
/* 4062 */     StreamReadWrite.writeNullableString(this.speech_key, data);
/* 4063 */     data.writeInt(this.speech_variant);
/* 4064 */     data.writeLong(this.speech_started);
/* 4065 */     StreamReadWrite.writeNullableItemStack(this.heldItem, data);
/* 4066 */     StreamReadWrite.writeInventory(this.inventory, data);
/* 4067 */     StreamReadWrite.writeNullableString(this.clothName, data);
/* 4068 */     StreamReadWrite.writeNullableResourceLocation(this.clothTexture, data);
/* 4069 */     StreamReadWrite.writeNullablePoint(getGoalDestPoint(), data);
/* 4070 */     data.writeBoolean(this.shouldLieDown);
/* 4071 */     StreamReadWrite.writeNullableString(this.dialogueTargetFirstName, data);
/* 4072 */     StreamReadWrite.writeNullableString(this.dialogueTargetLastName, data);
/* 4073 */     data.writeChar(this.dialogueColour);
/* 4074 */     data.writeBoolean(this.dialogueChat);
/* 4075 */     data.writeFloat(func_110143_aJ());
/*      */     
/* 4077 */     if (isSpawn) {
/* 4078 */       calculateMerchantGoods();
/*      */       
/* 4080 */       data.writeInt(this.merchantSells.size());
/*      */       
/* 4082 */       for (Goods g : this.merchantSells.keySet()) {
/* 4083 */         StreamReadWrite.writeNullableGoods(g, data);
/* 4084 */         data.writeInt(((Integer)this.merchantSells.get(g)).intValue());
/*      */       }
/*      */     } else {
/* 4087 */       data.writeInt(-1);
/*      */     }
/*      */     
/* 4090 */     if (getGoalDestEntity() != null) {
/* 4091 */       data.writeInt(getGoalDestEntity().func_145782_y());
/*      */     } else {
/* 4093 */       data.writeInt(-1);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Users\brl\Downloads\Millenaire6.0.0\Millenaire 6.0.0\Put in mods folder\millenaire-6.0.0.jar!\org\millenaire\common\MillVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */